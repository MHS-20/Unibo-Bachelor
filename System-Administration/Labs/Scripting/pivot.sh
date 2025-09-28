#!/bin/bash
IP=$1
PROCNAME=$2

# Indice del processo nella tabella
INDEX=$(snmpwalk -v 1 -c supercom $IP 1.3.6.1.4.1.2021.2 | grep $PROCNAME | \
    cut -f3 -d: | cut -d. -f2 | cut -f1 -d" ")


MIN=$(snmpwalk -v 1 -c public $IP 1.3.6.1.4.1.2021.2 | grep "prMin.$INDEX" | rev | cut -f1 -d: | rev)

MAX=$(snmpwalk -v 1 -c public $IP 1.3.6.1.4.1.2021.2 | grep "prMax.$INDEX" | rev | cut -f1 -d: | rev)

TH=$(snmpwalk -v 1 -c public $IP 1.3.6.1.4.1.2021.2 | grep "prCount.$INDEX" | rev | cut -f1 -d: | rev)

echo "$MIN $MAX $TH"