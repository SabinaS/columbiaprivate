
#
# Makefile for lab 3, part 2
#

CC  = gcc
CXX = g++

INCLUDES = -I../part1
CFLAGS   = -g -Wall $(INCLUDES)
CXXFLAGS = -g -Wall $(INCLUDES)

LDFLAGS = -g -L../part1
LDLIBS = -lmylist

revecho:

.PHONY: clean
clean:
	rm -f *.o *~ a.out core revecho

.PHONY: all
all: clean revecho

