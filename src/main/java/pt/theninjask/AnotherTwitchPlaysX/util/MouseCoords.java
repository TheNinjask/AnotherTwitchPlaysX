package pt.theninjask.AnotherTwitchPlaysX.util;

import java.awt.MouseInfo;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MouseCoords implements Runnable {

	private AtomicInteger x;

	private AtomicInteger y;

	private AtomicBoolean isRunning;

	private ConcurrentLinkedQueue<MouseCoordsListener> queue;

	private Thread worker;

	private static MouseCoords singleton = new MouseCoords();

	public MouseCoords() {
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
			start();
		}
	}

	public void unregisterListener(MouseCoordsListener event) {
		queue.remove(event);
		if(queue.isEmpty()) {
			stop();
		}
	}

	public void clearListeners() {
		queue.clear();
		stop();
	}

	private void start() {
		worker = new Thread(this);
		worker.start();
	}

	@Override
	public void run() {
		isRunning.set(true);
		while (isRunning.get()) {
			x.set(MouseInfo.getPointerInfo().getLocation().x);
			y.set(MouseInfo.getPointerInfo().getLocation().y);
			try {
				queue.forEach(e -> {
					e.function(x, y);
				});
			} catch (Exception e) {
			}
		}
	}

	private void stop() {
		isRunning.set(false);
	}
}
