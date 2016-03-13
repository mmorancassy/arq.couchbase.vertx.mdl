package es.arq.persistence.provider;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.RawJsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

public class CouchBasePersistenceImpl implements DatabaseProvider {

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
		}
		
		return connection;
	}

	@Override
	public String insert(String document) {
		// TODO try catch block
		String id = UUID.randomUUID().toString();
		RawJsonDocument jsondoc = RawJsonDocument.create(id, document);
		RawJsonDocument stored = bucket.insert(jsondoc);
		
		return stored.id();
	}

	@Override
	public String delete(String documentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update(String document) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocument(String documentId) {
		// TODO Auto-generated method stub
		return null;
	}

}
