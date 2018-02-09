package com.passwd.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;

import com.passwd.action.DBCPUtil11;
import com.passwd.dao.CrackHashDao;
import com.passwd.model.CrackTask;
import com.passwd.model.Dictionary;
import com.passwd.model.HashType;
import com.passwd.util.DBCPUtil;

/**
 * @author Admin
 *
 */
public class CrackHashDaoImpl implements CrackHashDao{

	private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());
	private final Logger logger = Logger.getLogger(CrackHashDaoImpl.class);
	
	
	public List<HashType>getHashtypes(){
		List<HashType> types = new ArrayList<>();
		String sql = "select * from hashtype";
		try {
			types= qr.query(sql, new BeanListHandler<HashType>(HashType.class));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return types;
	}
	
	public void addCrackTask(int userid, int taskId) {
		try {
			String sql = "insert into crackhash(uid, task_id) values(?,?)";
			Object params[] = { userid, taskId,};
			qr.update(sql, params);
		} catch (SQLException e) {
			logger.error("添加Hash破解任务出错：" + e.getMessage());
		}
	}

	public List<CrackTask> getCrackTasks(int userid) {
		List<CrackTask> types = new ArrayList<>();
		String sql = "select * from crackhash where uid = ? order by c_time desc";
		Object[] params = {userid };
		try {
			types= qr.query(sql, new BeanListHandler<CrackTask>(CrackTask.class), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return types;
	}
	@Override
	public CrackTask getCrackTask(int userid, int taskId) {
		CrackTask type = null;
		String sql = "select * from crackhash where userid = ? && task_id = ?";
		Object[] params = { userid,taskId };
		try {
			type = qr.query(sql, new BeanHandler<CrackTask>(CrackTask.class), params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return type;
	}


	
	@Override
	public void addCrackTask(int userid, int minLength, int maxLength, String charset) {
		
		
	}

	@Override
	public void delCrackTask(int userid, int taskId) {
		String sql = "delete from crackhash where uid = ? and taskId = ?";
		Object[] params = {userid, taskId };
		try {
			qr.update(sql, params);
		}catch (SQLException e) {
			logger.error("删除crackhash里面的任务出错。");
		}
	}

	@Override
	public void delnocracked(int taskId) {
		String sql = "delete from crackhash where task_id = ?";
		Object[] params = {taskId};	
		try {
			qr.update(sql, params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
}
