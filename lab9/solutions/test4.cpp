
/*
 * test4.cpp
 */

#include "mystring.h"

static MyString add(MyString s1, MyString s2)
{
    MyString temp(" and ");
    return s1 + temp + s2;
}

int main()
{
    MyString s1("one");
    MyString s2("two");

    MyString s3 = add(s1, s2);

    cout << s3 << endl;
    return 0;
}

