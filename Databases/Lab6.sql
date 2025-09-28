--
-- Script SQL: Soluzione dell'esercitazione 6 (L06)
--
-- Per comodità, tutti i trigger sono definiti mediante
-- CREATE OR REPLACE TRIGGER
-- 
-- ------------------------------------------------------------------
--
-- ESERCIZIO 1
--

DROP TABLE R;
DROP TABLE S;
DROP TRIGGER R_C_UNIQUE;
DROP TRIGGER S_C_FOREIGNKEY;
DROP TRIGGER S_CASCADED_DELETE;

CREATE TABLE R(
        A INT NOT NULL PRIMARY KEY,
        C INT				);

CREATE TABLE S (
        B INT NOT NULL PRIMARY KEY,
        C INT NOT NULL 			);

-- 1) Trigger che garantisce l'unicita' dei valori di R.C
CREATE OR REPLACE TRIGGER R_C_UNIQUE
BEFORE INSERT ON R
REFERENCING NEW AS NewR
FOR EACH ROW
WHEN ( EXISTS (SELECT *
               FROM   R
               WHERE  R.C = NewR.C) )
SIGNAL SQLSTATE '70001' ('R.C non ammette valori duplicati');
--
-- ATTENZIONE! Non funziona nel caso di insert multipli, se vengono inserite 2
-- o più tuple con uno stesso valore di R.C non presente prima dell'insert.
-- Per ovviare a cio' si rimanda al trigger AccettazioniSingole dell'esercizio 3

-- 2) Trigger che impone un vincolo di integrita' referenziale su S.C
CREATE OR REPLACE TRIGGER S_C_FOREIGNKEY
BEFORE INSERT ON S
REFERENCING NEW AS NewS
FOR EACH ROW
WHEN ( NOT EXISTS (	SELECT * 
		   			FROM   R 
		   			WHERE  R.C = NewS.C )  )
SIGNAL SQLSTATE '70002' ('I valori di S.C devono essere un sottoinsieme di quelli di R.C');
--
-- NB: Usando la forma 
/*
WHEN ( NewS.C NOT IN (SELECT R.C FROM R )) al posto di not exist
*/
-- il trigger non funziona correttamente in caso di valori nulli in R.C.
-- Occorre quindi aggiungere nella subquery: WHERE R.C IS NOT NULL

-- 3) Trigger che esegue la cancellazione in cascata su S
-- Funziona anche se OldR.C e' NULL, nel qual caso non cancella nulla da S 
CREATE OR REPLACE TRIGGER S_CASCADED_DELETE
AFTER DELETE ON R
REFERENCING OLD AS OldR
FOR EACH ROW
DELETE
FROM  S
WHERE S.C = OldR.C;


-- Inserimenti validi
INSERT INTO R VALUES(1, 100);
INSERT INTO R VALUES(2, NULL);
INSERT INTO R VALUES(3, 200);

INSERT INTO S VALUES(10, 100);
INSERT INTO S VALUES(20, 100);

-- Inserimento non valido: R.C duplicato
INSERT INTO R VALUES(4, 200);

-- Inserimento non valido: violazione del vincolo di foreign key
INSERT INTO S VALUES(30, 300);

-- Esempio di cancellazione in cascata
DELETE FROM R
WHERE  C = 100;


-- ------------------------------------------------------------------
--
-- ESERCIZIO 2
--

DROP TABLE PRODOTTI;
DROP TABLE ORDINI;
DROP TABLE VENDITE;
DROP TRIGGER NewSale;
DROP TRIGGER DropSale;
DROP TRIGGER UpdateSale;
DROP TRIGGER ManageShippingCost;


CREATE TABLE PRODOTTI(
    PCODE 	 CHAR(5) NOT NULL PRIMARY KEY,  	
    DESCRIZIONE VARCHAR(20) NOT NULL,
	PREZZO 	 DEC(6,2) NOT NULL CHECK (PREZZO > 0)	);

CREATE TABLE ORDINI(
	ID 		 CHAR(5) NOT NULL PRIMARY KEY,
	NOMECLIENTE VARCHAR(20) NOT NULL,
	SPESESPEDIZIONE DEC(6,2) NOT NULL DEFAULT 7 CHECK (SPESESPEDIZIONE IN (0,7)), 
	TOTALE 	 DEC(6,2) NOT NULL DEFAULT 7 CHECK (TOTALE >=0)	);	

CREATE TABLE VENDITE(
	PCODE 	 CHAR(5) NOT NULL REFERENCES PRODOTTI,
	ID 		 CHAR(5) NOT NULL REFERENCES ORDINI,
	QUANTITA INT NOT NULL CHECK (QUANTITA > 0),
	PRIMARY KEY (PCODE,ID)						);

-- Il seguente trigger aggiorna TOTALE in caso di inserimento in VENDITE 
CREATE OR REPLACE TRIGGER NewSale
AFTER INSERT ON VENDITE
REFERENCING NEW AS NewV
FOR EACH ROW
UPDATE ORDINI
SET   TOTALE = TOTALE + 
			   NewV.QUANTITA * (SELECT PREZZO
					 			FROM   PRODOTTI
					 			WHERE  PCODE = NewV.PCODE )
WHERE ID = NewV.ID;

-- Il seguente trigger aggiorna TOTALE in caso di cancellazione da VENDITE 
CREATE OR REPLACE TRIGGER DropSale
AFTER DELETE ON VENDITE
REFERENCING OLD AS OldV
FOR EACH ROW
UPDATE ORDINI
SET   TOTALE = TOTALE - 
			   OldV.QUANTITA * (SELECT PREZZO
					 			FROM   PRODOTTI
					 			WHERE  PCODE = OldV.PCODE )
WHERE ID = OldV.ID;

-- Il seguente trigger aggiorna TOTALE in caso di aggiornamento di quantita' in VENDITE 
CREATE OR REPLACE TRIGGER UpdateSale
AFTER UPDATE OF QUANTITA ON VENDITE
REFERENCING OLD AS OldV NEW AS NewV
FOR EACH ROW
UPDATE ORDINI
SET   TOTALE = TOTALE + 
			   (NewV.QUANTITA - OldV.QUANTITA) * (SELECT PREZZO
					 		   					  FROM   PRODOTTI
					 		   					  WHERE  PCODE = NewV.PCODE )
WHERE ID = NewV.ID;

--
-- Gestione delle spese di spedizione
--
-- La soluzione con 2 trigger (commentati di seguito) NON funziona su DB2, 
-- a causa di ricorsione multipla!
-- Usando il seguente trigger funziona tutto correttamente

CREATE OR REPLACE TRIGGER ManageShippingCost
AFTER UPDATE OF TOTALE ON ORDINI
REFERENCING NEW AS NewO
FOR EACH ROW
IF (NewO.TOTALE > 57 AND NewO.SPESESPEDIZIONE = 7) --CHECK spedizione non ancora gratuita
THEN
	UPDATE 	ORDINI
	SET   	TOTALE = NewO.TOTALE - 7,
			SPESESPEDIZIONE = 0
	WHERE 	ID = NewO.ID;
ELSE
	IF (NewO.TOTALE <= 50 AND NewO.SPESESPEDIZIONE = 0)
	THEN
		UPDATE 	ORDINI
		SET   	TOTALE = NewO.TOTALE + 7,
				SPESESPEDIZIONE = 7
		WHERE 	ID = NewO.ID;
	END IF	
		;
END IF	

/*
-- Coppia di trigger che non funziona
--
-- Trigger che provvede ad azzerare le spese di spedizione 
CREATE OR REPLACE TRIGGER AzzeraSpeseSpedizione
AFTER UPDATE OF TOTALE ON ORDINI
REFERENCING NEW AS NewO
FOR EACH ROW
WHEN (NewO.TOTALE > 57 AND NewO.SPESESPEDIZIONE = 7)
UPDATE 	ORDINI
SET   	TOTALE = NewO.TOTALE - 7,
		SPESESPEDIZIONE = 0
WHERE 	ID = NewO.ID;

-- Trigger che ripristina le spese di spedizione 
CREATE OR REPLACE TRIGGER RipristinaSpeseSpedizione
AFTER UPDATE OF TOTALE ON ORDINI
REFERENCING NEW AS NewO
FOR EACH ROW
WHEN (NewO.TOTALE <= 50 AND NewO.SPESESPEDIZIONE = 0)
UPDATE 	ORDINI
SET   	TOTALE = NewO.TOTALE + 7,
		SPESESPEDIZIONE = 7
WHERE 	ID = NewO.ID;
*/

-- Inserimenti in PRODOTTI e ORDINI
INSERT INTO PRODOTTI VALUES ('P0001', 'Panettone', 4.50);
INSERT INTO ORDINI(ID,NOMECLIENTE) VALUES ('11111','Paolino Paperino');		

-- Inserimento in VENDITE
INSERT INTO VENDITE VALUES ('P0001','11111',2);
-- 

-- Aggiornamento di VENDITE (spedizione gratuita)
UPDATE 	VENDITE 
SET   	QUANTITA = QUANTITA + 10
WHERE 	PCODE = 'P0001'
AND		ID = '11111';

-- Altro aggiornamento di VENDITE (spedizione torna a 7 Euro)
UPDATE 	VENDITE 
SET   	QUANTITA = QUANTITA - 2
WHERE 	PCODE = 'P0001'
AND		ID = '11111';

-- Cancellazione da VENDITE
DELETE FROM VENDITE 
WHERE 	PCODE = 'P0001'
AND		ID = '11111';


-- ------------------------------------------------------------------
--
-- ESERCIZIO 3
--

DROP TABLE STUDENTI;
DROP TABLE CORSI;
DROP TABLE APPELLI;
DROP TABLE ESAMI;
DROP TABLE VERBALIZZAZIONI;

DROP TRIGGER AccettazioniSingole;
DROP TRIGGER EsameInvalido;
DROP TRIGGER Verbalizzazione;
DROP TRIGGER RimuoviEsami;

CREATE TABLE STUDENTI(
    MATR 	CHAR(6) NOT NULL PRIMARY KEY,  -- CHAR(6) solo per brevità
    NOME 	VARCHAR(30) NOT NULL,
    COGNOME VARCHAR(30) NOT NULL					);

CREATE TABLE CORSI (
    CODC 	CHAR(3) NOT NULL PRIMARY KEY,
	NOME 	VARCHAR(30) NOT NULL UNIQUE				);

CREATE TABLE APPELLI (
    CODC 	CHAR(3) NOT NULL REFERENCES CORSI,
	DATA 	DATE NOT NULL, 
	LUOGO 	VARCHAR(30) NOT NULL,
	PRIMARY KEY (CODC, DATA)					);

CREATE TABLE ESAMI (
	MATR 	CHAR(6) NOT NULL REFERENCES STUDENTI,
    CODC 	CHAR(3) NOT NULL,
	DATA 	DATE NOT NULL, 
	VOTO 	INT NOT NULL CHECK (VOTO BETWEEN 0 AND 31),
	ACCETTATO CHAR(1) DEFAULT NULL CHECK (ACCETTATO = 'Y'),
	FOREIGN KEY (CODC, DATA) REFERENCES APPELLI,
	PRIMARY KEY (MATR, CODC, DATA)					);

CREATE TABLE VERBALIZZAZIONI (
	MATR 	CHAR(6) NOT NULL REFERENCES STUDENTI,
    CODC 	CHAR(3) NOT NULL,
	DATA 	DATE NOT NULL, 
	VOTO 	INT NOT NULL CHECK (VOTO BETWEEN 18 AND 31),
	FOREIGN KEY (CODC, DATA) REFERENCES APPELLI,
	PRIMARY KEY (MATR, CODC)					);


-- Inserimenti validi 
INSERT INTO STUDENTI VALUES ('765432', 'Carlo', 'Rubbia');

INSERT INTO CORSI VALUES ('SIT', 'Sistemi Informativi T'), 
			 ('CET', 'Calcolatori Elettronici T');

INSERT INTO APPELLI VALUES ('SIT', '10.02.2017', 'LAB 4'),
			   ('SIT', '22.06.2017', 'LAB 3'),
			   ('CET', '20.01.2017', 'AULA 2.9'),
			   ('CET', '14.07.2017', 'AULA 2.4');

INSERT INTO ESAMI VALUES ('765432', 'SIT', '10.02.2017', 19, NULL),
			 ('765432', 'SIT', '22.06.2017', 27, NULL),
			 ('765432', 'CET', '20.01.2017', 15, NULL),
			 ('765432', 'CET', '14.07.2017', 31, NULL);


-- Il seguente trigger evita che, nel caso di update multipli, 
-- in ESAMI si abbiano più tuple con ACCETTATO = 'Y' per la stessa
-- coppia studente,corso

CREATE OR REPLACE TRIGGER AccettazioniSingole
AFTER UPDATE OF ACCETTATO ON ESAMI
REFERENCING NEW_TABLE AS NTA
FOR EACH STATEMENT
WHEN ( EXISTS (	SELECT 	*	 
	       		FROM 	NTA NTA1, NTA NTA2
	       		WHERE 	(NTA1.MATR,NTA1.CODC,NTA1.ACCETTATO) 
						  = (NTA2.MATR,NTA2.CODC,NTA2.ACCETTATO)  
				AND		NTA1.DATA <> NTA2.DATA				) )
SIGNAL SQLSTATE '70001' ('Aggiornamento invalido di ESAMI: scelta di piu'' esami per 
una stessa coppia studente,corso.');

-- oppure, raggrupando:

CREATE OR REPLACE TRIGGER AccettazioniSingole
AFTER UPDATE OF ACCETTATO ON ESAMI
REFERENCING NEW_TABLE AS NTA
FOR EACH STATEMENT
WHEN ( 1 < ANY (SELECT 	COUNT(*)	 
	       		FROM 	NTA
	       		GROUP BY MATR, CODC	) )
SIGNAL SQLSTATE '70001' ('Aggiornamento invalido di ESAMI: scelta di piu'' esami per 
una stessa coppia studente,corso.');

-- In alternativa:
CREATE OR REPLACE TRIGGER AccettazioniSingole
AFTER UPDATE OF ACCETTATO ON ESAMI
REFERENCING NEW AS N
FOR EACH ROW
WHEN ( EXISTS (	SELECT 	*	 
	       		FROM 	ESAMI E
	       		WHERE 	(N.MATR,N.CODC,N.ACCETTATO) = (E.MATR,E.CODC,E.ACCETTATO)  
				AND	N.DATA <> E.DATA				) )
SIGNAL SQLSTATE '70001' ('Aggiornamento invalido di ESAMI: scelta di piu'' esami per 
una stessa coppia studente,corso.');
-- Quest'ultima soluzione può essere (molto) meno efficiente delle altre, 
-- in quanto coinvolge tutta la relazione ESAMI

-- Update non valido
UPDATE 	ESAMI
SET 	ACCETTATO = 'Y'
WHERE	(MATR,CODC) = ('765432','SIT');

-- Il seguente trigger evita che in ESAMI si inseriscano tuple con ACCETTATO = 'Y',
-- valore che può essere settato solo con un aggiornamento

CREATE OR REPLACE TRIGGER EsameInvalido
BEFORE INSERT ON ESAMI
REFERENCING NEW AS NE
FOR EACH ROW
WHEN (NE.ACCETTATO IS NOT NULL)
SIGNAL SQLSTATE '70002' ('Non e'' consentito inserire esami con scelta di accettazione.');

-- Insert non valido
INSERT INTO ESAMI VALUES ('765432', 'SIT', '15.07.2017', 30, 'Y');

-- Il seguente trigger inserisce in VERBALIZZAZIONI tutti gli esami
-- che sono stati aggiornati ponendo ACCETTATO = 'Y'

CREATE OR REPLACE TRIGGER Verbalizzazione
AFTER UPDATE OF ACCETTATO ON ESAMI
REFERENCING NEW_TABLE AS NTE
FOR EACH STATEMENT
INSERT INTO VERBALIZZAZIONI
SELECT MATR, CODC, DATA, VOTO
FROM   NTE
WHERE  ACCETTATO = 'Y' ;  

-- Il seguente trigger elimina gli esami per cui c'è stata la verbalizzazione

CREATE OR REPLACE TRIGGER RimuoviEsami
AFTER INSERT ON VERBALIZZAZIONI
REFERENCING NEW AS NV
FOR EACH ROW
DELETE FROM ESAMI 
WHERE (MATR,CODC) = (NV.MATR,NV.CODC);

-- Update valido
UPDATE 	ESAMI
SET 	ACCETTATO = 'Y'
WHERE	(MATR,CODC,DATA) = ('765432','SIT','22.06.2017');

