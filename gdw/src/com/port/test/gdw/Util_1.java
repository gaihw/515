package com.port.test.gdw;

import java.math.BigDecimal;

import net.sf.json.JSONObject;

public class Util_1 {
	public static void main(String[] args) {
		BigDecimal one = new BigDecimal("0.6");
		BigDecimal two = new BigDecimal("1.4");
		String param_1 = "{\"volume\":\"21491\",\"high\":\"8071.81\",\"amount\":\"173419341.99\",\"low\":\"8067.26\",\"id\":\"1569744005371\",\"time\":\"1569744000000\",\"type\":\"5min\",\"close\":\"8071.81\",\"open\":\"8068.05\"}\",\"platform\":\"COIN58\",\"product\":\"2001\"}";
		String param_1_1 = "{\n" + 
				"  \"avgNumber\": 0.00109417,\n" + 
				"  \"count\": 1019,\n" + 
				"  \"lastKLineTime\": \"1569744000000\",\n" + 
				"  \"sum\": \"1.11496353\"\n" + 
				"}";
		JSONObject param_temp = JSONObject.fromObject(param_1);
		double high = param_temp.getDouble("high");
		double low = param_temp.getDouble("low");
		double open = param_temp.getDouble("open");
		double close = param_temp.getDouble("close");
		
		BigDecimal high_temp = new BigDecimal(high);
		BigDecimal low_temp = new BigDecimal(low);
		BigDecimal open_temp = new BigDecimal(open);
		BigDecimal close_temp = new BigDecimal(close);
		BigDecimal high_low = new BigDecimal(String.valueOf(high_temp.subtract(low_temp).setScale(8, BigDecimal.ROUND_HALF_UP)));
		BigDecimal emplitude = high_low.divide(open_temp,10,BigDecimal.ROUND_HALF_UP).setScale(8,BigDecimal.ROUND_HALF_UP);		
		JSONObject param_temp_0 = JSONObject.fromObject(param_1_1);
		double avgNumber = param_temp_0.getDouble("avgNumber");
		BigDecimal avgNumber_temp = new BigDecimal(avgNumber);
		System.out.println("1分钟振幅="+emplitude);
		System.out.println("1分钟振幅平均值="+avgNumber);
		System.out.println("1分钟振幅-1分钟振幅平均值="+emplitude.subtract(avgNumber_temp.multiply(two)));
	}
}
