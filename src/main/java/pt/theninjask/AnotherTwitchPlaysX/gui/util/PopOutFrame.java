package pt.theninjask.AnotherTwitchPlaysX.gui.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.event.datamanager.LanguageUpdateEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.util.ClosePopOutFrameEvent;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.util.OpenPopOutFrameEvent;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class PopOutFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private JFrame parent;
	
	public PopOutFrame(JComponent comp) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s(%s)", PopOutFrame.class.getSimpleName(),this.hashCode()));
		
		OpenPopOutFrameEvent event = new OpenPopOutFrameEvent(this);
		EventManager.triggerEvent(event);
		if(event.isCancelled())
			return;
		
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
		//DataManager.registerLangEvent(this);
		this.setVisible(true);
		PopOutFrame tmp = this;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				EventManager.triggerEvent(new ClosePopOutFrameEvent(tmp));
				//EventManager.unregisterEventListener(tmp);
			}
		});
	}
	
	public PopOutFrame(JComponent comp, JFrame parentInc) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s(%s)", PopOutFrame.class.getSimpleName(),this.hashCode()));
		
		OpenPopOutFrameEvent event = new OpenPopOutFrameEvent(this);
		EventManager.triggerEvent(event);
		if(event.isCancelled())
			return;
		
		this.parent = parentInc;
		this.parent.setEnabled(false);
		this.add(comp);
		
		this.setTitle(DataManager.getLanguage().getTitle());
		this.setMinimumSize(new Dimension(300, 300));
		ImageIcon icon = new ImageIcon(Constants.ICON_PATH);
		this.setIconImage(icon.getImage());
		this.setBackground(Constants.TWITCH_COLOR);
		//DataManager.registerLangEvent(this);
		PopOutFrame tmp = this;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				EventManager.triggerEvent(new ClosePopOutFrameEvent(tmp));
				//DataManager.unregisterLangEvent("this");
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

	//@Handler
	public void updateLang(LanguageUpdateEvent event) {
		if(event.getLanguage()!=null)
			this.setTitle(event.getLanguage().getTitle());
	}
	
}
