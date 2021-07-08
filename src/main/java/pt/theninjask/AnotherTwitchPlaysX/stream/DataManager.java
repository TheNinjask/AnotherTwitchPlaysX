package pt.theninjask.AnotherTwitchPlaysX.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.engio.mbassy.bus.MBassador;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.data.TwitchSessionData;
import pt.theninjask.AnotherTwitchPlaysX.data.YouTubeSessionData;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class DataManager {

	private static DataManager singleton = new DataManager();

	private MBassador<Object> dispatcher = Constants.setupDispatcher();

	private TwitchSessionData twitchSession;

	private YouTubeSessionData youtubeSession;

	public static boolean disableSession = false;

	public static final TwitchSessionData TWITCH_DUMMY_SESSION = new TwitchSessionData("dummy", "dummy", "dummy");

	public static final YouTubeSessionData YOUTUBE_DUMMY_SESSION = new YouTubeSessionData("dummy", "dummy");

	private List<CommandData> commands;

	private Lang language;

	private DataManager() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", DataManager.class.getSimpleName()));
		this.commands = new ArrayList<CommandData>();
	}

	private DataManager(TwitchSessionData twitchSession, List<CommandData> commands) {
		this.twitchSession = twitchSession;
		this.commands = commands;
	}

	public static TwitchSessionData getTwitchSession() {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.getTwitchSession()", DataManager.class.getSimpleName()));
		if (disableSession)
			return TWITCH_DUMMY_SESSION;
		return singleton.twitchSession;
	}

	public static void setTwitchSession(TwitchSessionData session) {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.setTwitchSession()", DataManager.class.getSimpleName()));
		singleton.twitchSession = session;
		if(session!=null)
			singleton.dispatcher.post(session).now();
	}

	public static void registerDataManagerEvent(Object event) {
		singleton.dispatcher.subscribe(event);
	}

	public static void unregisterDataManagerEvent(Object event) {
		singleton.dispatcher.unsubscribe(event);
	}

	public static YouTubeSessionData getYouTubeSession() {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.getYoutubeSession()", DataManager.class.getSimpleName()));
		if (disableSession)
			return YOUTUBE_DUMMY_SESSION;
		return singleton.youtubeSession;
	}

	public static void setYouTubeSession(YouTubeSessionData session) {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.setYoutubeSession()", DataManager.class.getSimpleName()));
		singleton.youtubeSession = session;
		if(session!=null)
			singleton.dispatcher.post(session).now();
	}

	public static List<CommandData> getCommands() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.getCommands()", DataManager.class.getSimpleName()));
		return singleton.commands;
	}

	public static void setCommands(List<CommandData> commands) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.setCommands()", DataManager.class.getSimpleName()));
		singleton.commands = commands;
	}

	public static Lang getLanguage() {
		// Constants.printVerboseMessage(Level.INFO, String.format("%s.getLanguage()",
		// DataManager.class.getSimpleName()));
		return singleton.language;
	}

	public static void setLanguage(Lang language) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.setLanguage()", DataManager.class.getSimpleName()));
		singleton.language = language;
		singleton.dispatcher.post(language).now();
	}

}
