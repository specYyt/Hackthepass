package com.passwd.htp.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Test {
	public static String get(String strUrl) throws MalformedURLException, IOException{
		StringBuilder sbR = new StringBuilder();
		
		//打开URL连接
		URL url = new URL(strUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		//给服务器送登录后的cookie
//		connection.setRequestProperty("Cookie", cookie);
		
		//读取返回的页面信息到br1
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		//取返回的页面,br1转sbR
		String line = "";
		boolean first = true;
		do{
			if(first == false)
				sbR.append(line + "\n");
			first = false;
			line = br.readLine();
		}while (line != null);
		return sbR.toString();		
	}
}
