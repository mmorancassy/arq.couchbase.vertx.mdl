package es.arq.persistence.provider;

import java.util.Map;

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
	 * Disconnect from database
	 * 
	 * @return
	 * @throws PersistenceException
	 */
	public boolean disconnect() throws PersistenceException;
	
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
	 * @param documentId
	 * @param document
	 * @return
	 */
	public String update(String documentId, String document) throws PersistenceException;
	
	/**
	 * Retrieves JSON document
	 * 
	 * @param documentId
	 * @return
	 */
	public String getById(String documentId) throws PersistenceException;
	
	/**
	 * Retrieves JSON document Map<id,json>
	 * 
	 * @param query
	 * @param limit
	 * @return
	 */
	public Map<String, String> query(String query, int limit) throws PersistenceException;
}
