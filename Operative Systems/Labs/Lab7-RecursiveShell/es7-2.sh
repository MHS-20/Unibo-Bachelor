#!/bin/bash
size=$1
shift
for dir in $*; do
if ! test -d $dir; then
    echo "`pwd`/$dir"
    KB=`stat --format="%s" $dir`
    if  [[ `expr $KB / 1000 ` -lt $size ]]
    then echo `head -c-10 $dir`
    tee -a riepilogo.txt
    fi
else
    cd $dir
    for file in * ; do
       "$0" "$size" "$file"
    done 
fi
done
