package pt.theninjask.AnotherTwitchPlaysX.event.datamanager;

import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;
import pt.theninjask.AnotherTwitchPlaysX.util.ColorTheme;

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
