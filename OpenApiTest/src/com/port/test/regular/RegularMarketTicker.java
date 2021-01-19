package com.port.test.regular;

import java.io.IOException;

import org.apache.commons.httpclient.methods.GetMethod;
import org.testng.annotations.Test;

import com.port.util.BaseUtil;
import com.port.util.Config;

public class RegularMarketTicker extends BaseUtil{
	@Test
	public void test_regularMarketTicker() {
		String url = Config.REST_CESHI_API+"/v1/regular/market/ticker?contractId=2001";
		GetMethod method = new GetMethod(url);
		try {
			client.executeMethod(method);
			System.out.println(method.getResponseBodyAsString());
			System.out.println(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
