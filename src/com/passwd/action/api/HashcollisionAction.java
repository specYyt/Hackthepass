package com.passwd.action.api;

import java.io.IOException;
import java.io.PrintWriter;

import com.alibaba.fastjson.JSONObject;
import com.passwd.action.SuperAction;
import com.passwd.dao.HashcollisionDao;
import com.passwd.dao.impl.HashcollisionDaoImpl;
import com.passwd.model.SearchResult;
import com.passwd.util.StringUtil;
import com.passwd.util.TokenUtil;

public class HashcollisionAction extends SuperAction {

	private static final long serialVersionUID = 1L;
	private String content;
	private String type;
	private String token;// 用户token
	private HashcollisionDao hd = new HashcollisionDaoImpl();

	// 基本搜索API
	public void ntmlBasic() {
		if (!TokenUtil.getUsersTokens().contains(token) || StringUtil.isEmpty(content)) {
			return;
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SearchResult sr = hd.basic(content, false, 0, 10000);
		writer.print(JSONObject.toJSONString(sr));
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
