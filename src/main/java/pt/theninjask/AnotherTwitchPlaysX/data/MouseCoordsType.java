package pt.theninjask.AnotherTwitchPlaysX.data;

public enum MouseCoordsType {

	ABS("Absolute"),
	REL("Relative");
	
	private String type;
	
	private MouseCoordsType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type;
	}
}
