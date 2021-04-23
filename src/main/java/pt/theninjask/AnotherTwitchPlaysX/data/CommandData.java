package pt.theninjask.AnotherTwitchPlaysX.data;

import java.util.List;

public class CommandData implements Data {

	private String input;
	
	private CommandType type;
	
	private List<ControlData> controls;
	
	public CommandData() {}
	
	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
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
	
}
