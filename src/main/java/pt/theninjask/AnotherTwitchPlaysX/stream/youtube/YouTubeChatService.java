package pt.theninjask.AnotherTwitchPlaysX.stream.youtube;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponseException;
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
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.bus.config.Feature;
import net.engio.mbassy.bus.error.IPublicationErrorHandler;
import net.engio.mbassy.bus.error.PublicationError;

public class YouTubeChatService {
	private static final String LIVE_CHAT_FIELDS = "items(authorDetails(channelId,displayName,isChatModerator,isChatOwner,isChatSponsor,"
			+ "profileImageUrl),snippet(displayMessage,superChatDetails,publishedAt),id),"
			+ "nextPageToken,pollingIntervalMillis";

	private ExecutorService executor;
	private String APP_ID = "N/A";
	private String APP_NAME = "N/A";
	private YouTube youtube;
	private String liveChatId;
	private boolean isInitialized;
	private String nextPageToken;
	private Timer pollTimer;

	private MBassador<Object> dispatcher = new MBassador<Object>(
			new BusConfiguration().addPublicationErrorHandler(new IPublicationErrorHandler() {
				@Override
				public void handleError(PublicationError error) {
				}
			}).addFeature(Feature.SyncPubSub.Default()).addFeature(Feature.AsynchronousHandlerInvocation.Default())
					.addFeature(Feature.AsynchronousMessageDispatch.Default()));

	private Logger logger = null;
	private boolean echoMessage = false;
	private Queue<String> localMsg = new ConcurrentLinkedQueue<String>();
	private static final PrintStream VOID_STREAM = new PrintStream(new OutputStream() {
		@Override
		public void write(int b) throws IOException {
		}
	});

	public YouTubeChatService(String... app) {
		if (app != null)
			switch (app.length) {
			default:
				if (app.length < 1)
					break;
			case 2:
				this.APP_NAME = app[1];
			case 1:
				this.APP_ID = app[0];
				break;
			}
	}

	public YouTubeChatService(Logger logger, String... app) {
		this(app);
		this.logger = logger;
	}

	private void log(String message, Level level) {
		if (logger != null)
			logger.log(level, message);
	}

	public void start(final String videoId, final String clientSecret) {
		if (isInitialized)
			return;
		executor = Executors.newCachedThreadPool();
		executor.execute(() -> {
			try {
				// Build auth scopes
				List<String> scopes = new ArrayList<String>();
				scopes.add(YouTubeScopes.YOUTUBE_FORCE_SSL);
				scopes.add(YouTubeScopes.YOUTUBE_READONLY);

				// Authorize the request
				PrintStream tmp = System.out;
				System.setOut(VOID_STREAM);
				Credential credential = Auth.authorize(scopes, clientSecret, APP_ID);
				System.setOut(tmp);
				// This object is used to make YouTube Data API requests
				youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
						.setApplicationName(APP_NAME).build();
				// Get the live chat id
				String identity;
				if (videoId != null && !videoId.isEmpty()) {
					identity = "videoId " + videoId;
					YouTube.Videos.List videoList = youtube.videos().list(List.of("liveStreamingDetails"))
							.setFields("items/liveStreamingDetails/activeLiveChatId").setId(List.of(videoId));
					VideoListResponse response = videoList.execute();
					for (Video v : response.getItems()) {
						liveChatId = v.getLiveStreamingDetails().getActiveLiveChatId();
						if (liveChatId != null && !liveChatId.isEmpty()) {
							log("Live chat id: " + liveChatId, Level.INFO);
							break;
						}
					}
				} else {
					identity = "current user";
					YouTube.LiveBroadcasts.List broadcastList = youtube.liveBroadcasts().list(List.of("snippet"))
							.setFields("items/snippet/liveChatId").setBroadcastType("all").setBroadcastStatus("active");
					LiveBroadcastListResponse broadcastListResponse = broadcastList.execute();
					for (LiveBroadcast b : broadcastListResponse.getItems()) {
						liveChatId = b.getSnippet().getLiveChatId();
						if (liveChatId != null && !liveChatId.isEmpty()) {
							log("Live chat id: " + liveChatId, Level.INFO);
							break;
						}
					}
				}

				if (liveChatId == null || liveChatId.isEmpty()) {
					log("Could not find live chat for " + identity, Level.INFO);
					return;
				}

				// Initialize next page token
				LiveChatMessageListResponse response = youtube.liveChatMessages().list(liveChatId, List.of("snippet"))
						.setFields("nextPageToken, pollingIntervalMillis").execute();
				nextPageToken = response.getNextPageToken();
				isInitialized = true;
				poll(response.getPollingIntervalMillis());
				log("YTC Service started", Level.INFO);
			} catch (Throwable t) {
				stop();
				if (t instanceof TokenResponseException || t instanceof UnknownHostException || t instanceof IOException)
					dispatcher.post(new YouTubeChatServiceException(t)).now();
				log(t.getMessage(), Level.WARNING);
				t.printStackTrace();
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
	
	public class YouTubeChatServiceException extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public YouTubeChatServiceException(Throwable reason) {
			super(reason);
		}
		
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
					YouTube.LiveChatMessages.Insert liveChatInsert = youtube.liveChatMessages()
							.insert(List.of("snippet"), liveChatMessage);
					String id = liveChatInsert.execute().getId();
					if (!echoMessage)
						localMsg.add(id);
				} catch (Throwable t) {
					stop();
					if (t instanceof TokenResponseException || t instanceof UnknownHostException || t instanceof IOException)
						dispatcher.post(new YouTubeChatServiceException(t)).now();
					log(t.getMessage(), Level.WARNING);
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
					stop();
					if (t instanceof TokenResponseException || t instanceof UnknownHostException)
						dispatcher.post(t).now();
					log(t.getMessage(), Level.WARNING);
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
					log("Getting live chat messages", Level.INFO);
					LiveChatMessageListResponse response = youtube.liveChatMessages()
							.list(liveChatId, List.of("id", "snippet", "authorDetails")).setPageToken(nextPageToken)
							.setFields(LIVE_CHAT_FIELDS).execute();
					nextPageToken = response.getNextPageToken();
					final List<LiveChatMessage> messages = response.getItems();
					if (executor != null)
						executor.execute(new Runnable() {
							@Override
							public void run() {
								log(String.format("Received %s messages", messages.size()), Level.INFO);
								for (int i = 0; i < messages.size(); i++) {
									if (localMsg.remove(messages.get(i).getId()))
										continue;
									dispatcher.post(messages.get(i)).now();
									try {
										Thread.sleep(response.getPollingIntervalMillis() / messages.size());
									} catch (InterruptedException e) {
									}
								}
							}
						});
					log("POLL DELAY: " + response.getPollingIntervalMillis(), Level.INFO);
					poll(response.getPollingIntervalMillis());
				} catch (Throwable t) {
					stop();
					if (t instanceof TokenResponseException || t instanceof UnknownHostException)
						dispatcher.post(t).now();
					log(t.getMessage(), Level.WARNING);
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
