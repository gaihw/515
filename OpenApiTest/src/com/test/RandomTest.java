package com.test;

public class RandomTest {
	public static void main(String[] args) {
//		方法1
//		(数据类型)(最小值+Math.random()*(最大值-最小值+1))
//		例:
//		(int)(1+Math.random()*(10-1+1))
		System.out.println((int)(1+Math.random()*2));
	}
}
