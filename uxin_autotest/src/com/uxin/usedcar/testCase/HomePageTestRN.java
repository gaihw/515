package com.uxin.usedcar.testCase;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.HowToUseLocators;
import io.appium.java_client.pagefactory.iOSFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import static org.junit.Assert.*;

import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Driver;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.MidiDevice.Info;
import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.apache.http.client.ClientProtocolException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.seleniumhq.jetty9.server.Authentication.Failed;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;
import com.sun.jna.win32.W32APIFunctionMapper;
import com.uxin.usedcar.test.libs.BaseTest;
import com.uxin.usedcar.test.libs.CaseConfig;

@SuppressWarnings("unused")
public class HomePageTestRN extends BaseTest{
	
	 @BeforeClass
	  public static void first()  throws Exception {
		 reports_HomePageTestRN.init("./report/HomePage/reportHomePageRN.html", true);
	
	  }

	@AfterClass 
	public static void last() {
		reports_HomePageTestRN.endTest();
		driver.quit();
		System.out.println("tearDown");
	}
	
	/**
	 * @Name 1100_banner
	 * @Catalogue 首页-banner
	 * @Grade 高级
	 * @FunctionPoint 首页-banner,点击banner查看跳转
	 **/
	@Test
	public void test_1100_banner() {
		reports_HomePageTestRN.startTest("test_1100_banner");
		gotocate(1);
		wait(1);
		int y = findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]").getLocation().y;
		int windowX = driver.manage().window().getSize().width/2;
		TouchAction action = new TouchAction(driver);
		System.out.println(windowX+","+y+200);
		action.tap(windowX, y+200).perform();
		reports_HomePageTestRN.log(LogStatus.INFO, "点击banner");
		wait(2);
		if(CheckViewVisibilty(By.id("tvBack"))) {
//				CheckViewVisibilty(By.id("tvTitle"))&&
//				CheckViewVisibilty(By.id("ivRefresh"))) {
			reports_HomePageTestRN.log(LogStatus.INFO, "点击banner页面进行了跳转");
			System.out.println("点击banner页面进行了跳转");
		}else {
			reports_HomePageTestRN.log(LogStatus.ERROR, "点击banner页面没有跳转");
			failAndMessage("点击banner页面没有跳转");
		}
	}
	
	/**
	 * @Name 1000_CITY
	 * @Catalogue 首页-城市切换
	 * @Grade 中级
	 * @FunctionPoint 首页-城市切换；检查首页修改城市是否会同步到买车页的城市
	 **/
	@Test
	public void test_1000_CITY() {
		reports_HomePageTestRN.startTest("test_1000_CITY");
		gotocate(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "检查当前城市定位是否为北京。");
		if(!CheckViewVisibiltyByName("北京")) {
			reports_HomePageTestRN.log(LogStatus.ERROR, "当前城市定位不是北京，请检查！");
			failAndMessage("当前城市定位不是北京，请检查！");
		}
//		try {
			clickElementByName("北京");
			wait(2);
			clickElementByName("成都");
			wait(1);
			gotoCateSet(1);   //车市
			wait(3);
			if(CheckViewVisibiltyByName("成都")) {
				reports_HomePageTestRN.log(LogStatus.INFO, "首页修改城市可以正常同步到买车页。");
				System.out.println("首页修改城市可以正常同步到买车页。");
			}
			else {
				reports_HomePageTestRN.log(LogStatus.ERROR, "首页修改城市没有同步到买车页，请检查。");
				failAndMessage("首页修改城市没有同步到买车页，请检查。");
			}
//		} finally {
//			clickElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[2]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]");
//			wait(2);
//			clickElementByName("北京");
//		}
	}
	
	/**
	 * @Name 1701_search
	 * @Catalogue 首页-搜索-车辆历史记录
	 * @Grade 高级
	 * @FunctionPoint 首页-搜索功能-点击车辆历史记录中直接搜索
	 */
	@Test
	public void test_1701_search() {
		reports_HomePageTestRN.startTest("test_1700_search_1");
		gotocate(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击搜索框");
		WebElement shurukuang =findElementByXpath("//*/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]");
		shurukuang.click();
		wait(2);
    	reports_HomePageTestRN.log(LogStatus.PASS, "光标聚焦在搜索框");
    	if(CheckViewVisibilty(By.id("ivDelete"))){
    		WebElement history= findElementById("tvSearchHistory");
    		String historyName = history.getText();
    		history.click();
    		reports_HomePageTestRN.log(LogStatus.INFO, "点击搜索历史>"+historyName);
    		System.out.println("点击的搜索历史是："+historyName);
    		wait(2);
			if(CheckViewVisibilty(By.id("tv_no_seek_car_str"))) {   //没有找到您想要的
				reports_HomePageTestRN.log(LogStatus.INFO, "搜索历史没有数据。");
				failAndMessage("搜索历史没有数据。");
			}
			String str = findElementById("tv_pop_content").getText();
			System.out.println(str);
			if (historyName.contains(str)) {
				reports_HomePageTestRN.log(LogStatus.INFO, "点击搜索历史，列表中第一辆车与搜索值匹配。");
				System.out.println("点击搜索历史，列表中第一辆车与搜索值匹配。");
			}else {
				GetScreenshot("test_1700_search_1_car");
				reports_HomePageTestRN.log(LogStatus.ERROR,"列表中第一辆车与搜索值不匹配");
				failAndMessage("列表中第一辆车与搜索值不匹配");
			}    		
    	}else {
			System.out.println("没有搜索历史");
			reports_HomePageTestRN.log(LogStatus.INFO, "没有搜索历史，开始创建搜索历史");
			input("id", "etSearchText", "", "奥迪", "");
			driver.pressKeyCode(AndroidKeyCode.ENTER);
			wait(2);
			gotoCateSet(0);
//			clickElementById("tv_search");
			WebElement shurukuang1 =findElementByXpath("//android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.ImageView[1]");
			shurukuang1.click();
			wait(1);
	    	reports_HomePageTestRN.log(LogStatus.PASS, "光标聚焦在搜索框");
	    	if(CheckViewVisibilty(By.id("ivDelete"))){
	    		String historyName = findElementById("tvSearchHistory").getText();
	    		reports_HomePageTestRN.log(LogStatus.INFO, "点击搜索历史>"+historyName);
	    		clickElementById("tvSearchHistory");
	    		wait(2);
//	    		if (findElementById("tv_search").getText().equals(historyName)) {
	    			if(CheckViewVisibilty(By.id("tv_no_seek_car_str"))) {
						reports_HomePageTestRN.log(LogStatus.INFO, "搜索历史没有数据。");
						failAndMessage("搜索历史没有数据。");
					}
	    			String str = findElementById("tv_pop_content").getText();
	    			System.out.println(str);
	    			if (historyName.contains(str)) {
						reports_HomePageTestRN.log(LogStatus.INFO, "点击搜索历史，列表中第一辆车与搜索值匹配。");
						System.out.println("点击搜索历史，列表中第一辆车与搜索值匹配。");
					}else {
						GetScreenshot("test_1700_search_1_car");
						reports_HomePageTestRN.log(LogStatus.ERROR,"列表中第一辆车与搜索值不匹配");
						failAndMessage("列表中第一辆车与搜索值不匹配");
					}
		}else {
			GetScreenshot("test_1700_search_1_searchPage");
			failAndMessage("没有找到对应的搜索历史");
			reports_HomePageTestRN.log(LogStatus.ERROR,"没有找到对应的搜索历史");
		}
	}
	}
	

	/**
	 * @Name 1710_search_suggest
	 * @Catalogue 首页-搜索suggest
	 * @Grade 高级
	 * @FunctionPoint 首页-搜索-检查搜索suggest词最大个数
	 */
	@Test
	public void test_1710_search_suggest() {
		reports_HomePageTestRN.startTest("test_1710_search_suggest");
		gotocate(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击搜索栏");
		WebElement shurukuang =findElementByXpath("//*/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]");
		shurukuang.click();
		sleep(1000);
		reports_HomePageTestRN.log(LogStatus.INFO, "搜索栏输入“奥迪”。");
		input("id", "etSearchText", "", "奥迪", "");
		List<WebElement> list = findElementById("lvTips").findElements(By.id("linLayItemLayout"));
		if(list.size() > 10) {
			reports_HomePageTestRN.log(LogStatus.ERROR, "搜索“奥迪”的suggest词个数超过10，请检查。");
			GetScreenshot("test_1710_search_suggest");
			failAndMessage("搜索“奥迪”的suggest词个数超过10，请检查。");
		}else {
			reports_HomePageTestRN.log(LogStatus.INFO, "搜索“奥迪”的suggest词个数未超过10，显示正确。");
			System.out.println("搜索“奥迪”的suggest词个数未超过10，显示正确。");
		}
	}
	
//	/**
//	 * @Name 1707_search
//	 * @Catalogue 首页-搜索
//	 * @Grade 高级
//	 * @FunctionPoint 首页-搜索功能-店铺搜索"。。。"，回车之后，检查店铺默认页
//	 */
//	@Test
//	public void test_1708_DPMR() {
//		reports_HomePageTestRN.startTest("test_1708_DPMR");
//		gotocate(1);
//		reports_HomePageTestRN.log(LogStatus.INFO, "点击搜索栏");
//		clickElementById("tv_search");
//		wait(1);
//		reports_HomePageTestRN.log(LogStatus.INFO, "切换为店铺搜索。");
//		clickElementById("tvSearch");//点击车辆/店铺切换按钮
//		clickElementById("alertdialog_shop");
//		reports_HomePageTestRN.log(LogStatus.INFO, "输入“。。。”，然后回车。");
//		input("id", "etSearchText", "", "。。。", "");
//		driver.pressKeyCode(AndroidKeyCode.ENTER);
//		sleep(500);
//		checkTitlebar1("店铺详情");
//		if(CheckViewVisibilty(By.id("tvDirectStatusNull"))) {
//			reports_HomePageTestRN.log(LogStatus.INFO, "搜索“。。。”店铺，没有相关店铺，进入店铺默认图！");
//			System.out.println("搜索“。。。”店铺，没有相关店铺，进入店铺默认图！");
//		}else if(CheckViewVisibilty(By.id("rlGuanZhu"))){
//			reports_HomePageTestRN.log(LogStatus.INFO, "搜索“。。。”店铺，有相关店铺，进入店铺详情！");
//			System.out.println("搜索“。。。”店铺，有相关店铺，进入店铺详情！");
//		}else {
//			reports_HomePageTestRN.log(LogStatus.ERROR, "搜索“。。。”店铺，没有相关店铺，但也没有进入店铺默认图！请检查！");
//			failAndMessage("搜索“。。。”店铺，没有相关店铺，但也没有进入店铺默认图！请检查！");
//		}
//	}

	/**
	 * @Name 1702_search
	 * @Catalogue 首页-搜索
	 * @Grade 高级
	 * @FunctionPoint 首页-搜索功能-清空车辆搜索历史记录
	 */
	@Test
	public void test_1702_search(){
		reports_HomePageTestRN.startTest("test_1700_search_2");
		gotocate(1);
//		clickElementById("tv_search");
		WebElement shurukuang =findElementByXpath("//*/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]");
		shurukuang.click();
		wait(2);
    	reports_HomePageTestRN.log(LogStatus.PASS, "光标聚焦在搜索框");
    	if(CheckViewVisibilty(By.id("ivDelete"))){
    		clickElementById("ivDelete");
    		sleep(500);
    		clickElementById("bt_confirm_ok");
    		if (CheckViewVisibilty(By.id("flowLayoutHistory"))) {
				GetScreenshot("test_1700_search_2");
    			failAndMessage("删除历史记录失败");
    			reports_HomePageTestRN.log(LogStatus.ERROR, "删除历史记录失败");
			}
    	}else {
			System.out.println("没有搜索历史");
			reports_HomePageTestRN.log(LogStatus.INFO, "没有搜索历史，开始创建搜索历史");
			input("id", "etSearchText", "", "奥迪", "");
			driver.pressKeyCode(AndroidKeyCode.ENTER);
			wait(2);
			gotoCateSet(0);
			clickElementById("tv_search");
			wait(1);
	    	reports_HomePageTestRN.log(LogStatus.PASS, "光标聚焦在搜索框");
	    	if(CheckViewVisibilty(By.id("ivDelete"))){
	    		reports_HomePageTestRN.log(LogStatus.INFO, "删除搜索历史");
	    		clickElementById("ivDelete");
	    		clickElementById("bt_confirm_ok");
	    		if (CheckViewVisibilty(By.id("flowLayoutHistory"))) {
					GetScreenshot("test_1700_search_2");
	    			failAndMessage("删除历史记录失败");
	    			reports_HomePageTestRN.log(LogStatus.ERROR, "删除历史记录失败");
	    		}
	    	}
    	}
	}
	
	/**
	 * @Name 1703_search
	 * @Catalogue 首页-搜索
	 * @Grade 中级
	 * @FunctionPoint 首页-搜索功能-店铺搜索,搜索后进入店铺并关注店铺后进如我的取消关注
	 */
//	@Test
//	public void test_1703_search()  {
//		reports_HomePageTestRN.startTest("test_1700_search_3");
//		gotocate(1);
//		reports_HomePageTestRN.log(LogStatus.INFO, "点击搜索框");
//		clickElementById("tv_search");
//		wait(1);
//		clickElementById("tvSearch");//点击车辆/店铺切换按钮
//		clickElementById("alertdialog_shop");
//		if (waitForVisible(By.id("etSearchText"), 3).getText().equals("请输入店铺名称")) {
//			reports_HomePageTestRN.log(LogStatus.INFO, "搜索店铺<优车诚>");
//			inputById("etSearchText", "优车诚");
//			sleep(100);
//			clickElementById("tvText");
//			wait(1);
//			checkTitleBarShop();
//			clickElementById("ivGuanZhu");
//			reports_HomePageTestRN.log(LogStatus.INFO, "关注店铺");
//			toastCheck("关注店铺成功");
//			clickElementById("ivGuanZhu");
//			toastCheck("取消关注成功");
//			
//		}else {
//			failAndMessage("切换店铺搜索失败");
//			reports_HomePageTestRN.log(LogStatus.ERROR, "切换店铺搜索失败");
//		}
//	}
	
	
	/**
	 * @Name 1705_search
	 * @Catalogue 首页-搜索
	 * @Grade 中级
	 * @FunctionPoint 首页-搜索功能-在搜索结果页遍历热门搜索中的推荐
	 */
	@Test
	public void test_1705_search()  {
		reports_HomePageTestRN.startTest("test_1700_search_5");
		gotocate(1);
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击搜索框");
//		clickElementById("tv_search");
		WebElement shurukuang =findElementByXpath("//*/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]");
		shurukuang.click();
		wait(2);
		String[] hotSearch = {"付一成开豪车","大众","宝马","奥迪","奔驰","丰田","超值准新车", "第一辆二手车", "女司机首选","百公里6个油","运动年轻范","月供千元练手","撩妹神车","捡漏好车"};
//		WebElement flowLayoutHotTip = driver.findElementById("flowLayoutHotTip");
//		List<WebElement> item = flowLayoutHotTip.findElements(By.id("tvSearchHistory"));
//		int itemContent = item.size();
//		System.out.println("总数="+itemContent);
		for (int i = 0; i <hotSearch.length; i++) {
			reports_HomePageTestRN.log(LogStatus.INFO, "开始遍历热门搜索");
			findElementByName(hotSearch[i]).click();
			System.out.println(hotSearch[i]+"*********"+i);
			wait(2);
			if(CheckViewVisibilty(By.id("tv_pop_content"))) {
				String str = findElementById("tv_pop_content").getText();
				if(str.equals(hotSearch[i]) ||
						str.equals("3-30万元") ||
						str.equals("5-15万元") ||
						str.equals("3-10万元") ||
						str.equals("5-20万元") ||
						str.equals("10万以下")) {
					gotoCateSet(0);
					wait(1);
					shurukuang.click();
//					clickElementById("tv_search");
					wait(2);
				}
			}else if (findElementById("tv_search").getText().equals(hotSearch[i])) {
				System.out.println(findElementById("tv_search").getText());
				gotoCateSet(0);
				wait(1);
				clickElementById("tv_search");
				wait(2);
			}else {
				GetScreenshot("test_1700_search_5");
				failAndMessage("热门搜索第"+i+"个,点击后跳转错误,应为>"+hotSearch[i]+"<实际为>"+findElementById("tv_search").getText());
				reports_HomePageTestRN.log(LogStatus.ERROR, "热门搜索第"+i+"个,点击后跳转错误,应为>"+hotSearch[i]+"<实际为>"+findElementById("tv_search").getText());
			}
		}
	}
	
	/**
	 * @Name test_1200_XXZX
	 * @Catalogue 首页-消息中心
	 * @Grade 高级
	 * @FunctionPoint 首页-消息中心，点击消息中心。
	 **/
	@Test
	public void test_1200_XXZX() {
		reports_HomePageTestRN.startTest("test_1200_XXZX");
		gotocate(1);
		wait(1);
		clickElementByName("消息");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页消息中心");
		checkTitlebar1("消息中心");
		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页进入到消息中心");
		System.out.println("点击首页进入到消息中心");
	}


	/**
	 * @Name 1100_MC
	 * @Catalogue 首页-买车和查看全部车源按钮
	 * @Grade 高级
	 * @FunctionPoint 首页-买车，点击买车和查看全部车源按钮
	 **/
	@Test
	public void test_1100_MC() {
		reports_HomePageTestRN.startTest("test_1100_MC");
		gotocate(1);
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页的买车商城。");
//		clickButtonShouYe("买车商城");
		clickElementByName("买车商城");
//		clickElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[2]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v7.widget.RecyclerView[1]/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.ImageView[1]");
		CheckNewListFromHomePage();
		reports_HomePageTestRN.log(LogStatus.INFO, "首页，点击买车商城，进入买车列表新页面。");
    	System.out.println("首页，点击买车商城，进入买车列表新页面。");
    	toDetailFromNewList();
    	checkTitlebar_Detail();
    	backBTN();
		backBTN();
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页的查看全部车源按钮。");
		clickElementByXpath("//android.widget.TextView[contains(@text,'共有')]");//查看全部XXXX车源
		wait(1);
		CheckNewListFromHomePage();
		reports_HomePageTestRN.log(LogStatus.INFO, "首页，点击查看全部车源按钮，进入买车列表新页面。");
    	System.out.println("首页，点击查看全部车源按钮，进入买车列表新页面。");
    	toDetailFromNewList();
    	checkTitlebar_Detail();
    	backBTN();
	}
	
	/**
	 * @Name 1210_WYMCDTCJ
	 * @Catalogue 首页-卖车
	 * @Grade 中级
	 * @FunctionPoint 首页-点击卖车通道，验证页面跳转
	 **/
	@Test
	public void test_1210_MC_ZM() {
		reports_HomePageTestRN.startTest("test_1210_MC_ZM");
		gotocate(1);
		wait(3);
//		clickButtonShouYe("卖车通道");
		clickElementByName("卖车通道");
		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页-卖车通道");
		wait(5);
		if(findElementByName("卖车").getAttribute("checked").equals("true")){
        	reports_HomePageTestRN.log(LogStatus.INFO, "首页，点击卖车通道，进入卖车tab页。");
        	System.out.println("首页，点击首页-卖车通道，进入卖车tab页。");
        }else {
        	reports_HomePageTestRN.log(LogStatus.ERROR, "首页，点击卖车通道，没有进入卖车tab页。");
       	 	failAndMessage("首页，点击首页-卖车通道，没有进入卖车tab页。");
		}
	}
	
	/**
	 * @Name 1200_CLGJ
	 * @Catalogue 首页-估价
	 * @Grade 高级
	 * @FunctionPoint 首页-免费估价，遍历页面
	 **/
	@Test
	public void test_1200_CLGJ() {
		 reports_HomePageTestRN.startTest("test_1200_CLGJ"); 
		 gotocate(1);
		 sleep(200);
//		 clickButtonShouYe("免费估价");
		 clickElementByName("免费估价");
		 reports_HomePageTestRN.log(LogStatus.INFO, "点击免费估价");
		 wait(1);
		 checkTitlebar1("车辆估价");
		 String[] str = {"卖车城市","","车辆牌照","品牌车系","","上牌时间","","行驶里程","车况自评"};
		 for(int i=1; i<str.length;i++){
				wait(3);
				if(CheckViewVisibilty(By.xpath("//android.widget.FrameLayout[1]/android.widget.LinearLayout[1]"
						+ "/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
						+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.ScrollView[1]"
						+ "/android.widget.LinearLayout[1]/android.widget.LinearLayout["+i+"]/android.widget.TextView[1]"))) {
					System.out.println(findElementByXpath("//android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout["+i+"]/android.widget.TextView[1]"));
					if (findElementByXpath("//android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout["+i+"]/android.widget.TextView[1]").getText().equals(str[i-1])) {
						
					}else {
						failAndMessage(str[i-1]+"没有正常显示");
						reports_HomePageTestRN.log(LogStatus.ERROR,str[i-1]+"没有正常显示");
						break;
					}
				}						
		 }
		 if(findElementById("tv_select_brand").getText().equals("品牌车系")) {
			 
		 }else {
			failAndMessage("品牌车系没有正常显示");
			reports_HomePageTestRN.log(LogStatus.ERROR, "品牌车系没有正常显示");
		}
		 if(findElementById("tv_car_self_evaluation_title").getText().equals("车况自评")) {
			 
		 }else {
			failAndMessage("车况自评没有正常显示");
			reports_HomePageTestRN.log(LogStatus.ERROR, "车况自评没有正常显示");
		}
	}
	
	/**
	 * @Name 1400_GJ
	 * @Catalogue 首页-免费估价
	 * @Grade 高级
	 * @FunctionPoint 首页-免费估价，点击免费估价查看跳转
	 **/
	@Test
	public void test_1400_GJ() {
		reports_HomePageTestRN.startTest("test_1400_GJ");
		gotocate(1);
		clickElementByName("免费估价");
		sleep(200);
		checkTitlebar1("车辆估价");
		if(CheckViewVisibilty(By.id("ll_sellcity"))&&
//				CheckViewVisibilty(By.id("ll_car_license_plate"))&&
				CheckViewVisibilty(By.id("rl_car_self_evaluation"))&&
				CheckViewVisibilty(By.id("rl_select_brand"))&&
				CheckViewVisibilty(By.id("ll_vehicle_condition"))&&
				CheckViewVisibilty(By.id("ll_plateTime"))) {
			reports_HomePageTestRN.log(LogStatus.INFO, "车辆估价页面显示正常。");
			System.out.println("车辆估价页面显示正常。");		
			reports_HomePageTestRN.log(LogStatus.INFO, "车辆估价页面-选择要卖的车。");
			clickElementById("car_name");
			wait(2);
			clickElementByName("奥迪");
			wait(2);
			clickElementByName("A4L");
			clickElementByName("2019款 1.4T 自动 35TFSI进取型 国V");
			sleep(500);
			reports_HomePageTestRN.log(LogStatus.INFO, "车辆估价页面-输入里程。");
			findElementById("et_vehicle_condition").sendKeys("25");
			reports_HomePageTestRN.log(LogStatus.INFO, "车辆估价页面-选择上牌时间。");
			clickElementByName("请选择车况");
			reports_BuyCarTest.log(LogStatus.INFO, "点击请选择车况");
			sleep(500);
			clickElementByName("从未有碰撞");
			clickElementByName("从未有损伤");
			clickElementByName("确定");
			sleep(500);
			reports_HomePageTestRN.log(LogStatus.INFO, "车辆估价页面-检查开始估价按钮是否变为可用。");
			if(findElementById("btn_commit").getAttribute("enabled").equals("true")) {
				reports_HomePageTestRN.log(LogStatus.INFO, "车辆估价页面-输入所有信息之后，开始估价按钮变为可用。");
				System.out.println("车辆估价页面-输入所有信息之后，开始估价按钮变为可用。");
			}
			else {
				reports_HomePageTestRN.log(LogStatus.ERROR, "车辆估价页面-输入所有信息之后，开始估价按钮仍然不可用，请检查。");
				GetScreenshot("test_1400_GJ_button");
				failAndMessage("车辆估价页面-输入所有信息之后，开始估价按钮仍然不可用，请检查。");
			}
		}else {
			reports_HomePageTestRN.log(LogStatus.ERROR, "车辆估价页面显示异常。");
			GetScreenshot("test_1400_GJ");
			failAndMessage("车辆估价页面显示异常。");	
		}
	}
	
	/**
	 * @Name 1300_ZNXC
	 * @Catalogue 首页-智能选车
	 * @Grade 高级
	 * @FunctionPoint 首页-智能选车，
	 **/
	@Test
	public void test_1300_ZNXC() {
		reports_HomePageTestRN.startTest("test_1300_ZNXC");
		gotocate(1);
		wait(2);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击购车顾问");
		clickElementByName("购车顾问");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击智能选车");
//		clickElementById("gujiall");//智能选车
//		clickElementByName("我是您的专属购车顾问，可以帮您更快更准的筛选超值好车，您也可以体验人工智能选车，点击体验");
		clickElementByName("优信为您提供购车顾问服务，帮您全国范围挑好车。您还可以选择智能选车（点击体验）");
		wait(1);
		checkTitlebar1("智能选车（1/3）");
		if(findElementById("askquestionstr").getText().equals("您更喜欢什么样类型的车？")) {
			clickElementById("confirm");  //再想想
			checkTitlebar1("智能选车（2/3）");
			if(findElementById("askquestionstr").getText().equals("选择您最关注的内容")) {
				int width = driver.manage().window().getSize().width;
				int hight = driver.manage().window().getSize().height;
				driver.tap(1, width/2, hight*9/20, 1);//空间
				if(findElementById("confirm").getText().equals("至少选择2项（1/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了一个最关注的内容");
					System.out.println("选了一个最关注的内容");
				}
				driver.tap(1, width/2, hight*5/8, 1);//配置
				if(findElementById("confirm").getText().equals("选好了（2/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了第2关注的内容");
					System.out.println("选了第2关注的内容");
				}
				clickElementById("confirm");
				wait(1);
				checkTitlebar1("智能选车（3/3）");
				if(findElementById("confirm").equals("条件太苛刻，请重新选择")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "条件太苛刻，没有合适车源。");
					System.out.println("条件太苛刻，没有合适车源。");
				}else {
					clickElementById("confirm");
					if(CheckViewVisibilty(By.id("carname"))) {
						reports_HomePageTestRN.log(LogStatus.INFO, "智能选车有合适车源。");
						System.out.println("智能选车有合适车源。");
					}
				}		
			}
		}	
	}
	
	/**
	 * @Name 1300_ZNXC_MODIFY
	 * @Catalogue 首页-智能选车
	 * @Grade 高级
	 * @FunctionPoint 首页-智能选车，智能选车，修改车型
	 **/
	@Test
	public void test_1300_ZNXC_MODIFY() {
		reports_HomePageTestRN.startTest("test_1300_ZNXC_MODIFY");
		gotocate(1);
		wait(2);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击购车顾问");
		clickElementByName("购车顾问");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击智能选车");
//		clickElementById("gujiall");//智能选车
//		clickElementByName("我是您的专属购车顾问，可以帮您更快更准的筛选超值好车，您也可以体验人工智能选车，点击体验");
		clickElementByName("优信为您提供购车顾问服务，帮您全国范围挑好车。您还可以选择智能选车（点击体验）");
		wait(1);
		checkTitlebar1("智能选车（1/3）");
		if(findElementById("askquestionstr").getText().equals("您更喜欢什么样类型的车？")) {
			clickElementById("confirm");  //再想想
			checkTitlebar1("智能选车（2/3）");
			if(findElementById("askquestionstr").getText().equals("选择您最关注的内容")) {
				int width = driver.manage().window().getSize().width;
				int hight = driver.manage().window().getSize().height;
				driver.tap(1, width/2, hight*9/20, 1);//空间
				if(findElementById("confirm").getText().equals("至少选择2项（1/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了一个最关注的内容");
					System.out.println("选了一个最关注的内容");
				}
				driver.tap(1, width/2, hight*5/8, 1);//配置
				if(findElementById("confirm").getText().equals("选好了（2/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了第2关注的内容");
					System.out.println("选了第2关注的内容");
				}
				clickElementById("confirm");
				wait(1);
				checkTitlebar1("智能选车（3/3）");	
				clickElementById("confirm");
				wait(1);
				clickElementById("tv_car_types_modify");//修改车型
				wait(1);
				driver.tap(1, width/2, hight*7/10, 1);
				wait(1);
				clickElementById("confirm");
				wait(1);
				if(findElementById("tv_car_types").getText().equals("SUV")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "智能选车，车型修改成功。");
					System.out.println("智能选车，车型修改成功。");
				}else {
					reports_HomePageTestRN.log(LogStatus.ERROR, "智能选车，车型修改失败。");
					failAndMessage("智能选车，车型修改失败。");
				}
			}
		}	
	}
	
	/**
	 * @Name 1300_ZNXC_RESULT_LIST
	 * @Catalogue 首页-智能选车
	 * @Grade 高级
	 * @FunctionPoint 首页-智能选车，检查车源结果列表
	 **/
	@Test
	public void test_1300_ZNXC_RESULT_LIST() {
		reports_HomePageTestRN.startTest("test_1300_ZNXC_RESULT_LIST");
		gotocate(1);
		wait(2);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击购车顾问");
		clickElementByName("购车顾问");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击智能选车");
//		clickElementById("gujiall");//智能选车
//		clickElementByName("我是您的专属购车顾问，可以帮您更快更准的筛选超值好车，您也可以体验人工智能选车，点击体验");
		clickElementByName("优信为您提供购车顾问服务，帮您全国范围挑好车。您还可以选择智能选车（点击体验）");
		wait(1);
		checkTitlebar1("智能选车（1/3）");
		if(findElementById("askquestionstr").getText().equals("您更喜欢什么样类型的车？")) {
			clickElementById("confirm");  //再想想
			checkTitlebar1("智能选车（2/3）");
			if(findElementById("askquestionstr").getText().equals("选择您最关注的内容")) {
				int width = driver.manage().window().getSize().width;
				int hight = driver.manage().window().getSize().height;
				driver.tap(1, width/2, hight*9/20, 1);//空间
				if(findElementById("confirm").getText().equals("至少选择2项（1/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了一个最关注的内容");
					System.out.println("选了一个最关注的内容");
				}
				driver.tap(1, width/2, hight*5/8, 1);//配置
				if(findElementById("confirm").getText().equals("选好了（2/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了第2关注的内容");
					System.out.println("选了第2关注的内容");
				}
				clickElementById("confirm");
				wait(1);
				checkTitlebar1("智能选车（3/3）");
				if(findElementById("confirm").equals("条件太苛刻，请重新选择")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "条件太苛刻，没有合适车源。");
					System.out.println("条件太苛刻，没有合适车源。");
				}else {
					clickElementById("confirm");
					if(CheckViewVisibilty(By.id("carname"))) {
						reports_HomePageTestRN.log(LogStatus.INFO, "智能选车有合适车源。");
						System.out.println("智能选车有合适车源.");
						if(CheckViewVisibilty(By.id("price"))&&//车价
								CheckViewVisibilty(By.id("findcount"))&&//找到XX款
								CheckViewVisibilty(By.id("score"))&&//推荐值分数
								CheckViewVisibilty(By.id("tuijiantxt"))) {//推荐值
							reports_HomePageTestRN.log(LogStatus.INFO, "智能选车的结果车系列表显示正确。");
							System.out.println("智能选车的结果车系列表显示正确。");	
						}else {
							reports_HomePageTestRN.log(LogStatus.ERROR, "智能选车的结果车系列表显示异常，请查看。");
							failAndMessage("智能选车的结果车系列表显示异常，请查看。");
						}
						reports_HomePageTestRN.log(LogStatus.INFO, "进入某个车系的车源详情列表。");
						clickElementById("carname");
						wait(2);
						if(CheckViewVisibilty(By.id("tvCarWholeName"))&&//标题
//								CheckViewVisibilty(By.id("tvCityName"))&&//城市
								CheckViewVisibilty(By.id("tvAge"))&&//车龄
								CheckViewVisibilty(By.id("tvMileage"))&&//里程
								CheckViewVisibilty(By.id("tvPrice"))&&//总价
								CheckViewVisibilty(By.id("tvHalfPrice"))&&//首付
								CheckViewVisibilty(By.id("tvmonthlyprice"))) {//月供
							reports_HomePageTestRN.log(LogStatus.INFO, "智能选车结果车源列表，车辆信息显示正常。");
							System.out.println("智能选车结果车源列表，车辆信息显示正常。");
						}else {
							reports_HomePageTestRN.log(LogStatus.ERROR, "智能选车结果车源列表，车辆信息显示异常。");
							failAndMessage("智能选车结果车源列表，车辆信息显示异常。");
						}	
					}
				}		
			}
		}	
	}
	
	/**
	 * @Name 1300_ZNXC_RESULT_SORT
	 * @Catalogue 首页-智能选车
	 * @Grade 高级
	 * @FunctionPoint 首页-智能选车，检查车源结果列表的排序是否已推荐分值大小
	 **/
	@Test
	public void test_1300_ZNXC_RESULT_SORT() {
		reports_HomePageTestRN.startTest("test_1300_ZNXC_RESULT_SORT");
		gotocate(1);
		wait(2);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击购车顾问");
		clickElementByName("购车顾问");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击智能选车");
//		clickElementById("gujiall");//智能选车
//		clickElementByName("我是您的专属购车顾问，可以帮您更快更准的筛选超值好车，您也可以体验人工智能选车，点击体验");
		clickElementByName("优信为您提供购车顾问服务，帮您全国范围挑好车。您还可以选择智能选车（点击体验）");
		wait(1);
		checkTitlebar1("智能选车（1/3）");
		if(findElementById("askquestionstr").getText().equals("您更喜欢什么样类型的车？")) {
			clickElementById("confirm");  //再想想
			checkTitlebar1("智能选车（2/3）");
			if(findElementById("askquestionstr").getText().equals("选择您最关注的内容")) {
				int width = driver.manage().window().getSize().width;
				int hight = driver.manage().window().getSize().height;
				driver.tap(1, width/2, hight*9/20, 1);//空间
				if(findElementById("confirm").getText().equals("至少选择2项（1/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了一个最关注的内容");
					System.out.println("选了一个最关注的内容");
				}
				driver.tap(1, width/2, hight*5/8, 1);//配置
				if(findElementById("confirm").getText().equals("选好了（2/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了第2关注的内容");
					System.out.println("选了第2关注的内容");
				}
				clickElementById("confirm");
				wait(1);
				checkTitlebar1("智能选车（3/3）");
				if(findElementById("confirm").equals("条件太苛刻，请重新选择")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "条件太苛刻，没有合适车源。");
					System.out.println("条件太苛刻，没有合适车源。");
				}else {
					clickElementById("confirm");
					String score1 = findElementByXpath("//android.widget.FrameLayout[1]/android.widget.LinearLayout[1]"
							+ "/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
							+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[2]/android.support.v7.widget.RecyclerView[1]"
							+ "/android.widget.RelativeLayout[2]/android.widget.TextView[3]").getText();
					System.out.println(score1);
					String score2 = findElementByXpath("//android.widget.FrameLayout[1]/android.widget.LinearLayout[1]"
							+ "/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
							+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[2]/android.support.v7.widget.RecyclerView[1]"
							+ "/android.widget.RelativeLayout[3]/android.widget.TextView[2]").getText();
					System.out.println(score2);	
					Double s1 = Double.parseDouble(score1);
					Double s2 = Double.parseDouble(score2);
					if(s1 >= s2) {
						reports_HomePageTestRN.log(LogStatus.INFO, "智能选车结果列表推荐分值由高到低排序正确。");
						System.out.println("智能选车结果列表推荐分值由高到低排序正确。");
					}else {
						reports_HomePageTestRN.log(LogStatus.ERROR, "智能选车结果列表推荐分值不是由高到低，排序不正确。");
						failAndMessage("智能选车结果列表推荐分值不是由高到低，排序不正确。");
					}
				}
			}
		}
	}
	
	/**
	 * @Name 1300_ZNXC_BRAND_TAG
	 * @Catalogue 首页-智能选车
	 * @Grade 高级
	 * @FunctionPoint 首页-智能选车，检查最关注品牌时结果对应的最匹配文案。
	 **/
	@Test
	public void test_1300_ZNXC_BRAND_TAG() {
		reports_HomePageTestRN.startTest("test_1300_ZNXC_BRAND_TAG");
		gotocate(1);
		wait(2);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击购车顾问");
		clickElementByName("购车顾问");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击智能选车");
//		clickElementById("gujiall");//智能选车
//		clickElementByName("我是您的专属购车顾问，可以帮您更快更准的筛选超值好车，您也可以体验人工智能选车，点击体验");
		clickElementByName("优信为您提供购车顾问服务，帮您全国范围挑好车。您还可以选择智能选车（点击体验）");
		wait(1);
		checkTitlebar1("智能选车（1/3）");
		if(findElementById("askquestionstr").getText().equals("您更喜欢什么样类型的车？")) {
			clickElementById("confirm");  //再想想
			checkTitlebar1("智能选车（2/3）");
			if(findElementById("askquestionstr").getText().equals("选择您最关注的内容")) {
				int width = driver.manage().window().getSize().width;
				int hight = driver.manage().window().getSize().height;
				reports_HomePageTestRN.log(LogStatus.INFO, "最关注的内容选择品牌");
				driver.tap(1, width/6, hight*9/20, 1);//品牌
				if(findElementById("confirm").getText().equals("至少选择2项（1/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了一个最关注的内容");
					System.out.println("选了一个最关注的内容");
				}
				driver.tap(1, width/2, hight*5/8, 1);//配置
				if(findElementById("confirm").getText().equals("选好了（2/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了第2关注的内容");
					System.out.println("选了第2关注的内容");
				}
				clickElementById("confirm");
				wait(1);
				checkTitlebar1("智能选车（3/3）");
				if(findElementById("confirm").equals("条件太苛刻，请重新选择")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "条件太苛刻，没有合适车源。");
					System.out.println("条件太苛刻，没有合适车源。");
				}else {
					clickElementById("confirm");
					if(findElementById("tag").getText().equals("品牌最响亮")) {
						reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择品牌，对应匹配的文案正确。");
						System.out.println("最关注的选择品牌，对应匹配的文案正确。");
					}else {
						reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择品牌，对应匹配的文案不正确，请检查。");
						failAndMessage("最关注的选择品牌，对应匹配的文案不正确，请检查。");
					}
				}
			}
		}
	}
	
	/**
	 * @Name test_1300_ZNXC_CROSS_COUNTRY_TAG
	 * @Catalogue 首页-智能选车
	 * @Grade 高级
	 * @FunctionPoint 首页-智能选车，检查最关注越野时结果对应的最匹配文案。
	 **/
	@Test
	public void test_1300_ZNXC_CROSS_COUNTRY_TAG() {
		reports_HomePageTestRN.startTest("test_1300_ZNXC_COUNTRY_TAG");
		gotocate(1);
		wait(2);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击购车顾问");
		clickElementByName("购车顾问");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击智能选车");
//		clickElementById("gujiall");//智能选车
//		clickElementByName("我是您的专属购车顾问，可以帮您更快更准的筛选超值好车，您也可以体验人工智能选车，点击体验");
		clickElementByName("优信为您提供购车顾问服务，帮您全国范围挑好车。您还可以选择智能选车（点击体验）");
		wait(1);
		checkTitlebar1("智能选车（1/3）");
		if(findElementById("askquestionstr").getText().equals("您更喜欢什么样类型的车？")) {
			clickElementById("confirm");  //再想想
			checkTitlebar1("智能选车（2/3）");
			if(findElementById("askquestionstr").getText().equals("选择您最关注的内容")) {
				int width = driver.manage().window().getSize().width;
				int hight = driver.manage().window().getSize().height;
				reports_HomePageTestRN.log(LogStatus.INFO, "最关注的内容选择越野");
				driver.tap(1, width/6, hight*5/8, 1);//越野
				if(findElementById("confirm").getText().equals("至少选择2项（1/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了一个最关注的内容");
					System.out.println("选了一个最关注的内容");
				}
				driver.tap(1, width/2, hight*5/8, 1);//配置
				if(findElementById("confirm").getText().equals("选好了（2/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了第2关注的内容");
					System.out.println("选了第2关注的内容");
				}
				clickElementById("confirm");
				wait(1);
				checkTitlebar1("智能选车（3/3）");
				if(findElementById("confirm").equals("条件太苛刻，请重新选择")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "条件太苛刻，没有合适车源。");
					System.out.println("条件太苛刻，没有合适车源。");
				}else {
					clickElementById("confirm");
					if(findElementById("tag").getText().equals("越野最出色")) {
						reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择越野，对应匹配的文案正确。");
						System.out.println("最关注的选择越野，对应匹配的文案正确。");
					}else {
						reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择越野，对应匹配的文案不正确，请检查。");
						failAndMessage("最关注的选择越野，对应匹配的文案不正确，请检查。");
					}
				}
			}
		}
	}
	
	/**
	 * @Name 1300_ZNXC_SAFE_TAG
	 * @Catalogue 首页-智能选车
	 * @Grade 高级
	 * @FunctionPoint 首页-智能选车，检查最关注安全时结果对应的最匹配文案。
	 **/
	@Test
	public void test_1300_ZNXC_SAFE_TAG() {
		reports_HomePageTestRN.startTest("test_1300_ZNXC_SAFE_TAG");
		gotocate(1);
		wait(2);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击购车顾问");
		clickElementByName("购车顾问");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击智能选车");
//		clickElementById("gujiall");//智能选车
//		clickElementByName("我是您的专属购车顾问，可以帮您更快更准的筛选超值好车，您也可以体验人工智能选车，点击体验");
		clickElementByName("优信为您提供购车顾问服务，帮您全国范围挑好车。您还可以选择智能选车（点击体验）");
		wait(1);
		checkTitlebar1("智能选车（1/3）");
		if(findElementById("askquestionstr").getText().equals("您更喜欢什么样类型的车？")) {
			clickElementById("confirm");  //再想想
			checkTitlebar1("智能选车（2/3）");
			if(findElementById("askquestionstr").getText().equals("选择您最关注的内容")) {
				int width = driver.manage().window().getSize().width;
				int hight = driver.manage().window().getSize().height;
				reports_HomePageTestRN.log(LogStatus.INFO, "最关注的内容选择安全");
				driver.tap(1, width/6, hight*13/16, 1);//安全
				if(findElementById("confirm").getText().equals("至少选择2项（1/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了一个最关注的内容");
					System.out.println("选了一个最关注的内容");
				}
				driver.tap(1, width/2, hight*5/8, 1);//配置
				if(findElementById("confirm").getText().equals("选好了（2/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了第2关注的内容");
					System.out.println("选了第2关注的内容");
				}
				clickElementById("confirm");
				wait(1);
				checkTitlebar1("智能选车（3/3）");
				if(findElementById("confirm").equals("条件太苛刻，请重新选择")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "条件太苛刻，没有合适车源。");
					System.out.println("条件太苛刻，没有合适车源。");
				}else {
					clickElementById("confirm");
					if(findElementById("tag").getText().equals("五星安全")) {
						reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择安全，对应匹配的文案正确。");
						System.out.println("最关注的选择安全，对应匹配的文案正确。");
					}else {
						reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择安全，对应匹配的文案不正确，请检查。");
						failAndMessage("最关注的选择安全，对应匹配的文案不正确，请检查。");
					}
				}
			}
		}
	}
	
	/**
	 * @Name 1300_ZNXC_SPACE_TAG
	 * @Catalogue 首页-智能选车
	 * @Grade 高级
	 * @FunctionPoint 首页-智能选车，检查最关注空间时结果对应的最匹配文案。
	 **/
	@Test
	public void test_1300_ZNXC_SPACE_TAG() {
		reports_HomePageTestRN.startTest("test_1300_ZNXC_SPACE_TAG");
		gotocate(1);
		wait(2);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击购车顾问");
		clickElementByName("购车顾问");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击智能选车");
//		clickElementById("gujiall");//智能选车
//		clickElementByName("我是您的专属购车顾问，可以帮您更快更准的筛选超值好车，您也可以体验人工智能选车，点击体验");
		clickElementByName("优信为您提供购车顾问服务，帮您全国范围挑好车。您还可以选择智能选车（点击体验）");
		wait(1);
		checkTitlebar1("智能选车（1/3）");
		if(findElementById("askquestionstr").getText().equals("您更喜欢什么样类型的车？")) {
			clickElementById("confirm");  //再想想
			checkTitlebar1("智能选车（2/3）");
			if(findElementById("askquestionstr").getText().equals("选择您最关注的内容")) {
				int width = driver.manage().window().getSize().width;
				int hight = driver.manage().window().getSize().height;
				reports_HomePageTestRN.log(LogStatus.INFO, "最关注的内容选择空间");
				driver.tap(1, width/2, hight*9/20, 1);//空间
				if(findElementById("confirm").getText().equals("至少选择2项（1/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了一个最关注的内容");
					System.out.println("选了一个最关注的内容");
				}
				driver.tap(1, width/2, hight*5/8, 1);//配置
				if(findElementById("confirm").getText().equals("选好了（2/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了第2关注的内容");
					System.out.println("选了第2关注的内容");
				}
				clickElementById("confirm");
				wait(1);
				checkTitlebar1("智能选车（3/3）");
				if(findElementById("confirm").equals("条件太苛刻，请重新选择")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "条件太苛刻，没有合适车源。");
					System.out.println("条件太苛刻，没有合适车源。");
				}else {
					clickElementById("confirm");
					if(findElementById("tag").getText().equals("空间最理想")) {
						reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择空间，对应匹配的文案正确。");
						System.out.println("最关注的选择空间，对应匹配的文案正确。");
					}else {
						reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择空间，对应匹配的文案不正确，请检查。");
						failAndMessage("最关注的选择空间，对应匹配的文案不正确，请检查。");
					}
				}
			}
		}
	}
	
	/**
	 * @Name 1300_ZNXC_CONFIG_TAG
	 * @Catalogue 首页-智能选车
	 * @Grade 高级
	 * @FunctionPoint 首页-智能选车，检查最关注配置时结果对应的最匹配文案。
	 **/
	@Test
	public void test_1300_ZNXC_CONFIG_TAG() {
		reports_HomePageTestRN.startTest("test_1300_ZNXC_CONFIG_TAG");
		gotocate(1);
		wait(2);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击购车顾问");
		clickElementByName("购车顾问");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击智能选车");
//		clickElementById("gujiall");//智能选车
//		clickElementByName("我是您的专属购车顾问，可以帮您更快更准的筛选超值好车，您也可以体验人工智能选车，点击体验");
		clickElementByName("优信为您提供购车顾问服务，帮您全国范围挑好车。您还可以选择智能选车（点击体验）");
		wait(1);
		checkTitlebar1("智能选车（1/3）");
		if(findElementById("askquestionstr").getText().equals("您更喜欢什么样类型的车？")) {
			clickElementById("confirm");  //再想想
			checkTitlebar1("智能选车（2/3）");
			if(findElementById("askquestionstr").getText().equals("选择您最关注的内容")) {
				int width = driver.manage().window().getSize().width;
				int hight = driver.manage().window().getSize().height;
				reports_HomePageTestRN.log(LogStatus.INFO, "最关注的内容选择配置");
				driver.tap(1, width/2, hight*5/8, 1);//配置
				if(findElementById("confirm").getText().equals("至少选择2项（1/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了一个最关注的内容");
					System.out.println("选了一个最关注的内容");
				}
				driver.tap(1, width/2, hight*9/20, 1);//空间
				if(findElementById("confirm").getText().equals("选好了（2/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了第2关注的内容");
					System.out.println("选了第2关注的内容");
				}
				clickElementById("confirm");
				wait(1);
				checkTitlebar1("智能选车（3/3）");
				if(findElementById("confirm").equals("条件太苛刻，请重新选择")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "条件太苛刻，没有合适车源。");
					System.out.println("条件太苛刻，没有合适车源。");
				}else {
					clickElementById("confirm");
					wait(1);
					List<WebElement> tagList = driver.findElementsById("tag");
					for(int i=0; i<tagList.size(); i++) {
						if(tagList.get(i).getText().equals("配置最贴心")) {
							reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择配置，智能选车结果页有对应匹配的文案。");
							System.out.println("最关注的选择配置，智能选车结果页有对应匹配的文案。");
							break;
						}else if(i == tagList.size()){
							reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择配置，智能选车结果页没有对应匹配的文案，请检查。");
							failAndMessage("最关注的选择配置，智能选车结果页没有对应匹配的文案，请检查。");
						}
					}
					
				}
			}
		}
	}
	
	/**
	 * @Name 1300_ZNXC_ZNXC_JOY_TAG
	 * @Catalogue 首页-智能选车
	 * @Grade 高级
	 * @FunctionPoint 首页-智能选车，检查最关注娱乐时结果对应的最匹配文案。
	 **/
	@Test
	public void test_1300_ZNXC_JOY_TAG() {
		reports_HomePageTestRN.startTest("test_1300_ZNXC_JOY_TAG");
		gotocate(1);
		wait(2);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击购车顾问");
		clickElementByName("购车顾问");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击智能选车");
//		clickElementById("gujiall");//智能选车
//		clickElementByName("我是您的专属购车顾问，可以帮您更快更准的筛选超值好车，您也可以体验人工智能选车，点击体验");
		clickElementByName("优信为您提供购车顾问服务，帮您全国范围挑好车。您还可以选择智能选车（点击体验）");
		wait(1);
		checkTitlebar1("智能选车（1/3）");
		if(findElementById("askquestionstr").getText().equals("您更喜欢什么样类型的车？")) {
			clickElementById("confirm");  //再想想
			checkTitlebar1("智能选车（2/3）");
			if(findElementById("askquestionstr").getText().equals("选择您最关注的内容")) {
				int width = driver.manage().window().getSize().width;
				int hight = driver.manage().window().getSize().height;
				reports_HomePageTestRN.log(LogStatus.INFO, "最关注的内容选择娱乐");
				driver.tap(1, width/2, hight*13/16, 1);//娱乐
				if(findElementById("confirm").getText().equals("至少选择2项（1/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了一个最关注的内容");
					System.out.println("选了一个最关注的内容");
				}
				driver.tap(1, width/2, hight*5/8, 1);//配置
				if(findElementById("confirm").getText().equals("选好了（2/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了第2关注的内容");
					System.out.println("选了第2关注的内容");
				}
				clickElementById("confirm");
				wait(1);
				checkTitlebar1("智能选车（3/3）");
				if(findElementById("confirm").equals("条件太苛刻，请重新选择")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "条件太苛刻，没有合适车源。");
					System.out.println("条件太苛刻，没有合适车源。");
				}else {
					clickElementById("confirm");
					if(findElementById("tag").getText().equals("体验最丰富")) {
						reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择娱乐，对应匹配的文案正确。");
						System.out.println("最关注的选择娱乐，对应匹配的文案正确。");
					}else {
						reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择娱乐，对应匹配的文案不正确，请检查。");
						failAndMessage("最关注的选择娱乐，对应匹配的文案不正确，请检查。");
					}
				}
			}
		}
	}
	
	/**
	 * @Name 1300_ZNXC_ZNXC_POWER_TAG
	 * @Catalogue 首页-智能选车
	 * @Grade 高级
	 * @FunctionPoint 首页-智能选车，检查最关注动力时结果对应的最匹配文案。
	 **/
	@Test
	public void test_1300_ZNXC_POWER_TAG() {
		reports_HomePageTestRN.startTest("test_1300_ZNXC_POWER_TAG");
		gotocate(1);
		wait(2);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击购车顾问");
		clickElementByName("购车顾问");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击智能选车");
//		clickElementById("gujiall");//智能选车
//		clickElementByName("我是您的专属购车顾问，可以帮您更快更准的筛选超值好车，您也可以体验人工智能选车，点击体验");
		clickElementByName("优信为您提供购车顾问服务，帮您全国范围挑好车。您还可以选择智能选车（点击体验）");
		wait(1);
		checkTitlebar1("智能选车（1/3）");
		if(findElementById("askquestionstr").getText().equals("您更喜欢什么样类型的车？")) {
			clickElementById("confirm");  //再想想
			checkTitlebar1("智能选车（2/3）");
			if(findElementById("askquestionstr").getText().equals("选择您最关注的内容")) {
				int width = driver.manage().window().getSize().width;
				int hight = driver.manage().window().getSize().height;
				reports_HomePageTestRN.log(LogStatus.INFO, "最关注的内容选择动力");
				driver.tap(1, width*5/6, hight*9/20, 1);//动力
				if(findElementById("confirm").getText().equals("至少选择2项（1/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了一个最关注的内容");
					System.out.println("选了一个最关注的内容");
				}
				driver.tap(1, width/2, hight*5/8, 1);//配置
				if(findElementById("confirm").getText().equals("选好了（2/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了第2关注的内容");
					System.out.println("选了第2关注的内容");
				}
				clickElementById("confirm");
				wait(1);
				checkTitlebar1("智能选车（3/3）");
				if(findElementById("confirm").equals("条件太苛刻，请重新选择")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "条件太苛刻，没有合适车源。");
					System.out.println("条件太苛刻，没有合适车源。");
				}else {
					clickElementById("confirm");
					if(findElementById("tag").getText().equals("动力最强劲")) {
						reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择动力，对应匹配的文案正确。");
						System.out.println("最关注的选择动力，对应匹配的文案正确。");
					}else {
						reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择动力，对应匹配的文案不正确，请检查。");
						failAndMessage("最关注的选择动力，对应匹配的文案不正确，请检查。");
					}
				}
			}
		}
	}
	
	/**
	 * @Name 1300_ZNXC_ZNXC_VALUE_TAG
	 * @Catalogue 首页-智能选车
	 * @Grade 高级
	 * @FunctionPoint 首页-智能选车，检查最关注保值时结果对应的最匹配文案。
	 **/
	@Test
	public void test_1300_ZNXC_VALUE_TAG() {
		reports_HomePageTestRN.startTest("test_1300_ZNXC_VALUE_TAG");
		gotocate(1);
		wait(2);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击购车顾问");
		clickElementByName("购车顾问");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击智能选车");
//		clickElementById("gujiall");//智能选车
//		clickElementByName("我是您的专属购车顾问，可以帮您更快更准的筛选超值好车，您也可以体验人工智能选车，点击体验");
		clickElementByName("优信为您提供购车顾问服务，帮您全国范围挑好车。您还可以选择智能选车（点击体验）");
		wait(1);
		checkTitlebar1("智能选车（1/3）");
		if(findElementById("askquestionstr").getText().equals("您更喜欢什么样类型的车？")) {
			clickElementById("confirm");  //再想想
			checkTitlebar1("智能选车（2/3）");
			if(findElementById("askquestionstr").getText().equals("选择您最关注的内容")) {
				int width = driver.manage().window().getSize().width;
				int hight = driver.manage().window().getSize().height;
				reports_HomePageTestRN.log(LogStatus.INFO, "最关注的内容选择保值");
				driver.tap(1, width*5/6, hight*5/8, 1);//保值
				if(findElementById("confirm").getText().equals("至少选择2项（1/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了一个最关注的内容");
					System.out.println("选了一个最关注的内容");
				}
				driver.tap(1, width/2, hight*5/8, 1);//配置
				if(findElementById("confirm").getText().equals("选好了（2/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了第2关注的内容");
					System.out.println("选了第2关注的内容");
				}
				clickElementById("confirm");
				wait(1);
				checkTitlebar1("智能选车（3/3）");
				if(findElementById("confirm").equals("条件太苛刻，请重新选择")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "条件太苛刻，没有合适车源。");
					System.out.println("条件太苛刻，没有合适车源。");
				}else {
					clickElementById("confirm");
					if(findElementById("tag").getText().equals("车型最保值")) {
						reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择保值，对应匹配的文案正确。");
						System.out.println("最关注的选择保值，对应匹配的文案正确。");
					}else {
						reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择保值，对应匹配的文案不正确，请检查。");
						failAndMessage("最关注的选择保值，对应匹配的文案不正确，请检查。");
					}
				}
			}
		}
	}
	
	/**
	 * @Name 1300_ZNXC_ZNXC_DRIVER_TAG
	 * @Catalogue 首页-智能选车
	 * @Grade 高级
	 * @FunctionPoint 首页-智能选车，检查最关注辅助驾驶时结果对应的最匹配文案。
	 **/
	@Test
	public void test_1300_ZNXC_DRIVER_TAG() {
		reports_HomePageTestRN.startTest("test_1300_ZNXC_DRIVER_TAG");
		gotocate(1);
		wait(2);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击购车顾问");
		clickElementByName("购车顾问");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击智能选车");
//		clickElementById("gujiall");//智能选车
//		clickElementByName("我是您的专属购车顾问，可以帮您更快更准的筛选超值好车，您也可以体验人工智能选车，点击体验");
		clickElementByName("优信为您提供购车顾问服务，帮您全国范围挑好车。您还可以选择智能选车（点击体验）");
		wait(1);
		checkTitlebar1("智能选车（1/3）");
		if(findElementById("askquestionstr").getText().equals("您更喜欢什么样类型的车？")) {
			clickElementById("confirm");  //再想想
			checkTitlebar1("智能选车（2/3）");
			if(findElementById("askquestionstr").getText().equals("选择您最关注的内容")) {
				int width = driver.manage().window().getSize().width;
				int hight = driver.manage().window().getSize().height;
				reports_HomePageTestRN.log(LogStatus.INFO, "最关注的内容选择辅助驾驶");
				driver.tap(1, width*5/6, hight*13/16, 1);//辅助驾驶
				if(findElementById("confirm").getText().equals("至少选择2项（1/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了一个最关注的内容");
					System.out.println("选了一个最关注的内容");
				}
				driver.tap(1, width/2, hight*5/8, 1);//配置
				if(findElementById("confirm").getText().equals("选好了（2/6）")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "选了第2关注的内容");
					System.out.println("选了第2关注的内容");
				}
				clickElementById("confirm");
				wait(1);
				checkTitlebar1("智能选车（3/3）");
				if(findElementById("confirm").equals("条件太苛刻，请重新选择")) {
					reports_HomePageTestRN.log(LogStatus.INFO, "条件太苛刻，没有合适车源。");
					System.out.println("条件太苛刻，没有合适车源。");
				}else {
					clickElementById("confirm");
					if(findElementById("tag").getText().equals("驾驶最舒适")) {
						reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择辅助驾驶，对应匹配的文案正确。");
						System.out.println("最关注的选择辅助驾驶，对应匹配的文案正确。");
					}else {
						reports_HomePageTestRN.log(LogStatus.INFO, "最关注的选择辅助驾驶，对应匹配的文案不正确，请检查。");
						failAndMessage("最关注的选择辅助驾驶，对应匹配的文案不正确，请检查。");
					}
				}
			}
		}
	}
	
	/**
	 * @Name 1300_2SCYCG_MORE
	 * @Catalogue 首页-一成购
	 * @Grade 中级
	 * @FunctionPoint 首页-一成购，点击一成购，查看跳转
	 **/
	@Test
	public void test_1300_2SCYCG_MORE() {
		reports_HomePageTestRN.startTest("test_1300_2SCYCG_MORE");
		gotocate(1);
		reports_HomePageTestRN.log(LogStatus.INFO,"点击一成购按钮");
		clickElementByName("一成购");
		wait(2);
		if (findElementById("tv_pop_content").getText().equals("一成购")) {
			CheckNewListFromHomePage();
				if(CheckViewVisibilty(By.id("ivyichengpayTag"))) {
					reports_HomePageTestRN.log(LogStatus.INFO, "点击一成购进入的新列表页显示正常");
					System.out.println("点击一成购进入的新列表页显示正常");
					toDetailFromNewList();
					checkTitlebar_Detail();
		        	backBTN();
				}
		}else {
			reports_HomePageTestRN.log(LogStatus.ERROR, "一成购列表显示错误");
			failAndMessage("一成购列表显示错误");
		}
	}
	
	/**
	 * @Name test_1300_ZSCK
	 * @Catalogue 首页-工具区-VR看车
	 * @Grade 中级
	 * @FunctionPoint 首页-工具区-VR看车，点击工具区-VR看车，查看跳转
	 **/
	@Test
	public void test_1300_ZSCK() {
		reports_HomePageTestRN.startTest("test_1300_ZSCK");
		gotocate(1);
		reports_HomePageTestRN.log(LogStatus.INFO,"点击VR看车按钮");
		clickElementByName("VR看车");
		wait(2);
		if (findElementById("tv_pop_content").getText().equals("VR看车")) {
			CheckNewListFromHomePage();
			reports_HomePageTestRN.log(LogStatus.INFO, "进入VR看车对应新列表页。");
			System.out.println("进入VR看车对应新列表页。");
			toDetailFromNewList();
			checkTitlebar_Detail();
        	backBTN();
		}else {
			reports_HomePageTestRN.log(LogStatus.ERROR, "首页，点击VR看车，没有进入VR看车对应新列表页。");
			failAndMessage("首页，点击VR看车，没有进入VR看车对应新列表页。");
		}
	}
	
	/**
	 * @Name test_1300_CZHC
	 * @Catalogue 首页-超值好车
	 * @Grade 中级
	 * @FunctionPoint 首页-超值好车，点击超值好车，查看跳转
	 **/
	@Test
	public void test_1300_CZHC() {
		reports_HomePageTestRN.startTest("test_1300_CZHC");
		gotocate(1);
		reports_HomePageTestRN.log(LogStatus.INFO,"点击超值好车按钮");
//		clickButtonShouYe("超值好车");
		clickElementByName("超值好车");
		wait(2);
		if (findElementById("tv_pop_content").getText().equals("超值好车")) {
			CheckNewListFromHomePage();
			reports_HomePageTestRN.log(LogStatus.INFO, "进入超值好车对应新列表页。");
			System.out.println("进入超值好车对应新列表页。");
			toDetailFromNewList();
			checkTitlebar_Detail();
        	backBTN();
		}else {
			reports_HomePageTestRN.log(LogStatus.ERROR, "首页，点击超值好车，没有进入超值好车对应新列表页。");
			failAndMessage("首页，点击超值好车，没有进入超值好车对应新列表页。");
		}
	}
	
	/**
	 * @Name test_1300_CZHC
	 * @Catalogue 首页-3天无理由
	 * @Grade 中级
	 * @FunctionPoint 首页-3天无理由，点击3天无理由，查看跳转
	 **/
	@Test
	public void test_1300_3TWLY() {
		reports_HomePageTestRN.startTest("test_1300_3TWLY");
		gotocate(1);
		reports_HomePageTestRN.log(LogStatus.INFO,"点击3天无理由按钮");
//		clickButtonShouYe("3天无理由");
		clickElementByName("3天无理由");
		wait(2);
		if (findElementById("tv_pop_content").getText().equals("3天无理由")) {
			CheckNewListFromHomePage();
			reports_HomePageTestRN.log(LogStatus.INFO, "进入3天无理由对应新列表页。");
			System.out.println("进入3天无理由对应新列表页。");
			toDetailFromNewList();
			checkTitlebar_Detail();
        	backBTN();
		}else {
			reports_HomePageTestRN.log(LogStatus.ERROR, "首页，点击3天无理由，没有进入3天无理由对应新列表页。");
			failAndMessage("首页，点击3天无理由，没有进入3天无理由对应新列表页。");
		}
	}
	
	/**
	 * @Name 1102_RMJG
	 * @Catalogue 首页-热门车价
	 * @Grade 高级
	 * @FunctionPoint 首页-热门车价，遍历热门车价查看跳转是否正确
	 * @备注：车价9.5版本已修改成5万以下
	 **/
	@Test
	public void test_1102_RMJG() {
		reports_HomePageTestRN.startTest("test_1102_RMJG");
		gotocate(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "开始遍历热门车价");
		String[] str = {"5万以下","5-10万","10-15万","15万以上"};
		String[] listStrings = {"5万以下","5-10万元","10-15万元","15万以上"};
		for (int i = 1; i <= 4; i++) {
//			if (CheckViewVisibilty(By.xpath("//android.widget.TextView[@text='"+str[i-1]+"']"))) {
//				clickElementByXpath("//android.widget.TextView[@text='"+str[i-1]+"']");
			if (CheckViewVisibiltyByName(str[i-1])) {
				clickElementByName(str[i-1]);
				wait(2);
				if (findElementById("tv_pop_content").getText().equals(listStrings[i-1])) {
					CheckNewListFromHomePage();
					toDetailFromNewList();
					checkTitlebar_Detail();
		        	backBTN();
					driver.pressKeyCode(AndroidKeyCode.BACK);
					sleep(500);
				}else {
					reports_HomePageTestRN.log(LogStatus.ERROR, "列表页筛选条件与预期不符，应为>"+listStrings[i-1]+"。实际为>"+findElementById("tv_pop_content").getText());
					failAndMessage("列表页筛选条件与预期不符，应为>"+listStrings[i-1]+"。实际为>"+findElementById("tv_pop_content").getText());
				}
			}else {
				reports_HomePageTestRN.log(LogStatus.ERROR, "与预期热门车价不一致，应为>"+str[i-1]);
				failAndMessage("与预期热门车价不一致，应为>"+str[i-1]);
			}
//			}
		}
	}
	
	/**
	 * @Name 1101_PPXC
	 * @Catalogue 首页-热门品牌
	 * @Grade 高级
	 * @FunctionPoint 首页-热门品牌，遍历热门品牌
	 */
	@Test
	public void test_1101_RMPP() {
		reports_HomePageTestRN.startTest("test_1100_PPXC");
		gotocate(1);
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*2/3, width/2, height*1/3, 1000);
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "开始遍历热门车系");
		String[] str = {"大众","别克","奔驰","本田","宝马","奥迪","丰田"};
		for (int i = 0; i < str.length; i++) {
			clickElementByName(str[i]);
			wait(2);
			if (findElementById("tv_pop_content").getText().equals(str[i])) {
				CheckNewListFromHomePage();
				reports_HomePageTestRN.log(LogStatus.INFO, "进入对应新列表页。");
				System.out.println("进入对应新列表页。");
				toDetailFromNewList();
				checkTitlebar_Detail();
	        	backBTN();
				driver.pressKeyCode(AndroidKeyCode.BACK);
				sleep(200);
			}else {
				reports_HomePageTestRN.log(LogStatus.ERROR, "没有进入进入预定列表页,应为"+str[i]+",实际为"+waitForVisible(By.id("tvSearchHistory"), 3).getAttribute("text"));
				failAndMessage("没有进入进入预定列表页,应为"+str[i]+",实际为"+waitForVisible(By.id("tvSearchHistory"), 3).getAttribute("text"));
			}
		}
	}
	
	/**
	 * @Name 1101_RMCX
	 * @Catalogue 首页-热门车型
	 * @Grade 高级
	 * @FunctionPoint 首页-热门车型，遍历热门车型
	 */
	@Test
	public void test_1101_RMCX() {
		reports_HomePageTestRN.startTest("test_1101_RMCX");
		gotocate(1);
		wait(1);
//		int width = driver.manage().window().getSize().width;
//		int height = driver.manage().window().getSize().height;
//		driver.swipe(width/2, height*2/3, width/2, height*1/3, 1000);
//		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "开始遍历热门车型");
		String[] str = {"SUV","MPV","三厢轿车","两厢轿车"};
		for (int i = 0; i < str.length; i++) {
			clickElementByName(str[i]);
			wait(2);
			if (findElementById("tv_pop_content").getText().equals(str[i])) {
				CheckNewListFromHomePage();
				reports_HomePageTestRN.log(LogStatus.INFO, "进入对应新列表页。");
				System.out.println("进入对应新列表页。");
				toDetailFromNewList();
				checkTitlebar_Detail();
	        	backBTN();
				driver.pressKeyCode(AndroidKeyCode.BACK);
				sleep(200);
			}else {
				reports_HomePageTestRN.log(LogStatus.ERROR, "没有进入进入预定列表页,应为"+str[i]+",实际为"+waitForVisible(By.id("tvSearchHistory"), 3).getAttribute("text"));
				failAndMessage("没有进入进入预定列表页,应为"+str[i]+",实际为"+waitForVisible(By.id("tvSearchHistory"), 3).getAttribute("text"));
			}
		}
	}
	
	/**
	 * @Name 1000_Start_1
	 * @Catalogue 首页
	 * @Grade 普通
	 * @FunctionPoint 首页UI检查
	 */
	@Test
	public void test_1000_Start_1(){
		reports_HomePageTestRN.startTest("test_1000_Start_1");
		gotocate(1);
		sliding("","","","down","");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "刷新界面");
//		if (CheckViewVisibilty(By.xpath("//android.widget.TextView[@text='北京']"))) {
//			if(CheckViewVisibilty(By.xpath("//android.widget.TextView[@text='5万以下']"))&&
//					CheckViewVisibilty(By.xpath("//android.widget.TextView[@text='3天无理由']"))&&
//					
		if (CheckViewVisibiltyByName("北京")) {
			if(CheckViewVisibiltyByName("买车商城")&&
					CheckViewVisibiltyByName("商城热卖")&&
					CheckViewVisibiltyByName("5万以下")&&  //3万以下
					CheckViewVisibiltyByName("SUV")&&
					CheckViewVisibilty(By.xpath("//android.widget.TextView[contains(@text,'共有')]"))) {//查看全部XXX辆车源
				reports_HomePageTestRN.log(LogStatus.INFO, "首页UI显示正常。");
				System.out.println("首页UI显示正常。");
			}else {
				reports_HomePageTestRN.log(LogStatus.INFO, "首页UI显示异常。");
				failAndMessage("首页UI显示异常。");
			}
		}else {
			Assert.assertTrue(false,"城市定位有问题，当前城市不是北京");
			reports_HomePageTestRN.log(LogStatus.ERROR,"城市定位有问题，当前城市不是北京");
		}
	}
//		}
//	  }
	
//	/**
//	 * @Name 1600_YXRZJS
//	 * @Catalogue 首页-优信金牌认证介绍页
//	 * @Grade 高级
//	 * @FunctionPoint 首页-优信金牌认证介绍页，点击入口查看跳转
//	 **/
//	@Test
//	public void test_1600_YXRZJS() {
//		reports_HomePageTestRN.startTest("test_1600_YXRZJS");
//		gotocate(1);
//		int width = driver.manage().window().getSize().width;
//		int height = driver.manage().window().getSize().height;
//		driver.swipe(width/2, height*2/3, width/2, height*1/3, 1000);
//		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页优信金牌认证入口");
//		clickElementById("type201");
//		wait(3);
//		checkTitlebar_Webview("优信金牌认证");
//	}
	
//	/**
//	 * @Name 1904_RMTJ_JRYH
//	 * @Catalogue 首页-热门推荐-今日特惠
//	 * @Grade 高级
//	 * @FunctionPoint 首页-热门推荐-今日特惠，验证今日特惠跳转功能
//	 **/
//	@Test
//	public void test_1904_RMTJ_JRYH() {
//		reports_HomePageTestRN.startTest("test_1904_RMTJ_JRYH");
//		gotocate(1);
//		wait(1);
//		int width = driver.manage().window().getSize().width;
//		int height = driver.manage().window().getSize().height;
//		driver.swipe(width/2, height*3/4, width/2, height/4, 1000);
//		wait(1);
//		reports_HomePageTestRN.log(LogStatus.INFO, "点击热门推荐今日特惠");
//		clickElementByName("今日特惠");
//		if (findElementById("tv_pop_content").getText().equals("今日特惠")) {
//			CheckNewListFromHomePage();
//			toDetailFromNewList();
//			checkTitlebar_Detail();
//        	backBTN();
//		}else {
//			reports_HomePageTestRN.log(LogStatus.INFO, "点击今日特惠跳转到的列表页筛选显示不正常");
//			failAndMessage("点击今日特惠跳转到的列表页筛选显示不正常");
//		}
//	}
	
	/**
	 * @Name 1904_RMTJ_HCJJ
	 * @Catalogue 首页-热门推荐-好车急将
	 * @Grade 高级
	 * @FunctionPoint 首页-热门推荐-好车急将/付一成开豪车，验证好车急将跳转功能
	 **/
	@Test
	public void test_1904_RMTJ_HCJJ() {
		reports_HomePageTestRN.startTest("test_1904_RMTJ_HCJJ");
		gotocate(1);
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*3/4, width/2, height/4, 1000);
		wait(1);
		if(CheckViewVisibiltyByName("好车急降")) {
			clickElementByName("好车急降");
			reports_HomePageTestRN.log(LogStatus.INFO, "点击热门推荐：好车急降");
			wait(2);
			checkTitlebar1("好车急降");
			reports_HomePageTestRN.log(LogStatus.INFO, "点击好车急降跳转到正常");
			System.out.println("点击好车急降跳转正常");
		}else {
			clickElementByName("付一成开豪车");
			reports_HomePageTestRN.log(LogStatus.INFO, "点击热门推荐：付一成开豪车");
			wait(2);
			if (findElementById("tv_pop_content").getText().equals("付一成开豪车")) {
				CheckNewListFromHomePage();
				toDetailFromNewList();
				checkTitlebar_Detail();
	        	backBTN();
			}else {
				reports_HomePageTestRN.log(LogStatus.INFO, "点击付一成开豪车跳转到的列表页筛选显示不正常");
				failAndMessage("点击付一成开豪车跳转到的列表页筛选显示不正常");
			}
		}
		
		
		
	}
	
	/**
	 * @Name 1904_RMTJ_TTTJ
	 * @Catalogue 首页-热门推荐-天天特价
	 * @Grade 高级
	 * @FunctionPoint 首页-热门推荐-天天特价，验证天天特价跳转功能
	 **/
	@Test
	public void test_1904_RMTJ_TTTJ() {
		reports_HomePageTestRN.startTest("test_1904_RMTJ_TTTJ");
		gotocate(1);
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*3/4, width/2, height/4, 1000);
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击热门推荐天天特价");
		clickElementByName("天天特价");
		if (findElementById("tv_pop_content").getText().equals("天天特价")) {
			CheckNewListFromHomePage();
			toDetailFromNewList();
			checkTitlebar_Detail();
       	backBTN();
		}else {
			reports_HomePageTestRN.log(LogStatus.INFO, "点击天天特价跳转到的列表页筛选显示不正常");
			failAndMessage("点击天天特价跳转到的列表页筛选显示不正常");
		}
	}
	
	/**
	 * @Name 1902_RMTJ_JRXS
	 * @Catalogue 首页-热门推荐-今日新上
	 * @Grade 高级
	 * @FunctionPoint 首页-热门推荐-今日新上，验证今日新上跳转功能
	 **/
	@Test
	public void test_1902_RMTJ_JRXS() {
		reports_HomePageTestRN.startTest("test_1900_RMTJ_JRXS");
		gotocate(1);
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*3/4, width/2, height/4, 1000);
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击热门推荐今日新上");
		clickElementByName("今日新上");
		wait(2);
		if (findElementById("tv_pop_content").getText().equals("今日新上")){
			CheckNewListFromHomePage();
			toDetailFromNewList();
			checkTitlebar_Detail();
        	backBTN();
		}else {
			reports_HomePageTestRN.log(LogStatus.INFO, "点击今日新上跳转到的列表页筛选显示不正常");
			failAndMessage("点击今日新上跳转到的列表页筛选显示不正常");
		}
	}
	
	/**
	 * @Name 1903_RMTJ_ZXC
	 * @Catalogue 首页-热门推荐
	 * @Grade 高级
	 * @FunctionPoint 首页-热门推荐，验证准新车跳转功能
	 **/
	@Test
	public void test_1903_RMTJ_ZXC() {
		reports_HomePageTestRN.startTest("test_1900_RMTJ_ZXC");
		gotocate(1);
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*3/4, width/2, height/4, 1000);
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击热门推荐准新车");
		clickElementByName("准新车");
		wait(2);
		if (findElementById("tv_pop_content").getText().equals("准新车")){
			CheckNewListFromHomePage();
			toDetailFromNewList();
			checkTitlebar_Detail();
        	backBTN();
		}else {
			reports_HomePageTestRN.log(LogStatus.INFO, "点击准新车跳转到的列表页筛选显示不正常");
			failAndMessage("点击准新车跳转到的列表页筛选显示不正常");
		}
	}
	
	/**
	 * @Name 1900_RMTJ_SYDB
	 * @Catalogue 首页-热门推荐
	 * @Grade 高级
	 * @FunctionPoint 首页-热门推荐，验证省油代步车跳转功能
	 **/
	@Test
	public void test_1900_RMTJ_SYDB() {
		reports_HomePageTestRN.startTest("test_1900_RMTJ_SYDB");
		gotocate(1);
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*3/4, width/2, height/4, 1000);
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击热门推荐省油代步");
		clickElementByName("省油代步车");
		wait(3);
		if (findElementById("tv_pop_content").getText().equals("省油代步车")) {
			CheckNewListFromHomePage();
			toDetailFromNewList();
			checkTitlebar_Detail();
        	backBTN();
		}else {
			GetScreenshot("test_1900_RMTJ_SYDB");
			reports_HomePageTestRN.log(LogStatus.INFO, "点击省油代步车跳转到的列表页筛选显示不正常");
			failAndMessage("点击省油代步车跳转到的列表页筛选显示不正常");
		}
	}
	
	/**
	 * @Name 1901_RMTJ_QYYG
	 * @Catalogue 首页-热门推荐
	 * @Grade 高级
	 * @FunctionPoint 首页-热门推荐，验证捡漏好车/精选好车/超低预算/性价之王/大众专场跳转功能
	 **/
	@Test
	public void test_1901_RMTJ_QYYG() {
		reports_HomePageTestRN.startTest("test_1900_RMTJ_QYYG");
		gotocate(1);
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*3/4, width/2, height/4, 1000);
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击热门推荐精选好车");
		clickElementByName("精选好车");
		wait(2);
		if (findElementById("tv_pop_content").getText().equals("精选好车")) {
			CheckNewListFromHomePage();
			toDetailFromNewList();
			checkTitlebar_Detail();
        	backBTN();
		}else {
			reports_HomePageTestRN.log(LogStatus.INFO, "点击精选好车跳转到的列表页筛选显示不正常");
			failAndMessage("点击精选好车跳转到的列表页筛选显示不正常");
		}
	}

	/**
	 * @Name 1601_YCGJS
	 * @Catalogue 首页-一成购介绍页
	 * @Grade 高级
	 * @FunctionPoint 首页-一成购详情介绍页，点击入口查看跳转
	 **/
	@Test
	public void test_1601_YCGJS() {
		reports_HomePageTestRN.startTest("test_1600_YXRZJS");
		gotocate(1);
		wait(1);
		sliding("up");
		wait(1);
//		clickDetaileShouYe("一成购");
		findElementByXpath("//android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]").click();
		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页一成购详情介绍页入口");
		wait(2);
		checkTitlebar_Webview("一成购介绍");
		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页一成购详情介绍页按钮进入一成购介绍页");
		System.out.println("点击首页一成购详情介绍页按钮进入一成购介绍页");
	}
	
	/**
	 * @Name 1601_YCG_XC
	 * @Catalogue 首页-一成购介绍页
	 * @Grade 高级
	 * @FunctionPoint 首页-一成购介绍页，点击一成购选车
	 **/
	@Test
	public void test_1601_YCG_XC() {
		reports_HomePageTestRN.startTest("test_1600_YCG_XC");
		gotocate(1);
		wait(1);
		sliding("up");
		wait(1);
//		clickDetaileShouYe("一成购");
		findElementByXpath("//android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]").click();
		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页一成购详情介绍页入口");
		wait(2);
		checkTitlebar_Webview("一成购介绍");
		wait(1);
//		try {
//			switchToWebView();
//		} catch (Exception e) {
//			System.out.println("第一次切换失败尝试再次切换");
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
//			clickDetaileShouYe("一成购");
//			wait(1);
//			switchToWebView();	
//		}
//		reports_HomePageTestRN.log(LogStatus.INFO, "点击一成购介绍页左下角的一成购选车按钮");
//		System.out.println("点击一成购介绍页左下角的一成购选车按钮");
//		clickElementByXpath("/html/body/div[2]/div/a[1]");
//		wait(2);
//		switchToNative();
//		wait(1);
//		if(findElementByName("买车").getAttribute("checked").equals("true")&&
//				findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[3]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.TextView[2]").getText().equals("1")) {
//			reports_HomePageTestRN.log(LogStatus.INFO, "点击一成购介绍页左下角的一成购选车按钮，进入一成购列表页。");
//        	System.out.println("点击一成购介绍页左下角的一成购选车按钮，进入一成购列表页。");
//        }else {
//        	reports_HomePageTestRN.log(LogStatus.ERROR, "点击一成购介绍页左下角的一成购选车按钮，没有进入一成购列表页。");
//       	 	failAndMessage("点击一成购介绍页左下角的一成购选车按钮，没有进入一成购列表页。");
//		}
	}
	
	/**
	 * @Name 1601_YCGJS_MORE
	 * @Catalogue 首页-一成购介绍页
	 * @Grade 高级
	 * @FunctionPoint 首页-一成购介绍页，点击查看更多车源
	 **/
	@Test
	public void test_1601_YCGJS_MORE() {
		reports_HomePageTestRN.startTest("test_1601_YCGJS_MORE");
		gotocate(1);
		wait(1);
		sliding("up");
		wait(1);
//		clickDetaileShouYe("一成购");
		findElementByXpath("//android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]").click();
		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页一成购详情介绍页入口");
		wait(2);
		checkTitlebar_Webview("一成购介绍");
		for(int i=0; i<8; i++) {
			sliding("up");
		}
		wait(1);
//		try {
//			switchToWebView();
//		} catch (Exception e) {
//			System.out.println("第一次切换失败尝试再次切换");
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
//			clickDetaileShouYe("一成购");
//			for(int i=0; i<8; i++) {
//				sliding("up");
//			}
//			wait(1);
//			switchToWebView();	
//		}
//		reports_HomePageTestRN.log(LogStatus.INFO, "点击一成购介绍页底部的查看更多车源按钮");
//		System.out.println("点击一成购介绍页底部的查看更多车源按钮");
//		clickElementByXpath("/html/body/div[1]/div[2]/div/div[2]/a");
//		wait(3);
//		switchToNative();
//		wait(1);
//		if(findElementByName("买车").getAttribute("checked").equals("true")&&
//				findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[3]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.TextView[2]").getText().equals("1")) {
//			reports_HomePageTestRN.log(LogStatus.INFO, "点击一成购介绍页底部的查看更多车源按钮，进入一成购列表页。");
//        	System.out.println("点击一成购介绍页底部的查看更多车源按钮，进入一成购列表页。");
//        }else {
//        	reports_HomePageTestRN.log(LogStatus.ERROR, "点击一成购介绍页底部的查看更多车源按钮，没有进入一成购列表页。");
//       	 	failAndMessage("点击一成购介绍页底部的查看更多车源按钮，没有进入一成购列表页。");
//		}
	}
	
	/**
	 * @Name 1102_RSCYCG_1
	 * @Catalogue 首页-二手车一成购
	 * @Grade 高级
	 * @FunctionPoint 首页-二手车一成购，点击一成购第一个热门品牌入口
	 **/
	@Test
	public void test_1102_RSCYCG_1() {
		reports_HomePageTestRN.startTest("test_1102_RSCYCG_1");
		gotocate(1);
		wait(1);
		sliding("up");
		wait(1);
//		int width = driver.manage().window().getSize().width;
//		int height = driver.manage().window().getSize().height;
//		driver.swipe(width/2, height*4/5, width/2, height*1/10, 1000);
		String title = findElementByXpath("//*/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]").getText();
		System.out.println(title);
		clickElementByXpath("//*/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]");
		reports_HomePageTestRN.log(LogStatus.INFO,"点击首页一成购第一个品牌车系入口");
		wait(2);
		System.out.println(findElementById("tv_pop_content").getText());
		if(findElementById("tv_pop_content").getText().equals(title)) {
			CheckNewListFromHomePage();
			toDetailFromNewList();
			checkTitlebar_Detail();
        	backBTN();
			driver.pressKeyCode(AndroidKeyCode.BACK);
			sleep(100);
		}else {
			reports_HomePageTestRN.log(LogStatus.ERROR,"点击首页一成购第一个品牌车系"+title+";跳转的不对。");
			failAndMessage("点击首页一成购第一个品牌车系"+title+";跳转的不对。");
		}
	}
	
	/**
	 * @Name 1102_RSCYCG_2
	 * @Catalogue 首页-二手车一成购
	 * @Grade 高级
	 * @FunctionPoint 首页-二手车一成购，点击一成购第2个热门品牌入口
	 **/
	@Test
	public void test_1102_RSCYCG_2() {
		reports_HomePageTestRN.startTest("test_1102_RSCYCG_2");
		gotocate(1);
		wait(1);
		sliding("up");
		wait(1);
//		int width = driver.manage().window().getSize().width;
//		int height = driver.manage().window().getSize().height;
//		driver.swipe(width/2, height*4/5, width/2, height*1/10, 1000);
		String title = findElementByXpath("//*/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]").getText();
		clickElementByXpath("//*/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]");
		reports_HomePageTestRN.log(LogStatus.INFO,"点击首页一成购第2个品牌车系入口");
		wait(2);
		if(findElementById("tv_pop_content").getText().equals(title)) {
			CheckNewListFromHomePage();
			toDetailFromNewList();
			checkTitlebar_Detail();
        	backBTN();
			driver.pressKeyCode(AndroidKeyCode.BACK);
			sleep(100);
		}else {
			reports_HomePageTestRN.log(LogStatus.ERROR,"点击首页一成购第2个品牌车系"+title+";跳转的不对。");
			failAndMessage("点击首页一成购第2个品牌车系"+title+";跳转的不对。");
		}
	}
	
//	/**
//	 * @Name 1103_RSCYCG
//	 * @Catalogue 首页-二手车一成购
//	 * @Grade 高级
//	 * @FunctionPoint 首页-二手车一成购，点击一成购banner查看跳转
//	 **/
//	@Test
//	public void test_1103_RSCYCG() {
//		reports_HomePageTestRN.startTest("test_1102_RSCYCG");
//		gotocate(1);
//		wait(1);
////		sliding("up");
////		sleep(1000);
//		swipeUntilElement(By.id("yichenggouad"), "up", 3);
//		wait(1);
////		int width = driver.manage().window().getSize().width;
////		int height = driver.manage().window().getSize().height;
////		driver.swipe(width/2, height*4/5, width/2, height*3/10, 1000);
//		if (CheckViewVisibilty(By.id("yichenggouad"))) {
//			clickElementById("yichenggouad");
//			wait(2);
//			if (findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[3]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.TextView[2]").getText().equals("1")) {
//				toDetail(1);
//			}else {
//				reports_HomePageTestRN.log(LogStatus.ERROR, "点击一成购banner跳转到的列表页显示不正常");
//				failAndMessage("点击一成购banner跳转到的列表页显示不正常");
//			}
//		}else {
//			reports_HomePageTestRN.log(LogStatus.ERROR,"一成购banner没有正常显示");
//			failAndMessage("一成购banner没有正常显示");
//		}
//	}
	
	/**
	 * @Name 1601_ZSCK_detail
	 * @Catalogue 首页-真实车况介绍页
	 * @Grade 高级
	 * @FunctionPoint 首页-真实车况介绍页，点击查看更多视频车源
	 **/
	@Test
	public void test_1601_ZSCK_detail() {
		reports_HomePageTestRN.startTest("test_1601_ZSCK_detail");
		gotocate(1);
		wait(2);
		sliding("up");
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*2/7, width/2, height*1/7, 1000);
		wait(2);
//		clickDetaileShouYe("真实车况");
		findElementByXpath("//android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]").click();
		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页真实车况介绍页入口");
		wait(2);
		checkTitlebar_Webview("视频检测");
		wait(1);
//		try {
//			switchToWebView();
//		} catch (Exception e) {
//			System.out.println("第一次切换失败尝试再次切换");
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
//			clickDetaileShouYe("真实车况");
//			wait(1);
//			switchToWebView();	
//		}
//		reports_HomePageTestRN.log(LogStatus.INFO, "点击真实车况介绍页底部的查看更多视频车源按钮");
//		System.out.println("点击真实车况介绍页底部的查看更多视频车源按钮");
//		clickElementByXpath("/html/body/div[2]/span");
//		wait(3);
//		switchToNative();
//		wait(1);
//		if(findElementByName("买车").getAttribute("checked").equals("true")&&
//				findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[3]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.TextView[2]").getText().equals("1")) {
//			reports_HomePageTestRN.log(LogStatus.INFO, "点击真实车况介绍页底部的查看更多视频车源按钮，进入买车列表页。");
//        	System.out.println("点击真实车况介绍页底部的查看更多视频车源按钮，进入买车列表页。");
//        }else {
//        	reports_HomePageTestRN.log(LogStatus.ERROR, "点击真实车况介绍页底部的查看更多视频车源按钮，没有进入买车列表页。");
//       	 	failAndMessage("点击真实车况介绍页底部的查看更多视频车源按钮，没有进入买车列表页。");
//		}
	}
	
	/**
	 * @Name 1900_ZSCK_video
	 * @Catalogue 首页-真实车况
	 * @Grade 高级
	 * @FunctionPoint 首页-真实车况，点击视频检测
	 **/
	@Test
	public void test_1900_ZSCK_video() {
		reports_HomePageTestRN.startTest("test_1900_ZSCK_video");
		gotocate(1);
		wait(1);
		sliding("up");
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*3/4, width/2, height*2/4, 1000);
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击真实车况-视频检测");
		clickElementByName("视频检测");
		wait(3);
		if (findElementById("tv_pop_content").getText().equals("视频检测")) {
			CheckNewListFromHomePage();
			toDetailFromNewList();
			checkTitlebar_Detail();
        	backBTN();
		}else {
			GetScreenshot("test_1900_ZSCK_video");
			reports_HomePageTestRN.log(LogStatus.INFO, "点击真实车况-视频检测跳转到的列表页筛选显示不正常");
			failAndMessage("点击真实车况-视频检测跳转到的列表页筛选显示不正常");
		}
	}
	
	/**
	 * @Name 1900_ZSCK_SSKC
	 * @Catalogue 首页-真实车况
	 * @Grade 高级
	 * @FunctionPoint 首页-真实车况，点击随时看车
	 **/
	@Test
	public void test_1900_ZSCK_SSKC() {
		reports_HomePageTestRN.startTest("test_1900_ZSCK_SSKC");
		gotocate(1);
		wait(1);
		sliding("up");
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*3/4, width/2, height*2/4, 1000);
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击真实车况-随时看车");
		clickElementByName("随时看车");
		wait(3);
		if (findElementById("tv_pop_content").getText().equals("随时看车")) {
			CheckNewListFromHomePage();
			toDetailFromNewList();
			checkTitlebar_Detail();
        	backBTN();
		}else {
			GetScreenshot("test_1900_ZSCK_SSKC");
			reports_HomePageTestRN.log(LogStatus.INFO, "点击真实车况-随时看车跳转到的列表页筛选显示不正常");
			failAndMessage("点击真实车况-随时看车跳转到的列表页筛选显示不正常");
		}
	}
	
	/**
	 * @Name 1601_CZHC_detail
	 * @Catalogue 首页-超值好车介绍页
	 * @Grade 高级
	 * @FunctionPoint 首页-超值好车介绍页，点击查看更多超值车源
	 **/
	@Test
	public void test_1601_CZHC_detail() {
		reports_HomePageTestRN.startTest("test_1601_CZHC_detail");
		gotocate(1);
		wait(1);
		sliding("up");
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*4/5, width/2, height*1/5, 1000);
		wait(1);
//		clickDetaileShouYe("超值好车");
		findElementByXpath("//*/android.view.ViewGroup[1]/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]").click();
		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页超值好车介绍页入口");
		wait(1);
		checkTitlebar_Webview("超值好车");
		wait(1);
//		try {
//			switchToWebView();
//		} catch (Exception e) {
//			System.out.println("第一次切换失败尝试再次切换");
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
//			clickDetaileShouYe("超值好车");
//			wait(1);
//			switchToWebView();	
//		}
//		reports_HomePageTestRN.log(LogStatus.INFO, "点击超值好车介绍页底部的查看更多超值车源按钮");
//		System.out.println("点击超值好车介绍页底部的查看更多超值车源按钮");
//		clickElementByXpath("/html/body/div[1]/span");
//		wait(3);
//		switchToNative();
//		wait(1);
//		if(findElementByName("买车").getAttribute("checked").equals("true")&&
//				findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[3]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.TextView[2]").getText().equals("1")) {
//			reports_HomePageTestRN.log(LogStatus.INFO, "点击超值好车介绍页底部的查看更多超值车源按钮，进入买车列表页。");
//        	System.out.println("点击超值好车介绍页底部的查看更多超值车源按钮，进入买车列表页。");
//        }else {
//        	reports_HomePageTestRN.log(LogStatus.ERROR, "点击超值好车介绍页底部的查看更多超值车源按钮，没有进入买车列表页。");
//       	 	failAndMessage("点击超值好车介绍页底部的查看更多超值车源按钮，没有进入买车列表页。");
//		}
	}
	
	/**
	 * @Name 1900_CZHC_XJBZX
	 * @Catalogue 首页-超值好车
	 * @Grade 高级
	 * @FunctionPoint 首页-超值好车，点击性价比之选
	 **/
	@Test
	public void test_1900_CZHC_XJBZX() {
		reports_HomePageTestRN.startTest("test_1900_CZHC_XJBZX");
		gotocate(1);
		wait(1);
		sliding("up");
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*3/4, width/2, height/4, 1000);
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击超值好车的点击性价比之选");
		clickElementByName("性价比之选");
		wait(3);
		if (findElementById("tv_pop_content").getText().equals("性价比之选")) {
			findElementById("tvCarWholeName").click();
			wait(3);
			reports_HomePageTestRN.log(LogStatus.INFO, "进入车辆详情页");
			checkTitlebar_Detail();
        	backBTN();
		}else {
			GetScreenshot("test_1900_CZHC_XJBZX");
			reports_HomePageTestRN.log(LogStatus.INFO, "点击超值好车的性价比之选跳转到的列表页筛选显示不正常");
			failAndMessage("点击超值好车的性价比之选跳转到的列表页筛选显示不正常");
		}
	}
	
	/**
	 * @Name 1900_CZHC_CZTH
	 * @Catalogue 首页-超值好车
	 * @Grade 高级
	 * @FunctionPoint 首页-超值好车，点击超值特惠
	 **/
	@Test
	public void test_1900_CZHC_CZTH() {
		reports_HomePageTestRN.startTest("test_1900_CZHC_CZTH");
		gotocate(1);
		wait(1);
		sliding("up");
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*3/4, width/2, height/4, 1000);
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击超值好车的点击超值特惠");
		clickElementByName("超值特惠");
		wait(3);
		if (findElementById("tv_pop_content").getText().equals("超值特惠")) {
			CheckNewListFromHomePage();
			reports_HomePageTestRN.log(LogStatus.INFO, "点击超值好车的超值特惠跳转到的列表页筛选显示正常");
			System.out.println("点击超值好车的超值特惠跳转到的列表页筛选显示正常");
			toDetailFromNewList();
			checkTitlebar_Detail();
        	backBTN();
		}else {
			GetScreenshot("test_1900_CZHC_XJBZX");
			reports_HomePageTestRN.log(LogStatus.ERROR, "点击超值好车的超值特惠跳转到的列表页筛选显示不正常");
			failAndMessage("点击超值好车的超值特惠跳转到的列表页筛选显示不正常");
		}
	}
	
	/**
	 * @Name 1601_3TWLY_more
	 * @Catalogue 首页-3天无理由介绍页
	 * @Grade 高级
	 * @FunctionPoint 首页-3天无理由介绍页-点击更多按钮
	 **/
	@Test
	public void test_1601_3TWLY_more() {
		reports_HomePageTestRN.startTest("test_1601_3TWLY_more");
		gotocate(1);
		wait(1);
		sliding("up");
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*6/7, width/2, height*1/7, 1000);
		wait(1);
//		clickElementById("no_reason_tv_more");
		findElementByXpath("//android.widget.TextView[@text='更多']").click();
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击3天无理由介绍页入口");
		if (findElementById("tv_pop_content").getText().equals("三天无理由")) {
			reports_HomePageTestRN.log(LogStatus.INFO, "点击三天无理由更多按钮，进入新列表页");
			System.out.println("点击三天无理由更多按钮，进入新列表页");
			findElementById("tvAge").click();
			wait(3);
			reports_HomePageTestRN.log(LogStatus.INFO, "进入车辆详情页");
			checkTitlebar_Detail();
        	backBTN();
		}else {
			reports_HomePageTestRN.log(LogStatus.ERROR, "点击三天无理由更多按钮，进入的新列表页异常，请检查");
			failAndMessage("点击三天无理由更多按钮，进入的新列表页异常，请检查");
		}
	}
	
	/**
	 * @Name 1900_3TWLY_1
	 * @Catalogue 首页-3天无理由介绍页
	 * @Grade 高级
	 * @FunctionPoint 首页-3天无理由介绍页，遍历点击品牌
	 * @author 
	 **/
	@Test
	public void test_1900_3TWLY_CAR(){
		reports_HomePageTestRN.startTest("test_1900_3TWLY_1");
		gotocate(1);
		wait(1);
		sliding("up");
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*6/7, width/2, height*1/7, 1000);
		wait(1);
		String[] str = {"上优信，放心购", "全国好车在线看", "好车才敢无理由"};
		reports_HomePageTestRN.log(LogStatus.INFO, "遍历点击3天无理由的品牌");
		System.out.println("遍历点击3天无理由的品牌");
		for(int i=1; i<4; i++) {
			String title = findElementByXpath("//*/android.view.ViewGroup[3]/android.view.ViewGroup[0"+i+"]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]").getText();
			System.out.println("title=="+title);
		    String des = findElementByXpath("//*/android.view.ViewGroup[3]/android.view.ViewGroup[0"+i+"]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[2]").getText();
			System.out.println("des>>"+des);
					if(des.equals(str[i-1])) {
				reports_HomePageTestRN.log(LogStatus.INFO, "3天无理由:"+title+"下面的文案是正确的。");
				System.out.println("3天无理由:"+title+"下面的文案是正确的。");
			}else {
				reports_HomePageTestRN.log(LogStatus.ERROR, "3天无理由:"+title+"下面的文案不正确，实际上显示为："+des);
				failAndMessage("3天无理由:"+title+"下面的文案不正确，为"+des);
			}
			findElementByXpath("//*/android.view.ViewGroup[3]/android.view.ViewGroup[0"+i+"]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]").click();
			reports_HomePageTestRN.log(LogStatus.INFO, "点击"+title);
			System.out.println("点击"+title);
			wait(1);
			if(findElementById("tv_pop_content").getText().equals(title)) { 
					reports_HomePageTestRN.log(LogStatus.INFO, "点击"+title+"进入的新列表页正常。");
				System.out.println("点击"+title+"进入的新列表页正常。");
				wait(1);
				findElementById("tvAge").click();
				wait(3);
				reports_HomePageTestRN.log(LogStatus.INFO, "进入车辆详情页");
				checkTitlebar_Detail();
	        	backBTN();
				driver.pressKeyCode(AndroidKeyCode.BACK);
				wait(1);
			}else {
				reports_HomePageTestRN.log(LogStatus.ERROR, "点击"+title+"进入的新列表页异常，请人工检查。");
				failAndMessage("点击"+title+"进入的新列表页异常，请人工检查。");
			}
		}		
	}
	
	/**
	 * @Name 1500_CNXH
	 * @Catalogue 首页-猜你喜欢
	 * @Grade 中级
	 * @FunctionPoint 首页-猜你喜欢，验证页面UI
	 **/
	@Test
	public void test_1500_CNXH() {
		reports_HomePageTestRN.startTest("test_1500_CNXH");
		gotocate(1);
		wait(2);
		clickElementByXpath("//*/android.widget.FrameLayout[3]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]");
		wait(2);
		if(CheckViewVisibilty(By.name("主人，小优还没有为您准备好车源"))&&
				CheckViewVisibilty(By.name("去车市"))) {
			reports_HomePageTestRN.log(LogStatus.INFO, "猜你喜欢暂无推荐车源。");
			System.out.println("猜你喜欢暂无推荐车源。");
			clickElementByName("去车市");
			if(findElementByName("买车").getAttribute("checked").equals("true")) {
				reports_HomePageTestRN.log(LogStatus.INFO, "点击猜你喜欢的去车市，进入到买车页。");
				System.out.println("点击猜你喜欢的去车市，进入到买车页。");
			}else {
				reports_HomePageTestRN.log(LogStatus.INFO, "点击猜你喜欢的去车市，没有进入到买车页，请检查。");
				failAndMessage("点击猜你喜欢的去车市，没有进入到买车页，请检查。");
			}
		}else if(CheckViewVisibiltyByName("猜你喜欢")&&
				CheckViewVisibilty(By.xpath("//android.widget.TextView[contains(@text,'款')]"))) {
			reports_HomePageTestRN.log(LogStatus.INFO, "猜你喜欢车辆信息显示正常！");
			System.out.println("猜你喜欢车辆信息显示正常！");
		}else {
			reports_HomePageTestRN.log(LogStatus.ERROR, "猜你喜欢车辆信息显示异常！");
			failAndMessage("猜你喜欢车辆信息显示异常！");
		}
	}
	
	/**
	 * @Name 1500_CNXH_XQ
	 * @Catalogue 首页-猜你喜欢
	 * @Grade 中级
	 * @FunctionPoint 首页-猜你喜欢，点击车辆，验证是否进入车辆详情页
	 **/
	@Test
	public void test_1500_CNXH_XQ() {
		reports_HomePageTestRN.startTest("test_1500_CNXH_XQ");
		gotocate(1);
		wait(2);
		clickElementByXpath("//*/android.widget.FrameLayout[3]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]");
		wait(1);
		if(CheckViewVisibilty(By.xpath("//android.widget.TextView[contains(@text,'款')]"))){
			String carName = findElementByXpath("//android.widget.TextView[contains(@text,'款')]").getText();
			System.out.println(carName);
			reports_HomePageTestRN.log(LogStatus.INFO, "点击猜你喜欢的某个车辆。");
			clickElementByName(carName);
			wait(2);
			String carDetailName = findElementById("tvVehicleDetailsCarName").getText();
			System.out.println(carDetailName);
			if(carDetailName.trim().equals(carName.trim())) {  //.trim()是为了去掉字符串首尾的空格
				reports_HomePageTestRN.log(LogStatus.INFO, "点击猜你喜欢的车辆进入车辆详情页。");
				System.out.println("点击猜你喜欢的车辆进入车辆详情页。");
			}else {
				reports_HomePageTestRN.log(LogStatus.ERROR, "点击猜你喜欢的车辆，进入车辆详情页异常。");
				failAndMessage("点击猜你喜欢的车辆，进入车辆详情页异常。");
			}
		}		
	}
	/**
	 * @Name 1500_CNXH_MORE
	 * @Catalogue 首页-猜你喜欢
	 * @Grade 中级
	 * @FunctionPoint 首页-猜你喜欢底部，点击查看更多车辆按钮，验证跳转页面
	 **/
	@Test
	public void test_1500_CNXH_MORE() {
		reports_HomePageTestRN.startTest("test_1500_CNXH_MORE");
		int i = 0;
		gotocate(1);
		wait(1);
		clickElementByXpath("//*/android.widget.FrameLayout[3]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]");
		wait(1);
		while(i < 3) {
			sliding("up");
			i = i + 1;
		}
		sleep(200);
		reports_HomePageTestRN.log(LogStatus.INFO, "先检查猜你喜欢是不是有推荐车源。");
		if(CheckViewVisibilty(By.name("查看更多车源"))){
			reports_HomePageTestRN.log(LogStatus.INFO, "点击查看更多车辆");
			clickElementByName("查看更多车源");
			wait(2);
			if(findElementByName("买车").getAttribute("checked").equals("true")){
	        	reports_HomePageTestRN.log(LogStatus.INFO, "首页，点击底部查看更多车辆按钮，进入买车tab页。");
	        	System.out.println("首页，点击底部查看更多车辆按钮，进入买车tab页。");
	        }else {
	        	GetScreenshot("test_1500_CNXH_MORE");
	        	reports_HomePageTestRN.log(LogStatus.ERROR, "首页，点击底部查看更多车辆按钮，没有进入买车tab页，请查看。");
	       	 	failAndMessage("首页，点击底部查看更多车辆按钮，没有进入买车tab页，请查看。");
			}
		}
	}
	
	/**
	 * @Name 1501_CNXH
	 * @Catalogue 首页-猜你喜欢
	 * @Grade 中级
	 * @FunctionPoint 首页-猜你喜欢，首屏点击首页tab之后跳转到猜你喜欢，再次点击，回到顶部
	 **/
	@Test
	public void test_1501_CNXH() {
		reports_HomePageTestRN.startTest("test_1501_CNXH");
		gotocate(1);
		sleep(200);
		clickElementByXpath("//*/android.widget.FrameLayout[3]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]");
		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页tab");
		wait(1);
		if (CheckViewVisibiltyByName("猜你喜欢")) {
			reports_HomePageTestRN.log(LogStatus.INFO, "点击首页tab跳转到猜你喜欢模块");
			System.out.println("点击首页tab跳转到猜你喜欢模块");
			clickElementByXpath("//*/android.widget.FrameLayout[3]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]");
			if(CheckViewVisibiltyByName("商城热卖")) {
				reports_HomePageTestRN.log(LogStatus.INFO, "点击回顶部tab跳转到首页顶部");
				System.out.println("点击回顶部tab跳转到首页顶部");
			}else {
				reports_HomePageTestRN.log(LogStatus.ERROR, "点击回顶部tab没有跳转到首页顶部，请人工检查");
				GetScreenshot("test_1501_CNXH");
				failAndMessage("点击回顶部tab没有跳转到首页顶部，请人工检查");
			}
		}else {
			reports_HomePageTestRN.log(LogStatus.ERROR, "点击悬浮按钮后跳转位置不正确");
			failAndMessage("点击悬浮按钮后跳转位置不正确");
		}
	}
	
	/**
	 * @Name 1501_CNXH_FFK_UI
	 * @Catalogue 首页-猜你喜欢负反馈
	 * @Grade 中级
	 * @FunctionPoint 首页-猜你喜欢负反馈，点击关闭按钮，查看弹窗的UI
	 **/
	@Test
	public void test_1501_CNXH_FFK_UI() {
		reports_HomePageTestRN.startTest("test_1501_CNXH_FFK_UI");
		gotocate(1);
		sleep(200);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页tab按钮");
		clickElementByXpath("//*/android.widget.FrameLayout[3]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击关闭按钮");
		clickElementByXpath("//android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[5]/android.widget.ImageView[1]");
		sleep(500);
		if(CheckViewVisibiltyByName("不感兴趣")) {
			reports_HomePageTestRN.log(LogStatus.INFO, "无勾选原因的时候，文案显示：不感兴趣。");
			System.out.println("无勾选原因的时候，文案显示：不感兴趣。");
		}else {
			reports_HomePageTestRN.log(LogStatus.ERROR, "无勾选原因的时候，文案显示的不是不感兴趣，请检查。");
			failAndMessage("无勾选原因的时候，文案显示的不是不感兴趣，请检查。");
		}
		reports_HomePageTestRN.log(LogStatus.INFO, "勾选车龄偏高时");
		clickElementByName("车龄偏高");
		if(CheckViewVisibiltyByName("确定")) {
			reports_HomePageTestRN.log(LogStatus.INFO, "已选1个理由的时候，文案显示：确定。");
			System.out.println("已选1个理由的时候，文案显示：确定。");
		}else {
			reports_HomePageTestRN.log(LogStatus.ERROR, "已选1个理由的时候，文案显示的不是确定，请检查。");
			failAndMessage("已选1个理由的时候，文案显示的不是确定，请检查。");
		}
	}
	
	/**
	 * @Name 1501_CNXH_FFK_DELETE
	 * @Catalogue 首页-猜你喜欢负反馈
	 * @Grade 中级
	 * @FunctionPoint 首页-猜你喜欢负反馈，点击关闭按钮，删除车辆
	 **/
	@Test
	public void test_1501_CNXH_FFK_DELETE() {
		reports_HomePageTestRN.startTest("test_1501_CNXH_FFK_DELETE");
		gotocate(1);
		sleep(200);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页tab按钮");
		clickElementByXpath("//*/android.widget.FrameLayout[3]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]");
		wait(1);
		String carName1 = findElementByXpath("//android.widget.TextView[contains(@text,'款')]").getText();
		reports_HomePageTestRN.log(LogStatus.INFO, "点击关闭按钮");
		clickElementByXpath("//android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[5]/android.widget.ImageView[1]");
		sleep(500);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击不感兴趣按钮");
		clickElementByName("不感兴趣");
		wait(2);
		String carName2 = findElementByXpath("//android.widget.TextView[contains(@text,'款')]").getText();
		if(carName1.equals(carName2)) {
			reports_HomePageTestRN.log(LogStatus.INFO, "猜你喜欢负反馈删除车辆失败，请检查！");
			failAndMessage("猜你喜欢负反馈删除车辆失败，请检查！");
		}else {
			reports_HomePageTestRN.log(LogStatus.INFO, "猜你喜欢负反馈删除车辆成功！");
			System.out.println("猜你喜欢负反馈删除车辆成功！");
		}
	}
	
	/**
	 * @Name 1501_CNXH_FFK_DELETE_LIMIT
	 * @Catalogue 首页-猜你喜欢负反馈
	 * @Grade 中级
	 * @FunctionPoint 首页-猜你喜欢负反馈，点击关闭按钮，删除车辆，最大值删除40辆车
	 **/
	@Test
	public void test_1501_CNXH_FFK_DELETE_LIMIT() {
		reports_HomePageTestRN.startTest("test_1501_CNXH_FFK_DELETE_LIMIT");
		gotocate(1);
		sleep(200);
		int i=0;
		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页tab按钮");
		clickElementByXpath("//*/android.widget.FrameLayout[3]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]");
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "遍历40次，点击关闭按钮，点击不感兴趣按钮");
		for(i=0; i<40; i++) {
			clickElementByXpath("//android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[5]/android.widget.ImageView[1]");
			sleep(500);
			reports_HomePageTestRN.log(LogStatus.INFO, "点击不感兴趣按钮");
			clickElementByName("不感兴趣");
			sleep(500);
			System.out.println(i);
		}
		if(!CheckViewVisibilty(By.xpath("//android.widget.TextView[contains(@text,'款')]"))) {
			reports_HomePageTestRN.log(LogStatus.INFO, "猜你喜欢负反馈，点击删除按钮，上限为40.");
			System.out.println("猜你喜欢负反馈，点击删除按钮，上限为40.");
		}else if(CheckViewVisibilty(By.id("ivDelete"))){
			System.out.println("i = " + i);
			GetScreenshot("test_1501_cnxh_num");
			reports_HomePageTestRN.log(LogStatus.INFO, "猜你喜欢的车辆数目超过40，请检查。");
			failAndMessage("猜你喜欢的车辆数目超过40，请检查。");
		}	
	}
	
	/**
	 * @Name 1100_KF
	 * @Catalogue 首页-客服按钮
	 * @Grade 高级
	 * @FunctionPoint 首页-客服按钮，点击客服按钮
	 **/
	@Test
	public void test_1100_KF() {
		reports_HomePageTestRN.startTest("test_1100_KF");
		gotocate(1);
		wait(1);
		clickElementByXpath("//*/android.widget.FrameLayout[3]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]");
		wait(2);
		reports_HomePageTestRN.log(LogStatus.INFO, "点击首页tab跳转到猜你喜欢模块");
		if(CheckViewVisibilty(By.xpath("//*/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[3]/android.widget.ImageView[1]"))) {
			reports_HomePageTestRN.log(LogStatus.INFO, "猜你喜欢模块有客服悬浮按钮");
			System.out.println("猜你喜欢模块有客服悬浮按钮");
			clickElementByXpath("//*/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[3]/android.widget.ImageView[1]");
			reports_HomePageTestRN.log(LogStatus.INFO, "点击客服悬浮按钮");
			wait(1);
			checkTitlebar1("优信二手车");
			reports_HomePageTestRN.log(LogStatus.INFO, "点击客服悬浮按钮进入到聊天页面");
			System.out.println("点击客服悬浮按钮进入到聊天页面");
		}else {
			reports_HomePageTestRN.log(LogStatus.ERROR, "猜你喜欢模块没有客服悬浮按钮，请检查");
			GetScreenshot("test_2100_KF");
			failAndMessage("猜你喜欢模块没有客服悬浮按钮，请检查");
		}
		
	}
	
	/**
	 * @Name 1501_list_top
	 * @Catalogue 首页一成购-新列表页-返回顶部按钮
	 * @Grade 中级
	 * @FunctionPoint 首页一成购-新列表页-返回顶部按钮
	 */
	@Test
	public void test_1501_list_top() {
		reports_BuyCarTest.startTest("test_1501_list_top");
		gotocate(1);
		wait(1);
		clickElementByName("一成购");
		wait(2);
		for (int i = 0; i < 4; i++) {
			sliding("up");
		}
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "列表页下滑3屏并且上滑一点");
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*5/8, width/2, height*6/8, 1000);
		wait(1);
		if(CheckViewVisibilty(By.id("iv_go_top"))) {
			clickElementById("iv_go_top");
			reports_BuyCarTest.log(LogStatus.INFO, "点击列表页返回顶部按钮");
			wait(1);
			if(!CheckViewVisibilty(By.id("iv_go_top"))
//					&&CheckViewVisibilty(By.id("lookupsimilarcar"))
					) {
				reports_BuyCarTest.log(LogStatus.INFO, "点击返回顶部按钮，返回到了顶部，功能正常！");
				System.out.println("点击返回顶部按钮，返回到了顶部，功能正常！");
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "列表页返回顶部按钮功能异常，请检查！");
				failAndMessage("列表页返回顶部按钮功能异常，请检查！");
			}
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "列表页没有返回顶部按钮，请检查！");
			failAndMessage("列表页没有返回顶部按钮，请检查！");
		}
	}
	
	/**
	 * @Name test_1100_SCRM
	 * @Catalogue 首页-文案检测 商城热卖
	 * @Grade 高级
	 * @FunctionPoint  首页-文案检测 商城热卖
	 **/
	@Test
	public void test_1100_WAJC() {
		reports_HomePageTestRN.startTest("test_1100_WAJC");
		gotocate(1);
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "检测商城热卖文案是否正确");
//		get_text(By.id("title"), "商城热卖");
		CheckViewVisibiltyByName("商城热卖");
		
	}
	/**
	 * @Name test_1100_xxCY
	 * @Catalogue 首页-文案检测 商城热卖-全部xxxx辆车源
	 * @Grade 高级
	 * @FunctionPoint  首页-文案检测 商城热卖-全部xxxx辆车源，判断车辆不等于0
	 **/
	@Test
	public void test_1100_xxCY() {
		reports_HomePageTestRN.startTest("test_1100_xxCY");
		gotocate(1);
		wait(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "检测文案包含全部 辆车源");
//		String str = findElementByXpath("//*/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.ScrollView[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[4]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]").getText();
		String str = findElementByXpath("//android.widget.TextView[contains(@text,'共有')]").getText();
		System.out.println(str);//全部
		int num =Integer.parseInt(str.substring(2, str.length()-3));//车辆数组
		String str1 = str.substring(str.length()-3);//辆车源
		if (str.contains("共有")&&str.contains(str1)) {
			System.out.println("文案检测成功");
		}else {
			System.out.println("文案检测失败，当前显示："+str);
			failAndMessage("文案检测失败，当前显示："+str);
		}
		if (num == 0) {
			reports_HomePageTestRN.log(LogStatus.ERROR, "检测文案包含全部 辆车源");
			failAndMessage("全部车源总数有问题，请人工检测");
		}else {
			System.out.println("全部车源显示正确>>>"+num);
			reports_HomePageTestRN.log(LogStatus.INFO, "全部车源显示正确>>>"+num);
		}
		
	}
	
	/**
	 * @Name 1102_RMCH
	 * @Catalogue 首页-热门车型文案检索
	 * @Grade 高级
	 * @FunctionPoint 首页-热门车型文案检索，遍历热门车型查看跳转是否正确
	 **/
	@Test
	public void test_1102_RMCH() {
		reports_HomePageTestRN.startTest("test_1102_RMCH");
		gotocate(1);
		reports_HomePageTestRN.log(LogStatus.INFO, "开始遍历热门车型");
		String[] str = {"SUV","MPV","三厢轿车","两厢轿车"};
		String[] listStrings = {"SUV","MPV","三厢轿车","两厢轿车"};
		for (int i = 1; i <= 4; i++) {
			if (CheckViewVisibiltyByName(str[i-1])) {
				clickElementByName(str[i-1]);
				wait(2);
				if (findElementById("tv_pop_content").getText().equals(listStrings[i-1])) {
					CheckNewListFromHomePage();
					toDetailFromNewList();
					checkTitlebar_Detail();
		        	backBTN();
					driver.pressKeyCode(AndroidKeyCode.BACK);
					sleep(500);
				}else {
					reports_HomePageTestRN.log(LogStatus.ERROR, "列表页筛选条件与预期不符，应为>"+listStrings[i-1]+"。实际为>"+findElementById("tv_pop_content").getText());
					failAndMessage("列表页筛选条件与预期不符，应为>"+listStrings[i-1]+"。实际为>"+findElementById("tv_pop_content").getText());
				}
			}else {
				reports_HomePageTestRN.log(LogStatus.ERROR, "与预期热门车型不一致，应为>"+str[i-1]);
				failAndMessage("与预期热门车型不一致，应为>"+str[i-1]);
			}
		}
	}
	
	/**
	 * @Name 1700_seach_IM
	 * @Catalogue 首页-搜索中间页的IM入口
	 * @Grade 高级
	 * @FunctionPoint 首页-搜索中间页的IM入口，点击IM进入客服聊天页面
	 **/
	@Test
	public void test_1700_seach_IM() {
		reports_HomePageTest.startTest("test_1700_seach_IM");
		gotocate(1);
		wait(1);
		WebElement shurukuang =findElementByXpath("//*/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.view.ViewGroup[2]/android.view.ViewGroup[1]/android.view.ViewGroup[1]/android.widget.TextView[1]");
		shurukuang.click();
		reports_HomePageTest.log(LogStatus.INFO, "点击搜索框");
//		clickElementById("tv_search");
		wait(2);
		if(CheckViewVisibilty(By.id("llChat"))) {
			clickElementById("llChat");
			wait(1);
			checkTitlebar1("优信二手车");
			if(CheckViewVisibilty(By.id("et_sendmessage"))&&
					CheckViewVisibilty(By.id("iv_face_normal"))&&
					CheckViewVisibilty(By.id("btn_more"))) {
				reports_HomePageTest.log(LogStatus.INFO, "点击搜索中间页的IM入口进入聊天页面");
				System.out.println("点击搜索中间页的IM入口进入聊天页面");
			}
		}else {
			reports_HomePageTest.log(LogStatus.ERROR, "当前搜索中间页无IM入口，请检查！");
			failAndMessage("当前搜索中间页无IM入口，请检查！");
		}
	}
	
	/**
	 * @Name 1101_more_brand_IM
	 * @Catalogue 首页-点击更多品牌-品牌筛选页-IM入口
	 * @Grade 高级
	 * @FunctionPoint 首页-点击更多品牌-品牌筛选页-IM入口，点击IM进入客服聊天页面
	 **/
	@Test
	public void test_1101_more_brand_IM() {
		reports_HomePageTest.startTest("test_1101_more_brand_IM");
		gotocate(1);
		wait(1);
		clickElementByName("更多");
		wait(1);
		reports_HomePageTest.log(LogStatus.INFO, "点击首页热门品牌处的更多按钮");
		checkTitlebar1("品牌导航");
		if(CheckViewVisibiltyByName("帮我找车")) {
			clickElementByName("帮我找车");
			wait(1);
			checkTitlebar1("优信二手车");
			if(CheckViewVisibilty(By.id("et_sendmessage"))&&
					CheckViewVisibilty(By.id("iv_face_normal"))&&
					CheckViewVisibilty(By.id("btn_more"))) {
				reports_BuyCarTest.log(LogStatus.INFO, "点击品牌导航栏页面的帮我找车按钮进入聊天页面");
				System.out.println("点击品牌导航栏页面的帮我找车按钮进入聊天页面");
				driver.pressKeyCode(AndroidKeyCode.BACK);
				wait(1);
			}
		}else {
			reports_BuyCarTest.log(LogStatus.INFO, "品牌导航栏页面没有帮我找车按钮。");
			failAndMessage("品牌导航栏页面没有帮我找车按钮。");
		}
		backBTN();
		reports_HomePageTest.log(LogStatus.INFO, "点击返回按钮");
		if(CheckViewVisibiltyByName("商城热卖")) {
			reports_HomePageTest.log(LogStatus.INFO, "点击导航栏的返回按钮返回到首页，逻辑正常");
			System.out.println("点击导航栏的返回按钮返回到首页，逻辑正常");
		}else {
			reports_HomePageTest.log(LogStatus.ERROR, "点击导航栏的返回按钮没有返回到首页，逻辑异常，请检查！");
			failAndMessage("点击导航栏的返回按钮没有返回到首页，逻辑异常，请检查！");
		}
	}
}
