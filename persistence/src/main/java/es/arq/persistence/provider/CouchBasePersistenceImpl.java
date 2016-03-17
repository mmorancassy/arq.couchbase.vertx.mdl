package es.arq.persistence.provider;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.RawJsonDocument;

import es.arq.persistence.provider.exceptions.PersistenceException;
import es.arq.persistence.provider.properties.ConnectionProperties;

public class CouchBasePersistenceImpl implements DatabaseProvider {

	// The Logger
	private final static Logger LOG = LoggerFactory.getLogger(CouchBasePersistenceImpl.class);
	
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
	
	// Connection properties
	private Properties persistenceProperties = null;
	
	protected CouchBasePersistenceImpl() {
		try {
			InputStream is = this.getClass().getResourceAsStream("/persistence.properties");
			persistenceProperties = new Properties();
			persistenceProperties.load(is);
			
			String nodes = (String)persistenceProperties.get(ConnectionProperties.DB_PROVIDER_NODES);
			nodeList = new ArrayList<String>(Arrays.asList(nodes.split(",")));
			bucketName = (String) persistenceProperties.get(ConnectionProperties.DB_PROVIDER_BUCKET);
			password = (String) persistenceProperties.get(ConnectionProperties.DB_PROVIDER_PASSWORD);
			
		} catch (Exception e) {
			LOG.error("Se ha producido un error al cargar el fichero de configuración persistence.properties", e);
		}
	}

	public static DatabaseProvider getInstance() {
		if (connection == null) {
			connection = new CouchBasePersistenceImpl();
			
			Cluster cluster = CouchbaseCluster.create(nodeList);
			bucket = cluster.openBucket(bucketName);
			
			LOG.info("Connection to host has been stablished");
		}
		
		return connection;
	}

	@Override
	public DatabaseProvider getConnection() throws PersistenceException {
		return connection;
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
	public String update(String document) throws PersistenceException {
		String objectId = null;
		
		try {
			RawJsonDocument jsondoc = RawJsonDocument.create(document);
			RawJsonDocument stored = bucket.replace(jsondoc);
			objectId = stored.id();
			
		} catch (Exception e) {
			LOG.error("An error has occured during update document", e);
			throw new PersistenceException("An error has occured during update document", e);
		}
		
		return objectId;
	}
	
	@Override
	public String upsert(String document) throws PersistenceException {
		String objectId = null;
		
		try {
			RawJsonDocument jsondoc = RawJsonDocument.create(document);
			RawJsonDocument stored = bucket.upsert(jsondoc);
			objectId = stored.id();
			
		} catch (Exception e) {
			LOG.error("An error has occured during upsert document", e);
			throw new PersistenceException("An error has occured during upsert document", e);
		}
		
		return objectId;
	}

	@Override
	public String query(String documentId) throws PersistenceException {
		String document = null;
		
		try {
			JsonDocument stored = bucket.get(documentId);
			document = stored.toString();
			
		} catch (Exception e) {
			LOG.error("An error has occured during query document", e);
			throw new PersistenceException("An error has occured during query document", e);
		}
		
		return document;
	}

}
