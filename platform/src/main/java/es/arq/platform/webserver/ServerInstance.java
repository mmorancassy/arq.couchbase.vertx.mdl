package es.arq.platform.webserver;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;


public class ServerInstance {

	public static void main(String[] args) {
		try {
			Verticle mainVerticle = new RoutingServer();
			Vertx.vertx().deployVerticle(mainVerticle);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
