package com.test.utils;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class ProLoading {
	//合约模块
	//交割
	public static String contractFilePath = "config/test.properties";
	public static String contract;

	static {
		//合约模块
		contract = getValueByKey(contractFilePath,"合约");

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
}