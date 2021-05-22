package pt.theninjask.AnotherTwitchPlaysX.gui.chat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.util.ComponentResizer;
import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class TwitchChatFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static TwitchChatFrame singleton = new TwitchChatFrame();

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
	
	private int posX,posY;

	private ComponentResizer cr;
	
	private AtomicBoolean enabledDR;
	
	private TwitchChatFrame() {
		this.type = ChatType.MINECRAFT;
		this.mode = ChatMode.SOLID;
		// this.setTitle(String.format("%s's Twitch Chat",
		// DataManager.getInstance().getSession().getChannel().substring(1)));
		this.setMinimumSize(new Dimension(300, 300));
		ImageIcon icon = new ImageIcon(Constants.ICON_PATH);
		this.setIconImage(icon.getImage());
		this.add(insertChat());
		setUndecorated(true);
		this.setBackground(Constants.TWITCH_COLOR);
		scroll.getParent().setBackground(null);
		
		Border border = BorderFactory.createLineBorder(new Color(0, 0, 0, 0),5);
		this.getRootPane().setBorder(border);
		cr = new ComponentResizer();
		cr.setMinimumSize(this.getMinimumSize());
		
		enabledDR = new AtomicBoolean(false);
		enableDragAndResize();
		 /*for (MouseListener elem : getMouseListeners()) {
			chat.addMouseListener(elem);
		}
		for (MouseMotionListener elem : getMouseMotionListeners()) {
			chat.addMouseMotionListener(elem);
		}*/
	}
	
	public static TwitchChatFrame getInstance() {
		singleton.setTitle(
				String.format("%s's Twitch Chat", DataManager.getInstance().getSession().getChannel().substring(1)));
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
	
	public void enableDragAndResize() {
		if(enabledDR.get())
			return;
		chat.addMouseListener(new MouseAdapter()
		{
		   public void mousePressed(MouseEvent e)
		   {
		      posX=e.getX();
		      posY=e.getY();
		   }
		});

		chat.addMouseMotionListener(new MouseAdapter()
		{
		     public void mouseDragged(MouseEvent evt)
		     {
				setLocation (evt.getXOnScreen()-posX,evt.getYOnScreen()-posY);
							
		     }
		});
		cr.registerComponent(this);
		enabledDR.set(true);
	}
	
	public void disableDragAndResize() {
		if(!enabledDR.get())
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

	@Handler
	public void onMessage(ChannelMessageEvent event) {
		synchronized (scroll) {
			synchronized (chat) {
				updateChatSize();
				chat.append(String.format(type.getFormat(), event.getActor().getNick(), event.getMessage()));
				scroll.repaint();
				scroll.revalidate();
			}
		}
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
}
