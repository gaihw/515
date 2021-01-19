package com.port.test.regular;

import org.testng.annotations.Test;

import com.port.util.BaseUtil;
import com.port.util.Config;

import net.sf.json.JSONObject;

public class RegularAccountTransfer extends BaseUtil{
	@Test
	public void test_regularAccountTransfer() {
		String url = Config.REST_CESHI_API+"/v1/regular/account/transfer";
		String[] str = {"amount=333.22","contractId=2001","action=2",Config.ACCESSKEYID+"="+X_58COIN_APIKEY,
				Config.SIGNATUREMETHOD+"="+Config.HMACSHA256,Config.SIGNATUREVERSION+"="+Config.TWO,Config.TIMESTAMP+"="+TIMESTAMP};
		JSONObject params = new JSONObject();
		params.put("amout", "333.22");
		params.put("contractId", "2001");
		params.put("action", "2");
		System.out.println(getPostResponseBody(client, url, params, str));
	}
}
