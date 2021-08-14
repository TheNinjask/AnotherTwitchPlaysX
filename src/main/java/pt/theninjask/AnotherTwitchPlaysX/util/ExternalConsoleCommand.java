package pt.theninjask.AnotherTwitchPlaysX.util;

public interface ExternalConsoleCommand {

	public String getCommand();

	public default String getDescription() {
		return "N/A";
	}
	
	public void executeCommand(String[] args);

}
