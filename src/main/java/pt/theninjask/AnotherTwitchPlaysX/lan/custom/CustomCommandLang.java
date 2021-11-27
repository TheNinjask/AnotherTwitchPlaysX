package pt.theninjask.AnotherTwitchPlaysX.lan.custom;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import pt.theninjask.AnotherTwitchPlaysX.lan.CommandLang;

public class CustomCommandLang implements CommandLang, ICustom{

	private String lead;
	private String cmdCooldown;
	private String cmdType;
	private String vars;
	private String remove;
	private String varAdd;
	private String varAddWarnMsg;
	private String varAddWarnTitle;
	private String varType;
	private String varString;
	private String varDigit;
	private String varTitle;
	private String viewMode;
	private String viewNormal;
	private String viewVars;
	private String back;
	private String notSavedMsg;
	private String notSavedTitle;
	private String syntax;
	private String syntaxFull;
	private String syntaxDemo;
	private String syntaxTitle;
	private String help;
	private String save;
	private String delete;
	private String addControl;
	private String addChooseControlType;
	private String addChooseControlTypeTitle;
	private String addKeyboard;
	private String addMouseClick;
	private String addMouseDrag;
	private String edit;

	public CustomCommandLang(JsonObject json) {
		if (json != null)
			for (Field field : CustomCommandLang.class.getDeclaredFields()) {
				try {
					JsonElement jsonField = json.get(field.getName());
					field.set(this, json.get(jsonField==null?null:jsonField.getAsString()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}
	
	public CustomCommandLang(Map<String, Object> map) {
		if (map != null)
			for (Field field : CustomCommandLang.class.getDeclaredFields()) {
				try {
					field.set(this, map.get(field.getName()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
	}
	
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		for (Field field : CustomCommandLang.class.getDeclaredFields()) {
			try {
				json.addProperty(field.getName(), (String) field.get(this));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	public String getLead() {
		return lead;
	};

	public String getCmdCooldown() {
		return cmdCooldown;
	};

	public String getCmdType() {
		return cmdType;
	};

	public String getVars() {
		return vars;
	};

	public String getRemove() {
		return remove;
	};

	public String getVarAdd() {
		return varAdd;
	};

	public String getVarAddWarnMsg() {
		return varAddWarnMsg;
	};

	public String getVarAddWarnTitle() {
		return varAddWarnTitle;
	};

	public String getVarType() {
		return varType;
	};

	public String getVarString() {
		return varString;
	};

	public String getVarDigit() {
		return varDigit;
	};

	public String getVarTitle() {
		return varTitle;
	};

	public String getViewMode() {
		return viewMode;
	};

	public String getViewNormal() {
		return viewNormal;
	};

	public String getViewVars() {
		return viewVars;
	};

	public String getBack() {
		return back;
	};

	public String getNotSavedMsg() {
		return notSavedMsg;
	};

	public String getNotSavedTitle() {
		return notSavedTitle;
	};

	public String getSyntax() {
		return syntax;
	};

	public String getSyntaxFull() {
		return syntaxFull;
	};

	public String getSyntaxDemo() {
		return syntaxDemo;
	};

	public String getSyntaxTitle() {
		return syntaxTitle;
	};

	public String getHelp() {
		return help;
	};

	public String getSave() {
		return save;
	};

	public String getDelete() {
		return delete;
	};

	public String getAddControl() {
		return addControl;
	};

	public String getAddChooseControlType() {
		return addChooseControlType;
	};

	public String getAddChooseControlTypeTitle() {
		return addChooseControlTypeTitle;
	};

	public String getAddKeyboard() {
		return addKeyboard;
	};

	public String getAddMouseClick() {
		return addMouseClick;
	};

	public String getAddMouseDrag() {
		return addMouseDrag;
	};

	public String getEdit() {
		return edit;
	};

}
