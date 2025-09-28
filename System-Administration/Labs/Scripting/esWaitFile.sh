#!/bin/bash

function count() {
    local COUNTER=0
    local BOUND=$3
    while sleep 1; do
        if test -f "$2"; then
            "$1" "$2"
        else
            COUNTER++
            if test "$COUNTER" -gt "$BOUND"; then
                exit 1
            fi
        fi
    done
}

function waitfile() {
    test -n "$1" || {
        echo "Il primo parametro deve essere un comando (ls, rm, touch)"
        exit 1
    }

    test -n "$2" || {
        echo "Il secondo parametro deve essere un nome file"
        exit 1
    }

    case "$1" in
    "ls") echo "inserito ls" ;;
    "rm") echo "inserito rm" ;;
    "touch") echo "inserito touch" ;;
    *)
        echo "Comandi accettati: ls, rm, touch"
        exit 1
        ;;
    esac

    case "$3" in
    "force") $1 $2 ;;
    [0-9]) count "$1" "$2" "$3" ;;
    *) count "$1" "$2" "10" ;;
    esac

} # funzione

# main
waitfile "$1" "$2" "$3"