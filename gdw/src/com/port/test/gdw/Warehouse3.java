package com.port.test.gdw;

import com.port.util.BaseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Warehouse3 {
	public static HttpClient client = new HttpClient();
	public static String url = "http://192.168.112.77:10020/v1/contract/market/kline";
	public static File file = new File("data/warehouse.txt");
	public static String FilePath = "pro/warehouse.properties";
	public static FileWriter fw ;
	//当天的零点零分
	public static long start_time_pro ;
	//上次爆仓时间
	public static long berofe_time_warehouse_pro ;
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
	public static long day_space = 86400000;
	static {	
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		long time = 0;
		try {
			Date date_ = format.parse(format.format(date));
			Calendar cal = Calendar.getInstance();
			cal.setTime(date_);
			time = cal.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		setValueByKey(FilePath,"start_time_pro",String.valueOf(time));
		setValueByKey(FilePath,"end_time_pro",String.valueOf(time+day_space));
		setValueByKey(FilePath,"day_warehouse_pro","0.00");
		setValueByKey(FilePath,"warehouse_sum_pro","0.0");
		setValueByKey(FilePath,"now_warehouse_pro","0.0");
		setValueByKey(FilePath,"big_warehouse_pro","0");
		setValueByKey(FilePath,"berofe_time_warehouse_pro","0");
		start_time_pro = Long.valueOf(getValueByKey(FilePath,"start_time_pro"));
		end_time_pro = Long.valueOf(getValueByKey(FilePath,"end_time_pro"));
		try {
			fw = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println("-------设定要指定任务--------");
                warehouse();
            }
        }, 0, 60*1000);// 这里设定将延时每天固定执行
	}
	//计算某1分钟是否出现爆仓单
	public static void warehouse() {
		//获取当前的时间
		long timestamp = System.currentTimeMillis();

		day_warehouse_pro = Double.valueOf(getValueByKey(FilePath,"day_warehouse_pro"));
		berofe_time_warehouse_pro = Long.valueOf(getValueByKey(FilePath,"berofe_time_warehouse_pro"));
		warehouse_sum_pro = Double.valueOf(getValueByKey(FilePath,"warehouse_sum_pro"));
		now_warehouse_pro = Double.valueOf(getValueByKey(FilePath,"now_warehouse_pro"));
		big_warehouse_pro = String.valueOf(getValueByKey(FilePath,"big_warehouse_pro"));

		//最近1000根k线1分钟振幅平均值
		Double averageEmplitude_temp = 0.00;
		averageEmplitude_temp = averageEmplitude(String.valueOf(timestamp).toString().substring(0,10));

		//当前1分钟k线振幅
		Double emplitude_temp = 0.00;

		//当前1分钟k线成交额
		Double amount = 0.00;

		//当前1分钟k线成交量
		double volume = 0.00;

		//1小时毫秒数
		long space = 3600000;

		//爆仓单量
		double warehouseVolume = 0 ;

		int max = 30;
		int min = 50;
		Random random = new Random();
		Double random_temp = (double) ((random.nextInt(max)%(max-min+1) + min))/100;

		String params_1minute = "platformContractId=1&klineType=0&since="+(new BigDecimal(String.valueOf(timestamp).toString().substring(0,10))).setScale(0, BigDecimal.ROUND_DOWN);
		try {
			JSONObject jsonobj = JSONObject.fromObject(BaseUtil.getResponse(client, url, params_1minute));
			JSONArray data = jsonobj.getJSONArray("data");
			Double high;
			Double low;
			Double open;
			open = data.getJSONObject(0).getDouble("open");
			high = data.getJSONObject(0).getDouble("high");;
			low = data.getJSONObject(0).getDouble("low");
			amount = data.getJSONObject(0).getDouble("amount");
			volume = data.getJSONObject(0).getDouble("volume");
			emplitude_temp = emplitude(high,low,open);
			//当前振幅大于等于振幅平均价*1.4&&60分钟之外
			if(emplitude_temp >= averageEmplitude_temp*1.4 && timestamp >= berofe_time_warehouse_pro+space ) {
				warehouseVolume = volume*random_temp;
				setValueByKey(FilePath,"berofe_time_warehouse_pro",String.valueOf(new BigDecimal(timestamp).setScale(0, BigDecimal.ROUND_DOWN)));
				setValueByKey(FilePath,"warehouse_sum_pro",String.valueOf(warehouse_sum_pro+warehouseVolume));
				setValueByKey(FilePath,"now_warehouse_pro",String.valueOf(warehouseVolume));
				if(emplitude_temp >= averageEmplitude_temp*1.6) {
					setValueByKey(FilePath,"big_warehouse_pro",String.valueOf(""+big_warehouse_pro+"/"+warehouseVolume+"{"+String.valueOf(new BigDecimal(timestamp).setScale(0, BigDecimal.ROUND_DOWN))+"}"+"/"));
				}
				if(timestamp >= start_time_pro && timestamp <= end_time_pro) {
					day_warehouse_pro += warehouseVolume ;
					setValueByKey(FilePath,"day_warehouse_pro",String.valueOf(day_warehouse_pro));
				}
			}
		}catch (Exception e) {
			System.out.println("接口返回值，data解析不是json格式...");
		}
//		//判断是否是另一天
//		if(timestamp != start_time_pro) {
//			setValueByKey(FilePath,"day_warehouse","0.00");
//			setValueByKey(FilePath,"flage","false");
//			setValueByKey(FilePath,"acount_warehouse","0");
//			setValueByKey(FilePath,"warehouse_sum","0.0");
//			setValueByKey(FilePath,"big_warehouse","0");
//			setValueByKey(FilePath,"start_time",String.valueOf(timestamp));
//			setValueByKey(FilePath,"end_time",String.valueOf(timestamp+day_space));
//		}
	}
	/**
	 * 获取某一分钟的振幅平均值，当前振幅
	 * @return
	 */
	public static Double averageEmplitude(String time) {
		Double emplitude_sum = 0.00;
		String params_1minute = "platformContractId=1&klineType=0&since="+(new BigDecimal(Long.valueOf(time)-60000)).setScale(0, BigDecimal.ROUND_DOWN);
		try {			
			JSONObject jsonobj = JSONObject.fromObject(BaseUtil.getResponse(client, url, params_1minute));
			JSONArray data = jsonobj.getJSONArray("data");
			Double high;
			Double low;
			Double open;
			for (int i = 0 ; i < data.size() ; i ++ ){
				open = data.getJSONObject(i).getDouble("open");
				high = data.getJSONObject(i).getDouble("high");;
				low = data.getJSONObject(i).getDouble("low");
				emplitude_sum += emplitude(high,low,open);
			}
			return  emplitude_sum/1000;
		} catch (Exception e) {
			System.out.println("接口返回值，data解析不是json格式...");
			return null;
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
