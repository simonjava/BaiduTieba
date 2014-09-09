package com.csm.mathUtil;

public class MathUtil
{
	public static long gcd(long a, long b)
	{
		a = Math.abs(a);
		b = Math.abs(b);
		long c = a % b;
		while (c != 0)
		{
			a = b;
			b = c;
			c = a % b;
		}
		return b;
	}

	public static long lcm(long a, long b)
	{
		a = Math.abs(a);
		b = Math.abs(b);

		return (a * b) / gcd(a, b);
	}

	public static int floor(double a)
	{
		return (int) Math.floor(a);
	}

	public static boolean isSquare(long a)
	{
		double sqrt = Math.sqrt(a);
		if (floor(sqrt) == sqrt)
			return true;
		return false;
	}

	// 求a*b = c mod m 中的b (a的逆元)
	public static long extendedEuclidean(long a, long m, long c)
	{
		long N = m;
		long t1 = 0;
		long t2 = 1;
		while (a != 1 && a != 0)
		{
			long f1 = m % a;
			long f2 = m / a;
			long temp = t1;
			m = a;
			t1 = t2;
			a = f1;
			t2 = temp - f2 * t2;
			t2 %= N;
			if (t2 < 0)
				t2 += N;
		}
		if (a == 0)
			return 0;
		return (t2 * c) % N;
	}

	// x=a^power mod n;
	public static long getMod(long a, long power, long n)
	{
		long r = 1;
		for (long i = 0; i < power; i++)
		{
			r *= a;
			r %= n;
		}
		return r;
	}
	public static long power(long a, long power)
	{
		long r = 1;
		for (long i = 0; i < power; i++)
		{
			r *= a;
		}
		return r;
	}
	public static boolean divisibility(long up,long down)
	{
		if(gcd(up,down)!=down)
		{
			return false;
		}
		return true;
	}
	public static void main(String[] agrs)
	{
//		System.out.println(getMod(37,121,123));
		long sum =1;
		for(int i=0;i<10000000;i++)
		{
			sum*=37;
			if(sum%123==15)
				System.out.println(i);
			sum%=123;
		}
		System.out.println("over");
	}
}
