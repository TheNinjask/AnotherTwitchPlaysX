package pt.theninjask.AnotherTwitchPlaysX.data;

import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;

public class CommandDataOnMessageMatchedEvent extends BasicEvent {

	private String user;
	private String message;

	public CommandDataOnMessageMatchedEvent(CommandData commandData, String user, String message) {
		super(CommandDataOnMessageMatchedEvent.class.getSimpleName());
		this.user = user;
		this.message = message;
	}

	public String getUser() {
		return user;
	}

	public String getMessage() {
		return message;
	}

}
