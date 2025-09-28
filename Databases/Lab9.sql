--
-- Script SQL: Soluzione dell'esercitazione 9 (L09)
-- 

-----------------
-- ESERCIZIO 1 --
-----------------

CREATE TABLE E2 (
K2 		INT NOT NULL PRIMARY KEY,
B1 		INT,
B2 		INT,
K2Y 	INT NOT NULL REFERENCES E2,
K1R2 	INT,
CONSTRAINT B CHECK ( (B1 IS NULL AND B2 IS NULL) OR    
			         (B1 IS NOT NULL AND B2 IS NOT NULL) )    );

CREATE TABLE E1 (
K1 		INT NOT NULL PRIMARY KEY,
A 		INT NOT NULL,
K2R1 	INT NOT NULL REFERENCES E2,
C 		INT NOT NULL				);
 
ALTER TABLE E2 
ADD CONSTRAINT FKR2 FOREIGN KEY (K1R2) REFERENCES E1;

-- Trigger che garantisce l'unicità  delle coppie di valori (B1,B2)
CREATE TRIGGER E2_KEY
BEFORE INSERT ON E2
REFERENCING NEW AS N
FOR EACH ROW 
WHEN (EXISTS (	SELECT 	* 
				FROM 	E2
				WHERE 	(N.B1,N.B2) = (E2.B1,E2.B2) )    ) 
SIGNAL SQLSTATE '70001' ('La coppia (B1,B2) non può essere duplicata!');

-- Trigger che garantisce il rispetto del vincolo al punto c), 
-- che può essere violato solo inserendo una tupla in E2 
-- che referenzia, via R3, un'altra tupla di E2 che partecipa a R2 
CREATE TRIGGER PUNTO_C	
BEFORE INSERT ON E2
REFERENCING NEW AS N
FOR EACH ROW 
WHEN (EXISTS ( 	SELECT 	* 
				FROM 	E2
				WHERE 	N.K2Y = E2.K2 				
				AND 	E2.K1R2 IS NOT NULL )  )			-- 
SIGNAL SQLSTATE '70002' ('La tupla inserita referenzia una tupla di E2 che partecipa a R2!');

-----------------
-- ESERCIZIO 2 --
-----------------

CREATE TABLE E2 (
K1 		INT NOT NULL, 
B   	INT NOT NULL,
C   	INT NOT NULL,
PRIMARY KEY (K1,B)   );

CREATE TABLE E1 (
K1 		INT NOT NULL PRIMARY KEY,
A 		INT NOT NULL,
D 		INT,
K1X 	INT REFERENCES E1,
K1E2 	INT,
B 		INT,
CONSTRAINT R2_DEFINED CHECK
	( (K1X IS NOT NULL AND K1E2 IS NOT NULL AND B IS NOT NULL AND D IS NOT NULL) OR
	  (K1X IS NULL AND K1E2 IS NULL AND B IS NULL AND D IS NULL) ),
CONSTRAINT FKR2 FOREIGN KEY (K1E2,B) REFERENCES E2,
CONSTRAINT PUNTO_C CHECK ( K1 <> K1X AND K1 <> K1E2 AND K1X <> K1E2)    );
 
ALTER TABLE E2 
ADD CONSTRAINT FKR1 FOREIGN KEY (K1) REFERENCES E1;	 

-- Per garantire il rispetto del vincolo di cui al punto d) 
-- è necessario definire il seguente trigger:
CREATE TRIGGER PUNTO_D	
BEFORE INSERT ON E2
REFERENCING NEW AS N
FOR EACH ROW 
WHEN (100 < N.B + ( SELECT 	SUM(E2.B)
  	  		  		FROM 	E2
			  		WHERE 	N.K1 = E2.K1 )    )
SIGNAL SQLSTATE '70001' ('La tupla inserita ha un valore di B troppo grande!');

-----------------
-- ESERCIZIO 3 --
-----------------

CREATE TABLE E4(
K1 		INT NOT NULL PRIMARY KEY,
D 		INT NOT NULL	);

CREATE TABLE E1(
K1 		INT NOT NULL PRIMARY KEY,
A 		INT NOT NULL,
E2 		INT, -- se non nullo l'istanza è di E2
K2 		INT,
B 		INT,
E3 		INT, -- se non nullo l'istanza è di E3
C 		INT,
R1_K1 	INT REFERENCES E4,
CONSTRAINT TOTALE_ESCLUSIVA CHECK (
(E2 IS NOT NULL AND E3 IS NULL) OR
(E2 IS NULL AND E3 IS NOT NULL)),
CONSTRAINT COEX_E2 CHECK (
(E2 IS NOT NULL AND K2 IS NOT NULL AND B IS NOT NULL) OR
(E2 IS NULL AND K2 IS NULL AND B IS NULL)), 
CONSTRAINT COEX_E3 CHECK (
(E3 IS NOT NULL AND C IS NOT NULL AND R1_K1 IS NOT NULL) OR
(E3 IS NULL AND C IS NULL AND R1_K1 IS NULL))   );

ALTER TABLE E4
ADD CONSTRAINT FK FOREIGN KEY (K1) REFERENCES E1;

-- trigger che garantisce che E4 sia un subset di E2
CREATE TRIGGER INS_E4
BEFORE INSERT ON E4
REFERENCING NEW AS N
FOR EACH ROW
WHEN ( NOT EXISTS (	SELECT 	*
  			    	FROM 	E1
  			    	WHERE 	E1.K1 = N.K1 
    				AND 	E1.E2 IS NOT NULL) )
SIGNAL SQLSTATE '70001' ('Un''istanza di E4 deve essere anche istanza di E2 !');

-- trigger che garantisce l'unicità dei valori di K2
CREATE TRIGGER INS_E1
BEFORE INSERT ON E1
REFERENCING NEW AS N
FOR EACH ROW
WHEN (EXISTS (	SELECT	*
  	  	        FROM 	E1
  		        WHERE 	E1.K2 = N.K2) )
SIGNAL SQLSTATE '70002' ('I valori di K2 non possono essere duplicati!');
-- si noti che, poiche’ NULL <> NULL, il trigger non dà errore se si 
-- inserisce un'istanza di E3 per cui K2 non è definito


