package pt.theninjask.AnotherTwitchPlaysX.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jnativehook.keyboard.NativeKeyEvent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.bus.config.Feature;
import net.engio.mbassy.bus.config.IBusConfiguration;
import net.engio.mbassy.bus.error.IPublicationErrorHandler;
import net.engio.mbassy.bus.error.PublicationError;
import pt.theninjask.AnotherTwitchPlaysX.data.ControlType;
import pt.theninjask.AnotherTwitchPlaysX.exception.ModNotLoadedException;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;

public final class Constants {

	private Constants() {
	}

	public static final String SAVE_PATH = Paths.get(System.getProperty("user.home"), ".ATPX").toString();

	public static final String MOD_FOLDER = "mods";

	public static final String CMD_FOLDER = "cmds";

	public static final String CONFIG_FILE = "config.json";

	public static final String TWITCH_FILE = "twitchSession.json";

	public static final String YOUTUBE_FILE = "youtubeSession.json";

	public static final URL ICON_PATH = Constants.class
			.getResource("/pt/theninjask/AnotherTwitchPlaysX/resource/image/favicon.png");

	public static final URL TWITCH_LOGO_PATH = Constants.class
			.getResource("/pt/theninjask/AnotherTwitchPlaysX/resource/logo/twitch.png");

	public static final URL YOUTUBE_LOGO_PATH = Constants.class
			.getResource("/pt/theninjask/AnotherTwitchPlaysX/resource/logo/youtube.png");

	public static final ImageIcon ICON = new ImageIcon(Constants.ICON_PATH);

	public static final String TWITCH_CHAT_OAUTH = "https://twitchapps.com/tmi/";

	public static final String YOUTUBE_CHAT_SECRET = "https://console.cloud.google.com/apis/credentials";

	public static final int TITLE_SCREEN_ICON_WIDTH = 25;

	public static final int TITLE_SCREEN_ICON_HEIGHT = 25;

	public static final Dimension X_BUTTON = new Dimension(23, 23);

	// MAYBE RECOMENDED
	public static final Color TWITCH_COLOR = new Color(123, 50, 250);

	public static final Color TWITCH_COLOR_COMPLEMENT = new Color(0xe5e5e5);

	// JUST FOR ME :) BUT NOT RECOMENDED
	@Deprecated
	public static final Color BLUE_COLOR = new Color(0x123456);

	public static int stopKey = NativeKeyEvent.VC_ESCAPE;

	public static final Map<String, Pair<Integer, ControlType>> STRING_TO_KEYCODE = getStringToKeyCode();

	public static boolean debug = false;

	public static final PrintStream VOID_STREAM = new PrintStream(new OutputStream() {
		@Override
		public void write(int b) throws IOException {
		}
	});

	public static final String LATEST_VERSION_URL = "https://api.github.com/repos/TheNinjask/AnotherTwitchPlaysX/releases/latest";

	public static final String README_URL = "https://api.github.com/repos/TheNinjask/AnotherTwitchPlaysX/readme";

	private static final SimpleFormatter LOGGER_FORMATTER = new SimpleFormatter() {
		// private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";
		private static final String format = "[%1$.4s] %2$s %n";

		@Override
		public synchronized String format(LogRecord lr) {
			return String.format(format, lr.getLevel().getLocalizedName(), lr.getMessage());
		}
	};

	private static final Logger LOGGER = setUpLogger();

	private static final Logger setUpLogger() {

		class DualConsoleHandler extends StreamHandler {

			private final ConsoleHandler stderrHandler = new ConsoleHandler();

			public DualConsoleHandler(Formatter format) {
				super(System.out, format);
				stderrHandler.setFormatter(format);
			}

			@Override
			public void publish(LogRecord record) {
				if (record.getLevel().intValue() <= Level.INFO.intValue()) {
					super.publish(record);
					super.flush();
				} else {
					stderrHandler.publish(record);
					stderrHandler.flush();
				}
			}
		}

		Logger logger = Logger.getGlobal();
		logger.setUseParentHandlers(false);
		StreamHandler handler = new DualConsoleHandler(LOGGER_FORMATTER);
		logger.addHandler(handler);
		logger.setLevel(Level.OFF);
		return logger;
	}

	public static void setLoggerLevel(Level level) {
		LOGGER.setLevel(level);
	}

	public static Level getLoggerLevel() {
		return LOGGER.getLevel();
	}

	public static void printVerboseMessage(Level level, Throwable message) {
		LOGGER.log(level, message.getClass().getSimpleName());
		if (debug)
			message.printStackTrace();
	}

	public static void printVerboseMessage(Level level, String message) {
		LOGGER.log(level, message);
	}

	public static Map<String, Pair<Integer, ControlType>> resetStringToKeyCode() {
		STRING_TO_KEYCODE.clear();
		try {
			for (Field elem : KeyEvent.class.getFields()) {
				if (elem.getName().contains("VK_")) {
					if (KeyEvent.getKeyText(elem.getInt(KeyEvent.class))
							.equalsIgnoreCase(NativeKeyEvent.getKeyText(stopKey)))
						continue;
					// if (elem.getName().equals("VK_ESCAPE"))
					// continue;
					STRING_TO_KEYCODE.put(KeyEvent.getKeyText(elem.getInt(KeyEvent.class)).toLowerCase(),
							new Pair<Integer, ControlType>(elem.getInt(KeyEvent.class), ControlType.KEY));
				}
			}
			STRING_TO_KEYCODE.put("Button Left".toLowerCase(),
					new Pair<Integer, ControlType>(MouseEvent.BUTTON1_DOWN_MASK, ControlType.MOUSE));
			STRING_TO_KEYCODE.put("Button Right".toLowerCase(),
					new Pair<Integer, ControlType>(MouseEvent.BUTTON3_DOWN_MASK, ControlType.MOUSE));
			STRING_TO_KEYCODE.put("Button Middle".toLowerCase(),
					new Pair<Integer, ControlType>(MouseEvent.BUTTON2_DOWN_MASK, ControlType.MOUSE));
		} catch (Exception e) {
			showExceptionDialog(e);
		}
		return STRING_TO_KEYCODE;
	}

	private static Map<String, Pair<Integer, ControlType>> getStringToKeyCode() {
		Map<String, Pair<Integer, ControlType>> map = new HashMap<String, Pair<Integer, ControlType>>();
		try {
			for (Field elem : KeyEvent.class.getFields()) {
				if (elem.getName().contains("VK_")) {
					if (KeyEvent.getKeyText(elem.getInt(KeyEvent.class))
							.equalsIgnoreCase(NativeKeyEvent.getKeyText(stopKey)))
						continue;
					// if (elem.getName().equals("VK_ESCAPE"))
					// continue;
					map.put(KeyEvent.getKeyText(elem.getInt(KeyEvent.class)).toLowerCase(),
							new Pair<Integer, ControlType>(elem.getInt(KeyEvent.class), ControlType.KEY));
				}
			}
			map.put("Button Left".toLowerCase(),
					new Pair<Integer, ControlType>(MouseEvent.BUTTON1_DOWN_MASK, ControlType.MOUSE));
			map.put("Button Right".toLowerCase(),
					new Pair<Integer, ControlType>(MouseEvent.BUTTON3_DOWN_MASK, ControlType.MOUSE));
			map.put("Button Middle".toLowerCase(),
					new Pair<Integer, ControlType>(MouseEvent.BUTTON2_DOWN_MASK, ControlType.MOUSE));
		} catch (Exception e) {
			showExceptionDialog(e);
		}
		return map;
	}

	public static ATPXMod loadMod(File modFile) throws Exception {
		ATPXMod mod = null;
		if (modFile == null)
			return null;
		try {
			switch (JarVerifier.getInstance().verifyJar(modFile)) {
			case MAIN:
				break;
			case THIRD_PARTY:
				JTextArea msg = new JTextArea(DataManager.getLanguage().getConstants().getModInfo());
				msg.setForeground(TWITCH_COLOR_COMPLEMENT);
				msg.setOpaque(false);
				showCustomColorMessageDialog(null, msg,
						String.format(DataManager.getLanguage().getConstants().getModInfoTitle(), modFile.getName()),
						JOptionPane.INFORMATION_MESSAGE, null, TWITCH_COLOR);
				break;
			case UNKNOWN:
			default:
				msg = new JTextArea(DataManager.getLanguage().getConstants().getModWarn());
				msg.setForeground(TWITCH_COLOR_COMPLEMENT);
				msg.setOpaque(false);
				int resp = showCustomColorOptionDialog(null, msg,
						String.format(DataManager.getLanguage().getConstants().getModWarnTitle(), modFile.getName()),
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null, TWITCH_COLOR);
				switch (resp) {
				case JOptionPane.OK_OPTION:
					break;
				case JOptionPane.CLOSED_OPTION:
				case JOptionPane.CANCEL_OPTION:
				default:
					return null;
				}
				break;
			}
			;
			JarFile jarFile = new JarFile(modFile.getAbsolutePath());
			Enumeration<JarEntry> e = jarFile.entries();

			URLClassLoader cl = URLClassLoader.newInstance(new URL[] { modFile.toURI().toURL() },
					ATPXMod.class.getClassLoader());

			while (e.hasMoreElements()) {
				JarEntry je = e.nextElement();
				if (je.isDirectory() || !je.getName().endsWith(".class")) {
					continue;
				}

				String className = je.getName().substring(0, je.getName().lastIndexOf("."));
				ClassParser cp = new ClassParser(jarFile.getInputStream(je), className);
				JavaClass jc = cp.parse();
				int access_flags = jc.getAccessFlags();
				final int ACC_MODULE = 0x8000;
				final int ACC_PUBLIC = 0x0001;
				if ((access_flags & ACC_MODULE) != 0)
					continue;
				if ((access_flags & ACC_PUBLIC) == 0)
					continue;

				className = className.replace('/', '.');
				Class<?> c = cl.loadClass(className);
				if (ATPXMod.class.isAssignableFrom(c)) {
					ATPXModProps annotation = c.getAnnotation(ATPXModProps.class);
					if (mod == null && annotation != null && annotation.main()) {
						mod = (ATPXMod) c.getConstructor().newInstance();
						break;
					}
				}

			}
		} catch (Exception | Error e) {

			Constants.printVerboseMessage(Level.WARNING, e);

			throw new ModNotLoadedException();
		}
		if (mod == null)
			throw new ModNotLoadedException();
		return mod;
	}

	public static File showSaveFile(File defaultFile, FileNameExtensionFilter filter, Component comp) {
		JFileChooser chooser = null;
		int resp = JFileChooser.ERROR_OPTION;
		try {
			LookAndFeel previousLF = UIManager.getLookAndFeel();
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			chooser = new JFileChooser();
			chooser.setSelectedFile(defaultFile);
			chooser.addChoosableFileFilter(filter);
			chooser.setFileFilter(filter);
			UIManager.setLookAndFeel(previousLF);
		} catch (Exception e) {

			Constants.printVerboseMessage(Level.WARNING, e);

			chooser = new JFileChooser();
			chooser.setSelectedFile(defaultFile);
			chooser.addChoosableFileFilter(filter);
			chooser.setFileFilter(filter);
		}
		resp = chooser.showSaveDialog(comp);
		switch (resp) {
		case JFileChooser.APPROVE_OPTION:
			return chooser.getSelectedFile();
		case JFileChooser.CANCEL_OPTION:
		case JFileChooser.ERROR_OPTION:
		default:
			return null;
		}
	}

	public static File showSaveFile(File defaultFile, FileNameExtensionFilter filter, Component comp, File path) {
		JFileChooser chooser = null;
		int resp = JFileChooser.ERROR_OPTION;
		try {
			LookAndFeel previousLF = UIManager.getLookAndFeel();
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			chooser = new JFileChooser(path);
			chooser.setSelectedFile(defaultFile);
			chooser.addChoosableFileFilter(filter);
			chooser.setFileFilter(filter);
			UIManager.setLookAndFeel(previousLF);
		} catch (Exception e) {

			Constants.printVerboseMessage(Level.WARNING, e);

			chooser = new JFileChooser(path);
			chooser.setSelectedFile(defaultFile);
			chooser.addChoosableFileFilter(filter);
			chooser.setFileFilter(filter);
		}
		resp = chooser.showSaveDialog(comp);
		switch (resp) {
		case JFileChooser.APPROVE_OPTION:
			return chooser.getSelectedFile();
		case JFileChooser.CANCEL_OPTION:
		case JFileChooser.ERROR_OPTION:
		default:
			return null;
		}
	}

	public static File showOpenFile(FileNameExtensionFilter filter, Component comp) {
		JFileChooser chooser = null;
		int resp = JFileChooser.ERROR_OPTION;
		try {
			LookAndFeel previousLF = UIManager.getLookAndFeel();
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			chooser = new JFileChooser();
			chooser.addChoosableFileFilter(filter);
			chooser.setFileFilter(filter);
			UIManager.setLookAndFeel(previousLF);
		} catch (Exception e) {

			Constants.printVerboseMessage(Level.WARNING, e);

			chooser = new JFileChooser();
			chooser.addChoosableFileFilter(filter);
			chooser.setFileFilter(filter);
		}
		resp = chooser.showOpenDialog(comp);
		switch (resp) {
		case JFileChooser.APPROVE_OPTION:
			return chooser.getSelectedFile();
		case JFileChooser.CANCEL_OPTION:
		case JFileChooser.ERROR_OPTION:
		default:
			return null;
		}
	}

	public static File showOpenFile(FileNameExtensionFilter filter, Component comp, File path) {
		JFileChooser chooser = null;
		int resp = JFileChooser.ERROR_OPTION;
		try {
			LookAndFeel previousLF = UIManager.getLookAndFeel();
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			chooser = new JFileChooser(path);
			chooser.addChoosableFileFilter(filter);
			chooser.setFileFilter(filter);
			UIManager.setLookAndFeel(previousLF);
		} catch (Exception e) {

			Constants.printVerboseMessage(Level.WARNING, e);

			chooser = new JFileChooser(path);
			chooser.addChoosableFileFilter(filter);
			chooser.setFileFilter(filter);
		}
		resp = chooser.showOpenDialog(comp);
		switch (resp) {
		case JFileChooser.APPROVE_OPTION:
			return chooser.getSelectedFile();
		case JFileChooser.CANCEL_OPTION:
		case JFileChooser.ERROR_OPTION:
		default:
			return null;
		}
	}

	public static final void showCustomColorMessageDialog(Component parentComponent, Object message, String title,
			int messageType, Icon icon, Color color) {
		Object paneBG = UIManager.get("OptionPane.background");
		Object panelBG = UIManager.get("Panel.background");
		UIManager.put("OptionPane.background", color);
		UIManager.put("Panel.background", color);

		JOptionPane.showMessageDialog(parentComponent, message, title, messageType, icon);

		UIManager.put("OptionPane.background", paneBG);
		UIManager.put("Panel.background", panelBG);
	}

	public static final int showCustomColorOptionDialog(Component parentComponent, Object message, String title,
			int optionType, int messageType, Icon icon, Object[] options, Object initialValue, Color color) {
		Object paneBG = UIManager.get("OptionPane.background");
		Object panelBG = UIManager.get("Panel.background");
		UIManager.put("OptionPane.background", color);
		UIManager.put("Panel.background", color);

		int value = JOptionPane.showOptionDialog(parentComponent, message, title, optionType, messageType, icon,
				options, initialValue);
		UIManager.put("OptionPane.background", paneBG);
		UIManager.put("Panel.background", panelBG);
		return value;
	}

	public static final void showMessageDialog(String msg, String... title) {
		JTextArea message = new JTextArea(msg);
		message.setOpaque(false);
		message.setEditable(false);
		message.setForeground(TWITCH_COLOR_COMPLEMENT);
		Object paneBG = UIManager.get("OptionPane.background");
		Object panelBG = UIManager.get("Panel.background");
		UIManager.put("OptionPane.background", TWITCH_COLOR);
		UIManager.put("Panel.background", TWITCH_COLOR);
		JOptionPane.showMessageDialog(null, message, title.length > 0 ? title[0] : "", JOptionPane.PLAIN_MESSAGE, null);
		UIManager.put("OptionPane.background", paneBG);
		UIManager.put("Panel.background", panelBG);
	}

	public static final void showExceptionDialog(Throwable e) {

		Constants.printVerboseMessage(Level.SEVERE, e);

		JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().getSimpleName(), JOptionPane.WARNING_MESSAGE);
	}

	public static final void showExpectedExceptionDialog(Throwable e) {

		Constants.printVerboseMessage(Level.WARNING, e);

		JTextArea exception = new JTextArea(e.getMessage());
		exception.setOpaque(false);
		exception.setEditable(false);
		exception.setForeground(TWITCH_COLOR_COMPLEMENT);
		Object paneBG = UIManager.get("OptionPane.background");
		Object panelBG = UIManager.get("Panel.background");
		UIManager.put("OptionPane.background", TWITCH_COLOR);
		UIManager.put("Panel.background", TWITCH_COLOR);
		JOptionPane.showMessageDialog(null, exception, e.getClass().getSimpleName(), JOptionPane.WARNING_MESSAGE, null);
		UIManager.put("OptionPane.background", paneBG);
		UIManager.put("Panel.background", panelBG);
	}

	public static final void openWebsite(String website) {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			try {
				Desktop.getDesktop().browse(new URI(website));
			} catch (IOException | URISyntaxException e1) {
				showExceptionDialog(e1);
			}
		} else {
			JOptionPane.showMessageDialog(null, DataManager.getLanguage().getConstants().getBrowserNotSupported(),
					DataManager.getLanguage().getConstants().getDefaultErrorTitle(), JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public static MBassador<Object> setupDispatcher() {
		IBusConfiguration config = new BusConfiguration().addPublicationErrorHandler(new IPublicationErrorHandler() {
			@Override
			public void handleError(PublicationError error) {
			}
		}).addFeature(Feature.SyncPubSub.Default()).addFeature(Feature.AsynchronousHandlerInvocation.Default())
				.addFeature(Feature.AsynchronousMessageDispatch.Default());
		return new MBassador<Object>(config);
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(Include.NON_NULL)
	public static class GitHubAPIError {
		public String message;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(Include.NON_NULL)
	public static class GitHubLatestJson {
		public String html_url;
		public String tag_name;
		@JsonProperty("name")
		public String update_name;
		public List<Asset> assets;
		public String body;

		@JsonIgnoreProperties(ignoreUnknown = true)
		@JsonInclude(Include.NON_NULL)
		public static class Asset {
			public String browser_download_url;
		}

	}

	public static GitHubLatestJson getLatestRelease() {
		try {
			Client client = ClientBuilder.newClient();
			return client.target(LATEST_VERSION_URL).request(MediaType.APPLICATION_JSON).get(GitHubLatestJson.class);
		} catch (WebApplicationException e) {
			printVerboseMessage(Level.WARNING, e);
			/*
			 * String response = "Unknown"; if (e.getResponse().hasEntity()) { try {
			 * response = new String(((InputStream)
			 * e.getResponse().getEntity()).readAllBytes()); response = new
			 * ObjectMapper().readValue(response, GitHubAPIError.class).message; } catch
			 * (IOException e1) { printVerboseMessage(Level.WARNING, e); } }
			 */
			// printVerboseMessage(Level.WARNING, String.format("%s - %s",
			// e.getResponse().getStatus(), response));
			return null;
		} catch (Exception e) {
			printVerboseMessage(Level.WARNING, e);
			return null;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(Include.NON_NULL)
	public static class GitHubLatestREADME {
		public String html_url;
		public String encoding;
		public String content;
	}

	public static void showREADME() {
		try {
			Client client = ClientBuilder.newClient();
			GitHubLatestREADME readme = client.target(README_URL).request(MediaType.APPLICATION_JSON)
					.get(GitHubLatestREADME.class);

			JPanel content = new JPanel(new BorderLayout());
			JScrollPane scroll = new JScrollPane();
			JEditorPane message = new JEditorPane();
			scroll = new JScrollPane();
			message = new JTextPane();

			// message.setEditorKit(new WrapEditorKit());
			message.setEditable(false);
			scroll.setViewportView(message);
			scroll.setFocusable(false);
			scroll.setEnabled(false);
			scroll.setBorder(null);
			scroll.setWheelScrollingEnabled(true);
			scroll.setOpaque(false);
			scroll.getViewport().setOpaque(false);

			message.setOpaque(false);
			message.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);

			scroll.setPreferredSize(new Dimension(151, 151));
			content.add(scroll, BorderLayout.CENTER);
			content.setOpaque(false);

			switch (readme.encoding) {
			case "base64":
				readme.content = new String(Base64.getMimeDecoder().decode(readme.content));
				break;
			default:
				throw new RuntimeException("No encoding option for " + readme.encoding);
			}

			Parser parser = Parser.builder().build();
			Node node = parser.parse(readme.content);
			HtmlRenderer renderer = HtmlRenderer.builder().build();
			readme.content = renderer.render(node);

			message.setContentType(MediaType.TEXT_HTML);

			// Fixes to links
			readme.content = readme.content.replaceAll("\"./login.png\"",
					"\"https://raw.githubusercontent.com/TheNinjask/AnotherTwitchPlaysX/master/login.png\"");
			readme.content = readme.content.replaceAll("\"./menu.png\"",
					"\"https://raw.githubusercontent.com/TheNinjask/AnotherTwitchPlaysX/master/menu.png\"");

			Pattern pattern = Pattern.compile("<h2>(.*)<\\/h2>");
			Matcher matcher = pattern.matcher(readme.content);
			matcher.find();
			for (MatchResult elem : matcher.results().toList()) {
				readme.content = readme.content.replaceAll(Pattern.quote(elem.group()),
						String.format("<h2 id=#%s>%s<\\/h2>",
								elem.group(1).replaceAll("\\.", "").replaceAll(" ", "-").toLowerCase(), elem.group(1)));
			}

			message.setText(readme.content);
			PopOutFrame readmeFrame = new PopOutFrame(content);

			message.addHyperlinkListener(new HyperlinkListener() {
				@Override
				public void hyperlinkUpdate(HyperlinkEvent e) {
					if (e.getEventType() == EventType.ACTIVATED)
						if (e.getURL() != null) {
							openWebsite(e.getURL().toString());
							readmeFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						} else {
							JEditorPane message = (JEditorPane) e.getInputEvent().getComponent();
							Pattern pattern = Pattern.compile(String.format("id=\"%s\"", e.getDescription()));
							Matcher matcher = pattern.matcher(message.getText());
							if (matcher.find()) {
								message.setCaretPosition((message.getDocument().getLength())
										* matcher.start() / message.getText().length());
							}
							readmeFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						}

					if (e.getEventType() == EventType.ENTERED) {
						readmeFrame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					} else if (e.getEventType() == EventType.EXITED) {
						readmeFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				}
			});
			content.setBackground(Constants.TWITCH_COLOR);

			readmeFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			message.setCaretPosition(0);
			readmeFrame.setVisible(true);
			// Constants.showCustomColorMessageDialog(null, content, "README",
			// JOptionPane.PLAIN_MESSAGE, null,
			// Constants.TWITCH_COLOR);

		} catch (

		WebApplicationException e) {
			printVerboseMessage(Level.WARNING, e);
			String response = DataManager.getLanguage().getConstants().getREADMEUnknown();
			if (e.getResponse().hasEntity()) {
				try {
					response = new String(((InputStream) e.getResponse().getEntity()).readAllBytes());
					response = new ObjectMapper().readValue(response, GitHubAPIError.class).message;
				} catch (IOException e1) {
					printVerboseMessage(Level.WARNING, e);
				}
			}
			showMessageDialog(String.format(DataManager.getLanguage().getConstants().getREADMENetException(), response),
					String.format(DataManager.getLanguage().getConstants().getREADMENetExceptionTitle(), e.getResponse().getStatus()));
		} catch (Exception e) {
			printVerboseMessage(Level.WARNING, e);
			showMessageDialog(DataManager.getLanguage().getConstants().getREADMEException(), DataManager.getLanguage().getConstants().getREADMEExceptionTitle());
		}
	}

}
