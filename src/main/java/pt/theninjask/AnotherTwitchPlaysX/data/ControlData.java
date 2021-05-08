package pt.theninjask.AnotherTwitchPlaysX.data;

import java.awt.MouseInfo;
import java.awt.Robot;
import java.util.Map;

import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class ControlData implements Data {

	private Integer key;
	
	private Integer duration;
	
	private Integer aftermathDelay;
	
	private ControlType type;
	
	private InDepthCursorData inDepthCursor;
	
	private Map<String, String> map;
	
	public ControlData() {
		this.duration = 0;
		this.aftermathDelay = 0;
	}

	public ControlData(Integer key, Integer duration, Integer aftermathDelay, ControlType type, InDepthCursorData inDepthCursor, Map<String, String> map) {
		this.key = key;
		this.duration = duration;
		this.aftermathDelay = aftermathDelay;
		this.type = type;
		this.inDepthCursor = inDepthCursor;
		this.map = map;
	}
	
	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public ControlType getType() {
		return type;
	}

	public void setType(ControlType type) {
		this.type = type;
		switch (this.type) {
		case MOUSE_CLICK:
		case MOUSE_MOV:
		case MOUSE_WHEEL:
			if(inDepthCursor==null)
				inDepthCursor = new InDepthCursorData();
			break;
		default:
			if(inDepthCursor!=null)
				inDepthCursor = null;
			break;
		}
	}

	public InDepthCursorData getInDepthCursor() {
		return inDepthCursor;
	}

	public void setInDepthCursor(InDepthCursorData inDepthCursor) {
		this.inDepthCursor = inDepthCursor;
	}
	
	public Integer getAftermathDelay() {
		return aftermathDelay;
	}

	public void setAftermathDelay(Integer aftermathDelay) {
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
			Integer duration;
			Integer scroll;
			Integer x;
			Integer y;
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
			tmp = getValueFromMap("x", vars);
			//this is implying dead code in x==null even tho it is the same
			//x = tmp==null? inDepthCursor.getX() : Integer.parseInt(tmp);
			if(tmp==null)
				x = inDepthCursor.getX();
			else
				x = Integer.parseInt(tmp);
			tmp = getValueFromMap("y", vars);
			//this is implying dead code in y==null even tho it is the same
			//y = tmp==null ? inDepthCursor.getY() : Integer.parseInt(tmp);
			if(tmp==null)
				y = inDepthCursor.getY();
			else
				y = Integer.parseInt(tmp);
			if(x!=null || y!=null) {
				if(x==null)
					x = MouseInfo.getPointerInfo().getLocation().x;
				if(y==null)
					y = MouseInfo.getPointerInfo().getLocation().y;
				robot.mouseMove(x,y);
			}
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
			Integer aftermathDelay = tmp==null? this.aftermathDelay : Integer.parseInt(tmp);
			robot.wait(aftermathDelay * 1000);
		} catch (InterruptedException e) {}*/
	}
}
