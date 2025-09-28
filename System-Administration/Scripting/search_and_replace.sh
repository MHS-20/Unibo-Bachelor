#!/bin/bash
ldapsearch -x -LLL -H ldap://10.2.2.17 -b "ou=People,dc=labammsis" -s sub |
    grep "dn: u\|uid:\|cn:\|sn:\|mail:" | cut -f2 -d: | paste -d' ' - - - - - |
    while read dn cn sn mail uid; do
        echo "Utente con uid: $uid"

        if [ -z "$cn" ]; then
            echo "\\nInserisci CN: di $uid"
            read cn
        fi

        if [ -z "$sn" ]; then
            echo "\\nInserisci SN: di $uid"
            read sn
        fi

        if [ -z "$mail" ]; then
            echo "\\nInserisci CN: di $uid"
            read mail20
        fi

        {
            echo dn: $dn
            echo changetype: modify
            echo replace: gecos
            echo gecos: $cn $sn $mail
        } | ldapmodify -x -D cn=admin,dc=labammsis -h 10.2.2.17 -w ciro # specifico IP agent se lo faccio dal manager
    done

#IFS=$\n
#myVar=$(</dev/stdin)
