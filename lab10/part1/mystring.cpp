#include <cstring>
#include <cstdio>
#include <stdio.h>
#include <stdlib.h> 

#include "mystring.h"

// default constructor

MyString::MyString() 
{
#ifdef BASIC4TRACE
    fprintf(stderr, "BASIC4TRACE: (%p)->MyString()\n", this);
#endif

	data = new char[1];
	data[0] = '\0';

	len = 0;
}

// constructor

MyString::MyString(const char* p)
{
#ifdef BASIC4TRACE
    fprintf(stderr, "BASIC4TRACE: (%p)->MyString(const char *)\n", this);
#endif

	if (p) {
	len = strlen(p);
	data = new char[len+1];
	strcpy(data, p);
	} else {
	data = new char[1];
	data[0] = '\0';
	len = 0;
	}
}

// destructor

MyString::~MyString() 
{
#ifdef BASIC4TRACE
    fprintf(stderr, "BASIC4TRACE: (%p)->~MyString()\n", this);
#endif

	delete[] data;
}

// copy constructor 

MyString::MyString(const MyString& s) 
{
#ifdef BASIC4TRACE
    fprintf(stderr, "BASIC4TRACE: (%p)->MyString(const MyString&)\n", this);
#endif

	len = s.len;

	data = new char[len+1];
	strcpy(data, s.data);
}

// assignment operator

MyString& MyString::operator=(const MyString& rhs)
{
#ifdef BASIC4TRACE
    fprintf(stderr, "BASIC4TRACE: (%p)->op=(const MyString&)\n", this);
#endif

	if (this == &rhs) {
	return *this;
	}

	// first, deallocate memory that 'this' used to hold

	delete[] data;

	// now copy from rhs

	len = rhs.len;

	data = new char[len+1];
	strcpy(data, rhs.data);

	return *this;
	}

// operator+

MyString operator+(const MyString& s1, const MyString& s2)
{
#ifdef BASIC4TRACE
    fprintf(stderr, "BASIC4TRACE: op+(const MyString&, const MyString&)\n"); 
#endif	
	MyString temp(s1); 
	return (MyString) operator+=(temp, s2); 
	
//	temp.len = s1.len + s2.len;

//	temp.data = new char[temp.len+1];
//	strcpy(temp.data, s1.data);
//	strcat(temp.data, s2.data);


}

// put-to operator

ostream& operator<<(ostream& os, const MyString& s)
{
	os << s.data;
	return os;
}

// get-from operator

istream& operator>>(istream& is, MyString& s)
{
	// this is kinda cheating, but this is just to illustrate how this
	// function can work.

	string temp;
	is >> temp;

	delete[] s.data;

	s.len = strlen(temp.c_str());
	s.data = new char[s.len+1];
	strcpy(s.data, temp.c_str());

	return is;
}

// operator[] - in real life this function should be declared inline

char& MyString::operator[](int i) 
{
	return data[i];
}

// operator[] const - in real life this should be inline

const char& MyString::operator[](int i) const
{
	// illustration of casting away constness
	return ((MyString&)*this)[i];
}

// less-than operator

int operator<(const MyString& s, const MyString& s1)  
{

	
	return min(strcmp(s.data, s1.data), 0); 


	 //negative if first arg is smaller than second, is same zero, is first larger pos 

	
}

// less-than-or-equal-to operator

int operator<=(const MyString& s, const MyString& s1 )  
{


	if (strcmp(s.data, s1.data) == 0){
		return 1;
	} 
	
	else if(min(strcmp(s.data, s1.data), 0) !=0){
		return 1; 
	}

	else {
		return 0; 
	}  
	
}

// greater-than operator

int operator>(const MyString& s, const MyString& s1)  
{

	
	return max(strcmp(s.data, s1.data), 0); 
	
}
// greater-than-or-equal-to operator 

int operator>=(const MyString& s, const MyString& s1) 
{

	if (strcmp(s.data, s1.data) == 0){
		return 1;
	}
	
	else if(max(strcmp(s.data, s1.data), 0) !=0){
		return 1; 
	}

	else {
		return 0; 
	}  
}

// equal-to operator

int operator==(const MyString& s, const MyString& s1)  
{

	if (strcmp(s.data, s1.data) == 0){
		return 1;
	} 
	
	else{
		return 0; 
	}
	
}

// not-equal-to operator

int operator!=(const MyString& s, const MyString& s1)  
{


	if (strcmp(s.data, s1.data) == 0){
		return 0;
	} 
	
	else{
		return 1; 
	}
		
}


MyString& operator+=(const MyString& s, const MyString& s1)  
{


	MyString &temp = (MyString&) s; 
	char *tempArray = (char*)calloc(1, (s.len +1)*sizeof(char)); 
	strcpy(tempArray, s.data); 

	delete[] temp.data; 

	temp.data = new char [s.len + s1.len + 1]; 
	
	strcpy(temp.data, tempArray); 
	strcpy(temp.data + s.len, s1.data); 

	temp.len = s.len + s1.len; 
	
	return temp; 
	
	
}
