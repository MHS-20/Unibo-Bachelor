--
-- Script SQL: Soluzione dell'esercitazione 2 (L02)
-- 

--
-- ESERCIZIO 1
--
-- DDL: Creazione tabelle
--

CREATE TABLE MODELLI (
	MODELLO  VARCHAR(20) NOT NULL PRIMARY KEY,
	MARCA    VARCHAR(20) NOT NULL,
	CILINDRATA  INT NOT NULL CHECK (CILINDRATA > 0),
	ALIMENTAZIONE VARCHAR(10) NOT NULL,
	VELMAX	INT NOT NULL CHECK (VELMAX > 0),
	PREZZOLISTINO DEC(8,2) NOT NULL CHECK (PREZZOLISTINO > 0)	);

CREATE TABLE RIVENDITORI (	
	CODR CHAR(5) NOT NULL PRIMARY KEY,
	CITTA VARCHAR(30) NOT NULL		);

CREATE TABLE AUTO (	
	TARGA CHAR(7) NOT NULL PRIMARY KEY,
	MODELLO VARCHAR(20) NOT NULL REFERENCES MODELLI,
	CODR CHAR(5) NOT NULL REFERENCES RIVENDITORI,
	PREZZOVENDITA DEC(8,2) NOT NULL CHECK (PREZZOVENDITA > 0),
	KM INT NOT NULL CHECK (KM >= 0),
	ANNO INT NOT NULL CHECK (ANNO >= 1900),
	VENDUTA CHAR(2) CHECK (VENDUTA = 'SI')		);

--
-- INSERT (si ringrazia Andrea Masini per il contributo!)
--

INSERT INTO RIVENDITORI VALUES 
('RIV01', 'Venezia'),
('RIV02','Bologna'),
('RIV03','Bologna'),
('RIV04','Rimini')
;

INSERT INTO MODELLI
VALUES 
('Agila', 'Opel', 998, 'Benzina', 180, 12000.00),
('Aventador', 'Lamborghini', 6498, 'Benzina', 350, 432729.00),
('Ghibli', 'Maserati', 3799, 'Benzina', 326, 150000.00),
('Stratos', 'Lancia', 2419, 'Benzina', 230, 130000.00);

INSERT INTO AUTO VALUES 
('AG123AG', 'Agila', 'RIV03', 10500.00, 50000, 2003, NULL),
('AG234AG', 'Agila', 'RIV03', 9000.00, 70000, 2003, NULL),
('AV456AV', 'Aventador', 'RIV02',430000.00, 0, 2017, NULL),
('AV567AV', 'Aventador', 'RIV02', 400000.00, 0, 2015, 'SI'),
('GH789GH', 'Ghibli', 'RIV01', 90000.00, 0, 2015, 'SI'),
('GH890GH', 'Ghibli', 'RIV02', 100000.00, 30000, 2013, NULL),
('GH901GH', 'Ghibli','RIV03', 70000.00, 50000, 2015, 'SI'),
('ST123ST', 'Stratos', 'RIV04', 80000.00, 15000, 1997, 'SI'),
('ST234ST', 'Stratos','RIV04', 95000.00, 70000, 2012, 'SI');

--
-- INTERROGAZIONI
--

-- Q1) Le Maserati ancora in vendita a Bologna a un prezzo inferiore al 70% del listino
SELECT 	M.*,A.*
FROM   	MODELLI M, RIVENDITORI R, AUTO A
WHERE  	M.MODELLO = A.MODELLO
AND		A.CODR = R.CODR
AND		M.MARCA = 'Maserati'
AND		R.CITTA = 'Bologna'
AND		A.PREZZOVENDITA < 0.7*M.PREZZOLISTINO
AND		A.VENDUTA IS NULL		;

-- Q2) Il prezzo medio di un auto a benzina con cilindrata (cc) < 1000, almeno 5 anni di vita 
--	e meno di 80000 Km
SELECT 	AVG(A.PREZZOVENDITA) AS PREZZOMEDIO
FROM 	MODELLI M, AUTO A
WHERE  	M.MODELLO = A.MODELLO
AND		M.ALIMENTAZIONE = 'Benzina'
AND		M.CILINDRATA < 1000
AND		A.KM < 80000
AND		YEAR(CURRENT DATE) - A.ANNO >= 5 	;

-- Q3) Per ogni modello con velocità massima > 180 Km/h, il prezzo più basso a Bologna
SELECT 	M.MODELLO, MIN(A.PREZZOVENDITA) AS PREZZOMIN_BO
FROM 	MODELLI M, RIVENDITORI R, AUTO A
WHERE  	M.MODELLO = A.MODELLO
AND		A.CODR = R.CODR
AND		R.CITTA = 'Bologna'
AND		M.VELMAX > 180
GROUP BY M.MODELLO 		;

-- Q4) Il numero di auto complessivamente trattate e vendute in ogni città
SELECT 	R.CITTA, COUNT(*) AS NUM_TRATTATE, COUNT(A.VENDUTA) AS NUM_VENDUTE
FROM 	RIVENDITORI R, AUTO A
WHERE  	A.CODR = R.CODR
GROUP BY R.CITTA 		;

-- Q5) I rivenditori che hanno ancora in vendita almeno il 20% delle auto complessivamente
--      trattate, ordinando il risultato per città e quindi per codice rivenditore
SELECT 	R.CODR, R.CITTA
FROM 	RIVENDITORI R, AUTO A
WHERE  	A.CODR = R.CODR
GROUP BY R.CODR, R.CITTA   	-- si raggruppa anche su CITTA per poter ordinare su tale attributo
HAVING	 COUNT(A.VENDUTA) <= 0.8*COUNT(*)
ORDER BY R.CITTA, R.CODR			;

-- Q6) I rivenditori che hanno disponibili auto di modelli mai venduti prima da loro 
SELECT 	DISTINCT A.CODR
FROM 	AUTO A
GROUP BY A.CODR, A.MODELLO
HAVING	 COUNT(A.VENDUTA) = 0		;

-- Q7) Per ogni rivenditore, il numero di auto vendute, solo se il prezzo medio di tali auto 
--	risulta minore di 12000 Euro 
SELECT 	A.CODR, COUNT(*) AS NUM_VENDUTE
FROM 	AUTO A
WHERE  	A.VENDUTA = 'SI'
GROUP BY A.CODR
HAVING	 AVG(A.PREZZOVENDITA) < 12000		;
-- provare con 90000

-- Q8) Per ogni auto A, il numero di auto vendute a un prezzo minore di quello di A 
SELECT 	A1.TARGA, COUNT(A2.TARGA) AS NUM_PREZZOMINORE
FROM 	AUTO A1 LEFT JOIN AUTO A2 ON (A1.PREZZOVENDITA > A2.PREZZOVENDITA) 
								 AND (A2.VENDUTA = 'SI')
GROUP BY A1.TARGA
ORDER BY NUM_PREZZOMINORE DESC;
-- il left join permette di vedere tutte le auto 

-- Q9) Per ogni anno e ogni modello, il rapporto medio tra prezzo di vendita e prezzo di listino,
--	considerando un minimo di 2 auto vendute 
SELECT 	M.MODELLO, A.ANNO, AVG(A.PREZZOVENDITA/M.PREZZOLISTINO) AS AVG_RAPPORTO
FROM 	MODELLI M, AUTO A
WHERE  	M.MODELLO = A.MODELLO
GROUP BY M.MODELLO, A.ANNO 		
HAVING	 COUNT(A.VENDUTA) >= 2	;

DROP TABLE MODELLI;
DROP TABLE RIVENDITORI;
DROP TABLE AUTO;


