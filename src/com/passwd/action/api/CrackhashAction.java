package com.passwd.action.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.passwd.action.SuperAction;
import com.passwd.dao.CrackHashDao;
import com.passwd.dao.DealHashtopus;
import com.passwd.dao.impl.CrackHashDaoImpl;
import com.passwd.dao.impl.DealHashtopusImpl;
import com.passwd.dao.impl.UserDaoImpl;
import com.passwd.htp.HtpUtil;
import com.passwd.htp.crack.Agent;
import com.passwd.htp.crack.Task;
import com.passwd.model.CrackTask;
import com.passwd.model.User;

public class CrackhashAction extends SuperAction {
	private static final long serialVersionUID = 1L;
	public class InnerCrackhashAction extends TimerTask{
		LinkedHashMap<Integer, Integer> taskcrack = new LinkedHashMap<Integer, Integer>();

		public LinkedList<Integer> taskIdlist;
		public LinkedList<String> cookielist;
		Timer t;
		public InnerCrackhashAction() {
			
		}
		
		public InnerCrackhashAction(LinkedList<Integer> taskIdlist, LinkedList<String> cookielist, Timer t) {
			this.taskIdlist = taskIdlist;
			this.cookielist = cookielist;
			this.t = t;
		}

		@Override
		public void run() {
			for(int j = 0; j <taskIdlist.size(); j++) {
				System.out.println("传递过来的taskId个数:"+ taskIdlist.size());
				System.out.println( "每一个taskId的值:"+taskIdlist.get(j));
				int hashlistid = dht.getHashlist(taskIdlist.get(j));
				int cracked = dht.getCracked(hashlistid);
				taskcrack.put(taskIdlist.get(j), cracked);
			}
			
			Iterator<Map.Entry<Integer, Integer>> iterator = taskcrack.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Integer, Integer> entry = iterator.next();
				System.out.println(entry.getKey() +":"+entry.getValue());  //TO-DO delete
				if(entry.getValue() == 1 ) {
					System.out.println("应该保留的taskId(破解成功的那个):"+ entry.getKey());
					for(int j = entry.getKey() - 1; j >= taskIdlist.getFirst(); j--) {
						dht.delnoCracked(j);
						chd.delnocracked(j);
					}

					for(int k =entry.getKey() + 1; k <= taskIdlist.getLast(); k++) {
						dht.delnoCracked(k);
						chd.delnocracked(k);
					}
					t.cancel();
			}	
			}
			long keyspace = dht.getKeyspace(taskIdlist.getLast());
			long progress = dht.getProgress(taskIdlist.getLast());
			if(keyspace != 0  && keyspace == progress) {
				for(int j = 0; j < taskIdlist.size() - 1; j++) {
					dht.delnoCracked(taskIdlist.get(j));
					chd.delnocracked(taskIdlist.get(j));
				}
				t.cancel();
			}

		}
	}

	private int hashtype;
	private String param;//自定义参数
	private String hash;
//	private String accesskey;
	private String token;
	private int salted;
	private char split;
	private int taskId;
	private String Hash;
	private int passlength0;  //指定密码长度
	private String passlength1;  //指定长度范围
	private String charset;   //指定字符集
	private int taskid;
	private CrackHashDao chd = new CrackHashDaoImpl();
	private DealHashtopus dht = new DealHashtopusImpl();

	/*
	 * 添加Hash破解任务
	 */
	public void addCrackhash() throws Exception {
//		String md5 = accesskey.substring(0, 32);
//		int uid = Integer.valueOf(accesskey.substring(32));
		UserDaoImpl ud = new UserDaoImpl();
		int uid = 0;
		uid = ud.getUserByTok(getToken());
		User user = ud.getUserByUid(uid);
		if(user == null) {
			return;
		}
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String date = sdf.format(new Date());
//		String oMd5 = MD5Util.md5Encode(user.getPassword()+date+ user.getPassword()+date);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		JSONObject json = new JSONObject();
		if( uid != 0 ) {
			String hashName = String.valueOf(uid + System.currentTimeMillis());
			param = "#HL# -a 3 -m " +hashtype+" "+"-1 ";// Hash类型
			if(getPasslength0() != 0 ) {
				StringBuilder sb = new StringBuilder();
				String[] a = charset.split(",");
				for(int i = 0; i < a.length; i++)
				{	
					sb.append("?");
					sb.append(a[i].trim());
				}
				sb.append(" ");
				for (int i = 0; i < getPasslength0(); ++i) {
					sb.append("?1");
				}
				param += sb.toString() + " --gpu-temp-disable";
//				System.out.println(param);
//				int taskId = HtpUtil.createHashTask(1, hashName, hashtype, 0, getSplit(), Hash, param);
//				chd.addCrackTask(uid, taskId);
				LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
				map = HtpUtil.createHashTask1(1, hashName, hashtype, salted, split, hash, param);
				Iterator<Entry<Integer, String>> iterator = map.entrySet().iterator();
				Entry<Integer, String> entry = iterator.next();
				int taskId = entry.getKey();
				String cookie = entry.getValue();
//				String cookie = entry.getValue();
				chd.addCrackTask(uid, taskId);
				int prePri = dht.getPriority();  //倒数第二条的优先级数，因为刚刚程序新建了一条task
				if(prePri == 0) {
					dht.crtPriority(1000000, taskId);
					CrackhashAction.assign1(taskId, cookie);  //为了防止优先级最高时hashtopus没有自动assign该任务，写一个手动assign
				}else {
					while(true) {
						int i = 1;
						int j = prePri - i;
						i += 2;
						if(j < prePri) {
							dht.crtPriority(j, taskId);
							break;
						}
					}
				}
				json.put("code", 0);
				json.put("id", taskId);
				writer.print(JSONObject.toJSONString(json));
			} else if( !getPasslength1().isEmpty() ){// 指定长度范围
				String d = String.valueOf(passlength1);
				String[] b = d.split(",");
				int minLength = Integer.valueOf(b[0].trim());
				int maxLength = Integer.valueOf(b[1].trim());
				StringBuilder sb = new StringBuilder();
				String[] a = charset.split(",");
				for(int i = 0; i < a.length; i++)
				{	
					sb.append("?");
					sb.append(a[i].trim());
				}
				sb.append(" ");				
				Timer t = new Timer();  //是为了实现40秒钟查询一次数据库,并且分配任务
				LinkedList<Integer> taskIdlist = new LinkedList<Integer>();
				LinkedList<String> cookielist = new LinkedList<String>();
				for ( int j = minLength ; j < maxLength + 1; j++) {
					if (j != minLength) {
						sb.append("?1");
					}else {
						for (int i = 0; i < j; i++) {
							sb.append("?1");
						}
					}
					String c = param + sb.toString() + " --gpu-temp-disable";
					LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
				    map = HtpUtil.createHashTask1(1, hashName, hashtype, getSalted(), getSplit(), hash, c);
					Iterator<Entry<Integer, String>> iterator = map.entrySet().iterator();
					Entry<Integer, String> entry = iterator.next();
					int taskId = entry.getKey();
					String cookie = entry.getValue();
					chd.addCrackTask(uid, taskId);
					Integer xx = (Integer)taskId;
					taskIdlist.add(xx);
					cookielist.add(cookie);
					int prePri = dht.getPriority();
					if(prePri == 0) {
						dht.crtPriority(1000000, taskId);
						CrackhashAction.assign1(taskId, cookie);  //为了防止优先级最高时hashtopus没有自动assign该任务，写一个手动assign
					}else {
						while(true) {
							int i = 1;
							int x = prePri - i;
							i += 2;
							if(x < prePri) {
								dht.crtPriority(x, taskId);
								break;
							}
						}
					}

				}
				json.put("code", 0);
				json.put("id", taskIdlist);
				writer.print(JSONObject.toJSONString(json));
				t.scheduleAtFixedRate(new InnerCrackhashAction(taskIdlist,cookielist,t), 500, 5000); //40秒钟查询一次数据
			}
		}else {
		json.put("code", 1);
		}
	}
	
	/*
	 * 获得任务结果
	 */
	public void getCrackResult() throws IOException {
		UserDaoImpl ud = new UserDaoImpl();
		int uid = 0;
		uid = ud.getUserByTok(getToken());
		User user = ud.getUserByUid(uid);
		if(user == null) {
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
		writer.print("{\"data\":" + HtpUtil.getCrackResult(getTaskid()) + "}");
	}
	
	
	/*
	 * 获得任务进度
	 */
	public void getCrackProgress() throws IOException {
		UserDaoImpl ud = new UserDaoImpl();
		int uid = 0;
		uid = ud.getUserByTok(getToken());
		User user = ud.getUserByUid(uid);
		if(user == null) {
			return;
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		
//		int userid = (Integer) session.getAttribute("com.passwd.userid");

		if(taskid == 0) {
		PrintWriter writer = null;
		List<CrackTask> list = chd.getCrackTasks(uid);
		List<JSONObject> tasks = HtpUtil.getTaskProgress(list);
		int i = 0;
		for (CrackTask task : list) {
			JSONObject object = tasks.get(i++);
			long cprogress = 0;
			try{
				long keyspace = Long.valueOf((String) object.get("keyspace"));
				Double progress = Double.valueOf((String) object.get("progress"));
				if (!((String) object.get("cprogress")).equals("")) {
					cprogress = Long.valueOf((String) object.get("cprogress"));
				}
				task.setCprogress(cprogress);
				task.setKeyspace(keyspace);
				task.setProgress(progress);
			}catch(Exception e) {
				
			}
			JSONObject result = new JSONObject();
			result.put("data", list);
			result.put("code", 0);
			try {
				writer = response.getWriter();
			} catch (IOException e) {
				e.printStackTrace();
			}
			writer.print(JSON.toJSONString(result));
		}
		}else {
			PrintWriter writer = null;
			JSONObject object = HtpUtil.getTaskProgress(taskid);
			CrackTask task = new CrackTask();
			try{
				long cprogress = 0;
				long keyspace = Long.valueOf((String) object.get("keyspace"));
				Double progress = Double.valueOf((String) object.get("progress"));
				if (!((String) object.get("cprogress")).equals("")) {
					cprogress = Long.valueOf((String) object.get("cprogress"));
				}
				task.setCprogress(cprogress);
				task.setKeyspace(keyspace);
				task.setProgress(progress);
				task.setTask_id(taskid);
			}catch(Exception e) {	
			}
			JSONObject result = new JSONObject();
			result.put("data", task);
			result.put("code", 0);
			try {
				writer = response.getWriter();
			} catch (IOException e) {
				e.printStackTrace();
			}
			writer.print(JSON.toJSONString(result));
		}
		
	}
	
	public static void assign1(int taskid, String cookie) throws Exception, IOException {
		Agent agent = new Agent();
		Task task = new Task();
		JSONArray jsonArrAgents = agent.getAgents(true);
		Iterator<Object> itr = jsonArrAgents.iterator();
		JSONObject jsonObjAgent;
//		Date date = new Date();
		while (itr.hasNext()) {
			jsonObjAgent = (JSONObject) itr.next();
			task.assign(cookie, taskid, jsonObjAgent.getIntValue("id"));
//			if (date.getTime()/1000 - jsonObjAgent.getLong("lasttime") < 60) {
//				task.assign(cookie, task.getTaskId(), jsonObjAgent.getIntValue("id"));
//			} else {
//				System.out.println("Dead Hashtopus Node: " + jsonObjAgent.getIntValue("id"));
//			}
		}
	}

	public int getHashtype() {
		return hashtype;
	}

	public void setHashtype(int hashtype) {
		this.hashtype = hashtype;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public int getPasslength() {
		return getPasslength0();
	}

	public void setPasslength(int passlength) {
		this.setPasslength0(passlength);
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPasslength1() {
		return passlength1;
	}

	public void setPasslength1(String passlength1) {
		this.passlength1 = passlength1;
	}

	public int getPasslength0() {
		return passlength0;
	}

	public void setPasslength0(int passlength0) {
		this.passlength0 = passlength0;
	}

	public char getSplit() {
		return split;
	}

	public void setSplit(char split) {
		this.split = split;
	}

	public int getSalted() {
		return salted;
	}

	public void setSalted(int salted) {
		this.salted = salted;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getTaskid() {
		return taskid;
	}

	public void setTaskid(int taskid) {
		this.taskid = taskid;
	}



}
