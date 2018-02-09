package com.passwd.htp.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;


public class HttpRequest {
	private static String responseCookie = "";
	
	public static String get(String strUrl, String cookie) throws MalformedURLException, IOException{
		StringBuilder sbR = new StringBuilder();
		
		//打开URL连接
		URL url = new URL(strUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		//给服务器送登录后的cookie
		connection.setRequestProperty("Cookie", cookie);
		
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
	
	public static String post(String strUrl, Map<String, String> header, String data) throws MalformedURLException, IOException{
		StringBuilder sbR = new StringBuilder();
		String cookie = null;
		//访问URL，并把信息存入sb中
		//如果服务端登录成功后，服务端的代码调用下面的代码 
		//response.sendRedirect("welcome.jsp");
		//则会不成功，原因(Step2，没有上传jsessionid值，导致没session)如下
		//Step1[login.jsp登录成功]->转到->
		//Step2[welcome.jsp不能得到session，判断没有登录成功]->转到->Step3[login.jsp要求用户登录]
		
		URL url = new URL(strUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true); // default true
		connection.setDoOutput(true);//允许连接提交信息		
		connection.setRequestMethod("POST");//网页默认“GET”提交方式
		connection.setUseCaches(false);
//		StringBuffer sb = new StringBuffer();
//		sb.append(data/*"Name="+usr*/);
		//sb.append("&pwd="+pwd);
//		if(cookie != null && cookie != ""){
//			connection.setRequestProperty("Cookie", cookie);
//		}
		if (header != null){
			@SuppressWarnings("rawtypes")
			Iterator itr = header.entrySet().iterator();
			while (itr.hasNext()){
				@SuppressWarnings("unchecked")
				Map.Entry<String, String> entry = (Entry<String, String>) itr.next();
				connection.setRequestProperty(entry.getKey(), entry.getValue());
				if(entry.getKey().equals("Cookie")){
					cookie = entry.getValue();
				}
			}
		}
		
		//connection.setRequestProperty("Content-Length", 
		//		Integer.toString(664));   
		OutputStream os = connection.getOutputStream();
		os.write(data.getBytes());
		os.flush();
		os.close();

		//取Cookie
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		if(cookie == null || cookie == ""){
			HttpRequest.responseCookie = connection.getHeaderField("Set-Cookie");//取到所用的Cookie
		}
		//取返回的页面
		String line = "";
		do{
			sbR.append(line + "\n");
			line = br.readLine();
		}while (line != null); 
		
		return sbR.toString();
	}
	
	public static String post(String strUrl, String cookie, String data) throws MalformedURLException, IOException, IOException{
		HashMap<String,String> header = new HashMap<String,String>();
		header.put("Cookie", cookie);
		return HttpRequest.post(strUrl, header, data);
	}
	
	public static String post(String strUrl, String cookie, LinkedHashMap<String, String> params) throws MalformedURLException, IOException, IOException{
		String boundary = "----WebKitFormBoundaryhDHGXmeqBxa3zABg";
		TreeMap<String, String> header = new TreeMap<String, String>();
		header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		header.put("Content-Type", "multipart/form-data; boundary=" + boundary);
		header.put("Cookie", cookie);
		
		String data = "";//"Content-Type: multipart/form-data; boundary=" + boundary + "\r\n\r\n";
		
		Iterator<Map.Entry<String, String>> itr = params.entrySet().iterator();
		
		while(itr.hasNext()){
			Map.Entry<String, String> entry = itr.next();
			data += ("--" + boundary + "\r\n");
			data += ("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + "\r\n\r\n");
			data += (entry.getValue() + "\r\n");
		}
		data += ("--" + boundary + "--\r\n");
		
		return HttpRequest.post(strUrl, header, data);	
	}
	
	public static String getResponseCookie(){
		return HttpRequest.responseCookie;
	}
}
