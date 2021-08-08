package pt.theninjask.AnotherTwitchPlaysX.event.data;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;

public class CommandDataOnTwitchMessageEvent extends BasicEvent{
	
	private CommandData cmd;
	
	private ChannelMessageEvent twitchMessage;
	
	public CommandDataOnTwitchMessageEvent(CommandData cmd, ChannelMessageEvent twitchMessage) {
		super(CommandDataOnTwitchMessageEvent.class.getSimpleName());
		this.cmd = cmd;
		this.twitchMessage = twitchMessage;
	}
	
	public CommandData getCommandData() {
		return cmd;
	}
	
	public ChannelMessageEvent getTwitchMessage() {
		return twitchMessage;
	}
}
