package com.port.test.gdw;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import net.sf.json.JSONObject;

public class Util_5 {
	public static void main(String[] args) {
//		BigDecimal one = new BigDecimal("0.6");
//		BigDecimal two = new BigDecimal("1.4");
//		
//		BigDecimal a_1_15 = new BigDecimal("1.15");
//		BigDecimal a_1_2 = new BigDecimal("1.2");
//		BigDecimal a_1_85 = new BigDecimal("0.85");
//		BigDecimal a_1_8 = new BigDecimal("0.8");
		
		String param_5 = "{\n" + 
				"            \"id\":\"1569571504808\",\n" + 
				"            \"open\":\"8046.79000000000000000000\",\n" + 
				"            \"high\":\"8048.64000000000000000000\",\n" + 
				"            \"low\":\"8046.79000000000000000000\",\n" + 
				"            \"close\":\"8048.64000000000000000000\",\n" + 
				"            \"volume\":\"95568.00000000000000000000\",\n" + 
				"            \"amount\":\"769095106.42000000000000000000\",\n" + 
				"            \"productId\":\"2001\",\n" + 
				"            \"type\":\"2\",\n" + 
				"            \"time\":\"1569571500000\"\n" + 
				"        }";
		String param_5_1 = "{\n" + 
				"    \"avgNumber\":0.00036914,\n" + 
				"    \"count\":1073,\n" + 
				"    \"lastKLineTime\":\"1569571500000\",\n" + 
				"    \"sum\":\"0.39609521\"\n" + 
				"}";
		JSONObject param_temp = JSONObject.fromObject(param_5);
		double high = param_temp.getDouble("high");
		double low = param_temp.getDouble("low");
		double open = param_temp.getDouble("open");
		double close = param_temp.getDouble("close");
		double amount = param_temp.getDouble("amount");
		JSONObject param_temp_0 = JSONObject.fromObject(param_5_1);
		double avgNumber = param_temp_0.getDouble("avgNumber");
		DecimalFormat df=new DecimalFormat("0.00000000000000000000");//设置保留位数		 
		double emplitude = Double.valueOf(df.format((float)(high-low)/open));
		if(emplitude >= avgNumber*1.4) {
			System.out.println("emplitude - avgNumber*1.4="+(emplitude - avgNumber*1.4));
			if(close >= open) {
				System.out.println("买入="+new BigDecimal(Double.valueOf(df.format(amount*(float)(1+0.15)/2))).setScale(8,BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(1+0.2)/2))).setScale(8, BigDecimal.ROUND_DOWN));
				System.out.println("卖出="+new BigDecimal(Double.valueOf(df.format(amount*(float)(1-0.2)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(1-0.15)/2))).setScale(8, BigDecimal.ROUND_DOWN));
				System.out.println("买入="+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.15)/2))).setScale(8,BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.2)/2))).setScale(8, BigDecimal.ROUND_DOWN));
				System.out.println("卖出="+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.2)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.15)/2))).setScale(8, BigDecimal.ROUND_DOWN));
			}else {
				System.out.println("买入="+new BigDecimal(Double.valueOf(df.format(amount*(float)(1-0.2)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(1-0.15)/2))).setScale(8, BigDecimal.ROUND_DOWN));				
				System.out.println("卖出="+new BigDecimal(Double.valueOf(df.format(amount*(float)(1+0.15)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(1+0.2)/2))).setScale(8, BigDecimal.ROUND_DOWN));				
				System.out.println("买入="+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.2)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.15)/2))).setScale(8, BigDecimal.ROUND_DOWN));				
				System.out.println("卖出="+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.15)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.2)/2))).setScale(8, BigDecimal.ROUND_DOWN));				
			}
		}else if(emplitude < avgNumber*1.4 && emplitude >= avgNumber*0.6) {
			System.out.println("avgNumber*1.4 - emplitude ="+(emplitude - avgNumber*1.4)+";emplitude - avgNumber*0.6="+(emplitude - avgNumber*0.6));
			if(close >= open) {
				System.out.println("买入="+new BigDecimal(Double.valueOf(df.format(amount*(float)(1+0.10)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(1+0.15)/2))).setScale(8, BigDecimal.ROUND_DOWN));
				System.out.println("卖出="+new BigDecimal(Double.valueOf(df.format(amount*(float)(1-0.15)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(1-0.1)/2))).setScale(8, BigDecimal.ROUND_DOWN));
				System.out.println("买入="+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.10)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.15)/2))).setScale(8, BigDecimal.ROUND_DOWN));
				System.out.println("卖出="+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.15)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.1)/2))).setScale(8, BigDecimal.ROUND_DOWN));
			}else {
				System.out.println("买入="+new BigDecimal(Double.valueOf(df.format(amount*(float)(1-0.15)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(1-0.1)/2))).setScale(8, BigDecimal.ROUND_DOWN));				
				System.out.println("卖出="+new BigDecimal(Double.valueOf(df.format(amount*(float)(1+0.10)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(1+0.15)/2))).setScale(8, BigDecimal.ROUND_DOWN));				
				System.out.println("买入="+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.15)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.1)/2))).setScale(8, BigDecimal.ROUND_DOWN));				
				System.out.println("卖出="+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.10)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.15)/2))).setScale(8, BigDecimal.ROUND_DOWN));				
			}
		}else if(emplitude < avgNumber*0.6) {
				System.out.println("avgNumber*0.6 - emplitude = "+(avgNumber*0.6 - emplitude));
				System.out.println("买入="+new BigDecimal(Double.valueOf(df.format(amount*(float)(1-0.1)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(1+0.1)/2))).setScale(8, BigDecimal.ROUND_DOWN));
				System.out.println("卖出="+new BigDecimal(Double.valueOf(df.format(amount*(float)(1+0.1)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(1-0.1)/2))).setScale(8, BigDecimal.ROUND_DOWN));
				System.out.println("买入="+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.1)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.1)/2))).setScale(8, BigDecimal.ROUND_DOWN));
				System.out.println("卖出="+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.1)/2))).setScale(8, BigDecimal.ROUND_DOWN)+"~"+new BigDecimal(Double.valueOf(df.format(amount*(float)(0.1)/2))).setScale(8, BigDecimal.ROUND_DOWN));
		}
		
	}
}
