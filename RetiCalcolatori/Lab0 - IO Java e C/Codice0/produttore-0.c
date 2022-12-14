#include <stdio.h>
#include <fcntl.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#define MAX_STRING_LENGTH 256

// produttore, e' un filtro
int main(int argc, char* argv[]){
	int fd, readValues, bytes_to_write, written, righe, i;
	char *file_out;
	char riga[MAX_STRING_LENGTH], buf[MAX_STRING_LENGTH];
	
	//controllo numero argomenti
	if (argc != 2){ 
		perror(" numero di argomenti sbagliato"); exit(1);
	} 
	
    file_out = argv[1];	
	fd = open(file_out, O_WRONLY|O_CREAT|O_TRUNC, 00640);
	if (fd < 0){
		perror("P0: Impossibile creare/aprire il file");
		exit(2);
	}
	
	printf("Inserire riga, EOF per terminare\n");
//     
    readValues = gets (riga);
	
    while(readValues > 0){
        riga[strlen(riga)+1]='\0';  
		riga[strlen(riga)]='\n';  
		
		written = write(fd, riga, strlen(riga));
		if (written < 0){
			perror("P0: errore nella scrittura sul file");
			exit(3);
		}
		
        printf("Inserire riga, EOF per terminare\n");
        readValues = gets (riga);
    }
    
	close(fd);
}
