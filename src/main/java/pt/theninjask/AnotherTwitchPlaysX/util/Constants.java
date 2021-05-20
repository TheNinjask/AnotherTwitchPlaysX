package pt.theninjask.AnotherTwitchPlaysX.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import pt.theninjask.AnotherTwitchPlaysX.exception.ModNotLoadedException;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.Mod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ModPanel;

public final class Constants {

	private Constants() {
	}

	public static final URL ICON_PATH = Constants.class
			.getResource("/pt/theninjask/AnotherTwitchPlaysX/resource/image/favicon.png");

	public static final ImageIcon ICON = new ImageIcon(Constants.ICON_PATH);

	public static final String TITLE = "Another TwitchPlaysX";

	public static final String TWITCH_CHAT_OAUTH = "https://twitchapps.com/tmi/";

	public static final String TWITCH_CHAT_OAUTH_BUTTON = "Get OAuth";

	public static final String DEFAULT_ERROR_TITLE = "An error has occurred!";

	public static final String BROWSER_NOT_SUPPORTED = "Browsing is not supported!";

	public static final String LOGIN_BUTTON = "Let's Start!";

	public static final String TWITCH_FIELD = "Name: ";

	public static final String CHANNEL_FIELD = "Channel: ";

	public static final String OAUTH_FIELD = "OAuth: ";

	public static final String TWITCH_FIELD_TIP = "Twitch Username";

	public static final String CHANNEL_FIELD_TIP = "Twitch Channel Name";

	public static final String OAUTH_FIELD_TIP = "Token for authentication";

	public static final String SAVING_SESSION_MSG = "Session saved in %s!";

	public static final String CURRENT_CHAT_SIZE = "Current Twitch Chat size: %s messages";

	public static final String IS_TWITCH_CHAT_ON_TOP = "Set Twitch Chat Always On Top?";

	public static final int TITLE_SCREEN_ICON_WIDTH = 25;

	public static final int TITLE_SCREEN_ICON_HEIGHT = 25;

	public static final Dimension X_BUTTON = new Dimension(23, 23);

	// MAYBE RECOMENDED
	public static final Color TWITCH_COLOR = new Color(123, 50, 250);

	public static final Color TWITCH_COLOR_COMPLEMENT = new Color(0xe5e5e5);

	// JUST FOR ME :) BUT NOT RECOMENDED
	@Deprecated
	public static final Color BLUE_COLOR = new Color(0x123456);

	public static final String MOD_WARN = "You are loading a mod that was not made by the creator of this app nor verified by them!\nProceed at your own risk.";

	public static final String MOD_INFO = "You are loading a third party mod that was validated by the creator of this app!";

	public static final Map<String, Integer> STRINGTOKEYCODE = getStringToKeyCode();

	@Deprecated
	//This is here due to pondering the access 
	//to the STRINGTOKEYCODE not being well defined
	//as of right now
	//POST NOTE
	//This may be unnecessary if removal/ban of certain key(s)
	public static Integer getKeyCodeFromText(String code) {
		return STRINGTOKEYCODE.get(code);
	}
	
	public static Map<String, Integer> refreshStringToKeyCode(){
		STRINGTOKEYCODE.clear();
		try {
			for (Field elem : KeyEvent.class.getFields()) {
				if (elem.getName().contains("VK_"))
					STRINGTOKEYCODE.put(KeyEvent.getKeyText(elem.getInt(KeyEvent.class)), elem.getInt(KeyEvent.class));
			}
		} catch (Exception e) {
			showExceptionDialog(e);
		}
		return STRINGTOKEYCODE;
	}
	
	
	private static Map<String, Integer> getStringToKeyCode() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			for (Field elem : KeyEvent.class.getFields()) {
				if (elem.getName().contains("VK_"))
					map.put(KeyEvent.getKeyText(elem.getInt(KeyEvent.class)), elem.getInt(KeyEvent.class));
			}
		} catch (Exception e) {
			showExceptionDialog(e);
		}
		return map;
	}

	public static ModPanel loadMod(File modFile) throws Exception {
		ModPanel mod = null;
		try {
			switch (JarVerifier.getInstance().verifyJar(modFile)) {
			case MAIN:
				break;
			case THIRD_PARTY:
				JTextArea msg = new JTextArea(MOD_INFO);
				msg.setForeground(TWITCH_COLOR_COMPLEMENT);
				msg.setOpaque(false);
				showCustomColorMessageDialog(null, msg, "Mod Info", JOptionPane.INFORMATION_MESSAGE, null,
						TWITCH_COLOR);
				break;
			case UNKNOWN:
			default:
				msg = new JTextArea(MOD_WARN);
				msg.setForeground(TWITCH_COLOR_COMPLEMENT);
				msg.setOpaque(false);
				int resp = showCustomColorOptionDialog(null, msg, "Mod Warning", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE, null, null, null, TWITCH_COLOR);
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
					ModPanel.class.getClassLoader());

			while (e.hasMoreElements()) {
				JarEntry je = e.nextElement();
				if (je.isDirectory() || !je.getName().endsWith(".class")) {
					continue;
				}

				String className = je.getName().substring(0, je.getName().lastIndexOf("."));
				className = className.replace('/', '.');
				Class<?> c = cl.loadClass(className);
				if (ModPanel.class.isAssignableFrom(c)) {
					Mod annotation = c.getDeclaredAnnotation(Mod.class);
					Object tmp = c.getConstructor().newInstance();

					if (mod == null && annotation != null && annotation.main()) {
						mod = (ModPanel) tmp;
						break;
					}
				}

			}
		} catch (Exception | Error e) {
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

	public static final void showExceptionDialog(Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().getSimpleName(), JOptionPane.WARNING_MESSAGE);
	}

	public static final void showExpectedExceptionDialog(Exception e) {
		JLabel exception = new JLabel(e.getMessage());
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
			JOptionPane.showMessageDialog(null, Constants.BROWSER_NOT_SUPPORTED, Constants.DEFAULT_ERROR_TITLE,
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
