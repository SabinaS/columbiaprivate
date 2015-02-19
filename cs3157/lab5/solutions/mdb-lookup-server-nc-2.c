
/*
 * mdb-lokup-server-nc-2.c
 */

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

int main(int argc, char **argv)
{
    pid_t pid;

    for (;;) {  // infinite loop

	// check if any child process has terminated.
	// we keep calling waitpid(...,WNOHANG) until it returns 0 or
	// -1, which indicate that all children are still running or
	// that there is no child process, respectively.
	
	while ((pid = waitpid((pid_t)-1, NULL, WNOHANG)) > 0) {
	    fprintf(stderr, "[pid=%d] ", (int)pid);
	    fprintf(stderr, "mdb-lookup-server terminated\n");
	}

	//////////////////////////////////////////////////////
	// read a port number.
	
	// display prompt.
	printf("port number: ");
	// start with an empty line buffer.
	char line[100];
	line[0] = '\0';
	// read a line from stdin.
	fgets(line, sizeof(line), stdin);
	// try parsing a number from the line buffer.
	// if there was no number in there, simply continue on to the
	// next iteration of the loop.
	int n;
	if (sscanf(line, "%d", &n) != 1)
	    continue;
	// we got a port number.
	// write it back out to line buffer (thereby getting rid of
	// initial whitespaces, newline at the end, etc.)
	sprintf(line, "%d", n);

	/////////////////////////////////////////////////////////////
	// fork a child process running an mdb-lookup-server-nc.sh.

	pid = fork();
	if (pid < 0) {
	    die("fork failed");
	} else if (pid == 0) {
	    // child process
	    fprintf(stderr, "[pid=%d] ", (int)getpid());
	    fprintf(stderr, "mdb-lookup-server started on port %s\n", line);
	    execl("./mdb-lookup-server-nc.sh", "mdb-lookup-server-nc.sh",
		    line, (char *)0);
	    die("execl failed");
	} else {
	    // parent process will continue on to the next iteration
	    // of the loop.
	}
    }

    return 0;
}

