/**
 * 
 */
package com.passwd.dao.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.passwd.dao.DictDao;
import com.passwd.model.Dictionary;
import com.passwd.util.DBCPUtil;

/**
 * @author Admin
 *
 */
public class DictDaoImpl implements DictDao {

	private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());
	private final Logger logger = Logger.getLogger(DictDaoImpl.class);
	
	@Override
	public List<Dictionary> getDictList() {
		 List<Dictionary> dicts = new ArrayList<>();
			String sql = "select id,name,path from dictionary";
			try {
				dicts = qr.query(sql, new BeanListHandler<Dictionary>(Dictionary.class));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return dicts;
	}

	@Override
	public boolean addDict(Dictionary dict) {
		try {
			String sql = "insert into dictionary(name, path) values(?,?)";
			Object params[] = { dict.getName(), dict.getPath()};
			qr.update(sql, params);
		} catch (SQLException e) {
			logger.error("添加字典出错：" + e.getMessage());
		}
		return false;
	}

	
	@Override
	public void deleteDict(String filename) {
		try {
			String sql = "delete from dictionary where name = ?";
			Object params[] = { filename };
			qr.update(sql, params);
		} catch (SQLException e) {
			logger.error("删除字典出错：" + e.getMessage());
		}
	}

}
