package com.tieba.work;

import java.io.FileInputStream;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.csm.common.DataUtil;
import com.csm.httpUtil.Result;
import com.csm.httpUtil.SendRequest;
import com.tieba.action.BasicInfor;
import com.tieba.action.Publish;
import com.tieba.config.LoginConfig;
import com.tieba.config.ParamsConfig;
import com.tieba.login.Login;
import com.tieba.login.User;

public class WorkArrange
{
	Site site;
	Random ran = new Random();

	public WorkArrange(String sitePath)
	{
		this.site = new Site(sitePath);
	}

	public void pubTopic()
	{
		// 发好主题帖子
		int k = 0;
		int userSize = LoginConfig.userList.size();
		k = ran.nextInt(userSize);
		for (String tieba : site.getTiebas())
		{
			User user = LoginConfig.userList.get(k % userSize);
			Login login = new Login(user);
			login.execute();
			if (!login.isLogin())
				return;
			Publish pub = new Publish(new BasicInfor(login));

			String tid = pub.pubTopic(tieba, site.getArticle());
			if (DataUtil.isNotNull(tid))
			{
				site.record(tid, tieba, user.getName());
			}
			k++;
		}
	}

	public void pubTopic(boolean select)
	{
		System.out.println();
		int userSize = LoginConfig.userList.size();
		int tiebaSize = site.getTiebas().size();
		Scanner scan = new Scanner(System.in);
		System.out.println("请选择发贴用户");
		for (int i = 0; i < userSize; i++)
		{
			User user = LoginConfig.userList.get(i);
			System.out.print(i + ":" + user.getName() + "   ");
			if ((i + 1) % 5 == 0)
				System.out.println();
		}
		int k1 = scan.nextInt();
		System.out.println("请选择发贴的贴吧");
		for (int i = 0; i < tiebaSize; i++)
		{
			String tieba = site.getTiebas().get(i);
			System.out.print(i + ":" + tieba + "   ");
			if ((i + 1) % 5 == 0)
				System.out.println();
		}
		int k2 = scan.nextInt();
		User user = LoginConfig.userList.get(k1);
		String tieba = site.getTiebas().get(k2);
		Login login = new Login(user);
		login.execute();
		if (!login.isLogin())
			return;
		Publish pub = new Publish(new BasicInfor(login));
		String tid = pub.pubTopic(tieba, site.getArticle());
		if (DataUtil.isNotNull(tid))
		{
			site.record(tid, tieba, user.getName());
		}
	}

	public void replyTopic()
	{
		List<String> tids = site.getRecords();
		List<String> replys = site.getReplys();
		int k = 0;
		int userSize = LoginConfig.userList.size();
		k = ran.nextInt(userSize);
		// 每个回复在每个贴吧回复一遍
		for (String reply : replys)
		{
			User user = LoginConfig.userList.get(k % userSize);
			Login login = new Login(user);
			login.execute();
			if (!login.isLogin())
				return;
			Publish pub = new Publish(new BasicInfor(login));
			for (String tid : tids)
			{
				pub.replyTopic(tid, reply, site.getSource());
			}
			k++;
		}

	}

	public void checkIsExist()
	{
		
		try
		{
			Scanner s = new Scanner(new FileInputStream(site.getSource()
					+ Site.RECORD));
			while (s.hasNextLine())
			{
				String line = s.nextLine();
				System.out.println(line);
				String url = DataUtil.getValueByPattern(line, "(.*?) [\\s|\\S]*");
				Result re = SendRequest.sendGet(url, null, null);
				String content = new String(re.getEntityBytes(),"gbk");
				if (content.indexOf("很抱歉，你访问的贴子不存在") != -1)
				{
					System.out.println(line+" 失效了");
				}
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args)
	{
		WorkArrange wm = new WorkArrange(ParamsConfig.SITES.get(0));
		// wm.pubTopic();
		wm.pubTopic(true);
		// wm.replyTopic();
//		wm.checkIsExist();
	}

}
