package pt.theninjask.AnotherTwitchPlaysX.lan;

public interface Lang {
	
	public default String getTitle() {
		return "AnotherTwitchPlaysX";
	};
	
	public default String getID() {
		return "ATPX";
	}
	
	public ConstantsLang getConstants();
	
	public ExceptionsLang getExceptions();
	
	public MainMenuLang getMainMenu();
	
	public LoginLang getLogin();
	
	public EmbeddedModMenuLang getEmbeddedModMenu();
	
	public ChatLang getChat();
	
	public AllCommandLang getAllCommand();
	
	public CommandLang getCommand();
	
	public ControlDataLang getControlData();
	
	public StringToKeyCodeLang getStringToKeyCode();
	
	public MainLoginLang getMainLogin();
	
	public default String getOkOpt() {
		return "Ok";
	}
	
	public default String getGoBackOpt() {
		return "Go Back";
	}
	
	public default String getNA() {
		return "N/A";
	}
	
	public default String getSavingSession() {
		return "Session saved in %s!";
	};
	
	public default String getSavingSessionTitle() {
		return "Saving Session";
	};
	
	public default String getLanTag() {
		return "EN";
	}
	
	public default String getAutoLoadModFail() {
		return "Could not load mod %s";
	}
	
	public default String getAutoLoadModFailTitle() {
		return "Mod Not Loaded";
	}
	
	public default String getUpdateNoticeTitle() {
		return "There is a new update available!";
	}
	
	public default String getUpdateNoticeWebsiteOption() {
		return "Go to download page";
	}
	
	public default String getUpdateNoticeDownloadOption() {
		return "Download";
	}
	
	public default String getUpdateNoticeSkipOption() {
		return "Skip";
	}
	
	public default String getUpdateNoticeTitleContent() {
		return "Update %s - %s";
	}
	
	public default String getHiddedItemTray() {
		return "Hide Window";
	}
	
	public default String getConsoleItemTray() {
		return "Show/Hide Console";
	}
	
	public default String getExitItemTray() {
		return "Exit";
	}
	
	public default String getCheckUpdateItemTray() {
		return "Check for update";
	}
	
	public default String getCheckUpdateErrorItemTray() {
		return "An error occurred when checking for update.";
	}
	
	public default String getCheckUpdateErrorTitleItemTray() {
		return "Could not find update";
	}
	
	public default String getCheckUpdateNoneItemTray() {
		return "This ATPX's version is up to date!";
	}
	
	public default String getCheckUpdateNoneTitleItemTray() {
		return "No updates found";
	}
}
