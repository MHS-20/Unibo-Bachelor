#include<string.h>
#include<stdio.h>

#define DIM 9

int main(int argc, char *argv[])
{

int i = 0; 
int count = 0;

//nessun opsite
if(argc < 1)
    return 0;

//controllo A
    if(strcmp(argv[1],"TEN")!=0 && strcmp(argv[1],"BUN")!=0 &&  strcmp(argv[1],"CAM")!=0)
    return 0;
    
//controllo identificatori
    for(i=2; i<argc; i++)
    {
         if(strlen(argv[i])<DIM)
         {
             printf("Identificatore non rispetta lunghezza");
              return 0;
        }
        
    if(strstr(argv[i], "TEN")==NULL && strstr(argv[i], "CAM")==NULL && strstr(argv[i], "BUN")==NULL)
         {
             printf("Identificatore non rispetta formato");
              return 0;
        }
    }

//conto ricorrenze A
    for(i=1; i<argc; i++)
    {
    if(strstr(argv[i], argv[1])!=NULL)
        count++;
    }

    if(count >0)
    printf("%d%s",count, "persone alloggiano in ",argv[1]);
    
    return 0;
}
