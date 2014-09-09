package com.tieba.vcode;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;

import com.csm.httpUtil.Result;
import com.csm.httpUtil.SendRequest;
import com.tieba.ui.test.VcodeFrame;

public class VcodeManager
{
	public static final String LOGIN_VCODE_PAGE = "https://passport.baidu.com/cgi-bin/genimage?";
	public static final String TIEBA_VCODE_PAGE = "http://tieba.baidu.com/cgi-bin/genimg?";
	public static final int TYPE_JIUGONGGE = 4;
	public static VcodeFrame vf = new VcodeFrame();
	
	public static void disposeLoginVcode(String codeString)
	{
		try
		{
			vf.updatePic(LOGIN_VCODE_PAGE + codeString);
			if(true)
			return;
			Result re = SendRequest.sendGet(LOGIN_VCODE_PAGE + codeString,
					null, null);
			System.out.println(re);
			System.out.println(re.getContentType());
			creatImage("/home/csmubuntu/JavaTest/", "temp",
					"gif", re.getEntityBytes());
//			creatImage("/home/csmubuntu/JavaTest/", "temp",
//					getImageType(re.getContentType()), re.getEntityBytes());
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void disposeTiebaVcode(String codeString,int type)
	{
		try
		{
			vf.updatePic(TIEBA_VCODE_PAGE + codeString);
			if(true)
			return;
			Result re = SendRequest.sendGet(TIEBA_VCODE_PAGE + codeString,
					null, null);
			System.out.println(re);
			System.out.println(re.getContentType());
			String ext = "jpg";
			if(type==TYPE_JIUGONGGE)
			{
				ext = "png";
			}
			creatImage("/home/csmubuntu/JavaTest/", "tieba",
					ext, re.getEntityBytes());
//			creatImage("/home/csmubuntu/JavaTest/", "temp",
//					getImageType(re.getContentType()), re.getEntityBytes());
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static String getVcode()
	{
		return getVcode(0);
	}
	
	// 
	public static String getVcode(int type)
	{
		System.out.println("请输入验证码");
		Scanner s = new Scanner(System.in);
//		String vcode = s.next();
		String vcode = vf.getVcode();
		if(vcode.length()!=4)
		{
			System.out.println("验证码应该是4位吧");
			vcode = vf.getVcode();
		}
		if(type == TYPE_JIUGONGGE)
		{
			vcode = getJiuGongGe(vcode);
		}
		System.out.println("vcode : "+vcode);
		return vcode;
	}
	
	private static void creatImage(String dir, String name, String type,
			byte[] content) throws IOException
	{
		File dirFile = new File(dir);
		if (!dirFile.isDirectory())
			dirFile.mkdirs();
		File imgFile = new File(dirFile, name + "." + type);
		FileOutputStream fos = new FileOutputStream(imgFile);
		fos.write(content);
	}

	private static String getImageType(String contentType)
	{
		if (contentType.equals("image/jpg"))
			return "jpg";
		return "";
	}
	
	private static String f(char a)
	{
		int b = a-49;
		int c1 = b%3;
		int c2 = b/3;
		return "000"+(char)(c1+48)+"000"+(char)(c2+48);
	}
	private static String getJiuGongGe(String srt)
	{
		String re = "";
		for(int i=0;i<srt.length();i++)
		{
			re+=f(srt.charAt(i));
		}
		return re;
	}
}
