package pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;

import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.TwitchChatFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.command.AllCommandPanel;
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
	
	private JButton commandsStartButton;
	
	private JButton gameButton;
	
	private JButton changeSessionButton;
	
	private JButton twitchChatButton;
	
	private JSlider twitchChatSize;
	
	private JCheckBox sponsor;
	
	private JSlider sponsorSlider;
	
	private JCheckBox twitchChatOnTop;
	
	private JPanel twitchChatColorModePanel;
	
	private JPanel twitchChatMode;
	
	private JPanel twitchChatOptionsPanel;
	
	private JComboBox<String> twitchChatFont;
	
	private JSlider twitchChatFontSize;
	
	private JPanel twitchChatFontPanel;
	
	private JPanel twitchChatFontSizePanel;
	
	private MainMenuPanel() {
		this.setBackground(Constants.TWITCH_COLOR);
		this.setLayout(new GridLayout(10, 1));
		this.add(connectButton());
		this.add(commandsButton());
		this.add(gameButton());
		this.add(changeSessionButton());
		this.add(twitchChatButton());
		twitchChatMode();
		twitchChatColorModePanel();
		setTwitchChatOnTop();
		twitchChatFont();
		twitchChatFontSize();
		this.add(twitchChatOptionsLabel());
		this.add(twitchChatOptionsPanel());
		this.add(twitchChatSlider());
		this.add(twitchChatSliderLabel(),this.getComponentCount()-1);
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
	
	private JPanel commandsButton() {
		JPanel tmp = new JPanel(new BorderLayout());
		commandsButton = new JButton("Set Commands");
		commandsButton.setFocusable(false);
		commandsButton.addActionListener(l->{
			MainFrame.getInstance().replacePanel(AllCommandPanel.getInstance());
		});
		tmp.add(commandsButton, BorderLayout.CENTER);
		commandsStartButton = new JButton("Start");
		commandsStartButton.setFocusable(false);
		commandsStartButton.setEnabled(false);
		commandsStartButton.addActionListener(l->{
			//TODO
		});
		tmp.add(commandsStartButton,BorderLayout.EAST);
		return tmp;
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
				TwitchChatFrame.getInstance().clearChat();
				TwitchPlayer.getInstance().unregisterEventListener(TwitchChatFrame.getInstance());
			}else {
				TwitchChatFrame.getInstance().setVisible(true);
				twitchChatButton.setText("Hide Twitch Chat");
				TwitchPlayer.getInstance().registerEventListener(TwitchChatFrame.getInstance());
			}
		});
		TwitchChatFrame.getInstance().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				twitchChatButton.setText("Show Twitch Chat");
				TwitchChatFrame.getInstance().clearChat();
				TwitchPlayer.getInstance().unregisterEventListener(TwitchChatFrame.getInstance());
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
		label.setHorizontalAlignment(JLabel.CENTER);
		return label;
	}
	
	private JPanel twitchChatOptionsLabel() {
		JPanel tmp = new JPanel(new BorderLayout());
		tmp.setOpaque(false);
		tmp.setFocusable(false);
		JLabel label = new JLabel();
		label.setText("Twitch Chat Options");
		label.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFocusable(false);

		JButton left = new JButton("<");
		left.setFocusable(false);
		//left.setHorizontalAlignment(JButton.LEFT);
		left.addActionListener(l->{
			moveOptionPanel(false);
		});
		tmp.add(left, BorderLayout.WEST);
		tmp.add(label, BorderLayout.CENTER);
		JButton right = new JButton(">");
		right.setFocusable(false);
		//right.setHorizontalAlignment(JButton.RIGHT);
		right.addActionListener(l->{
			moveOptionPanel(true);
		});
		tmp.add(right, BorderLayout.EAST);
		
		
		return tmp;
	}
	
	private JPanel twitchChatColorModePanel() {
		twitchChatColorModePanel = new JPanel(new FlowLayout());
		twitchChatColorModePanel.setOpaque(false);
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
		twitchChatColorModePanel.add(light);
		twitchChatColorModePanel.add(night);
		twitchChatColorModePanel.add(twitch);
		return twitchChatColorModePanel;
	}
	
	private JCheckBox setTwitchChatOnTop() {
		twitchChatOnTop = new JCheckBox();
		
		twitchChatOnTop.setText(Constants.IS_TWITCH_CHAT_ON_TOP);
		twitchChatOnTop.setHorizontalTextPosition(JCheckBox.LEFT);
		twitchChatOnTop.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		twitchChatOnTop.setHorizontalAlignment(JCheckBox.CENTER);
		twitchChatOnTop.setOpaque(false);
		twitchChatOnTop.setFocusable(false);
		
		twitchChatOnTop.addActionListener(l->{
			if(twitchChatOnTop.isSelected()) {
				twitchChatOnTop.setText(Constants.IS_TWITCH_CHAT_ON_TOP);
				TwitchChatFrame.getInstance().setAlwaysOnTop(true);
			}else {
				twitchChatOnTop.setText(Constants.IS_TWITCH_CHAT_ON_TOP);
				TwitchChatFrame.getInstance().setAlwaysOnTop(false);
				MainFrame.getInstance().toFront();
				MainFrame.getInstance().requestFocus();
			}
		});
		
		return twitchChatOnTop;
	}
	
	private JPanel twitchChatMode() {
		twitchChatMode = new JPanel(new FlowLayout());
		twitchChatMode.setOpaque(false);
		ButtonGroup group = new ButtonGroup();
		JRadioButton plain = new JRadioButton("All Mode");
		JRadioButton cmd = new JRadioButton("Cmd Only Mode");
		plain.setOpaque(false);
		plain.setFocusable(false);
		plain.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		plain.addActionListener(i->{
			TwitchChatFrame.getInstance().setColor(Color.BLACK, Color.WHITE);
		});
		cmd.setOpaque(false);
		cmd.setFocusable(false);
		cmd.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		cmd.addActionListener(i->{
			TwitchChatFrame.getInstance().setColor(Color.WHITE, Color.BLACK);
		});
		group.add(plain);
		group.add(cmd);
		twitchChatMode.add(plain);
		twitchChatMode.add(cmd);
		plain.setEnabled(false);
		cmd.setEnabled(false);
		return twitchChatMode;
	}
	
	private JPanel twitchChatOptionsPanel() {
		twitchChatOptionsPanel = new JPanel(new GridBagLayout());
		twitchChatOptionsPanel.setOpaque(false);
		twitchChatOptionsPanel.add(twitchChatColorModePanel);
		twitchChatOptionsPanel.setFocusable(false);
		return twitchChatOptionsPanel;
	}
	
	private void moveOptionPanel(boolean right) {
		
		List<JComponent> options = Arrays.asList(
				twitchChatColorModePanel,
				twitchChatMode,
				twitchChatFontPanel,
				twitchChatFontSizePanel,
				twitchChatOnTop);
		
		int index = options.indexOf(twitchChatOptionsPanel.getComponent(0));
		
		if(right) {
			index+=1;
			index = index>=options.size() ? 0 : index;
		}else {
			index-=1;
			index = index<0 ? options.size()-1 : index;
		}
		twitchChatOptionsPanel.removeAll();
		twitchChatOptionsPanel.add(options.get(index));
		
		twitchChatOptionsPanel.revalidate();
		twitchChatOptionsPanel.repaint();
	}
	
	private JPanel twitchChatFont() {
		twitchChatFontPanel = new JPanel(new FlowLayout());
		twitchChatFontPanel.setFocusable(false);
		twitchChatFontPanel.setOpaque(false);
		JLabel tmp = new JLabel("Font:");
		tmp.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		twitchChatFontPanel.add(tmp);
		twitchChatFont = new JComboBox<String>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
		twitchChatFont.setSelectedItem(TwitchChatFrame.getInstance().getCurrentFont().getName());
		twitchChatFont.addActionListener(l->{
			TwitchChatFrame.getInstance().setFont((String) twitchChatFont.getSelectedItem());
		});
		twitchChatFontPanel.add(twitchChatFont);
		return twitchChatFontPanel;		
	}
	
	private JPanel twitchChatFontSize() {
		twitchChatFontSizePanel = new JPanel(new FlowLayout());
		twitchChatFontSizePanel.setFocusable(false);
		twitchChatFontSizePanel.setOpaque(false);
		JLabel tmp = new JLabel("Font Size: (12)");
		tmp.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		twitchChatFontSizePanel.add(tmp);
		twitchChatFontSize = new JSlider(5, 50, 12);
		
		twitchChatFontSize.setMajorTickSpacing(45);
		twitchChatFontSize.setOpaque(false);
		twitchChatFontSize.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		twitchChatFontSize.addChangeListener(l->{
			tmp.setText(String.format("Font Size: (%s)", twitchChatFontSize.getValue()));
			TwitchChatFrame.getInstance().setFontSize(twitchChatFontSize.getValue());
		});
		
		twitchChatFontSizePanel.add(twitchChatFontSize);
		return twitchChatFontSizePanel;		
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
				label.setText("Before going any further, I wanna leave it clear that this app will use the account of the OAuth provided to send messages for me in chat.\nSo don't worry that the account is not being hacked.\nJust wanted to state to not provoke any worry or/and confusion.\nIf you want to do it but are scared of this, you can use a burner account the app will still work.");
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