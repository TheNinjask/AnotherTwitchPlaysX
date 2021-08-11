package pt.theninjask.AnotherTwitchPlaysX.data;

public class ATPXConfig implements Data {

	public enum Verbose{
		NONE,VERBOSE,WARNING
	}
	
	private boolean outsideConsole = false;
	
	private Verbose verbose = Verbose.NONE;
	
	private boolean printCommand = false;
	
	private boolean eventLog = false;
	
	private boolean debug = false;
	
	private boolean disableSession = false;
	
	private boolean help = false;
	
	private String twitchNickname = null;
	
	private String twitchChannel = null;
	
	private String twitchToken = null;
	
	private String youtubeToken = null;
	
	private String youtubeVideoId = null;
	
	public ATPXConfig() {
	}

	public boolean isOutsideConsole() {
		return outsideConsole;
	}

	public void setOutsideConsole(boolean outsideConsole) {
		this.outsideConsole = outsideConsole;
	}

	public Verbose getVerbose() {
		return verbose;
	}

	public void setVerbose(Verbose verbose) {
		this.verbose = verbose;
	}

	public boolean isPrintCommand() {
		return printCommand;
	}

	public void setPrintCommand(boolean printCommand) {
		this.printCommand = printCommand;
	}

	public boolean isEventLog() {
		return eventLog;
	}

	public void setEventLog(boolean eventLog) {
		this.eventLog = eventLog;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isDisableSession() {
		return disableSession;
	}

	public void setDisableSession(boolean disableSession) {
		this.disableSession = disableSession;
	}

	public boolean isHelp() {
		return help;
	}

	public void setHelp(boolean help) {
		this.help = help;
	}

	public String getTwitchNickname() {
		return twitchNickname;
	}

	public void setTwitchNickname(String twitchNickname) {
		this.twitchNickname = twitchNickname;
	}

	public String getTwitchChannel() {
		return twitchChannel;
	}

	public void setTwitchChannel(String twitchChannel) {
		this.twitchChannel = twitchChannel;
	}

	public String getTwitchToken() {
		return twitchToken;
	}

	public void setTwitchToken(String twitchToken) {
		this.twitchToken = twitchToken;
	}

	public String getYoutubeToken() {
		return youtubeToken;
	}

	public void setYoutubeToken(String youtubeToken) {
		this.youtubeToken = youtubeToken;
	}

	public String getYoutubeVideoId() {
		return youtubeVideoId;
	}

	public void setYoutubeVideoId(String youtubeVideoId) {
		this.youtubeVideoId = youtubeVideoId;
	}
	
	
	
}
