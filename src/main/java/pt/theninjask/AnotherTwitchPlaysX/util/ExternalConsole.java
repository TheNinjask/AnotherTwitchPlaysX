package pt.theninjask.AnotherTwitchPlaysX.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.App;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.event.externalconsole.InputCommandExternalConsoleEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.ChatFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModManager;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants.GitHubReleaseJson;

public class ExternalConsole extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JScrollPane scroll;
	private JTextPane console;
	private ExternalConsoleOutputStream out;
	private ExternalConsoleErrorOutputStream err;
	private ExternalConsoleInputStream in;

	private JPanel messagePanel;

	private JTextField input;

	private boolean autoScroll;

	private Map<String, ExternalConsoleCommand> cmds;

	private ColorTheme currentTheme;

	private static ExternalConsoleCommand help = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "help";
		}

		@Override
		public String getDescription() {
			return "Shows all commands and their descriptions";
		}

		@Override
		public boolean executeCommand(String[] args) {
			println("Available Commands:");
			List<ExternalConsoleCommand> helpSorted = singleton.cmds.values().stream()
					.sorted(ExternalConsoleCommand.comparator).toList();
			for (ExternalConsoleCommand cmd : helpSorted) {
				// int spacing = 4 + cmd.getCommand().length();
				println(String.format("\t%s - %s", cmd.getCommand(),
						// cmd.getDescription().replaceAll("\n", "\n\t" + " ".repeat(spacing))));
						cmd.getDescription().replaceAll("\n", "\n\t\t")));
			}
			return true;
		}
	};

	private static ExternalConsoleCommand autoscroll = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "autoscroll";
		}

		@Override
		public String getDescription() {
			return "Enables/Disables autoscrolling of ExternalConsole";
		}

		@Override
		public boolean executeCommand(String[] args) {
			Options options = new Options();
			OptionGroup scroll = new OptionGroup();
			// scroll.setRequired(true);
			scroll.addOption(new Option("a", "auto", false, "Sets Scroll to Auto"));
			scroll.addOption(new Option("m", "manual", false, "Sets Scroll to Manual"));
			options.addOptionGroup(scroll);
			try {
				CommandLineParser parser = new DefaultParser();
				// CommandLine cmd = parser.parse(options, args);
				parser.parse(options, args);
				switch (String.valueOf(scroll.getSelected())) {
				case "a":
					singleton.autoScroll = true;
					break;
				case "m":
					singleton.autoScroll = false;
					break;
				default:
					println(String.format("autoscroll is set as: %s", singleton.autoScroll));
					println("Options: -a, -m");
					break;
				}

			} catch (ParseException e) {
				println(e.getMessage());
				return false;
			}
			return true;
		}

		@Override
		public String[] getParamOptions(int number, String[] currArgs) {
			switch (number) {
			case 0:
				return new String[] { "--auto", "--manual" };
			default:
				return null;
			}
		}
	};

	private static ExternalConsoleCommand verbose = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "verbose";
		}

		@Override
		public String getDescription() {
			return "Enables/Disables Verbose mode of app";
		}

		@Override
		public boolean executeCommand(String[] args) {
			Options options = new Options();
			OptionGroup verbose = new OptionGroup();
			// verbose.setRequired(true);
			verbose.addOption(new Option("n", "none", false, "Sets Verbose to OFF"));
			verbose.addOption(new Option("v", "verbose", false, "Sets Verbose to ALL"));
			verbose.addOption(new Option("w", "warning", false, "Sets Verbose to WARN"));
			options.addOptionGroup(verbose);
			try {
				CommandLineParser parser = new DefaultParser();
				parser.parse(options, args);
				switch (String.valueOf(verbose.getSelected())) {
				case "n":
					Constants.setLoggerLevel(Level.OFF);
					break;
				case "v":
					Constants.setLoggerLevel(Level.ALL);
					break;
				case "w":
					Constants.setLoggerLevel(Level.WARNING);
					break;
				default:
					println(String.format("verbose is set as: %s", Constants.getLoggerLevel()));
					println("Options: -n, -v, -w");
					break;
				}
			} catch (ParseException e) {
				println(e.getMessage());
				return false;
			}
			return true;
		}

		@Override
		public String[] getParamOptions(int number, String[] currArgs) {
			switch (number) {
			case 0:
				return new String[] { "--none", "--verbose", "--warning" };
			default:
				return null;
			}
		}
	};

	private static ExternalConsoleCommand printcommand = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "printcommand";
		}

		@Override
		public String getDescription() {
			return "Enables/Disables printing of CommandData execution";
		}

		@Override
		public boolean executeCommand(String[] args) {
			Options options = new Options();
			OptionGroup print = new OptionGroup();
			// print.setRequired(true);
			print.addOption(new Option("t", "true", false, "Enabled print commands execution"));
			print.addOption(new Option("f", "false", false, "Disabled print commands execution"));
			options.addOptionGroup(print);
			try {
				CommandLineParser parser = new DefaultParser();
				parser.parse(options, args);
				switch (String.valueOf(print.getSelected())) {
				case "t":
					CommandData.enableLogging(true);
					break;
				case "f":
					CommandData.enableLogging(false);
					break;
				default:
					println(String.format("Print Commands Execution is set as: %s", CommandData.isEnableLogging()));
					println("Options: -t, -f");
					break;
				}
			} catch (ParseException e) {
				println(e.getMessage());
				return false;
			}
			return true;
		}

		@Override
		public String[] getParamOptions(int number, String[] currArgs) {
			switch (number) {
			case 0:
				return new String[] { "--true", "--false" };
			default:
				return null;
			}
		}
	};

	private static ExternalConsoleCommand eventlog = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "eventlog";
		}

		@Override
		public String getDescription() {
			return "Logs app events";
		}

		@Override
		public boolean executeCommand(String[] args) {
			Options options = new Options();
			OptionGroup event = new OptionGroup();
			// event.setRequired(true);
			event.addOption(new Option("t", "true", false, "Enabled print event trigger"));
			event.addOption(new Option("f", "false", false, "Disabled print event trigger"));
			options.addOptionGroup(event);
			try {
				CommandLineParser parser = new DefaultParser();
				parser.parse(options, args);
				switch (String.valueOf(event.getSelected())) {
				case "t":
					EventManager.enableLogging(true);
					break;
				case "f":
					EventManager.enableLogging(false);
					break;
				default:
					println(String.format("Print Event Trigger is set as: %s", EventManager.isEnableLogging()));
					println("Options: -t, -f");
					break;
				}

			} catch (ParseException e) {
				println(e.getMessage());
				return false;
			}
			return true;
		}

		@Override
		public String[] getParamOptions(int number, String[] currArgs) {
			switch (number) {
			case 0:
				return new String[] { "--true", "--false" };
			default:
				return null;
			}
		}
	};

	private static ExternalConsoleCommand debug = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "debug";
		}

		@Override
		public String getDescription() {
			return "Enables/Disables printStackTrace()";
		}

		@Override
		public boolean executeCommand(String[] args) {
			Options options = new Options();
			OptionGroup debug = new OptionGroup();
			// debug.setRequired(true);
			debug.addOption(new Option("t", "true", false, "Debug Set to True"));
			debug.addOption(new Option("f", "false", false, "Debug Set to False"));
			options.addOptionGroup(debug);
			try {
				CommandLineParser parser = new DefaultParser();
				parser.parse(options, args);
				switch (String.valueOf(debug.getSelected())) {
				case "t":
					Constants.debug = true;
					break;
				case "f":
					Constants.debug = false;
					break;
				default:
					println(String.format("Debug is set as: %s", Constants.debug));
					println("Options: -t, -f");
					break;
				}
			} catch (ParseException e) {
				println(e.getMessage());
				return false;
			}
			return true;
		}

		@Override
		public String[] getParamOptions(int number, String[] currArgs) {
			switch (number) {
			case 0:
				return new String[] { "--true", "--false" };
			default:
				return null;
			}
		}
	};

	private static ExternalConsoleCommand disablesession = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "disablesession";
		}

		@Override
		public String getDescription() {
			return "Disables/Enables access to SessionData from DataManager (might \"brick\" app)\nMight be useful to check when a external mod gains access";
		}

		@Override
		public boolean executeCommand(String[] args) {
			Options options = new Options();
			OptionGroup disableSession = new OptionGroup();
			// disableSession.setRequired(true);
			disableSession.addOption(new Option("t", "true", false, "DisableSession Set to True"));
			disableSession.addOption(new Option("f", "false", false, "DisableSession Set to False"));
			options.addOptionGroup(disableSession);
			try {
				CommandLineParser parser = new DefaultParser();
				parser.parse(options, args);
				switch (String.valueOf(disableSession.getSelected())) {
				case "t":
					DataManager.disableSession = true;
					break;
				case "f":
					DataManager.disableSession = false;
					break;
				default:
					println(String.format("DisableSession is set as: %s", DataManager.disableSession));
					println("Options: -t, -f");
					break;
				}
			} catch (ParseException e) {
				println(e.getMessage());
				return false;
			}
			return true;
		}

		@Override
		public String[] getParamOptions(int number, String[] currArgs) {
			switch (number) {
			case 0:
				return new String[] { "--true", "--false" };
			default:
				return null;
			}
		}
	};

	private static ExternalConsoleCommand clear = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "cls";
		}

		@Override
		public String getDescription() {
			return "Clears ExternalConsole";
		}

		@Override
		public boolean executeCommand(String[] args) {
			singleton.console.setText("");
			return true;
		}
	};

	private static ExternalConsoleCommand appfolder = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "appfolder";
		}

		@Override
		public String getDescription() {
			return "Opens App Folder";
		}

		@Override
		public boolean executeCommand(String[] args) {
			try {
				Desktop.getDesktop().open(new File(Constants.SAVE_PATH));
			} catch (IOException e) {
				println(e.getMessage());
				return false;
			}
			return true;
		}
	};

	private static ExternalConsoleCommand mod = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "mod";
		}

		@Override
		public String getDescription() {
			return "Operations on mods";
		}

		@Override
		public boolean executeCommand(String[] args) {
			Options options = new Options();
			OptionGroup mod = new OptionGroup();
			// print.setRequired(true);
			mod.addOption(new Option("a", "add", false, "Adds a mod"));
			mod.addOption(new Option("r", "remove", true, "Removes a mod (their effects may linger.)"));
			mod.addOption(new Option("c", "clear", false, "Removes all mods (their effects may linger.)"));
			mod.addOption(new Option("l", "list", false, "Lists All Mods"));
			options.addOptionGroup(mod);
			try {
				CommandLineParser parser = new DefaultParser();
				CommandLine cmd = parser.parse(options, args);
				switch (String.valueOf(mod.getSelected())) {
				case "a":
					try {
						File file = Constants.showOpenFile(new FileNameExtensionFilter("JAR", "jar"), null,
								Paths.get(Constants.SAVE_PATH, Constants.MOD_FOLDER).toFile());
						ATPXMod newMod = Constants.loadMod(file);
						if (newMod == null)
							return true;
						newMod.refresh();
						if (newMod.getClass().getAnnotation(ATPXModProps.class).hasPanel())
							if (newMod.getClass().getAnnotation(ATPXModProps.class).popout())
								new PopOutFrame(newMod.getJPanelInstance()).setVisible(true);
							else
								MainFrame.replacePanel(newMod.getJPanelInstance());
						if (newMod.getClass().getAnnotation(ATPXModProps.class).keepLoaded()) {
							ATPXModManager.addMod(newMod);
							if (newMod.getClass().getAnnotation(ATPXModProps.class).hasPanel())
								MainMenuPanel.getInstance().setMod(newMod);
						}
						println(String.format("Loaded Mod %s.", newMod.getClass().getSimpleName()));
					} catch (Exception e) {
						println(e.getMessage());
						return false;
					}
					break;
				case "r":
					String modName = cmd.getOptionValue('r');
					/**
					 * The weird/spaghetti code below is to avoid having a reference to the mod
					 */
					if (ATPXModManager.getAllMods().parallelStream().filter((m) -> {
						if (m.getClass().getSimpleName().equals(modName)) {
							ATPXModManager.removeMod(m);
							if (MainMenuPanel.getInstance().getMod() == m)
								MainMenuPanel.getInstance().setMod(null);
							return true;
						}
						return false;
					}).toList().isEmpty()) {
						println("No mod found!");
					} else {
						System.gc();
						println("Mod removed.");
					}
					break;
				case "c":
					MainMenuPanel.getInstance().setMod(null);
					ATPXModManager.removeAllMods();
					System.gc();
					println("Mods cleared!");
					break;
				case "l":
					ArrayList<ATPXMod> list = ATPXModManager.getAllMods();
					if (list.isEmpty()) {
						println("No mods loaded.");
						break;
					}
					println("All mods loaded:");
					for (ATPXMod atpxMod : list) {
						println(String.format("\t%s", atpxMod.getClass().getSimpleName()));
					}
					break;
				default:
					println("Options: -a, -r <mod>, -c, -l");
					break;
				}
			} catch (ParseException e) {
				println(e.getMessage());
				return false;
			}
			return true;
		}

		@Override
		public String[] getParamOptions(int number, String[] currArgs) {
			switch (number) {
			case 0:
				return new String[] { "--add", "--remove", "--clear", "--list" };
			case 1:
				switch (currArgs[0]) {
				case "-r":
				case "-remove":
				case "--remove":
					return ATPXModManager.getAllMods().stream().map(c -> {
						return c.getClass().getSimpleName();
					}).toList().toArray(new String[0]);
				default:
					return null;
				}

			default:
				return null;
			}
		}
	};

	private static ExternalConsoleCommand hide = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "hide";
		}

		@Override
		public String getDescription() {
			return "Hides External Console";
		}

		@Override
		public boolean executeCommand(String[] args) {
			singleton.setExtendedState(JFrame.ICONIFIED);
			return true;
		}
	};

	private static ExternalConsoleCommand top = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "top";
		}

		@Override
		public String getDescription() {
			return "Flag for ExternalConsole be always on top";
		}

		@Override
		public boolean executeCommand(String[] args) {
			Options options = new Options();
			OptionGroup top = new OptionGroup();
			top.addOption(new Option("t", "true", false, "AlwaysOnTop Set to True"));
			top.addOption(new Option("f", "false", false, "AlwaysOnTop Set to False"));
			options.addOptionGroup(top);
			try {
				CommandLineParser parser = new DefaultParser();
				parser.parse(options, args);
				switch (String.valueOf(top.getSelected())) {
				case "t":
					singleton.setAlwaysOnTop(true);
					break;
				case "f":
					singleton.setAlwaysOnTop(false);
					break;
				default:
					println(String.format("AlwaysOnTop is set as: %s", singleton.isAlwaysOnTop()));
					println("Options: -t, -f");
					break;
				}
			} catch (ParseException e) {
				println(e.getMessage());
				return false;
			}
			return true;
		}

		@Override
		public String[] getParamOptions(int number, String[] currArgs) {
			switch (number) {
			case 0:
				return new String[] { "--true", "--false" };
			default:
				return null;
			}
		}
	};

	private static ExternalConsoleCommand theme = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "theme";
		}

		@Override
		public String getDescription() {
			return "Changes Theme of console";
		}

		@Override
		public boolean executeCommand(String[] args) {
			Options options = new Options();
			OptionGroup theme = new OptionGroup();
			theme.addOption(new Option("t", "twitch", false, "Set EC's Theme to Twitch"));
			theme.addOption(new Option("d", "day", false, "Set EC's Theme to Day"));
			theme.addOption(new Option("n", "night", false, "Set EC's Theme to Night"));
			options.addOptionGroup(theme);
			try {
				CommandLineParser parser = new DefaultParser();
				parser.parse(options, args);
				switch (String.valueOf(theme.getSelected())) {
				case "t":
					setTheme(Constants.TWITCH_THEME);
					break;
				case "d":
					setTheme(Constants.DAY_THEME);
					break;
				case "n":
					setTheme(Constants.NIGHT_THEME);
					break;
				default:
					String current = "Unknown";
					if (singleton.currentTheme != null)
						current = singleton.currentTheme.getName();
					println(String.format("Theme is set as: %s", current));
					println("Options: -d, -n, -t");
					break;
				}
			} catch (ParseException e) {
				println(e.getMessage());
				return false;
			}
			return true;
		}

		@Override
		public String[] getParamOptions(int number, String[] currArgs) {
			switch (number) {
			case 0:
				return new String[] { "--day", "--night", "--twitch" };
			default:
				return null;
			}
		}

	};

	private static ExternalConsoleCommand apptheme = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "apptheme";
		}

		@Override
		public String getDescription() {
			return "Changes Theme of app";
		}

		@Override
		public boolean executeCommand(String[] args) {
			Options options = new Options();
			OptionGroup theme = new OptionGroup();
			theme.addOption(new Option("t", "twitch", false, "Set App's Theme to Twitch"));
			theme.addOption(new Option("d", "day", false, "Set App's Theme to Day"));
			theme.addOption(new Option("n", "night", false, "Set App's Theme to Night"));
			options.addOptionGroup(theme);
			try {
				CommandLineParser parser = new DefaultParser();
				parser.parse(options, args);
				switch (String.valueOf(theme.getSelected())) {
				case "t":
					DataManager.setTheme(Constants.TWITCH_THEME);
					break;
				case "d":
					DataManager.setTheme(Constants.DAY_THEME);
					break;
				case "n":
					DataManager.setTheme(Constants.NIGHT_THEME);
					break;
				default:
					String current = "Unknown";
					if (DataManager.getTheme() != null)
						current = DataManager.getTheme().getName();
					println(String.format("App Theme is set as: %s", current));
					println("Options: -d, -n, -t");
					break;
				}
			} catch (ParseException e) {
				println(e.getMessage());
				return false;
			}
			return true;
		}

		@Override
		public String[] getParamOptions(int number, String[] currArgs) {
			switch (number) {
			case 0:
				return new String[] { "--day", "--night", "--twitch" };
			default:
				return null;
			}
		}

	};

	private static ExternalConsoleCommand localmsg = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "localmsg";
		}

		@Override
		public String getDescription() {
			return "Send a local msg to ChatFrame/ExternalConsole";
		}

		@Override
		public boolean executeCommand(String[] args) {
			Options options = new Options();
			OptionGroup local = new OptionGroup();
			local.addOption(new Option("f", "chatframe", true, "Sends local message to ChatFrame"));
			local.addOption(new Option("e", "externalconsole", true, "Sends local message to ExternalConsole"));
			options.addOptionGroup(local);
			try {
				CommandLineParser parser = new DefaultParser();
				CommandLine cmd = parser.parse(options, args);
				switch (String.valueOf(local.getSelected())) {
				case "f":
					ChatFrame.getInstance().onMessage(String.format("cmd.%s", getCommand()), cmd.getOptionValue('f'));
					break;
				case "e":
					println(cmd.getOptionValue('e'));
					break;
				default:
					println(String.format("localmsg: %s", getDescription()));
					println("Options: -f, -e");
					break;
				}
			} catch (ParseException e) {
				println(e.getMessage());
				return false;
			}
			return true;
		}

		@Override
		public String[] getParamOptions(int number, String[] currArgs) {
			switch (number) {
			case 0:
				return new String[] { "--chatframe", "--externalconsole" };
			default:
				return null;
			}
		}

	};

	private static ExternalConsoleCommand stop = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "stop";
		}

		@Override
		public String getDescription() {
			return "Terminates ATPX App";
		}

		@Override
		public boolean executeCommand(String[] args) {
			singleton.dispose();
			MainFrame.getInstance().dispatchEvent(new WindowEvent(MainFrame.getInstance(), WindowEvent.WINDOW_CLOSING));
			return true;
		}
	};

	private static ExternalConsoleCommand readme = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "readme";
		}

		@Override
		public String getDescription() {
			return "Displays README of app";
		}

		@Override
		public boolean executeCommand(String[] args) {
			Constants.showREADME();
			return true;
		}
	};

	private static ExternalConsoleCommand update = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "update";
		}

		@Override
		public String getDescription() {
			return "Checks if there is an update available";
		}

		@Override
		public boolean executeCommand(String[] args) {
			GitHubReleaseJson update = Constants.getLatestRelease();
			if (update == null) {
				println("Could not check update.");
				return true;
			}
			StringBuilder builder = new StringBuilder(update.tag_name.replaceAll("[^\\d]", ""));
			builder.insert(0, 0);
			int gitVersion = Integer.parseInt(builder.toString());
			builder = new StringBuilder(App.VERSION.replaceAll("[^\\d]", ""));
			builder.insert(0, 0);
			int currentVersion = Integer.parseInt(builder.toString());
			if (gitVersion <= currentVersion)
				println(String.format("No Update Available. (Current Version: %s)", App.VERSION));
			else
				println(String.format("Update Available: %s", update.tag_name));
			return true;
		}
	};

	private static ExternalConsoleCommand changelog = new ExternalConsoleCommand() {

		private String[] tagsCache = null;

		@Override
		public String getCommand() {
			return "changelog";
		}

		@Override
		public String getDescription() {
			return "View Changelogs";
		}

		@Override
		public boolean executeCommand(String[] args) {
			GitHubReleaseJson release;
			if (args.length <= 0) {
				println("Showing latest");
				release = Constants.getLatestRelease();

			} else {
				println("Showing " + args[0]);
				release = Constants.getRelease(args[0]);
			}

			JPanel content = new JPanel(new BorderLayout());
			JScrollPane scroll = new JScrollPane();
			JTextPane message = new JTextPane();
			JTextField title = new JTextField();
			scroll = new JScrollPane();
			message = new JTextPane();

			title.setOpaque(false);
			title.setForeground(DataManager.getTheme().getFont());
			title.setText(String.format(DataManager.getLanguage().getUpdateNoticeTitleContent(), release.tag_name,
					release.update_name));
			title.setBorder(null);
			title.setFont(
					new Font(title.getFont().getFontName(), title.getFont().getStyle(), title.getFont().getSize() + 7));
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

			message.setText(release.body);

			Constants.showCustomColorMessageDialog(null, content, "Changelog: " + release.tag_name,
					JOptionPane.PLAIN_MESSAGE, null, DataManager.getTheme().getBackground());
			return true;
		}

		@Override
		public String[] getParamOptions(int number, String[] currArgs) {
			switch (number) {
			case 0:
				if (tagsCache == null)
					tagsCache = Constants.getAllReleases();
				return tagsCache;
			default:
				return null;
			}
		}

	};

	private static class UsedCommand {

		private UsedCommand previous;

		private UsedCommand next;

		private String fullCommand;

		private static final UsedCommand NULL_UC = new UsedCommand();

		private UsedCommand() {
			this.fullCommand = null;
			this.previous = this;
			this.next = this;
		}

		public UsedCommand(String fullCommand, UsedCommand previous, UsedCommand next) {
			this.fullCommand = fullCommand;
			this.previous = previous;
			this.next = next;
		}

		public String getFullCommand() {
			return fullCommand;
		}

		public UsedCommand getPrevious() {
			return previous;
		}

		/*
		 * public void setPrevious(UsedCommand previous) { this.previous = previous; }
		 */

		public UsedCommand getNext() {
			return next;
		}

		public void setNext(UsedCommand next) {
			this.next = next;
		}
	}

	private UsedCommand last;

	private static ExternalConsole singleton = new ExternalConsole();

	private ExternalConsole() {
		this.setTitle("External Console");
		this.currentTheme = Constants.NIGHT_THEME;
		this.setMinimumSize(new Dimension(300, 300));
		ImageIcon icon = new ImageIcon(Constants.ICON_PATH);
		this.setIconImage(icon.getImage());
		this.setLayout(new BorderLayout());
		this.add(insertConsole(), BorderLayout.CENTER);
		this.add(inputConsole(), BorderLayout.SOUTH);
		scroll.getParent().setBackground(null);
		this.out = new ExternalConsoleOutputStream();
		this.err = new ExternalConsoleErrorOutputStream();
		this.in = new ExternalConsoleInputStream();
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				if (currentTheme == Constants.NIGHT_THEME)
					setTheme(Constants.DAY_THEME);
				else
					setTheme(Constants.NIGHT_THEME);
			}
		});

		this.setAlwaysOnTop(true);
		this.autoScroll = true;
		this.cmds = new HashMap<>();

		this.cmds.put(help.getCommand(), help);
		this.cmds.put(autoscroll.getCommand(), autoscroll);
		this.cmds.put(verbose.getCommand(), verbose);
		this.cmds.put(printcommand.getCommand(), printcommand);
		this.cmds.put(eventlog.getCommand(), eventlog);
		this.cmds.put(debug.getCommand(), debug);
		this.cmds.put(disablesession.getCommand(), disablesession);
		this.cmds.put(clear.getCommand(), clear);
		this.cmds.put(appfolder.getCommand(), appfolder);
		this.cmds.put(mod.getCommand(), mod);
		this.cmds.put(hide.getCommand(), hide);
		this.cmds.put(top.getCommand(), top);
		this.cmds.put(theme.getCommand(), theme);
		this.cmds.put(stop.getCommand(), stop);
		this.cmds.put(localmsg.getCommand(), localmsg);
		this.cmds.put(readme.getCommand(), readme);
		this.cmds.put(update.getCommand(), update);
		this.cmds.put(apptheme.getCommand(), apptheme);
		this.cmds.put(changelog.getCommand(), changelog);
		
		this.last = UsedCommand.NULL_UC;

		this.console.setForeground(currentTheme.getFont());
		this.setBackground(currentTheme.getBackground());

		this.input.setBorder(BorderFactory.createLineBorder(currentTheme.getFont(), 1));
		this.input.setForeground(currentTheme.getFont());
		this.input.setCaretColor(currentTheme.getFont());
		this.input.setBackground(currentTheme.getBackground());

		EventManager.registerEventListener(this);
	}

	/*
	 * public static ExternalConsole getInstance() { return singleton; }
	 */

	public static boolean isViewable() {
		return singleton.isVisible();
	}

	public static void setViewable(boolean b) {
		singleton.setVisible(b);
	}

	public static boolean isClosable() {
		return singleton.getDefaultCloseOperation() >= JFrame.DISPOSE_ON_CLOSE;
	}

	public static void setClosable(boolean b) {
		if (b)
			singleton.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		else
			singleton.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	public static void addCommand(ExternalConsoleCommand newCmd) {
		singleton.cmds.put(newCmd.getCommand(), newCmd);
	}

	public static void removeCommand(String cmd) {
		singleton.cmds.remove(cmd);
	}

	public static ExternalConsoleCommand getCommand(String cmd) {
		return singleton.cmds.get(cmd);
	}

	public static boolean executeCommand(String cmd, String... args) {
		ExternalConsoleCommand exe = singleton.cmds.get(cmd);
		if (exe == null)
			return false;
		exe.executeCommand(args);
		return true;
	}

	private JScrollPane insertConsole() {
		scroll = new JScrollPane();
		console = new JTextPane();
		console.setEditorKit(new WrapEditorKit());
		// console.setBorder(null);
		// console.setTabSize(4);
		console.setEditable(false);
		// console.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret) console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scroll.setViewportView(console);
		scroll.setFocusable(false);
		scroll.setEnabled(false);
		scroll.setBorder(null);
		scroll.setWheelScrollingEnabled(true);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		console.setOpaque(false);
		// console.setForeground(Color.BLACK);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent event) {
				scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
			}
		});
		return scroll;
	}

	private JPanel inputConsole() {
		messagePanel = new JPanel(new BorderLayout());
		input = new JTextField();
		input.setBorder(null);

		// input.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		// input.setForeground(Color.BLACK);
		// input.setCaretColor(Color.BLACK);
		// input.setBackground(Color.WHITE);
		input.setFocusTraversalKeysEnabled(false);
		input.addKeyListener(new KeyListener() {

			private int tabPos = -1;

			private String ref = null;

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() != KeyEvent.VK_TAB && e.getKeyCode() != KeyEvent.VK_SHIFT) {
					tabPos = -1;
					ref = null;
				}
				switch (e.getKeyCode()) {
				default:
					break;
				case KeyEvent.VK_TAB:
					String[] args = input.getText().split(" ", -1);
					ExternalConsoleCommand cmd = cmds.get(args[0]);
					boolean next = !KeyPressedAdapter.isKeyPressed(KeyEvent.VK_SHIFT);
					if (ref == null) {
						ref = args[args.length - 1];
					}
					String[] currArgs;
					if (args.length > 2)
						currArgs = Arrays.copyOfRange(args, 1, args.length - 1);
					else
						currArgs = new String[] {};
					if (args.length == 0 || cmd == null || cmd.getParamOptions(args.length - 2, currArgs) == null) {
						List<ExternalConsoleCommand> options = cmds.values().stream()
								.filter(c -> c.getCommand().startsWith(ref)).sorted(ExternalConsoleCommand.comparator)
								.toList();
						if (next)
							tabPos = tabPos + 1 >= options.size() ? 0 : tabPos + 1;
						else
							tabPos = tabPos - 1 < 0 ? options.size() - 1 : tabPos - 1;
						if (tabPos < 0 || tabPos >= options.size())
							break;
						args[args.length - 1] = options.get(tabPos).getCommand();
						input.setText(String.join(" ", args));
					} else {
						String[] paramOptions = cmd.getParamOptions(args.length - 2, currArgs);
						List<String> options = Stream.of(paramOptions).filter(c -> c.startsWith(ref)).toList();
						if (next)
							tabPos = tabPos + 1 >= options.size() ? 0 : tabPos + 1;
						else
							tabPos = tabPos - 1 < 0 ? options.size() - 1 : tabPos - 1;
						if (tabPos < 0 || tabPos >= options.size())
							break;
						args[args.length - 1] = options.get(tabPos);
						input.setText(String.join(" ", args));
					}
					break;
				case KeyEvent.VK_UP:
				case KeyEvent.VK_KP_UP:
					if (last != UsedCommand.NULL_UC) {
						if (last.getPrevious() != UsedCommand.NULL_UC) {
							input.setText(last.getFullCommand());
							last = last.getPrevious();
						}
					} else {
						input.setText("");
					}
					break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_KP_DOWN:
					if (last != UsedCommand.NULL_UC && last.getNext() != UsedCommand.NULL_UC
							&& last.getNext().getNext() != UsedCommand.NULL_UC) {
						last = last.getNext();
						input.setText(last.getNext().getFullCommand());
					}
					break;
				case KeyEvent.VK_ENTER:
					InputCommandExternalConsoleEvent event = new InputCommandExternalConsoleEvent(
							input.getText().split(" "));
					/*
					 * EventManager.triggerEvent(event); if (event.isCancelled()) return;
					 */

					while (last.getNext() != UsedCommand.NULL_UC)
						last = last.getNext();
					if (last == UsedCommand.NULL_UC)
						last = new UsedCommand(null, UsedCommand.NULL_UC, UsedCommand.NULL_UC);
					UsedCommand current = new UsedCommand(input.getText(), last, UsedCommand.NULL_UC);
					last.setNext(current);
					last = current;

					// console.append(input.getText());
					// console.append("\n");
					println(input.getText());

					scroll.repaint();
					scroll.revalidate();
					in.contents = input.getText().getBytes();
					in.pointer = 0;
					input.setText("");
					// event.finishedEvent();
					EventManager.triggerEvent(event);
					break;
				}
			}
		});
		messagePanel.add(input, BorderLayout.CENTER);
		return messagePanel;

	}

	public static OutputStream getExternalConsoleOutputStream() {
		return singleton.out;
	}

	public static OutputStream getExternalConsoleErrorOutputStream() {
		return singleton.err;
	}

	public static InputStream getExternalConsoleInputStream() {
		return singleton.in;
	}

	private class ExternalConsoleOutputStream extends OutputStream {

		@Override
		public void write(int b) throws IOException {
			try {
				StyledDocument doc = singleton.console.getStyledDocument();
				doc.insertString(doc.getLength(), Character.toString(b), null);
				if (autoScroll)
					console.setCaretPosition(doc.getLength());
				singleton.scroll.repaint();
				singleton.scroll.revalidate();
			} catch (BadLocationException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// continue
			}
			/*
			 * console.append(Character.toString(b)); if (autoScroll) try {
			 * console.setCaretPosition(console.getLineStartOffset(console.getLineCount() -
			 * 1)); } catch (BadLocationException e) { } scroll.repaint();
			 * scroll.revalidate();
			 */
		}

	}

	private class ExternalConsoleErrorOutputStream extends OutputStream {

		private SimpleAttributeSet errorSet;

		public ExternalConsoleErrorOutputStream() {
			errorSet = new SimpleAttributeSet();
			StyleConstants.setForeground(errorSet, Color.RED);
		}

		@Override
		public void write(int b) throws IOException {
			try {
				StyledDocument doc = singleton.console.getStyledDocument();
				doc.insertString(doc.getLength(), Character.toString(b), errorSet);
				if (autoScroll)
					console.setCaretPosition(doc.getLength());
				singleton.scroll.repaint();
				singleton.scroll.revalidate();
			} catch (BadLocationException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// continue
			}
			/*
			 * console.append(Character.toString(b)); if (autoScroll) try {
			 * console.setCaretPosition(console.getLineStartOffset(console.getLineCount() -
			 * 1)); } catch (BadLocationException e) { } scroll.repaint();
			 * scroll.revalidate();
			 */
		}

	}

	public static void setTheme(ColorTheme theme) {
		singleton.currentTheme = theme;
		singleton.console.setForeground(theme.getFont());
		singleton.setBackground(theme.getBackground());

		singleton.input.setBorder(BorderFactory.createLineBorder(theme.getFont(), 1));
		singleton.input.setForeground(theme.getFont());
		singleton.input.setCaretColor(theme.getFont());
		singleton.input.setBackground(theme.getBackground());
	}

	public static void println() {
		try {
			StyledDocument doc = singleton.console.getStyledDocument();
			doc.insertString(doc.getLength(), "\n", null);
			if (singleton.autoScroll)
				singleton.console.setCaretPosition(doc.getLength());
			// singleton.console.append("\n");
			singleton.scroll.repaint();
			singleton.scroll.revalidate();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public static void println(String msg) {
		try {
			StyledDocument doc = singleton.console.getStyledDocument();
			doc.insertString(doc.getLength(), String.format("%s\n", msg), null);
			if (singleton.autoScroll)
				singleton.console.setCaretPosition(doc.getLength());
			// singleton.console.append(String.format("%s\n", msg));
			singleton.scroll.repaint();
			singleton.scroll.revalidate();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public static ColorTheme getTheme() {
		return singleton.currentTheme;
	}

	private class ExternalConsoleInputStream extends InputStream {

		private byte[] contents;
		private int pointer = 0;

		@Override
		public int read() throws IOException {
			if (pointer >= contents.length)
				return -1;
			return this.contents[pointer++];
		}

	}

	@Handler
	public void onCommand(InputCommandExternalConsoleEvent event) {
		if (event.getArgs() == null || event.getArgs().length == 0)
			return;
		// event.addAfterEvent(() -> {
		String[] args = Arrays.copyOfRange(event.getArgs(), 1, event.getArgs().length);
		ExternalConsoleCommand cmd = cmds.get(event.getArgs()[0]);
		if (cmd != null)
			cmd.executeCommand(args);
		// });

	}
}
