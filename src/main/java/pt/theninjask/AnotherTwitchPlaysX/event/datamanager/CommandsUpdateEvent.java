package pt.theninjask.AnotherTwitchPlaysX.event.datamanager;

import java.util.List;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;

public class CommandsUpdateEvent extends BasicEvent{

	private List<CommandData> commands;
	
	public CommandsUpdateEvent(List<CommandData> commands) {
		super(CommandsUpdateEvent.class.getSimpleName());
		this.commands = commands;
	}
	
	public List<CommandData> getCommands(){
		return commands;
	}
	
}
