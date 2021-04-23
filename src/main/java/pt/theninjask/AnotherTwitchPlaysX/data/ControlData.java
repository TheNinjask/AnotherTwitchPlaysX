package pt.theninjask.AnotherTwitchPlaysX.data;

public class ControlData implements Data {

	private int key;
	
	private int duration;
	
	private ControlType type;
	
	private InDepthCursorData inDepthCursor;
	
	public ControlData() {}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public ControlType getType() {
		return type;
	}

	public void setType(ControlType type) {
		this.type = type;
	}

	public InDepthCursorData getInDepthCursor() {
		return inDepthCursor;
	}

	public void setInDepthCursor(InDepthCursorData inDepthCursor) {
		this.inDepthCursor = inDepthCursor;
	}
	
}
