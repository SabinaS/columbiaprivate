#ifndef __MYSTRING_H__
#define __MYSTRING_H__

using namespace std;

#include <iostream>

class MyString {

    public:

	// default constructor
	MyString();

	// constructor
	MyString(const char* p);

	// destructor
	~MyString();

	// copy constructor 
	MyString(const MyString& s);

	// assignment operator
	MyString& operator=(const MyString& s);

	// returns the length of the string
	int length() const { return len; }
	
	// operator+
	friend MyString operator+(const MyString& s1, const MyString& s2);

	// put-to operator
	friend ostream& operator<<(ostream& os, const MyString& s);

	// get-from operator
	friend istream& operator>>(istream& is, MyString& s);

	// operator[]
	char& operator[](int i);

	// operator[] const
	const char& operator[](int i) const;

	// operator<
	friend int operator<(const MyString& s, const MyString& s1); 

	// operator<= 
	friend int operator<=(const MyString& s, const MyString& s1); 
	
	// operator>
	friend int operator>(const MyString& s, const MyString& s1); 

	// operator>= 
	friend int operator>=(const MyString& s, const MyString& s1); 

	// operator==
	friend int operator==(const MyString& s, const MyString& s1); 

	// operator!= 
	friend int operator!=(const MyString& s, const MyString& s1); 

	// operator+= 
	friend MyString& operator+=(const MyString& s, const MyString& s1); 



    private:

	char* data;

	int len;
};

#endif
