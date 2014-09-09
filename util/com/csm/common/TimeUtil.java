package com.csm.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TimeUtil
{

	/**
	 * @return 得到当前时间
	 */
	public static String getCurrentTime()
	{
		Date d = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(d);
	}

	public static String getTodayDate()
	{
		Date d = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(d);
	}
	
	/**
	 * @param time1
	 * @param time2
	 * @return 如果time1较早 返回真。
	 */
	public static boolean compareTime(String time1,String time2)
	{
		int a[] = getTimeIntValue(time1);
		int b[] = getTimeIntValue(time2);
		for(int i=0;i<6;i++)
		{
			if(a[i]>b[i])
				return false;
			else if(a[i]<b[i])
				return true;
		}
		return true;
	}
	
	private static int[] getTimeIntValue(String time)
	{
		int a[] = new int[6];
		String t[] =  time.split(" ");
		String t1[] = t[0].split("-");
		for(int i=0;i<3;i++)
		{
			a[i] = Integer.parseInt(t1[i]);
		}
		String t2[] = t[1].split(":");
		for(int i=0;i<3;i++)
		{
			a[i+3] = Integer.parseInt(t2[i]);
		}
		return a;
	}
	
	public static long getTimeInterval(String time1,String time2)
	{
		long v1 =  getSecondOfTime(time1);
		long v2 = getSecondOfTime(time2);
		System.out.println(v1);
		return v1 - v2;
	}
	
	/**
	 * @param time
	 * @return 不将日期计算在内
	 */
	private static long getSecondOfTime(String time)
	{
		Pattern pattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})");
		Matcher m =pattern.matcher(time);
		if(!m.matches())
		{
			System.err.println("不是时间格式");
			return -1;
		}
		int year = Integer.parseInt(m.group(1));
		int month = Integer.parseInt(m.group(2));
		int day = Integer.parseInt(m.group(3));
		
		int hour = Integer.parseInt(m.group(4));
		int munite = Integer.parseInt(m.group(5));
		int second = Integer.parseInt(m.group(6));
		return (hour*3600+munite*60+second);

//		return (year*365+month*30+day)*(24*3600)+(hour*3600+munite*60+second);
	}
	public static void main(String[] args)
	{
		System.out.println(getTodayDate());
		System.out.println(getTimeInterval("2012-03-02 22:33:22","2012-03-02 22:33:20"));
	}

}
