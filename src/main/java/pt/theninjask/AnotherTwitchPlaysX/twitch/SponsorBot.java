package pt.theninjask.AnotherTwitchPlaysX.twitch;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import org.kitteh.irc.client.library.event.connection.ClientConnectionEndedEvent;

import net.engio.mbassy.listener.Handler;
import pt.theninjask.AnotherTwitchPlaysX.exception.NotSetupException;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class SponsorBot implements Runnable{
	
	private static SponsorBot singleton = new SponsorBot();

	private static final String SPONSOR_MSG = "Hi guys this ain't %s but the creator of the app AnotherTwitchPlaysX if you wanna check it out go to %s!";
	
	private int cooldown;
	
	private Thread sponsor;
	
	private final AtomicBoolean running = new AtomicBoolean(false);
	
	private SponsorBot() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s()", SponsorBot.class.getSimpleName()));
		this.cooldown = 30 * 60 * 1000;
	}
	
	public static SponsorBot getInstance() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.getInstance()", SponsorBot.class.getSimpleName()));
		return singleton;
	}
	
	public static String getSponsorMsg() {
		return String.format(SPONSOR_MSG, 
				DataManager.getSession().getNickname(),
				"https://github.com/TheNinjask/AnotherTwitchPlaysX");
	}
	
	public void setCooldown(int minutes) {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.setCooldown()", SponsorBot.class.getSimpleName()));
		this.cooldown = minutes * 60 * 1000;
	}
	
	public void start() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.start()", SponsorBot.class.getSimpleName()));
		sponsor = new Thread(this);
		TwitchPlayer.getInstance().registerEventListener(this);
		sponsor.start();
	}
	
	public void stop() {
		Constants.printVerboseMessage(Level.INFO, String.format("%s.stop()", SponsorBot.class.getSimpleName()));
		running.set(false);
		try {
			TwitchPlayer.getInstance().unregisterEventListener(this);			
		}catch (NotSetupException e) {
			Constants.printVerboseMessage(Level.WARNING, e);
		}
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
				Constants.printVerboseMessage(Level.WARNING, e);
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
