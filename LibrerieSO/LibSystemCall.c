#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <sys/wait.h>
#include <time.h>
#define DIM 50

int main(int argc, char **argv)
argc tiene conto anche del nome del file, non parte da zero


//CONTROLLO ARGOMENTI
if ( argc != 4 ) {
        perror("Numero di parametri non valido.");
        exit(EXIT_FAILURE);
    }

//controllo che siano caratteri
     if (1 != strlen(argv[3]) || 1 != strlen(argv[4])) {
        fprintf(stderr, "c1 and c2 must be one character strings\n");
        exit(1);
    }

//controllo che sia percorso assoluto
    if (argv[1][0] != '/') {
        printf("%s deve essere un nome assoluto\n", argv[1]);
        exit(-1);
    }

//RANDOM
srand(time(NULL));   // Initialization, should only be called once.
srand(getpid() * time(NULL));
r = rand(); // % max
r = numbers[rand() % k]; //numbers è vettore


//FORK
pid == 0 per il processo figlio
pid > 0 per il processo padre
getpid()
wait(NULL);

//KILL 
kill(pid, SIGTERM);
kill(0, SIGTERM); //terminare tutti i figli, però term è gestibile, meglio kill
                  // con zero segnale mandato a tutti gli eredi

int kill(intpid, intsig);
pid> 0: l’intero è il pid dell’unico processo destinatario
pid=0: il segnale è spedito a tutti i processi appartenenti al gruppo del mittente
pid<-1: il segnale è spedito a tutti i processi con groupId uguale al valore assoluto di pid
pid== -1: vari comportamenti possibili (Posix non specifica)

//SIGNAL 
if(signal(SIGUSR1, handler)==SIG_ERR)
        perror("Errore nella signal \n");



//APERTURA FILE
fd=open(argv[1], O_RDONLY);
    if(fd < 0){
        perror("Errore nell'apertura del file Fingressi.\n");
        exit(EXIT_FAILURE);
    }

    O_RDONLY (= 0)
    O_WRONLY(= 1) 
    O_APPEND (= 2)
    O_CREAT
    O_TRUNC

    O_CREAT|O_TRUNC|O_WRONLY

//  I/O 
      while(read(fd, &m, sizeof(int)) > 0)
      while(read(pipe[0], &counter, sizeof(int)))
      while (read(in, &c, sizeof(char)) > 0)
      write(pipe[1], &counter, sizeof(int));
      sprintf(buff, "%d ciao ciao", num);

    legth = lseek(fd, 0, SEEK_END); //lunghezza del file


//PIPE 
        int pp[2];
        fd[0]: lato di lettura(receive) della pipe
        fd[1]: lato di scrittura(send) della pipe

        close(pp[1]); //chiudo scrittura
        close(pp[0]); //chiudo lettura

        pp[N][2] //N pipe 
        pipe(pp);

        Se un processo P:
        •tenta una letturada una pipe vuotail cui lato di scrittura è effettivamentechiuso: read ritorna 0 
        •tenta unascritturada una pipe il cui lato di lettura è effettivamente chiuso: writeritorna -1, 
        ed il segnale SIGPIPE viene inviato a P (broken pipe).

        in teoria dovrebbe, però pipe NON scrive il \n, devi usare un altro flag per indicare fine della riga

         if ( pipe(pp) < 0 ) {
        perror("Problems while creating pipe");
        exit(1);
    }

    //EXEC
    execlp("cp", "cp", fileI, dir2,(char*) 0 );
    execlp("rm", "rm", fileI, (char*) 0 );
    execlp("ls", "ls", "-l", dir2, (char*) 0);
    execlp("mv","mv", fileI, newname, (char*) 0);
    execlp("cat", "cat", Fout,(char *) 0);

    //LSEEK
    lseek(int fd, int offset,int origine);
    •fdè il file descriptor del file
    •offsetè lo spostamento (in byte) rispetto all’origine
    •originepuò valere:
    &0: inizio file (SEEK_SET)
    &1: posizione corrente (SEEK_CUR)
    &2 :fine file(SEEK_END)
    •in caso di successo, restituisce un intero positivo che rappresenta la nuova posizione.


//STRUTTURA 2 FIGLI

 pid1=fork();
    if (pid1==0) {
        //CODICE P1        

    }else if(pid1>0){
        pid2=fork();
        if (pid2==0) {
            //CODICE P2
            
        }else if(pid2>0){
            //CODICE P0
            
        }else{
            printf("P0: Impossibile creare processo figlio P2\n");
            exit(3);
        }
        
    }else{
        printf("P0: Impossibile creare processo figlio P1\n");
        exit(3);
    }

    //STRUTTURA N FIGLI (figli con stesso codice)
    //fare exit prima della prossima iterazione, usare delle chiamate a procedura
     for(i=0; i<n; i++)
    {
        pid[i] = fork();
        if ( pid[i] == 0 )
        {
            //figli (con exit!)
        }
        else if ( pid[i] > 0 )
        {
            //padre, codice ripetuto ad ogni iterazione
        }
        else
        {
            perror("Unable to fork");
            exit(EXIT_FAILURE);
        }

    }

    for ( i=0; i<N; i++ ) { // creazione figli
    pid[i] = fork();
    if ( pid[i] == 0 ) { // Eseguito dai figli
        figlio(i);
    }
    else if ( pid[i] > 0 ) { // Eseguito dal padre
        printf("%d: ho creato il figlio %d (PID %d)\n", getpid(),i, pid[i]);
    }
    else {
        perror("Fork error:");
        exit(1);
    }}

//WAIT
    void wait_child() {
    int pid_terminated, status;
    pid_terminated = wait(&status);
    if (pid_terminated < 0)    {
        fprintf(stderr, "P0: errore in wait. \n");
        exit(EXIT_FAILURE);
    }
    if (WIFEXITED(status)) {
        printf("P0: terminazione volontaria del figlio %d con stato %d\n", pid_terminated, WEXITSTATUS(status));
        if (WEXITSTATUS(status) == EXIT_FAILURE) {
            fprintf(stderr, "P0: errore nella terminazione del figlio pid_terminated\n");
            exit(EXIT_FAILURE);
        }
    }
    else if (WIFSIGNALED(status)) {
        fprintf(stderr, "P0: terminazione involontaria del figlio %d a causa del segnale %d\n", pid_terminated,WTERMSIG(status));
        exit(EXIT_FAILURE);
    }
} // wait_child


//ARRAY
Gli operatori * e [] sono intercambiabili.
V = &V[0]
*V = V[0]
*(v+i) = v[i]

- &: operatore estrazione di indirizzo
 ricavare l’indirizzo di una variabile 

- * : operatore di dereferenziamento 
dereferenziare un indirizzo di variabile, denotando la variabile




    /* strtol, simile ad aoi, ma permette di controllare eventuali erroi */
char *endptr = NULL;
    k = strtol(argv[1], &endptr, 10);
    if (*endptr != '\0' || k <= 0 || k > N) {
        fprintf(stderr,
            "Il primo argomento non è un intero positivo valido\n");
        exit(EXIT_FAILURE);
    }

***************************
pid1 = fork();
if(pid1 == 0){ //FILGIO P1

    }
    else {

        pid2 = fork();
        if(pid2 == 0 ){ //FIGLIO P2

        }
        else { //PADRE P0

        }
    }