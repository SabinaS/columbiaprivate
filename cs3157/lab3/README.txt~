Sabina Smajlaj
ss3912
Lab #3

Part1

Instead of "make all", please use the command "make clean" and then "make" since my Makefile does not contain a 'main' function.

All parts run as specified. 

This is the valgrind output:

ss3912@damascus /home/ss3912/cs3157/lab3/part1 $ valgrind --leak-check=yes ./mylist-test
==2785== Memcheck, a memory error detector
==2785== Copyright (C) 2002-2011, and GNU GPL'd, by Julian Seward et al.
==2785== Using Valgrind-3.7.0 and LibVEX; rerun with -h for copyright info
==2785== Command: ./mylist-test
==2785== 
testing addFront(): 9.0 8.0 7.0 6.0 5.0 4.0 3.0 2.0 1.0 
testing flipSignDouble(): -9.0 -8.0 -7.0 -6.0 -5.0 -4.0 -3.0 -2.0 -1.0 
testing flipSignDouble() again: 9.0 8.0 7.0 6.0 5.0 4.0 3.0 2.0 1.0 
testing findNode(): OK
popped 9.0, the rest is: [ 8.0 7.0 6.0 5.0 4.0 3.0 2.0 1.0 ]
popped 8.0, the rest is: [ 7.0 6.0 5.0 4.0 3.0 2.0 1.0 ]
popped 7.0, the rest is: [ 6.0 5.0 4.0 3.0 2.0 1.0 ]
popped 6.0, the rest is: [ 5.0 4.0 3.0 2.0 1.0 ]
popped 5.0, the rest is: [ 4.0 3.0 2.0 1.0 ]
popped 4.0, the rest is: [ 3.0 2.0 1.0 ]
popped 3.0, the rest is: [ 2.0 1.0 ]
popped 2.0, the rest is: [ 1.0 ]
popped 1.0, the rest is: [ ]
testing addAfter(): 1.0 2.0 3.0 4.0 5.0 6.0 7.0 8.0 9.0 
popped 1.0, and reversed the rest: [ 9.0 8.0 7.0 6.0 5.0 4.0 3.0 2.0 ]
popped 9.0, and reversed the rest: [ 2.0 3.0 4.0 5.0 6.0 7.0 8.0 ]
popped 2.0, and reversed the rest: [ 8.0 7.0 6.0 5.0 4.0 3.0 ]
popped 8.0, and reversed the rest: [ 3.0 4.0 5.0 6.0 7.0 ]
popped 3.0, and reversed the rest: [ 7.0 6.0 5.0 4.0 ]
popped 7.0, and reversed the rest: [ 4.0 5.0 6.0 ]
popped 4.0, and reversed the rest: [ 6.0 5.0 ]
popped 6.0, and reversed the rest: [ 5.0 ]
popped 5.0, and reversed the rest: [ ]
==2785== 
==2785== HEAP SUMMARY:
==2785==     in use at exit: 0 bytes in 0 blocks
==2785==   total heap usage: 18 allocs, 18 frees, 288 bytes allocated
==2785== 
==2785== All heap blocks were freed -- no leaks are possible
==2785== 
==2785== For counts of detected and suppressed errors, rerun with: -v
==2785== ERROR SUMMARY: 0 errors from 0 contexts (suppressed: 2 from 2)




Part2

Here as well, instead of "make all", please use the command "make clean" and then "make" since my Makefile does not contain a 'main' function.

All parts run as specified. 

This is the valgrind output:

ss3912@damascus /home/ss3912/cs3157/lab3/part2 $ valgrind --leak-check=yes ./revecho my bad dude
==3480== Memcheck, a memory error detector
==3480== Copyright (C) 2002-2011, and GNU GPL'd, by Julian Seward et al.
==3480== Using Valgrind-3.7.0 and LibVEX; rerun with -h for copyright info
==3480== Command: ./revecho my bad dude
==3480== 
dude
bad
my
Dude was found!==3480== 
==3480== HEAP SUMMARY:
==3480==     in use at exit: 0 bytes in 0 blocks
==3480==   total heap usage: 3 allocs, 3 frees, 48 bytes allocated
==3480== 
==3480== All heap blocks were freed -- no leaks are possible
==3480== 
==3480== For counts of detected and suppressed errors, rerun with: -v
==3480== ERROR SUMMARY: 0 errors from 0 contexts (suppressed: 2 from 2)











