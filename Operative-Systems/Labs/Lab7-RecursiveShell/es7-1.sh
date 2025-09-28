#!/bin/bash
if ! test -d "$2"; then
    echo "`pwd`/$2"
    KB=`stat --format="%s" $2`
    if  [[ `expr $KB / 1000 ` -lt $1 ]]
    then echo `head -c-10 $2`
    fi
else
    cd "$2"
    for file in * ; do
       "$0" "$1" "$file"
    done 
fi
