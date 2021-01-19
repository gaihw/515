package com.uiPackage.utils.mine;

import java.util.concurrent.TimeUnit;

import com.uiPackage.config.Config;
import com.uiPackage.utils.BaseUtils;
import com.uiPackage.utils.ProLoading;
import com.uiPackage.utils.welcomePage.WelcomePage;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;

public class Login {
	public static AndroidDriver<?> driver = BaseUtils.getDriver();
	public static void login() {
		//首先进入欢迎页面
//		WelcomePage.welcomePage();
        BaseUtils.sleep(10);
        //跳过欢迎页面，进入登录页面
        BaseUtils.id(ProLoading.mine).click();
        if(BaseUtils.xpathBoolean(ProLoading.pleseaLogin)) {
        	BaseUtils.xpath(ProLoading.pleseaLogin).click();
        	BaseUtils.id(ProLoading.writePhone).clear();
        	BaseUtils.id(ProLoading.writePhone).sendKeys(Config.PHONE_NUMBER);
        	BaseUtils.id(ProLoading.writePassword).clear();
        	BaseUtils.id(ProLoading.writePassword).sendKeys(Config.PHONE_PASSWORD);
        	
        	//适配，不同到手机，点击密码框时，有的会有键盘弹出
        	BaseUtils.clickPress();
        	BaseUtils.id(ProLoading.loginButton).click(); 
        	System.out.println("账号为"+Config.PHONE_NUMBER+",已登录...");
        }else {
        	System.out.println("账号为"+Config.PHONE_NUMBER+",已登录...");
        }             
    }
	/**
	 * 
	 * @param phone 手机号
	 * @param password 登录的密码
	 */
	public static void login(String phone,String password) {
		//首先进入欢迎页面
		WelcomePage.welcomePage();
		BaseUtils.sleep(10);
        //跳过欢迎页面，进入登录页面
        BaseUtils.id(ProLoading.mine).click();
        if(BaseUtils.xpathBoolean(ProLoading.pleseaLogin)) {
        	BaseUtils.xpath(ProLoading.pleseaLogin).click();
        	BaseUtils.id(ProLoading.writePhone).clear();
        	BaseUtils.id(ProLoading.writePhone).sendKeys(phone);
        	BaseUtils.id(ProLoading.writePassword).clear();
        	BaseUtils.id(ProLoading.writePassword).sendKeys(password);
        	BaseUtils.sleep(10);
        	//适配，不同到手机，点击密码框时，有的会有键盘弹出
        	BaseUtils.clickPress();
        	BaseUtils.id(ProLoading.loginButton).click(); 
        	System.out.println("账号为"+phone+",已登录...");
        }else {
        	System.out.println("账号为"+phone+",已登录...");
        }
              
    }
}
