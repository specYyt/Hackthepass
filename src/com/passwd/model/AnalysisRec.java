/**
 * 
 */
package com.passwd.model;

/**
 * @author Admin
 *
 */
public class AnalysisRec {

	private int id;
	private int uid;
	private String name;
	private String filename;
	private String ctime;
	private int status;
	
	public AnalysisRec(int uid, String name, String filename, String ctime){
		this.uid= uid;
		this.name = name;
		this.filename = filename;
		this.ctime = ctime;
		this.status = 0;
	}

	public AnalysisRec(int id, int uid, String filename, String ctime) {
		super();
		this.id = id;
		this.uid = uid;
		this.filename = filename;
		this.ctime = ctime;
	}

	public AnalysisRec(){
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
