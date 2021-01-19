package com.port.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import net.sf.json.JSONObject;

public class AccessToken {
//		接口地址：http://test-tbex-cfd-web-controller.cfdtest.lianbe.com/user/login
//		请求参数：googleCode=&user_name=14412345678&password=12345678&ticket=&device=web
	public static String getAccessToken(HttpClient client,String url,JSONObject params) {
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
				String accessToken = JSONObject.fromObject(method.getResponseBodyAsString()).getJSONObject("data").getString("accessToken");
				System.out.println("接口返回数据正常，accessToken="+accessToken);
				return accessToken;
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
