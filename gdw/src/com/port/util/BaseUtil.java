package com.port.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;

//import net.sf.json.JSONObject;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;


public class BaseUtil {	
	public static long TIMESTAMP ;	
	public static String X_58COIN_APIKEY = Config.API_KEY;
//	public static String X_58COIN_APIKEY = Config.API_CESHI_KEY;
//	public static String API_SECRET = Config.API_CESHI_SECRET;
	public static String API_SECRET = Config.API_SECRET;

	public static HttpClient client = new HttpClient();
	public static PostMethod post ;
	public static GetMethod get ;
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	public static String getResponse(HttpClient client,String url,String params) {
		GetMethod method = new GetMethod(url+"?"+params);
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
//				System.out.println("接口返回数据正常，responseBody="+responseBody);
				return responseBody.toString();
			}else {
//				System.out.println(params);
				System.out.println("接口数据获取失败，请查看！status_code="+status_code);
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	public static String getPostResponseBody(HttpClient client,String url,JSONObject params) {
		PostMethod method = new PostMethod(url);
		method.addRequestHeader("Content-Type","application/x-www-form-urlencoded");
//		method.addRequestHeader("ACCESS_TOKEN", "dIbsyvJGFKMR10vTrJbYDtKgI710UYeQg2tsPqZX2019");
		method.addRequestHeader("ACCESS_TOKEN", "OgGA9kFVi1aH036exnzUAALNV711JLkql8eSeN9B2019");
		//方法一
//		DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
//		方法二
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
	public static String getPostResponseBody_json(HttpClient client,String url,JSONObject params) {
		PostMethod method = new PostMethod(url);
		method.addRequestHeader("Content-Type","application/json");
		method.addRequestHeader("Authorization", "Bearer 472d6c4b-2ebc-e24b-4424-9e34-0ece454b320d");
		RequestEntity requestEntity = new StringRequestEntity(params.toString());
		method.setRequestEntity(requestEntity);
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
//				System.out.println("接口返回数据正常，responseBody="+responseBody);
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
	public static String getPostResponseBody(HttpClient client,String url) {
		PostMethod method = new PostMethod(url);
		method.addRequestHeader("Content-Type","application/x-www-form-urlencoded");
//		method.addRequestHeader("ACCESS_TOKEN", "dIbsyvJGFKMR10vTrJbYDtKgI710UYeQg2tsPqZX2019");
		method.addRequestHeader("ACCESS_TOKEN", "OgGA9kFVi1aH036exnzUAALNV711JLkql8eSeN9B2019");
		//方法一
//		DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
//		方法二
//		Set<String> set = params.keySet();
//		Iterator it = set.iterator();
//		while(it.hasNext()) {
//			String key = (String) it.next();
//			method.addParameter(key, params.getString(key));
//		}
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


	/**
	 * 发送表单
	 * @param url
	 * @param params
	 * @return
	 */
	public static String postByForm(String url, String params){
		post = new PostMethod(url) ;
		JSONObject p = JSONObject.parseObject(params);
		Set paramsKey = p.keySet();
		Iterator iterator = paramsKey.iterator();
		while (iterator.hasNext()){
			String key = String.valueOf(iterator.next());
			String value = p.getString(key);
			post.addParameter(key,value);
		}
		post.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		try {
			System.out.println("开始："+df.format(new Date()));// new Date()为获取当前系统时间
			int code = client.executeMethod(post);
			System.out.println("结束："+df.format(new Date()));// new Date()为获取当前系统时间
			InputStream respone = post.getResponseBodyAsStream();
			System.out.println("结束："+df.format(new Date()));// new Date()为获取当前系统时间
			BufferedReader br = new BufferedReader(new InputStreamReader(respone));
			String tempbf;
			StringBuffer re=new StringBuffer(100);
			while((tempbf=br.readLine())!=null){
				re.append(tempbf);
			}
			return re.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送带登录token的表单数据
	 * @param url
	 * @param authorization
	 * @param params
	 * @return
	 */
	public static String postByForm(String url, String authorization,String params){
		post = new PostMethod(url) ;
		JSONObject p = JSONObject.parseObject(params);
		Set paramsKey = p.keySet();
		Iterator iterator = paramsKey.iterator();
		while (iterator.hasNext()){
			String key = String.valueOf(iterator.next());
			String value = p.getString(key);
			post.addParameter(key,value);
		}
		post.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		post.setRequestHeader("Authorization",authorization);
		try {
			System.out.println("开始："+df.format(new Date()));// new Date()为获取当前系统时间
			int code = client.executeMethod(post);
			System.out.println("结束："+df.format(new Date()));// new Date()为获取当前系统时间
			InputStream respone = post.getResponseBodyAsStream();
			System.out.println("结束："+df.format(new Date()));// new Date()为获取当前系统时间
			BufferedReader br = new BufferedReader(new InputStreamReader(respone));
			String tempbf;
			StringBuffer re=new StringBuffer(100);
			while((tempbf=br.readLine())!=null){
				re.append(tempbf);
			}
			return re.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送json格式的数据
	 * @param url
	 * @param params
	 * @return
	 */
	public static String postByJson(String url, String params){
		post = new PostMethod(url) ;
		RequestEntity se = null;
		try {
			se = new StringRequestEntity(params,"application/json" ,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		post.setRequestEntity(se);
		post.setRequestHeader("Content-Type","application/json");
		try {
			System.out.println("开始："+df.format(new Date()));// new Date()为获取当前系统时间
			int code = client.executeMethod(post);
			System.out.println("结束："+df.format(new Date()));// new Date()为获取当前系统时间
			InputStream respone = post.getResponseBodyAsStream();
			System.out.println("结束："+df.format(new Date()));// new Date()为获取当前系统时间
			BufferedReader br = new BufferedReader(new InputStreamReader(respone));
			String tempbf;
			StringBuffer re=new StringBuffer(100);
			while((tempbf=br.readLine())!=null){
				re.append(tempbf);
			}
			return re.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送带token的json数据
	 * @param url
	 * @param authorization
	 * @param params
	 * @return
	 */
	public static String postByJson(String url,String authorization, String params){
		post = new PostMethod(url) ;
		RequestEntity se = null;
		try {
			se = new StringRequestEntity(params,"application/json" ,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		post.setRequestEntity(se);
		post.setRequestHeader("Content-Type","application/json");
		post.setRequestHeader("Authorization",authorization);
		try {
			System.out.println("开始："+df.format(new Date()));// new Date()为获取当前系统时间
			int code = client.executeMethod(post);
			System.out.println("结束："+df.format(new Date()));// new Date()为获取当前系统时间
			InputStream respone = post.getResponseBodyAsStream();
//            System.out.println("结束："+df.format(new Date()));// new Date()为获取当前系统时间
			BufferedReader br = new BufferedReader(new InputStreamReader(respone));
			String tempbf;
			StringBuffer re=new StringBuffer(100);
			while((tempbf=br.readLine())!=null){
				re.append(tempbf);
			}
			return re.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送表单数据
	 * @param url
	 * @param params
	 * @return
	 */
	public static String getByForm(String url, String params){
		get = new GetMethod(url) ;
		try {
			int code = client.executeMethod(get);
			InputStream respone = get.getResponseBodyAsStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(respone));
			String tempbf;
			StringBuffer re=new StringBuffer(100);
			while((tempbf=br.readLine())!=null){
				re.append(tempbf);
			}
			return re.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
