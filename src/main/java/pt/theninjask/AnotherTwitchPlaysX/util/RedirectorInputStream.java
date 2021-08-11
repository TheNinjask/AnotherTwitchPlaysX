package pt.theninjask.AnotherTwitchPlaysX.util;

import java.io.IOException;
import java.io.InputStream;

public class RedirectorInputStream extends InputStream {

	
	private static final InputStream VOID = new InputStream() {
		@Override
		public int read() throws IOException {
			return -1;
		}
	};

	private static InputStream defaultStream = VOID;
	
	private static RedirectorInputStream singleton = new RedirectorInputStream(VOID);
	
	private InputStream stream;

	private RedirectorInputStream(InputStream input) {
		stream = input;
	}
	
	public static RedirectorInputStream getInstance() {
		return singleton;
	}

	@Override
	public int read() throws IOException {
		return stream.read();
	}
	
	public static void changeDefault(InputStream newDefaultStream) {
		defaultStream = newDefaultStream;
	}
	
	public static void changeRedirectToDefault() {
		singleton.stream = defaultStream;
	}

	public static void changeRedirect(InputStream newOut) {
		singleton.stream = newOut;
	}

	public static void changeRedirectToVoid() {
		singleton.stream = VOID;
	}

}
