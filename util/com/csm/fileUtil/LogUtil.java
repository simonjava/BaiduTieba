package com.csm.fileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import com.csm.common.TimeUtil;

/**
 * @author csmubuntu
 * 记载日志，有多个日志文件，内部标号
 */
public class LogUtil
{
	public static  String dir;
	
	public static  String date;
	public static  String fileName1;
	public static  String fileName2;
	public static  String fileName3;
	public static  PrintStream ps1;
	public static  PrintStream ps2;
	public static  PrintStream ps3;
	
	public static String exception;
	public static  PrintStream errPs;
	
	public static String systemErr;
	public static  PrintStream systemErrPs;
	static
	{
		init();
	}
	
	
	public static PrintStream errLogPs()
	{
		errPs.println("当前时间:"+TimeUtil.getCurrentTime());
		return errPs;
	}
	public static void log1(Object o)
	{
		System.out.println(o);
		ps1.println(o);
	}
	
	public static void log2(Object o)
	{
		System.out.println(o);
		ps2.println(o);
	}
	
	public static void log3(Object o)
	{
		System.out.println(o);
		ps3.println(o);
	}
	//直接重定向系统的标准输出当作重定向
	public static void clear()
	{
		new File(dir,date + fileName1).delete();
		new File(dir,date + fileName2).delete();
		new File(dir,date + fileName3).delete();
		new File(dir,date + exception).delete();
		new File(dir,date + systemErr).delete();
	}
	
	public static void init()
	{
		date = TimeUtil.getTodayDate();
		Properties p = new Properties();
		try
		{
			p.load(new FileInputStream(DirUtil.getProjectDir()+"config/log.properties"));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		fileName1 = p.getProperty("fileName1");
		fileName2 = p.getProperty("fileName2");
		fileName3 = p.getProperty("fileName3");
		exception = p.getProperty("exception");
		systemErr = p.getProperty("systemErr");
		
		dir = p.getProperty("dir");
		Directory.creatDirector(dir);
		
		try
		{
			ps1 = new PrintStream(new FileOutputStream(new File(dir,date + fileName1), true), true, "utf-8");
			ps2 = new PrintStream(new FileOutputStream(new File(dir,date + fileName2), true), true, "utf-8");
			ps3 = new PrintStream(new FileOutputStream(new File(dir,date + fileName3), true), true, "utf-8");
			errPs = new PrintStream(new FileOutputStream(new File(dir,date + exception), true), true, "utf-8");
			systemErrPs = new PrintStream(new FileOutputStream(new File(dir,date + systemErr), true), true, "utf-8");
			
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void restart(boolean isDeugMode)
	{
		init();
		if(!isDeugMode) //如果不是开发模式，定义系统错误输出到文件
			System.setErr(systemErrPs);
	}
	public static void main(String[] args)
	{
		
	}

}
