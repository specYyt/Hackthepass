package com.passwd.action.api;

import java.io.IOException;
import java.io.PrintWriter;

import com.alibaba.fastjson.JSONObject;
import com.passwd.action.SuperAction;
import com.passwd.dao.PersonalDao;
import com.passwd.dao.impl.PersonalDaoImpl;
import com.passwd.model.SearchResult;
import com.passwd.util.StringUtil;
import com.passwd.util.TokenUtil;

public class PersonalAction extends SuperAction {

	private static final long serialVersionUID = 1L;
	private String content;
	private String type;
	private boolean analyzed;
	private boolean quoted;
	private String token;// 用户token
	private String mode;
	private PersonalDao pd = new PersonalDaoImpl();

	// 基本搜索 API接口
	public void personalBasic() {
		if (!TokenUtil.getUsersTokens().contains(token) || StringUtil.isEmpty(content)) {
			return;
		}
		if(mode != null){
			switch (mode) {
			case "before":
				content += "*";
				break;
			case "after":
				content = "*" + content;
				break;
			case "none":
				content = "*" + content + "*";
				break;
			default:
				break;
			}
		}
		if (analyzed == true && quoted == true) {
			content = "\"" + content + "\"";
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SearchResult sr = pd.basic(content, analyzed, 0, 10000);
		writer.print(JSONObject.toJSONString(sr));
	}

	private String objectType;
	private String property;

	// 类型搜索API
	public void personalMetaType() {//|| StringUtil.isEmpty(property)
		if (StringUtil.isEmpty(content) || "-1".equals(objectType) ) {
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
		if(property == null) property = "";
		SearchResult sr = pd.metatype(content, analyzed, objectType, property, 0, 10000);
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

	public boolean isAnalyzed() {
		return analyzed;
	}

	public void setAnalyzed(boolean analyzed) {
		this.analyzed = analyzed;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean getQuoted() {
		return quoted;
	}

	public void setQuoted(boolean quoted) {
		this.quoted = quoted;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

}
