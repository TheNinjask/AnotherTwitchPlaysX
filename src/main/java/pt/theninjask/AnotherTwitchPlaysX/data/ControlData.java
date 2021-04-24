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
	
	public void execute(Robot robot, Map<String, String> map) {
		switch(type) {
		default:
			int duration;
			int scroll;
			int x;
			int y;
			String tmp;
			break;
		case KEY:
			robot.keyPress(key);
			tmp = map.get("duration");
			if(tmp==null)
				duration = this.duration;
			else
				duration = Integer.parseInt(tmp);
			robot.delay(duration);
			robot.keyRelease(key);
			break;
		case MOUSE_MOV:
			tmp = map.get("x");
			if(tmp==null)
				x = inDepthCursor.getX();
			else
				x = Integer.parseInt(tmp);
			tmp = map.get("y");
			if(tmp==null)
				y = inDepthCursor.getY();
			else
				y = Integer.parseInt(tmp);
			robot.mouseMove(x,y);
			break;
		case MOUSE_CLICK:
			robot.mousePress(key);
			tmp = map.get("duration");
			if(tmp==null)
				duration = this.duration;
			else
				duration = Integer.parseInt(tmp);
			robot.delay(duration);
			robot.mouseRelease(key);
			break;
		case MOUSE_WHEEL:
			tmp = map.get("scroll");
			if(tmp==null)
				scroll = inDepthCursor.getScroll();
			else
				scroll = Integer.parseInt(tmp);
			robot.mouseWheel(scroll);
			break;			
		}
	}
}
