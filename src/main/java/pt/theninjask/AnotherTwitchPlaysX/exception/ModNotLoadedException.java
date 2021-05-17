package pt.theninjask.AnotherTwitchPlaysX.exception;

public class ModNotLoadedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ModNotLoadedException() {
		super("Mod could not be loaded!");
	}

}
