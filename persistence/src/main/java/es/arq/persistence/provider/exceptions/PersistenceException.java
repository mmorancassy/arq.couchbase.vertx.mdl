package es.arq.persistence.provider.exceptions;

public class PersistenceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5151527842120452679L;

	public PersistenceException() {
		super();
	}
	
	public PersistenceException(String message) {
		super(message);
	}
	
	public PersistenceException(Throwable t) {
		super(t);
	}
	
	public PersistenceException(String message, Throwable t) {
		super(message, t);
	}
}
