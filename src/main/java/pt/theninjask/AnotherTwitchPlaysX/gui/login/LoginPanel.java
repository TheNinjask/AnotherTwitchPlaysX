package pt.theninjask.AnotherTwitchPlaysX.gui.login;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import pt.theninjask.AnotherTwitchPlaysX.data.SessionData;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;
import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager.OnUpdateLanguage;
import pt.theninjask.AnotherTwitchPlaysX.twitch.TwitchPlayer;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class LoginPanel extends JPanel implements OnUpdateLanguage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static LoginPanel singleton = new LoginPanel();
	
	private JTextField nickname;
	
	private JTextField channel;
	
	private JPasswordField oauth;
	
	private JButton openOauth;
	
	private JCheckBox rememberSession;

	private JLabel nickLabel;

	private JLabel channelLabel;

	private JCheckBox oauthCheckBox;

	private JLabel oauthLabel;

	private JButton goToMainMenu;
	
	public static LoginPanel getInstance() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.getInstance()", LoginPanel.class.getSimpleName()));
		return singleton;
	}
	
	private LoginPanel() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", LoginPanel.class.getSimpleName()));
		this.setBackground(Constants.TWITCH_COLOR);
		this.setLayout(new GridLayout(4, 1));
		this.add(setupNickSlot());
		this.add(setupChannelSlot());
		this.add(setupOAuthSlot());
		this.add(setupStartAppSlot());
		//DataManager.registerLangEvent(this);
	}
	
	private JPanel setupNickSlot() {
		JPanel tmp = new JPanel();
		tmp.setLayout(new FlowLayout(FlowLayout.CENTER));
		tmp.add(nickLabel());
		tmp.add(insertNick());
		tmp.setOpaque(false);
		return tmp;
	}
	
	private JLabel nickLabel() {
		nickLabel = new JLabel(DataManager.getLanguage().getLogin().getTwitchField());
		nickLabel.setBorder(null);
		nickLabel.setHorizontalAlignment(JTextField.RIGHT);
		nickLabel.setOpaque(false);
		nickLabel.setToolTipText(DataManager.getLanguage().getLogin().getTwitchFieldTip());
		nickLabel.setFocusable(false);
		nickLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		return nickLabel;
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
	
	private JLabel channelLabel() {
		channelLabel = new JLabel(DataManager.getLanguage().getLogin().getChannelField());
		channelLabel.setBorder(null);
		channelLabel.setHorizontalAlignment(JTextField.RIGHT);
		channelLabel.setOpaque(false);
		channelLabel.setFocusable(false);
		channelLabel.setToolTipText(DataManager.getLanguage().getLogin().getChannelFieldTip());
		channelLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		return channelLabel;
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
		
		oauthCheckBox = new JCheckBox(DataManager.getLanguage().getLogin().getShowToken());
		oauthCheckBox.setFocusable(false);
		oauthCheckBox.setOpaque(false);
		oauthCheckBox.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		oauthCheckBox.addActionListener(l->{
			if(oauthCheckBox.isSelected()) {
				oauth.setEchoChar((char)0);
			}else {
				oauth.setEchoChar((char)8226);
			}
		});
		tmp.add(oauthCheckBox);
		
		tmp.setOpaque(false);
		return tmp;
	}
	
	private JLabel oauthLabel() {
		oauthLabel = new JLabel(DataManager.getLanguage().getLogin().getOAuthField());
		oauthLabel.setBorder(null);
		oauthLabel.setHorizontalAlignment(JTextField.RIGHT);
		oauthLabel.setOpaque(false);
		oauthLabel.setFocusable(false);
		oauthLabel.setToolTipText(DataManager.getLanguage().getLogin().getOAuthFieldTip());
		oauthLabel.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		return oauthLabel;
	}
	
	private JPasswordField insertOAuth() {
		oauth = new JPasswordField();
		oauth.setPreferredSize(new Dimension(145, 25));
		oauth.setBorder(null);
		return oauth;
	}
	
	private JButton openTwitchOauth() {
		openOauth = new JButton(DataManager.getLanguage().getLogin().getOAuthButton());
		openOauth.setFocusable(false);
		openOauth.setMargin(new Insets(2,10,2,10));
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
		goToMainMenu = new JButton(DataManager.getLanguage().getLogin().getLoginButton());
		goToMainMenu.setFocusable(false);
		goToMainMenu.addActionListener(e->{
			if(!(nickname.getText().length()>0)) {
				JLabel msg = new JLabel(DataManager.getLanguage().getLogin().getMissingUsernameMsg());
				msg.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				Constants.showCustomColorMessageDialog(null, msg, DataManager.getLanguage().getLogin().getMissingUsernameTitle(), JOptionPane.WARNING_MESSAGE, null, Constants.TWITCH_COLOR);
				return;
			}
			if(!(channel.getText().length()>0)) {
				JLabel msg = new JLabel(DataManager.getLanguage().getLogin().getMissingChannelMsg());
				msg.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				Constants.showCustomColorMessageDialog(null, msg, DataManager.getLanguage().getLogin().getMissingChannelTitle(), JOptionPane.WARNING_MESSAGE, null, Constants.TWITCH_COLOR);
				return;
			}
			if(!(oauth.getPassword().length>0)) {
				String[] options = {DataManager.getLanguage().getOkOpt(), DataManager.getLanguage().getLogin().getMissingOAuthGet()};
				JLabel msg = new JLabel(DataManager.getLanguage().getLogin().getMissingOAuthMsg());
				msg.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				switch(Constants.showCustomColorOptionDialog(
						null,
						msg, 
						DataManager.getLanguage().getLogin().getMissingOAuthTitle(), 
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE,
						null,
						options,
						null,
						Constants.TWITCH_COLOR)) {
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
					String.format("#%s", channel.getText()),
					new String(oauth.getPassword())
					);
			DataManager.setSession(
					session
					);
			TwitchPlayer.getInstance().setSession(session);
			MainFrame.replacePanel(MainMenuPanel.getInstance());
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
	
	public boolean rememberSession() {
		return rememberSession.isSelected();
	}
	
	public void setSession(SessionData session) {
		nickname.setText(session.getNickname());
		channel.setText(session.getChannel().substring(1));
		oauth.setText(session.getOauth());
		//ANNOYING LITTLE PIECE OF CODE ofc ;)
		//rememberSession.setSelected(true);
	}
	
	public void setSession(String nickname, String channel, String oauth) {
		if(nickname!=null)
			this.nickname.setText(nickname);
		if(channel!=null)
			this.channel.setText(channel.substring(1));
		if(oauth!=null)
			this.oauth.setText(oauth);
	}

	@Override
	public void updateLang(Lang session) {
		nickLabel.setText(session.getLogin().getTwitchField());
		nickLabel.setToolTipText(session.getLogin().getTwitchFieldTip());
		channelLabel.setText(session.getLogin().getChannelField());
		channelLabel.setToolTipText(session.getLogin().getChannelFieldTip());
		oauthLabel.setText(session.getLogin().getOAuthField());
		oauthLabel.setToolTipText(session.getLogin().getOAuthFieldTip());
		openOauth.setText(session.getLogin().getOAuthButton());
		oauthCheckBox.setText(session.getLogin().getShowToken());
		goToMainMenu.setText(session.getLogin().getLoginButton());
		rememberSession.setText(session.getLogin().getRememberSession());
	}
	
}
