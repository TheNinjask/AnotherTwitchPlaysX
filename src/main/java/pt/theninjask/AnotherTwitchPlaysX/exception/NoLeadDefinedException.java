package pt.theninjask.AnotherTwitchPlaysX.exception;

public class NoLeadDefinedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoLeadDefinedException() {
		super("No Lead provided");
	}

}
