package pt.theninjask.AnotherTwitchPlaysX.event.data;

import com.google.api.services.youtube.model.LiveChatMessage;

import pt.theninjask.AnotherTwitchPlaysX.data.CommandData;
import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;

public class CommandDataOnYouTubeMessage extends BasicEvent {

	private CommandData cmd;
	
	private LiveChatMessage youtubeMessage;
	
	public CommandDataOnYouTubeMessage(CommandData cmd, LiveChatMessage youtubeMessage) {
		super(CommandDataOnYouTubeMessage.class.getSimpleName());
		this.cmd = cmd;
		this.youtubeMessage = youtubeMessage;
	}
	
	public CommandData getCommandData() {
		return cmd;
	}
	
	public LiveChatMessage getYouTubeMessage() {
		return youtubeMessage;
	}
	
}
