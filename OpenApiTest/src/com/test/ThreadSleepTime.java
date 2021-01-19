package com.test;

import java.awt.List;

public class ThreadSleepTime {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] arr = {1,2,3};
		for (int i : arr) {
			try {
				Thread.sleep(10000);
				System.out.println(i);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
