CFLAGS= -Wall -g -I../../lab3/part1 

mdb-lookup: mdb-lookup.o 
	gcc -Wall -g -o mdb-lookup mdb-lookup.o -L../../lab3/part1 -lmylist -I../../lab3/part1 


.PHONY: clean
clean: 
	rm -f *.o *~

.PHONY: all
all: clean main 
