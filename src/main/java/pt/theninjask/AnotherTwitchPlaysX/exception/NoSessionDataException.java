package pt.theninjask.AnotherTwitchPlaysX.exception;

import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;

public class NoSessionDataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSessionDataException() {
		super(DataManager.getLanguage().getExceptions().getNoSessionData());
	}
	
}
