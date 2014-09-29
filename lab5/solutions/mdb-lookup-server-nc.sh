#!/bin/sh

mkfifo mypipe-$$

nc -l $1 < mypipe-$$ | /home/jae/cs3157-pub/bin/mdb-lookup-cs3157 > mypipe-$$

rm -f mypipe-$$
