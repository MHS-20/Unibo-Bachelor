#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#define MAX_STRING_LENGTH 256
#define NUM_FILES 4

int main(int argc, char *argv[]) {
    char *prefix = argv[1];
    char  err[MAX_STRING_LENGTH], buffer[MAX_STRING_LENGTH];

    int  ret, found, nread, i, j; 
    int fd, pid[NUM_FILES], tempfd; 
    char read_char;
    FILE * src, * target; 

    // CHECK ARGS
    if(argc > NUM_FILES + 2){ //+2 perché c'è nome del file e la stringa filtro
	perror("P0: Numero massimo di file raggiunto");
        	return EXIT_FAILURE;
	}

    // FILTER STRING
    found = 0;

    for ( i=0; i<argc - 2; i++ ) { // creazione figli
    pid[i] = fork();
    if ( pid[i] == 0 ) { // Eseguito dai figli

    src = fopen("tempchild.txt", "r");
    target = fopen(argv[i + 2], "w");

    fd = open(argv[i + 2], O_RDONLY);
        if (fd < 0) {
            perror("P0: Impossibile aprire un file");
            return EXIT_FAILURE;
        }

    tempfd = creat("tempchild.txt", O_WRONLY);
        if (tempfd < 0) {
            perror("P0: Impossibile creare file temporaneo");
            return EXIT_FAILURE;
        }

    while ((nread = read(fd, &read_char, sizeof(char)))) { // till EOF
        if (nread < 0) {
            perror("Errore lettura file!");
            return EXIT_FAILURE;
        }

        for (j = 0; j < strlen(prefix); j++) {
            if (read_char == prefix[j]) {
                found = 1;
            }
        }

        if (!found) {
            printf("%c", read_char);
            fputc(read_char, src);
        }

        found = 0;
    } //fine while

    while ((read_char = fgetc(src)) != EOF)
    fputc(read_char, target);

    printf("File copied successfully.\n");
    close(fd); 
    close(tempfd);
    fclose(target);
    fclose(src);
    exit(EXIT_SUCCESS); //termine del figlo

    } //fine figli (if)
    else if ( pid[i] > 0 ) { // Eseguito dal padre
        printf("%d: ho creato il figlio %d (PID %d)\n", getpid(),i, pid[i]);
    }
    else {
        perror("Fork error:");
        exit(1);
    }
    } //fine for figli  
    
    return EXIT_SUCCESS; //fine padre
}