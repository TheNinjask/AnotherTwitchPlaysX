package pt.theninjask.AnotherTwitchPlaysX.event.gui.mainMenu;

import javax.swing.JButton;

import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;

public class ModButtonClickEvent extends BasicEvent {
	
	private JButton button;
	
	private boolean toSecretMenu;
	
	public ModButtonClickEvent(JButton button, boolean toSecretMenu) {
		super(ModButtonClickEvent.class.getSimpleName());
		this.button = button;
		this.toSecretMenu = toSecretMenu;
	}
	
	public JButton getButton() {
		return button;
	}
	
	public boolean toSecretMenu() {
		return toSecretMenu;
	}
	
}
