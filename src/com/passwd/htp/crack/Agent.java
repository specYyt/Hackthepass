package com.passwd.htp.crack;

import java.io.IOException;
import java.net.MalformedURLException;


import com.alibaba.fastjson.*;
import com.passwd.htp.login.Server;
import com.passwd.htp.request.HttpRequest;

public class Agent {
	private int countActive = -1;
	private int countInactive = -1;
	
	public JSONArray getAgents(boolean active) throws MalformedURLException, IOException{
		String strUrl = Server.host + "/api/getAgents.php?active=" + active; //调用hashtopus接口，判断agent是否active
		String agentRes = HttpRequest.get(strUrl, Server.cookie);
		JSONArray jsonArr = JSONArray.parseArray(agentRes);
		if (true)
			this.countActive = jsonArr.size();
//		else
//			this.countInactive = jsonArr.size();
		return jsonArr;
		
	}
	public JSONArray getAgentsAll() throws MalformedURLException, IOException{
		JSONArray jsonArr;
		jsonArr = this.getAgents(true);
		jsonArr.addAll(this.getAgents(false));
		return jsonArr;
	}
	
	// 获取节点个数
	public int getCountActive() throws MalformedURLException, IOException{
		this.getAgents(true);
		return this.countActive;
	}
	//
	public int getCountInactive() throws MalformedURLException, IOException{
		this.getAgents(false);
		return this.countInactive;
	}
	//
	public int getCountAll() throws MalformedURLException, IOException{
		this.getAgentsAll();
		return this.countActive + this.countInactive;
	}
}
