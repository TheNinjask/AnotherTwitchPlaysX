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

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.data.TwitchSessionData;
import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.ColorThemeUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.LanguageUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class TwitchLoginPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	private static TwitchLoginPanel singleton = new TwitchLoginPanel();

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

	private JButton back;

	public static TwitchLoginPanel getInstance() {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.getInstance()", TwitchLoginPanel.class.getSimpleName()));
		//singleton.refresh();
		return singleton;
	}

	private TwitchLoginPanel() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", TwitchLoginPanel.class.getSimpleName()));
		this.setBackground(DataManager.getTheme().getBackground());
		this.setLayout(new GridLayout(4, 1));
		this.add(setupNickSlot());
		this.add(setupChannelSlot());
		this.add(setupOAuthSlot());
		this.add(setupStartAppSlot());
		// DataManager.registerLangEvent(this);
		EventManager.registerEventListener(this);
	}

	public void refresh() {
		TwitchSessionData session = DataManager.getTwitchSession();
		setSession(session);
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
		nickLabel.setForeground(DataManager.getTheme().getFont());
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
		channelLabel.setForeground(DataManager.getTheme().getFont());
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
		oauthCheckBox.setForeground(DataManager.getTheme().getFont());
		oauthCheckBox.addActionListener(l -> {
			if (oauthCheckBox.isSelected()) {
				oauth.setEchoChar((char) 0);
			} else {
				oauth.setEchoChar((char) 8226);
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
		oauthLabel.setForeground(DataManager.getTheme().getFont());
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
		openOauth.setMargin(new Insets(2, 10, 2, 10));
		openOauth.addActionListener(e -> {
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
		back = new JButton(DataManager.getLanguage().getLogin().getGoBack());
		back.setFocusable(false);
		back.addActionListener(l->{
			setSession(DataManager.getTwitchSession());
			MainFrame.replacePanel(MainLoginPanel.getInstance());
		});
		tmp.add(back);
		return tmp;
	}

	private JButton gotoMainMenu() {
		goToMainMenu = new JButton(DataManager.getLanguage().getLogin().getLoginButton());
		goToMainMenu.setFocusable(false);
		goToMainMenu.addActionListener(e -> {
			if (nickname.getText().isBlank()) {
				JLabel msg = new JLabel(DataManager.getLanguage().getLogin().getMissingUsernameMsg());
				msg.setForeground(DataManager.getTheme().getFont());
				Constants.showCustomColorMessageDialog(null, msg,
						DataManager.getLanguage().getLogin().getMissingUsernameTitle(), JOptionPane.WARNING_MESSAGE,
						null, DataManager.getTheme().getBackground());
				return;
			}
			if (!(oauth.getPassword().length > 0)) {
				String[] options = { DataManager.getLanguage().getOkOpt(),
						DataManager.getLanguage().getLogin().getMissingOAuthGet() };
				JLabel msg = new JLabel(DataManager.getLanguage().getLogin().getMissingOAuthMsg());
				msg.setForeground(DataManager.getTheme().getFont());
				switch (Constants.showCustomColorOptionDialog(null, msg,
						DataManager.getLanguage().getLogin().getMissingOAuthTitle(), JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, null, DataManager.getTheme().getBackground())) {
				case JOptionPane.NO_OPTION:
					Constants.openWebsite(Constants.TWITCH_CHAT_OAUTH);
					break;
				default:
					break;
				}
				return;
			}

			String sessionChannel = channel.getText() == null || channel.getText().isBlank() ? null
					: String.format("#%s", channel.getText());

			TwitchSessionData session = new TwitchSessionData(nickname.getText(),
					sessionChannel, new String(oauth.getPassword()));
			DataManager.setTwitchSession(session);
			// TwitchPlayer.getInstance().setSession(session);
			MainFrame.replacePanel(MainLoginPanel.getInstance());
		});
		return goToMainMenu;
	}

	private JCheckBox insertRememberSession() {
		rememberSession = new JCheckBox();
		rememberSession.setOpaque(false);
		rememberSession.setText(DataManager.getLanguage().getLogin().getRememberSession());
		rememberSession.setForeground(DataManager.getTheme().getFont());
		return rememberSession;
	}

	public boolean rememberSession() {
		return rememberSession.isSelected();
	}

	//forces
	public void setSession(TwitchSessionData session) {
		if(session == null) {
			nickname.setText("");
			channel.setText("");
			oauth.setText("");
			return;
		}
		if(session.getNickname()!=null)
			nickname.setText(session.getNickname());			
		else
			nickname.setText("");
		if(session.getChannel()!=null && session.getChannel().length()>0)
			channel.setText(session.getChannel().substring(1));
		else
			channel.setText("");
		if(session.getOauth()!=null)
			oauth.setText(session.getOauth());
		else
			oauth.setText("");
	}

	//best effort
	public void setSession(String nickname, String channel, String oauth) {
		if (nickname != null)
			this.nickname.setText(nickname);
		if (channel != null && channel.length()>0)
			this.channel.setText(channel.substring(1));
		if (oauth != null)
			this.oauth.setText(oauth);
	}

	//@Handler
	public void updateLang(LanguageUpdateEvent event) {
		Lang session = event.getLanguage();
		if(session == null)
			return;
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
		back.setText(DataManager.getLanguage().getLogin().getGoBack());
	}

	@Handler
	public void updateTheme(ColorThemeUpdateEvent event) {
		if(event.getTheme()!=null) {
			this.setBackground(event.getTheme().getBackground());
			nickLabel.setForeground(event.getTheme().getFont());
			channelLabel.setForeground(event.getTheme().getFont());
			oauthCheckBox.setForeground(event.getTheme().getFont());
			oauthLabel.setForeground(event.getTheme().getFont());
			rememberSession.setForeground(event.getTheme().getFont());
		}
	}
	
}
