package com.tieba.action;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.csm.common.DataUtil;

public class Article
{
	String id;
	String title;
	List<String> floors;
	String first;

	public boolean hasSub()
	{
		if (floors.size() > 1)
			return true;
		return false;
	}

	public String[] allSub()
	{
		if (!hasSub())
			return new String[]
			{};
		String subs[] = new String[this.floors.size() - 1];
		for (int i = 1; i < floors.size(); i++)
			subs[i-1] = floors.get(i);
		return subs;
	}

	public String getTitle()
	{
		return title;
	}

	public String getFirst()
	{
		if (DataUtil.isNotNull(first))
			return first;
		first = floors.get(0);
		return first;
	}

	public Article(String id, String title, List<String> floors)
	{
		super();
		this.id = id;
		this.title = title;
		this.floors = floors;
	}

	public Article(String article,String baseUrl)
	{
		System.out.println("x: "+baseUrl);
		Document doc = Jsoup.parse(article);
		
		Element e2 = doc.select("article").first();
		String id = e2.attr("id");
		String title = e2.select("title").html();
		List<String> floors = new ArrayList<String>();
		for (Element e : doc.select("floor"))
		{
			
			String html = e.html();
			System.out.println(html);
			html = html.replaceAll("<img src=\"[.]/","<img src=\""+baseUrl);
			floors.add(html);	
			System.out.println(html);
			
		}
		this.id = id;
		this.title = title;
		this.floors = floors;
	}
}
