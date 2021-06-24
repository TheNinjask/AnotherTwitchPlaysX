package pt.theninjask.AnotherTwitchPlaysX.exception;

import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;

public class NotSetupException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotSetupException() {
		super(DataManager.getLanguage().getExceptions().getNotSetup());
	}
	
}
