package io.github.mariomatheus.doub.util;

public class Pair<F, S> {
	
	private F first;
	private S second;
	
	private Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}
	
	public static <F, S> Pair<F, S> of(F first, S second) {
		return new Pair<F, S>(first, second);
	}

	public F first() {
		return first;
	}

	public void setFirst(F first) {
		this.first = first;
	}

	public S second() {
		return second;
	}

	public void setSecond(S second) {
		this.second = second;
	}

}
