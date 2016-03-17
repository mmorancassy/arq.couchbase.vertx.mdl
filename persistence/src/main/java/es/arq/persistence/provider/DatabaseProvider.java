package es.arq.persistence.provider;

import es.arq.persistence.provider.exceptions.PersistenceException;

public interface DatabaseProvider {

	/**
	 * Returns connection to a specific database
	 * 
	 * @return
	 * @throws PersistenceException
	 */
	public DatabaseProvider getConnection() throws PersistenceException;
	
	/**
	 * Inserts JSON document
	 * 
	 * @param document
	 * @return
	 */
	public String insert(String document) throws PersistenceException;
	
	/**
	 * Deletes JSON document
	 * 
	 * @param documentId
	 * @return
	 */
	public boolean delete(String documentId) throws PersistenceException;
	
	/**
	 * Updates JSON document
	 * 
	 * @param document
	 * @return
	 */
	public String update(String document) throws PersistenceException;
	
	/**
	 * Updates JSON document or creates if not exists
	 * 
	 * @param document
	 * @return
	 */
	public String upsert(String document) throws PersistenceException;
	
	/**
	 * Retrieves JSON document
	 * 
	 * @param documentId
	 * @return
	 */
	public String query(String documentId) throws PersistenceException;
}
