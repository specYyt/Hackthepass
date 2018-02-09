/**
 * 
 */
package com.passwd.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.passwd.model.SearchRecord;
import com.passwd.util.DBCPUtil;

/**
 * @author Admin
 *
 */
public class SearchDaoImpl {

	private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());
	
	//添加不重复的搜索记录
	public boolean addSearchRecord(int uid, String type, String query) {
		try {
			String sql = "insert into search_record(uid, type, query) values(?,?,?)";
			Object params[] = { uid, type, query};
			qr.update(sql, params);
		} catch (SQLException e) {
			return true;
		}
		return false;
	}
	
	//得到用户搜索记录数量
	public long getSearchCount(int uid) {
		String sql = "select count(*) from search_record where uid = ?";
		Object params[] = {uid};
		try {
			return (long) qr.query(sql, new ScalarHandler(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	//得到用户搜索记录
	public List<SearchRecord> getSearchRecords(int uid) {
		String sql = "select * from search_record where uid = ?";
		Object params[] = {uid};
		try {
			return  qr.query(sql, new BeanListHandler<SearchRecord>(SearchRecord.class), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	//清空用户搜索记录
	public void deleteAllSearch(int uid){
		String sql = "delete from search_record where uid = ?";
		Object params[] = {uid};
		try {
			 qr.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//删除用户单条记录
	public void deleteSearchRecord(int uid, int id){
		String sql = "delete from search_record where uid = ? and id = ?";
		Object params[] = {uid, id};
		try {
			 qr.update(sql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
