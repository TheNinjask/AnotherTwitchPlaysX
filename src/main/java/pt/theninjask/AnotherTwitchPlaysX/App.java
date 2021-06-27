package pt.theninjask.AnotherTwitchPlaysX;

import java.awt.KeyboardFocusManager;
import java.io.PrintStream;
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

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.data.ControlData;
import pt.theninjask.AnotherTwitchPlaysX.data.SessionData;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.LoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.lan.en.EnglishLang;
import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.twitch.TwitchPlayer;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.KeyPressedAdapter;

public class App {

	public static void main(String[] args) {

		int amountOfSessionOptions = 0;

		Options options = new Options();

		OptionGroup verbose = new OptionGroup();
		verbose.addOption(new Option("v", "verbose", false, "Sets Verbose to ALL"));
		verbose.addOption(new Option("w", "verboseWarning", false, "Sets Verbose to WARN"));
		options.addOptionGroup(verbose);

		options.addOption("p", "printCommand", false, "Print Commands execution");
		
		options.addOption("d", "debug", false, "Enables printStackTrace()");
		options.addOption("s", "disableSession", false,
				"Disables access to SessionData from DataManager (might \"brick\" app)\nMight be useful to check when a external mod gains access");
		options.addOption("h", "help", false, "Prints Help");

		options.addOption("n", "nickname", true, "Twitch Account Nickname");
		options.addOption("c", "channel", true, "Twitch Channel Name");
		options.addOption("t", "token", true, "Twitch OAuth Token of nickname");

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
				Constants.printVerboseMessage(Level.INFO, "Verbose Set to ALL");
			} else if (cmd.hasOption('w')) {
				Constants.setLoggerLevel(Level.WARNING);
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
			if(cmd.hasOption('p')) {
				CommandData.enableLogging(true);
				Constants.printVerboseMessage(Level.INFO, "Enabled print commands exetution");
			}
			String nickname = null;
			String channel = null;
			String oauth = null;
			if (cmd.hasOption('n')) {
				amountOfSessionOptions++;
				nickname = cmd.getOptionValue('n');
			}
			if (cmd.hasOption('c')) {
				amountOfSessionOptions++;
				channel = cmd.getOptionValue('c');
			}
			if (cmd.hasOption('t')) {
				amountOfSessionOptions++;
				oauth = cmd.getOptionValue('t');
			}
			globalSetUp();

			if (amountOfSessionOptions >= 3) {
				LoginPanel.getInstance().setVisible(false);
				MainFrame.getInstance();
				skipLoginPanel(nickname, channel, oauth);
				LoginPanel.getInstance().setVisible(true);
			} else {
				MainFrame.getInstance();
				LoginPanel.getInstance().setSession(nickname, channel == null? null : String.format("#%s", channel), oauth);
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

	private static void skipLoginPanel(String nickname, String channel, String oauth) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.skipLoginPanel()",App.class.getSimpleName()));
		SessionData session = new SessionData(nickname, String.format("#%s", channel), oauth);
		DataManager.setSession(session);
		LoginPanel.getInstance().setSession(session);
		TwitchPlayer.getInstance().setSession(session);
		MainFrame.replacePanel(MainMenuPanel.getInstance());
	}

	private static void globalSetUp() throws Exception {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.globalSetUp()",App.class.getSimpleName()));
		PrintStream tmp = System.out;
		try {
			// Get the logger for "com.github.kwhat.jnativehook" and set the level to off.
			// LogManager.getLogManager().reset();
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			logger.setLevel(Level.OFF);

			// Don't forget to disable the parent handlers.
			logger.setUseParentHandlers(false);

			// This below feels dishonest but I really want the console to be clear
			// so to prevent printing this is the cheat
			// TODO add credit other place where it will have more visibility
			// also this can work as a compatibility test now that I think about it
			System.setOut(null);
			GlobalScreen.registerNativeHook();
			GlobalScreen.unregisterNativeHook();
			System.setOut(tmp);

			ControlData.setTranslation(Constants.STRING_TO_KEYCODE);
			DataManager.setLanguage(new EnglishLang());
			KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyPressedAdapter());
		} catch (Exception | Error e) {
			System.setOut(tmp);
			throw e;
		}
	}
}
