package com.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//获取时间戳
		//方法 一
		System.currentTimeMillis();
		//方法 二
		Calendar.getInstance().getTimeInMillis();
		//方法 三
		new Date().getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
		System.out.println(date);

	}

}
