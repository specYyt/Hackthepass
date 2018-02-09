package com.passwd.dao;

import com.passwd.model.User;

public interface UserDao {

	// 管理员登陆验证֤
	public boolean isLogin(User user);

	// 通过用户名获得用户
	public User getUser(String username);


	// 获得所有用户列表
	//public List<User> getUsers();

	// 根据ID删除用户
	//public void deleteUser(int uid);

	
	//public void addUser(User user);


	public User getUserByUid(int uid);
	
	//根据cookie获取用户，调用接口
	public int getUserByTok(String token);
	//public void updateUser(User user);

}
