mylist-test: mylist-test.o libmylist.a 
	gcc -c mylist-test.c -Wall -g 
	gcc mylist-test.o -L. -lmylist -o mylist-test 

libmylist.a: mylist.h 
	gcc -c mylist.c -o mylist.o 
	ar rc libmylist.a mylist.o 
	ranlib libmylist.a 

mylist-test.o: libmylist.a 

mylist.h: mylist.c 

.PHONY: clean 
clean: 
	rm -f *.o *~ libmylist.a mylist-test 

.PHONY: all
all: clean main 


