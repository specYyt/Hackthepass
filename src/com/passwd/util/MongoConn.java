package com.passwd.util;

import java.util.Arrays;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class MongoConn {

	private static String username = "htp";//�û���
	private static String password = "htppwd!";//����
	private static String database = "hackthepass";//���ݿ�
	private static MongoClient client ;
	
	public static MongoDatabase getConn() {
		MongoDatabase db = null;
		// ����MongoDB��������ַ����
    	ServerAddress address = new ServerAddress("127.0.0.1", 27017);
    	// ����MongoDB�������û���֤����
    	MongoCredential credential = MongoCredential.createCredential(username,
    			database,password.toCharArray());
    	// ��½��MongoDB������
    	client = new MongoClient(address, Arrays.asList(credential));
    	// ��ȡ�����MongoDB��������ָ�������ݿ�
    	db =  client.getDatabase(database);
    	
		return db;
	}
	
	public static void closeConn(){
		if(client != null){
			client.close();
		}
	}
	

}
