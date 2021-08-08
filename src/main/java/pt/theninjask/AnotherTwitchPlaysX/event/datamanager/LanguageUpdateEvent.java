package pt.theninjask.AnotherTwitchPlaysX.event.datamanager;

import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;

public class LanguageUpdateEvent extends BasicEvent {

	private Lang language;
	
	public LanguageUpdateEvent(Lang language) {
		super(LanguageUpdateEvent.class.getSimpleName());
		this.language = language;
	}
	
	public Lang getLanguage() {
		return language;
	}
	
}
