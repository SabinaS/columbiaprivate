#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <cassert>

#include "strlist.h"


//default constructor
StrList::StrList(){

	initList(&list); 

}//end default constructor 



//destrcutor
StrList::~StrList(){
	while(!isEmpty()){
		MyString *s = (MyString *)::popFront(&list); 
		delete s; 
	}
	
}//end destructor 

//copy consturctor
StrList::StrList(const StrList& l1){

	initList(&list); 

	StrList& copy = (StrList&) l1; 
	struct Node *l1head = copy.list.head; 

	while(l1head !=NULL){
		MyString *temp = (MyString *) l1head->data; 
		MyString *temp2 = new MyString(*temp);
		::addBack(&list, temp2); 
		l1head= l1head->next; 
		 
	}	
	

}//end copy constructor


//assignment operator 
StrList& StrList::operator=(const StrList& l1){
	//check to make sure not copying itself 
	if(this == &l1)
		return *this; 
	//clear memory errors 
	while(!isEmpty()){
		MyString *s = (MyString *)::popFront(&list);
		delete s; 
	}
	
	//copy stuff 
	initList(&list); 

	StrList& copy = (StrList&) l1; 
	struct Node *l1head = copy.list.head; 

	while(l1head !=NULL){
		MyString *temp = (MyString *) l1head->data;  
		MyString *temp2 = new MyString(*temp);
		addBack(&list, temp2);
		l1head= l1head->next;   
	}
	
	return *this; 	

}//end assignemnt operator 

void inline printString(void *p); 

//size function 

int StrList::size() const 
{
	int sizecount=0; 
	struct Node *node= list.head; 
	while(node != NULL){
		sizecount++;
		node = node->next; 
	}

	return sizecount; 

}//end size 

//addFront function
void StrList::addFront( const MyString s)
{

	MyString *string = new MyString(s); 
	::addFront(&list, string); 
	
}//end addFront


//popFront function

MyString StrList::popFront()
{
	MyString *s = (MyString *) ::popFront(&list); 
	MyString copy(*s);
	delete s; 
	return copy; 	
	
}//end popFront 


//reverse function

void StrList::reverse()
{
	::reverseList(&list); 
	
}//end reverse 


//operator +=
StrList& operator+=(const StrList& l1, const StrList& l2)
{
	while(&l1 == &l2){
		break;
	} 


	StrList& temp1 = (StrList&) l1; 

	struct Node *l2head = (struct Node *) (l2.list.head); 

	while(l2head !=NULL){
		MyString *temp = (MyString *) l2head->data; 
		MyString *temp2 = new MyString(*temp);
		::addBack(&(temp1.list), temp2);
		l2head= l2head->next;   
	}	

	return temp1; 
}//end += 

//operator +

StrList operator+(const StrList& l1, const StrList& l2)
{
	StrList temp(l1); 
	temp += l2; 
	return temp; 
}//end + 


//operator <<
ostream& operator<<(ostream& os, StrList& l1)
{
	os << "{"; 		
	struct Node *head = l1.list.head; 
	while(head){
		MyString *temp = (MyString *) head->data; 
		os << *temp << " "; 
		head = head->next; 
	}
	os << "}"; 
	return os; 
}//end <<

//operator []
MyString& StrList::operator[](int i) 
{
	int count=0; 
	struct Node *temp = list.head; 		
	while(count++ < i)
		temp = temp->next; 
	return *(MyString *) (temp->data); 
	
}//end [] 

//operator [] const 
const MyString& StrList::operator[](int i) const
{
	return ((StrList&) *this)[i]; 
}//end const [] 


