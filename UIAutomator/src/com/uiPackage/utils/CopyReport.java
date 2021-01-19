package com.uiPackage.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.uiPackage.config.Config;

public class CopyReport {
	public final static String REPORT_PATH = Config.REPORT_PATH;
	public final static String REPORT_TOMCAT_PATH = Config.REPORT_TOMCAT_PATH;
	public static void copyReport()  {
		long bt = System.currentTimeMillis();
        FileInputStream fis = null;
        FileOutputStream fos = null;
		try {
			// 1、创建一个字节输入流对象，构造方法中绑定要读取的数据；
			File fileInDir = new File(REPORT_PATH);  
			if(!fileInDir .exists()  && !fileInDir .isDirectory())      
			{       
				fileInDir .mkdir();    
			} 
			File fileIn = new File(REPORT_PATH+"index.html");
			if(!fileIn.exists())    
			{    
			    try {    
			    	fileIn.createNewFile();    
			    } catch (IOException e) {    
			        // TODO Auto-generated catch block    
			        e.printStackTrace();    
			    }    
			} 
			fis = new FileInputStream(fileIn);
			//2、创建一个字节输出流对象，构造方法中绑定要写入的数据的目的地
			File fileOutDir = new File(REPORT_TOMCAT_PATH);  
			if(!fileOutDir .exists()  && !fileOutDir .isDirectory())      
			{       
				fileOutDir .mkdir();    
			} 
			File fileOut = new File(REPORT_TOMCAT_PATH+"index.html");
			if(fileOut.exists()) {
				fileOut.delete();
			}    
		    try {    
		    	fileOut.createNewFile();    
		    } catch (IOException e) {    
		        // TODO Auto-generated catch block    
		        e.printStackTrace();    
		    }    			
			fos =new FileOutputStream(fileOut);
			//3、调用字节输入流的读取数据的方法；
			byte[] bytes=new byte[1024*1024];//定义一个字节缓存区，
			int len=0;
			while ((len=fis.read(bytes))!=-1){//先从绑定的数据源获取到数据
				//4、调用字节输出流的写入硬盘数据的方法；
				fos.write(bytes,0,len);//写入硬盘读取到的字节个数
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//5、关闭流操作，释放资源
		try {
			if(fos!=null)
				fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//必须先要关闭写入流操作，已经写完了，就说明一定读完了，但是读完了，却不一定写完了，				
		try {
			if(fis!=null)
				fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        long ct = System.currentTimeMillis();//用于计算共耗费的时间
        System.out.println("复制这个文件共耗费了"+(ct-bt)+"毫秒");
	}
}
