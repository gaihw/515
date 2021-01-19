package com.port.test.spot;

import org.testng.annotations.Test;

import com.port.util.BaseUtil;
import com.port.util.Config;

public class SpotTicker extends BaseUtil{
	@Test
	public void test_spotTickerPrice() {		
		String url = Config.REST_CESHI_API+"/v1/spot/ticker?symbol=ltc_btc";
		String[] str = {"symbol=ltc_btc",Config.ACCESSKEYID+"="+X_58COIN_APIKEY,Config.SIGNATUREMETHOD+"="+
				Config.HMACSHA256,Config.SIGNATUREVERSION+"="+Config.TWO,Config.TIMESTAMP+"="+TIMESTAMP};
		System.out.println(getGetResponseBody(client, url, str));
		
	}
}
