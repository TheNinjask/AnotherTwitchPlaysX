package pt.theninjask.AnotherTwitchPlaysX.stream.youtube;

import java.util.logging.Level;

import pt.theninjask.AnotherTwitchPlaysX.App;
import pt.theninjask.AnotherTwitchPlaysX.data.YouTubeSessionData;
import pt.theninjask.AnotherTwitchPlaysX.exception.AlreadyConnectedException;
import pt.theninjask.AnotherTwitchPlaysX.exception.NoSessionDataException;
import pt.theninjask.AnotherTwitchPlaysX.exception.NotSetupException;
import pt.theninjask.AnotherTwitchPlaysX.stream.Player;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class YouTubePlayer implements Player {

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

	@Override
	public boolean hasRequired() {
		return session != null;
	}

	public void setSession(YouTubeSessionData session) {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.setSession()", YouTubePlayer.class.getSimpleName()));
		this.session = session;
	}

	@Override
	public boolean isConnected() {
		return connected;
	}
	
	@Override
	public boolean isSetup() {
		return client!=null;
	}

	@Override
	public void setup() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.setup()", YouTubePlayer.class.getSimpleName()));
		if (session == null)
			throw new NoSessionDataException();
		if (connected)
			throw new AlreadyConnectedException();
		client = new YouTubeChatService(App.ID, App.NAME);
	}

	@Override
	public void setupAndConnect() {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.setupAndConnect()", YouTubePlayer.class.getSimpleName()));
		/*if (session == null)
			throw new NoSessionDataException();
		if (connected)
			throw new AlreadyConnectedException();
		client = new YouTubeChatService(App.ID, App.NAME);*/
		setup();
		client.start(session.getVideoId(), session.getSecret());
		connected = true;
	}

	@Override
	public void registerEventListener(Object listener) {
		if(client==null)
			throw new NotSetupException();
		client.subscribe(listener);
	}

	@Override
	public void unregisterEventListener(Object listener) {
		if(client==null)
			throw new NotSetupException();
		client.unsubscribe(listener);
	}

	@Override
	public void connect() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.connect()", YouTubePlayer.class.getSimpleName()));
		if (client != null) {
			client.start(session.getVideoId(), session.getSecret());
			connected = true;
		}
	}

	@Override
	public void disconnect() {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.disconnect()", YouTubePlayer.class.getSimpleName()));
		connected = false;
		if (client != null)
			client.stop();
		client = null;
	}

	@Override
	public void sendMessage(String message) {
		Constants.printVerboseMessage(Level.INFO,
				String.format("%s.sendMessage()", YouTubePlayer.class.getSimpleName()));
		client.postMessage(message);
	}

}
