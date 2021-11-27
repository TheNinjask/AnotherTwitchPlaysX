package pt.theninjask.AnotherTwitchPlaysX;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;

import pt.theninjask.AnotherTwitchPlaysX.data.ATPXConfig;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.data.ControlData;
import pt.theninjask.AnotherTwitchPlaysX.data.TwitchSessionData;
import pt.theninjask.AnotherTwitchPlaysX.data.YouTubeSessionData;
import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
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
import pt.theninjask.AnotherTwitchPlaysX.util.Constants.GitHubReleaseJson;
import pt.theninjask.AnotherTwitchPlaysX.util.ExternalConsole;
import pt.theninjask.AnotherTwitchPlaysX.util.KeyPressedAdapter;
import pt.theninjask.AnotherTwitchPlaysX.util.RedirectorErrorOutputStream;
import pt.theninjask.AnotherTwitchPlaysX.util.RedirectorInputStream;
import pt.theninjask.AnotherTwitchPlaysX.util.RedirectorOutputStream;
import pt.theninjask.AnotherTwitchPlaysX.util.ThreadPool;
import pt.theninjask.AnotherTwitchPlaysX.util.WrapEditorKit;

public class App {

	public static final String ID = "ATPX";

	public static final String NAME = "AnotherTwitchPlaysX";

	public static final String VERSION = getVersion();

	public static void main(String[] args) {

		RedirectorOutputStream.changeRedirect(System.out);
		RedirectorOutputStream.changeDefault(System.out);
		System.setOut(RedirectorOutputStream.getInstance());

		RedirectorErrorOutputStream.changeRedirect(System.err);
		RedirectorErrorOutputStream.changeDefault(System.err);
		System.setErr(RedirectorErrorOutputStream.getInstance());

		RedirectorInputStream.changeRedirect(System.in);
		RedirectorInputStream.changeDefault(System.in);
		System.setIn(RedirectorInputStream.getInstance());
		
		DataManager.setLanguage(new EnglishLang());
		DataManager.setTheme(Constants.TWITCH_THEME);
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyPressedAdapter());
		
		int amountOfRequiredTwitchSessionOptions = 0;
		int amountOfRequiredYouTubeSessionOptions = 0;
		Options options = new Options();

		options.addOption("o", "outsideConsole", false, "Outside \"Console\"");

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
			ATPXConfig config = loadConfigFile();
			if(config.getTheme()!=null) {
				DataManager.setTheme(Constants.THEMES.getOrDefault(config.getTheme(), Constants.TWITCH_THEME));
			}
			if (cmd.hasOption('o') || config.isOutsideConsole()) {
				RedirectorOutputStream.changeRedirect(ExternalConsole.getExternalConsoleOutputStream());
				RedirectorErrorOutputStream.changeRedirect(ExternalConsole.getExternalConsoleErrorOutputStream());
				RedirectorInputStream.changeRedirect(ExternalConsole.getExternalConsoleInputStream());
				ExternalConsole.setViewable(true);
			}
			if (cmd.hasOption('h') || config.isHelp()) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("java -jar ATPXapp.jar", options, true);
				ExternalConsole.setClosable(true);
				return;
			}
			if (cmd.hasOption('v') || config.getVerbose() == ATPXConfig.Verbose.VERBOSE) {
				Constants.setLoggerLevel(Level.ALL);
				Constants.printVerboseMessage(Level.INFO, String.format("%s - version: %s", NAME, VERSION));
				Constants.printVerboseMessage(Level.INFO, "Verbose Set to ALL");
			} else if (cmd.hasOption('w') || config.getVerbose() == ATPXConfig.Verbose.WARNING) {
				Constants.setLoggerLevel(Level.WARNING);
				Constants.printVerboseMessage(Level.WARNING, String.format("%s - version: %s", NAME, VERSION));
				Constants.printVerboseMessage(Level.WARNING, "Verbose Set to WARN");
			}
			if (cmd.hasOption('d') || config.isDebug()) {
				Constants.debug = true;
				Constants.printVerboseMessage(Level.INFO, "Debug Set to True");
			}
			if (cmd.hasOption('s') || config.isDisableSession()) {
				DataManager.disableSession = true;
				Constants.printVerboseMessage(Level.INFO, "DisableSession Set to True");
			}
			if (cmd.hasOption('p') || config.isPrintCommand()) {
				CommandData.enableLogging(true);
				Constants.printVerboseMessage(Level.INFO, "Enabled print commands execution");
			}
			if (cmd.hasOption('e') || config.isEventLog()) {
				EventManager.enableLogging(true);
				Constants.printVerboseMessage(Level.INFO, "Enabled print event trigger");
			}
			String nickname = null;
			String channel = null;
			String oauth = null;
			if (cmd.hasOption('n') || config.getTwitchNickname() != null) {
				amountOfRequiredTwitchSessionOptions++;
				if (cmd.hasOption('n'))
					nickname = cmd.getOptionValue('n');
				else
					nickname = config.getTwitchNickname();
			}
			if (cmd.hasOption('c') || config.getTwitchChannel() != null) {
				// amountOfRequiredTwitchSessionOptions++;
				channel = cmd.getOptionValue('c');
				if (cmd.hasOption('c'))
					channel = cmd.getOptionValue('c');
				else
					channel = config.getTwitchChannel();
			}
			if (cmd.hasOption('t') || config.getTwitchToken() != null) {
				amountOfRequiredTwitchSessionOptions++;
				if (cmd.hasOption('t'))
					oauth = cmd.getOptionValue('t');
				else
					oauth = config.getTwitchToken();
			}

			String secret = null;
			String videoId = null;
			if (cmd.hasOption('y') || config.getYoutubeToken() != null) {
				String path;
				if (cmd.hasOption('y'))
					path = cmd.getOptionValue('y');
				else
					path = config.getYoutubeToken();
				try {
					File file = new File(path);
					secret = Files.readString(file.toPath());
					if (secret != null && !secret.isBlank())
						amountOfRequiredYouTubeSessionOptions++;
				} catch (Exception e) {
				}
			}
			if (cmd.hasOption('i') || config.getYoutubeVideoId() != null) {
				if (cmd.hasOption('i'))
					videoId = cmd.getOptionValue('i');
				else
					videoId = config.getYoutubeVideoId();
			}

			globalSetUp();
			channel = channel == null ? null : String.format("#%s", channel);
			MainFrame.getInstance();
			if (amountOfRequiredTwitchSessionOptions >= 2 && amountOfRequiredYouTubeSessionOptions >= 1) {
				// MainLoginPanel.getInstance().setVisible(false);
				skipMainLoginPanel(nickname, channel, oauth, secret, videoId);
				// MainLoginPanel.getInstance().setVisible(true);
			} else {
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
			MainFrame.getInstance().setVisible(true);
		} catch (MissingArgumentException | AlreadySelectedException e) {
			ExternalConsole.setClosable(true);
			System.out.println(e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar ATPXapp.jar", options, true);
		} catch (Exception e) {
			ExternalConsole.setClosable(true);
			Constants.showExpectedExceptionDialog(e);
		} catch (UnsatisfiedLinkError e) {
			// TODO change
			ExternalConsole.setClosable(true);
			Constants.printVerboseMessage(Level.WARNING, e);
			JLabel exception = new JLabel(e.getMessage());
			exception.setForeground(DataManager.getTheme().getFont());
			Constants.showCustomColorMessageDialog(null, exception, e.getClass().getName(), JOptionPane.WARNING_MESSAGE,
					null, DataManager.getTheme().getBackground());
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

	private static ATPXConfig loadConfigFile() {
		File configFile = new File(Constants.SAVE_PATH, Constants.CONFIG_FILE);
		if (configFile.exists())
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				ATPXConfig config = objectMapper.readValue(configFile, ATPXConfig.class);
				return config;
			} catch (IOException e) {
				Constants.showExceptionDialog(e);
			}
		return new ATPXConfig();
	}

	private static void checkForUpdate() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.checkForUpdate()", App.class.getSimpleName()));
		GitHubReleaseJson update = Constants.getLatestRelease();
		if (update == null)
			return;
		StringBuilder builder = new StringBuilder(update.tag_name.replaceAll("[^\\d]", ""));
		builder.insert(0, 0);
		int gitVersion = Integer.parseInt(builder.toString());
		builder = new StringBuilder(VERSION.replaceAll("[^\\d]", ""));
		builder.insert(0, 0);
		int currentVersion = Integer.parseInt(builder.toString());
		if (gitVersion <= currentVersion)
			return;

		JPanel content = new JPanel(new BorderLayout());
		JScrollPane scroll = new JScrollPane();
		JTextPane message = new JTextPane();
		JTextField title = new JTextField();
		scroll = new JScrollPane();
		message = new JTextPane();
		
		title.setOpaque(false);
		title.setForeground(DataManager.getTheme().getFont());
		title.setText(String.format(DataManager.getLanguage().getUpdateNoticeTitleContent(), update.tag_name, update.update_name));
		title.setBorder(null);
		title.setFont(new Font(title.getFont().getFontName(), title.getFont().getStyle(), title.getFont().getSize()+7));
		title.setEditable(false);
		
		message.setEditorKit(new WrapEditorKit());
		message.setEditable(false);
		scroll.setViewportView(message);
		scroll.setFocusable(false);
		scroll.setEnabled(false);
		scroll.setBorder(null);
		scroll.setWheelScrollingEnabled(true);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);

		message.setOpaque(false);
		message.setForeground(DataManager.getTheme().getFont());
		
		scroll.setPreferredSize(new Dimension(151, 151));
		content.add(scroll, BorderLayout.CENTER);
		content.add(title, BorderLayout.NORTH);
		content.setOpaque(false);
		
		message.setText(update.body);
		
		String[] options = { DataManager.getLanguage().getUpdateNoticeWebsiteOption(),
				DataManager.getLanguage().getUpdateNoticeDownloadOption(),
				DataManager.getLanguage().getUpdateNoticeSkipOption() };

		int resp = Constants.showCustomColorOptionDialog(null, content,
				DataManager.getLanguage().getUpdateNoticeTitle(), JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, null, DataManager.getTheme().getBackground());
		switch (resp) {
		case JOptionPane.YES_OPTION:
			Constants.openWebsite(update.html_url);
			break;
		case JOptionPane.NO_OPTION:
			Constants.openWebsite(update.assets.get(0).browser_download_url);
			break;
		default:
		case JOptionPane.CANCEL_OPTION:
		case JOptionPane.CLOSED_OPTION:
			break;
		}
	}

	private static void globalSetUp() throws Exception {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.globalSetUp()", App.class.getSimpleName()));
		PrintStream tmp = System.out;
		try {
			ThreadPool.execute(()->{
				checkForUpdate();
			});

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
				throw new RuntimeException(String.format(DataManager.getLanguage().getExceptions().getNotDirectory(),
						Constants.SAVE_PATH));
			}

			Path cmdsFolderPath = Paths.get(Constants.SAVE_PATH, Constants.CMD_FOLDER);
			if (!Files.exists(cmdsFolderPath)) {
				Files.createDirectory(cmdsFolderPath);
				// Files.setAttribute(path, "dos:hidden", true);
			} else if (!Files.isDirectory(cmdsFolderPath)) {
				throw new RuntimeException(String.format(DataManager.getLanguage().getExceptions().getNotDirectory(),
						Constants.SAVE_PATH));
			}

			Path modFolderPath = Paths.get(Constants.SAVE_PATH, Constants.MOD_FOLDER);
			if (!Files.exists(modFolderPath)) {
				Files.createDirectory(modFolderPath);
				// Files.setAttribute(path, "dos:hidden", true);
			} else if (!Files.isDirectory(modFolderPath)) {
				throw new RuntimeException(String.format(DataManager.getLanguage().getExceptions().getNotDirectory(),
						Constants.SAVE_PATH));
			} else {
				File modFolder = modFolderPath.toFile();
				for (File modFile : modFolder.listFiles()) {
					if (modFile.isFile() && modFile.getName().endsWith(".jar")) {
						try {
							ATPXMod mod = Constants.loadMod(modFile);
							if (mod == null)
								continue;
							mod.refresh();
							if (mod.getClass().getAnnotation(ATPXModProps.class).hasPanel())
								if (mod.getClass().getAnnotation(ATPXModProps.class).popout())
									new PopOutFrame(mod.getJPanelInstance());
								else
									MainFrame.replacePanel(mod.getJPanelInstance());
							if (mod.getClass().getAnnotation(ATPXModProps.class).keepLoaded())
								ATPXModManager.addMod(mod);
						} catch (Exception e) {
							Constants.showMessageDialog(
									String.format(DataManager.getLanguage().getAutoLoadModFail(), modFile.getName()),
									DataManager.getLanguage().getAutoLoadModFailTitle());
						}
					}
				}
			}
			ControlData.setTranslation(Constants.STRING_TO_KEYCODE);
		} catch (Exception | Error e) {
			System.setOut(tmp);
			throw e;
		}
	}
}
