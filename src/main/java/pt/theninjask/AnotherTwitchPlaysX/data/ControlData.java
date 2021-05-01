package pt.theninjask.AnotherTwitchPlaysX.data;

import java.awt.Robot;
import java.util.Map;

import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class ControlData implements Data {

	private int key;
	
	private int duration;
	
	private int aftermathDelay;
	
	private ControlType type;
	
	private InDepthCursorData inDepthCursor;
	
	private Map<String, String> map;
	
	public ControlData() {}

	public ControlData(int key, int duration, int aftermathDelay, ControlType type, InDepthCursorData inDepthCursor, Map<String, String> map) {
		this.key = key;
		this.duration = duration;
		this.aftermathDelay = aftermathDelay;
		this.type = type;
		this.inDepthCursor = inDepthCursor;
		this.map = map;
	}
	
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

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
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
	
	private String getValueFromMap(String key, Map<String, String> vars) {
		String var = this.map.get(key);
		if(var==null)
			return null;
		String value = vars.get(var);
		return value;
	}
	
	@SuppressWarnings("unused")
	private String getValueFromMap(String key, Map<String, String> vars, String defaultValue) {
		String var = this.map.get(key);
		if(var==null)
			return null;
		String value = vars.getOrDefault(key, defaultValue);
		return value;
	}
	
	public void execute(Robot robot, Map<String, String> vars) {
		switch(type) {
		default:
			int duration;
			int scroll;
			int x;
			int y;
			String tmp;
			break;
		case KEY:
			robot.keyPress(Constants.getKeyCodeOrDefault(getValueFromMap("key", vars),key));
			tmp = getValueFromMap("duration", vars);
			duration = tmp==null ? this.duration : Integer.parseInt(tmp);
			robot.delay(duration);
			robot.keyRelease(key);
			break;
		case MOUSE_MOV:
			tmp = getValueFromMap("x", vars);
			x =  tmp==null? inDepthCursor.getX() : Integer.parseInt(tmp);
			tmp = getValueFromMap("y", vars);
			y = tmp==null ? inDepthCursor.getY() : Integer.parseInt(tmp);
			robot.mouseMove(x,y);
			break;
		case MOUSE_CLICK:
			robot.mousePress(key);
			tmp = getValueFromMap("duration", vars);
			duration = tmp==null ? this.duration : Integer.parseInt(tmp);
			robot.delay(duration);
			robot.mouseRelease(key);
			break;
		case MOUSE_WHEEL:
			tmp = getValueFromMap("scroll", vars);
			scroll = tmp==null ? inDepthCursor.getScroll() : Integer.parseInt(tmp);
			robot.mouseWheel(scroll);
			break;			
		}
		/*try {
			String tmp = getValueFromMap("aftermathDelay", vars);
			int aftermathDelay = tmp==null? this.aftermathDelay : Integer.parseInt(tmp);
			robot.wait(aftermathDelay * 1000);
		} catch (InterruptedException e) {}*/
	}
}
