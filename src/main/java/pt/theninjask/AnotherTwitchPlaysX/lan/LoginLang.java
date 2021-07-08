package pt.theninjask.AnotherTwitchPlaysX.lan;

public interface LoginLang {

	public default String getOAuthButton() {
		return "Get OAuth";
	}

	public default String getLoginButton() {
		return "Set this Session";
	}

	public default String getTwitchField() {
		return "Name: ";
	}

	public default String getChannelField() {
		return "Channel: ";
	}

	public default String getOAuthField() {
		return "OAuth: ";
	}

	public default String getTwitchFieldTip() {
		return "Twitch Username";
	}

	public default String getChannelFieldTip() {
		return "Twitch Channel Name (If empty equals Twitch Username)";
	}

	public default String getOAuthFieldTip() {
		return "Token for authentication";
	}
	
	public default String getShowToken() {
		return "Show Token";
	}
	
	public default String getMissingUsernameMsg() {
		return "Please insert your twitch username.";
	}
	
	public default String getMissingUsernameTitle() {
		return "Missing Username";
	}
	
	public default String getMissingChannelMsg() {
		return "Please insert your twitch channel name.";
	}
	
	public default String getMissingChannelTitle() {
		return "Missing Channel Name";
	}
	
	public default String getMissingOAuthMsg() {
		return "Please insert your OAuth Token";
	}
	
	public default String getMissingOAuthTitle() {
		return "Missing OAuth";
	}
	
	public default String getMissingOAuthGet() {
		return "Go to get OAuth Token";
	}
	
	public default String getRememberSession() {
		return "Remember Session?";
	}
	
	public default String getMissingSecretMsg() {
		return "Please insert your Client Secret";
	}
	
	public default String getMissingSecretTitle() {
		return "Missing Client Secret";
	}
	
	public default String getMissingSecretGet() {
		return "Go to get Client Secret";
	}
	
	public default String getSecretField() {
		return "Client Secret: ";
	}
	
	public default String getSecretButton() {
		return "Get Secret";
	}
	
	public default String getSecretClear() {
		return "[x]";
	}
	
	public default String getSetSecret() {
		return "Set Secret";
	}
	
	public default String getViewSecret() {
		return "View Secret";
	}
	
	public default String getVideoField() {
		return "Video Id: ";
	}
	
	public default String getGoBack() {
		return "Go Back";
	}
	
}
