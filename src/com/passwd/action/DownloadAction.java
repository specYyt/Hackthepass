
package com.passwd.action;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.ServletActionContext;

import com.passwd.util.TokenUtil;

public class DownloadAction extends SuperAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fileName;
	private String inputPath = "doc";
	private String token;
	private static final String reg = "[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}\\.(?=targets|users|pwds)";

	public InputStream getTargetFile() throws Exception {
		if (TokenUtil.getTokens().contains(token)) {
			Matcher m = Pattern.compile(reg).matcher(fileName);
			if (m.find()) {
				inputPath += File.separator + fileName;
			} else {
				inputPath += File.separator + "null.txt";
			}
		} else {
			inputPath += File.separator + "null.txt";
		}
		return ServletActionContext.getServletContext().getResourceAsStream(inputPath);
	}

	public String execute() {
		return SUCCESS;
	}

	public String getFileName() throws UnsupportedEncodingException {
		this.fileName = new String(fileName.getBytes(), "ISO-8859-1");
		return this.fileName;
	}

	public void setFileName(String fileName) {
		try {
			fileName = new String(fileName.getBytes("ISO-8859-1"), "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.fileName = fileName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
