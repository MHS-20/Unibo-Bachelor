#!/bin/bash
declare -A pids

function handler() {
    echo "Ricevuto segnale Ctrl+C o Ctrl+D"
    for PID in ${!A[@]}; do
        kill $PID
    done
    exit 0
}

trap handler SIGINT SIGQUIT

for CMD in "$*"; do
    "$CMD" &
    pids["$!"]=$CMD
done

echo "${pids[*]}"
echo ${!pids[@]}

while sleep 3; do
    CHECK=0 # flag per terminazione quando hanno tutti terminato (oppure wait / watch)
    for VAR in ${!pids[@]}; do

        echo $VAR
        `ps -o state= -p $VAR` # non va 

        STATO=$(ps -o state= -p $VAR)
        PID=$(ps -o pid= -p "$VAR")
        NAME=$(ps -o cmd= -p "$VAR")

        #PID=`ps hp $VAR | awk '{ print $1 }'`
        #STATO=`ps hp $VAR | awk '{ print $3 }'`
        #NAME=`ps hp $VAR | awk '{ print $5 }'`

        echo "$PID"
        echo "$STATO"

        if test ${pids[$PID]}!=$NAME; then
            echo "errore: $NAME non corrisponde a ${pids["$PID"]}"
        fi

        if test -z "$STATO"; then
            echo "$NAME con PID ${pids[$PID]} ha terminato"
        else
            CHECK=1 # non tutti i comandi hanno terminato, quindi non deve terminare
        fi

        echo "PID $PID STATO $STATO COMANDO $NAME" >>log
    done # for

    if [ $CHECK -eq 0 ]; then
        echo "Tutti hanno terminato, quindi termino"
        exit 1
    fi
done # while
