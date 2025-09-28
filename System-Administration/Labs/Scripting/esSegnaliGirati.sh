#!/bin/bash
report() {
    echo $(date) osservate $TOT nuove righe
    TOT=0
}
echo $BASHPID >/tmp/logwatch.pid
TOT=0
trap report USR1

while read R; do
    TOT=$((TOT + 1))
done < <(tail -n +0 -f "$1") 
# usando la pipeline invece della ridirezione non funziona
# perché la variabile TOT viene incrementata nella subshell del while
# quindi l'handler non può accedere al valore di TOT perché è un contesto diverso
