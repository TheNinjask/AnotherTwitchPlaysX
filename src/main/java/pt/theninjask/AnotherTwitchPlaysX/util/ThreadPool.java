package pt.theninjask.AnotherTwitchPlaysX.util;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPool {

	private ThreadPoolExecutor executor;

	private static ThreadPool poolUnison = new ThreadPool();
	private static ThreadPool poolQueue = new ThreadPool();
	
	private static ThreadPool singleton = new ThreadPool();

	private ThreadPool() {
		executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		// I do not expect to reach this high but just in case of some1 with huge chat
		// above only applies if below is not commented
		// executor.setMaximumPoolSize(100);
	}

	public static ThreadPool getUnisonInstance() {
		return poolUnison;
	}

	public static ThreadPool getQueueInstance() {
		return poolQueue;
	}
	
	public static ThreadPool getInstance() {
		return singleton;
	}

	public void setCorePoolSize(int size) {
		executor.setCorePoolSize(size);
	}

	public void setMaximumPoolSize(int size) {
		executor.setMaximumPoolSize(size);
	}
	
	/**
	 * 
	 * @param task task to run
	 * @return if true means that it was set to be executed else
	 * if false means the pool is capped out most likely
	 * it is asking for more execution than it executes or task is null.
	 */
	public static boolean executeUnison(Runnable task) {
		try {
			poolUnison.executor.execute(task);			
		}catch (NullPointerException|RejectedExecutionException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param task task to run
	 * @return if true means that it was set to be executed else
	 * if false means the pool is capped out most likely
	 * it is asking for more execution than it executes or task is null.
	 */
	public static boolean executeQueue(Runnable task) {
		try {			
			poolQueue.executor.execute(task);
		}catch (NullPointerException|RejectedExecutionException e) {
			return false;
		}
		return true;
	}
	
	public static boolean execute(Runnable task) {
		try {			
			singleton.executor.execute(task);
		}catch (NullPointerException|RejectedExecutionException e) {
			return false;
		}
		return true;
	}
	
	public static void stopAll() {
		poolUnison.executor.shutdown();
		poolQueue.executor.shutdown();
		singleton.executor.shutdown();
	}

	public static void restartAll() {
		poolUnison.executor.shutdown();
		poolUnison = new ThreadPool();
		poolQueue.executor.shutdown();
		poolQueue = new ThreadPool();
		singleton.executor.shutdown();
		singleton = new ThreadPool();
	}
}
