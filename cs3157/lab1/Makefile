main: main.o gcd.o prime.o 
	gcc -o -Wall -g main.o gcd.o prime.0 

main.o: main.c prime.h gcd.h

prime.o: prime.c prime.h

gcd.o: gcd.c gcd.h

.PHONY: clean
clean: 
	rm -f *.exe *.o *~ 
