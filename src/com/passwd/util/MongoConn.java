package com.passwd.util;

import java.util.Arrays;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class MongoConn {

	private static String username = "htp";//用户名
	private static String password = "htppwd!";//密码
	private static String database = "hackthepass";//数据库
	private static MongoClient client ;
	
	public static MongoDatabase getConn() {
		MongoDatabase db = null;
		// 创建MongoDB服务器地址对象
    	ServerAddress address = new ServerAddress("127.0.0.1", 27017);
    	// 创建MongoDB服务器用户验证对象
    	MongoCredential credential = MongoCredential.createCredential(username,
    			database,password.toCharArray());
    	// 登陆到MongoDB服务器
    	client = new MongoClient(address, Arrays.asList(credential));
    	// 获取存放在MongoDB服务器上指定的数据库
    	db =  client.getDatabase(database);
    	
		return db;
	}
	
	public static void closeConn(){
		if(client != null){
			client.close();
		}
	}
	

}
