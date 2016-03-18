package es.arq.platform.webserver.handlers.rest;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import org.apache.log4j.Logger;

import es.arq.persistence.provider.DataBaseProviderFactory;
import es.arq.persistence.provider.DatabaseProvider;
import es.arq.persistence.provider.enums.DBType;

public class GetRestHandler implements Handler<RoutingContext>{

	// The Logger
	private final static Logger LOG = Logger.getLogger(GetRestHandler.class);
	
	public GetRestHandler() {
		super();
	}
	
	@Override
	public void handle(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		response.setChunked(true);		
		
		try {
			HttpServerRequest request = routingContext.request();
			String documentId = request.params().get("documentId");
			
			response.putHeader("content-type", "text/plain");	
			
			if (documentId != null && !"".equals(documentId)) {
				LOG.info("Retrieve document with Id: " + documentId);
				
				DatabaseProvider databaseProvider = DataBaseProviderFactory.getInstance(DBType.COUCHBASE);
				String document = databaseProvider.getById(documentId);
				
				response.write(document);
				
			} else {
				response.write("Hello World!");
			}
								
			response.setStatusCode(200);
			response.end();
			
		} catch (Exception e) {
			response.setChunked(true);
			response.putHeader("content-type", "text/plain");
			response.write(e.getMessage());
			
			// Not found
			response.setStatusCode(204);
			response.end();
		}
		
	}

}
