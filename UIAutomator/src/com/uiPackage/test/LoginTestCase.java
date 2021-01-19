package com.uiPackage.test;

import java.util.concurrent.TimeUnit;

import org.databene.benerator.anno.Source;
import org.databene.feed4testng.FeedTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.uiPackage.config.Config;
import com.uiPackage.utils.BaseUtils;
import com.uiPackage.utils.CreateAndroidDriver;
import com.uiPackage.utils.ExcuteCommand;
import com.uiPackage.utils.SendMail;
import com.uiPackage.utils.contract.RBRegular;
import com.uiPackage.utils.mine.Accounts;
import com.uiPackage.utils.mine.Login;
import com.uiPackage.utils.mine.LoginOut;
import com.uiPackage.utils.welcomePage.WelcomePage;

import io.appium.java_client.android.AndroidDriver;

public class LoginTestCase extends FeedTest{
	public static AndroidDriver<?> driver ;
	@Test(dataProvider = "feeder")
	@Source("source/mine/accounts.csv")	
	public void test_accounts(String num,String phone,String password,String xxxAccounts,String money) {
		long start = BaseUtils.time();
		System.out.println("*************start***********欢迎页面->登录->转账->退出登录************start************");
		WelcomePage.welcomePage();
		Login.login(phone,password);
		Accounts.transferAccounts(xxxAccounts,money);
		LoginOut.logout();
		long end = BaseUtils.time();
		System.out.println("*************end***********欢迎页面->登录->转账->退出登录************end************");
		System.out.println("************************耗时"+(end-start)+"s************************");
	}
	@BeforeClass
    public void setUp(){
		//启动手机
        CreateAndroidDriver.createAndroidDriver();
        driver = BaseUtils.getDriver();
    }
   @AfterClass
    public void tearDown(){
        driver.quit(); 	
    } 
}
