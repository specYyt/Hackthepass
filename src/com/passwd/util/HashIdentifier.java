/**
 * 
 */
package com.passwd.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Admin
 *
 */
public class HashIdentifier {

	private static List<Entry<Object, Object>> hashes = new ArrayList<>();
	
	
	static {
		Properties ps = new Properties();
		try {
			ps.load(ThriftUtil.class.getResourceAsStream("/hashes.properties"));
			Set<Entry<Object, Object>> set = ps.entrySet();
			
			for(Entry<Object,Object> entry : set) {
				hashes.add(entry);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String identifyHashes(String hash) {
		for(Entry<Object, Object> entry : hashes){
			String name = (String) entry.getKey();
			String regex = (String) entry.getValue();
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(hash);
			if(matcher.matches()){
				return name;
			}
		}
		return "无法识别";
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(HashIdentifier.identifyHashes("asfafsfd"));
	}
}
