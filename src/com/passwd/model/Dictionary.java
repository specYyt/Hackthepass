/**
 * 
 */
package com.passwd.model;

/**
 * @author Admin
 *
 */
public class Dictionary {

	private int id;
	private String name;
	private String path;
	private String filename;
	private String c_time;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getC_time() {
		return c_time;
	}
	public void setC_time(String c_time) {
		this.c_time = c_time;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
