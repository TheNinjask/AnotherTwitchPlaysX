package pt.theninjask.AnotherTwitchPlaysX.exception;

import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;

public class AlreadyConnectedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AlreadyConnectedException() {
		super(DataManager.getLanguage().getExceptions().getAlreadyConnected());
	}

}
