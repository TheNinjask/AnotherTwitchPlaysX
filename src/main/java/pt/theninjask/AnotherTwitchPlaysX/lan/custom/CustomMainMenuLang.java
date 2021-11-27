package pt.theninjask.AnotherTwitchPlaysX.lan.custom;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import pt.theninjask.AnotherTwitchPlaysX.lan.MainMenuLang;

public class CustomMainMenuLang implements MainMenuLang, ICustom{

	private String connect;
	private String disconnect;
	private String setCommands;
	private String mod;
	private String start;
	private String stop;
	private String changeSession;
	private String showChat;
	private String hideChat;
	private String currentChatSize;
	private String infinite;
	private String chatOptions;
	private String lightMode;
	private String nightMode;
	private String twitchMode;
	private String transparent;
	private String semiSolid;
	private String solid;
	private String isChatOnTop;
	private String showInputTextBoxInChat;
	private String minecraftStyle;
	private String twitchStyle;
	private String font;
	private String fontSize;
	private String sponsorCreator;
	private String gotIt;
	private String nevermind;
	private String sponsorTitle;
	private String sponsorMsgFirst;
	private String sponsorMsgTime;
	private String sponsorMsgDemo;
	private String yesNoCheck;
	private String yesCheck;
	private String youtubeTokenError;

	public CustomMainMenuLang(JsonObject json) {
		if (json != null)
			for (Field field : CustomMainMenuLang.class.getDeclaredFields()) {
				try {
					JsonElement jsonField = json.get(field.getName());
					field.set(this, json.get(jsonField==null?null:jsonField.getAsString()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}
	
	public CustomMainMenuLang(Map<String, Object> map) {
		if (map != null)
			for (Field field : CustomMainMenuLang.class.getDeclaredFields()) {
				try {
					field.set(this, map.get(field.getName()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}

	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		for (Field field : CustomMainMenuLang.class.getDeclaredFields()) {
			try {
				json.addProperty(field.getName(), (String) field.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return json;
	}
	
	public String getConnect() {
		return connect;
	}

	public String getDisconnect() {
		return disconnect;
	}

	public String getSetCommands() {
		return setCommands;
	}

	public String getMod() {
		return mod;
	}

	public String getStart() {
		return start;
	}

	public String getStop() {
		return stop;
	}

	public String getChangeSession() {
		return changeSession;
	}

	public String getShowChat() {
		return showChat;
	}

	public String getHideChat() {
		return hideChat;
	}

	public String getCurrentChatSize() {
		return currentChatSize;
	}

	public String getInfinite() {
		return infinite;
	}

	public String getChatOptions() {
		return chatOptions;
	}

	public String getLightMode() {
		return lightMode;
	}

	public String getNightMode() {
		return nightMode;
	}

	public String getTwitchMode() {
		return twitchMode;
	}

	public String getTransparent() {
		return transparent;
	}

	public String getSemiSolid() {
		return semiSolid;
	}

	public String getSolid() {
		return solid;
	}

	public String getIsChatOnTop() {
		return isChatOnTop;
	}

	public String getShowInputTextBoxInChat() {
		return showInputTextBoxInChat;
	}

	public String getMinecraftStyle() {
		return minecraftStyle;
	}

	public String getTwitchStyle() {
		return twitchStyle;
	}

	public String getFont() {
		return font;
	}

	public String getFontSize() {
		return fontSize;
	}

	public String getSponsorCreator() {
		return sponsorCreator;
	}

	public String getGotIt() {
		return gotIt;
	}

	public String getNevermind() {
		return nevermind;
	}

	public String getSponsorTitle() {
		return sponsorTitle;
	}

	public String getSponsorMsgFirst() {
		return sponsorMsgFirst;
	}

	public String getSponsorMsgTime() {
		return sponsorMsgTime;
	}

	public String getSponsorMsgDemo() {
		return sponsorMsgDemo;
	}

	public String getYesNoCheck() {
		return yesNoCheck;
	}

	public String getYesCheck() {
		return yesCheck;
	}

	public String getYouTubeTokenError() {
		return youtubeTokenError;
	}

}
