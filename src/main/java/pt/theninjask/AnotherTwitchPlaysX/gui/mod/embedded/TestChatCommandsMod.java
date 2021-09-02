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

import pt.theninjask.AnotherTwitchPlaysX.gui.chat.ChatFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.ChatFrame.ChatType;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;
import pt.theninjask.AnotherTwitchPlaysX.util.ThreadPool;

@ATPXModProps(keepLoaded = false, popout = true)
public class TestChatCommandsMod extends ATPXMod {

	private int messageCap = 5;
	private ChatType type = ChatType.MINECRAFT;

	private JPanel mainPanel;
	private JScrollPane scroll;
	private JTextArea chat;

	private String[] users = { "TheNinjask", "Dragonboil", "Drakekax", "GoncaAC", "JoGoKa?" };

	public TestChatCommandsMod() {

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Constants.TWITCH_THEME.getBackground());

		mainPanel.add(setupChat(), BorderLayout.CENTER);

		JPanel messagePanel = new JPanel(new BorderLayout());
		JTextField input = new JTextField();
		input.setBorder(null);
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
					onMessage(users[ThreadLocalRandom.current().nextInt(users.length)], input.getText());
					input.setText("");
				}
			}
		});
		messagePanel.add(input, BorderLayout.CENTER);
		JButton send = new JButton("Send");
		send.addActionListener(l -> {
			onMessage(users[ThreadLocalRandom.current().nextInt(users.length)], input.getText());
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
		scroll.getViewport().setBackground(Constants.TWITCH_THEME.getBackground());

		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);

		chat.setOpaque(false);
		chat.setForeground(Constants.TWITCH_THEME.getFont());
		return scroll;
	}

	public void onMessage(String nick, String message) {
		ThreadPool.execute(() -> {
			DataManager.getCommands().forEach(cmd -> {
				cmd.onMessage(message);
			});
		});
		synchronized (scroll) {
			synchronized (chat) {
				updateChatSize();
				chat.append(String.format(type.getFormat(), nick, message));
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
				if (messageCap < ChatFrame.MSG_DISPLAY_INFINITE) {
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
	}

	@Override
	public JPanel getJPanelInstance() {
		return mainPanel;
	}

}
