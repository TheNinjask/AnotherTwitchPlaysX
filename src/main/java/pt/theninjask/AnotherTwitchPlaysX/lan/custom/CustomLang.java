package pt.theninjask.AnotherTwitchPlaysX.lan.custom;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import pt.theninjask.AnotherTwitchPlaysX.lan.AllCommandLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.ChatLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.CommandLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.ConstantsLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.ControlDataLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.EmbeddedModMenuLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.ExceptionsLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;
import pt.theninjask.AnotherTwitchPlaysX.lan.LoginLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.MainLoginLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.MainMenuLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.StringToKeyCodeLang;

public class CustomLang implements Lang, ICustom{

	private String title;
	private String id;
	private String okOpt;
	private String goBackOpt;
	private String na;
	private String savingSession;
	private String savingSessionTitle;
	private String lanTag;
	private String autoLoadModFail;
	private String autoLoadModFailTitle;
	private String updateNoticeTitle;
	private String updateNoticeWebsiteOption;
	private String updateNoticeDownloadOption;
	private String updateNoticeSkipOption;
	private String updateNoticeTitleContent;
	private CustomConstantsLang constants;
	private CustomExceptionsLang exceptions;
	private CustomMainMenuLang mainMenu;
	private CustomLoginLang login;
	private CustomEmbeddedModMenuLang embeddedModMenu;
	private CustomChatLang chat;
	private CustomAllCommandLang allCommand;
	private CustomCommandLang command;
	private CustomControlDataLang controlData;
	private CustomStringToKeyCodeLang stringToKeyCode;
	private CustomMainLoginLang mainLogin;

	public CustomLang(JsonObject json) {
		if (json != null)
			json = new JsonObject();
		for (Field field : CustomLang.class.getDeclaredFields()) {
			try {
				JsonElement jsonField = json.get(field.getName());
				if (String.class.isAssignableFrom(field.getType()))
					field.set(this, jsonField==null?null:jsonField.getAsString());
				else if(ICustom.class.isAssignableFrom(field.getType()))
					field.set(this, field.getType().getConstructor(JsonObject.class)
							.newInstance(jsonField==null?null:jsonField.getAsJsonObject()));
			} catch (IllegalArgumentException | IllegalAccessException | InstantiationException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}

	public CustomLang(Map<String, Object> map) {
		if (map != null)
			map = new HashMap<String, Object>();
		for (Field field : CustomLang.class.getDeclaredFields()) {
			try {
				if (String.class.isAssignableFrom(field.getType()))
					field.set(this, map.get(field.getName()));
				else if(ICustom.class.isAssignableFrom(field.getType()))
					field.set(this, field.getType().getConstructor(Map.class).newInstance(map.get(field.getName())));
			} catch (IllegalArgumentException | IllegalAccessException | InstantiationException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}

	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		for (Field field : CustomLang.class.getDeclaredFields()) {
			try {
				if (String.class.isAssignableFrom(field.getType()))
					json.addProperty(field.getName(), (String) field.get(this));
				else if(ICustom.class.isAssignableFrom(field.getType()))
					json.add(field.getName(), ((ICustom)field.get(this)).toJson());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return json;
	}
	
	public String getTitle() {
		return title;
	};

	public String getID() {
		return id;
	}

	public String getOkOpt() {
		return okOpt;
	}

	public String getGoBackOpt() {
		return goBackOpt;
	}

	public String getNA() {
		return na;
	}

	public String getSavingSession() {
		return savingSession;
	};

	public String getSavingSessionTitle() {
		return savingSessionTitle;
	};

	public String getLanTag() {
		return lanTag;
	}

	public String getAutoLoadModFail() {
		return autoLoadModFail;
	}

	public String getAutoLoadModFailTitle() {
		return autoLoadModFailTitle;
	}

	public String getUpdateNoticeTitle() {
		return updateNoticeTitle;
	}

	public String getUpdateNoticeWebsiteOption() {
		return updateNoticeWebsiteOption;
	}

	public String getUpdateNoticeDownloadOption() {
		return updateNoticeDownloadOption;
	}

	public String getUpdateNoticeSkipOption() {
		return updateNoticeSkipOption;
	}

	public String getUpdateNoticeTitleContent() {
		return updateNoticeTitleContent;
	}

	@Override
	public ConstantsLang getConstants() {
		return constants;
	}

	@Override
	public ExceptionsLang getExceptions() {
		return exceptions;
	}

	@Override
	public MainMenuLang getMainMenu() {
		return mainMenu;
	}

	@Override
	public LoginLang getLogin() {
		return login;
	}

	@Override
	public EmbeddedModMenuLang getEmbeddedModMenu() {
		return embeddedModMenu;
	}

	@Override
	public ChatLang getChat() {
		return chat;
	}

	@Override
	public AllCommandLang getAllCommand() {
		return allCommand;
	}

	@Override
	public CommandLang getCommand() {
		return command;
	}

	@Override
	public ControlDataLang getControlData() {
		return controlData;
	}

	@Override
	public StringToKeyCodeLang getStringToKeyCode() {
		return stringToKeyCode;
	}

	@Override
	public MainLoginLang getMainLogin() {
		return mainLogin;
	}

}
