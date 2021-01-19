package com.port.test.spot;

import org.testng.annotations.Test;

import com.port.util.BaseUtil;
import com.port.util.Config;

public class SpotOrder_book extends BaseUtil{
	@Test
	public void test_spotOrder_book() {
		String url = Config.REST_CESHI_API+"/v1/spot/order_book?symbol=btc_usdt&limit=10";
		String[] str = {"symbol=btc_usdt","limit=10",Config.ACCESSKEYID+"="+X_58COIN_APIKEY,Config.SIGNATUREMETHOD+"="+
				Config.HMACSHA256,Config.SIGNATUREVERSION+"="+Config.TWO,Config.TIMESTAMP+"="+TIMESTAMP};
		System.out.println(getGetResponseBody(client, url, str));
	}
}	
