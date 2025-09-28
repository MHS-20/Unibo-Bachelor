#!/bin/bash
#$3 è il nome della dir da navigare

#verifico che sia un nome assoluto
case $3 in
    /*) ;;    
    *)  echo  $3 non è un nome assoluto
        exit 1;;
    esac

 #controllo parametri
if test $# -lt 3
then echo Errore nel numero di parametri
        exit 2
fi

#controllo direttorio
if [ ! -d $3 ]
  then echo Il  parametro $3 deve essere una directory esistente
       	exit 3
fi

dir=$3 #directory da navigare, punto di partenza
recfile=27giugno2014recursion.sh #nome del file recursion

case "$0" in
    /*) #Path assoluto.
    dir_name=`dirname $0`
    recursive_command="$dir_name"/$recfile
    ;;
    */*)  # Path relativo.
    dir_name=`dirname $0`
    recursive_command="`pwd`/$dir_name/$recfile"
    ;;
    *)
    #comando nel path
    recursive_command=$recfile
    ;;
esac

#innesco la ricorsione
"$recursive_command" "$3" "$1" "$2" #passo dir e altri parametri