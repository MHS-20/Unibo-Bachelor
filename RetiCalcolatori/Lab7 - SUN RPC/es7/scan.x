// in xdr le <> richiedono allocazione con malloc nel codice
// invece vettore di char è già allocato

struct ResultFile{
	int numch; 
	int numwords; 
	int numlines;
};

struct Name{
	char name[25]; 
};

struct ResultDir{
	int num_file; 
	Name names[8];
};

struct RequestDir{
	int dim; 
	char dirName[50];
};

program SCANPROG {
	version SCANVERS {
		ResultFile file_scan(string) = 1;
		ResultDir dir_scan(RequestDir) = 2;
	} = 1;
} = 0x20000013;