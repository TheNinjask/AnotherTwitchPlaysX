package pt.theninjask.AnotherTwitchPlaysX.twitch;

import java.util.concurrent.atomic.AtomicBoolean;

import org.kitteh.irc.client.library.event.connection.ClientConnectionEndedEvent;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.exception.NotSetupException;

public class SponsorBot implements Runnable{
	
	private static SponsorBot singleton = new SponsorBot();

	private static final String SPONSOR_MSG = "Hi guys this ain't %s but the creator of the app AnotherTwitchPlaysX if you wanna check it out go to %s!";
	
	private int cooldown;
	
	private Thread sponsor;
	
	private final AtomicBoolean running = new AtomicBoolean(false);
	
	private SponsorBot() {
		this.cooldown = 30 * 60 * 1000;
	}
	
	public static SponsorBot getInstance() {
		return singleton;
	}
	
	public static String getSponsorMsg() {
		return String.format(SPONSOR_MSG, 
				DataManager.getInstance().getSession().getNickname(),
				"#INSERT LINK");
	}
	
	public void setCooldown(int minutes) {
		this.cooldown = minutes * 60 * 1000;
	}
	
	public void start() {
		sponsor = new Thread(this);
		TwitchPlayer.getInstance().registerEventListener(this);
		sponsor.start();
	}
	
	public void stop() {
		running.set(false);
		try {
			TwitchPlayer.getInstance().unregisterEventListener(this);			
		}catch (NotSetupException e) {}
		if(sponsor!=null && sponsor.isAlive())
			sponsor.interrupt();
	}

	public void run() {
		running.set(true);
        while (running.get()) {
        	try {
        		TwitchPlayer.getInstance().sendMessage(
        				getSponsorMsg()
        				);
				Thread.sleep(cooldown);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
         } 
	}
	// The way the app is built this is never gonna happen
	/*@Handler
	public void onConection(ClientConnectionEstablishedEvent event) {
	}*/
	
	@Handler
	public void onLeave(ClientConnectionEndedEvent event) {
		this.stop();		
	}
	
}
