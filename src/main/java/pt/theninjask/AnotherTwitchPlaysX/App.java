package pt.theninjask.AnotherTwitchPlaysX;

import java.awt.Robot;

public class App {
	
	public static void main(String[] args) throws Exception{
		Robot robot = new Robot();
		long start = System.currentTimeMillis();
		while(System.currentTimeMillis() < start + 15*1000) {
			robot.mouseMove(0, 0);
			Thread.sleep(1000);
			robot.mouseMove(100, 100);
			Thread.sleep(1000);
			robot.mouseMove(200, 200);
			Thread.sleep(1000);
		}
		System.out.println("Hello World!");
	}
	
}
