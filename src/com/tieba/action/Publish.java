package com.tieba.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;

import com.csm.common.DataUtil;
import com.csm.common.ThreadUtil;
import com.csm.httpUtil.Result;
import com.csm.httpUtil.SendRequest;
import com.csm.tuple.ThreeTuple;
import com.csm.tuple.TwoTuple;
import com.tieba.config.ParamsConfig;
import com.tieba.config.TiebaConfig;
import com.tieba.login.Login;
import com.tieba.vcode.VcodeManager;

public class Publish
{
	BasicInfor infor;

	public Publish(BasicInfor infor)
	{
		this.infor = infor;
	}

	public String pubTopic(String kw, Article art)
	{
		try
		{
			infor.setKw(kw);
			infor.setInforationFromURL(TiebaConfig.TIEBA + kw,
					BasicInfor.POST_TOPIC);
			if(infor.exception)
			{
				System.out.println("悲剧帐号被封了"+infor.getNickname());
				return "";
			}
			disposeLike();
			String title = art.getTitle();
			String vcode_md5 = "";
			String vcode = "";
			// 先发表一个新帖子
			String content = art.getFirst();
			TwoTuple<String, String> conentPackage = uploadImgInConent(content);
			content = conentPackage.getFirst();
			// 有图的
			Result re = null;
			{
				System.out.println("title" + title);
				System.out.println("content : " + content);
				re = TiebaCore.postTopic(title, content, vcode_md5, vcode,
						this.infor);
			}
			System.out.println(re);
			ThreeTuple<Boolean, String, Integer> rePack = checkResult(re
					.getEntityContent());
			boolean success = true;
			// 如果发送有图的需要验证码
			if (!rePack.getFirst())
			{
				// 设定为放弃验证码
				if (ParamsConfig.ABANDON_VCODE)
				{
					// 如果本来就没有图
					if (!infor.hasImage)
					{
						System.out.println(kw + " 无图版本还是需要验证码，发贴失败");
						success = false;
					} else
					{
						// 尝试发送无图版本
						content = conentPackage.getSecond();
						infor.hasImage = false;
						// 再次提交表单
						re = TiebaCore.postTopic(title, content, vcode_md5,
								vcode, this.infor);
						rePack = checkResult(re.getEntityContent());
						if (rePack.getFirst())
						{
							System.out.println(kw + " 无图版本发贴成功");
							success = true;
						} else
						{
							System.out.println(kw + " 无图版本还是需要验证码，发贴失败");
							success = false;
						}
					}
				} else
				{
					// 输入验证码
					vcode_md5 = rePack.getSecond();
					VcodeManager
							.disposeTiebaVcode(vcode_md5, rePack.getThird());
					vcode = VcodeManager.getVcode(rePack.getThird());
					// 再次提交表单
					re = TiebaCore.postTopic(title, content, vcode_md5, vcode,
							this.infor);
					rePack = checkResult(re.getEntityContent());
					if (rePack.getFirst())
					{
						System.out.println(kw + " 人工发贴成功");
						success = true;
					} else
					{
						System.out.println(kw + " 人工发贴失败，可能是验证码输入错误");
						success = false;
					}
				}
			} else
			{
				System.out.println(kw + " 直接发贴成功");
				success = true;
			}
			if (!success)
				return null;
			String tid = DataUtil.getValueByPattern(re.getEntityContent(),
					"[\\S|\\s]*\"tid\":(\\d*),[\\s|\\S]*");
			if (!art.hasSub())
				return tid;
			Thread.sleep(5000);
			// 发主题帖中的回帖
			for (String sub : art.allSub())
			{
				replyTopic(tid, sub);
				Thread.sleep(1500);
			}
			System.out.println(re);
			if (isExist())
				return tid;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void replyTopic(String tid, String content, String imgBaseUrl)
	{
		content = content.replaceAll("<img src=\"[.]/", "<img src=\""
				+ imgBaseUrl + "");
		replyTopic(tid, content);
	}

	// 回帖
	public void replyTopic(String tid, String content)
	{
		try
		{
			this.infor.setTid(tid);
			this.infor.setInforationFromURL(TiebaConfig.TIEBA_REPLY + tid,
					BasicInfor.REPLY_TOPIC);
			String kw = infor.getKw();
			if (!isExist())
			{
				System.out.println(kw + " 你要回复的帖子不存在了");
				return;
			}
			disposeLike();
			TwoTuple<String, String> conentPackage = uploadImgInConent(content);
			content = conentPackage.getFirst();
			String vcode_md5 = "";
			String vcode = "";
			Result re = null;
			{
				System.out.println("content : " + content);
				re = TiebaCore
						.replyTopic(content, vcode_md5, vcode, this.infor);
			}
			System.out.println(re);
			ThreeTuple<Boolean, String, Integer> rePack = checkResult(re
					.getEntityContent());
			boolean success = false;
			// 如果发送有图的需要验证码
			if (!rePack.getFirst())
			{
				// 设定为放弃验证码
				if (ParamsConfig.ABANDON_VCODE)
				{
					// 如果本来就没有图
					if (!infor.hasImage)
					{
						System.out.println(kw + " 无图版本还是需要验证码，回贴失败");
						success = false;
					} else
					{
						// 尝试发送无图版本
						content = conentPackage.getSecond();
						infor.hasImage = false;
						// 再次提交表单
						re = TiebaCore.replyTopic(content, vcode_md5, vcode,
								this.infor);
						rePack = checkResult(re.getEntityContent());
						if (rePack.getFirst())
						{
							System.out.println(kw + " 无图版本回贴成功");
							success = true;
						} else
						{
							System.out.println(kw + " 无图版本还是需要验证码，回贴失败");
							success = false;
						}
					}
				} else
				{
					while (!success)
					{
						// 输入验证码
						vcode_md5 = rePack.getSecond();
						VcodeManager.disposeTiebaVcode(vcode_md5,
								rePack.getThird());
						vcode = VcodeManager.getVcode(rePack.getThird());
						// 再次提交表单
						re = TiebaCore.replyTopic(content, vcode_md5, vcode,
								this.infor);
						System.out.println("回帖： "+re);
						rePack = checkResult(re.getEntityContent());
						if (rePack.getFirst())
						{
							System.out.println(kw + " 人工回贴成功");
							success = true;
						} else
						{
							System.out.println(kw + " 人工回贴失败，可能是验证码输入错误");
							System.out.println("是否要继续尝试 1.是 2.否 请输入编号");
							Scanner s = new Scanner(System.in);
							int select = s.nextInt();
							if(select == 1)
								continue;
							success = false;
							System.out.println("不尝试，回帖失败");
							break;
						}
					}
				}
			} else
			{
				System.out.println(kw + " 直接回贴成功");
				success = true;
			}
			if (!success)
				return;
		} catch (ClientProtocolException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public boolean isExist()
	{
		return infor.isTieziExist();
	}

	void disposeLike() throws ClientProtocolException, IOException
	{
		if (!infor.isLike())
		{
			System.out.println("设定为喜欢这个贴吧");
			Result re = TiebaCore.likeThisTieba(infor);
			System.out.println(re);
			ThreadUtil.sleep(1500);
		}
	}

	TwoTuple<String, String> uploadImgInConent(String content)
			throws ClientProtocolException, IOException
	{
		String hasImage = new String(content);
		String noImage = new String(content);
		String imgPattern = "<img src=\"(.*?)\"[ ]?/>";
		Pattern p = Pattern.compile(imgPattern);
		Matcher m = p.matcher(content);

		while (m.find())
		{

			infor.hasImage = true;
			{
				noImage = noImage.replaceFirst(imgPattern, "");
			}
			{
				String path = m.group(1);
				System.out.println("path : " + path);
				File file = new File(path);
				if (!file.exists())
					System.out.println("图片不存在");
				// 先得到图像的IMG TBS
				Result re = SendRequest.sendGet(TiebaConfig.IMAGE_TBS_URL,
						infor.getCookie(), null);
				// {"no":0,"data":{"tbs":"d1e2c5ac186658b6013724298370125500_1","is_login":1},"error":""}
				String tbsContent = re.getEntityContent();
				String imgTbs = DataUtil.getValueByPattern(tbsContent,
						".*tbs\":\"([\\w]*)\",.*");
				System.out.println("imgTbs : " + imgTbs);
				Map<String, String> headers = new HashMap<String, String>();
				// headers.put("Content-Type", "");
				headers.put("Cookie", re.getCookie());
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("file", file);
				params.put("tbs", imgTbs);
				params.put("fid", infor.getFid());
				params.put("Filename", file.getName());
				params.put("Upload", "Submit Query");

				re = SendRequest.sendFilePost(TiebaConfig.IMAGE_POST_URL,
						headers, params);
				System.out.println(re);
				String imgSrc = parseImgInfor(re.getEntityContent());
				hasImage = hasImage.replaceFirst(imgPattern, imgSrc);
			}
		}
		return new TwoTuple<String, String>(hasImage, noImage);
	}

	private String parseImgInfor(String jsonSrc)
	{

		String src = DataUtil.getValueByPattern(jsonSrc,
				"[\\S|\\s]*\"pic_water\":\"(.*?)\"[\\S|\\s]*");
		if (!DataUtil.isNotNull(src))
			return "";
		String height = DataUtil.getValueByPattern(jsonSrc,
				"[\\S|\\s]*\"fullpic_height\":(\\d*),[\\S|\\s]*");
		String width = DataUtil.getValueByPattern(jsonSrc,
				"[\\S|\\s]*\"fullpic_width\":(\\d*),[\\S|\\s]*");
		String bdwater = "";
		StringBuilder imgSrc = new StringBuilder();
		imgSrc.append(
				"<img pic_type=\\\"0\\\" class=\\\"BDE_Image\\\" src=\\\"")
				.append(src).append("\\\" pic_ext=\\\"\\\" bdwater=\\\"")
				.append(bdwater).append("\\\" height=\\\"").append(height)
				.append("\\\" width=\\\"").append(width).append("\\\">");
		// imgSrc.append(src);

		return imgSrc.toString();
	}

	private ThreeTuple<Boolean, String, Integer> checkResult(String result)
	{
		String vcode_md5 = DataUtil.getValueByPattern(result,
				"[\\S|\\s]*\"captcha_vcode_str\":\"([\\w]*)\",[\\S|\\s]*");
		ThreeTuple<Boolean, String, Integer> pack = new ThreeTuple<Boolean, String, Integer>(
				true, "", 0);
		if (DataUtil.isNotNull(vcode_md5) && !vcode_md5.equals("null"))
		{
			pack.setFirst(false);
			pack.setSecond(vcode_md5);
			String typeStr = DataUtil.getValueByPattern(result,
					"[\\S|\\s]*\"captcha_code_type\":([\\d]*)[\\S|\\s]*");
			pack.setThird(Integer.parseInt(typeStr));
		}
		return pack;
	}

	public static void main(String[] args)
	{
		Login login = new Login("chengsimin1989@sina.com", "s2343288");
		login.execute();
		if (!login.isLogin())
		{
			System.out.println("未登录");
			return;
		}
		BasicInfor infor = new BasicInfor(login.getCookie());
		infor.setNickname(login.getNickname());
		infor.setUsername(login.getUsername());
		Publish pub = new Publish(infor);
		String html = "<article id='1'><title>我爱盘静静</title><floor>计算数论是一们很好玩的学科<img src=\"/home/csmubuntu/JavaTest/content/1.png\"/>sss</floor><floor>就是这样的顶起啊</floor><floor>这么好的东西不顶没人性啊</floor></article>";
		pub.pubTopic("计算数论", new Article(html, ""));
		// String html =
		// "我是超人哈<img src=\"/home/csmubuntu/JavaTest/content/1.png\"/>哈后";
		// pub.replyTopic("242021393233", html);
	}

}
