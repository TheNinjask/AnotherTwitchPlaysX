package pt.theninjask.AnotherTwitchPlaysX.data;

public enum CommandVarType {

	STRING("string", "."),
	DIGIT("digit", "\\d");
	
	private String type;
	
	private String regex;
	
	private CommandVarType(String type, String regex) {
		this.type = type;
		this.regex = regex;
	}

	public String getType() {
		return type;
	}

	public String getRegex() {
		return regex;
	}
	
}
