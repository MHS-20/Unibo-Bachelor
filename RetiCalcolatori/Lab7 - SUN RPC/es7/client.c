#include "scan.h"
#include <rpc/rpc.h>
#include <stdio.h>
#define DIM 100

int main(int argc, char *argv[]) {

    CLIENT *cl;
    ResultFile *file_res; 
    ResultDir *dir_res; 
    RequestDir reqDir; 

    int dim; 
    char   *server;
    char   * msg;
    char    ok[5];

    if (argc < 2) {
        fprintf(stderr, "uso: %s host\n", argv[0]);
        exit(1);
    }

    server = argv[1];

    cl = clnt_create(server, SCANPROG, SCANVERS, "udp");
    if (cl == NULL) {
        clnt_pcreateerror(server);
        exit(1);
    }

    /* CORPO DEL CLIENT:
    /* ciclo di accettazione di richieste da utente ------- */
    printf("Scegli il servizio: F O D, EOF per terminare");

    while (gets(msg)) {
        switch(msg){
            case "F": {
                printf("Inserisci nome file:");
                gets(msg);
                file_res = file_scan_1(&msg, cl); 
            if (file_res == NULL) {
                fprintf(stderr, "%s: %s fallisce la rpc\n", argv[0], server);
                clnt_perror(cl, server);
                exit(1);
            }

            printf("Messaggio consegnato a %s: %s\n", server, msg);
            printf("Messaggio ricevuto da %s: %d, %d, %d\n", server, file_res->numch, file_res->numwords, file_res->numlines );
            memset(msg, 0, sizeof(msg));
            break; 
            }
            case "D": {
                printf("Inserisci nome direttorio e dimensione massima dei file:");
                scanf("%s %d", msg, &dim);
                reqDir.dim = dim; 
                reqDir.dirName = *msg; 
                dir_res = dir_res_1(&reqDir, cl); 
                if (dir_res == NULL) {
                    fprintf(stderr, "%s: %s fallisce la rpc\n", argv[0], server);
                    clnt_perror(cl, server);
                    exit(1);
                }   

            printf("Messaggio consegnato a %s: %s\n", server, msg);
            printf("Messaggio ricevuto da %s: %d\n", server, dir_res->num_file);
            //stampare in un ciclo la lista di nomi
            break; 
            }
            default: {
                printf("Scegliere un servizio disponibile"); 
                continue;
            }
        }
    printf("Scegli il servizio: F O D, EOF per terminare");
    } // while gets(msg)

    // Libero le risorse: memoria allocata con malloc e gestore di trasporto
    free(msg);
    clnt_destroy(cl);
    printf("Termino...\n");
    exit(0);
}