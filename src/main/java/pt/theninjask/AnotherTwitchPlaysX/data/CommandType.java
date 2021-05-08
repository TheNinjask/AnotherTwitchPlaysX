package pt.theninjask.AnotherTwitchPlaysX.data;

public enum CommandType {

	QUEUE("queue"),
	UNISON("unison");
	
	private String type;
	
	private CommandType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public static CommandType[] getAll() {
		return new CommandType[] {QUEUE,UNISON};
	}

}
