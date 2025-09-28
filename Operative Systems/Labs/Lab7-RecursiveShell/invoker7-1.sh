#!/bin/bash
#ADATTATO IL CODICE DELL'ESEMPIO AL MIO ES7-1

# Controllo argomenti
if [ $# -ne 2 ] ; then
    echo -e "Sintassi: $0 dir size" >&2
    exit 1
fi

if ! [[ -d "$1" ]] ; then
    echo -e "Il file $1 non è una directory" >&2
    exit 1
fi 

if [[ "$0" = /* ]] ; then

    dir_name=`dirname "$0"`
    recursive_command="$dir_name/es7-1.sh"
elif [[ "$0" = */* ]] ; then
    dir_name=`dirname "$0"`
    recursive_command="`pwd`/$dir_name/es7-1.sh"
else 
    recursive_command=es7-1.sh
fi

"$recursive_command" "$2" "$1"
