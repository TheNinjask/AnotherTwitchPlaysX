package pt.theninjask.AnotherTwitchPlaysX.util;

public class JComboItem<T> {

	private T object;
	
	private String rep;
	
	public JComboItem(T object, String rep) {
		this.object = object;
		this.rep = rep;
	}
	
	public T get() {
		return object;
	}
	
	public String toString() {
		return rep;
	}
	
}
