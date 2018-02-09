package com.passwd.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.passwd.dao.CrackHashDao;
import com.passwd.dao.DealHashtopus;
import com.passwd.dao.DictDao;
import com.passwd.dao.impl.CrackHashDaoImpl;
import com.passwd.dao.impl.DealHashtopusImpl;
import com.passwd.dao.impl.DictDaoImpl;
import com.passwd.htp.HtpUtil;
import com.passwd.htp.crack.Agent;
import com.passwd.htp.crack.Task;
import com.passwd.model.CrackTask;
import com.passwd.model.Dictionary;
import com.passwd.model.HashType;
import com.passwd.util.Cmd5API;
import com.passwd.util.HashIdentifier;

public class CrackhashAction extends SuperAction{
	private static final long serialVersionUID = 1L;
	class InnerCrackhashAction extends TimerTask{
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
//				System.out.println("传递过来的taskId个数:"+ taskIdlist.size());
//				System.out.println( "每一个taskId的值:"+taskIdlist.get(j));
				int hashlistid = dht.getHashlist(taskIdlist.get(j));
				int cracked = dht.getCracked(hashlistid);
				taskcrack.put(taskIdlist.get(j), cracked);
			}
			
			Iterator<Map.Entry<Integer, Integer>> iterator = taskcrack.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Integer, Integer> entry = iterator.next();
//				System.out.println(entry.getKey() +":"+entry.getValue());  //TO-DO delete
				if(entry.getValue() == 1 ) {
//					System.out.println("应该保留的taskId(破解成功的那个):"+ entry.getKey());
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
	private int method;// 1 直接输入  0  上传文件
	private String Hash;
	private File hashfile;
	private String savePath;
	private int salted;
	private String param;//自定义参数
	private int passLength;//密码长度
	private String charset;//内建字符集 
	private int crackMethod;//0普通暴力破解，1字典暴力破解
	private String Dictlist; //字典保存路径
	private char split;
	private CrackHashDao chd = new CrackHashDaoImpl();
	private DictDao dd = new DictDaoImpl();
	private DealHashtopus dht = new DealHashtopusImpl();

	private int lengthMethod;
	private int minLength; 
	private int maxLength;

	private final Logger logger = Logger.getLogger(CrackhashAction.class);
	/*
	 * 添加Hash破解任务
	 */
	public String addCrackhash() throws Exception {
		int userid = (Integer) session.getAttribute("com.passwd.userid");
		String hashName = String.valueOf(userid + System.currentTimeMillis());
		
		if(method == 0) {
			Hash = uploadFile(hashfile);//上传文件
		}
		if (getCrackMethod() == 1) {// 普通暴力破解
			param += "#HL# -a 3 -m " +hashtype+" "+"-1 ";// Hash类型
			if (lengthMethod == 1) {// 指定长度
				StringBuilder sb = new StringBuilder();
				String[] a = charset.split(",");
				for(int i = 0; i < a.length; i++)
				{	
					sb.append(a[i].trim());
				}
				sb.append(" ");
				for (int i = 0; i < passLength; ++i) {
					sb.append("?1");   //注意这里是数字1不是l。
				}
				param += sb.toString() + " --gpu-temp-disable";
				LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
				map = HtpUtil.createHashTask1(method, hashName, hashtype, salted, split, Hash, param);
				Iterator<Entry<Integer, String>> iterator = map.entrySet().iterator();
				Entry<Integer, String> entry = iterator.next();
				int taskId = entry.getKey();
				System.out.println("taskId:" + taskId);
				String cookie = entry.getValue();
				chd.addCrackTask(userid, taskId);
				int prePri = dht.getPriority();  //倒数第二条的优先级数，因为刚刚程序新建了一条task
				System.out.println("prePri:" + prePri);
				if(prePri == 0) {
					dht.crtPriority(1000000, taskId);
					CrackhashAction.assign1(taskId, cookie);  //为了防止优先级最高时hashtopus没有自动assign该任务，写一个手动assign
				}else {
					while(true) {
						int i = 1;
						int j = prePri - i;
						i += 2;
						System.out.println("运行时不等于0的："+ j);
						if(j < prePri) {
							dht.crtPriority(j, taskId);
							break;
						}
					}
				}
			} else if (lengthMethod == 2) {// 指定长度范围
				StringBuilder sb = new StringBuilder();
				String[] a = charset.split(",");
				for(int i = 0; i < a.length; i++)
				{	
					sb.append(a[i].trim());
				}
				sb.append(" ");
				Timer t = new Timer();  //是为了实现60秒钟查询一次数据库
				LinkedList<Integer> taskIdlist = new LinkedList<Integer>();
				LinkedList<String> cookielist = new LinkedList<String>();
				for ( int j = minLength ; j < maxLength + 1; j++) {
					if (j != minLength) {
						sb.append("?1");   //注意这里是数字1不是l。
					}else {
						for (int i = 0; i < j; i++) {
							sb.append("?1");
						}
					}
					String b = param + sb.toString() + " --gpu-temp-disable";
					LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
	
				    map = HtpUtil.createHashTask1(method, hashName, hashtype, salted, split, Hash, b);
					Iterator<Entry<Integer, String>> iterator = map.entrySet().iterator();
					Entry<Integer, String> entry = iterator.next();
					int taskId = entry.getKey();
					String cookie = entry.getValue();
					chd.addCrackTask(userid, taskId);
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
			t.scheduleAtFixedRate(new InnerCrackhashAction(taskIdlist,cookielist,t), 500, 2500); //2.5秒钟查询一次数据库
		}
		}

		if(getCrackMethod() == 2) { //字典暴力破解
				param += "#HL# -a 0 -m " +hashtype +" "+getDictlist();   //这儿的getDictlist就是个get方法，和前端交互的
				int taskId = HtpUtil.createHashTask(method, hashName, hashtype, salted,
						split, Hash, param);
				chd.addCrackTask(userid, taskId);
		}
		
		return SUCCESS;
	}
	
	/*
	 * 获得所有任务进度
	 */
	public void getCrackProgress() throws IOException {
		int userid = (Integer) session.getAttribute("com.passwd.userid");
		List<CrackTask> list = chd.getCrackTasks(userid);
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
			}catch(Exception e){
				logger.error("无法获取任务:" + task.getTask_id());
			}
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject result = new JSONObject();
		result.put("data", list);
		writer.print(JSON.toJSONString(result));
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

	private int taskId;
	public CrackhashAction(int taskId) { //为了传参进内部类，额，好像直接this.taskId = taskId就OK了
		this.taskId = taskId;
	}
	
	public CrackhashAction() {  //struts要求要有空构造器（如果有非空构造器的时候）
	}

	/*
	 * 获得任务结果
	 */
	public void getCrackResult() throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.print("{\"data\":" + HtpUtil.getCrackResult(taskId) + "}");
	}
	
	/*
	 * 获得字典列表
	 */	
	public void getDictList() {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<Dictionary> dicts = dd.getDictList();
		writer.print(JSON.toJSONString(dicts));
	}

	/*
	 * 获得Hash类型
	 */
	public void getHashTypes() {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<HashType> types = chd.getHashtypes();
		writer.print(JSON.toJSONString(types));
	}
	

	// Hash识别
	public void hashIdentifier() throws IOException {
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		if (Hash.trim().equals("")) {
			writer.print("请输入Hash");
			return;
		}
		String hash = HashIdentifier.identifyHashes(Hash);
		writer.print(hash);
	}
	
	public void cmd5Hash() throws IOException, IOException {
		response.setCharacterEncoding("utf-8");
		PrintWriter writer = response.getWriter();
		if (Hash.trim().equals("")) {
			writer.print("请输入Hash");
			return;
		}
		String result = Cmd5API.queryHash("md5", Hash);
		writer.print(result);
	}
	
	
	public String uploadFile(File file) {
	    String uuid = UUID.randomUUID().toString();
		String dstPath = ServletActionContext.getServletContext().getRealPath(this.getSavePath()) + File.separator
				+ uuid;
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		basePath += "hash/" + uuid +".txt";
		try {
			FileUtils.copyFile(file, new File(dstPath + ".txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return basePath;
	}
	

	public int getHashtype() {
		return hashtype;
	}

	public void setHashtype(int hashtype) {
		this.hashtype = hashtype;
	}

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public String getHash() {
		return Hash;
	}

	public void setHash(String hash) {
		Hash = hash;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public int getSalted() {
		return salted;
	}

	public void setSalted(int salted) {
		this.salted = salted;
	}

	public char getSplit() {
		return split;
	}

	public void setSplit(char split) {
		this.split = split;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getPassLength() {
		return passLength;
	}

	public void setPassLength(int passLength) {
		this.passLength = passLength;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int getLengthMethod() {
		return lengthMethod;
	}

	public void setLengthMethod(int lengthMethod) {
		this.lengthMethod = lengthMethod;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public File getHashfile() {
		return hashfile;
	}

	public void setHashfile(File hashfile) {
		this.hashfile = hashfile;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public int getCrackMethod() {
		return crackMethod;
	}

	public void setCrackMethod(int crackMethod) {
		this.crackMethod = crackMethod;
	}

	public String getDictlist() {
		return Dictlist;
	}

	public void setDictlist(String dictlist) {
		Dictlist = dictlist;
	}


}
