package com.passwd.model;

public class User {

	private int uid; // 用户编号
	private int id;
	// private String name;// 昵称
	private String username;// 用户名
	private String password;// 密码
	// private String salt;
	// private Long phone;// 用户手机号
	// private int ban;// 是否禁用 1为禁用 0 为不禁用
	// private int Tuid;
	private int group_id; // 用户权限
	private String token;
	private int competence;
	

/*	public int getUId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}*/

	public int getGroup_id() {

		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCompetence() {
		return competence;
	}

	public void setCompetence(int competence) {
		this.competence = competence;
	}

}
