package com.test;

import java.io.IOException;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;

import com.port.util.GetCookie;

public class TickerAll {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("https://api.58ex.com/market/ticker/all");
		method.setRequestHeader("ACCESS_TOKEN", "Sq5ziQSQkI5y19ITqda9RLNlNNWms3C9ZYreVw");
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		try {
			client.executeMethod(method);
			System.out.println(method.getResponseBodyAsString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
