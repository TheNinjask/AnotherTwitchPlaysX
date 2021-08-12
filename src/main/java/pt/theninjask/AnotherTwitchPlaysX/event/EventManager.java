package pt.theninjask.AnotherTwitchPlaysX.event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import net.engio.mbassy.bus.MBassador;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class EventManager {
	
	private static final SimpleFormatter LOGGER_FORMATTER = new SimpleFormatter() {
		private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		@Override
		public synchronized String format(LogRecord lr) {
			return String.format("[%s] %s\n", sdf.format(new Date(lr.getMillis())), lr.getMessage());
		}
	};
	
	private static Logger logger = setUpLogger();
	
	private static EventManager singleton = new EventManager();

	private MBassador<Object> dispatcher = Constants.setupDispatcher();
	
	private EventManager() {
	}
	
	private static final Logger setUpLogger() {
		Logger logger = Logger.getLogger(EventManager.class.getName());
		logger.setUseParentHandlers(false);
		StreamHandler handler = new StreamHandler(System.out, LOGGER_FORMATTER) {
			@Override
			public synchronized void publish(LogRecord record) {
				super.publish(record);
				super.flush();
			}
		};
		logger.addHandler(handler);
		logger.setLevel(Level.OFF);
		return logger;
	}
	
	public static void enableLogging(boolean val) {
		if (val) {
			logger.setLevel(Level.ALL);
		} else {
			logger.setLevel(Level.OFF);
		}
	}
	
	public static boolean isEnableLogging() {
		if(logger.getLevel().equals(Level.ALL))
			return true;
		else
			return false;
	}
	
	public static void registerEventListener(Object listener) {
		singleton.dispatcher.subscribe(listener);
	}

	public static void unregisterEventListener(Object listener) {
		singleton.dispatcher.unsubscribe(listener);
	}
	
	public static void triggerEvent(Event event) {
		logger.info(String.format("Event %s triggered", event.getName()));
		singleton.dispatcher.post(event).now();
	}
}
