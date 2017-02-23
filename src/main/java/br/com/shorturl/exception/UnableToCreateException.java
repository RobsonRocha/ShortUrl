package br.com.shorturl.exception;

public class UnableToCreateException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6905459940480889126L;
	private static final String CODE = "005"; 
	
	public UnableToCreateException(String message){
		super(message);
	}

	public static String getCode() {
		return CODE;
	}

}
