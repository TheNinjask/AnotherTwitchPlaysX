package pt.theninjask.AnotherTwitchPlaysX.twitch;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.Client.Builder.Server.SecurityType;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.util.StsUtil;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.data.SessionData;
import pt.theninjask.AnotherTwitchPlaysX.exception.AlreadyConnectedException;
import pt.theninjask.AnotherTwitchPlaysX.exception.NoSessionDataException;

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
	
	public static class TwitchListener{
		
		@Handler
		public void onMessage(ChannelMessageEvent event){
			//TODO
		}
		
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public boolean connect() {
		if(session==null)
			throw new NoSessionDataException();
		if(connected)
			throw new AlreadyConnectedException();
		try {
			client = Client.builder().nick(session.getNickname())
					.server().host(SERVER).port(PORT, SecurityType.SECURE).password(session.getOauth()).then()
					.management().stsStorageManager(StsUtil.getDefaultStorageManager()).then()
					.build();
			client.getEventManager().registerEventListener(new TwitchListener());
			client.addChannel(session.getChannel());
			client.connect();
			connected = true;
			return true;
		}catch (Exception e) {
			return false;
		}
	}
	
	public void disconnect() {
		connected = false;
		client.shutdown();
	}

}
