package com.tieba.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.ClientProtocolException;
import com.csm.httpUtil.Result;
import com.csm.httpUtil.SendRequest;
import com.tieba.config.TiebaConfig;

public class TiebaCore
{

	public static Result postTopic(String title, String content,
			String vcode_md5, String vcode, BasicInfor in)
			throws ClientProtocolException, IOException
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("kw", in.getKw());
		params.put("fid", in.getFid());
		params.put("tid", in.getTid());
		params.put("title", title);
		params.put("content", content);
		params.put("vcode_md5", vcode_md5);
		params.put("vcode", vcode);
		params.put("tbs", in.getTbs());
		params.put("is_login", "1");
		params.put("floor_number", "0");
		params.put("useSignName", "off");
		params.put("add_post_submit", " 发 表 ");
		params.put("ie", "utf-8");
		if (in.hasImage())
		{
			params.put("hasuploadpic", "1");
		} else
		{
			params.put("hasuploadpic", "0");
		}
		Result re = SendRequest.sendPost(TiebaConfig.TOPIC_POST_URL,
				in.getCookie(), params);
		return re;
	}

	public static Result replyTopic(String content, String vcode_md5,
			String vcode, BasicInfor in) throws ClientProtocolException,
			IOException
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("kw", in.getKw());
		params.put("fid", in.getFid());
		params.put("tid", in.getTid());
		params.put("content", content);
		params.put("vcode_md5", vcode_md5);
		params.put("vcode", vcode);
		params.put("tbs", in.getTbs());
		params.put("is_login", "1");
		params.put("ie", "utf-8");
		params.put("rich_text", "1");
		params.put("lp_type", "0");
		params.put("lp_sub_type", "0");
		params.put("tag", "11");
		params.put("new_vcode", "1");
		params.put("mouse_pwd", "10,2,0,30,0,5,7,5,59,3,30,2,30,3,30,2,30,3,30,2,30,3,30,2,30,3,30,2,59,6,7,10,3,4,59,3,1,4,4,30,5,4,10,13731831178501");
		params.put("mouse_pwd_t", "1373183117850");
		params.put("mouse_pwd_isclick", "1");
		if (in.hasImage())
		{
			params.put("hasuploadpic", "1");
		} else
		{
			params.put("hasuploadpic", "0");
		}
		Result re = SendRequest.sendPost(TiebaConfig.REPLY_POST_URL,
				in.getCookie(), params);
		return re;

	}

	public static Result likeThisTieba(BasicInfor in)
			throws ClientProtocolException, IOException
	{
		Map<String, String> params = new HashMap<String, String>();

		params.put("fname", in.getKw());
		params.put("fid", in.getFid());
		params.put("tbs", in.getTbs());
		params.put("uid", in.getNickname());
		params.put("ie", "utf-8");
		Result re = SendRequest.sendPost(TiebaConfig.LIKE_POST_URL,
				in.getCookie(), params);
		return re;
	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
