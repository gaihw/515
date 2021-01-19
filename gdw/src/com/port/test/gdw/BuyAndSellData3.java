package com.port.test.gdw;

import com.port.util.BaseUtil;
import com.port.util.Config;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

public class BuyAndSellData3 {
	public static HttpClient client = new HttpClient();
	public static String url = "http://192.168.112.77:10020/v1/contract/market/kline";
	public static File file = new File("data/data5.txt");
	public static File file_buySellNetInflows = new File("data/data5_buySellNetInflows.txt");
	public static File file_buySellNetInflows_random = new File("data/data5_buySellNetInflows_random.txt");
	public static FileWriter fw ;
	public static FileWriter fw_buySellNetInflows ;
	public static FileWriter fw_buySellNetInflows_random ;
//	public static String FilePath = "pro/pro5.properties";
	public static FileWriter fw1 ;
	public static Double[] arr_buy = new Double[288];
	public static Double[] arr_sell = new Double[288];
	public static Double[] arr_netInflows = new Double[288];
	//振幅计数
	public static int acount_emplitude_pro;
	//每次开始的时间
	public static double every_start_time_pro ;
	//振幅总和
	public static double sum_emplitude_pro;
	//是否初始化
	public static boolean first_pro;
	static {
		Arrays.fill(arr_buy,0.00);
		Arrays.fill(arr_sell,0.00);
		Arrays.fill(arr_netInflows,0.00);
//		setValueByKey(FilePath,"sum_emplitude","0.00");
//		setValueByKey(FilePath,"acount_emplitude","0");
//		setValueByKey(FilePath,"first","false");
//		setValueByKey(FilePath,"every_start_time","0");
		try {
			fw = new FileWriter(file);
			fw_buySellNetInflows = new FileWriter(file_buySellNetInflows);
			fw_buySellNetInflows_random = new FileWriter(file_buySellNetInflows_random);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException {
//		long time = System.currentTimeMillis();
//		String params_5minute = "platformContractId=1&klineType=0&since="+String.valueOf((new BigDecimal(String.valueOf(time).toString().substring(0,10))).setScale(0, BigDecimal.ROUND_DOWN));
//		System.out.println(BaseUtil.getResponse(client, url, params_5minute));
//		System.out.println(time);
//		System.out.println(String.valueOf(time).toString().substring(0,10));

		Map<Integer,String> hashmap = new HashMap<Integer, String>();
		hashmap.put(0, "5分钟");
		hashmap.put(1, "30分钟");
		hashmap.put(2, "1小时");
		hashmap.put(3, "4小时");
		hashmap.put(4, "24小时");
		fw_buySellNetInflows.write("时间/买入/卖出/净流入"+"\r\n");
		fw_buySellNetInflows.flush();
        System.out.println("-------设定要指定任务--------");
        Double[][] result = buySellNetInflowsAcountValue();
        try {
            fw_buySellNetInflows.write("--------------------------------"+"\r\n");
            fw_buySellNetInflows.flush();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        for (int i = 0; i < 5; i++) {
            try {
                fw_buySellNetInflows.write(hashmap.get(i)+"/"+String.valueOf(new BigDecimal(result[i][0]).setScale(8, BigDecimal.ROUND_DOWN))+"/"+String.valueOf(new BigDecimal(result[i][1]).setScale(8, BigDecimal.ROUND_DOWN))+"/"+String.valueOf(new BigDecimal(result[i][2]).setScale(8, BigDecimal.ROUND_DOWN)+"\r\n"));
                fw_buySellNetInflows.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
		/*
		Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println("-------设定要指定任务--------");
                Double[][] result = buySellNetInflowsAcountValue();
                try {
					fw_buySellNetInflows.write("--------------------------------"+"\r\n");
					fw_buySellNetInflows.flush();					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		for (int i = 0; i < 5; i++) {
        			try {
        				fw_buySellNetInflows.write(hashmap.get(i)+"/"+String.valueOf(new BigDecimal(result[i][0]).setScale(8, BigDecimal.ROUND_DOWN))+"/"+String.valueOf(new BigDecimal(result[i][1]).setScale(8, BigDecimal.ROUND_DOWN))+"/"+String.valueOf(new BigDecimal(result[i][2]).setScale(8, BigDecimal.ROUND_DOWN)+"\r\n"));
        				fw_buySellNetInflows.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
            }
        }, 0, 5*60*1000);// 这里设定将延时每天固定执行
		 */
	}
	/**
	 * 计算不同时间段的买入/卖出/净流入
	 *   1-5分钟 6-30分钟 12-1小时 48-4小时 288-24小时
	 * @return
	 */
	public static Double[][] buySellNetInflowsAcountValue() {	
//		first_pro = Boolean.valueOf(getValueByKey(FilePath,"first"));
		double buy1_acount = 0.00;
		double sell1_acount = 0.00;
		double netInflows1_acount = 0.00;	
		double buy6_acount = 0.00;
		double sell6_acount = 0.00;
		double netInflows6_acount = 0.00;	
		double buy12_acount = 0.00;
		double sell12_acount = 0.00;
		double netInflows12_acount = 0.00;	
		double buy48_acount = 0.00;
		double sell48_acount = 0.00;
		double netInflows48_acount = 0.00;	
		double buy288_acount = 0.00;
		double sell288_acount = 0.00;
		double netInflows288_acount = 0.00;

//		long time = System.currentTimeMillis();
		long time = Long.valueOf(new String("1574320200000"));
		Double averageEmplitude_temp = averageEmplitude(String.valueOf(time).toString().substring(0,10));
		Double emplitude_temp = 0.00;
        System.out.println("averageEmplitude_temp="+averageEmplitude_temp);

		String params_5minute = "platformContractId=12&klineType=2&since=";//+String.valueOf((new BigDecimal(String.valueOf(time).toString().substring(0,10))).setScale(0, BigDecimal.ROUND_DOWN));
		JSONObject jsonobj = JSONObject.fromObject(BaseUtil.getResponse(client, url, params_5minute));
		try {
			JSONArray data  = jsonobj.getJSONArray("data");
			Double high;
			Double low;
			Double open;
			Double close;
			Double amount;
			high = data.getJSONObject(0).getDouble("high");
			low = data.getJSONObject(0).getDouble("low");
			open = data.getJSONObject(0).getDouble("open");
			close = data.getJSONObject(0).getDouble("close");
			amount = data.getJSONObject(0).getDouble("amount");
			emplitude_temp = emplitude(high,low,open);
			Double[] result_temp = buySellNetInflowsValue(emplitude_temp,averageEmplitude_temp,open,close,high,low,amount,time);
			for (int i = arr_buy.length-1; i >= 1; i--) {
				arr_buy[i] = arr_buy[i-1];
				arr_sell[i] = arr_sell[i-1];
				arr_netInflows[i] = arr_netInflows[i-1];
			}
			arr_buy[0] = result_temp[0];
			arr_sell[0] = result_temp[1];
			arr_netInflows[0] = result_temp[2];
			//取不同时间段的累计和
			buy1_acount = arr_buy[0];
			sell1_acount = arr_sell[0];
			netInflows1_acount = arr_netInflows[0];
			for (int i = 0; i < 6; i++) {
				buy6_acount += arr_buy[i];
				sell6_acount += arr_sell[i];
				netInflows6_acount += arr_netInflows[i];
			}
			for (int i = 0; i < 12; i++) {
				buy12_acount += arr_buy[i];
				sell12_acount += arr_sell[i];
				netInflows12_acount += arr_netInflows[i];
			}
			for (int i = 0; i < 48; i++) {
				buy48_acount += arr_buy[i];
				sell48_acount += arr_sell[i];
				netInflows48_acount += arr_netInflows[i];
			}
			for (int i = 0; i < 288; i++) {
				buy288_acount += arr_buy[i];
				sell288_acount += arr_sell[i];
				netInflows288_acount += arr_netInflows[i];
			}
			Double[][] result_acount = {{buy1_acount,sell1_acount,netInflows1_acount},
					{buy6_acount,sell6_acount,netInflows6_acount},
					{buy12_acount,sell12_acount,netInflows12_acount},
					{buy48_acount,sell48_acount,netInflows48_acount},
					{buy288_acount,sell288_acount,netInflows288_acount}
			};
			return result_acount;
		}catch (Exception e) {
			System.out.println("buySellNetInflowsAcountValue接口返回值，data解析不是json格式...");
			return null;
		}

	}
	/**
	 * 计算当前5分钟时间点的流入/流出/净流入
	 * @param emplitude_temp 振幅
	 * @param averageEmplitude 100个的振幅平均价
	 * @return
	 */
	public static Double[] buySellNetInflowsValue(Double emplitude_temp,Double averageEmplitude,Double open ,Double close,Double high ,Double low,Double amount,long time) {
		int max;
		int min;
		double random_temp=0.00;
		Random random = new Random();
		if(emplitude_temp>=averageEmplitude*1.4) {
			max = 20;
			min = 15;
			random_temp = (double) ((random.nextInt(max)%(max-min+1) + min))/100;
		}else if(emplitude_temp<(averageEmplitude*1.4)&&emplitude_temp>(averageEmplitude*0.6)) {
			//取0或1的随机数
			int a = (int)(Math.random()*2);
			//计算正负数
			int a_pow = (int)(Math.pow(-1, a));
			//获取0-10的随机数
			int num = (int)(Math.random()*16);
			random_temp = (double)a_pow*num/100;

		}else if(emplitude_temp<averageEmplitude*0.6) {
			max = 20;
			min = 15;
			random_temp = -(double) ((random.nextInt(max)%(max-min+1) + min))/100;
		}
		Double netInflows = amount*random_temp;
		Double buy = (amount+netInflows)/2;
		Double sell = (amount-netInflows)/2;
		Double[] result = {buy,sell,netInflows,random_temp};
		if(Double.valueOf(time) >= Double.valueOf(Config.start5_time)) {
			try {
				fw_buySellNetInflows_random.write("时间:"+time+"/"+
						"振幅:"+String.valueOf(new BigDecimal(emplitude_temp).setScale(8, BigDecimal.ROUND_DOWN))+"/"+
						"平均振幅"+String.valueOf(new BigDecimal(averageEmplitude).setScale(8, BigDecimal.ROUND_DOWN))+"/"+
						"开:"+String.valueOf(open)+"/"+
						"收:"+String.valueOf(close)+"/"+
						"高:"+String.valueOf(high)+"/"+
						"低:"+String.valueOf(low)+"/"+
						"成交额:"+String.valueOf(amount)+"/"+
						"买入:"+String.valueOf(new BigDecimal(buy).setScale(8, BigDecimal.ROUND_DOWN))+"/"+
						"卖出:"+String.valueOf(new BigDecimal(sell).setScale(8, BigDecimal.ROUND_DOWN))+"/"+
						"净流入:"+String.valueOf(new BigDecimal(netInflows).setScale(8, BigDecimal.ROUND_DOWN))+
						"随机数:"+String.valueOf(new BigDecimal(random_temp).setScale(8, BigDecimal.ROUND_DOWN))+"\r\n"
						);
				fw_buySellNetInflows_random.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 获取每一个k线的振幅和平均值
	 * @return
	 */
	public static double averageEmplitude(String time) {
		//1000根振幅和
		Double emplitude_sum = 0.00;
		//如果初始化完成，start_time为初始化的起始时间，则start_time为后面的开始时间，
		String params_5minute = "platformContractId=12&klineType=2&since=";//+String.valueOf((new BigDecimal(Long.valueOf(time))).setScale(0, BigDecimal.ROUND_DOWN));
		String response = BaseUtil.getResponse(client, url, params_5minute);
		JSONObject jsonobj = JSONObject.fromObject(response);
//		System.out.println(response);

		try {			
			JSONArray data  = jsonobj.getJSONArray("data");
			System.out.println("size="+data.size());
			Double high;
			Double low;
			Double open;
			Double close;
			Double amount;
			fw.write(response);
			fw.flush();
//			fw.write(new BigDecimal(data.getJSONObject(0).getDouble("time")).setScale(0, BigDecimal.ROUND_DOWN)+"/"+new BigDecimal(data.getJSONObject(2999).getDouble("time")).setScale(0, BigDecimal.ROUND_DOWN)+"\r\n");
//			fw.flush();
//			for(int i = 1 ; i <= 1000 ; i ++){
//				high = data.getJSONObject(i).getDouble("high");
//				low = data.getJSONObject(i).getDouble("low");
//				open = data.getJSONObject(i).getDouble("open");
//				emplitude_sum += emplitude(high,low,open);
//				fw.write(new BigDecimal(data.getJSONObject(i).getDouble("time")).setScale(0, BigDecimal.ROUND_DOWN)+"/"+high+"/"+low+"/"+open+"/"+emplitude(high,low,open)+"\r\n");
//				fw.flush();
//			}
			return emplitude_sum/1000;
		}catch (Exception e) {
			System.out.println("averageEmplitude接口返回值，data解析不是json格式...");
			return 0.00;
		}
	}

	/**
	 * 计算振幅
	 * @param high
	 * @param low
	 * @param open
	 * @return
	 */
	public static Double emplitude(Double high,Double low,Double open) {
		DecimalFormat df=new DecimalFormat("0.00000000000000000000");//设置保留位数		 
	    return Double.valueOf(df.format((float)(high-low)/open));
	}
	public static String getValueByKey(String filePath, String key)  {
		Reader fileReader;
		try {
			fileReader = new FileReader(filePath);
			Properties properties = new Properties();
			properties.load(fileReader);
			fileReader.close();
			return properties.getProperty(key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static void setValueByKey(String filePath, String key,String value) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
		    fis = new FileInputStream(filePath);
		    Properties prop = new Properties();
		    prop.load(fis);
		    prop.setProperty(key, value);
		    prop.setProperty(key, value);
		    fos = new FileOutputStream(filePath);
		    prop.store(fos, null);
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}finally{
		    try {
		        fis.close();
		        fos.close();
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
		     
		}
	}
}
