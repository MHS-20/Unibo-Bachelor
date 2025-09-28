#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <signal.h>
#include <fcntl.h>
#include <unistd.h>

#define numChild 2
#define DIM 25


typedef struct{
//int ruolo;
int id;
int ore;
int minuti;
}marcatura;

int main(int argc, char **argv){
    
    int pid[numChild];
    int status; 
    int f, fp1, fp2;
    int i=0, campo=0, r=0;
    char buff[DIM], ch; 
    marcatura record;
    
    pid[0] = fork();
    
    if( pid[0] == 0) //codice P1
    {
        f = open(argv[1], O_RDONLY);
        fp1 = open("./temp1", O_RDWR | O_CREAT | O_TRUNC, 00700);

        while (read(f, &ch, sizeof(char))>0){
        if(ch == '\n'){
            write(fp1, &record, sizeof(marcatura));
            exit(0);}
        else 
            if (ch == ','){
            switch(campo){
                case 0: //letto il ruolo
                    if(buff != atoi(argv[3]))
                        lseek(f, +sizeof(marcature) , SEEK_CUR); //salto riga se ruolo è diverso da quello che cerco;
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
        exit(1); //fine codice P1
        
    } else 
        if( pid[0] > 0){
            pid[1] = fork();
             if( pid[1] == 0){ //codice P2
                f = open(argv[2], O_RDONLY);
                fp2 = open("./temp2", O_RDWR | O_CREAT | O_TRUNC, 00700);
                lseek(f, 0, SEEK_END);

        while (read(f, &ch, sizeof(char))>0){
        if(ch == '\n'){
            write(fp2, &record, sizeof(marcatura));
            exit(0);}
        else 
            if (ch == ','){
                lseek(f, -sizeof(int) , SEEK_CUR);
            switch(campo){
                case 3: //letto il ruolo
                    if(buff != atoi(argv[3]))
                        lseek(f, -sizeof(marcature) , SEEK_CUR); //salto riga se ruolo è diverso da quello che cerco;
                case 2: 
                    record.id = atoi(buff);
                    break;
                case 1: 
                    record.ore = atoi(buff);
                    break;
                case 0: 
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
        exit(1); //fine codice P1
            }
        } else { //codice P0
            wait(&status);
            //...
            
        }
}

 




