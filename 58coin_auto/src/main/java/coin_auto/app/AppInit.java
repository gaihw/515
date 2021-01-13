package coin_auto.app;

import coin_auto.config.Config;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

//@Component
public class AppInit {

    public static AppiumDriver driver;

    /**
     * 启动app，建立AppiumDriver对象
     */
    public static void init() {
        //设置自动化相关参数
        DesiredCapabilities des = new DesiredCapabilities();
        des.setCapability("platformName", "Android");//平台名称
        des.setCapability("platformVersion", Config.PLATFORMVERSION);//手机操作系统版本
        des.setCapability("udid", Config.UDID);//连接的物理设备的唯一设备标识
        des.setCapability("deviceName", Config.UDID);//使用的手机类型或模拟器类型  UDID
        des.setCapability("noReset", true);//使用的手机类型或模拟器类型  UDID
        des.setCapability("appPackage", "com.tbex.trader");//App安装后的包名
        des.setCapability("appActivity", ".module.launch.SplashActivity");//app测试人员常常要获取activity，
        des.setCapability("unicodeKeyboard", true);//支持中文输入
        des.setCapability("resetKeyboard", true);//支持中文输入
//        des.setCapability("automationName", "UiAutomator2");
//        des.setCapability(MobileCapabilityType.AUTOMATION_NAME,AutomationName.ANDROID_UIAUTOMATOR2);
//        des.setCapability("autoGrantPermissions", true);//使用的手机类型或模拟器类型  UDID
        //初始化
        try {
            driver = new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"), des);
            Thread.sleep(10000);
            System.out.println(driver.getAppStrings());
//            driver.findElementById("com.tbex.trader:id/loginButton").click();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取AppiumDriver对象
     * @return
     */
    public static AppiumDriver getDriver(){
        init();
        return driver;
    }

    public static void main(String[] args) {
//        getDriver().closeApp();
        System.out.println(getDriver().currentActivity());
    }
}
