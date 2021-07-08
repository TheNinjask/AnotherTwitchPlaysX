package pt.theninjask.AnotherTwitchPlaysX.lan;

public interface Lang {
	
	public default String getTitle() {
		return "Another TwitchPlaysX";
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
	
	public default String getLanTag() {
		return "EN";
	}
	
}
