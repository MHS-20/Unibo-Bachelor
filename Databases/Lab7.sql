--
-- Script SQL: Soluzione dell'esercitazione 7 (L07)
--
-- Esercizio 3
--

-- PARTE 1

create table AUTO (
     Targa char(7) not null,
     Anno INT not null,
     Km INT not null,
     PrezzoRichiesto dec(8,2) not null,
     Casa varchar(30) not null,
     Nome varchar(30) not null,
     constraint ID_AUTO_ID primary key (Targa));

create table ESPERTO (
     Casa varchar(30) not null,
     Nome varchar(30) not null,
     CF char(16) not null,
     constraint ID_ESPERTO_ID primary key (CF, Casa, Nome));

create table MODELLI (
     Casa varchar(30) not null,
     Nome varchar(20) not null,
     constraint ID_MODELLI__ID primary key (Casa, Nome));

create table VENDITA (
     Targa char(7) not null,
     PrezzoVendita DEC(8,2) not null,
     CF char(16) not null,
     constraint FKVEN_AUT_ID primary key (Targa));

create table VENDITORI (
     CF char(16) not null,
     Nome varchar(30) not null,
     Cognome varchar(30) not null,
     constraint ID_VENDITORI_ID primary key (CF));

alter table AUTO add constraint FKMA_FK
     foreign key (Casa, Nome)
     references MODELLI;

alter table ESPERTO add constraint FKESP_VEN
     foreign key (CF)
     references VENDITORI;

alter table ESPERTO add constraint FKESP_MOD_FK
     foreign key (Casa, Nome)
     references MODELLI;

alter table VENDITA add constraint FKVEN_VEN_FK
     foreign key (CF)
     references VENDITORI;

alter table VENDITA add constraint FKVEN_AUT_FK
     foreign key (Targa)
     references AUTO;

-- PARTE 2

-- Definire una vista che, per ogni venditore, 
-- fornisca il numero di auto vendute per ogni modello
CREATE VIEW NUMAUTO(CF,Casa,NomeModello,NAuto) AS ( 
SELECT	V.CF, A.Casa, A.Nome, COUNT(*)
FROM	VENDITA V JOIN AUTO A ON (V.Targa = A.Targa)
GROUP BY V.CF, A.Casa, A.Nome
)

-- Definire un trigger che vieti di vendere auto 
-- a un prezzo maggiore di quello richiesto
CREATE OR REPLACE TRIGGER PrezzoTroppoAlto
BEFORE INSERT ON VENDITA
REFERENCING NEW AS NV
FOR EACH ROW
WHEN (NV.PrezzoVendita > (	SELECT	A.PrezzoRichiesto
							FROM	AUTO A
							WHERE	A.Targa = NV.Targa ) )
SIGNAL SQLSTATE '70001' ('Non e'' possibile vendere un''auto a un prezzo maggiore di quello richiesto!');

-- Definire un trigger che, all’atto dell’inserimento di una nuova auto, 
-- calcoli il prezzo richiesto come media di quelli di altre auto 
-- dello stesso modello (se non ce ne sono lasciare il valore fornito in input, 
-- ma se questo non è presente segnalare errore)
CREATE OR REPLACE TRIGGER NuovaAuto
BEFORE INSERT ON AUTO
REFERENCING NEW AS NA
FOR EACH ROW
IF EXISTS (	SELECT	*
			FROM	AUTO A 
			WHERE	(A.Casa,A.Nome)=(NA.Casa,Na.Nome) )
THEN	SET NA.PrezzoRichiesto = (	SELECT 	AVG(A.PrezzoRichiesto)
									FROM	AUTO A
									WHERE	(A.Casa,A.Nome)=(NA.Casa,Na.Nome) );
ELSE	IF NA.PrezzoRichiesto IS NULL
		THEN SIGNAL SQLSTATE '70002' ('Il prezzo della prima auto di un modello deve essere definito!');
		END IF
	;
END IF
		

