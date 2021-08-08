package pt.theninjask.AnotherTwitchPlaysX.event.gui.chat;

import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.ChatFrame;

public class ChatFrameOnMessageEvent extends BasicEvent{

	private ChatFrame frame;
	
	private String nick;
	
	private String message;
	
	public ChatFrameOnMessageEvent(ChatFrame frame, String nick, String message) {
		super(ChatFrameOnMessageEvent.class.getSimpleName());
		this.frame = frame;
		this.nick = nick;
		this.message = message;
	}

	public ChatFrame getChatFrame() {
		return frame;
	}

	public String getNick() {
		return nick;
	}

	public String getMessage() {
		return message;
	}
	
	
	
}
