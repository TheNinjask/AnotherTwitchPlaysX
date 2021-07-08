package pt.theninjask.AnotherTwitchPlaysX.stream.twitch;

import java.util.logging.Level;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.Client.Builder.Server.SecurityType;
import org.kitteh.irc.client.library.feature.twitch.TwitchSupport;
import org.kitteh.irc.client.library.util.StsUtil;

import pt.theninjask.AnotherTwitchPlaysX.data.TwitchSessionData;
import pt.theninjask.AnotherTwitchPlaysX.exception.AlreadyConnectedException;
import pt.theninjask.AnotherTwitchPlaysX.exception.NoSessionDataException;
import pt.theninjask.AnotherTwitchPlaysX.exception.NotConnectedException;
import pt.theninjask.AnotherTwitchPlaysX.exception.NotSetupException;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class TwitchPlayer {

	private static final String SERVER = "irc.chat.twitch.tv";

	private static final int PORT = 6697;

	private Client client;

	private TwitchSessionData session;

	private boolean connected;

	private static TwitchPlayer instance = new TwitchPlayer();

	private TwitchPlayer() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", TwitchPlayer.class.getSimpleName()));
		this.connected = false;
	}

	public static TwitchPlayer getInstance() {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.getInstance()", TwitchPlayer.class.getSimpleName()));
		return instance;
	}

	public boolean hasRequired() {
		return session != null;
	}

	public void setSession(TwitchSessionData session) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.setSession()", TwitchPlayer.class.getSimpleName()));
		this.session = session;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setup() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.setup()", TwitchPlayer.class.getSimpleName()));
		if (session == null)
			throw new NoSessionDataException();
		if (connected)
			throw new AlreadyConnectedException();
		client = Client.builder().nick(session.getNickname().toLowerCase()).server().host(SERVER)
				.port(PORT, SecurityType.SECURE).password(session.getOauth()).then().management()
				.stsStorageManager(StsUtil.getDefaultStorageManager()).then().build();
		TwitchSupport.addSupport(client);
		if (session.getChannel() != null && !session.getChannel().isEmpty())
			client.addChannel(session.getChannel().toLowerCase());
		else
			client.addChannel(session.getNickname().toLowerCase());
	}

	public void setupAndConnect() {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.setupAndConnect()", TwitchPlayer.class.getSimpleName()));
		if (session == null)
			throw new NoSessionDataException();
		if (connected)
			throw new AlreadyConnectedException();
		client = Client.builder().nick(session.getNickname().toLowerCase()).server().host(SERVER)
				.port(PORT, SecurityType.SECURE).password(session.getOauth()).then().management()
				.stsStorageManager(StsUtil.getDefaultStorageManager()).then().build();
		TwitchSupport.addSupport(client);
		if (session.getChannel() != null && !session.getChannel().isEmpty())
			client.addChannel(session.getChannel().toLowerCase());
		else
			client.addChannel(session.getNickname().toLowerCase());
		client.connect();
		connected = true;
	}

	public void registerEventListener(Object listener) {
		if (client == null)
			throw new NotSetupException();
		client.getEventManager().registerEventListener(listener);
	}

	public void unregisterEventListener(Object listener) {
		if (client == null)
			throw new NotSetupException();
		client.getEventManager().unregisterEventListener(listener);
	}

	public void connect() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.connect()", TwitchPlayer.class.getSimpleName()));
		if (client != null) {
			client.connect();
			connected = true;
		}
	}

	public void disconnect() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.disconnect()", TwitchPlayer.class.getSimpleName()));
		connected = false;
		if (client != null)
			client.shutdown();
		client = null;
	}

	public void sendMessage(String message) {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.sendMessage()", TwitchPlayer.class.getSimpleName()));
		if (session == null)
			throw new NoSessionDataException();
		if (!connected)
			throw new NotConnectedException();
		client.getChannel(
				session.getChannel() != null && !session.getChannel().isEmpty() ? session.getChannel().toLowerCase()
						: session.getNickname().toLowerCase())
				.ifPresent(c -> {
					c.sendMessage(message);
				});
	}

}
