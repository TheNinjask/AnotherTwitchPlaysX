package pt.theninjask.AnotherTwitchPlaysX.gui.login;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pt.theninjask.AnotherTwitchPlaysX.data.SessionData;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.twitch.TwitchPlayer;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class LoginPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static LoginPanel singleton = new LoginPanel();
	
	private JTextField nickname;
	
	private JTextField channel;
	
	private JTextField oauth;
	
	private JButton openOauth;
	
	private JCheckBox rememberSession;
	
	public static LoginPanel getInstance() {
		return singleton;
	}
	
	private LoginPanel() {
		this.setBackground(Constants.TWITCH_COLOR);
		this.setLayout(new GridLayout(4, 1));
		this.add(setupNickSlot());
		this.add(setupChannelSlot());
		this.add(setupOAuthSlot());
		this.add(setupStartAppSlot());
	}
	
	private JPanel setupNickSlot() {
		JPanel tmp = new JPanel();
		tmp.setLayout(new FlowLayout(FlowLayout.CENTER));
		tmp.add(nickLabel());
		tmp.add(insertNick());
		tmp.setOpaque(false);
		return tmp;
	}
	
	private JTextField nickLabel() {
		JTextField tmp = new JTextField(Constants.TWITCH_FIELD);
		tmp.setEditable(false);
		tmp.setBorder(null);
		tmp.setHorizontalAlignment(JTextField.RIGHT);
		tmp.setOpaque(false);
		tmp.setToolTipText(Constants.TWITCH_FIELD_TIP);
		tmp.setFocusable(false);
		tmp.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		return tmp;
	}
	
	private JTextField insertNick() {
		nickname = new JTextField();
		nickname.setPreferredSize(new Dimension(150, 25));
		nickname.setBorder(null);
		return nickname;
	}
	
	private JPanel setupChannelSlot() {
		JPanel tmp = new JPanel();
		tmp.setLayout(new FlowLayout(FlowLayout.CENTER));
		tmp.add(channelLabel());
		tmp.add(insertChannel());
		tmp.setOpaque(false);
		return tmp;
	}
	
	private JTextField channelLabel() {
		JTextField tmp = new JTextField(Constants.CHANNEL_FIELD);
		tmp.setEditable(false);
		tmp.setBorder(null);
		tmp.setHorizontalAlignment(JTextField.RIGHT);
		tmp.setOpaque(false);
		tmp.setFocusable(false);
		tmp.setToolTipText(Constants.CHANNEL_FIELD_TIP);
		tmp.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		return tmp;
	}
	
	private JTextField insertChannel() {
		channel = new JTextField();
		channel.setPreferredSize(new Dimension(150, 25));
		channel.setBorder(null);
		return channel;
	}
	
	private JPanel setupOAuthSlot() {
		JPanel tmp = new JPanel();
		tmp.setLayout(new FlowLayout(FlowLayout.CENTER));
		tmp.add(oauthLabel());
		tmp.add(insertOAuth());
		tmp.add(openTwitchOauth());
		tmp.setOpaque(false);
		return tmp;
	}
	
	private JTextField oauthLabel() {
		JTextField tmp = new JTextField(Constants.OAUTH_FIELD);
		tmp.setEditable(false);
		tmp.setBorder(null);
		tmp.setHorizontalAlignment(JTextField.RIGHT);
		tmp.setOpaque(false);
		tmp.setFocusable(false);
		tmp.setToolTipText(Constants.OAUTH_FIELD_TIP);
		tmp.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		return tmp;
	}
	
	private JTextField insertOAuth() {
		oauth = new JTextField();
		oauth.setPreferredSize(new Dimension(145, 25));
		oauth.setBorder(null);
		return oauth;
	}
	
	private JButton openTwitchOauth() {
		openOauth = new JButton(Constants.TWITCH_CHAT_OAUTH_BUTTON);
		openOauth.setFocusable(false);
		openOauth.addActionListener(e->{
			Constants.openWebsite(Constants.TWITCH_CHAT_OAUTH);
		});
		return openOauth;
	}
	
	private JPanel setupStartAppSlot() {
		JPanel tmp = new JPanel();
		tmp.setLayout(new FlowLayout());
		tmp.add(gotoMainMenu());
		tmp.add(insertRememberSession());
		tmp.setOpaque(false);
		return tmp;
	}

	private JButton gotoMainMenu() {
		JButton tmp = new JButton(Constants.LOGIN_BUTTON);
		tmp.setFocusable(false);
		tmp.addActionListener(e->{
			if(!(nickname.getText().length()>0)) {
				JOptionPane.showMessageDialog(null, "Please insert your twitch username.", "Missing Username", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if(!(channel.getText().length()>0)) {
				JOptionPane.showMessageDialog(null, "Please insert your twitch channel name.", "Missing Channel Name", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if(!(oauth.getText().length()>0)) {
				String[] options = {"Ok", "Go to get OAuth Token"};
				switch(JOptionPane.showOptionDialog(
						null,
						"Please insert your OAuth Token", 
						"Missing OAuth", 
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE,
						null,
						options,
						0)) {
						case JOptionPane.NO_OPTION:
							Constants.openWebsite(Constants.TWITCH_CHAT_OAUTH);
							break;
						default:
							break;
				}
				return;
			}
			SessionData session = new SessionData(
					nickname.getText(),
					channel.getText(),
					oauth.getText()
					);
			DataManager.getInstance().setSession(
					session
					);
			TwitchPlayer.getInstance().setSession(session);
			MainFrame.getInstance().replacePanel(MainMenuPanel.getInstance());
		});
		return tmp;
	}
	
	private JCheckBox insertRememberSession() {
		rememberSession = new JCheckBox();
		rememberSession.setOpaque(false);
		rememberSession.setText("Remember Session?");
		rememberSession.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		return rememberSession;
	}
	
	public boolean rememberSession() {
		return rememberSession.isSelected();
	}
	
}
