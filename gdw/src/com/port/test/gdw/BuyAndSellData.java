package com.port.test.gdw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import com.port.util.Config;
import org.apache.commons.httpclient.HttpClient;
//import org.testng.annotations.Test;

import com.port.util.BaseUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
public class BuyAndSellData{
	public static HttpClient client = new HttpClient();
	public static String url = "http://192.168.200.150:10020/v1/contract/market/kline";
	public static File file = new File("data/data5.txt");
	public static File file_buy = new File("data/data5_buy.txt");
	public static File file_sell = new File("data/data5_sell.txt");
	public static File file_netInflows = new File("data/data5_netInflows.txt");
	public static FileWriter fw ;
	public static FileWriter fw_buy ;
	public static FileWriter fw_sell ;
	public static FileWriter fw_netInflows ;
	public static FileReader fr_buy ;
	public static FileReader fr_sell ;
	public static FileReader fr_netInflows ;
	public static String FilePath = "pro/pro5.properties";
	public static FileWriter fw1 ;
	//振幅计数
	public static int acount_emplitude_pro;
	//每次开始的时间
	public static double every_start_time_pro ;
	//振幅总和
	public static double sum_emplitude_pro;
	//是否初始化
	public static boolean first_pro;
	public static long day_space = 86400000;
	static {	
		first_pro = Boolean.valueOf(getValueByKey(FilePath,"first"));
		try {
			fw = new FileWriter(file);
			fw_buy = new FileWriter(file_buy,true);
			fw_sell = new FileWriter(file_sell,true);
			fw_netInflows = new FileWriter(file_netInflows,true);
			fr_buy = new FileReader(file_buy);
			fr_sell = new FileReader(file_sell);
			fr_netInflows = new FileReader(file_netInflows);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException {
//		result_print(1);
//		result_print(6);
//		result_print(12);
//		result_print(48);
//		result_print(288);
//		try {
//			fw.close();
//			fw_buy.close();
//			fw_sell.close();
//			fw_netInflows.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Map<Integer,String> hashmap = new HashMap<Integer, String>();
		hashmap.put(0, "5分钟");
		hashmap.put(1, "30分钟");
		hashmap.put(2, "1小时");
		hashmap.put(3, "4小时");
		hashmap.put(4, "24小时");
		System.out.println("时间/买入/卖出/净流入");
		fw_buy.write("时间/买入/卖出/净流入"+"\r\n");
		fw_buy.flush();
		Double[][] result = buySellNetInflowsAcountValue();
		for (int i = 0; i < result.length; i++) {
			fw_buy.write(hashmap.get(i)+"/"+result[i][0]+"/"+result[i][1]+"/"+result[i][2]+"\r\n");
			fw_buy.flush();
		}
	}
	/**
	 * 
	 * @param duringTime 1-5分钟 6-30分钟 12-1小时 48-4小时 288-24小时
	 * @throws IOException 
	 */
	public static void result_print(int duringTime) throws IOException {
		buySellNetInflowsAcountValue();
		BufferedReader br_buy = new BufferedReader(fr_buy);
		BufferedReader br_sell = new BufferedReader(fr_sell);
		BufferedReader br_netInflows = new BufferedReader(fr_netInflows);
		String s_buy;
		String s_sell;
		String s_netInflows;
        StringBuffer sb_buy = new StringBuffer();
        StringBuffer sb_sell = new StringBuffer();
        StringBuffer sb_netInflows = new StringBuffer();
        while((s_buy=br_buy.readLine()) != null){
            sb_buy.append(s_buy);
        }
        while((s_sell=br_sell.readLine()) != null){
        	sb_sell.append(s_sell);
        }
        while((s_netInflows=br_netInflows.readLine()) != null){
        	sb_netInflows.append(s_netInflows);
        }
		String[] list_buy = sb_buy.toString().split(",");
		String[] list_sell = sb_sell.toString().split(",");
		String[] list_netInflows = sb_netInflows.toString().split(",");
		Map<Integer,String> hashmap = new HashMap<Integer, String>();
		hashmap.put(1, "5分钟");
		hashmap.put(6, "30分钟");
		hashmap.put(12, "1小时");
		hashmap.put(48, "4小时");
		hashmap.put(288, "24小时");
		System.out.println("时间/买入/卖出/净流入");
		Double buy_acount = 0.00;//总的买入
		Double sell_acount = 0.00;//总的卖出
		Double netInflows_acount = 0.00;//净流入
		for(int i = list_buy.length-1 ; i >= list_buy.length-duringTime ; i--) {
			buy_acount = Double.valueOf(list_buy[i]);
			sell_acount = Double.valueOf(list_sell[i]);
			netInflows_acount = Double.valueOf(list_netInflows[i]);
		}
		fw.write(hashmap.get(duringTime)+"/"+buy_acount+"/"+sell_acount+"/"+netInflows_acount+"\r\n");
		fw.write("---------------------------------------------------------------------------------"+"\r\n");
		fw.flush();
	}
	/**
	 * 计算不同时间段的买入/卖出/净流入
	 * @param duringTime 1-5分钟 6-30分钟 12-1小时 48-4小时 288-24小时
	 * @return
	 */
	public static Double[][] buySellNetInflowsAcountValue() {		
		Double averageEmplitude_temp ;
		Double high = 0.00;//针对一个时间点
		Double low = 0.00;//针对一个时间点
		Double open = 0.00;//针对一个时间点
		Double close = 0.00;//针对一个时间点
		Double amount = 0.00;//针对一个时间点
		Double emplitude_temp ;//针对一个时间点
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
		int minute_acount = 0;
		double time = 0.00;
		//5分钟的k线参数
		String params_5minute = "platformContractId=1&klineType=2&since=";
		JSONObject jsonobj = JSONObject.fromObject(BaseUtil.getResponse(client, url, params_5minute));
		JSONArray data = jsonobj.getJSONArray("data");	
		if(every_start_time_pro == 0) {			
			minute_acount = (int) ((data.getJSONObject(data.size()-1).getLong("time") - Long.valueOf(Config.start5_time))/300000);
		}else {
			minute_acount = (int) ((data.getJSONObject(data.size()-1).getLong("time") - every_start_time_pro)/300000);						
		}
		Date date ;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(int i = 1 ; i <= minute_acount ; i ++) {
			//先初始化
			if(first_pro == false) {
				initEmplitude();
			}
			Double [] d = averageEmplitude();
			averageEmplitude_temp = d[0];
			emplitude_temp = d[1];
			time = d[2];
			open = d[3];
			close = d[4];
			amount = d[5];
			Double[] result = buySellNetInflowsValue(emplitude_temp,averageEmplitude_temp,open,close,amount);
//			buy_acount += result[0];
//			sell_acount += result[1];
//			netInflows_acount += result[2];	
//			date = new Date((long) time);
//			System.out.println(time+"/"+format.format(date)+"/振幅:"+emplitude_temp+"/振幅平均价:"+averageEmplitude_temp+
//					"/amount:"+amount+"/buy:"+result[0]+"/sell:"+result[1]+"/netInflows:"+result[2]+"/random:"+result[3]);
			if((data.getJSONObject(data.size()-1).getLong("time")-time) <= 86400000) {
				buy288_acount += result[0];
				sell288_acount = result[1];
				netInflows288_acount = result[2];
			}
			if((data.getJSONObject(data.size()-1).getLong("time")-time) <= 14400000) {
				buy48_acount += result[0];
				sell48_acount = result[1];
				netInflows48_acount = result[2];
			}
			if((data.getJSONObject(data.size()-1).getLong("time")-time) <= 3600000) {
				buy12_acount += result[0];
				sell12_acount = result[1];
				netInflows12_acount = result[2];
			}
			if((data.getJSONObject(data.size()-1).getLong("time")-time) <= 1800000) {
				buy6_acount += result[0];
				sell6_acount = result[1];
				netInflows6_acount = result[2];
			}
			if((data.getJSONObject(data.size()-1).getLong("time")-time) <= 300000) {
				buy1_acount += result[0];
				sell1_acount = result[1];
				netInflows1_acount = result[2];
			}
//			try {
////				fw.write(time+"/"+format.format(date)+"/振幅:"+emplitude_temp+"/振幅平均价:"+averageEmplitude_temp+
////						"/amount:"+amount+"/buy:"+result[0]+"/sell:"+result[1]+"/netInflows:"+result[2]+"/random:"+result[3]+"\r\n");
////				fw.flush();
//				fw_buy.write(String.valueOf(result[0])+",");
//				fw_buy.flush();
//				fw_sell.write(String.valueOf(result[1])+",");
//				fw_sell.flush();
//				fw_netInflows.write(String.valueOf(result[2])+",");
//				fw_netInflows.flush();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		
		}
		Double[][] result_acount = {{buy1_acount,sell1_acount,netInflows1_acount},
				{buy6_acount,sell6_acount,netInflows6_acount},
				{buy12_acount,sell12_acount,netInflows12_acount},
				{buy48_acount,sell48_acount,netInflows48_acount},
				{buy288_acount,sell288_acount,netInflows288_acount}
				};
		return result_acount;
	}
	/**
	 * 计算当前5分钟时间点的流入/流出/净流入
	 * @param emplitude_temp 振幅
	 * @param averageEmplitude 100个的振幅平均价
	 * @return
	 */
	public static Double[] buySellNetInflowsValue(Double emplitude_temp,Double averageEmplitude,Double open ,Double close,Double amount) {
		int max=20;
		int min=10;	
		double random_temp=0.00;
		Random random = new Random();
		if(emplitude_temp>=averageEmplitude*1.4) {
			if(close>=open) {
				max = 20;
				min = 15;
				random_temp = (double) ((random.nextInt(max)%(max-min+1) + min))/100;		        				
			}else {
				max = 20;
				min = 15;
				random_temp = -(double) ((random.nextInt(max)%(max-min+1) + min))/100;		        				
			}
		}else if(emplitude_temp<(averageEmplitude*1.4)&&emplitude_temp>(averageEmplitude*0.6)) {
			if(close>=open) {
				max = 15;
				min = 10;
				random_temp = (double) ((random.nextInt(max)%(max-min+1) + min))/100;		        				
			}else {
				max = 15;
				min = 10;
				random_temp = -(double) ((random.nextInt(max)%(max-min+1) + min))/100;		        				
			}
		}else if(emplitude_temp<(averageEmplitude*0.6)) {
			//取0或1的随机数
			int a = (int)(Math.random()*2);
			//计算正负数
			int a_pow = (int)(Math.pow(-1, a));
			//获取0-10的随机数
			int num = (int)(Math.random()*11);
			random_temp = (double)a_pow*num/100;
		}
//		System.out.println("random_temp::"+random_temp);
		Double netInflows = amount*random_temp;
		Double buy = (amount+netInflows)/2;
		Double sell = (amount-netInflows)/2;
		Double[] result = {buy,sell,netInflows,random_temp};
		return result;
	}
	/**
	 * 获取每一个k线的振幅和平均值
	 * @return
	 */
	public static Double[] averageEmplitude() {
		acount_emplitude_pro = Integer.valueOf(getValueByKey(FilePath,"acount_emplitude"));
		sum_emplitude_pro = Double.valueOf(getValueByKey(FilePath,"sum_emplitude"));
		every_start_time_pro = Double.valueOf(getValueByKey(FilePath,"every_start_time"));
		int acount = acount_emplitude_pro ;
		//1000根振幅和
		Double emplitude_sum = sum_emplitude_pro;
		double now_emplitude = 0.00;
		//如果初始化完成，start_time为初始化的起始时间，则start_time为后面的开始时间，
		String params_5minute = "platformContractId=1&klineType=2&since="+String.valueOf((new BigDecimal(every_start_time_pro+300000)).setScale(0, BigDecimal.ROUND_DOWN));
		JSONObject jsonobj = JSONObject.fromObject(BaseUtil.getResponse(client, url, params_5minute));
		JSONArray data = jsonobj.getJSONArray("data");
		System.out.println(data.toString());
		Double high;
		Double low;
		Double open;
		high = data.getJSONObject(0).getDouble("high");
		low = data.getJSONObject(0).getDouble("low");
		open = data.getJSONObject(0).getDouble("open");
		emplitude_sum += emplitude(high,low,open);
		acount += 1;
		setValueByKey(FilePath,"every_start_time",String.valueOf((new BigDecimal(data.getJSONObject(0).getDouble("time"))).setScale(0, BigDecimal.ROUND_DOWN)));			
		setValueByKey(FilePath,"acount_emplitude",String.valueOf(acount));			
		setValueByKey(FilePath,"sum_emplitude",String.valueOf(emplitude_sum));
		Double[] result = {emplitude_sum/acount,emplitude(high,low,open),data.getJSONObject(0).getDouble("time"),high,low,data.getJSONObject(0).getDouble("amount")};
		return  result;		
		
	}
	/**
	 * 初始化振幅平均值,获取1000个样本的振幅
	 */
	public static void initEmplitude() {
		acount_emplitude_pro = Integer.valueOf(getValueByKey(FilePath,"acount_emplitude"));
		sum_emplitude_pro = Double.valueOf(getValueByKey(FilePath,"sum_emplitude"));
		int acount = acount_emplitude_pro ;
		//1000根振幅和
		Double emplitude_sum = sum_emplitude_pro;
		long start_time = Long.valueOf(Config.start5_time);			
		String space = "300000000" ;//1000个总共的毫秒数
//			long start_time = System.currentTimeMillis();
		long run_time = start_time-Long.parseLong(space);
		String params_1minute = "platformContractId=1&klineType=2&since="+run_time;
		JSONObject jsonobj = JSONObject.fromObject(BaseUtil.getResponse(client, url, params_1minute));
		JSONArray data = jsonobj.getJSONArray("data");
		Double high;
		Double low;
		Double open;
		try {
			fw.write("length--->"+data.size()+"\r\n");
			fw.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(data.size()>=1000) {
			for(int i = data.size()-1000 ; i < data.size() ; i ++) {
				high = data.getJSONObject(i).getDouble("high");
				low = data.getJSONObject(i).getDouble("low");
				open = data.getJSONObject(i).getDouble("open");
				emplitude_sum += emplitude(high,low,open);
				acount += 1;
				try {
					fw.write(new BigDecimal(data.getJSONObject(i).getDouble("time")).setScale(0, BigDecimal.ROUND_DOWN)+"--->"+String.valueOf(acount)+"--->"+String.valueOf(high)+"/"+String.valueOf(low)+"/"+String.valueOf(open)+"--->"+String.valueOf(emplitude(high,low,open))+"\r\n");
					fw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
		}else {
			System.out.println("data的列表不足1060{"+data.size()+"}");
		}
		setValueByKey(FilePath,"first","true");
		setValueByKey(FilePath,"acount_emplitude",String.valueOf(acount));			
		setValueByKey(FilePath,"sum_emplitude",String.valueOf(emplitude_sum));
		setValueByKey(FilePath,"every_start_time",""+new BigDecimal(Config.start5_time).setScale(0, BigDecimal.ROUND_DOWN));
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
