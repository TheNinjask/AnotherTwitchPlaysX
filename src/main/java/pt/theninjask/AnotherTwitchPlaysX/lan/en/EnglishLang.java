package pt.theninjask.AnotherTwitchPlaysX.lan.en;

import pt.theninjask.AnotherTwitchPlaysX.lan.AllCommandLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.CommandLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.ConstantsLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.ControlDataLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.EmbeddedModMenuLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.ExceptionsLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;
import pt.theninjask.AnotherTwitchPlaysX.lan.LoginLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.MainMenuLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.StringToKeyCodeLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.TwitchChatLang;

public class EnglishLang implements Lang{

	private ExceptionsLang exceptionsLang = new EnglishExceptionLang();
	
	private ConstantsLang constantsLang = new EnglishConstantsLang();
	
	private MainMenuLang mainMenuLang = new EnglishMainMenuLang();

	private LoginLang loginLang = new EnglishLoginLang();
	
	private EmbeddedModMenuLang embeddedModMenuLang = new EnglishEmbeddedModMenuLang();
	
	private TwitchChatLang twitchChatLang = new EnglishTwitchChatLang();
	
	private AllCommandLang allCommandLang = new EnglishAllCommandLang();
	
	private CommandLang commandLang = new EnglishCommandLang();
	
	private ControlDataLang ControlDataLang = new EnglishControlDataLang();
	
	private StringToKeyCodeLang stringToKeyCodeLang = new EnglishStringToKeyCodeLang();
	
	@Override
	public ExceptionsLang getExceptions() {
		return exceptionsLang;
	}

	@Override
	public ConstantsLang getConstants() {
		return constantsLang;
	}

	@Override
	public MainMenuLang getMainMenu() {
		return mainMenuLang;
	}
	
	@Override
	public LoginLang getLogin() {
		return loginLang;
	}

	@Override
	public EmbeddedModMenuLang getEmbeddedModMenu() {
		return embeddedModMenuLang;
	}

	@Override
	public TwitchChatLang getTwitchChat() {
		return twitchChatLang;
	}

	@Override
	public AllCommandLang getAllCommand() {
		return allCommandLang;
	}

	@Override
	public CommandLang getCommand() {
		return commandLang;
	}

	@Override
	public pt.theninjask.AnotherTwitchPlaysX.lan.ControlDataLang getControlData() {
		return ControlDataLang;
	}

	@Override
	public StringToKeyCodeLang getStringToKeyCode() {
		return stringToKeyCodeLang;
	}

}
