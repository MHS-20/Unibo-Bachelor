package it.unibo.paw.dao;

import it.unibo.paw.dao.db2.PersistenceException;

public interface IdBroker {

	public int newId() throws PersistenceException;
	
}
