#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>
#include <signal.h>
#include <sys/wait.h>
#include <time.h>

void handler(int);

int main(int argc, char **argv){

	int fin, fout, n, pp[2]; 
	int pid1, pid2, status; 
	char ch, mex[3], flag; 

	if ( argc != 4 ) {
        perror("Numero di parametri non valido.");
        exit(EXIT_FAILURE);
    }

    if(signal(SIGUSR1, SIG_IGN)==SIG_ERR){
        perror("Errore nella signal \n");
        exit(EXIT_FAILURE);
    }

    n = atoi(argv[2]); 
    flag = 'a';

    if ( pipe(pp) < 0 ) {
        perror("Problems while creating pipe");
        exit(1);
    }

pid1 = fork();
if(pid1 == 0){ //FILGIO P1

	close(pp[0]);
	fin=open(argv[1], O_RDONLY);
    if(fin < 0){
        perror("Errore nell'apertura del file fin.\n");
        exit(EXIT_FAILURE);
    }

    printf("\nSono P1, inizio a leggere");
    while(read(fin, &ch, sizeof(char)) > 0){
    	lseek(fin, n, SEEK_CUR);
    	write(pp[1], &ch, sizeof(char));
    	printf("\nSono P1, letto %c", ch);
    }

    close(pp[1]);
    close(fin);
    exit(EXIT_SUCCESS);

    }
    else {
    	printf("\nCreo secondo figlio");
        pid2 = fork();
        if(pid2 == 0 ){ //FIGLIO P2
        	close(pp[0]);
        	close(pp[1]);
        	printf("\nSono P2, chiudo la pipe e aspetto");
        	pause();
        	execlp("cut", "cut", argv[3], (char*)0 );
   		}
        else { //PADRE P0
        	close(pp[1]);
        	fout=open(argv[1], O_WRONLY);
        	
    		if(fout < 0){
        		perror("Errore nell'apertura del file fout.\n");
        		exit(EXIT_FAILURE);
			}

	    while(read(pp[0], &ch, sizeof(char)) > 0){
	    	sprintf(mex, "%c%c", ch, flag); //aggiungo A davanti a ch
	    	write(fout, &mex, sizeof(mex));
	    	printf("\nSono P0, scrivo sul file");
        }

        close(pp[0]);
        close(fout);

      	printf("\nSono P0, segnalo P2");
      	kill(pid2, SIGUSR1); 

      	wait(&status);
      	wait(&status);
      	printf("\nFigli terminati");
      	exit(EXIT_SUCCESS);
    }
}
}