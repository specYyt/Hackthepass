/**
 * 
 */
package com.passwd.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.passwd.dao.DictDao;
import com.passwd.dao.impl.DictDaoImpl;
import com.passwd.model.Dictionary;

/**
 * @author Admin
 *
 */
public class DictAction extends SuperAction{

	
	
	private static final long serialVersionUID = 1L;
	private File dict;// 字典
	private String dictFileName;
	private String savePath;// 文件保存路径
	private DictDao dictDao = new DictDaoImpl();
	private List<Dictionary> dictList = new ArrayList<Dictionary>();
	
	// 上传字典
	public String uploadDict() {
		// 提交分析任务
		String name = uploadFile(dict);
		Dictionary dict = new Dictionary();
		dict.setName(dictFileName);
//		dict.setFilename(filename);
		dictDao.addDict(dict);
		return SUCCESS;
	}
	
	// 字典列表
	public String viewDict() {
		dictList = dictDao.getDictList();
		return SUCCESS;
	}
	
	// 删除字典
	public String deleteDict() {
		dictDao.deleteDict(dictFileName);
		String url = ServletActionContext.getServletContext().getRealPath(savePath) + File.separator + dictFileName
				+ ".txt";
		File file = new File(url);
		if (file.exists()) {
			file.delete();
		}
		return SUCCESS;
	}
	
	public String downloadDict() {
		return SUCCESS;
	}
	
	public InputStream getTargetFile() {
		String savePath = "dict";
		savePath += File.separator + dictFileName + ".txt";
		return ServletActionContext.getServletContext().getResourceAsStream(savePath);
	}
	
	public String uploadFile(File file) {
		final String uuid = UUID.randomUUID().toString();
		final String dstPath = ServletActionContext.getServletContext().getRealPath(this.getSavePath()) + File.separator
				+ uuid;
		try {
			FileUtils.copyFile(file, new File(dstPath + ".txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return uuid;
	}
	
	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	
	public String getDictFileName() {
		return dictFileName;
	}

	public void setDictFileName(String dictFileName) {
		this.dictFileName = dictFileName;
	}

	public File getDict() {
		return dict;
	}

	public void setDict(File dict) {
		this.dict = dict;
	}

	public List<Dictionary> getDictList() {
		return dictList;
	}

	public void setDictList(List<Dictionary> dictList) {
		this.dictList = dictList;
	}
	
}
