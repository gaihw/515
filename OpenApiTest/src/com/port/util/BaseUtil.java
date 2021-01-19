package com.port.util;

import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Set;


public class BaseUtil {	
	public static long TIMESTAMP ;	
//	public static String X_58COIN_APIKEY = Config.API_KEY;
	public static String X_58COIN_APIKEY = Config.API_CESHI_KEY;
	public static String API_SECRET = Config.API_CESHI_SECRET;
//	public static String API_SECRET = Config.API_SECRET;
	public static HttpClient client;
//	static {
//		TIMESTAMP = GetTimestamp.getTimestamp(client, Config.REST_CESHI_API);
//	}
	@BeforeMethod
	public void setUp() {
		client = new HttpClient();
//		TIMESTAMP = GetTimestamp.getTimestamp(client, Config.REST_API);
		TIMESTAMP = System.currentTimeMillis();
		
	}
	@AfterMethod
	public void tearDown() {
	
	}
	public static String getAccessToken(HttpClient client,String url,JSONObject params,String[] str) {
		String accessToken = JSONObject.fromObject(getPostResponseBody(client,url,params,str)).getJSONObject("data").getString("accessToken");
		System.out.println("接口返回数据正常，accessToken="+accessToken);
		return accessToken;
	}
	public static String getPostResponseBodyAndAccess_Tooken(HttpClient client,String url,JSONObject params,String access_token) {
		PostMethod method = new PostMethod(url);
		client.getParams().setParameter("http.protocol.single-cookie-header", true);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		method.addRequestHeader("Content-Type","application/x-www-form-urlencoded");
		method.addRequestHeader("ACCESS_TOKEN",access_token);		
		//方法一
//		DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
//		方法二
		client.getParams().setParameter("http.protocol.single-cookie-header", true);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		Set<String> set = params.keySet();
		Iterator it = set.iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			method.addParameter(key, params.getString(key));
		}
		int status_code;
		try {
			status_code = client.executeMethod(method);
			if(status_code == HttpStatus.SC_OK) {
				System.out.println("接口获取成功,status_code="+status_code);
//				String responseBody = method.getResponseBodyAsString();
				InputStream inputStream = method.getResponseBodyAsStream();  
			    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));  
			    StringBuffer responseBody = new StringBuffer();  
			    String str_tmp= "";  
			    while((str_tmp = br.readLine()) != null){  
			    	responseBody .append(str_tmp);  
			    } 
				System.out.println("接口返回数据正常，responseBody="+responseBody);
				return responseBody.toString();
			}else {
				System.out.println("接口数据获取失败，请查看！status_code="+status_code);
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static String getPostResponseBody(HttpClient client,String url,JSONObject params,String[] str) {
		String Signature = GetHMACSHA256.getHMACSHA256(str,API_SECRET);
		PostMethod method = new PostMethod(url);
		client.getParams().setParameter("http.protocol.single-cookie-header", true);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		method.addRequestHeader("Content-Type","application/x-www-form-urlencoded");
		method.addRequestHeader("X-58COIN-APIKEY", X_58COIN_APIKEY);
		method.addRequestHeader("Signature", Signature);
		method.addRequestHeader("Timestamp",""+TIMESTAMP);
		//方法一
//		DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
//		方法二
		client.getParams().setParameter("http.protocol.single-cookie-header", true);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		Set<String> set = params.keySet();
		Iterator it = set.iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			method.addParameter(key, params.getString(key));
		}
		int status_code;
		try {
			status_code = client.executeMethod(method);
			if(status_code == HttpStatus.SC_OK) {
				System.out.println("接口获取成功,status_code="+status_code);
//				String responseBody = method.getResponseBodyAsString();
				InputStream inputStream = method.getResponseBodyAsStream();  
			    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));  
			    StringBuffer responseBody = new StringBuffer();  
			    String str_tmp= "";  
			    while((str_tmp = br.readLine()) != null){  
			    	responseBody .append(str_tmp);  
			    } 
				System.out.println("接口返回数据正常，responseBody="+responseBody);
				return responseBody.toString();
			}else {
				System.out.println("接口数据获取失败，请查看！status_code="+status_code);
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static String getGetResponseBody(HttpClient client,String url,String[] str) {
		String Signature = GetHMACSHA256.getHMACSHA256(str,API_SECRET);
		GetMethod method = new GetMethod(url);
		client.getParams().setParameter("http.protocol.single-cookie-header", true);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		method.addRequestHeader("Content-Type","application/x-www-form-urlencoded");
		method.addRequestHeader("X-58COIN-APIKEY", X_58COIN_APIKEY);
		method.addRequestHeader("Signature", Signature);
		method.addRequestHeader("Timestamp",""+TIMESTAMP);
		//方法一
//		DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
//		方法二
		client.getParams().setParameter("http.protocol.single-cookie-header", true);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		int status_code;
		try {
			status_code = client.executeMethod(method);
			if(status_code == HttpStatus.SC_OK) {
				System.out.println("接口获取成功,status_code="+status_code);
//				String responseBody = method.getResponseBodyAsString();
				InputStream inputStream = method.getResponseBodyAsStream();  
			    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));  
			    StringBuffer responseBody = new StringBuffer();  
			    String str_tmp= "";  
			    while((str_tmp = br.readLine()) != null){  
			    	responseBody .append(str_tmp);  
			    } 
				System.out.println("接口返回数据正常，responseBody="+responseBody);
				return responseBody.toString();
			}else {
				System.out.println("接口数据获取失败，请查看！status_code="+status_code);
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
