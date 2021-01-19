package com.test;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;

import com.port.util.GetCookie;

import net.sf.json.JSONObject;

public class Login {

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
//		接口地址：http://test-tbex-cfd-web-controller.cfdtest.lianbe.com/user/login
//		请求参数：googleCode=&user_name=14412345678&password=12345678&ticket=&device=web
		HttpClient client = new HttpClient();
		String url = "http://test-tbex-cfd-web-controller.cfdtest.lianbe.com/user/login";
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("content type", "application/x-www-form-urlencoded");
		method.addParameter("googleCode", "");
		method.addParameter("user_name", "13020071928");
		method.addParameter("password", "12345678");
		method.addParameter("ticket", "");
		method.addParameter("device", "web");
//		NameValuePair[] params = { new NameValuePair("googleCode", ""),new NameValuePair("user_name", "14412345678"),new NameValuePair("password", "12345678"),new NameValuePair("ticket", ""),new NameValuePair("device", "web" )};
//		method.setRequestBody(params);
		int status_code;
		try {
			status_code = client.executeMethod(method);
			if(status_code == 200) {
				System.out.println("接口获取成功,status_code="+status_code);
				//获取接口返回内容
				String response = method.getResponseBodyAsString();	
				//获取data值
				JSONObject data = JSONObject.fromObject(response).getJSONObject("data");
				String accessToken = data.getString("accessToken");
				System.out.println("接口返回数据正常，accessToken="+accessToken);
			}else {
				System.out.println("接口数据获取失败，请查看！status_code="+status_code);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println(GetCookie.getCookie(client, url, params));
	}
}
