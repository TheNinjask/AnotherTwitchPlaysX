package pt.theninjask.AnotherTwitchPlaysX.stream.youtube;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.LiveBroadcast;
import com.google.api.services.youtube.model.LiveBroadcastListResponse;
import com.google.api.services.youtube.model.LiveChatMessage;
import com.google.api.services.youtube.model.LiveChatMessageListResponse;
import com.google.api.services.youtube.model.LiveChatMessageSnippet;
import com.google.api.services.youtube.model.LiveChatTextMessageDetails;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import net.engio.mbassy.bus.MBassador;
import pt.theninjask.AnotherTwitchPlaysX.App;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class YouTubeChatService {
	private static final String LIVE_CHAT_FIELDS = "items(authorDetails(channelId,displayName,isChatModerator,isChatOwner,isChatSponsor,"
			+ "profileImageUrl),snippet(displayMessage,superChatDetails,publishedAt),id),"
			+ "nextPageToken,pollingIntervalMillis";
	private ExecutorService executor;
	private YouTube youtube;
	private String liveChatId;
	private boolean isInitialized;
	private String nextPageToken;
	private Timer pollTimer;
	private MBassador<Object> dispatcher = Constants.setupDispatcher();
	private Logger logger = null;
	private boolean echoMessage = false;
	private Queue<String> localMsg = new ConcurrentLinkedQueue<String>();
	
	public YouTubeChatService() {
	}

	public YouTubeChatService(Logger logger) {
		this.logger = logger;
	}

	private void log(String message) {
		if (logger != null)
			logger.info(message);
	}

	public void start(final String videoId, final String clientSecret) {
		if (isInitialized)
			return;
		executor = Executors.newCachedThreadPool();
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					// Build auth scopes
					List<String> scopes = new ArrayList<String>();
					scopes.add(YouTubeScopes.YOUTUBE_FORCE_SSL);
					scopes.add(YouTubeScopes.YOUTUBE);

					// Authorize the request
					PrintStream tmp = System.out;
					System.setOut(Constants.VOID_STREAM);
					Credential credential = Auth.authorize(scopes, clientSecret, App.ID);
					System.setOut(tmp);
					// This object is used to make YouTube Data API requests
					youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
							.setApplicationName(App.NAME).build();
					// Get the live chat id
					String identity;
					if (videoId != null && !videoId.isEmpty()) {
						identity = "videoId " + videoId;
						YouTube.Videos.List videoList = youtube.videos().list("liveStreamingDetails")
								.setFields("items/liveStreamingDetails/activeLiveChatId").setId(videoId);
						VideoListResponse response = videoList.execute();
						for (Video v : response.getItems()) {
							liveChatId = v.getLiveStreamingDetails().getActiveLiveChatId();
							if (liveChatId != null && !liveChatId.isEmpty()) {
								log("Live chat id: " + liveChatId);
								break;
							}
						}
					} else {
						identity = "current user";
						YouTube.LiveBroadcasts.List broadcastList = youtube.liveBroadcasts().list("snippet")
								.setFields("items/snippet/liveChatId").setBroadcastType("all")
								.setBroadcastStatus("active");
						LiveBroadcastListResponse broadcastListResponse = broadcastList.execute();
						for (LiveBroadcast b : broadcastListResponse.getItems()) {
							liveChatId = b.getSnippet().getLiveChatId();
							if (liveChatId != null && !liveChatId.isEmpty()) {
								log("Live chat id: " + liveChatId);
								break;
							}
						}
					}

					if (liveChatId == null || liveChatId.isEmpty()) {
						log("Could not find live chat for " + identity);
						return;
					}

					// Initialize next page token
					LiveChatMessageListResponse response = youtube.liveChatMessages().list(liveChatId, "snippet")
							.setFields("nextPageToken, pollingIntervalMillis").execute();
					nextPageToken = response.getNextPageToken();
					isInitialized = true;
					poll(response.getPollingIntervalMillis());
					log("YTC Service started");
				} catch (Throwable t) {
					log(t.getMessage());
					t.printStackTrace();
				}
			}
		});
	}

	public void stop() {
		stopPolling();
		if (executor != null) {
			executor.shutdown();
			executor = null;
		}
		liveChatId = null;
		isInitialized = false;
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public void subscribe(Object listener) {
		dispatcher.subscribe(listener);
	}

	public void unsubscribe(Object listener) {
		dispatcher.unsubscribe(listener);
	}

	/**
	 * Posts a live chat message and notifies the caller of the message Id posted.
	 */
	public void postMessage(String message) {
		if (!isInitialized) {
			return;
		}

		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					LiveChatMessage liveChatMessage = new LiveChatMessage();
					LiveChatMessageSnippet snippet = new LiveChatMessageSnippet();
					snippet.setType("textMessageEvent");
					snippet.setLiveChatId(liveChatId);
					LiveChatTextMessageDetails details = new LiveChatTextMessageDetails();
					details.setMessageText(message);
					snippet.setTextMessageDetails(details);
					liveChatMessage.setSnippet(snippet);
					YouTube.LiveChatMessages.Insert liveChatInsert = youtube.liveChatMessages().insert("snippet",
							liveChatMessage);
					String id = liveChatInsert.execute().getId();
					if(!echoMessage)
						localMsg.add(id);
				} catch (Throwable t) {
					log(t.getMessage());
					t.printStackTrace();
				}
			}
		});
	}

	public void deleteMessage(String messageId) {
		if (messageId == null || messageId.isEmpty() || executor == null) {
			return;
		}
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					YouTube.LiveChatMessages.Delete liveChatDelete = youtube.liveChatMessages().delete(messageId);
					liveChatDelete.execute();
				} catch (Throwable t) {
					log(t.getMessage());
					t.printStackTrace();
				}
			}
		});
	}

	private void poll(long delay) {
		pollTimer = new Timer();
		pollTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					// Get chat messages from YouTube
					log("Getting live chat messages");
					LiveChatMessageListResponse response = youtube.liveChatMessages()
							.list(liveChatId, "id, snippet, authorDetails").setPageToken(nextPageToken)
							.setFields(LIVE_CHAT_FIELDS).execute();
					nextPageToken = response.getNextPageToken();
					final List<LiveChatMessage> messages = response.getItems();
					if (executor != null)
						executor.execute(new Runnable() {
							@Override
							public void run() {
								log(String.format("Received %s messages", messages.size()));
								for (int i = 0; i < messages.size(); i++) {
									if(localMsg.remove(messages.get(i).getId()))
										continue;
									dispatcher.post(messages.get(i)).now();
									try {
										Thread.sleep(response.getPollingIntervalMillis() / messages.size());
									} catch (InterruptedException e) {
									}
								}
							}
						});
					log("POLL DELAY: " + response.getPollingIntervalMillis());
					poll(response.getPollingIntervalMillis());
				} catch (Throwable t) {
					log(t.getMessage());
					t.printStackTrace();
				}
			}
		}, delay);
	}

	private void stopPolling() {
		if (pollTimer != null) {
			pollTimer.cancel();
			pollTimer = null;
		}
	}

	public boolean isEchoing() {
		return echoMessage;
	}

	public void setEcho(boolean echo) {
		this.echoMessage = echo;
	}
}
