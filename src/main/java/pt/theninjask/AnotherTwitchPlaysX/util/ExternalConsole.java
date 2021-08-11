package pt.theninjask.AnotherTwitchPlaysX.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

public class ExternalConsole extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static ExternalConsole singleton = new ExternalConsole();

	private JScrollPane scroll;
	private JTextArea console;
	private ExternalConsoleOutputStream out;
	private ExternalConsoleErrorOutputStream err;
	private ExternalConsoleInputStream in;

	private JPanel messagePanel;

	private JTextField input;

	private ExternalConsole() {
		this.setTitle("External Console");
		this.setMinimumSize(new Dimension(300, 300));
		ImageIcon icon = new ImageIcon(Constants.ICON_PATH);
		this.setIconImage(icon.getImage());
		this.setLayout(new BorderLayout());
		this.add(insertConsole(), BorderLayout.CENTER);
		this.add(inputConsole(), BorderLayout.SOUTH);
		scroll.getParent().setBackground(null);
		this.setBackground(Color.WHITE);
		this.out = new ExternalConsoleOutputStream();
		this.err = new ExternalConsoleErrorOutputStream();
		this.in = new ExternalConsoleInputStream();
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setAlwaysOnTop(true);
	}

	public static ExternalConsole getInstance() {
		return singleton;
	}

	private JScrollPane insertConsole() {
		scroll = new JScrollPane();
		console = new JTextArea();
		console.setEditable(false);
		// console.setFocusable(false);
		console.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret) console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scroll.setViewportView(console);
		scroll.setFocusable(false);
		scroll.setEnabled(false);
		scroll.setBorder(null);
		scroll.setWheelScrollingEnabled(true);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);

		console.setOpaque(false);
		console.setForeground(Color.BLACK);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent event) {
				scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
			}
		});
		return scroll;
	}

	private JPanel inputConsole() {
		messagePanel = new JPanel(new BorderLayout());
		input = new JTextField();
		input.setBorder(null);
		
		input.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		input.setForeground(Color.BLACK);
		input.setCaretColor(Color.BLACK);
		input.setBackground(Color.WHITE);
		
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
					console.append(input.getText());
					console.append("\n");
					scroll.repaint();
					scroll.revalidate();
					in.contents = input.getText().getBytes();
					in.pointer = 0;
					input.setText("");
				}
			}
		});
		messagePanel.add(input, BorderLayout.CENTER);
		return messagePanel;
	}

	public OutputStream getExternalConsoleOutputStream() {
		return out;
	}

	public OutputStream getExternalConsoleErrorOutputStream() {
		return err;
	}
	
	public InputStream getExternalConsoleInputStream() {
		return in;
	}

	private class ExternalConsoleOutputStream extends OutputStream {

		@Override
		public void write(int b) throws IOException {
			console.append(Character.toString(b));
			scroll.repaint();
			scroll.revalidate();
		}

	}

	private class ExternalConsoleErrorOutputStream extends OutputStream {

		@Override
		public void write(int b) throws IOException {
			console.append(Character.toString(b));
			scroll.repaint();
			scroll.revalidate();
		}

	}

	public void setDay() {
		console.setForeground(Color.BLACK);
		this.setBackground(Color.WHITE);
		
		input.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		input.setForeground(Color.BLACK);
		input.setCaretColor(Color.BLACK);
		input.setBackground(Color.WHITE);
	}

	public void setNight() {
		console.setForeground(Color.WHITE);
		this.setBackground(Color.BLACK);
		
		input.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		input.setForeground(Color.WHITE);
		input.setCaretColor(Color.WHITE);
		input.setBackground(Color.BLACK);
	}

	private class ExternalConsoleInputStream extends InputStream {

		private byte[] contents;
		private int pointer = 0;

		@Override
		public int read() throws IOException {
			if (pointer >= contents.length)
				return -1;
			return this.contents[pointer++];
		}
		
	}

}
