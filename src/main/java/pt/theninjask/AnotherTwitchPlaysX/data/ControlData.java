package pt.theninjask.AnotherTwitchPlaysX.data;

import java.awt.MouseInfo;
import java.awt.Robot;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ControlData implements Data {

	private Integer key;
	
	private Integer duration;
	
	private Integer aftermathDelay;
	
	private ControlType type;
	
	private InDepthCursorData inDepthCursor;
	
	private Map<String, String> map;
	
	public ControlData() {
		this.key = null;
		this.duration = 0;
		this.aftermathDelay = 0;
		this.type = null;
		this.inDepthCursor = null;
		this.map = new HashMap<String, String>();
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
			if(key==null)
				break;
			robot.keyPress(key);
			if(duration!=null);
				robot.delay(duration);
			robot.keyRelease(key);
			break;
		case MOUSE_MOV:
			if(inDepthCursor!=null && inDepthCursor.getX()!=null && inDepthCursor.getY()!=null)
				robot.mouseMove(inDepthCursor.getX(), inDepthCursor.getY());
			break;
		case MOUSE_CLICK:
			if(inDepthCursor!=null && inDepthCursor.getX()!=null && inDepthCursor.getY()!=null)
				robot.mouseMove(inDepthCursor.getX(), inDepthCursor.getY());
			if(key==null)
				break;
			robot.mousePress(key);
			if(duration!=null);
				robot.delay(duration);
			robot.mouseRelease(key);
			break;
		case MOUSE_WHEEL:
			if(inDepthCursor!=null && inDepthCursor.getScroll()!=null)
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
			if(key==null)
				break;
			robot.keyPress(key);
			tmp = getValueFromMap("duration", vars);
			duration = tmp==null ? this.duration : Integer.parseInt(tmp);
			if(duration!=null)
				robot.delay(duration);
			robot.keyRelease(key);
			break;
			
		case MOUSE_MOV:
			tmp = getValueFromMap("x", vars);
			if(tmp==null){
				if(inDepthCursor!=null)
					x=inDepthCursor.getX();
				else
					x=null;
			}else x=Integer.parseInt(tmp);
			if(x==null)
				x=MouseInfo.getPointerInfo().getLocation().x;
			tmp = getValueFromMap("y", vars);
			if(tmp==null){
				if(inDepthCursor!=null)
					y=inDepthCursor.getY();
				else
					y=null;
			}else y=Integer.parseInt(tmp);
			if(y==null)
				y=MouseInfo.getPointerInfo().getLocation().y;
			robot.mouseMove(x,y);
			break;
			
		case MOUSE_CLICK:	
			tmp = getValueFromMap("x", vars);
			//deprecated comment
			//this is implying dead code in x==null even tho it is the same
			//x = tmp==null? inDepthCursor.getX() : Integer.parseInt(tmp);
			if(tmp==null){
				if(inDepthCursor!=null)
					x=inDepthCursor.getX();
				else
					x=null;
			}else x=Integer.parseInt(tmp);
			if(x==null)
				x=MouseInfo.getPointerInfo().getLocation().x;
			tmp = getValueFromMap("y", vars);
			//deprecated comment
			//this is implying dead code in y==null even tho it is the same
			//y = tmp==null ? inDepthCursor.getY() : Integer.parseInt(tmp);
			if(tmp==null){
				if(inDepthCursor!=null)
					y=inDepthCursor.getY();
				else
					y=null;
			}else y=Integer.parseInt(tmp);
			if(y==null)
				y=MouseInfo.getPointerInfo().getLocation().y;
			robot.mouseMove(x,y);
			robot.mousePress(key);
			tmp = getValueFromMap("duration", vars);
			duration = tmp==null ? this.duration : Integer.parseInt(tmp);
			robot.delay(duration);
			robot.mouseRelease(key);
			break;
		
		case MOUSE_WHEEL:
			tmp = getValueFromMap("scroll", vars);
			if(tmp==null){
				if(inDepthCursor!=null)
					scroll=inDepthCursor.getScroll();
				else
					scroll=null;
			}else scroll=Integer.parseInt(tmp);
			if(scroll!=null)
				robot.mouseWheel(scroll);
			break;			
		}
		/*try {
			String tmp = getValueFromMap("aftermathDelay", vars);
			Integer aftermathDelay = tmp==null? this.aftermathDelay : Integer.parseInt(tmp);
			robot.wait(aftermathDelay * 1000);
		} catch (InterruptedException e) {}*/
	}
	
	public boolean equals(ControlData other) {
		if(other==null)
			return false;
		if(key==null) {
			if(other.key!=null)
				return false;				
		}else{
			if(!key.equals(other.key))
				return false;
		}
		if(duration==null) {
			if(other.duration!=null)
				return false;				
		}else{
			if(!duration.equals(other.duration))
				return false;
		}
		if(aftermathDelay==null) {
			if(other.aftermathDelay!=null)
				return false;				
		}else{
			if(!aftermathDelay.equals(other.aftermathDelay))
				return false;
		}
		if(type==null) {
			if(other.type!=null)
				return false;				
		}else{
			if(!type.equals(other.type))
				return false;
		}
		if(inDepthCursor==null) {
			if(other.inDepthCursor!=null)
				return false;				
		}else{
			if(!inDepthCursor.equals(other.inDepthCursor))
				return false;
		}
		if(map==null) {
			if(other.map!=null)
				return false;
		}else {
			if(other.map==null)
				return false;
			if(map.size()!=other.map.size())
				return false;
			for (Entry<String, String> elem : map.entrySet()) {
				String value = other.map.get(elem.getKey());
				if(value!=elem.getValue())
					return false;
			}
		}
		return true;
	}
	
	public ControlData clone() {
		ControlData copy = new ControlData();
		copy.setKey(key==null?null:new Integer(key.intValue()));
		copy.setDuration(duration==null?null:new Integer(duration.intValue()));
		copy.setAftermathDelay(aftermathDelay==null?null:new Integer(aftermathDelay.intValue()));
		copy.setType(type==null?null:type);
		copy.setInDepthCursor(inDepthCursor==null?null:inDepthCursor.clone());
		if(map==null)
			copy.setMap(null);
		else {
			ConcurrentHashMap<String, String> mapCopy = new ConcurrentHashMap<String, String>(map.size());
			map.forEach((k,v)->{
				mapCopy.put(k, v);
			});
			copy.setMap(mapCopy);
		}
		return copy;
	}
	
}
