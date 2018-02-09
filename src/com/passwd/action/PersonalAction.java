package com.passwd.action;

import java.util.HashMap;
import java.util.Map;

import com.passwd.dao.PersonalDao;
import com.passwd.dao.impl.PersonalDaoImpl;
import com.passwd.dao.impl.SearchDaoImpl;
import com.passwd.model.SearchResult;
import com.passwd.util.StringUtil;

public class PersonalAction extends SuperAction {

	private static final long serialVersionUID = 1L;
	private String content;
	private String type;
	private boolean analyzed;
	private PersonalDao pd = new PersonalDaoImpl();
	private SearchDaoImpl search = new SearchDaoImpl();
	private int page;//当前页面
	private int lastpage;//最后一页
	private Map<Integer, Boolean> pageList = new HashMap<>();
	// 基本搜寻行为
	public String personalBasic() {
		if (StringUtil.isEmpty(content)) {
			return ERROR;
		}
		if(page < 1){
			page = 0;
		}
		
		SearchResult sr = pd.basic(content, analyzed, page*10000, (page+1)*10000);
		lastpage = sr.getTotalHits() / 10000;
		if(lastpage < 5) {
			for(int i = 0; i <= lastpage; i++) {
				if(page == i){
					pageList.put(i, true);
				}else{
					pageList.put(i, false);
				}
			}
		}
		if(lastpage > 5) {
			if(page == 0){
				pageList.put(1, true);
				pageList.put(2, false);
				pageList.put(3, false);
				pageList.put(4, false);
				pageList.put(5, false);
			}else{
				pageList.put(1, true);
				pageList.put(2, false);
				pageList.put(3, false);
				pageList.put(4, false);
				pageList.put(5, false);
			}
		}
		int uid = (int) session.getAttribute("com.passwd.userid");
		search.addSearchRecord(uid, "个人密码搜寻", content);// 记录搜索行为
		session.setAttribute("Perrno", sr.getErrno());
		session.setAttribute("Perrmsg", sr.getErrmsg());
		session.setAttribute("Ptime", (double)sr.getTime()/1000);
		session.setAttribute("PtotalHits", sr.getTotalHits());
		session.setAttribute("Presult", sr.getResults());
		return SUCCESS;
	}

	private String mode;

	// 模糊搜索
	public String personalFuzzy() {
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
			default:break;
		}
		SearchResult sr = pd.basic(content, true, 0, 10000);
		int uid = (int) session.getAttribute("com.passwd.userid");
		search.addSearchRecord(uid, "个人密码搜寻", content);// 记录搜索行为
		session.setAttribute("Perrno", sr.getErrno());
		session.setAttribute("Perrmsg", sr.getErrmsg());
		session.setAttribute("Ptime", (double)sr.getTime()/1000);
		session.setAttribute("PtotalHits", sr.getTotalHits());
		session.setAttribute("Presult", sr.getResults());
		return SUCCESS;
	}

	private String objectType;
	private String property;

	// 类型搜索
	public String personalMetaType() {
		if (StringUtil.isEmpty(content) || "-1".equals(objectType)) {
			return ERROR;
		}
		if (StringUtil.isEmpty(property)) {
			property = "";
		}

		SearchResult sr = pd.metatype(content, analyzed, objectType, property, 0, 10000);
		session.setAttribute("Perrno", sr.getErrno());
		session.setAttribute("Perrmsg", sr.getErrmsg());
		session.setAttribute("Ptime", (double)sr.getTime()/1000);
		session.setAttribute("PtotalHits", sr.getTotalHits());
		session.setAttribute("Presult", sr.getResults());
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

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLastpage() {
		return lastpage;
	}

	public void setLastpage(int lastpage) {
		this.lastpage = lastpage;
	}

	public Map<Integer, Boolean> getPageList() {
		return pageList;
	}

	public void setPageList(Map<Integer, Boolean> pageList) {
		this.pageList = pageList;
	}

}
