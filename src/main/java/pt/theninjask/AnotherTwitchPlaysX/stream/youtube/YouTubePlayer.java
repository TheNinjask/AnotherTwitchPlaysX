package pt.theninjask.AnotherTwitchPlaysX.stream.youtube;

import java.util.logging.Level;

import com.google.api.services.youtube.model.LiveChatMessage;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.data.YouTubeSessionData;
import pt.theninjask.AnotherTwitchPlaysX.exception.AlreadyConnectedException;
import pt.theninjask.AnotherTwitchPlaysX.exception.NoSessionDataException;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class YouTubePlayer {

	private static YouTubePlayer singleton = new YouTubePlayer();

	private YouTubeChatService client;

	private YouTubeSessionData session;

	private boolean connected;

	private YouTubePlayer() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", YouTubePlayer.class.getSimpleName()));
		this.connected = false;
	}

	public static YouTubePlayer getInstance() {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.getInstance()", YouTubePlayer.class.getSimpleName()));
		return singleton;
	}

	public boolean hasRequired() {
		return session != null;
	}

	public void setSession(YouTubeSessionData session) {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.setSession()", YouTubePlayer.class.getSimpleName()));
		this.session = session;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setup() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.setup()", YouTubePlayer.class.getSimpleName()));
		if (session == null)
			throw new NoSessionDataException();
		if (connected)
			throw new AlreadyConnectedException();
		client = new YouTubeChatService();
	}

	public void setupAndConnect() {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.setupAndConnect()", YouTubePlayer.class.getSimpleName()));
		if (session == null)
			throw new NoSessionDataException();
		if (connected)
			throw new AlreadyConnectedException();
		client = new YouTubeChatService();
		client.start(session.getVideoId(), session.getSecret());
		connected = true;
	}

	public void registerEventListener(Object listener) {
		client.subscribe(listener);
	}

	public void unregisterEventListener(Object listener) {
		client.unsubscribe(listener);
	}

	public void connect() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.connect()", YouTubePlayer.class.getSimpleName()));
		if (client != null) {
			client.start(session.getVideoId(), session.getSecret());
			connected = true;
		}
	}

	public void disconnect() {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.disconnect()", YouTubePlayer.class.getSimpleName()));
		connected = false;
		if (client != null)
			client.stop();
		client = null;
	}

	public void sendMessage(String message) {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.sendMessage()", YouTubePlayer.class.getSimpleName()));
		client.postMessage(message);
	}

	@Handler
	public void onMessage(LiveChatMessage message) {
		System.out.println(message.getSnippet().getDisplayMessage());
	}

}
