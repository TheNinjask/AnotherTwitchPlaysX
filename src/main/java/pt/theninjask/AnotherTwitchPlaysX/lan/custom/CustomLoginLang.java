package pt.theninjask.AnotherTwitchPlaysX.lan.custom;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import pt.theninjask.AnotherTwitchPlaysX.lan.LoginLang;

public class CustomLoginLang implements LoginLang, ICustom{

	private String oauthButton;
	private String loginButton;
	private String twitchField;
	private String channelField;
	private String oauthField;
	private String twitchFieldTip;
	private String channelFieldTip;
	private String oauthFieldTip;
	private String showToken;
	private String missingUsernameMsg;
	private String missingUsernameTitle;
	private String missingChannelMsg;
	private String missingChannelTitle;
	private String missingOAuthMsg;
	private String missingOAuthTitle;
	private String missingOAuthGet;
	private String rememberSession;
	private String missingSecretMsg;
	private String missingSecretTitle;
	private String missingSecretGet;
	private String secretField;
	private String secretButton;
	private String secretClear;
	private String setSecret;
	private String viewSecret;
	private String videoField;
	private String goBack;

	public CustomLoginLang(JsonObject json) {
		if (json != null)
			for (Field field : CustomLoginLang.class.getDeclaredFields()) {
				try {
					JsonElement jsonField = json.get(field.getName());
					field.set(this, json.get(jsonField==null?null:jsonField.getAsString()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}
	
	public CustomLoginLang(Map<String, Object> map) {
		if (map != null)
			for (Field field : CustomLoginLang.class.getDeclaredFields()) {
				try {
					field.set(this, map.get(field.getName()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}
	
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		for (Field field : CustomLoginLang.class.getDeclaredFields()) {
			try {
				json.addProperty(field.getName(), (String) field.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	public String getOAuthButton() {
		return oauthButton;
	}

	public String getLoginButton() {
		return loginButton;
	}

	public String getTwitchField() {
		return twitchField;
	}

	public String getChannelField() {
		return channelField;
	}

	public String getOAuthField() {
		return oauthField;
	}

	public String getTwitchFieldTip() {
		return twitchFieldTip;
	}

	public String getChannelFieldTip() {
		return channelFieldTip;
	}

	public String getOAuthFieldTip() {
		return oauthFieldTip;
	}

	public String getShowToken() {
		return showToken;
	}

	public String getMissingUsernameMsg() {
		return missingUsernameMsg;
	}

	public String getMissingUsernameTitle() {
		return missingUsernameTitle;
	}

	public String getMissingChannelMsg() {
		return missingChannelMsg;
	}

	public String getMissingChannelTitle() {
		return missingChannelTitle;
	}

	public String getMissingOAuthMsg() {
		return missingOAuthMsg;
	}

	public String getMissingOAuthTitle() {
		return missingOAuthTitle;
	}

	public String getMissingOAuthGet() {
		return missingOAuthGet;
	}

	public String getRememberSession() {
		return rememberSession;
	}

	public String getMissingSecretMsg() {
		return missingSecretMsg;
	}

	public String getMissingSecretTitle() {
		return missingSecretTitle;
	}

	public String getMissingSecretGet() {
		return missingSecretGet;
	}

	public String getSecretField() {
		return secretField;
	}

	public String getSecretButton() {
		return secretButton;
	}

	public String getSecretClear() {
		return secretClear;
	}

	public String getSetSecret() {
		return setSecret;
	}

	public String getViewSecret() {
		return viewSecret;
	}

	public String getVideoField() {
		return videoField;
	}

	public String getGoBack() {
		return goBack;
	}

}
