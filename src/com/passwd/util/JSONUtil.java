package com.passwd.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.passwd.model.Result;

public class JSONUtil {

	public static String url;
	static {
		Properties ps = new Properties();
		try {
			ps.load(JSONUtil.class.getResourceAsStream("/control.properties"));
			String target = ps.getProperty("target");
			url = "http://" + target;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/*
	 * 把输入流转化为JSON
	 */
	private static String ConvertStream2Json(InputStream inputStream) {
		String jsonStr = "";
		// ByteArrayOutputStream相当于内存输出流
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		// 将输入流转移到内存输出流中
		try {
			while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, len);
			}
			// 将内存流转换为字符串
			jsonStr = new String(out.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}
	
	/*
	 * 通过任务ID得到任务结果
	 */
	public static List<Result> getTaskResult(String taskid) throws IOException{
		URL url2 = new URL(url + "/getTaskResults?Task_id=" +taskid);
		HttpURLConnection httpConn = (HttpURLConnection) url2.openConnection();
		// 设置连接属性
		httpConn.setConnectTimeout(3000);
		httpConn.setDoInput(true);
		httpConn.setRequestMethod("GET");
		// 获取相应码
		int respCode = httpConn.getResponseCode();
		String str = null;
		if (respCode == 200) {
			str = ConvertStream2Json(httpConn.getInputStream());
		}
		List<Result> results = new ArrayList<Result>();
		results = JSON.parseArray(str, Result.class);
		return results;
	}

	/*
	 * 根据任务ID得到任务进度
	 */
	public static String getTaskProgress(String task_id) throws IOException {
		
		URL url2 = new URL(url +"/getTaskProgress?Task_id="+ task_id);
		HttpURLConnection httpConn = (HttpURLConnection) url2.openConnection();
		// 设置连接属性
		httpConn.setConnectTimeout(3000);
		httpConn.setDoInput(true);
		httpConn.setRequestMethod("GET");
		// 获取相应码
		int respCode = httpConn.getResponseCode();
		String result = null;
		if (respCode == 200) {
			result = ConvertStream2Json(httpConn.getInputStream());
		}
		JSONObject object = JSON.parseObject(result);
		if(object == null){
			return "error";
		}
		if(object.getString("progress") != null){
			return object.getString("progress");
		}
		return "0";
	}
	
	/*
	 * 根据任务ID得到任务状态
	 */
	public static String getTaskStatus(String taskid) throws IOException {
		URL url2 = new URL(url +"/getTaskStatus?Task_id="+ taskid);
		HttpURLConnection httpConn = (HttpURLConnection) url2.openConnection();
		// 设置连接属性
		httpConn.setConnectTimeout(3000);
		httpConn.setDoInput(true);
		httpConn.setRequestMethod("GET");
		// 获取相应码
		int respCode = httpConn.getResponseCode();
		String result = null;
		if (respCode == 200) {
			result = ConvertStream2Json(httpConn.getInputStream());
		}
		JSONObject object = (JSONObject) JSON.parse(result);
		return object.getString("status");
	}
	
}
