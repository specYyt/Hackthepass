
package com.passwd.action;

import com.passwd.dao.HashcollisionDao;
import com.passwd.dao.impl.HashcollisionDaoImpl;
import com.passwd.dao.impl.SearchDaoImpl;
import com.passwd.model.SearchResult;
import com.passwd.util.StringUtil;

public class HashcollisionAction extends SuperAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String content;
	private String type;
	private HashcollisionDao hd = new HashcollisionDaoImpl();
	private SearchDaoImpl search = new SearchDaoImpl();
	
	// 基本搜寻行为
	public String ntmlBasic() {
		if (StringUtil.isEmpty(content)) {
			return ERROR;
		}
		SearchResult sr = hd.basic(content, false, 0, 10000);
		int uid = (int) session.getAttribute("com.passwd.userid");
		search.addSearchRecord(uid, "企业Hash碰撞", content);//记录搜索行为
		session.setAttribute("Herrno", sr.getErrno());
		session.setAttribute("Herrmsg", sr.getErrmsg());
		session.setAttribute("Htime", (double)sr.getTime()/1000);
		session.setAttribute("HtotalHits", sr.getTotalHits());
		session.setAttribute("Hresult", sr.getResults());
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

}
