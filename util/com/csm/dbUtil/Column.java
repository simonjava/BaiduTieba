package com.csm.dbUtil;

import com.csm.tuple.ThreeTuple;

// 体会到了多态的好
public class Column extends ThreeTuple<String, Object, Class<?>>
{

	public Column(String name, Object value, Class<?> type)
	{
		super(name, value, type);
	}

	public String getName()
	{
		return getFirst();
	}

	public Object getValue()
	{
		return getSecond();
	}

	public Class<?> getType()
	{
		return getThird();
	}

	public void setName(String name)
	{
		setFirst(name);
	}

	public void setValue(Object value)
	{
		setSecond(value);
	}

	public void setType(Class<?> type)
	{
		setThird(type);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{ name:").append(getName()).append(",value:")
				.append(getValue()).append(",type:").append(getType())
				.append(" }");
		return sb.toString();
	}
}