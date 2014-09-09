package com.csm.httpUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import com.csm.common.DataUtil;


/**
 * 发送请求
 * 
 * @author Legend、
 * 
 */

public class SendRequest
{
	public static DefaultHttpClient client = new DefaultHttpClient();

	// 这是模拟get请求
	public static Result sendGet(String url, Map<String, String> headers,
			Map<String, String> params, String encoding, boolean duan)
			throws ClientProtocolException, IOException
	{
		// 实例化一个Httpclient的
		// DefaultHttpClient client = new DefaultHttpClient();

		// 如果有参数的就拼装起来 拼装参数可能出现的两种情况 已有部分参数，没有参数
		if (params != null && params.size() > 0)
		{
			if (url.indexOf('?') == -1)
			{
				url = url + '?' + assemblyParameter(params);
			} else
			{
				url = url + '&' + assemblyParameter(params);
			}
		}
		// 这是实例化一个get请求
		HttpGet hp = new HttpGet(url);
		// 如果需要头部就组装起来
		if (null != headers)
			hp.setHeaders(assemblyHeader(headers));
		// 执行请求后返回一个HttpResponse
		HttpResponse response = client.execute(hp);
		// 如果为true则断掉这个get请求
		if (duan)
			hp.abort();
		return setResutl(client, response);
	}

	public static Result sendGet(String url, Map<String, String> headers,
			Map<String, String> params, String encoding)
			throws ClientProtocolException, IOException
	{
		return sendGet(url, headers, params, encoding, false);
	}

	public static Result sendGet(String url, String cookie,
			Map<String, String> params) throws ClientProtocolException,
			IOException
	{
		Map<String, String> headers = new HashMap<String, String>();
		if (DataUtil.isNotNull(url))
			headers.put("Cookie", cookie);
		return sendGet(url, headers, params, "utf-8", false);
	}

	// 这是模拟post请求
	public static Result sendPost(String url, Map<String, String> headers,
			Map<String, String> params, String encoding)
			throws ClientProtocolException, IOException
	{
		// 实例化一个Httpclient的
		// DefaultHttpClient client = new DefaultHttpClient();
		// 实例化一个post请求
		HttpPost post = new HttpPost(url);

		// 设置需要提交的参数
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (String temp : params.keySet())
		{
			list.add(new BasicNameValuePair(temp, params.get(temp)));
		}
		post.setEntity(new UrlEncodedFormEntity(list, encoding));
		// 设置头部
		if (null != headers)
			post.setHeaders(assemblyHeader(headers));
		// 实行请求并返回
		HttpResponse response = client.execute(post);
		return setResutl(client, response);
	}


	public static Result sendFilePost(String url, Map<String, String> headers,
			Map<String, Object> params) throws ClientProtocolException,
			IOException
	{
		// DefaultHttpClient client = new DefaultHttpClient();

		HttpPost httppost = new HttpPost(url);
		httppost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,2000);
		// 设置头部
		if (null != headers)
			httppost.setHeaders(assemblyHeader(headers));

		MultipartEntity reqEntity = new MultipartEntity();
		for (String temp : params.keySet())
		{
			Object obj = params.get(temp);
			if (obj instanceof File)
				reqEntity.addPart(temp, new FileBody((File) obj));
			else if (obj instanceof String)
				reqEntity.addPart(temp, new StringBody((String) obj));
		}
		httppost.setEntity(reqEntity);

		System.out.println("executing request " + httppost.getRequestLine());

		HttpResponse response = client.execute(httppost);
		return setResutl(client, response);
	}

	public static Result sendPost(String url, String cookie,
			Map<String, String> params) throws ClientProtocolException,
			IOException
	{
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Cookie", cookie);
		return sendPost(url, headers, params, "utf-8");
	}

	// 这是组装头部
	static Header[] assemblyHeader(Map<String, String> headers)
	{
		Header[] allHeader = new BasicHeader[headers.size()];
		int i = 0;
		for (String str : headers.keySet())
		{
			allHeader[i] = new BasicHeader(str, headers.get(str));
			i++;
		}
		return allHeader;
	}

	// 这是组装cookie 不能分行
	// static String assemblyCookie(List<Cookie> cookies)
	// {
	// StringBuffer sbu = new StringBuffer();
	// for (int i = 0; i < cookies.size(); i++)
	// {
	// Cookie cookie = cookies.get(i);
	// sbu.append(cookie.getName()).append("=").append(cookie.getValue());
	// if (i != cookies.size())
	// sbu.append(";").append("\n");
	// }
	// return sbu.toString();
	// }

	// 这是组装cookie
	public static String assemblyCookie(List<Cookie> cookies)
	{
		StringBuffer sbu = new StringBuffer();
		for (Cookie cookie : cookies)
		{
			sbu.append(cookie.getName()).append("=").append(cookie.getValue())
					.append(";");
		}
		if (sbu.length() > 0)
			sbu.deleteCharAt(sbu.length() - 1);
		return sbu.toString();
	}

	// 这是组装参数
	static String assemblyParameter(Map<String, String> parameters)
	{
		String para = "";
		for (String str : parameters.keySet())
		{
			para += str + "=" + parameters.get(str) + "&";
		}
		return para.substring(0, para.length() - 1);
	}

	static Result setResutl(DefaultHttpClient client, HttpResponse response)
	{
		HttpEntity entity = response.getEntity();
		// 封装返回的参数
		Result result = new Result();
		// 设置返回的cookie
		result.setCookie(assemblyCookie(client.getCookieStore().getCookies()));
		// 设置返回的状态
		result.setStatusCode(response.getStatusLine().getStatusCode());
		// 设置返回的头部信心
		result.setHeaders(response.getAllHeaders());
		// 设置返回的信息
		result.setHttpEntity(entity);
		return result;
	}

	public static void main(String s[]) throws ClientProtocolException,
			IOException
	{

	}
}

abstract class Sub implements Runnable
{
	Result re;

	public Sub(Result re)
	{
		this.re = re;
	}

}
