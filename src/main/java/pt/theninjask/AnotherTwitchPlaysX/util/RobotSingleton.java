package pt.theninjask.AnotherTwitchPlaysX.util;

import java.awt.AWTException;
import java.awt.Robot;

public class RobotSingleton {

	private Robot robot;
	
	private static RobotSingleton robotQueue = new RobotSingleton();
	
	private static RobotSingleton robotUnison = new RobotSingleton();
	
	private RobotSingleton() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static RobotSingleton getUnisonInstance() {
		return robotUnison;
	}
	
	public static RobotSingleton getQueueInstance() {
		return robotQueue;
	}

	public Robot getRobot() {
		return robot;
	}
	
}
