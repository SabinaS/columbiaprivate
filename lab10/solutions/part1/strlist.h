#ifndef __STRLIST_H__
#define __STRLIST_H__

#include "mystring.h"

extern "C" {
#include "mylist.h"
}

class StrList {

    public:

	// default constructor
	StrList();

	// destructor
	~StrList();

	// copy constructor 
	StrList(const StrList& src);

	// assignment operator
	StrList& operator=(const StrList& rhs);

	// test if it's an empty list
	int isEmpty() const { return isEmptyList((List *)&list); }

	// return the number of nodes in the list
	int size() const;

	// add a string to the front of the list
	void addFront(const MyString& str);

	// pop a string from the front of the list
	// The result of popping from an empty list is undefined.
	MyString popFront();

	// reverse the list
	void reverse();

	// operator+=
	// The result of "sl += sl" is undefined.
	StrList& operator+=(const StrList& rhs);

	// operator+
	friend StrList operator+(const StrList& lhs, const StrList& rhs);

	// put-to operator
	friend ostream& operator<<(ostream& os, const StrList& rhs);

	// operator[]: 
	// The result of accessing beyond the last element is undefined.
	MyString& operator[](int idx);

	// operator[] const:
	// The result of accessing beyond the last element is undefined.
	const MyString& operator[](int idx) const { 
	    return ((StrList&)*this)[idx];
	}

    private:

	struct List list;
};

#endif
