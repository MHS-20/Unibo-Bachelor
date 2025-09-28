#include <dirent.h>
#include <errno.h>
#include <fcntl.h>
#include <netdb.h>
#include <netinet/in.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

#define DIM_BUFF 100
#define DIM_WORD 25
#define LENGTH_FILE_NAME 20
#define max(a, b) ((a) > (b) ? (a) : (b))

typedef struct
{
    char file_name[LENGTH_FILE_NAME];
    char word[DIM_WORD];
} Request; // per UDP

/********************************************************/
void gestore(int signo)
{
    int stato;
    printf("esecuzione gestore di SIGCHLD\n");
    wait(&stato);
}
/********************************************************/
void resetString(char *word, int dim)
{
    int j = 0;
    for (j = 0; j < dim; j++)
    {
        word[j] = '\0';
    }
}
/********************************************************/

int main(int argc, char **argv)
{
    int listenfd, connfd, udpfd, fd_file, nready, maxfdp1;
    const int on = 1;
    char zero = 0, buff[DIM_BUFF], nome_file[LENGTH_FILE_NAME], nome_dir[LENGTH_FILE_NAME];
    fd_set rset;
    char ch, word[DIM_WORD], flag = ';', endNameList = '\n';
    int len, nread, nwrite, num, port, i, fd;
    struct sockaddr_in cliaddr, servaddr;
    Request req;

    /* CONTROLLO ARGOMENTI ---------------------------------- */
    if (argc != 2)
    { // port
        printf("Error: %s port\n", argv[0]);
        exit(1);
    }

    // porta
    nread = 0;
    while (argv[1][nread] != '\0')
    {
        if ((argv[1][nread] < '0') || (argv[1][nread] > '9'))
        {
            printf("Terzo argomento non intero\n");
            exit(2);
        }
        nread++;
    }
    port = atoi(argv[1]);
    if (port < 1024 || port > 65535)
    {
        printf("Porta scorretta...");
        exit(2);
    }

    /* INIZIALIZZAZIONE INDIRIZZO SERVER ----------------------------------------- */
    memset((char *)&servaddr, 0, sizeof(servaddr));
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = INADDR_ANY;
    servaddr.sin_port = htons(port);

    printf("Server avviato\n");

    /* CREAZIONE SOCKET TCP ------------------------------------------------------ */
    listenfd = socket(AF_INET, SOCK_STREAM, 0);
    if (listenfd < 0)
    {
        perror("apertura socket TCP ");
        exit(1);
    }
    printf("Creata la socket TCP d'ascolto, fd=%d\n", listenfd);

    if (setsockopt(listenfd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof(on)) < 0)
    {
        perror("set opzioni socket TCP");
        exit(2);
    }
    printf("Set opzioni socket TCP ok\n");

    if (bind(listenfd, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0)
    {
        perror("bind socket TCP");
        exit(3);
    }
    printf("Bind socket TCP ok\n");

    if (listen(listenfd, 5) < 0)
    {
        perror("listen");
        exit(4);
    }
    printf("Listen ok\n");

    /* CREAZIONE SOCKET UDP ------------------------------------------------ */
    udpfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (udpfd < 0)
    {
        perror("apertura socket UDP");
        exit(5);
    }
    printf("Creata la socket UDP, fd=%d\n", udpfd);

    if (setsockopt(udpfd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof(on)) < 0)
    {
        perror("set opzioni socket UDP");
        exit(6);
    }
    printf("Set opzioni socket UDP ok\n");

    if (bind(udpfd, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0)
    {
        perror("bind socket UDP");
        exit(7);
    }
    printf("Bind socket UDP ok\n");

    /* gestore per figli zombie */
    signal(SIGCHLD, gestore);

    /* settaggio maschera */
    FD_ZERO(&rset);
    maxfdp1 = max(listenfd, udpfd) + 1;

    /* CICLO DI RICEZIONE EVENTI DALLA SELECT */
    for (;;)
    {
        FD_SET(listenfd, &rset);
        FD_SET(udpfd, &rset);

        if ((nready = select(maxfdp1, &rset, NULL, NULL, NULL)) < 0)
        {
            if (errno == EINTR)
                continue;
            else
            {
                perror("select");
                exit(8);
            }
        }

        /* GESTIONE RICHIESTE TCP (nomi file nei direttori) */
        if (FD_ISSET(listenfd, &rset))
        {
            printf("Ricevuta richiesta per un direttorio\n");
            len = sizeof(struct sockaddr_in);
            if ((connfd = accept(listenfd, (struct sockaddr *)&cliaddr, &len)) < 0)
            {
                if (errno == EINTR)
                    continue;
                else
                {
                    perror("accept");
                    exit(9);
                }
            }

            if (fork() == 0)
            { /* processo figlio che serve la richieste (con connesione) */
                close(listenfd);
                printf("Dentro il figlio, pid = %i\n", getpid());

                DIR *dir1;
                DIR *dir2;
                struct dirent *dd1;
                struct dirent *dd2;
                int count = 0;

                for (;;)
                {
                    if ((read(connfd, &nome_dir, sizeof(nome_dir))) <= 0)
                    {
                        perror("read");
                        break;
                    }

                    printf("Richiesta per cartella:  %s\n", nome_dir);
                    dir1 = opendir(nome_dir);

                    if (dir1 == NULL)
                    {
                        printf("Direttorio inestistente\n");
                        write(connfd, &endNameList, sizeof(endNameList));
                        continue;
                    }

                    // navigo direttorio
                    // readdir legge uno alla volta gli elementi dentro al cartella
                    while ((dd1 = readdir(dir1)) != NULL) // primo livello
                    {
                        if (strcmp(dd1->d_name, ".") != 0 && strcmp(dd1->d_name, "..") != 0)
                        {
                            printf("Trovato il file/cartella %s\n", dd1->d_name);
                            count++;

                            printf("Provo ad aprire %s\n", dd1->d_name);
                            if ((dir2 = opendir(dd1->d_name)) != NULL) // secondo livello
                            {
                                printf("Trovata la cartella interna %s\n", dd1->d_name);
                                while ((dd2 = readdir(dir2)) != NULL)
                                {
                                    printf("Trovato il file %s\n", dd2->d_name);
                                    write(connfd, &dd2->d_name, strlen(dd2->d_name)); // scrivo nome
                                    write(connfd, &flag, sizeof(flag));               // fine singolo nome
                                }
                                closedir(dir2);
                            }
                            else
                            {
                                printf("%s non è una cartella \n", dd1->d_name);
                            }
                        }
                    }
                    write(connfd, &endNameList, sizeof(endNameList)); // fine lista di nomi
                    closedir(dir1);
                } // for
                printf("Figlio %i: chiudo connessione e termino\n", getpid());
                close(connfd);
                exit(0);
            } // figlio

            /* padre chiude la socket dell'operazione */
            close(connfd);
        } /* fine gestione richieste con connessione (contare file dentro dir) */

        /* GESTIONE RICHIESTE UDP (eliminazione parole in file) */
        if (FD_ISSET(udpfd, &rset))
        {
            printf("Server: ricevuta richiesta per un file\n");
            len = sizeof(struct sockaddr_in);
            if (recvfrom(udpfd, &req, sizeof(req), 0, (struct sockaddr *)&cliaddr, &len) < 0)
            {
                perror("recvfrom");
                continue;
            }

            printf("Richiesta eliminazione parole in file %s\n", req.file_name);
            printf("Parola da eliminare %s\n", req.word);
            // file in cui eliminare le parole
            fd_file = open(req.file_name, O_RDONLY);
            if (fd_file < 0)
            {
                perror("Errore nell'apertura del file .\n");
                exit(EXIT_FAILURE);
            }

            printf("Aperto il file richiesto\n");
            // file nuovo in cui scrivere
            fd = creat("temp", 0777);
            if (fd < 0)
            {
                perror("Errore nella creazione del file temporaneo .\n");
                exit(EXIT_FAILURE);
            }

            // problemi
            // al primo giro con una parola singola funziona
            // al secondo giro non va più
            // con una parola che compare più volte non va prorpio
            // forse in word manca il terminatore quindi la cmp non funziona

            // elimino le parole
            i = 0;
            num = 0;
            while ((nread = read(fd_file, &ch, sizeof(char))) > 0)
            {
                printf("Leggo file richiesto %c\n", ch);
                if (ch == ' ' || ch == '\n')
                {
                    printf("Ulitma parola letta: %s\n", word);
                    // i = 0;
                    if (strcmp(word, req.word) != 0) // parole diverse
                    {
                        word[i] = ' ';                        // evito che le parole siano tutte attaccate
                        write(fd, &word, sizeof(char) * ++i); // scrivo sul temporaneo
                    }
                    else
                    {
                        printf("Una parola trovata, la elimino\n");
                        num++;
                    }
                    i = 0;
                    resetString(word, DIM_WORD);
                }
                else
                {
                    word[i] = ch;
                    i++;
                }
            } // fine scrittura su temp

            printf("Invio risposta, numero: %d", num);

            unlink(req.file_name);
            rename("temp", req.file_name);

            if (sendto(udpfd, &num, sizeof(num), 0, (struct sockaddr *)&cliaddr, len) < 0)
            {
                perror("sendto");
                continue;
            }

        } /* fine gestione richieste UDP */
    }     /* ciclo for della select */
}