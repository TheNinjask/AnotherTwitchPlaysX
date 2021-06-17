package pt.theninjask.AnotherTwitchPlaysX.util;

import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class TaskCooldown {

	private long ms;

	private long start = 0;

	private AtomicBoolean isDisabled = new AtomicBoolean(false);
	
	public TaskCooldown() {
		this.ms = 0;
	}

	public TaskCooldown(long secs) {
		this.ms = secs*1000;
	}

	@JsonProperty("timer")
	public void setTimer(long secs) {
		this.ms = secs*1000;
	}

	@JsonProperty("timer")
	public long getTimer() {
		return ms/1000;
	}

	public void run(Runnable task) {
		if(ms<=0) {
			task.run();
			return;
		}
		if(inCooldown())
			return;
		isDisabled.set(true);
		task.run();
		start = System.currentTimeMillis();
		isDisabled.set(false);
	}
	
	public boolean inCooldown() {
			if(isDisabled.get())
				return false;
			long duration = System.currentTimeMillis() - start;
			if (duration >= ms) {
				return false;
			}
			return true;
	}

}
