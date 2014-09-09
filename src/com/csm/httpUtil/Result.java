package com.csm.httpUtil;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;


/**
 * 封住请求返回的参数
 * 
 * @author Legend、
 * 
 */

public class Result
{
	public static long id = 0;
	private String cookie;
	private int statusCode;
	private HashMap<String, Header> headerAll;
	private HttpEntity httpEntity;
	private byte[] contentBytes = null;
	private String content = null;

	public Result()
	{
		id++;
	}

	public String getCookie()
	{
		return cookie;
	}

	public void setCookie(String cookie)
	{
		this.cookie = cookie;
	}

	public int getStatusCode()
	{
		return statusCode;
	}

	public void setStatusCode(int statusCode)
	{
		this.statusCode = statusCode;
	}

	public HashMap<String, Header> getHeaders()
	{
		return headerAll;
	}

	public void setHeaders(Header[] headers)
	{
		headerAll = new HashMap<String, Header>();
		for (Header header : headers)
		{
			headerAll.put(header.getName(), header);
		}
	}

	public HttpEntity getHttpEntity()
	{
		return httpEntity;
	}

	public void setHttpEntity(HttpEntity httpEntity)
	{
		this.httpEntity = httpEntity;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("result id :").append(id).append("\n");
		sb.append("打印返回的所有信息如下：\n");
//		sb.append("statusCode : ").append(statusCode).append("\n");
//		sb.append("cookie ： ").append("\n");
//		for (String ct : cookie.split(";"))
//		{
//			sb.append("   ").append(ct).append("\n");
//		}
//
//		sb.append("所有头部信息 : ").append("\n");
//		for (String key : headerAll.keySet())
//		{
//			sb.append("   ").append(key).append(" = ")
//					.append(headerAll.get(key)).append("\n");
//		}

		sb.append("内容实体 ：").append(getEntityContent()).append("\n");

		sb.append("\n*********************************************\n");
		return sb.toString();

	}

	public byte[] getEntityBytes()
	{
		if (contentBytes != null)
			return contentBytes;
		try
		{
			contentBytes = EntityUtils.toByteArray(httpEntity);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contentBytes;
	}

	public String getEntityContent()
	{
		if (content != null)
		{
			return content;
		}
		if (getContentType().indexOf("image") != -1)
		{
			return "内容是图片流";
		}
		return new String(getEntityBytes());
	}

	public String getContentType()
	{
		Header header = headerAll.get("Content-Type");
		if (header == null)
			return "";
		return header.getValue();
	}
}
