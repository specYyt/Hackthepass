/**
 * 
 */
package com.passwd.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;

/**
 * @author Admin
 *
 */
public class RSAUtil {

	private static PublicKey publicKey;

	static  {
		try {
			String publickeyFile = "public.pem";
			//RSAUtil.class.getClassLoader().getResourceAsStream("/public.pem");
			publickeyFile = RSAUtil.class.getResource("/public.pem").getFile();
			BASE64Decoder base64decoder = new BASE64Decoder();
			BufferedReader br = new BufferedReader(new FileReader(publickeyFile));
			String s = br.readLine();
			StringBuffer publickey = new StringBuffer();
			s = br.readLine();
			while (s.charAt(0) != '-') {
				publickey.append(s + "\r");
				s = br.readLine();
			}
			byte[] keybyte = base64decoder.decodeBuffer(publickey.toString());
			KeyFactory kf = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keybyte);
			publicKey = kf.generatePublic(keySpec);
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String RSA(String str) {
		String result = "";
		try {
			X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(publicKey.getEncoded());
			KeyFactory keyFactory2 = KeyFactory.getInstance("RSA");
			PublicKey publicKey2 = keyFactory2.generatePublic(x509EncodedKeySpec2);
			Cipher cipher2 = Cipher.getInstance("RSA");
			cipher2.init(Cipher.ENCRYPT_MODE, publicKey2);
			byte[] result2 = cipher2.doFinal(str.getBytes());
			result = Base64.encodeBase64String(result2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(RSA("123"));
	}
}
