package com.port.test.regular;

import java.io.IOException;

import org.apache.commons.httpclient.methods.GetMethod;
import org.testng.annotations.Test;

import com.port.util.BaseUtil;
import com.port.util.Config;

public class RegularGuideSettlementList extends BaseUtil{
	@Test
	public void regularGuideSettlementList() {
		GetMethod method = new GetMethod(Config.REST_CESHI_API+"/v1/regular/guide/settlement/list");
		method.addRequestHeader("contractIds", "2001");
		method.addRequestHeader("limit", "1");
		try {
			client.executeMethod(method);
			System.out.println(method.getResponseBodyAsString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
