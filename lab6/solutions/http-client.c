
/*
 * http-client.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <netdb.h>

#define BUF_SIZE 4096

static void die(const char *msg) {
    perror(msg);
    exit(1);
}

static void printUsage() {
    fprintf(stderr, "usage: http-client <host name> <port number> <file path>\n");
    fprintf(stderr, "   ex) http-client www.example.com 80 /index.html\n");
    exit(1);
}

int main(int argc, char **argv) {

    char *serverName;
    char *serverIP;
    char *serverPort;
    char *filePath;
    char *fname;

    int sock;
    struct sockaddr_in serverAddr;
    struct hostent *he;
    char buf[BUF_SIZE];

    if (argc != 4) {
	printUsage();
    }

    // parse args
    serverName = argv[1];
    serverPort = argv[2];
    filePath = argv[3];
    char *p = strrchr(filePath, '/');
    if (!p)
	printUsage();
    fname = p + 1;

    // get server ip from server name
    if ((he = gethostbyname(serverName)) == NULL) {
	die("gethoatbyname failed");
    }
    serverIP = inet_ntoa(*(struct in_addr *)he->h_addr);

    /* for debugging
       printf("1: %s\n", serverName);
       printf("2: %s\n", serverIP);
       printf("3: %s\n", serverPort);
       printf("4: %s\n", filePath);
       printf("5: %s\n", fname);
       */

    // create socket
    if ((sock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0) {
	die("socket failed");
    }

    // construct server address
    memset(&serverAddr, 0, sizeof(serverAddr));
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_addr.s_addr = inet_addr(serverIP);
    unsigned short port = atoi(serverPort);
    serverAddr.sin_port = htons(port);

    // connect
    if (connect(sock, (struct sockaddr *)&serverAddr, sizeof(serverAddr)) < 0) {
	die("connect failed");
    }

    // send HTTP request
    snprintf(buf, sizeof(buf), 
	    // note that C language concatenates adjacent string literals
	    "GET %s HTTP/1.0\r\n"
	    "Host: %s:%s\r\n"
	    "\r\n",
	    filePath, serverName, serverPort);
    if (send(sock, buf, strlen(buf), 0) != strlen(buf)) {
	die("send failed");
    }

    // wrap the socket with a FILE* so that we can read the socket using fgets()
    FILE *fd;
    if ((fd = fdopen(sock, "r")) == NULL) {
	die("fdopen failed");
    }

    // read the 1st line
    if (fgets(buf, sizeof(buf), fd) == NULL) {
	if (ferror(fd))
	    die("IO error");
	else {
	    fprintf(stderr, "server terminated connection without response");
	    exit(1);
	}
    } 
    if (strncmp("HTTP/1.0 ", buf, 9) != 0 && strncmp("HTTP/1.1 ", buf, 9) != 0) {
	fprintf(stderr, "unknown protocol response: %s\n", buf);
	exit(1);
    }
    if (strncmp("200", buf + 9, 3) != 0) {
	fprintf(stderr, "%s\n", buf);
	exit(1);
    }

    // If we're still here, it means we have a successful HTTP
    // response (i.e., response code 200).

    // Now, skip the header lines.
    for (;;) {
	if (fgets(buf, sizeof(buf), fd) == NULL) {
	    if (ferror(fd))
		die("IO error");
	    else {
		fprintf(stderr, "server terminated connection without sending file");
		exit(1);
	    }
	}
	if (strcmp("\r\n", buf) == 0) {
	    // this marks the end of header lines
	    // get out of the for loop.
	    break;
	}
    }

    // Now it's time to read the file.
    // We switch to fread()/fwrite() so that we can download a binary
    // file as well as an HTML file.
    // (Handling binary file is not required for the assignment.)

    FILE *outputFile = fopen(fname, "wb");
    if (outputFile == NULL)
	die("can't open output file");

    size_t n;
    while ((n = fread(buf, 1, sizeof(buf), fd)) > 0) {
	if (fwrite(buf, 1, n, outputFile) != n) 
	    die("fwrite failed");
    }
    // fread() returns 0 on EOF or on error
    // so we need to check if there was an error.
    if (ferror(fd))
	die("fread failed");

    // All done.  Clean up.
    
    fclose(outputFile);

    // closing fd closes the underlying socket as well.
    fclose(fd);
  
    return 0;
}

