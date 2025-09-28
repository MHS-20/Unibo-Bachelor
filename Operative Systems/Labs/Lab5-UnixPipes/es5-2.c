#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>

#define DIM 17


int main(int argc, char **argv){
    
    int pp[2], pid, c=0, status;
    int fine = 0, nread;
    char codice[DIM], ch, out[256];
        
    if (pipe(pp)<0)
        exit(EXIT_FAILURE);
    
    pid=fork();
    
    if(pid==0){ //codice figlio 
        close(pp[0]);
            
        close(1);
        dup(pp[1]);
        close(pp[1]);
        
       execlp("wc", "wc", "-w", argv[1], (char*)0);
       perror("Errore in exec");

    } else { //codice padre
        close(pp[1]);
        
        read(pp[0], &ch, 1);
        c = atoi(&ch);
        
        sprintf(out, "\nNumero di parole lette: %d\n", c);
        write(1, out, sizeof(char) * strlen(out));
        
        wait(&status);
        close(pp[0]);
        exit(EXIT_SUCCESS);
    }
        
    }
