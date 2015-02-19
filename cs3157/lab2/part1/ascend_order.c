#include <stdio.h>
#include <stdlib.h>
#include "ascend_order.h"


int ascend_order(int * t, int a)
{
	int j, i, temp; 
	
	for(j=1; j<a; j++)
	{
		temp=t[j]; 
		for(i=j-1; (i >=0) && (t[i] > temp); i--)
		{
			t[i+1]=t[i]; 
		}/*end inner for loop*/ 
	
		t[i+1]=temp; 

	}/*end outer for loop*/

	return * t; 

} /*end function*/ 
