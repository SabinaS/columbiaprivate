#include <stdio.h>      /* for printf() and fprintf() */
#include <sys/socket.h> /* for socket(), bind(), and connect() */
#include <sys/stat.h> 	/* for stat() */ 
#include <arpa/inet.h>  /* for sockaddr_in and inet_ntoa() */
#include <stdlib.h>     /* for atoi() and exit() */
#include <string.h>     /* for memset() */
#include <unistd.h>     /* for close() */
#include <signal.h> 	/* for signal() */ 
#include <time.h>	/* for time() */ 
#include <netdb.h> 	/* for gethostbyname() and struct hostent */ 

#define MAXPENDING 5    /* Maximum outstanding connection requests */
#define IO_BUFFER_SIZE 5000 

static int makeListeningSocket(short port); 
static int mdbLookupConnection( const char *mdbHost, short mdbPort); 
ssize_t sendErrorCheck(int socket, const char *buffer); 
static char *getStatusReason(int statusCode);  
static int printStatus(int clientSocket, int statusCode); 
static int handleMdbLookup( char *requestURI, FILE *mdbfp, int mdbSocket, int clientSocket); 
static int handleFileRequest(char *webRoot, char *requestURI, int clientSocket); 
//static struct httpcodes HTTP_StatusCodes[6]; 

static void die(const char *msg){
	perror(msg);	
	exit(1); 
}// end die 

/* HTTP status codes */
static struct {
	int status; 
	char *reason; 
} HTTP_StatusCodes[] = { {200, "OK"}, {400, "Bad Request"}, {403, "Forbidden"}, {404, "Not Found"}, {501, "Not Implemented"}, {0, NULL} }; 

static int makeListeningSocket(short port) 
{
	int serverSocket; 				/* Socket descriptor for server */
	struct sockaddr_in serverAdder; 		/* Local address */ 
	
	/*create socket to listen for incoming connections */ 
	if ((serverSocket = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0)
		die("Operation socket() failed"); 

	/*create struct for local address */
	memset(&serverAdder, 0, sizeof(serverAdder)); 	/*set all values of serverAdder to zero */
	serverAdder.sin_family = AF_INET; 		/* Internet address family */ 
	serverAdder.sin_addr.s_addr = htonl(INADDR_ANY); /* Any incoming interface */
   	serverAdder.sin_port = htons(port);      	/* Local port */ 
	
	/* Bind to the local address */
	if (bind(serverSocket, (struct sockaddr *) &serverAdder, sizeof(serverAdder)) < 0)
		die("bind() failed");

	/* Mark the socket so it will listen for incoming connections */
	if (listen(serverSocket, MAXPENDING) < 0)
		die("listen() failed");

	return serverSocket; 

}//end makeListeningSocket 

/* Create connection to mdb-lookup-server running on mdbHost listening on mdbPort */

static int mdbLookupConnection( const char *mdbHost, short mdbPort) 
{
	int socket1; 				/* socket descriptor */ 
	struct sockaddr_in serverAdder; 	/* local address */ 
	struct hostent *host; 			/* an entry in host database that maps between 
							host names and host numbers */
	/* get server ip */
	if((host = gethostbyname(mdbHost)) == NULL) 
	{
		die("gethostbyname() failed"); 
	}//end if 
	
	char *serverIP = inet_ntoa(*(struct in_addr *)host->h_addr); //assigning server IP 
	
	/*create socket */
	if((socket1 = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0)
	{
		die("socket() failed"); 
	}//end if
	
	/*construct server address */
	memset(&serverAdder, 0, sizeof(serverAdder)); 	/*set all values of serverAdder to zero */
	serverAdder.sin_family = AF_INET; 		/* Internet address family */ 
	serverAdder.sin_addr.s_addr = inet_addr(serverIP); /* Server IP address only */
   	serverAdder.sin_port = htons(mdbPort);      	/* port */ 

	/* connect socket to address of server*/
	if(connect(socket1, (struct sockaddr *)&serverAdder, sizeof(serverAdder)) < 0) 	
	{			
		die("connect() failed"); 
	}//end if 

	return socket1; 

}//end mdbLookupConnection 

/* Check for errors */
ssize_t sendErrorCheck(int socket, const char *buffer)
{
	size_t length = strlen(buffer); 
	ssize_t rest = send(socket, buffer, length, 0); 
	if(rest != length) 
	{
		perror("send failed()"); 
		return -1; 
	}//end if 
	else
		return rest; 

}//end sendErrorCheck 

 

static char *getStatusReason(int statusCode)
{
	int i=0; 
	while(HTTP_StatusCodes[i].status > 0){
		if(HTTP_StatusCodes[i].status == statusCode)
			return HTTP_StatusCodes[i].reason; //getting reason for status code 
		i++; 
	}//end while 
	return "Unknown Status Code"; 

}//end getStatusCodes 

static int printStatus(int clientSocket, int statusCode)
{
	char buffer[1000];
	char *reason= getStatusReason(statusCode); 

	sprintf(buffer, "HTTP/1.0 %d", statusCode);
	strcat(buffer, reason); 
	strcat(buffer, "/r/n"); 

	strcat(buffer, "/r/n"); //need to send blank line 

	if (statusCode != 200){
		char body[1000]; 
		sprintf(body, ">html><body>\n""<h1>%d %s</h1>\n" "</body></html>\n", statusCode, reason); //change to html so can easily display 
		strcat(buffer, body); 
		
	}//end if 

	sendErrorCheck(clientSocket, buffer); //send buffer to browser

	return 1; 

}//end printStatus 

static int handleMdbLookup( char *requestURI, FILE *mdbfp, int mdbSocket, int clientSocket)
{

	char *form= "<html><body>\n" "<h1>mdb-lookup</h1>" "<p>\n" "<form method=GET action=/mdb-lookup>\n" "lookup: <input type= text name=key>\n" "<input type=submit>\n" "</form>\n" "<p>\n"; 

	int strlength= strlen(form); 
	char *keyURI = "/mdb-lookup?key="; 
	int reqlen= strlen(requestURI); 

	if(strcmp(requestURI, "/mdb-lookup") ==0) 
	{
		if(send(clientSocket, "HTTP/1.0 200 OK\r\n\r\n", 19, 0) !=19){
			perror("send() failed");
			goto func_end; 
		}//end if

		if(send(clientSocket, form, strlength, 0) != strlength){
			perror("send() failed"); 
			goto func_end; 	
		}//end of 
		
		if(send(clientSocket, "\r\n\r\n", 4, 0) !=4){
			perror("send failed");
			goto func_end; 
		}//end if 
	
		printf( "%s %s 200 OK\n", requestURI, keyURI); 
	}//end 

	else if(strncmp(requestURI, keyURI, strlen(keyURI)) == 0){
		char *phrase = (char *)calloc(1, (reqlen - 15)* sizeof(	char)); 
		strcpy(phrase, requestURI + 16);
		int phraselen= strlen(phrase); 
		
		if(send(mdbSocket, phrase, phraselen, 0) != phraselen){
			perror("send failed");
			goto func_end; 
		}//end if 

		if(send(mdbSocket, "\n", 1, 0) !=1){
			perror("send failed");
			goto func_end; 
		}//end if 

		free(phrase); 

		if(send(clientSocket, "HTTP/1.0 200 OK\r\n\r\n", 19, 0) !=19){
			perror("send failed");
			goto func_end; 
		}//end if  

		if(send(clientSocket, form, strlength, 0) != strlength){
			perror("send() failed"); 
			goto func_end; 	
		}//end of 

		char line[1000]; 
		char *table_header = "<p><table border=\"1\">"; 
		if(sendErrorCheck(clientSocket, table_header) < 0)
			goto func_end; 
		int row = 1; 
		
		while(fgets(line, sizeof(line), mdbfp) != NULL){
			if(line[0] == '\n' || line[0] == '\r') { //done 
				break; 
			}//end if

			if(row == 1){
				if(send(clientSocket, "<tr><td style=\"background-color:yellow;\">", 41, 0) !=41){
					perror("send() failed"); 
					goto func_end; 
				}//end if 

				row= 0; 
			}//end if 
			else {
				if(send(clientSocket, "<tr><td style=\"background-color:white;\">", 40, 0) !=40){
					perror("send() failed"); 
					goto func_end; 
				}//end if 
	
				row=1; 
			}//end else 


			int linelength= strlen(line); 

			if(send(clientSocket, line, linelength, 0) != linelength){
				perror("send() failed"); 
				goto func_end; 	
			}//end if 	

			if(send(clientSocket, "</td></tr>\n", 11, 0) != 11){
				perror("send() failed"); 
				goto func_end; 	
			}//end if 

							
		}//end while loop 
		
		if(send(clientSocket, "</table></p>\n\r\n\r\n", 17, 0) != 17){
				perror("send() failed"); 
				goto func_end; 	
		}//end if 

		printf( "%s %s 200 OK\n", requestURI, keyURI); 

		close(clientSocket); 

	}//end if 
	

	else 
	{
		if(printStatus(clientSocket, 404) == 0){
			perror("send() failed"); 
				goto func_end;
		}//end if 
	}//end if 


	func_end:
		return clientSocket; 
		
}//end handleMdbLookup 

static int handleFileRequest(char *webRoot, char *requestURI, int clientSocket)
{
	int statusCode; 
	FILE *fp= NULL; 
	
	char *file = (char *)malloc(strlen(webRoot) + strlen(requestURI) + 100); 
	if(file == NULL)
		die("malloc fialed"); 
	strcpy(file, webRoot); 
	strcat(file, requestURI);	
	if(file[strlen(file)-1] == '/') {
		strcat(file, "index.html");
	}//end if 
	
	struct stat st; 
	if(stat(file, &st) == 0 && S_ISDIR(st.st_mode)) {
		statusCode= 403; //forbidden 
		printStatus(clientSocket, statusCode); 
		goto func_end; 
	}//end if 
	
	fp= fopen(file, "rb");
	if(fp == NULL)	{
		statusCode= 404; //not found 
		printStatus(clientSocket, statusCode);
		goto func_end; 
	}//end if 

	statusCode= 200; //OKfc
	printStatus(clientSocket, statusCode); 
	
	size_t x; 
	char buffer[IO_BUFFER_SIZE];	
	while((x = fread(buffer, 1, sizeof(buffer), fp)) >0) {
		if(send(clientSocket, buffer, x, 0) != x) {
			perror("sendErrorCheck failed");
			break; 
		}//end if 

	}//end while loop 

	if(ferror(fp))
		perror("fread failed"); 

	func_end:
		free(file); 
		if(fp)
			fclose(fp);
		return statusCode; 

}//end handleFileRequest 

int main(int argc, char *argv[])
{
	if(signal(SIGPIPE, SIG_IGN) == SIG_ERR)
		die("signal failed"); 
	
	if(argc !=5) {
		fprintf(stderr, "usage: <server_port> <web_root> <mdb-lookup_host> <mdb-lookup_port>\n"); 
		exit(1); 
	}//end if 
	
	
	unsigned short serverPort = atoi(argv[1]); 
	char *webRoot = argv[2]; 
	char *mdbHost = argv[3]; 
	short mdbPort = atoi(argv[4]); 

	int mdbSocket = mdbLookupConnection(mdbHost, mdbPort); 
	FILE *mdbfp= fdopen(mdbSocket, "r");
	if(mdbfp == NULL)
		die("fdopen failed");	

	int serverSocket = makeListeningSocket(serverPort); 

	char line[1000]; 
	char requestLine[1000]; 
	int statusCode; 
	struct sockaddr_in clientAddr; 
	
	for(;;){
		unsigned int clientlen= sizeof(clientAddr);
		int clientSocket = accept(serverSocket, (struct sockaddr *)&clientAddr, &clientlen);	

		if(clientSocket < 0 )
			die("fopen failed");	
	
		FILE *clientfp= fdopen(clientSocket, "r");
		if(clientfp == NULL)
			die("fopen failed");
	
		if(fgets(requestLine, sizeof(requestLine), clientfp) == NULL){
			statusCode= 400; //bad request 
			goto end_loop; 
		}//end if 

		char *tokenseparators = "\t \r\n"; //tabe, space, newline 
		char *method= strtok(requestLine, tokenseparators);	
		char *requestURI= strtok(NULL, tokenseparators); 
		char *httpversion = strtok(NULL, tokenseparators); 
		char *extras = strtok(NULL, tokenseparators); 
	
		if(!method || !requestURI || !httpversion || extras) {
			statusCode=501; //not implemented 
			printStatus(clientSocket, statusCode); 
			goto end_loop; 
		}//end if

		if(strcmp(method, "GET") !=0) {
			statusCode= 501; //not implemented
			printStatus(clientSocket, statusCode);	
			goto end_loop; 
		}//end if 

		if(strcmp(httpversion, "HTTP/1.0") !=0 && strcmp(httpversion, "HTTP/1.1") !=0) {
			statusCode= 501; //not implemented
			printStatus(clientSocket, statusCode);
			goto end_loop; 
		}//end if 

		if(!requestURI || *requestURI != '/') {
			statusCode= 400; //bad request 
			printStatus(clientSocket, statusCode);
			goto end_loop; 
		}//end if 

		while(1) {
			if(fgets(line, sizeof(line), clientfp) == NULL){
				statusCode= 400; //bad request
				goto end_loop; 
				
			}//end if 	
			
			if(strcmp("\r\n", line) ==0 || strcmp("\n", line) == 0){
				break; 
			}//end if 

		}//end while loop 

		char *mdbURI1 = "/mdb-lookup"; 
		
		if(strncmp(requestURI, mdbURI1, 11) == 0 ) {
			statusCode = handleMdbLookup(requestURI, mdbfp, mdbSocket, clientSocket); 
		}//end if 
		else{
			statusCode = handleFileRequest(webRoot, requestURI, clientSocket);
		}//end else 

		end_loop:
			fprintf(stderr, "%s \"%s %s %s\" %d %s\n", inet_ntoa(clientAddr.sin_addr), method, requestURI, httpversion, statusCode, getStatusReason(statusCode));
			fclose(clientfp); 
			
  	}//end for loop 

	return 0; 

}//end main


