package pt.theninjask.AnotherTwitchPlaysX.util;

import java.awt.Color;

public class ColorTheme {

	private String name;
	
	private Color font;
	
	private Color background;
	
	public ColorTheme(String name, Color font, Color background) {
		this.name = name;
		this.font = font;
		this.background = background;
	}

	public String getName() {
		return name;
	}

	public Color getFont() {
		return font;
	}

	public Color getBackground() {
		return background;
	}
}
