package pt.theninjask.AnotherTwitchPlaysX;

import java.awt.KeyboardFocusManager;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.jnativehook.GlobalScreen;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.data.ControlData;
import pt.theninjask.AnotherTwitchPlaysX.data.TwitchSessionData;
import pt.theninjask.AnotherTwitchPlaysX.data.YouTubeSessionData;
import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.MainLoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.TwitchLoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.YoutubeLoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModManager;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
import pt.theninjask.AnotherTwitchPlaysX.lan.en.EnglishLang;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.stream.twitch.TwitchPlayer;
import pt.theninjask.AnotherTwitchPlaysX.stream.youtube.YouTubePlayer;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.KeyPressedAdapter;

public class App {

	public static final String ID = "ATPX";

	public static final String NAME = "AnotherTwitchPlaysX";

	public static final String VERSION = getVersion();

	public static void main(String[] args) {

		int amountOfRequiredTwitchSessionOptions = 0;
		int amountOfRequiredYouTubeSessionOptions = 0;

		Options options = new Options();

		OptionGroup verbose = new OptionGroup();
		verbose.addOption(new Option("v", "verbose", false, "Sets Verbose to ALL"));
		verbose.addOption(new Option("w", "verboseWarning", false, "Sets Verbose to WARN"));
		options.addOptionGroup(verbose);

		options.addOption("p", "printCommand", false, "Print Commands execution");

		options.addOption("e", "eventLog", false, "Print events trigger");

		options.addOption("d", "debug", false, "Enables printStackTrace()");
		options.addOption("s", "disableSession", false,
				"Disables access to SessionData from DataManager (might \"brick\" app)\nMight be useful to check when a external mod gains access");
		options.addOption("h", "help", false, "Prints Help");

		options.addOption("n", "twitchNickname", true, "Twitch Account Nickname");
		options.addOption("c", "twitchChannel", true, "Twitch Channel Name (Optional Param)");
		options.addOption("t", "twitchToken", true, "Twitch OAuth Token of nickname");

		options.addOption("y", "youtubeToken", true, "Youtube Client Secret Path");
		options.addOption("i", "youtubeVideoId", true, "Youtube Video Id (Optional Param)");

		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption('h')) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("java -jar ATPXapp.jar", options, true);
				return;
			}
			if (cmd.hasOption('v')) {
				Constants.setLoggerLevel(Level.ALL);
				Constants.printVerboseMessage(Level.INFO, String.format("%s - version: %s", NAME, VERSION));
				Constants.printVerboseMessage(Level.INFO, "Verbose Set to ALL");
			} else if (cmd.hasOption('w')) {
				Constants.setLoggerLevel(Level.WARNING);
				Constants.printVerboseMessage(Level.WARNING, String.format("%s - version: %s", NAME, VERSION));
				Constants.printVerboseMessage(Level.WARNING, "Verbose Set to WARN");
			}
			if (cmd.hasOption('d')) {
				Constants.debug = true;
				Constants.printVerboseMessage(Level.INFO, "Debug Set to True");
			}
			if (cmd.hasOption('s')) {
				DataManager.disableSession = true;
				Constants.printVerboseMessage(Level.INFO, "DisableSession Set to True");
			}
			if (cmd.hasOption('p')) {
				CommandData.enableLogging(true);
				Constants.printVerboseMessage(Level.INFO, "Enabled print commands execution");
			}
			if (cmd.hasOption('e')) {
				EventManager.enableLogging(true);
				Constants.printVerboseMessage(Level.INFO, "Enabled print event trigger");
			}
			String nickname = null;
			String channel = null;
			String oauth = null;
			if (cmd.hasOption('n')) {
				amountOfRequiredTwitchSessionOptions++;
				nickname = cmd.getOptionValue('n');
			}
			if (cmd.hasOption('c')) {
				// amountOfRequiredTwitchSessionOptions++;
				channel = cmd.getOptionValue('c');
			}
			if (cmd.hasOption('t')) {
				amountOfRequiredTwitchSessionOptions++;
				oauth = cmd.getOptionValue('t');
			}

			String secret = null;
			String videoId = null;
			if (cmd.hasOption('y')) {
				String path = cmd.getOptionValue('y');
				try {
					File file = new File(path);
					secret = Files.readString(file.toPath());
					if (secret != null && !secret.isBlank())
						amountOfRequiredYouTubeSessionOptions++;
				} catch (Exception e) {
				}
			}
			if (cmd.hasOption('i')) {
				videoId = cmd.getOptionValue('i');
			}

			globalSetUp();
			channel = channel == null ? null : String.format("#%s", channel);
			if (amountOfRequiredTwitchSessionOptions >= 2 && amountOfRequiredYouTubeSessionOptions >= 1) {
				MainLoginPanel.getInstance().setVisible(false);
				MainFrame.getInstance();
				skipMainLoginPanel(nickname, channel, oauth, secret, videoId);
				MainLoginPanel.getInstance().setVisible(true);
			} else {
				MainFrame.getInstance();
				if (amountOfRequiredTwitchSessionOptions >= 2) {
					TwitchSessionData twitch = new TwitchSessionData(nickname, channel, oauth);
					DataManager.setTwitchSession(twitch);
					TwitchLoginPanel.getInstance().setSession(twitch);
				} else {
					TwitchLoginPanel.getInstance().setSession(nickname, channel, oauth);
				}
				if (amountOfRequiredYouTubeSessionOptions >= 1) {
					YouTubeSessionData youtube = new YouTubeSessionData(secret, videoId);
					DataManager.setYouTubeSession(youtube);
					YoutubeLoginPanel.getInstance().setSession(youtube);
				} else {
					YoutubeLoginPanel.getInstance().setSession(secret, videoId);
				}
			}
		} catch (MissingArgumentException | AlreadySelectedException e) {
			System.out.println(e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar ATPXapp.jar", options, true);
		} catch (Exception e) {
			Constants.showExpectedExceptionDialog(e);
		} catch (UnsatisfiedLinkError e) {
			// TODO change
			Constants.printVerboseMessage(Level.WARNING, e);
			JLabel exception = new JLabel(e.getMessage());
			exception.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
			Constants.showCustomColorMessageDialog(null, exception, e.getClass().getName(), JOptionPane.WARNING_MESSAGE,
					null, Constants.TWITCH_COLOR);
		}
	}

	private static final String getVersion() {
		String version = "\\?.?.?";
		try {
			Properties p = new Properties();
			p.load(App.class.getResourceAsStream("/META-INF/maven/pt.theninjask/anothertwitchplaysx/pom.properties"));
			version = p.getProperty("version", version);
		} catch (Exception e) {
		}

		return version;
	}

	private static void skipMainLoginPanel(String nickname, String channel, String oauth, String secret,
			String videoId) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.skipMainLoginPanel()", App.class.getSimpleName()));
		TwitchSessionData twitch = new TwitchSessionData(nickname, channel, oauth);
		YouTubeSessionData youtube = new YouTubeSessionData(secret, videoId);
		DataManager.setTwitchSession(twitch);
		DataManager.setYouTubeSession(youtube);
		TwitchLoginPanel.getInstance().setSession(twitch);
		YoutubeLoginPanel.getInstance().setSession(youtube);
		TwitchPlayer.getInstance().setSession(twitch);
		YouTubePlayer.getInstance().setSession(youtube);
		MainFrame.replacePanel(MainMenuPanel.getInstance());
	}

	private static void globalSetUp() throws Exception {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.globalSetUp()", App.class.getSimpleName()));
		PrintStream tmp = System.out;
		try {
			DataManager.setLanguage(new EnglishLang());
			// Get the logger for "com.github.kwhat.jnativehook" and set the level to off.
			// LogManager.getLogManager().reset();
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.OFF);

			// Don't forget to disable the parent handlers.
			logger.setUseParentHandlers(false);

			logger = Logger.getLogger(AuthorizationCodeInstalledApp.class.getName());
			logger.setLevel(Level.OFF);
			logger.setUseParentHandlers(false);
			// This below feels dishonest but I really want the console to be clear
			// so to prevent printing this is the cheat
			// TODO add credit other place where it will have more visibility
			// also this can work as a compatibility test now that I think about it
			System.setOut(Constants.VOID_STREAM);
			GlobalScreen.registerNativeHook();
			GlobalScreen.unregisterNativeHook();
			System.setOut(tmp);

			Path path = Paths.get(Constants.SAVE_PATH);
			if (!Files.exists(path)) {
				Files.createDirectory(path);
				// Files.setAttribute(path, "dos:hidden", true);
			} else if (!Files.isDirectory(path)) {
				throw new RuntimeException(String.format("%s is not a Directory!", Constants.SAVE_PATH));
			}
			
			Path modFolderPath = Paths.get(Constants.SAVE_PATH, Constants.MOD_FOLDER);
			if (!Files.exists(modFolderPath)) {
				Files.createDirectory(modFolderPath);
				// Files.setAttribute(path, "dos:hidden", true);
			} else if (!Files.isDirectory(modFolderPath)) {
				throw new RuntimeException(String.format("%s is not a Directory!", Constants.SAVE_PATH));
			} else {
				File modFolder = modFolderPath.toFile();
				for (File modFile : modFolder.listFiles()) {
					if(modFile.isFile() && modFile.getName().endsWith(".jar")) {
						try {
							ATPXMod mod = Constants.loadMod(modFile);
							if(mod==null)
								continue;
							mod.refresh();
							if (mod.getClass().getAnnotation(ATPXModProps.class).hasPanel())
								if (mod.getClass().getAnnotation(ATPXModProps.class).popout())
									new PopOutFrame(mod.getJPanelInstance());
								else
									MainFrame.replacePanel(mod.getJPanelInstance());
							if (mod.getClass().getAnnotation(ATPXModProps.class).keepLoaded())
								ATPXModManager.addMod(mod);
						}catch (Exception e) {
							Constants.showMessageDialog(String.format("Could not load mod %s", modFile.getName()), "Mod Not Loaded");
						}
					}
				}
			}

			ControlData.setTranslation(Constants.STRING_TO_KEYCODE);
			KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyPressedAdapter());
		} catch (Exception | Error e) {
			System.setOut(tmp);
			throw e;
		}
	}
}
