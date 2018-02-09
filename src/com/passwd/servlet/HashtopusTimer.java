/**
 * 
 */
package com.passwd.servlet;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServlet;


/**
 * @author Admin
 *
 */
public class HashtopusTimer extends HttpServlet {


	private static final long serialVersionUID = 1L;

	public void init() {
		int time = 30;//默认为30秒
		try {
			time = Integer.valueOf(getInitParameter("interval"));
		}catch(Exception e) {
			System.out.println("定时任务设置的时间参数出错！");
		}
		Timer timer = new Timer();
		timer.schedule(new MyTask(), 5000, time*1000);
	}

	class MyTask extends TimerTask {

		@Override
		public void run() {
			//查询数据库，添加任务
			//查找complete = 0的数据中，min<max的任务 
			
			//变成1的情况
			//  already的任务破解出了结果
			//  already == maxLength
			System.out.println("dddd");
		}

	}
}
