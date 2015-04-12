CFLAGS= -Wall -g -I../part1 

revecho: revecho.o 
	gcc -Wall -g -o revecho revecho.o -L../part1 -lmylist -I../part1 

.PHONY: clean 
clean: 
	rm -f *.o *~ revecho 

.PHONY: all
all: clean main 


