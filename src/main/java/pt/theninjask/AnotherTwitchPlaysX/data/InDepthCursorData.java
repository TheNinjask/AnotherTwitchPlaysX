package pt.theninjask.AnotherTwitchPlaysX.data;

public class InDepthCursorData implements Data {

	private Integer x;
	
	private Integer y;
	
	private Integer scroll;
	
	public InDepthCursorData() {
		this.x=0;
		this.y=0;
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
	
}
