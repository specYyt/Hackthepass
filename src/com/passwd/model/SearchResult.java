/**
 * 
 */
package com.passwd.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Admin
 *
 */
public class SearchResult {

	private String errmsg;// 错误信息
	private String errno;// 错误号

	private int totalHits;// 结果数量
	private int time;// 用时
	private List<Data> results = new ArrayList<Data>();//结果集

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getErrno() {
		return errno;
	}

	public void setErrno(String errno) {
		this.errno = errno;
	}

	public int getTotalHits() {
		return totalHits;
	}

	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public  List<Data> getResults() {
		return results;
	}

	public void setResults( List<Data> results) {
		this.results = results;
	}

}
