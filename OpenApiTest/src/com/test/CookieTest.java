package com.test;

import java.io.IOException;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.port.util.AccessToken;
import com.port.util.Config;
import com.port.util.GetCookie;

import net.sf.json.JSONObject;

public class CookieTest {
	public static HttpClient client = new HttpClient();
 
	public static void main(String[] args) {
//		get_Cookie1();
//		get_v1_login();
//		get_WithCookie();
//		get_ParametertListTest();
//		get_58Cookie();
//		get_58BTCCookie();
		restTemplate();
	}
	public static void restTemplate() {
		GetMethod method = new GetMethod("http://localhost:8890/get/twolisttest");
		try {
			client.executeMethod(method);
			System.out.println(method.getResponseBodyAsString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void get_58BTCCookie() {
		GetMethod method = new GetMethod("http://58btc.udesk.cn/spa1/im_web_plugins/38854/out_config?company_code=22ab1akk&language=&session_key=&callback=udesk_jsonp0");
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		try {
			client.executeMethod(method);
			Cookie[] cookies = client.getState().getCookies();
			StringBuffer tmpcookies = new StringBuffer();
			for (Cookie c : cookies) {
				tmpcookies.append(c.toString() + ";");
			}
//			System.out.println(method.getResponseBodyAsString());
			System.out.println("tmpcookies==="+tmpcookies);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static void get_58Cookie() {
		String access_token_url = Config.ACCESS_TOKEN_TEST_URL;
		NameValuePair[] params = {new NameValuePair("googleCode", ""),new NameValuePair("user_name", "13020071928"),
				new NameValuePair("password", "12345678"),new NameValuePair("ticket", ""),new NameValuePair("device", "web")
				};
//		JSONObject access_token_params = new JSONObject();
//		access_token_params.put("googleCode", "");
//		access_token_params.put("user_name", "13020071928");
//		access_token_params.put("password", "12345678");
//		access_token_params.put("ticket", "");
//		access_token_params.put("device", "web");
		
		GetCookie.getCookie(client, access_token_url, params);
		
	}
	//通过在请求头添加cookie信息，来请求接口
	private static void get_ParametertListTest() {
		// TODO Auto-generated method stub
		GetMethod method = new GetMethod("http://localhost:8890/get/parameter/?start=1&end=2");
		method.setRequestHeader("cookie", "gaihongwei=111111;gaihongwei01=000000");
		try {
			client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			client.executeMethod(method);
			// 获得登陆后的 Cookie
			Cookie[] cookies = client.getState().getCookies();
			StringBuffer tmpcookies = new StringBuffer();
			for (Cookie c : cookies) {
				System.out.println(c.toString());
				tmpcookies.append(c.toString() + ";");
			}
			System.out.println(method.getResponseBodyAsString());
			System.out.println("cookies==="+tmpcookies);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//通过在请求头添加cookie信息，来请求接口
	private static void get_WithCookie() {
		GetMethod method = new GetMethod("http://localhost:8890/get/with/cookie");
		GetMethod method1 = new GetMethod("http://localhost:8890/get/parameter?start=1&end=2");//获取cookie，再访问下一个接口
		method1.setRequestHeader("cookie", "gaihongwei=111111;gaihongwei01=000000");
		try {
			client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			client.executeMethod(method1);
			// 获得登陆后的 Cookie
			Cookie[] cookies = client.getState().getCookies();
			StringBuffer tmpcookies = new StringBuffer();
			for (Cookie c : cookies) {
				System.out.println(c.toString());
				tmpcookies.append(c.toString() + ";");
			}
		method.setRequestHeader("cookie", tmpcookies.toString());
			client.executeMethod(method);
			System.out.println(method.getResponseBodyAsString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//登录获取cookie
	private static void get_v1_login() {
		// TODO Auto-generated method stub
		PostMethod method = new PostMethod("http://localhost:8890/v1/post/login");
		method.addParameter("username", "gaihongwei");
		method.addParameter("password", "111111");
		try {
//			client.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
			client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			client.executeMethod(method);
			// 获得登陆后的 Cookie
			Cookie[] cookies = client.getState().getCookies();
			StringBuffer tmpcookies = new StringBuffer();
			for (Cookie c : cookies) {
				tmpcookies.append(c.toString() + ";");
			}
			PostMethod method1 = new PostMethod("http://localhost:8890/v1/post/userlist");
			String params = "{\"username\":\"zhangsan\",\"password\":\"123456\",\"name\":\"gaihongwei\"}";
			RequestEntity entity ;
			entity = new StringRequestEntity(params, "application/json", "utf-8");
			method1.setRequestEntity(entity);
			method1.addRequestHeader("cookie", tmpcookies.toString());
			client.executeMethod(method1);
			System.out.println(method1.getResponseBodyAsString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public static void get_Cookie1() {
		// 登陆 Url
		String loginUrl = "";
		// 需登陆后访问的 Url
		String dataUrl = ""; 
		// 模拟登陆，按实际服务器端要求选用 Post 或 Get 请求方式
		PostMethod postMethod = new PostMethod(loginUrl);
		// 设置登陆时要求的信息，用户名和密码
		NameValuePair[] data = { new NameValuePair("name", "admin"),new NameValuePair("password", "123456") };
		postMethod.setRequestBody(data);
		try {
			// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
			client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			client.executeMethod(postMethod);
			// 获得登陆后的 Cookie
			Cookie[] cookies = client.getState().getCookies();
			StringBuffer tmpcookies = new StringBuffer();
			for (Cookie c : cookies) {
				tmpcookies.append(c.toString() + ";");
			}
			// 进行登陆后的操作1581,1602,1603,1610,1609,1608,1607,1606,1605,1620,1619,1617,1616,1622,1626,1642,1648,1647,1657
			GetMethod getMethod = new GetMethod(dataUrl);
			// 每次访问需授权的网址时需带上前面的 cookie 作为通行证
			getMethod.setRequestHeader("cookie", tmpcookies.toString());
			// 你还可以通过 PostMethod/GetMethod 设置更多的请求后数据
			// 例如，referer 从哪里来的，UA 像搜索引擎都会表名自己是谁，无良搜索引擎除外
			postMethod.setRequestHeader("Referer", "http://www.cc");
			postMethod.setRequestHeader("User-Agent", "www Spot");
			client.executeMethod(getMethod);
			// 打印出返回数据，检验一下是否成功
			String text = getMethod.getResponseBodyAsString();
			System.out.println(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
