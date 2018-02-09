package com.passwd.htp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.passwd.htp.crack.Agent;
import com.passwd.htp.crack.Hashlist;
import com.passwd.htp.crack.Task;
import com.passwd.htp.login.HttpLogin;
import com.passwd.htp.login.Server;
import com.passwd.htp.request.HttpRequest;
import com.passwd.model.CrackTask;

public class HtpUtil {
	private static Properties prop = new Properties();
	private static HttpLogin httpLogin;

	static{
		
		try {
			prop.load(HtpUtil.class.getResourceAsStream("htp.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String host = prop.getProperty("host");
		httpLogin = new HttpLogin(host, "/admin.php");
	}
	
	public static void main(String[] args) throws IOException {
		
		
		/*
		 * 首先判断是否登录, 未登录就执行登录。 此部分代码如无需更改可直接使用。
		 */
		String cookie;
		String ret;
		// Properties prop = new Properties();
		prop.load(HtpUtil.class.getResourceAsStream("htp.properties"));
		cookie = prop.getProperty("cookie"); 
		// host = prop.getProperty("host");
		Server.host = prop.getProperty("host");
		// 判断是否已登录
		do {
			cookie = prop.getProperty("cookie");
			if (cookie != null && cookie != "") {
				ret = HttpRequest.get(Server.host + "/admin.php?a=", cookie);// httpLogin.viewPage("http://www.htp.com/admin.php?a=agents");

				if (-1 < ret.indexOf("<input type=\"submit\" value=\"Login\"></form>")) {
					doLogin();
				} else {
					System.out.println("原Cookie生效!");
					break;
				}
			} else {
				doLogin();
			}
		} while (true);
		// 更新Cookie
		Server.cookie = cookie;
		System.out.println("更新完毕");
		/*
		 * 
		 * 下面是创建 Hashlist，即要破解的hash的列表，创建方式:0、从url获取;1、直接传值(字符串)
		 * 
		 */
		//
		Hashlist hashlist = new Hashlist();
		/*
		 * 方式0: ret = hashlist.create((byte) 0, cookie, "2016-6-29", 0, 0, 0,
		 * ':', "http://www.htp.com/files/hashlist.txt");
		 */
		// 方式1: 5100 是16位MD5，0是32位MD5。其他hash的代号详见hashcat用法
		//String url = "http://10.10.11.8:8080/hackthepass/hash/be194eeb-c08f-464b-b8d0-506cdfbea368.txt";
		String hash = "17f0019c64b042e4\n965eb72c92a549dd";
		ret = hashlist.create((byte) 0, cookie, "2017-8-24", 5100, 0, 0, ':', hash);
//		 System.out.println(ret);
		/*
		 * 
		 * 下面是创建 task
		 * 
		 */
		Task task = new Task();
		String a = "-a 3 #HL# ?l?l?l";
		ret = task.create(cookie, "task2017824", hashlist.getHashlistId(), a, 1200, 5, 0,
				"");
		task.getTaskId();
		// System.out.println(ret + "\n\n////////////" + cookie);
		// HttpRequest.get("http://www.htp.com/admin.php?a=logout",
		// httpLogin.getCookie());
		/*
		 * 
		 * 下面是 分配到Agents上，一个task应当只分配到一台机器上去。
		 * 
		 */
		Agent agent = new Agent();
		JSONArray jsonArrAgents = agent.getAgents(true);
		Iterator<Object> itr = jsonArrAgents.iterator();
		JSONObject jsonObjAgent;
		Date date = new Date();
		while (itr.hasNext()) {
			jsonObjAgent = (JSONObject) itr.next();
//			if(true) {
			if (date.getTime()/1000 - jsonObjAgent.getLong("lasttime") < 30) {
				task.assign(cookie, task.getTaskId(), jsonObjAgent.getIntValue("id"));
			} 
			else {
				System.out.println("Dead Hashtopus Node: " + jsonObjAgent.getIntValue("id"));
			}
		}

		/*
		 * 
		 * 下面获取任务34在各个Agents上进度
		 * 
		 */
		//JSONArray progresOnAgents = task.getProgressesOnAgents(cookie, 34);
		//System.out.println(progresOnAgents.toJSONString());

		/*
		 * 
		 * 下面获取任务34的总进度
		 * 
		 */
		//JSONObject progres = task.getProgress(cookie, 34);
		//System.out.println(progres.toJSONString());
		/*
		 * 
		 * 下面获取任务32结果
		 * 
		 */
		//JSONArray resJSONArray = task.getResult(32);
		//System.out.println(resJSONArray.toJSONString());
	}

	/*
	 * 登陆
	 */
	private static void doLogin() throws IOException {
		String password = prop.getProperty("password");
		HtpUtil.httpLogin.login("", password);
		String cookie = HtpUtil.httpLogin.getCookie();
		Server.cookie = cookie;
		prop.setProperty("cookie", cookie);
		FileOutputStream fos = new FileOutputStream(HtpUtil.class.getResource("htp.properties").getFile());

		// System.out.println(TestLogin.class.getResource("cookie.properties").getFile());
		prop.store(fos, null);
		fos.flush();
		fos.close();
	}

	/*
	 * 创建Hash任务
	 */
	public static int createHashTask(int method, String hashName, int hashtype, int salted, char split, String hashText,
			String param) throws IOException {
		int taskId;
		String cookie;
		String ret = null;
		try {
			prop.load(HtpUtil.class.getResourceAsStream("htp.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		cookie = prop.getProperty("cookie");
		Server.host = prop.getProperty("host");
		do {
			cookie = prop.getProperty("cookie");
			if (cookie != null && cookie != "") {
				try {
					ret = HttpRequest.get(Server.host + "/admin.php?a=", cookie);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	
				if (-1 < ret.indexOf("<input type=\"submit\" value=\"Login\"></form>")) {
					try {
						doLogin();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("原Cookie生效!");
					break;
				}
			} else {
				try {
					doLogin();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} while (true);
		Server.cookie = cookie;
		Hashlist hashlist = new Hashlist();
		hashlist.create((byte) method, cookie, hashName, hashtype, 0, salted, split, hashText);
//		Date date = new Date();
		Task task = new Task();
		ret = task.create(cookie, hashName, hashlist.getHashlistId(), param, 1200, 5, 0, "");
		taskId = task.getTaskId();
		Agent agent = new Agent();
		JSONArray jsonArrAgents = agent.getAgents(true);
		Iterator<Object> itr = jsonArrAgents.iterator();
		JSONObject jsonObjAgent;
		while (itr.hasNext()) {
			jsonObjAgent = (JSONObject) itr.next();
			task.assign(cookie, task.getTaskId(), jsonObjAgent.getIntValue("id"));
//			if (date.getTime()/1000 - jsonObjAgent.getLong("lasttime") < 60) {
//				task.assign(cookie, task.getTaskId(), jsonObjAgent.getIntValue("id"));
//			} else {
//				System.out.println("Dead Hashtopus Node: " + jsonObjAgent.getIntValue("id"));
//			}
		}
		
		return taskId;
	}
	
	public static LinkedHashMap<Integer, String> createHashTask1(int method, String hashName, int hashtype, int salted, char split, String hashText,
			String param) throws IOException {
		int taskId;
		String cookie;
		String ret = null;
		LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
		try {
			prop.load(HtpUtil.class.getResourceAsStream("htp.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		cookie = prop.getProperty("cookie");
		Server.host = prop.getProperty("host");
		do {
			cookie = prop.getProperty("cookie");
			if (cookie != null && cookie != "") {
				try {
					ret = HttpRequest.get(Server.host + "/admin.php?a=", cookie);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	
				if (-1 < ret.indexOf("<input type=\"submit\" value=\"Login\"></form>")) {
					try {
						doLogin();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println("原Cookie生效!");
					break;
				}
			} else {
				try {
					doLogin();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} while (true);
		Server.cookie = cookie;
		Hashlist hashlist = new Hashlist();
		hashlist.create((byte) method, cookie, hashName, hashtype, 0, salted, split, hashText);

		Task task = new Task();
		ret = task.create(cookie, hashName, hashlist.getHashlistId(), param, 1200, 5, 0, "");
		taskId = task.getTaskId();
//		Agent agent = new Agent();
//		JSONArray jsonArrAgents = agent.getAgents(true);
////		JSONArray jsonTaskProg = task.getProgressesOnAgents(cookie, taskId);
////		System.out.println(jsonTaskProg);
//		Iterator<Object> itr = jsonArrAgents.iterator();
//		JSONObject jsonObjAgent;
//		while (itr.hasNext()) {
//			jsonObjAgent = (JSONObject) itr.next();
//			task.assign(cookie, taskId, jsonObjAgent.getIntValue("id"));
//		}
		map.put(taskId, cookie);
		return map;
	}
	
	
	/*
	 * 获得任务总进度
	 */
	public static List<JSONObject>  getTaskProgress(List<CrackTask> list) throws  IOException{
		String cookie;
		String ret;
		prop.load(HtpUtil.class.getResourceAsStream("htp.properties"));
		cookie = prop.getProperty("cookie");
		Server.host = prop.getProperty("host");
		//do {
			cookie = prop.getProperty("cookie");
			if (cookie != null && cookie != "") {
				ret = HttpRequest.get(Server.host + "/admin.php?a=", cookie);// httpLogin.viewPage("http://www.htp.com/admin.php?a=agents");

				if (-1 < ret.indexOf("<input type=\"submit\" value=\"Login\"></form>")) {
					doLogin();
				} else {
					System.out.println("原Cookie生效!");
					//break;
				}
			} else {
				doLogin();
			}
		//} while (true);
		Server.cookie = cookie;
		Task task = new Task();
		List<JSONObject> result = new ArrayList<JSONObject>();
		for(CrackTask cracktask : list) {
			JSONObject progres = task.getProgress(cookie, cracktask.getTask_id());
			result.add(progres);
		}
		return result;
	}
	
	public static JSONObject getTaskProgress(int taskId) throws  IOException{
		String cookie;
		String ret;
		prop.load(HtpUtil.class.getResourceAsStream("htp.properties"));
		cookie = prop.getProperty("cookie");
		Server.host = prop.getProperty("host");
		//do {
			cookie = prop.getProperty("cookie");
			if (cookie != null && cookie != "") {
				ret = HttpRequest.get(Server.host + "/admin.php?a=", cookie);// httpLogin.viewPage("http://www.htp.com/admin.php?a=agents");

				if (-1 < ret.indexOf("<input type=\"submit\" value=\"Login\"></form>")) {
					doLogin();
				} else {
					System.out.println("原Cookie生效!");
					//break;
				}
			} else {
				doLogin();
			}
		//} while (true);
		Server.cookie = cookie;
		Task task = new Task();
			JSONObject progress = task.getProgress(cookie, taskId);
		return progress;
	}
	/*
	 * 获得任务结果
	 */
	public static String getCrackResult(int taskId) throws  IOException{
		String cookie;
		String ret;
		prop.load(HtpUtil.class.getResourceAsStream("htp.properties"));
		cookie = prop.getProperty("cookie");
		Server.host = prop.getProperty("host");
		//do {
			cookie = prop.getProperty("cookie");
			if (cookie != null && cookie != "") {
				ret = HttpRequest.get(Server.host + "/admin.php?a=", cookie);// httpLogin.viewPage("http://www.htp.com/admin.php?a=agents");
				if (-1 < ret.indexOf("<input type=\"submit\" value=\"Login\"></form>")) {
					doLogin();
				} else {
					System.out.println("原Cookie生效!");
					//break;
				}
			} else {
				doLogin();
			}
		//} while (true);
		Server.cookie = cookie;
		Task task = new Task();
		JSONArray resJSONArray = task.getResult(taskId);
		return resJSONArray.toJSONString();
	}
}
