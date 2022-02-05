package pt.theninjask.AnotherTwitchPlaysX.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import externalconsole.util.ColorTheme;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.data.TwitchSessionData;
import pt.theninjask.AnotherTwitchPlaysX.data.YouTubeSessionData;
import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.ColorThemeUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.CommandsUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.LanguageUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.TwitchSessionUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.YoutubeSessionUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class DataManager {

	private static DataManager singleton = new DataManager();

	private TwitchSessionData twitchSession;

	private YouTubeSessionData youtubeSession;

	private ColorTheme theme;
	
	public static boolean disableSession = false;

	public static final TwitchSessionData TWITCH_DUMMY_SESSION = new TwitchSessionData("dummy", "dummy", "dummy");

	public static final YouTubeSessionData YOUTUBE_DUMMY_SESSION = new YouTubeSessionData("dummy", "dummy");

	private List<CommandData> commands;

	private Lang language;

	private DataManager() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", DataManager.class.getSimpleName()));
		this.commands = new ArrayList<CommandData>();
	}

	/*private DataManager(TwitchSessionData twitchSession, List<CommandData> commands) {
		this.twitchSession = twitchSession;
		this.commands = commands;
	}*/

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
		if (disableSession)
			session = TWITCH_DUMMY_SESSION;
		TwitchSessionUpdateEvent event = new TwitchSessionUpdateEvent(session);
		EventManager.triggerEvent(event);
		if(event.isCancelled())
			return;
		singleton.twitchSession = session;
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
		if (disableSession)
			session = YOUTUBE_DUMMY_SESSION;
		YoutubeSessionUpdateEvent event = new YoutubeSessionUpdateEvent(session);
		EventManager.triggerEvent(event);
		if(event.isCancelled())
			return;
		singleton.youtubeSession = session;
	}

	public static List<CommandData> getCommands() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.getCommands()", DataManager.class.getSimpleName()));
		return singleton.commands;
	}

	public static void setCommands(List<CommandData> commands) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.setCommands()", DataManager.class.getSimpleName()));
		CommandsUpdateEvent event = new CommandsUpdateEvent(commands);
		EventManager.triggerEvent(event);
		if(event.isCancelled())
			return;
		singleton.commands = commands;
	}

	public static Lang getLanguage() {
		// Constants.printVerboseMessage(Level.INFO, String.format("%s.getLanguage()",
		// DataManager.class.getSimpleName()));
		return singleton.language;
	}

	public static void setLanguage(Lang language) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.setLanguage()", DataManager.class.getSimpleName()));
		LanguageUpdateEvent event = new LanguageUpdateEvent(language);
		EventManager.triggerEvent(event);
		if(event.isCancelled())
			return;
		singleton.language = language;
	}
	
	public static ColorTheme getTheme() {
		return singleton.theme;
	}

	public static void setTheme(ColorTheme theme) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.setTheme()", DataManager.class.getSimpleName()));
		ColorThemeUpdateEvent event = new ColorThemeUpdateEvent(theme);
		EventManager.triggerEvent(event);
		if(event.isCancelled())
			return;
		singleton.theme = theme;
	}
}
