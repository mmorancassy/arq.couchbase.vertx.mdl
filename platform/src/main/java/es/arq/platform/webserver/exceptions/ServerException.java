package es.arq.platform.webserver.exceptions;

public class ServerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServerException() {
		super();
	}
	
	public ServerException(String message) {
		super(message);
	}
	
	public ServerException(Throwable t) {
		super(t);
	}
	
	public ServerException(String message, Throwable t) {
		super(message, t);
	}
	
}
