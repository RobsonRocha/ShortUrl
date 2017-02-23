package br.com.shorturl.exception;

public class CustomAliasAlreadyExistsException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4723162718758669982L;
	private static final String CODE = "001"; 
	
	public CustomAliasAlreadyExistsException(String message){
		super(message);
	}

	public static String getCode() {
		return CODE;
	}

}
