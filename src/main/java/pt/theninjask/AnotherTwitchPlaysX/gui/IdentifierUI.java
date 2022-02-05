package pt.theninjask.AnotherTwitchPlaysX.gui;

import java.util.Map;

import pt.theninjask.AnotherTwitchPlaysX.gui.chat.ChatFrame;
import pt.theninjask.AnotherTwitchPlaysX.gui.command.AllCommandPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.command.CommandPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.command.ControlDataPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.command.util.StringToKeyCodePanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.MainLoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.TwitchLoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.login.YoutubeLoginPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.util.PopOutFrame;

public enum IdentifierUI {

	UNKNOWN(-1, 0), MODFRAME(0, 0b1), MODPANEL(0, 0b10), MAINFRAME(1, 0b1), POPOUTFRAME(2, 0b1), MAINMENUPANEL(1, 0b10),
	MAINLOGINPANEL(2, 0b10), TWITCHLOGINPANEL(3, 0b10), YOUTUBELOGINPANEL(4, 0b10), ALLCOMMANDPANEL(5, 0b10),
	COMMANDPANEL(6, 0b10), CONTROLDATAPANEL(7, 0b10), STRINGTOCODEPANEL(8, 0b10), CHATFRAME(3, 0b1);

	private int id;
	private int type;

	private static final Map<Class<?>, IdentifierUI> map = Map.ofEntries(
			Map.entry(MainFrame.class, MAINFRAME),
			Map.entry(PopOutFrame.class, POPOUTFRAME),
			Map.entry(MainMenuPanel.class, MAINMENUPANEL),
			Map.entry(MainLoginPanel.class, MAINLOGINPANEL),
			Map.entry(TwitchLoginPanel.class, TWITCHLOGINPANEL),
			Map.entry(YoutubeLoginPanel.class, YOUTUBELOGINPANEL),
			Map.entry(AllCommandPanel.class, ALLCOMMANDPANEL),
			Map.entry(CommandPanel.class, COMMANDPANEL),
			Map.entry(ControlDataPanel.class, CONTROLDATAPANEL),
			Map.entry(StringToKeyCodePanel.class, STRINGTOCODEPANEL),
			Map.entry(ChatFrame.class, CHATFRAME)
			);
	
	private IdentifierUI(int id, int type) {
		this.id = id;
		this.type = type;
	}

	public boolean equals(IdentifierUI id) {
		return this.id == id.id && this.type == id.type;
	}

	public boolean equals(int id, int type) {
		return this.id == id && this.type == type;
	}

	public static IdentifierUI getIDFrom(Object obj) {
		return map.getOrDefault(obj.getClass(), UNKNOWN);
	}

}
