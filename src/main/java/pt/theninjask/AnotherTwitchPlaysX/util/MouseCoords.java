package pt.theninjask.AnotherTwitchPlaysX.util;

import java.awt.MouseInfo;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

public class MouseCoords implements NativeMouseInputListener {

	private AtomicInteger x;

	private AtomicInteger y;

	private AtomicBoolean isRunning;

	private ConcurrentLinkedQueue<MouseCoordsListener> queue;

	private static MouseCoords singleton = new MouseCoords();

	@FunctionalInterface
	public interface MouseCoordsListener {

		public void function(AtomicInteger x, AtomicInteger y);
		
	}
	
	private MouseCoords() {
		this.x = new AtomicInteger(MouseInfo.getPointerInfo().getLocation().x);
		this.y = new AtomicInteger(MouseInfo.getPointerInfo().getLocation().y);
		this.isRunning = new AtomicBoolean(false);
		this.queue = new ConcurrentLinkedQueue<MouseCoordsListener>();
	}

	public static MouseCoords getInstance() {
		return singleton;
	}

	public AtomicInteger getX() {
		return x;
	}

	public AtomicInteger getY() {
		return y;
	}

	public void registerListener(MouseCoordsListener event) {
		queue.add(event);
		if(!isRunning.get()) {
			try {
				GlobalScreen.registerNativeHook();
				GlobalScreen.addNativeMouseMotionListener(this);
				isRunning.set(true);
			} catch (NativeHookException e) {
				Constants.showExceptionDialog(e);
			}
		}
	}

	public void unregisterListener(MouseCoordsListener event) {
		queue.remove(event);
		if(queue.isEmpty()) {
			try {
				GlobalScreen.unregisterNativeHook();
				isRunning.set(false);
			} catch (NativeHookException e) {
				Constants.showExceptionDialog(e);
			}
		}
	}

	public void clearListeners() {
		queue.clear();
		try {
			GlobalScreen.unregisterNativeHook();
			isRunning.set(false);
		} catch (NativeHookException e) {
			Constants.showExceptionDialog(e);
		}
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent e) {
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent e) {
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent e) {
		x.set(e.getX());
		y.set(e.getY());
		queue.forEach(elem -> {
			elem.function(x, y);
		});	
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent e) {
	}
}
