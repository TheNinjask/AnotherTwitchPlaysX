package pt.theninjask.AnotherTwitchPlaysX.util;

public interface ExternalConsoleCommand {

	public String getCommand();

	public default String getDescription() {
		return "N/A";
	}
	
	/**
	 * 
	 * @param args
	 * @return true if success else false
	 */
	public boolean executeCommand(String[] args);

}
