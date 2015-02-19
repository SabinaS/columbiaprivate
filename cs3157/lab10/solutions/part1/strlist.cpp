#include <cstdio>
#include <cstdlib>
#include <cstring>

#include "strlist.h"

// default constructor
StrList::StrList()
{
    initList(&list);
}

static void deleteStr(void *s)
{
    MyString *str = (MyString *)s;
    delete str;
}

// destructor
StrList::~StrList()
{
    traverseList(&list, &deleteStr);
    removeAllNodes(&list);
}

static void die(const char *message)
{
    perror(message);
    exit(1); 
}

static void appendAllElements(struct List *target, struct List *source)
{
    // reverse the larget list
    reverseList(target);

    // Traverse the source list, duplicating each MyString object and
    // adding it to the front of the target list.
    struct Node *node = source->head;
    while (node) {
	MyString *s_src = (MyString *)node->data;
	MyString *s_tgt = new MyString(*s_src);
	if (addFront(target, s_tgt) == NULL)
	    die("addFront() failed");
	node = node->next;
    }

    // reverse the larget list again
    reverseList(target);
}

// copy constructor 
StrList::StrList(const StrList& src)
{
    initList(&list);
    appendAllElements(&list, (List *)&src.list);
}

// assignment operator
StrList& StrList::operator=(const StrList& rhs)
{
    // check for self-assignment
    if (this == &rhs) {
	return *this;
    }

    // remove everything I used to have
    traverseList(&list, &deleteStr);
    removeAllNodes(&list);

    // copy things over from rhs
    appendAllElements(&list, (List *)&rhs.list);
    return *this;
}

// return the number of nodes in the list
int StrList::size() const
{
    int count = 0;
    struct Node *node = list.head;
    while (node) {
	count++;
	node = node->next;
    }
    return count;
}

// add a string to the front of the list
void StrList::addFront(const MyString& str)
{
    MyString *newStr = new MyString(str);
    if (::addFront(&list, newStr) == NULL)
	die("addFront() failed");
}

// Pop a string from the front of the list.
// The result of popping from an empty list is undefined.
MyString StrList::popFront()
{
    MyString *str = (MyString *)::popFront(&list);
    MyString strToReturn(*str);
    delete str;
    return strToReturn;
}

// reverse the list
void StrList::reverse()
{
    reverseList(&list);
}

// operator+=
// The result of "sl += sl" is undefined.
StrList& StrList::operator+=(const StrList& rhs)
{
    appendAllElements(&list, (List *)&rhs.list);
    return *this;
}

// operator+
StrList operator+(const StrList& lhs, const StrList& rhs)
{
    StrList sl;
    appendAllElements(&sl.list, (List *)&lhs.list);
    appendAllElements(&sl.list, (List *)&rhs.list);
    return sl;
}

// put-to operator
ostream& operator<<(ostream& os, const StrList& rhs)
{
    os << "{ ";
    struct Node *node = rhs.list.head;
    while (node) {
	os << *(MyString *)node->data << " ";
	node = node->next;
    }
    os << "}";
    return os;
}

// operator[]: 
// The result of accessing beyond the last element is undefined.
MyString& StrList::operator[](int idx)
{
    struct Node *node = list.head;
    for (int i = 0; i < idx; i++) 
	node = node->next;
    return *(MyString *)node->data;
}
