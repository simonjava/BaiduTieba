package com.csm.tuple;

public class ThreeTuple<A,B,C> extends TwoTuple<A, B>{

	C third;
	public C getThird() {
		return third;
	}
	public void setThird(C third) {
		this.third = third;
	}
	public ThreeTuple(A a, B b,C c) {
		super(a, b);
		this.third = c;
	}

}
