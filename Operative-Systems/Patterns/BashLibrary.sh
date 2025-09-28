#verifico che sia un nome assoluto
case $i in  
    /*) ;;    
    *)  echo  $i non è un nome assoluto
        exit 1;;
    esac

#controllo parametri ($# non conta $0, è lo zero)
if test $# -ne 3    
then echo Errore nel numero di parametri
        exit 2
fi

#controllo directory
        if [ ! -d $i ]
    then    echo Il  parametro $i deve essere una directory esistente
            exit 3
    fi

#conta le occorrenze di $2 nelle prime 10 righe del file
count=`head "$f" | grep -o "$2" | wc -l`

# modo CORRETTO per ciclare su tutti i file del direttorio d
#CONTARE OCCORRENZE (nocc = num occorrenze)
for d in $*
do
    for i in $d/* 
    do
        if [ -f $i ]
        then
            nocc=`cat $i |grep -o $S |  wc -w` 
            echo il file $i nella directory $d contiene $nocc occorrenze di $S #(verifica)
            if [ $nocc -eq $M ]
            then 
                TOT=`expr $TOT + 1 ` 
                echo $d/$i contiene $M occorrenze di $S
            fi
        fi
    done
done
echo Numero totale di file con $M occorrenze della stringa $S: $TOT

# Ricorsione (invoker)
#è tutto qua, nient'altro 
oldpath=$PATH
PATH=$PATH:`pwd`
recursion.sh "$1" $2 "$3" 
PATH=$oldpath

#CERCARE FILE DA NOME UTENTE
# e contare dimensione in byte
if test -f $dacercare #il file esiste
        then 
            owner=`ls -l $dacercare | cut -d ' ' -f3`
            if test $owner = $utente
            then  
                dim=`cat $dacercare | wc -c`
                echo Il file $nomefile dell’utente $utente nella directory $d contiene $dim caratteri.
            fi
        fi

#UID e GID
#looking at the /etc/passwd file
#the uid is the 3rd field and the gid is the 4th
#sono terzo e quarto anche nella ls -l
groupid=`ls -l $file | cut -d ' ' -f4` #è una stringa
        if test $group = $groupdacercare; then

#stampare max di qualcosa salvato su temp
echo grep "$2" | sort -n | head -n 1 < $temp

#trovare dimensione del file
comando stat per ottenerela dimensione del file e la suarappresentazionein KB
size=`stat --format="%s" "$file"`
size=`expr $size / 1024`
data_modifica=`stat --format="%z" "$file"`

#primi 10 caratteri
head con opzione –c per ottenere i byte
headOfFile=`head -c 10 "$file"`

#copiare in nuova cartella
newdir="$3"/$count
if test ! -d $newdir
then
    mkdir $newdir
fi
cp $file $newdir