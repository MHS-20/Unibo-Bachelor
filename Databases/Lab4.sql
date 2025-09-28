--
-- Script SQL: Soluzione dell'esercitazione 4 (L04.2)
--
-- Per evitare ambiguita', table e view sono sempre referenziate includendo 
-- anche il nome dello schema (SYSCAT)

--
-- ESERCIZIO 1
--

-- Q1)	Determinare per ogni table (type = 'T') il numero di foreign key, 
-- 	ignorando quelle autoreferenziali (escludere le tabelle con 0 foreign key) 
-- 	e ordinando per valori decrescenti (a parità per nome di schema e di tabella)

SELECT 	 TABSCHEMA, TABNAME, (PARENTS-SELFREFS) AS NUMFK
FROM 	 SYSCAT.TABLES
WHERE 	 TYPE = 'T'
AND 	 PARENTS > 0 
ORDER BY NUMFK DESC, TABSCHEMA, TABNAME;

-- Q2)	Mostrare gli schemi con almeno 5 table o view, ordinando in senso decrescente 
--	per numero totale di oggetti 
SELECT 	 T.TABSCHEMA, COUNT(*) AS NUMOBJECTS 
FROM 	 SYSCAT.TABLES T
WHERE 	 T.TYPE IN ('T','V')
GROUP BY T.TABSCHEMA
HAVING   COUNT(*) >= 5
ORDER BY NUMOBJECTS DESC;

-- Q3)	Per ogni vista di SYSCAT, determinare da quanti oggetti 
--	di ciascun tipo dipende
SELECT 	 T.TABNAME, T.BTYPE, COUNT(*) AS NUMDEP
FROM 	 SYSCAT.TABDEP T
WHERE 	 T.TABSCHEMA = 'SYSCAT'
GROUP BY T.TABNAME, T.BTYPE
ORDER BY NUMDEP DESC;

-- Q4)	Senza usare l'attributo TABLES.COLCOUNT, ne' viste, 
--	determinare la table (TYPE = 'T') con il maggior numero di colonne

--
-- 	Usando una Common Table Expression:
--
WITH TABLECOLUMNS(TABSCHEMA, TABNAME, NUMCOLS) 
AS (	SELECT C.TABSCHEMA, C.TABNAME, COUNT(*)
	FROM SYSCAT.COLUMNS C, SYSCAT.TABLES T
	WHERE (T.TABSCHEMA,T.TABNAME) = (C.TABSCHEMA,C.TABNAME)
	AND T.TYPE = 'T'
	GROUP BY C.TABSCHEMA, C.TABNAME
   )
SELECT	*
FROM 	TABLECOLUMNS
WHERE 	NUMCOLS = (	SELECT 	MAX(NUMCOLS)
			FROM 	TABLECOLUMNS	)	;

--
--	In alternativa:
--
SELECT 	 C.TABSCHEMA, C.TABNAME, COUNT(*) AS NUMCOLS
FROM 	 SYSCAT.COLUMNS C, SYSCAT.TABLES T
WHERE  	 (T.TABSCHEMA,T.TABNAME) = (C.TABSCHEMA,C.TABNAME)
AND 	 T.TYPE = 'T'
GROUP BY C.TABSCHEMA, C.TABNAME
HAVING   COUNT(*) >= ALL
	    (	SELECT	 COUNT(*)
		FROM 	 SYSCAT.COLUMNS C, SYSCAT.TABLES T
		WHERE 	 (T.TABSCHEMA,T.TABNAME) = (C.TABSCHEMA,C.TABNAME)
		AND 	 T.TYPE = 'T'
		GROUP BY C.TABSCHEMA, C.TABNAME	)	;



-- Q5)	Per ogni tipo di dato (COLUMNS.TYPENAME), il numero di oggetti 
--	in cui quel tipo di dato è il più usato 
WITH	
TYPECOUNT(SCHEMA,TABLE,TYPE,NUM) 
AS (	SELECT	 TABSCHEMA, TABNAME, TYPENAME, COUNT(*)
	FROM	 SYSCAT.COLUMNS
	GROUP BY TABSCHEMA, TABNAME, TYPENAME),
TYPEWINS(SCHEMA,TABLE,TYPE) -- il tipo piu' frequente in una table; SCHEMA e TABLE non sono necessari in output
AS (	SELECT 	T.SCHEMA, T.TABLE, T.TYPE
	FROM 	TYPECOUNT T
	WHERE 	T.NUM >= ALL 
	    (	SELECT 	T1.NUM 
		FROM 	TYPECOUNT T1 
		WHERE (T1.SCHEMA,T1.TABLE) = (T.SCHEMA,T.TABLE)	)  )
SELECT	 TYPE, COUNT(*) AS NUM_TABLES
FROM 	 TYPEWINS
GROUP BY TYPE
ORDER BY NUM_TABLES DESC ;



-- Q6)	La coppia di nomi di table che compaiono più frequentemente insieme 
--	in uno stesso schema
WITH TABLEPAIRS(TABLE1, TABLE2, NUMSCHEMA) 
AS (	SELECT 	T1.TABNAME, T2.TABNAME, COUNT(*)
	FROM 	SYSCAT.TABLES T1, SYSCAT.TABLES T2
	WHERE 	T1.TABSCHEMA = T2.TABSCHEMA
	AND	T1.TABNAME < T2.TABNAME  -- < anziché <> per evitare di duplicare le coppie
	AND 	T1.TYPE = 'T'
	AND	T2.TYPE = 'T'
	GROUP BY T1.TABNAME, T2.TABNAME
   )
SELECT	*
FROM 	TABLEPAIRS
WHERE 	NUMSCHEMA = (	SELECT 	MAX(NUMSCHEMA)
			FROM 	TABLEPAIRS	)	;

-- Q7)	Determinare la table più "popolare" su SIT_STUD, fornendo il nome
--	e i timestamp di creazione minimo e massimo
WITH TABLENUMS(TABLE,NUMSCHEMA,MIN_TIMESTAMP,MAX_TIMESTAMP)
AS (	SELECT 	 T.TABNAME, COUNT(*) AS NUMSCHEMA, MIN(CREATE_TIME), MAX(CREATE_TIME) 
	FROM 	 SYSCAT.TABLES T
	WHERE 	 T.TYPE = 'T'
	GROUP BY T.TABNAME	)
SELECT	*
FROM 	TABLENUMS
WHERE 	NUMSCHEMA = (	SELECT 	MAX(NUMSCHEMA)
			FROM 	TABLENUMS	)	;


-- 
--
-- Esercizio 2
-- Si assume che l'utente sia gia' connesso al database SIT_STUD
--

-- Q1)	Definire una vista che mostri, per ogni table o view nel proprio schema, 
--	il numero di (altre) table o view con lo stesso nome nel DB SIT_STUD, 
--	create prima (SAMEBEFORE) e dopo (SAMEAFTER)
-- 
CREATE OR REPLACE VIEW CAT_SAMENAME(TABNAME, SAMEBEFORE, SAMEAFTER) 
AS (	SELECT	COALESCE(TA.TABNAME,TB.TABNAME), TB.NUMBEFORE, TA.NUMAFTER
	FROM 	( SELECT M.TABNAME, COUNT(*)
		  FROM   SYSCAT.TABLES M, SYSCAT.TABLES Y
		  WHERE	 M.TABSCHEMA = CURRENT USER
		  AND	 Y.TABSCHEMA <> M.TABSCHEMA
		  AND 	 M.TABNAME = Y.TABNAME
		  AND    M.CREATE_TIME > Y.CREATE_TIME 
		  GROUP BY M.TABNAME ) AS TB(TABNAME, NUMBEFORE) 
		FULL JOIN
		( SELECT M.TABNAME, COUNT(*)
		  FROM   SYSCAT.TABLES M, SYSCAT.TABLES Y
		  WHERE	 M.TABSCHEMA = CURRENT USER
		  AND	 Y.TABSCHEMA <> M.TABSCHEMA
		  AND 	 M.TABNAME = Y.TABNAME
		  AND    M.CREATE_TIME < Y.CREATE_TIME 
		  GROUP BY M.TABNAME ) AS TA(TABNAME, NUMAFTER)
		ON (TA.TABNAME = TB.TABNAME)	) ;
-- La funzione COALESCE restituisce il primo valore non nullo

-- Q2)	Definire una vista che mostri per ogni table o view del proprio schema e definita 
--	con lo stesso nome anche in altri schemi, le eventuali differenze esistenti 
--	sui nomi degli attributi definiti
--
CREATE OR REPLACE VIEW CAT_TABDIFF(MYTABLE, YOURSCHEMA, DIFFCOLUMN, DIFF) 
AS (	SELECT	DISTINCT M.TABNAME, Y.TABSCHEMA, M.COLNAME, '-'
	FROM 	SYSCAT.COLUMNS M, SYSCAT.COLUMNS Y
	WHERE 	M.TABSCHEMA = CURRENT USER
	AND	Y.TABSCHEMA <> M.TABSCHEMA
	AND 	M.TABNAME = Y.TABNAME
	AND	NOT EXISTS ( SELECT *
			     FROM   SYSCAT.COLUMNS Y2
			     WHERE  Y2.COLNAME = M.COLNAME
			     AND    Y2.TABNAME = Y.TABNAME
			     AND    Y2.TABSCHEMA = Y.TABSCHEMA)
    UNION ALL
	SELECT	DISTINCT M.TABNAME, Y.TABSCHEMA, Y.COLNAME, '+'
	FROM 	SYSCAT.COLUMNS M, SYSCAT.COLUMNS Y
	WHERE 	M.TABSCHEMA = CURRENT USER
	AND	Y.TABSCHEMA <> M.TABSCHEMA
	AND 	M.TABNAME = Y.TABNAME
	AND	NOT EXISTS ( SELECT *
			     FROM   SYSCAT.COLUMNS Y2
			     WHERE  Y2.COLNAME = Y.COLNAME
			     AND    Y2.TABNAME = M.TABNAME
			     AND    Y2.TABSCHEMA = M.TABSCHEMA)		) ;
