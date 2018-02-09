/**
 * 
 */
package com.passwd.action.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.passwd.action.SuperAction;
import com.passwd.dao.AnalyzedDao;
import com.passwd.dao.impl.AnalyzedDaoImpl;
import com.passwd.model.AnalysisRec;
import com.passwd.util.TokenUtil;

/**
 * @author Admin
 *
 */
public class AnalyzedAction extends SuperAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private AnalyzedDao ad = new AnalyzedDaoImpl();

	private int userid;
	private String token;

	// 密码分析列表API
	public void getAnalyzedList() {
		if (!TokenUtil.getUsersTokens().contains(token)) {
			return;
		}
		List<AnalysisRec> AnalysisList = ad.getAnalysisRecList(userid);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject object = new JSONObject();
		object.put("data", AnalysisList);
		writer.write(JSONObject.toJSONString(object));
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
