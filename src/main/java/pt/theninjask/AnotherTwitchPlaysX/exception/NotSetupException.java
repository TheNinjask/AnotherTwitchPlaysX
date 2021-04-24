package pt.theninjask.AnotherTwitchPlaysX.exception;

public class NotSetupException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotSetupException() {
		super("Twitch Player has yet to be setup");
	}
	
}
