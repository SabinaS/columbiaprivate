#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <unistd.h>

static void die(const char *s)
{
	perror(s);
	exit(1);
}
int main()
{ 

	while("\n"){
		
		int b; 
		pid_t thispid; 
		thispid = waitpid( (pid_t) -1, NULL, WNOHANG); //id of terminated programs 
		printf("%d", thispid); 
		
		int a=0; 

		while(a<=0){ 
			printf("Please enter a port number:"); 
			char buffer[1000];
			int c= 1000; 
			fgets(buffer, c, stdin); 
			a= atoi(buffer); 
 		}//end while 

		pid_t pid = fork();

		if (pid < 0) {
		die("fork failed");
		} 
		
		else if (pid == 0) {
		// child process
		fprintf(stderr, "[pid=%d] ", (int)getpid());
		b= (int)getpid(); 
		fprintf(stderr, "mdb-lookup-server started on port %d\n", a);
		char array[(int) sizeof(int) +1]; 
		sprintf(array, "%d", a); 
		execl("./mdb-lookup-server-nc.sh", "mdb-lookup-server-nc.sh", array, (char *) 0);
		die("execl failed");
		} 
		
		
		printf("An instance of mdb-lookup-server has started on pid and port number: "); 
		printf("%d %d", b, a); //printing out the pid and port number 

	}//end outer while loop 
return 0;
   
}
