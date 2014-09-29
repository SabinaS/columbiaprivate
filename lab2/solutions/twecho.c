
/*
 * twecho
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

static char **duplicateArgs(int argc, char **argv)
{
    char **copy;
    int i;

    // allocate the overall array
    copy = (char **)malloc((argc + 1) * sizeof(char *));
    if (copy == NULL) {
        perror("malloc failed");
        exit(1);
    }

    for (i = 0; i < argc; i++) {

        // allocate space for each string
        copy[i] = (char *)malloc(strlen(argv[i]) + 1);
        if (copy == NULL) {
            perror("malloc failed");
            exit(1);
        }

        // basically do the strcpy(), but with capitalization
        char *t = copy[i];
        char *s = argv[i];
        while ((*t++ = toupper(*s++)) != 0)
            ;
    }

    // don't forget to put NULL at the end of the overall array
    copy[argc] = NULL;

    return copy;
}

static void freeDuplicatedArgs(char **copy)
{
    char **p = copy;

    // free all the strings
    while (*p)
        free(*p++);

    // free the overall array
    free(copy);
}

int main(int argc, char **argv)
{
    if (argc <= 1)
        return 1;

    char **copy = duplicateArgs(argc, argv);
    char **p = copy;

    argv++;
    p++;
    while (*argv) {
        printf("%s %s\n", *argv++, *p++);
    }

    freeDuplicatedArgs(copy);

    return 0;
}

