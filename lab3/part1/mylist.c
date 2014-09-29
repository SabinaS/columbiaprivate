#include <stdio.h> 
#include <stdlib.h>
#include "mylist.h" 


/*
 * Create a node that holds the given data pointer,
 * and add the node to the front of the list.
 *
 * Note that this function does not manage the lifetime of the object
 * pointed to by 'data'.
 * 
 * It returns the newly created node on success and NULL on failure.
 */

struct Node *addFront(struct List *list, void *data){

	struct Node *newNode; //creating new node 
	newNode= (struct Node *)malloc( sizeof(struct Node) ); //allocating memory to it 
	
	newNode -> data = data; //datas not same thing 

	newNode -> next= list ->head; //head is first element 
	list -> head = newNode; //first element now points to new node we created 

	return newNode; //return data if newly created node sucessful 


}//end addFront 

/*
 * Traverse the list, calling f() with each data item.
 */

void traverseList(struct List *list, void (*f)(void *)){ //f is pointer to function that takes void * and returns void 

	struct Node *traverser= list -> head; 


	while ( traverser != 0) //looking at each element in the list 
        {
            	f(traverser -> data); 
		traverser = traverser->next; 
        }//end while loop 
  

}//end traverseList 

/*
 * Compare two double values pointed to by the two pointers.
 * Return 0 if they are the same value, 1 otherwise.
 */
int compareDouble(const void *data1, const void *data2) {
	
	double c= *(double *) data1- *(double *) data2; 
	if (c ==0) {
		return 0; 
	}
	else return 1; 

} //end compareDouble 

/*
 * Traverse the list, comparing each data item with 'dataSought' using
 * 'compar' function.  ('compar' returns 0 if the data pointed to by
 * the two parameters are equal, non-zero value otherwise.)
 *
 * Returns the first node containing the matching data, 
 * NULL if not found.
 */

struct Node *findNode(struct List *list, const void *dataSought, int (*compar)(const void *, const void *)) {

	struct Node *traverser = list -> head; 
 
	while ( traverser != 0) //for each element 
	{
    		if ( compar(traverser -> data, dataSought) == 0){ //looks for match using compare and returns node containing match 
			return traverser; 
		}//end inner if 
		
		traverser=  traverser -> next; 

	}//end while loop 

	return NULL; //returns null if no match found 
       

}//end findNode 

/*
 * Flip the sign of the double value pointed to by 'data' by
 * multiplying -1 to it and putting the result back into the memory
 * location.
 */
void flipSignDouble(void *data) {
	
	double * x = (double *) data; 
	double y= *x*(-1); //dereference x 

	*x= y; //data now holds the address of the new value 

}//end flipSignDouble 


/*
 * Remove the first node from the list, deallocate the memory for the
 * ndoe, and return the 'data' pointer that was stored in the node.
 * Returns NULL is the list is empty.
 */
void *popFront(struct List *list){

	struct Node *traverser= list -> head; 

  	if ( traverser != 0 ) { //while list not empty 
		
		list -> head = traverser -> next; 
		void * pointer= traverser ->data; 
		free(traverser); 
		return pointer; 
	
    	} //end if statement  

	else
		return NULL; //returns null if list is empty    
}//end popFront  

/*
 * Remove all nodes from the list, deallocating the memory for the
 * nodes.  You can implement this function using popFront().
 */
void removeAllNodes(struct List *list){

	while ( list->head != 0) //looking at each element in the list 
	{
    		popFront(list); //removes each node at front hence removing all nodes 
	
	}//end while loop 
   
}//end removeAllNodes 

/*
 * Create a node that holds the given data pointer,
 * and add the node right after the node passed in as the 'prevNode'
 * parameter.  If 'prevNode' is NULL, this function is equivalent to
 * addFront().
 *
 * Note that prevNode, if not NULL, is assumed to be one of the nodes
 * in the given list.  The behavior of this function is undefined if
 * prevNode does not belong in the given list.
 *
 * Note that this function does not manage the lifetime of the object
 * pointed to by 'data'.
 * 
 * It returns the newly created node on success and NULL on failure.
 */
struct Node *addAfter(struct List *list, struct Node *prevNode, void *data){
	
	struct Node *dataNode= NULL; 
	if( prevNode ==NULL){ 
		dataNode= addFront(list, data); 
		
	} //end if statement 
	else {
	dataNode= (struct Node *)malloc(sizeof(struct Node)); 
	dataNode -> data= data; //holds data pointer 
	
	dataNode-> next=prevNode -> next; 
	prevNode-> next= dataNode; 
	} 
	return dataNode; 
	

}//end addAfter 


/*

 * Note that this function reverses the list purely by manipulating
 * pointers.  It does NOT call malloc directly or indirectly (which
 * means that it does not call addFront() or addAfter()).
 *
 * Implementation hint: keep track of 3 consecutive nodes (previous,
 * current, next) and move them along in a while loop.  Your function
 * should start like this:


      struct Node *prv = NULL;
      struct Node *cur = list->head;
      struct Node *nxt;

      while (cur) {
          ......


 * And at the end, prv will end up pointing to the first element of
 * the reversed list.  Don't forget to assign it to list->head.
 */
void reverseList(struct List *list){

	struct Node *prv = NULL;
      	struct Node *cur = list->head;
      	struct Node *nxt;

      	while (cur) {
		
		nxt = cur -> next; 
		cur -> next = prv; 
		prv = cur; 
		cur = nxt; 
	
	}//end while loop  

	list -> head = prv; 
}//end reversist 


