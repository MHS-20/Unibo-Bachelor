#include <fcntl.h>
#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>

#define DIM 16

int main(int argc, char **argv){
    
    int pp[2], pid, fd, c=0, status;
    char codice[DIM], ch, out[256];
    
    if (pipe(pp)<0)
        exit(EXIT_FAILURE);
    
    pid=fork();
    
    if(pid==0){ //codice figlio 
        close(pp[0]);
        
        fd = open(argv[1], O_RDONLY);
            if(fd < 0)
                perror("Errore in apertura");
         
            
            
        do{ 
            //codice[DIM] = '\0';
            read(fd, codice, sizeof(char) * DIM);
            write(pp[1], codice, sizeof(char) * DIM);
           // read(fd, &ch, sizeof(char));
            printf("\n letto" );
        }while(read(fd, &ch, sizeof(char)) > 0);
        
        printf("\n uscito " );
        
        close(pp[1]);
        close(fd);
        exit(EXIT_SUCCESS);
        
    } else { //codice padre
        close(pp[1]);
        
        while(read(pp[0], codice, sizeof(char) * DIM)>0)
            c++;
        //esce dal ciclo quando la pipe è vuota ed il figlio l'ha chiusa
        
       sprintf(out, "\nNumero di parole lette: %d\n", c);
        write(1, out, sizeof(char) * strlen(out));
        
        wait(&status);
        close(pp[0]);
        exit(EXIT_SUCCESS);
    }
    
}
