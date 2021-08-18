package pt.theninjask.AnotherTwitchPlaysX.lan;

public interface EmbeddedModMenuLang {

	public default String getLabel() {
		return "Secret Embedded Mod Menu";
	}
	
	public default String getWarning() {
		return "(This stuff might not work)";
	}
	
	public default String getGoBack() {
		return "Go Back";
	}
	
}
