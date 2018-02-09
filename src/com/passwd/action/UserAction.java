package com.passwd.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.passwd.dao.LoginDao;
import com.passwd.dao.UserDao;
import com.passwd.dao.impl.LoginDaoImpl;
import com.passwd.dao.impl.UserDaoImpl;
import com.passwd.model.User;
import com.passwd.util.HttpUtil;
import com.passwd.util.JsonParamUtil;
import com.passwd.util.MD5Util;

public class UserAction extends SuperAction {
	
	private final Logger logger = Logger.getLogger(UserAction.class);
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String rand;// 输入的验证码
	private UserDao userdao = new UserDaoImpl();
	private String errMsg;
	private int competence;
	private List<User> usersList = new ArrayList<>();
	
	// 用户登录校验
	public String userLogin() throws IOException {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
//		String gRand = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);// 生成的验证码
//		gRand = gRand.toLowerCase();
//		rand = rand.toLowerCase();
//		if (!gRand.equals(rand)){
//			errMsg = "验证码错误。请重新输入。";
//			return INPUT;
//		}
		if (userdao.isLogin(user)) {
			user = userdao.getUser(username);
			session.setAttribute("com.passwd.userid", user.getUid());
			session.setAttribute("com.passwd.username", username);
			session.setAttribute("com.passwd.token",user.getToken());
			session.setAttribute("competence", user.getCompetence());
			return SUCCESS;
		} else {
			errMsg = "用户名或密码错误。请重新输入。";
			return INPUT;
		}
	}
	
/*	// 用户登录校验
	public String userLogin() throws IOException {
		//User user = new User();
		//user.setUsername(username);
		//user.setPassword(password);
		String gRand = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);// 生成的验证码
		gRand = gRand.toLowerCase();
		rand = rand.toLowerCase();
		if (!gRand.equals(rand)) {
			errMsg = "验证码错误。请重新输入。";
			return INPUT;
		}
		String result;
		try{
			result = HttpUtil.loginCheck(username, password);
		}catch(Exception e){
			System.out.println("无法连接starmap服务器");
			errMsg = "服务器出错了，请联系管理员";
			return INPUT;
		}
		Map<String,Object> map =JsonParamUtil.getJsonObject(result);
		try{
			if(map.size() == 0){
				errMsg = "服务器出错了，请联系管理员";
				return INPUT;
			}
		}catch(Exception e){
			e.printStackTrace();
			errMsg = "服务器出错了，请联系管理员";
			return INPUT;
		}
		
		String code = map.get("code").toString();
		if (code.equals("success")) {
			user = userdao.getUser(username);
			session.setAttribute("user", user);
			session.setAttribute("ControlServer", "http://password.anet/ControlServer");
			session.setAttribute("hashtopus", "http://password.anet:50080/");
			session.setAttribute("com.passwd.userid", user.getId());
			session.setAttribute("com.passwd.username", username);
			//session.setAttribute("com.passwd.token", user.getToken());
			return SUCCESS;
		} else {
			errMsg = "用户名或密码错误。请重新输入。";
			logger.error("登陆失败："+ map);
			return INPUT;
		}
	}*/

	// 用户注销
	public String userlogout() {
		session.removeAttribute("username");
		session.invalidate();
		return SUCCESS;
	}

/*	// 用户列表
	public String getUsers() {
		User user = (User) session.getAttribute("user");
		try{
			if (user.getCompetence() == 1) {
				usersList = userdao.getUsers();
			}
		}catch(Exception e){
			return ERROR;
		}
		return SUCCESS;
	}*/

	private int uid;
	
/*	// 删除用户
	public String deleteUser() {
		int competence = ((User) session.getAttribute("user")).getCompetence();
		try{
			if (competence == 1) {
				userdao.deleteUser(uid);
			}
		}catch(Exception e){
			return ERROR;
		}
		return SUCCESS;
	}*/

	private User user;
	
/*	// 添加用户
	public String addUser() {
		int competence = ((User) session.getAttribute("user")).getCompetence();
		try{
			if (competence == 1) {
				String salt = UUID.randomUUID().toString().substring(0, 7);
				user.setSalt(salt);
				user.setPassword(MD5Util.md5Encode(salt + user.getPassword()));
				user.setToken(UUID.randomUUID().toString());
				userdao.addUser(user);
			} 
		}catch(Exception e){
			return ERROR;
		}
		return SUCCESS;
	}*/
	
	private String accesskey;
	
	//外部平台使用accesskey登陆
	public String loginCheck() throws Exception {
		
		String md5 = accesskey.substring(0, 32);
		int uid = Integer.valueOf(accesskey.substring(32));
		response.setCharacterEncoding("utf-8");
		LoginDao ld = new LoginDaoImpl();
		//System.out.println(uid);
		User user = ld.getUser(uid);
		if(user == null) {
			return ERROR;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		String oMd5 = MD5Util.md5Encode(user.getPassword()+date+ user.getPassword()+date);
		
		if(md5.equals(oMd5)) {
		
		//User user = new User();
			session.setAttribute("user", user);
			session.setAttribute("ControlServer", "http://password.anet/ControlServer");
			session.setAttribute("com.passwd.userid", user.getId());
			session.setAttribute("com.passwd.username", user.getUsername());
			
			//session.setAttribute("com.passwd.userid", 1);
			//session.setAttribute("com.passwd.username", "admin");
			
			//session.setAttribute("com.passwd.token", user.getToken());
			return SUCCESS;
		}
		
		return INPUT;
	}
	
	
	
//	public String updateUserView() {
//		user = userdao.getUserByUid(uid);
//		return SUCCESS;
//	}
	
//	public String updateUser() {
//		int competence = ((User) session.getAttribute("user")).getCompetence();
//		try{
//			if (competence == 1) {
//				userdao.updateUser(user);
//			} 
//		}catch(Exception e){
//			return ERROR;
//		}
//		return SUCCESS;
//	}
	
	public String getRand() {
		return rand;
	}

	public void setRand(String rand) {
		this.rand = rand;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public List<User> getUsersList() {
		return usersList;
	}

	public void setUsersList(List<User> usersList) {
		this.usersList = usersList;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAccesskey() {
		return accesskey;
	}

	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}

	public int getCompetence() {
		return competence;
	}

	public void setCompetence(int competence) {
		this.competence = competence;
	}
	
}
