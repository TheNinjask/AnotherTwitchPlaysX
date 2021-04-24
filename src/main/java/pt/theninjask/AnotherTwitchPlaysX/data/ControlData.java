package pt.theninjask.AnotherTwitchPlaysX.data;

import java.awt.Robot;
import java.util.Map;

public class ControlData implements Data {

	private int key;
	
	private int duration;
	
	private int aftermathDelay;
	
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
	
	public int getAftermathDelay() {
		return aftermathDelay;
	}

	public void setAftermathDelay(int aftermathDelay) {
		this.aftermathDelay = aftermathDelay;
	}

	public void execute(Robot robot){
		switch(type) {
		case KEY:
			robot.keyPress(key);
			robot.delay(duration);
			robot.keyRelease(key);
			break;
		case MOUSE_MOV:
			robot.mouseMove(inDepthCursor.getX(), inDepthCursor.getY());
			break;
		case MOUSE_CLICK:
			robot.mousePress(key);
			robot.delay(duration);
			robot.mouseRelease(key);
			break;
		case MOUSE_WHEEL:
			robot.mouseWheel(inDepthCursor.getScroll());
			break;
		default:
			break;
			
		}
	}
	
	public void execute(Robot robot, Map<String, Object> map) {
		switch(type) {
		default:
			int duration;
			int scroll;
			int x;
			int y;
			break;
		case KEY:
			robot.keyPress(key);
			duration = (int)map.getOrDefault("duration", this.duration);
			robot.delay(duration);
			robot.keyRelease(key);
			break;
		case MOUSE_MOV:
			x = (int)map.getOrDefault("x", inDepthCursor.getX());
			y = (int)map.getOrDefault("y", inDepthCursor.getY());
			robot.mouseMove(x,y);
			break;
		case MOUSE_CLICK:
			robot.mousePress(key);
			duration = (int)map.getOrDefault("duration", this.duration);
			robot.delay(duration);
			robot.mouseRelease(key);
			break;
		case MOUSE_WHEEL:
			scroll = (int)map.getOrDefault("scroll", inDepthCursor.getScroll());
			robot.mouseWheel(scroll);
			break;			
		}
	}
}
