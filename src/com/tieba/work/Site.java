package com.tieba.work;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.csm.common.DataUtil;
import com.csm.common.FileIOUtil;
import com.tieba.action.Article;
import com.tieba.config.TiebaConfig;

public class Site
{
	public String getSource()
	{
		return source;
	}

	public List<String> getTiebas()
	{
		return tiebas;
	}

	public Article getArticle()
	{
		return article;
	}

	public List<String> getReplys()
	{
		return replys;
	}

	Article article;
	List<String> replys;
	List<String> tiebas;
	private static final String TOPIC = "topic.txt";
	private static final String REPLY = "reply.txt";
	private static final String TIEBA = "tieba.txt";
	 static final String RECORD = "record.txt";
	String source;

	public Site(String source)
	{
		this.source = source;
		System.out.println("载入工作站点信息" + source);
		String art = FileIOUtil.readText(source + TOPIC);
		this.article = new Article(art,source);
		{
			String replysAll = FileIOUtil.readText(source + REPLY);
			String temps[] = replysAll.split("[-]{5,}\n");
			this.replys = Arrays.asList(temps);
		}
		{
			String tiebaAll = FileIOUtil.readText(source + TIEBA);
			String temps[] = tiebaAll.split("\n");
			this.tiebas = Arrays.asList(temps);
		}
		System.out.println("工作站点信息载入完成");
	}

	public void record(String tid,String tieba,String userName)
	{
		FileIOUtil.writeText(source + RECORD, TiebaConfig.TIEBA_REPLY + tid+"  "+tieba+"  "+userName
				+ "\r\n", "utf-8", true);
	}

	public List<String> getRecords()
	{
		List<String> tids = new ArrayList<String>();
		String res = FileIOUtil.readText(source + RECORD, "utf-8");
		String temps[] = res.split("\n");
		for (String temp : temps)
		{
			tids.add(DataUtil.getValueByPattern(temp, TiebaConfig.TIEBA_REPLY
					+ "(\\d+)\\s*"));
		}
		return tids;
	}
}
