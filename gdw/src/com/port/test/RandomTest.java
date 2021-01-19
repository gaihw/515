package com.port.test;

import java.util.Random;

public class RandomTest {
	public static String kline_5seconds = "";
	public static String kline_1day = "";
	public static void main(String[] args) {
		int max = 20;
		int min = 15;
		Random random = new Random();
		Double random_temp = -(double) ((random.nextInt(max)%(max-min+1) + min))/100;
//		System.out.println(random_temp);
//		System.out.println(random.nextInt(10));
//		System.out.println(random.nextInt(10)%21);
		for (int i = 0; i < 20; i++) {			
			int a = (int)(Math.random()*2);
			System.out.println("a="+a);
			int aa = (int)(Math.pow(-1, a));
			System.out.println("aa="+aa);
			int aaa = (int)(Math.random()*11);
			double num = (double)aa*aaa/100;
//			System.out.println(num);
			
		}
	}
}
