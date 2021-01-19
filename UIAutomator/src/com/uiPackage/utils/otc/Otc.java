package com.uiPackage.utils.otc;

import java.util.concurrent.TimeUnit;

import com.uiPackage.utils.BaseUtils;
import com.uiPackage.utils.ProLoading;

import io.appium.java_client.android.AndroidDriver;

public class Otc {
	public static AndroidDriver<?> driver = BaseUtils.getDriver();
	public static void otc() {
		BaseUtils.id(ProLoading.otc).click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);//隐式等待
		BaseUtils.slideUpAWholeScreen();
	}
}
