package pt.theninjask.AnotherTwitchPlaysX.util.mock;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.ServerMessage;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.helper.ActorMessageEvent;

public class ChannelMessageEventMock implements ActorMessageEvent<User> {

	private User actor;

	private String message;

	public ChannelMessageEventMock(String nick, String message) {
		this.actor = new UserMock(nick);
		this.message = message;
	}

	@Override
	public User getActor() {
		return actor;
	}

	@Override
	public Client getClient() {
		// Never gonna be used for this mock right?
		return null;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public ServerMessage getSource() {
		// Never gonna be used for this mock
		return null;
	}

}