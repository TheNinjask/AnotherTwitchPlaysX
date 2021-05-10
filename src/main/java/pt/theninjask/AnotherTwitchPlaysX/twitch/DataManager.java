package pt.theninjask.AnotherTwitchPlaysX.twitch;

import java.util.ArrayList;
import java.util.List;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.data.SessionData;

public class DataManager {

	private static DataManager singleton = new DataManager();
	
	private SessionData session;
	
	private List<CommandData> commands;
	
	private DataManager() {
		this.commands = new ArrayList<CommandData>();
	}
	
	private DataManager(SessionData session, List<CommandData> commands) {
		this.session = session;
		this.commands = commands;
	}
	
	public static DataManager getInstance() {
		return singleton;
	}

	public SessionData getSession() {
		return session;
	}

	public void setSession(SessionData session) {
		this.session = session;
	}
	
	public List<CommandData> getCommands(){
		return commands;
	}
	
	public void setCommands(List<CommandData> commands) {
		this.commands = commands;
	}
	
}
