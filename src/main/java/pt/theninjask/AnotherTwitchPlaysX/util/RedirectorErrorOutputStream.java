package pt.theninjask.AnotherTwitchPlaysX.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class RedirectorErrorOutputStream extends PrintStream {

	private static final OutputStream VOID = new OutputStream() {
		@Override
		public void write(int b) throws IOException {
		}
	};
	private static RedirectorErrorOutputStream singleton = new RedirectorErrorOutputStream(VOID);

	private RedirectorErrorOutputStream(OutputStream out) {
		super(out);
	}

	public static RedirectorErrorOutputStream getInstance() {
		return singleton;
	}

	public static void changeRedirect(OutputStream newOut) {
		singleton.out = newOut;
	}

	public static void changeRedirectToVoid() {
		singleton.out = VOID;
	}

}
