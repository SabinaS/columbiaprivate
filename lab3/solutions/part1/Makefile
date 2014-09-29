
#
# Makefile for lab 3, part 1.
#

CC  = gcc
CXX = g++

INCLUDES =
CFLAGS   = -g -Wall $(INCLUDES)
CXXFLAGS = -g -Wall $(INCLUDES)

LDFLAGS = -g
LDLIBS = 

mylist-test: libmylist.a

libmylist.a: mylist.o
	ar rc libmylist.a mylist.o
	ranlib libmylist.a

# header dependency
mylist-test.o: mylist.h
mylist.o: mylist.h

.PHONY: clean
clean:
	rm -f *.o *~ a.out core libmylist.a mylist-test

.PHONY: all
all: clean libmylist.a mylist-test

