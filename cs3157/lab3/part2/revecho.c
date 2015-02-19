#include <stdio.h>
#include <string.h>
#include "mylist.h"

int main (int argc, char ** argv) {

	struct List thisList; 
	initList(&thisList); //passing memeory address of list 

	int c=1; //0 true, 1 false, initialized to false 
	
	argv++; //dont want name of program as input 

	void thisFunction(void *s){
		printf("%s\n", (char *)s); 
	}//end function 
		
	while (*argv !=0){

		if(c!=0){
			c= strcmp("dude", *argv);  //returns 0 if equal, 1 otherwise 
			
		}//end if 
		
		addFront(&thisList, *argv); 
		argv++; 

	}//end while 

	traverseList(&thisList, &thisFunction); 
	
	if(c==0){

	printf("Dude was found!"); 
	}//end if 

	else printf("Dude was not found =( "); 	
	
	removeAllNodes(&thisList); 

	return 0; 
}//end mai
