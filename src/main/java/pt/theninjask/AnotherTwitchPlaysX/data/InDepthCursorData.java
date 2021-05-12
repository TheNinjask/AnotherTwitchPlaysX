package pt.theninjask.AnotherTwitchPlaysX.data;

import java.awt.MouseInfo;

public class InDepthCursorData implements Data {

	private Integer x;
	
	private Integer y;
	
	private Integer finalX;
	
	private Integer finalY;
	
	private Integer scroll;
	
	public InDepthCursorData() {
		this.x = null;
		this.y = null;
		this.scroll = null;
	}
	
	public InDepthCursorData(Integer x, Integer y, Integer scroll) {
		this.x = x;
		this.y = y;
		this.scroll = scroll;
	}


	public Integer getX() {
		return x;
	}

	public int getXorDefault() {
		if(x==null)
			return MouseInfo.getPointerInfo().getLocation().x;
		return x;
	}
	
	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}
	
	public int getYorDefault() {
		if(y==null)
			return MouseInfo.getPointerInfo().getLocation().y;
		return y;
	}
	
	public void setY(Integer y) {
		this.y = y;
	}

	public Integer getFinalX() {
		return finalX;
	}

	public int getFinalXorDefault() {
		if(finalX==null)
			return MouseInfo.getPointerInfo().getLocation().x;
		return finalX;
	}
	
	public void setFinalX(Integer finalX) {
		this.finalX = finalX;
	}

	public Integer getFinalY() {
		return finalY;
	}
	
	public int getFinalYorDefault() {
		if(finalY==null)
			return MouseInfo.getPointerInfo().getLocation().y;
		return finalY;
	}

	public void setFinalY(Integer finalY) {
		this.finalY = finalY;
	}
	
	public Integer getScroll() {
		return scroll;
	}

	public void setScroll(Integer scroll) {
		this.scroll = scroll;
	}
	
	public boolean equals(InDepthCursorData other) {
		if(other==null)
			return false;
		if(x==null) {
			if(other.x!=null)
				return false;				
		}else{
			if(!x.equals(other.x))
				return false;
		}
		if(y==null) {
			if(other.y!=null)
				return false;				
		}else{
			if(!y.equals(other.y))
				return false;
		}
		if(scroll==null) {
			if(other.scroll!=null)
				return false;				
		}else{
			if(!scroll.equals(other.scroll))
				return false;
		}
		return true;
	}
	
	public InDepthCursorData clone() {
		InDepthCursorData copy = new InDepthCursorData();
		copy.setX(x==null?null:new Integer(x.intValue()));
		copy.setY(y==null?null:new Integer(y.intValue()));
		copy.setScroll(scroll==null?null:new Integer(scroll.intValue()));
		return copy;
	}
}
