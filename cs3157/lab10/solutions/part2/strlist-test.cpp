#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <cassert>

#include "strlist.h"

int main()
{
    StrList l1;
    l1.addFront("0");
    l1.addFront("1");
    
    // l1: { 1 0 }

    {
	StrList l2(l1);
	l2.addFront("2");
	l2.addFront("3");
	l1 += l2;

	// l2: { 3 2 1 0 }
	// l1: { 1 0 3 2 1 0 }
	//
	// l2 goes out of scope here
    }

    assert("1" == l1.popFront());
    assert("0" == l1.popFront());

    // l1: { 3 2 1 0 }
    
    MyString s1("4");
    l1.addFront(s1);
    s1[0] = '*'; // This shouldn't affect the string in l1.
    assert(l1[0] == "4");
    assert(s1 == "*");
    
    MyString s2("five");
    l1.addFront(s2);
    l1[0] = "5";
    assert(l1[0] == "5");
    assert(s2 == "five");

    // l1: { 5 4 3 2 1 0 }

    const StrList l3 = l1;
    assert(l1.size() == l3.size());

    int size = l3.size();
    assert(size == 6);

    int i;
    for (i = 0; i < size; i++) {
	assert(l1[i] == l3[i]);
	l1[i] += l3[i];
    }

    // l1: { 55 44 33 22 11 00 }

    for (i = 0; i < 100; i++) {
	l1.addFront("junk");
    }

    // l1: { junk junk ...(100 times)... junk 55 44 33 22 11 00 }

    l1 = l1;
    l1 = l3;
    l1 = l1;

    // l1: { 5 4 3 2 1 0 }

    l1 += l1 + l3 + l1 + l3;
    assert(l1.size() == size * 5);

    l1.reverse();

    // l1: { 0 1 2 3 4 5 0 1 2 3 4 5 0 1 2 3 4 5 0 1 2 3 4 5 0 1 2 3 4 5 }

    int n;
    char buf[100];
    for (i = 0; i < size * 5; i++) {
	MyString& s = l1[i];
	char c = s[0];
	// This is how you convert a single digit character like '3'
	// into the corresponding interger.
	n = c - '0'; 
	n += size * (i / size);
	sprintf(buf, "%d", n);
	s = buf;
    }

    // l1: { 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 
    //      15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 }

    l1.reverse();
    while (n >= 10) {
	sprintf(buf, "%d", n);
	assert(buf == l1.popFront());
	n--;
    }
    assert(l1.size() == 10);
    l1.reverse();
    assert(l1.size() == 10);

    // l1: { 0 1 2 3 4 5 6 7 8 9 } 

    cout << l1 << endl;

    return 0;
}
