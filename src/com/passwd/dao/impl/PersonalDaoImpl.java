/**
 * 
 */
package com.passwd.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.passwd.dao.PersonalDao;
import com.passwd.model.Data;
import com.passwd.model.SearchResult;
import com.passwd.util.ThriftUtil;

import mgdbservice.thrift.DBService.Client;
import mgdbservice.thrift.MetasPageRequest;

/**
 * @author Admin
 *
 */
public class PersonalDaoImpl implements PersonalDao{
	
	private static TTransport transport = ThriftUtil.getTTransport();
	private static Client client;
	

	
	//基本搜索
	public SearchResult basic(String query, boolean analyzed, int start, int size) {
		SearchResult sr= new SearchResult();
		 List<Data> results = new ArrayList<Data>();
		try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
		if (transport == null)
			return sr;
		TProtocol protocol = new TBinaryProtocol(transport);
		client = new Client(protocol);
		try {
			
			MetasPageRequest mpr = new MetasPageRequest();
			mpr.setCommand("metapagetypes");
			mpr.setQuery(query);
			mpr.setAnalyzed(analyzed);
			mpr.setStart(start);
			mpr.setSize(size);
			List<String> list = new ArrayList<>();
			// personalpwds
			String ojbectResult = client.getObjectTypesByDB("pwds", "mysql");//通过DBname和DBtype来找对象类型
			JSONArray objectData= JSON.parseObject(ojbectResult).getJSONArray("data");// 数据
			String objecttypes = objectData.toJSONString();
			
			JSONArray objarr = JSONArray.parseArray(objecttypes);
			for(int i = 0; i < objarr.size(); i++){
				JSONObject obj=(JSONObject) objarr.get(i);
				list.add((String)obj.get("id"));
			}
			mpr.setObjectTypes(list);
			
			String result = client.metasPageHandler(mpr, 1);
			
			sr.setErrmsg(JSON.parseObject(result).getString("errmsg"));// 错误信息
			sr.setErrno(JSON.parseObject(result).getString("errno"));// 错误代码

			JSONObject data = JSON.parseObject(result).getJSONObject("data");// 数据

			sr.setTotalHits(data.getInteger("totalHits"));
			sr.setTime(data.getInteger("time"));
	
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
				results.add(new Data(label, rs.toString()));
		
			}
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			transport.close();
		}
		sr.setResults(results);
		return sr;
	}

	//根据对象类型和属性类型搜索
	public SearchResult metatype(String query, boolean analyzed, String objectType, String propertyType, int start, int size) {
		SearchResult sr= new SearchResult();
		 List<Data> results = new ArrayList<Data>();
		try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
		if (transport == null)
			return sr;
		TProtocol protocol = new TBinaryProtocol(transport);
		client = new Client(protocol);
		try {
			
			MetasPageRequest mpr = new MetasPageRequest();
			mpr.setCommand("metapagetypes");
			mpr.setQuery(query);
			mpr.setAnalyzed(analyzed);
			mpr.setStart(start);
			mpr.setSize(size);
			List<String> list = new ArrayList<>();
			
			String ojbectResult = client.getObjectTypesByDB("pwds", "mysql");//通过DBname和DBtype来找对象类型
			JSONArray objectData= JSON.parseObject(ojbectResult).getJSONArray("data");// 数据
			String objecttypes = objectData.toJSONString();
			
			JSONArray objarr = JSONArray.parseArray(objecttypes);
			for(int i = 0; i < objarr.size(); i++){
				JSONObject obj=(JSONObject) objarr.get(i);
				list.add((String)obj.get("id"));
			}
			mpr.setObjectTypes(list);
			List<String> property = Arrays.asList(propertyType);
			mpr.setPropertyTypes(property);
			String result = client.metasPageHandler(mpr, 1);
		
			sr.setErrmsg(JSON.parseObject(result).getString("errmsg"));// 错误信息
			sr.setErrno(JSON.parseObject(result).getString("errno"));// 错误代码
			if(sr.getErrno().equals("2")){//查找失败
				return sr;
			}
			JSONObject data = JSON.parseObject(result).getJSONObject("data");// 数据
			
			sr.setTotalHits(data.getInteger("totalHits"));
			sr.setTime(data.getInteger("time"));
	
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
				
				results.add(new Data(label, rs.toString()));
				
			}
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			transport.close();
		}
		sr.setResults(results);
		return sr;
	}

	
	
	 
	
	 
}
