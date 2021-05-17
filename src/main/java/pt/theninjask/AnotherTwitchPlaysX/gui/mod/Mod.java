package pt.theninjask.AnotherTwitchPlaysX.gui.mod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Mod {

	public boolean main() default true;
	
	public boolean hasPanel() default true;
	
	public int version() default 0;
}
