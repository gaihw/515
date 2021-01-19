package com.uiPackage.utils.contract;

import java.util.concurrent.TimeUnit;

import com.uiPackage.utils.BaseUtils;

import io.appium.java_client.android.AndroidDriver;

public class SwitchContract {
	public static AndroidDriver<?> driver = (AndroidDriver<?>) BaseUtils.getDriver();
	public static void switchContract() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);//隐式等待
		if(BaseUtils.xpathBoolean("//*[@text='点击可切换合约']")) {
			BaseUtils.clickPress(500,900);
			System.out.println("已跳过可切换合约蒙层页面...");
		}
	}
	
}
