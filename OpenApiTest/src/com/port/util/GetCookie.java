package com.port.util;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;

public class GetCookie {
	public static String getCookie(HttpClient client,String url,NameValuePair[] params) {
		// 模拟登陆，按实际服务器端要求选用 Post 或 Get 请求方式
		PostMethod postMethod = new PostMethod(url);
		// 设置登陆时要求的信息，用户名和密码
		postMethod.setRequestBody(params);
		int status_code;
		try {
			client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			status_code = client.executeMethod(postMethod);
			// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
			if(status_code == HttpStatus.SC_OK) {				
				System.out.println("登录接口状态码status="+status_code);
				// 获得登陆后的 Cookie
				Cookie[] cookies = client.getState().getCookies();
				StringBuffer tmpcookies = new StringBuffer();
				for (Cookie c : cookies) {
					tmpcookies.append(c.toString() + ";");
				}
				System.out.println("cookie获取成功<"+tmpcookies.toString()+">");
				return tmpcookies.toString();
			}else {
				System.out.println("登录接口失败，状态码status="+status_code);
				return null;
			}
		} catch (Exception e) {
			System.out.println("登录接口失败，请查看！");
			e.printStackTrace();	
			return null;
		}
	}
}
