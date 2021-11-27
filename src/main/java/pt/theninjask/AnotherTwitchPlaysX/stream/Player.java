package pt.theninjask.AnotherTwitchPlaysX.stream;

public interface Player {

	boolean hasRequired();

	boolean isConnected();

	boolean isSetup();

	void setup();

	void setupAndConnect();

	void registerEventListener(Object listener);

	void unregisterEventListener(Object listener);

	void connect();

	void disconnect();

	void sendMessage(String message);

}