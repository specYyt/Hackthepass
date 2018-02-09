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
		// ����MongoDB��������ַ����
    	ServerAddress address = new ServerAddress("127.0.0.1", 27017);
    	// ����MongoDB�������û���֤����
    	MongoCredential credential = MongoCredential.createCredential("htp",
    			"hackthepass",
    			"htppwd!".toCharArray());
    	// ��½��MongoDB������
    	client = new MongoClient(address, Arrays.asList(credential));
    	// ��ȡ�����MongoDB��������ָ�������ݿ�
    	db =  client.getDatabase("hackthepass");

	}
	
	//������м�����
	public void getAllCollections() {
       MongoIterable<String> colls = db.listCollectionNames(); //������м�����
       //������м�����
       for (String s : colls) {  
            System.out.println(s);  
        }
   }

	
	//��������
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
	
	
	//ͳ��ָ������������Ԫ������
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
	

	
	//ָ�����ϴ�������
	public void createIndex(String coll,String name) {  
	    db.getCollection(coll).createIndex(new BasicDBObject(name, 1));  
	} 
	
	//�����ĵ�
	public List<Document> find(String key,String value){
		/*modified by sunjysun; No longer fetch ALL collections, query involved collections in `KeyColl`*/
		
		MongoCursor<Document>  collsCursor = db.getCollection("KeyColl").find(new Document("key", key)).iterator();//.listCollectionNames();//ȡ�����м��ϵ�����
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
		boolean flag = false;//����Ƿ��в�ѯ���
	    for (String coll : colls) {
	    	Document docFilter = new Document( key, value);//�����ĵ�
			MongoCursor<Document> cursor = db.getCollection(coll).find(docFilter).iterator();
			try {
			   while(cursor.hasNext()) {
				   Document ob = cursor.next();//�õ���ѯ�Ľ���ĵ�
				   ob.append("source",coll);
				   listResult.add(ob);
				   flag = true;
			   }
			} finally {
			   cursor.close();
			}
	    }
		if(!flag){
			Document message = new Document("��ʾ","û���ҵ���ѯ���");
			listResult.add(message);
		}
		return listResult;
	}
	
//	public static void main(String[] args) throws UnknownHostException {
//		MongoImpl m = new MongoImpl();
//	}
	
	
}
