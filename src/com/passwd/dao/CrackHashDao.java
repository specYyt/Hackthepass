package com.passwd.dao;

import java.util.List;

import com.passwd.model.CrackTask;
import com.passwd.model.HashType;

public interface CrackHashDao {

	/**
	 * 获得所有Hash类型
	 */
	public List<HashType>getHashtypes();

	/**
	 * 添加Hash破解任务
	 */
	public void addCrackTask(int userid, int taskId);
	
	/**
	 * 获得所有Hash破解任务
	 */
	public List<CrackTask> getCrackTasks(int userid);
	
	/**
	 * 获得指定Hash破解任务
	 */
	public CrackTask getCrackTask(int userid, int taskId);
	
	/**
	 * 删除Hash破解任务
	 */	
	public void delCrackTask(int userid, int taskId);
	
	/**
	 * @param userid 	用户ID
	 * @param minLength  密码最小长度
	 * @param maxLength  密码最大长度
	 * @param charset   
	 * @author Admin
	 */
	public void addCrackTask(int userid, int minLength, int maxLength, String charset);
	
	/**
	 * 当某一个任务的cracked为1是，删除另外几个没有破解出来的在crackhash里的taskid
	 */
	public void delnocracked(int taskId);
}
