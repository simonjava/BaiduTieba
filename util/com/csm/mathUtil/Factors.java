package com.csm.mathUtil;

import java.util.ArrayList;
import java.util.Collection;

public class Factors extends ArrayList<Factor>
{
	public Factors()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public Factors(Collection<? extends Factor> c)
	{
		super(c);
		// TODO Auto-generated constructor stub
	}

	public Factors(int initialCapacity)
	{
		super(initialCapacity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<this.size();i++)
		{
			Factor f = this.get(i);
			sb.append(f);
			if(i!=this.size()-1)
				sb.append("*");
		}
		sb.append("\n");
		return sb.toString();
		
	}
	
	public void addFactor(Factor f)
	{
		for(int i=0;i<this.size();i++)
		{
			Factor now = this.get(i);
			if(now.value == f.value)
			{
				now.power+=f.power;
				return;
			}
		}
		this.add(f);
	}
	
	public boolean isSquare()
	{
		for(int i=0;i<this.size();i++)
		{
			Factor now = this.get(i);
			if(now.power%2!=0)
			{
				return false;
			}
		}
		return true;
	}
}
