package pt.theninjask.AnotherTwitchPlaysX.event.datamanager;

import pt.theninjask.AnotherTwitchPlaysX.data.YouTubeSessionData;
import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;

public class YoutubeSessionUpdateEvent extends BasicEvent{
	
	private YouTubeSessionData session;
	
	public YoutubeSessionUpdateEvent(YouTubeSessionData session) {
		super(YoutubeSessionUpdateEvent.class.getSimpleName());
		this.session = session;
	}
	
	public YouTubeSessionData getSession() {
		return session;
	}
}
