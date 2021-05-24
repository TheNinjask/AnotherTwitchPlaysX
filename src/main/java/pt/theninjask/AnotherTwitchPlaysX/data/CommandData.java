package pt.theninjask.AnotherTwitchPlaysX.data;

import java.awt.AWTException;
import java.awt.Robot;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.exception.NoLeadDefinedException;
import pt.theninjask.AnotherTwitchPlaysX.util.Pair;
import pt.theninjask.AnotherTwitchPlaysX.util.RobotSingleton;
import pt.theninjask.AnotherTwitchPlaysX.util.SmoothMoveRobot;
import pt.theninjask.AnotherTwitchPlaysX.util.ThreadPool;

public class CommandData implements Data {

	private String lead;

	private CommandType type;

	private List<ControlData> controls;

	private List<Pair<String, CommandVarType>> vars;

	public CommandData() {
		this.lead = "";
		this.type = CommandType.UNISON;
		this.controls = new ArrayList<ControlData>();
		this.vars = new ArrayList<Pair<String, CommandVarType>>();
	}

	public CommandData(String lead, CommandType type, List<ControlData> controls,
			List<Pair<String, CommandVarType>> vars) {
		this.lead = lead;
		this.type = type;
		this.controls = controls;
		this.vars = vars;
	}

	public String getLead() {
		return lead;
	}

	public void setLead(String lead) {
		this.lead = lead;
	}

	public CommandType getType() {
		return type;
	}

	public void setType(CommandType type) {
		this.type = type;
	}

	public List<ControlData> getControls() {
		return controls;
	}

	public void setControls(List<ControlData> controls) {
		this.controls = controls;
	}

	public List<Pair<String, CommandVarType>> getVars() {
		return vars;
	}

	public void setVars(List<Pair<String, CommandVarType>> vars) {
		this.vars = vars;
	}

	public String toString() {
		return lead;
	}

	@JsonIgnore
	public String getRegex() {
		if (lead == null)
			throw new NoLeadDefinedException();
		StringBuilder regex = new StringBuilder(lead);
		for (Pair<String, CommandVarType> elem : vars) {
			regex.append(String.format("\\s?(?<%s>%s)", elem.getLeft(), elem.getRight().getRegex()));
		}
		return regex.toString();
	}

	@JsonIgnore
	public String getRegexExample() {
		if (lead == null)
			throw new NoLeadDefinedException();
		StringBuilder example = new StringBuilder(lead);
		Random tmp = new Random();
		for (Pair<String, CommandVarType> elem : vars) {
			byte[] array = new byte[4];
			new Random().nextBytes(array);
			example.append(String.format(" %s", CommandVarType.DIGIT == elem.getRight() ? tmp.nextInt(100)
					: new String(array, Charset.forName("UTF-8"))));
		}
		return example.toString();
	}

	public boolean equals(CommandData other) {
		if (other == null)
			return false;
		if (lead == null) {
			if (other.lead != null)
				return false;
		} else {
			if (!lead.equals(other.lead))
				return false;
		}
		if (type == null) {
			if (other.type != null)
				return false;
		} else {
			if (!type.equals(other.type))
				return false;
		}
		if (controls == null) {
			if (other.controls != null)
				return false;
		} else {
			if (other.controls == null)
				return false;
			if (controls.size() != other.controls.size())
				return false;
			for (int i = 0; i < controls.size(); i++) {
				if (!controls.get(i).equals(other.controls.get(i)))
					return false;
			}
		}
		if (vars == null) {
			if (other.vars != null)
				return false;
		} else {
			if (other.vars == null)
				return false;
			if (vars.size() != other.vars.size())
				return false;
			for (int i = 0; i < vars.size(); i++) {
				if (!vars.get(i).equals(other.vars.get(i)))
					return false;
			}
		}
		return true;
	}

	public CommandData clone() {
		CommandData copy = new CommandData();
		copy.setLead(lead == null ? null : new String(lead));
		copy.setType(type == null ? null : type);
		if (controls == null)
			copy.setControls(null);
		else {
			ArrayList<ControlData> tmp = new ArrayList<ControlData>();
			for (ControlData elem : controls) {
				tmp.add(elem.clone());
			}
			copy.setControls(tmp);
		}
		if (vars == null)
			copy.setVars(null);
		else {
			ArrayList<Pair<String, CommandVarType>> tmp = new ArrayList<Pair<String, CommandVarType>>();
			for (Pair<String, CommandVarType> elem : vars) {
				tmp.add(new Pair<String, CommandVarType>(new String(elem.getLeft()), elem.getRight()));
			}
			copy.setVars(tmp);
		}
		return copy;
	}

	@Handler
	public void onMessage(ChannelMessageEvent event) {
		// if(!event.getActor().getNick().equalsIgnoreCase("mytwitchusername69420"))
		// return;
		if (!event.getActor().getNick().equalsIgnoreCase("wynautcritical"))
			return;
		Pattern pattern = Pattern.compile(getRegex(), Pattern.CASE_INSENSITIVE);
		Matcher match = pattern.matcher(event.getMessage());
		Map<String, String> map = new HashMap<String, String>();
		for (Pair<String, CommandVarType> elem : vars) {
			String value = match.group(elem.getLeft());
			if (value != null && value.isEmpty())
				value = null;
			if (value != null)
				map.put(elem.getLeft(), value.toLowerCase());
		}
		switch (type) {
		case QUEUE:
			if (map.isEmpty())
				executeQueue();
			executeQueue(map);
			break;
		case UNISON:
		default:
			if (map.isEmpty())
				executeUnison();
			executeUnison(map);
			break;
		}
	}

	public void executeUnison() {
		ThreadPool.execute(() -> {
			for (ControlData elem : controls) {
				ThreadPool.executeUnison(() -> {
					try {
						Robot robot = new SmoothMoveRobot();
						elem.execute(robot);
					} catch (AWTException e) {
						throw new RuntimeException(e);
					}
				});
				if(elem.getAftermathDelay()!=null)
					RobotSingleton.getInstance().getRobot().delay(elem.getAftermathDelay());
			}
		});
		/*
		 * Robot robot = RobotSingleton.getUnisonInstance().getRobot(); for (ControlData
		 * elem : controls) { synchronized (robot) { elem.execute(robot); } }
		 */

	}

	public void executeUnison(Map<String, String> map) {
		ThreadPool.execute(() -> {
			for (ControlData elem : controls) {
				ThreadPool.executeUnison(() -> {
					try {
						Robot robot = new SmoothMoveRobot();
						elem.execute(robot, map);
					} catch (AWTException e) {
						throw new RuntimeException(e);
					}
				});
				if(elem.getAftermathDelay()!=null)
					RobotSingleton.getInstance().getRobot().delay(elem.getAftermathDelay());
			}
		});
		/*
		 * Robot robot = RobotSingleton.getUnisonInstance().getRobot(); for (ControlData
		 * elem : controls) { synchronized (robot) { elem.execute(robot, map); } }
		 */

	}

	public void executeQueue() {
		ThreadPool.execute(() -> {
			Robot robot = RobotSingleton.getInstance().getRobot();
			synchronized (robot) {
				for (ControlData elem : controls) {
					ThreadPool.executeQueue(() -> {
						elem.execute(robot);
					});
					if(elem.getAftermathDelay() != null)
						robot.delay(elem.getAftermathDelay());
				}
			}
		});
		/*
		Robot robot = RobotSingleton.getQueueInstance().getRobot();
		synchronized (robot) {
			for (ControlData elem : controls) {
				elem.execute(robot);
			}
		}*/
	}

	public void executeQueue(Map<String, String> map) {
		ThreadPool.execute(() -> {
			Robot robot = RobotSingleton.getInstance().getRobot();
			synchronized (robot) {
				for (ControlData elem : controls) {
					ThreadPool.executeQueue(() -> {
						elem.execute(robot, map);
					});
					if(elem.getAftermathDelay() != null)
						robot.delay(elem.getAftermathDelay());
				}
			}
		});
		/*
		Robot robot = RobotSingleton.getQueueInstance().getRobot();
		synchronized (robot) {
			for (ControlData elem : controls) {
				elem.execute(robot, map);
			}
		}*/
	}

}
