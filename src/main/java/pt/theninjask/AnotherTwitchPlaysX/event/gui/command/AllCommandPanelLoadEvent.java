package pt.theninjask.AnotherTwitchPlaysX.event.gui.command;

import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;

public class AllCommandPanelLoadEvent extends BasicEvent {

	private boolean isLoaded;
	
	public AllCommandPanelLoadEvent(boolean isLoaded) {
		super(AllCommandPanelLoadEvent.class.getSimpleName());
		this.isLoaded = isLoaded;
	}
	
	public boolean isLoaded() {
		return isLoaded;
	}
	
}
