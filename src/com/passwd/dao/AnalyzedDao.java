/**
 * 
 */
package com.passwd.dao;

import java.util.List;

import com.passwd.model.AnalysisRec;

/**
 * @author Admin
 *
 */
public interface AnalyzedDao {

	/**
	 * 获得密码分析列表
	 */
	public List<AnalysisRec> getAnalysisRecList(int uid);
	/**
	 * 添加密码分析任务
	 */
	public void addAnalysis(AnalysisRec rec);
	/**
	 * 根据文件名删除密码分析任务
	 */
	public void deleteAnalysis(int uid, String filename);
	/**
	 * 更新密码分析任务状态
	 */
	public void updateAnalysisStatus(int status, String filename);
	/**
	 * 获得密码分析任务数量
	 */
	public long getAnalyzedCount(int uid);
	
	/**
	 * 获得未完成的密码分析任务
	 */
	public List<AnalysisRec> getUnfinishedAnalysis(int uid);
}
