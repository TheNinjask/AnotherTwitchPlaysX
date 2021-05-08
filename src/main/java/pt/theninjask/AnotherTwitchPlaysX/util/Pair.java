package pt.theninjask.AnotherTwitchPlaysX.util;

public class Pair<X,Y> {
	
	private X key;
	
	private Y value;
	
	public Pair(X key, Y value) {
		this.key = key;
		this.value = value;
	}

	public X getKey() {
		return key;
	}

	public Y getValue() {
		return value;
	}
	
}
