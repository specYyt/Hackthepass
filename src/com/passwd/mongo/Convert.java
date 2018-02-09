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
		String inputfile = "C:\\csdn.txt";//�账���ļ�·��
		String outputfile = "C:\\a.csv";//����ļ�·��
		String symbol = " ---- ";//�ָ���
		//String symbol = "a|b";//��a������b��Ϊ�ָ����Կ�
		t.handle( inputfile, outputfile, symbol);
	}
	/**
	 * 	 �����ļ���ʽ
	 * @param inputfile	  �����ļ�
	 * @param outputfile	�����ļ�
	 * @param symbol	�������
	 */
	public void handle(String inputfile,String outputfile,String symbol){
		FileInputStream fis = null;
		InputStreamReader isr = null;
	    BufferedReader br = null; //���ڰ�װInputStreamReader,��ߴ������ܡ���ΪBufferedReader�л���ģ���InputStreamReaderû�С�
	    BufferedWriter bw = null;
	    try {
	    	fis = new FileInputStream(inputfile);
	    	FileWriter fw = new FileWriter(outputfile);	
 			bw=new BufferedWriter(fw);
		    // ���ļ�ϵͳ�е�ĳ���ļ��л�ȡ�ֽ�
		    isr = new InputStreamReader(fis);// InputStreamReader ���ֽ���ͨ���ַ���������,
		    br = new BufferedReader(isr);// ���ַ��������ж�ȡ�ļ��е�����,��װ��һ��new InputStreamReader�Ķ���
		    String str = "";
		    String str1 = "";
		    while ((str = br.readLine()) != null) {//�����ļ��ж�ȡ����һ�в�Ϊ��
		    	String regex ="\"";//����ƥ���������ʽ  ˫������Ҫת��
		    	String replacement ="\"\"";//�����滻��������ʽ  ˫������Ҫת��
			    str = str.replaceAll(regex, replacement); //������"�滻��""
			    String array[] = str.split(symbol);//��symbol��Ϊ�ָ������ָ�
			    str1 = "\""+array[0]+"\""+","+"\""+array[1]+"\""+","+"\""+array[2]+"\""+"\r\n";//�Էָ����ַ�������д���
			    bw.write(str1);//д�����ݵ��ļ�
		    }
	    } catch (FileNotFoundException e) {
	    	System.out.println("�Ҳ���ָ���ļ�");
	    } catch (IOException e) {
	    	System.out.println("��ȡ�ļ�ʧ��");
	    } finally {
			try {
				 br.close();
				 isr.close();
				 fis.close();
				 bw.close();
				 // �رյ�ʱ����ð����Ⱥ�˳��ر���󿪵��ȹر������ȹ�s,�ٹ�n,����m
			} catch (IOException e) {
				 e.printStackTrace();
			}
	   }
	}
	/**
	 *  ͨ�������е������ݣ�������Window�µ�������
	 * @param column	keyֵ����
	 * @param table	Collection��
	 * @param file	�ļ���
	 */
	public void importData(String column[],String table,String file){
		file = "C:\\test.csv";//�ļ�·��
		table = "csdn";//������
		String columnname= "";//�ֶ�����
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
