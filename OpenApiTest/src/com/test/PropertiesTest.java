package com.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesTest {
	final static Properties p ;
	static {
		p = new Properties();
		FileReader fr ;
		File f = new File("./source/pro.properties");
		try {
			fr = new FileReader(f);
			p.load(fr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		p.list(System.out);
	}
	public static void main(String[] args) {
		System.out.println("run ...");
		System.out.println(p.getProperty("key1"));
		
		
	}

}
