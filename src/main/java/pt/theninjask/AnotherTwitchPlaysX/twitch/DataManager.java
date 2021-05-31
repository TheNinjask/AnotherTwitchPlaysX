package pt.theninjask.AnotherTwitchPlaysX.twitch;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.data.SessionData;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class DataManager {

	private static DataManager singleton = new DataManager();
	
	private SessionData session;
	
	public static final SessionData DUMMY_SESSION = new SessionData("dummy","dummy","dummy");
	
	private List<CommandData> commands;
	
	private DataManager() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", DataManager.class.getSimpleName()));
		this.commands = new ArrayList<CommandData>();
	}
	
	private DataManager(SessionData session, List<CommandData> commands) {
		this.session = session;
		this.commands = commands;
	}
	
	public static DataManager getInstance() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.getInstance()", DataManager.class.getSimpleName()));
		return singleton;
	}

	public SessionData getSession() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.getSession()", DataManager.class.getSimpleName()));
		if(Constants.disableSession)
			return DUMMY_SESSION;
		return session;
	}

	public void setSession(SessionData session) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.setSession()", DataManager.class.getSimpleName()));
		this.session = session;
	}
	
	public List<CommandData> getCommands(){
		Constants.printVerboseMessage(Level.INFO, String.format("%s.getCommands()", DataManager.class.getSimpleName()));
		return commands;
	}
	
	public void setCommands(List<CommandData> commands) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.setCommands()", DataManager.class.getSimpleName()));
		this.commands = commands;
	}
	
}
