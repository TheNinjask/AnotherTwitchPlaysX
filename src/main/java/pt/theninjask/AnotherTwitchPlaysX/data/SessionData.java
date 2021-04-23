package pt.theninjask.AnotherTwitchPlaysX.data;


public class SessionData implements Data {
	
	private String nickname;
	
	private String oauth;
	
	private String channel;

	public SessionData() {}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getOauth() {
		return oauth;
	}

	public void setOauth(String oauth) {
		this.oauth = oauth;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
}
