package pt.theninjask.AnotherTwitchPlaysX.exception;

import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;

public class ModNotLoadedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ModNotLoadedException() {
		super(DataManager.getLanguage().getExceptions().getModNotLoaded());
	}

}
