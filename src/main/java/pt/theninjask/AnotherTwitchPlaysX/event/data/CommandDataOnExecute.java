package pt.theninjask.AnotherTwitchPlaysX.event.data;

import java.util.Map;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;

public class CommandDataOnExecute extends BasicEvent {

	private CommandData cmd;
	
	private Map<String, String> map;
	
	public CommandDataOnExecute(CommandData cmd) {
		this(cmd,null);
	}
	
	public CommandDataOnExecute(CommandData cmd, Map<String, String> map) {
		super(CommandDataOnExecute.class.getSimpleName());
		this.cmd = cmd;
		this.map = map;
	}
	
	public CommandData getCommandData() {
		return cmd;
	}
	
	public Map<String, String> getMapping(){
		return map;
	}
	
}
