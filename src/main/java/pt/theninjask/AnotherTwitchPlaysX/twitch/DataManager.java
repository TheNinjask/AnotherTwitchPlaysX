package pt.theninjask.AnotherTwitchPlaysX.twitch;

import pt.theninjask.AnotherTwitchPlaysX.data.SessionData;

public class DataManager {

	private static DataManager singleton = new DataManager();
	
	private SessionData session;
	
	private DataManager() {}
	
	public static DataManager getInstance() {
		return singleton;
	}

	public SessionData getSession() {
		return session;
	}

	public void setSession(SessionData session) {
		this.session = session;
	}
	
}
