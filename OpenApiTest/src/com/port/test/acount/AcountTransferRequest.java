package com.port.test.acount;

import org.testng.annotations.Test;

import com.port.util.BaseUtil;
import com.port.util.Config;

import net.sf.json.JSONObject;

public class AcountTransferRequest extends BaseUtil{
	@Test
	public void test_acountTransferRequest() {
		String[] str = {"currencyName=BTC","triggerSiteId=1","targetSiteId=3","amount=10","AccessKeyId="+X_58COIN_APIKEY,"SignatureMethod=HmacSHA256","SignatureVersion=2","Timestamp="+TIMESTAMP};
		String url = Config.REST_CESHI_API+"/v1/account/transfer/request";
		JSONObject params = new JSONObject();
		params.put("currencyName", "BTC");
		params.put("triggerSiteId", 1);
		params.put("targetSiteId", 3);
		params.put("amount", "10");
		String responseBody = getPostResponseBody(client,url,params,str);
		System.out.println(responseBody);
	}

}
