package pt.theninjask.AnotherTwitchPlaysX;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;

import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class App {

	public static void main(String[] args) {
		try {
			globalSetUp();
		} catch (Exception e) {
			Constants.showExpectedExceptionDialog(e);
		}
		MainFrame.getInstance();
	}

	private static void globalSetUp() throws Exception {
		// Get the logger for "com.github.kwhat.jnativehook" and set the level to off.
		LogManager.getLogManager().reset();
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);

		// Don't forget to disable the parent handlers.
		logger.setUseParentHandlers(false);

		// This below feels dishonest but I really want the console to be clear
		// so to prevent printing this is the cheat
		// TODO add credit other place where it will have more visibility
		// also this can work as a compatibility test now that I think about it
		PrintStream tmp = System.out;
		System.setOut(null);
		GlobalScreen.registerNativeHook();
		GlobalScreen.unregisterNativeHook();
		System.setOut(tmp);
		//
	}
}
