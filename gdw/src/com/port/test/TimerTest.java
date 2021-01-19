package com.port.test;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {
	public static void main(String[] args) {
//		BigDecimal a = new BigDecimal(50.6666665);
//		BigDecimal b = new BigDecimal(20);
		
//		System.out.println(a.add(b));
//		System.out.println(a.subtract(b));
//		System.out.println(a.multiply(b));
//		System.out.println(a.divide(b));
//		System.out.println(a.setScale(3, BigDecimal.ROUND_CEILING));
//		System.out.println(a.setScale(3, BigDecimal.ROUND_DOWN));
//		System.out.println(a.setScale(3, BigDecimal.ROUND_FLOOR));
//		System.out.println(a.setScale(3, BigDecimal.ROUND_HALF_DOWN));
//		System.out.println(a.setScale(3, BigDecimal.ROUND_HALF_EVEN));
//		System.out.println(a.setScale(3, BigDecimal.ROUND_HALF_UP));
//		System.out.println(a.setScale(3, BigDecimal.ROUND_UP));
//		String aa = "26666666666666667777777774443333334";
//		System.out.println(new BigDecimal(aa).setScale(0));
//		new BigDecimal(params).setScale(0, BigDecimal.ROUND_DOWN);

		String data1 = "{\"code\":\"0000\",\"message\":\"成功\",\"data\":[{\"id\":\"7394940\",\"open\":\"2.91060000000000000000\",\"high\":\"2.91060000000000000000\",\"low\":\"2.91060000000000000000\",\"close\":\"2.91060000000000000000\",\"volume\":\"0.00\",\"amount\":\"0.00\",\"productId\":\"1002\",\"type\":\"2\",\"time\":\"1574323500000\"},{\"id\":\"7395018\",\"open\":\"2.91060000000000000000\",\"high\":\"2.91060000000000000000\",\"low\":\"2.91060000000000000000\",\"close\":\"2.91060000000000000000\",\"volume\":\"0.00\",\"amount\":\"0.00\",\"productId\":\"1002\",\"type\":\"2\",\"time\":\"1574323800000\"},{\"id\":\"7395104\",\"open\":\"2.91060000000000000000\",\"high\":\"2.91060000000000000000\",\"low\":\"2.91060000000000000000\",\"close\":\"2.91060000000000000000\",\"volume\":\"0.00\",\"amount\":\"0.00\",\"productId\":\"1002\",\"type\":\"2\",\"time\":\"1574324100000\"},{\"id\":\"7395164\",\"open\":\"2.91060000000000000000\",\"high\":\"2.91060000000000000000\",\"low\":\"2.91060000000000000000\",\"close\":\"2.91060000000000000000\",\"volume\":\"0.00\",\"amount\":\"0.00\",\"productId\":\"1002\",\"type\":\"2\",\"time\":\"1574324400000\"},{\"id\":\"7395230\",\"open\":\"2.91060000000000000000\",\"high\":\"2.91060000000000000000\",\"low\":\"2.91060000000000000000\",\"close\":\"2.91060000000000000000\",\"volume\":\"0.00\",\"amount\":\"0.00\",\"productId\":\"1002\",\"type\":\"2\",\"time\":\"1574324700000\"},{\"id\":\"7395314\",\"open\":\"2.91060000000000000000\",\"high\":\"2.91060000000000000000\",\"low\":\"2.91060000000000000000\",\"close\":\"2.91060000000000000000\",\"volume\":\"0.00\",\"amount\":\"0.00\",\"productId\":\"1002\",\"type\":\"2\",\"time\":\"1574325000000\"},{\"id\":\"7395387\",\"open\":\"2.91060000000000000000\",\"high\":\"2.91060000000000000000\",\"low\":\"2.91060000000000000000\",\"close\":\"2.91060000000000000000\",\"volume\":\"0.00\",\"amount\":\"0.00\",\"productId\":\"1002\",\"type\":\"2\",\"time\":\"1574325300000\"},{\"id\":\"7395468\",\"open\":\"2.91060000000000000000\",\"high\":\"2.91060000000000000000\",\"low\":\"2.91060000000000000000\",\"close\":\"2.91060000000000000000\",\"volume\":\"0.00\",\"amount\":\"0.00\",\"productId\":\"1002\",\"type\":\"2\",\"time\":\"1574325600000\"}]}";
		JSONObject jsonobj = JSONObject.fromObject(data1);
			JSONArray data  = jsonobj.getJSONArray("data");
			System.out.println("size="+data.size());
			Double high;
			Double low;
			Double open;
			Double close;
			Double amount;
			Double emplitude_sum = 0.00;
			Double emplitude = 0.00;
			for(int i = 0 ; i < data.size() ; i ++) {
				high = data.getJSONObject(i).getDouble("high");
				low = data.getJSONObject(i).getDouble("low");
				open = data.getJSONObject(i).getDouble("open");
				emplitude_sum += emplitude(high, low, open);
			}

			System.out.println(emplitude_sum/1000);
	}
	public static Double emplitude(Double high,Double low,Double open) {
		DecimalFormat df=new DecimalFormat("0.00000000000000000000");//设置保留位数
		return Double.valueOf(df.format((float)(high-low)/open));
	}

	public void timerRun() {
		Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println("-------设定要指定任务--------");
                show();
            }
        }, 0, 1000);
	}
	public static void show() {
		try {
			System.out.println(1/0);
		}catch (Exception e) {
			System.out.println("........");
		}
	}
}
