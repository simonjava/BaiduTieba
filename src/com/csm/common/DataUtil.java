package com.csm.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {
	public static boolean isNotNull(String str)
	{
		if(str!=null&&!str.equals(""))
		{
			return true;
		}
		return false;
	}
	
	public static String getValueByPattern(String content,String pattern)
	{
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(content);
		if(m.matches())
			return m.group(1);
		return null;
	}
}
