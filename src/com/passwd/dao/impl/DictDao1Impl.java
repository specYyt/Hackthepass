/**
 * 
 */
package com.passwd.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import com.passwd.dao.DictDao1;
import com.passwd.model.Dictionary;
import com.passwd.util.DBCPUtil;

/**
 * @author Admin
 *
 */
public class DictDao1Impl implements DictDao1 {

	private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());
	private final Logger logger = Logger.getLogger(DictDao1Impl.class);
	
	@Override
	public List<Dictionary> getDictList() {
		 List<Dictionary> list = new ArrayList<>();
			String sql = "select id,name, filename,c_time from dict";
			try {
				return qr.query(sql, new BeanListHandler<Dictionary>(Dictionary.class));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return list;
	}


	@Override
	public boolean addDict(Dictionary dict) {
		try {
			String sql = "insert into dict(name, filename) values(?,?)";
			Object params[] = { dict.getName(), dict.getFilename()};
			qr.update(sql, params);
		} catch (SQLException e) {
			logger.error("添加字典出错：" + e.getMessage());
		}
		return false;
	}

	
	@Override
	public void deleteDict(String filename) {
		try {
			String sql = "delete from dict where filename = ?";
			Object params[] = { filename };
			qr.update(sql, params);
		} catch (SQLException e) {
			logger.error("删除字典出错：" + e.getMessage());
		}
	}

}
