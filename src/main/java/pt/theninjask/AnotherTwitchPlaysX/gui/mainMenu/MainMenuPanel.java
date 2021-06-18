package pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.TwitchChatFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.TwitchChatFrame.ChatMode;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.TwitchChatFrame.ChatType;
import pt.theninjask.AnotherTwitchPlaysX.gui.command.AllCommandPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.LoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.embedded.EmbeddedModMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.twitch.SponsorBot;
import pt.theninjask.AnotherTwitchPlaysX.twitch.TwitchPlayer;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.KeyPressedAdapter;

public class MainMenuPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static MainMenuPanel singleton = new MainMenuPanel();

	private JButton connectButton;

	private JButton commandsButton;

	private JButton modButton;

	private JButton gameButton;

	private JButton changeSessionButton;

	private JButton twitchChatButton;

	private List<JComponent> twitchChatOptions;
	
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

	private AtomicBoolean isAppStarted;

	private ATPXMod mod = null;

	private JPanel twitchChatTransparencyModePanel;

	private List<Runnable> eventsWithStart;
	
	private MainMenuPanel() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", MainMenuPanel.class.getSimpleName()));
		this.isAppStarted = new AtomicBoolean(false);
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
		twitchChatTransparencyModePanel();
		twitchChatOptions = new ArrayList<JComponent>(Arrays.asList(twitchChatColorModePanel, twitchChatMode, twitchChatFontPanel,
				twitchChatFontSizePanel, twitchChatTransparencyModePanel, twitchChatOnTop));
		this.add(twitchChatOptionsLabel());
		this.add(twitchChatOptionsPanel());
		this.add(twitchChatSlider());
		this.add(twitchChatSliderLabel(), this.getComponentCount() - 1);
		this.add(sponsorMeInChat());
		eventsWithStart = new CopyOnWriteArrayList<Runnable>();
	}

	public static MainMenuPanel getInstance() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.getInstance()", MainMenuPanel.class.getSimpleName()));
		if (singleton.shouldGameButtonBeEnabled()) {
			singleton.gameButton.setEnabled(true);
		} else {
			singleton.gameButton.setEnabled(false);
		}
		return singleton;
	}

	private JButton connectButton() {
		connectButton = new JButton("Connect");
		connectButton.setFocusable(false);
		connectButton.addActionListener(l -> {
			if (TwitchPlayer.getInstance().isConnected()) {
				sponsor.setEnabled(false);
				sponsor.setSelected(false);
				SponsorBot.getInstance().stop();
				TwitchPlayer.getInstance().disconnect();
				connectButton.setText("Connect");
				twitchChatButton.setEnabled(false);
				twitchChatButton.setText("Show Twitch Chat");
				gameButton.setEnabled(false);
				gameButton.setText("Start");
				changeSessionButton.setEnabled(true);
				TwitchChatFrame.getInstance().setVisible(false);
				TwitchChatFrame.getInstance().clearChat();
			} else {
				TwitchPlayer.getInstance().setupAndConnect();
				connectButton.setText("Disconnect");
				twitchChatButton.setEnabled(true);
				if (shouldGameButtonBeEnabled())
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
		commandsButton.addActionListener(l -> {
			MainFrame.replacePanel(AllCommandPanel.getInstance());
		});
		tmp.add(commandsButton, BorderLayout.CENTER);
		modButton = new JButton("Mod");
		modButton.setFocusable(false);
		//modButton.setEnabled(false);
		modButton.addActionListener(l -> {
			if(KeyPressedAdapter.isKeyPressed(KeyEvent.VK_SHIFT)) {
				MainFrame.replacePanel(EmbeddedModMenuPanel.getInstance());
				return;
			}
			if (mod == null) {
				try {
					File file = Constants.showOpenFile(new FileNameExtensionFilter("JAR", "jar"), this);
					mod = Constants.loadMod(file);	
					if(mod==null)
						return;
					mod.refresh();
					if(mod.getClass().getAnnotation(ATPXModProps.class).hasPanel())
						MainFrame.replacePanel(mod.getJPanelInstance());
					if(!mod.getClass().getAnnotation(ATPXModProps.class).keepLoaded())
						mod = null;
				} catch (Exception e) {
					Constants.showExpectedExceptionDialog(e);
					mod = null;
				}
			} else {
				mod.refresh();
				if(mod.getClass().getAnnotation(ATPXModProps.class).hasPanel())
					if(mod.getClass().getAnnotation(ATPXModProps.class).popout())
						new PopOutFrame(mod.getJPanelInstance());
					else
						MainFrame.replacePanel(mod.getJPanelInstance());
			}
		});
		tmp.add(modButton, BorderLayout.EAST);

		return tmp;
	}

	private boolean shouldGameButtonBeEnabled() {
		if (DataManager.getInstance().getCommands().isEmpty())
			return false;
		if (TwitchPlayer.getInstance().isConnected())
			return true;
		return false;
	}

	private JButton gameButton() {
		gameButton = new JButton("Start");
		gameButton.setFocusable(false);
		gameButton.setEnabled(false);
		gameButton.addActionListener(l -> {
			if (isAppStarted.get()) {
				try {
					GlobalScreen.unregisterNativeHook();
					for (CommandData elem : DataManager.getInstance().getCommands()) {
						TwitchPlayer.getInstance().unregisterEventListener(elem);
					}
					gameButton.setText("Start");
					commandsButton.setEnabled(true);
					connectButton.setEnabled(true);
					isAppStarted.set(false);
					eventsWithStart.forEach(a->{
						a.run();
					});
				} catch (NativeHookException nativeHookException) {
					Constants.showExceptionDialog(nativeHookException);
				}
			} else {
				try {
					gameButton.setText("Stop");
					commandsButton.setEnabled(false);
					connectButton.setEnabled(false);
					for (CommandData elem : DataManager.getInstance().getCommands()) {
						TwitchPlayer.getInstance().registerEventListener(elem);
					}
					GlobalScreen.registerNativeHook();
					GlobalScreen.addNativeKeyListener(new NativeKeyListener() {

						@Override
						public void nativeKeyTyped(NativeKeyEvent e) {
							// DO NOTHING
						}

						@Override
						public void nativeKeyReleased(NativeKeyEvent e) {
							// DO NOTHING
						}

						@Override
						public void nativeKeyPressed(NativeKeyEvent e) {
							if (e.getKeyCode() == Constants.stopKey) {
								try {
									GlobalScreen.unregisterNativeHook();
									for (CommandData elem : DataManager.getInstance().getCommands()) {
										TwitchPlayer.getInstance().unregisterEventListener(elem);
									}
									gameButton.setText("Start");
									commandsButton.setEnabled(true);
									connectButton.setEnabled(true);
									MainFrame.getInstance().setState(JFrame.NORMAL);
									isAppStarted.set(false);
								} catch (NativeHookException nativeHookException) {
									Constants.showExceptionDialog(nativeHookException);
								}
							}
						}
					});
					MainFrame.getInstance().setState(JFrame.ICONIFIED);
					isAppStarted.set(true);
					eventsWithStart.forEach(a->{
						a.run();
					});
				} catch (NativeHookException e) {
					Constants.showExceptionDialog(e);
				}

			}
		});
		return gameButton;
	}

	private JButton changeSessionButton() {
		changeSessionButton = new JButton("Change Session");
		changeSessionButton.setFocusable(false);
		changeSessionButton.addActionListener(l -> {
			MainFrame.replacePanel(LoginPanel.getInstance());
		});
		return changeSessionButton;
	}

	private JButton twitchChatButton() {
		twitchChatButton = new JButton("Show Twitch Chat");
		twitchChatButton.setFocusable(false);
		twitchChatButton.setEnabled(false);
		twitchChatButton.addActionListener(l -> {
			if (TwitchChatFrame.getInstance().isVisible()) {
				TwitchChatFrame.getInstance().setVisible(false);
				twitchChatButton.setText("Show Twitch Chat");
				TwitchChatFrame.getInstance().clearChat();
				TwitchPlayer.getInstance().unregisterEventListener(TwitchChatFrame.getInstance());
			} else {
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
		twitchChatSize = new JSlider(TwitchChatFrame.MSG_DISPLAY_MIN, TwitchChatFrame.MSG_DISPLAY_INFINITE, 5);
		twitchChatSize.setMajorTickSpacing(45);
		twitchChatSize.setPaintLabels(true);
		twitchChatSize.setOpaque(false);
		// twitchChatSize.setFocusable(false);
		twitchChatSize.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		return twitchChatSize;
	}

	private JLabel twitchChatSliderLabel() {
		JLabel label = new JLabel();
		label.setText(String.format(Constants.CURRENT_CHAT_SIZE, twitchChatSize.getValue()));
		label.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		label.setFocusable(false);
		twitchChatSize.addChangeListener(e -> {
			int value = twitchChatSize.getValue();
			label.setText(String.format(Constants.CURRENT_CHAT_SIZE,
					value < TwitchChatFrame.MSG_DISPLAY_INFINITE ? value : "Infinite"));
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
		// left.setHorizontalAlignment(JButton.LEFT);
		left.addActionListener(l -> {
			moveOptionPanel(false);
		});
		tmp.add(left, BorderLayout.WEST);
		tmp.add(label, BorderLayout.CENTER);
		JButton right = new JButton(">");
		right.setFocusable(false);
		// right.setHorizontalAlignment(JButton.RIGHT);
		right.addActionListener(l -> {
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
		light.addActionListener(i -> {
			TwitchChatFrame.getInstance().setColor(Color.BLACK, Color.WHITE);
		});
		night.setOpaque(false);
		night.setFocusable(false);
		night.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		night.addActionListener(i -> {
			TwitchChatFrame.getInstance().setColor(Color.WHITE, Color.BLACK);
		});
		twitch.setOpaque(false);
		twitch.setFocusable(false);
		twitch.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		twitch.setSelected(true);
		twitch.addActionListener(i -> {
			TwitchChatFrame.getInstance().setColor(Constants.TWITCH_COLOR_COMPLEMENT, Constants.TWITCH_COLOR);
		});
		group.add(light);
		group.add(night);
		group.add(twitch);
		twitchChatColorModePanel.add(light);
		twitchChatColorModePanel.add(night);
		twitchChatColorModePanel.add(twitch);
		return twitchChatColorModePanel;
	}
	
	private JPanel twitchChatTransparencyModePanel() {
		twitchChatTransparencyModePanel = new JPanel(new FlowLayout());
		twitchChatTransparencyModePanel.setOpaque(false);
		ButtonGroup group = new ButtonGroup();
		JRadioButton transp = new JRadioButton("Transparent");
		JRadioButton semisolid = new JRadioButton("Semi-Solid");
		JRadioButton solid = new JRadioButton("Solid");
		transp.setOpaque(false);
		transp.setFocusable(false);
		transp.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		transp.addActionListener(i -> {
			TwitchChatFrame.getInstance().setChatMode(ChatMode.TRANSPARENT);
		});
		semisolid.setOpaque(false);
		semisolid.setFocusable(false);
		semisolid.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		semisolid.addActionListener(i -> {
			TwitchChatFrame.getInstance().setChatMode(ChatMode.SEMI_SOLID);
		});
		solid.setOpaque(false);
		solid.setFocusable(false);
		solid.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		solid.setSelected(true);
		solid.addActionListener(i -> {
			TwitchChatFrame.getInstance().setChatMode(ChatMode.SOLID);
		});
		group.add(transp);
		group.add(semisolid);
		group.add(solid);
		twitchChatTransparencyModePanel.add(transp);
		twitchChatTransparencyModePanel.add(semisolid);
		twitchChatTransparencyModePanel.add(solid);
		return twitchChatTransparencyModePanel;
	}

	private JCheckBox setTwitchChatOnTop() {
		twitchChatOnTop = new JCheckBox();

		twitchChatOnTop.setText(Constants.IS_TWITCH_CHAT_ON_TOP);
		twitchChatOnTop.setHorizontalTextPosition(JCheckBox.LEFT);
		twitchChatOnTop.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		twitchChatOnTop.setHorizontalAlignment(JCheckBox.CENTER);
		twitchChatOnTop.setOpaque(false);
		twitchChatOnTop.setFocusable(false);
		twitchChatOnTop.setText(Constants.IS_TWITCH_CHAT_ON_TOP);
		twitchChatOnTop.addActionListener(l -> {
			if (twitchChatOnTop.isSelected()) {
				TwitchChatFrame.getInstance().setAlwaysOnTop(true);
				TwitchChatFrame.getInstance().disableDragAndResize();
			} else {
				twitchChatOnTop.setText(Constants.IS_TWITCH_CHAT_ON_TOP);
				TwitchChatFrame.getInstance().setAlwaysOnTop(false);
				TwitchChatFrame.getInstance().enableDragAndResize();
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
		JRadioButton mine = new JRadioButton(ChatType.MINECRAFT.getType());
		mine.setOpaque(false);
		mine.setFocusable(false);
		mine.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		mine.addActionListener(i -> {
			TwitchChatFrame.getInstance().setChatType(ChatType.MINECRAFT);
		});
		JRadioButton twitch = new JRadioButton(ChatType.TWITCH.getType());
		twitch.setOpaque(false);
		twitch.setFocusable(false);
		twitch.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		twitch.addActionListener(i -> {
			TwitchChatFrame.getInstance().setChatType(ChatType.TWITCH);
		});
		mine.setSelected(true);
		group.add(mine);
		group.add(twitch);
		twitchChatMode.add(mine);
		twitchChatMode.add(twitch);
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
		int index = twitchChatOptions.indexOf(twitchChatOptionsPanel.getComponent(0));

		if (right) {
			index += 1;
			index = index >= twitchChatOptions.size() ? 0 : index;
		} else {
			index -= 1;
			index = index < 0 ? twitchChatOptions.size() - 1 : index;
		}
		twitchChatOptionsPanel.removeAll();
		if(index != -1)
			twitchChatOptionsPanel.add(twitchChatOptions.get(index));

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
		twitchChatFont = new JComboBox<String>(
				GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
		twitchChatFont.setSelectedItem(TwitchChatFrame.getInstance().getCurrentFont().getName());
		twitchChatFont.addActionListener(l -> {
			TwitchChatFrame.getInstance().setFont((String) twitchChatFont.getSelectedItem());
		});
		twitchChatFontPanel.add(twitchChatFont);
		return twitchChatFontPanel;
	}

	private JPanel twitchChatFontSize() {
		twitchChatFontSizePanel = new JPanel(new BorderLayout());
		twitchChatFontSizePanel.setFocusable(false);
		twitchChatFontSizePanel.setOpaque(false);
		JLabel tmp = new JLabel("Font Size: (12)");
		tmp.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		twitchChatFontSizePanel.add(tmp, BorderLayout.WEST);
		twitchChatFontSize = new JSlider(5, 50, 12);

		twitchChatFontSize.setMajorTickSpacing(45);
		twitchChatFontSize.setOpaque(false);
		twitchChatFontSize.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		twitchChatFontSize.addChangeListener(l -> {
			tmp.setText(String.format("Font Size: (%s)", twitchChatFontSize.getValue()));
			TwitchChatFrame.getInstance().setFontSize(twitchChatFontSize.getValue());
		});

		twitchChatFontSizePanel.add(twitchChatFontSize, BorderLayout.CENTER);
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
		sponsor.addActionListener(l -> {
			if (sponsor.isSelected()) {
				String[] options = { "Got it", "Nevermind" };
				JTextArea label = new JTextArea();
				label.setText(
						"Before going any further, I wanna leave it clear that this app will use the account of the OAuth provided to send messages for me in chat.\nSo don't worry that the account is not being hacked.\nJust wanted to state to not provoke any worry or/and confusion.\nIf you want to do it but are scared of this, you can use a burner account the app will still work.");
				label.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				label.setFocusable(false);
				label.setOpaque(false);
				int resp = Constants.showCustomColorOptionDialog(null, label, "Thank you for wanting to sponsor me!",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, null,
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
				String[] options2 = { "Yes and not check message", "Yes and check message", "Nevermind" };
				resp = Constants.showCustomColorOptionDialog(null, sponsorPanel(),
						"Thank you for wanting to sponsor me!", JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, options2, null, Constants.TWITCH_COLOR);
				switch (resp) {
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
				resp = Constants.showCustomColorOptionDialog(null, label, "Thank you for wanting to sponsor me!",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null,
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
			} else {
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
		sponsorSlider.addChangeListener(e -> {
			int value = sponsorSlider.getValue();
			label.setText(String.format("Sponsor me every %s min.", value));
			// TwitchChatFrame.getInstance().setMessageCap(value);
			// TwitchChatFrame.getInstance().updateChatSize();
		});
		label.setHorizontalAlignment(JLabel.CENTER);
		tmp.add(label);
		tmp.add(sponsorSlider);
		tmp.setFocusable(false);
		tmp.setBackground(Constants.TWITCH_COLOR);
		return tmp;
	}

	public JButton getConnectButton() {
		return connectButton;
	}

	public JButton getCommandsButton() {
		return commandsButton;
	}

	public JButton getModButton() {
		return modButton;
	}

	public JButton getGameButton() {
		return gameButton;
	}

	public JButton getChangeSessionButton() {
		return changeSessionButton;
	}

	public JButton getTwitchChatButton() {
		return twitchChatButton;
	}

	public List<JComponent> getTwitchChatOptions() {
		return twitchChatOptions;
	}

	public AtomicBoolean getIsAppStarted() {
		return isAppStarted;
	}

	public ATPXMod getMod() {
		return mod;
	}

	public void setMod(ATPXMod mod) {
		this.mod = mod;
	}
	
	/**
	 * This will ensure this is the last this ran after pressing the button
	 */
	public void attachEventListenerToGameButton(Runnable action) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.attachEventListenerToGameButton(Runnable)", MainMenuPanel.class.getSimpleName()));
		eventsWithStart.add(action);
	}

	public boolean dettachEventListenerToGameButton(Runnable action) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.dettachEventListenerToGameButton(Runnable)", MainMenuPanel.class.getSimpleName()));
		return eventsWithStart.remove(action);
	}
}
