package pt.theninjask.AnotherTwitchPlaysX.util;

public interface ExternalConsoleCommand {

	public String getCommand();

	public void executeCommand(String[] args);

}
