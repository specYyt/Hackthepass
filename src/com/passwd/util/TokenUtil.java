/**
 * 
 */
package com.passwd.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.log4j.Logger;

/**
 * @author Admin
 *
 */
public class TokenUtil {

	private static QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());
	private static final Logger logger = Logger.getLogger(TokenUtil.class);
	
	//获得控制端的Token
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<String> getTokens(){
		String sql = "select token  from control_token";
		try {
			return (List)qr.query(sql, new ColumnListHandler("token"));
		} catch (SQLException e) {
			logger.error("获取控制端Token出错：" + e.getMessage());
			return new ArrayList<>();
		}
	}
	
	//获得所有用户Token
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<String>getUsersTokens(){
		String sql = "select token  from user";
		try {
			return (List)qr.query(sql, new ColumnListHandler("token"));
		} catch (SQLException e) {
			logger.error("获取所有用户Token出错" + e.getMessage());
			return new ArrayList<>();
		}
	}
}
