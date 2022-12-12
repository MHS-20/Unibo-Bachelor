/* Server che fornisce la valutazione di un'operazione tra due interi */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <errno.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <string.h>
#define DIM 50

/*Struttura di una richiesta*/
/********************************************************/
typedef struct
{
	char file_name[DIM];
} Request;
/********************************************************/

int main(int argc, char **argv)
{
	int sd, port, len, ris, fd, length, maxlength, num1, num2;
	char ch;
	const int on = 1;
	struct sockaddr_in cliaddr, servaddr;
	struct hostent *clienthost;
	Request *req = (Request *)malloc(sizeof(Request));

	/* CONTROLLO ARGOMENTI ---------------------------------- */
	if (argc != 2)
	{ // solo porta
		printf("Error: %s port\n", argv[0]);
		exit(1);
	}
	else
	{
		num1 = 0;
		while (argv[1][num1] != '\0')
		{
			if ((argv[1][num1] < '0') || (argv[1][num1] > '9'))
			{
				printf("Secondo argomento non intero\n");
				printf("Error: %s port\n", argv[0]);
				exit(2);
			}
			num1++;
		}
		port = atoi(argv[1]);
		if (port < 1024 || port > 65535)
		{
			printf("Error: %s port\n", argv[0]);
			printf("1024 <= port <= 65535\n");
			exit(2);
		}
	}

	/* INIZIALIZZAZIONE INDIRIZZO SERVER ---------------------------------- */
	memset((char *)&servaddr, 0, sizeof(servaddr));
	servaddr.sin_family = AF_INET;
	servaddr.sin_addr.s_addr = INADDR_ANY;
	servaddr.sin_port = htons(port);

	/* CREAZIONE, SETAGGIO OPZIONI E CONNESSIONE SOCKET -------------------- */
	sd = socket(AF_INET, SOCK_DGRAM, 0);
	if (sd < 0)
	{
		perror("creazione socket ");
		exit(1);
	}
	printf("Server: creata la socket, sd=%d\n", sd);

	if (setsockopt(sd, SOL_SOCKET, SO_REUSEADDR, &on, sizeof(on)) < 0)
	{
		perror("set opzioni socket ");
		exit(1);
	}
	printf("Server: set opzioni socket ok\n");

	if (bind(sd, (struct sockaddr *)&servaddr, sizeof(servaddr)) < 0)
	{
		perror("bind socket ");
		exit(1);
	}
	printf("Server: bind socket ok\n");

	/* CICLO DI RICEZIONE RICHIESTE ------------------------------------------ */
	for (;;)
	{
		len = sizeof(struct sockaddr_in);
		if (recvfrom(sd, req, sizeof(Request), 0, (struct sockaddr *)&cliaddr, &len) < 0)
		{
			perror("recvfrom ");
			continue;
		}

		printf("Operazione richiesta: %s\n", req->file_name);
		clienthost = gethostbyaddr((char *)&cliaddr.sin_addr, sizeof(cliaddr.sin_addr), AF_INET);
		if (clienthost == NULL)
			printf("client host information not found\n");
		else
			printf("Operazione richiesta da: %s %i\n", clienthost->h_name, (unsigned)ntohs(cliaddr.sin_port));

		// RICERCO PAROLE
		fd = open(req->file_name, O_RDONLY);
		if (fd < 0)
		{
			perror("Errore nell'apertura del file.\n");
			exit(EXIT_FAILURE);
		}

		// ciclo lettura file
		length = 0;
		maxlength = 0;
		while (read(fd, &ch, sizeof(ch)) > 0)
		{
			if (ch == ' ' || ch == '\n')
			{
				if (length > maxlength)
					maxlength = length;
				else
					length = 0;
			}
			else
				length++;
		} // while

		printf("%d\n", maxlength);
		close(fd);

		ris = htonl(maxlength);
		if (sendto(sd, &ris, sizeof(ris), 0, (struct sockaddr *)&cliaddr, len) < 0)
		{
			perror("sendto ");
			continue;
		}

	} // for
}