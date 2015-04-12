
/*
 * hole.c
 */

#include <stdio.h>
#include <stdlib.h>

int main()
{
    char *filename = "file-with-hole";
    FILE *fp = fopen(filename, "wb");
    if (fp == NULL) {
	perror(filename);
	exit(1);
    }

    if (fputc('A', fp) == EOF) {
	perror(filename);
	exit(1);
    }
    
    if (fseek(fp, 1000000, SEEK_SET) == -1) {
	perror(filename);
	exit(1);
    }

    if (fputc('Z', fp) == EOF) {
	perror(filename);
	exit(1);
    }
    
    fclose(fp);
    return 0;
}

