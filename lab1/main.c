#include <stdio>
#include <math.h>
#include "gcd.h"
#include "prime.h"

int gcd(int x, int y);
int prime(int x);

int main()
{
	int a, b, d, e;
	float c; 

	printf("Enter the first value:");
	scanf("%d", a);

	printf("Enter the second value")
	scanf("%d", b);

	
	c= ((a+b)/2); 
	printf("The average of the two is:");
	printf("%f", c); 

	d= prime(a);
	e= prime(b); 
	
	if(d==0){
		printf("Your first value is a prime!"); 
	}/*end if statement*/

	if(e==0){
		printf("Your second value is a prime!");
	}/*end if statement*/ 

	int f= gcd(int a, int b)
	printf("The GCD of your two numbers is:");
	printf("%d", f);

	return 0; 
}/*end function*/ 

