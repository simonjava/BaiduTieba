package chapter14_2.Class对象;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

class A
{
	int a; 
	public char a2;
	protected int a3;
	
	void f()
	{
		System.out.println("run f()");
	};

	public void f2()
	{
		System.out.println("run f2()");
	};
}

class B extends A
{
	int b;

	void g()
	{
	};

	public void g2()
	{
		System.out.println("run g2()");
	};

	public void g3(Integer b, int a)
	{
		System.out.println("a = " + a);
	};

	public void g4(int... a)
	{
		for (int a1 : a)
			System.out.println("a = " + a1);
	};
}

public class ClassDemo
{
	/**
	 * @param o
	 * 将对象o的所有方法运行输出一遍，如果这个方法需要参数，参数都为null，所以报错是正常的
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 */
	public static void runAllMethodWithObject(Object o)
	{
		Class<?> oc = o.getClass();
		for (Method m : oc.getMethods())
		{
			try
			{
				System.out.println(m);
				// System.out.println("是否为含有可变参数"+m.isVarArgs()); //是否有可变参数
				Class<?>[] pcs = m.getParameterTypes();
				Object[] args = new Object[pcs.length];
				int i = 0;
				System.out.print("该方法需要的参数有(");
				for (Class<?> c : pcs)
				{
					System.out.print(c.getName() + ",");
					args[i++] = judge(c);
				}
				System.out.println(")");
				Object obj = m.invoke(o, args);
				System.out.print("执行结果");
				// 如果得到的结果是个数组 输出数组具体内容
				if(obj.getClass().isArray())
					System.out.println(Arrays.toString((Object[])obj));
				else
					System.out.println(obj);
			} catch (Exception e)
			{
//				e.printStackTrace();
				System.out.println("该方法出错了");
			}finally
			{
				System.out.println();
			}
		}
	}

	private static Object judge(Class<?> c)
	{
		Object result = null;
		if (c == int.class || c == double.class || c == char.class
				|| c == float.class)
		{
			result = 0;
		}
		return result;
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException
	{
		A a = new B();
//		runAllMethodWithObject(a);
		runAllMethodWithObject(a.getClass());
		System.out.println(A.class.isAssignableFrom(B.class));
		System.out.println(B.class.isMemberClass());
	}

}
