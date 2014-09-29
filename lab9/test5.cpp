#include <cstdio> 
#include <cassert>
#include "mystring.h"

int main(){ 

	MyString *s1 = new MyString("abcd"); 
	MyString *s2 = new MyString("abce");
	MyString *s3 = new MyString("abcc"); 


	cout << "Testing < assertion:" << endl; 
	assert(*s1 < *s2); 
	cout << "Testing <= assertion:" << endl; 
	assert(*s1 <= *s2); 
	cout << "Testing > assertion:" << endl; 
	assert(*s1 > *s3); 
	cout << "Testing >= assertion:" << endl; 
	assert(*s1 >= *s3); 
	cout << "Testing == assertion:" << endl; 
	assert(*s1 == *s1); 
	cout << "Testing != assertion:" << endl; 
	assert(*s1 != *s2); 


	MyString sp(" ");
        MyString period(".");
        MyString str;
        str += "This" + sp + "should" + sp 
            += "work" + sp + "without" 
            += sp + "any" + sp + "memory" 
            += sp + "leak" 
            += period;
        cout << str << endl;

	return 0; 
}//end amin 
