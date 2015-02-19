#include <stdio.h>
#include <stdlib.h>
#include "ascend_order.h"
#include "descend_order.h" 

int ascend_order(int * t, int a);
int descend_order(int * s, int a);

int main()
{
	int a; /*the user input for size of array*/
	
	printf("Please enter the size of the array you wish to crease: \n");
	scanf("%d", & a); 
	
	int *array=(int *) malloc(a *sizeof(int)); /*original array and allocating memory to it*/ 
	int *array_one=(int *) malloc(a *sizeof(int));  /*first copy of array and allocating memory to it*/
	int *array_two=(int *) malloc(a *sizeof(int)); /*second copy of array and allocating memory to it*/

	int i;  
	for (i=0; i<a; i++)
	{
		array[i]= random(); /*fills the ith place in the array with a random number*/
	} /*end for loop*/ 
	
	int c; 
	for (c=0; c<a; c++)
	{
		array_one[c] = array[c]; /*copies array into array_one */ 
	} /*end for loop*/ 

	ascend_order(array_one, a); /*sorts array_one so smallest integer is first*/

	int d; 
	for (d=0; d<a; d++)
	{
		array_two[d]= array[d]; /*copies array into array_two */ 
	} /*end for loop*/  

	descend_order(array_two,a); /*sorts array_two so largest integer is first */ 

	printf("The elements of the original array: \n"); 
	
	int e; 
	for(e=0; e<a; e++)
	{
		printf("%d\n", array[e]); /*printing each element of the original array*/ 
	} /*end for loop */ 

	printf("The elements of the first copy are: \n"); 
	
	int f; 
	for(f=0; f<a; f++)
	{
		printf("%d\n", array_one[f]); 
	} /*end for loop*/ 

	printf("The elements of the second copy are: \n"); 

	int g; 
	for(g=0; g<a; g++)
	{
		printf("%d\n", array_two[g]); 
	} /*end for loop*/ 
	
	free(array); //freeing the memory allocated
	free(array_one); 
	free(array_two);  

} /*end main function*/ 
