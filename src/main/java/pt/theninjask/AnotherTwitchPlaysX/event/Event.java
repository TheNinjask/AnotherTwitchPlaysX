package pt.theninjask.AnotherTwitchPlaysX.event;

public interface Event {
	
	public String getName();
	
	public String getID();
	
	public void setCancelled(boolean cancelled);

	public boolean isCancelled();
	
}
