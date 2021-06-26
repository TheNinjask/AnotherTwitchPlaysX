package pt.theninjask.AnotherTwitchPlaysX.data;

import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import pt.theninjask.AnotherTwitchPlaysX.util.Pair;

public class ControlData implements Data {
	
	public static final String KEY_VAR = "key";
	private Integer key;
	
	public static final String DURATION_VAR = "duration";
	private Integer duration;
	
	public static final String AFTERMATH_VAR = "aftermathDelay";
	private Integer aftermathDelay;
	
	private ControlType type;
	
	private InDepthCursorData inDepthCursor;
	public static final String X_VAR = "x";
	public static final String Y_VAR = "y";
	public static final String FINAL_X_VAR = "final_x";
	public static final String FINAL_Y_VAR = "final_y";
	public static final String SCROLL_VAR = "scroll";
	
	private Map<String, String> map;
	
	private static Map<String, Pair<Integer, ControlType>> translation;
	
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
		this.duration = duration * 1000;
		this.aftermathDelay = aftermathDelay * 1000;
		this.type = type;
		this.inDepthCursor = inDepthCursor;
		this.map = map;
	}
	
	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		if(duration>60000)
			this.duration = 60000;
		else if(duration<0) 
			this.duration = 0;
		else
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
		//case MOUSE_CLICK:
		//case MOUSE_MOV:
		case MOUSE:
		case MOUSE_DRAG:
		case MOUSE_WHEEL:
			if(inDepthCursor==null)
				inDepthCursor = new InDepthCursorData();
			if(this.type==ControlType.MOUSE_DRAG) {
				inDepthCursor.setFinalX(new Pair<Integer, MouseCoordsType>(null, MouseCoordsType.ABS));
				inDepthCursor.setFinalY(new Pair<Integer, MouseCoordsType>(null, MouseCoordsType.ABS));
			}
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
		if(aftermathDelay>60000)
			this.aftermathDelay = 60000;
		else if(aftermathDelay<0) 
			this.aftermathDelay = 0;
		else
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
		default:
			Integer x;
			Integer y;
			break;			
		case KEY:
			if(key==null)
				break;
			robot.keyPress(key);
			if(duration!=null)
				robot.delay(duration);
			robot.keyRelease(key);
			break;
		/*case MOUSE_MOV:
			if(inDepthCursor!=null && inDepthCursor.getX()!=null && inDepthCursor.getY()!=null)
				robot.mouseMove(inDepthCursor.getX(), inDepthCursor.getY());
			break;
		case MOUSE_CLICK:*/
		case MOUSE:
			if(inDepthCursor!=null) {
				x = inDepthCursor.getXorDefault();
				if(inDepthCursor.getX()!=null && inDepthCursor.getX().getRight()==MouseCoordsType.REL) {
					x = MouseInfo.getPointerInfo().getLocation().x + x;
				}
				y = inDepthCursor.getYorDefault();
				if(inDepthCursor.getY()!=null && inDepthCursor.getY().getRight()==MouseCoordsType.REL) {
					y = MouseInfo.getPointerInfo().getLocation().y + y;
				}
				robot.mouseMove(x, y);
			}
			if(key!=null)
				robot.mousePress(key);
			if(duration!=null)
				robot.delay(duration);
			if(key!=null)
				robot.mouseRelease(key);
			break;
		case MOUSE_DRAG:
			if(inDepthCursor!=null) {
				x = inDepthCursor.getXorDefault();
				if(inDepthCursor.getX()!=null && inDepthCursor.getX().getRight()==MouseCoordsType.REL) {
					x = MouseInfo.getPointerInfo().getLocation().x + x;
				}
				y = inDepthCursor.getYorDefault();
				if(inDepthCursor.getY()!=null && inDepthCursor.getY().getRight()==MouseCoordsType.REL) {
					y = MouseInfo.getPointerInfo().getLocation().y + y;
				}
				robot.mouseMove(x, y);
			}
			if(key!=null)
				robot.mousePress(key);
			if(inDepthCursor!=null) {
				x = inDepthCursor.getFinalXorDefault();
				if(inDepthCursor.getFinalX()!=null && inDepthCursor.getFinalX().getRight()==MouseCoordsType.REL) {
					x = MouseInfo.getPointerInfo().getLocation().x + x;
				}
				y = inDepthCursor.getFinalYorDefault();
				if(inDepthCursor.getFinalY()!=null && inDepthCursor.getFinalY().getRight()==MouseCoordsType.REL) {
					y = MouseInfo.getPointerInfo().getLocation().y + y;
				}
				robot.mouseMove(x, y);
			}
			if(key!=null)
				robot.mouseRelease(key);
			break;
		case MOUSE_WHEEL:
			if(inDepthCursor!=null && inDepthCursor.getScroll()!=null)
				robot.mouseWheel(inDepthCursor.getScroll());
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
			return defaultValue;
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
			Integer key;
			String tmp;
			Pair<Integer, ControlType> trans;
			break;
			
		case KEY:
			tmp = getValueFromMap(KEY_VAR, vars);
			trans = translation.get(tmp);
			if(trans==null || trans.getRight() != ControlType.KEY) {
				key = this.key;
			}else {
				key = trans.getLeft();
			}
			if(key==null)
				break;
			robot.keyPress(key);
			tmp = getValueFromMap(DURATION_VAR, vars);
			duration = tmp==null ? this.duration : Integer.parseInt(tmp);
			if(duration!=null)
				robot.delay(duration);
			robot.keyRelease(key);
			break;
			
		/*case MOUSE_MOV:
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
			
		case MOUSE_CLICK:*/
		case MOUSE:
			tmp = getValueFromMap(X_VAR, vars);
			//deprecated comment x2 xD
			//this is implying dead code in x==null even tho it is the same
			x= tmp==null? inDepthCursor.getXorDefault() : Integer.parseInt(tmp);
			if(inDepthCursor.getX()!=null && inDepthCursor.getX().getRight()==MouseCoordsType.REL) {
				x = MouseInfo.getPointerInfo().getLocation().x + x;
			}
			tmp = getValueFromMap(Y_VAR, vars);
			//deprecated comment x2
			//this is implying dead code in y==null even tho it is the same
			y = tmp==null ? inDepthCursor.getYorDefault() : Integer.parseInt(tmp);
			if(inDepthCursor.getY()!=null && inDepthCursor.getY().getRight()==MouseCoordsType.REL) {
				y = MouseInfo.getPointerInfo().getLocation().y + y;
			}
			robot.mouseMove(x,y);
			
			tmp = getValueFromMap(KEY_VAR, vars);
			trans = translation.get(tmp);
			if(trans==null || trans.getRight() != ControlType.MOUSE) {
				key = this.key;
			}else {
				key = trans.getLeft();
			}
			if(key!=null)
				robot.mousePress(key);
			tmp = getValueFromMap(DURATION_VAR, vars);
			duration = tmp==null ? this.duration : Integer.parseInt(tmp);
			if(duration!=null)
				robot.delay(duration);
			if(key!=null)
				robot.mouseRelease(key);
			break;
			
		case MOUSE_DRAG:
			tmp = getValueFromMap(X_VAR, vars);
			x= tmp==null? inDepthCursor.getXorDefault() : Integer.parseInt(tmp);
			if(inDepthCursor.getX()!=null && inDepthCursor.getX().getRight()==MouseCoordsType.REL) {
				x = MouseInfo.getPointerInfo().getLocation().x + x;
			}
			
			tmp = getValueFromMap(Y_VAR, vars);
			y = tmp==null ? inDepthCursor.getYorDefault() : Integer.parseInt(tmp);
			if(inDepthCursor.getY()!=null && inDepthCursor.getY().getRight()==MouseCoordsType.REL) {
				y = MouseInfo.getPointerInfo().getLocation().y + y;
			}
			
			robot.mouseMove(x,y);
			
			tmp = getValueFromMap(KEY_VAR, vars);
			trans = translation.get(tmp);
			if(trans==null || trans.getRight() != ControlType.MOUSE) {
				key = this.key;
			}else {
				key = trans.getLeft();
			}
			
			if(key!=null)
				robot.mousePress(key);
			/*tmp = getValueFromMap("duration", vars);
			duration = tmp==null ? this.duration : Integer.parseInt(tmp);
			robot.delay(duration);*/
			tmp = getValueFromMap(FINAL_X_VAR, vars);
			x= tmp==null? inDepthCursor.getFinalXorDefault() : Integer.parseInt(tmp);
			if(inDepthCursor.getFinalX()!=null && inDepthCursor.getFinalX().getRight()==MouseCoordsType.REL) {
				x = MouseInfo.getPointerInfo().getLocation().x + x;
			}
			
			tmp = getValueFromMap(FINAL_Y_VAR, vars);
			y = tmp==null ? inDepthCursor.getFinalYorDefault() : Integer.parseInt(tmp);
			if(inDepthCursor.getFinalY()!=null && inDepthCursor.getFinalY().getRight()==MouseCoordsType.REL) {
				y = MouseInfo.getPointerInfo().getLocation().y + y;
			}
			
			robot.mouseMove(x,y);
			if(key!=null)
				robot.mouseRelease(key);
			break;
			
		case MOUSE_WHEEL:
			tmp = getValueFromMap(SCROLL_VAR, vars);
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
		copy.setKey(key==null?null:Integer.valueOf(key.intValue()));
		copy.setDuration(duration==null?null:Integer.valueOf(getDuration()));
		copy.setAftermathDelay(aftermathDelay==null?null:Integer.valueOf(getAftermathDelay()));
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

	public static void setTranslation(Map<String, Pair<Integer, ControlType>> translation) {
		ControlData.translation = translation;
	}
	
	@Override
	public String toString() {
		if(type == ControlType.KEY)
			return String.format("Control %s(%s)", KeyEvent.getKeyText(key), type.toString());
		else
			switch (key) {
			case MouseEvent.BUTTON1_DOWN_MASK:
				return String.format("Control %s(%s)", "Button Left", type.toString());
			case MouseEvent.BUTTON3_DOWN_MASK:
				return String.format("Control %s(%s)", "Button Right", type.toString());
			case MouseEvent.BUTTON2_DOWN_MASK:
				return String.format("Control %s(%s)", "Button Middle", type.toString());
			default:
				return String.format("Control %s(%s)", key, type.toString());
			}
	}
	
}
