#!/bin/bash

test -d "$1" || {
    echo "Il primo parametro deve essere una cartella esistente"
    exit 1
}

(ls -R "$1" | egrep '\..+$' | rev | cut -f1 -d. | rev | sort | uniq -c | sort -nr) | while read NUM EXT; do
    for ARGS in "${@:2}"; do
        if test "$ARGS" = "$EXT"; then
            echo "$NUM" "$EXT"
        fi

        # echo "$NUM" "$EXT"
    done
done
