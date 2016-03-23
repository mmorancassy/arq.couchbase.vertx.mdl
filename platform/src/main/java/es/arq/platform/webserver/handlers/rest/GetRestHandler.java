package es.arq.platform.webserver.handlers.rest;

import java.util.Collection;
import java.util.Map;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
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
			
			DatabaseProvider databaseProvider = DataBaseProviderFactory.getInstance(DBType.COUCHBASE);
			
			response.putHeader("content-type", "application/json; charset=utf-8");	
			
			if (documentId != null && !"".equals(documentId)) {
				LOG.info("Retrieve document with Id: " + documentId);
								
				String document = databaseProvider.getById(documentId);
				
				response.setStatusCode(200);
				response.end(Json.encodePrettily(document));
				
			} else {
				// TODO dealing with ids
				Map<String, String> documents = databaseProvider.query("collection_view", 50);
				Collection<String> documentList = documents.values();
				response.setStatusCode(200);
				response.end(Json.encodePrettily(documentList));
			}
								

			
		} catch (Exception e) {
			response.setChunked(true);
			response.putHeader("content-type", "text/plain");
			
			// Not found
			response.setStatusCode(204);
			response.end(e.getMessage());
		}
		
	}

}
