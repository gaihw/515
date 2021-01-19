package com.uiPackage.utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import io.appium.java_client.AppiumDriver;


public class TestNGListener extends TestListenerAdapter{
	 @Override
    public void onTestFailure(ITestResult tr){
        try {
            super.onTestFailure(tr);
            //调用屏幕截图方法
            captureScreenShot(tr,tr.getMethod().getMethodName());
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
 
    public void captureScreenShot(ITestResult result,String name) {
    	System.out.println("******"+name+"******用例失败，正在截图...");
    	AppiumDriver driver = BaseUtils.getDriver();
        File srcFile = driver.getScreenshotAs(OutputType.FILE);
        String filepath = ".//screenshot//";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            FileUtils.copyFile(srcFile, new File(filepath + File.separator +name+"->"+ dateFormat.format(new Date()) + ".png"));
            System.out.println("******"+name+"******已截图...");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
   
    }
 
}