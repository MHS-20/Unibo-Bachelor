#include "scan.h"
#include <rpc/rpc.h>
#include <stdio.h>
#include <dirent.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
#include <fcntl.h>

ResultFile *file_scan_1_svc(char **msg, struct svc_req *rp) {
    static ResultFile result;

    int fd, numch = 0, numwords = 0, numlines = 0; 
    char ch; 
    fd = open(*msg, O_RDONLY);

    if(fd < 0){
        perror("Errore nell'apertura del file.\n");
        result.numch = -1; 
        result.numwords = -1; 
        result.numlines = -1;
        return (&result);
    }

    while(read(fd, &ch, sizeof(char)) > 0){
        if(ch == ' '){
            numch++; 
            numwords++; 
        }

        if(ch == '\n'){
            numlines++; 
        }
    }

    result.numch = numch; 
    result.numwords = numwords; 
    result.numlines = numlines; 
    return (&result);
}

ResultDir *dir_scan_1_svc(RequestDir *req, struct svc_req *rp) {
    static ResultDir result;
    DIR *dir1;
    struct dirent *dd1;
    char newDir[50];
    int fd, sz, numf = 0, i = 0;  

    if ((dir1 = opendir(req->dirName)) != NULL){
        while ((dd1 = readdir(dir1)) != NULL){
            if (strcmp(dd1->d_name, ".") != 0 && strcmp(dd1->d_name, "..") != 0){
                // build new path
                newDir[0] = '\0';
                strcat(newDir, dir);
                strcat(newDir, "/");
                strcat(newDir, dd1->d_name);
                if(opendir(newDir) == NULL){
                    fd = open(newDir, O_RDONLY);
                    fseek(fd, 0, SEEK_END);
                    sz = ftell(fd);
                    if (sz > req->dim){
                        numf++; 
                        result.names[i] = dd1->d_name;
                        i++;
                    }
                }
            }
            result.num_file = numf;
        }
    }else {
            result.num_file = -1;
    }

    return (&result);
}