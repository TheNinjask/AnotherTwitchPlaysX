package pt.theninjask.AnotherTwitchPlaysX.event.data;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;

public class CommandDataOnMessageEvent extends BasicEvent {
	
	private CommandData cmd; 
	
	public CommandDataOnMessageEvent(CommandData cmd) {
		super(CommandDataOnMessageEvent.class.getSimpleName());
		this.cmd = cmd;
	}
	
	public CommandData getCommandData() {
		return cmd;
	}
	
}
