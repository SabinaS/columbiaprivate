
/*
*tlist.h
*/

#ifndef __TLIST_H__
#define __TLIST_H__

using namespace std;
#include <vector>
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
		vector<T> v1;
};

template <typename T>
ostream& operator<<(ostream& os, const TList<T>& la)
{

	os << "{ ";

	typename vector<T>::const_iterator i; //get iterator 
	for(i = la.v1.begin(); i != la.v1.end(); ++i) //recurse through list 
		os << *i << " ";

	os << "}";
	return os;
}

template <typename T>
int inline TList<T>::size() const
{
	return v1.size(); //just getting size, function already defined 
}

template <typename T>
void inline TList<T>::reverse()
{
	::reverse(v1.begin(), v1.end()); //reverse defined in algorithm 
}

template <typename T>
TList<T>& TList<T>::addFront(const T& s)
{
	typename vector<T>::iterator i;
	i = v1.begin(); 
	v1.insert(i, s);  
	return *this;
}

template <typename T>
T TList<T>::popFront()
{
	T s = v1.front(); 
	::reverse(v1.begin(), v1.end()); 
	v1.pop_back();
	::reverse(v1.begin(), v1.end()); 
		
	return s; 	
}


template <typename T>
TList<T>& operator+=(const TList<T>& l1, const TList<T>& l2)
{
	TList<T>& temp = (TList<T>&) l1;
	

	::reverse(temp.v1.begin(), temp.v1.end());

	typename vector<T>::iterator i;
	i = temp.v1.begin();

	TList<T>& temp2 = (TList<T>&) l2;
	::reverse(temp2.v1.begin(), temp2.v1.end());
	temp.v1.insert(i, l2.v1.begin(), l2.v1.end());

	::reverse(temp.v1.begin(), temp.v1.end());
	::reverse(temp2.v1.begin(), temp2.v1.end());
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
	typename vector<T>::iterator it;
	it = v1.begin(); //start at beginning 

	
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
