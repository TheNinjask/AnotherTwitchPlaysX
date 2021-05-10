package pt.theninjask.AnotherTwitchPlaysX.data;

public class InDepthCursorData implements Data {

	private Integer x;
	
	private Integer y;
	
	private Integer scroll;
	
	public InDepthCursorData() {
	}
	
	public InDepthCursorData(Integer x, Integer y, Integer scroll) {
		this.x = x;
		this.y = y;
		this.scroll = scroll;
	}


	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
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
