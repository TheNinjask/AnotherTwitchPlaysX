package pt.theninjask.AnotherTwitchPlaysX.data;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pt.theninjask.AnotherTwitchPlaysX.util.Pair;

public class CommandData implements Data {

	private String lead;
	
	private CommandType type;
	
	private List<ControlData> controls;
	
	private List<Pair<String, CommandVarType>> vars;
	
	public CommandData() {
		this.controls = new ArrayList<ControlData>();
		this.vars = new ArrayList<Pair<String,CommandVarType>>();
	}
	
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
	
	public String toString() {
		return lead;
	}
	
	public String getRegex() {
		StringBuilder regex = new StringBuilder(lead);
		for (Pair<String, CommandVarType> elem : vars) {
			regex.append(
					String.format("\\s?(?<%s>%s*)", elem.getKey(), elem.getValue().getRegex())
					);
		}
		return regex.toString();
	}
	
	public String getRegexExample() {
		StringBuilder example = new StringBuilder(lead);
		Random tmp = new Random();
		for (Pair<String, CommandVarType> elem : vars) {
			byte[] array = new byte[4];
		    new Random().nextBytes(array);
			example.append(
					String.format(" %s", CommandVarType.DIGIT == elem.getValue() ? tmp.nextInt(100) : new String(array, Charset.forName("UTF-8")))
					);
		}
		return example.toString();
	}
}
