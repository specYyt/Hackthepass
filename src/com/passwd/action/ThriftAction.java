/**
 * 
 */
package com.passwd.action;

import java.io.IOException;
import java.io.PrintWriter;

import com.passwd.util.StringUtil;
import com.passwd.util.ThriftUtil;

/**
 * @author Admin
 *
 */
public class ThriftAction extends SuperAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	private String objectId;

	// 通过对象类型得到属性列表
	public void getProperties() {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String rs = ThriftUtil.getProptiesByObjectType(objectId);
		writer.print(rs);
	}

	// 得到对象类型
	public String getObjectTypes() {
		if (StringUtil.isEmpty(key)) {
			return null;
		}
		String dbname = "";
		String dbtype = "mysql";
		switch (key) {
		case "person":
			dbname = "pwds";
			break;
		case "enterprise":
			dbname = "pwds";
			break;
		case "ntml":
			dbname = "ntml";
			break;
		default:
			return null;
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String rs = ThriftUtil.getObjectTypeByDatabase(dbname, dbtype);
		writer.print(rs);
		return null;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

}
