/* 
* tlist.h
*/

#ifndef __TLIST_H__
#define __TLIST_H__

using namespace std;
#include <algorithm>
#include <iostream>
#include <vector> 

template <typename T>
class TList;
template <typename T>
ostream& operator<<(ostream& os, const TList<T>& rhs);
 
template <typename T> 
TList<T>& operator+=(const TList<T>& l1, const TList<T>& l2);   

template <typename T>
class TList
{

	public:
	/* The compiler-generated basic 4 functions work just fine 
		hence we do not need to recreate them
	*/
	
	public:
		T& operator[](int i);
		const T& operator[](int i) const;
		TList<T> operator+(const TList<T>& la);
		TList<T>& addFront(const T& s);
		T popFront();
		int size() const;
		//void reverse();
		friend ostream& operator<< <T>(ostream& os, const TList<T>& la);
		friend TList<T>& operator+= <T>(const TList<T>& l1, const TList<T>& l2); 
	
	private: 
		vector<T> v1; 

}; //end TList 

/* Basic 4 functions are fine as complier-generated 
* hence we do not need to rewrite them again
*/



template <typename T>
ostream& operator<<(ostream& os, const TList<T>& rhs)
{
	os << "{ ";
	typename vector<T>::const_iterator i; //getting pointer to first element 
	for (i = rhs.v1.begin(); i != rhs.v1.end(); ++i) //iterating through entire list 
		os << *i << " ";
	os << "}";
	return os; 
}

//reverse function
//template <typename T>
//void inline TList<T>::reverse()
//{
//	::reverse(v1.begin(), v1.end()); 
	
//}//end reverse 
//size function 
template <typename T>
int TList<T>::size() const 
{
	return v1.size(); 

}//end size 

//addFront function
template <typename T>
TList<T>& TList<T>::addFront( const T& s)
{
	typename vector<T>::iterator i;
	i = v1.begin(); 
	v1.insert(i, s);  
	return *this;
	 
	
}//end addFront


//popFront function
template <typename T>
T TList<T>::popFront()
{
	T s = v1.front(); 
	//::reverse(v1.begin(), v1.end()); 
	v1.pop_back();
	//::reverse(v1.begin(), v1.end()); 
		
	return s; 	
	
}//end popFront 





//operator +=
template <typename T>
TList<T>& operator+=(const TList<T>& l1, const TList<T>& l2)
{
	TList<T>& temp = (TList<T>&) l1;
	

	//::reverse(temp.v1.begin(), temp.v1.end());
	
	typename vector<T>::iterator i;
	i = temp.v1.begin();

	//TList<T>& temp2 = (TList<T>&) l2;
	//::reverse(temp2.v1.begin(), temp2.v1.end());
	temp.v1.insert(i, l2.v1.begin(), l2.v1.end());

	//::reverse(temp.v1.begin(), temp.v1.end());
	//::reverse(temp2.v1.begin(), temp2.v1.end());
	return temp;
		
}//end += 

//operator +
template <typename T>
TList<T> TList<T>::operator+(const TList<T>& l2)
{
	TList<T> temp = *this;
	temp += l2;
	return temp;
}//end + 




//operator []

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

	
}//end [] 

//operator [] const 
template <typename T>
const T& TList<T>::operator[](int i) const
{
	return ((TList<T>&) *this)[i]; 
}//end const [] 

#endif
