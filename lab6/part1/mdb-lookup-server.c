#include <stdio.h>      /* for printf() and fprintf() */
#include <sys/socket.h> /* for socket(), bind(), and connect() */
#include <arpa/inet.h>  /* for sockaddr_in and inet_ntoa() */
#include <stdlib.h>     /* for atoi() and exit() */
#include <string.h>     /* for memset() */
#include <unistd.h>     /* for close() */
#include <signal.h> 	/*for sigpip */ 
#include "mdb.h"
#include "mylist.h" 

#define MAXPENDING 5    /* Maximum outstanding connection requests */
#define KeyMax 5

void HandleTCPClient(int clntSocket, char *database_filename);   /* TCP client handling function */


static void die(const char *msg){
	perror(msg);	
	exit(1); 
}// end die 

int main(int argc, char *argv[])
{
    int servSock;                    /* Socket descriptor for server */
    int clntSock;                    /* Socket descriptor for client */
    struct sockaddr_in echoServAddr; /* Local address */
    struct sockaddr_in echoClntAddr; /* Client address */
    unsigned short echoServPort;     /* Server port */
    unsigned int clntLen;            /* Length of client address data structure */

    // ignore SIGPIPE so that we don’t terminate when we call
    // send() on a disconnected socket.
    if (signal(SIGPIPE, SIG_IGN) == SIG_ERR) 
        die("signal() failed");

    if (argc != 3)     /* Test for correct number of arguments */
    {
        fprintf(stderr, "Usage:  %s <database_file> <Server Port>\n", argv[0]);
        exit(1);
    }

    echoServPort = atoi(argv[2]);  /* Second arg:  local port; change from string to int */
    char *database_filename = argv[1]; 

    /* Create socket for incoming connections */
    if ((servSock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0)
        die("socket() failed");
      
    /* Construct local address structure */
    memset(&echoServAddr, 0, sizeof(echoServAddr));   /* Zero out structure */
    echoServAddr.sin_family = AF_INET;                 /* Internet address family */
    echoServAddr.sin_addr.s_addr = htonl(INADDR_ANY); /* Any incoming interface */
    echoServAddr.sin_port = htons(echoServPort);      /* Local port */

    /* Bind to the local address */
    if (bind(servSock, (struct sockaddr *) &echoServAddr, sizeof(echoServAddr)) < 0)
        die("bind() failed");

    /* Mark the socket so it will listen for incoming connections */
    if (listen(servSock, MAXPENDING) < 0)
        die("listen() failed");

    for (;;) /* Run forever */
    {
        /* Set the size of the in-out parameter */
        clntLen = sizeof(echoClntAddr);

        /* Wait for a client to connect */
        if ((clntSock = accept(servSock, (struct sockaddr *) &echoClntAddr, 
                               &clntLen)) < 0)
            die("accept() failed");

	/* clntSock is connected to client */
	fprintf(stderr, "\nconnection started from: %s\n", inet_ntoa(echoClntAddr.sin_addr)); 

	HandleTCPClient(clntSock, database_filename); 
	
	fprintf(stderr, "connection terminated from: %s\n", inet_ntoa(echoClntAddr.sin_addr)); 
	
    }//end for 
    /* NOT REACHED */
  
}//end main

void HandleTCPClient(int clntSocket, char *database_filename)
{

	FILE *fp = fopen(database_filename, "rb");		
	if (fp == NULL) 
	die(database_filename);

	/*
	* read all records into memory
	*/

	struct List list;
	initList(&list);

	struct MdbRec r;
	struct Node *node = NULL;

	while (fread(&r, sizeof(r), 1, fp) == 1) {

		// allocate memory for a new record and copy into it the one
		// that was just read from the database.
		struct MdbRec *rec = (struct MdbRec *)malloc(sizeof(r));
		if (!rec)
		    die("malloc failed");
		memcpy(rec, &r, sizeof(r));

		// add the record to the linked list.
		node = addAfter(&list, node, rec);
		if (node == NULL) 
		    die("addAfter() failed");
	}//end while loop 

	// see if fread() produced error
	if (ferror(fp)) 
	die(database_filename);

	/*
	* lookup loop
	*/

	FILE * input = fdopen(clntSocket, "r"); 
	if(input == NULL){
		die("fdopen failed"); 
	}//end if 

	char line[1000];
	char key[KeyMax + 1];
	char sendArray[1000];
	int length; 
	int rest; 

	while (fgets(line, sizeof(line), input) != NULL) {

		// must null-terminate the string manually after strncpy().
		strncpy(key, line, sizeof(key) - 1);
		key[sizeof(key) - 1] = '\0';

		// if newline is there, remove it.
		size_t last = strlen(key) - 1;
		if (key[last] == '\n')
		    key[last] = '\0';

		// traverse the list, printing out the matching records
		struct Node *node = list.head;
		int recNo = 1;
		while (node) {
		    struct MdbRec *rec = (struct MdbRec *)node->data;
			if (strstr(rec->name, key) || strstr(rec->msg, key)) {
				length= sprintf(sendArray, "%4d: {%s} said {%s}\n", recNo, rec->name, rec->msg); 
				if((rest= send(clntSocket, sendArray, length, 0)) != length){
					perror("send() failed");
					break; 
				}//end inner if 
			}//end outer if 
		    node = node->next;
		    recNo++;
		}//end inner while 

	}//end outer while 

	length= sprintf(sendArray, "\n"); 

	if ((rest = send(clntSocket, sendArray, length, 0)) !=length) {
		perror("send() failed"); 
	}//end if 

	// see if fgets() produced error
	if(ferror(input)) {
		perror("fgets failed"); 
	}//end if 

	/*
	* clean up and quit
	*/

	// free all the records
	traverseList(&list, &free);

	removeAllNodes(&list);

	fclose(fp);
	fclose(input); 
}//end HandleTCPClient

