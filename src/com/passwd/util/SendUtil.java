package com.passwd.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import com.alibaba.fastjson.JSON;
import com.passwd.action.ReceiveAction;
import com.passwd.dao.ServiceDao;
import com.passwd.dao.impl.ServiceDaoImpl;
import com.passwd.model.Service;

/**
 * @author Admin
 *
 */
@SuppressWarnings("deprecation")
public class SendUtil {

	private static String targetURL;// 控制端URL
	private static ServiceDao sd = new ServiceDaoImpl();

	/*
	 * 读取控制端的URL
	 */
	static {
		Properties ps = new Properties();
		try {
			ps.load(ReceiveAction.class.getResourceAsStream("/control.properties"));
			targetURL = ps.getProperty("target");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 发送任务到控制端
	 */
	public static void sendTask(Service service) throws IOException {
		String control = "http://" + targetURL + "/receive.action";
		@SuppressWarnings({ "resource" })
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost post = postForm(control , service);
		HttpResponse response = httpclient.execute(post);   
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			sd.updateTaskStatus("success");
		}else{
			sd.updateTaskStatus("error");
		}
	}
	
	/*
	 * 封装Service对象到POST请求体
	 */
	private static HttpPost postForm(String url, Service service){  
        
        HttpPost httpost = new HttpPost(url);  
        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
        nvps.add(new BasicNameValuePair("param", JSON.toJSONString(service)));
        try {  
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        return httpost;  
    } 

	/*
	 * 发送停止命令到控制端
	 */
	public static void stopService(String taskid) throws IOException {
		String control = "http://" + targetURL + "/stopTask.action";
		@SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(control);  
		List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
	    nvps.add(new BasicNameValuePair("Task_id", taskid));
	    httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
		HttpResponse response = httpclient.execute(httpost);   
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			sd.updateTaskStatus("已停止");
		}
	}
}
