main: main.o ascend_order.o descend_order.o 
	gcc -o isort -Wall -g main.o ascend_order.o descend_order.o 

main.o: main.c ascend_order.h descend_order.h

ascend_order.o: ascend_order.c ascend_order.h 

descend_order.o: descend_order.c descend_order.h 

.PHONY: clean 
clean: 
	rm -f *.o *~  

.PHONY: all
all: clean main 
