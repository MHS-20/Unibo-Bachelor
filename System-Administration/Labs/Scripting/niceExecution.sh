#!/bin/bash

# Se il carico del sistema è inferiore ad una soglia specificata come primo parametro dello script, lancia il comando specificato come secondo parametro.
# Altrimenti, con at, rischedula il test dopo 2 minuti, e procede così finchè non riesce a lanciare il comando.

function test() {
    CARICO=$(uptime | cut -f12 -d' ' | sed 's/[,]//g') # carico del sistema nell'ultimo minuto, sed serve ad eliminare le virgole per fare il confronto numerico

    echo $1 $2 $3
    echo $CARICO
    echo $COUNTER

    # Condizione per interrompere il loop
    if [ "$CARICO" -lt "$2"]; then
        echo "eseguo il comando"
        "$1"
        exit 1
    elif [ "$COUNTER" -lt "$3"]; then
        COUNTER++
    else
        exit 1
    fi

    echo "test $1 $2 $3" | at now + 1 minutes

}

# Definizione delle opzioni accettate
short_options="n:s:" # -n max_tentativi -s soglia
parsed_options=$(getopt -o $short_options -n "$0" -- "$@")

# Verifica degli errori di parsing
if [ $? -ne 0 ]; then
    echo "Errore nel parsing delle opzioni. Terminazione dello script."
    exit 1
fi

# Reset degli argomenti in base alle opzioni parsate
eval set -- "$parsed_options"

COUNTER=0
echo $1 $2 $3

case "$2" in
-n) test $1 $3 $2 ;;
-s) test $1 $2 $3 ;;
esac