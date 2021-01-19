package com.port.test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.apache.commons.httpclient.HttpClient;

import com.GetSign;
import com.port.util.BaseUtil;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;

public class KlineTest {
	public static File file = new File("data/kline.txt");
	public static FileWriter fw ;
	public static File file_ticker = new File("data/ticker.txt");
	public static FileWriter fw_ticker ;
	static DecimalFormat df = new DecimalFormat("0.000000000000");
	static {	
		try {
			fw = new FileWriter(file);
			fw_ticker = new FileWriter(file_ticker,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
//		Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            public void run() {
//            	ticker();
//            }
//        }, 1, 5000);
//		kline();

//		cal();
	}


	public static void cal(){

		String template = "";
		StringBuffer buff = new StringBuffer();
		JSONObject obj = JSONObject.parseObject(template);
		JSONArray arr = obj.getJSONArray("detail").getJSONObject(2).getJSONObject("detail").getJSONArray("$ljyk_data");
		for (int i = 0; i < arr.size(); i++) {
			buff.append(String.valueOf(df.format(arr.getJSONObject(i).getDouble("count_profit")))+",");
		}
//		double zssl = 0.0001;
//		double zsykb = 127.27;
//		double qzsl = 0.2728434150647749;
//		double qzykb = 37.3867456877729;
//		double max_yl = 2.7914056450001485;
//		double max_hc = 54.73236415500002;


		double zssl = obj.getJSONArray("detail").getJSONObject(6).getDouble("kpiVal");
		double zsykb = obj.getJSONArray("detail").getJSONObject(7).getDouble("kpiVal");
		double qzsl = obj.getJSONArray("detail").getJSONObject(3).getDouble("kpiVal");
		double qzykb = obj.getJSONArray("detail").getJSONObject(2).getDouble("kpiVal");
		double max_yl = obj.getJSONArray("detail").getJSONObject(2).getJSONObject("detail").getDouble("$max_ljyk");
		double max_hc = obj.getJSONArray("detail").getJSONObject(2).getJSONObject("detail").getDouble("$zdhc_ljyk");
		System.out.println("真实期望值="+String.valueOf(df.format(zssl*zsykb-1+zssl)));
		System.out.println("潜在期望值="+String.valueOf(df.format(qzsl*qzykb-1+qzsl)));
		System.out.println("风险报酬比="+String.valueOf(df.format(max_yl/(max_hc))));
		System.out.println(buff.toString());
		String str = "592322366018428928,592337301020622848,592337665052655616,592338019823140870,592341123130466304,592342012801064960,592342311578116096,592342592026066944,592342698988683265,592346821780316160,592347279882199040,592347810662989824,592348082386780160,592348845271949312,594337199970598912,594337418854023169,594338693327372288,594338793009192960,594338884646354944,594338970319208448,594339150917550080,594339380899102720,594341470350884864,594344715693080576,594346725889286144,594346731601412097,594347228232687616,594347284326793217,597230099779829760,597230231480975360,597230378466164736,597233479264256000,598143642075144192,598143665118658560,598147433625296896,598147474712707072,598147505465335808,598151309371908096,598151340837576704,598151366431219712,598152018544828416,598152561908531200,598755390171922432,598755427618668544,598755456383205376,598762623341445120";
		System.out.println(str.split(",").length);
	}

	/**
	 * 网站的ticker数据
	 */
	public static void ticker() {
		HttpClient client = new HttpClient();
		String url = "https://rusdtfuture.58ex.com/rusdt/market/bar";
//		String url = "http://rusdtfuture.test.58ex.com/rusdt/market/ticker/list";
		String respose = BaseUtil.getPostResponseBody(client, url);
		//1-btc 7-eos
		int product = 1;
		double multipler = 0.002;
		JSONArray response_obj = JSONObject.parseObject(respose).getJSONObject("data").getJSONArray("ticker");
		double amount = 0.00;
		String time = "";
		double volume = 0.00;
		DecimalFormat df=new DecimalFormat("0.000000");//设置保留位数
		JSONObject list = response_obj.getJSONObject(product);
		amount = list.getDouble("amount");
		volume = list.getDouble("volume");
		time = list.getString("time");
		try {
			if(volume != 0) {
				fw_ticker.write("time:"+time+"--->amount:"+String.valueOf(df.format(amount))+"--->volume:"+String.valueOf(df.format(volume))+"--->price:"+String.valueOf(df.format((amount/volume)/multipler))+"\r\n");
				fw_ticker.flush();
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 网站的k线数据
	 */
	public static void kline() {
		HttpClient client = new HttpClient();
		String url = "https://rusdtfuture.58ex.com/rusdt/market/bar";
//		String url = "http://rusdtfuture.test.58ex.com/rusdt/market/bar";
		//k线类型
		String type = "2";
		String product = "2001";
		//合约乘数
		double multipler = 0.002;
		JSONObject param = new JSONObject();
		param.put("product", product);
		param.put("type", type);
//		param.put("since", "1571554544000");
		param.put("since", "1572516000000");
		String respose = BaseUtil.getPostResponseBody(client, url, param);

		JSONArray response_obj = JSONObject.parseObject(respose).getJSONObject("data").getJSONArray("bar_data");
		double amount = 0.00;
		String time = "";
		double volume = 0.00;
		DecimalFormat df=new DecimalFormat("0.000000");//设置保留位数
		for (int i = 0; i < response_obj.size(); i++) {
		JSONObject list = response_obj.getJSONObject(i);
			amount = list.getDouble("amount");
			volume = list.getDouble("volume");
			time = list.getString("time");
			try {
				if(volume != 0) {
				fw.write("time:"+time+"--->amount:"+String.valueOf(df.format(amount))+"--->volume:"+String.valueOf(df.format(volume))+"--->price:"+String.valueOf(df.format((amount/volume)/multipler))+"\r\n");
				fw.flush();
				}
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}
