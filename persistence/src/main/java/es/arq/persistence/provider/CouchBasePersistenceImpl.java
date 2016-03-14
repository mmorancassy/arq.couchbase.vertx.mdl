package es.arq.persistence.provider;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.RawJsonDocument;

import es.arq.persistence.provider.exceptions.PersistenceException;

public class CouchBasePersistenceImpl implements DatabaseProvider {

	// The Logger
	private final static Logger LOG = LoggerFactory.getLogger(CouchBasePersistenceImpl.class);
	
	// Singleton instance
	private static DatabaseProvider connection = null;
	
	// Cluster nodes
	private List<URI> nodeList = null;
	
	// Bucket username
	private String username = null;
	
	// Bucket password
	private String password = null;
	
	// Bucket name
	private String bucketName = null;
	
	// The Bucket
	private Bucket bucket = null;
	
	protected CouchBasePersistenceImpl() {}

	@Override
	public DatabaseProvider getConnection() {
		if (connection == null) {
			connection = new CouchBasePersistenceImpl();
			
			Cluster cluster = CouchbaseCluster.create();
			bucket = cluster.openBucket(bucketName);
			
			LOG.info("Connection to host has been stablished");
		}
		
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
