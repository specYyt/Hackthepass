package com.passwd.dao.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.passwd.dao.ServiceDao;
import com.passwd.model.Service;
import com.passwd.model.ServiceType;
import com.passwd.util.DBCPUtil;

public class ServiceDaoImpl implements ServiceDao {

	private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());
	private final Logger logger = Logger.getLogger(ServiceDaoImpl.class);

	// 根据用户查找所有提交的任务
	public List<Service> findTaskList(int userid) {
		String sql = "select task_id,type,time,status from service where userid =? order by time desc";
		Object params[] = { userid };
		try {
			return qr.query(sql, new BeanListHandler<Service>(Service.class), params);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("查找所有服务密码爆破任务出错:" + e.getMessage());
			return new ArrayList<>();
		}
	}

	// 提交任务
	public void addTask(Service service) {
		try {
			String sql = "insert into service(userid,type,target,custom,userdoc,passdoc,time,task_id,status,split) values(?,?,?,?,?,?,?,?,?,?)";
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String join_time = df.format(new Date());
			Object params[] = { service.getUserid(), service.getType(), service.getTarget(), service.getCustom(),
					service.getUserdoc(), service.getPassdoc(), join_time, service.getTask_id(), "准备开始",
					service.getSplit() };
			qr.update(sql, params);
		} catch (SQLException e) {
			logger.error("添加服务密码爆破任务出错:" + e.getMessage());
		}
	}

	// 修改任务状态
	public void updateTaskStatus(String service) {
		try {
			String sql = "update  service set status = ?";
			Object params[] = { service };
			qr.update(sql, params);
		} catch (SQLException e) {
			logger.error("修改任务状态出错:" + e.getMessage());
		}
	}

	// 获得服务类型列表 JSON
	public String getServiceTypes() {
		JSONArray array = new JSONArray();
		try {
			String sql = "select * from  service_type";
			List<ServiceType> result = qr.query(sql, new BeanListHandler<ServiceType>(ServiceType.class));
			for (ServiceType st : result) {
				JSONObject object = new JSONObject();
				object.put("type", st.getType());
				object.put("value", st.getValue());
				array.add(object);
			}
		} catch (SQLException e) {
			logger.error("获取服务类型出错:" + e.getMessage());
		}
		return array.toJSONString();
	}


	public long getServiceCount(int uid) {
		String sql = "select count(*) from service where userid = ?";
		Object params[] = { uid };
		try {
			return (long) qr.query(sql, new ScalarHandler(), params);
		} catch (SQLException e) {
			logger.error("获取用户任务数量出错:" + e.getMessage());
		}
		return 0;
	}
	
	public void deleteService(String task_id, int uid){
		try {
			String sql = "delete  from service where  task_id = ? and userid = ?";
			Object params[] = { task_id, uid };
			qr.update(sql, params);
		} catch (SQLException e) {
			logger.error("删除任务出错:" + e.getMessage());
		}
	}
}
