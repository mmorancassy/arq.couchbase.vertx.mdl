package es.arq.persistence.provider;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.RawJsonDocument;

import es.arq.persistence.provider.exceptions.PersistenceException;
import es.arq.persistence.provider.properties.ConnectionProperties;

public class CouchBasePersistenceImpl implements DatabaseProvider {

	// The Logger
	private final static Logger LOG = Logger.getLogger(CouchBasePersistenceImpl.class);
	
	// Configuration
	private final static String CONFIG_PROPERTIES = "/persistence.properties";
	
	// Singleton instance
	private static DatabaseProvider connection = null;
	
	// Cluster nodes
	private static List<String> nodeList = null;
	
	// Bucket password
	private static String password = null;
	
	// Bucket name
	private static String bucketName = null;
	
	// The Bucket
	private static Bucket bucket = null;
	
	// Couchbase cluster
	private static Cluster cluster = null;
	
	// Connection properties
	private Properties persistenceProperties = null;
	
	protected CouchBasePersistenceImpl() throws Exception {
		try {
			InputStream is = this.getClass().getResourceAsStream(CONFIG_PROPERTIES);
			persistenceProperties = new Properties();
			persistenceProperties.load(is);
			
			String nodes = (String)persistenceProperties.get(ConnectionProperties.DB_PROVIDER_NODES);
			nodeList = new ArrayList<String>(Arrays.asList(nodes.split(",")));
			bucketName = (String) persistenceProperties.get(ConnectionProperties.DB_PROVIDER_BUCKET);
			password = (String) persistenceProperties.get(ConnectionProperties.DB_PROVIDER_PASSWORD);
			
		} catch (Exception e) {
			LOG.error("Se ha producido un error al cargar el fichero de configuración persistence.properties", e);
			throw e;
		}
	}

	public static DatabaseProvider getInstance() throws PersistenceException {
		try {
			if (connection == null) {
				// Thread-safe - double checked locking
				synchronized (CouchBasePersistenceImpl.class) {
					if (connection == null) {
						connection = new CouchBasePersistenceImpl();
						
						cluster = CouchbaseCluster.create(nodeList);
						bucket = cluster.openBucket(bucketName);
						
						LOG.info("Connection to bucket: " + bucketName + " on host has been stablished");	
					}
					
				}
			}
		} catch (Exception e) {
			LOG.error("An error has occured during database connection", e);
			throw new PersistenceException("An error has occured during database connection", e);
		}
		
		return connection;
	}

	@Override
	public DatabaseProvider getConnection() throws PersistenceException {
		return connection;
	}
	
	@Override
	public boolean disconnect() throws PersistenceException {
		try {
			if (connection != null) {					
				boolean statusBucket = bucket.close();
				boolean statusCluster = cluster.disconnect();				
				connection = null;
				return statusBucket && statusCluster;
			}
		} catch (Exception e) {
			LOG.error("An error has occured during database disconnect", e);
			throw new PersistenceException("An error has occured during database disconnect", e);
		}
		
		return false;
	}

	@Override
	public String insert(String document) throws PersistenceException {
		String objectId = null;
		
		try {
			String id = UUID.randomUUID().toString();
			RawJsonDocument jsondoc = RawJsonDocument.create(id, document);
			RawJsonDocument stored = bucket.insert(jsondoc);
			objectId = stored.id();
			
		} catch (Exception e) {
			LOG.error("An error has occured during document insertion", e);
			throw new PersistenceException("An error has occured during document insertion", e);
		}
		
		return objectId;
	}

	@Override
	public boolean delete(String documentId) throws PersistenceException {
		boolean result = false;
		try {
			JsonDocument doc = bucket.remove(documentId);
			result = (doc != null && !"".equals(doc));
			
		} catch (Exception e) {
			LOG.error("An error has occured during document deletion", e);
			throw new PersistenceException("An error has occured during document deletion", e);
		}
		
		return result;
	}

	@Override
	public String update(String documentId, String document) throws PersistenceException {
		String storedDocument = null;
		
		try {
			RawJsonDocument jsondoc = RawJsonDocument.create(documentId, document);
			RawJsonDocument stored = bucket.replace(jsondoc);
			storedDocument = stored.content();
			
		} catch (Exception e) {
			LOG.error("An error has occured during update document", e);
			throw new PersistenceException("An error has occured during update document", e);
		}
		
		return storedDocument;
	}

	@Override
	public String getById(String documentId) throws PersistenceException {
		String storedDocument = null;
		
		try {
			JsonDocument stored = bucket.get(documentId);
			
			if (stored != null) {
				storedDocument = stored.content().toString();
			} else {
				storedDocument = "{\"noContent\": \"true\"}";
			}
			
		} catch (Exception e) {
			LOG.error("An error has occured during query document", e);
			throw new PersistenceException("An error has occured during query document", e);
		}
		
		return storedDocument;
	}

	@Override
	public String query(String query) throws PersistenceException {
		// TODO Not implemented
		return null;
	}

}
