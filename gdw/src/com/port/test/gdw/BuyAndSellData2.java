package com.port.test.gdw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.port.util.Config;
import org.apache.commons.httpclient.HttpClient;

import com.port.util.BaseUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
public class BuyAndSellData2{
	public static HttpClient client = new HttpClient();
	public static String url = "http://192.168.200.150:10020/v1/contract/market/kline";
	public static File file = new File("data/data5.txt");
	public static File file_buySellNetInflows = new File("data/data5_buySellNetInflows.txt");
	public static File file_buySellNetInflows_random = new File("data/data5_buySellNetInflows_random.txt");
	public static FileWriter fw ;
	public static FileWriter fw_buySellNetInflows ;
	public static FileWriter fw_buySellNetInflows_random ;
	public static String FilePath = "pro/pro5.properties";
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
		setValueByKey(FilePath,"sum_emplitude","0.00");
		setValueByKey(FilePath,"acount_emplitude","0");
		setValueByKey(FilePath,"first","false");
		setValueByKey(FilePath,"every_start_time","0");
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
		Map<Integer,String> hashmap = new HashMap<Integer, String>();
		hashmap.put(0, "5分钟");
		hashmap.put(1, "30分钟");
		hashmap.put(2, "1小时");
		hashmap.put(3, "4小时");
		hashmap.put(4, "24小时");
		fw_buySellNetInflows.write("时间/买入/卖出/净流入"+"\r\n");
		fw_buySellNetInflows.flush();
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
	}
	/**
	 * 计算不同时间段的买入/卖出/净流入
	 * @param duringTime 1-5分钟 6-30分钟 12-1小时 48-4小时 288-24小时
	 * @return
	 */
	public static Double[][] buySellNetInflowsAcountValue() {	
		first_pro = Boolean.valueOf(getValueByKey(FilePath,"first"));
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
		//先初始化
		if(first_pro == false) {
			initEmplitude();
		}
		averageEmplitude();
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
	}
	/**
	 * 计算当前5分钟时间点的流入/流出/净流入
	 * @param emplitude_temp 振幅
	 * @param averageEmplitude 100个的振幅平均价
	 * @return
	 */
	public static Double[] buySellNetInflowsValue(Double emplitude_temp,Double averageEmplitude,Double open ,Double close,Double high ,Double low,Double amount,String time) {
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
	public static void averageEmplitude() {
		acount_emplitude_pro = Integer.valueOf(getValueByKey(FilePath,"acount_emplitude"));
		sum_emplitude_pro = Double.valueOf(getValueByKey(FilePath,"sum_emplitude"));
		every_start_time_pro = Double.valueOf(getValueByKey(FilePath,"every_start_time"));
		int acount = acount_emplitude_pro ;
		//1000根振幅和
		Double emplitude_sum = sum_emplitude_pro;
		//如果初始化完成，start_time为初始化的起始时间，则start_time为后面的开始时间，
		String params_5minute = "platformContractId=1&klineType=2&since="+String.valueOf((new BigDecimal(every_start_time_pro+300000)).setScale(0, BigDecimal.ROUND_DOWN));
		JSONObject jsonobj = JSONObject.fromObject(BaseUtil.getResponse(client, url, params_5minute));		
		try {			
			JSONArray data  = jsonobj.getJSONArray("data");
			Double high;
			Double low;
			Double open;
			Double close;
			Double amount;
			String time = "";
			high = data.getJSONObject(0).getDouble("high");
			low = data.getJSONObject(0).getDouble("low");
			open = data.getJSONObject(0).getDouble("open");
			close = data.getJSONObject(0).getDouble("close");
			amount = data.getJSONObject(0).getDouble("amount");
			time = String.valueOf(new BigDecimal(data.getJSONObject(0).getDouble("time")).setScale(0, BigDecimal.ROUND_DOWN));
			emplitude_sum += emplitude(high,low,open);
			acount += 1;
			setValueByKey(FilePath,"every_start_time",String.valueOf((new BigDecimal(data.getJSONObject(0).getDouble("time"))).setScale(0, BigDecimal.ROUND_DOWN)));			
			setValueByKey(FilePath,"acount_emplitude",String.valueOf(acount));			
			setValueByKey(FilePath,"sum_emplitude",String.valueOf(emplitude_sum));
			Double[] result_temp = buySellNetInflowsValue(emplitude(high,low,open),emplitude_sum/acount,open,close,high,low,amount,time);
			for (int i = arr_buy.length-1; i >= 1; i--) {
				arr_buy[i] = arr_buy[i-1];
				arr_sell[i] = arr_sell[i-1];
				arr_netInflows[i] = arr_netInflows[i-1];
			}
			arr_buy[0] = result_temp[0];
			arr_sell[0] = result_temp[1];
			arr_netInflows[0] = result_temp[2];
//			Double[] result = {emplitude_sum/acount,emplitude(high,low,open),data.getJSONObject(0).getDouble("time"),high,low,data.getJSONObject(0).getDouble("amount"),high,low};
//			return  result;		
		}catch (Exception e) {
			System.out.println("接口返回值，data解析不是json格式...");
//			return null;
		}
		
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
		Double close;
		Double amount;
		String time = "";
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
				close = data.getJSONObject(i).getDouble("close");
				amount = data.getJSONObject(i).getDouble("amount");
				time = String.valueOf(new BigDecimal(data.getJSONObject(i).getDouble("time")).setScale(0, BigDecimal.ROUND_DOWN));
				emplitude_sum += emplitude(high,low,open);
				acount += 1;
				try {
					fw.write(new BigDecimal(data.getJSONObject(i).getDouble("time")).setScale(0, BigDecimal.ROUND_DOWN)+"--->"+String.valueOf(acount)+"--->"+String.valueOf(high)+"/"+String.valueOf(low)+"/"+String.valueOf(open)+"--->"+String.valueOf(emplitude(high,low,open))+"\r\n");
					fw.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Double[] result_temp = buySellNetInflowsValue(emplitude(high,low,open),emplitude_sum/acount,open,close,high,low,amount,time);
				for (int j = arr_buy.length-1; j >= 1; j--) {
					arr_buy[j] = arr_buy[j-1];
					arr_sell[j] = arr_sell[j-1];
					arr_netInflows[j] = arr_netInflows[j-1];
				}
				arr_buy[0] = result_temp[0];
				arr_sell[0] = result_temp[1];
				arr_netInflows[0] = result_temp[2];
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
