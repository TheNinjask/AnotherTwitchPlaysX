package pt.theninjask.AnotherTwitchPlaysX.gui.mod.embedded;

import javax.swing.JPanel;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.event.EventManager;
import pt.theninjask.AnotherTwitchPlaysX.event.gui.mainMenu.ModButtonClickEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXModProps;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

@ATPXModProps(hasPanel = false)
public class HelloWorld extends ATPXMod {

	public HelloWorld() {
		/*JButton modButton = MainMenuPanel.getInstance().getModButton();
		for (ActionListener elem : modButton.getActionListeners()) {
			modButton.removeActionListener(elem);
		}
		modButton.addActionListener(l->{
			Constants.showMessageDialog("Greetings World!", "Title-Hello World!");
		});*/
		EventManager.registerEventListener(this);	
	}
	
	@Override
	public JPanel getJPanelInstance() {
		return null;
	}

	@Override
	public void refresh() {
	}
	
	@Handler
	public void newModButton(ModButtonClickEvent event) {
		if(event.toSecretMenu())
			return;
		event.setCancelled(true);
		Constants.showMessageDialog("Greetings World!", "Title-Hello World!");
	}

}
