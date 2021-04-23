package pt.theninjask.AnotherTwitchPlaysX.data;

public enum ControlType {
	
	MOUSE("mouse"),
	KEY("key"),
	WHEEL("wheel");
	
	private String type;
	
	private ControlType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
