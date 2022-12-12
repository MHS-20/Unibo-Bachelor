#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#define MAX_STRING_LENGTH 256
#define NUM_FILES 4

int main(int argc, char *argv[]) {
    int   fd[NUM_FILES], written, n = 0, i; 
    char  riga[MAX_STRING_LENGTH]; 
	char *file_out[NUM_FILES];

	if(argc > NUM_FILES + 1){ //+1 perche' primo argomento e' nome del file
	perror("P0: Numero massimo di file raggiunto");
        	return EXIT_FAILURE;
	}

	//apro tutti i file
    for(i = 0; i < argc -1 ; i++){ //-1 perche' primo argomento e' nome del file
        file_out[i] = argv[i + 1];
        fd[i] = open(file_out[i], O_APPEND|O_CREAT, 00640);
        
        if (fd[i]  < 0){
		perror("P0: Impossibile creare/aprire il file");
		exit(2);
	}
    }

    printf("Inserisci le nuove righe, o EOF [CTRL^D] per terminare\n");

    while (gets(riga)) {
      printf("%d", riga[0]);
        riga[strlen(riga)]     = '\n'; // aggiungo il fine linea
        riga[strlen(riga) + 1] = '\0'; // aggiungo 0 binario per permettere una corretta strlen()
		n = atoi(riga); //numero del file su cui scrivere
        
		if(n < 0 || n > argc ){
			perror("P0: Inserita riga per file non dichiarato");
            printf("File numero: %d", n);
        	return EXIT_FAILURE;
		}

        written = write(fd[n - 1], riga, strlen(riga));
        if (written < 0) {
            perror("P0: errore nella scrittura sul file");
			        	return EXIT_FAILURE;
        }
    }

	//chiudo tutti i file
	for(i = 0; i <  argc; i++){
    close(fd[i]);
	}

    return EXIT_SUCCESS;
}