package com.passwd.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.passwd.dao.ServiceDao;
import com.passwd.dao.impl.ServiceDaoImpl;
import com.passwd.model.Service;
import com.passwd.util.SendUtil;

public class ReceiveAction extends SuperAction {

	private static final long serialVersionUID = 1L;

	private String type;// 服务类型
	private File target;// 目标文件
	private boolean order;
	private String custom;//自定义参数
	private File userdoc;// 用户名字典文件
	private File passdoc;// 密码字典文件
	private int split;
	private String ciscopass;
	private String savePath;// 文件保存路径
	private ServiceDao servicedao = new ServiceDaoImpl();

	// 后台通过HTTP请求发送任务信息到控制端
	public String execute() throws IOException {
		String task_id = UUID.randomUUID().toString();
		int userid = (Integer) session.getAttribute("com.passwd.userid");
		String targeturl = uploadFile(task_id, "targets", target);
		String userdocurl = uploadFile(task_id, "users", userdoc);
		String passdocurl = uploadFile(task_id, "pwds", passdoc);
		if(!order) {
			custom = "";
		}
		Service service = new Service(task_id, userid, type, targeturl, custom, userdocurl, passdocurl, split);
		service.setCiscopass(ciscopass== null?"":ciscopass);
		servicedao.addTask(service);// 存储任务信息
		SendUtil.sendTask(service);// 发送任务到控制端
		return SUCCESS;
	}

	public String uploadFile(String task_id, String extensions, File file) {
		String url = "";
		String dstPath = ServletActionContext.getServletContext().getRealPath(this.getSavePath());
		dstPath += File.separator + task_id + "." + extensions;
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		basePath += "download.action?fileName=" + task_id + "." + extensions;
		try {
			url = URLEncoder.encode(basePath, "utf-8");// 对url进行编码
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			FileUtils.copyFile(file, new File(dstPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return url;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public File getTarget() {
		return target;
	}

	public void setTarget(File target) {
		this.target = target;
	}

	public File getUserdoc() {
		return userdoc;
	}

	public void setUserdoc(File userdoc) {
		this.userdoc = userdoc;
	}

	public File getPassdoc() {
		return passdoc;
	}

	public void setPassdoc(File passdoc) {
		this.passdoc = passdoc;
	}

	public int getSplit() {
		return split;
	}

	public void setSplit(int split) {
		this.split = split;
	}

	public boolean getOrder() {
		return order;
	}

	public void setOrder(boolean order) {
		this.order = order;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public String getCiscopass() {
		return ciscopass;
	}

	public void setCiscopass(String ciscopass) {
		this.ciscopass = ciscopass;
	}

	
}
