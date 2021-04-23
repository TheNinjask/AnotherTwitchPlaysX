package pt.theninjask.AnotherTwitchPlaysX.exception;

public class NoSessionDataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSessionDataException() {
		super("No SessionData provided");
	}
	
}
