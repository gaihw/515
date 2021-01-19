package com.test;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.port.util.Config;
import com.port.util.GetHMACSHA256;

public class OrderTest {
	public static long TIMESTAMP = System.currentTimeMillis();

	public static void main(String[] args) throws IOException  {
//		API Key：cbcbbc94-09c9-49c8-bfbb-70ecc4ab2cd0
//		Secret Key：3D2A7BA48C3B55E01E64906250DA6ED4
//		IP 白名单：182.50.114.226
//		/v1/spot/my/order
//		X-58COIN-APIKEY
//		Signature
//		https://openapi.58coin.com
//		symbol	string	yes	货币对的名称
//		order_id	string	yes	订单的ID
//		AccessKeyId=089cf604-7b87-4b13-b806-eaadb67c8b70&SignatureMethod=HmacSHA256&SignatureVersion=2&Timestamp=1551084749915
		String X_58COIN_APIKEY = "cbcbbc94-09c9-49c8-bfbb-70ecc4ab2cd0";
		String[] str = {"contractId=2001","action=1","amount=100","AccessKeyId="+X_58COIN_APIKEY,"SignatureMethod=HmacSHA256","SignatureVersion=2","Timestamp="+TIMESTAMP};
		String API_SECRET = "3D2A7BA48C3B55E01E64906250DA6ED4";
		String Signature = GetHMACSHA256.getHMACSHA256(str,API_SECRET);
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("https://openapi.58ex.com/v1/regular/account/transfer");	
		method.addRequestHeader("Content-Type","application/x-www-form-urlencoded");
		method.addRequestHeader("X_58COIN_APIKEY", X_58COIN_APIKEY);
		method.addRequestHeader("Signature", Signature);
		method.addRequestHeader("Timestamp", ""+TIMESTAMP);
		method.addParameter("contractId", "2001");
		method.addParameter("action", "1");
		method.addParameter("amount", "100");
		System.out.println(TIMESTAMP);
		System.out.println(client.executeMethod(method));
		System.out.println(method.getResponseBodyAsString()+"aaaa");
		
	}

}
