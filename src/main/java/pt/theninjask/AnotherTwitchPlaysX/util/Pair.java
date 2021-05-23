package pt.theninjask.AnotherTwitchPlaysX.util;

public class Pair<X,Y> {
	
	private X left;
	
	private Y right;
	
	public Pair() {}
	
	public Pair(X left, Y right) {
		this.left = left;
		this.right = right;
	}

	public void setLeft(X left) {
		this.left = left;
	}
	
	public X getLeft() {
		return left;
	}

	public void setRight(Y right) {
		this.right = right;
	}
	
	public Y getRight() {
		return right;
	}
	
	public boolean equals(Pair<X,Y> other) {
		if(other==null)
			return false;
		if(left==null) {
			if(other.left!=null)
				return false;
		}else {
			if(!left.equals(other.left))
				return false;
		}
		if(right==null) {
			if(other.right!=null)
				return false;
		}else {
			if(!right.equals(other.right))
				return false;
		}
		return true;
	}
	
	public Pair<X, Y> clone(){
		return new Pair<X, Y>(left, right);
	}
}
