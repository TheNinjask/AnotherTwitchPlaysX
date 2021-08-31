package pt.theninjask.AnotherTwitchPlaysX.lan;

public interface ConstantsLang {

	public default String getDefaultErrorTitle() {
		return "An error has occurred!";
	}
	
	public default String getBrowserNotSupported() {
		return "Browsing is not supported!";
	}
	
	public default String getModInfoTitle() {
		return "%s - Mod Info";
	}
		
	public default String getModInfo() {
		return "You are loading a third party mod that was validated by the creator of this app!";
	}
	
	public default String getModWarnTitle() {
		return "%s - Mod Warning";
	}
	
	public default String getModWarn() {
		return "You are loading a mod that was not made by the creator of this app nor verified by them!\nProceed at your own risk.";
	}
	
	public default String getREADMEUnknown() {
		return "Unknown";
	}
	
	public default String getREADMENetExceptionTitle() {
		return "README - %s";
	}
	
	public default String getREADMENetException() {
		return "README: %s";
	}
	
	public default String getREADMEExceptionTitle() {
		return "README";
	}
	
	public default String getREADMEException() {
		return "README not available";
	}
}
