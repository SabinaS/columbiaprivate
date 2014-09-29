#include <stdio.h>
#include <math.h>
#include "prime.h"

int prime(int x){
	int x; 
	for(int i=2; i<x; i++){
		if(x%i == 0 && i != x){
			return 0; /*returns a zero when false*/
		}
	}/* end for loop*/

	return 1; /*returns a 1 when true*/

}
