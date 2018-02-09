/**
 * 
 */
package com.passwd.action;

import java.util.ArrayList;
import java.util.List;

import com.passwd.dao.AnalyzedDao;
import com.passwd.dao.ServiceDao;
import com.passwd.dao.impl.AnalyzedDaoImpl;
import com.passwd.dao.impl.SearchDaoImpl;
import com.passwd.dao.impl.ServiceDaoImpl;
import com.passwd.model.AnalysisRec;
import com.passwd.model.SearchRecord;
import com.passwd.model.Service;

/**
 * @author Admin
 *
 */
public class TotalAction extends SuperAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long searchCount;
	private long serviceCount;
	private long hashCount;
	private long analyzedCount;
	private List<AnalysisRec> analysisList = new ArrayList<AnalysisRec>();
	private List<Service> serviceList = new ArrayList<Service>();
	
	private AnalyzedDao ad = new AnalyzedDaoImpl();
	private ServiceDao sd = new ServiceDaoImpl();
	private SearchDaoImpl sdl = new SearchDaoImpl();
	List<SearchRecord> records = new ArrayList<>();

	public String execute() {
		int uid = (int) session.getAttribute("com.passwd.userid");
		analyzedCount = ad.getAnalyzedCount(uid);
		serviceCount = sd.getServiceCount(uid);
		searchCount = sdl.getSearchCount(uid);
		analysisList = ad.getUnfinishedAnalysis(uid);
		
		return SUCCESS;
	}

	//搜索记录列表
	public String viewSearch(){
		int uid = (int) session.getAttribute("com.passwd.userid");
		records = sdl.getSearchRecords(uid);
		return SUCCESS;
	}
	
	private int id;
	
	public String deleteSearchRecord(){
		int uid = (int) session.getAttribute("com.passwd.userid");
		sdl.deleteSearchRecord(uid, id);
		return SUCCESS;
	}
	
	public String deleteAllSearch(){
		int uid = (int) session.getAttribute("com.passwd.userid");
		sdl.deleteAllSearch(uid);
		return SUCCESS;
	}
	
	
	public long getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(long searchCount) {
		this.searchCount = searchCount;
	}

	public long getServiceCount() {
		return serviceCount;
	}

	public void setServiceCount(long serviceCount) {
		this.serviceCount = serviceCount;
	}

	public long getHashCount() {
		return hashCount;
	}

	public void setHashCount(long hashCount) {
		this.hashCount = hashCount;
	}

	public long getAnalyzedCount() {
		return analyzedCount;
	}

	public void setAnalyzedCount(long analyzedCount) {
		this.analyzedCount = analyzedCount;
	}

	public void setAnalyzedCount(int analyzedCount) {
		this.analyzedCount = analyzedCount;
	}

	public List<AnalysisRec> getAnalysisList() {
		return analysisList;
	}

	public void setAnalysisList(List<AnalysisRec> analysisList) {
		this.analysisList = analysisList;
	}

	public List<Service> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<Service> serviceList) {
		this.serviceList = serviceList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SearchRecord> getRecords() {
		return records;
	}

	public void setRecords(List<SearchRecord> records) {
		this.records = records;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}

