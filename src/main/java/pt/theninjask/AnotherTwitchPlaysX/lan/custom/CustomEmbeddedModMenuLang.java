package pt.theninjask.AnotherTwitchPlaysX.lan.custom;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import pt.theninjask.AnotherTwitchPlaysX.lan.EmbeddedModMenuLang;

public class CustomEmbeddedModMenuLang implements EmbeddedModMenuLang, ICustom {

	private String label;
	private String warning;
	private String goBack;

	public CustomEmbeddedModMenuLang(JsonObject json) {
		if (json != null)
			for (Field field : CustomEmbeddedModMenuLang.class.getDeclaredFields()) {
				try {
					JsonElement jsonField = json.get(field.getName());
					field.set(this, json.get(jsonField==null?null:jsonField.getAsString()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}
	
	public CustomEmbeddedModMenuLang(Map<String, Object> map) {
		if (map != null)
			for (Field field : CustomEmbeddedModMenuLang.class.getDeclaredFields()) {
				try {
					field.set(this, map.get(field.getName()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}
	
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		for (Field field : CustomEmbeddedModMenuLang.class.getDeclaredFields()) {
			try {
				json.addProperty(field.getName(), (String) field.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	public String getLabel() {
		return label;
	}

	public String getWarning() {
		return warning;
	}

	public String getGoBack() {
		return goBack;
	}

}
