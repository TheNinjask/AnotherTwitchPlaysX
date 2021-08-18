package pt.theninjask.AnotherTwitchPlaysX.lan;

public interface ExceptionsLang {

	public default String getAlreadyConnected() {
		return "TwitchPlayer is already connected";
	}
	
	public default String getModNotLoaded() {
		return "Mod could not be loaded!";
	}
	
	public default String getNoLeadDefined() {
		return "No Lead provided";
	}
	
	public default String getNoSessionData() {
		return "No SessionData provided";
	}
	
	public default String getNotConnected() {
		return "Twitch Player is not connected yet";
	}
	
	public default String getNotSetup() {
		return "Twitch Player has yet to be setup";
	}
	
	public default String getNotVerifyJar() {
		return "Could not verify jar!";
	}
	
	public default String getNotDirectory() {
		return "%s is not a Directory!";
	}
	
}
