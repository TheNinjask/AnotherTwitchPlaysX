package pt.theninjask.AnotherTwitchPlaysX.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class RedirectorOutputStream extends PrintStream {

	private static final OutputStream VOID = new OutputStream() {
		@Override
		public void write(int b) throws IOException {
		}
	};
	private static RedirectorOutputStream singleton = new RedirectorOutputStream(VOID);

	private RedirectorOutputStream(OutputStream out) {
		super(out);
	}

	public static RedirectorOutputStream getInstance() {
		return singleton;
	}

	public static void changeRedirect(OutputStream newOut) {
		singleton.out = newOut;
	}

	public static void changeRedirectToVoid() {
		singleton.out = VOID;
	}

}
