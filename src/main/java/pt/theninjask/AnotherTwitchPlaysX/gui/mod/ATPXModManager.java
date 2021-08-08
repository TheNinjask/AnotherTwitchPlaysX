package pt.theninjask.AnotherTwitchPlaysX.gui.mod;

import java.util.ArrayList;
import java.util.List;

public class ATPXModManager {
	
	private static ATPXModManager singleton = new ATPXModManager();
	
	private List<ATPXMod> mods = new ArrayList<>();
	
	private ATPXModManager() {
	}
	
	public static void addMod(ATPXMod mod) {
		singleton.mods.add(mod);
	}
	
	public static void removeMod(ATPXMod mod) {
		singleton.mods.remove(mod);
	}
	
	public static void removeAllMods() {
		singleton.mods.clear();
	}
}
