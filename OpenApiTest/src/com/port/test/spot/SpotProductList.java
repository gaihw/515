package com.port.test.spot;

import org.testng.annotations.Test;

import com.port.util.BaseUtil;
import com.port.util.Config;

public class SpotProductList extends BaseUtil{
	@Test
	public void test_spotProductList() {
		String url = Config.REST_CESHI_API+"/v1/spot/product/list?symbol=btc_usdt";
		String[] str = {"symbol=btc_usdt",Config.ACCESSKEYID+"="+X_58COIN_APIKEY,Config.SIGNATUREMETHOD+"="+
				Config.HMACSHA256,Config.SIGNATUREVERSION+"="+Config.TWO,Config.TIMESTAMP+"="+TIMESTAMP};
		System.out.println(getGetResponseBody(client, url, str));
	}
}
