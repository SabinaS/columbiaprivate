
/*
 * test1.cpp - test driver for stack.h
 */

using namespace std;
#include <string>
#include "stack.h"

int main()
{
    Stack<string> s;

    s.push("one");
    s.push("two");
    s.push("three");
    s.push("four");

    while (!s.empty()) {
        cout << s << endl;
        cout << "popped " << s.pop() << endl;
        s.reverse();
        cout << "reversed the stack: " << s << endl;
        cout << endl;
    }

    return 0;
}

