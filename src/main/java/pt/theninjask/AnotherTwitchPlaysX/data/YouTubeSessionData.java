package pt.theninjask.AnotherTwitchPlaysX.data;

public class YouTubeSessionData implements Data{

	private String secret;
	
	private String videoId;
	
	public YouTubeSessionData() {
	}
	
	public YouTubeSessionData(String secret, String videoId){
		this.secret = secret;
		this.videoId = videoId;	
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	
}
