package com.passwd.action;

import com.passwd.dao.EnterpriseDao;
import com.passwd.dao.impl.EnterpriseDaoImpl;
import com.passwd.dao.impl.SearchDaoImpl;
import com.passwd.model.SearchResult;
import com.passwd.util.StringUtil;

public class EnterpriseAction extends SuperAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String content;
	private String type;
	private boolean analyzed;
	private EnterpriseDao ed = new EnterpriseDaoImpl();
	private SearchDaoImpl search = new SearchDaoImpl();
	// 基本搜寻行为
	public String enterpriseBasic() {
		if (StringUtil.isEmpty(content)) {
			return ERROR;
		}
		SearchResult sr = ed.basic(content, analyzed, 0, 10000);
		int uid = (int) session.getAttribute("com.passwd.userid");
		search.addSearchRecord(uid, "企业密码搜寻", content);//记录搜索行为
		session.setAttribute("Eerrno", sr.getErrno());
		session.setAttribute("Eerrmsg", sr.getErrmsg());
		session.setAttribute("Etime", (double)sr.getTime()/1000);
		session.setAttribute("EtotalHits", sr.getTotalHits());
		session.setAttribute("Eresult", sr.getResults());
		return SUCCESS;
	}

	private String mode;
	// 模糊搜索
	public String enterpriseFuzzy() {
		if (StringUtil.isEmpty(content) || StringUtil.isEmpty(mode)) {
			return ERROR;
		}
		switch (mode) {
			case "before":
				content += "*";
				break;
			case "after":
				content = "*" + content ;
				break;
			case "none":
				content = "*" + content +"*";
				break;
			default:
				return ERROR;
		}
		SearchResult sr = ed.basic(content, true, 0, 10000);
		int uid = (int) session.getAttribute("com.passwd.userid");
		search.addSearchRecord(uid, "企业密码搜寻", content);// 记录搜索行为
		session.setAttribute("Eerrno", sr.getErrno());
		session.setAttribute("Eerrmsg", sr.getErrmsg());
		session.setAttribute("Etime", (double)sr.getTime()/1000);
		session.setAttribute("EtotalHits", sr.getTotalHits());
		session.setAttribute("Eresult", sr.getResults());
		return SUCCESS;
	}
	
	private String objectType;
	private String property;

	// 类型搜索
	public String enterpriseMetaType() {
		if (StringUtil.isEmpty(content)) {
			return ERROR;
		}
		SearchResult sr = ed.metatype(content, analyzed, objectType, property, 0, 10000);
		int uid = (int) session.getAttribute("com.passwd.userid");
		search.addSearchRecord(uid, "企业密码搜寻", content);//记录搜索行为
		session.setAttribute("Eerrno", sr.getErrno());
		session.setAttribute("Eerrmsg", sr.getErrmsg());
		session.setAttribute("Etime", (double)sr.getTime()/1000);
		session.setAttribute("EtotalHits", sr.getTotalHits());
		session.setAttribute("Eresult", sr.getResults());
		return SUCCESS;
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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

}
