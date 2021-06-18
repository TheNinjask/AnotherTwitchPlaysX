package pt.theninjask.AnotherTwitchPlaysX.gui.mod;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface ATPXModProps {

	public boolean main() default true;
	
	public boolean hasPanel() default true;
	
	@Deprecated
	public int version() default 0;
	
	public boolean keepLoaded() default true;
	
	public boolean popout() default false;
}
