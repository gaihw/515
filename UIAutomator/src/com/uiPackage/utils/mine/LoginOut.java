package com.uiPackage.utils.mine;

import java.util.concurrent.TimeUnit;

import com.uiPackage.utils.BaseUtils;
import com.uiPackage.utils.ProLoading;

import io.appium.java_client.android.AndroidDriver;

public class LoginOut {
	public static AndroidDriver<?> driver = (AndroidDriver<?>) BaseUtils.getDriver();
	public static void logout() {
    	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    	BaseUtils.id(ProLoading.mine).click();
    	if(BaseUtils.idBoolean(ProLoading.setting)) {
    		BaseUtils.id(ProLoading.setting).click();
    		BaseUtils.id(ProLoading.quit).click();
    		BaseUtils.id(ProLoading.accept).click();
    		System.out.println("已退出登录...");
    	}else {
    		System.out.println("已退出登录...");
    	}   	
    }
}
