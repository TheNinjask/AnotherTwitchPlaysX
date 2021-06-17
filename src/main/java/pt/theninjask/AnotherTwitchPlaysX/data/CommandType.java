package pt.theninjask.AnotherTwitchPlaysX.data;

public enum CommandType {

	UNISON("Unison"),
	@Deprecated
	QUEUE("Queue"),
	SINGLE("Single");
	
	private String type;
	
	private CommandType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type;
	}
	
	public static CommandType[] getAll() {
		return new CommandType[] {UNISON,SINGLE};
	}

}
