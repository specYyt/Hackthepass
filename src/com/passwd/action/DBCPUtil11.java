package com.passwd.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;




public class DBCPUtil11 {

	private static DataSource ds;
	
	static {

		InputStream in = DBCPUtil11.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties props = new Properties();
		try {
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			ds = BasicDataSourceFactory.createDataSource(props);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

	public static DataSource getDataSource() {
		return ds;
	}

}
