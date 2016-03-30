package es.arq.platform.webserver.tests;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.impl.FileResolver;
import es.arq.platform.webserver.RoutingServer;

public class TestRoutingServer {

	public static void main(String[] args) {
		try {			
			// Disable vert.x file caching - only for develepment purposes!
			System.setProperty(FileResolver.DISABLE_FILE_CACHING_PROP_NAME, "true");
			
			Verticle mainVerticle = new RoutingServer();
			Vertx.vertx().deployVerticle(mainVerticle);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
