package pt.theninjask.AnotherTwitchPlaysX.event.gui.chat;

import com.google.api.services.youtube.model.LiveChatMessage;

import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.chat.ChatFrame;

public class ChatFrameOnYouTubeMessageEvent extends BasicEvent {
	
	private ChatFrame frame;
	private LiveChatMessage youtubeMessage;

	public ChatFrameOnYouTubeMessageEvent(ChatFrame frame, LiveChatMessage youtubeMessage) {
		super(ChatFrameOnYouTubeMessageEvent.class.getSimpleName());
		this.frame = frame;
		this.youtubeMessage = youtubeMessage;
	}

	public ChatFrame getChatFrame() {
		return frame;
	}

	public LiveChatMessage getYouTubeMessage() {
		return youtubeMessage;
	}

}
