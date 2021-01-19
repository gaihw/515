package config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * 
 * @author Muguozheng
 * @date 2018年10月26日
 * @version 1.0
 * @description 配置中心,项目重要配置都在这里集中管理
 */
public class ConfigCenter {
	public static Properties dataProperties = new Properties();
	public static Properties emailProperties = new Properties();
	public static Properties executeProperties = new Properties();
	public static Properties paramProperties = new Properties();

	// 合约乘数 BTC EOS ETH LTC XRP ETC DASH
//	final public static double[] ratios = { -1, 0.5 };
	final public static double[] ratios = { -1, 0.002, 2, 0.002,0.07, 0.2, 40, 1, 0.5, 0.05, 0.1 ,0.5};
//	final public static double[] ratios = { -1, 0.002, 2, 0.05, 0.2, 40, 1, 0.1, 0.05, 0.1 ,0.5};
	// taker费率 BTC EOS ETH LTC XRP ETC DASH
	final public static double[] takers = { -1, 0.00015, 0.00015, 0.0006,0.0006, 0.00015, 0.00015, 0.00015, 0.0006, 0.00015,
			0.00015 ,0.0006};
//	final public static double[] takers = { -1, 0.00015, 0.00015, 0.00015, 0.00015, 0.00015, 0.00015, 0.00015, 0.00015,
//			0.00015 ,0.0006};
	// maker费率 BTC EOS ETH LTC XRP ETC DASH
	final public static double[] makers = { -1, 0.00015, 0.00015, 0.0003,0.0003, 0.00015, 0.00015, 0.00015, 0.0003, 0.00015,
			0.00015 ,0.0003};
//	final public static double[] makers = { -1, 0.00015, 0.00015, 0.00015, 0.00015, 0.00015, 0.00015, 0.00015, 0.00015,
//			0.00015 ,0.0003};
	// 维持保证金率,全局参数,不区分币种
	final public static double MM = 0.005;
	// 定义几个配置文件的路径
	final public static String dataProFile = "config/dataSource.properties";
	final public static String emailProFile = "config/email.properties";
	final public static String executeProFile = "config/execute.properties";

	// 测试方法
	public static void main(String[] args) throws FileNotFoundException, IOException {
		System.out.println(dataParser());
		System.out.println(executeParser());
		System.out.println(emailParser());
	}

	public static Properties dataParser() throws FileNotFoundException, IOException {
		dataProperties.load(new FileInputStream(dataProFile));
		return dataProperties;
	}

	public static Properties emailParser() throws FileNotFoundException, IOException {
		emailProperties.load(new FileInputStream(emailProFile));
		return emailProperties;
	}

	public static Properties executeParser() throws FileNotFoundException, IOException {
		executeProperties.load(new FileInputStream(executeProFile));
		return executeProperties;
	}

	public static double[] getRatios() {
		return ratios;
	}

	public static HashMap<String, double[]> getParams() {
		HashMap<String, double[]> params = new HashMap<String, double[]>();
		params.put("ratios", ratios);
		params.put("makers", makers);
		params.put("takers", takers);
		return params;
	}
}
