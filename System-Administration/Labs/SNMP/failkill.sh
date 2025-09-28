#!/bin/bash
options=":f:s:"

parsed_options=$(getopt "$options" "$@")

# Controlla se getopt ha restituito un errore
if [ $? -ne 0 ]; then
    echo "Errore: Argomenti non validi."
    exit 1
fi

eval set -- "$parsed_options"

# Salvo valori flag
while true; do
    case "$1" in
    -f)
        file="$2"
        shift 2
        ;;
    -s)
        soglia="$2"
        shift 2
        ;;
    --)
        shift
        break
        ;;
    *)
        echo "Opzione non valida: $1"
        exit 1
        ;;
    esac
done

# Controllo file
if [ ! -f "$file" ]; then
    echo "File non trovato: $file"
    exit 1
fi

# Leggo il file linea per linea
while read -r line; do
    count=$(./failcount.sh "$line")

    echo $line $count $soglia

    if [ "$count" -gt "$soglia" ]; then
        echo "$line Soglia superata\n\n"
        ssh vagrant@$line vagrant halt
    fi
done <"$file"
