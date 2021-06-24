package pt.theninjask.AnotherTwitchPlaysX.lan;

public interface TwitchChatLang {
	
	public default String getTitle() {
		return "%s's Twitch Chat";
	}
	
	public default String getSend() {
		return "Send";
	}
	
}
