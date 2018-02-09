/**
 * 
 */
package com.passwd.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Properties;

/**
 * @author Admin
 *
 */
public class HttpUtil {

	private static String strUrl;
	
	static {
		Properties ps = new Properties();
		try {
			ps.load(ThriftUtil.class.getResourceAsStream("/star.properties"));
			strUrl = ps.getProperty("strUrl");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args) throws IOException {
//		String username = "admin";
//		String password = "admin";
//		String result = loginCheck(username, password);
//		System.out.println(result);
//		Map<String,Object> map =JsonParamUtil.getJsonObject(result);
//		String code = map.get("code").toString();
//		System.out.println(code);
//	}

	public static String loginCheck(String username, String password) {
		username = RSAUtil.RSA(username);
		password = RSAUtil.RSA(password);
		String param = "username="+username +"&password="+password;
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(strUrl);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    } 
	
}
