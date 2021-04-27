package pt.theninjask.AnotherTwitchPlaysX.exception;

public class NotConnectedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotConnectedException() {
		super("Twitch Player is not connected yet");
	}

}
