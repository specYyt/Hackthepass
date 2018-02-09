package com.passwd.util;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.passwd.htp.request.HttpRequest;

public class Cmd5API {
	private static String strUrl = "http://www.cmd5.com/"; // cmd5网址
	private static String cookie = ""; // 访问cmd5时的cookie
	/* 查询hash
	 * param1: hashType,即hash类型。字符串，与cmd5.com网站下拉列表里的option的value一致，否则返回“error”
	 * param2: hashStr, 即待解密的hash密文
	 */
	public static String queryHash(String hashType, String hashStr) throws MalformedURLException, IOException{
		String html = HttpRequest.get(strUrl, cookie);
		//Jsoup去解析html，获取里面的input标签
		Document doc = Jsoup.parse(html);
		// 解析支持的hash类型
		Elements eles_opt = doc.getElementsByTag("option");
		// 放进数组
		String[] hash_types = new String[eles_opt.size()];
		//hash类型是否有效的标志
		boolean ok_hashType = false;
		for (Element ele_opt : eles_opt) {
			//如果存在，就改为true表示有效
			if(ele_opt.attr("value").equals(hashType))
				ok_hashType = true;
			// 写进数组
			hash_types[eles_opt.indexOf(ele_opt)] = ele_opt.attr("value");
		}
		if (ok_hashType == false){
			System.out.println("Hash类型错误!");
			return "Error";
		}
		Elements eles = doc.getElementsByTag("input");
		// 准备组装要提交的数据，先组装隐藏字段
		String data2Post = "";
		for (Element element : eles) {
			// &符号连接
			if (!data2Post.equals(""))
				data2Post += "&";
			// 有些字段应为空
			//if (element.attr("name").substring(0, 3).equals("__E"))
			//	data2Post += (element.attr("name") + "=");
			//如果是填密文的字段
			/*else */if(element.attr("name").equals("ctl00$ContentPlaceHolder1$TextBoxInput")){
				data2Post += (element.attr("name") + "=" + URLEncoder.encode(hashStr, "utf-8"));
			}
			// 否则就是隐藏字段或值不需要用户填写的
			else
				data2Post += (element.attr("name") + "=" + URLEncoder.encode(element.attr("value"),"utf-8"));
		}
		//密文类型
		Element ele_hash_type = doc.getElementById("ctl00_ContentPlaceHolder1_InputHashType");
		data2Post += ("&" + ele_hash_type.attr("name") + "=" + URLEncoder.encode(hashType, "utf-8"));
		
		// 提交查询，返回带有结果的html_res
		HashMap hashMap_Referer = new HashMap<String,String>();
		hashMap_Referer.put("Referer",strUrl);
		String html_res = HttpRequest.post(strUrl, hashMap_Referer, data2Post);
		// 解析html，提取结果
		Document doc_res = Jsoup.parse(html_res);
		Element ele_span_res = doc_res.getElementById("ctl00_ContentPlaceHolder1_LabelAnswer");
		String str_res = ele_span_res.text();
		
		return str_res.split(" ")[0];
	}
}
