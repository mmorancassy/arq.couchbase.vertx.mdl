package es.arq.persistence.provider.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import es.arq.persistence.provider.CouchBasePersistenceImpl;
import es.arq.persistence.provider.DatabaseProvider;
import es.arq.persistence.provider.exceptions.PersistenceException;

public class TestCouchBasePersistenceImpl {

	@Test
	public void testGetConnection() {
		try {
			DatabaseProvider databaseProvider = CouchBasePersistenceImpl.getInstance();
			
			assertTrue(databaseProvider.getConnection() != null);
		} catch (PersistenceException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testInsert() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpsert() {
		fail("Not yet implemented");
	}

	@Test
	public void testQuery() {
		fail("Not yet implemented");
	}

}
