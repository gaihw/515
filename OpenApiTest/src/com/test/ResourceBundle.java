package com.test;

import java.util.Locale;

public class ResourceBundle {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("pro", Locale.CHINA);
		System.out.println(bundle.getString("key2"));
	}

}
