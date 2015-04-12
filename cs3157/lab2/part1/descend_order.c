#include <stdio.h>
#include <stdlib.h>
#include "descend_order.h" 

int descend_order(int * s, int a)
{
	int i, j, temp; 

	for(j=1; j<a; j++)
	{
		temp= s[j]; 
		for(i=j-1; (i >=0) && (s[i] < temp); i--)
		{
			s[i+1]=s[i]; 
		} /*end inner for loop*/ 
		
		s[i+1]=temp; 

	} /*end outer for loop*/ 

	return * s; 
}/*end function*/ 
