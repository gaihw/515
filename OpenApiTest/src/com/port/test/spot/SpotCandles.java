package com.port.test.spot;

import org.testng.annotations.Test;

import com.port.util.BaseUtil;
import com.port.util.Config;

public class SpotCandles extends BaseUtil{
	@Test
	public void test_spotCandles() {
		String url = Config.REST_CESHI_API+"/v1/spot/candles?symbol=ltc_btc&period=1min&since=1561719869&limit=2";
		String[] str = {"symbol=btc_usdt","period=1min","since=1561719869","limit=1",Config.ACCESSKEYID+"="+X_58COIN_APIKEY,Config.SIGNATUREMETHOD+"="+
				Config.HMACSHA256,Config.SIGNATUREVERSION+"="+Config.TWO,Config.TIMESTAMP+"="+TIMESTAMP};
		System.out.println(getGetResponseBody(client, url, str));
	}
}
