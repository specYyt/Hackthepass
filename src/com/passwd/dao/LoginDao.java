/**
 * 
 */
package com.passwd.dao;

import com.passwd.model.User;

/**
 * @author Admin
 *
 */
public interface LoginDao {

	

	// 获得密码
	public String getPassword(int uid) ;


	public User getUser(int uid);
	
	
}
