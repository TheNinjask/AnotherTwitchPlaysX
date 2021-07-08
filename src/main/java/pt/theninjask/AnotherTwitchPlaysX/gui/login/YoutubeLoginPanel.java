package pt.theninjask.AnotherTwitchPlaysX.gui.login;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.nio.file.Files;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import pt.theninjask.AnotherTwitchPlaysX.data.YouTubeSessionData;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class YoutubeLoginPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static YoutubeLoginPanel singleton = new YoutubeLoginPanel();

	private String secret;

	private JButton goToMainMenu;

	private JCheckBox rememberSession;

	private JLabel secretLabel;

	private JButton openSecret;

	private JButton clearSecret;

	private JButton secretButton;

	private JLabel videoLabel;

	private JTextField video;

	private JButton back;

	private YoutubeLoginPanel() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", YoutubeLoginPanel.class.getSimpleName()));
		this.setBackground(Constants.TWITCH_COLOR);
		this.setLayout(new GridLayout(3, 1));
		this.add(setupSecretSlot());
		this.add(setupVideoSlot());
		this.add(setupStartAppSlot());
	}

	public static YoutubeLoginPanel getInstance() {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.getInstance()", YoutubeLoginPanel.class.getSimpleName()));
		//singleton.refresh();
		return singleton;
	}

	public void refresh() {
		YouTubeSessionData session = DataManager.getYouTubeSession();
		setSession(session);
	}

	private JPanel setupSecretSlot() {
		JPanel tmp = new JPanel();
		tmp.setLayout(new FlowLayout(FlowLayout.CENTER));
		tmp.add(secretLabel());
		tmp.add(insertSecret());
		tmp.add(clearSecret());
		tmp.add(openSecret());

		tmp.setOpaque(false);
		return tmp;
	}

	private JLabel secretLabel() {
		secretLabel = new JLabel(DataManager.getLanguage().getLogin().getSecretField());
		secretLabel.setBorder(null);
		secretLabel.setHorizontalAlignment(JTextField.RIGHT);
		secretLabel.setOpaque(false);
		secretLabel.setFocusable(false);
		// secretLabel.setToolTipText("");
		secretLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		return secretLabel;
	}

	private JButton insertSecret() {
		secretButton = new JButton(DataManager.getLanguage().getLogin().getSetSecret());
		secretButton.setFocusable(false);
		secretButton.setMargin(new Insets(2, 2, 2, 2));
		secretButton.addActionListener(l -> {
			if(secret==null || secret.isBlank()) {
				File file = Constants.showOpenFile(null, this);
				try {
					secret = Files.readString(file.toPath());
				} catch (Exception e) {
					return;
				}
				if(secret!=null && !secret.isBlank())
					secretButton.setText(DataManager.getLanguage().getLogin().getViewSecret());
			}else {
				JScrollPane pane = new JScrollPane();
				pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonElement je = JsonParser.parseString(secret);
				String prettySecret = gson.toJson(je);
				
				JTextArea area = new JTextArea(prettySecret);
				area.setEditable(false);
				
				pane.setViewportView(area);
				pane.setFocusable(false);
				pane.setEnabled(false);
				new PopOutFrame(pane, MainFrame.getInstance());
			}
		});
		return secretButton;
	}

	private JButton clearSecret() {
		clearSecret = new JButton(DataManager.getLanguage().getLogin().getSecretClear());
		clearSecret.setFocusable(false);
		clearSecret.setMargin(new Insets(2, 2, 2, 2));
		clearSecret.addActionListener(l -> {
			secret = null;
			secretButton.setText(DataManager.getLanguage().getLogin().getSetSecret());
		});
		return clearSecret;
	}

	private JButton openSecret() {
		openSecret = new JButton(DataManager.getLanguage().getLogin().getSecretButton());
		openSecret.setFocusable(false);
		openSecret.setMargin(new Insets(2, 2, 2, 2));
		openSecret.addActionListener(e -> {
			Constants.openWebsite(Constants.YOUTUBE_CHAT_SECRET);
		});
		return openSecret;
	}
	
	private JPanel setupVideoSlot() {
		JPanel tmp = new JPanel();
		tmp.setLayout(new FlowLayout(FlowLayout.CENTER));
		tmp.add(videoLabel());
		tmp.add(insertVideo());
		tmp.setOpaque(false);
		return tmp;
	}

	private JLabel videoLabel() {
		videoLabel = new JLabel(DataManager.getLanguage().getLogin().getVideoField());
		videoLabel.setBorder(null);
		videoLabel.setHorizontalAlignment(JTextField.RIGHT);
		videoLabel.setOpaque(false);
		videoLabel.setFocusable(false);
		//videoLabel.setToolTipText("");
		videoLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		return videoLabel;
	}

	private JTextField insertVideo() {
		video = new JTextField();
		video.setPreferredSize(new Dimension(150, 25));
		video.setBorder(null);
		return video;
	}
	
	private JPanel setupStartAppSlot() {
		JPanel tmp = new JPanel();
		tmp.setLayout(new FlowLayout());
		tmp.add(gotoMainMenu());
		tmp.add(insertRememberSession());
		tmp.setOpaque(false);
		
		back = new JButton(DataManager.getLanguage().getLogin().getGoBack());
		back.setFocusable(false);
		back.addActionListener(l->{
			setSession(DataManager.getYouTubeSession());
			MainFrame.replacePanel(MainLoginPanel.getInstance());
		});
		tmp.add(back);
		
		return tmp;
	}

	private JButton gotoMainMenu() {
		goToMainMenu = new JButton(DataManager.getLanguage().getLogin().getLoginButton());
		goToMainMenu.setFocusable(false);
		goToMainMenu.addActionListener(e -> {
			if (secret == null || secret.isBlank()) {
				String[] options = { DataManager.getLanguage().getOkOpt(),
						DataManager.getLanguage().getLogin().getMissingSecretGet() };
				JLabel msg = new JLabel(DataManager.getLanguage().getLogin().getMissingSecretMsg());
				msg.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				switch (Constants.showCustomColorOptionDialog(null, msg,
						DataManager.getLanguage().getLogin().getMissingSecretTitle(), JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, null, Constants.TWITCH_COLOR)) {
				case JOptionPane.NO_OPTION:
					Constants.openWebsite(Constants.YOUTUBE_CHAT_SECRET);
					break;
				default:
					break;
				}
				return;
			}
			String videoId = video.getText() == null || video.getText().isBlank() ? null : video.getText();
			YouTubeSessionData session = new YouTubeSessionData(secret, videoId);
			DataManager.setYouTubeSession(session);
			MainFrame.replacePanel(MainLoginPanel.getInstance());
		});
		return goToMainMenu;
	}

	private JCheckBox insertRememberSession() {
		rememberSession = new JCheckBox();
		rememberSession.setOpaque(false);
		rememberSession.setText(DataManager.getLanguage().getLogin().getRememberSession());
		rememberSession.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		return rememberSession;
	}

	//forces
	public void setSession(YouTubeSessionData session) {
		if(session == null) {
			secret = null;
			secretButton.setText(DataManager.getLanguage().getLogin().getSetSecret());
			video.setText("");
			return;
		}
		secret = session.getSecret();
		if(secret == null || secret.isBlank())
			secretButton.setText(DataManager.getLanguage().getLogin().getSetSecret());
		else
			secretButton.setText(DataManager.getLanguage().getLogin().getViewSecret());
		if(session.getVideoId() != null)
			video.setText(session.getVideoId());
		else
			video.setText("");
	}
	
	//best effort
	public void setSession(String secret, String videoId) {
		if(secret!=null) {
			this.secret = secret;
			secretButton.setText(DataManager.getLanguage().getLogin().getViewSecret());
		}
		if(videoId != null)
			video.setText(videoId);
	}
	public boolean rememberSession() {
		return rememberSession.isSelected();
	}

	// @Handler
	public void updateLang(Lang session) {
		secretLabel.setText(DataManager.getLanguage().getLogin().getSecretField());
		if(secret == null || secret.isBlank())
			secretButton.setText(DataManager.getLanguage().getLogin().getSetSecret());
		else
			secretButton.setText(DataManager.getLanguage().getLogin().getViewSecret());
		clearSecret.setText(DataManager.getLanguage().getLogin().getSecretClear());
		openSecret.setText(DataManager.getLanguage().getLogin().getSecretButton());
		
		videoLabel.setText(DataManager.getLanguage().getLogin().getVideoField());
		
		goToMainMenu.setText(DataManager.getLanguage().getLogin().getLoginButton());
		rememberSession.setText(DataManager.getLanguage().getLogin().getRememberSession());
		
		back.setText(DataManager.getLanguage().getLogin().getGoBack());
	}

}
