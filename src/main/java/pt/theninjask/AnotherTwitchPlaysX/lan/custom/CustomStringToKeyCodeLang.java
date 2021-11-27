package pt.theninjask.AnotherTwitchPlaysX.lan.custom;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import pt.theninjask.AnotherTwitchPlaysX.lan.StringToKeyCodeLang;

public class CustomStringToKeyCodeLang implements StringToKeyCodeLang, ICustom{

	private String back;
	private String reset;
	private String remove;

	public CustomStringToKeyCodeLang(JsonObject json) {
		if (json != null)
			for (Field field : CustomStringToKeyCodeLang.class.getDeclaredFields()) {
				try {
					JsonElement jsonField = json.get(field.getName());
					field.set(this, json.get(jsonField==null?null:jsonField.getAsString()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}
	
	public CustomStringToKeyCodeLang(Map<String, Object> map) {
		if (map != null)
			for (Field field : CustomStringToKeyCodeLang.class.getDeclaredFields()) {
				try {
					field.set(this, map.get(field.getName()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		for (Field field : CustomStringToKeyCodeLang.class.getDeclaredFields()) {
			try {
				json.addProperty(field.getName(), (String) field.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return json;
	}
	
	public String getBack() {
		return back;
	}

	public String getReset() {
		return reset;
	}

	public String getRemove() {
		return remove;
	}

}
