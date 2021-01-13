package com.uxin.usedcar.test.libs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Driver;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.record.PageBreakRecord.Break;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.remote.server.handler.FindElements;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import com.uxin.usedcar.testCase.BuyCarTest;
import com.uxin.usedcar.testCase.HomePageTest;
import com.uxin.usedcar.testCase.HomePageTestRN;
import com.uxin.usedcar.testCase.MyTest;
import com.uxin.usedcar.testCase.SellCarTest;
import com.uxin.usedcar.testCase.ValuableBookTest;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.MobilePlatform;

@SuppressWarnings("unused")
public class BaseTest {
	/** 当前状态保存路径 */
	public static final String CURRENT_STATE = "CaseRunTempFiles"
			+ File.separator + "CaseRunStateInfo.xml";
	public static AndroidDriver driver;
	//生成报告
	public static final ExtentReports reports_HomePageTest = ExtentReports.get(HomePageTest.class);
	public static final ExtentReports reports_HomePageTestRN = ExtentReports.get(HomePageTestRN.class);
	public static final ExtentReports reports_BuyCarTest = ExtentReports.get(BuyCarTest.class);
	public static final ExtentReports reports_MyTest = ExtentReports.get(MyTest.class);
	public static final ExtentReports reports_ValuableBookTest = ExtentReports.get(ValuableBookTest.class);
	public static final ExtentReports reports_SellCarTest = ExtentReports.get(SellCarTest.class);
	@BeforeMethod
	public void setUp() throws Exception {
		//负责启动服务端时的参数设置，启动session的时候是必须提供的。
		DesiredCapabilities capabilities = new DesiredCapabilities();
		//设置被测试手机的浏览器
		capabilities.setCapability(CapabilityType.BROWSER_NAME, "AndroidBrowser");
		//设置操作平台（android/ios）
	    capabilities.setCapability("platformName",MobilePlatform.ANDROID);
	    //设置操作的手机名称
	    capabilities.setCapability("deviceName", "androidDevice");  
//	    设置安卓系统版本
//	    capabilities.setCapability("platformVersion", "7.1.2"); //7.1.2
//	    capabilities.setCapability("udid", "2625c16a");
	    //设置app的主包名和主类名
        capabilities.setCapability("appPackage", "com.uxin.usedcar");
        capabilities.setCapability("appActivity", ".ui.fragment.SplashActivity");
        //设置安卓马甲包的包名和主类名
//        capabilities.setCapability("appPackage", "com.uxin.uxinusedcar");
//        capabilities.setCapability("appActivity", "com.uxin.uxinusedcar.ui.fragment.MainActivity");
        //设置unicode输入法
        capabilities.setCapability("unicodeKeyboard", true);
        //重置输入法
        capabilities.setCapability("resetKeyboard", true);
        //解决 打开第二个webview识别第一个webview元素
        capabilities.setCapability("recreateChromeDriverSessions", true);
        //设置命令超时
        capabilities.setCapability("newCommandTimeout", 0);
        //避免每次启动session
		capabilities.setCapability("session-override", true);
		capabilities.setCapability("RECREATE_CHROME_DRIVER_SESSIONS", true);
//		capabilities.setCapability("webdriver.chrome.driver", "/usr/local/lib/node_modules/appium/node_modules/appium-chromedriver/chromedriver/mac/chromedriver");
		//使用uiautomator2自动化引擎
		capabilities.setCapability("automationName", "uiautomator2");
		//防止重装app
		capabilities.setCapability("noReset", true);
		//使用adb方法进行截图
		capabilities.setCapability("nativeWebScreenshot", true);
		//显示webview日志
		capabilities.setCapability("showChromedriverLog", true);
        //初始化
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities); 
	}
	
	@AfterMethod
	public void tearDown(){
		driver.quit();
	}
	/** 保存当前状态 */
	public static void SaveCurrentState(String key, String value) {
		String separator = System.getProperty("line.separator", "\n");
		FileWriter mFileWriter;
		BufferedWriter mBufferedWriter = null;
		try {
			mFileWriter = new FileWriter(new File(CURRENT_STATE), true);
			mBufferedWriter = new BufferedWriter(mFileWriter);
			mBufferedWriter.write("#key" + key + "#value" + value
					+ "#timestamp" + System.currentTimeMillis() + separator+"\n");
			mBufferedWriter.write("\n");
			mBufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/** 按行读取文件 */
	public static ArrayList<String> ReadFileByLine(String filepath) {
		ArrayList<String> readstrings = new ArrayList<String>();
		BufferedReader mReader = null;
		try {
			FileReader mFileReader = new FileReader(new File(filepath));
			mReader = new BufferedReader(mFileReader);
			String tempString = null;
			// 一次读入一行，知道文件为空
			while ((tempString = mReader.readLine()) != null) {
				if (!StringUtils.replaceBlank(tempString).equals("")) {
					readstrings.add(tempString);
				}
			}
			mReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return readstrings;
	}
	
	/**
	 * 输入tab位置跳转到指定tab
	 * 首页 ---- 1,
	 * 买车 ---- 2,
	 * 卖车 ---- 3,
	 * 宝典 ---- 4,
	 * 我的 ---- 5
	 */
	public void gotocate(int i) {
		//首次启动忽略系统弹框，确保初始化成功
		if(!ReadCurrentState(CaseConfig.START_STATE, "首次启动").equals(
				CaseConfig.START_UNFIRST)){
			SaveCurrentState(CaseConfig.START_STATE, "非首次启动");
			System.out.println("首次启动");
			try {
				wait(5);
		
				 //关闭开启卖车新模式窗口
				 if(CheckViewVisibilty(By.id("ivSellCar"))) {
					 clickElementById("inCancel");
				 }
				 wait(1);
				//城市错误是处理
				if (CheckViewVisibilty(By.id("tvDialogMessage"))) {
					clickElementById("bt_confirm_ok");
				}
				//关闭欢迎页
				if (CheckViewVisibilty(By.id("title"))) {
					clickElementById("skip");
				}
				//关闭升级弹框
				if(CheckViewVisibilty(By.id("alertdialog_confirm"))) {
					clickElementById("dialog_cancelId");
				}
				wait(1);
				//关闭世界杯窗口
				if(CheckViewVisibilty(By.id("iv_worldcup"))) {
					clickElementById("iv_cancle");
				}
				//RN提示
				if(CheckViewVisibiltyByName("RN")) {
					driver.pressKeyCode(AndroidKeyCode.BACK);
				}
//			//关闭运营开屏图
//			if(CheckViewVisibilty(By.id("tv_countdown"))){
//				clickElementById("tv_countdown");
//			}
			wait(6);
//			changePlan();
			login();
			SaveCurrentState(CaseConfig.LOGIN, "已登录账号");
			if(!ReadCurrentState(CaseConfig.LOGIN, "已登录账号").equals(CaseConfig.LOGINSTATE)){
				System.out.println("登录失败，重新登录");
				wait(1);
				login();
				SaveCurrentState(CaseConfig.LOGIN, "已登录账号");
				if(!ReadCurrentState(CaseConfig.LOGIN, "已登录账号").equals(CaseConfig.LOGINSTATE)){
					failAndMessage("再次登录失败，请人工检查");
				}
			}
			gotoCateSet(i-1);
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("启动时发生异常");
				wait(1);
				login();
				SaveCurrentState(CaseConfig.LOGIN, "已登录账号");
				if(!ReadCurrentState(CaseConfig.LOGIN, "已登录账号").equals(CaseConfig.LOGINSTATE)){
					System.out.println("登录失败，重新登录");
					wait(1);
					login();
					SaveCurrentState(CaseConfig.LOGIN, "已登录账号");
					if(!ReadCurrentState(CaseConfig.LOGIN, "已登录账号").equals(CaseConfig.LOGINSTATE)){
						failAndMessage("再次登录失败，请人工检查");
					}
				}
				gotoCateSet(i-1);
			}
		}else{
			try {			
//				//关闭运营开屏图
//				if(CheckViewVisibilty(By.id("tv_countdown"))){
//					clickElementById("tv_countdown");
//				}
			wait(5);
			 //关闭开启卖车新模式窗口
			 if(CheckViewVisibilty(By.id("ivSellCar"))) {
				 clickElementById("inCancel");
			 }
			 wait(1);
			//城市错误是处理
			if (CheckViewVisibilty(By.id("tvDialogMessage"))) {
				clickElementById("bt_confirm_ok");
			}
			if (CheckViewVisibilty(By.id("title"))) {
				clickElementById("skip");
			}
			//关闭升级弹框
			if(CheckViewVisibilty(By.id("alertdialog_confirm"))) {
				clickElementById("dialog_cancelId");
			}
			wait(1);
			//关闭世界杯窗口
			if(CheckViewVisibilty(By.id("iv_worldcup"))) {
				clickElementById("iv_cancle");
			}
			wait(1);
			//RN提示
			if(CheckViewVisibiltyByName("RN")) {
				driver.pressKeyCode(AndroidKeyCode.BACK);
			}
			wait(6);
//			changePlan();
			if(!ReadCurrentState(CaseConfig.LOGIN, "已登录账号").equals(CaseConfig.LOGINSTATE)){
				System.out.println("非首次启动，try中登录失败，重新登录");
				wait(1);
				login();
				SaveCurrentState(CaseConfig.LOGIN, "已登录账号");
				if(!ReadCurrentState(CaseConfig.LOGIN, "已登录账号").equals(CaseConfig.LOGINSTATE)){
					failAndMessage("再次登录失败，请人工检查");
				}
			}
			gotoCateSet(i-1);
			System.out.println("非首次启动");
			}catch(Exception e){
				System.out.println("广告没有配置或加载失败");
				if(!ReadCurrentState(CaseConfig.LOGIN, "已登录账号").equals(CaseConfig.LOGINSTATE)){
					System.out.println("非首次启动，catch登录失败，重新登录");
					wait(1);
					login();
					SaveCurrentState(CaseConfig.LOGIN, "已登录账号");
					if(!ReadCurrentState(CaseConfig.LOGIN, "已登录账号").equals(CaseConfig.LOGINSTATE)){
						failAndMessage("再次登录失败，请人工检查");
					}
				}else {
					System.out.println(ReadCurrentState(CaseConfig.LOGIN, "已登录账号"));
				}
				gotoCateSet(i-1);
				System.out.println("走catch时失败了");
			}
		}
	}
	
	//切换新详情页方案，每次重启APP都要切换
//	public void changePlan() {
//		gotoCateSet(4);
//		clickElementById("vgSetting");
//		wait(1);
//		clickElementByName("关于优信二手车");
//		wait(1);
//		clickElementById("about_icon");
//		wait(1);
////		clickElementById("rbPlanA");//询底价
//		clickElementById("rbPlanB");//我要优惠
////		clickElementById("rbPlanC");//砍价
////		clickElementById("rbPlanD");//免费电话
////		clickElementById("imgBtBack");
//		wait(1);
//		driver.pressKeyCode(AndroidKeyCode.BACK);
//		wait(1);
//		driver.pressKeyCode(AndroidKeyCode.BACK);
//		wait(1);
//		driver.pressKeyCode(AndroidKeyCode.BACK);
//		wait(1);
//		System.out.println("详情页成功切换为方案B我要优惠；");
//	}
	
	
	/**
	 * 登录，默认使用14725836915
	 * 验证码登录
	 */
		public void login() {
			gotoCateSet(4);
			if (findElementById("tvTips").getText().equals("点击登录")) {
				System.out.println("********未登录*********");
				clickElementById("imgTouXiang");
				input("id", "smslogin_phone", "",CaseConfig.USERNAME_PUBLIC5, "");
				clickElementByName("获取验证码");
				input("id", "smslogin_verifycode", "", "666666", "");
				clickElementById("smslogin_ok");
				wait(3);
				System.out.println("开始寻找");
				String userName=findElementById("tvTips").getText();
				System.out.println(userName);
				if (userName.equals("147****6915")) {
					System.out.println("登录成功");
				}else {
					failAndMessage("登录失败请人工检查");
				}
			}else if (!findElementById("tvTips").getText().toString().substring(0, 3).equals("147")) {
				System.out.println(findElementById("tvTips").getText());
				sliding("","","","up","");
				clickElementById("vgSetting");
				clickElementByName("退出登录");
				failAndMessage("登录账号不是自动化账号，请退出后再执行");
			}else {
				System.out.println("******已登录*******");
			}
			
		}
	/**
	 * 传入tab值跳转到对应tab
	 * @param int i
	 **/
	public static void gotoCateSet(int i){
		if (i==0) {
			if(CheckViewVisibilty(By.id("tvHomeTop"))) {
				if(findElementById("tvHomeTop").getAttribute("checkable").equals("true")) {
					wait(1);
					clickElementById("tvHomeTop"); //v9.6
				}
			}else if(CheckViewVisibilty(By.id("rbShouYe"))){
				clickElementById("rbShouYe");
			}
		}
		if (i==1) {
			wait(1);
			clickElementById("rbCheShi"); 
		}
		
		if (i==2) {
			wait(1);
			clickElementById("rbSellCar"); 
		}
		
		if (i==3) {
			wait(1);
			clickElementById("rbFaXian");
		}
		
		if (i==4) {
			wait(1);
			clickElementById("rbWo");
		}
		
		
	}
	
	/**
     * 通过xpath查找元素
     * @param driver
     * @param xmlPath
     * @return
     * @throws NoSuchElementException
     */
	public static WebElement findElementByXpath(String xmlPath){
		WebElement Element = null;
		try {
			 Element = driver.findElement(By.xpath(xmlPath));
			
		} catch (NoSuchElementException e) {
			Element=null;
			// TODO: handle exception
		}
		return Element;
		
	}
	
	/**
	 *通过id查找元素
	 * @param driver 
	 * @param id
	 * @return
	 * @throws NoSuchElementException
	 */
	public static WebElement findElementById(String id){
		WebElement Element = driver.findElement(By.id(id));
		return Element;
	}
	
	/**
	 *通过name查找元素
	 * @param name
	 * @return WebElement
	 * @throws NoSuchElementException
	 */
	public static WebElement findElementByName(String name){
		System.out.println("传入值name>>>"+name);
		WebElement Element = null;
		try {
			Element = driver.findElementByAndroidUIAutomator("text(\""+name+"\")");
		} catch (Exception e) {
			failAndMessage("没有找到>>>"+name);
		}
		return Element;
	}
	
	/**
	 *通过name查找元素(注意：appium 1.5版本之后不再支持by.name ，可以参考使用CheckViewVisibiltyByName()方法  )
	 * @param name
	 * @return boolean
	 * @throws NoSuchElementException
	 */
	public static boolean CheckViewVisibiltyByName(String name) {
		WebElement Element = null;
		try {
			Element = driver.findElementByAndroidUIAutomator("text(\""+name+"\")");
		} catch (Exception e) {
			return false;
		}
		if(Element != null) {
			return true;
		}else {
			return false;
		}
	}
	
	
	/**
	 * 通过xpath找到元素并点击
	 * @param driver
	 * @param xmlPath
	 */
	public static void clickElementByXpath(String xmlPath){
		driver.findElement(By.xpath(xmlPath)).click();
	}
	
	/**
	 * 通过id找到元素并点击
	 * @param driver
	 * @param id
	 */
	public static void clickElementById(String id){
		driver.findElement(By.id(id)).click();
	}
	
	/**
	 * 通过name找到元素并点击
	 * @param driver
	 * @param name
	 */
	public static void clickElementByName(String name){
		findElementByName(name).click();
	}
	
	/**
	 * 控件点击
	 * @param driver       Driver 实例
	 * @param type         定位方式
	 * @param objectProper 元素属性
	 * @param data         测试参数化数据
	 * @param checkvalue   校验数据
	 * @throws IOException
	 */
	public static void click(String type,String objectProper, String objectProperParamer,String data, String string)
			{
		if (objectProper.contains("element") && !objectProperParamer.equals("")) {
			// 当属性字段中存在element 字符且data 有值，第一步先将属性值替换
			objectProper = objectProper.replace("element", objectProperParamer);
		}
		System.out.println("元素路径=="+objectProper);
		try {
			// 控件定位
			
			WebElement webElement = getWebelement(type, objectProper, objectProperParamer, data, string);
			if (webElement != null) {
				// 点击控件
				webElement.click();
				System.out.println("控件点击成功==");
				CaseConfig.STEPRESULT = CaseConfig.STEPRESULTPASS;
			}
		} catch (Exception e) {
			// 异常截图
			GetScreenshot();
			CaseConfig.STEPRESULT = CaseConfig.STEPRESULTFAIL;
//			reports.log(LogStatus.FAIL, "点击失败");
		}
	}
	
	/**
	 * 
	 * @param type 传入定位方式 如id,name
	 * @param objectProper 传入定位属性
	 * @return   String type,String objectProper, String objectProperParamer,String data, String string
	 */
	public static WebElement getWebelement(String type,String objectProper, String objectProperParamer,String data, String string) {
		WebElement webElement = null;
		String locatorValue = objectProper;
		try {
			WebDriverWait wait=new WebDriverWait(driver, 20);
			
			switch (type) {

			case "xpath":
				// 当检测到是androidDriver 时
				if (driver instanceof AndroidDriver) {
					if (locatorValue.contains("//*[@text")) {
						String text = locatorValue.split("=")[1].replace("'", "")
								.replace("]", "").replace("\"", "");
						String uiautomatorExpress = "new UiSelector().text(\""
								+ text + "\")";
					} else if (locatorValue.contains("//*[contains(@text")) {
						String text = locatorValue.split(",")[1].replace("'", "")
								.replace("]", "").replace("\"", "")
								.replace(")", "");
						String uiautomatorExpress = "new UiSelector().textContains(\""
								+ text + "\")";
					} else {
						webElement = driver.findElement(By.xpath(objectProper));
					}
					
				} else if (driver instanceof WebDriver) {
				webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorValue))); 
				}
				break;
			case "id":
				webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorValue)));
				break;
			case "cssSelector":
				webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locatorValue)));
				break;
			case "name":
				webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorValue)));
				break;
				//By.className(locatorValue)
			case "className":
				webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(locatorValue)));
				break;
			case "linkText":
				webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(locatorValue)));
				break;
			case "partialLinkText":
				webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(locatorValue)));
				break;
			case "tagName":
				webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName(locatorValue)));
				break;
			default:
//				webElement = driver.findElement(By.xpath(locatorValue));
				
				webElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locatorValue)));
				
				break;

			}
			
		} catch (Exception e) {
			webElement=null;
			// TODO: handle exception
		}
		return webElement;
	}

	/**
	 * 截图方法，不需要自定义图片名称时使用
	 */
	public static void GetScreenshot(){
	GetScreenshot("");
	}
	/**
	 * 截图方法，必须传入caseName以区别图片
	 *@param String name图片名称
	 */
	public static void GetScreenshot(String caseName){
		File screenShotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = sdf.format(new Date());
		//目录不存在时创建目录
		if (new File(CaseConfig.CATCH_PICTURE).isDirectory()) {
			System.out.println("创建文件夹");
			new File(CaseConfig.CATCH_PICTURE).mkdir();
		}
		System.out.println("准备复制");
		try {
			FileUtils.copyFile(screenShotFile, new File(CaseConfig.CATCH_PICTURE+File.separator+ caseName + "-" + date+ ".png"));
		} catch (IOException e) {
			failAndMessage("截图失败");
			e.printStackTrace();
		}
	}
	//通过id输入
	public void inputById(String id,String data) {
		input("id", id, "", data, "");
	}
	
	//通过name输入
		public void inputByName(String name,String data) {
			input("id", name, "", data, "");
		}
		//通过xpath输入
		public void inputByXpath(String xpath,String data) {
			input("id", xpath, "", data, "");
		}
	
	/**
	 * 控件输入
	 * 
	 * @param driver
	 *            Driver 实例
	 * @param type
	 *            定位方式
	 * @param objectProper
	 *            元素属性
	 * @param data
	 *            测试参数化数据
	 * @param checkvalue
	 *            校验数据
	 * @throws IOException
	 */
	public static void input(String type,String objectProper,String objectProperParamer, String data, String checkvalue)
			{
		try {
			if (objectProper.contains("element") && !objectProperParamer.equals("")) {
				// 当属性字段中存在element 字符且data 有值，第一步先将属性值替换
				objectProper = objectProper.replace("element", objectProperParamer);
			}
			System.out.println("元素路径=="+objectProper);
			// 控件定位
			AndroidElement webElement = (AndroidElement) getWebelement(type, objectProper, objectProperParamer, data, checkvalue);
			
			 String txt=webElement.getText();
			 System.out.println(txt);
			 System.out.println(txt.length());
			 if(!(txt.length()==0)){
				 webElement.clear();
			 }
//				webElement.setValue(data);
			 	webElement.sendKeys(data);
				System.out.println("控件输入数据成功输入的数据是=="+data);
				CaseConfig.STEPRESULT = CaseConfig.STEPRESULTPASS;
			
		} catch (Exception e) {
			// 异常截图
			GetScreenshot();
			CaseConfig.STEPRESULT = CaseConfig.STEPRESULTFAIL;
			e.printStackTrace();
		}
	}
	
	/**
	 * 滑动一屏，需传入方向
	 * @param data 参数可为：up，down，left，right
	 */
	public void  sliding(String data) {
	sliding("", "", "", data, "");
	}
	/**
	 * 控件滑动
	 * 
	 * @param driver
	 *            Driver 实例
	 * @param type
	 *            定位方式
	 * @param objectProper
	 *            元素属性
	 * @param data 传入up，down，left，right
	 * @param checkvalue
	 *            校验数据
	 * @throws IOException
	 */
	public static void sliding(String type,String objectProper,String objectProperParamer, String data, String checkvalue){
		// 获取屏的宽度
		int width = driver.manage().window().getSize().width;
		// 获取屏的高度
		int height = driver.manage().window().getSize().height;

		try {
			if (!objectProperParamer.equals("")) {
				int xcoordStart = (int) (Double.parseDouble(objectProperParamer.split(",")[0]) * width);
				int ycoordStart = (int) (Double.parseDouble(objectProperParamer.split(",")[1]) * height);
				int xcoordEnd = (int) (Double.parseDouble(objectProperParamer.split(",")[2]) * width);
				int ycoordEnd = (int) (Double.parseDouble(objectProperParamer.split(",")[3]) * height);
				System.out.println("起点坐标==" + xcoordStart + "," + ycoordStart
						+ "结束坐标===" + xcoordEnd + "," + ycoordEnd);
				driver.swipe(xcoordStart, ycoordStart, xcoordEnd, ycoordEnd, 1000);
				CaseConfig.STEPRESULT = CaseConfig.STEPRESULTPASS;	
			}
			if (data.equals("up")) {
				System.out.println("上滑一屏，开始位置"+height*8/10+"结束位置"+height*1/10);
				driver.swipe(width/2, height*8/10, width/2, height*1/10, 1000);
			}
			if (data.equals("down")) {
				System.out.println("下滑一屏，开始位置"+height*1/10+"结束位置"+height*8/10);
				driver.swipe(width/2, height*2/10, width/2, height*8/10, 1000);
			}
			
			if (data.equals("left")) {
				System.out.println("左滑一屏，开始位置"+width*1/10+"结束位置"+width*8/10);
				driver.swipe(width*1/10, height/2, width*8/10, height/2, 1000);
			}
			if (data.equals("right")) {
				System.out.println("右滑一屏，开始位置"+width*8/10+"结束位置"+width*1/10);
				driver.swipe(width*8/10, height/2, width*1/10, height/2, 1000);
			}
		} catch (Exception e) {
			// 异常截图
			GetScreenshot();
			CaseConfig.STEPRESULT = CaseConfig.STEPRESULTFAIL;
		}

	}
	
	/**   点击控件如果点击不到就执行滑动操作
	 * 
	 * @param driver
	 * @param bytype
	 * @param object
	 * @param data
	 * @param checkvalue
	 *  
	 */
	public static void slidingTo(String type,String objectProper, String objectProperParamer,String data, String checkvalue)   {
		//获取屏的宽度
		int width=driver.manage().window().getSize().width;
		//获取屏的高度
		int height=driver.manage().window().getSize().height; 
		try {
			WebElement we = null;
			driver.swipe(width/5,height*9/10, width/5,height/10, 1000);
			driver.swipe(width/5,height*9/10, width/5,height/10, 1000);
			driver.swipe(width/5,height*9/10, width/5,height/10, 1000);
//			clickElementByName("1.在优信能卖到理想价位吗？");
//			we = driver.findElementByXPath("//XCUIElementTypeStaticText[@name='']");
			
			}
      catch (Exception e) {
		
			for (int i=0;i<20;i++){
				System.out.print("滑动的次数"+i);
		   
			if (checkvalue.equals("左滑")) {
				driver.swipe(width*9/10,height/2, width*2/10,height/2, 1000);//从宽度的9/10 滑动到1/10
			}
			if (checkvalue.equals("右滑")) {
				driver.swipe(width*2/10,height/2, width*9/10,height/2, 1000);//从宽度的1/10 滑动到9/10
			}
			if (checkvalue.equals("上滑")) {
				driver.swipe(width/5,height*9/10, width/5,height/10, 1000);	//从高度的9/10 滑动到1/10
				System.out.println("---------------Debug2 上滑---------------------");
			}
			if (checkvalue.equals("下滑")) {
				driver.swipe(width/5,height/10, width/5,height*9/10, 1000);	//从高度的1/10 滑动到9/10
			}
			
			}
		}
	
}
	
	/**
	 * 在指定元素内滑动，需传入webElement，及滑动方向
	 * @param webElement 传入webEelement
	 * @param direction 传入方向值up,down,left,right
	 */
	public static void slidingInElement(WebElement webElement,String direction){
		//获取控件开始位置
		Point location = webElement.getLocation();
		int startX = location.getX();
		int startY = location.getY();
		System.out.println("控件开始X："+startX+"控件开始Y："+startY);
		//获取坐标轴差
		 Dimension q = webElement.getSize();
	     int x = q.getWidth();
	     int y = q.getHeight();
	     System.out.println("坐标差值X："+x+"坐标差值Y："+y);
	     // 计算出控件结束坐标
	     int endX = x + startX;
	     int endY = y + startY;
	     System.out.println("控件结束坐标X："+endX+"控件结束坐标Y："+endY);
		try {
			if (direction.equals("down")) {
				System.out.println("向上滑动，开始像素值"+startY+"滑动到"+endY*9/10);
				driver.swipe(endX/2, startY, endX/2, endY*9/10, 1000);
			}
			if (direction.equals("up")) {
				System.out.println("向下滑动，开始像素值"+endY*9/10+"滑动到"+startY);
				driver.swipe(endX/2, endY*9/10, endX/2, startY, 1000);
			}
			
			if (direction.equals("left")) {
				System.out.println("向左滑动，开始像素值"+endX*9/10+"滑动到"+endX*1/10);
				driver.swipe(endX*9/10, endY-30, endX*1/10, endY-30, 1000);
			}
			if (direction.equals("right")) {
				System.out.println("向右滑动，开始像素值"+endX*1/10+"滑动到"+endX*9/10);
				driver.swipe(endX*1/10, endY/2, endX*9/10, endY/2, 1000);
			}
		} catch (Exception e) {
			// 异常截图
			GetScreenshot();
			CaseConfig.STEPRESULT = CaseConfig.STEPRESULTFAIL;
		}

	}
	/** 读取当前状态 */
	public static String ReadCurrentState(String key, String defaultvalue) {
		ArrayList<String> readstrings = ReadFileByLine(CURRENT_STATE);
		// 获取所有状态信息中包含key的信息
		ArrayList<String> existkeystrings = new ArrayList<String>();
		for (String string : readstrings) {
			if (string.contains("#key" + key)) {
				existkeystrings.add(string);
			}
		}
		// 获取所有包含key的信息中最新的
		String tempString = defaultvalue;
		Long temptimestamp = 0L;
		for (String string : existkeystrings) {
//			System.out.println("值为:"+string);
			try {
				Long timestamp = Long.parseLong(string.substring(string
						.indexOf("#timestamp") + 10));
				if (timestamp > temptimestamp) {
					temptimestamp = timestamp;
					tempString = string;
//					System.out.println("tp:"+tempString);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		if (tempString.equals(defaultvalue)) {
//			System.out.println("等于时："+tempString);
			return tempString;
		} else {
//			System.out.println("不等于时："+tempString.substring(tempString.indexOf("#value") + 6,
//					tempString.indexOf("#timestamp")));
			return tempString.substring(tempString.indexOf("#value") + 6,
					tempString.indexOf("#timestamp"));
		}
	}
	
	//失败提示
		public static void failAndMessage(String msg){
			Assert.assertTrue(false,msg);
		}
		/**
		 * 等待N秒
		 * @param num
		 */
		public static void wait(int num){
			try {
				Thread.sleep(num * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public static void sleep(int num){
			try {
				Thread.sleep(num);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/**
		 * 列表页下拉刷新
		 */
		public static void Check_Pull_Refresh_List() {
			driver.switchTo();
		}
		/**
		 * 判断控件是否存在并返回控件
		 * @param by
		 * @param waitTime
		 * @return
		 */
		public static WebElement waitForVisible(final By by, int waitTime) {
			Date start = new Date();
			WebDriverWait wait = new WebDriverWait(driver, waitTime);
			for (int attempt = 0; attempt < waitTime; attempt++) {
				try {
					driver.findElement(by);
					break;
					} catch (Exception e) {
						driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
						}
				}
			Date end = new Date();
			System.out.println("耗时:"+(end.getTime()-start.getTime())+"ms");
			return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			}
		/**
		 * 判断控件是否存在，并返回boolean值  (注意：appium 1.5版本之后不再支持by.name ，可以参考使用CheckViewVisibiltyByName()方法  )
		 * @param by
		 * @return boolean返回按钮存在与否的状态，存在时返回ture，不存在是返回false
		 */
		public static boolean CheckViewVisibilty(final By by) {
			Date start = new Date();
			WebElement wb = null;
			WebDriverWait wait = new WebDriverWait(driver, 3);
			for (int attempt = 0; attempt < 3; attempt++) {
					try {
						wb= driver.findElement(by);
						if (wb.isDisplayed()&&wb.isEnabled()) {
							break;
						}
						} catch (Exception e) {
							driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
							return false;
							}
			}
			if(wb != null){
				Date end = new Date();
				System.out.println("耗时:"+(end.getTime()-start.getTime())+"ms");
				return true;
			}else {
				Date end = new Date();
				System.out.println("耗时:"+(end.getTime()-start.getTime())+"ms");
				return false;
			}
		}
		
		/**
		 * 点击坐标
		 * @param x
		 * @param y
		 * @param duration
		 */
		public static void clickScreen(int x, int y) {
			//适用于appium 1.4.16版本
//			JavascriptExecutor js = (JavascriptExecutor) driver;
//			HashMap tapObject = new HashMap();
//			tapObject.put("x", x);
//			tapObject.put("y", y);
//			tapObject.put("duration", duration);
//			js.executeScript("mobile: tap", tapObject);
			//
//			driver.tap(1, x, y, duration);
			TouchAction action = new TouchAction(driver);
			action.tap(x, y).perform();
		}
		
		/**
		 * 从买车列表进入详情页
		 * @param i 进入第几个详情页
		 */
		public void toDetail(int i){
			String listTitle = getTextByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
					+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]"
					+ "/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]"
					+ "/android.view.ViewGroup[1]/android.widget.FrameLayout[2]/android.widget.LinearLayout[1]"
					+ "/android.widget.FrameLayout[1]/android.support.v7.widget.RecyclerView[1]"
					+ "/android.widget.FrameLayout["+i+"]/android.widget.FrameLayout[1]"
							+ "/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]"
							+ "/android.widget.TextView[1]");
			List<WebElement> ageElements = driver.findElementsById("tvAge");
			String age = ageElements.get(i-1).getAttribute("text");
			System.out.println("列表页第"+i+"辆车的上牌时间："+age);
			
			List<WebElement> mileageElements = driver.findElementsById("tvMileage");
			String mileage = mileageElements.get(i-1).getAttribute("text");
			int len = mileage.length();
			mileage = mileage.substring(3, len);
			System.out.println("列表页第"+i+"辆车的行驶里程："+mileage);
				
			List<WebElement> priceElements = driver.findElementsById("tvPrice");
			String price = priceElements.get(i).getAttribute("text");  //筛选处价格的id也是tvPrice，所以就不-1了。
			System.out.println("列表页第"+i+"辆车的价格："+price);
			
			findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
					+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]"
					+ "/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]"
					+ "/android.view.ViewGroup[1]/android.widget.FrameLayout[2]/android.widget.LinearLayout[1]"
					+ "/android.widget.FrameLayout[1]/android.support.v7.widget.RecyclerView[1]"
					+ "/android.widget.FrameLayout["+i+"]/android.widget.FrameLayout[1]"
							+ "/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]"
							+ "/android.widget.TextView[1]").click();
			System.out.println("进入第"+i+"个详情页");
			wait(3);
			if(CheckViewVisibilty(By.id("alertdialog_title"))) {
				if(findElementById("alertdialog_title").getText().equals("该车已下架")) {
					System.out.println("此车是已下架车辆");
				}
			}else if (CheckViewVisibilty(By.id("ivYiShou"))) {
				System.out.println("此车是已售车辆");
			}else {
	//			if(listTitle.substring(0, 4).equals("今日特惠")) {
	//				listTitle = listTitle.substring(5, listTitle.length());
	//			}
				wait(3);
				//标题
				String detailTitle = getTextById("tvVehicleDetailsCarName");
				System.out.println("list:"+listTitle+"***************"+"detail:"+detailTitle);
				//.trim()是为了去掉字符串首尾的空格
				if (listTitle.trim().replace(" ", "").equals(detailTitle.trim().replace(" ", "")) || 
						detailTitle.trim().replace(" ", "").contains(listTitle.trim().replace(" ", ""))) {
					System.out.println("进入详情页，并且详情页标题与列表页一致。");
				}else {
					failAndMessage("未进入指定详情页或者详情页标题"+detailTitle+",与列表页标题"+listTitle+"不一致，请检查");
				}
				//价格
				String detailPrice = getTextById("tvVehicleDetailsPrice");
				if(price.equals(detailPrice)) {
					System.out.println("进入详情页，并且详情页价格与列表页一致。");
				}else {
					failAndMessage("进入详情页，并且详情页价格:"+detailPrice+",与列表页价格"+price+"不一致，请检查");
				}
				String priceStr = getTextById("tvDownPayment");

				int width = driver.manage().window().getSize().width;
				int height = driver.manage().window().getSize().height;
				driver.swipe(width/2, height*4/5, width/2, height*2/5, 1000);
				wait(3);
				//上牌年限
				String detailAge = getTextById("tvVehicleDetailsRegistDate").substring(0, 5);
				System.out.println(detailAge);
				if(age.equals(detailAge)) {
					System.out.println("进入详情页，并且详情页上牌时间与列表页一致。");
				}else {
					failAndMessage("进入详情页，并且详情页上牌时间"+detailAge+"与列表页上牌时间"+age+"不一致，请检查");
				}
				//行驶里程
				String detailMileage = getTextById("tvVehicleDetailsMileage");
				if(mileage.equals(detailMileage)) {
					System.out.println("进入详情页，并且详情页行驶里程与列表页一致。");
				}else {
					failAndMessage("进入详情页，并且详情页行驶里程"+detailMileage+"与列表页里程"+mileage+"不一致，请检查。");
				}
				driver.swipe(width/2, height*1/5, width/2, height*4/5, 1000);
				wait(1);
			}
		}
		
		/**
		 * 从买车列表进入半价车详情页
		 * @param i 进入第几个详情页
		 */
		public void toDetail_half(int i){
			String listTitle = getTextByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
					+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]"
					+ "/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]"
					+ "/android.view.ViewGroup[1]/android.widget.FrameLayout[2]/android.widget.LinearLayout[1]"
					+ "/android.widget.FrameLayout[1]/android.support.v7.widget.RecyclerView[1]"
					+ "/android.widget.FrameLayout["+i+"]/android.widget.FrameLayout[1]"
							+ "/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]"
							+ "/android.widget.TextView[1]");
			
			List<WebElement> ageElements = driver.findElementsById("tvAge");
			String age = ageElements.get(i-1).getAttribute("text");
			System.out.println("列表页第"+i+"辆车的上牌时间："+age);
			
			List<WebElement> mileageElements = driver.findElementsById("tvMileage");
			String mileage = mileageElements.get(i-1).getAttribute("text");
			int len = mileage.length();
			mileage = mileage.substring(3, len);
			System.out.println("列表页第"+i+"辆车的行驶里程："+mileage);
				
			List<WebElement> priceElements = driver.findElementsById("tvPrice");
			String price = priceElements.get(i).getAttribute("text");  //筛选处价格的id也是tvPrice，所以就不-1了。
			System.out.println("列表页第"+i+"辆车的价格："+price);
			
			List<WebElement> halfPriceElements = driver.findElementsById("tvHalfPrice");
			String halfPrice = halfPriceElements.get(i-1).getAttribute("text");
			System.out.println("列表页第"+i+"辆车的首付价格："+halfPrice);
			
			List<WebElement> monthlyPriceElements = driver.findElementsById("tvmonthlyprice");
			String monthlyPrice = monthlyPriceElements.get(i-1).getAttribute("text");
			System.out.println("列表页第"+i+"辆车的月供价格："+monthlyPrice);
			
			findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
					+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]"
					+ "/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]"
					+ "/android.view.ViewGroup[1]/android.widget.FrameLayout[2]/android.widget.LinearLayout[1]"
					+ "/android.widget.FrameLayout[1]/android.support.v7.widget.RecyclerView[1]"
					+ "/android.widget.FrameLayout["+i+"]/android.widget.FrameLayout[1]"
							+ "/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]"
							+ "/android.widget.TextView[1]").click();
			System.out.println("进入第"+i+"个详情页");
			wait(3);
			if(CheckViewVisibilty(By.id("alertdialog_title"))) {
				if(findElementById("alertdialog_title").getText().equals("该车已下架")) {
					System.out.println("此车是已下架车辆");
				}
			}else if (CheckViewVisibilty(By.id("ivYiShou"))) {
				System.out.println("此车是已售车辆");
			}else {
	//			if(listTitle.substring(0, 4).equals("今日特惠")) {
	//				listTitle = listTitle.substring(5, listTitle.length());
	//			}
				wait(3);
				//标题
				String detailTitle = getTextById("tvVehicleDetailsCarName");
				System.out.println("list:"+listTitle+"***************"+"detail:"+detailTitle);
				//.trim()是为了去掉字符串首尾的空格
				if (listTitle.trim().replace(" ", "").equals(detailTitle.trim().replace(" ", "")) || 
						detailTitle.trim().replace(" ", "").contains(listTitle.trim().replace(" ", ""))) {
					System.out.println("进入详情页，并且详情页标题与列表页一致。");
				}else {
					failAndMessage("未进入指定详情页或者详情页标题与列表页不一致，请检查");
				}
				//价格
				String detailPrice = getTextById("tvVehicleDetailsPrice");
				if(price.equals(detailPrice)) {
					System.out.println("进入详情页，并且详情页价格与列表页一致。");
				}else {
					failAndMessage("进入详情页，并且详情页价格与列表页不一致，请检查。");
				}
				String priceStr = getTextById("tvDownPayment");
				//首付价格
				String detailHalfPrice = priceStr.split(" ")[0];
				System.out.println(detailHalfPrice);
				if(detailHalfPrice.substring(0, 2).equals("一成")) {
					detailHalfPrice = detailHalfPrice.substring(2, detailHalfPrice.length());
				}
				if(halfPrice.equals(detailHalfPrice)) {
					System.out.println("进入详情页，并且详情页首付价格与列表页一致。");
				}else {
					failAndMessage("进入详情页，并且详情页首付价格与列表页不一致，请检查。");
				}
				//月供价格
				String detailMonthlyPrice = priceStr.split(" ")[1];
				System.out.println(detailMonthlyPrice);
				if(monthlyPrice.equals(detailMonthlyPrice)) {
					System.out.println("进入详情页，并且详情页月供价格与列表页一致。");
				}else {
					failAndMessage("进入详情页，并且详情页月供价格与列表页不一致，请检查。");
				}
				int width = driver.manage().window().getSize().width;
				int height = driver.manage().window().getSize().height;
				driver.swipe(width/2, height*4/6, width/2, height*3/6, 1000);
				wait(3);
				//上牌年限
				String detailAge = getTextById("tvVehicleDetailsRegistDate").substring(0, 5);
				if(age.equals(detailAge)) {
					System.out.println("进入详情页，并且详情页上牌时间与列表页一致。");
				}else {
					failAndMessage("进入详情页，并且详情页上牌时间与列表页不一致，请检查。");
				}
				//行驶里程
				String detailMileage = getTextById("tvVehicleDetailsMileage");
				if(mileage.equals(detailMileage)) {
					System.out.println("进入详情页，并且详情页行驶里程与列表页一致。");
				}else {
					failAndMessage("进入详情页，并且详情页行驶里程与列表页不一致，请检查。");
				}
				driver.swipe(width/2, height*3/6, width/2, height*4/6, 1000);
				wait(2);
			}
		}
		
		/**
		 * 从首页进入新列表页
		 * 
		 */
		public void CheckNewListFromHomePage() {
			if(CheckViewVisibilty(By.id("title_icon_id")) ||
					CheckViewVisibilty(By.id("imgBtBack"))) {	
			}else {
				failAndMessage("新列表页返回按钮异常，或者没有进入到新列表页，请检查。");
			}
			if(CheckViewVisibilty(By.id("vgSearch"))) {	
			}else {
				failAndMessage("新列表页搜索框异常。");
			}
			if(CheckViewVisibilty(By.id("tvSort"))&&
					CheckViewVisibilty(By.id("tvBrand"))&&
					CheckViewVisibilty(By.id("tvPrice"))&&
					CheckViewVisibilty(By.id("tv_filter"))) {	
			}else {
				failAndMessage("新列表页排序、或者品牌、或者价格、或者高级筛选按钮异常，请检查。");
			}
			if(CheckViewVisibilty(By.id("market_uiswitch"))) {	
			}else {
				failAndMessage("新列表页没有车辆/客服胶囊。");
			}	
		}
		
		/**
		 * 从新列表页进入详情页
		 * 
		 */
		public void toDetailFromNewList(){
			System.out.println("进入第1个详情页");
			String listTitle = findElementById("tvCarWholeName").getText();
			findElementById("tvAge").click();
			wait(3);
			if(CheckViewVisibilty(By.id("alertdialog_title"))) {
				if(findElementById("alertdialog_title").getText().equals("该车已下架")) {
					System.out.println("此车是已下架车辆");
				}
			}else {
				String detailTitle = getTextById("tvVehicleDetailsCarName");
				System.out.println("list:"+listTitle+"***************"+"detail:"+detailTitle);
				//.trim()是为了去掉字符串首尾的空格
				if (listTitle.trim().replace(" ", "").equals(detailTitle.trim().replace(" ", "")) || 
						detailTitle.trim().replace(" ", "").contains(listTitle.trim().replace(" ", ""))) {
					System.out.println("进入详情页，并且详情页标题与列表页一致。");
				}else {
					
					failAndMessage("未进入详情页或者详情页标题与列表页不一致，请检查");
				}
			}
		}
		
		/**
		 * 根据xpath获取元素文本
		 * 
		 * @param xpath
		 * @return 文本
		 */
		public String getTextByXpath(String xpath) {
			try {
				WebDriverWait wait=new WebDriverWait(driver, 20);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
				String text = driver.findElement(By.xpath(xpath)).getText();
				System.out.println("当前返回的文本--->    "+text);
				return text;
			} catch (Exception e) {
				e.printStackTrace();
				return null;

			}
		}
		
		/**
		 * 根据id获取元素文本
		 * 
		 * @param id
		 * @return 文本
		 */
		public String getTextById(String id) {
			try {
				WebDriverWait wait=new WebDriverWait(driver, 20);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
				String text = driver.findElement(By.id(id)).getText();
				System.out.println("当前返回的文本--->    "+text);
				return text;
			} catch (Exception e) {
				e.printStackTrace();
				return null;

			}
		}
		
		/**
		 * 检查详情页titlebar显示
		 */
		public void checkTitlebar_Detail() {
			if(CheckViewVisibilty(By.id("alertdialog_title"))) {
				if(findElementById("alertdialog_title").getText().equals("该车已下架")) {
					System.out.println("此车是已下架车辆");
				}
			}else if (CheckViewVisibilty(By.id("ivYiShou"))) {
				System.out.println("此车是已售车辆");
			}else {
				wait(1);
				if(CheckViewVisibilty(By.id("imgBtBack"))&&
						CheckViewVisibilty(By.id("imgChat"))&&
						CheckViewVisibilty(By.id("ivCompare"))&&
						CheckViewVisibilty(By.id("llSecond"))&&
						CheckViewVisibilty(By.id("ivShare"))) {
					System.out.println("详情页顶部控件返回、IM聊天按钮、对比按钮、看车清单按钮以及分享按钮UI显示正常");
					clickElementById("ivShare");
					Assert.assertEquals(getTextByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
							+ "/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]"
							+ "/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]"), "微信", "微信显示不正常");
					Assert.assertEquals(getTextByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
							+ "/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]"
							+ "/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.TextView[1]"), "朋友圈", "朋友圈显示不正常");
					Assert.assertEquals(getTextByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
							+ "/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]"
							+ "/android.widget.LinearLayout[1]/android.widget.LinearLayout[3]/android.widget.TextView[1]"), "QQ", "QQ显示不正常");
					Assert.assertEquals(getTextByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
							+ "/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]"
							+ "/android.widget.LinearLayout[1]/android.widget.LinearLayout[4]/android.widget.TextView[1]"), "链接", "链接显示不正常");
					findElementById("share_cancel").click();
				}else {
					failAndMessage("详情页顶部控件按钮UI异常，请检查。");
				}
				if(CheckViewVisibilty(By.id("llFocus"))&&   //关注
						CheckViewVisibilty(By.id("rlCenter"))&&  //我要优惠按钮
						CheckViewVisibilty(By.id("rlRight"))&&   //电话客服
						CheckViewVisibilty(By.id("rlOnlineService"))) {//在线客服
					System.out.println("详情页底部关注、我要优惠、电话客服以及在线客服UI显示正常");
					if(findElementById("tvFocus").getText().equals("关注") ||
							findElementById("tvFocus").getText().equals("已关注")) {
					}else {
						failAndMessage("详情页底部关注按钮文案异常，请检查。");
					}
					if(findElementById("tvCenter").getText().equals("我要优惠")) {
					}else {
						failAndMessage("详情页底部我要优惠按钮文案异常，请检查。");
					}
					if(findElementById("tvRight").getText().equals("电话客服")) {
					}else {
						failAndMessage("详情页底部电话客服按钮文案异常，请检查。");
					}
					if(findElementById("tvOnlineService").getText().equals("在线咨询")) {
					}else {
						failAndMessage("详情页底部在线客服按钮文案异常，请检查。");
					}
				}else if (CheckViewVisibilty(By.id("llFocus"))&&   //关注
						CheckViewVisibilty(By.id("rlRight"))   //电话客服
						) {
					System.out.println("详情页底部关注、电话客服UI显示正常");
					if(findElementById("tvFocus").getText().equals("关注") ||
							findElementById("tvFocus").getText().equals("已关注")) {
					}else {
						failAndMessage("详情页底部关注按钮文案异常，请检查。");
					}
					if(findElementById("tvRight").getText().equals("电话客服")) {
					}else {
						failAndMessage("详情页底部电话客服按钮文案异常，请检查。");
					}
				}
				else {
					failAndMessage("详情页底部控件按钮UI异常，请检查。");
				}
			}
		}
		
		/**
		 * 检查webview的titlebar显示
		 */
		public void checkTitlebar_Webview(String titleString){
			for (int i = 0; i < 29; i++) {
				sleep(1000);
				if (findElementById("tvTitle").getText().equals(titleString)) {
					break;
				}else if(i == 28){
					GetScreenshot();
					failAndMessage("title文案"+titleString+"15s没有正常展示");
				}
			}
		}
		
	/**
	 * 获取当前页面信息并储存
	 */
		public void getPageSourseToSave(){
			String temp = driver.getPageSource();
			BufferedWriter bw = null;
			try {
				FileWriter fw = new FileWriter(new File("./CaseRunTempFiles/PageSourse.xml"),false);
				bw = new BufferedWriter(fw);
				bw.write(temp+"\n");
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/**
		 * 检查返回按钮+文案类型的titlebar
		 * @param titleString 标题文案
		 */
public void checkTitlebar1(String titleString) {
	for (int i = 0; i < 29; i++) {
		if (//CheckViewVisibilty(By.id("imgBtBack"))&&
				findElementById("tvTitle").getText().equals(titleString)) {
			break;
		}else if (i==28) {
			GetScreenshot("checktltlebar1");
			failAndMessage("title文案显示不正常，应为："+titleString+",实际为："+findElementById("tvTitle").getText());
		}
	}
}
/**
 * 文案类型的titlebar
 * @param titleString 标题文案
 */
public void checkTitlebar3(String titleString) {
	for (int i = 0; i < 29; i++) {
		System.out.println(findElementById("tv_title").getText());
		if (//CheckViewVisibilty(By.id("imgBtBack"))&&
				findElementById("tv_title").getText().equals(titleString)) {
			break;
		}else if (i==28) {
			GetScreenshot("checktltlebar3");
			failAndMessage("title文案显示不正常，应为："+titleString+",实际为："+findElementById("tvTitle").getText());
		}
	}
}


/**
 * 检查返回按钮+文案类型+button的titlebar
 * @param titleString 标题文案
 * @param buttonString 按钮文案
 */
public void checkTitlebar2(String titleString,String buttonString) {
for (int i = 0; i < 29; i++) {
if (CheckViewVisibilty(By.id("imgBtBack"))&&
		findElementById("tvTitle").getText().equals(titleString)
		&&findElementById("btManage").getText().equals(buttonString)) {
	break;
}else if (i==28) {
	GetScreenshot("checktltlebar1");
	failAndMessage("title文案显示不正常，应为："+titleString+",实际为："
	+findElementById("tvTitle").getText()+"或button文案显示错误应为"+buttonString+findElementById("btManage").getText());
}
}
}
	/**
	 * 点击车市列表中的客服悬浮按钮,需要先进入买车页
	 */
public void ClickOnChatByList(){
	wait(2);
	int windowX = driver.manage().window().getSize().width;
	int windowY = driver.manage().window().getSize().height;
	int startY = findElementById("market_uiswitch").getLocation().y;
	int n = findElementById("market_uiswitch").getSize().getWidth()/2;//按钮X中间值
	int m = findElementById("market_uiswitch").getSize().getHeight()/2;//按钮Y中间坐标值
	int x = windowX/2+n/4;
	int y = startY+m;
	System.out.println("屏幕大小"+driver.manage().window().getSize());
	System.out.println("控件大小"+findElementById("market_uiswitch").getSize());
	System.out.println("x "+x+"y "+y);
//	driver.tap(1, x, y, 1);
	TouchAction action = new TouchAction(driver);
	action.tap(x, y).perform();
	wait(1);
}

/**
 * 从native切换到webview，需要在debug包中使用
 */
public void switchToWebView(){
	Set<String> contextNames = driver.getContextHandles();
	System.out.println(contextNames);
	for (String contextName : contextNames) {
		System.out.println("contextName是:"+contextName);
		if (contextName.contains("WEBVIEW")){
				try {
					driver.context(contextName);
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("使用switchto切换");
					driver.switchTo().window(contextName);
					e.printStackTrace();
			}
		}else{
			System.out.println("不是WEBVIEW"); 
		}
		}//测试切换到webview
	}

/**
 * 从webview切换到native，需要在debug包中使用
 **/
public void switchToNative(){
	Set<String> contextNames = driver.getContextHandles();
	System.out.println(contextNames);
	for (String contextName : contextNames) {
		System.out.println("contextName是:"+contextName);
		if (contextName.contains("NATIVE_")){
			try {
				driver.context(contextName);
				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("走switchto");
				driver.switchTo().window(contextName);
			}
		}else{
		System.out.println("不是NATIVE"); 
		}
		}//测试切换到native
}

/**
 * 列表页切换城市
 */
public void switchCity(String name) {
	clickElementById("btChooseCity");
	wait(3);
	clickElementByName(name);
//	if (name.equals("京津冀")||name.equals("江浙沪")
//			||name.equals("云贵川")||name.equals("guang")) {
//		clickElementByName(name);
//		clickElementByName("不限");
//	}else if (name.contains("市")) {
//		switch(name){
//			case "天津市" :
//				clickElementByName("京津冀");
//				clickElementByName(name);
//				break;
//			case "河北省" :
//				clickElementByName("京津冀");
//				clickElementByName(name);
//				break;
//			case "江苏省" :
//				clickElementByName("江浙沪");
//				clickElementByName(name);
//				break;
//			case "浙江省" :
//				clickElementByName("江浙沪");
//				clickElementByName(name);
//				break;
//			case "上海市" :
//				clickElementByName("江浙沪");
//				clickElementByName(name);
//				break;
//				
//		}
//	}else {
//		clickElementByName(name);
////		scrollToElementClick(name, 3);
//	}
  }

public String scrollToElementClick(String text,int times) {
    WebElement element = driver.findElementByName(text);
    try {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HashMap<String, String> scrollObject = new HashMap<>();
        scrollObject.put("direction", "down");
        //value 为false时候wdLabel
        scrollObject.put("element", ((RemoteWebElement) element).getId());
//        scrollObject.put("predicateString", "value == "+ text);
//        scrollObject.put("predicateString", "wdLabel == '" + text + "'");
        js.executeScript("mobile:scroll", scrollObject);
        element.click();
        wait(times);
        return element.getText();//blank
    } catch (Exception e) {
       e.printStackTrace();
       return element.getText();
    }
}

//获取车辆档案中上牌时间，并格式为2017-08
		public  String getCarDate(){
			String nian = findElementById("tvVehicleDetailsRegistDate").getText().substring(0, 4);
			String yue = findElementById("tvVehicleDetailsRegistDate").getText().substring(6, 8);
			String date =nian+"-"+yue;
			System.out.println(date);
			return date;
		}
		/**
		 * @param toast
		 */
		public static void toastCheck(String toast) {
			try {
				final WebDriverWait wait = new WebDriverWait(driver, 2);
				Assert.assertNotNull(wait.until(
						ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@text," + toast + ")]"))));
				System.out.println("找到了toast:" + toast);
			} catch (Exception e) {
				throw new AssertionError("找不到：" + toast);
			}
		}
		
		/**
		 * 控件比对，chekcLabel  区别于 chekcValue  通过data控制获取文本的方式 因为有些控件没有value只有name
		 * @param driver       Driver 实例
		 * @param type         定位方式
		 * @param objectProper 元素属性(如id，xpath，name)
		 * @param objectProperParamer 当选择xpath定位时最后一位可参数
		 * @param attribute    需要检查的元素属性
		 * @param checkvalue   校验数据
		 * 
		 */
		public static void chekcLabel(String type,String objectProper,String objectProperParamer, String attribute, String checkvalue){
			if (objectProper.contains("element") && !objectProperParamer.equals("")) {
				// 当属性字段中存在element 字符且data 有值，第一步先将属性值替换
				objectProper = objectProper.replace("element", objectProperParamer);
			}
			System.out.println("元素路径=="+objectProper);
			// 控件定位
			WebElement webElement = getWebelement(type, objectProper, objectProperParamer, attribute, checkvalue);
			try {
				if (webElement != null) {
					// 点击控件
					String checkText = webElement.getAttribute(attribute).trim();
					Thread.sleep(2000);
	                System.out.println("--------------打印ValueAttribute--------------------"+checkText);
					if (checkText.equals(checkvalue.trim())) {
						// 预期结果和实际结果相同
						System.out.println("--->   "+checkText+"   等价于"+"   "+checkvalue.trim());
						CaseConfig.STEPRESULT = CaseConfig.STEPRESULTPASS;
					} else {
						// 预期结果和实际结果不同
						CaseConfig.STEPRESULT = CaseConfig.STEPRESULTFAIL;
						// 异常截图
						GetScreenshot();
						failAndMessage("预期值与实际值不符，预期为:"+checkvalue+"实际为"+checkText);
					}
				}
			} catch (Exception e) {
				CaseConfig.STEPRESULT = CaseConfig.STEPRESULTFAIL;
				// 异常截图
				GetScreenshot();
				failAndMessage("指定控件不存在");
			}
		}
		
		/**
		 * 控件比对，chekcLabel  区别于 chekcValue  通过data控制获取文本的方式 因为有些控件没有value只有name
		 * @param driver       Driver 实例
		 * @param type         定位方式
		 * @param objectProper 元素属性
		 * @param data         测试参数化数据
		 * @param checkvalue   校验数据
		 * 
		 */
		public static void chekcLabel_ex(String type,String objectProper,String objectProperParamer, String data, String checkvalue){
			if (objectProper.contains("element") && !objectProperParamer.equals("")) {
				// 当属性字段中存在element 字符且data 有值，第一步先将属性值替换
				objectProper = objectProper.replace("element", objectProperParamer);
			}
			System.out.println("元素路径=="+objectProper);
			// 控件定位
			WebElement webElement = getWebelement(type, objectProper, objectProperParamer, data, checkvalue);
			try {
				if (webElement != null) {
					// 点击控件
					if(webElement.getText().trim() != null){
						String checkText = webElement.getText().trim();
						Thread.sleep(1000);
		                System.out.println("--------------打印ValueAttribute--------------------"+checkText);
						if (checkText.equals(checkvalue.trim())) {
							// 预期结果和实际结果相同
							System.out.println("--->   "+checkText+"   等价于"+"   "+checkvalue.trim());
							CaseConfig.STEPRESULT = CaseConfig.STEPRESULTPASS;
						} else {
							// 预期结果和实际结果不同
							CaseConfig.STEPRESULT = CaseConfig.STEPRESULTFAIL;
							// 异常截图
							GetScreenshot();
							failAndMessage("预期值与实际值不符，预期为:"+checkvalue+"实际为"+checkText);
						}
					}
					

				}
			} catch (Exception e) {
				CaseConfig.STEPRESULT = CaseConfig.STEPRESULTFAIL;
				// 异常截图
				GetScreenshot();
				failAndMessage("指定控件不存在");
			}
		}
		
		/**
		 * 检查店铺详情页titlebar显示
		 */
		public void checkTitleBarShop() {
			for (int i = 0; i < 30; i++) {
				if (CheckViewVisibilty(By.id("imgBtBack"))&&CheckViewVisibilty(By.id("ivShare"))
						&&waitForVisible(By.id("tvTitle"), 3).getText().equals("店铺详情")) {
					break;
				}else if(i==29){
					GetScreenshot();
					failAndMessage("店铺详情页titlebar显示异常");
				}
			}
		}
		
		/**
		 * 某方向滑动直到边界，如底部，顶部(在没有边界标识的时候使用)
		 * @param direction
		 * @param strSrc
		 * @param strDes
		 * @return
		 * 实现滑动前后的字符串集合list的对比
		 */
		public void swipeUntilBoundary(String direction,By by){
			boolean flag=false;
			while(!flag){
				List<String> strSrc=new ArrayList<String>();
				List<String> strDes=new ArrayList<String>();
				//滑动前定位元素并将元素的text添加到集合strSrc里
				List<AndroidElement> elementOld=driver.findElements(by);
				for(AndroidElement ae:elementOld){
					strSrc.add(ae.getText());
				}
				sliding(direction);
				this.wait(1000);
				//滑动后定位元素并将元素的text添加到集合strSrc里
				List<AndroidElement> elementNew=driver.findElements(by);
				for(AndroidElement ae:elementNew){
					strDes.add(ae.getText());
				}
				flag=this.listStrEquals(strSrc,strDes);
			}
		}
		
		/**
		 * 判断两个字符串集合list是否元素对应相等
		 * @param strSrc
		 * @param strDes
		 * @return
		 */
		public static Boolean listStrEquals(List<String> strSrc,List<String> strDes){
			Boolean flag=false;
			if((!strSrc.isEmpty()&&strSrc!=null)&&(!strDes.isEmpty()&&strDes!=null)){
				if(strSrc.size()==strDes.size()){
					for(int i=0;i<strDes.size();i++){
						if(strSrc.get(i).equals(strDes.get(i))){
							flag=true;
							continue;
						}else{
							flag=false;
							//System.out.println(strSrc.get(i)+" "+strDes.get(i));
							break;
						}
					}
				}else{
					System.out.println("两个list大小不相等");
				}
			}else{
				System.out.println("list为空或者为null");
			}
			return flag;
		}
		
		/**
		 * 向某方向滑动直到某元素出现
		 * @param by 查找对象
		 * @param direction 方向
		 * @param duration 每次滑动时间，单位毫秒
		 * @param findCount 查找次数
		 */
		public boolean swipeUntilElementAppear(By by,String direction,int findCount){
			//this.implicitlyWait(3);
			boolean flag=false;
			while(!flag&&findCount>0){
				try {
					driver.findElement(by);
					flag=true;
				} catch (Exception e) {
					// TODO: handle exception
					sliding(direction);
					findCount--;
				}
			}
			return flag;
		}
		public AndroidElement swipeUntilElement(By by,String direction,int findCount){
			//this.implicitlyWait(3);
			AndroidElement element=null;
			boolean flag=false;
			while(!flag&&findCount>0){
				try {
					element=(AndroidElement) driver.findElement(by);
					flag=true;
				} catch (Exception e) {
					// TODO: handle exception
					sliding(direction);
					findCount--;
				}
			}
			return element;
		}
		
		//获取当前登录的手机号
		public String getLoginPhoneNum() {
			String phoneNum = "";
			gotoCateSet(4);
			wait(1);
			if(findElementById("tvTips").getText().equals("147****6911")) {
				phoneNum = CaseConfig.USERNAME_PUBLIC1;
			}else if(findElementById("tvTips").getText().equals("147****6912")) {
				phoneNum = CaseConfig.USERNAME_PUBLIC2;
			}else if(findElementById("tvTips").getText().equals("147****6913")) {
				phoneNum = CaseConfig.USERNAME_PUBLIC3;
			}else if(findElementById("tvTips").getText().equals("147****6914")) {
				phoneNum = CaseConfig.USERNAME_PUBLIC4;
			}else if(findElementById("tvTips").getText().equals("147****6915")) {
				phoneNum = CaseConfig.USERNAME_PUBLIC5;
			}else if(findElementById("tvTips").getText().equals("147****6916")) {
				phoneNum = CaseConfig.USERNAME_PUBLIC6;
			}else {
				failAndMessage("登录账号异常，请检查。");
			}
			return phoneNum;
		}
		
		//车市列表页切换单双列(在车市页执行)
		//type：single：单列
		//     double：双列
		public void changeListShow(String type) {
			clickElementByName("筛选");
			wait(1);
			clickElementByName("排放标准");
			clickElementByName("视图模式");
			if(type.equals("single")) {
				clickElementByName("单列");
			}else {
				clickElementByName("双列");
			}
			clickElementById("ll_search");
			wait(1);
		}
		
		//车市页切换到分期购列表（在车市页执行）
		public void clickFQG() {
			clickElementByName("筛选");
			wait(1);
			clickElementByName("优信服务");
			wait(1);
			clickElementByName("分期购");
			clickElementById("ll_search");
			wait(1);
		}
		
		public static   WebElement getListByLocator(final By  locator,int index ) {
			try {
				List<WebElement> list = driver.findElements(locator);
				 if (null != list && list.size() > 0) {
		            	System.out.println("索引返回节点个数--->>  "+list.size());
//		            	System.out.println("索引元素对应的文本值--->>  " +list.get(index);
		                return list.get(index);
		            } else {
		                return null;
		            }
			} catch (Exception e) {
				GetScreenshot("索引错误");
				e.printStackTrace();
				return null;
		   }
	   }
		
		public static void backBTN() {
			if (CheckViewVisibilty(By.id("imgBtBack"))) {
	        	clickElementById("imgBtBack");
	        } else if(CheckViewVisibilty(By.id("tvBack"))){
	        	clickElementById("tvBack");
	        }else {
				driver.pressKeyCode(AndroidKeyCode.BACK);
			}
			wait(1);
		}
		
		//点击方案B首页(买车商城、卖车通道、免费估价、购车顾问、一成购、真实车况、超值好车)功能区按钮，前提是进入首页
		public void clickButtonShouYe(String text) {
			int width = findElementById("multipletoolimage").getSize().getWidth()/2;
			int height = findElementById("multipletoolimage").getSize().getHeight()/2;
			int startX = findElementByName(text).getLocation().x;
			int startY = findElementByName(text).getLocation().y;
			int x = startX + width;
			int y = startY - height;
			System.out.println(x);
			System.out.println(y);
//			driver.tap(1, x, y, 1);
			TouchAction action = new TouchAction(driver);
			action.tap(x, y).perform();
			System.out.println("点击"+text);
			wait(2);
		}
		
		//点击方案B首页一成购、真实车况或者超值好车的详情按钮，前提是进入首页
		public void clickDetaileShouYe(String text) {
			//id和text一样的时候，找前面或者后面的控件来确定y坐标；
			int startX = findElementById("yichenggou_detaile").getLocation().x;
			int startY = findElementByName(text).getLocation().y;
			int m = findElementById("yichenggou_detaile").getSize().getWidth()/2; //控件的宽的一半
			int n = findElementById("yichenggou_detaile").getSize().getHeight()/2; //控件的高的一半
			int x = startX + m;
			int y = startY + n;
			System.out.println(x);
			System.out.println(y);
//			driver.tap(1, x, y, 1);
			TouchAction action = new TouchAction(driver);
			action.tap(x, y).perform();
			wait(1);
		}
		/**
		 * @检测元素id对应的文案是否正确
		 * @param locator:定位参数
		 * @param name：检测文案名称
		 */
		public void get_text(final By locator,String name){
			try {
				WebElement list = driver.findElement(locator);
				if (list.getText().equals(name)) {
					reports_HomePageTest.log(LogStatus.INFO, "文案检测正确>>>"+name);
					System.out.println("文案检测正确>>>"+name);
				}else {
					reports_HomePageTest.log(LogStatus.INFO, "文案检测失败>>>"+name);
					System.out.println("文案检测失败>>>"+name);
					failAndMessage("文案检测失败>>>"+name);
				}
			} catch (Exception e) {
				// TODO: handle exception
				GetScreenshot("test_yichang");
				failAndMessage("文案检测失败,请手动查看");
			}
		}
		
		/*
		 * 退出登录方法
		 * 进入我的页面，判断app是否已登录，未登录什么都不做，已登录的情况下退出登录
		 */
		public void loginOut() {
				clickElementById("rbWo");
				if (findElementById("tvTips").getText().equals("点击登录")) {  // 未登陆
					System.out.println("当前登录状态是未登录状态");
				}else {//已登录
					int width = driver.manage().window().getSize().width;
					int height = driver.manage().window().getSize().height;
					driver.swipe(width/2, height*4/5, width/2, height*3/5, 1000);
					wait(2);
			        clickElementById("vgSetting");		    
					sleep(2000);
					driver.findElement(By.xpath("//android.widget.TextView[@text='退出登录']")).click();
					sleep(1000);
					clickElementById("bt_confirm_ok");
					sleep(1000);
					driver.swipe(width/2, height*3/5, width/2, height*4/5, 1000);
					wait(1);
				}
		}
		
}

