package com.port.test.acount;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.DefaultHttpParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.testng.annotations.Test;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.cookie.CookiePolicy;

import com.port.util.BaseUtil;
import com.port.util.Config;
import com.port.util.GetHMACSHA256;

public class AcountAssetsSites extends BaseUtil{
	@Test
	public void test_acountAssetsSites(){	
//		String[] str = {"siteId=1",Config.ACCESSKEYID+"="+X_58COIN_APIKEY,Config.SIGNATUREMETHOD+"="+
//		Config.HMACSHA256,Config.SIGNATUREVERSION+"="+Config.TWO,Config.TIMESTAMP+"="+TIMESTAMP};
//		System.out.println(getGetResponseBody(client, url, str));	
//		/v1/swaps/position/get?contractId=1
		String url = Config.REST_CESHI_API+"/v1/swaps/order/fill/list?contractId=1&beginDate=2019-11-15%2010%3A00%3A00&endDate=2019-12-18%2010%3A00%3A00&page=1&pageSize=10";
		String[] str = {"contractId=1","beginDate=2019-11-15%2010%3A00%3A00","endDate=2019-12-18%2010%3A00%3A00","page=1","pageSize=10",Config.ACCESSKEYID+"="+X_58COIN_APIKEY,Config.SIGNATUREMETHOD+"="+
				Config.HMACSHA256,Config.SIGNATUREVERSION+"="+Config.TWO,Config.TIMESTAMP+"="+TIMESTAMP};
		System.out.println(getGetResponseBody(client, url, str));		
	}
}
