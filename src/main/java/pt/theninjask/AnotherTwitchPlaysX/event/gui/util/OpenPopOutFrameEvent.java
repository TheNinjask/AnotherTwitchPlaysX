package pt.theninjask.AnotherTwitchPlaysX.event.gui.util;

import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;

public class OpenPopOutFrameEvent extends BasicEvent {

	private PopOutFrame frame;
	
	public OpenPopOutFrameEvent(PopOutFrame frame) {
		super(OpenPopOutFrameEvent.class.getSimpleName());
		this.frame = frame;
	}
	
	public PopOutFrame getFrame() {
		return frame;
	}
}
