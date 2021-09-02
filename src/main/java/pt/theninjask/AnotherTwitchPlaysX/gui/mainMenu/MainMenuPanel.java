package pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.ColorThemeUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.LanguageUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.mainMenu.GameButtonClickEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.mainMenu.ModButtonClickEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.ChatFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.ChatFrame.ChatMode;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.ChatFrame.ChatType;
import pt.theninjask.AnotherTwitchPlaysX.gui.command.AllCommandPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.MainLoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModManager;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.embedded.EmbeddedModMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.stream.SponsorBot;
import pt.theninjask.AnotherTwitchPlaysX.stream.twitch.TwitchPlayer;
import pt.theninjask.AnotherTwitchPlaysX.stream.youtube.YouTubePlayer;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.ExternalConsole;
import pt.theninjask.AnotherTwitchPlaysX.util.KeyPressedAdapter;
import pt.theninjask.AnotherTwitchPlaysX.util.RedirectorErrorOutputStream;
import pt.theninjask.AnotherTwitchPlaysX.util.RedirectorInputStream;
import pt.theninjask.AnotherTwitchPlaysX.util.RedirectorOutputStream;

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

	private AtomicBoolean inConnection;

	private ATPXMod mod = null;

	private JPanel twitchChatTransparencyModePanel;

	// private List<Runnable> eventsWithStart;

	private JCheckBox twitchChatInput;

	private JLabel twitchChatSliderLabel;

	private JLabel twitchChatOptionsLabel;

	private JRadioButton light;

	private JRadioButton night;

	private JRadioButton twitch;

	private JRadioButton transp;

	private JRadioButton semisolid;

	private JRadioButton solid;

	private JLabel twitchChatFontLabel;

	private JLabel twitchChatFontSizeLabel;

	private JRadioButton mineRadio;

	private JRadioButton twitchRadio;

	private MainMenuPanel() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", MainMenuPanel.class.getSimpleName()));
		this.isAppStarted = new AtomicBoolean(false);
		this.inConnection = new AtomicBoolean(false);
		this.setBackground(Constants.TWITCH_THEME.getBackground());
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
		setTwitchChatInput();
		twitchChatOptions = new ArrayList<JComponent>(
				Arrays.asList(twitchChatColorModePanel, twitchChatMode, twitchChatFontPanel, twitchChatFontSizePanel,
						twitchChatTransparencyModePanel, twitchChatInput, twitchChatOnTop));
		this.add(twitchChatOptionsLabel());
		this.add(twitchChatOptionsPanel());
		this.add(twitchChatSlider());
		this.add(twitchChatSliderLabel(), this.getComponentCount() - 1);
		this.add(sponsorMeInChat());
		// eventsWithStart = new CopyOnWriteArrayList<Runnable>();
		// DataManager.registerLangEvent(this);
		EventManager.registerEventListener(this);
	}

	public static MainMenuPanel getInstance() {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.getInstance()", MainMenuPanel.class.getSimpleName()));
		if (singleton.shouldGameButtonBeEnabled()) {
			singleton.gameButton.setEnabled(true);
		} else {
			singleton.gameButton.setEnabled(false);
		}
		return singleton;
	}

	private JButton connectButton() {
		connectButton = new JButton(DataManager.getLanguage().getMainMenu().getConnect());
		connectButton.setFocusable(false);
		connectButton.addActionListener(l -> {
			if (inConnection.get()) {
				sponsor.setEnabled(false);
				sponsor.setSelected(false);
				SponsorBot.getInstance().stop();
				if (TwitchPlayer.getInstance().isConnected())
					TwitchPlayer.getInstance().disconnect();
				if (YouTubePlayer.getInstance().isConnected())
					YouTubePlayer.getInstance().disconnect();
				connectButton.setText(DataManager.getLanguage().getMainMenu().getConnect());
				twitchChatButton.setEnabled(false);
				twitchChatButton.setText(DataManager.getLanguage().getMainMenu().getShowTwitchChat());
				gameButton.setEnabled(false);
				gameButton.setText(DataManager.getLanguage().getMainMenu().getStart());
				changeSessionButton.setEnabled(true);
				ChatFrame.getInstance().setVisible(false);
				ChatFrame.getInstance().clearChat();
				inConnection.set(false);
			} else {
				if (TwitchPlayer.getInstance().hasRequired() && !TwitchPlayer.getInstance().isConnected())
					TwitchPlayer.getInstance().setupAndConnect();
				if (YouTubePlayer.getInstance().hasRequired() && !YouTubePlayer.getInstance().isConnected())
					YouTubePlayer.getInstance().setupAndConnect();
				connectButton.setText(DataManager.getLanguage().getMainMenu().getDisconnect());
				twitchChatButton.setEnabled(true);
				if (shouldGameButtonBeEnabled())
					gameButton.setEnabled(true);
				changeSessionButton.setEnabled(false);
				sponsor.setEnabled(true);
				inConnection.set(true);
			}
		});
		return connectButton;
	}

	private JPanel commandsButton() {
		JPanel tmp = new JPanel(new BorderLayout());
		commandsButton = new JButton(DataManager.getLanguage().getMainMenu().getSetCommands());
		commandsButton.setFocusable(false);
		commandsButton.addActionListener(l -> {
			boolean changeConsole = KeyPressedAdapter.isKeyPressed(KeyEvent.VK_SHIFT);
			if (changeConsole) {
				if (ExternalConsole.isViewable()) {
					RedirectorOutputStream.changeRedirectToDefault();
					RedirectorErrorOutputStream.changeRedirectToDefault();
					RedirectorInputStream.changeRedirectToDefault();
					ExternalConsole.setViewable(false);
				} else {
					RedirectorOutputStream.changeRedirect(ExternalConsole.getExternalConsoleOutputStream());
					RedirectorErrorOutputStream.changeRedirect(ExternalConsole.getExternalConsoleErrorOutputStream());
					RedirectorInputStream.changeRedirect(ExternalConsole.getExternalConsoleInputStream());
					ExternalConsole.setViewable(true);
				}
				return;
			}
			MainFrame.replacePanel(AllCommandPanel.getInstance());
		});
		tmp.add(commandsButton, BorderLayout.CENTER);
		modButton = new JButton(DataManager.getLanguage().getMainMenu().getMod());
		modButton.setFocusable(false);
		// modButton.setEnabled(false);
		modButton.addActionListener(l -> {
			boolean secretMenu = KeyPressedAdapter.isKeyPressed(KeyEvent.VK_SHIFT);
			ModButtonClickEvent event = new ModButtonClickEvent(modButton, secretMenu);
			EventManager.triggerEvent(event);
			if (event.isCancelled())
				return;
			if (secretMenu) {
				MainFrame.replacePanel(EmbeddedModMenuPanel.getInstance());
				return;
			}
			if (mod == null) {
				try {
					File file = Constants.showOpenFile(new FileNameExtensionFilter("JAR", "jar"), this,
							Paths.get(Constants.SAVE_PATH, Constants.MOD_FOLDER).toFile());
					mod = Constants.loadMod(file);
					if (mod == null)
						return;
					mod.refresh();
					if (mod.getClass().getAnnotation(ATPXModProps.class).hasPanel())
						if (mod.getClass().getAnnotation(ATPXModProps.class).popout())
							new PopOutFrame(mod.getJPanelInstance()).setVisible(true);
						else
							MainFrame.replacePanel(mod.getJPanelInstance());
					if (mod.getClass().getAnnotation(ATPXModProps.class).keepLoaded()) {
						ATPXModManager.addMod(mod);
						if (!mod.getClass().getAnnotation(ATPXModProps.class).hasPanel())
							mod = null;
					} else
						mod = null;
				} catch (Exception e) {
					Constants.showExpectedExceptionDialog(e);
					mod = null;
				}
			} else {
				mod.refresh();
				if (mod.getClass().getAnnotation(ATPXModProps.class).hasPanel())
					if (mod.getClass().getAnnotation(ATPXModProps.class).popout())
						new PopOutFrame(mod.getJPanelInstance()).setVisible(true);
					else
						MainFrame.replacePanel(mod.getJPanelInstance());
			}
		});
		tmp.add(modButton, BorderLayout.EAST);

		return tmp;
	}

	private boolean shouldGameButtonBeEnabled() {
		if (DataManager.getCommands().isEmpty())
			return false;
		if (inConnection.get())
			return true;
		return false;
	}

	private JButton gameButton() {
		gameButton = new JButton(DataManager.getLanguage().getMainMenu().getStart());
		gameButton.setFocusable(false);
		gameButton.setEnabled(false);
		gameButton.addActionListener(l -> {
			GameButtonClickEvent event = new GameButtonClickEvent(gameButton, isAppStarted.get());
			EventManager.triggerEvent(event);
			if (event.isCancelled())
				return;
			if (isAppStarted.get()) {
				try {
					GlobalScreen.unregisterNativeHook();
					for (CommandData elem : DataManager.getCommands()) {
						if (TwitchPlayer.getInstance().isSetup())
							TwitchPlayer.getInstance().unregisterEventListener(elem);
						if (YouTubePlayer.getInstance().isSetup())
							YouTubePlayer.getInstance().unregisterEventListener(elem);
					}
					gameButton.setText(DataManager.getLanguage().getMainMenu().getStart());
					commandsButton.setEnabled(true);
					connectButton.setEnabled(true);
					isAppStarted.set(false);
				} catch (NativeHookException nativeHookException) {
					Constants.showExceptionDialog(nativeHookException);
				}
			} else {
				try {
					gameButton.setText(DataManager.getLanguage().getMainMenu().getStop());
					commandsButton.setEnabled(false);
					connectButton.setEnabled(false);
					for (CommandData elem : DataManager.getCommands()) {
						if (TwitchPlayer.getInstance().isSetup())
							TwitchPlayer.getInstance().registerEventListener(elem);
						if (YouTubePlayer.getInstance().isSetup())
							YouTubePlayer.getInstance().registerEventListener(elem);
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
									for (CommandData elem : DataManager.getCommands()) {
										if (TwitchPlayer.getInstance().isSetup())
											TwitchPlayer.getInstance().unregisterEventListener(elem);
										if (YouTubePlayer.getInstance().isSetup())
											YouTubePlayer.getInstance().unregisterEventListener(elem);
									}
									gameButton.setText(DataManager.getLanguage().getMainMenu().getStart());
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
				} catch (NativeHookException e) {
					Constants.showExceptionDialog(e);
				}

			}
		});
		return gameButton;
	}

	private JButton changeSessionButton() {
		changeSessionButton = new JButton(DataManager.getLanguage().getMainMenu().getChangeSession());
		changeSessionButton.setFocusable(false);
		changeSessionButton.addActionListener(l -> {
			// MainFrame.replacePanel(LoginPanel.getInstance());
			MainFrame.replacePanel(MainLoginPanel.getInstance());
		});
		return changeSessionButton;
	}

	private JButton twitchChatButton() {
		twitchChatButton = new JButton(DataManager.getLanguage().getMainMenu().getShowTwitchChat());
		twitchChatButton.setFocusable(false);
		twitchChatButton.setEnabled(false);
		twitchChatButton.addActionListener(l -> {
			if (ChatFrame.getInstance().isVisible()) {
				ChatFrame.getInstance().setVisible(false);
				twitchChatButton.setText(DataManager.getLanguage().getMainMenu().getShowTwitchChat());
				ChatFrame.getInstance().clearChat();
				if (TwitchPlayer.getInstance().isSetup())
					TwitchPlayer.getInstance().unregisterEventListener(ChatFrame.getInstance());
				if (YouTubePlayer.getInstance().isSetup())
					YouTubePlayer.getInstance().unregisterEventListener(ChatFrame.getInstance());
			} else {
				ChatFrame.getInstance().setVisible(true);
				twitchChatButton.setText(DataManager.getLanguage().getMainMenu().getHideTwitchChat());
				if (TwitchPlayer.getInstance().isSetup())
					TwitchPlayer.getInstance().registerEventListener(ChatFrame.getInstance());
				if (YouTubePlayer.getInstance().isSetup())
					YouTubePlayer.getInstance().registerEventListener(ChatFrame.getInstance());
			}
		});
		ChatFrame.getInstance().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				twitchChatButton.setText(DataManager.getLanguage().getMainMenu().getShowTwitchChat());
				ChatFrame.getInstance().clearChat();
				if (TwitchPlayer.getInstance().isSetup())
					TwitchPlayer.getInstance().unregisterEventListener(ChatFrame.getInstance());
				if (YouTubePlayer.getInstance().isSetup())
					YouTubePlayer.getInstance().unregisterEventListener(ChatFrame.getInstance());
			}
		});
		return twitchChatButton;
	}

	private JSlider twitchChatSlider() {
		twitchChatSize = new JSlider(ChatFrame.MSG_DISPLAY_MIN, ChatFrame.MSG_DISPLAY_INFINITE, 5);
		twitchChatSize.setMajorTickSpacing(45);
		twitchChatSize.setPaintLabels(true);
		twitchChatSize.setOpaque(false);
		// twitchChatSize.setFocusable(false);
		twitchChatSize.setForeground(DataManager.getTheme().getFont());
		return twitchChatSize;
	}

	private JLabel twitchChatSliderLabel() {
		twitchChatSliderLabel = new JLabel(
				String.format(DataManager.getLanguage().getMainMenu().getCurrentChatSize(), twitchChatSize.getValue()));
		twitchChatSliderLabel.setForeground(DataManager.getTheme().getFont());
		twitchChatSliderLabel.setFocusable(false);
		twitchChatSize.addChangeListener(e -> {
			int value = twitchChatSize.getValue();
			twitchChatSliderLabel.setText(String.format(DataManager.getLanguage().getMainMenu().getCurrentChatSize(),
					value < ChatFrame.MSG_DISPLAY_INFINITE ? value
							: DataManager.getLanguage().getMainMenu().getInfinite()));
			ChatFrame.getInstance().setMessageCap(value);
			ChatFrame.getInstance().updateChatSize();
		});
		twitchChatSliderLabel.setHorizontalAlignment(JLabel.CENTER);
		return twitchChatSliderLabel;
	}

	private JPanel twitchChatOptionsLabel() {
		JPanel tmp = new JPanel(new BorderLayout());
		tmp.setOpaque(false);
		tmp.setFocusable(false);
		twitchChatOptionsLabel = new JLabel(DataManager.getLanguage().getMainMenu().getTwitchChatOptions());
		twitchChatOptionsLabel.setForeground(DataManager.getTheme().getFont());
		twitchChatOptionsLabel.setHorizontalAlignment(JLabel.CENTER);
		twitchChatOptionsLabel.setFocusable(false);

		JButton left = new JButton("<");
		left.setFocusable(false);
		// left.setHorizontalAlignment(JButton.LEFT);
		left.addActionListener(l -> {
			moveOptionPanel(false);
		});
		tmp.add(left, BorderLayout.WEST);
		tmp.add(twitchChatOptionsLabel, BorderLayout.CENTER);
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
		light = new JRadioButton(DataManager.getLanguage().getMainMenu().getLightMode());
		night = new JRadioButton(DataManager.getLanguage().getMainMenu().getNightMode());
		twitch = new JRadioButton(DataManager.getLanguage().getMainMenu().getTwitchMode());
		light.setOpaque(false);
		light.setFocusable(false);
		light.setForeground(DataManager.getTheme().getFont());
		light.addActionListener(i -> {
			ChatFrame.getInstance().setColor(Constants.DAY_THEME.getFont(), Constants.DAY_THEME.getBackground());
		});
		night.setOpaque(false);
		night.setFocusable(false);
		night.setForeground(DataManager.getTheme().getFont());
		night.addActionListener(i -> {
			ChatFrame.getInstance().setColor(Constants.NIGHT_THEME.getFont(), Constants.NIGHT_THEME.getBackground());
		});
		twitch.setOpaque(false);
		twitch.setFocusable(false);
		twitch.setForeground(DataManager.getTheme().getFont());
		twitch.setSelected(true);
		twitch.addActionListener(i -> {
			ChatFrame.getInstance().setColor(Constants.TWITCH_THEME.getFont(), Constants.TWITCH_THEME.getBackground());
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
		transp = new JRadioButton(DataManager.getLanguage().getMainMenu().getTransparent());
		semisolid = new JRadioButton(DataManager.getLanguage().getMainMenu().getSemiSolid());
		solid = new JRadioButton(DataManager.getLanguage().getMainMenu().getSolid());
		transp.setOpaque(false);
		transp.setFocusable(false);
		transp.setForeground(DataManager.getTheme().getFont());
		transp.addActionListener(i -> {
			ChatFrame.getInstance().setChatMode(ChatMode.TRANSPARENT);
		});
		semisolid.setOpaque(false);
		semisolid.setFocusable(false);
		semisolid.setForeground(DataManager.getTheme().getFont());
		semisolid.addActionListener(i -> {
			ChatFrame.getInstance().setChatMode(ChatMode.SEMI_SOLID);
		});
		solid.setOpaque(false);
		solid.setFocusable(false);
		solid.setForeground(DataManager.getTheme().getFont());
		solid.setSelected(true);
		solid.addActionListener(i -> {
			ChatFrame.getInstance().setChatMode(ChatMode.SOLID);
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

		twitchChatOnTop.setText(DataManager.getLanguage().getMainMenu().getIsTwitchChatOnTop());
		twitchChatOnTop.setHorizontalTextPosition(JCheckBox.LEFT);
		twitchChatOnTop.setForeground(DataManager.getTheme().getFont());
		twitchChatOnTop.setHorizontalAlignment(JCheckBox.CENTER);
		twitchChatOnTop.setOpaque(false);
		twitchChatOnTop.setFocusable(false);
		twitchChatOnTop.setText(DataManager.getLanguage().getMainMenu().getIsTwitchChatOnTop());
		twitchChatOnTop.addActionListener(l -> {
			if (twitchChatOnTop.isSelected()) {
				ChatFrame.getInstance().setAlwaysOnTop(true);
				ChatFrame.getInstance().disableDragAndResize();
			} else {
				twitchChatOnTop.setText(DataManager.getLanguage().getMainMenu().getIsTwitchChatOnTop());
				ChatFrame.getInstance().setAlwaysOnTop(false);
				ChatFrame.getInstance().enableDragAndResize();
				MainFrame.getInstance().toFront();
				MainFrame.getInstance().requestFocus();
			}
		});

		return twitchChatOnTop;
	}

	private JCheckBox setTwitchChatInput() {
		twitchChatInput = new JCheckBox();

		twitchChatInput.setText(DataManager.getLanguage().getMainMenu().getShowInputTextBoxInChat());
		twitchChatInput.setHorizontalTextPosition(JCheckBox.LEFT);
		twitchChatInput.setForeground(DataManager.getTheme().getFont());
		twitchChatInput.setHorizontalAlignment(JCheckBox.CENTER);
		twitchChatInput.setOpaque(false);
		twitchChatInput.setFocusable(false);
		twitchChatInput.addActionListener(l -> {
			ChatFrame.getInstance().showInputMessage(twitchChatInput.isSelected());
		});

		return twitchChatInput;
	}

	private JPanel twitchChatMode() {
		twitchChatMode = new JPanel(new FlowLayout());
		twitchChatMode.setOpaque(false);
		ButtonGroup group = new ButtonGroup();
		mineRadio = new JRadioButton(ChatType.MINECRAFT.getType());
		mineRadio.setOpaque(false);
		mineRadio.setFocusable(false);
		mineRadio.setForeground(DataManager.getTheme().getFont());
		mineRadio.addActionListener(i -> {
			ChatFrame.getInstance().setChatType(ChatType.MINECRAFT);
		});
		twitchRadio = new JRadioButton(ChatType.TWITCH.getType());
		twitchRadio.setOpaque(false);
		twitchRadio.setFocusable(false);
		twitchRadio.setForeground(DataManager.getTheme().getFont());
		twitchRadio.addActionListener(i -> {
			ChatFrame.getInstance().setChatType(ChatType.TWITCH);
		});
		mineRadio.setSelected(true);
		group.add(mineRadio);
		group.add(twitchRadio);
		twitchChatMode.add(mineRadio);
		twitchChatMode.add(twitchRadio);
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
		if (index != -1)
			twitchChatOptionsPanel.add(twitchChatOptions.get(index));

		twitchChatOptionsPanel.revalidate();
		twitchChatOptionsPanel.repaint();
	}

	private JPanel twitchChatFont() {
		twitchChatFontPanel = new JPanel(new FlowLayout());
		twitchChatFontPanel.setFocusable(false);
		twitchChatFontPanel.setOpaque(false);
		twitchChatFontLabel = new JLabel(DataManager.getLanguage().getMainMenu().getFont());
		twitchChatFontLabel.setForeground(DataManager.getTheme().getFont());
		twitchChatFontPanel.add(twitchChatFontLabel);
		twitchChatFont = new JComboBox<String>(
				GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
		twitchChatFont.setSelectedItem(ChatFrame.getInstance().getCurrentFont().getName());
		twitchChatFont.addActionListener(l -> {
			ChatFrame.getInstance().setFont((String) twitchChatFont.getSelectedItem());
		});
		twitchChatFontPanel.add(twitchChatFont);
		return twitchChatFontPanel;
	}

	private JPanel twitchChatFontSize() {
		twitchChatFontSizePanel = new JPanel(new BorderLayout());
		twitchChatFontSizePanel.setFocusable(false);
		twitchChatFontSizePanel.setOpaque(false);
		twitchChatFontSize = new JSlider(5, 50, 12);

		twitchChatFontSizeLabel = new JLabel(
				String.format(DataManager.getLanguage().getMainMenu().getFontSize(), twitchChatFontSize.getValue()));
		twitchChatFontSizeLabel.setForeground(DataManager.getTheme().getFont());
		twitchChatFontSizePanel.add(twitchChatFontSizeLabel, BorderLayout.WEST);

		twitchChatFontSize.setMajorTickSpacing(45);
		twitchChatFontSize.setOpaque(false);
		twitchChatFontSize.setForeground(DataManager.getTheme().getFont());
		twitchChatFontSize.addChangeListener(l -> {
			twitchChatFontSizeLabel.setText(String.format(DataManager.getLanguage().getMainMenu().getFontSize(),
					twitchChatFontSize.getValue()));
			ChatFrame.getInstance().setFontSize(twitchChatFontSize.getValue());
		});

		twitchChatFontSizePanel.add(twitchChatFontSize, BorderLayout.CENTER);
		return twitchChatFontSizePanel;
	}

	private JCheckBox sponsorMeInChat() {
		sponsor = new JCheckBox();
		sponsor.setText(DataManager.getLanguage().getMainMenu().getSponsorCreator());
		sponsor.setHorizontalTextPosition(JCheckBox.LEFT);
		sponsor.setForeground(DataManager.getTheme().getFont());
		sponsor.setHorizontalAlignment(JCheckBox.CENTER);
		sponsor.setOpaque(false);
		sponsor.setFocusable(false);
		sponsor.setEnabled(false);
		sponsor.addActionListener(l -> {
			if (sponsor.isSelected()) {
				String[] options = { DataManager.getLanguage().getMainMenu().getGotIt(),
						DataManager.getLanguage().getMainMenu().getNevermind() };
				JTextArea label = new JTextArea();
				label.setText(DataManager.getLanguage().getMainMenu().getSponsorMsgFirst());
				label.setForeground(DataManager.getTheme().getFont());
				label.setFocusable(false);
				label.setOpaque(false);
				int resp = Constants.showCustomColorOptionDialog(null, label,
						DataManager.getLanguage().getMainMenu().getSponsorTitle(), JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE, null, options, null, DataManager.getTheme().getBackground());
				switch (resp) {
				case JOptionPane.OK_OPTION:
					break;
				case JOptionPane.CANCEL_OPTION:
				case JOptionPane.CLOSED_OPTION:
				default:
					sponsor.setSelected(false);
					return;
				}
				String[] options2 = { DataManager.getLanguage().getMainMenu().getYesNoCheck(),
						DataManager.getLanguage().getMainMenu().getYesCheck(),
						DataManager.getLanguage().getMainMenu().getNevermind() };
				resp = Constants.showCustomColorOptionDialog(null, sponsorPanel(),
						DataManager.getLanguage().getMainMenu().getSponsorTitle(), JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, options2, null, DataManager.getTheme().getBackground());
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
				label.setText(String.format(DataManager.getLanguage().getMainMenu().getSponsorMsgDemo(),
						SponsorBot.getSponsorMsg()));
				label.setForeground(DataManager.getTheme().getFont());
				label.setFocusable(false);
				label.setOpaque(false);
				resp = Constants.showCustomColorOptionDialog(null, label,
						DataManager.getLanguage().getMainMenu().getSponsorTitle(), JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, options, null, DataManager.getTheme().getBackground());
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
		sponsorSlider.setForeground(DataManager.getTheme().getFont());

		JLabel label = new JLabel();
		label.setText(
				String.format(DataManager.getLanguage().getMainMenu().getSponsorMsgTime(), sponsorSlider.getValue()));
		label.setForeground(DataManager.getTheme().getFont());
		label.setFocusable(false);
		sponsorSlider.addChangeListener(e -> {
			int value = sponsorSlider.getValue();
			label.setText(String.format(DataManager.getLanguage().getMainMenu().getSponsorMsgTime(), value));
			// TwitchChatFrame.getInstance().setMessageCap(value);
			// TwitchChatFrame.getInstance().updateChatSize();
		});
		label.setHorizontalAlignment(JLabel.CENTER);
		tmp.add(label);
		tmp.add(sponsorSlider);
		tmp.setFocusable(false);
		tmp.setBackground(DataManager.getTheme().getBackground());
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

	// @Handler
	public void updateLang(LanguageUpdateEvent event) {
		Lang session = event.getLanguage();
		if (session == null)
			return;
		if (inConnection.get())
			connectButton.setText(session.getMainMenu().getDisconnect());
		else
			connectButton.setText(session.getMainMenu().getConnect());

		commandsButton.setText(session.getMainMenu().getSetCommands());

		if (mod != null)
			modButton.setText(session.getMainMenu().getMod());

		if (isAppStarted.get())
			gameButton.setText(session.getMainMenu().getStop());
		else
			gameButton.setText(session.getMainMenu().getStart());
		changeSessionButton.setText(session.getMainMenu().getChangeSession());

		if (ChatFrame.getInstance().isVisible())
			twitchChatButton.setText(session.getMainMenu().getHideTwitchChat());
		else
			twitchChatButton.setText(session.getMainMenu().getShowTwitchChat());

		int value = twitchChatSize.getValue();
		twitchChatSliderLabel.setText(String.format(session.getMainMenu().getCurrentChatSize(),
				value < ChatFrame.MSG_DISPLAY_INFINITE ? value : session.getMainMenu().getInfinite()));

		twitchChatOptionsLabel.setText(session.getMainMenu().getTwitchChatOptions());

		light.setText(session.getMainMenu().getLightMode());
		night.setText(session.getMainMenu().getNightMode());
		twitch.setText(session.getMainMenu().getTwitchMode());

		transp.setText(session.getMainMenu().getTransparent());
		semisolid.setText(session.getMainMenu().getSemiSolid());
		solid.setText(session.getMainMenu().getSolid());

		twitchChatOnTop.setText(session.getMainMenu().getIsTwitchChatOnTop());

		twitchChatInput.setText(session.getMainMenu().getShowInputTextBoxInChat());

		twitchChatFontLabel.setText(session.getMainMenu().getFont());

		twitchChatFontSizeLabel
				.setText(String.format(session.getMainMenu().getFontSize(), twitchChatFontSize.getValue()));

		sponsor.setText(session.getMainMenu().getSponsorCreator());
	}

	@Handler
	public void updateTheme(ColorThemeUpdateEvent event) {
		if (event.getTheme() != null) {
			this.setBackground(event.getTheme().getBackground());
			twitchChatSize.setForeground(event.getTheme().getFont());
			twitchChatSliderLabel.setForeground(event.getTheme().getFont());
			twitchChatOptionsLabel.setForeground(event.getTheme().getFont());
			light.setForeground(event.getTheme().getFont());
			night.setForeground(event.getTheme().getFont());
			twitch.setForeground(event.getTheme().getFont());
			transp.setForeground(event.getTheme().getFont());
			semisolid.setForeground(event.getTheme().getFont());
			solid.setForeground(event.getTheme().getFont());
			twitchChatOnTop.setForeground(event.getTheme().getFont());
			twitchChatInput.setForeground(event.getTheme().getFont());
			mineRadio.setForeground(event.getTheme().getFont());
			twitchRadio.setForeground(event.getTheme().getFont());
			twitchChatFontLabel.setForeground(event.getTheme().getFont());
			twitchChatFontSizeLabel.setForeground(event.getTheme().getFont());
			twitchChatFontSize.setForeground(event.getTheme().getFont());
			sponsor.setForeground(event.getTheme().getFont());
		}
	}
}
