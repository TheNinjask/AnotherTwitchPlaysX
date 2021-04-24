package pt.theninjask.AnotherTwitchPlaysX.data;

import java.util.List;

import pt.theninjask.AnotherTwitchPlaysX.util.Pair;

public class CommandData implements Data {

	private String lead;
	
	private CommandType type;
	
	private List<ControlData> controls;
	
	private List<Pair<String, CommandVarType>> vars;
	
	public CommandData() {}
	
	public String getLead() {
		return lead;
	}

	public void setLead(String lead) {
		this.lead = lead;
	}

	public CommandType getType() {
		return type;
	}

	public void setType(CommandType type) {
		this.type = type;
	}

	public List<ControlData> getControls() {
		return controls;
	}

	public void setControls(List<ControlData> controls) {
		this.controls = controls;
	}

	public List<Pair<String, CommandVarType>> getVars() {
		return vars;
	}

	public void setVars(List<Pair<String, CommandVarType>> vars) {
		this.vars = vars;
	}
	
}
