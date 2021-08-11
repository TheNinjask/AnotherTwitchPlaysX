package pt.theninjask.AnotherTwitchPlaysX.event.externalconsole;

import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;

public class InputCommandExternalConsoleEvent extends BasicEvent {

	private String[] args;
	
	public InputCommandExternalConsoleEvent(String[] args) {
		super(InputCommandExternalConsoleEvent.class.getSimpleName());
		this.args = args;
	}
	
	public String[] getArgs() {
		return args;
	}
	
}
