package pt.theninjask.AnotherTwitchPlaysX.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
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
import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.event.externalconsole.InputCommandExternalConsoleEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModManager;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;

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
		public void executeCommand(String[] args) {
			println("Available Commands:");
			List<ExternalConsoleCommand> helpSorted = singleton.cmds.values().stream()
					.sorted(new Comparator<ExternalConsoleCommand>() {
						@Override
						public int compare(ExternalConsoleCommand o1, ExternalConsoleCommand o2) {
							return o1.getCommand().compareTo(o2.getCommand());
						}
					}).toList();
			for (ExternalConsoleCommand cmd : helpSorted) {
				// int spacing = 4 + cmd.getCommand().length();
				println(String.format("\t%s - %s", cmd.getCommand(),
						// cmd.getDescription().replaceAll("\n", "\n\t" + " ".repeat(spacing))));
						cmd.getDescription().replaceAll("\n", "\n\t\t")));
			}
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
		public void executeCommand(String[] args) {
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
		public void executeCommand(String[] args) {
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
		public void executeCommand(String[] args) {
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
		public void executeCommand(String[] args) {
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
		public void executeCommand(String[] args) {
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
		public void executeCommand(String[] args) {
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
				// Constants.showExpectedExceptionDialog(e);
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
		public void executeCommand(String[] args) {
			singleton.console.setText("");
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
		public void executeCommand(String[] args) {
			try {
				Desktop.getDesktop().open(new File(Constants.SAVE_PATH));
			} catch (IOException e) {
				println(e.getMessage());
			}
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
		public void executeCommand(String[] args) {
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
							return;
						newMod.refresh();
						if (newMod.getClass().getAnnotation(ATPXModProps.class).hasPanel())
							if (newMod.getClass().getAnnotation(ATPXModProps.class).popout())
								new PopOutFrame(newMod.getJPanelInstance());
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
		public void executeCommand(String[] args) {
			singleton.setExtendedState(JFrame.ICONIFIED);
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
		public void executeCommand(String[] args) {
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
		public void executeCommand(String[] args) {
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
					setCustom(Constants.TWITCH_COLOR_COMPLEMENT, Constants.TWITCH_COLOR);
					break;
				case "d":
					setDay();
					break;
				case "n":
					setNight();
					break;
				default:
					Color c = singleton.getBackground();
					String current = "Unknown";
					if (c.equals(Color.BLACK))
						current = "Night";
					else if (c.equals(Color.WHITE))
						current = "Day";
					else if (c.equals(Constants.TWITCH_COLOR))
						current = "Twitch";
					println(String.format("Theme is set as: %s", current));
					println("Options: -d, -n, -t");
					break;
				}
			} catch (ParseException e) {
				println(e.getMessage());
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
		public void executeCommand(String[] args) {
			singleton.dispose();
			MainFrame.getInstance().dispatchEvent(new WindowEvent(MainFrame.getInstance(), WindowEvent.WINDOW_CLOSING));
		}
	};

	private class UsedCommand {

		private UsedCommand previous;

		private UsedCommand next;

		private String fullCommand;

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
		this.setMinimumSize(new Dimension(300, 300));
		ImageIcon icon = new ImageIcon(Constants.ICON_PATH);
		this.setIconImage(icon.getImage());
		this.setLayout(new BorderLayout());
		this.add(insertConsole(), BorderLayout.CENTER);
		this.add(inputConsole(), BorderLayout.SOUTH);
		scroll.getParent().setBackground(null);
		this.setBackground(Color.WHITE);
		this.out = new ExternalConsoleOutputStream();
		this.err = new ExternalConsoleErrorOutputStream();
		this.in = new ExternalConsoleInputStream();
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				if (singleton.getBackground() == Color.BLACK)
					setDay();
				else
					setNight();
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
		this.last = null;
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
		// console.setBorder(null);
		// console.setTabSize(4);
		console.setEditable(false);
		// console.setFocusable(false);
		// console.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret) console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scroll.setViewportView(console);
		scroll.setFocusable(false);
		scroll.setEnabled(false);
		scroll.setBorder(null);
		scroll.setWheelScrollingEnabled(true);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);

		console.setOpaque(false);
		console.setForeground(Color.BLACK);
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

		input.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		input.setForeground(Color.BLACK);
		input.setCaretColor(Color.BLACK);
		input.setBackground(Color.WHITE);

		input.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				default:
					break;
				case KeyEvent.VK_UP:
				case KeyEvent.VK_KP_UP:
					if (last != null) {
						input.setText(last.getFullCommand());
						if (last.getPrevious() != null)
							last = last.getPrevious();
					}
					break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_KP_DOWN:
					if (last != null)
						if (last.getNext() != null)
							if (last.getNext().getNext() != null) {
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

					if (last != null)
						while (last.getNext() != null)
							last = last.getNext();
					UsedCommand current = new UsedCommand(input.getText(), last, null);
					if (last != null)
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
				doc.insertString(doc.getLength(), Character.toString(b), null);
				doc.setCharacterAttributes(doc.getLength() - Character.toString(b).length(),
						Character.toString(b).length(), errorSet, false);
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

	public static void setCustom(Color font, Color background) {
		singleton.console.setForeground(font);
		singleton.setBackground(background);

		singleton.input.setBorder(BorderFactory.createLineBorder(font, 1));
		singleton.input.setForeground(font);
		singleton.input.setCaretColor(font);
		singleton.input.setBackground(background);
	}

	public static void setDay() {
		setCustom(Color.BLACK, Color.WHITE);
		/*
		 * singleton.console.setForeground(Color.BLACK);
		 * singleton.setBackground(Color.WHITE);
		 * 
		 * singleton.input.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		 * singleton.input.setForeground(Color.BLACK);
		 * singleton.input.setCaretColor(Color.BLACK);
		 * singleton.input.setBackground(Color.WHITE);
		 */
	}

	public static void setNight() {
		setCustom(Color.WHITE, Color.BLACK);
		/*
		 * singleton.console.setForeground(Color.WHITE);
		 * singleton.setBackground(Color.BLACK);
		 * 
		 * singleton.input.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		 * singleton.input.setForeground(Color.WHITE);
		 * singleton.input.setCaretColor(Color.WHITE);
		 * singleton.input.setBackground(Color.BLACK);
		 */
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
