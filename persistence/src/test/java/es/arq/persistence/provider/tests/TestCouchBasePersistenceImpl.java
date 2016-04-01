package es.arq.persistence.provider.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import es.arq.persistence.provider.DataBaseProviderFactory;
import es.arq.persistence.provider.DatabaseProvider;
import es.arq.persistence.provider.enums.DBType;
import es.arq.persistence.provider.exceptions.PersistenceException;

public class TestCouchBasePersistenceImpl {
	
	// Connection
	private static DatabaseProvider databaseProvider = null;
	
	// The Logger
	private final static Logger LOG = Logger.getLogger(TestCouchBasePersistenceImpl.class);

	@BeforeClass
	public static void establishConnection() {
		try {
			databaseProvider = DataBaseProviderFactory.getInstance(DBType.COUCHBASE);
			
			LOG.info("Connection to couchbase database established");
			
		} catch (PersistenceException e) {
			fail(e.getMessage());
		}
	}
	
	@AfterClass
	public static void closeConnection() {
		try {
			boolean status = databaseProvider.disconnect();
			
			LOG.info("Couchbase database sucessfully disconnected: " + status);
			
		} catch (PersistenceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testInsert() {
		try {
			String document = "{\"test\" : \"couchbase persistence test\"}";
			String id = databaseProvider.insert(document);
			
			LOG.info("Document sucessfully inserted in bucket with id: " + id);
			
			assertTrue(id != null && !"".equals(id));
			
		} catch (PersistenceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testDelete() {
		try {
			String document = "{\"test\" : \"couchbase persistence test\"}";
			String id = databaseProvider.insert(document);
			
			LOG.info("Document sucessfully inserted in bucket with id: " + id);
			
			boolean status = databaseProvider.delete(id);
			
			assertTrue(status);
			
		} catch (PersistenceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testUpdate() {
		try {
			String documentInsert = "{\"test\" : \"couchbase persistence test\"}";
			String id = databaseProvider.insert(documentInsert);
			
			LOG.info("Document sucessfully inserted in bucket with id: " + id);
			
			String documentUpdate = "{\"test\" : \"couchbase persistence test\", \"update\": \"OK\"}";
			String updatedDocument = databaseProvider.update(id, documentUpdate);

			assertTrue(updatedDocument.equals(documentUpdate));
			
		} catch (PersistenceException e) {
			fail(e.getMessage());
		}
	}
		
	@Test
	public void testGetById() {
		try {
			String documentInsert = "{\"test\" : \"couchbase persistence test\"}";
			String id = databaseProvider.insert(documentInsert);
			
			LOG.info("Document sucessfully inserted in bucket with id: " + id);
			
			String retrieveDocument = databaseProvider.getById(id);

			assertTrue(retrieveDocument != null && !"".equals(retrieveDocument));
			
		} catch (PersistenceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testQuery() {
		try {
			String queryView = "collection_view";						
			
			Map<String, String> retrieveDocuments = databaseProvider.query(queryView, 1000);
			
			LOG.info("Number of documents retrieve from bucket: " + retrieveDocuments.size());

			assertTrue(retrieveDocuments != null);
			
		} catch (PersistenceException e) {
			fail(e.getMessage());
		}
	}	

}
