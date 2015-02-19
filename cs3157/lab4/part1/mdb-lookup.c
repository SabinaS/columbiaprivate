#include <stdio.h>
#include <stdlib.h>
#include "mdb.h"
#include "mylist.h" 

int main(int argc, char ** argv){

	struct List thisList; 
	initList(&thisList); //passing memeory address of list 
	
	argv++; //dont want name of program as input 
	
	if (*argv !=0){ //adding contents of database to linked list 

		char *x;
		x = calloc(1, sizeof(FILE));

	
		FILE *file;
		*file= argv[1];
		int i = 0; 

		while ((x[i] = getc(file)) != EOF){

		  	addAfter(&thisList, x[i-1], x[i]); 
			i++; 

		}//end while 

	}//end if 

	printf("What would you like to seach for?\n"); 

	char *a; 
	scanf("%c \n", a); //accept user input 

	char *search; 

	char *strncpy (char *search, * a, 5); //copies 5 characters from a into search 

	struct list *list_pointer = &thisList; //list pointer being used to traverse list 
	
	while(thisList !=0){//searching through the linked list 

		if(list_pointer->value == search){
			printf("%d\n", list_pointer->value);//print location of list_pointer 
			printf("%c\n", list_pointer.value); //print value of list_pointer 
		}//end if 

		list_pointer = list_pointer->next;//go onto next element 

	
	}//end while 

	return 0; 

}//end main function 
