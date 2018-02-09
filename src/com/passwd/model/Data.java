/**
 * 
 */
package com.passwd.model;

import java.io.Serializable;

/**
 * @author Admin
 *
 */
public class Data implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;//对象
	private String value;//对应的值
	
	public Data(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
