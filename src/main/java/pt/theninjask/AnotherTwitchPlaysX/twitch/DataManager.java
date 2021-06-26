package pt.theninjask.AnotherTwitchPlaysX.twitch;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.data.SessionData;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class DataManager {

	private static DataManager singleton = new DataManager();
	
	private SessionData session;
	
	@FunctionalInterface
	public interface OnUpdateSession{
		void updateSession(SessionData session);
	}
	
	private List<OnUpdateSession> sessionEvents = new ArrayList<OnUpdateSession>();
	
	public static boolean disableSession = false;
	
	public static final SessionData DUMMY_SESSION = new SessionData("dummy","dummy","dummy");
	
	private List<CommandData> commands;
	
	private Lang language;
	
	@FunctionalInterface
	public interface OnUpdateLanguage{
		void updateLang(Lang session);
	}
	
	private List<OnUpdateLanguage> langEvents = new ArrayList<OnUpdateLanguage>();
	
	private DataManager() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", DataManager.class.getSimpleName()));
		this.commands = new ArrayList<CommandData>();
	}
	
	private DataManager(SessionData session, List<CommandData> commands) {
		this.session = session;
		this.commands = commands;
	}
	
	/*public static DataManager getInstance() {
		//Constants.printVerboseMessage(Level.INFO, String.format("%s.getInstance()", DataManager.class.getSimpleName()));
		return singleton;
	}*/

	public static SessionData getSession() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.getSession()", DataManager.class.getSimpleName()));
		if(disableSession)
			return DUMMY_SESSION;
		return singleton.session;
	}

	public static void setSession(SessionData session) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.setSession()", DataManager.class.getSimpleName()));
		singleton.session = session;
		for (OnUpdateSession elem : singleton.sessionEvents) {
			elem.updateSession(session);
		}
	}
	
	public static void registerSessionEvent(OnUpdateSession event) {
		singleton.sessionEvents.add(event);
	}
	
	public static void unregisterSessionEvent(OnUpdateSession event) {
		singleton.sessionEvents.remove(event);
	}
	
	public static List<CommandData> getCommands(){
		Constants.printVerboseMessage(Level.INFO, String.format("%s.getCommands()", DataManager.class.getSimpleName()));
		return singleton.commands;
	}
	
	public static void setCommands(List<CommandData> commands) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.setCommands()", DataManager.class.getSimpleName()));
		singleton.commands = commands;
	}

	public static Lang getLanguage() {
		//Constants.printVerboseMessage(Level.INFO, String.format("%s.getLanguage()", DataManager.class.getSimpleName()));
		return singleton.language;
	}

	public static void setLanguage(Lang language) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.setLanguage()", DataManager.class.getSimpleName()));
		singleton.language = language;
		for (OnUpdateLanguage elem : singleton.langEvents) {
			elem.updateLang(language);
		}
	}
	
	public static void registerLangEvent(OnUpdateLanguage event) {
		singleton.langEvents.add(event);
	}
	
	public static void unregisterLangEvent(OnUpdateLanguage event) {
		singleton.langEvents.remove(event);
	}
	
}
