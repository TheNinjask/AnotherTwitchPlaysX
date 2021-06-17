package pt.theninjask.AnotherTwitchPlaysX.util;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;

public class SmoothMoveRobot extends Robot {

	private static int speed=151;

	private static int steps=40;

	public SmoothMoveRobot() throws AWTException {
		super();
	}

	public static void setDefaultSpeed(int speed) {
		SmoothMoveRobot.speed = speed;
	}

	public static void setDefaultSteps(int steps) {
		SmoothMoveRobot.steps = steps;
	}
	
	public static int getDefaultSpeed() {
		return speed;
	}
	
	public static int getDefaultSteps() {
		return steps;
	}

	@Override
	public void mouseMove(int x, int y) {
		smoothMove(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y, x, y);
	}

	private void smoothMove(int fromX, int fromY, int toX, int toY) {
		int sleepPerStep = speed / steps;
		double deltaX = (toX - fromX) / (double)steps;
	    double deltaY = (toY - fromY) / (double)steps;
	    for(int step=0; step<steps ;step++) {
	    	double newX = deltaX * (step + 1) + fromX;
	        double newY = deltaY * (step + 1) + fromY;
	        super.mouseMove((int)Math.round(newX), (int)Math.round(newY));        
	        this.delay(sleepPerStep);
	    }
	}

}
