package pt.theninjask.AnotherTwitchPlaysX.gui.util;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class PopOutFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JFrame parent;
	
	public PopOutFrame(JComponent comp) {
		this.parent = null;
		this.add(comp);

		this.setTitle(Constants.TITLE);
		this.setMinimumSize(new Dimension(300, 300));
		ImageIcon icon = new ImageIcon(Constants.ICON_PATH);
		this.setIconImage(icon.getImage());
		this.setBackground(Constants.TWITCH_COLOR);
		this.setVisible(true);
	}
	
	public PopOutFrame(JComponent comp, JFrame parentInc) {
		this.parent = parentInc;
		this.parent.setEnabled(false);
		this.add(comp);
		
		this.setTitle(Constants.TITLE);
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
		this.setVisible(true);
	}
	
	public JFrame getParent() {
		return parent;
	}
	
}
