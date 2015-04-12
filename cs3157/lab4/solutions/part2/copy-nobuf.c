
/*
 * copy-nobuf.c
 */

#include <stdio.h>
#include <stdlib.h>

int main()
{
    setbuf(stdin, NULL); 
    setbuf(stdout, NULL); 

    int c;
    while ((c = getchar()) != EOF) {
	if (putchar(c) == EOF) {
	    perror("stdout");
	    exit(1);
	}
    }

    if (ferror(stdin)) {
	perror("stdin");
	exit(1);
    }

    return 0;
}

