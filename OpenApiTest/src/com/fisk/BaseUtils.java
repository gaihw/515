package com.fisk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import net.sf.json.JSONObject;

public class BaseUtils {
	public static HttpClient client;
	public static String responseBody(HttpClient client,String url,JSONObject params) {
		PostMethod method = new PostMethod(url);
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
}
