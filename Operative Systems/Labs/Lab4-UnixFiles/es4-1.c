#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <signal.h>
#include <fcntl.h>
#include <unistd.h>

#define DIM 25

typedef struct{
int ruolo;
int id;
int ore;
int minuti;
}marcatura;

int main(int argc, char **argv){
    
    int f, fingressi, fuscite; 
    int i=0, campo = 0, r = 0;  
    char ch, buff[DIM];
    marcatura record; 
    
    f = open(argv[1], O_RDONLY);
    fingressi = open(argv[2], O_RDWR | O_CREAT | O_TRUNC, 00700);
    fuscite = open(argv[3], O_RDWR | O_CREAT | O_TRUNC, 00700);
    
    if(f==0 || fingressi==0 || fuscite==0)
        perror("Errore in apertura file");
    
    while (read(f, &ch, sizeof(char))>0)
    { //leggo e scrivo
        
       if(ch == '\n')
       { //scirvo nel file
           campo = 0; 
           if(buff == 'I')
                write(fingressi, &record, sizeof(marcatura));
           else
                write(fuscite, &record, sizeof(marcatura)); 
    }
        else 
            if (ch == ','){
                
            switch(campo){
                case 0: 
                    record.ruolo = atoi(buff);
                    break;
                case 1: 
                    record.id = atoi(buff);
                    break;
                case 2: 
                    record.ore = atoi(buff);
                    break;
                case 3: 
                    record.minuti = atoi(buff);
                    break;
                }
                    
                    campo++;
                    i = 0; 
                for(r=0; r<DIM-1; r++) //resetto buff
                    buff[r]=""; 
                buff[DIM-1] = '\0';
            }
                else {
                    buff[i] = ch;
                    i++;
                }
    } //fine while
    
    close(f);
    close(fingressi);
    close(fuscite);
        
}
