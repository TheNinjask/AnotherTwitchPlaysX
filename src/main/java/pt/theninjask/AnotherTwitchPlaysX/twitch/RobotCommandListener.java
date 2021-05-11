package pt.theninjask.AnotherTwitchPlaysX.twitch;

import java.awt.Robot;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.data.CommandVarType;
import pt.theninjask.AnotherTwitchPlaysX.data.ControlData;
import pt.theninjask.AnotherTwitchPlaysX.util.Pair;
import pt.theninjask.AnotherTwitchPlaysX.util.RobotSingleton;

@Deprecated
public class RobotCommandListener {

	private CommandData data;
	
	private Pattern pattern;
	
	public RobotCommandListener(CommandData data) {
		this.data = data;
	}
	
	@Handler
	public void onMessage(ChannelMessageEvent event){
		pattern = Pattern.compile(this.data.getRegex(), Pattern.CASE_INSENSITIVE);
		Matcher match = pattern.matcher(event.getMessage());
		if(!match.matches())
			return;
		Map<String, String> map = new HashMap<String, String>();
		for (Pair<String, CommandVarType> elem : data.getVars()) {
			String value = match.group(elem.getKey());
			if(value!=null)
				map.put(elem.getKey(), value);
		}
		switch (data.getType()) {
			case QUEUE:
				if(map.isEmpty())
					executeQueue();
				executeQueue(map);
				break;
		default:
				if(map.isEmpty())
					executeUnison();
				executeUnison(map);
				break;
		}
	}
	
	public void executeUnison() {
		Robot robot = RobotSingleton.getUnisonInstance().getRobot();
		for (ControlData elem : data.getControls()) {
			synchronized (robot) {
				elem.execute(robot);				
			}
		}
		
	}
	
	public void executeUnison(Map<String, String> map) {
		Robot robot = RobotSingleton.getUnisonInstance().getRobot();
		for (ControlData elem : data.getControls()) {
			synchronized (robot) {
				elem.execute(robot, map);				
			}
		}
		
	}
	
	public void executeQueue() {
		Robot robot = RobotSingleton.getQueueInstance().getRobot();
		synchronized (robot) {
			for (ControlData elem : data.getControls()) {
				elem.execute(robot);				
			}
		}
	}
	
	public void executeQueue(Map<String, String> map) {
		Robot robot = RobotSingleton.getQueueInstance().getRobot();
		synchronized (robot) {
			for (ControlData elem : data.getControls()) {
				elem.execute(robot, map);				
			}
		}
	}
	
}
