package com.csm.tuple;

public class TwoTuple<A,B> {
	A first;
	B second;
	public TwoTuple(A a,B b)
	{
		this.first=a;
		this.second=b;
	}
	public A getFirst() {
		return first;
	}
	public void setFirst(A first) {
		this.first = first;
	}
	public B getSecond() {
		return second;
	}
	public void setSecond(B second) {
		this.second = second;
	}
	
}
