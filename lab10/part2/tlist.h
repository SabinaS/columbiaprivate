/*
*tlist.h
*/

#ifndef __TLIST_H__
#define __TLIST_H__

using namespace std;
#include <list>
#include <algorithm>
#include <iostream>

// class declaration
template <typename T>
class TList;

template <typename T>
ostream& operator<<(ostream& os, const TList<T>& l);

template <typename T>
TList<T>& operator+=(const TList<T>& tl1, const TList<T>& tl2);

template <typename T>
class TList
{
	/* The compiler-generated basic 4 functions work just fine 
		hence we do not need to recreate them
	*/
	
	public:
		T& operator[](int i);
		const T& operator[](int i) const;
		TList<T> operator+(const TList<T>& la);
		TList<T>& addFront(const T& t);
		T popFront();
		int size() const;
		void reverse();
		friend ostream& operator<< <T>(ostream& os, const TList<T>& la);
		friend TList<T>& operator+= <T>(const TList<T>& l1, const
			TList<T>& l2);
	
	private:
		list<T> list1;
};

template <typename T>
ostream& operator<<(ostream& os, const TList<T>& la)
{

	os << "{ ";

	typename list<T>::const_iterator i; //get iterator 
	for(i = la.list1.begin(); i != la.list1.end(); ++i) //recurse through list 
		os << *i << " ";

	os << "}";
	return os;
}

template <typename T>
int inline TList<T>::size() const
{
	return list1.size(); //just getting size, function already defined 
}

template <typename T>
void inline TList<T>::reverse()
{
	list1.reverse(); //reverse is also already defined 
}

template <typename T>
TList<T>& TList<T>::addFront(const T& s)
{
	list1.push_front(s);
	return *this;
}

template <typename T>
T TList<T>::popFront()
{
	T t = list1.front();
	list1.pop_front();
	return t;
}


template <typename T>
TList<T>& operator+=(const TList<T>& l1, const TList<T>& l2)
{

	TList<T>& temp = (TList<T>&) l1;
	

	temp.list1.reverse();
	
	typename list<T>::iterator i;
	i = temp.list1.begin();

	TList<T>& temp2 = (TList<T>&) l2;
	temp2.list1.reverse();
	temp.list1.insert(i, l2.list1.begin(), l2.list1.end());

	temp.list1.reverse();
	temp2.list1.reverse();
	return temp;
}

template <typename T>
TList<T> TList<T>::operator+(const TList<T>& la)
{
	TList<T> temp = *this;
	temp += la;
	return temp;
}

template <typename T>
T& TList<T>::operator[](int i)
{
	int count = 0;
	// Get the iterator
	typename list<T>::iterator it;
	it = list1.begin(); //start at beginning 

	
	while(count++ < i)
		it++;
	return *it;
}

template <typename T>
const T& TList<T>::operator[](int i) const
{
	return ((TList<T>&) *this)[i];
}
#endif

