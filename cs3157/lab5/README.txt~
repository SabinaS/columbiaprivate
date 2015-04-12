Sabina Smajlaj
ss3912	
LAb Assignemnt #5

Programs work as they should, except for checking for terminated child processes. If I had more time, I would make a loop that counted the number and looped through, printing each until the total number were 0.  

This is a few minutes late just because it has been taking me a long time to submit it due to not knowing I had to clone the lab5 files into my own directory. That took a few extra minutes to do since I did not want it to overwrite my own files. 

Now this is really late. Turns out I added my old files to lab5, the ones that didn't work. So I had to rewrite all of them. Then I forgot to save that, so I had to rewrite them all once again. I feel idiotic. Please don't penalize me for it... 

Run make all instead of make. 


Valgrind output for (b)

==16437== Memcheck, a memory error detector
==16437== Copyright (C) 2002-2011, and GNU GPL'd, by Julian Seward et al.
==16437== Using Valgrind-3.7.0 and LibVEX; rerun with -h for copyright info
==16437== Command: ./mdb-lookup-server-nc-1
==16437== 
usage: ./mdb-lookup-server-nc-1 <port>
==16437== 
==16437== HEAP SUMMARY:
==16437==     in use at exit: 0 bytes in 0 blocks
==16437==   total heap usage: 0 allocs, 0 frees, 0 bytes allocated
==16437== 
==16437== All heap blocks were freed -- no leaks are possible
==16437== 
==16437== For counts of detected and suppressed errors, rerun with: -v
==16437== ERROR SUMMARY: 0 errors from 0 contexts (suppressed: 2 from 2)


Valgrind output for (c)

rm -f *.o *~
gcc -Wall -g     mdb-lookup-server-nc-2.c   -o mdb-lookup-server-nc-2
gcc -Wall -g     mdb-lookup-server-nc-1.c   -o mdb-lookup-server-nc-1
ss3912@havana /home/ss3912/cs3157/lab5/part1 $ valgrind --leak-check=yes ./mdb-lookup-server-nc-2
==16386== Memcheck, a memory error detector
==16386== Copyright (C) 2002-2011, and GNU GPL'd, by Julian Seward et al.
==16386== Using Valgrind-3.7.0 and LibVEX; rerun with -h for copyright info
==16386== Command: ./mdb-lookup-server-nc-2
==16386== 
usage: ./mdb-lookup-server-nc-2 <port>
==16386== 
==16386== HEAP SUMMARY:
==16386==     in use at exit: 0 bytes in 0 blocks
==16386==   total heap usage: 0 allocs, 0 frees, 0 bytes allocated
==16386== 
==16386== All heap blocks were freed -- no leaks are possible
==16386== 
==16386== For counts of detected and suppressed errors, rerun with: -v
==16386== ERROR SUMMARY: 0 errors from 0 contexts (suppressed: 2 from 2)

