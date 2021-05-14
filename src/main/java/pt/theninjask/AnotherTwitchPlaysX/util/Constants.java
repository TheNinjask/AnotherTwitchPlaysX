package pt.theninjask.AnotherTwitchPlaysX.util;

import static java.awt.event.KeyEvent.VK_0;
import static java.awt.event.KeyEvent.VK_1;
import static java.awt.event.KeyEvent.VK_2;
import static java.awt.event.KeyEvent.VK_3;
import static java.awt.event.KeyEvent.VK_4;
import static java.awt.event.KeyEvent.VK_5;
import static java.awt.event.KeyEvent.VK_6;
import static java.awt.event.KeyEvent.VK_7;
import static java.awt.event.KeyEvent.VK_8;
import static java.awt.event.KeyEvent.VK_9;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_AMPERSAND;
import static java.awt.event.KeyEvent.VK_ASTERISK;
import static java.awt.event.KeyEvent.VK_AT;
import static java.awt.event.KeyEvent.VK_B;
import static java.awt.event.KeyEvent.VK_BACK_QUOTE;
import static java.awt.event.KeyEvent.VK_BACK_SLASH;
import static java.awt.event.KeyEvent.VK_C;
import static java.awt.event.KeyEvent.VK_CIRCUMFLEX;
import static java.awt.event.KeyEvent.VK_CLOSE_BRACKET;
import static java.awt.event.KeyEvent.VK_COLON;
import static java.awt.event.KeyEvent.VK_COMMA;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DOLLAR;
import static java.awt.event.KeyEvent.VK_E;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_EQUALS;
import static java.awt.event.KeyEvent.VK_EXCLAMATION_MARK;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_G;
import static java.awt.event.KeyEvent.VK_H;
import static java.awt.event.KeyEvent.VK_I;
import static java.awt.event.KeyEvent.VK_J;
import static java.awt.event.KeyEvent.VK_K;
import static java.awt.event.KeyEvent.VK_L;
import static java.awt.event.KeyEvent.VK_LEFT_PARENTHESIS;
import static java.awt.event.KeyEvent.VK_M;
import static java.awt.event.KeyEvent.VK_MINUS;
import static java.awt.event.KeyEvent.VK_N;
import static java.awt.event.KeyEvent.VK_NUMBER_SIGN;
import static java.awt.event.KeyEvent.VK_O;
import static java.awt.event.KeyEvent.VK_OPEN_BRACKET;
import static java.awt.event.KeyEvent.VK_P;
import static java.awt.event.KeyEvent.VK_PERIOD;
import static java.awt.event.KeyEvent.VK_PLUS;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_QUOTE;
import static java.awt.event.KeyEvent.VK_QUOTEDBL;
import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_RIGHT_PARENTHESIS;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_SEMICOLON;
import static java.awt.event.KeyEvent.VK_SLASH;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_T;
import static java.awt.event.KeyEvent.VK_TAB;
import static java.awt.event.KeyEvent.VK_U;
import static java.awt.event.KeyEvent.VK_UNDERSCORE;
import static java.awt.event.KeyEvent.VK_V;
import static java.awt.event.KeyEvent.VK_W;
import static java.awt.event.KeyEvent.VK_X;
import static java.awt.event.KeyEvent.VK_Y;
import static java.awt.event.KeyEvent.VK_Z;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class Constants {

	private Constants() {}

	public static final URL ICON_PATH = Constants.class.getResource("/pt/theninjask/AnotherTwitchPlaysX/resource/image/favicon.png");
	
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
	
	//MAYBE RECOMENDED
	public static final Color TWITCH_COLOR = new Color(123, 50, 250);
	
	public static final Color TWITCH_COLOR_COMPLEMENT = new Color(0xe5e5e5);
	
	//JUST FOR ME :) BUT NOT RECOMENDED
	public static final Color BLUE_COLOR = new Color(0x123456);
	
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
	
	
	public static final void showCustomColorMessageDialog(Component parentComponent, 
			Object message, 
			String title, 
			int messageType, 
			Icon icon, 
			Color color) {
		Object paneBG = UIManager.get("OptionPane.background");
	    Object panelBG = UIManager.get("Panel.background");
	    UIManager.put("OptionPane.background", color);
	    UIManager.put("Panel.background", color);

	    JOptionPane.showMessageDialog(parentComponent, message, title, messageType, icon);

	    UIManager.put("OptionPane.background", paneBG);
	    UIManager.put("Panel.background", panelBG);
	}
	
	public static final int showCustomColorOptionDialog(Component parentComponent, 
			Object message, 
			String title, 
			int optionType, 
			int messageType, 
			Icon icon, 
			Object[] options, 
			Object initialValue,
			Color color) {
		Object paneBG = UIManager.get("OptionPane.background");
	    Object panelBG = UIManager.get("Panel.background");
	    UIManager.put("OptionPane.background", color);
	    UIManager.put("Panel.background", color);

	    int value = JOptionPane.showOptionDialog(parentComponent, message, title, optionType, messageType, icon, options, initialValue);

	    UIManager.put("OptionPane.background", paneBG);
	    UIManager.put("Panel.background", panelBG);
	    return value;
	}

	
	public static final void showExceptionDialog(Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().getName(), JOptionPane.WARNING_MESSAGE);
	}
	
	public static final void showExpectedExceptionDialog(Exception e) {
		JLabel exception = new JLabel(e.getMessage());
		exception.setForeground(TWITCH_COLOR_COMPLEMENT);
		Object paneBG = UIManager.get("OptionPane.background");
	    Object panelBG = UIManager.get("Panel.background");
	    UIManager.put("OptionPane.background", TWITCH_COLOR);
	    UIManager.put("Panel.background", TWITCH_COLOR);
		JOptionPane.showMessageDialog(null, exception, e.getClass().getName(), JOptionPane.WARNING_MESSAGE, null);
		UIManager.put("OptionPane.background", paneBG);
	    UIManager.put("Panel.background", panelBG);
	}
	
	public static final void openWebsite(String website){
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
		    try {
				Desktop.getDesktop().browse(new URI(website));
			} catch (IOException | URISyntaxException e1) {
				showExceptionDialog(e1);
			}
		}else {
			JOptionPane.showMessageDialog(null, Constants.BROWSER_NOT_SUPPORTED, Constants.DEFAULT_ERROR_TITLE, JOptionPane.INFORMATION_MESSAGE);
		}
	}

	@Deprecated
	public static final int getKeyCodeOrDefault(String key, int defaultValue) {
		Integer value = getKeyCode(key);
		if(value==null)
			return defaultValue;
		return value;
	}
	
	@Deprecated
	/**
	 * Avoid using this function at least I dont like it the way it is now
	 * Maybe in the future have a "learn" capability like a area where the user
	 * presses keys and show the code and string associated
	 * and it is saved as a map<string,keycode> for the vars
	 */
	public static final Integer getKeyCode(String key) {
		// adapted from sauce: https://stackoverflow.com/a/1248709
		if(key==null || key.length()!=1 )
			return null;
		switch (key.charAt(0)) {
        	case 'a': return VK_A;
        	case 'b': return VK_B;
        	case 'c': return VK_C;
        	case 'd': return VK_D;
        	case 'e': return VK_E;
        	case 'f': return VK_F;
        	case 'g': return VK_G;
        	case 'h': return VK_H;
        	case 'i': return VK_I;
        	case 'j': return VK_J;
        	case 'k': return VK_K;
        	case 'l': return VK_L;
        	case 'm': return VK_M;
        	case 'n': return VK_N;
        	case 'o': return VK_O;
        	case 'p': return VK_P;
        	case 'q': return VK_Q;
        	case 'r': return VK_R;
        	case 's': return VK_S;
        	case 't': return VK_T;
        	case 'u': return VK_U;
        	case 'v': return VK_V;
        	case 'w': return VK_W;
        	case 'x': return VK_X;
        	case 'y': return VK_Y;
        	case 'z': return VK_Z;
        	case 'A': return VK_A; //VK_SHIFT, VK_A;
        	case 'B': return VK_B; //VK_SHIFT, VK_B;
        	case 'C': return VK_C; //VK_SHIFT, VK_C;
        	case 'D': return VK_D; //VK_SHIFT, VK_D;
        	case 'E': return VK_E; //VK_SHIFT, VK_E;
        	case 'F': return VK_F; //VK_SHIFT, VK_F;
        	case 'G': return VK_G; //VK_SHIFT, VK_G;
        	case 'H': return VK_H; //VK_SHIFT, VK_H;
        	case 'I': return VK_I; //VK_SHIFT, VK_I;
        	case 'J': return VK_J; //VK_SHIFT, VK_J;
        	case 'K': return VK_K; //VK_SHIFT, VK_K;
        	case 'L': return VK_L; //VK_SHIFT, VK_L;
        	case 'M': return VK_M; //VK_SHIFT, VK_M;
        	case 'N': return VK_N; //VK_SHIFT, VK_N;
        	case 'O': return VK_O; //VK_SHIFT, VK_O;
        	case 'P': return VK_P; //VK_SHIFT, VK_P;
        	case 'Q': return VK_Q; //VK_SHIFT, VK_Q;
        	case 'R': return VK_R; //VK_SHIFT, VK_R;
        	case 'S': return VK_S; //VK_SHIFT, VK_S;
        	case 'T': return VK_T; //VK_SHIFT, VK_T;
        	case 'U': return VK_U; //VK_SHIFT, VK_U;
        	case 'V': return VK_V; //VK_SHIFT, VK_V;
        	case 'W': return VK_W; //VK_SHIFT, VK_W;
        	case 'X': return VK_X; //VK_SHIFT, VK_X;
        	case 'Y': return VK_Y; //VK_SHIFT, VK_Y;
        	case 'Z': return VK_Z; //VK_SHIFT, VK_Z;
        	case '`': return VK_BACK_QUOTE;
        	case '0': return VK_0;
        	case '1': return VK_1;
        	case '2': return VK_2;
        	case '3': return VK_3;
        	case '4': return VK_4;
        	case '5': return VK_5;
        	case '6': return VK_6;
        	case '7': return VK_7;
        	case '8': return VK_8;
        	case '9': return VK_9;
        	case '-': return VK_MINUS;
        	case '=': return VK_EQUALS;
        	//case '~': return VK_SHIFT, VK_BACK_QUOTE;
        	case '!': return VK_EXCLAMATION_MARK;
        	case '@': return VK_AT;
        	case '#': return VK_NUMBER_SIGN;
        	case '$': return VK_DOLLAR;
        	//case '%': return VK_SHIFT, VK_5;
        	case '^': return VK_CIRCUMFLEX;
        	case '&': return VK_AMPERSAND;
        	case '*': return VK_ASTERISK;
        	case '(': return VK_LEFT_PARENTHESIS;
        	case ')': return VK_RIGHT_PARENTHESIS;
        	case '_': return VK_UNDERSCORE;
        	case '+': return VK_PLUS;
        	case '\t': return VK_TAB;
        	case '\n': return VK_ENTER;
        	case '[': return VK_OPEN_BRACKET;
        	case ']': return VK_CLOSE_BRACKET;
        	case '\\': return VK_BACK_SLASH;
        	//case '{': return VK_SHIFT, VK_OPEN_BRACKET;
        	//case '}': return VK_SHIFT, VK_CLOSE_BRACKET;
        	//case '|': return VK_SHIFT, VK_BACK_SLASH;
        	case ';': return VK_SEMICOLON;
        	case ':': return VK_COLON;
        	case '\'': return VK_QUOTE;
        	case '"': return VK_QUOTEDBL;
        	case ',': return VK_COMMA;
        	//case '<': return VK_SHIFT, VK_COMMA;
        	case '.': return VK_PERIOD;
        	//case '>': return VK_SHIFT, VK_PERIOD;
        	case '/': return VK_SLASH;
        	//case '?': return VK_SHIFT, VK_SLASH;
        	case ' ': return VK_SPACE;
        	default:
        		return null;
        }
	}
}
