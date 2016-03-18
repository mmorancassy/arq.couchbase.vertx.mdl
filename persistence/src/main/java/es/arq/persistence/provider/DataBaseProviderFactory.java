package es.arq.persistence.provider;

import es.arq.persistence.provider.enums.DBType;
import es.arq.persistence.provider.exceptions.PersistenceException;

public class DataBaseProviderFactory {

	/**
	 * @param type database type
	 * @return
	 */
	public static DatabaseProvider getInstance(DBType type) throws PersistenceException {
		DatabaseProvider instance = null;
		
		try {
			switch (type) {
			case COUCHBASE:
				instance = CouchBasePersistenceImpl.getInstance();
				break;
			case MONGODB:
				instance = null; // TODO not implemented yet
			case COUCHDB:
				instance = null; // TODO not implemented yet
			default:
				break;
			}
					
		} catch (PersistenceException e) {
			throw e;
		}
		
		return instance;
	}
}
