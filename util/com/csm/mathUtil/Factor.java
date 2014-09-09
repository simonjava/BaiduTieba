package com.csm.mathUtil;

import java.io.Serializable;

//为了进行深拷贝
public class Factor implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int value;
	int power;
	@Override
	public String toString()
	{
		
		return "("+value+"^"+power+")";
	}
	public Factor(int value, int power)
	{
		super();
		this.value = value;
		this.power = power;
	}
	public int getValue()
	{
		return value;
	}
	public void setValue(int value)
	{
		this.value = value;
	}
	public int getPower()
	{
		return power;
	}
	public void setPower(int power)
	{
		this.power = power;
	}
	public long getValueByPower()
	{
		long r = 1;
		for(int i=0;i<power;i++)
		{
			r*=value;
		}
		return r;
	}
}
