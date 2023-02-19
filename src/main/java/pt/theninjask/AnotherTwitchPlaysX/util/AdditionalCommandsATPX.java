package pt.theninjask.AnotherTwitchPlaysX.util;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import pt.theninjask.AnotherTwitchPlaysX.App;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.ChatFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModManager;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
import pt.theninjask.AnotherTwitchPlaysX.lan.custom.CustomLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.en.EnglishLang;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants.GitHubReleaseJson;
import pt.theninjask.externalconsole.console.ExternalConsole;
import pt.theninjask.externalconsole.console.ExternalConsoleCommand;

public class AdditionalCommandsATPX {

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
		public int executeCommand(String... args) {
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
					ExternalConsole.println(String.format("verbose is set as: %s", Constants.getLoggerLevel()));
					new HelpFormatter().printHelp("verbose", options, true);
					break;
				}
			} catch (ParseException e) {
				ExternalConsole.println(e.getMessage());
				return 1;
			}
			return 0;
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

		@Override
		public boolean accessibleInCode() {
			return true;
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
		public int executeCommand(String... args) {
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
					ExternalConsole.println(
							String.format("Print Commands Execution is set as: %s", CommandData.isEnableLogging()));
					new HelpFormatter().printHelp("printcommand", options, true);
					break;
				}
			} catch (ParseException e) {
				ExternalConsole.println(e.getMessage());
				return 1;
			}
			return 0;
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

		@Override
		public boolean accessibleInCode() {
			return true;
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
		public int executeCommand(String... args) {
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
					ExternalConsole.println(
							String.format("Print Event Trigger is set as: %s", EventManager.isEnableLogging()));
					new HelpFormatter().printHelp("eventlog", options, true);
					break;
				}

			} catch (ParseException e) {
				ExternalConsole.println(e.getMessage());
				return 1;
			}
			return 0;
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

		@Override
		public boolean accessibleInCode() {
			return true;
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
		public int executeCommand(String... args) {
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
					ExternalConsole.println(String.format("Debug is set as: %s", Constants.debug));
					new HelpFormatter().printHelp("debug", options, true);
					break;
				}
			} catch (ParseException e) {
				ExternalConsole.println(e.getMessage());
				return 1;
			}
			return 0;
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

		@Override
		public boolean accessibleInCode() {
			return true;
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
		public int executeCommand(String... args) {
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
					ExternalConsole.println(String.format("DisableSession is set as: %s", DataManager.disableSession));
					new HelpFormatter().printHelp("disablesession", options, true);
					break;
				}
			} catch (ParseException e) {
				ExternalConsole.println(e.getMessage());
				return 1;
			}
			return 0;
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
		public int executeCommand(String... args) {
			try {
				Desktop.getDesktop().open(new File(Constants.SAVE_PATH));
			} catch (IOException e) {
				ExternalConsole.println(e.getMessage());
				return 1;
			}
			return 0;
		}

		@Override
		public boolean accessibleInCode() {
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
		public int executeCommand(String... args) {
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
							return 1;
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
						ExternalConsole.println(String.format("Loaded Mod %s.", newMod.getClass().getSimpleName()));
					} catch (Exception e) {
						ExternalConsole.println(e.getMessage());
						return 0;
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
						ExternalConsole.println("No mod found!");
					} else {
						System.gc();
						ExternalConsole.println("Mod removed.");
					}
					break;
				case "c":
					MainMenuPanel.getInstance().setMod(null);
					ATPXModManager.removeAllMods();
					System.gc();
					ExternalConsole.println("Mods cleared!");
					break;
				case "l":
					ArrayList<ATPXMod> list = ATPXModManager.getAllMods();
					if (list.isEmpty()) {
						ExternalConsole.println("No mods loaded.");
						break;
					}
					ExternalConsole.println("All mods loaded:");
					for (ATPXMod atpxMod : list) {
						ExternalConsole.println(String.format("\t%s", atpxMod.getClass().getSimpleName()));
					}
					break;
				default:
					new HelpFormatter().printHelp("mod", options, true);
					break;
				}
			} catch (ParseException e) {
				ExternalConsole.println(e.getMessage());
				return 1;
			}
			return 0;
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
		public int executeCommand(String... args) {
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
						current = DataManager.getTheme().name();
					ExternalConsole.println(String.format("App Theme is set as: %s", current));
					new HelpFormatter().printHelp("apptheme", options, true);
					break;
				}
			} catch (ParseException e) {
				ExternalConsole.println(e.getMessage());
				return 1;
			}
			return 0;
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

		@Override
		public boolean accessibleInCode() {
			return true;
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
		public int executeCommand(String... args) {
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
					ExternalConsole.println(cmd.getOptionValue('e'));
					break;
				default:
					ExternalConsole.println(String.format("localmsg: %s", getDescription()));
					new HelpFormatter().printHelp("localmsg", options, true);
					break;
				}
			} catch (ParseException e) {
				ExternalConsole.println(e.getMessage());
				return 1;
			}
			return 0;
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

		@Override
		public boolean accessibleInCode() {
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
		public int executeCommand(String... args) {
			Constants.showREADME();
			return 0;
		}

		@Override
		public boolean accessibleInCode() {
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
		public int executeCommand(String... args) {
			GitHubReleaseJson update = Constants.getLatestRelease();
			if (update == null) {
				ExternalConsole.println("Could not check update.");
				return -1;
			}
			String regex = "v?(\\d+).(\\d+).(\\d+)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(update.tag_name);
			int gitVersionMajor = 0;
			int gitVersionMinor = 0;
			int gitVersionPatch = 0;
			if (matcher.matches()) {
				gitVersionMajor = Integer.parseInt(matcher.group(1));
				gitVersionMinor = Integer.parseInt(matcher.group(2));
				gitVersionPatch = Integer.parseInt(matcher.group(3));
			}
			matcher = pattern.matcher(App.VERSION);
			int currVersionMajor = 0;
			int currVersionMinor = 0;
			int currVersionPatch = 0;
			if (matcher.matches()) {
				currVersionMajor = Integer.parseInt(matcher.group(1));
				currVersionMinor = Integer.parseInt(matcher.group(2));
				currVersionPatch = Integer.parseInt(matcher.group(3));
			}
			if (currVersionMajor > gitVersionMajor
					|| (currVersionMajor == gitVersionMajor && currVersionMinor > gitVersionMinor)
					|| (currVersionMajor == gitVersionMajor && currVersionMinor == gitVersionMinor
							&& currVersionPatch >= gitVersionPatch)) {
				ExternalConsole.println(String.format("No Update Available. (Current Version: %s)", App.VERSION));
				return 0;
			}

			JPanel content = new JPanel(new BorderLayout());
			JScrollPane scroll = new JScrollPane();
			JTextPane message = new JTextPane();
			JTextField title = new JTextField();
			scroll = new JScrollPane();
			message = new JTextPane();

			title.setOpaque(false);
			title.setForeground(DataManager.getTheme().font());
			title.setText(String.format(DataManager.getLanguage().getUpdateNoticeTitleContent(), update.tag_name,
					update.update_name));
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
			message.setForeground(DataManager.getTheme().font());

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
					JOptionPane.PLAIN_MESSAGE, null, options, null, DataManager.getTheme().background());
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
			return 1;
			/*
			 * switch (result) { case 0: ExternalConsole.println(String.
			 * format("No Update Available. (Current Version: %s)", App.VERSION)); return 0;
			 * case 1: ExternalConsole.println("Update Available. Showing Update"); return
			 * 1; default: ExternalConsole.println("Could not check update."); return -1; }
			 */
		}

		@Override
		public boolean accessibleInCode() {
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
		public int executeCommand(String... args) {
			GitHubReleaseJson release;
			if (args.length <= 0) {
				ExternalConsole.println("Showing latest");
				release = Constants.getLatestRelease();

			} else {
				ExternalConsole.println("Showing " + args[0]);
				release = Constants.getRelease(args[0]);
			}

			JPanel content = new JPanel(new BorderLayout());
			JScrollPane scroll = new JScrollPane();
			JTextPane message = new JTextPane();
			JTextField title = new JTextField();
			scroll = new JScrollPane();
			message = new JTextPane();

			title.setOpaque(false);
			title.setForeground(DataManager.getTheme().font());
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
			message.setForeground(DataManager.getTheme().font());

			scroll.setPreferredSize(new Dimension(151, 151));
			content.add(scroll, BorderLayout.CENTER);
			content.add(title, BorderLayout.NORTH);
			content.setOpaque(false);

			message.setText(release.body);

			Constants.showCustomColorMessageDialog(null, content, "Changelog: " + release.tag_name,
					JOptionPane.PLAIN_MESSAGE, null, DataManager.getTheme().background());
			return 0;
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

		@Override
		public boolean accessibleInCode() {
			return true;
		}
	};

	private static ExternalConsoleCommand lang = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "lang";
		}

		@Override
		public String getDescription() {
			return "Operations on language";
		}

		@Override
		public int executeCommand(String... args) {
			Options options = new Options();
			OptionGroup mod = new OptionGroup();
			// print.setRequired(true);
			mod.addOption(new Option("l", "load", false, "Load a language from a file"));
			mod.addOption(new Option("d", "default", false, "Sets to the default language (English)"));
			mod.addOption(new Option("e", "extract", false, "Extracts a sample of lang json"));
			options.addOptionGroup(mod);
			try {
				CommandLineParser parser = new DefaultParser();
				parser.parse(options, args);
				switch (String.valueOf(mod.getSelected())) {
				case "l":
					try {
						File file = Constants.showOpenFile(new FileNameExtensionFilter("JSON", "json"), null,
								Paths.get(Constants.SAVE_PATH).toFile());
						JsonReader json = new JsonReader(new FileReader(file));
						CustomLang lang = new CustomLang(JsonParser.parseReader(json).getAsJsonObject());
						DataManager.setLanguage(lang);
						ExternalConsole.println("Set lang as: " + lang.getLanTag());
					} catch (Exception e) {
						ExternalConsole.println(e.getMessage());
						return 1;
					}
					break;
				case "d":
					DataManager.setLanguage(new EnglishLang());
					ExternalConsole.println("Set lang to default");
					break;
				case "e":
					try {
						File file = Constants.showSaveFile(new File("lang.json"),
								new FileNameExtensionFilter("JSON", "json"), null,
								Paths.get(Constants.SAVE_PATH).toFile());
						try (FileWriter writer = new FileWriter(file)) {
							new GsonBuilder().setPrettyPrinting().serializeNulls().create()
									.toJson(new CustomLang(new JsonObject()).toJson(), writer);
						}
					} catch (JsonIOException | IOException e) {
						e.printStackTrace();
					}
					ExternalConsole.println("Extracted lang json");
					break;
				default:
					ExternalConsole.println("Current: " + DataManager.getLanguage().getLanTag());
					new HelpFormatter().printHelp("lang", options, true);
					break;
				}
			} catch (ParseException e) {
				ExternalConsole.println(e.getMessage());
				return 1;
			}
			return 0;
		}

		@Override
		public String[] getParamOptions(int number, String[] currArgs) {
			switch (number) {
			case 0:
				return new String[] { "--load", "--default", "--extract" };
			default:
				return null;
			}
		}

		@Override
		public boolean accessibleInCode() {
			return true;
		}
	};

	private static ExternalConsoleCommand dummy = new ExternalConsoleCommand() {

		private AtomicBoolean on = new AtomicBoolean(false);

		@Override
		public String getCommand() {
			return "dummy";
		}

		@Override
		public String getDescription() {
			return "Dummy Command for testing";
		}

		@Override
		public int executeCommand(String... args) {
			try {
				if (on.get()) {
					on.set(false);
				} else {
					on.set(true);
					new Thread(() -> {
						try {
							Robot robot = new SmoothMoveRobot();
							Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
							int width = (int) dim.getWidth();
							int height = (int) dim.getHeight();
							int x = ThreadLocalRandom.current().nextInt(0, width);
							int y = ThreadLocalRandom.current().nextInt(0, height);
							int xSpeed = 100;
							int ySpeed = 100;
							while (on.get()) {
								robot.mouseMove(x,y);
								x += xSpeed;
								y += ySpeed;
								x = Math.min(x, width);
								x = Math.max(x, 0);
								y = Math.min(y, height);
								y = Math.max(y, 0);
								if(x == width || x==0)
									xSpeed *= -1;
								if(y == height || y==0)
									ySpeed *= -1;
								//Thread.sleep(151);								
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}).start();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
			return 0;
		}
	};

	private AdditionalCommandsATPX() {
	}

	public static void addCommands() {
		for (ExternalConsoleCommand cmd : AdditionalCommandsATPX.getCommands()) {
			ExternalConsole.addCommand(cmd);
		}
	}

	private static ExternalConsoleCommand[] getCommands() {
		return new ExternalConsoleCommand[] { verbose, printcommand, eventlog, debug, disablesession, appfolder, mod,
				apptheme, localmsg, readme, update, changelog, lang, dummy };
	}

}
