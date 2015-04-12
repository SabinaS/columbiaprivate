#include <stdio.h>
#include <math.h>
#include "gcd.h"

int gcd(int x, int y){
	
	int x, y 

	if(y>x){ /*switches the values of x and y so x is always the larger number*/
		int a=x; 
		x=y; 
		y=a; 
	}/*end*/


	while(y !=0){
		int b= x%y; 
		x=y; 
		y=b;
	}/*end*/

	return x; 
}

