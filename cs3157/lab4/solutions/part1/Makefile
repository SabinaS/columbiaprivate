
#
# Makefile for lab 4 part 1
#

CC  = gcc
CXX = g++

INCLUDES = -I../../lab3/part1
CFLAGS   = -g -Wall $(INCLUDES)
CXXFLAGS = -g -Wall $(INCLUDES)

LDFLAGS = -g -L../../lab3/part1
LDLIBS = -lmylist

.PHONY: default
default: mdb-add mdb-lookup

# header dependency
mdb-add.o: mdb.h
mdb-lookup.o: mdb.h

.PHONY: clean
clean:
	rm -f *.o *~ a.out core mdb-add mdb-lookup

.PHONY: all
all: clean default

