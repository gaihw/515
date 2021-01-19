package com.port.test.gdw;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class Radit {
	public static void main(String[] args) {
		String param = "{\"code\":\"0000\",\"message\":\"成功\",\"data\":{\"bids\":[[\"80.78\",\"84\"],[\"189\",\"1096\"],[\"950\",\"2000\"],[\"960\",\"2000\"],[\"970\",\"2000\"],[\"980\",\"2000\"],[\"990\",\"2000\"],[\"993\",\"2000\"],[\"994\",\"2000\"],[\"995\",\"2000\"],[\"996\",\"2000\"],[\"997\",\"2000\"],[\"998\",\"2000\"],[\"999\",\"2000\"],[\"1000\",\"543\"],[\"8248.40\",\"360000\"],[\"8733.60\",\"240000\"],[\"9500.00\",\"1000001\"],[\"9500.01\",\"1000000\"],[\"9500.02\",\"1000000\"],[\"9682.76\",\"3765399\"],[\"9685.67\",\"588000\"],[\"9688.58\",\"441000\"],[\"9691.49\",\"392000\"],[\"9694.40\",\"343000\"],[\"9697.31\",\"284200\"],[\"9700.22\",\"191100\"],[\"9701.68\",\"98000\"],[\"9703.13\",\"17150\"],[\"9704.59\",\"9329\"]],\"asks\":[[\"9710.90\",\"21836\"],[\"9712.36\",\"31850\"],[\"9713.82\",\"182000\"],[\"9715.27\",\"354900\"],[\"9718.18\",\"527800\"],[\"9721.10\",\"637000\"],[\"9724.02\",\"728000\"],[\"9726.94\",\"819000\"],[\"9729.86\",\"1092000\"],[\"9732.78\",\"5307695\"],[\"9925.81\",\"1000000\"],[\"9925.82\",\"1999999\"],[\"9925.83\",\"1589999\"],[\"9925.84\",\"1000000\"],[\"9925.85\",\"1000000\"],[\"10674.40\",\"240000\"],[\"11159.60\",\"360000\"]]}}";
		HashMap<Double, Double> hashmap_bids = new HashMap<Double, Double>();
		HashMap<Double, Double> hashmap_asks = new HashMap<Double, Double>();
		List<Double> list_bids = new ArrayList<Double>();
		List<Double> list_asks = new ArrayList<Double>();
		JSONObject jsonobj = JSONObject.fromObject(param);
		JSONArray bids = jsonobj.getJSONObject("data").getJSONArray("bids");
		JSONArray asks = jsonobj.getJSONObject("data").getJSONArray("asks");
		
		for (int i = 0; i < bids.size(); i++) {
			hashmap_bids.put(bids.getJSONArray(i).getDouble(0),bids.getJSONArray(i).getDouble(1));
			list_bids.add(bids.getJSONArray(i).getDouble(0));
		}
		for (int i = 0; i < asks.size(); i++) {
			hashmap_asks.put(asks.getJSONArray(i).getDouble(0),asks.getJSONArray(i).getDouble(1));
			list_asks.add(asks.getJSONArray(i).getDouble(0));
		}
		list_bids.sort(null);
		list_asks.sort(null);
		int acount_bids = 0 ;
		int acount_asks = 0 ;
		for (int i = list_bids.size() -1 ; i >= list_bids.size()-10; i--) {
//			System.out.println(list_bids.get(i));
			acount_bids += hashmap_bids.get(list_bids.get(i));
		}
//		System.out.println("----------");
		for (int i = 0; i < list_asks.size() && i < 10; i++) {
//			System.out.println(list_asks.get(i));
			acount_asks += hashmap_asks.get(list_asks.get(i));
		}
		DecimalFormat df=new DecimalFormat("0.00");//设置保留位数	
		System.out.println("买盘="+acount_bids+",卖盘="+acount_asks);
		System.out.println("买盘10档："+Double.valueOf(df.format((float)(acount_bids)/(acount_bids+acount_asks)*100))+"%");
		System.out.println("卖盘10档："+Double.valueOf(df.format((float)(acount_asks)/(acount_bids+acount_asks)*100))+"%");
	}
}
