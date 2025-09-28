--
-- Script SQL: Soluzione dell'esercitazione 3 (L03)
-- Si assume che sia stata preventivamente eseguita la connessione 
-- al DB SAMPLE (schema DB2INST1)
--

--
-- ESERCIZIO 1
--
-- INTERROGAZIONI
--

-- Q1)	Il numero dei dipartimenti con almeno 7 dipendenti
SELECT	COUNT(*) AS NUM_DIP_7_EMP
FROM	DEPARTMENT D
WHERE	D.DEPTNO IN	
	     ( 	SELECT	E.WORKDEPT
		FROM 	EMPLOYEE E
		GROUP BY E.WORKDEPT
		HAVING 	 COUNT(*) >= 7 )	; 

-- Q2)	I dati dei dipendenti che lavorano in un dipartimento con almeno 7 dipendenti
SELECT 	E.*
FROM	EMPLOYEE E
WHERE	E.WORKDEPT IN	
	     ( 	SELECT	E1.WORKDEPT
		FROM 	EMPLOYEE E1
		GROUP BY E1.WORKDEPT
		HAVING 	 COUNT(*) >= 7 )	; 

-- Q3) 	I dati del dipartimento con il maggior numero di dipendenti
SELECT	D.*
FROM	DEPARTMENT D
WHERE	D.DEPTNO IN
	     ( 	SELECT	E.WORKDEPT
		FROM 	EMPLOYEE E
		GROUP BY E.WORKDEPT
		HAVING 	 COUNT(*) >= ALL 
			    (	SELECT COUNT(*)
				FROM EMPLOYEE E1
				GROUP BY E1.WORKDEPT  ) )	; 

-- Q4)	Il nome delle regioni e il totale delle vendite per ogni regione 
--	con un totale di vendite maggiore di 30, ordinando per totale vendite decrescente
SELECT 	REGION, SUM(SALES) AS SUM_SALES
FROM 	SALES
GROUP BY REGION
HAVING 	 SUM(SALES) > 30
ORDER BY SUM_SALES DESC	;

-- Q5) 	Lo stipendio medio degli impiegati che non sono manager di nessun dipartimento
SELECT	CAST(AVG(SALARY) AS DEC(9,2)) AS AVG_SALARY
FROM 	EMPLOYEE
WHERE 	EMPNO NOT IN
	     (	SELECT	MGRNO
		FROM 	DEPARTMENT
		WHERE 	MGRNO IS NOT NULL)	;
-- commento: se si omette MGRNO IS NOT NULL la query non restituisce nulla!
-- Questo perche' NOT IN equivale a <> ALL e se il risultato della subquery
-- contiene valori nulli allora EMPNO NOT IN (..) e' UNKNOWN

-- Q6)	I dipartimenti che non hanno impiegati il cui cognome inizia per 'L'
SELECT	D.*
FROM 	DEPARTMENT D
WHERE 	NOT EXISTS 
	     (	SELECT 	*
		FROM 	EMPLOYEE E
		WHERE 	E.WORKDEPT = D.DEPTNO
		AND 	E.LASTNAME LIKE 'L%'  )	;

-- Q7)	I dipartimenti e il rispettivo massimo stipendio  per tutti i dipartimenti 
--	aventi un salario medio minore del salario medio calcolato considerando 
--	i dipendenti di tutti gli altri dipartimenti
SELECT 	E.WORKDEPT, MAX(E.SALARY) AS MAX_SALARY
FROM 	EMPLOYEE E
GROUP BY E.WORKDEPT
HAVING 	 AVG(E.SALARY) < 
	     (	SELECT 	AVG(E1.SALARY)
		FROM 	EMPLOYEE E1
		WHERE 	E1.WORKDEPT <> E.WORKDEPT   )	;

-- Q8)  Per ogni dipartimento determinare lo stipendio medio per ogni lavoro per il quale 
--	il livello di educazione medio e' maggiore di quello degli impiegati 
--	dello stesso dipartimento che fanno un lavoro differente
SELECT 	E.WORKDEPT, E.JOB, CAST(AVG(E.SALARY) AS DEC(9,2)) AS AVG_SALARY
FROM 	EMPLOYEE E
GROUP BY E.WORKDEPT, E.JOB
HAVING   AVG(E.EDLEVEL/1.0) > 
	  (	SELECT 	AVG(E1.EDLEVEL/1.0)
		FROM 	EMPLOYEE E1
		WHERE 	E1.WORKDEPT = E.WORKDEPT
		AND 	E1.JOB <> E.JOB	)	;

-- Q9)	Lo stipendio medio degli impiegati che non sono addetti alle vendite
SELECT 	CAST(AVG(E.SALARY) AS DEC(9,2)) AS AVG_SALARY
FROM 	EMPLOYEE E
WHERE	E.LASTNAME NOT IN 
	  (	SELECT 	S.SALES_PERSON
		FROM 	SALES S        )	;

-- Q10)	Per ogni regione, i dati dell’impiegato che ha il maggior numero di vendite 
--	(SUM(SALES)) in quella regione
SELECT 	DISTINCT S.REGION, E.*
FROM	EMPLOYEE E, SALES S
WHERE	E.LASTNAME = S.SALES_PERSON
AND	(S.SALES_PERSON, S.REGION) IN
	     (	SELECT 	S1.SALES_PERSON, S1.REGION
		FROM	SALES S1
		GROUP BY S1.SALES_PERSON, S1.REGION
		HAVING	 SUM(S1.SALES) >= ALL
		     (	SELECT 	SUM(S2.SALES)
			FROM	SALES S2
			WHERE	S2.REGION = S1.REGION
			GROUP BY S2.SALES_PERSON	 ) )	;

-- Q11) I codici dei dipendenti che svolgono un'attivita' per la quale ogni tupla di EMPPROJACT 
--	riguarda un periodo minore di 200 giorni
SELECT 	DISTINCT E.EMPNO
FROM	EMPPROJACT E
WHERE	NOT EXISTS
	(	SELECT	*
		FROM	EMPPROJACT E1
		WHERE	E1.ACTNO = E.ACTNO
		AND	DAYS(E1.EMENDATE) - DAYS(E1.EMSTDATE) >= 200	)	;

-- Soluzione alternativa
SELECT 	DISTINCT E.EMPNO
FROM	EMPPROJACT E
WHERE	E.ACTNO IN
	(	SELECT	 E1.ACTNO
		FROM	 EMPPROJACT E1
		GROUP BY E1.ACTNO
		HAVING   MAX(DAYS(E1.EMENDATE) - DAYS(E1.EMSTDATE)) < 200 )	;


