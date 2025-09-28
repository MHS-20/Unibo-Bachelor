--
--
-- Script SQL: Soluzione dell'esercitazione 5 (L05)
--
-- Esercizio 2: Grant in cascata
--

-- Versione 1
-- Determinare da chi si e' ricevuto il GRANT direttamente o indirettamente 
-- e a che distanza minima si trova

-- Si presentano 2 soluzioni: 
-- nella prima la C.T.E. ricorsiva calcola tutti i percorsi, e quindi il risultato
-- si ottiene filtrando con il valore di CURRENT USER (come GRANTEE);
-- nella seconda la C.T.E. ricorsiva calcola solo i percorsi relativi al CURRENT USER

-- prima soluzione (tutti i percorsi)
--
WITH DACHI(From, To, Depth) AS (
SELECT T.GRANTOR, T.GRANTEE, 1
FROM   SYSCAT.TABAUTH T
WHERE  T.TABSCHEMA = 'B16884' 
AND    T.TABNAME = 'CASCATA'
AND    T.GRANTOR <> 'SYSIBM'
	   UNION ALL
SELECT T.GRANTOR, D.To, D.Depth+1
FROM   SYSCAT.TABAUTH T, DACHI D
WHERE  T.TABSCHEMA = 'B16884'
AND    T.TABNAME = 'CASCATA'
AND    D.From = T.GRANTEE
AND    T.GRANTOR <> 'SYSIBM'
AND    D.Depth+1 <= 10
)
SELECT   D.From, MIN(D.Depth) AS Dist
FROM     DACHI D
WHERE	 D.To = CURRENT USER
GROUP BY D.From
ORDER BY Dist;

-- seconda soluzione (solo i percorsi del CURRENT USER)
--
WITH DACHI(From, Depth) AS (
SELECT T.GRANTOR, 1
FROM   SYSCAT.TABAUTH T
WHERE  T.TABSCHEMA = 'B16884' 
AND    T.TABNAME = 'CASCATA'
AND    T.GRANTEE = CURRENT USER  
AND    T.GRANTOR <> 'SYSIBM'
	   UNION ALL
SELECT T.GRANTOR, D.Depth+1
FROM   SYSCAT.TABAUTH T, DACHI D
WHERE  T.TABSCHEMA = 'B16884'
AND    T.TABNAME = 'CASCATA'
AND    D.From = T.GRANTEE
AND    T.GRANTOR <> 'SYSIBM'
AND    D.Depth+1 <= 10
)
SELECT   D.From, MIN(D.Depth) AS Dist
FROM     DACHI D
GROUP BY D.From
ORDER BY Dist;

-- Determinare a chi si e' concesso il GRANT direttamente o indirettamente 
-- e a che distanza minima si trova.

-- Nella prima soluzione (non riportata) la C.T.E. è uguale alla precedente 
-- (qui la chiamiamo ACHI), e basta quindi scrivere:
-- 
/* 
WITH ACHI(From, To, Depth) AS ( .... )
SELECT   A.To, MIN(A.Depth) AS Dist
FROM     ACHI A
WHERE	 A.From = CURRENT USER
GROUP BY A.To
ORDER BY Dist;
*/

-- la seconda soluzione calcola solo i percorsi relativi al CURRENT USER
--
WITH ACHI(To, Depth) AS (
SELECT T.GRANTEE, 1
FROM   SYSCAT.TABAUTH T
WHERE  T.TABSCHEMA = 'B16884' 
AND    T.TABNAME = 'CASCATA'
AND    T.GRANTOR = CURRENT USER
	   UNION ALL
SELECT T.GRANTEE, A.Depth+1
FROM   SYSCAT.TABAUTH T, ACHI A
WHERE  T.TABSCHEMA = 'B16884' 
AND    T.TABNAME = 'CASCATA'
AND    A.To = T.GRANTOR
AND    A.Depth+1 <= 10
)
SELECT   A.To , MIN(A.Depth) AS Dist
FROM     ACHI A
GROUP BY A.To
ORDER BY Dist;

-- Versione 2
-- Come 1, ma aggiungendo informazione, in forma di stringhe, 
-- sui percorsi (catene di usernames) 

-- si presenta solo la soluzione con i percorsi relativi al CURRENT USER
-- (idem per le versioni 3 e 4)
-- 
WITH DACHI(From, Depth, Path) AS (
SELECT T.GRANTOR, 1, varchar(T.GRANTOR,2000)
FROM   SYSCAT.TABAUTH T
WHERE  T.TABSCHEMA = 'B16884' 
AND    T.TABNAME = 'CASCATA'
AND    T.GRANTEE = CURRENT USER
AND    T.GRANTOR <> 'SYSIBM'
	   UNION ALL
SELECT T.GRANTOR, D.Depth+1, T.GRANTOR || ' - ' || Path -- aggiunge GRANTOR in testa al Path
FROM   SYSCAT.TABAUTH T, DACHI D
WHERE  T.TABSCHEMA = 'B16884'
AND    T.TABNAME = 'CASCATA'
AND    D.From = T.GRANTEE
AND    T.GRANTOR <> 'SYSIBM'
AND    D.Depth+1 <= 10
)
SELECT D.From, D.Depth, D.Path
FROM   DACHI D
WHERE  (D.From, D.Depth) IN ( 
			SELECT D.From, MIN(D.Depth) AS Dist
			FROM   DACHI D
			GROUP BY D.From )
ORDER BY D.Depth;
-- con i Path non è possibile raggruppare come nella versione 1

-- La seconda query, che usa ACHI, è simile, e non viene dettagliata 
-- (idem per le versioni 3 e 4)

-- Versione 3
-- Come 2, ma evitando la formazione di cicli

WITH DACHI(From, Depth, Path) AS (
SELECT T.GRANTOR, 1, varchar(T.GRANTOR,2000)
FROM   SYSCAT.TABAUTH T
WHERE  T.TABSCHEMA = 'B16884' 
AND    T.TABNAME = 'CASCATA'
AND    T.GRANTEE = CURRENT USER
AND    T.GRANTOR <> 'SYSIBM'
	   UNION ALL
SELECT T.GRANTOR, D.Depth+1, T.GRANTOR || ' - ' || Path -- aggiunge GRANTOR in testa al Path
FROM   SYSCAT.TABAUTH T, DACHI D
WHERE  T.TABSCHEMA = 'B16884'
AND    T.TABNAME = 'CASCATA'
AND    D.From = T.GRANTEE
AND    T.GRANTOR <> 'SYSIBM'
AND	   D.Path NOT LIKE '%' || T.GRANTOR || '%' -- evita di aggiungere T.GRANTOR al Path se già presente
)
SELECT D.From, D.Depth, D.Path
FROM   DACHI D
WHERE  (D.From, D.Depth) IN ( 
			SELECT D.From, MIN(D.Depth) AS Dist
			FROM   DACHI D
			GROUP BY D.From )
ORDER BY D.Depth;

-- Versione 4
-- Come 3, ma usando catene di nicknames anziché di usernames

WITH DACHI(From, Depth, NickPath) AS (
SELECT T.GRANTOR, 1, varchar(C.NICK,2000)
FROM   SYSCAT.TABAUTH T, B16884.CASCATA C
WHERE  T.TABSCHEMA = 'B16884' 
AND    T.TABNAME = 'CASCATA'
AND    T.GRANTEE = CURRENT USER 
AND    T.GRANTOR <> 'SYSIBM'
AND	   T.GRANTOR = C.USERNAME
	   UNION ALL
SELECT T.GRANTOR, D.Depth+1, C.NICK || ' - ' || NickPath -- aggiunge NICK in testa al Path
FROM   SYSCAT.TABAUTH T, DACHI D, B16884.CASCATA C
WHERE  T.TABSCHEMA = 'B16884'
AND    T.TABNAME = 'CASCATA'
AND    D.From = T.GRANTEE
AND    T.GRANTOR <> 'SYSIBM'
AND	   T.GRANTOR = C.USERNAME
AND	   D.NickPath NOT LIKE '%' || C.NICK || '%' -- evita di riaggiungere C.NICK al NickPath
AND    D.Depth+1 <= 40
)
SELECT C.NICK AS CHIMELHAPASSATO, D.Depth, D.NickPath
FROM   DACHI D JOIN B16884.CASCATA C ON (D.From = C.USERNAME)
WHERE (D.From, D.Depth) IN ( 
			SELECT D.From, MIN(D.Depth) AS Dist
			FROM   DACHI D
			GROUP BY D.From )
ORDER BY D.Depth ;
