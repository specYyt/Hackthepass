package com.passwd.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class Service {
	
	@JSONField(serialize=false)  
	private int id;
	private String task_id;//任务id
	private int userid;//用户id
	private String type;//服务类型
	private String target;// 目标文件的url
	private String custom;//自定义参数
	private String userdoc;// 用户文件的url
	private String passdoc;// 密码文件的url
	private String time;// 添加任务时间
	private String progress;
	private String status;//任务状态
	private int split;//分割
	private String ciscopass;//思科密码

	public Service(String task_id, int userid, String type, String target,String custom,
			String userdoc, String passdoc, int split) {
		super();
		this.task_id = task_id;
		this.userid = userid;
		this.type = type;
		this.target = target;
		this.custom = custom;
		this.userdoc = userdoc;
		this.passdoc = passdoc;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.time = df.format(new Date());
		this.split = split;
	}

	public Service() {

	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getUserdoc() {
		return userdoc;
	}

	public void setUserdoc(String userdoc) {
		this.userdoc = userdoc;
	}

	public String getPassdoc() {
		return passdoc;
	}

	public void setPassdoc(String passdoc) {
		this.passdoc = passdoc;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public int getSplit() {
		return split;
	}

	public void setSplit(int split) {
		this.split = split;
	}

	@Override
	public String toString() {
		return "Service [id=" + id + ", task_id=" + task_id + ", userid=" + userid + ", type=" + type + ", target="
				+ target + ", custom=" + custom + ", userdoc=" + userdoc + ", passdoc=" + passdoc + ", time=" + time
				+ ", status=" + status + ", split=" + split + "]";
	}

	public String getCiscopass() {
		return ciscopass;
	}

	public void setCiscopass(String ciscopass) {
		this.ciscopass = ciscopass;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

}
