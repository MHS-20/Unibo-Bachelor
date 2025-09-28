package it.unibo.paw.dao.db2;

import it.unibo.paw.dao.IdBroker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Db2IdBroker implements IdBroker{

	//PER UTILIZZARE SOLUZIONE CON IDBROKER DOVETE PRIMA AVER CREATO UNA SEQUENCE NEL DB
	//per farlo lanciare su dbeaver
	/*
	CREATE SEQUENCE sequenza_id
	AS INTEGER
	START WITH 1
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 9999999
	NO CYCLE; 
	*/
	
	//per eliminare la sequenza
	// DROP SEQUENCE sequenza_id;

	
	public int newId() throws PersistenceException {
		int newId = -1;
		ResultSet result = null;
		PreparedStatement statement = null;
		
		// --- Apertura della connessione ---
		Connection connection = Db2DAOFactory.createConnection();
		
		try {
			//Query per richiedere al db il nuovo valore dalla sequenza...provare (per credere) su dbeaver
			String sqlQuery = "SELECT NEXT VALUE FOR sequenza_id AS newId FROM SYSIBM.SYSDUMMY1";
			statement = connection.prepareStatement(sqlQuery);
			result = statement.executeQuery();
			if (result.next()) newId = result.getInt("newId");
			else throw new PersistenceException("invalid id");
		} catch(SQLException e) {
			throw new PersistenceException(e.getMessage());
		}
		finally {
			try {
				if (statement != null) statement.close();
				if (connection!= null) connection.close();
			} catch (SQLException e) {
				throw new PersistenceException(e.getMessage());
			}
		}
		
		return newId;
	}

}
