package pt.theninjask.AnotherTwitchPlaysX.gui.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import com.google.api.services.youtube.model.LiveChatMessage;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandDataOnMessageMatchedEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.LanguageUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.chat.ChatFrameOnMessageEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.chat.ChatFrameOnTwitchMessageEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.chat.ChatFrameOnYouTubeMessageEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.chat.ChatFrameSendMessageEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.util.ComponentResizer;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.stream.twitch.TwitchPlayer;
import pt.theninjask.AnotherTwitchPlaysX.stream.youtube.YouTubePlayer;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.WrapEditorKit;

public class ChatFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static ChatFrame singleton = new ChatFrame();

	private static int MSG_DISPLAY_MIN = 5;

	private static int MSG_DISPLAY_MAX = 50;

	private static int MSG_DISPLAY_INFINITE = MSG_DISPLAY_MAX + 1;

	public enum ChatStyle {
		MINECRAFT("Minecraft Style", "<%s> %s\n"), TWITCH("Twitch Style", "%s: %s\n");

		private String style;

		private String format;

		private ChatStyle(String style, String format) {
			this.style = style;
			this.format = format;
		}

		public String getStyle() {
			return style;
		}

		public String getFormat() {
			return format;
		}

	}

	public enum ChatMode {
		TRANSPARENT("Transparent"), SEMI_SOLID("Semi-Solid"), SOLID("Solid");

		private String type;

		private ChatMode(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}

	}

	public enum ChatType {
		NONE("NONE", 0x0), CMD("CMD", 0x1), TWITCH("TWITCH", 0x10), YOUTUBE("YOUTUBE", 0x100), STREAM("STREAM", 0x110),
		ALL("ALL", 0x111);

		private String name;

		private int id;

		private ChatType(String name, int id) {
			this.name = name;
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public int getId() {
			return id;
		}
		
		public boolean equals(ChatType type) {
			return this == type || (this.id & type.id) != 0;
		}

	}

	private JTextPane chat;

	private JScrollPane scroll;

	private int messageCap = 5;

	private ChatStyle style;

	private ChatMode mode;
	
	private ChatType type;

	private int posX, posY;

	private ComponentResizer cr;

	private AtomicBoolean enabledDR;

	private JPanel messagePanel;

	private JTextField input;

	private JButton send;

	private ChatFrame() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", ChatFrame.class.getSimpleName()));
		this.style = ChatStyle.MINECRAFT;
		this.mode = ChatMode.SOLID;
		this.type = ChatType.STREAM;
		// this.setTitle(String.format("%s's Twitch Chat",
		// DataManager.getInstance().getSession().getChannel().substring(1)));
		this.setMinimumSize(new Dimension(150, 150));
		this.setSize(300, 300);
		ImageIcon icon = new ImageIcon(Constants.ICON_PATH);
		this.setIconImage(icon.getImage());
		this.setLayout(new BorderLayout());

		this.add(insertChat(), BorderLayout.CENTER);

		this.add(inputChat(), BorderLayout.SOUTH);
		showInputMessage(false);
		setUndecorated(true);
		this.setBackground(Constants.TWITCH_THEME.background());
		scroll.getParent().setBackground(null);

		Border border = BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 5);
		this.getRootPane().setBorder(border);
		cr = new ComponentResizer();
		cr.setMinimumSize(this.getMinimumSize());

		enabledDR = new AtomicBoolean(false);
		enableDragAndResize();
		// DataManager.registerLangEvent(this);
		EventManager.registerEventListener(this);
		this.setTitle(String.format(DataManager.getLanguage().getChat().getTitle(), DataManager.getLanguage().getID()));
	}

	public static ChatFrame getInstance() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.getInstance()", ChatFrame.class.getSimpleName()));
		return singleton;
	}

	public static boolean setMsgDisplayMin(int min) {
		if (min < 0)
			return false;
		MSG_DISPLAY_MIN = min;
		if (MSG_DISPLAY_MAX < MSG_DISPLAY_MIN) {
			MSG_DISPLAY_MAX = MSG_DISPLAY_MIN;
			MSG_DISPLAY_INFINITE = MSG_DISPLAY_MAX + 1;
		}
		return true;
	}

	public static int getMsgDisplayMin() {
		return MSG_DISPLAY_MIN;
	}

	public static boolean setMsgDisplayMax(int max) {
		if (max < 0)
			return false;
		MSG_DISPLAY_MAX = max;
		MSG_DISPLAY_INFINITE = MSG_DISPLAY_MAX + 1;
		if (MSG_DISPLAY_MAX < MSG_DISPLAY_MIN)
			MSG_DISPLAY_MIN = MSG_DISPLAY_MAX;
		return true;
	}

	public static int getMsgDisplayMax() {
		return MSG_DISPLAY_MAX;
	}

	public static int getMsgDisplayInf() {
		return MSG_DISPLAY_INFINITE;
	}

	private JScrollPane insertChat() {
		scroll = new JScrollPane();
		chat = new JTextPane();
		chat.setEditorKit(new WrapEditorKit());
		// chat = new JTextArea();
		// chat.setTabSize(4);
		chat.setEditable(false);
		chat.setFocusable(false);
		// chat.setLineWrap(true);
		for (MouseListener elem : chat.getMouseListeners()) {
			chat.removeMouseListener(elem);
		}
		for (MouseMotionListener elem : chat.getMouseMotionListeners()) {
			chat.removeMouseMotionListener(elem);
		}
		DefaultCaret caret = (DefaultCaret) chat.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scroll.setViewportView(chat);
		scroll.setFocusable(false);
		scroll.setEnabled(false);
		scroll.setBorder(null);
		scroll.setWheelScrollingEnabled(false);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scroll.getViewport().setBackground(Constants.TWITCH_THEME.background());

		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);

		chat.setOpaque(false);
		chat.setForeground(Constants.TWITCH_THEME.font());
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent event) {
				scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
			}
			/*
			 * @Override public void componentMoved(ComponentEvent e) {
			 * scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().
			 * getMaximum()); }
			 */
		});
		return scroll;
	}

	private JPanel inputChat() {
		messagePanel = new JPanel(new BorderLayout());
		input = new JTextField();
		input.setFocusTraversalKeysEnabled(false);
		input.setBorder(BorderFactory.createLineBorder(Constants.TWITCH_THEME.font(), 1));
		input.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					ChatFrameSendMessageEvent event = new ChatFrameSendMessageEvent(
							DataManager.getLanguage().getChat().getUser(), input.getText());
					EventManager.triggerEvent(event);
					if (event.isCancelled())
						return;

					onMessage(DataManager.getLanguage().getChat().getUser(), input.getText());
					if (TwitchPlayer.getInstance().isConnected()) {
						TwitchPlayer.getInstance().sendMessage(input.getText());
					}
					if (YouTubePlayer.getInstance().isConnected()) {
						YouTubePlayer.getInstance().sendMessage(input.getText());
					}
					input.setText("");
				}
			}
		});
		messagePanel.add(input, BorderLayout.CENTER);
		send = new JButton(DataManager.getLanguage().getChat().getSend());
		send.addActionListener(l -> {

			ChatFrameSendMessageEvent event = new ChatFrameSendMessageEvent(
					DataManager.getLanguage().getChat().getUser(), input.getText());
			EventManager.triggerEvent(event);
			if (event.isCancelled())
				return;

			onMessage(DataManager.getLanguage().getChat().getUser(), input.getText());
			if (TwitchPlayer.getInstance().isConnected()) {
				TwitchPlayer.getInstance().sendMessage(input.getText());
			}
			if (YouTubePlayer.getInstance().isConnected()) {
				YouTubePlayer.getInstance().sendMessage(input.getText());
			}
			input.setText("");
		});
		messagePanel.add(send, BorderLayout.EAST);
		return messagePanel;
	}

	public void enableDragAndResize() {
		if (enabledDR.get())
			return;
		chat.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				posX = e.getX() - (chat.getWidth() - (scroll.getWidth() + singleton.getWidth()) / 2);
				posY = e.getY() - (chat.getHeight() - (scroll.getHeight() + singleton.getHeight()) / 2);
			}
		});
		chat.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);

			}
		});
		cr.registerComponent(this);
		enabledDR.set(true);
	}

	public void disableDragAndResize() {
		if (!enabledDR.get())
			return;
		for (MouseListener elem : chat.getMouseListeners()) {
			chat.removeMouseListener(elem);
		}
		for (MouseMotionListener elem : chat.getMouseMotionListeners()) {
			chat.removeMouseMotionListener(elem);
		}
		cr.deregisterComponent(this);
		enabledDR.set(false);
	}

	public void onMessage(String nick, String message) {
		ChatFrameOnMessageEvent event = new ChatFrameOnMessageEvent(this, nick, message);
		EventManager.triggerEvent(event);
		if (event.isCancelled())
			return;
		synchronized (scroll) {
			synchronized (chat) {
				try {
					updateChatSize();
					// chat.append(String.format(type.getFormat(), nick, message));
					StyledDocument doc = chat.getStyledDocument();
					doc.insertString(doc.getLength(), String.format(style.getFormat(), nick, message), null);
					scroll.repaint();
					scroll.revalidate();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Handler
	public void onMessage(ChannelMessageEvent event) {
		if(!type.equals(ChatType.TWITCH))
			return;
		ChatFrameOnTwitchMessageEvent trigger = new ChatFrameOnTwitchMessageEvent(this, event);
		EventManager.triggerEvent(trigger);
		if (trigger.isCancelled())
			return;
		onMessage(event.getActor().getNick(), event.getMessage());
	}

	@Handler
	public void onMessage(LiveChatMessage event) {
		if(!type.equals(ChatType.YOUTUBE))
			return;
		ChatFrameOnYouTubeMessageEvent trigger = new ChatFrameOnYouTubeMessageEvent(this, event);
		EventManager.triggerEvent(trigger);
		if (trigger.isCancelled())
			return;
		onMessage(event.getAuthorDetails().getDisplayName(), event.getSnippet().getDisplayMessage());
	}

	@Handler
	public void onMessage(CommandDataOnMessageMatchedEvent event) {
		if(!type.equals(ChatType.CMD))
			return;
		onMessage(event.getUser(), event.getMessage());
	}
	
	public void clearChat() {
		synchronized (chat) {
			chat.setText("");
		}
	}

	public void updateChatSize() {
		synchronized (chat) {
			try {
				if (messageCap < MSG_DISPLAY_INFINITE) {
					/*
					 * int toClear = chat.getLineCount() - messageCap - 1; if (toClear >= 0) {
					 * chat.replaceRange("", 0, chat.getLineEndOffset(toClear)); }
					 */
					Element root = chat.getDocument().getDefaultRootElement();
					int toClear = root.getElementCount() - messageCap - 1;
					if (toClear >= 0) {
						Element first = root.getElement(0);
						Element last = root.getElement(toClear);
						chat.getDocument().remove(first.getStartOffset(), last.getEndOffset());
					}
				}
			} catch (Exception e) {
				Constants.showExceptionDialog(e);
			}
		}
	}

	public void setTextColor(Color newColor) {
		synchronized (chat) {
			chat.setForeground(newColor);
		}
	}

	public Color getTextColor() {
		return chat.getForeground();
	}

	public void setBGColor(Color newColor) {
		int alpha;
		switch (mode) {
		case TRANSPARENT:
			alpha = 0;
			break;
		case SEMI_SOLID:
			alpha = 127;
			break;
		case SOLID:
		default:
			alpha = 255;
			break;
		}
		setBackground(new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), alpha));
	}

	public Color getBGColor() {
		return getBackground();
	}

	public void setColor(Color text, Color bg) {
		synchronized (chat) {
			chat.setForeground(text);
			input.setBorder(BorderFactory.createLineBorder(text, 1));
			setBGColor(bg);
		}
	}

	public void setMessageCap(int newCap) {
		messageCap = newCap;
	}

	public void setFontSize(int size) {
		synchronized (chat) {
			chat.setFont(new Font(chat.getFont().getFontName(), 0, size));
		}
	}

	public void setFont(String name) {
		synchronized (chat) {
			chat.setFont(new Font(name, 0, chat.getFont().getSize()));
		}
	}

	public Font getCurrentFont() {
		return chat.getFont();
	}

	public void setChatStyle(ChatStyle style) {
		this.style = style;
	}

	public ChatStyle getChatStyle() {
		return style;
	}

	public void setChatMode(ChatMode mode) {
		if (mode == this.mode)
			return;
		Color currentColor = getBGColor();
		this.mode = mode;
		setBGColor(currentColor);
	}
	
	public void setChatType(ChatType type) {
		this.type = type;
	}

	public ChatType getChatType() {
		return type;
	}

	public void showInputMessage(boolean show) {
		messagePanel.setVisible(show);
	}

	// @Handler
	public void updateLang(LanguageUpdateEvent event) {
		if (event.getLanguage() == null)
			return;
		send.setText(event.getLanguage().getChat().getSend());
		this.setTitle(String.format(event.getLanguage().getChat().getTitle(), event.getLanguage().getID()));
	}
}
