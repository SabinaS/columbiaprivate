
# 
# Makefile for lab 9, part 2
#

CC  = g++
CXX = g++ -O0 -fno-elide-constructors

# Comment out one of the following 2 lines to enable/disable BASIC4TRACE.
#INCLUDES = -DBASIC4TRACE
INCLUDES =

CFLAGS   = -g -Wall $(INCLUDES)
CXXFLAGS = -g -Wall $(INCLUDES)

LDFLAGS = -g
LDLIBS  =

executables = test1 test2 test3 test4 test5
objects = mystring.o test1.o test2.o test3.o test4.o test5.o

.PHONY: default
default: $(executables)

$(executables): mystring.o

$(objects): mystring.h

.PHONY: clean
clean:
	rm -f *~ a.out core $(objects) $(executables)

.PHONY: all
all: clean default

