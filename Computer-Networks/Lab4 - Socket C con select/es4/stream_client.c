/* Client per richiedere l'invio di un file (get, versione 1) */

#include <fcntl.h>
#include <netdb.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <unistd.h>

#define DIM_BUFF 100
#define LENGTH_FILE_NAME 20
#define LENGTH_DIR_NAME 20

int main(int argc, char *argv[])
{
    int sd, nread, nwrite, port, i = 0;
    char ch;
    char buff[DIM_BUFF], nome_dir[LENGTH_DIR_NAME], file_name[LENGTH_FILE_NAME];
    struct hostent *host;
    struct sockaddr_in servaddr;

    /* CONTROLLO ARGOMENTI ---------------------------------- */
    if (argc != 3)
    { // localhost, porta
        printf("Error:%s serverAddress serverPort\n", argv[0]);
        exit(1);
    }
    printf("Client avviato\n");

    /* PREPARAZIONE INDIRIZZO SERVER ----------------------------- */
    memset((char *)&servaddr, 0, sizeof(struct sockaddr_in));
    servaddr.sin_family = AF_INET;
    host = gethostbyname(argv[1]);
    if (host == NULL)
    {
        printf("%s not found in /etc/hosts\n", argv[1]);
        exit(2);
    }

    // porta da argv
    nread = 0;
    while (argv[2][nread] != '\0')
    {
        if ((argv[2][nread] < '0') || (argv[2][nread] > '9'))
        {
            printf("Secondo argomento non intero\n");
            exit(2);
        }
        nread++;
    }
    port = atoi(argv[2]);
    if (port < 1024 || port > 65535)
    {
        printf("Porta scorretta...");
        exit(2);
    }

    servaddr.sin_addr.s_addr = ((struct in_addr *)(host->h_addr))->s_addr;
    servaddr.sin_port = htons(port);

    /* CREAZIONE E CONNESSIONE SOCKET (BIND IMPLICITA) ----------------- */
    sd = socket(AF_INET, SOCK_STREAM, 0);
    if (sd < 0)
    {
        perror("apertura socket ");
        exit(3);
    }
    printf("Creata la socket sd=%d\n", sd);

    if (connect(sd, (struct sockaddr *)&servaddr, sizeof(struct sockaddr)) < 0)
    {
        perror("Errore in connect");
        exit(4);
    }
    printf("Connect ok\n");

    /* CORPO DEL CLIENT: */
    /* ciclo di accettazione di richieste di file ------- */
    printf("Nome del direttorio da richiedere: ");

    while (gets(nome_dir))
    {
        if (write(sd, nome_dir, (strlen(nome_dir) + 1)) < 0)
        {
            perror("write");
            printf("Nome del direttorio da richiedere: ");
            continue;
        }
        printf("Richiesta del direttorio %s inviata... \n", nome_dir);

        // ciclo lettura : leggo nomi dei file a carattere e li stampo uno alla volta
        file_name[0] = ' ';
        while (read(sd, &ch, sizeof(char)) > 0)
        {
            if(ch == '\n')
            break; 

            if (ch == ';') // fine nome file
            { 
                i = 0;
                printf("Nome file: %s", file_name); 
            }
            else
            {
                file_name[i] = ch;
                i++;
            }
        }
        printf("Nome del direttorio da richiedere: ");

    } // while
    printf("Chiudo connessione\n");
    close(sd);
    printf("\nClient: termino...\n");
    exit(0);
}