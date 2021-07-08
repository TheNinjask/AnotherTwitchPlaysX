package pt.theninjask.AnotherTwitchPlaysX.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.theninjask.AnotherTwitchPlaysX.data.TwitchSessionData;
import pt.theninjask.AnotherTwitchPlaysX.data.YouTubeSessionData;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.MainLoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.TwitchLoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.YoutubeLoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.stream.twitch.TwitchPlayer;
import pt.theninjask.AnotherTwitchPlaysX.stream.youtube.YouTubePlayer;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class MainFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static MainFrame singleton = new MainFrame();
	
	private JPanel currentPanel = MainLoginPanel.getInstance();
	
	private MainFrame() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", MainFrame.class.getSimpleName()));
		this.onStart();
		this.getContentPane().setBackground(Constants.TWITCH_COLOR);
		this.setTitle(DataManager.getLanguage().getTitle());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(300, 300));
		ImageIcon icon = new ImageIcon(Constants.ICON_PATH);
		this.setIconImage(icon.getImage());
		this.add(currentPanel);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		this.setResizable(false);
		//DataManager.registerLangEvent(this);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				if(TwitchLoginPanel.getInstance().rememberSession())
					saveTwitchSession();
				if(YoutubeLoginPanel.getInstance().rememberSession())
					saveYouTubeSession();
				if(TwitchPlayer.getInstance().isConnected())
					TwitchPlayer.getInstance().disconnect();
				
				if(YouTubePlayer.getInstance().isConnected())
					YouTubePlayer.getInstance().disconnect();
			}
		});
	}
	
	private void saveTwitchSession() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.saveTwitchSession()", MainFrame.class.getSimpleName()));
		if(currentPanel!=TwitchLoginPanel.getInstance()) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				File file = new File(Constants.SAVE_PATH,"twitchSession.json");
				JTextField tmp = new JTextField(String.format(DataManager.getLanguage().getSavingSession(), file.getAbsolutePath()));
				tmp.setEditable(false);
				tmp.setBorder(null);
				tmp.setOpaque(false);
				tmp.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				objectMapper.writeValue(file, DataManager.getTwitchSession());
				Constants.showCustomColorMessageDialog(null, 
						tmp, 
						"Saving Session", JOptionPane.INFORMATION_MESSAGE, null, Constants.TWITCH_COLOR);			
			} catch (IOException e) {
				Constants.showExceptionDialog(e);
			}	
		}
	}
	
	private void saveYouTubeSession() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.saveYouTubeSession()", MainFrame.class.getSimpleName()));
		if(currentPanel!=YoutubeLoginPanel.getInstance()) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				File file = new File(Constants.SAVE_PATH,"youtubeSession.json");
				JTextField tmp = new JTextField(String.format(DataManager.getLanguage().getSavingSession(), file.getAbsolutePath()));
				tmp.setEditable(false);
				tmp.setBorder(null);
				tmp.setOpaque(false);
				tmp.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				objectMapper.writeValue(file, DataManager.getYouTubeSession());
				Constants.showCustomColorMessageDialog(null, 
						tmp, 
						"Saving Session", JOptionPane.INFORMATION_MESSAGE, null, Constants.TWITCH_COLOR);			
			} catch (IOException e) {
				Constants.showExceptionDialog(e);
			}	
		}
	}
	
	public static MainFrame getInstance() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.getInstance()", MainFrame.class.getSimpleName()));
		return singleton;
	}

	private void onStart() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.onStart()", MainFrame.class.getSimpleName()));
		
		File twitchSessionFile = new File(Constants.SAVE_PATH,"twitchSession.json");
		if(twitchSessionFile.exists())
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				TwitchSessionData session = objectMapper.readValue(twitchSessionFile, TwitchSessionData.class);
				TwitchLoginPanel.getInstance().setSession(session);
			} catch (IOException e) {
				Constants.showExceptionDialog(e);
			}
		
		File youtubeSessionFile = new File(Constants.SAVE_PATH,"youtubeSession.json");
		if(youtubeSessionFile.exists())
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				YouTubeSessionData session = objectMapper.readValue(youtubeSessionFile, YouTubeSessionData.class);
				YoutubeLoginPanel.getInstance().setSession(session);
			} catch (IOException e) {
				Constants.showExceptionDialog(e);
			}
	}
	
	public static void replacePanel(JPanel newPanel) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.replacePanel()", MainFrame.class.getSimpleName()));
		singleton.remove(singleton.currentPanel);
		singleton.currentPanel = newPanel;
		singleton.add(newPanel);
		singleton.revalidate();
		singleton.repaint();
	}

	//@Handler
	public void updateLang(Lang session) {
		this.setTitle(session.getTitle());	
	}
	
}
