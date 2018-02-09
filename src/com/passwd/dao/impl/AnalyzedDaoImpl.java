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
import org.apache.log4j.Logger;

import com.passwd.dao.AnalyzedDao;
import com.passwd.model.AnalysisRec;
import com.passwd.util.DBCPUtil;

/**
 * @author Admin
 *
 */
public class AnalyzedDaoImpl implements AnalyzedDao {

	private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());
	private final Logger logger = Logger.getLogger(AnalyzedDaoImpl.class);


			public List<AnalysisRec> getAnalysisRecList(int uid) {
				List<AnalysisRec> list = new ArrayList<>();
				String sql = "select * from analyzsis where uid = ?";
				Object params[] = {uid};
				try {
					return qr.query(sql, new BeanListHandler<AnalysisRec>(AnalysisRec.class), params);
				} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void addAnalysis(AnalysisRec rec) {
		try {
			String sql = "insert into analyzsis(uid, name, filename,ctime) values(?,?,?,?)";
			Object params[] = { rec.getUid(), rec.getName(), rec.getFilename(), rec.getCtime()};
			qr.update(sql, params);
		} catch (SQLException e) {
			logger.error("添加密码分析任务出错：" + e.getMessage());
		}
	}

	public void deleteAnalysis(int uid, String filename) {
		try {
			String sql = "delete from analyzsis where uid = ? and filename = ?";
			Object params[] = { uid, filename };
			qr.update(sql, params);
		} catch (SQLException e) {
			logger.error("删除密码分析出错：" + e.getMessage());
		}
	}
	
	public void updateAnalysisStatus(int status,String filename){
		try {
			String sql = "update  analyzsis set status = ? where filename = ?";
			Object params[] = { status, filename };
			qr.update(sql, params);
		} catch (SQLException e) {
			logger.error("更新密码分析任务状态出错：" + e.getMessage());
		}
	}

	
	public long getAnalyzedCount(int uid) {
		String sql = "select count(*) from analyzsis where uid = ?";
		Object params[] = {uid};
		try {
			return (long) qr.query(sql, new ScalarHandler(), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<AnalysisRec> getUnfinishedAnalysis(int uid) {
		List<AnalysisRec> list = new ArrayList<>();
		String sql = "select * from analyzsis where uid = ? and status = 0";
		Object params[] = {uid};
		try {
			return qr.query(sql, new BeanListHandler<AnalysisRec>(AnalysisRec.class), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
