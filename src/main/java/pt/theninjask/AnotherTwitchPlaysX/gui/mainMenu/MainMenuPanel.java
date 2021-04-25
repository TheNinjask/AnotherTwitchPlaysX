package pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.LoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.twitch.TwitchPlayer;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class MainMenuPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static MainMenuPanel singleton = new MainMenuPanel();
	
	private JButton connectButton;
	
	private JButton commandsButton;
	
	private JButton gameButton;
	
	private JButton changeSessionButton;
	
	private JButton twitchChatButton;
	
	private JSlider twitchChatSize;
	
	private MainMenuPanel() {
		this.setBackground(Constants.TWITCH_COLOR);
		this.setLayout(new GridLayout(7, 1));
		this.add(connectButton());
		this.add(commandsButton());
		this.add(gameButton());
		this.add(changeSessionButton());
		this.add(twitchChatButton());
		this.add(twitchChatSlider());
		this.add(twitchChatSliderLabel(),5);
	}
	
	public static MainMenuPanel getInstance() {
		return singleton;
	}
	
	private JButton connectButton() {
		connectButton = new JButton("Connect");
		connectButton.setFocusable(false);
		connectButton.addActionListener(l->{
			if(TwitchPlayer.getInstance().isConnected()) {
				TwitchPlayer.getInstance().disconnect();
				connectButton.setText("Connect");
				twitchChatButton.setEnabled(false);
				twitchChatButton.setText("Show Twitch Chat");
				gameButton.setEnabled(false);
				gameButton.setText("Start");
				changeSessionButton.setEnabled(true);
			}else {
				TwitchPlayer.getInstance().setupAndConnect();
				connectButton.setText("Disconnect");
				twitchChatButton.setEnabled(true);
				gameButton.setEnabled(true);
				changeSessionButton.setEnabled(false);
			}
		});
		return connectButton;
	}
	
	private JButton commandsButton() {
		commandsButton = new JButton("Set Commands");
		commandsButton.setFocusable(false);
		return commandsButton;
	}
	
	private JButton gameButton() {
		gameButton = new JButton("Start");
		gameButton.setFocusable(false);
		gameButton.setEnabled(false);
		return gameButton;
	}
	
	private JButton changeSessionButton() {
		changeSessionButton = new JButton("Change Session");
		changeSessionButton.setFocusable(false);
		changeSessionButton.addActionListener(l->{
			MainFrame.getInstance().replacePanel(LoginPanel.getInstance());
		});
		return changeSessionButton;
	}
	
	private JButton twitchChatButton() {
		twitchChatButton = new JButton("Show Twitch Chat");
		twitchChatButton.setFocusable(false);
		twitchChatButton.setEnabled(false);
		return twitchChatButton;
	}
	
	private JSlider twitchChatSlider() {
		twitchChatSize = new JSlider(5,20,5);
		twitchChatSize.setMajorTickSpacing(15);
		twitchChatSize.setPaintLabels(true);
		twitchChatSize.setOpaque(false);
		twitchChatSize.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		return twitchChatSize;
	}
	
	private JLabel twitchChatSliderLabel() {
		JLabel label = new JLabel();
		label.setText(String.format(Constants.CURRENT_CHAT_SIZE, twitchChatSize.getValue()));
		label.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		twitchChatSize.addChangeListener(e->{
			label.setText(String.format(Constants.CURRENT_CHAT_SIZE, twitchChatSize.getValue()));
		});
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}
	
}
