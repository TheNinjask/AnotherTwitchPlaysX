package pt.theninjask.AnotherTwitchPlaysX.exception;

import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;

public class NotConnectedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotConnectedException() {
		super(DataManager.getLanguage().getExceptions().getNotConnected());
	}

}
