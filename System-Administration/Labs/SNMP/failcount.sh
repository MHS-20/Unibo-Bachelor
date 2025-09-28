#!/bin/bash
snmpwalk -v 1 -c public "$1" NET-SNMP-EXTEND-MIB::nsExtendOutputFull.'"cronlogauth"' | \
    rev | cut -f1 -d: | rev