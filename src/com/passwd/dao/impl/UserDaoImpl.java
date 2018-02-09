package com.passwd.dao.impl;

import java.sql.SQLException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;

import com.passwd.dao.UserDao;
import com.passwd.exception.MyException;
import com.passwd.model.User;
import com.passwd.util.DBCPUtil;
import com.passwd.util.DBCPUtil2;
import com.passwd.util.MD5Util;

public class UserDaoImpl implements UserDao {

	private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());
	private final Logger logger = Logger.getLogger(UserDaoImpl.class);
	
	// 登陆验证
	@Override
	public boolean isLogin(User user) {
		String password = null;
		String salt = null;
		User u = null;
		try {
			String sql = "select salt from user where username=?";// 取出该用户的salt
			Object[] params = { user.getUsername() };
			salt = (String) qr.query(sql, new ScalarHandler(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (salt == null)
			return false;
		try {
			password = MD5Util.md5Encode(salt + user.getPassword());// 使用md5对salt+密码进行加密
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			String sql = "select uid from user where username=? and password=?";
			Object[] params = { user.getUsername(), password };
			u = qr.query(sql, new BeanHandler<User>(User.class), params);
		} catch (SQLException e) {
			logger.error("登陆出错："+e.getMessage());
		}
		if (u != null)
			return true;
		return false;
	}

	// 通过用户名获得用户
	public User getUser(String username) {
		String sql = "select * from user where username = ?";
		Object[] params = { username };
		try {
			return qr.query(sql, new BeanHandler<User>(User.class),params);
		} catch (SQLException e) {
			throw new MyException("根据用户名获得用户出错");
		}
	}


//	@Override
//	public List<User> getUsers() {
//		String sql = "select * from user";
//		try {
//			return qr.query(sql, new BeanListHandler<User>(User.class));
//		} catch (SQLException e) {
//			logger.error("获得用户列表出错："+e.getMessage());
//		}
//		return new ArrayList<>();
//	}

	
//	@Override
//	public void deleteUser(int uid) {
//		try {
//			String sql = "delete from user where uid = ?";
//			Object params[] = { uid };
//			qr.update(sql, params);
//		} catch (SQLException e) {
//			logger.error("删除用户出错："+e.getMessage());
//		}
//	}

	
//	@Override
//	public void addUser(User user) {
//		try {
//			String sql = "insert into user(username,name,password,salt,Tuid,token, ban,competence)"
//					+ " values(?,?,?,?,?,?,?,?)";
//			Object params[] = { user.getUsername(), user.getName(), user.getPassword(), user.getSalt(), user.getTuid(),
//					user.getToken(), user.getBan(), user.getCompetence()};
//			qr.update(sql, params);
//		} catch (SQLException e) {
//			e.printStackTrace();
//			logger.error("添加用户出错：" + e.getMessage());
//		}
//		
//	}

	@Override
	public User getUserByUid(int uid) {
		String sql = "select * from user where uid = ?";
		Object[] params = { uid };
		try {
			return qr.query(sql, new BeanHandler<User>(User.class),params);
		} catch (SQLException e) {
			logger.error("根据ID获得用户出错："+e.getMessage());
		}
		return new User();
	}
	
	public int getUserByTok(String token) {
		String sql = "select uid from user where token = ?";
		Object[] params = { token };
		int uid = 0;
		try {
			uid = (int) qr.query(sql, new ScalarHandler(), params);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uid;
		
	}


//	@Override
//	public void updateUser(User user) {
//		try {
//			String sql = "update user set username = ? , name = ?, Tuid = ?, "
//					+ "competence = ?, ban = ? where uid = ?";
//			Object params[] = { user.getUsername(), user.getName(), user.getTuid(), user.getCompetence(),
//					user.getBan(), user.getUid()};
//			qr.update(sql, params);
//		} catch (SQLException e) {
//			logger.error("更新用户出错："+e.getMessage());
//		}
//	}
	
}
