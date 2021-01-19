package com.port.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.port.util.ASCIIToSort;

public class GetHMACSHA256 {
	/**
	 * 
	 * @param str 参数数组，例子：AccessKeyId=089cf604-7b87-4b13-b806-eaadb67c8b70&SignatureMethod=HmacSHA256&SignatureVersion=2&Timestamp=1551084749915&contractId=2001&side=1&size=1&type=2
	 * @param key
	 * @return
	 */
	public static String getHMACSHA256(String[] str, String key)  {
		//对字符串数组进行排序
		String[] str_ASCIIToSort = ASCIIToSort.getUrlParam(str);
		//对排序后的字符串数组进行拼接
		String str_new = "";
		for (int i = 0 ; i < str_ASCIIToSort.length ; i ++) {
			if(i!=str_ASCIIToSort.length-1)
				str_new += str_ASCIIToSort[i]+"&";
			else
				str_new += str_ASCIIToSort[i];
		}		
		try {
			//调用HMACSHA256方法
//			String h = HMACSHA256(str_new,key);
			String h = HMACSHA256(str_new,key);
			System.out.println("HMACSHA256加密完毕<"+h+">");
			return h;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 第一种加密方式
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static String HMACSHA256(String data, String key) throws Exception {
	       Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
	       SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
	       sha256_HMAC.init(secret_key);
	       byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
	       StringBuilder sb = new StringBuilder();
	       for (byte item : array) {
	           sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
	       }
	       return urlEncode(sb.toString());
	   }
	/**
	 * 第二种加密方式
	 * @param message
	 * @param apiSecret
	 * @return
	 */
	public static String createSignature( String message, String  apiSecret ) {
	        Mac hmacSha256;
	        try {
	            hmacSha256 = Mac.getInstance("HmacSHA256");
	            SecretKeySpec secKey = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
	            hmacSha256.init(secKey);
	        } catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException("No such algorithm: " + e.getMessage());
	        } catch (InvalidKeyException e) {
	            throw new RuntimeException("Invalid key: " + e.getMessage());
	        }

	        String payload = message.toString();
//	       LOGGER.info("待签名的请求: {}.", payload);
	        byte[] hash = hmacSha256.doFinal(payload.getBytes(StandardCharsets.UTF_8));
	        return urlEncode(new String(Hex.encodeHex(hash)));
	    }
	 public static String urlEncode(String s) {
	        try {
	            return URLEncoder.encode(s,"UTF-8").replaceAll("\\+", "%20");
	        } catch(UnsupportedEncodingException e) {
	        	
	            throw new IllegalArgumentException("UTF-8 encoding not supported!");
	        }
	    }
}
