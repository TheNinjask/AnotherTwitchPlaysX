package pt.theninjask.AnotherTwitchPlaysX.gui.mod.embedded.extra;

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

public class DemoLang implements Lang {

	@Override
	public ConstantsLang getConstants() {
		return new ConstantsLang() {};
	}

	@Override
	public ExceptionsLang getExceptions() {
		return new ExceptionsLang() {};
	}

	@Override
	public MainMenuLang getMainMenu() {
		return new MainMenuLang() {};
	}

	@Override
	public LoginLang getLogin() {
		return new LoginLang() {};
	}

	@Override
	public EmbeddedModMenuLang getEmbeddedModMenu() {
		return new EmbeddedModMenuLang() {};
	}

	@Override
	public TwitchChatLang getTwitchChat() {
		return new TwitchChatLang() {};
	}

	@Override
	public AllCommandLang getAllCommand() {
		return new AllCommandLang() {};
	}

	@Override
	public CommandLang getCommand() {
		return new CommandLang() {};
	}

	@Override
	public ControlDataLang getControlData() {
		return new ControlDataLang() {};
	}

	@Override
	public StringToKeyCodeLang getStringToKeyCode() {
		return new StringToKeyCodeLang() {};
	}

	@Override
	public String getTitle() {
		return "DEMO";
	}
}
