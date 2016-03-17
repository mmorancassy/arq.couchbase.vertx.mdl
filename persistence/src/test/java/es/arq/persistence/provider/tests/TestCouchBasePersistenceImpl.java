package es.arq.persistence.provider.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.arq.persistence.provider.CouchBasePersistenceImpl;
import es.arq.persistence.provider.DatabaseProvider;
import es.arq.persistence.provider.exceptions.PersistenceException;

public class TestCouchBasePersistenceImpl {
	
	// The Logger
	private final static Logger LOG = LoggerFactory.getLogger(TestCouchBasePersistenceImpl.class);

	@Test
	public void testGetConnection() {
		DatabaseProvider databaseProvider = null;
		try {
			databaseProvider = CouchBasePersistenceImpl.getInstance();
			
			assertTrue(databaseProvider.disconnect());
		} catch (PersistenceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testInsert() {
		DatabaseProvider databaseProvider = null;
		try {
			databaseProvider = CouchBasePersistenceImpl.getInstance();
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
		DatabaseProvider databaseProvider = null;
		try {
			databaseProvider = CouchBasePersistenceImpl.getInstance();
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
		DatabaseProvider databaseProvider = null;
		try {
			databaseProvider = CouchBasePersistenceImpl.getInstance();
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
		DatabaseProvider databaseProvider = null;
		try {
			databaseProvider = CouchBasePersistenceImpl.getInstance();
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
		fail("Not yet implemented");
	}	

}
