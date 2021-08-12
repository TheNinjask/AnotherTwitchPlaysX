package pt.theninjask.AnotherTwitchPlaysX.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

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
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;

public class ExternalConsole extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JScrollPane scroll;
	private JTextArea console;
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
		public void executeCommand(String[] args) {
			println("Available Commands:");
			for (String cmd : singleton.cmds.keySet()) {
				println(String.format("\t%s", cmd));
			}
		}
	};

	private static ExternalConsoleCommand autoscroll = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "autoscroll";
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
				Constants.showExpectedExceptionDialog(e);
			}
		}
	};

	private static ExternalConsoleCommand verbose = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "verbose";
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
				Constants.showExpectedExceptionDialog(e);
			}
		}
	};

	private static ExternalConsoleCommand printcommand = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "printcommand";
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
				Constants.showExpectedExceptionDialog(e);
			}
		}
	};

	private static ExternalConsoleCommand eventlog = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "eventlog";
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
				Constants.showExpectedExceptionDialog(e);
			}
		}
	};

	private static ExternalConsoleCommand debug = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "debug";
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
				Constants.showExpectedExceptionDialog(e);
			}
		}
	};

	private static ExternalConsoleCommand disablesession = new ExternalConsoleCommand() {

		@Override
		public String getCommand() {
			return "disablesession";
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
				Constants.showExpectedExceptionDialog(e);
			}
		}
	};

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

		EventManager.registerEventListener(this);
	}

	/*public static ExternalConsole getInstance() {
		return singleton;
	}*/
	
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
		if(b)
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

	private JScrollPane insertConsole() {
		scroll = new JScrollPane();
		console = new JTextArea();
		console.setEditable(false);
		// console.setFocusable(false);
		console.setLineWrap(true);
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
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					InputCommandExternalConsoleEvent event = new InputCommandExternalConsoleEvent(
							input.getText().split(" "));
					EventManager.triggerEvent(event);
					if (event.isCancelled())
						return;

					console.append(input.getText());
					console.append("\n");
					scroll.repaint();
					scroll.revalidate();
					in.contents = input.getText().getBytes();
					in.pointer = 0;
					input.setText("");
					event.finishedEvent();
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
			console.append(Character.toString(b));
			if (autoScroll)
				try {
					console.setCaretPosition(console.getLineStartOffset(console.getLineCount() - 1));
				} catch (BadLocationException e) {
				}
			scroll.repaint();
			scroll.revalidate();
		}

	}

	private class ExternalConsoleErrorOutputStream extends OutputStream {

		@Override
		public void write(int b) throws IOException {
			console.append(Character.toString(b));
			if (autoScroll)
				try {
					console.setCaretPosition(console.getLineStartOffset(console.getLineCount() - 1));
				} catch (BadLocationException e) {
				}
			scroll.repaint();
			scroll.revalidate();
		}

	}

	public static void setDay() {
		singleton.console.setForeground(Color.BLACK);
		singleton.setBackground(Color.WHITE);

		singleton.input.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		singleton.input.setForeground(Color.BLACK);
		singleton.input.setCaretColor(Color.BLACK);
		singleton.input.setBackground(Color.WHITE);
	}

	public static void setNight() {
		singleton.console.setForeground(Color.WHITE);
		singleton.setBackground(Color.BLACK);

		singleton.input.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		singleton.input.setForeground(Color.WHITE);
		singleton.input.setCaretColor(Color.WHITE);
		singleton.input.setBackground(Color.BLACK);
	}

	public static void println() {
		singleton.console.append("\n");
		singleton.scroll.repaint();
		singleton.scroll.revalidate();
	}

	public static void println(String msg) {
		singleton.console.append(String.format("%s\n", msg));
		singleton.scroll.repaint();
		singleton.scroll.revalidate();
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
		event.addAfterEvent(() -> {
			String[] args = Arrays.copyOfRange(event.getArgs(), 1, event.getArgs().length);
			ExternalConsoleCommand cmd = cmds.get(event.getArgs()[0]);
			if (cmd != null)
				cmd.executeCommand(args);
		});

	}
}
