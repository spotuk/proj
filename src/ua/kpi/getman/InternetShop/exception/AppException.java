package ua.kpi.getman.InternetShop.exception;

/**
 * An exception that provides information on an application error.
 * 
 * @author Getman Valentine
 * 
 */
public class AppException extends Exception {

	private static final long serialVersionUID = 8288779062647218916L;

	public AppException() {
		super();
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppException(String message) {
		super(message);
	}

}
