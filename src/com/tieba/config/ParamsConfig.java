package com.tieba.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class ParamsConfig
{
	private static final int abandon_vcode;
	public static final boolean ABANDON_VCODE;
	public static final String WORKSPACE;
	public static final List<String> SITES;
	static
	{
		Properties p = new Properties();
		try
		{
			p.load(new FileInputStream("config/params.properties"));
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		abandon_vcode = Integer.parseInt(p.getProperty("abandon_vcode"));
		System.out.println("abandon_vcode : " + abandon_vcode);
		ABANDON_VCODE = isAbandon();
		WORKSPACE = p.getProperty("workspace");
		String temp = p.getProperty("sites");
		SITES = new ArrayList<String>();
		for (String t : temp.split("&"))
		{
			SITES.add(WORKSPACE + t+"/");
		}
		System.out.println("配置信息载入结束！！");
		System.out.println(SITES);
	}

	private static boolean isAbandon()
	{
		if (abandon_vcode != 0)
		{
			return true;
		}
		return false;
	}

}
