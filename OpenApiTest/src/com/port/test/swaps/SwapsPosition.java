package com.port.test.swaps;

import com.port.util.BaseUtil;
import com.port.util.Config;
import org.testng.annotations.Test;

public class SwapsPosition extends BaseUtil{
	@Test
	public void test_swapsPosition(){
		String url = Config.REST_CESHI_API+"/v1/swaps/position/get?contractId=1";
		String[] str = {"contractId=1",Config.ACCESSKEYID+"="+X_58COIN_APIKEY,Config.SIGNATUREMETHOD+"="+
				Config.HMACSHA256,Config.SIGNATUREVERSION+"="+Config.TWO,Config.TIMESTAMP+"="+TIMESTAMP};
		System.out.println(getGetResponseBody(client, url, str));		
	}
}
