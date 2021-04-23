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

	public void setType(String type) {
		this.type = type;
	}
	
}
