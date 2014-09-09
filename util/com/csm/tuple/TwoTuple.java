package com.csm.tuple;

import java.util.List;


public class TwoTuple<A, B> implements Describable
{
	A first;
	B second;

	public TwoTuple(A first, B second)
	{
		this.first = first;
		this.second = second;
	}

	public A getFirst()
	{
		return first;
	}

	public void setFirst(A first)
	{
		this.first = first;
	}

	public B getSecond()
	{
		return second;
	}

	public void setSecond(B second)
	{
		this.second = second;
	}
	
	@Override
	public void describe()
	{
		System.out.print("first:");
		printObj(first);
		System.out.println();
		System.out.print("second:");
		printObj(second);
		System.out.println();
	}
	
	protected void printObj(Object obj)
	{
		if(obj == null)
		{
			System.out.println("null");
			return;
		}
		if(obj.getClass().isArray())
		{
			for(Object o : (Object[])obj)
				printObj(o);
		}
		else if(List.class.isInstance(obj))
		{
			for(Object o : (List<?>)obj)
				printObj(o);
		}
		else if(Describable.class.isInstance(obj))
		{
			((Describable)obj).describe();
		}
		else
		{
			System.out.print(obj);
		}
	}

}
