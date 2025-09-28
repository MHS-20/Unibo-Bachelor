#!/bin/bash
read path
if ! test -f $path
then 
	echo file non esistente
	exit
fi
if ! test -r $path
then 
	echo diritti di lettura mancanti
	exit
fi
grep -i $1 $path | sort -r > results_`whoami`.out
echo completato
exit