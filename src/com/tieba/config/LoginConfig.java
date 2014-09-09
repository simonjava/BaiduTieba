package com.tieba.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tieba.login.User;

public class LoginConfig
{
	public static final String BAIDU = "http://www.baidu.com";
	public static final String TOKEN_PAGE = "https://passport.baidu.com/v2/api/?getapi&class=login&tpl=mn&tangram=false";
	public static final String ISNEED_VCODE_URL = "https://passport.baidu.com/v2/api/?logincheck&tpl=mn&apiver=v3&&isphone=false";
	public static final String LOGIN_POST_URL = "https://passport.baidu.com/v2/api/?login";
	private static final String UserList = "user.list";
	public static List<User> userList = new ArrayList();
	static
	{
		try
		{
			Scanner s = new Scanner(new FileInputStream(ParamsConfig.WORKSPACE
					+ UserList));
			while (s.hasNext())
			{
				userList.add(new User(s.next(), s.next()));
			}
			System.out.println("载入帐号完成");
			System.out.println(userList);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}
