package pt.theninjask.AnotherTwitchPlaysX.exception;

public class AlreadyConnectedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AlreadyConnectedException() {
		super("TwitchPlayer is already connected");
	}

}
