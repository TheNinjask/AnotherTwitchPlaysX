package pt.theninjask.AnotherTwitchPlaysX.lan.custom;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import pt.theninjask.AnotherTwitchPlaysX.lan.ConstantsLang;

public class CustomConstantsLang implements ConstantsLang, ICustom {

	private String defaultErrorTitle;

	private String browserNotSupported;

	private String modInfoTitle;

	private String modInfo;

	private String modWarnTitle;

	private String modWarn;

	private String READMEUnknown;

	private String READMENetExceptionTitle;

	private String READMENetException;

	private String READMEExceptionTitle;

	private String READMEException;

	public CustomConstantsLang(JsonObject json) {
		if (json != null)
			for (Field field : CustomConstantsLang.class.getDeclaredFields()) {
				try {
					JsonElement jsonField = json.get(field.getName());
					field.set(this, json.get(jsonField==null?null:jsonField.getAsString()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}
	
	public CustomConstantsLang(Map<String, Object> map) {
		if (map != null)
			for (Field field : CustomConstantsLang.class.getDeclaredFields()) {
				try {
					field.set(this, map.get(field.getName()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}
	
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		for (Field field : CustomConstantsLang.class.getDeclaredFields()) {
			try {
				json.addProperty(field.getName(), (String) field.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	public String getDefaultErrorTitle() {
		return defaultErrorTitle;
	}

	public String getBrowserNotSupported() {
		return browserNotSupported;
	}

	public String getModInfoTitle() {
		return modInfoTitle;
	}

	public String getModInfo() {
		return modInfo;
	}

	public String getModWarnTitle() {
		return modWarnTitle;
	}

	public String getModWarn() {
		return modWarn;
	}

	public String getREADMEUnknown() {
		return READMEUnknown;
	}

	public String getREADMENetExceptionTitle() {
		return READMENetExceptionTitle;
	}

	public String getREADMENetException() {
		return READMENetException;
	}

	public String getREADMEExceptionTitle() {
		return READMEExceptionTitle;
	}

	public String getREADMEException() {
		return READMEException;
	}

}
