
/*
 * test5.cpp
 */

#include <cassert>
#include "mystring.h"

int main()
{
    // test relational ops

    MyString s1("abc");
    MyString s2("xyz");

    assert(s1 != s2);
    assert(!(s1 == s2));

    assert(s1 < s2);
    assert(s2 > s1);

    assert(!(s1 >= s2));
    assert(!(s2 <= s1));

    // test op+=() and op+()

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
}

