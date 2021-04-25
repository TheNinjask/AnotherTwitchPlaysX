package pt.theninjask.AnotherTwitchPlaysX.gui;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.theninjask.AnotherTwitchPlaysX.gui.login.LoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.twitch.TwitchPlayer;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static MainFrame singleton = new MainFrame();
	
	private JPanel currentPanel = MainMenuPanel.getInstance();
	
	private MainFrame() {
		this.setTitle(Constants.TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(300, 300));
		ImageIcon icon = new ImageIcon(Constants.ICON_PATH);
		this.add(currentPanel);
		this.setIconImage(icon.getImage());
		this.setVisible(true);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				if(LoginPanel.getInstance().rememberSession()) {
					saveSession();
					if(TwitchPlayer.getInstance().isConnected())
						TwitchPlayer.getInstance().disconnect();
				}
				super.windowClosing(event);
			}
		});
	}
	
	private void saveSession() {
		if(currentPanel!=LoginPanel.getInstance()) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				File file = new File("session.json");
				objectMapper.writeValue(file, DataManager.getInstance().getSession());
				JOptionPane.showMessageDialog(null, String.format(Constants.SAVING_SESSION_MSG, file.getAbsolutePath()), "Saving Session", JOptionPane.INFORMATION_MESSAGE);			
			} catch (IOException e) {
				Constants.showExceptionDialog(e);
			}	
		}
	}
	
	public static MainFrame getInstance() {
		return singleton;
	}
	
	public void replacePanel(JPanel newPanel) {
		this.remove(currentPanel);
		this.currentPanel = newPanel;
		this.add(newPanel);
		this.revalidate();
		this.repaint();
	}
	
}
