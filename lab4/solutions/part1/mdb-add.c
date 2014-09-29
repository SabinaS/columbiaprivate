/*
 * mdb-add.c
 */

#include "mdb.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

static void sanitize(char *s)
{
    while (*s) {
	if (!isprint(*s)) 
	    *s = ' ';
	s++;
    }
}

int main(int argc, char **argv)
{
    /*
     * open the database file specified in the command line
     */

    if (argc != 2) {
        fprintf(stderr, "%s\n", "usage: mdb-add <database_file>");
        exit(1);
    }

    char *filename = argv[1];
    FILE *fp = fopen(filename, "ab"); // open in append, binary mode
    if (fp == NULL) {
        perror(filename);
        exit(1);
    }

    /*
     * read name
     */

    struct MdbRec r;
    char line[1000];
    
    printf("name please (will truncate to %d chars): ", (int)sizeof(r.name)-1);
    if (fgets(line, sizeof(line), stdin) == NULL) {
        fprintf(stderr, "%s\n", "could not read name");
        exit(1);
    }
    // must null-terminate the string manually after strncpy().
    strncpy(r.name, line, sizeof(r.name) - 1);
    r.name[sizeof(r.name) - 1] = '\0';
    // if newline is there, remove it.
    size_t last = strlen(r.name) - 1;
    if (r.name[last] == '\n')
        r.name[last] = '\0';

    /*
     * read msg
     */

    printf("msg please (will truncate to %d chars): ", (int)sizeof(r.msg)-1);
    if (fgets(line, sizeof(line), stdin) == NULL) {
        fprintf(stderr, "%s\n", "could not read msg");
        exit(1);
    }
    // must null-terminate the string manually after strncpy().
    strncpy(r.msg, line, sizeof(r.msg) - 1);
    r.msg[sizeof(r.msg) - 1] = '\0';
    // if newline is there, remove it.
    last = strlen(r.msg) - 1;
    if (r.msg[last] == '\n')
        r.msg[last] = '\0';

    /*
     * write the name and msg to the database file
     */

    // remove non-printable chars from the strings
    sanitize(r.name);
    sanitize(r.msg);

    if (fwrite(&r, sizeof(r), 1, fp) < 1) {
        perror("can't write record");
        exit(1);
    }
    if (fflush(fp) != 0) {
	perror("can't write to file");
	exit(1);
    }

    printf("successfully added: \n"
            "   name = {%s}\n"
            "   msg  = {%s}\n", r.name, r.msg);
    fflush(stdout);

    fclose(fp);
    return 0;
}
