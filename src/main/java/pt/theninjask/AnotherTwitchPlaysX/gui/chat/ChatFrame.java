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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import com.google.api.services.youtube.model.LiveChatMessage;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.util.ComponentResizer;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.stream.twitch.TwitchPlayer;
import pt.theninjask.AnotherTwitchPlaysX.stream.youtube.YouTubePlayer;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class ChatFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static ChatFrame singleton = new ChatFrame();

	public static int MSG_DISPLAY_MIN = 5;

	public static int MSG_DISPLAY_MAX = 50;

	public static int MSG_DISPLAY_INFINITE = 51;

	public enum ChatType {
		MINECRAFT("Minecraft Style", "<%s> %s\n"), TWITCH("Twitch Style", "%s: %s\n");

		private String type;

		private String format;

		private ChatType(String type, String format) {
			this.type = type;
			this.format = format;
		}

		public String getType() {
			return type;
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

	private JTextArea chat;

	private JScrollPane scroll;

	private int messageCap = 5;

	private ChatType type;

	private ChatMode mode;

	private int posX, posY;

	private ComponentResizer cr;

	private AtomicBoolean enabledDR;

	private JPanel messagePanel;

	private JTextField input;

	private JButton send;

	private ChatFrame() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", ChatFrame.class.getSimpleName()));
		this.type = ChatType.MINECRAFT;
		this.mode = ChatMode.SOLID;
		// this.setTitle(String.format("%s's Twitch Chat",
		// DataManager.getInstance().getSession().getChannel().substring(1)));
		this.setMinimumSize(new Dimension(300, 300));
		ImageIcon icon = new ImageIcon(Constants.ICON_PATH);
		this.setIconImage(icon.getImage());
		this.setLayout(new BorderLayout());

		this.add(insertChat(), BorderLayout.CENTER);

		this.add(inputChat(), BorderLayout.SOUTH);
		showInputMessage(false);
		setUndecorated(true);
		this.setBackground(Constants.TWITCH_COLOR);
		scroll.getParent().setBackground(null);

		Border border = BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 5);
		this.getRootPane().setBorder(border);
		cr = new ComponentResizer();
		cr.setMinimumSize(this.getMinimumSize());

		enabledDR = new AtomicBoolean(false);
		enableDragAndResize();
		// DataManager.registerLangEvent(this);
		this.setTitle(String.format(DataManager.getLanguage().getChat().getTitle(),
				DataManager.getLanguage().getID()));
	}

	public static ChatFrame getInstance() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.getInstance()", ChatFrame.class.getSimpleName()));
		return singleton;
	}

	private JScrollPane insertChat() {
		scroll = new JScrollPane();
		chat = new JTextArea();
		chat.setEditable(false);
		chat.setFocusable(false);
		chat.setLineWrap(true);
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
		scroll.getViewport().setBackground(Constants.TWITCH_COLOR);

		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);

		chat.setOpaque(false);
		chat.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
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
		input.setBorder(BorderFactory.createLineBorder(Constants.TWITCH_COLOR_COMPLEMENT, 1));
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
					onMessage(DataManager.getLanguage().getChat().getUser(), input.getText());
					if(TwitchPlayer.getInstance().isConnected()) {
						TwitchPlayer.getInstance().sendMessage(input.getText());						
					}
					if(YouTubePlayer.getInstance().isConnected()) {
						YouTubePlayer.getInstance().sendMessage(input.getText());
					}
					input.setText("");
				}
			}
		});
		messagePanel.add(input, BorderLayout.CENTER);
		send = new JButton(DataManager.getLanguage().getChat().getSend());
		send.addActionListener(l -> {
			onMessage(DataManager.getLanguage().getChat().getUser(), input.getText());
			if(TwitchPlayer.getInstance().isConnected()) {
				TwitchPlayer.getInstance().sendMessage(input.getText());						
			}
			if(YouTubePlayer.getInstance().isConnected()) {
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
				posX = e.getX() - (chat.getWidth() - scroll.getWidth());
				posY = e.getY() - (chat.getHeight() - scroll.getHeight());
			}
		});
		chat.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent evt) {
				setLocation(evt.getXOnScreen() - posX, evt.getYOnScreen() - posY);

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
		synchronized (scroll) {
			synchronized (chat) {
				updateChatSize();
				chat.append(String.format(type.getFormat(), nick, message));
				scroll.repaint();
				scroll.revalidate();
			}
		}
	}

	@Handler
	public void onMessage(ChannelMessageEvent event) {
		onMessage(event.getActor().getNick(), event.getMessage());
	}
	
	@Handler
	public void onMessage(LiveChatMessage message) {
		onMessage(message.getAuthorDetails().getDisplayName(), message.getSnippet().getDisplayMessage());
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
					int toClear = chat.getLineCount() - messageCap - 1;
					if (toClear >= 0) {
						chat.replaceRange("", 0, chat.getLineEndOffset(toClear));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
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

	public void setChatType(ChatType type) {
		this.type = type;
	}

	public ChatType getChatType() {
		return type;
	}

	public JScrollPane getScroll() {
		return scroll;
	}

	public JTextArea getChat() {
		return chat;
	}

	public void setChatMode(ChatMode mode) {
		if (mode == this.mode)
			return;
		Color currentColor = getBGColor();
		this.mode = mode;
		setBGColor(currentColor);
	}

	public void showInputMessage(boolean show) {
		messagePanel.setVisible(show);
	}

	//@Handler
	public void updateLang(Lang session) {
		send.setText(session.getChat().getSend());
		this.setTitle(String.format(DataManager.getLanguage().getChat().getTitle(),
				DataManager.getLanguage().getID()));
	}
}