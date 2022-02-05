package pt.theninjask.AnotherTwitchPlaysX.event.datamanager;

import externalconsole.util.ColorTheme;
import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;

public class ColorThemeUpdateEvent extends BasicEvent {

	private ColorTheme theme;
	
	public ColorThemeUpdateEvent(ColorTheme theme) {
		super(ColorThemeUpdateEvent.class.getSimpleName());
		this.theme = theme;
	}

	public ColorTheme getTheme() {
		return theme;
	}
	
}
