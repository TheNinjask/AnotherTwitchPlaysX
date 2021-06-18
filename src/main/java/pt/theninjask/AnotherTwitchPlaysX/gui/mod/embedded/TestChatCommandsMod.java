package pt.theninjask.AnotherTwitchPlaysX.gui.mod.embedded;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.helper.ActorMessageEvent;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.TwitchChatFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.TwitchChatFrame.ChatType;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;
import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.ThreadPool;
import pt.theninjask.AnotherTwitchPlaysX.util.mock.ChannelMessageEventMock;

@ATPXModProps(keepLoaded = false, popout = true)
public class TestChatCommandsMod extends ATPXMod {

	// private Client mockClient;
	// private static final String NICK_MOCK = "MOCK";
	// private static final String HOST_MOCK = "0.0.0.0";
	// private static final int PORT_MOCK = 6697;
	// private static final String CHANNEL_MOCK = "#MOCK";
	// private AtomicBoolean isConnected = new AtomicBoolean(false);

	private int messageCap = 5;
	private ChatType type = ChatType.MINECRAFT;

	private JPanel mainPanel;
	private JScrollPane scroll;
	private JTextArea chat;

	private String[] users = { "TheNinjask", "Dragonboil", "Drakekax", "GoncaAC", "JoGoKa?" };

	public TestChatCommandsMod() {

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Constants.TWITCH_COLOR);

		mainPanel.add(setupChat(), BorderLayout.CENTER);

		JPanel messagePanel = new JPanel(new BorderLayout());
		JTextField input = new JTextField();
		input.setBorder(null);
		input.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					onMessage(new ChannelMessageEventMock(users[ThreadLocalRandom.current().nextInt(users.length)],
							input.getText()));
					input.setText("");
				}
			}
		});
		messagePanel.add(input, BorderLayout.CENTER);
		JButton send = new JButton("Send");
		send.addActionListener(l -> {
			onMessage(new ChannelMessageEventMock(users[ThreadLocalRandom.current().nextInt(users.length)],
					input.getText()));
			input.setText("");
		});
		messagePanel.add(send, BorderLayout.EAST);

		mainPanel.add(messagePanel, BorderLayout.SOUTH);

	}

	private JScrollPane setupChat() {
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
		return scroll;
	}

	@Handler
	public void onMessage(ActorMessageEvent<User> event) {
		ThreadPool.execute(() -> {
			DataManager.getInstance().getCommands().forEach(cmd -> {
				cmd.onMessage(event);
			});
		});
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
				if (messageCap < TwitchChatFrame.MSG_DISPLAY_INFINITE) {
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

	@Override
	public void refresh() {
		/*
		 * if (isConnected.get()) { for (Object listener :
		 * mockClient.getEventManager().getRegisteredEventListeners()) {
		 * mockClient.getEventManager().unregisterEventListener(listener); }
		 * mockClient.shutdown(); isConnected.set(false); } mockClient.connect();
		 * isConnected.set(true); DataManager.getInstance().getCommands().forEach(cmd ->
		 * { mockClient.getEventManager().registerEventListener(cmd); });
		 */
	}

	@Override
	public JPanel getJPanelInstance() {
		return mainPanel;
	}

}
