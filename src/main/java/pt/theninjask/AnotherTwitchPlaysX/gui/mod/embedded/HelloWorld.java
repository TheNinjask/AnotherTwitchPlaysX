package pt.theninjask.AnotherTwitchPlaysX.gui.mod.embedded;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import pt.theninjask.AnotherTwitchPlaysX.gui.mainMenu.MainMenuPanel;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

@ATPXModProps(hasPanel = false)
public class HelloWorld extends ATPXMod {

	public HelloWorld() {
		JButton modButton = MainMenuPanel.getInstance().getModButton();
		for (ActionListener elem : modButton.getActionListeners()) {
			modButton.removeActionListener(elem);
		}
		modButton.addActionListener(l->{
			Constants.showMessageDialog("Greetings World!", "Title-Hello World!");
		});
	}
	
	@Override
	public JPanel getJPanelInstance() {
		return null;
	}

	@Override
	public void refresh() {
		//DO NOTHING
	}

}
