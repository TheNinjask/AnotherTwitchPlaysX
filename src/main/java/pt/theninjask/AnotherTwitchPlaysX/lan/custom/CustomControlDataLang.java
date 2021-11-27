package pt.theninjask.AnotherTwitchPlaysX.lan.custom;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import pt.theninjask.AnotherTwitchPlaysX.lan.ControlDataLang;

public class CustomControlDataLang implements ControlDataLang, ICustom {

	private String typeKey;
	private String typeButton;
	private String keyNone;
	private String keyCurrent;
	private String buttonNone;
	private String buttonLeft;
	private String buttonRight;
	private String buttonMiddle;
	private String duration;
	private String aftermath;
	private String x;
	private String xClear;
	private String abs;
	private String rel;
	private String y;
	private String yclear;
	private String initialCoords;
	private String finalCoords;
	private String finalX;
	private String finalXClear;
	private String finalY;
	private String finalYClear;
	private String varNone;
	private String index;
	private String remove;

	public CustomControlDataLang(JsonObject json) {
		if (json != null)
			for (Field field : CustomControlDataLang.class.getDeclaredFields()) {
				try {
					JsonElement jsonField = json.get(field.getName());
					field.set(this, json.get(jsonField == null ? null : jsonField.getAsString()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}

	public CustomControlDataLang(Map<String, Object> map) {
		if (map != null)
			for (Field field : CustomControlDataLang.class.getDeclaredFields()) {
				try {
					field.set(this, map.get(field.getName()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		for (Field field : CustomControlDataLang.class.getDeclaredFields()) {
			try {
				json.addProperty(field.getName(), (String) field.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	public String getTypeKey() {
		return typeKey;
	}

	public String getTypeButton() {
		return typeButton;
	}

	public String getKeyNone() {
		return keyNone;
	}

	public String getKeyCurrent() {
		return keyCurrent;
	}

	public String getButtonNone() {
		return buttonNone;
	}

	public String getButtonLeft() {
		return buttonLeft;
	}

	public String getButtonRight() {
		return buttonRight;
	}

	public String getButtonMiddle() {
		return buttonMiddle;
	}

	public String getDuration() {
		return duration;
	}

	public String getAftermath() {
		return aftermath;
	}

	public String getX() {
		return x;
	}

	public String getXClear() {
		return xClear;
	}

	public String getAbs() {
		return abs;
	}

	public String getRel() {
		return rel;
	}

	public String getY() {
		return y;
	}

	public String getYClear() {
		return yclear;
	}

	public String getInitialCoords() {
		return initialCoords;
	}

	public String getFinalCoords() {
		return finalCoords;
	}

	public String getFinalX() {
		return finalX;
	}

	public String getFinalXClear() {
		return finalXClear;
	}

	public String getFinalY() {
		return finalY;
	}

	public String getFinalYClear() {
		return finalYClear;
	}

	public String getVarNone() {
		return varNone;
	}

	public String getIndex() {
		return index;
	}

	public String getRemove() {
		return remove;
	}

}
