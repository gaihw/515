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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import com.port.util.Config;
import org.apache.commons.httpclient.HttpClient;

import com.port.util.BaseUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Warehouse {
	public static HttpClient client = new HttpClient();
	public static String url = "http://192.168.200.150:10020/v1/contract/market/kline";
	public static File file = new File("data/warehouse.txt");
	public static String FilePath = "pro/warehouse.properties";
	public static FileWriter fw ;
	//是否爆仓标示
	public static boolean flage_pro ;
	//当天的零点零分
	public static long start_time_pro ;
	//每次开始的时间
	public static long every_start_time_pro ;
	//上次爆仓时间
	public static long berofe_time_warehouse_pro ;
	//一共爆仓几次
	public static int acount_warehouse_pro ;
	//爆仓累计
	public static double warehouse_sum_pro ;
	//当前爆仓值
	public static double now_warehouse_pro ;
	//当天的结束时间
	public static long end_time_pro;
	//当天的爆仓累计
	public static double day_warehouse_pro;
	//大额爆仓单
	public static String big_warehouse_pro;
	//振幅计数
	public static int acount_emplitude_pro;
	//振幅总和
	public static double sum_emplitude_pro;
	//是否初始化
	public static boolean first_pro;
	public static long day_space = 86400000;
	static {	
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		long timestamp = 0;
		try {
			Date date_ = format.parse(format.format(date));
			Calendar cal = Calendar.getInstance();
			cal.setTime(date_);
			timestamp = cal.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		setValueByKey(FilePath,"start_time",String.valueOf(timestamp));
		setValueByKey(FilePath,"end_time",String.valueOf(timestamp+day_space));
		setValueByKey(FilePath,"day_warehouse","0.00");
		setValueByKey(FilePath,"flage","false");
		setValueByKey(FilePath,"acount","0");
		setValueByKey(FilePath,"warehouse_sum","0.0");
		setValueByKey(FilePath,"now_warehouse","0.0");
		setValueByKey(FilePath,"big_warehouse","0");
		start_time_pro = Long.valueOf(getValueByKey(FilePath,"start_time"));
		end_time_pro = Long.valueOf(getValueByKey(FilePath,"end_time"));
		day_warehouse_pro = Double.valueOf(getValueByKey(FilePath,"day_warehouse"));
		flage_pro = Boolean.valueOf(getValueByKey(FilePath,"flage"));
		berofe_time_warehouse_pro = Long.valueOf(getValueByKey(FilePath,"berofe_time_warehouse"));
		acount_warehouse_pro = Integer.valueOf(getValueByKey(FilePath,"acount_warehouse"));
		warehouse_sum_pro = Double.valueOf(getValueByKey(FilePath,"warehouse_sum"));
		now_warehouse_pro = Double.valueOf(getValueByKey(FilePath,"now_warehouse"));
		big_warehouse_pro = String.valueOf(getValueByKey(FilePath,"big_warehouse"));
		first_pro = Boolean.valueOf(getValueByKey(FilePath,"first"));
		try {
			fw = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
//		warehouse();
		initEmplitude();
	}
	//计算某1分钟是否出现爆仓单
	public static void warehouse() { 
//		Date date = new Date(System.currentTimeMillis());
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		long timestamp = 0;
//		try {
//			Date date_ = format.parse(format.format(date));
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(date_);
//			timestamp = cal.getTimeInMillis();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		String params_1minute = "platformContractId=1&klineType=0&since=";
		JSONObject jsonobj = JSONObject.fromObject(BaseUtil.getResponse(client, url, params_1minute));
		JSONArray data = jsonobj.getJSONArray("data");
		int minute_acount = 0;
		if(every_start_time_pro == 0) {			
			minute_acount = (int) ((data.getJSONObject(data.size()-1).getLong("time") - Long.valueOf(Config.start1_time))/60000);
		}else {
			minute_acount = (int) ((data.getJSONObject(data.size()-1).getLong("time") - every_start_time_pro)/60000);						
		}
		//爆仓单量
		double warehouseVolume = 0 ;
		//60分钟振幅
		Double Emplitude_temp = 0.00;
		Double averageEmplitude_temp = 0.00;
		Double time = 0.00;
		long space = 3600000;
		double volume = 0.00;
		for(int i = 1 ; i <= minute_acount ; i ++) {
			//先初始化
			if(first_pro == false) {
				initEmplitude();
			}
			Double [] d = averageEmplitude();
			averageEmplitude_temp = d[0];
			Emplitude_temp = d[1];
			time = d[2];
			volume = d[3];
			int max = 30;
			int min = 50;
			Random random = new Random();
			Double random_temp = (double) ((random.nextInt(max)%(max-min+1) + min))/100;
			//当前振幅大于等于振幅平均价*1.4&&60分钟之外
			if(Emplitude_temp >= averageEmplitude_temp*1.4 && time >= berofe_time_warehouse_pro+space ) {
				flage_pro = true;
				warehouseVolume = volume*random_temp;
				setValueByKey(FilePath,"flage","true");
				setValueByKey(FilePath,"time",String.valueOf(time));			
				setValueByKey(FilePath,"acount_warehouse",String.valueOf(acount_warehouse_pro+1));			
				setValueByKey(FilePath,"sum",String.valueOf(warehouse_sum_pro+warehouseVolume));			
				setValueByKey(FilePath,"now_warehouse",String.valueOf(warehouseVolume));		
				if(warehouseVolume >= warehouse_sum_pro/acount_warehouse_pro*1.4) {
					setValueByKey(FilePath,"big_warehouse",String.valueOf(warehouseVolume+"{"+time+"}"+"/"));	
					System.out.println("该仓为大额爆仓单，time{"+time+"}");
				}
				if(time >= start_time_pro && time <= end_time_pro) {
					day_warehouse_pro += warehouseVolume ;
					setValueByKey(FilePath,"day_warehouse",String.valueOf(day_warehouse_pro));					
				}
			}
		}
	}
	/**
	 * 获取某一分钟的振幅平均值，当前振幅
	 * @return
	 */
	public static Double[] averageEmplitude() {
		acount_emplitude_pro = Integer.valueOf(getValueByKey(FilePath,"acount_emplitude"));
		sum_emplitude_pro = Double.valueOf(getValueByKey(FilePath,"sum_emplitude"));
		every_start_time_pro = Long.valueOf(getValueByKey(FilePath,"every_start_time"));
		try {
			fw.write("*********************************************************************************************");
			fw.write(String.valueOf((new BigDecimal(every_start_time_pro)).setScale(0, BigDecimal.ROUND_DOWN)));
			fw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int acount = acount_emplitude_pro ;
		//1000根振幅和
		Double emplitude_sum = sum_emplitude_pro;
		double now_emplitude = 0.00;
		//如果初始化完成，start_time为初始化的起始时间，则start_time为后面的开始时间，并且往前找59根k线
		String params_1minute = "platformContractId=1&klineType=0&since="+(new BigDecimal(every_start_time_pro+60000-3540000)).setScale(0, BigDecimal.ROUND_DOWN);
		JSONObject jsonobj = JSONObject.fromObject(BaseUtil.getResponse(client, url, params_1minute));
		JSONArray data = jsonobj.getJSONArray("data");
		Double high;
		Double low;
		Double open;
		Double[] d =highOrLow(data,59);
		high = d[0];
		low = d[1];
		open = d[2];
		emplitude_sum += emplitude(high,low,open);
		acount += 1;
		setValueByKey(FilePath,"every_start_time",String.valueOf(new BigDecimal(d[3]).setScale(0, BigDecimal.ROUND_DOWN)));			
		setValueByKey(FilePath,"acount_emplitude",String.valueOf(acount));			
		setValueByKey(FilePath,"sum_emplitude",String.valueOf(emplitude_sum));
		Double[] result = {emplitude_sum/acount,emplitude(high,low,open),d[3],d[4]};
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
		long start_time = Long.valueOf(Config.start1_time);			
		String space = "66000000" ;//1100个总共的毫秒数
//			long start_time = System.currentTimeMillis();
		long run_time = start_time-Long.parseLong(space);
		String params_1minute = "platformContractId=1&klineType=0&since="+run_time;
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
		if(data.size()>=1060) {
			for(int i = data.size()-1000 ; i < data.size() ; i ++) {
				Double[] d =highOrLow(data,i);
				high = d[0];
				low = d[1];
				open = d[2];
				emplitude_sum += emplitude(high,low,open);
				acount += 1;
				try {
					fw.write(new BigDecimal(d[3]).setScale(0, BigDecimal.ROUND_DOWN)+"--->"+String.valueOf(acount)+"--->"+String.valueOf(high)+"/"+String.valueOf(low)+"/"+String.valueOf(open)+"--->"+String.valueOf(emplitude(high,low,open))+"\r\n");
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
		setValueByKey(FilePath,"every_start_time",String.valueOf(new BigDecimal(Config.start1_time).setScale(0, BigDecimal.ROUND_DOWN)));
	}
	/**
	 * 
	 * @param data 数据源
	 * @param index 起始的位置
	 * @return
	 */
	public static Double[] highOrLow(JSONArray data,int index){
		double open = data.getJSONObject(index-59).getDouble("open");
		double time = data.getJSONObject(index).getDouble("time");
		Double max = data.getJSONObject(index).getDouble("high");; 
		Double min = data.getJSONObject(index).getDouble("low");
		double volume = data.getJSONObject(index).getDouble("volume");
		for(int i = index - 59 ; i <= index ; i++) {
			if(max < data.getJSONObject(i).getDouble("high")) {
				max = data.getJSONObject(i).getDouble("high");
			}
			if(min > data.getJSONObject(i).getDouble("low")) {
				min = data.getJSONObject(i).getDouble("low");
			}
		}
		Double[] result = {max,min,open,time,volume};
		return result;
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
