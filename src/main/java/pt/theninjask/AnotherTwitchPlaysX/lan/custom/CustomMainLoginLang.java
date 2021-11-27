package pt.theninjask.AnotherTwitchPlaysX.lan.custom;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import pt.theninjask.AnotherTwitchPlaysX.lan.MainLoginLang;

public class CustomMainLoginLang implements MainLoginLang, ICustom{

	private String start;
	private String connect;
	private String connected;
	private String clear;

	public CustomMainLoginLang(JsonObject json) {
		if (json != null)
			for (Field field : CustomMainLoginLang.class.getDeclaredFields()) {
				try {
					JsonElement jsonField = json.get(field.getName());
					field.set(this, json.get(jsonField==null?null:jsonField.getAsString()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}
	
	public CustomMainLoginLang(Map<String, Object> map) {
		if (map != null)
			for (Field field : CustomMainLoginLang.class.getDeclaredFields()) {
				try {
					field.set(this, map.get(field.getName()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		for (Field field : CustomMainLoginLang.class.getDeclaredFields()) {
			try {
				json.addProperty(field.getName(), (String) field.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	
	public String getStart() {
		return start;
	}

	public String getConnect() {
		return connect;
	}

	public String getConnected() {
		return connected;
	}

	public String getClear() {
		return clear;
	}
}
