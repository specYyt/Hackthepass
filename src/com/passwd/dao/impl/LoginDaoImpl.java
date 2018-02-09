/**
 * 
 */
package com.passwd.dao.impl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;

import com.passwd.dao.LoginDao;
import com.passwd.model.User;
import com.passwd.util.DBCPUtil2;

/**
 * @author Admin
 *
 */
public class LoginDaoImpl implements LoginDao{

	private QueryRunner qr = new QueryRunner(DBCPUtil2.getDataSource());
	private final Logger logger = Logger.getLogger(LoginDaoImpl.class);
	
	@Override
	public String getPassword(int uid) {
		String password = "";
		try {
			String sql = "select password from users where id = ? ";// 取出该用户的salt
			Object[] params = { uid };
			password = (String) qr.query(sql, new ScalarHandler(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return password;
	}

	@Override
	public User getUser(int uid) {
		String sql = "select id,username, password,group_id from users where id = ?";
		Object[] params = { uid };
		try {
			return qr.query(sql, new BeanHandler<User>(User.class),params);
		} catch (SQLException e) {
			logger.error("根据id获得用户出错："+e.getMessage());
		}
		return new User();
	}

	
}
