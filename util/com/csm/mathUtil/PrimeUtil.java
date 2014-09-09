package com.csm.mathUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PrimeUtil
{
	private static List<Integer> primers;
	// 当前的primers自动保存在nowRange之前的所有素数，当nowRange改变时primers自动改变
	private static int nowRange = 0;

	public static int getNowRange()
	{
		return nowRange;
	}

	public static void setNowRange(int nowRange)
	{
		if (primers == null)
		{
			primers = getAllPrimeBefore(nowRange);
			PrimeUtil.nowRange = nowRange;
			return;
		}
		if (nowRange > PrimeUtil.nowRange)
		{
			addPrimeOnNowRange(nowRange - PrimeUtil.nowRange);
			PrimeUtil.nowRange = nowRange;
		}
	}

	public static Factors getPrimeOfNumber(long num)
	{
		Factors result = new Factors();
		if (num < 0)
		{
			result.addFactor(new Factor(-1, 1));
			num *= (-1);
		}

		double sqrt = Math.sqrt(num);
		int range = MathUtil.floor(sqrt) + 1;
		setNowRange(range);

		for (int i = 0; i < primers.size(); i++)
		{
			sqrt = Math.sqrt(num);
			int p = primers.get(i);
			if (p > sqrt)
				break;
			if (num % p == 0)
			{
				result.addFactor(new Factor(p, 1));
				num = num / p;
				i--;
			}
		}
		result.addFactor(new Factor((int)num, 1));
		return result;
	}

	public static List<Integer> getAllPrimeBefore(int num)
	{
		List<Integer> primers = new ArrayList<Integer>();
		primers.add(2);
		for (int i = 3; i <= num; i++)
		{
			boolean flag = true;
			for (int temp : primers)
			{
				if (temp <= Math.sqrt(i))
				{
					if (i % temp == 0)
					{
						flag = false;
						break;
					}
				} else
				{
					break;
				}
			}
			if (flag)
			{
				primers.add(i);
			}
		}
		return primers;
	}

	public static List<Integer> addPrimeOnNowRange(int addNum)
	{
		for (int i = nowRange + 1; i <= nowRange + addNum; i++)
		{
			boolean flag = true;
			for (int temp : primers)
			{
				if (temp <= Math.sqrt(i))
				{
					if (i % temp == 0)
					{
						flag = false;
						break;
					}
				} else
				{
					break;
				}
			}
			if (flag)
			{
				primers.add(i);
			}
		}
		return primers;
	}

	public static boolean isSqure(Factors... fs)
	{
		Factors tfs1 = (Factors) deepCopy(fs[0]);
		for (int i = 1; i < fs.length; i++)
		{
			Factors tfs2 = (Factors) deepCopy(fs[i]);
			for (Factor f : tfs2)
			{
				tfs1.addFactor(f);
			}
		}
		return tfs1.isSquare();
	}

	public static List deepCopy(List src)
	{
		try
		{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(src);

			ByteArrayInputStream byteIn = new ByteArrayInputStream(
					byteOut.toByteArray());
			ObjectInputStream in = new ObjectInputStream(byteIn);
			List dest = (List) in.readObject();
			return dest;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args)
	{

		System.out.println(MathUtil.gcd(0, 100));
	}
}
