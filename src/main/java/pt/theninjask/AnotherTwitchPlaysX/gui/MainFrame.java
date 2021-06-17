package pt.theninjask.AnotherTwitchPlaysX.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.theninjask.AnotherTwitchPlaysX.data.SessionData;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.LoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.twitch.TwitchPlayer;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static MainFrame singleton = new MainFrame();
	
	private JPanel currentPanel = LoginPanel.getInstance();
	
	private MainFrame() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", MainFrame.class.getSimpleName()));
		this.onStart();
		this.getContentPane().setBackground(Constants.TWITCH_COLOR);
		this.setTitle(Constants.TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(300, 300));
		ImageIcon icon = new ImageIcon(Constants.ICON_PATH);
		this.setIconImage(icon.getImage());
		this.add(currentPanel);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		this.setResizable(false);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				if(LoginPanel.getInstance().rememberSession()) {
					saveSession();
					if(TwitchPlayer.getInstance().isConnected())
						TwitchPlayer.getInstance().disconnect();
				}
			}
		});
	}
	
	private void saveSession() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.saveSession()", MainFrame.class.getSimpleName()));
		if(currentPanel!=LoginPanel.getInstance()) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				File file = new File("session.json");
				JTextField tmp = new JTextField(String.format(Constants.SAVING_SESSION_MSG, file.getAbsolutePath()));
				tmp.setEditable(false);
				tmp.setBorder(null);
				tmp.setOpaque(false);
				tmp.setToolTipText(Constants.CHANNEL_FIELD_TIP);
				tmp.setForeground(Constants.TWITCH_COLOR_COMPLEMENT);
				objectMapper.writeValue(file, DataManager.getInstance().getSession());
				Constants.showCustomColorMessageDialog(null, 
						tmp, 
						"Saving Session", JOptionPane.INFORMATION_MESSAGE, null, Constants.TWITCH_COLOR);			
			} catch (IOException e) {
				Constants.showExceptionDialog(e);
			}	
		}
	}
	
	public static MainFrame getInstance() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.getInstance()", MainFrame.class.getSimpleName()));
		return singleton;
	}

	private void onStart() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.onStart()", MainFrame.class.getSimpleName()));
		File sessionFile = new File("session.json");
		if(sessionFile.exists())
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				SessionData session = objectMapper.readValue(sessionFile, SessionData.class);
				DataManager.getInstance().setSession(session);
				LoginPanel.getInstance().setSession(session);
			} catch (IOException e) {
				Constants.showExceptionDialog(e);
			}
	}
	
	public void replacePanel(JPanel newPanel) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.replacePanel()", MainFrame.class.getSimpleName()));
		this.remove(currentPanel);
		this.currentPanel = newPanel;
		this.add(newPanel);
		this.revalidate();
		this.repaint();
	}
	
}
