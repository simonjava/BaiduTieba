package com.tieba.action;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;

import com.csm.common.DataUtil;
import com.csm.httpUtil.Result;
import com.csm.httpUtil.SendRequest;
import com.tieba.login.Login;

public class BasicInfor
{

	static final int POST_TOPIC = 1;
	static final int REPLY_TOPIC = 2;
	String kw;
	String username;
	String cookie;
	String nickname;
	private String tbs;
	private String fid;
	private boolean isLike = false;
	boolean hasImage = false;
	boolean isExist = true;
	boolean exception = false;
	
	public BasicInfor(Login login)
	{
		this.setCookie(login.getCookie());
		this.setNickname(login.getNickname());
		this.setUsername(login.getUsername());
	}

	public String getTbs()
	{
		return tbs;
	}

	public String getFid()
	{
		return fid;
	}

	public boolean isLike()
	{
		return isLike;
	}



	public String getKw()
	{
		return kw;
	}

	public void setKw(String kw)
	{
		this.kw = kw;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getCookie()
	{
		return cookie;
	}

	public void setCookie(String cookie)
	{
		this.cookie = cookie;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public void setInforationFromURL(String url, int type)
			throws ClientProtocolException, IOException
	{
		isExist = true;
		isLike = false;
		hasImage = false;
		exception = false;
		
		Result re = SendRequest.sendGet(url, this.cookie, null);
		String content = new String(re.getEntityBytes(), "gbk");
		this.fid = DataUtil.getValueByPattern(content,
				"[\\S|\\s]*fid:'([\\d]*)',[\\S|\\s]*");
		if(content.indexOf("你的账号发生异常，现处于屏蔽状态，你发表的贴子已被隐藏")!=-1)
			this.exception = true;
		if (content.indexOf("<div class=\"userlike_info_manager\">") != -1)
			this.isLike = true;
		if (type == POST_TOPIC)
		{
			this.tbs = DataUtil.getValueByPattern(content,
					"[\\S|\\s]*PageData.tbs = \"([\\w]*)\";[\\S|\\s]*");
		} else if (type == REPLY_TOPIC)
		{
			if (content.indexOf("很抱歉，你访问的贴子不存在") != -1)
			{
				isExist = false;
				return;
			}
			this.kw = DataUtil.getValueByPattern(content,
					"[\\S|\\s]*forum_name:\"(.*?)\",[\\S|\\s]*");
			System.out.println("kw : " + kw);
			this.tbs = DataUtil.getValueByPattern(content,
					"[\\S|\\s]*'tbs'[\\s]*: \"([\\w]*)\",[\\S|\\s]*");
		}
		System.out.println("tbs : " + tbs);
		System.out.println("fid ：" + fid);
		System.out.println("isLike : " + isLike);

	}

	public BasicInfor(String cookie)
	{
		super();
		this.cookie = cookie;
	}

	String tid = "0";

	public String getTid()
	{
		return tid;
	}

	public void setTid(String tid)
	{
		this.tid = tid;
	}

	public boolean hasImage()
	{
		return hasImage;
	}

	public boolean isTieziExist()
	{
		return this.isExist;
	}
}
