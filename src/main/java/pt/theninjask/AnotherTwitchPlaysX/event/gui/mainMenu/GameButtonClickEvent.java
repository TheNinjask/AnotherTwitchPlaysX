package pt.theninjask.AnotherTwitchPlaysX.event.gui.mainMenu;

import javax.swing.JButton;

import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;

public class GameButtonClickEvent extends BasicEvent{
	
	private JButton button;
	
	private boolean active;
	
	public GameButtonClickEvent(JButton button, boolean active) {
		super(GameButtonClickEvent.class.getSimpleName());
		this.button = button;
		this.active = active;
	}
	
	public JButton getButton() {
		return button;
	}
	
	public boolean getActive() {
		return active;
	}
	
}
