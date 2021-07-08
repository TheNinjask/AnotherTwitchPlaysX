package pt.theninjask.AnotherTwitchPlaysX.lan;

public interface ChatLang {
	
	public default String getTitle() {
		return "%s's Chat";
	}
	
	public default String getSend() {
		return "Send";
	}

	public default String getUser() {
		return "ATPX.User";
	};
	
}
