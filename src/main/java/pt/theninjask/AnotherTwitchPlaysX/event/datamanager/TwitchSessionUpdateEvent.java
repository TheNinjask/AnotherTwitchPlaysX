package pt.theninjask.AnotherTwitchPlaysX.event.datamanager;

import pt.theninjask.AnotherTwitchPlaysX.data.TwitchSessionData;
import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;

public class TwitchSessionUpdateEvent extends BasicEvent{

	private TwitchSessionData session;

	public TwitchSessionUpdateEvent(TwitchSessionData session) {
		super(TwitchSessionUpdateEvent.class.getSimpleName());
		this.session = session;
	}

	public TwitchSessionData getSession() {
		return session;
	}

}
