#!/bin/bash

#$1 = dir da navigare

cd "$1"
for file in * ; do
	if [[ -f "$file" ]] ; then
		#operazioni sul file
		fi
	elif [[ -d "$file" ]] ; then
        echo "Starting recursion on dir `pwd`/$file"
        "$0" "$file" #"$2" "$3" ... 
        #invoco questo file con next dir poi gli altri argomenti
    fi
done