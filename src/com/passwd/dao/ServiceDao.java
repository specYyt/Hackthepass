package com.passwd.dao;

import java.util.List;

import com.passwd.model.Service;

public interface ServiceDao {

	// 查找当前用户所有服务密码爆破任务
	public List<Service> findTaskList(int userid);

	// 添加任务
	public void addTask(Service service);

	// 获得服务类型
	public String getServiceTypes();

	// 更新任务状态
	public void updateTaskStatus(String string);

	// 获得任务数量
	public long getServiceCount(int uid);

	// 删除任务
	public void deleteService(String task_id, int uid);
}
