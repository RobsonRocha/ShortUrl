package br.com.shorturl.exception;

public class ShortURLNotFoundException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6291044261725563094L;
	private static final String CODE = "004"; 
	
	public ShortURLNotFoundException(String message){
		super(message);
	}

	public static String getCode() {
		return CODE;
	}

}
