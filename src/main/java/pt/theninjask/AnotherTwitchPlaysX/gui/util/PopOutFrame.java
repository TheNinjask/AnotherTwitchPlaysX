package pt.theninjask.AnotherTwitchPlaysX.gui.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import pt.theninjask.AnotherTwitchPlaysX.twitch.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class PopOutFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JFrame parent;
	
	public PopOutFrame(JComponent comp) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s(%s)", PopOutFrame.class.getSimpleName(),this.hashCode()));
		this.parent = null;
		this.add(comp);
		
		this.setTitle(DataManager.getLanguage().getTitle());
		this.setMinimumSize(new Dimension(300, 300));
		ImageIcon icon = new ImageIcon(Constants.ICON_PATH);
		this.setIconImage(icon.getImage());
		this.setBackground(Constants.TWITCH_COLOR);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		comp.requestFocusInWindow();
		this.setVisible(true);
	}
	
	public PopOutFrame(JComponent comp, JFrame parentInc) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s(%s)", PopOutFrame.class.getSimpleName(),this.hashCode()));
		this.parent = parentInc;
		this.parent.setEnabled(false);
		this.add(comp);
		
		this.setTitle(DataManager.getLanguage().getTitle());
		this.setMinimumSize(new Dimension(300, 300));
		ImageIcon icon = new ImageIcon(Constants.ICON_PATH);
		this.setIconImage(icon.getImage());
		this.setBackground(Constants.TWITCH_COLOR);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				parent.setEnabled(true);
				parent.revalidate();
				parent.repaint();
			}
		});
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		comp.requestFocusInWindow();
		this.setVisible(true);
	}
	
	public JFrame getParent() {
		//Constants.printVerboseMessage(Level.INFO, String.format("%s.getParent(%s)", PopOutFrame.class.getSimpleName(),this.hashCode()));
		return parent;
	}
	
}
