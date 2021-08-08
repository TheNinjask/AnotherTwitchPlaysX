package pt.theninjask.AnotherTwitchPlaysX.event.gui.chat;

import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;

public class ChatFrameSendMessageEvent extends BasicEvent{

	private String nick;
	
	private String message;
	
	public ChatFrameSendMessageEvent(String nick, String message) {
		super(ChatFrameSendMessageEvent.class.getSimpleName());
		this.nick = nick;
		this.message = message;
	}
	
	public String getNick() {
		return nick;
	}
	
	public String getMessage() {
		return message;
	}
	
}
