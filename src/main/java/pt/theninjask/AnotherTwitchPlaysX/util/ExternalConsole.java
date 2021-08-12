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

	private static ExternalConsole singleton = new ExternalConsole();

	private JScrollPane scroll;
	private JTextArea console;
	private ExternalConsoleOutputStream out;
	private ExternalConsoleErrorOutputStream err;
	private ExternalConsoleInputStream in;

	private JPanel messagePanel;

	private JTextField input;

	private boolean autoScroll;

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
					singleton.setDay();
				else
					singleton.setNight();
			}
		});
		this.setAlwaysOnTop(true);
		this.autoScroll = true;
		EventManager.registerEventListener(this);
	}

	public static ExternalConsole getInstance() {
		return singleton;
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

	public OutputStream getExternalConsoleOutputStream() {
		return out;
	}

	public OutputStream getExternalConsoleErrorOutputStream() {
		return err;
	}

	public InputStream getExternalConsoleInputStream() {
		return in;
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

	public void setDay() {
		console.setForeground(Color.BLACK);
		this.setBackground(Color.WHITE);

		input.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		input.setForeground(Color.BLACK);
		input.setCaretColor(Color.BLACK);
		input.setBackground(Color.WHITE);
	}

	public void setNight() {
		console.setForeground(Color.WHITE);
		this.setBackground(Color.BLACK);

		input.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		input.setForeground(Color.WHITE);
		input.setCaretColor(Color.WHITE);
		input.setBackground(Color.BLACK);
	}

	public void println() {
		console.append("\n");
		scroll.repaint();
		scroll.revalidate();
	}

	public void println(String msg) {
		console.append(String.format("%s\n", msg));
		scroll.repaint();
		scroll.revalidate();
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
			switch (event.getArgs()[0]) {
			case "help":
				onCommandHelp(args);
				break;
			case "autoscroll":
				onCommandAutoscroll(args);
				break;
			case "verbose":
				onCommandVerbose(args);
				break;
			case "printcommand":
				onCommandPrintCommand(args);
				break;
			case "eventlog":
				onCommandEventLog(args);
				break;
			case "debug":
				onCommandDebug(args);
				break;
			case "disablesession":
				onCommandDisableSession(args);
				break;
			default:
				return;
			}
		});

	}

	private void onCommandHelp(String[] args) {
		println("Available Commands:");
		println("\tautoscroll");
		println("\tverbose");
		println("\tprintcommand");
		println("\teventlog");
		println("\tdebug");
		println("\tdisablesession");
	}

	private void onCommandAutoscroll(String[] args) {
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
				this.autoScroll = true;
				break;
			case "m":
				this.autoScroll = false;
				break;
			default:
				println(String.format("autoscroll is set as: %s", this.autoScroll));
				println("Options: -a, -m");
				break;
			}

		} catch (ParseException e) {
			Constants.showExpectedExceptionDialog(e);
		}
	}

	private void onCommandVerbose(String[] args) {
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

	private void onCommandPrintCommand(String[] args) {
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

	private void onCommandEventLog(String[] args) {
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

	private void onCommandDebug(String[] args) {
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

	private void onCommandDisableSession(String[] args) {
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

}
