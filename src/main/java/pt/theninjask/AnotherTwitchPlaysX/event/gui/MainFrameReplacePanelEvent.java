package pt.theninjask.AnotherTwitchPlaysX.event.gui;

import javax.swing.JPanel;

import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;

public class MainFrameReplacePanelEvent extends BasicEvent {
	
	private JPanel older;
	
	private JPanel newer;
	
	public MainFrameReplacePanelEvent(JPanel older, JPanel newer) {
		super(MainFrameReplacePanelEvent.class.getSimpleName());
		this.older = older;
		this.newer = newer;
	}
	
	public JPanel getOld() {
		return older;
	}
	
	public JPanel getNew() {
		return newer;
	}
}
