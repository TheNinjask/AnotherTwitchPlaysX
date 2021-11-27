package pt.theninjask.AnotherTwitchPlaysX.lan.en;

import pt.theninjask.AnotherTwitchPlaysX.lan.AllCommandLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.CommandLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.ConstantsLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.ControlDataLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.EmbeddedModMenuLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.ExceptionsLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.Lang;
import pt.theninjask.AnotherTwitchPlaysX.lan.MainLoginLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.LoginLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.MainMenuLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.StringToKeyCodeLang;
import pt.theninjask.AnotherTwitchPlaysX.lan.ChatLang;

public class EnglishLang implements Lang{

	private ExceptionsLang exceptionsLang = new ExceptionsLang(){};
	
	private ConstantsLang constantsLang = new ConstantsLang(){};
	
	private MainMenuLang mainMenuLang = new MainMenuLang() {};

	private LoginLang loginLang = new LoginLang(){};
	
	private EmbeddedModMenuLang embeddedModMenuLang = new EmbeddedModMenuLang(){};
	
	private ChatLang twitchChatLang = new ChatLang(){};
	
	private AllCommandLang allCommandLang = new AllCommandLang(){};
	
	private CommandLang commandLang = new CommandLang(){};
	
	private ControlDataLang ControlDataLang = new ControlDataLang(){};
	
	private StringToKeyCodeLang stringToKeyCodeLang = new StringToKeyCodeLang(){};
	
	private MainLoginLang mainLoginLang = new MainLoginLang() {};
	
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
	public ChatLang getChat() {
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
	public ControlDataLang getControlData() {
		return ControlDataLang;
	}

	@Override
	public StringToKeyCodeLang getStringToKeyCode() {
		return stringToKeyCodeLang;
	}

	@Override
	public MainLoginLang getMainLogin() {
		return mainLoginLang;
	}

}
