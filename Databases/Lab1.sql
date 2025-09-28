--
-- Script SQL: Soluzione dell'esercitazione 1 (L01.2)

--
-- ESERCIZIO 1
--
-- DDL: Creazione tabelle
--

CREATE TABLE UTENTI (
	TESSERA  CHAR(5) NOT NULL PRIMARY KEY,
	NOME     VARCHAR(30) NOT NULL,
	COGNOME  VARCHAR(30) NOT NULL,
	TELEFONO VARCHAR(20) NOT NULL,
	CONSTRAINT NCT_KEY UNIQUE(NOME,COGNOME,TELEFONO)		);

CREATE TABLE LIBRI (	
	CODICE CHAR(5) NOT NULL PRIMARY KEY,
	TITOLO VARCHAR(60) NOT NULL,
	AUTORI VARCHAR(60) NOT NULL DEFAULT 'Anonimo',
	NOTE   VARCHAR(300)		);

CREATE TABLE PRESTITI (	
	CODICELIBRO CHAR(5) NOT NULL REFERENCES LIBRI ON DELETE CASCADE, -- cancellazione in cascata
	TESSERA CHAR(5) NOT NULL REFERENCES UTENTI ON DELETE CASCADE,	 -- idem	
	DATA_OUT DATE NOT NULL,
	DATA_IN DATE,
	PRIMARY KEY (CODICELIBRO,TESSERA,DATA_OUT),			 -- vincolo di tabella
	CONSTRAINT DATA_VALIDA CHECK (DATA_IN >= DATA_OUT)	);       -- idem

--
-- INSERT validi di esempio
--

INSERT INTO UTENTI
VALUES ('00001','Paolino','Paperino','123-123456'),
       ('00002','Mickey','Mouse','123-456789'),
       ('00003','Carlo','Pravettoni','234-123123') ;

INSERT INTO LIBRI
VALUES ('L0001','Acqua in bocca','A. Camilleri, C.Lucarelli','Libro scritto a 4 mani...'),
       ('L0002','La gita a Tindari','A. Camilleri',NULL),
       ('L0003','Storia di Ochikubo',DEFAULT,'Narrativa Giapponese classica'),
       ('L0004','In bocca al lupo','F. Negrin',NULL) ;
 
-- NB: si usa il formato 'dd.MM.yyyy' per le date in input
--
INSERT INTO PRESTITI
VALUES ('L0001','00001','01.10.2018','03.10.2018'),
       ('L0003','00001','13.07.2018',NULL),
       ('L0003','00001','11.05.2018','29.05.2018'),
       ('L0002','00002','13.10.2017','02.01.2018'),
       ('L0001','00002','13.10.2017','02.01.2018'),
       ('L0004','00002','12.04.2017','02.06.2017'),
       ('L0001','00003','21.08.2018',NULL),
       ('L0003','00003','21.08.2018',NULL);

--
-- UPDATE
--

-- U1) Modifica del numero di telefono dell'utente con tessera '00001'
UPDATE UTENTI
SET    TELEFONO = '123-654321'
WHERE  TESSERA = '00001' ;

-- U2) Aggiunta di una nota al libro di codice 'L0002'
UPDATE LIBRI
SET    NOTE = 'Romanzo del 2000...'
WHERE  CODICE = 'L0002' ;

-- U3) Aggiunta della data di restituzione a un prestito
UPDATE PRESTITI
SET    DATA_IN = '29.09.2018'
WHERE  CODICELIBRO = 'L0003'
AND    TESSERA = '00003'
AND    DATA_OUT = '21.08.2018' ;

--
-- DELETE
--
-- D1) Cancellazione dell'utente con tessera '00003'
DELETE FROM UTENTI
WHERE  TESSERA = '00003' ;

-- D2) Cancellazione del libro con codice 'L0002'
DELETE FROM LIBRI
WHERE  CODICE = 'L0002' ;

--
-- INTERROGAZIONI
--

-- Q1) Libri con autore Camilleri e nel cui titolo compare la parola 'gita'
SELECT *
FROM   LIBRI
WHERE  AUTORI LIKE '%Camilleri%'
AND    LOWER(TITOLO) LIKE '%gita%' ;
-- LOWER(TITOLO), così fa match anche con GITA, Gita, ecc.

-- Q2) Utenti con cognome Paperino
SELECT *
FROM   UTENTI
WHERE  COGNOME = 'Paperino' ;

-- Q3) Prestiti del 2018
SELECT *
FROM   PRESTITI
WHERE  YEAR(DATA_OUT) = 2018 ;

-- Q4) Prestiti in cui la restituzione non e' avvenuta lo stesso anno
SELECT *
FROM   PRESTITI
WHERE  YEAR(DATA_OUT) < YEAR(DATA_IN) ;

-- Q5) Codici dei libri presi in prestito dall'utente Mickey Mouse, tel. 123-456789 
SELECT DISTINCT P.CODICELIBRO
FROM   PRESTITI P, UTENTI U
WHERE  P.TESSERA = U.TESSERA
AND    U.NOME = 'Mickey'
AND    U.COGNOME = 'Mouse'
AND    U.TELEFONO = '123-456789' ;
-- DISTINCT elimina eventuali codici duplicati

-- Q6) Come Q5, ma considerando l'intervallo 01.10.2017 - 31.12.2017 
SELECT DISTINCT P.CODICELIBRO
FROM   PRESTITI P, UTENTI U
WHERE  P.TESSERA = U.TESSERA
AND    U.NOME = 'Mickey'
AND    U.COGNOME = 'Mouse'
AND    U.TELEFONO = '123-456789'
AND    P.DATA_OUT BETWEEN '01.10.2017' AND '31.12.2017' ; 

-- Q7) Come Q6, ma fornendo tutti i dettagli dei libri 
SELECT DISTINCT L.*
FROM   PRESTITI P, UTENTI U, LIBRI L
WHERE  P.TESSERA = U.TESSERA
AND    P.CODICELIBRO = L.CODICE
AND    U.NOME = 'Mickey'
AND    U.COGNOME = 'Mouse'
AND    U.TELEFONO = '123-456789'
AND    P.DATA_OUT BETWEEN '01.10.2017' AND '31.12.2017' ;
-- Il DISTINCT serve nel caso uno stesso libro sia stato preso in prestito più volte

-- Q8) Utenti che hanno preso in prestito almeno 2 libri nel 2017 
SELECT DISTINCT P1.TESSERA
FROM   PRESTITI P1, PRESTITI P2
WHERE  YEAR(P1.DATA_OUT) = 2017
AND    YEAR(P2.DATA_OUT) = 2017
AND    P1.TESSERA = P2.TESSERA
AND    P1.CODICELIBRO <> P2.CODICELIBRO ;
-- E' un casi di self-join: stesso anno, stesso utente, ma libri diversi

-- Q9) Utenti che nel 2017 non hanno preso in prestito nessun libro
SELECT U.TESSERA
FROM   UTENTI U
  EXCEPT
SELECT P.TESSERA
FROM   PRESTITI P
WHERE  YEAR(P.DATA_OUT) = 2017;

-- oppure, usando l'outer join, che permette di ottenere facilmente 
-- anche gli altri attributi di UTENTI (con la differenza è più compicato):
SELECT U.TESSERA -- oppure U.*
FROM   UTENTI U LEFT JOIN PRESTITI P ON (U.TESSERA = P.TESSERA) AND YEAR(P.DATA_OUT) = 2017
WHERE  P.CODICELIBRO IS NULL;

-- Q10) Utenti che non hanno mai preso in prestito un libro senza autori e che nei
--      commenti include entrambe le parole 'narrativa' e 'classica'
SELECT U.TESSERA
FROM   UTENTI U
  EXCEPT
SELECT P.TESSERA
FROM   PRESTITI P, LIBRI L
WHERE  P.CODICELIBRO = L.CODICE
AND    L.AUTORI = 'Anonimo'
AND    LOWER(L.NOTE) LIKE '%narrativa%'
AND    LOWER(L.NOTE) LIKE '%classica%' ;

DROP TABLE UTENTI;
DROP TABLE LIBRI;
DROP TABLE PRESTITI;


