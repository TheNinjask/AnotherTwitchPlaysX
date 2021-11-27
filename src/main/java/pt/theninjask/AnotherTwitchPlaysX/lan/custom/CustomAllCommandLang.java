package pt.theninjask.AnotherTwitchPlaysX.lan.custom;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import pt.theninjask.AnotherTwitchPlaysX.lan.AllCommandLang;

public class CustomAllCommandLang implements AllCommandLang, ICustom{

	private String create;
	private String load;
	private String back;
	private String listOfCmds;
	private String insert;
	private String codes;
	private String help;
	private String popOut;
	private String save;
	private String saveMsg;
	private String saveTitle;

	public CustomAllCommandLang(JsonObject json) {
		if (json != null)
			for (Field field : CustomAllCommandLang.class.getDeclaredFields()) {
				try {
					JsonElement jsonField = json.get(field.getName());
					field.set(this, json.get(jsonField==null?null:jsonField.getAsString()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}
	
	public CustomAllCommandLang(Map<String, Object> map) {
		if (map != null)
			for (Field field : CustomAllCommandLang.class.getDeclaredFields()) {
				try {
					field.set(this, map.get(field.getName()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}

	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		for (Field field : CustomAllCommandLang.class.getDeclaredFields()) {
			try {
				json.addProperty(field.getName(), (String) field.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return json;
	}
	
	public String getCreate() {
		return create;
	};

	public String getLoad() {
		return load;
	};

	public String getBack() {
		return back;
	};

	public String getListOfCmds() {
		return listOfCmds;
	};

	public String getInsert() {
		return insert;
	};

	public String getCodes() {
		return codes;
	};

	public String getHelp() {
		return help;
	};

	public String getPopOut() {
		return popOut;
	};

	public String getSave() {
		return save;
	};

	public String getSaveMsg() {
		return saveMsg;
	};

	public String getSaveTitle() {
		return saveTitle;
	};

}
