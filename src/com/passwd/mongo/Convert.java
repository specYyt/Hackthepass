package com.passwd.mongo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Convert {
	
	public static void main(String[] args) {
		Convert t = new Convert();
		String inputfile = "C:\\csdn.txt";//需处理文件路径
		String outputfile = "C:\\a.csv";//输出文件路径
		String symbol = " ---- ";//分隔符
		//String symbol = "a|b";//以a或者以b作为分隔符皆可
		t.handle( inputfile, outputfile, symbol);
	}
	/**
	 * 	 处理文件格式
	 * @param inputfile	  导入文件
	 * @param outputfile	导出文件
	 * @param symbol	间隔符号
	 */
	public void handle(String inputfile,String outputfile,String symbol){
		FileInputStream fis = null;
		InputStreamReader isr = null;
	    BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
	    BufferedWriter bw = null;
	    try {
	    	fis = new FileInputStream(inputfile);
	    	FileWriter fw = new FileWriter(outputfile);	
 			bw=new BufferedWriter(fw);
		    // 从文件系统中的某个文件中获取字节
		    isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
		    br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
		    String str = "";
		    String str1 = "";
		    while ((str = br.readLine()) != null) {//当从文件中读取的下一行不为空
		    	String regex ="\"";//用来匹配的正则表达式  双引号需要转义
		    	String replacement ="\"\"";//用来替换的正则表达式  双引号需要转义
			    str = str.replaceAll(regex, replacement); //把所有"替换成""
			    String array[] = str.split(symbol);//用symbol作为分隔符来分割
			    str1 = "\""+array[0]+"\""+","+"\""+array[1]+"\""+","+"\""+array[2]+"\""+"\r\n";//对分割后的字符数组进行处理
			    bw.write(str1);//写入数据到文件
		    }
	    } catch (FileNotFoundException e) {
	    	System.out.println("找不到指定文件");
	    } catch (IOException e) {
	    	System.out.println("读取文件失败");
	    } finally {
			try {
				 br.close();
				 isr.close();
				 fis.close();
				 bw.close();
				 // 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
			} catch (IOException e) {
				 e.printStackTrace();
			}
	   }
	}
	/**
	 *  通过命令行导入数据，这里是Window下导入命令
	 * @param column	key值数组
	 * @param table	Collection名
	 * @param file	文件名
	 */
	public void importData(String column[],String table,String file){
		file = "C:\\test.csv";//文件路径
		table = "csdn";//集合名
		String columnname= "";//字段名称
		for(int i = 0;i < column.length; i++){
			if(i == column.length-1){
				columnname += column[i];
			}else{
				columnname += column[i] + ",";
			}
		}
		try {		
	        String cmdStr = "cmd /c " + "mongoimport -h localhost -d " + table + " -c csdn -f " + columnname + "--type csv --file " + file;
	        Runtime.getRuntime().exec(cmdStr);
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	}
	
 }
