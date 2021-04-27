package pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.TwitchChatFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.LoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.twitch.SponsorBot;
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
	
	private JCheckBox sponsor;
	
	private JSlider sponsorSlider;
	
	private MainMenuPanel() {
		this.setBackground(Constants.TWITCH_COLOR);
		this.setLayout(new GridLayout(10, 1));
		this.add(connectButton());
		this.add(commandsButton());
		this.add(gameButton());
		this.add(changeSessionButton());
		this.add(twitchChatButton());
		this.add(twitchChatColorModeLabel());
		this.add(twitchChatColorModePanel());
		this.add(twitchChatSlider());
		this.add(twitchChatSliderLabel(),7);
		this.add(sponsorMeInChat());
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
				TwitchChatFrame.getInstance().setVisible(false);
				TwitchChatFrame.getInstance().clearChat();
				sponsor.setEnabled(false);
				sponsor.setSelected(false);
				SponsorBot.getInstance().stop();
			}else {
				TwitchPlayer.getInstance().setupAndConnect();
				connectButton.setText("Disconnect");
				twitchChatButton.setEnabled(true);
				gameButton.setEnabled(true);
				changeSessionButton.setEnabled(false);
				sponsor.setEnabled(true);
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
		twitchChatButton.addActionListener(l->{
			if(TwitchChatFrame.getInstance().isVisible()) {
				TwitchChatFrame.getInstance().setVisible(false);
				twitchChatButton.setText("Show Twitch Chat");
				TwitchPlayer.getInstance().unregisterEventListener(TwitchChatFrame.getInstance());
			}else {
				TwitchChatFrame.getInstance().setVisible(true);
				TwitchChatFrame.getInstance().addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent event) {
						twitchChatButton.setText("Show Twitch Chat");
						TwitchPlayer.getInstance().unregisterEventListener(TwitchChatFrame.getInstance());
					}
				});
				twitchChatButton.setText("Hide Twitch Chat");
				TwitchPlayer.getInstance().registerEventListener(TwitchChatFrame.getInstance());
			}
		});
		return twitchChatButton;
	}
	
	private JSlider twitchChatSlider() {
		twitchChatSize = new JSlider(TwitchChatFrame.MSG_DISPLAY_MIN,TwitchChatFrame.MSG_DISPLAY_INFINITE,5);
		twitchChatSize.setMajorTickSpacing(45);
		twitchChatSize.setPaintLabels(true);
		twitchChatSize.setOpaque(false);
		//twitchChatSize.setFocusable(false);
		twitchChatSize.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		return twitchChatSize;
	}
	
	private JLabel twitchChatSliderLabel() {
		JLabel label = new JLabel();
		label.setText(String.format(Constants.CURRENT_CHAT_SIZE, twitchChatSize.getValue()));
		label.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		label.setFocusable(false);
		twitchChatSize.addChangeListener(e->{
			int value = twitchChatSize.getValue();
			label.setText(String.format(Constants.CURRENT_CHAT_SIZE, value<TwitchChatFrame.MSG_DISPLAY_INFINITE ? value : "Infinite"));
			TwitchChatFrame.getInstance().setMessageCap(value);
			TwitchChatFrame.getInstance().updateChatSize();
		});
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}
	
	private JLabel twitchChatColorModeLabel() {
		JLabel label = new JLabel();
		label.setText("Color Mode for Twitch Chat");
		label.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFocusable(false);
		return label;
	}
	
	private JPanel twitchChatColorModePanel() {
		JPanel tmp = new JPanel(new FlowLayout());
		tmp.setOpaque(false);
		ButtonGroup group = new ButtonGroup();
		JRadioButton light = new JRadioButton("Light Mode");
		JRadioButton night = new JRadioButton("Night Mode");
		JRadioButton twitch = new JRadioButton("Twitch Mode");
		light.setOpaque(false);
		light.setFocusable(false);
		light.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		light.addActionListener(i->{
			TwitchChatFrame.getInstance().setColor(Color.BLACK, Color.WHITE);
		});
		night.setOpaque(false);
		night.setFocusable(false);
		night.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		night.addActionListener(i->{
			TwitchChatFrame.getInstance().setColor(Color.WHITE, Color.BLACK);
		});
		twitch.setOpaque(false);
		twitch.setFocusable(false);
		twitch.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		twitch.setSelected(true);
		twitch.addActionListener(i->{
			TwitchChatFrame.getInstance().setColor(Constants.TWITCH_COLOR_COMPLEMENT,
					Constants.TWITCH_COLOR);
		});
		group.add(light);
		group.add(night);
		group.add(twitch);
		tmp.add(light);
		tmp.add(night);
		tmp.add(twitch);
		return tmp;
	}
	
	private JCheckBox sponsorMeInChat() {
		sponsor = new JCheckBox();
		sponsor.setText("Sponsor Me(Creator) in chat?");
		sponsor.setHorizontalTextPosition(JCheckBox.LEFT);
		sponsor.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		sponsor.setHorizontalAlignment(JCheckBox.CENTER);
		sponsor.setOpaque(false);
		sponsor.setFocusable(false);
		sponsor.setEnabled(false);
		sponsor.addActionListener(l->{
			if(sponsor.isSelected()) {
				String[] options = {"Got it","Nevermind"};
				JTextArea label = new JTextArea();
				label.setText("Before going any further, I wanna leave it clear that this app will use the account of the OAuth provided to send messages for me in chat.\nSo don't worry that the account is not being hacked.\nJust wanted to state to not provoke any worry or/and confusion.\nIf you want to do it but are scared of this you can use a burner account, the app will still work.");
				label.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				label.setFocusable(false);
				label.setOpaque(false);
				int resp = Constants.showCustomColorOptionDialog(
						null, 
						label, 
						"Thank you for wanting to sponsor me!", 
						JOptionPane.OK_CANCEL_OPTION, 
						JOptionPane.WARNING_MESSAGE, 
						null, 
						options, 
						null,
						Constants.TWITCH_COLOR);
				switch (resp) {
					case JOptionPane.OK_OPTION:
						break;
					case JOptionPane.CANCEL_OPTION:
					case JOptionPane.CLOSED_OPTION:
					default:
						sponsor.setSelected(false);
						return;
				}
				String[] options2 = {"Yes and not check message","Yes and check message","Nevermind"};
				resp = Constants.showCustomColorOptionDialog(
						null, 
						sponsorPanel(), 
						"Thank you for wanting to sponsor me!", 
						JOptionPane.YES_NO_CANCEL_OPTION, 
						JOptionPane.PLAIN_MESSAGE, 
						null, 
						options2, 
						null,
						Constants.TWITCH_COLOR);
				switch(resp) {
					case JOptionPane.YES_OPTION:
						SponsorBot bot = SponsorBot.getInstance();
						bot.setCooldown(sponsorSlider.getValue());
						bot.start();
						return;
					case JOptionPane.NO_OPTION:
						break;
					case JOptionPane.CANCEL_OPTION:
					case JOptionPane.CLOSED_OPTION:
					default:
						sponsor.setSelected(false);
						return;	
				}
				label.setText(String.format("The message will be:\n%s", SponsorBot.getSponsorMsg()));
				label.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				label.setFocusable(false);
				label.setOpaque(false);
				resp = Constants.showCustomColorOptionDialog(
						null, 
						label, 
						"Thank you for wanting to sponsor me!", 
						JOptionPane.OK_CANCEL_OPTION, 
						JOptionPane.PLAIN_MESSAGE, 
						null, 
						options, 
						null,
						Constants.TWITCH_COLOR);
				switch (resp) {
					case JOptionPane.OK_OPTION:
						SponsorBot bot = SponsorBot.getInstance();
						bot.setCooldown(sponsorSlider.getValue());
						bot.start();
						return;
					case JOptionPane.CANCEL_OPTION:
					case JOptionPane.CLOSED_OPTION:
					default:
						sponsor.setSelected(false);
						return;
			}
			}else {
				SponsorBot.getInstance().stop();
			}
		});
		return sponsor;
	}
		
	private JPanel sponsorPanel() {
		JPanel tmp = new JPanel(new GridLayout(1, 2));
		
		sponsorSlider = new JSlider(5, 30, 30);
		sponsorSlider.setMajorTickSpacing(25);
		sponsor.setFocusable(false);
		sponsorSlider.setPaintLabels(true);
		sponsorSlider.setOpaque(false);
		sponsorSlider.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		
		JLabel label = new JLabel();
		label.setText(String.format("Sponsor me every %s min.", sponsorSlider.getValue()));
		label.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		label.setFocusable(false);
		sponsorSlider.addChangeListener(e->{
			int value = sponsorSlider.getValue();
			label.setText(String.format("Sponsor me every %s min.", value));
			//TwitchChatFrame.getInstance().setMessageCap(value);
			//TwitchChatFrame.getInstance().updateChatSize();
		});
		label.setHorizontalAlignment(JLabel.CENTER);
		tmp.add(label);
		tmp.add(sponsorSlider);
		tmp.setFocusable(false);
		tmp.setBackground(Constants.TWITCH_COLOR);
		return tmp;
	}
	
}
