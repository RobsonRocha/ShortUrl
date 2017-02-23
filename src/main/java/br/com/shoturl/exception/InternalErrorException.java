package br.com.shoturl.exception;

public class InternalErrorException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3502736454903120866L;
	private static final String CODE = "003"; 
	
	public InternalErrorException(String message){
		super(message);
	}

	public static String getCode() {
		return CODE;
	}

}
