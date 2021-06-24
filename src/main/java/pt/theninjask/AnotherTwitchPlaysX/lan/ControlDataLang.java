package pt.theninjask.AnotherTwitchPlaysX.lan;

public interface ControlDataLang {

	public default String getTypeKey() {
		return "Key";
	}
	
	public default String getTypeButton() {
		return "Button";
	}
	
	public default String getKeyNone() {
		return "None";
	}
	
	public default String getKeyCurrent() {
		return "Press a key (Current: %s)";
	}
	
	public default String getButtonNone() {
		return "None";
	}
	
	public default String getButtonLeft() {
		return "Left";
	}
	
	public default String getButtonRight() {
		return "Right";
	}
	
	public default String getButtonMiddle() {
		return "Middle";
	}
	
	public default String getDuration() {
		return "Duration (sec):";
	}
	
	public default String getAftermath() {
		return "Aftermath (sec):";
	}
	
	public default String getX() {
		return "X (%s):";
	}
	
	public default String getXClear() {
		return "[x]";
	}
	
	public default String getAbs() {
		return "Abs";
	}
	
	public default String getRel() {
		return "Rel";
	}
	
	public default String getY() {
		return "Y (%s):";
	}
	
	public default String getYClear() {
		return "[x]";
	}
	
	public default String getInitialCoords() {
		return "Initial Coords";
	}
	
	public default String getFinalCoords() {
		return "Final Coords";
	}
	
	public default String getFinalX() {
		return "X (%s):";
	}
	
	public default String getFinalXClear() {
		return "[x]";
	}
	
	public default String getFinalY() {
		return "Y (%s):";
	}
	
	public default String getFinalYClear() {
		return "[x]";
	}
	
	public default String getIndex() {
		return "#index";
	}
	
	public default String getRemove() {
		return "[x]";
	}
	
}
