package pt.theninjask.AnotherTwitchPlaysX.exception;

import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;

public class NotVerifyJarException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotVerifyJarException() {
		super(DataManager.getLanguage().getExceptions().getNotVerifyJar());
	}
	
}
