package pt.theninjask.AnotherTwitchPlaysX.util;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class KeyPressedAdapter implements KeyEventDispatcher {

	private static volatile Set<Integer> pressedKeys = new CopyOnWriteArraySet<Integer>();

	public static boolean isKeyPressed(int keycode) {
		return pressedKeys.contains(keycode);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {
		synchronized (this) {
			switch (e.getID()) {
			case KeyEvent.KEY_PRESSED:
				pressedKeys.add(e.getKeyCode());
				break;
			case KeyEvent.KEY_RELEASED:
				pressedKeys.remove(e.getKeyCode());
				break;
			}
			return false;
		}
	}

}
