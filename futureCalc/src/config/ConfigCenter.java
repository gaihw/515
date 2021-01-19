package config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * 
 * @author Muguozheng
 * @date 2018��10��26��
 * @version 1.0
 * @description ��������,��Ŀ��Ҫ���ö������Ｏ�й���
 */
public class ConfigCenter {
	public static Properties dataProperties = new Properties();
	public static Properties emailProperties = new Properties();
	public static Properties executeProperties = new Properties();
	public static Properties paramProperties = new Properties();

	// ��Լ���� BTC EOS ETH LTC XRP ETC DASH
//	final public static double[] ratios = { -1, 0.5 };
	final public static double[] ratios = { -1, 0.002, 2, 0.002,0.07, 0.2, 40, 1, 0.5, 0.05, 0.1 ,0.5};
//	final public static double[] ratios = { -1, 0.002, 2, 0.05, 0.2, 40, 1, 0.1, 0.05, 0.1 ,0.5};
	// taker���� BTC EOS ETH LTC XRP ETC DASH
	final public static double[] takers = { -1, 0.00015, 0.00015, 0.0006,0.0006, 0.00015, 0.00015, 0.00015, 0.0006, 0.00015,
			0.00015 ,0.0006};
//	final public static double[] takers = { -1, 0.00015, 0.00015, 0.00015, 0.00015, 0.00015, 0.00015, 0.00015, 0.00015,
//			0.00015 ,0.0006};
	// maker���� BTC EOS ETH LTC XRP ETC DASH
	final public static double[] makers = { -1, 0.00015, 0.00015, 0.0003,0.0003, 0.00015, 0.00015, 0.00015, 0.0003, 0.00015,
			0.00015 ,0.0003};
//	final public static double[] makers = { -1, 0.00015, 0.00015, 0.00015, 0.00015, 0.00015, 0.00015, 0.00015, 0.00015,
//			0.00015 ,0.0003};
	// ά�ֱ�֤����,ȫ�ֲ���,�����ֱ���
	final public static double MM = 0.005;
	// ���弸�������ļ���·��
	final public static String dataProFile = "config/dataSource.properties";
	final public static String emailProFile = "config/email.properties";
	final public static String executeProFile = "config/execute.properties";

	// ���Է���
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
