package pt.theninjask.AnotherTwitchPlaysX.event.gui.command;

import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;

public class AllCommandPanelBackEvent extends BasicEvent {

	private boolean isLoaded;
	
	public AllCommandPanelBackEvent(boolean isLoaded) {
		super(AllCommandPanelBackEvent.class.getSimpleName());
		this.isLoaded = isLoaded;
	}
	
	public boolean isLoaded() {
		return isLoaded;
	}
}
