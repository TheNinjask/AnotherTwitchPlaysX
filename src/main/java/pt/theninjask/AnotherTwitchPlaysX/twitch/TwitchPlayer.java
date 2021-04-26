package pt.theninjask.AnotherTwitchPlaysX.twitch;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.Client.Builder.Server.SecurityType;
import org.kitteh.irc.client.library.feature.twitch.TwitchListener;
import org.kitteh.irc.client.library.util.StsUtil;

import pt.theninjask.AnotherTwitchPlaysX.data.SessionData;
import pt.theninjask.AnotherTwitchPlaysX.exception.AlreadyConnectedException;
import pt.theninjask.AnotherTwitchPlaysX.exception.NoSessionDataException;
import pt.theninjask.AnotherTwitchPlaysX.exception.NotSetupException;

public class TwitchPlayer {

	private static final String SERVER = "irc.chat.twitch.tv";
	
	private static final int PORT = 6697;
	
	private Client client;
	
	private SessionData session;
	
	private boolean connected;
	
	private static TwitchPlayer instance = new TwitchPlayer();
	
	private TwitchPlayer() {
		this.connected = false;
	}
	
	public static TwitchPlayer getInstance() {
		return instance;
	}

	public void setSession(SessionData session) {
		this.session = session;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public void setup() {
		if(session==null)
			throw new NoSessionDataException();
		if(connected)
			throw new AlreadyConnectedException();
		client = Client.builder().nick(session.getNickname())
				.server().host(SERVER).port(PORT, SecurityType.SECURE).password(session.getOauth()).then()
				.management().stsStorageManager(StsUtil.getDefaultStorageManager()).then()
				.build();
		client.getEventManager().registerEventListener(new TwitchListener(client));
		client.addChannel(session.getChannel());
	}
	
	public void setupAndConnect() {
		if(session==null)
			throw new NoSessionDataException();
		if(connected)
			throw new AlreadyConnectedException();
		client = Client.builder().nick(session.getNickname())
					.server().host(SERVER).port(PORT, SecurityType.SECURE).password(session.getOauth()).then()
					.management().stsStorageManager(StsUtil.getDefaultStorageManager()).then()
					.build();
		client.getEventManager().registerEventListener(new TwitchListener(client));
		client.addChannel(session.getChannel());
		client.connect();
		connected = true;
	}
	
	public void registerEventListener(Object listener) {
		if(client == null)
			throw new NotSetupException();
		client.getEventManager().registerEventListener(listener);
	}
	
	public void unregisterEventListener(Object listener) {
		if(client == null)
			throw new NotSetupException();
		client.getEventManager().unregisterEventListener(listener);
	}
	
	public void connect() {
		client.connect();
		connected = true;
	}
	
	public void disconnect() {
		connected = false;
		client.shutdown();
		client = null;
	}

}
