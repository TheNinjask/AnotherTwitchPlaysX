package pt.theninjask.AnotherTwitchPlaysX.util;

import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JOptionPane;

public final class Constants {

	private Constants() {}

	public static final URL ICON_PATH = Constants.class.getResource("/pt/theninjask/AnotherTwitchPlaysX/resource/image/favicon.png");
	
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
	
	public static final int TITLE_SCREEN_ICON_WIDTH = 25;
	
	public static final int TITLE_SCREEN_ICON_HEIGHT = 25;
	
	//MAYBE RECOMENDED
	public static final Color TWITCH_COLOR = new Color(123, 50, 250);
	
	public static final Color TWITCH_COLOR_COMPLEMENT = new Color(0xe5e5e5);
	
	//JUST FOR ME :) BUT NOT RECOMENDED
	public static final Color BLUE_COLOR = new Color(0x123456);
	
	public static final void showExceptionDialog(Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().getName(), JOptionPane.WARNING_MESSAGE);
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
	
}
