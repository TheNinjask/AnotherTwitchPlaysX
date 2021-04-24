package pt.theninjask.AnotherTwitchPlaysX.twitch;

import java.awt.Robot;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.data.ControlData;
import pt.theninjask.AnotherTwitchPlaysX.util.RobotSingleton;

public class RobotCommandListener {

	public CommandData data;
	
	public RobotCommandListener() {
	}
	
	@Handler
	public void onMessage(ChannelMessageEvent event){
		//THIS IS NOT CORRECT! JUST WROTE LINES TO EASE LOGIC (hint map missing)
		switch (data.getType()) {
		case QUEUE:
			executeQueue(RobotSingleton.getQueueInstance().getRobot());
			break;
		default:
			executeUnison(RobotSingleton.getUnisonInstance().getRobot());
			break;
		}
	}
	
	public void executeUnison(Robot robot) {
		for (ControlData elem : data.getControls()) {
			synchronized (robot) {
				elem.execute(robot);				
			}
		}
		
	}
	
	public void executeQueue(Robot robot) {
		synchronized (robot) {
			for (ControlData elem : data.getControls()) {
				elem.execute(robot);				
			}
		}
	}
	
}
