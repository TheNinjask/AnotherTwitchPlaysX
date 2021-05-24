package pt.theninjask.AnotherTwitchPlaysX.util;

import java.awt.AWTException;
import java.awt.Robot;

public class RobotSingleton {

	private Robot robot;

	@Deprecated
	private static RobotSingleton robotQueue = new RobotSingleton();
	
	@Deprecated
	private static RobotSingleton robotUnison = new RobotSingleton();
	
	private static RobotSingleton singleton = new RobotSingleton();
	
	private RobotSingleton() {
		try {
			robot = new SmoothMoveRobot();
		} catch (AWTException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Deprecated
	public static RobotSingleton getUnisonInstance() {
		return robotUnison;
	}
	
	@Deprecated
	public static RobotSingleton getQueueInstance() {
		return robotQueue;
	}

	public static RobotSingleton getInstance() {
		return singleton;
	}
	
	public Robot getRobot() {
		return robot;
	}
	
}
