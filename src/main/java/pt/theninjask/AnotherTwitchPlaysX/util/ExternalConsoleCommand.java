package pt.theninjask.AnotherTwitchPlaysX.util;

import java.util.Comparator;

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

	/**
	 * 
	 * @param number - options of parameter in number
	 * @return null if nothing to provide else all available options
	 */
	public default String[] getParamOptions(int number) {
		return null;
	}

	public static Comparator<ExternalConsoleCommand> comparator = new Comparator<ExternalConsoleCommand>() {
		@Override
		public int compare(ExternalConsoleCommand o1, ExternalConsoleCommand o2) {
			return o1.getCommand().compareTo(o2.getCommand());
		}
	};
}
