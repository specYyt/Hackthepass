package com.passwd.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.passwd.dao.ServiceDao;
import com.passwd.dao.impl.ServiceDaoImpl;
import com.passwd.model.Result;
import com.passwd.model.Service;
import com.passwd.util.JSONUtil;
import com.passwd.util.SendUtil;

public class ServiceAction extends SuperAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Service> tasks = new ArrayList<Service>();
	private List<Result> results = new ArrayList<Result>();
	private ServiceDao sd = new ServiceDaoImpl(); 
	private String taskid;

	//服务密码爆破列表
	public void getSeriviceList() {
		ServiceDao servicedao = new ServiceDaoImpl();
		int userid = (Integer) session.getAttribute("com.passwd.userid");
		tasks = servicedao.findTaskList(userid);
		for (Service task : tasks) {
			try {
				task.setProgress(JSONUtil.getTaskProgress(task.getTask_id()));
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

	public String deleteService(){
		int userid = (Integer) session.getAttribute("com.passwd.userid");
		sd.deleteService(taskid, userid);
		return SUCCESS;
	}
	
	//任务结果
	public void getServiceResult() throws IOException {
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

	//任务类型
	public String getServiceTypes() {
		ServiceDao servicedao = new ServiceDaoImpl();
		String types = servicedao.getServiceTypes();
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.print(types);
		return null;
	}

	//停止服务
	public String stopService() throws IOException{
		SendUtil.stopService(taskid);
		return SUCCESS;
	}
	public List<Service> getTasks() {
		return tasks;
	}

	public void setTasks(List<Service> tasks) {
		this.tasks = tasks;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}
}
