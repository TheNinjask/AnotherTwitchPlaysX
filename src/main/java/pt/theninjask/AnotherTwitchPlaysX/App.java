package pt.theninjask.AnotherTwitchPlaysX;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.jnativehook.GlobalScreen;

import pt.theninjask.AnotherTwitchPlaysX.data.ControlData;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class App {

	public static void main(String[] args) {
		try {
			Options options = new Options();
			options.addOption("v", "verbose", false, "Set Verbose - default ALL, options are: WARN, ALL");
			options.addOption("d", "debug", false, "Enables printStackTrace()");
			options.addOption("s", "disableSession", false,
					"Disables access to SessionData from DataManager (might \"brick\" app)\nMight be useful to check when a external mod gains access");
			options.addOption("h", "help", false, "Prints Help");
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption('h')) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("java -jar ATPXapp.jar", options, true);
				return;
			}
			if (cmd.hasOption('v')) {
				String val = cmd.getOptionValue('v');
				if(val!=null)
					switch (val) {
						case "WARN":
							Constants.setLoggerLevel(Level.WARNING);
							break;
						case "ALL":
						default:
							Constants.setLoggerLevel(Level.ALL);
							break;
				}else {
					Constants.setLoggerLevel(Level.ALL);
				}
			}
			if (cmd.hasOption('d'))
				Constants.debug = true;
			if (cmd.hasOption('s'))
				Constants.disableSession = true;
			globalSetUp();
			MainFrame.getInstance();
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

	private static void globalSetUp() throws Exception {
		Constants.printVerboseMessage(Level.INFO, "globalSetUp()");
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
		} catch (Exception | Error e) {
			System.setOut(tmp);
			throw e;
		}
	}
}
