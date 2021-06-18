package pt.theninjask.AnotherTwitchPlaysX.util.mock;

import java.util.Optional;
import java.util.Set;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.element.User;

public class UserMock implements User {

	private String nick;

	public UserMock(String nick) {
		this.nick = nick;
	}

	@Override
	public String getMessagingName() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public long getCreationTime() {
		return 0;
	}

	@Override
	public Client getClient() {
		return null;
	}

	@Override
	public boolean isStale() {
		return false;
	}

	@Override
	public Optional<String> getAccount() {
		return null;
	}

	@Override
	public Optional<String> getAwayMessage() {
		return null;
	}

	@Override
	public Set<String> getChannels() {
		return null;
	}

	@Override
	public String getHost() {
		return null;
	}

	@Override
	public String getNick() {
		return nick;
	}

	@Override
	public Optional<String> getOperatorInformation() {
		return null;
	}

	@Override
	public Optional<String> getRealName() {
		return null;
	}

	@Override
	public Optional<String> getServer() {
		return null;
	}

	@Override
	public String getUserString() {
		return null;
	}

	@Override
	public boolean isAway() {
		return false;
	}

}
