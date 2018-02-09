/**
 * 
 */
package com.passwd.action.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.passwd.action.SuperAction;
import com.passwd.dao.ServiceDao;
import com.passwd.dao.impl.ServiceDaoImpl;
import com.passwd.model.Result;
import com.passwd.model.Service;
import com.passwd.util.JSONUtil;
import com.passwd.util.StringUtil;
import com.passwd.util.TokenUtil;

/**
 * @author Admin
 *
 */
public class ServiceAction extends SuperAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int userid;
	private String token;
	private String taskid;
	
	public void getServiceList() {
		if (!TokenUtil.getUsersTokens().contains(token) || StringUtil.isEmpty(taskid)) {
			return;
		}
		List<Service> tasks = new ArrayList<Service>();
		ServiceDao servicedao = new ServiceDaoImpl();
		tasks = servicedao.findTaskList(userid);
		for (Service task : tasks) {
			try {
				task.setStatus(JSONUtil.getTaskStatus(task.getTask_id()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject object = new JSONObject();
		object.put("data", tasks);
		writer.write(JSONObject.toJSONString(object));
	}

	private List<Result> results = new ArrayList<Result>();
	
	public void getServiceResult() throws IOException{
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		results = JSONUtil.getTaskResult(taskid);
		JSONObject object = new JSONObject();
		object.put("data", results);
		writer.write(JSONObject.toJSONString(object));
	}
	
	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

}
