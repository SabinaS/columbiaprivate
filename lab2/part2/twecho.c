/*
 * twecho
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

static char **duplicateArgs(int argc, char **argv)
{
	char** cpyarray=(char **)calloc((argc+1), sizeof(char *)); 

	int b; 
		
	for (b=0; b<argc; b++)//loops through every string 
	{
		int d= 1+ strlen(argv[b]); 
		
		cpyarray[b]= (char *) calloc(d , sizeof(char)); //same as malloc but no need to manually add zero at the end 
		int f; 
		for(f=0; f<d; f++)//loops through every character of the string 
		{
			
			cpyarray[b][f]= toupper(argv[b][f]); 

		} //end inner for loop 	 
	
	}//end outer for loop 

	return cpyarray; 

}//end function 

static void freeDuplicatedArgs(char **copy)
{
	int g=0; 
 
	while(copy[g] != 0)
	{

		free(copy[g]); 
		g++; 
	} //end while loop 

	free(copy); 

}//end function

/*
 * DO NOT MODIFY main().
 */
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
