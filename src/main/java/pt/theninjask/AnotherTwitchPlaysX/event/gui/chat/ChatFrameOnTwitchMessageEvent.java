package pt.theninjask.AnotherTwitchPlaysX.event.gui.chat;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;

import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.ChatFrame;

public class ChatFrameOnTwitchMessageEvent extends BasicEvent{
	
	private ChatFrame frame;
	private ChannelMessageEvent twitchMessage;

	public ChatFrameOnTwitchMessageEvent(ChatFrame frame, ChannelMessageEvent twitchMessage) {
		super(ChatFrameOnTwitchMessageEvent.class.getSimpleName());
		this.frame = frame;
		this.twitchMessage = twitchMessage;
	}

	public ChatFrame getChatFrame() {
		return frame;
	}

	public ChannelMessageEvent getTwitchMessage() {
		return twitchMessage;
	}
	
}
