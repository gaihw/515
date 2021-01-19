package com.uiPackage.utils;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

import org.databene.feed4testng.FeedTest;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
 
public class BaseUtils extends FeedTest{
	public static Duration duration = Duration.ofMillis(300);//滑动300ms
	public static AndroidDriver<?> driver;
	public static int start ;
	public static int end ;
    public static AndroidDriver getDriver(){
        driver = CreateAndroidDriver.getDriver();
        return driver;
    }
    @BeforeClass
    public void setUp(){
		//启动手机
        CreateAndroidDriver.createAndroidDriver();
        driver = BaseUtils.getDriver();
    }
   //@AfterClass
    public void tearDown(){
        driver.quit();  	
    }
    public static long time() {
    	return System.currentTimeMillis();
    }
    public static WebElement xpath(String xpathName) {
    	return driver.findElementByXPath(xpathName);
    }
    public static boolean xpathBoolean(String xpathName) {
    	try {
    		driver.findElementByXPath(xpathName);
    		return true;
		} catch (NoSuchElementException e) {
			// TODO: handle exception
			return false;
		}
    }
    public static boolean idBoolean(String idName) {
    	try {
    		driver.findElementById(idName);
    		return true;
		} catch (NoSuchElementException e) {
			// TODO: handle exception
			return false;
		}
    }
    public static WebElement id(String idName) {
    	return driver.findElementById(idName);
    } 
    public static WebElement className(String className) {
    	return driver.findElementByClassName(className);
    }
    public static boolean classNameBoolean(String className) {
    	try {
    		driver.findElementByClassName(className);
    		return true;
		} catch (NoSuchElementException e) {
			// TODO: handle exception
			return false;
		}
    }
    public static WebElement className(String className, int index) {

        return findElementList(By.className(className)).get(index);
    }
    public static List<WebElement> findElementList(By by) {
        List<WebElement> elementList = null;
        try {
            elementList = (List<WebElement>) driver.findElements(by);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return elementList;
    }
    public static void sleep(int seconds) {
    	driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);//隐式等待
    }
    public static void quit() {
    	driver.quit();
    }
    /**
     *向上滑动屏幕,每次滑动一格，例如：2X->3X
     */
    public static void slideUp() {
    	 int width = driver.manage().window().getSize().width;
    	 int height = driver.manage().window().getSize().height;
    	 if(width==720&&height==1184) {   		 
    		 new TouchAction(driver)
    		 .press(PointOption.point(width / 2, height*3614/4000 ))
//    		 .waitAction(WaitOptions.waitOptions(duration))
    		 .moveTo(PointOption.point(width / 2, height*3500/4000))
    		 .release()
    		 .perform();
    	 }else if(width==1080&&height==1821) {
    		 new TouchAction(driver)
    		 .press(PointOption.point(width / 2, height-156 ))
//    		 .waitAction(WaitOptions.waitOptions(duration))
    		 .moveTo(PointOption.point(width / 2, height-186))
    		 .release()
    		 .perform();
    	 }else if(width==1080&&height==1920) {
    		 new TouchAction(driver)
    		 .press(PointOption.point(width / 2, height-196 ))
//    		 .waitAction(WaitOptions.waitOptions(duration))
    		 .moveTo(PointOption.point(width / 2, height-216))
    		 .release()
    		 .perform();
    	 }

    }
    /**
     * 向上滑动一整屏
     */
    public static void slideUpAWholeScreen() {
    	int width = driver.manage().window().getSize().width;
   	 	int height = driver.manage().window().getSize().height;
//   	 	System.out.println(width+","+height);
   	 	if(width==720&&height==1184) {   	 		
   	 		new TouchAction(driver)
   	 		.press(PointOption.point(width / 2, height-184 ))
   	 		.waitAction(WaitOptions.waitOptions(duration))
   	 		.moveTo(PointOption.point(width / 2, height-784))
   	 		.release()
   	 		.perform();
   	 	}else if(width==1080&&height==1821) {   	 		
   	 		new TouchAction(driver)
   	 		.press(PointOption.point(width / 2, height-300 ))
   	 		.waitAction(WaitOptions.waitOptions(duration))
   	 		.moveTo(PointOption.point(width / 2, height-1500))
   	 		.release()
   	 		.perform();
   	 	}
	   	System.out.println("向上滑动页面...");
    }
    /**
     *向下滑动屏幕,每次滑动一格，例如：3X->2X
     */
    public static void slideDown() {
    	 int width = driver.manage().window().getSize().width;
    	 int height = driver.manage().window().getSize().height;
    	 if(width==720&&height==1184) {
    		 new TouchAction(driver)
        	 .press(PointOption.point(width / 2, height*3500/4000 ))
//        	 .waitAction(WaitOptions.waitOptions(duration))
        	 .moveTo(PointOption.point(width / 2, height*3614/4000))
        	 .release()
        	 .perform();
    	 }else if(width==1080&&height==1821) {
    		 new TouchAction(driver)
    		 .press(PointOption.point(width / 2, height-186 ))
//    		 .waitAction(WaitOptions.waitOptions(duration))
    		 .moveTo(PointOption.point(width / 2, height-156))
    		 .release()
    		 .perform();
    	 }else if(width==1080&&height==1920) {
    		 new TouchAction(driver)
    		 .press(PointOption.point(width / 2, height-216 ))
//    		 .waitAction(WaitOptions.waitOptions(duration))
    		 .moveTo(PointOption.point(width / 2, height-196))
    		 .release()
    		 .perform();
    	 }
    }
    /**
     * 向下滑动一整屏
     */
    public static void slideDownAWholeScreen() {
    	int width = driver.manage().window().getSize().width;
    	int height = driver.manage().window().getSize().height;
    	new TouchAction(driver)
    	.press(PointOption.point(width / 2, height-1500 ))
    	.waitAction(WaitOptions.waitOptions(duration))
    	.moveTo(PointOption.point(width / 2, height-200))
    	.release()
    	.perform();
    	System.out.println("向下滑动页面...");
    }
    /**
     * 适配，不同到手机，点击密码框时，有的会有键盘弹出
     */
    public static void clickPress() {
    	int width = driver.manage().window().getSize().width;
   	 	int height = driver.manage().window().getSize().height;
	   	 if(width==720&&height==1184) {
	    	new TouchAction(driver).tap(PointOption.point(width*630/720, height*1088/1184)).perform().release();
	   	 }else if(width==1080&&height==1821) {
	   		 new TouchAction(driver).tap(PointOption.point(width*958/1080, height*1738/1821)).perform().release();	   		 
	   	 }
    }
    public static void clickPress(int x,int y) {
    	new TouchAction(driver).tap(PointOption.point(x, y)).perform().release();
    }
//    public static void clickPress(WebElement element) {
//    	new TouchAction(driver).tap(element).perform().release();
//    }
    /**
     * 
     * @param lever 杠杆
     */
    public static void getLeverX(String lever) {
    	getElementLeverX(lever);
		while(start!=end) {			
			//如果输入参数的索引值大于页面的倍数，则页面向上滑动
			if(end>start) {		
				for (int i = 0; i < end-start; i++) {			
					slideUp();
				}
			}
			if(end<start){
				for (int i = 0; i < start-end; i++) {			
					slideDown();
				}
			}
			getElementLeverX(lever);
		}
    }
    public static void getElementLeverX(String lever) {
    	HashMap<String,Integer> leverX = new HashMap<String,Integer>();
		leverX.put("2X",1);
		leverX.put("3X",2);
		leverX.put("5X",3);
		leverX.put("10X",4);
		leverX.put("20X",5);
		leverX.put("33X",6);
		leverX.put("50X",7);
		leverX.put("100X",8);
		if(BaseUtils.xpathBoolean(ProLoading.twoX)
				&&BaseUtils.xpathBoolean(ProLoading.threeX)
				&&!BaseUtils.xpathBoolean(ProLoading.fiveX)) {
			start = leverX.get("2X");
		}else if(BaseUtils.xpathBoolean(ProLoading.twoX)
				&&BaseUtils.xpathBoolean(ProLoading.threeX)
				&&BaseUtils.xpathBoolean(ProLoading.fiveX)) {
			start = leverX.get("3X");			
		}else if(BaseUtils.xpathBoolean(ProLoading.fiveX)
				&&BaseUtils.xpathBoolean(ProLoading.threeX)
				&&BaseUtils.xpathBoolean(ProLoading.tenX)) {
			start = leverX.get("5X");			
		}else if(BaseUtils.xpathBoolean(ProLoading.tenX)
				&&BaseUtils.xpathBoolean(ProLoading.fiveX)
				&&BaseUtils.xpathBoolean(ProLoading.twentyX)) {
			start = leverX.get("10X");			
		}else if(BaseUtils.xpathBoolean(ProLoading.twentyX)
				&&BaseUtils.xpathBoolean(ProLoading.tenX)
				&&BaseUtils.xpathBoolean(ProLoading.thirty_threeX)) {
			start = leverX.get("20X");			
		}else if(BaseUtils.xpathBoolean(ProLoading.thirty_threeX)
				&&BaseUtils.xpathBoolean(ProLoading.twentyX)
				&&BaseUtils.xpathBoolean(ProLoading.fiftyX)) {
			start = leverX.get("33X");			
		}else if(BaseUtils.xpathBoolean(ProLoading.fiftyX)
				&&BaseUtils.xpathBoolean(ProLoading.thirty_threeX)
				&&BaseUtils.xpathBoolean(ProLoading.one_hundredX)) {
			start = leverX.get("50X");			
		}else if(BaseUtils.xpathBoolean(ProLoading.one_hundredX)
				&&BaseUtils.xpathBoolean(ProLoading.fiftyX)
				&&!BaseUtils.xpathBoolean(ProLoading.thirty_threeX)){
			start = leverX.get("100X");						
		}
		//获取输入参数的杠杆的索引值
		end = leverX.get(lever);
//		System.out.println("start==="+start+";end==="+end);
    }
    /**
     * 判断toast提示是否存在
     * @param toast
     * @return
     */
    public static String toastBoolean(String toast) {

		WebDriverWait wait = new WebDriverWait(driver, 1);
		WebElement target = wait.until(
			     ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@text,'"+toast+"')]")));
		return target.getText();

	}
    /**
     * 点击系统的返回键
     */
    public static void goBack() {
    	driver.pressKeyCode(AndroidKeyCode.BACK);
    }
}