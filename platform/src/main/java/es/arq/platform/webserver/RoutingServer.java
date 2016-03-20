package es.arq.platform.webserver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import es.arq.platform.webserver.exceptions.ServerException;
import es.arq.platform.webserver.handlers.rest.GetRestHandler;
import es.arq.platform.webserver.properties.ServerProperties;

public class RoutingServer extends AbstractVerticle {
	
	// The Logger
	private final static Logger LOG = Logger.getLogger(RoutingServer.class);
	
	// Configuration
	private final static String CONFIG_PROPERTIES = "/server.properties";
	
	// Default HTTP listen port
	private int listenPort = 8080;
	
	// Server properties
	private Properties serverProperties = null;
	
	public RoutingServer() throws ServerException {
		super();
		try {
			LOG.info("Loading server configuration properties...");
			
			InputStream is = this.getClass().getResourceAsStream(CONFIG_PROPERTIES);
			serverProperties = new Properties();
			serverProperties.load(is);
			
			listenPort = Integer.valueOf((String)serverProperties.get(ServerProperties.LISTEN_PORT)).intValue();			
			
		} catch(Exception e) {
			LOG.error("It has has been an error during server properties loading", e);
			throw new ServerException("It has has been an error during server properties loading", e);
		}
		
	}

	@Override
	public void start() throws Exception {
		try {
			LOG.info("Server starting...");
			
			HttpServer server = vertx.createHttpServer();
			Router router = Router.router(vertx);
			
			StaticHandler staticHandler = StaticHandler.create();
			staticHandler.setWebRoot("../front/webRoot");
			staticHandler.setIndexPage("/main.html");
			
			//router.route(HttpMethod.GET, "/").handler(staticHandler);
			router.route().handler(staticHandler);
			router.route(HttpMethod.GET, "/home/index").handler(new GetRestHandler());	
			router.route(HttpMethod.GET, "/home/:documentId").handler(new GetRestHandler());	
			
			// TODO
			//router.route(HttpMethod.POST, "/home/index").handler(new PostRestHandler());
			//router.route(HttpMethod.PUT, "/home/index").handler(new PutRestHandler());
			//router.route(HttpMethod.DELETE, "/home/index").handler(new DeleteRestHandler());
			
			server.requestHandler(router::accept).listen(listenPort);
			
			LOG.info("Server listening at port: " + listenPort);
			
		} catch (Exception e) {
			LOG.error("It has has been an error during server starting", e);
		}
	}

}
