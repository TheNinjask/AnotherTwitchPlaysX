package pt.theninjask.AnotherTwitchPlaysX.lan.custom;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import pt.theninjask.AnotherTwitchPlaysX.lan.ChatLang;

public class CustomChatLang implements ChatLang, ICustom{

	private String title;
	private String send;
	private String user;

	public CustomChatLang(JsonObject json) {
		if (json != null)
			for (Field field : CustomChatLang.class.getDeclaredFields()) {
				try {
					JsonElement jsonField = json.get(field.getName());
					field.set(this, json.get(jsonField==null?null:jsonField.getAsString()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}
	
	public CustomChatLang(Map<String, Object> map) {
		if (map != null)
			for (Field field : CustomChatLang.class.getDeclaredFields()) {
				try {
					field.set(this, map.get(field.getName()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		for (Field field : CustomChatLang.class.getDeclaredFields()) {
			try {
				json.addProperty(field.getName(), (String) field.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return json;
	}
	
	public String getTitle() {
		return title;
	}

	public String getSend() {
		return send;
	}

	public String getUser() {
		return user;
	};

}
