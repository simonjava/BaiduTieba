package com.tieba.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;

import com.csm.common.DataUtil;
import com.csm.httpUtil.Result;
import com.csm.httpUtil.SendRequest;
import com.tieba.config.LoginConfig;
import com.tieba.config.ParamsConfig;
import com.tieba.vcode.VcodeManager;

public class Login
{
	String username;
	String password;
	String cookie;
	String token;
	String codeString;
	String error;
	boolean isNeedVcode = false;
	boolean isLogin = false;
	String nickname;
	public String getCookie()
	{
		return cookie;
	}
	
	public String getUsername()
	{
		return username;
	}

	public String getError()
	{
		return error;
	}

	public boolean isLogin()
	{
		return isLogin;
	}

	public String getNickname()
	{
		return nickname;
	}

	
	public Login(User user)
	{
		this.username = user.getName();
		this.password = user.getPassword();
	}
	
	public Login(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	public void execute()
	{
		System.out.println("正在登录"+username+".....");
		Result re = null;
		try
		{
			re = step1();
			if (re == null)
			{
				System.out.println("获取Baidu Cookie失败");
				return;
			}
			System.out.println(re);
			this.cookie = re.getCookie();

			re = step2();
			System.out.println(re);

			token = DataUtil
					.getValueByPattern(re.getEntityContent(),
							"[\\S|\\s]*bdPass.api.params.login_token='([\\w]*)';[\\S|\\s]*");
			System.out.println("token = " + token);

			re = isNeedVcode();
			System.out.println(re);
			codeString = DataUtil.getValueByPattern(re.getEntityContent(),
					"[\\S|\\s]*\"codeString\" : \"([\\w]*)\"[\\S|\\s]*");
			if (!DataUtil.isNotNull(codeString))
			{
				System.out.println("这个帐号不需要验证码！");
			} else
			{
				System.out.println("codeString : " + codeString);
				isNeedVcode = true;
				VcodeManager.disposeLoginVcode(codeString);
			}
			if(isNeedVcode && ParamsConfig.ABANDON_VCODE)
			{
				System.out.println("需要填写验证码！，放弃！");
				return;
			}
			re = postLoginForm();
			System.out.println(re);
			error = DataUtil.getValueByPattern(re.getEntityContent(),
					"[\\S|\\s]*error=([\\d]+)'[\\S|\\s]*");
			System.out.println("error ： " + error);
			if (error.equals("0"))
			{
				System.out.println("登录成功");
				isLogin = true;
				nickname = DataUtil.getValueByPattern(re.getEntityContent(),
						"[\\S|\\s]*username=([\\w]+)&[\\S|\\s]*");
				System.out.println("nickname : " + nickname);
				this.cookie = re.getCookie();
			}
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	Result postLoginForm() throws ClientProtocolException, IOException
	{
		HashMap<String, String> map = new HashMap<String, String>();

		map.put("charset", "utf-8");
		map.put("tpl", "mn");
		map.put("ppui_logintime", "9987");
		map.put("isPhone", "false");
		map.put("index", "0");
		map.put("mem_pass", "on");
		map.put("loginType", "1");
		map.put("safeflg", "0");

		map.put("username", username);
		map.put("password", password);
		map.put("token", token);
		if (isNeedVcode)
		{
			String verifycode = VcodeManager.getVcode();
			map.put("codestring", codeString);
			map.put("verifycode", verifycode);
		}
		Result re = null;
		re = SendRequest.sendPost(LoginConfig.LOGIN_POST_URL, this.cookie, map);
		return re;
	}

	private Result isNeedVcode() throws ClientProtocolException, IOException
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", token);
		params.put("username", username);
		Result re = null;
		re = SendRequest.sendGet(LoginConfig.ISNEED_VCODE_URL, this.cookie,
				params);
		return re;
	}

	// 得到baidu的cookie
	private Result step1() throws ClientProtocolException, IOException
	{
		Result re = null;
		re = SendRequest.sendGet(LoginConfig.BAIDU, null, null);
		return re;
	}

	private Result step2() throws ClientProtocolException, IOException
	{
		Result re = null;
		re = SendRequest.sendGet(LoginConfig.TOKEN_PAGE, this.cookie, null);
		return re;
	}

	public static void main(String[] args)
	{
		Login login = new Login("chengsimin1989@sina.com", "s2343288");
		login.execute();
	}

}
