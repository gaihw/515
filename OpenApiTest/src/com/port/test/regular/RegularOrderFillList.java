package com.port.test.regular;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.testng.annotations.Test;

import com.port.util.BaseUtil;
import com.port.util.Config;
import com.port.util.GetHMACSHA256;

public class RegularOrderFillList extends BaseUtil{
	@Test
	public void test_regularOrderFillList() {
		String url = Config.REST_CESHI_API+"/v1/regular/order/fill/list?contractId=2001&close=1&limit=10";
		String[] str = {"contractId=2001","close=1","limit=10",Config.ACCESSKEYID+"="+X_58COIN_APIKEY,
				Config.SIGNATUREMETHOD+"="+Config.HMACSHA256,Config.SIGNATUREVERSION+"="+Config.TWO,Config.TIMESTAMP+"="+TIMESTAMP};
		System.out.println(getGetResponseBody(client, url, str));
	}
}
