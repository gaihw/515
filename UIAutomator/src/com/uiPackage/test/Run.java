package com.uiPackage.test;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.uiPackage.utils.BaseUtils;
import com.uiPackage.utils.CreateAndroidDriver;
import com.uiPackage.utils.ExcuteCommand;
import com.uiPackage.utils.SendMail;

import io.appium.java_client.android.AndroidDriver;

public class Run {
	public static void main(String[] args) {
		//执行用例前，删除掉之前的截图
        ExcuteCommand.delScreenshot();
		TestNG testNG = new TestNG();
		List<String> suites = new ArrayList<String>();	
//		suites.add(System.getProperty("user.dir")+"\\testng.xml");	
		suites.add("testng.xml");	
		testNG.setTestSuites(suites);
		testNG.run();
		SendMail.send(); 
	}
}
