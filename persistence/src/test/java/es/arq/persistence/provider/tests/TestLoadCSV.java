package es.arq.persistence.provider.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.junit.Test;

import es.arq.persistence.provider.DataBaseProviderFactory;
import es.arq.persistence.provider.DatabaseProvider;
import es.arq.persistence.provider.enums.DBType;

public class TestLoadCSV {
	
	// The Logger
	private final static Logger LOG = Logger.getLogger(TestLoadCSV.class);

	@Test
	public void testLoadCSV() {
		DatabaseProvider databaseProvider = null;
		
		String csvFile = File.separator + "music-collection.csv";
		
		BufferedReader br = null;
		String line = "";
		String delimiter = ",";

		try {
			databaseProvider = DataBaseProviderFactory.getInstance(DBType.COUCHBASE);
			
			String file = this.getClass().getResource(csvFile).getFile();
			br = new BufferedReader(new FileReader(file));
			
			LOG.info("File " + csvFile + " succesfully loaded");
			
			// Catalog# 		-> 0
			// Artist   		-> 1
			// Title    		-> 2 
			// Label    		-> 3
			// Format 			-> 4
			// Rating   		-> 5
			// Released 		-> 6
			// release_id   	-> 7
			// CollectionFolder -> 8
			// Date Added       -> 9
			// Collection Style -> 10
			// Collection Notes -> 11
			boolean headerLine = false;
			String[] headers = null;
			while ((line = br.readLine()) != null) {
			    // use comma as separator
				if (!headerLine) {
					headers = line.split(delimiter);
					headerLine = true;
				} else {
					line = line.replaceAll(",\\s", "-");
					
					String[] collectionEntry = line.split(delimiter);
					StringBuffer json = new StringBuffer("{");
					for(int i=0; i < collectionEntry.length; i++) {
						if (collectionEntry[i] != null && !"".equals(collectionEntry[i])) {
							json.append("\"" + headers[i].toLowerCase().trim() +  "\"")
								.append(":")
								.append("\"" + collectionEntry[i].replace("\"", "") + "\"")
								.append(",");
						}
					}
					
					json.deleteCharAt(json.length()-1);
					json.append("}");										
															
					String id = databaseProvider.insert(json.toString());
					
					LOG.info("Document succesfully formatted: " + json.toString() + " an inserted into database with id: " + id); 
					
				}			
			}

			databaseProvider.disconnect();
			
			assertTrue(true);
		} catch (Exception e) {
			LOG.error(e);
			fail(e.getMessage());			
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					LOG.error(e);
					fail(e.getMessage());
				}
			}
		}		
		
		
	}

}
