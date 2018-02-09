package com.passwd.htp.crack;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.passwd.htp.login.Server;
import com.passwd.htp.request.HttpRequest;

public class Task {
	private int task_id;
	// 创建任务
	public String create(String cookie, String taskName, int hashlist_id, String cmdline, int chunk, int status, int autoadjust, String color) throws MalformedURLException, IOException, IOException{
		
		String strUrl = Server.host + "/admin.php?a=newtaskp";
//		StringBuilder sb = new StringBuilder();
//		sb.append("name=" + name);
//		sb.append("&hashlist=" + hashlist_id);
//		sb.append("&cmdline=" + cmdline);
//		sb.append("&chunk=" + chunk);
//		sb.append("&status=" + status);
//		sb.append("&autoadjust=" + autoadjust);
//		sb.append("&color=" + color);//http://www.htp.com/files/hashlist.txt
		
		LinkedHashMap<String,String> params = new LinkedHashMap<String, String>();
		params.put("name", taskName);
		params.put("hashlist", Integer.toString(hashlist_id));
		params.put("cmdline", String.valueOf(cmdline));
		params.put("chunk", Integer.toString(chunk));
		params.put("status", Integer.toString(status));
		params.put("autoadjust", Integer.toString(autoadjust));
		params.put("color", color);
		
		String res = HttpRequest.post(strUrl, cookie, params);
		
		String[] arrLine = res.split("\n");
		Pattern p = Pattern.compile("OK \\(id: ([\\d]+)\\)<br>");
		Matcher m;
		for (int i = 0; i < arrLine.length; i++){
			m = p.matcher(arrLine[i]);
			if (m.find()){
				this.task_id = Integer.parseInt(m.group(1));
				break;
			}
		}
		return res;
	}
	
	// 分配任务
	public void assign(String cookie, int task, int agent) throws MalformedURLException, IOException{
		String strUrl = Server.host + "/admin.php?a=agentassign";
		String data = "task=" + task + "&agent=" + agent;
		String ret = HttpRequest.post(strUrl, cookie, data);
	}
	
	// 获取任务当前在各个Agents上的进度
	public JSONArray getProgressesOnAgents(String cookie, int taskId) throws MalformedURLException, IOException{
		String strUrl = Server.host + "/api/getProgressesOnAgents.php?task=" + taskId;
		String hashesRes = HttpRequest.get(strUrl, Server.cookie);
		JSONArray jsonArr = JSONArray.parseArray(hashesRes);
		return jsonArr;
	}
	
	// 获取任务总进度
	public JSONObject getProgress(String cookie, int taskId) {
		JSONObject jsonObj = null;
		try{
			String strUrl = Server.host + "/api/getProgresses.php?task=" + taskId;
			String hashesRes = HttpRequest.get(strUrl, Server.cookie);
			jsonObj = JSONObject.parseObject(hashesRes);
		}catch(Exception e){
			return jsonObj;
		}
		return jsonObj;
	}
	
	// 获取结果
	public JSONArray getResult(int taskId) throws MalformedURLException, IOException{
		String strUrl = Server.host + "/api/getHashes.php?task=" + taskId;
		String hashesRes = HttpRequest.get(strUrl, Server.cookie);
		JSONArray jsonArr = JSONArray.parseArray(hashesRes);
		return jsonArr;
	}
	public int getTaskId(){
		return this.task_id;
	}
}
