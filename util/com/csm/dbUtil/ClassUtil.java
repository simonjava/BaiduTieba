package com.csm.dbUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassUtil
{
	private static final String NOTMATCH = "NOT_METHOD_STRING";
	private static final String[] DefaultBanField = new String[]
	{ "getClass" };

	/**
	 * 检查字段和字段值是否正确对应此Class
	 * 
	 * @param objClass
	 *            被检查的class
	 * @return 字段检查器
	 */
	public static FieldChecker getCLassFieldChecker(Class<?> objClass,
			Columns columns)
	{
		return new FieldChecker(objClass, columns);
	}

	public static String fillFieldInformationOfObj(Object obj, Columns columns)
	{
		return fillFieldInformationOfObj(obj, columns, new String[]
		{});
	}

	/**
	 * 使用Columns填充一个对象
	 * 
	 * @param obj
	 *            被填充对象的实例,这里通过set方法填充
	 * @param columns
	 *            封装了该对象的字段值
	 * @param banNames
	 *            不填充的字段名字 禁止通过set的字段名,如setName，这里要传入字段名name
	 * @return 填充结果
	 */
	public static String fillFieldInformationOfObj(Object obj, Columns columns,
			String... banNames)
	{
		Class<?> objClass = obj.getClass();
		columns.setInvalidColumns(banNames);
		try
		{
			for (Column column : columns)
			{
				System.out.println("column:"+column);
				Method m = objClass.getMethod(
						fieldToSetMethod(column.getName()), column.getType());
				m.invoke(obj, new Object[]
				{ column.getValue() });
			}
			return "填充"+obj.getClass().getSimpleName()+"成功";
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		} catch (SecurityException e)
		{
			e.printStackTrace();
		} catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		return "填充"+obj.getClass().getSimpleName()+"失败";
	}

	public static Columns getFieldInformationOfObj(Object obj)
	{
		return getFieldInformationOfObj(obj, (String[]) null);
	}

	/**
	 * @param obj
	 *            获取的对象
	 * @param ban
	 *            禁止通过get的字段名,如getName，这里要传入字段名name 默认去除getClass
	 * @return <表名,字段名&字段值&字段类型> 字段值可能为空，字段名和字段类型可安全获得。
	 */
	public static Columns getFieldInformationOfObj(Object obj,
			String... banNames)
	{
		Class<?> objClass = obj.getClass();
		Columns columns = new Columns();
		List<String> banList = new LinkedList<String>();
		// 将传入的字段变成getXxxx 方便方法名比较
		if (banNames != null)
		{
			for (String ban : banNames)
			{
				banList.add(fieldToGetMethod(ban));
			}
		}
		for (String defaultBanField : DefaultBanField)
			banList.add(defaultBanField);
		label: for (Method m : objClass.getMethods())
		{
			String mName = m.getName();
			for (String ban : banList)
			{
				if (ban.equals(mName))
				{
					banList.remove(ban);
					continue label;
				}
			}
			String column = getMethodToField(mName);
			if (column != NOTMATCH)
			{
				try
				{
					Object o = m.invoke(obj, new Object[]
					{});
					columns.add(new Column(column, o, m.getReturnType()));
				} catch (IllegalArgumentException e)
				{

					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				} catch (InvocationTargetException e)
				{
					e.printStackTrace();
				}
			}
		}
		if (!banList.isEmpty())
		{
			System.err.println("有的禁止字段不存在");
		}
		return columns;
	}

	static String fieldToGetMethod(String field)
	{
		return "get" + uppercaseFirstCharacter(field);
	}

	static String getMethodToField(String getMethod)
	{
		Pattern pattern = Pattern.compile("get(\\w*)");
		Matcher matcher = pattern.matcher(getMethod);
		if (matcher.matches())
		{
			String field = lowercaseFirstCharacter(matcher.group(1));
			return field;
		}
		return NOTMATCH;
	}

	static String fieldToSetMethod(String field)
	{
		return "set" + uppercaseFirstCharacter(field);
	}

	static String setMethodToField(String getMethod)
	{
		Pattern pattern = Pattern.compile("set(\\w*)");
		Matcher matcher = pattern.matcher(getMethod);
		if (matcher.matches())
		{
			String field = lowercaseFirstCharacter(matcher.group(1));
			return field;
		}
		return NOTMATCH;
	}

	private static String lowercaseFirstCharacter(String str)
	{
		return str.substring(0, 1).toLowerCase()
				+ str.substring(1, str.length());
	}

	private static String uppercaseFirstCharacter(String str)
	{
		return str.substring(0, 1).toUpperCase()
				+ str.substring(1, str.length());
	}
}

class FieldChecker
{
	Class<?> objClass;
	Columns columns;

	public FieldChecker(Class<?> objClass, Columns columns)
	{
		this.objClass = objClass;
		this.columns = columns;
	}

	/**
	 * 检查columns中字段名和字段类型是否和class的匹配
	 * 
	 * @return
	 */
	public boolean check()
	{
		List<String> fieldList = new LinkedList<String>(
				columns.getColumnsName());
		List<Object> typeList = new LinkedList<Object>(columns.getColumnsType());
		label: for (Method m : objClass.getMethods())
		{
			String field = ClassUtil.getMethodToField(m.getName());
			for (int i = 0; i < fieldList.size(); i++)
			{
				if (field.equals(fieldList.get(i))
						&& m.getReturnType().equals(typeList.get(i)))
				{
					fieldList.remove(i);
					typeList.remove(i);
					continue label;
				}
			}
		}
		if (fieldList.isEmpty() && typeList.isEmpty())
		{
			return true;
		} else
		{
			System.err.println("检查到字段或者类型不匹配到" + objClass.getName());
			return false;
		}
	}
};
