#include <stdio.h>
#include <unistd.h>
#include <signal.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <string.h>
#include <math.h>

#define dim 2
#define max_com 256

int n, c=0, status;
char com[max_com]; 

void handlerCOM(int signum);
void handlerP1(int signum);
void handlerP0(int signum);

int main(int argc, char* argv[]){
    
int pid[dim];
int i = 1; 

n = atoi(argv[2]);
strcpy(com, argv[1]);

if (argc != 3) {
        fprintf(stderr, "Errore nel numero di argomenti\n");
        exit(EXIT_FAILURE);
    }

signal(SIGUSR1, handlerP1);
signal(SIGUSR2, handlerP1);
signal(SIGCHLD, handlerP0);
signal(SIGALRM,handlerCOM);


pid[0] = fork();
if(pid[0] == 0) //codice P1
    pause();

if(pid[0] > 0) { //codice P0
    pid[1]=fork();
    if(pid[1]>0){
        while(c < dim){
        sleep(1);
        printf("P0 e^%d: %d\n", i, i); //exp(i)
        i++;}}
    else 
            if(pid[1] == 0){ //CODICE P2
    if(getpid()%2 == 0)
        kill(pid[0], SIGUSR2);
    else
        kill(pid[0], SIGUSR1);
    exit(EXIT_SUCCESS);
}}

return 0;} //fine main

void handlerCOM(int signum)
{
    //printf("check exec");
    execlp(com, com, (char*)0 );
    printf("Errore in execl\n");
    exit(EXIT_FAILURE);
}

void handlerP1(int signum)
{
if(signum==SIGUSR1) {
     printf("PID di P1 %d; Fine\n", getpid());
     exit(EXIT_SUCCESS);}
 
if (signum==SIGUSR2) {
    alarm(n);
    for(;;){
         sleep(1); //rallentare stampa
         printf("PID di %d P1\n", getpid());
    }   }
}

void handlerP0(int signum){
    c++; 
    wait(&status);
    if(c>=dim)
        exit(EXIT_SUCCESS);
}
