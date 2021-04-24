package pt.theninjask.AnotherTwitchPlaysX.data;

public class InDepthCursorData implements Data {

	private int x;
	
	private int y;
	
	private int scroll;
	
	public InDepthCursorData() {}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getScroll() {
		return scroll;
	}

	public void setScroll(int scroll) {
		this.scroll = scroll;
	}
	
}
