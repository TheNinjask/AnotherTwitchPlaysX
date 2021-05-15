package pt.theninjask.AnotherTwitchPlaysX.util;

public class Pair<X,Y> {
	
	private X key;
	
	private Y value;
	
	public Pair() {}
	
	public Pair(X key, Y value) {
		this.key = key;
		this.value = value;
	}

	public void setKey(X key) {
		this.key = key;
	}
	
	public X getKey() {
		return key;
	}

	public void setValue(Y value) {
		this.value = value;
	}
	
	public Y getValue() {
		return value;
	}
	
	public boolean equals(Pair<X,Y> other) {
		if(other==null)
			return false;
		if(key==null) {
			if(other.key!=null)
				return false;
		}else {
			if(!key.equals(other.key))
				return false;
		}
		if(value==null) {
			if(other.value!=null)
				return false;
		}else {
			if(!value.equals(other.value))
				return false;
		}
		return true;
	}
}
