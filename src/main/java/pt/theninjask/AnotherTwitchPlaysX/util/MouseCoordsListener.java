package pt.theninjask.AnotherTwitchPlaysX.util;

import java.util.concurrent.atomic.AtomicInteger;

@FunctionalInterface
public interface MouseCoordsListener {

	public void function(AtomicInteger x, AtomicInteger y);
	
}
