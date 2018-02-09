package com.passwd.htp.login;

import java.io.IOException;

import com.passwd.htp.request.HttpRequest;

/*
 * 模拟登录系统的简单示例
 * */

public class HttpLogin {
	
	private static String host;
	private static String sURL;
	private static String responseCookie;//标示Session必须
	
	public HttpLogin(String host, String path){
		HttpLogin.sURL = host + path;
	}
	//测试登录功能，返回“自动”登录后的页面
	public String login(String usr,String pwd) throws IOException
	{
		String strPageHTML = HttpRequest.post(sURL, "", "pwd="+pwd);
		HttpLogin.responseCookie = HttpRequest.getResponseCookie();
		return strPageHTML;
	}
	
	//返回页面
	public String viewPage(String strURL) throws IOException
	{
		String strPageHTML = HttpRequest.get(strURL, HttpLogin.responseCookie);
		return strPageHTML;
	}
	
	// 获取Cookie
	public String getCookie(){
		return HttpLogin.responseCookie;
	}
}