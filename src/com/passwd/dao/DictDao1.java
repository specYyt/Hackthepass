/**
 * 
 */
package com.passwd.dao;

import java.util.List;

import com.passwd.model.Dictionary;

/**
 * @author Admin
 *
 */
public interface DictDao1 {

	/*
	 * 获得字典列表
	 */
	public List<Dictionary> getDictList( );
	/*
	 * 上传字典
	 */
	public boolean addDict(Dictionary dict);
	/*
	 * 根据文件名删除字典
	 */
	public void deleteDict(String filename);
	
}
