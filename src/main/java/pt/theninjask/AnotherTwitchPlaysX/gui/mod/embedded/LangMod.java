package pt.theninjask.AnotherTwitchPlaysX.gui.mod.embedded;

import javax.swing.JPanel;

import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.embedded.extra.DemoLang;
import pt.theninjask.AnotherTwitchPlaysX.stream.DataManager;

@ATPXModProps(hasPanel = false, keepLoaded = false)
public class LangMod extends ATPXMod {

	@Override
	public void refresh() {
		DataManager.setLanguage(new DemoLang());
	}

	@Override
	public JPanel getJPanelInstance() {
		return null;
	}

}
