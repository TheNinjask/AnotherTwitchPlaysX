package pt.theninjask.AnotherTwitchPlaysX.data;

import java.awt.MouseInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import pt.theninjask.AnotherTwitchPlaysX.util.Pair;

public class InDepthCursorData implements Data {

	private Pair<Integer, MouseCoordsType> x;

	private Pair<Integer, MouseCoordsType> y;

	private Pair<Integer, MouseCoordsType> finalX;

	private Pair<Integer, MouseCoordsType> finalY;

	private Integer scroll;

	public InDepthCursorData() {
		this.x = new Pair<Integer, MouseCoordsType>(null, MouseCoordsType.ABS);
		this.y = new Pair<Integer, MouseCoordsType>(null, MouseCoordsType.ABS);
		this.scroll = null;
	}

	public InDepthCursorData(Pair<Integer, MouseCoordsType> x, Pair<Integer, MouseCoordsType> y, Integer scroll) {
		this.x = x;
		this.y = y;
		this.scroll = scroll;
	}

	public Pair<Integer, MouseCoordsType> getX() {
		return x;
	}

	@JsonIgnore
	public int getXorDefault() {
		if (x == null || x.getLeft() == null) {
			if (x != null)
				switch (x.getRight()) {
				case REL:
					return 0;
				case ABS:
				default:
					return MouseInfo.getPointerInfo().getLocation().x;
				}
			return MouseInfo.getPointerInfo().getLocation().x;
		}
		return x.getLeft();
	}

	public void setX(Pair<Integer, MouseCoordsType> x) {
		this.x = x;
	}

	public Pair<Integer, MouseCoordsType> getY() {
		return y;
	}

	@JsonIgnore
	public int getYorDefault() {
		if (y == null || y.getLeft() == null) {
			if (y != null)
				switch (y.getRight()) {
				case REL:
					return 0;
				case ABS:
				default:
					return MouseInfo.getPointerInfo().getLocation().y;
				}
			return MouseInfo.getPointerInfo().getLocation().y;
		}
		return y.getLeft();
	}

	public void setY(Pair<Integer, MouseCoordsType> y) {
		this.y = y;
	}

	public Pair<Integer, MouseCoordsType> getFinalX() {
		return finalX;
	}

	@JsonIgnore
	public int getFinalXorDefault() {
		if (finalX == null || finalX.getLeft() == null) {
			if (finalX != null)
				switch (finalX.getRight()) {
				case REL:
					return 0;
				case ABS:
				default:
					return MouseInfo.getPointerInfo().getLocation().x;
				}
			return MouseInfo.getPointerInfo().getLocation().x;
		}
		return finalX.getLeft();
	}

	public void setFinalX(Pair<Integer, MouseCoordsType> finalX) {
		this.finalX = finalX;
	}

	public Pair<Integer, MouseCoordsType> getFinalY() {
		return finalY;
	}

	@JsonIgnore
	public int getFinalYorDefault() {
		if (finalY == null || finalY.getLeft() == null) {
			if (finalY != null)
				switch (finalY.getRight()) {
				case REL:
					return 0;
				case ABS:
				default:
					return MouseInfo.getPointerInfo().getLocation().y;
				}
			return MouseInfo.getPointerInfo().getLocation().y;
		}
		return finalY.getLeft();
	}

	public void setFinalY(Pair<Integer, MouseCoordsType> finalY) {
		this.finalY = finalY;
	}

	public Integer getScroll() {
		return scroll;
	}

	public void setScroll(Integer scroll) {
		this.scroll = scroll;
	}

	public boolean equals(InDepthCursorData other) {
		if (other == null)
			return false;
		if (x == null) {
			if (other.x != null)
				return false;
		} else {
			if (!x.equals(other.x))
				return false;
		}
		if (y == null) {
			if (other.y != null)
				return false;
		} else {
			if (!y.equals(other.y))
				return false;
		}
		if (scroll == null) {
			if (other.scroll != null)
				return false;
		} else {
			if (!scroll.equals(other.scroll))
				return false;
		}
		return true;
	}

	public InDepthCursorData clone() {
		InDepthCursorData copy = new InDepthCursorData();
		copy.setX(x == null ? null : x.clone());
		copy.setY(y == null ? null : y.clone());
		copy.setFinalX(finalX == null ? null : finalX.clone());
		copy.setFinalY(finalY == null ? null : finalY.clone());
		copy.setScroll(scroll == null ? null : Integer.valueOf(scroll.intValue()));
		return copy;
	}
}
