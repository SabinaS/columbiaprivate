
/*
 * stack.h
 */

#ifndef __STACK_H__
#define __STACK_H__

using namespace std;
#include <deque>
#include <algorithm>
#include <iostream>

template <typename T>
class Stack;
template <typename T>
ostream& operator<<(ostream& os, const Stack<T>& rhs);

template <typename T>
class Stack
{
    public:

	bool empty() const { return q.empty(); }
	
	void push(const T& t) { q.push_back(t); }
	
        T pop();

        void reverse();

	friend ostream& operator<< <T>(ostream& os, const Stack<T>& rhs);

    private:
	
	deque<T> q;
};

template <typename T>
T Stack<T>::pop() 
{
    T t = q.back();
    q.pop_back();
    return t;
}

template <typename T>
void Stack<T>::reverse() 
{ 
    ::reverse(q.begin(), q.end()); 
}

template <typename T>
ostream& operator<<(ostream& os, const Stack<T>& rhs)
{
    os << "[ ";
    typename deque<T>::const_iterator i;
    for (i = rhs.q.begin(); i != rhs.q.end(); ++i)
	os << *i << " ";
    os << "<";
    return os;
}

#endif

