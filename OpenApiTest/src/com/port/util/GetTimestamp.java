package com.port.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;

import net.sf.json.JSONObject;

public class GetTimestamp {

	public static String getTimestamp(HttpClient client,String ip)  {
		GetMethod method = new GetMethod(ip+"/v1/base/timestamp");
		client.getParams().setParameter("http.protocol.single-cookie-header", true);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		int status_code;
		try {
			status_code = client.executeMethod(method);
			if(status_code == HttpStatus.SC_OK) {
				System.out.println("获取服务器时间接口成功，status_code="+status_code);
				InputStream inputStream = method.getResponseBodyAsStream();  
			    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));  
			    StringBuffer responseBody = new StringBuffer();  
			    String str_tmp= "";  
			    while((str_tmp = br.readLine()) != null){  
			    	responseBody.append(str_tmp);  
			    } 
				System.out.println("接口返回数据正常，responseBody="+responseBody);
				JSONObject response = JSONObject.fromObject(responseBody.toString());
				if(response.equals("")||response.equals(null)||response.isEmpty()) {
					System.out.println("接口返回数据为空，或者不是json格式，请查看！<"+responseBody+">");
					return null;
				}else {
					String timestamp = response.getString("data");
					System.out.println("获取服务器时间成功，timestamp="+timestamp);
					return timestamp;
				}
			}else {
				System.out.println("获取服务器时间接口失败，status_code="+status_code);
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("获取服务器时间接口失败，请查看！");
			e.printStackTrace();
			return null;
		}
	}
}
