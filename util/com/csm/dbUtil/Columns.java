package com.csm.dbUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * @author simin 存储字段的名称和值
 */
public class Columns extends LinkedList<Column>
{
	private static final long serialVersionUID = 1L;

	public Columns()
	{
	}

	/**
	 * @param keysColumn
	 *            多个字段
	 * @param values
	 *            多个字段对应的值
	 * @throws Exception 
	 */
	public Columns(String[] keysColumn, Object[] values)
	{
		if (keysColumn != null && values != null)
		{
			if (keysColumn.length != values.length)
			{
				System.err.println("keysColumn与values的长度不匹配");
			} else
			{
				for (int i = 0; i < keysColumn.length; i++)
				{
					add(new Column(keysColumn[i], values[i],values[i].getClass()));
				}
			}
		}
	}

	/**
	 * 去除指定的不起作用无效字段
	 * 
	 * @param invalidColumns
	 *            无效的字段
	 */
	public void setInvalidColumns(String... invalidColumns)
	{
		if (invalidColumns == null)
			return;
		List<String> invalidColumnList = new LinkedList<String>(
				Arrays.asList(invalidColumns));
		for (String invalidColumn : invalidColumnList)
		{
			for (int i = 0; i < size(); i++)
			{
				Column column = this.get(i);
				if (column.getName().equals(invalidColumn))
				{
					remove(i);
					invalidColumnList.remove(invalidColumnList);
				}
			}
		}
		if (!invalidColumnList.isEmpty())
			System.err.println("指定的无效字段有的不存在");
	}

	/**
	 * @param keysColumn
	 *            where字句的字段 为了能正确循环，需要将where的字段的查询关键字排在后面
	 */
	public void orderByKeys(String[] keysColumn)
	{
		if (keysColumn == null)
			return;
		Columns temp = new Columns();
		List<String> keysTemp = new LinkedList<String>(
				Arrays.asList(keysColumn));
		label: for (Column c : this)
		{
			for (int i = 0; i < keysTemp.size(); i++)
			{
				String key = keysTemp.get(i);
				if (c.getName().equals(key))
				{
					temp.addLast(c);
					keysTemp.remove(i);
					continue label;
				}
			}
			temp.addFirst(c);
		}
		if (!keysTemp.isEmpty())
			System.err.println("指定的keyColumnm字段有的不存在");
		this.clear();
		this.addAll(temp);
	}

	public List<String> getColumnsName()
	{
		List<String> columnsName = new ArrayList<String>();
		for (Column column : this)
		{
			columnsName.add(column.getName());
		}
		return columnsName;
	}

	public List<Object> getColumnsValue()
	{
		List<Object> columnsvalue = new ArrayList<Object>();
		for (Column column : this)
		{
			columnsvalue.add(column.getValue());
		}
		return columnsvalue;
	}
	
	public List<Class<?>> getColumnsType()
	{
		List<Class<?>> columnsvalue = new ArrayList<Class<?>>();
		for (Column column : this)
		{
			columnsvalue.add(column.getType());
		}
		return columnsvalue;
	}
}

