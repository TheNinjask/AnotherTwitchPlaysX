package pt.theninjask.AnotherTwitchPlaysX;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import pt.theninjask.AnotherTwitchPlaysX.gui.MainFrame;
import pt.theninjask.AnotherTwitchPlaysX.util.Constants;

public class App {
	
	public static void main(String[] args) {
		// Get the logger for "com.github.kwhat.jnativehook" and set the level to off.
		LogManager.getLogManager().reset();
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);

		// Don't forget to disable the parent handlers.
		logger.setUseParentHandlers(false);
		
		//This below feels dishonest but I really want the console to be clear
		//so to prevent printing this is the cheat //TODO add credit other place
		//and can work as a compatibility test now that I think about it
		try {
			PrintStream tmp = System.out;
			System.setOut(null);
			GlobalScreen.registerNativeHook();
			GlobalScreen.unregisterNativeHook();
			System.setOut(tmp);
			MainFrame.getInstance();			
		} catch (NativeHookException e) {
			e.printStackTrace();
			Constants.showExpectedExceptionDialog(e);
		}		
	}
	
}
