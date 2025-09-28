--
-- Script SQL: Soluzione dell'esercitazione 8 (L08)
-- 
-- Per tutti gli esercizi lo script generato da DB-Main � stato modificato 
-- - eliminando il comando CREATE DATABASE
-- - eliminando la Index Section
-- - commentando i CHECK non gestibili da DB2
-- - modificando il tipo degli attributi, in particolare quelli necessari 
--   per le operazioni e i trigger richiesti
--
-- Sono incluse le operazioni e i trigger richiesti,
-- oltre a inserimenti/aggiornamenti d'esempio per il test dei trigger

-----------------
-- ESERCIZIO 1 --
-----------------

create table CARBURANTI (
     Nome char(3) not null,
     constraint ID_CARBURANTI_ID primary key (Nome));

create table POMPE (
     CodiceDistr char(3) not null,
     Numero INT not null,
     constraint ID_POMPE_ID primary key (CodiceDistr, Numero));

create table PC (
     Nome char(3) not null,
     CodiceDistr char(3) not null,
     Numero INT not null,
     Prezzo DEC(5,3) not null,  
     LitriErogati DEC(8,2) not null, 
     constraint ID_PC_ID primary key (Nome, CodiceDistr, Numero));

create table RIFORNIMENTI (
     ID char(5) not null,
     Timestamp TIMESTAMP not null,
     Litri DEC(5,2) not null,
     Nome char(3) not null,
     CodiceDistr char(3) not null,
     Numero INT not null,
     constraint ID_RIFORNIMENTI_ID primary key (ID));

-- Constraints Section
-- ___________________ 

-- alter table CARBURANTI add constraint ID_CARBURANTI_CHK
--     check(exists(select * from PC
--                  where PC.Nome = Nome)); 
--
-- Non gestibile nemmeno con trigger

alter table RIFORNIMENTI add constraint FKRC_FK
     foreign key (Nome)
     references CARBURANTI;

alter table RIFORNIMENTI add constraint FKRP_FK
     foreign key (CodiceDistr, Numero)
     references POMPE;

alter table PC add constraint FKPC_POM_FK
     foreign key (CodiceDistr, Numero)
     references POMPE;

alter table PC add constraint FKPC_CAR
     foreign key (Nome)
     references CARBURANTI;

-- Operazioni e trigger

INSERT INTO POMPE VALUES('D01',1);
INSERT INTO CARBURANTI VALUES('GAS');
INSERT INTO PC VALUES('GAS','D01',1,1.25,27250.16);

CREATE OR REPLACE TRIGGER UpdateTotLitri
AFTER INSERT ON RIFORNIMENTI
REFERENCING NEW AS N
FOR EACH ROW
UPDATE PC
SET LitriErogati = LitriErogati + N.Litri
WHERE (CodiceDistr, Numero) = (N.CodiceDistr, N.Numero)
AND Nome = N.Nome;

INSERT INTO RIFORNIMENTI VALUES ('R0001',CURRENT_TIMESTAMP,27.35,'GAS','D01',1);
     
-----------------
-- ESERCIZIO 2 --
-----------------

create table CAMERE (
     NomeAlbergo char(10) not null,
     Numero SMALLINT not null,
     Comune char(10) not null,
     PostiLetto SMALLINT not null,
     constraint ID_CAMERE_ID primary key (NomeAlbergo, Numero, Comune));

create table PRENOTAZIONI (
     NomeAlbergo char(10) not null,
     Numero SMALLINT not null,
     Comune char(10) not null,
     DataInizio DATE not null,
     DataFine DATE not null,
     NumPersone SMALLINT not null,
     CF char(16) not null,
     constraint ID_PRENOTAZIONI_ID primary key (NomeAlbergo, Numero, Comune, DataInizio));

-- Constraints Section
-- ___________________ 

alter table PRENOTAZIONI add constraint FKPCA
     foreign key (NomeAlbergo, Numero, Comune)
     references CAMERE;

-- Operazioni e trigger

INSERT INTO CAMERE VALUES('Jolly',27,'Bologna',4);

CREATE OR REPLACE TRIGGER CheckPrenotazione
BEFORE INSERT ON PRENOTAZIONI
REFERENCING NEW AS N
FOR EACH ROW
WHEN (EXISTS (	SELECT 	* 
				FROM 	PRENOTAZIONI P  
				WHERE 	N.DataInizio <= P.DataFine 
				AND		N.DataFine >= P.DataInizio
				AND     (N.NomeAlbergo, N.Numero, N.Comune) =
						(P.NomeAlbergo, P.Numero, P.Comune)
				) )
SIGNAL SQLSTATE '70001' ('Prenotazione in conflitto con una gia'' esistente!');

INSERT INTO PRENOTAZIONI 
VALUES ('Jolly',27,'Bologna','17.12.2020','20.12.2020',3,'abcdef72a19g443t');

INSERT INTO PRENOTAZIONI 
VALUES ('Jolly',27,'Bologna','15.12.2020','24.12.2020',4,'ggalme83b26a995q');

-----------------
-- ESERCIZIO 3 --
-----------------

create table PREVENTIVI (
     Numero INT not null,
     NomeCliente char(10) not null,
     DescrizioneLavori varchar(50) not null,
     ImportoPrevisto DEC(8,2) not null,
     DataPreventivo DATE not null,
     DataInizioLavori DATE not null,
     constraint ID_PREVENTIVI_ID primary key (Numero));

create table FATTURE (
     CodFattura char(5) not null,
     Numero INT not null,
     NomeCliente char(10) not null,
     Data DATE not null,
     Importo DEC(8,2) not null,
     constraint ID_FATTURE_ID primary key (CodFattura),
     constraint FKPF_ID unique (Numero));

create table STATISTICHE (
     CodFattura char(5) not null,
     DurataLavori INT not null,
     VariazionePercImporto DEC(6,2) not null,
     constraint FKFS_ID primary key (CodFattura));

-- Constraints Section
-- ___________________ 

-- alter table FATTURE add constraint ID_FATTURE_CHK
--     check(exists(select * from STATISTICHE
--                  where STATISTICHE.CodFattura = CodFattura)); 

alter table FATTURE add constraint FKPF_FK
     foreign key (Numero)
     references PREVENTIVI;

alter table STATISTICHE add constraint FKFS_FK
     foreign key (CodFattura)
     references FATTURE;

-- Operazioni e trigger

INSERT INTO PREVENTIVI 
VALUES(215,'Paperino','Rifacimento tetto',15000,'26.11.2020','01.12.2020');

CREATE OR REPLACE TRIGGER SetNomeCliente
BEFORE INSERT ON FATTURE
REFERENCING NEW AS N
FOR EACH ROW
SET N.NomeCliente = (	SELECT 	NomeCliente
						FROM	PREVENTIVI
						WHERE	Numero = N.Numero);

CREATE OR REPLACE TRIGGER GeneraStatistiche
AFTER INSERT ON FATTURE
REFERENCING NEW AS N
FOR EACH ROW
INSERT INTO STATISTICHE
SELECT  N.CodFattura, 
		DAYS(N.Data) - DAYS(P.DataInizioLavori),
		((N.Importo - P.ImportoPrevisto)/P.ImportoPrevisto)*100
FROM	PREVENTIVI P
WHERE	P.Numero = N.Numero;
						
INSERT INTO FATTURE(CodFattura,Numero,DATA,Importo)
VALUES ('F1234',215,'14.03.2021',18000);

-------------------------------
-- ESERCIZIO 4 (facoltativo) --
-------------------------------

create table UTENTI (
     Nickname char(10) not null,
     Password char(10) not null,
     Punteggio INT not null,
     constraint ID_UTENTI_ID primary key (Nickname));

create table PARTITE (
     IDPartita INT not null,
     Esito char(1) CHECK (Esito IN ('B','N','P')), 
     	-- B (N) = vince il Bianco (Nero), P = patta
     PuntiVinceBianco INT not null,
     PuntiVinceNero INT not null,
     PuntiPatta INT not null,
     Nickname_B char(10) not null, -- nome modificato: Bianco
     Nickname_N char(10) not null,  -- nome modificato: Nero
     constraint ID_PARTITE_ID primary key (IDPartita));

create table COMMENTI (
     ID INT not null,
     Timestamp TIMESTAMP not null,
     Nickname char(10) not null,
     IDPartita INT not null,
     constraint ID_COMMENTI_ID primary key (ID));

-- Constraints Section
-- ___________________ 

alter table PARTITE add constraint FKGiocaBianco_FK
     foreign key (Nickname_B)
     references UTENTI;

alter table PARTITE add constraint FKGiocaNero_FK
     foreign key (Nickname_N)
     references UTENTI;
     
alter table COMMENTI add constraint FKUC_FK
     foreign key (Nickname)
     references UTENTI;

alter table COMMENTI add constraint FKPC_FK
     foreign key (IDPartita)
     references PARTITE;

-- Operazioni e trigger

INSERT INTO UTENTI 
VALUES('Paperoga','nonricordo',1600),
	  ('Archimede','a�$&()$P',2430);

-- V: vincitore, P: perdente
-- Caso 1: 
-- Se Punteggio(V) <= Punteggio(P): 5 + (Punteggio(P) � Punteggio(V))/20
-- Caso 2:
-- Se Punteggio(V) > Punteggio(P): 1 + Min{500/(Punteggio(V) � Punteggio(P)),10}

CREATE OR REPLACE TRIGGER CalcolaPunteggi
BEFORE INSERT ON PARTITE
REFERENCING NEW AS NewP
FOR EACH ROW
IF (( 	SELECT	B.Punteggio
	  	FROM	UTENTI B
	  	WHERE	B.Nickname = NewP.Nickname_B) <=
	( 	SELECT	N.Punteggio
	  	FROM	UTENTI N
	  	WHERE	N.Nickname = NewP.Nickname_N))
THEN -- siamo nel caso 1 per il bianco e 2 per il nero
SET NewP.PuntiVinceBianco = 
	5 + (( 	SELECT	N.Punteggio
	  		FROM	UTENTI N
	  		WHERE	N.Nickname = NewP.Nickname_N) -
	  	 ( 	SELECT	B.Punteggio
	  		FROM	UTENTI B
	  		WHERE	B.Nickname = NewP.Nickname_B))/20,
	NewP.PuntiVinceNero = 
	1 + MIN(500/
		(( 	SELECT	N.Punteggio
	  		FROM	UTENTI N
	  		WHERE	N.Nickname = NewP.Nickname_N) -
	  	 ( 	SELECT	B.Punteggio
	  		FROM	UTENTI B
	  		WHERE	B.Nickname = NewP.Nickname_B)),10);
ELSE -- siamo nel caso 2 per il bianco e 1 per il nero
SET NewP.PuntiVinceBianco =  
	1 + MIN(500/
		(( 	SELECT	B.Punteggio
	  		FROM	UTENTI B
	  		WHERE	B.Nickname = NewP.Nickname_B) -
	  	 ( 	SELECT	N.Punteggio
	  		FROM	UTENTI N
	  		WHERE	N.Nickname = NewP.Nickname_N)),10) ,
	NewP.PuntiVinceNero = 
	5 + (( 	SELECT	B.Punteggio
	  		FROM	UTENTI B
	  		WHERE	B.Nickname = NewP.Nickname_B) -
	  	 ( 	SELECT	N.Punteggio
	  		FROM	UTENTI N
	  		WHERE	N.Nickname = NewP.Nickname_N))/20;
END IF

-- In questa versione si fa uso di variabili locali al trigger, 
-- che evitano di ripetere le stesse query 3 volte
-- 
CREATE OR REPLACE TRIGGER CalcolaPunteggi
BEFORE INSERT ON PARTITE
REFERENCING NEW AS NewP
FOR EACH ROW
BEGIN ATOMIC
DECLARE PuntiB INT;
DECLARE PuntiN INT; 
SET PuntiB = 
 	(	SELECT	B.Punteggio
		FROM	UTENTI B
		WHERE	B.Nickname = NewP.Nickname_B);
SET PuntiN =
	(	SELECT	N.Punteggio
		FROM	UTENTI N
		WHERE	N.Nickname = NewP.Nickname_N);
--
IF (PuntiB <= PuntiN)
THEN -- siamo nel caso 1 per il bianco e 2 per il nero
SET NewP.PuntiVinceBianco = 
	5 + (PuntiN - PuntiB)/20,
	NewP.PuntiVinceNero = 
	1 + MIN(500/(PuntiN - PuntiB),10);
ELSE -- siamo nel caso 2 per il bianco e 1 per il nero
SET NewP.PuntiVinceBianco =  
	1 + MIN(500/(PuntiB - PuntiN),10) ,
	NewP.PuntiVinceNero = 
	5 + (PuntiB - PuntiN)/20;
END IF;
END


INSERT INTO PARTITE(IDPartita,PuntiPatta,Nickname_B,Nickname_N)
VALUES(54276,0,'Paperoga','Archimede');

CREATE OR REPLACE TRIGGER UpdatePunteggi
AFTER UPDATE OF Esito ON PARTITE
REFERENCING NEW AS NewP
FOR EACH ROW
IF (NewP.Esito = 'B')
THEN 
UPDATE 	UTENTI
SET		Punteggio = Punteggio + NewP.PuntiVinceBianco
WHERE	Nickname = NewP.Nickname_B ;
UPDATE 	UTENTI
SET		Punteggio = Punteggio - NewP.PuntiVinceBianco
WHERE	Nickname = NewP.Nickname_N ;
ELSE
UPDATE 	UTENTI
SET		Punteggio = Punteggio + NewP.PuntiVinceNero
WHERE	Nickname = NewP.Nickname_N ;
UPDATE 	UTENTI
SET		Punteggio = Punteggio - NewP.PuntiVinceNero
WHERE	Nickname = NewP.Nickname_B ;
END IF		

UPDATE 	PARTITE
SET		Esito = 'B'
WHERE	IDPartita = 54276;

INSERT INTO PARTITE(IDPartita,PuntiPatta,Nickname_B,Nickname_N)
VALUES(54277,0,'Archimede','Paperoga');

UPDATE 	PARTITE
SET		Esito = 'N'
WHERE	IDPartita = 54277;