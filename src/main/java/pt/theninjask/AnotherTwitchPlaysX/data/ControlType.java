package pt.theninjask.AnotherTwitchPlaysX.data;

public enum ControlType {
	
	MOUSE("mouse"),
	KEY("key"),
	MOUSE_DRAG("mouse_drag"),
	MOUSE_WHEEL("mouse_wheel");
	
	private String type;
	
	private ControlType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}

}
