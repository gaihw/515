package com.uiPackage.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;

import com.uiPackage.config.Config;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;

public class CreateAndroidDriver {
	public static AndroidDriver<?> driver;
    private static AppiumDriver driver1;
	public static AndroidDriver getDriver() {

	    return driver;
	}
	public static void createAndroidDriver() {
		DesiredCapabilities des = new DesiredCapabilities();
  //    des.setCapability("automationName", "Appium");//Selendroid //自动化的模式选择
//	    des.setCapability("browserName", "chrome");  //h5
//	    des.setCapability("app", "/Users/gaihongwei/tools/app-58coin-tbtest.apk");//配置待测试的apk的路径
        des.setCapability("platformName", "Android");//平台名称
        des.setCapability("platformVersion", Config.PLATFORMVERSION);//手机操作系统版本
        des.setCapability("udid", Config.UDID);//连接的物理设备的唯一设备标识
        des.setCapability("deviceName", Config.UDID);//使用的手机类型或模拟器类型  UDID
        des.setCapability("autoGrantPermissions", true);//使用的手机类型或模拟器类型  UDID
        des.setCapability("noReset", true);//使用的手机类型或模拟器类型  UDID
        des.setCapability("appPackage", "com.tbex.trader");//App安装后的包名
        des.setCapability("appActivity", ".module.launch.SplashActivity");//app测试人员常常要获取activity，
//        des.setCapability("noReset",true);
//        des.setCapability("automationName", "UiAutomator2");
//        des.setCapability(MobileCapabilityType.AUTOMATION_NAME,AutomationName.ANDROID_UIAUTOMATOR2);
        des.setCapability("unicodeKeyboard", true);//支持中文输入
        des.setCapability("resetKeyboard", true);//支持中文输入
//        des.setCapability("newCommandTimeout", "10");//没有新命令时的超时时间设置
//        des.setCapability("nosign", "True");//跳过检查和对应用进行 debug 签名的步骤        
        try {
			driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), des);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);//隐式等待
	}
}
