package pt.theninjask.AnotherTwitchPlaysX.lan.custom;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import pt.theninjask.AnotherTwitchPlaysX.lan.ExceptionsLang;

public class CustomExceptionsLang implements ExceptionsLang, ICustom{

	private String alreadyConnected;
	private String modNotLoaded;
	private String noLeadDefined;
	private String noSessionData;
	private String notConnected;
	private String notSetup;
	private String notVerifyJar;
	private String notDirectory;

	public CustomExceptionsLang(JsonObject json) {
		if (json != null)
			for (Field field : CustomExceptionsLang.class.getDeclaredFields()) {
				try {
					JsonElement jsonField = json.get(field.getName());
					field.set(this, json.get(jsonField==null?null:jsonField.getAsString()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}
	
	public CustomExceptionsLang(Map<String, Object> map) {
		if (map != null)
			for (Field field : CustomExceptionsLang.class.getDeclaredFields()) {
				try {
					field.set(this, map.get(field.getName()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}
	
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		for (Field field : CustomExceptionsLang.class.getDeclaredFields()) {
			try {
				json.addProperty(field.getName(), (String) field.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	public String getAlreadyConnected() {
		return alreadyConnected;
	}

	public String getModNotLoaded() {
		return modNotLoaded;
	}

	public String getNoLeadDefined() {
		return noLeadDefined;
	}

	public String getNoSessionData() {
		return noSessionData;
	}

	public String getNotConnected() {
		return notConnected;
	}

	public String getNotSetup() {
		return notSetup;
	}

	public String getNotVerifyJar() {
		return notVerifyJar;
	}

	public String getNotDirectory() {
		return notDirectory;
	}

}
