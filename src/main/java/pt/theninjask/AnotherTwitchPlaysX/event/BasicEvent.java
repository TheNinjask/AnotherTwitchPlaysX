package pt.theninjask.AnotherTwitchPlaysX.event;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BasicEvent implements Event{
	
	private UUID id;
	
	private String name;
	
	private AtomicBoolean cancelled;
	
	public BasicEvent() {
		this(BasicEvent.class.getSimpleName());
	}
	
	public BasicEvent(String name) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.cancelled = new AtomicBoolean();
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
	
}
