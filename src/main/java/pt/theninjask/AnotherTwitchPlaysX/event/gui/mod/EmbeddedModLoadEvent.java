package pt.theninjask.AnotherTwitchPlaysX.event.gui.mod;

import pt.theninjask.AnotherTwitchPlaysX.event.BasicEvent;
import pt.theninjask.AnotherTwitchPlaysX.gui.mod.ATPXMod;

public class EmbeddedModLoadEvent extends BasicEvent {

	private ATPXMod mod;
	
	public EmbeddedModLoadEvent(ATPXMod mod) {
		super(EmbeddedModLoadEvent.class.getSimpleName());
		this.mod = mod;
	}
	
	public ATPXMod getMod() {
		return mod;
	}
	
}
