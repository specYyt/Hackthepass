/**
 * 
 */
package com.passwd.interceptor;


import java.util.Map;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class LoginInterceptor extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public String intercept(ActionInvocation ai) throws Exception {
		// 取得跟踪用户的HTTP Session
	    Map<String, Object> session=ai.getInvocationContext().getSession();
	    int uid = (int) session.get("com.passwd.userid");
		 // 如果用户Session中userId属性为null，即用户还未登录
	    if(uid == 0){
			return Action.LOGIN;
	     }
		return ai.invoke();
	}

}
