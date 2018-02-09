package com.passwd.htp.crack;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
//import java.net.URLEncoder;
//import java.util.HashMap;

//import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.passwd.htp.login.Server;
import com.passwd.htp.request.HttpRequest;

public class Hashlist {
	private int hashlist_id = -1;
	// 创建待破解的哈希列表Hashlist
	public String create(byte method, String cookie, String hashlistName, int hashtype, int format, int salted, char separator, String hashlistUrlOrString) throws UnsupportedEncodingException, IOException{
		String strUrl = Server.host + "/admin.php?a=newhashlistp";
//		StringBuilder sb = new StringBuilder();
//		sb.append("name=" + hashlistName);
//		sb.append("&hashtype=" + hashtype);
//		sb.append("&format=" + format);
//		sb.append("&salted=" + salted);
//		sb.append("&separator=" + separator);
//		sb.append("&source=url");
//		sb.append("&url=" + URLEncoder.encode(hashlistUrl, "utf-8"));//http://www.htp.com/files/hashlist.txt
//		
//		String data = sb.toString();
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String,String>();
		params.put("name", hashlistName);
		params.put("hashtype", Integer.toString(hashtype));
		params.put("format", Integer.toString(format));
		params.put("salted", Integer.toString(salted));
		params.put("separator", String.valueOf(separator));
		
		//params.put("hashfield","");
		if (method == 0){        //一定要记得把这块注释取消了
			params.put("source", "url");
			params.put("url", hashlistUrlOrString);
		}else if (method ==1){
			params.put("source", "paste");
			params.put("hashfield", hashlistUrlOrString);
		}
		
		String res = HttpRequest.post(strUrl, cookie, /*data*/params); /* params */
		
		String[] arrLine = res.split("\n");
		Pattern p = Pattern.compile("<b>Adding file hashlist_([\\d]+):</b>");
		Matcher m;
		//int hashlist_id = -1;
		for (int i = 0; i < arrLine.length; i++){
			m = p.matcher(arrLine[i]);
			if (m.find()){
				this.hashlist_id = Integer.parseInt(m.group(1));
				break;
			}
		}
		return res;
	}
	
	// 删除hashlist
	public String delete(){
		return "";
	}
	
	public int getHashlistId(){
		return this.hashlist_id;
	}
}
