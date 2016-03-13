package es.arq.persistence.provider;

public interface DatabaseProvider {

	public DatabaseProvider getConnection();
	
	/**
	 * Inserts JSON document
	 * 
	 * @param document
	 * @return
	 */
	public String insert(String document);
	
	/**
	 * Deletes JSON document
	 * 
	 * @param documentId
	 * @return
	 */
	public String delete(String documentId);
	
	/**
	 * Updates JSON document
	 * 
	 * @param document
	 * @return
	 */
	public String update(String document);
	
	/**
	 * Retrieves JSON document
	 * 
	 * @param documentId
	 * @return
	 */
	public String getDocument(String documentId);
}
