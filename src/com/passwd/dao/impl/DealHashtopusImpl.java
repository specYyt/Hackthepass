package com.passwd.dao.impl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;

import com.passwd.action.DBCPUtil1;
import com.passwd.dao.DealHashtopus;
public class DealHashtopusImpl implements DealHashtopus {
	
	private QueryRunner qr = new QueryRunner(DBCPUtil1.getDataSource());
	private final Logger logger = Logger.getLogger(DealHashtopusImpl.class);
	
	@Override
	public int getHashlist(int taskId) {
		String sql = "select hashlist from tasks where id = ?";
		int Hashlistid = 0;
		Object[] params = { taskId};
		try {
			 return (int) qr.query(sql, new ScalarHandler(), params);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return Hashlistid;
	}

	@Override
	public int getCracked(int Hashlistid) {
		String sql = "select cracked from hashlists where id = ?";
		int cracked = 0;
		Object[] params = { Hashlistid };
		try {
			return (int) qr.query(sql, new ScalarHandler(), params);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return cracked;
	}

	@Override
	public void delnoCracked(int taskId) {
		String sql = "delete from tasks where id = ?";
		Object[] params = { taskId };
		try {
			qr.update(sql, params);
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void crtPriority(int priority, int taskId) {
		String sql = "update tasks set priority = ? where id = ?";
		Object[] params = { priority, taskId};
		try {
			qr.update(sql, params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

	@Override
	public int getPriority() {
		String sql = "select priority from tasks order by id desc limit 1,1";
		int a = 0;
		try {
			a = (int) qr.query(sql, new ScalarHandler());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public long getKeyspace(int taskId) {
		String sql = "select keyspace from tasks where id = ?";
		long a = 0;
		Object[] params = { taskId };
		try {
			a = (long) qr.query(sql, new ScalarHandler(), params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

	@Override
	public long getProgress(int taskId) {
		String sql = "select progress from tasks where id = ?";
		long a = 0;
		Object[] params = { taskId };
		try {
			a = (long) qr.query(sql, new ScalarHandler(), params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}


	
}
