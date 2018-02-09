package com.passwd.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

public class MongoImpl {
	private MongoDatabase db = null;
	private MongoClient client = null;
	
	public MongoImpl() throws UnknownHostException{
		// 创建MongoDB服务器地址对象
    	ServerAddress address = new ServerAddress("127.0.0.1", 27017);
    	// 创建MongoDB服务器用户验证对象
    	MongoCredential credential = MongoCredential.createCredential("htp",
    			"hackthepass",
    			"htppwd!".toCharArray());
    	// 登陆到MongoDB服务器
    	client = new MongoClient(address, Arrays.asList(credential));
    	// 获取存放在MongoDB服务器上指定的数据库
    	db =  client.getDatabase("hackthepass");

	}
	
	//获得所有集合名
	public void getAllCollections() {
       MongoIterable<String> colls = db.listCollectionNames(); //获得所有集合名
       //输出所有集合名
       for (String s : colls) {  
            System.out.println(s);  
        }
   }

	
	//插入数据
	public void insert(){
		MongoCollection<Document> coll=db.getCollection("test");
		Document doc = new Document("name", "demo").append("type","database").append("count", 1);
		coll.insertOne(doc);
		Document doc1 = new Document("name", "struts").append("type","mvc").append("count1", 1);
		coll.insertOne(doc1);
		Document doc2 = new Document("name", "spring").append("type","service").append("count2", 1);
		coll.insertOne(doc2);
		queryAll(coll);
	}
	
	
	//统计指定集合内所有元素数量
	public long queryAll(MongoCollection<Document> coll){
		MongoCursor<Document> cursor = coll.find().iterator();
		try {
		   while(cursor.hasNext()) {
		       System.out.println(cursor.next());
		   }
		} finally {
		   cursor.close();
		}
		return coll.count();
	}
	

	
	//指定集合创建索引
	public void createIndex(String coll,String name) {  
	    db.getCollection(coll).createIndex(new BasicDBObject(name, 1));  
	} 
	
	//查找文档
	public List<Document> find(String key,String value){
		/*modified by sunjysun; No longer fetch ALL collections, query involved collections in `KeyColl`*/
		
		MongoCursor<Document>  collsCursor = db.getCollection("KeyColl").find(new Document("key", key)).iterator();//.listCollectionNames();//取得所有集合的名字
		List<String> colls = new ArrayList<String>();
		try{
			while(collsCursor.hasNext()){
				Document doc = collsCursor.next();
				colls.add((String)doc.get("coll"));
			}
		}finally{
			collsCursor.close();
		}
		/**************************/
		List<Document> listResult = new ArrayList<Document>();
		boolean flag = false;//标记是否有查询结果
	    for (String coll : colls) {
	    	Document docFilter = new Document( key, value);//查找文档
			MongoCursor<Document> cursor = db.getCollection(coll).find(docFilter).iterator();
			try {
			   while(cursor.hasNext()) {
				   Document ob = cursor.next();//得到查询的结果文档
				   ob.append("source",coll);
				   listResult.add(ob);
				   flag = true;
			   }
			} finally {
			   cursor.close();
			}
	    }
		if(!flag){
			Document message = new Document("提示","没有找到查询结果");
			listResult.add(message);
		}
		return listResult;
	}
	
//	public static void main(String[] args) throws UnknownHostException {
//		MongoImpl m = new MongoImpl();
//	}
	
	
}
