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
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.data.YouTubeSessionData;
import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.ColorThemeUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.LanguageUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.WrapEditorKit;

public class YoutubeLoginPanel extends JPanel {

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
		this.setBackground(DataManager.getTheme().background());
		this.setLayout(new GridLayout(3, 1));
		this.add(setupSecretSlot());
		this.add(setupVideoSlot());
		this.add(setupStartAppSlot());
		EventManager.registerEventListener(this);
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
		secretLabel.setForeground(DataManager.getTheme().font());
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
				
				JTextPane area = new JTextPane();
				area.setEditorKit(new WrapEditorKit());
				area.setText(prettySecret);
				area.setEditable(false);
				
				pane.setViewportView(area);
				pane.setFocusable(false);
				pane.setEnabled(false);
				new PopOutFrame(pane, MainFrame.getInstance()).setVisible(true);
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
		videoLabel.setForeground(DataManager.getTheme().font());
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
				msg.setForeground(DataManager.getTheme().font());
				switch (Constants.showCustomColorOptionDialog(null, msg,
						DataManager.getLanguage().getLogin().getMissingSecretTitle(), JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, null, DataManager.getTheme().background())) {
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
		rememberSession.setForeground(DataManager.getTheme().font());
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
	public void updateLang(LanguageUpdateEvent event) {
		if(event.getLanguage()==null)
			return;
		secretLabel.setText(event.getLanguage().getLogin().getSecretField());
		if(secret == null || secret.isBlank())
			secretButton.setText(event.getLanguage().getLogin().getSetSecret());
		else
			secretButton.setText(event.getLanguage().getLogin().getViewSecret());
		clearSecret.setText(event.getLanguage().getLogin().getSecretClear());
		openSecret.setText(event.getLanguage().getLogin().getSecretButton());
		
		videoLabel.setText(event.getLanguage().getLogin().getVideoField());
		
		goToMainMenu.setText(event.getLanguage().getLogin().getLoginButton());
		rememberSession.setText(event.getLanguage().getLogin().getRememberSession());
		
		back.setText(event.getLanguage().getLogin().getGoBack());
	}
	
	@Handler
	public void updateTheme(ColorThemeUpdateEvent event) {
		if(event.getTheme()!=null) {
			this.setBackground(event.getTheme().background());
			secretLabel.setForeground(event.getTheme().font());
			videoLabel.setForeground(event.getTheme().font());
			rememberSession.setForeground(event.getTheme().font());
		}
	}

}
