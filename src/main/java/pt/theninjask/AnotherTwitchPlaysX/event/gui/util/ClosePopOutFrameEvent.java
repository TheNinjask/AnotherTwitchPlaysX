package pt.theninjask.AnotherTwitchPlaysX.event.gui.util;

import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;
/**
 * 
 * This event is not cancellable at current moment
 *
 */
public class ClosePopOutFrameEvent extends BasicEvent {

	private PopOutFrame frame;

	public ClosePopOutFrameEvent(PopOutFrame frame) {
		super(ClosePopOutFrameEvent.class.getSimpleName());
		this.frame = frame;
	}

	public PopOutFrame getFrame() {
		return frame;
	}

}
