package pt.theninjask.AnotherTwitchPlaysX.lan;

public interface CommandLang {

	public default String getLead() {
		return "Lead: ";
	};
	
	public default String getCmdCooldown() {
		return "Cmd Cooldown (sec): ";
	};
	
	public default String getCmdType() {
		return "Type: ";
	};
	
	public default String getVars() {
		return "Vars: ";
	};
	
	public default String getRemove() {
		return "Remove";
	};
	
	public default String getVarAdd() {
		return "ADD";
	};

	public default String getVarAddWarnMsg() {
		return "Standard (and Already In) Variables Ran Out!";
	};
	
	public default String getVarAddWarnTitle() {
		return "Ran Out Of Vars";
	};
	
	public default String getVarType() {
		return "Type of variable";
	};
	
	public default String getVarString() {
		return "String";
	};
	
	public default String getVarDigit() {
		return "Digit";
	};
	
	public default String getVarTitle() {
		return "Choose type";
	};
	
	public default String getViewMode() {
		return "View Mode: ";
	};
	
	public default String getViewNormal() {
		return "Normal";
	};
	
	public default String getViewVars() {
		return "Vars";
	};
	
	public default String getBack() {
		return "Back";
	};
	
	public default String getNotSavedMsg() {
		return "This command has not saved changes!";
	};
	
	public default String getNotSavedTitle() {
		return "COMMAND NOT SAVED";
	};
	
	public default String getSyntax() {
		return "Syntax";
	};
	
	public default String getSyntaxFull() {
		return "Syntax: %s";
	};
	
	public default String getSyntaxDemo() {
		return "Example: %s";
	};
	
	public default String getSyntaxTitle() {
		return "Syntax of command";
	};
	
	public default String getHelp() {
		return "Help";
	};
	
	public default String getSave() {
		return "Save";
	};
	
	public default String getDelete() {
		return "Delete";
	};
	
	public default String getAddControl() {
		return "ADD";
	};
	
	public default String getAddChooseControlType() {
		return "Choose a type";
	};
	
	public default String getAddChooseControlTypeTitle() {
		return "Adding New Control";
	};
	
	public default String getAddKeyboard() {
		return "Keyboard";
	};
	
	public default String getAddMouseClick() {
		return "Mouse Click";
	};
	
	public default String getAddMouseDrag() {
		return "Mouse Drag";
	};
	
	public default String getEdit() {
		return "Edit";
	};
	
}
