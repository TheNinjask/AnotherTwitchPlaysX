package pt.theninjask.AnotherTwitchPlaysX.event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BasicEvent implements Event {

	private UUID id;

	private String name;

	private AtomicBoolean cancelled;

	private boolean isFinished;

	private List<Runnable> afterEvent;

	public BasicEvent() {
		this(BasicEvent.class.getSimpleName());
	}

	public BasicEvent(String name) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.cancelled = new AtomicBoolean();

		this.afterEvent = new ArrayList<>();
		this.isFinished = false;
	}

	public String getName() {
		return name;
	}

	public String getID() {
		return id.toString();
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled.set(cancelled);
	}

	public boolean isCancelled() {
		return cancelled.get();
	}

	public synchronized void addAfterEvent(Runnable run) {
		if (isFinished)
			run.run();
		else
			afterEvent.add(run);
	}

	public synchronized void finishedEvent() {
		isFinished = true;
		afterEvent.forEach(run -> {
			run.run();
		});
	}

}
