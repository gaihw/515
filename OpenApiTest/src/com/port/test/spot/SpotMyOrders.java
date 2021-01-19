package com.port.test.spot;

import org.testng.annotations.Test;

import com.port.util.BaseUtil;
import com.port.util.Config;

public class SpotMyOrders extends BaseUtil{
	@Test
	public void test_spotMyOrders() {
		String url = Config.REST_CESHI_API+"/v1/spot/my/orders?symbol=btc_usdt&status=finished&page=&limit=10&isHistory=false";
		String[] str = {"symbol=btc_usdt","status=finished","page=","limit=10","isHistory=false",Config.ACCESSKEYID+"="+X_58COIN_APIKEY,Config.SIGNATUREMETHOD+"="+
				Config.HMACSHA256,Config.SIGNATUREVERSION+"="+Config.TWO,Config.TIMESTAMP+"="+TIMESTAMP};
		System.out.println(getGetResponseBody(client, url, str));
	}
}
