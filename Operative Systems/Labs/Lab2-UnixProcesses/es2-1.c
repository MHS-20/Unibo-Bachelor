#include<math.h>
#include<stdio.h>
#include<stdlib.h>

#define DIM 12

int main(int argc, char *argv[]){
    
    int  n_children = atoi(argv[1]);
    float costo_medio = atof(argv[2]);
    
    int i,j, pid, status;
    float sum;
    int m[n_children][DIM];
    

    for(i = 0; i<n_children; i++){
        //printf("\nRiga ");
        for(j = 0; j<DIM; j++){
                m[i][j] = rand() % 100;
                //printf("%d\t", m[i][j]);
            }
    }
    
    for(i = 0; i<n_children; i++){
        for(j = 0; j<DIM; j++)
            printf("%d\t", m[i][j]);
    }
    
    for (i=0; i<n_children; i++){
        sum = 0;
        pid=fork();
        if (pid==0){ //codice figlio;
            for(j = 0; j<DIM; j++)
                sum = sum + (costo_medio * m[i][j]);
            exit(sum/DIM); //fine codice figlio;
        } 
        
    }
        
        printf("\n\n");
        
//non recupero indice perché ho fatto i for separati
       for (i=0; i<n_children; i++){ //codice padre
            pid = wait(&status);
            printf("\n Autoarticolato pid: %d, media spesa: %d", pid, status); }
        
        printf("\n\n");
  return 0;  
    
}

