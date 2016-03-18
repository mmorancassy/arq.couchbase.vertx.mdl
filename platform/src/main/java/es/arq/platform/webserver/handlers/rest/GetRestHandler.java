package es.arq.platform.webserver.handlers.rest;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class GetRestHandler implements Handler<RoutingContext>{

	@Override
	public void handle(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		
		response.setChunked(true);
		response.putHeader("content-type", "text/plain");
		response.write("Hello World!");
		
		response.setStatusCode(200);
		response.end();
		
	}

}
