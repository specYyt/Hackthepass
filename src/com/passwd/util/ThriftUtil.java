/**
 * 
 */
package com.passwd.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import mgdbservice.thrift.DBService.Client;
import mgdbservice.thrift.KeywordRequest;

/**
 * @author Admin
 *
 */
public class ThriftUtil {


	private static TTransport transport;
	private static Client client;
	
	private String errmsg;//错误信息
	private String errno;//错误号

	private int totalHits;//结果数量
	private int time;//用时
	
	static {
		Properties ps = new Properties();
		try {
			ps.load(ThriftUtil.class.getResourceAsStream("/thrift.properties"));
			 transport = new TSocket(ps.getProperty("url"), Integer.valueOf(ps.getProperty("port")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static TTransport getTTransport(){
		return transport;
	}
	
	// 基本搜索
	public Map<String, String> basicSearch(String query, boolean analyzed, int start, int size) {
		Map<String, String> results = new HashMap<String, String>();
		try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
		if (transport == null)
			return results;
		TProtocol protocol = new TBinaryProtocol(transport);
		client = new Client(protocol);
		try {
			KeywordRequest kr = new KeywordRequest();
			kr.setCommand("basic");
			kr.setQuery(query);
			kr.setAnalyzed(analyzed);
			kr.setStart(start);
			kr.setSize(size);
			String result = client.keywordHandler(kr, 1);

			errmsg = JSON.parseObject(result).getString("errmsg");// 错误信息
			errno = JSON.parseObject(result).getString("errno");// 错误代码

			JSONObject data = JSON.parseObject(result).getJSONObject("data");// 数据

			totalHits = data.getInteger("totalHits");
			time = data.getInteger("time");

			JSONArray contents = data.getJSONArray("content");

			for (Object content : contents) {
				StringBuilder rs = new StringBuilder();
				JSONObject jo = (JSONObject) content;
				String label = jo.getString("label");
				JSONArray sources = jo.getJSONArray("source");
				for (int i = 0; i < sources.size(); i++) {
					JSONObject so = (JSONObject) sources.get(i);
					rs.append(so.getString("propertyType"));
					rs.append(":");
					rs.append(so.getString("propertyValue"));
					if (i != sources.size() - 1)
						rs.append(" ");
				}
				results.put(label, rs.toString());
			}
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			transport.close();
		}
		return results;
	}
	
	
	/*
	 * 根据数据库名称和类型来获得对象类型ID
	 */
	public static String getObjectTypeByDatabase(String dbname, String dbtype){
		String result = "";
		try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
		TProtocol protocol = new TBinaryProtocol(transport);
		client = new Client(protocol);
		JSONArray data = null;
		try {
			result = client.getObjectTypesByDB(dbname, dbtype);//通过DBname和DBtype来找对象类型
			data= JSON.parseObject(result).getJSONArray("data");// 数据
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			transport.close();
		}
	
		return data.toJSONString();
	}

	/*
	 * 根据对象类型ID获得属性ID列表
	 */
	public static String getProptiesByObjectType(String objectId){
		try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
		TProtocol protocol = new TBinaryProtocol(transport);
		client = new Client(protocol);
		JSONArray rs = new JSONArray();
		try {
			String result = client.getPropertyTypesOfObjectType(objectId);// 对象类型的所有属性类型
			
			JSONArray data= JSON.parseObject(result).getJSONArray("data");// 数据
			for (int i = 0; i < data.size(); i++) {
				JSONObject object = (JSONObject) data.get(i);
				JSONObject o = new JSONObject();
				o.put("id", object.get("id"));
				o.put("name", object.get("name"));//field
				JSONArray semaTypes = (JSONArray) object.get("semaTypes");
				if(semaTypes.size() > 0){
					o.put("name", ((JSONObject)semaTypes.get(0)).get("name"));
				}
				rs.add(o);
			}
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			transport.close();
		}
		return rs.toJSONString();
	}
	
	
	
	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getErrno() {
		return errno;
	}

	public void setErrno(String errno) {
		this.errno = errno;
	}

	public int getTotalHits() {
		return totalHits;
	}

	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}



}
