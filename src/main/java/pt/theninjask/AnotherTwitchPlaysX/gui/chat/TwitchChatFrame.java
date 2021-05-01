package pt.theninjask.AnotherTwitchPlaysX.gui.chat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class TwitchChatFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static TwitchChatFrame singleton = new TwitchChatFrame();
	
	public static final int MSG_DISPLAY_MIN = 5;
	
	public static final int MSG_DISPLAY_MAX = 50;
	
	public static final int MSG_DISPLAY_INFINITE = 51;
	
	private JTextArea chat;
	
	private JScrollPane scroll;
	
	private int messageCap = 5;
	
	private TwitchChatFrame() {
		this.setTitle(String.format("%s's Twitch Chat", DataManager.getInstance().getSession().getChannel().substring(1)));
		this.setMinimumSize(new Dimension(300, 300));
		ImageIcon icon = new ImageIcon(Constants.ICON_PATH);
		this.setIconImage(icon.getImage());
		this.add(insertChat());
	}

	public static TwitchChatFrame getInstance() {
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
		DefaultCaret caret = (DefaultCaret)chat.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scroll.setViewportView(chat);
		scroll.setFocusable(false);
		scroll.setEnabled(false);
		scroll.setBorder(null);
		scroll.setWheelScrollingEnabled(false);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scroll.getViewport().setBackground(Constants.TWITCH_COLOR);
		chat.setOpaque(false);
		chat.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent event) {
				scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
			}
			/*@Override
			public void componentMoved(ComponentEvent e) {
				scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
			}*/
		});
		return scroll;
	}
	
	@Handler
	public void onMessage(ChannelMessageEvent event){
		updateChatSize();
		chat.append(String.format("<%s> %s\n",
					event.getActor().getNick(), event.getMessage()));
	}
	
	public void clearChat() {
		chat.setText("");
	}
	
	public void updateChatSize() {
		try {
			if(messageCap<MSG_DISPLAY_INFINITE) {
				int toClear = chat.getLineCount() - messageCap - 1;
				if(toClear>=0) {
					chat.replaceRange("", 0, chat.getLineEndOffset(toClear));
				}
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void setTextColor(Color newColor) {
		chat.setForeground(newColor);
	}
	
	public void setBGColor(Color newColor) {
		scroll.getViewport().setBackground(newColor);
	}
	
	public void setColor(Color text, Color bg) {
		chat.setForeground(text);
		scroll.getViewport().setBackground(bg);
	}
	
	public void setMessageCap(int newCap) {
		messageCap = newCap;
	}
	
	public void setFontSize(int size) {
		chat.setFont(new Font(chat.getFont().getFontName(), 0, size));
	}
	
	public void setFont(String name) {
		chat.setFont(new Font(name, 0, chat.getFont().getSize()));
	}
	
	public Font getCurrentFont() {
		return chat.getFont();
	}
}
