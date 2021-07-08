package pt.theninjask.AnotherTwitchPlaysX.exception;

import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;

public class NoLeadDefinedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoLeadDefinedException() {
		super(DataManager.getLanguage().getExceptions().getNoLeadDefined());
	}

}
