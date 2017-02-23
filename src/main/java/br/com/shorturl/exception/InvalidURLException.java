package br.com.shorturl.exception;

public class InvalidURLException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6339069042525335568L;
	
	private static final String CODE = "002"; 
	
	public InvalidURLException(String message){
		super(message);
	}

	public static String getCode() {
		return CODE;
	}

}
