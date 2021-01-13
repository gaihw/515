package com.uxin.usedcar.testCase;

import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Watchable;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.sound.midi.MidiDevice.Info;

import org.apache.commons.lang3.Validate;
import org.apache.http.conn.util.PublicSuffixList;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.handler.FindElement;
import org.openqa.selenium.remote.server.handler.FindElements;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs;
import org.seleniumhq.jetty9.server.Authentication.Failed;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.javascript.host.media.webkitMediaStream;
import com.relevantcodes.extentreports.LogStatus;
import com.sun.java.swing.plaf.windows.resources.windows;
import com.sun.mail.imap.protocol.ID;
import com.sun.org.apache.xpath.internal.operations.And;
import com.sun.scenario.effect.impl.prism.PrImage;
import com.uxin.usedcar.test.libs.BaseTest;
import com.uxin.usedcar.test.libs.CaseConfig;
import com.uxin.usedcar.test.libs.DateUtil;

@SuppressWarnings("unused")
public class BuyCarTest extends BaseTest  {
	@BeforeClass
	  public static void first() throws Exception {
		 reports_BuyCarTest.init("./report/BuyCar/reportBuyCar.html",true);
		
	  }

	@AfterClass
	public static void last(){
		System.out.println("*******************tearDown**************");
		reports_BuyCarTest.endTest();
	}
	
	/**
	 * @Name 2510_listRefresh
	 * @Subcatalog 买车-列表
	 * @FunctionPoint 买车-列表，刷新UI检查
	 */
	@Test
	public void test_2510_listRefresh() {
		reports_BuyCarTest.startTest("test_2510_listRefresh");
		gotocate(2);
		wait(1);
		sliding("","","","down","");
		reports_BuyCarTest.log(LogStatus.INFO, "刷新界面");
		reports_BuyCarTest.log(LogStatus.INFO, "遍历筛选栏");
		String[] str = {"智能排序","品牌","车价","筛选"};
		String[] id = {"tvSort","tvBrand","tvPrice","tv_filter"};
		for(int i=0; i<str.length;i++){
			if (findElementById(id[i]).getText().equals(str[i])) {
				System.out.println(findElementById(id[i]).getText());
			}else {
				failAndMessage(str[i]+"没有正常显示");
				reports_BuyCarTest.log(LogStatus.ERROR, str[i]+"没有正常显示");
				break;
			}
		}
	}
	
	
//	/**
//	 * @Name 2520_listChange
//	 * @Catalogue 买车-列表-视图切换
//	 * @Grade 高级
//	 * @FunctionPoint 买车-列表视图切换，切换到双列表视图检查UI后切换回默认视图
//	 */
//	@Test
//	public void test_2520_listChange(){
//		try {
//			reports_BuyCarTest.startTest("test_2520_listChange");
//			gotocate(2);
//			wait(2);
//			changeListShow("double");
//			reports_BuyCarTest.log(LogStatus.INFO, "点击筛选进入高级筛选切换至双列");
//			if (CheckViewVisibilty(By.id("root_left"))) {
//				reports_BuyCarTest.log(LogStatus.INFO, "切换双列成功");
//				System.out.println("切换双列成功");
//			}else {
//				reports_BuyCarTest.log(LogStatus.ERROR, "切换双列失败");
//				failAndMessage("切换双列失败");
//			}
//		} finally {
//			//还原车市页默认的单列
//			if (CheckViewVisibilty(By.id("root_left"))) {
//				changeListShow("single");
//			}
//		}
//		
//	}
	
	/**
	 * @Name test_2500_ZuJi
	 * @Catalogue 买车-列表-足迹
	 * @Grade 高级
	 * @FunctionPoint 买车-足迹，点击足迹查看浏览历史
	 */
	@Test
	public void test_2500_ZuJi() {
		reports_BuyCarTest.startTest("test_2500_ZuJi");
		gotocate(2);
		wait(1);
		if(findElementById("message").getText().equals("足迹")) {
			reports_BuyCarTest.log(LogStatus.INFO, "足迹文案显示正常");
			System.out.println("足迹文案显示正常");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "足迹文案显示不正确，目前显示为："+findElementById("message").getText());
			failAndMessage("足迹文案显示不正确，目前显示为："+findElementById("message").getText());
		}
		clickElementById("ivFootprintrl");
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "点击列表的足迹按钮");
		checkTitlebar1("浏览历史");
		if(CheckViewVisibilty(By.id("empty_msg"))) {
			reports_BuyCarTest.log(LogStatus.INFO, "浏览历史为空");
			System.out.println("浏览历史为空");
			clickElementById("empty_more");
			reports_BuyCarTest.log(LogStatus.INFO, "点击去看看车按钮");
			System.out.println("点击去看看车按钮");
			toDetail(1);
			backBTN();
			clickElementById("ivFootprintrl");
			reports_BuyCarTest.log(LogStatus.INFO, "再次点击列表的足迹按钮");
		}
		if(CheckViewVisibilty(By.id("rootLine"))&&         //整体
//				 CheckViewVisibilty(By.id("tvCityName"))&&   //车辆名称
				 CheckViewVisibilty(By.id("tvAge"))&&        //车龄
				 CheckViewVisibilty(By.id("tvMileage"))&&   //公里
				 CheckViewVisibilty(By.id("tvPrice"))&&     //车价
				 CheckViewVisibilty(By.id("ivItemPic"))) {  //图片
			 reports_MyTest.log(LogStatus.INFO, "浏览历史页面显示正常！");
			 System.out.println("浏览历史页面显示正常！");
		 }else {
			 reports_MyTest.log(LogStatus.ERROR, "浏览历史页面显示异常！");
			 GetScreenshot("test_2500_ZuJi");
			 failAndMessage("浏览历史页面显示异常！");
		 }
		 slidingInElement(findElementById("rootLine"), "left");
		 clickElementById("imgBtBack");
		 reports_MyTest.log(LogStatus.INFO, "浏览历史页面的车辆左滑删除成功！");
		 System.out.println("浏览历史页面的车辆左滑删除成功！");
	}
		 
	 /**
	 * @Name test_2500_ZuJi_compare_button
	 * @Catalogue 买车-列表-足迹
	 * @Grade 高级
	 * @FunctionPoint 买车-足迹-对比，对比按钮显示
	 */
	@Test
	public void test_2500_ZuJi_compare_button() {
		reports_BuyCarTest.startTest("test_2500_ZuJi_compare_button");
		gotocate(2);
		wait(1);
		clickElementById("ivFootprintrl");
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "点击列表的足迹按钮");
		checkTitlebar1("浏览历史");
		if(CheckViewVisibilty(By.id("empty_msg"))) {
			reports_BuyCarTest.log(LogStatus.INFO, "浏览历史为空");
			System.out.println("浏览历史为空");
			if(!CheckViewVisibilty(By.id("tv_right"))) {
				reports_BuyCarTest.log(LogStatus.INFO, "足迹-浏览历史为空的时候，没有对比按钮，显示正确。");
				System.out.println("足迹-浏览历史为空的时候，没有对比按钮，显示正确。");
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "足迹-浏览历史为空的时候，有对比按钮，显示异常，请检查。");
				failAndMessage("足迹-浏览历史为空的时候，有对比按钮，显示异常，请检查。");
			}
			clickElementById("empty_more");
			reports_BuyCarTest.log(LogStatus.INFO, "点击去看看车按钮");
			System.out.println("点击去看看车按钮");
			toDetail(1);
			backBTN();
		}
		WebElement parent = findElementById("android:id/list");
		List<WebElement> list = parent.findElements(By.id("tvCarWholeName"));
		int count = list.size();
		if(count == 1) {
			if(!CheckViewVisibilty(By.id("tv_right"))) {
				reports_BuyCarTest.log(LogStatus.INFO, "足迹-浏览历史有1辆车的时候，没有对比按钮，显示正确。");
				System.out.println("足迹-浏览历史有1辆车的时候，没有对比按钮，显示正确。");
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "足迹-浏览历史有1辆车的时候，有对比按钮，显示异常，请检查。");
				failAndMessage("足迹-浏览历史有1辆车的时候，有对比按钮，显示异常，请检查。");
			}
		}else {
			if(CheckViewVisibilty(By.id("tv_right"))) {
				reports_BuyCarTest.log(LogStatus.INFO, "足迹-浏览历史大于1辆车的时候，有对比按钮，显示正确。");
				System.out.println("足迹-浏览历史大于1辆车的时候，有对比按钮，显示正确。");
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "足迹-浏览历史大于1辆车的时候，没有对比按钮，显示异常，请检查。");
				failAndMessage("足迹-浏览历史大于1辆车的时候，没有对比按钮，显示异常，请检查。");
			}
		}
	}
	
	 /**
	 * @Name test_2500_ZuJi_compare
	 * @Catalogue 买车-列表-足迹
	 * @Grade 高级
	 * @FunctionPoint 买车-足迹-对比，开始对比按钮是否可用，勾选一辆车的时候按钮不可点击；勾选两辆车时按钮可点击；点击开始对比
	 */
	@Test
	public void test_2500_ZuJi_compare() {
		reports_BuyCarTest.startTest("test_2500_ZuJi_compare");
		gotocate(2);
		wait(1);
		clickElementById("ivFootprintrl");
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "点击列表的足迹按钮");
		checkTitlebar1("浏览历史");
		if(CheckViewVisibilty(By.id("empty_msg"))) {
			reports_BuyCarTest.log(LogStatus.INFO, "浏览历史为空");
			System.out.println("浏览历史为空");
			clickElementById("empty_more");
			reports_BuyCarTest.log(LogStatus.INFO, "点击去看看车按钮");
			System.out.println("点击去看看车按钮");
			toDetail(1);
			backBTN();
			toDetail(2);
			backBTN();
			clickElementById("ivFootprintrl");
			reports_BuyCarTest.log(LogStatus.INFO, "点击列表的足迹按钮");
			wait(1);
		}
		WebElement parent = findElementById("android:id/list");
		List<WebElement> list = parent.findElements(By.id("tvCarWholeName"));
		int count = list.size();
		if(count == 1) {
			backBTN();
			toDetail(2);
			backBTN();
			clickElementById("ivFootprintrl");
			reports_BuyCarTest.log(LogStatus.INFO, "点击列表的足迹按钮");
			wait(1);
		}
		clickElementById("tv_right");
		reports_BuyCarTest.log(LogStatus.INFO, "点击足迹浏览历史的对比按钮");
		List<WebElement> checkList = driver.findElements(By.id("iv_check"));
		checkList.get(0).click();
//		clickElementById("iv_check");
		reports_BuyCarTest.log(LogStatus.INFO, "勾选第一辆车");
		if(findElementById("tvCompare").getText().equals("选择1辆车开始对比")) {
			reports_BuyCarTest.log(LogStatus.INFO, "勾选一辆车的时候，按钮不可点击，显示正常");
			System.out.println("勾选一辆车的时候，按钮不可点击，显示正常");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "勾选一辆车的时候，按钮异常");
			failAndMessage("勾选一辆车的时候，按钮异常");
		}	
		checkList.get(1).click();
		reports_BuyCarTest.log(LogStatus.INFO, "勾选第2辆车");
		if(findElementById("tvCompare").getText().equals("开始对比")) {
			reports_BuyCarTest.log(LogStatus.INFO, "勾选2辆车的时候，按钮可点击，显示正常");
			System.out.println("勾选2辆车的时候，按钮可点击，显示正常");
			clickElementById("tvCompare");
			reports_BuyCarTest.log(LogStatus.INFO, "点击开始对比按钮");
			wait(2);
			checkTitlebar_Webview("参数对比");
			if(findElementById("tvTitle").getText().equals("参数对比")) {
				reports_BuyCarTest.log(LogStatus.INFO, "点击开始对比按钮，进入参数对比页面。");
				System.out.println("点击开始对比按钮，进入参数对比页面。");
			}else {
				failAndMessage("点击开始对比按钮没有进入参数对比页面，请检查。");
			}
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "勾选2辆车的时候，按钮异常");
			failAndMessage("勾选2辆车的时候，按钮异常");
		}
		
	}
		
	
	/**
	 * @Name test_2500_XQ_LLLS
	 * @Catalogue 买车-详情页下拉-浏览历史
	 * @Grade 高级
	 * @FunctionPoint 买车-详情页下拉-浏览历史
	 */
	@Test
	public void test_2500_XQ_LLLS() {
		reports_BuyCarTest.startTest("test_2500_XQ_LLLS");
		gotocate(2);
		wait(1);
		toDetail_half(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*2/5, width/2, height*4/5, 1000);
		wait(2);
//		sliding("","","","down","");
//		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "下拉刷新界面");
		if(CheckViewVisibilty(By.id("foot_title"))) {
			reports_MyTest.log(LogStatus.INFO, "详情页下拉刷新出现浏览历史足迹！");
			System.out.println("详情页下拉刷新出现浏览历史足迹！");
			if(CheckViewVisibilty(By.id("foot_nohis"))) {
				reports_MyTest.log(LogStatus.INFO, "详情页下拉刷新，提示还没有浏览历史！");
				System.out.println("详情页下拉刷新，提示还没有浏览历史！");
			}else if(findElementById("foot_title").getText().equals("浏览历史(1/1)")) {
//				int width = driver.manage().window().getSize().width;
//				int height = driver.manage().window().getSize().height;
//				driver.tap(1, width/2, height*2/3, 1);
				TouchAction action = new TouchAction(driver);
				action.tap(width/2, height*2/3).perform();
				reports_BuyCarTest.log(LogStatus.INFO, "关闭详情页的足迹");
				wait(1);
				backBTN();
				toDetail(2);
				backBTN();
				toDetail(3);
				sliding("","","","down","");
				reports_BuyCarTest.log(LogStatus.INFO, "下拉刷新界面");
			}else {
				String carName1 = findElementById("foot_carname").getText();
				slidingInElement(findElementById("foot_pager"), "left");
				String carName2 = findElementById("foot_carname").getText();
				if(!carName1.equals(carName2)) {
					reports_BuyCarTest.log(LogStatus.INFO, "足迹车辆左滑成功");
					System.out.println("足迹车辆左滑成功");
				}else {
					reports_BuyCarTest.log(LogStatus.INFO, "足迹车辆左滑失败");
					failAndMessage("足迹车辆左滑失败");
				}
//				int width = driver.manage().window().getSize().width;
//				int height = driver.manage().window().getSize().height;
//				driver.tap(1, width/2, height*2/3, 1);
				TouchAction action = new TouchAction(driver);
				action.tap(width/2, height*2/3).perform();
				reports_BuyCarTest.log(LogStatus.INFO, "关闭详情页的足迹");
			}
		}else {
			reports_MyTest.log(LogStatus.ERROR, "详情页下拉刷新没有出现浏览历史足迹！");
			GetScreenshot("test_2500_XQ_LLLS");
			failAndMessage("详情页下拉刷新没有出现浏览历史足迹！");
		}
	}
	
	/**
	 * @Name 2311_FQGXQ
	 * @Catalogue 买车-详情页-分期购详情页
	 * @Grade 高级
	 * @FunctionPoint 买车-详情页，设置类型筛选条件为分期购，点击第一辆车进入详情页后分期购查看跳转
	 */
	@Test
	public void test_2311_FQGXQ() {
		reports_BuyCarTest.startTest("test_2311_FQGXQ");
		gotocate(2);
		wait(2);
		clickFQG();
		reports_BuyCarTest.log(LogStatus.INFO, "进入高级筛选点击分期购按钮");
		wait(3);
		toDetail_half(1);
		checkTitlebar_Detail();
		clickElementById("tvDownPayment");
		wait(3);
		reports_BuyCarTest.log(LogStatus.INFO, "点击详情页的分期购介绍");
		System.out.println("点击详情页的分期购介绍");
		checkTitlebar_Webview("分期购");
		if(findElementById("tvTitle").getText().equals("分期购")) {
			
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "点击详情页的分期购介绍没有进入分期购页面，显示异常，请检查");
			failAndMessage("点击详情页的分期购介绍没有进入分期购页面，显示异常，请检查");
		}
	}
	
	/**
	 * @Name 2311_XQ_PZLD
	 * @Catalogue 买车-详情页
	 * @Grade 高级
	 * @FunctionPoint 买车-详情页，进入详情页后检查详情页配置亮点、参数配置的页面跳转
	 */
	@Test
	public void test_2311_XQ_PZLD(){
		reports_BuyCarTest.startTest("test_2311_XQ_PZLD");
		gotocate(2);
		wait(2);
		toDetail_half(1);
		wait(1);
		checkTitlebar_Detail();
		wait(2);
		sliding("","","","up","");
		wait(1);
//			int width = driver.manage().window().getSize().width;
//			int height = driver.manage().window().getSize().height;
//			driver.swipe(width/2, height*7/8, width/2, height*5/8, 1000);
//			wait(1);
		if(CheckViewVisibilty(By.id("llD"))) {
			clickElementById("llD");
			wait(2);
			checkTitlebar_Webview("配置亮点");
			backBTN();
			clickElementById("ll_check_detail");
			wait(2);
			reports_BuyCarTest.log(LogStatus.INFO, "检查详细参数配置");
			checkTitlebar_Webview("参数配置");
			if(CheckViewVisibilty(By.id("tvChat"))) {
				clickElementById("tvChat");
				wait(1);
				checkTitlebar1("优信二手车");
				clickElementById("tvCarWholeName");
				checkTitlebar_Detail();
				driver.pressKeyCode(AndroidKeyCode.BACK);
				if(CheckViewVisibilty(By.id("btn_set_mode_voice"))&&
						CheckViewVisibilty(By.id("et_sendmessage"))&&
						CheckViewVisibilty(By.id("rl_face"))&&
						CheckViewVisibilty(By.id("btn_more"))) {
					reports_BuyCarTest.log(LogStatus.INFO, "从参数配置的页面点击在线咨询聊天显示正常");
					System.out.println("从参数配置的页面点击在线咨询聊天显示正常");
				}
			}
		}else {
			reports_BuyCarTest.log(LogStatus.INFO, "当前车辆无配置亮点");
			System.out.println("当前车辆无配置亮点");
		}
	}
	
//	/**
//	 * @Name 2311_XQ_WYYH_PhoneNum
//	 * @Catalogue 买车-详情页
//	 * @Grade 高级
//	 * @FunctionPoint 买车-详情页，检查我要优惠-带入的默认手机号是目前登录的账号
//	 */
//	@Test
//	public void test_2311_XQ_WYYH_PhoneNum(){
//		reports_BuyCarTest.startTest("test_2311_XQ_WYYH_PhoneNum");
//		gotocate(2);
//		reports_BuyCarTest.log(LogStatus.INFO, "获取已登录账号的手机号");
//		String phoneNum = getLoginPhoneNum();
//		System.out.println(phoneNum);
//		gotoCateSet(1);
//		reports_BuyCarTest.log(LogStatus.INFO,"进入详情页");
//		toDetail(1);
//		wait(1);
//		checkTitlebar_Detail();
//		if ((findElementById("tvFocus").getText().equals("关注")||findElementById("tvFocus").getText().equals("已关注"))&&
//				findElementById("tvCenter").getText().equals("我要优惠")&&
//				findElementById("tvRight").getText().equals("电话客服")&&
//				findElementById("tvOnlineService").getText().equals("在线咨询")) {  
//			reports_BuyCarTest.log(LogStatus.INFO, "详情页-显示正常");
//			System.out.println("详情页-我要优惠显示正常");
//			reports_BuyCarTest.log(LogStatus.INFO, "点击我要优惠");
//			clickElementById("tvCenter");
//			wait(1);
//			String string = findElementById("tvPhone").getText();
//			if (string.subSequence(5, string.length()).equals(phoneNum)) {
//				System.out.println("显示正常");
//			}else {
//				reports_BuyCarTest.log(LogStatus.ERROR, "我要优惠没有显示默认登录的账号");
//				failAndMessage("我要优惠没有显示默认登录的账号");
//			}
//		}else {
//			reports_BuyCarTest.log(LogStatus.ERROR, "详情页显示异常");
//			GetScreenshot("test_2311_XQ_WYYH_PhoneNum");
//			failAndMessage("详情页显示异常");
//		}
//	} 
	
	/**
	 * @Name 2311_XQ_YXJPRZ
	 * @Catalogue 买车-详情页-优信金牌认证详情页
	 * @Grade 高级
	 * @FunctionPoint 买车-详情页，设置类型筛选条件为优信金牌认证，点击第一条帖子进入详情页后检查详情页检优信金牌认证模块的UI展示及功能
	 */
	@Test
	public void test_2311_XQ_YXJPRZ(){
		reports_BuyCarTest.startTest("test_2311_XQ_YXJPRZ");
		gotocate(2);
		wait(2);
		clickElementById("btAdvancedFilter");
		wait(1);
		checkTitlebar1("高级筛选");
		clickElementByName("优信服务");
		wait(1);
		clickElementByName("优信金牌认证");//优信金牌认证按钮
		reports_BuyCarTest.log(LogStatus.INFO, "筛选优信金牌认证");
		clickElementById("ll_search");//点击找到xx辆车
		wait(1);
		toDetail(1);
		reports_BuyCarTest.log(LogStatus.INFO,"进入详情页");
		checkTitlebar_Detail();
		wait(1);
		sliding("up");
		wait(1);
		clickElementByName("车图");
		sleep(500);
		reports_BuyCarTest.log(LogStatus.INFO, "点击车图导航栏");
		swipeUntilElement(By.id("authen_name"), "down", 2);  //优信金牌认证
		reports_BuyCarTest.log(LogStatus.INFO, "滑动到优信金牌认证卡片");
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		if(CheckViewVisibiltyByName("服务咨询")&&
				CheckViewVisibiltyByName("30天包退")&&        //30天包退
				CheckViewVisibiltyByName("一年保修")&&     //一年保修
				CheckViewVisibiltyByName("315项排查")&&     //315项排查
				CheckViewVisibiltyByName("全国联保")) {    //全国联保
			reports_BuyCarTest.log(LogStatus.INFO, "详情页优信金牌认证显示正常。");
			System.out.println("详情页优信金牌认证显示正常。");
			reports_BuyCarTest.log(LogStatus.INFO, "点击一年保修");
			clickElementByName("一年保修");
			clickElementById("baoxui_xitong_detail");
			wait(2);
			checkTitlebar_Webview("优信金牌认证质保范围");
			driver.pressKeyCode(AndroidKeyCode.BACK);
			sleep(500);
			driver.swipe(width/2, height*5/8, width/2, height*4/8, 1000);
			wait(1);
			clickElementById("baoxui_liucheng_detail");
			wait(2);
			checkTitlebar_Webview("售后保障");
			driver.pressKeyCode(AndroidKeyCode.BACK);
			sleep(500);
			reports_BuyCarTest.log(LogStatus.INFO, "点击30天包退");
			clickElementByName("30天包退");
			clickElementById("baotui_shigu_detail");
			wait(2);
			checkTitlebar_Webview("优信金牌认证质保范围");
			driver.pressKeyCode(AndroidKeyCode.BACK);
			sleep(500);
			clickElementById("baotui_tuiche_detail");
			wait(2);
			checkTitlebar_Webview("售后保障");
			driver.pressKeyCode(AndroidKeyCode.BACK);
		}else {
			reports_BuyCarTest.log(LogStatus.INFO, "没有找到优信金牌认证或者显示异常");
			failAndMessage("没有找到优信金牌认证或者显示异常");
		}
	}
	
	/**
	 * @Name 2311_XQ_YXYPRZ
	 * @Catalogue 买车-详情页-优信银牌认证详情页
	 * @Grade 高级
	 * @FunctionPoint 买车-详情页，设置类型筛选条件为优信银牌认证，点击第一条帖子进入详情页后检查详情页检优信银牌认证模块的UI展示及功能
	 */
	@Test
	public void test_2311_XQ_YXYPRZ(){
		reports_BuyCarTest.startTest("test_2311_XQ_YXYPRZ");
		gotocate(2);
		wait(2);
		clickElementById("btAdvancedFilter");
		wait(1);
		checkTitlebar1("高级筛选");
		clickElementByName("优信服务");
		wait(1);
		clickElementByName("优信银牌认证");//优信银牌认证按钮
		reports_BuyCarTest.log(LogStatus.INFO, "筛选优信银牌认证");
		clickElementById("ll_search");//点击找到xx辆车
		wait(1);
		toDetail(1);
		reports_BuyCarTest.log(LogStatus.INFO,"进入详情页");
		checkTitlebar_Detail();
		wait(1);
		sliding("up");
		wait(2);
		clickElementByName("车图");
		reports_BuyCarTest.log(LogStatus.INFO, "点击车图导航栏");
		sleep(500);
		swipeUntilElement(By.id("authen_name"), "down", 2);  //优信银牌认证
		reports_BuyCarTest.log(LogStatus.INFO, "上滑动到优信银牌认证卡片");
		wait(1);
//		int width = driver.manage().window().getSize().width;
//		int height = driver.manage().window().getSize().height;
		if(CheckViewVisibiltyByName("30天包退")&&        //30天包退
				CheckViewVisibiltyByName("6个月保修")&&     //6个月保修
				CheckViewVisibiltyByName("315项排查")&&     //315项排查
				CheckViewVisibiltyByName("全国联保")) {    //全国联保
			reports_BuyCarTest.log(LogStatus.INFO, "详情页优信银牌认证显示正常。");
			System.out.println("详情页优信银牌认证显示正常。");
			clickElementByName("6个月保修");
			reports_BuyCarTest.log(LogStatus.INFO, "点击6个月保修");
			clickElementById("baoxui_xitong_detail");
			wait(2);
			checkTitlebar_Webview("优信银牌认证质保范围");
			driver.pressKeyCode(AndroidKeyCode.BACK);
			sleep(500);
			clickElementById("baoxui_liucheng_detail");
			wait(2);
			checkTitlebar_Webview("售后保障");
			driver.pressKeyCode(AndroidKeyCode.BACK);
			sleep(500);
			reports_BuyCarTest.log(LogStatus.INFO, "点击30天包退");
			clickElementByName("30天包退");
			clickElementById("baotui_shigu_detail");
			wait(2);
			checkTitlebar_Webview("优信银牌认证质保范围");
			driver.pressKeyCode(AndroidKeyCode.BACK);
			sleep(500);
			clickElementById("baotui_tuiche_detail");
			wait(2);
			checkTitlebar_Webview("售后保障");
			driver.pressKeyCode(AndroidKeyCode.BACK);
		}else {
			reports_BuyCarTest.log(LogStatus.INFO, "没有找到优信银牌认证或者显示异常");
			failAndMessage("没有找到优信银牌认证或者显示异常");
		}
	}
	
	/**
	 * @Name 2312_typeYXJPRZ
	 * @Catalogue 买车-列表
	 * @Grade 高级
	 * @FunctionPoint 买车-列表类型切换检查，设置类型筛选为“优信金牌认证”检查列表UI显示，并点击任意itme进入详情页
	 */
	@Test
	public void test_2312_typeYXJPRZ(){
		reports_BuyCarTest.startTest("test_2312_typeYXJPRZ");
		gotocate(2);
		clickElementById("btAdvancedFilter");
		wait(2);
		checkTitlebar1("高级筛选");
//		clickElementByName("优信服务");
		clickElementByName("优信金牌认证");
		reports_BuyCarTest.log(LogStatus.INFO, "筛选优信金牌认证");
		clickElementById("ll_search");//点击找到xx辆车
		wait(3);
		if (findElementById("tvUxin").getText().equals("优信金牌认证")
//				findElementById("tvUxinShowText").getText().equals("30天包退  一年保修")
				) {			
			reports_BuyCarTest.log(LogStatus.INFO,"进入详情页");
			toDetail(1);
			checkTitlebar_Detail();
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "筛选优信金牌认证失败");
			GetScreenshot("test_2312_typeYXRZ");
			failAndMessage("筛选优信金牌认证失败");
		}
	}
	
	/**
	 * @Name 2312_typeYXYPRZ
	 * @Catalogue 买车-列表
	 * @Grade 高级
	 * @FunctionPoint 买车-列表类型切换检查，设置类型筛选为“优信银牌认证”检查列表UI显示，并点击任意itme进入详情页
	 */
	@Test
	public void test_2312_typeYXYPRZ(){
		reports_BuyCarTest.startTest("test_2312_typeYXYPRZ");
		gotocate(2);
		clickElementById("btAdvancedFilter");
		wait(2);
		checkTitlebar1("高级筛选");
//		clickElementByName("优信服务");
		clickElementByName("优信银牌认证");
		reports_BuyCarTest.log(LogStatus.INFO, "筛选优信银牌认证");
		clickElementById("ll_search");//点击找到xx辆车
		wait(3);
		if (findElementById("tvUxin").getText().equals("优信银牌认证")&&
				findElementById("tvUxinShowText").getText().equals("30天包退  6个月保修")) {			
			reports_BuyCarTest.log(LogStatus.INFO,"进入详情页");
			toDetail(1);
			checkTitlebar_Detail();
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "筛选优信银牌认证失败");
			GetScreenshot("test_2312_typeYXRZ");
			failAndMessage("筛选优信银牌认证失败");
		}
	}
	
	/**
	 * @Name 2321_brand
	 * @Catalogue 买车-列表
	 * @Grade 高级
	 * @FunctionPoint 买车-品牌筛选项，点击品牌筛选项，选择“奥迪A4L”后跳转到列表页查看列表页显示
	 */
	@Test
	public void test_2321_brand(){
		reports_BuyCarTest.startTest("test_2321_brand");
		gotocate(2);
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "筛选奥迪A4L");
		clickElementById("linLayBrand");
		wait(1);
		clickElementByName("奥迪");
		clickElementByName("A4L");
//		WebElement ptrListViewMarket = findElementById("ptrListViewMarket");
//		List<WebElement> items = ptrListViewMarket.findElements(By.id("tvSearchHistory"));
//		if (items.get(0).getAttribute("text").equals("奥迪")&&
		if(findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
				+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
				+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
				+ "/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]"
				+ "/android.view.ViewGroup[1]/android.widget.LinearLayout[1]"
				+ "/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]"
				+ "/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]"
				+ "/android.widget.HorizontalScrollView[1]/android.widget.LinearLayout[1]"
				+ "/android.widget.LinearLayout[1]"
				+ "/android.widget.TextView[1]").getText().equals("奥迪")&&
				findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
						+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
						+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
						+ "/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]"
						+ "/android.view.ViewGroup[1]/android.widget.LinearLayout[1]"
						+ "/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]"
						+ "/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]"
						+ "/android.widget.HorizontalScrollView[1]/android.widget.LinearLayout[1]"
						+ "/android.widget.LinearLayout[2]/android.widget.TextView[1]").getText().equals("A4L")) {
			toDetail(1);
			checkTitlebar_Detail();
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR,"筛选‘奥迪A4L’失败，请人工检查");
			GetScreenshot("test_2321_brand");
			failAndMessage("筛选‘奥迪A4L’失败，请人工检查");
		}
	}
	
	/**
	 * @Name 2331_SortJGZD
	 * @Catalogue 买车-列表
	 * @Grade 高级
	 * @FunctionPoint 买车-列表排序方式切换检查，设置排序为“车价最低”检查列表UI显示，并点击任意itme进入详情页
	 */
	@Test
	public void test_2331_SortJGZD(){
		reports_BuyCarTest.startTest("test_2331_SortJGZD");
		gotocate(2);
		wait(2);
		clickElementById("linLaySort");
		wait(2);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		TouchAction action = new TouchAction(driver);
		action.tap(width/2, height*5/12).perform();
//		clickElementByName("车价最低");
		reports_BuyCarTest.log(LogStatus.INFO, "筛选车价最低");
		wait(2);
		List<WebElement> titleList = driver.findElementsById("tvCarWholeName");
		List<WebElement> priceList = driver.findElementsById("tvPrice");
		String t1 = titleList.get(0).getText();
		System.out.println(t1);
		String price1 = priceList.get(1).getText().substring(0, 4);
		System.out.println(price1);
		String t2 = titleList.get(1).getText();
		System.out.println(t2);
		String price2 = priceList.get(2).getText().substring(0, 4);
		System.out.println(price2);
		Double p1 = Double.parseDouble(price1);
		Double p2 = Double.parseDouble(price2);
		reports_BuyCarTest.log(LogStatus.INFO, "第一项标题及车价"+t1+"**"+p1);
		System.out.println(t1+"**"+p1);
		reports_BuyCarTest.log(LogStatus.INFO, "第二项标题及车价"+t2+"**"+p2);
		System.out.println(t2+"**"+p2);
		if (p1<=p2) {
			toDetail(1);
			checkTitlebar_Detail();
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "筛选条件<车价最低>没有生效，请人工检查");
			GetScreenshot("test_2331_SortJGZD");
			failAndMessage("筛选条件<车价最低>没有生效，请人工检查");
		}
	}
	
	/**
	 * @Name 2332_SortJGZG
	 * @Catalogue 买车-列表
	 * @Grade 高级
	 * @FunctionPoint 买车-列表排序方式切换检查,设置排序为“车价最高”检查列表UI显示，并点击任意itme进入详情页
	 */
	@Test
	public void test_2332_SortJGZG(){
		reports_BuyCarTest.startTest("test_2332_SortJGZG");
		gotocate(2);
		wait(2);
		clickElementById("linLaySort");
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		TouchAction action = new TouchAction(driver);
		action.tap(width/2, height*11/24).perform();
//		clickElementByName("车价最高");
		reports_BuyCarTest.log(LogStatus.INFO, "筛选车价最低高");
		wait(2);
		List<WebElement> titleList = driver.findElementsById("tvCarWholeName");
		List<WebElement> priceList = driver.findElementsById("tvPrice");
		
		String t1 = titleList.get(0).getText();
		String price1 = priceList.get(1).getText().substring(0, 6);
		String t2 = titleList.get(1).getText();
		String price2 = priceList.get(2).getText().substring(0, 6);
		Double p1 = Double.parseDouble(price1);
		Double p2 = Double.parseDouble(price2);
		reports_BuyCarTest.log(LogStatus.INFO, "第一项标题及车价"+t1+"**"+p1);
		System.out.println(t1+"**"+p1);
		reports_BuyCarTest.log(LogStatus.INFO, "第二项标题及车价"+t2+"**"+p2);
		System.out.println(t2+"**"+p2);
		if (p1>=p2) {
			toDetail(1);
			checkTitlebar_Detail();
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "筛选条件<车价最高>没有生效，请人工检查");
			GetScreenshot("test_2332_SortJGZG");
			failAndMessage("筛选条件<车价最高>没有生效，请人工检查");
		}
	}
	
	/**
	 * @Name 2333_SortCLZD
	 * @Catalogue 买车-列表
	 * @Grade 高级
	 * @FunctionPoint 买车-列表排序方式切换检查，设置排序为“车龄最短”检查列表UI显示，并点击任意itme进入详情页
	 */
	@Test
	public void test_2333_SortCLZD(){
		reports_BuyCarTest.startTest("test_2333_SortCLZD");
		gotocate(2);
		wait(2);
		clickElementById("linLaySort");
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		TouchAction action = new TouchAction(driver);
		action.tap(width/2, height*13/24).perform();
//		clickElementByName("车龄最短");
		reports_BuyCarTest.log(LogStatus.INFO, "车龄最短");
		wait(2);
		toDetail(1);
		checkTitlebar_Detail();
		wait(1);
		driver.swipe(width/2, height*8/10, width/2, height*3/10, 1000);
		String first=getCarDate();
		clickElementById("imgBtBack");
		wait(2);
		toDetail(2);
		checkTitlebar_Detail();
		wait(1);
		driver.swipe(width/2, height*8/10, width/2, height*3/10, 1000);
		String second = getCarDate();
		if (DateUtil.compareDate(first, second)) {
			reports_BuyCarTest.log(LogStatus.INFO, "筛选条件<车龄最短>生效");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "筛选条件<车龄最短>没有生效，请人工检查");
			GetScreenshot("test_2333_SortCLZD");
			failAndMessage("筛选条件<车龄最短>没有生效，请人工检查");
		}
	}

	
	/**
	 * @Name 2324_SortLCZS
	 * @Catalogue 买车-列表
	 * @Grade 高级
	 * @FunctionPoint 买车-列表排序方式切换检查，设置排序为“里程最少”检查列表UI显示，并点击任意itme进入详情页
	 */
	@Test
	public void test_2334_SortLCZS(){
		reports_BuyCarTest.startTest("test_2334_SortLCZS");
		gotocate(2);
		wait(2);
		clickElementById("linLaySort");
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		TouchAction action = new TouchAction(driver);
		action.tap(width/2, height*15/24).perform();
//		clickElementByName("里程最少");
		reports_BuyCarTest.log(LogStatus.INFO, "里程最少");
		wait(2);
		List<WebElement> mileList = driver.findElementsById("tvMileage");
		String lc1=mileList.get(0).getText().substring(2, 6);
		String lc2=mileList.get(1).getText().substring(2, 6);
		System.out.println(lc1+"---------"+lc2);
		Double lc_1 = Double.parseDouble(lc1);
		Double lc_2 = Double.parseDouble(lc2);
		System.out.println(lc_1+"**"+lc_2);
		if (lc_1<=lc_2) {
			reports_BuyCarTest.log(LogStatus.INFO, "筛选里程最少成功，进入详情页");
			toDetail(1);
//			checkTitlebar_Detail();
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "筛选条件<里程最少>没有生效，请人工检查");
			GetScreenshot("test_2334_SortLCZS");
			failAndMessage("筛选条件<里程最少>没有生效，请人工检查");
		}
		
	}
	
	/**
	 * @Name 2335_SortSFZD
	 * @Catalogue 买车-列表
	 * @Grade 高级
	 * @FunctionPoint 买车-列表排序方式切换检查，设置排序为“首付最低”检查列表UI显示，并点击任意itme进入详情页
	 */
	@Test
	public void test_2335_SortSFZD(){
		reports_BuyCarTest.startTest("test_2335_SortSFZD");
		gotocate(2);
		wait(2);
		clickElementById("linLaySort");
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		TouchAction action = new TouchAction(driver);
		action.tap(width/2, height*2/3).perform();
		reports_BuyCarTest.log(LogStatus.INFO, "首付最低");
		wait(2);
		List<WebElement> halfPriceList = driver.findElementsById("tvHalfPrice");
		String p1_str=halfPriceList.get(0).getText();
		String p1 = p1_str.substring(2, p1_str.length()-1);
		String p2_str=halfPriceList.get(1).getText();
		String p2 = p2_str.substring(2, p2_str.length()-1);
		System.out.println(p1+"---------"+p2);
		Double p_1 = Double.parseDouble(p1);
		Double p_2 = Double.parseDouble(p2);
		System.out.println(p_1+"**"+p_2);
		if (p_1<=p_2) {
			reports_BuyCarTest.log(LogStatus.INFO, "筛选首付最低成功，进入详情页");
			toDetail(1);
//			checkTitlebar_Detail();
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "筛选条件<首付最低>没有生效，请人工检查");
			GetScreenshot("test_2335_SortSFZD");
			failAndMessage("筛选条件<首付最低>没有生效，请人工检查");
		}
		
	}
	
	/**
	 * @Name 2336_SortYGZS
	 * @Catalogue 买车-列表
	 * @Grade 高级
	 * @FunctionPoint 买车-列表排序方式切换检查，设置排序为“月供最少”检查列表UI显示，并点击任意itme进入详情页
	 */
	@Test
	public void test_2336_SortYGZS(){
		reports_BuyCarTest.startTest("test_2336_SortYGZS");
		gotocate(2);
		wait(2);
		clickElementById("linLaySort");
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		TouchAction action = new TouchAction(driver);
		action.tap(width/2, height*3/4).perform();
		reports_BuyCarTest.log(LogStatus.INFO, "月供最少");
		wait(2);
		List<WebElement> monthlyPriceList = driver.findElementsById("tvmonthlyprice");
		String mp1_str=monthlyPriceList.get(0).getText();
		String mp1 = mp1_str.substring(2, mp1_str.length()-1);
		String mp2_str=monthlyPriceList.get(1).getText();
		String mp2 = mp2_str.substring(2, mp2_str.length()-1);
		System.out.println(mp1+"---------"+mp2);
		Double mp_1 = Double.parseDouble(mp1);
		Double mp_2 = Double.parseDouble(mp2);
		System.out.println(mp_1+"**"+mp_2);
		if (mp_1<=mp_2) {
			reports_BuyCarTest.log(LogStatus.INFO, "筛选月供最少成功，进入详情页");
			toDetail(1);
//			checkTitlebar_Detail();
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "筛选条件<月供最少>没有生效，请人工检查");
			GetScreenshot("test_2336_SortYGZS");
			failAndMessage("筛选条件<月供最少>没有生效，请人工检查");
		}
		
	}
	
	/**
	 * @Name 2325_SortZXFB
	 * @Catalogue 买车-列表
	 * @Grade 高级
	 * @FunctionPoint 买车-列表排序方式切换检查，设置排序为“最新发布”检查列表UI显示，并点击任意itme进入详情页
	 */
//	@Test
//	public void test_2335_SortZXFB(){
//		reports_BuyCarTest.startTest("test_2335_SortZXFB");
//		gotocate(2);
//		wait(2);
//		reports_BuyCarTest.log(LogStatus.INFO, "最新发布");
//		clickElementById("linLaySort");
//		wait(1);
//		clickElementByName("最新发布");
//		wait(2);
////		toDetail(1);
////		sliding("up");
////		String time = findElementById("tvVehicleDetailsPublishDate").getText();
//		if (CheckViewVisibilty(By.id("ivNewArrival"))) {
//			reports_BuyCarTest.log(LogStatus.INFO, "筛选最新发布成功");
//		}else {
//			reports_BuyCarTest.log(LogStatus.ERROR, "筛选条件<最新发布>没有新上架的车辆，请人工审查");
//			GetScreenshot("test_2335_SortZXFB");
//			failAndMessage("筛选条件<最新发布>没有新上架的车辆，请人工审查");
//		}
//	}
	
	/**
	 * @Name 2501_QGZGXQ
	 * @Catalogue 详情页-对比清单模块
	 * @Grade 高级
	 * @FunctionPoint 详情页-对比清单模块
	 */
	@Test
	public void test_2501_QGZGXQ() {
		reports_BuyCarTest.startTest("test_2501_QGZGXQ");
		gotocate(2);
		toDetail_half(1);//为了后面在对比页面有选择对比的车辆
		wait(3);
		driver.pressKeyCode(AndroidKeyCode.BACK);
		wait(3);
		toDetail_half(2);
		wait(3);
		String carName = findElementById("tvVehicleDetailsCarName").getText();
		System.out.println(carName);
		reports_BuyCarTest.log(LogStatus.INFO, "检查车辆对比功能");
		clickElementById("ivCompare");
		wait(1);
		checkTitlebar1("对比清单");
		String name = findElementById("tv_compare_car_name_left").getText();
	   System.out.println(name);
		if (carName.trim().contains(name.trim())) {
			waitForVisible(By.id("rb_history"), 3);
//			waitForVisible(By.id("rb_collection"), 3);
//			clickElementById("iv_check");//选择浏览的车中的第二辆对比
			List<WebElement> checkList = driver.findElements(By.id("iv_check"));
			checkList.get(1).click();//选择浏览的车中的第二辆对比
			sleep(500);
			clickElementByName("开始对比");
			wait(3);
			checkTitlebar_Webview("参数对比");
			System.out.println("详情："+carName+",对比清单列表:"+name);
		}else {
			reports_BuyCarTest.log(LogStatus.INFO, "添加对比失败");
			GetScreenshot("test_2501_QGZGXQ对比清单列表页");
			failAndMessage("添加对比失败");
		}		
	}
		
	/**
	 * @Name 2704_search_1
	 * @Catalogue 搜索栏的交互
	 * @Grade 高级
	 * @FunctionPoint 点击搜索，选择热门搜索中的”宝马"，查看列表页显示
	 */
	@Test
	public void test_2704_search_1() {
		reports_BuyCarTest.startTest("test_2704_search_1");
		gotocate(2);
		wait(2);
		clickElementById("tv_search");
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "搜索宝马");
		input("id", "etSearchText", "", "宝马", "");
		driver.pressKeyCode(AndroidKeyCode.ENTER);
		wait(1);
		if (waitForVisible(By.id("tv_pop_content"), 3).getAttribute("text").equals("宝马")) {
			System.out.println("搜索宝马成功");
			reports_BuyCarTest.log(LogStatus.INFO, "搜索宝马成功");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "搜索宝马后进入的列表页错误");
			GetScreenshot("test_2501_FYBSS");
			failAndMessage("搜索宝马后进入的列表页错误");
		}
	}
	
	/**
	 * @Name 2502_price_brand
	 * @Catalogue 筛选栏的交互
	 * @Grade 高级
	 * @FunctionPoint 选取品牌“奥迪-不限车系”，选取车价区间"15-20万"，校验第一辆车是否符合规则
	 */
	@Test
	public void test_2502_price_brand(){
		reports_BuyCarTest.startTest("test_2502_price_brand");
		gotocate(2);
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "筛选奥迪-不限");
		clickElementByName("品牌");
		wait(2);
		clickElementByName("奥迪");
		wait(1);
		clickElementByName("不限车系");
		wait(2);
//		WebElement ptrListViewMarket = findElementById("ptrListViewMarket");
//		List<WebElement> items = ptrListViewMarket.findElements(By.id("tvSearchHistory"));
//		if (items.get(0).getAttribute("text").equals("奥迪")) 
		if (findElementById("tv_pop_content").getText().equals("奥迪")){
			reports_BuyCarTest.log(LogStatus.INFO, "筛选车价15-20万");
			clickElementByName("车价");
			int width = driver.manage().window().getSize().width;
			int height = driver.manage().window().getSize().height;
			TouchAction action = new TouchAction(driver);
			action.tap(width*1/2, height*5/12).perform();//15-20万
//			clickElementByName("15-20万");
			wait(2);
//			items = ptrListViewMarket.findElements(By.id("tvSearchHistory"));
//			System.out.println(items.size());
//			if (items.get(1).getAttribute("text").equals("15-20万元")) 
			if (findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
					+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
					+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
					+ "/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]"
					+ "/android.view.ViewGroup[1]/android.widget.LinearLayout[1]"
					+ "/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]"
					+ "/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]"
					+ "/android.widget.HorizontalScrollView[1]/android.widget.LinearLayout[1]"
					+ "/android.widget.LinearLayout[2]/android.widget.TextView[1]").getText().equals("15-20万元")){
				reports_BuyCarTest.log(LogStatus.INFO, "筛选奥迪-不限，15-20万成功");
				System.out.println("筛选奥迪不限，15-20万成功");
				clickElementById("tvCarWholeName");
				wait(2);
				if (findElementById("tvVehicleDetailsCarName").getText().contains("奥迪")) {
					String price = findElementById("tvVehicleDetailsPrice").getText().substring(0, 5);
					System.out.println(price);
					Double carPrice = Double.parseDouble(price);
					if (15.00<=carPrice&&carPrice<=20.00) {
						System.out.println("车辆名称和车价验证通过");
					}else {
						failAndMessage("车价不符合筛选");
						reports_BuyCarTest.log(LogStatus.ERROR, "车价不是15-20万");
					}
				}else {
					failAndMessage("车辆名称不符合筛选");
					reports_BuyCarTest.log(LogStatus.ERROR, "筛选奥迪失败，车辆名称中不包括奥迪");
				}
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "筛选车价15-20万失败");
				GetScreenshot("test_2502_FYBSXL筛选车价");
				failAndMessage("筛选车价15-20万失败");
			}
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "筛选品牌奥迪失败");
			GetScreenshot("test_2502_FYBSXL筛选品牌");
			failAndMessage("筛选品牌奥迪失败");
		}
	}
	
	/**
	 * @Name 2401_FYC
	 * @Catalogue 新车付一成
	 * @Grade 高级
	 * @FunctionPoint 新车付一成，筛选奥迪-不限检查新车付一成入口
	 */
	@Test
	public void test_2401_FYC() {
		reports_BuyCarTest.startTest("test_2401_FYC");
		gotocate(2);
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "筛选奥迪-不限");
		clickElementByName("品牌");
		wait(2);
		clickElementByName("奥迪");
		wait(1);
		clickElementByName("不限车系");
		wait(2);
		swipeUntilElementAppear(By.id("ivZhiGouTag"), "up", 2);
		reports_BuyCarTest.log(LogStatus.INFO, "滑动到付一成图标处");
		wait(1);
		if(findElementById("ivZhiGouTag").getText().equals("付一成")) {
			reports_BuyCarTest.log(LogStatus.INFO, "有新车付一成入口");
			System.out.println("有新车付一成入口");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "没有新车付一成入口");
			failAndMessage("没有新车付一成入口");
		}
	}
	
	/**
	 * @Name 2401_FYC_List
	 * @Catalogue 新车付一成
	 * @Grade 高级
	 * @FunctionPoint 新车付一成，筛选奥迪-不限检查新车付一成入口，并点击进入直租列表检查列表UI
	 */
	@Test
	public void test_2401_FYC_List(){
		reports_BuyCarTest.startTest("test_2401_FYC_List");
		gotocate(2);
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "筛选奥迪-不限");
		clickElementByName("品牌");
		wait(2);
		clickElementByName("奥迪");
		wait(1);
		clickElementByName("不限车系");
		wait(2);
		swipeUntilElementAppear(By.id("ivZhiGouTag"), "up", 2);
		reports_BuyCarTest.log(LogStatus.INFO, "滑动到付一成图标处");
		wait(1);
		clickElementById("ivZhiGouTag");
		reports_BuyCarTest.log(LogStatus.INFO, "进入直购列表页");
		wait(1);
		checkTitlebar_Webview("买好车，一成就购");
		wait(3);
//		try {
//			switchToWebView();
//		} catch (Exception e) {
//			System.out.println("第一次切换失败尝试再次切换");
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
//			clickElementById("ivZhiGouTag");
//			wait(3);
//			switchToWebView();
//		}
//		String brand = driver.findElementByXPath("//*[@id='root']/div/div[1]/div[3]/div[2]/i").getText();
//		System.out.println("brand="+brand);
//		String carName= driver.findElementByXPath("//*[@id='root']/div/div[4]/div/div[2]/h2").getText();
//		System.out.println("carName="+carName);
//		if (brand.equals("奥迪")) {
//			if (carName.contains("奥迪")) {
//				System.out.println(carName);
//			}else {
//				failAndMessage("列表页车辆名称显示的不是标致-不限");
//				reports_BuyCarTest.log(LogStatus.ERROR, "列表页车辆名称显示的不是标致-不限");
//			}
//		}else {
//			failAndMessage("付一成列表页品牌显示错误应为'标致',实际为："+brand);
//			reports_BuyCarTest.log(LogStatus.INFO, "付一成列表页品牌显示错误应为'标致',实际为："+brand);
//		}
	}
	
	
	/**
	 * @Name 2500_BWMC
	 * @Catalogue 买车-列表页-帮我买车
	 * @Grade 中级
	 * @FunctionPoint 买车-列表页-帮我买车，检查帮我买的UI
	 **/
	@Test
	public void test_2500_BWMC() { 
		reports_BuyCarTest.startTest("test_2500_BWMC");
		gotocate(2);
		wait(3);
		reports_BuyCarTest.log(LogStatus.INFO, "滑动列表到底部");
		swipeUntilElementAppear(By.id("bt_post_wishlist"), "up", 12);
		wait(3);
		reports_BuyCarTest.log(LogStatus.INFO, "查看当前页面是否有帮我找车模块，找到后点击帮我找车按钮。");
		clickElementByName("帮我找车");
		reports_BuyCarTest.log(LogStatus.INFO, "点击帮我找车按钮。");
		checkTitlebar1("帮我找车");
		if(CheckViewVisibilty(By.id("vgContainer"))) {
			clickElementById("bt_submit");//帮我找车
			toastCheck("请选择您喜欢的车型");
			clickElementByName("请选择爱车");
			wait(2);
			clickElementByName("奥迪");
			clickElementByName("A4");
			clickElementById("bt_submit");//帮我找车
			toastCheck("请输入购车预算");
		}else {
			reports_BuyCarTest.log(LogStatus.INFO, "没有找到帮我找车。");
			System.out.println("没有找到帮我找车。");
		}
	}
	
	/**
	 * @Name 2500_BWZC
	 * @Catalogue 买车-列表页-帮我买车
	 * @Grade 中级
	 * @FunctionPoint 买车-列表页-帮我买车，检查帮我买的UI
	 **/
	@Test
	public void test_2500_BWZC() {
		reports_BuyCarTest.startTest("test_2500_BWZC");
		gotocate(2);
		wait(1);
		swipeUntilElementAppear(By.id("bt_post_wishlist"), "up", 12);
		wait(1);
		clickElementById("bt_post_wishlist");
		reports_BuyCarTest.log(LogStatus.INFO, "点击列表页帮我买车按钮");
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "开始设置心愿单为：大众cc，10w");
		checkTitlebar1("帮我找车");
		clickElementById("tvCache");
		wait(1);
		checkTitlebar1("品牌导航");
		clickElementByName("大众");
		sleep(100);
		clickElementByName("CC");
		inputById("et_price", "20");
		clickElementById("tvCarAge");
		wait(1);
		slidingInElement(findElementById("wheel_age"), "up");
		clickElementById("btn_ok");
		wait(1);
//		clickElementById("bt_submit");
//		wait(1);
//		if (CheckViewVisibilty(By.id("bt_confirm"))) {
//			clickElementById("bt_confirm");
//		}else {
//			failAndMessage("心愿单提交失败");
//			reports_BuyCarTest.log(LogStatus.ERROR, "心愿单提交失败");
//		}
	}
	
	/**
	 * @Name 2501_BWZC
	 * @Catalogue 买车-列表页-帮我买车
	 * @Grade 低级
	 * @FunctionPoint 买车-列表页-帮我买车，检查保持默认是点击提交的报错信息
	 **/
	@Test
	public void test_2501_BWZC() {
		reports_BuyCarTest.startTest("test_2501_BWZC");
		gotocate(2);
		wait(2);
		swipeUntilElementAppear(By.id("bt_post_wishlist"), "up", 13);
		wait(1);
		clickElementById("bt_post_wishlist");
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "保持默认提交，查看错误提示");
		clickElementById("bt_submit");
		toastCheck("请选择喜欢的车型");
		reports_BuyCarTest.log(LogStatus.INFO, "选择品牌后，查看错误提示");
		clickElementById("tvCache");
		wait(1);
		checkTitlebar1("品牌导航");
		clickElementByName("大众");
		sleep(100);
		clickElementByName("CC");
		clickElementById("bt_submit");
		toastCheck("请输入购车预算");
	}
	
	/**
	 * @Name 2600_RSYCG
	 * @Catalogue 买车-列表页-二手车一成购
	 * @Grade 低级
	 * @FunctionPoint 买车-列表页-二手车一成购，查看列表显示
	 **/
	@Test
	public void test_2600_RSYCG() {
		reports_BuyCarTest.startTest("test_2600_RSYCG");
		gotocate(2);
		clickElementById("btAdvancedFilter");
		wait(1);
		checkTitlebar1("高级筛选");
		clickElementByName("优信服务");
		clickElementByName("一成购");
		clickElementById("ll_search");
		wait(3);
		toDetail(1);
		if (findElementById("tvDownPayment").getText().substring(0, 4).equals("一成首付")) {
			checkTitlebar_Detail();
			clickElementById("rlVehicleDetailHalfPrice");
			wait(3);
			checkTitlebar_Webview("分期购");
		}else {
			failAndMessage("一成购筛选列表页显示不是一成购的车，请检查");
			reports_BuyCarTest.log(LogStatus.ERROR, "一成购筛选列表页显示不是一成购的车，请检查");
		}
	}
	
	/**
	 * @Name 2601_RSYCG
	 * @Catalogue 买车-列表页-二手车一成购
	 * @Grade 低级
	 * @FunctionPoint 买车-列表页-二手车一成购，查看一成购详情页显示
	 **/
//	@Test
//	public void test_2601_RSYCG() {
//		reports_BuyCarTest.startTest("test_2601_RSYCG");
//		gotocate(2);
//		clickElementById("btAdvancedFilter");
//		wait(1);
//		checkTitlebar1("高级筛选");
//		clickElementByName("优信服务");
//		clickElementByName("一成购");
//		clickElementById("ll_search");
//		wait(3);
//		if (findElementById("ivyichengpayTag").getText().equals("一成购")) {
////			waitForVisible(By.id("ivyichengpayTag"), 3);
//			toDetail_half(1);
//			if (CheckViewVisibilty(By.id("yichenggou"))) {
//				clickElementById("rlVehicleDetailHalfPrice");
//				wait(3);
//				checkTitlebar_Webview("分期购");
//			}
//		}else {
//			failAndMessage("一成购列表页显示不正常");
//			reports_BuyCarTest.log(LogStatus.ERROR, "一成购列表页显示不正常");
//		}
//	}
	
	/**
	 * @Name 2600_YYKC_NUM
	 * @Catalogue 买车-详情页-关注
	 * @Grade 低级
	 * @FunctionPoint 买车-详情页-点击关注，检查详情页左上角关注清单的数字气泡
	 **/
	@Test
	public void test_2600_YYKC_NUM() {
		reports_BuyCarTest.startTest("test_2600_YYKC_NUM");
		gotocate(2);
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "点击第一个车进入详情页，点击关注。");
		toDetail(1);
		if(CheckViewVisibilty(By.id("tvCarListNum"))) {  //有数字气泡
			String detailNum1 = findElementById("tvCarListNum").getText();
			clickElementById("rlFocus");
			wait(1);
			String detailNum2 = findElementById("tvCarListNum").getText();
			if(Integer.parseInt(detailNum1)+1 == Integer.parseInt(detailNum2)) {
				reports_BuyCarTest.log(LogStatus.INFO, "详情页点击关注，右上角关注清单数字加1.");
				System.out.println("详情页点击关注，右上角关注清单数字加1.");
			}else {
				reports_BuyCarTest.log(LogStatus.INFO, "详情页点击关注，右上角关注清单数字没有加1.此车已被加入关注清单。");
				System.out.println("详情页点击关注，右上角关注清单数字没有加1.此车已被加入关注清单。");
			}
		}else {
			clickElementById("rlFocus");
			wait(1);
			if(CheckViewVisibilty(By.id("tvCarListNum"))) {  //有数字气泡
				if(findElementById("tvCarListNum").getText().equals("1")) {
					reports_BuyCarTest.log(LogStatus.INFO, "详情页点击关注，右上角关注清单数字加1.");
					System.out.println("详情页点击关注，右上角关注清单数字加1.");
				}else {
					reports_BuyCarTest.log(LogStatus.ERROR, "详情页点击关注，右上角关注清单数字不是加1.请人工查看。");
					failAndMessage("详情页点击关注，右上角关注清单数字不是加1.请人工查看。");
				}		
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "详情页点击关注，右上角没有数字气泡，请人工查看。");
				failAndMessage("详情页点击关注，右上角没有数字气泡，请人工查看。");
			}
		}
	}
	
	/**
	 * @Name 2600_YYKC_SORT
	 * @Catalogue 买车-详情页-关注
	 * @Grade 低级
	 * @FunctionPoint 买车-详情页-关注，检查关注清单顺序-倒序
	 **/
	@Test
	public void test_2600_YYKC_SORT() {
		reports_BuyCarTest.startTest("test_2600_YYKC_SORT");
		gotocate(2);
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "点击第一个车进入详情页，点击关注。");
		toDetail(1);
		reports_BuyCarTest.log(LogStatus.INFO, "检查关注清单是否有数据，有的话清除");
		if(CheckViewVisibilty(By.id("tvCarListNum"))) {
			clickElementById("llSecond");
			wait(4);
			clickElementById("iv_edit");
			sleep(500);
			clickElementById("ll_bottom_button_edit");
			sleep(500);
			clickElementById("tv_commit");  //删除
			sleep(500);
			if(CheckViewVisibilty(By.id("tvDialogMessage"))) {  //请确认删除所选车源
				clickElementById("bt_confirm_ok");
			}
			wait(1);
			reports_BuyCarTest.log(LogStatus.INFO, "检查关注清单页面是否为默认页，并且返回到详情页查看是否有数字气泡。");
			if(CheckViewVisibilty(By.id("tv_see_car_empty"))) {
				driver.pressKeyCode(AndroidKeyCode.BACK);
				wait(1);
				if(CheckViewVisibilty(By.id("tvCarListNum"))) {
					System.out.println(findElementById("tvCarListNum").getText());
					reports_BuyCarTest.log(LogStatus.ERROR, "关注清单全选删除失败。");
					failAndMessage("关注清单全选删除失败。");
				}else {
					reports_BuyCarTest.log(LogStatus.INFO, "关注清单全选删除成功。");
					System.out.println("关注清单全选删除成功。");
				}	
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "关注清单全选删除失败。");
				failAndMessage("关注清单全选删除失败。");
			}
			wait(1);		
		}
		reports_BuyCarTest.log(LogStatus.INFO, "点击第一辆车的关注");
		clickElementById("rlFocus");
		wait(1);
		driver.pressKeyCode(AndroidKeyCode.BACK);
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*8/10, width/2, height*6/10, 1000);
		wait(3);
		toDetail(3);
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "点击第二辆车的关注按钮，并且获取车辆名称");
		String detailTitle = getTextById("tvVehicleDetailsCarName");
//		System.out.println(detailTitle);
		clickElementById("rlFocus");
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "进入关注清单");
		clickElementById("llSecond");
		wait(4);	
		reports_BuyCarTest.log(LogStatus.INFO, "获取关注清单的第一辆车的车辆名称");
		List<WebElement> tileList = driver.findElementsById("tvCarWholeName");
		String carName = tileList.get(0).getText();
//		System.out.println(carName);
		if(carName.trim().equals(detailTitle.trim())) {
			reports_BuyCarTest.log(LogStatus.INFO, "关注清单顺序正确。");
			System.out.println("关注清单顺序正确。");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "关注清单顺序不正确，请查看。");
			failAndMessage("关注清单顺序不正确，请查看。");
		}	
		reports_BuyCarTest.log(LogStatus.INFO, "下拉刷新关注清单，查看顺序是否正确。");
//		int inwidth = driver.manage().window().getSize().width;
//		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height/3, width/2, height*2/3, 1000);
		wait(1);
		tileList = driver.findElementsById("tvCarWholeName");
		carName = tileList.get(0).getText();
		if(carName.trim().equals(detailTitle.trim())) {
			reports_BuyCarTest.log(LogStatus.INFO, "下拉刷新关注清单之后，关注清单顺序正确。");
			System.out.println("下拉刷新关注清单之后，关注清单顺序正确。");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "下拉刷新关注清单之后，关注清单顺序不正确，请查看。");
			failAndMessage("下拉刷新关注清单之后，关注清单顺序不正确，请查看。");
		}	
		clickElementById("iv_edit");
		sleep(500);
		clickElementByName("全选");
		sleep(500);
		clickElementById("tv_commit");  //删除
		sleep(500);
		if(CheckViewVisibilty(By.id("tvDialogMessage"))) {  //请确认删除所选车源
			clickElementById("bt_confirm_ok");
		}
		reports_BuyCarTest.log(LogStatus.INFO, "检查关注清单页面是否为默认页，并且返回到详情页查看是否有数字气泡。");
		if(CheckViewVisibilty(By.id("tv_see_car_empty"))) {
			driver.pressKeyCode(AndroidKeyCode.BACK);
			if(CheckViewVisibilty(By.id("tvCarListNum"))) {
				reports_BuyCarTest.log(LogStatus.ERROR, "关注清单全选删除失败。");
				failAndMessage("关注清单全选删除失败。");
			}else {
				reports_BuyCarTest.log(LogStatus.INFO, "关注清单全选删除成功。");
				System.out.println("关注清单全选删除成功。");
			}	
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "关注清单全选删除失败。");
			failAndMessage("关注清单全选删除失败。");
		}
//		clickElementById("iv_back");
		wait(1);	
	}
	
	/**
	 * @Name 2600_YYKC_COUNT
	 * @Catalogue 买车-详情页-关注
	 * @Grade 低级
	 * @FunctionPoint 买车-详情页-关注，删除关注清单的一辆车，检查详情页关注清单数字是否随着减一
	 **/
	@Test
	public void test_2600_YYKC_COUNT() {
		reports_BuyCarTest.startTest("test_2600_YYKC_COUNT");
		gotocate(2);
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "点击第一个车进入详情页，点击关注。");
		toDetail(1);
		clickElementById("rlFocus");
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "获取关注看车数字气泡，如果为1，则返回列表页再加入另一个车辆到关注清单。");
		String detailNum1 = findElementById("tvCarListNum").getText();
		System.out.println(detailNum1.equals("1"));
		if(detailNum1.equals("1")) {
			driver.pressKeyCode(AndroidKeyCode.BACK);
			int width = driver.manage().window().getSize().width;
			int height = driver.manage().window().getSize().height;
			driver.swipe(width/2, height*7/10, width/2, height*6/10, 1000);
			wait(2);
			reports_BuyCarTest.log(LogStatus.INFO, "进入第3个车详情页，并点击关注。");
			toDetail(3);
			wait(2);
			clickElementById("rlFocus");
			wait(1);
			detailNum1 = findElementById("tvCarListNum").getText();
		}
		System.out.println(detailNum1);
		reports_BuyCarTest.log(LogStatus.INFO, "进入关注清单。");
		clickElementById("llSecond");
		wait(4);
		clickElementById("iv_edit");
		clickElementById("iv_check");
		clickElementById("tv_commit");  //删除
		wait(1);
		if(CheckViewVisibilty(By.id("tvDialogMessage"))) {  //请确认删除所选车源
			clickElementById("bt_confirm_ok");
			sleep(500);
		}
		backBTN();
		wait(2);
		String detailNum2 = findElementById("tvCarListNum").getText();
		System.out.println(detailNum2);
		if(Integer.parseInt(detailNum1) == Integer.parseInt(detailNum2)+1) {
			reports_BuyCarTest.log(LogStatus.INFO, "关注清单中删除某个车辆，详情页关注数目显示正确。");
			System.out.println("关注清单中删除某个车辆，详情页关注数目显示正确。");
		}else {
			reports_BuyCarTest.log(LogStatus.INFO, "关注清单中删除某个车辆，详情页关注数目显示不正确。");
			failAndMessage("关注清单中删除某个车辆，详情页关注数目显示不正确。");
		}
	}
	
//	/**
//	 * @Name 2600_YYKC_DIRECT
//	 * @Catalogue 买车-全国直购车-无预约看车
//	 * @Grade 低级
//	 * @FunctionPoint 买车-全国直购车-无预约看车，检查全国直购车是否有预约看车按钮。
//	 **/
//	@Test
//	public void test_2600_YYKC_DIRECT() {
//		reports_BuyCarTest.startTest("test_2600_YYKC_DIRECT");
//		gotocate(2);
//		wait(2);
//		reports_BuyCarTest.log(LogStatus.INFO, "进入列表页第二个车的详情页");
//		toDetail(2);
//		wait(1);
//		reports_BuyCarTest.log(LogStatus.INFO, "判断详情页里面是否有全国直购的图标");
//		if(CheckViewVisibilty(By.id("imgDirectPurchase"))) {
//			reports_BuyCarTest.log(LogStatus.INFO, "有全国直购的图标，然后判断是否有预约看车按钮。");
//			if(CheckViewVisibilty(By.id("rlReserve"))) {
//				reports_BuyCarTest.log(LogStatus.ERROR, "全国直购的车辆有预约看车按钮，是新详情页，请查看。");
//				failAndMessage("全国直购的车辆有预约看车按钮，是新详情页，请查看。");
//			}else {
//				reports_BuyCarTest.log(LogStatus.INFO, "全国直购的车辆没有预约看车按钮，是老详情页。");
//				System.out.println("全国直购的车辆没有预约看车按钮，是老详情页。");
//			}
//		}else {
//			reports_BuyCarTest.log(LogStatus.ERROR, "列表页第二辆车不是全国直购的车，请查看。");
//			failAndMessage("列表页第二辆车不是全国直购的车，请查看。");
//		}
//	}
	
	/**
	 * @Name 2600_YYKC_LDT
	 * @Catalogue 买车-关注
	 * @Grade 低级
	 * @FunctionPoint 买车-关注，检查关注里面-点击雷达图
	 **/
	@Test
	public void test_2600_YYKC_LDT() {
		reports_BuyCarTest.startTest("test_2600_YYKC_LDT");
		gotocate(2);
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "进入第一辆车的详情页，点击关注");
		toDetail(1);
		if(CheckViewVisibilty(By.id("tvCarListNum"))) {//如果关注清单有车辆就清空车辆
			clickElementById("llSecond");
			wait(4);
			clickElementById("iv_edit");
			clickElementById("ll_bottom_button_edit");
			clickElementById("tv_commit");  //删除
			sleep(500);
			if(CheckViewVisibilty(By.id("tvDialogMessage"))) {  //请确认删除所选车源
				clickElementById("bt_confirm_ok");
				sleep(500);
			}
			wait(1);
			backBTN();
		}
		clickElementById("rlFocus");
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "进入看车清单,点击去预约按钮。");
		clickElementById("llSecond");
		wait(4);
		String carName = findElementById("tvCarWholeName").getText();
		clickElementById("radar_view");
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "点击雷达图");
		checkTitlebar1("参数配置");
		String name = findElementById("tvCarName").getText();
		if(carName.equals(name)) {
			reports_BuyCarTest.log(LogStatus.INFO, "点击雷达图进入的参数配置，车辆名称显示正常。");
			System.out.println("点击雷达图进入的参数配置，车辆名称显示正常。");
		}else {
			reports_BuyCarTest.log(LogStatus.FAIL, "点击雷达图进入的参数配置，车辆名称显示异常，请查看。");
			failAndMessage("点击雷达图进入的参数配置，车辆名称显示异常，请查看。");
		}
		clickElementByName("在线咨询");
		reports_BuyCarTest.log(LogStatus.INFO, "点击参数配置页面的在线咨询。");
		checkTitlebar1("优信二手车");
		System.out.println("点击雷达图进入的参数配置页面，底部的在线咨询功能正常");
		reports_BuyCarTest.log(LogStatus.INFO, "点击雷达图进入的参数配置页面，底部的在线咨询功能正常");
	}
	
	/**
	 * @Name 2600_YYKC_CLICK
	 * @Catalogue 买车-关注
	 * @Grade 低级
	 * @FunctionPoint 买车-关注，检查点击关注清单车辆进入对应车辆的详情页。
	 **/
	@Test
	public void test_2600_YYKC_CLICK() {
		reports_BuyCarTest.startTest("test_2600_YYKC_CLICK");
		gotocate(2);
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "进入第一辆车的详情页，点击关注");
		toDetail(1);
		clickElementById("rlFocus");
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "进入关注清单");
		clickElementById("llSecond");
		wait(4);
		reports_BuyCarTest.log(LogStatus.INFO, "获取关注清单第一辆车的车辆名称，并点击该车。");
		List<WebElement> tileList = driver.findElementsById("tvCarWholeName");
		String carName = tileList.get(0).getText();
		clickElementById("rootLine");
		wait(1);
		String detailTitle = getTextById("tvVehicleDetailsCarName");
		if(carName.equals(detailTitle)) {
			reports_BuyCarTest.log(LogStatus.INFO, "点击关注清单中的车辆进入到此车的详情页。");
			System.out.println("点击关注清单中的车辆进入到此车的详情页。");
			if(findElementById("tvFocus").getText().equals("已关注")) {
				reports_BuyCarTest.log(LogStatus.INFO, "关注清单进去的车的详情页显示已关注，显示正常。");
				System.out.println("关注清单进去的车的详情页显示已关注，显示正常。");
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "关注清单进去的车的详情页显示:"+findElementById("tvFocus").getText()+"，显示异常。");
				failAndMessage("关注清单进去的车的详情页显示:"+findElementById("tvFocus").getText()+"，显示异常。");
			}
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "点击关注清单中的车辆没有进入到此车的详情页。");
			failAndMessage("点击关注清单中的车辆没有进入到此车的详情页。");
		}
	}
	
	/**
	 * @Name 2600_YYKC_DELETE
	 * @Catalogue 买车-关注
	 * @Grade 低级
	 * @FunctionPoint 买车-关注，检查关注清单车辆左滑删除
	 **/
	@Test
	public void test_2600_YYKC_DELETE() {
		reports_BuyCarTest.startTest("test_2600_YYKC_DELETE");
		gotocate(2);
		wait(2);
		toDetail(1);
		clickElementById("rlFocus");
		wait(1);
		String detailNum1 = findElementById("tvCarListNum").getText();
		reports_BuyCarTest.log(LogStatus.INFO, "进入关注清单");
		clickElementById("llSecond");
		wait(4);
		reports_BuyCarTest.log(LogStatus.INFO, "左滑删除");
		slidingInElement(findElementById("rootLine"), "left");
		clickElementById("rl_delete");
		sleep(500);
		backBTN();
		if(CheckViewVisibilty(By.id("tvCarListNum"))) {
			String detailNum2 = findElementById("tvCarListNum").getText();
			if(Integer.parseInt(detailNum1) == Integer.parseInt(detailNum2)+1) {
				reports_BuyCarTest.log(LogStatus.INFO, "关注清单，左滑删除成功。");
				System.out.println("关注清单，左滑删除成功。");
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "关注清单，左滑删除失败。");
				GetScreenshot("test_2600_YYKC_DELETE_1");
				failAndMessage("关注清单，左滑删除失败。");
			}	
		}else if(Integer.parseInt(detailNum1) == 1){
			reports_BuyCarTest.log(LogStatus.INFO, "关注清单，左滑删除成功。");
			System.out.println("关注清单，左滑删除成功。");
		}
	}
	
	/**
	 * @Name 2600_YYKC_button
	 * @Catalogue 买车-关注
	 * @Grade 低级
	 * @FunctionPoint 买车-关注，检查关注清单车辆的在线咨询按钮点击
	 **/
	@Test
	public void test_2600_YYKC_button() {
		reports_BuyCarTest.startTest("test_2600_YYKC_button");
		gotocate(2);
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "进入第一辆车的详情页，点击关注");
		toDetail(1);
		if(CheckViewVisibilty(By.id("tvCarListNum"))) {//如果关注清单有车辆就清空车辆
			clickElementById("llSecond");
			wait(4);
			clickElementById("iv_edit");
			clickElementById("ll_bottom_button_edit");
			clickElementById("tv_commit");  //删除
			wait(1);
			if(CheckViewVisibilty(By.id("tvDialogMessage"))) {  //请确认删除所选车源
				clickElementById("bt_confirm_ok");
				sleep(500);
			}
			wait(1);
			driver.pressKeyCode(AndroidKeyCode.BACK);
			wait(1);
		}
		clickElementById("rlFocus");
		wait(1);
		clickElementById("llSecond");
		reports_BuyCarTest.log(LogStatus.INFO, "进入看车清单。");
		wait(4);
		clickElementByName("在线咨询");
		reports_BuyCarTest.log(LogStatus.INFO, "点击在线咨询");
		wait(1);
		checkTitlebar1("优信二手车");
		System.out.println("在线咨询功能正常");
		driver.pressKeyCode(AndroidKeyCode.BACK);
		reports_BuyCarTest.log(LogStatus.INFO, "我的关注车辆的留咨入口功能正常。");
		System.out.println("我的关注车辆的留咨入口功能正常。");
	}
	
	/**
	 * @Name 2600_YYKC_compare_button
	 * @Catalogue 买车-关注
	 * @Grade 低级
	 * @FunctionPoint 买车-关注，检查关注清单车辆的对比按钮,只有一辆车的时候是没有对比按钮的，大于一辆车的时候对比按钮存在
	 **/
	@Test
	public void test_2600_YYKC_compare_button() {
		reports_BuyCarTest.startTest("test_2600_YYKC_compare_button");
		gotocate(2);
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "进入第一辆车的详情页");
		toDetail(1);
		if(CheckViewVisibilty(By.id("tvCarListNum"))) {//如果关注清单有车辆就清空车辆
			clickElementById("llSecond");
			wait(4);
			clickElementById("iv_edit");
			clickElementById("ll_bottom_button_edit");
			clickElementById("tv_commit");  //删除
			wait(1);
			if(CheckViewVisibilty(By.id("tvDialogMessage"))) {  //请确认删除所选车源
				clickElementById("bt_confirm_ok");
				sleep(500);
			}
			wait(1);
			driver.pressKeyCode(AndroidKeyCode.BACK);
			wait(1);
		}
		clickElementById("rlFocus");
		reports_BuyCarTest.log(LogStatus.INFO, "进入第一辆车的详情页，点击关注");
		wait(1);
		clickElementById("llSecond");
		wait(4);
		reports_BuyCarTest.log(LogStatus.INFO, "进入第一辆车的详情页，点击看车清单按钮，进入看车清单");
		if(!CheckViewVisibilty(By.id("tv_compare"))) {
			reports_BuyCarTest.log(LogStatus.INFO, "关注清单只有一辆车的时候没有对比按钮，显示正常。");
			System.out.println("关注清单只有一辆车的时候没有对比按钮，显示正常。");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "关注清单只有一辆车的时候有对比按钮，显示异常，请检查。");
			GetScreenshot("test_2600_YYKC_compare_button_1");
			failAndMessage("关注清单只有一辆车的时候有对比按钮，显示异常，请检查。");
		}
		driver.pressKeyCode(AndroidKeyCode.BACK);
		wait(1);
		driver.pressKeyCode(AndroidKeyCode.BACK);
		wait(1);
		toDetail(2);
		clickElementById("rlFocus");
		reports_BuyCarTest.log(LogStatus.INFO, "进入第2辆车的详情页，点击关注");
		wait(1);
		clickElementById("llSecond");
		wait(4);
		reports_BuyCarTest.log(LogStatus.INFO, "进入第2辆车的详情页，点击看车清单按钮，进入看车清单");
		if(CheckViewVisibilty(By.id("tv_compare"))) {
			reports_BuyCarTest.log(LogStatus.INFO, "关注清单有2辆车的时候有对比按钮，显示正常。");
			System.out.println("关注清单有2辆车的时候有对比按钮，显示正常。");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "关注清单有2辆车的时候依旧没有对比按钮，显示异常，请检查。");
			GetScreenshot("test_2600_YYKC_compare_button_2");
			failAndMessage("关注清单有2辆车的时候依旧没有对比按钮，显示异常，请检查。");
		}
	}
	
	/**
	 * @Name 2600_YYKC_click_all
	 * @Catalogue 买车-关注
	 * @Grade 低级
	 * @FunctionPoint 买车-关注，关注清单，点击到全部，检查车辆名字、车价
	 **/
	@Test
	public void test_2600_YYKC_click_all() {
		reports_BuyCarTest.startTest("test_2600_YYKC_click_all");
		gotocate(2);
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "进入第一辆车的详情页");
		toDetail(1);
		if(CheckViewVisibilty(By.id("tvCarListNum"))) {//如果关注清单有车辆就清空车辆
			clickElementById("llSecond");
			wait(4);
			clickElementById("iv_edit");
			clickElementById("ll_bottom_button_edit");
			clickElementById("tv_commit");  //删除
			wait(1);
			if(CheckViewVisibilty(By.id("tvDialogMessage"))) {  //请确认删除所选车源
				clickElementById("bt_confirm_ok");
				sleep(500);
			}
			wait(1);
			driver.pressKeyCode(AndroidKeyCode.BACK);
			wait(1);
		}
		clickElementById("rlFocus");
		reports_BuyCarTest.log(LogStatus.INFO, "进入第一辆车的详情页，点击关注");
		wait(1);
		clickElementById("llSecond");
		wait(4);
		reports_BuyCarTest.log(LogStatus.INFO, "进入第一辆车的详情页，点击看车清单按钮，进入看车清单");
		String focusCarName = findElementById("tvCarWholeName").getText();
		String focusPrice = findElementById("tvPrice").getText();
		clickElementByName("全部");
		String allCarName = findElementById("tvCarWholeName").getText();
		String allPrice = findElementById("tvPrice").getText();
		if(focusCarName.equals(allCarName)&&
				focusPrice.equals(allPrice)) {
			reports_BuyCarTest.log(LogStatus.INFO, "关注列表和全部列表的车辆信息一致。");
			System.out.println("关注列表和全部列表的车辆信息一致。");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "关注列表和全部列表的车辆信息不一致，请检查。");
			failAndMessage("关注列表和全部列表的车辆信息不一致，请检查。");
		}
		if(CheckViewVisibilty(By.id("iv_follow"))) {
			reports_BuyCarTest.log(LogStatus.INFO, "全部列表有已关注标签");
			System.out.println("全部列表有已关注标签");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "全部列表没有已关注标签，请检查。");
			failAndMessage("全部列表没有已关注标签，请检查。");
		}
	}
	
	
	/**
	 * @Name 2600_YYKC_compare_button_enable
	 * @Catalogue 买车-关注
	 * @Grade 低级
	 * @FunctionPoint 买车-关注，检查关注清单车辆的对比按钮跳转功能；勾选一辆车时按钮不可点击；勾选两辆车时按钮可点击；
	 **/
	@Test
	public void test_2600_YYKC_compare_button_enable() {
		reports_BuyCarTest.startTest("test_2600_YYKC_compare_button_enable");
		gotocate(2);
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "进入第一辆车的详情页");
		toDetail(1);
		if(CheckViewVisibilty(By.id("tvCarListNum"))) {
			if(findElementById("tvCarListNum").getText().equals("1")) {
				if(findElementById("tvFocus").getText().equals("已关注")) {
					backBTN();
					toDetail(2);
				}
				clickElementById("rlFocus");
				reports_BuyCarTest.log(LogStatus.INFO, "点击关注");
				wait(1);
			}
		}else {
			clickElementById("rlFocus");
			reports_BuyCarTest.log(LogStatus.INFO, "点击关注");
			wait(1);
			backBTN();
			toDetail(2);
			clickElementById("rlFocus");
			reports_BuyCarTest.log(LogStatus.INFO, "点击关注");
			wait(1);
		}
		clickElementById("llSecond");
		wait(4);
		reports_BuyCarTest.log(LogStatus.INFO, "点击看车清单按钮，进入看车清单");
		clickElementById("tv_compare");
		reports_BuyCarTest.log(LogStatus.INFO, "点击看车清单的对比按钮");
		List<WebElement> carList = driver.findElementsById("iv_check");
		carList.get(0).click();
//		clickElementById("iv_check");
		reports_BuyCarTest.log(LogStatus.INFO, "勾选第一辆车");
		if(findElementById("tvCompare").getText().equals("选择1辆车开始对比")) {
			reports_BuyCarTest.log(LogStatus.INFO, "勾选第一辆车，按钮文案为：" + findElementById("tvCompare").getText());
			System.out.println("勾选第一辆车，按钮文案为：" + findElementById("tvCompare").getText());
		}else {
			failAndMessage("勾选第一辆车，按钮文案为："+ findElementById("tvCompare").getText());
		}
		carList.get(1).click();
		reports_BuyCarTest.log(LogStatus.INFO, "勾选第2辆车");
		if(findElementById("tvCompare").getText().equals("开始对比")) {
			reports_BuyCarTest.log(LogStatus.INFO, "勾选2辆车，按钮文案为：" + findElementById("tvCompare").getText());
			System.out.println("勾选2辆车，按钮文案为：" + findElementById("tvCompare").getText());
		}else {
			failAndMessage("勾选2辆车，按钮文案为："+ findElementById("tvCompare").getText());
		}
		clickElementById("tvCompare");
		reports_BuyCarTest.log(LogStatus.INFO, "点击开始对比");
		wait(3);
		checkTitlebar_Webview("参数对比");
		if(findElementById("tvTitle").getText().equals("参数对比")) {
			reports_BuyCarTest.log(LogStatus.INFO, "点击开始对比按钮，进入参数对比页面。");
			System.out.println("点击开始对比按钮，进入参数对比页面。");
		}else {
			failAndMessage("点击开始对比按钮没有进入参数对比页面，请检查。");
		}
	}
	
	
//	/**
//	 * @Name 2311_CYDT
//	 * @Catalogue 买车-详情页-车源动态
//	 * @Grade 高级
//	 * @FunctionPoint 买车-详情页-车源动态，检查我要优惠
//	 */
//	@Test
//	public void test_2311_CYDT() {
//		reports_BuyCarTest.startTest("test_2311_CYDT");
//		gotocate(2);
//		wait(2);
////		reports_BuyCarTest.log(LogStatus.INFO, "获取已登录账号的手机号");
////		String phoneNum = getLoginPhoneNum();
////		System.out.println(phoneNum);
////		gotoCateSet(1);
//		reports_BuyCarTest.log(LogStatus.INFO, "进入第一个车的详情页");
//		toDetail(1);
//		reports_BuyCarTest.log(LogStatus.INFO, "上滑找到车源动态模块");
//		swipeUntilElementAppear(By.id("rl_head"), "up", 2);
//		wait(1);
//		reports_BuyCarTest.log(LogStatus.INFO, "点击车源动态");
//		clickElementById("rl_head");
//		wait(1);
//		if(findElementById("tvRight").getText().equals("我要优惠")) {
//			reports_BuyCarTest.log(LogStatus.INFO, "车源动态-我要优惠，显示正常。");
//			System.out.println("车源动态-我要优惠，显示正常。");
//		}else {
//			reports_BuyCarTest.log(LogStatus.INFO, "车源动态-我要优惠，显示异常。");
//			failAndMessage("车源动态-我要优惠，显示异常。");
//		}
////		clickElementByName("询底价");
////		wait(1);
////		waitForVisible(By.id("tv_notify"), 3);
////		reports_BuyCarTest.log(LogStatus.INFO, "判断车源动态-询底价默认的手机号是否是当前登录的手机号。");
////		if (waitForVisible(By.id("et_phone_num"), 3).getAttribute("text").equals(phoneNum)) {
////			reports_BuyCarTest.log(LogStatus.INFO, "车源动态模块-询底价默认手机号是当前登录的手机号");
////			System.out.println("车源动态模块-询底价默认手机号是当前登录的手机号");
////		}else {
////			reports_BuyCarTest.log(LogStatus.INFO, "车源动态模块-询底价默认手机号不是当前登录的手机号");
////			failAndMessage("询底价默认手机号码不是登录的手机号码");
////		}
//	}
	
//	/**
//	 * @Name 2500_reset
//	 * @Subcatalog 买车-列表
//	 * @Grade 高级
//	 * @FunctionPoint 买车-列表，重置按钮
//	 */
//	@Test
//	public void test_2500_reset() {
//		reports_BuyCarTest.startTest("test_2500_reset");
//		gotocate(2);
//		wait(2);
//		reports_BuyCarTest.log(LogStatus.INFO, "进入买车页，筛选大众不限车系，查看是否出现重置按钮");
//		clickElementByName("品牌");
//		wait(1);
//		clickElementByName("大众");
//		clickElementByName("不限车系");
//		wait(2);
//		if(CheckViewVisibilty(By.id("tv_reset"))) {  //重置
//			reports_BuyCarTest.log(LogStatus.ERROR, "只有一个关键词的时候出现了重置按钮，请检查。");
//			GetScreenshot("test_2500_reset_1");
//			failAndMessage("只有一个关键词的时候出现了重置按钮，请检查。");
//		}else {
//			reports_BuyCarTest.log(LogStatus.INFO, "只有一个关键词的时候没有重置按钮。");
//			System.out.println("只有一个关键词的时候没有重置按钮。");
//		}
//		reports_BuyCarTest.log(LogStatus.INFO, "点击品牌，选择车系帕萨特；");
//		clickElementByName("品牌");
//		clickElementByName("帕萨特");
//		wait(2);
//		if(CheckViewVisibilty(By.id("tv_reset"))) {  //重置
//			reports_BuyCarTest.log(LogStatus.ERROR, "有两个关键词的时候有重置按钮，请检查。");
//			GetScreenshot("test_2500_reset_2");
//			failAndMessage("有两个关键词的时候有重置按钮，请检查。");
//		}else {
//			reports_BuyCarTest.log(LogStatus.INFO, "有两个关键词的时候没有重置按钮。");
//			System.out.println("有两个关键词的时候没有重置按钮。");
//		}
//	}
	
	/**
	 * @Name 2500_brand_seriesCard
	 * @Subcatalog 买车-列表
	 * @Grade 高级
	 * @FunctionPoint 买车-列表，筛选品牌之后展示的车系卡片
	 */
	@Test
	public void test_2500_brand_seriesCard() {
		reports_BuyCarTest.startTest("test_2500_brand_seriesCard");
		gotocate(2);
		wait(2);
		String listCarName1 = "";
		reports_BuyCarTest.log(LogStatus.INFO, "筛选大众品牌，查看是否有车系卡片");
		clickElementByName("品牌");
		wait(1);
		clickElementByName("大众");
		clickElementByName("不限车系");
		wait(2);
		if(CheckViewVisibilty(By.id("recommend_series_recyclerview"))) {
			if(CheckViewVisibilty(By.id("rl_recommend_series"))&&
					CheckViewVisibilty(By.id("tv_car_series"))) {
				reports_BuyCarTest.log(LogStatus.INFO, "筛选大众，有车系卡片，卡片信息显示正常。");
				System.out.println("筛选大众，有车系卡片，卡片信息显示正常。");
				reports_BuyCarTest.log(LogStatus.INFO, "筛选大众，左右滑动车系卡片左右滑动");
				String car1 = findElementById("tv_car_series").getText();
				System.out.println(car1);
				slidingInElement(findElementById("recommend_series_recyclerview"), "left");
				wait(1);
				String car2 = findElementById("tv_car_series").getText();
				System.out.println(car2);
				if(car1.equals(car2)) {
					reports_BuyCarTest.log(LogStatus.ERROR, "左滑车系卡片失败。");
					failAndMessage("左滑车系卡片失败。");
				}else {
					reports_BuyCarTest.log(LogStatus.INFO, "左滑车系卡片成功。");
					System.out.println("左滑车系卡片成功。");
				}
//				slidingInElement(findElementById("recommend_series_recyclerview"), "right");
//				wait(1);
//				String car3 = findElementById("tv_car_series").getText();
//				System.out.println(car3);
//				if(car1.equals(car3)) {
//					reports_BuyCarTest.log(LogStatus.INFO, "右滑车系卡片成功。");
//					System.out.println("右滑车系卡片成功。");
//				}else {
//					reports_BuyCarTest.log(LogStatus.ERROR, "右滑车系卡片失败。");
//					failAndMessage("右滑车系卡片失败。");
//				}
				clickElementById("tv_car_series");
				reports_BuyCarTest.log(LogStatus.ERROR, "点击车系卡片。");
				wait(2);
//				String listSeries = findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]/android.widget.HorizontalScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.TextView[1]").getText();
//				System.out.println(listSeries);
				String listCarName = findElementById("tvCarWholeName").getText();
				System.out.println(listCarName);
				if(listCarName.substring(0, 4).equals("今日特惠")) {
					listCarName1 = listCarName.substring(5, car2.length()+6).replace(" ", "");
				}else {
					listCarName1 = listCarName.substring(0, car2.length()+1).replace(" ", "");
				}
				System.out.println(listCarName1);
				if(car2.equals(listCarName1)) {
					reports_BuyCarTest.log(LogStatus.INFO, "点击车系卡片，进入对应车系车系列表。");
					System.out.println("点击车系卡片，进入对应车系车系列表。");
				}else {
					reports_BuyCarTest.log(LogStatus.ERROR, "点击车系卡片，没有进入对应车系车系列表。");
					failAndMessage("点击车系卡片，没有进入对应车系车系列表。");
				}
			}
		}else {
			reports_BuyCarTest.log(LogStatus.INFO, "筛选大众，没有车系卡片");
			System.out.println("筛选大众，没有车系卡片");
		}	
	}
	
	/**
	 * @Name 2500_price_seriesCard
	 * @Subcatalog 买车-列表
	 * @Grade 高级
	 * @FunctionPoint 买车-列表，筛选车价之后展示的车型卡片
	 */
	@Test
	public void test_2500_price_seriesCard() {
		reports_BuyCarTest.startTest("test_2500_price_seriesCard");
		gotocate(2);
		wait(2);
		int i = 0;
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		reports_BuyCarTest.log(LogStatus.INFO, "遍历筛选不同车价，查看是否有对应车型卡片");
		String[] str = {"5万以下", "5-10万", "10-15万", "15-20万", "20-30万", "30-50万", "50万以上"};
		int[] int_x = {width*1/2, width*5/6, width*1/6, width*1/2, width*5/6, width*1/6, width*1/2};
		int[] int_y = {height*1/3, height*1/3, height*5/12, height*5/12, height*5/12, height*1/2, height*1/2};
		String[] str_pop = {"5万以下", "5-10万元", "10-15万元", "15-20万元", "20-30万元", "30-50万元", "50万以上"};
		for(i=0; i<str.length; i++) {
			clickElementByName("车价");
			wait(1);
			System.out.println("x:"+int_x[i]+",  y:"+int_y[i]);
			TouchAction action = new TouchAction(driver);
			action.tap(int_x[i], int_y[i]).perform();
//			clickElementByName(str[i]);
			wait(2);
			if(CheckViewVisibilty(By.id("recommend_series_recyclerview"))) {
				reports_BuyCarTest.log(LogStatus.INFO, "点击车价："+str[i]+"，有对应的车型卡片。");
				System.out.println("点击车价："+str[i]+"，有对应的车型卡片。");
				if(findElementById("tv_pop_content").getText().equals(str_pop[i])) {
					reports_BuyCarTest.log(LogStatus.INFO, "点击对应的车价显示正确");
					System.out.println("点击对应的车价显示正确");
				}else {
					reports_BuyCarTest.log(LogStatus.ERROR, "点击车价["+str[i]+"]，之前搜索框显示["+str_pop[i]+"],现在搜索框显示["+findElementById("tv_pop_content").getText()+"],与之前不一样，请确认需求");
					failAndMessage("点击车价["+str[i]+"]，之前搜索框显示["+str_pop[i]+"],现在搜索框显示["+findElementById("tv_pop_content").getText()+"],与之前不一样，请确认需求");
				}
			}else {
				reports_BuyCarTest.log(LogStatus.INFO, "点击车价："+str[i]+"，没有对应的车系卡片，请检查。");
				failAndMessage("点击车价："+str[i]+"，没有对应的车系卡片，请检查。");
			}
		}
	}
	
	/**
	 * @Name 2500_model_seriesCard
	 * @Subcatalog 买车-列表
	 * @Grade 高级
	 * @FunctionPoint 买车-列表，筛选车型之后展示的车系卡片
	 */
	@Test
	public void test_2500_model_seriesCard() {
		reports_BuyCarTest.startTest("test_2500_model_seriesCard");
		gotocate(2);
		wait(2);
		int i = 0;
		int j = 0;
		reports_BuyCarTest.log(LogStatus.INFO, "遍历筛选不同车型，查看是否有对应车系卡片");
		String[] str = {"三厢轿车", "SUV", "两厢轿车", "MPV", "跑车", "面包车", "皮卡", "旅行车"};
		for(i=0; i<str.length; i++) {
			for(j=0; j<5; j++) {
				if(CheckViewVisibilty(By.id("iv_pop_delete"))) {
					clickElementById("iv_pop_delete");
					wait(3);
				}
			}
			clickElementByName("筛选");
			reports_BuyCarTest.log(LogStatus.INFO, "点击高级筛选");
			wait(1);
			if(CheckViewVisibilty(By.id("resetinfo_lly"))) {
				clickElementById("resetinfo_lly");
				reports_BuyCarTest.log(LogStatus.INFO, "存在重置按钮，先点击重置");
			}
			clickElementByName("车型");
			clickElementByName(str[i]);
			wait(2);
			String carNum = findElementById("advanced_search_counttv").getText();
			if(carNum.equals("查看周边城市车辆")) {
				reports_BuyCarTest.log(LogStatus.INFO, "查看周边城市车辆");
				System.out.println("查看周边城市车辆");
				driver.pressKeyCode(AndroidKeyCode.BACK);
				wait(2);
				continue;
			}
			String carNum1 = carNum.substring(2, carNum.length()-2);
			System.out.println(carNum1);
			if(Integer.valueOf(carNum1) < 20) {
				reports_BuyCarTest.log(LogStatus.INFO, "车型："+str[i]+"推荐车辆不足20辆。");
				System.out.println("车型："+str[i]+"推荐车辆不足20辆。");
				continue;
			}
			clickElementById("advanced_search_counttv");
			wait(2);				
			if(CheckViewVisibilty(By.id("recommend_series_recyclerview"))) {
				clickElementById("rl_recommend_price");
				wait(1);
				if(CheckViewVisibilty(By.id("rl_recommend_series"))) {
					reports_BuyCarTest.log(LogStatus.INFO, "选择车型："+str[i]+"，点击车价标签，有对应的车系卡片。");
					System.out.println("选择车型："+str[i]+"，点击车价标签，有对应的车系卡片。");
					reports_BuyCarTest.log(LogStatus.INFO, "点击车系卡片。");
					clickElementById("tv_car_series");
					wait(2);
					reports_BuyCarTest.log(LogStatus.INFO, "点击车系卡片进入对应车系的列表页。");
					System.out.println("点击车系卡片进入对应车系的列表页。");	
				}
			}
//			else {
//				reports_BuyCarTest.log(LogStatus.INFO, "点击车型："+str[i]+"，没有对应的卡片，请检查。");
//				failAndMessage("点击车型："+str[i]+"，没有对应的卡片，请检查。");
//			}
		}
	}
	
//	/**
//	 * @Name 2500_click_maintenance_error
//	 * @Subcatalog 买车-列表
//	 * @Grade 高级
//	 * @FunctionPoint 买车-列表，详情页瑕疵控件增加点击事件
//	 */
//	@Test
//	public void test_2500_click_maintenance_error() {
//		reports_BuyCarTest.startTest("test_2500_click_maintenance_error");
//		gotocate(2);
//		wait(2);
//		reports_BuyCarTest.log(LogStatus.INFO, "进入详情页");
//		toDetail(1);
//		reports_BuyCarTest.log(LogStatus.INFO, "滑动页面找到瑕疵按钮");
//		swipeUntilElementAppear(By.id("ll_historyflawcount"), "up", 6);
//		if(CheckViewVisibilty(By.id("ll_historyflawcount"))) {
//			reports_BuyCarTest.log(LogStatus.INFO, "点击瑕疵按钮");
//			clickElementById("ll_historyflawcount");
//			wait(2);
//			checkTitlebar_Webview("车辆历史报告");
//			reports_BuyCarTest.log(LogStatus.INFO, "点击瑕疵按钮，进入车况概要页面。");
//			System.out.println("点击瑕疵按钮，进入车况概要页面。");
//		}else {
//			reports_BuyCarTest.log(LogStatus.INFO, "没有找到瑕疵按钮。");
//			System.out.println("没有找到瑕疵按钮。");
//		}
//	}
	
	/**
	 * @Name 2500_IM_list
	 * @Subcatalog 买车-列表-客服
	 * @Grade 高级
	 * @FunctionPoint 买车-列表-客服，点击客服悬浮按钮进入IM客服聊天
	 */
	@Test
	public void test_2500_IM_list() {
		reports_BuyCarTest.startTest("test_2500_IM_list");
		gotocate(2);
		wait(1);
		ClickOnChatByList();
		reports_BuyCarTest.log(LogStatus.INFO, "点击买车列表页的客服悬浮按钮");
		wait(1);
		if(CheckViewVisibilty(By.id("list"))&&
				CheckViewVisibilty(By.id("iv_userhead"))&&   //客服头像
				CheckViewVisibilty(By.id("tv_userid"))&&
				CheckViewVisibilty(By.id("tv_chatcontent"))&&
				CheckViewVisibilty(By.id("et_sendmessage"))) {
			reports_BuyCarTest.log(LogStatus.INFO, "点击列表的客服悬浮按钮，进入客服聊天窗口");
			System.out.println("点击列表的客服悬浮按钮，进入客服聊天窗口");
			clickElementById("imgDelete");
			int width = driver.manage().window().getSize().width;
			int height = driver.manage().window().getSize().height;
			driver.swipe(width/2, height*3/5, width/2, height*4/5, 1000);
			wait(1);
			reports_BuyCarTest.log(LogStatus.INFO, "点击客服聊天的关闭按钮");
			if(findElementById("rbCheShi").getAttribute("checked").equals("true")) {
				reports_BuyCarTest.log(LogStatus.INFO, "点击关闭客服聊天按钮之后返回到买车tab页");
				System.out.println("点击关闭客服聊天按钮之后返回到买车tab页");
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "点击关闭客服聊天按钮之后没有返回到买车tab页，请检查");
				GetScreenshot("test_2500_IM_list_close");
				failAndMessage("点击关闭客服聊天按钮之后没有返回到买车tab页，请检查");
			}
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "点击列表的客服悬浮按钮，没有进入客服聊天窗口，请检查");
			failAndMessage("点击列表的客服悬浮按钮，没有进入客服聊天窗口，请检查");
		}
	}
	
//	/**
//	 * @Name 2500_IM_unread_mesg_list
//	 * @Subcatalog 买车-列表
//	 * @Grade 高级
//	 * @FunctionPoint 买车-列表，点击IM入口,如果有未读消息，进入我的会话，查看是否显示正确，进入未读消息，查看红点是否消失       
//	 */
//	@Test
//	public void test_2500_IM_unread_mesg_list() {
//		reports_BuyCarTest.startTest("test_2500_IM_unread_mesg_list");
//		gotocate(2);
//		wait(1);
//		if(CheckViewVisibilty(By.id("tvMessageCount"))) {
//			reports_BuyCarTest.log(LogStatus.INFO, "买车列表的IM入口有未读消息");
//			clickElementById("rlmessage");
//			wait(1);
//			if(CheckViewVisibilty(By.id("list_itease_layout"))) {
//				if(CheckViewVisibilty(By.id("unread_msg_number"))) {
//					reports_BuyCarTest.log(LogStatus.INFO, "我的会话也有未读消息，显示正常");
//					System.out.println("我的会话也有未读消息，显示正常");
//					reports_BuyCarTest.log(LogStatus.INFO, "进入未读消息");
//					clickElementById("list_itease_layout");
//					wait(1);
//					driver.pressKeyCode(AndroidKeyCode.BACK);
//					wait(1);
//					if(CheckViewVisibilty(By.id("unread_msg_number"))) {
//						reports_BuyCarTest.log(LogStatus.ERROR, "未读消息已经被阅读但我的会话仍然显示未读消息，请检查");
//						GetScreenshot("test_2500_IM_unread_mesg_1");
//						failAndMessage("未读消息已经被阅读但我的会话仍然显示未读消息，请检查");
//					}else {
//						driver.pressKeyCode(AndroidKeyCode.BACK);
//						if(CheckViewVisibilty(By.id("tvMessageCount"))) {
//							reports_BuyCarTest.log(LogStatus.ERROR, "未读消息已经被阅读但买车列表IM入口仍然显示未读消息，请检查");
//							GetScreenshot("test_2500_IM_unread_mesg_2");
//							failAndMessage("未读消息已经被阅读但买车列表IM入口仍然显示未读消息，请检查");
//						}
//					}
//				}else {
//					reports_BuyCarTest.log(LogStatus.ERROR, "我的会话没有未读消息，但是买车列表页IM入口有未读消息红点，显示异常，请检查。");
//					failAndMessage("我的会话没有未读消息，但是买车列表页IM入口有未读消息红点，显示异常，请检查。");
//				}
//			}else {
//				reports_BuyCarTest.log(LogStatus.ERROR, "暂时没有会话，但是买车列表页IM入口有未读消息红点，显示异常，请检查。");
//				failAndMessage("暂时没有会话，但是买车列表页IM入口有未读消息红点，显示异常，请检查。");
//			}
//		}else {
//			reports_BuyCarTest.log(LogStatus.INFO, "买车列表的IM入口没有未读消息");
//			System.out.println("买车列表的IM入口没有未读消息");
//		}
//	}
	
	/**
	 * @Name 2500_IM_XQ
	 * @Subcatalog 买车-详情页
	 * @Grade 高级
	 * @FunctionPoint 买车-详情页，点击IM入口进入我的会话    
	 */
	@Test
	public void test_2500_IM_XQ() {
		reports_BuyCarTest.startTest("test_2500_IM_XQ");
		gotocate(2);
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "进入详情页");
		toDetail(1);
		reports_BuyCarTest.log(LogStatus.INFO, "点击详情页的IM入口");
		clickElementById("rlChat");
		wait(1);
		if(CheckViewVisibilty(By.id("tvTitle")) && findElementById("tvTitle").getText().equals("我的会话")) {
			if(CheckViewVisibilty(By.id("list_itease_layout"))) {
				clickElementById("list_itease_layout");
				wait(1);
				checkTitlebar1("优信二手车");
				if(CheckViewVisibilty(By.id("bubble"))) {
					clickElementById("tvCarWholeName");
					wait(1);
					checkTitlebar_Detail();
					driver.pressKeyCode(AndroidKeyCode.BACK);
					if(CheckViewVisibilty(By.id("btn_set_mode_voice"))&&
							CheckViewVisibilty(By.id("et_sendmessage"))&&
							CheckViewVisibilty(By.id("rl_face"))&&
							CheckViewVisibilty(By.id("btn_more"))) {
						reports_BuyCarTest.log(LogStatus.INFO, "从我的会话进入的在线咨询聊天显示正常");
						System.out.println("从我的会话进入的在线咨询聊天显示正常");
					}
				}
			}else {
				reports_BuyCarTest.log(LogStatus.INFO, "暂时没有会话");
				System.out.println("暂时没有会话");
			}
		}else {
			reports_BuyCarTest.log(LogStatus.INFO, "点击详情页的IM入口没有进入我的会话列表。");
			System.out.println("点击详情页的IM入口没有进入我的会话列表。");
		}
	}
	
	/**
	 * @Name 2500_IM_unread_mesg_XQ
	 * @Subcatalog 买车-详情页
	 * @Grade 高级
	 * @FunctionPoint 买车-详情页，点击IM入口,如果有未读消息，进入消息中心，查看是否显示正确，
	 */
	@Test
	public void test_2500_IM_unread_mesg_XQ() {
		reports_BuyCarTest.startTest("test_2500_IM_unread_mesg_XQ");
		gotocate(2);
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "进入详情页");
		toDetail(1);
		if(CheckViewVisibilty(By.id("tvChatNum"))) {
			reports_BuyCarTest.log(LogStatus.INFO, "买车详情页的IM入口有未读消息");
			clickElementById("rlChat");
			wait(1);
			if(CheckViewVisibilty(By.id("tv_reddot"))) {
				reports_BuyCarTest.log(LogStatus.INFO, "消息中心也有未读消息，显示正常");
				System.out.println("消息中心也有未读消息，显示正常");
				reports_BuyCarTest.log(LogStatus.INFO, "进入未读消息");
				clickElementById("tv_reddot");
				wait(1);
				driver.pressKeyCode(AndroidKeyCode.BACK);
				reports_BuyCarTest.log(LogStatus.INFO, "消息中心有未读消息，显示正常。");
				System.out.println("消息中心有未读消息，显示正常。");
//					wait(1);
//					if(CheckViewVisibilty(By.id("unread_msg_number"))) {
//						reports_BuyCarTest.log(LogStatus.ERROR, "未读消息已经被阅读但我的会话仍然显示未读消息，请检查");
//						GetScreenshot("test_2500_IM_unread_mesg_1");
//						failAndMessage("未读消息已经被阅读但我的会话仍然显示未读消息，请检查");
//					}else {
//						driver.pressKeyCode(AndroidKeyCode.BACK);
//						if(CheckViewVisibilty(By.id("tvChatNum"))) {
//							reports_BuyCarTest.log(LogStatus.ERROR, "未读消息已经被阅读但详情页的IM入口仍然显示未读消息，请检查");
//							GetScreenshot("test_2500_IM_unread_mesg_2");
//							failAndMessage("未读消息已经被阅读但详情页的IM入口仍然显示未读消息，请检查");
//						}
//					}
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "我的会话没有未读消息，但是详情页的IM入口有未读消息红点，显示异常，请检查。");
				failAndMessage("我的会话没有未读消息，但是详情页的IM入口有未读消息红点，显示异常，请检查。");
			}
		}else {
			reports_BuyCarTest.log(LogStatus.INFO, "详情页的IM入口没有未读消息");
			System.out.println("详情页的IM入口没有未读消息");
		}
	}
	
	/**
	 * @Name 2500_XQ_CHAT
	 * @Subcatalog 买车-详情页
	 * @Grade 高级
	 * @FunctionPoint 买车-详情页，点击在线咨询，检查在线聊天
	 */
	@Test
	public void test_2500_XQ_CHAT() {
		reports_BuyCarTest.startTest("test_2500_XQ_CHAT");
		gotocate(2);
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "进入车辆详情页");
		toDetail(1);
		if(findElementById("tvOnlineService").getText().equals("在线咨询")) {
			clickElementById("tvOnlineService");
			wait(1);
			checkTitlebar1("优信二手车");
			if(CheckViewVisibilty(By.id("bubble"))) {
				clickElementById("tvCarWholeName");
				wait(1);
				checkTitlebar_Detail();
				driver.pressKeyCode(AndroidKeyCode.BACK);
				if(CheckViewVisibilty(By.id("btn_set_mode_voice"))&&
						CheckViewVisibilty(By.id("et_sendmessage"))&&
						CheckViewVisibilty(By.id("rl_face"))&&
						CheckViewVisibilty(By.id("btn_more"))) {
					reports_BuyCarTest.log(LogStatus.INFO, "点击在线咨询进入的聊天页面显示正常。");
					System.out.println("点击在线咨询进入的聊天页面显示正常。");
				}
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "点击在线咨询进入的聊天页面显示异常，请检查。");
				GetScreenshot("test_2500_XQ_CHAT");
				failAndMessage("点击在线咨询进入的聊天页面显示异常，请检查。");
			}
		}
	}
	
//	/**
//	 * @Name 2500_XQ_GZ
//	 * @Subcatalog 买车-详情页
//	 * @Grade 高级
//	 * @FunctionPoint 买车-详情页，关注列表页
//	 */
//	@Test
//	public void test_2500_XQ_GZ() {
//		reports_BuyCarTest.startTest("test_2500_XQ_GZ");
//		gotocate(2);
//		wait(1);
//		reports_BuyCarTest.log(LogStatus.INFO, "点击列表页的第1辆车");
//		toDetail(1);
//		if(CheckViewVisibilty(By.id("tvCarListNum"))) {
//			reports_BuyCarTest.log(LogStatus.INFO, "进入看车列表，清空");
//			clickElementById("llSecond");
//			wait(4);
//			clickElementById("iv_edit");
//			clickElementByName("全选");
//			clickElementById("tv_commit");  //删除
//			wait(1);
//			if(CheckViewVisibilty(By.id("tvDialogMessage"))) {  //请确认删除所选车源
//				clickElementById("bt_confirm_ok");
//			}
//			backBTN();
//			wait(1);
//		}
//		driver.pressKeyCode(AndroidKeyCode.BACK);
//		wait(1);
//		reports_BuyCarTest.log(LogStatus.INFO, "点击列表页的第二辆车（全国直购）");
//		toDetail(2);
//		reports_BuyCarTest.log(LogStatus.INFO, "点击关注");
//		clickElementById("rlFocus");
//		wait(1);
//		reports_BuyCarTest.log(LogStatus.INFO, "进入看车列表");
//		clickElementById("llSecond");
//		wait(4);
//		
//	}
	
//	/**
//	 * @Name 2500_Baicheng_XQ_QGZG_YYKC
//	 * @Subcatalog 买车-百城-详情页
//	 * @Grade 高级
//	 * @FunctionPoint 买车-百城-详情页，在关注列表页里留咨按钮文案显示依次为：我要优惠、申请贷款、电话客服和在线咨询
//	 */
//	@Test
//	public void test_2500_Baicheng_XQ_QGZG_YYKC() {
//		try {
//			reports_BuyCarTest.startTest("test_2500_Baicheng_XQ_QGZG_YYKC");
//			reports_BuyCarTest.log(LogStatus.INFO, "去车市页");
//			gotocate(2);
//			wait(1);
//			reports_BuyCarTest.log(LogStatus.INFO, "切换城市到蚌埠");
//			clickElementById("btChooseCity");
//			wait(3);
////			int width = driver.manage().window().getSize().width;
////			int height = driver.manage().window().getSize().height;
////			driver.swipe(width/2, height*3/4, width/2, height*2/4, 1000);
////			wait(1);
//			clickElementByName("蚌埠");
//			wait(2);
//			reports_BuyCarTest.log(LogStatus.INFO, "点击列表页的第1辆车");
//			toDetail(1);
//			if(CheckViewVisibilty(By.id("tvCarListNum"))) {
//				reports_BuyCarTest.log(LogStatus.INFO, "进入看车列表，清空");
//				clickElementById("llSecond");
//				wait(4);
//				clickElementById("iv_edit");
////				clickElementById("iv_check");
//				clickElementByName("全选");
//				clickElementById("tv_commit");  //删除
//				wait(1);
//				if(CheckViewVisibilty(By.id("tvDialogMessage"))) {  //请确认删除所选车源
//					clickElementById("bt_confirm_ok");
//				}
//				backBTN();
//				wait(1);
//			}
//			reports_BuyCarTest.log(LogStatus.INFO, "点击关注");
//			clickElementById("tvFocus");
//			wait(1);
//			reports_BuyCarTest.log(LogStatus.INFO, "进入看车列表");
//			clickElementById("llSecond");
//			wait(4);
//			if(CheckViewVisibilty(By.id("tvDiscounts"))&& //我要优惠
//					CheckViewVisibilty(By.id("tvLoans"))&& //申请贷款
//					CheckViewVisibilty(By.id("tvOrder"))&& //电话客服
//					CheckViewVisibilty(By.id("tvService"))) {  //在线咨询
//				reports_BuyCarTest.log(LogStatus.INFO, "百城蚌埠城市的关注列表中有留咨入口。");
//				System.out.println("百城蚌埠城市的关注列表中有留咨入口。");
//				if(findElementById("tvOrder").getText().equals("电话客服")) {
//					reports_BuyCarTest.log(LogStatus.INFO, "百城蚌埠城市的关注列表中留咨入口显示正常。");
//					System.out.println("百城蚌埠城市的关注列表中留咨入口显示正常。");
//				}else {
//					reports_BuyCarTest.log(LogStatus.INFO, "百城蚌埠城市的关注列表中留咨入口显示异常为：" + findElementById("tvOrder").getText());
//					GetScreenshot("test_2500_Baicheng_XQ_QGZG_YYKC_1");
//					failAndMessage("百城蚌埠城市的关注列表中留咨入口显示异常为：" + findElementById("tvOrder").getText());
//				}
//			}else {
//				reports_BuyCarTest.log(LogStatus.INFO, "百城蚌埠城市的关注列表中没有留咨入口");
//				GetScreenshot("test_2500_Baicheng_XQ_QGZG_YYKC_2");
//				failAndMessage("百城蚌埠城市的关注列表中没有留咨入口");
//			}
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);			
//		} finally {
////			driver.pressKeyCode(AndroidKeyCode.BACK);
////			wait(1);
////			clickElementById("btChooseCity");
////			wait(3);
////			clickElementByName("北京");
//		}
//	}
	
	/**
	 * @Name 2500_Baicheng_XQ
	 * @Subcatalog 买车-百城-详情页
	 * @Grade 高级
	 * @FunctionPoint 买车-百城-详情页，检查百城车辆详情页的底部按钮，查看头部顶图和详情页图片的大图，查看大图底部按钮
	 */
	@Test
	public void test_2500_Baicheng_XQ() {
		try {
			reports_BuyCarTest.startTest("test_2500_Baicheng_XQ");
			gotocate(2);
			wait(1);
			reports_BuyCarTest.log(LogStatus.INFO, "切换城市到蚌埠");
			clickElementById("btChooseCity");
			wait(3);
			int width = driver.manage().window().getSize().width;
			int height = driver.manage().window().getSize().height;
//			driver.swipe(width/2, height*3/4, width/2, height*1/4, 1000);
//			wait(1);
			clickElementByName("北京");
			wait(2);
			reports_BuyCarTest.log(LogStatus.INFO, "点击列表页的第1辆车");
			toDetail(1);
			if(CheckViewVisibilty(By.id("rlFocus"))&&
//					CheckViewVisibilty(By.id("rlRight"))&&
					CheckViewVisibilty(By.id("rlOnlineService"))&&
					findElementById("tvAbTest").getText().equals("我要优惠")) {
				reports_BuyCarTest.log(LogStatus.INFO, "百城车辆详情页的底部按钮显示正常。");
				System.out.println("百城车辆详情页的底部按钮显示正常。");
			}else {
				reports_BuyCarTest.log(LogStatus.INFO, "百城车辆详情页的底部按钮显示异常，请检查。");
				failAndMessage("百城车辆详情页的底部按钮显示异常，请检查。");
			}
			reports_BuyCarTest.log(LogStatus.INFO, "点击头部顶图，查看大图");
			clickElementById("rlVehicleDetailsTop");
			wait(2);
			if (CheckViewVisibilty(MobileBy.AndroidUIAutomator("text(\""+"外观"+"\")"))&&
					CheckViewVisibilty(MobileBy.AndroidUIAutomator("text(\""+"内饰"+"\")"))&&
					CheckViewVisibilty(MobileBy.AndroidUIAutomator("text(\""+"细节"+"\")"))) {
				clickElementByName("外观");
				wait(1);
				clickElementById("ivItemPic");
				reports_BuyCarTest.log(LogStatus.INFO, "点击外观图片进入大图页面 ");
				waitForVisible(By.id("ivVehicleGallery"), 3);
				driver.pressKeyCode(AndroidKeyCode.BACK);
				reports_BuyCarTest.log(LogStatus.INFO, "点击返回到图片页");
				if(findElementById("tvBargin").getText().equals("在线咨询")) {
					reports_BuyCarTest.log(LogStatus.INFO, "百城车辆的顶图大图底部在线咨询通栏显示正常。");
					clickElementByName("在线咨询");
					wait(2);
					checkTitlebar1("优信二手车");
					if(CheckViewVisibilty(By.id("iv_userhead"))) {
						reports_BuyCarTest.log(LogStatus.INFO, "点击在线咨询，进入客服聊天的页面。");
						System.out.println("点击在线咨询，进入客服聊天的页面。");
						driver.pressKeyCode(AndroidKeyCode.BACK);
						wait(1);
					}else {
						failAndMessage("点击头图的在线咨询按钮没有进入客服聊天页面");
					}
				}else {
					GetScreenshot("test_2500_Baicheng_XQ_Chat_1");
					failAndMessage("头图底部没有在线咨询按钮");
				}
				wait(2);
				driver.pressKeyCode(AndroidKeyCode.BACK);
				wait(2);
				driver.swipe(width/2, height*6/7, width/2, height*1/7, 1000);
				wait(2);
				reports_BuyCarTest.log(LogStatus.INFO, "滑动到详情页的车图时进行点击，进入大图");
				clickElementByName("车图");
				clickElementById("ivChildItem");
				wait(1);
				if(findElementById("tvBargin").getText().equals("在线咨询")) {
					reports_BuyCarTest.log(LogStatus.INFO, "从百城车辆的详情页进入的大图底部在线咨询通栏显示正常。");
					clickElementByName("在线咨询");
					checkTitlebar1("优信二手车");
					if(CheckViewVisibilty(By.id("iv_userhead"))) {
						reports_BuyCarTest.log(LogStatus.INFO, "点击在线咨询，进入客服聊天的页面。");
						driver.pressKeyCode(AndroidKeyCode.BACK);
						wait(1);
					}else {
						failAndMessage("点击头图的在线咨询按钮没有进入客服聊天页面");
					}
				}else {
					GetScreenshot("test_2500_Baicheng_XQ_Chat_2");
					failAndMessage("头图底部没有在线咨询按钮");
				}
				driver.pressKeyCode(AndroidKeyCode.BACK);
				wait(1);
				driver.pressKeyCode(AndroidKeyCode.BACK);
				wait(1);
			}else {
				failAndMessage("图片详情页没有显示3个tab");
			}
			
		} finally {
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
//			clickElementById("btChooseCity");
//			wait(3);
//			clickElementByName("北京");
		}
	}
	/**
	 * @Name 2500_SPJC_01
	 * @Category 买车-详情页-视频检测车辆
	 * @Grade 高级
	 * @FunctionPoint 买车-详情页-视频播放头图页面UI，检查头图页面的标题和车价是否与详情页一致
	 */
	@Test
	public void test_2500_SPJC_01(){
		reports_BuyCarTest.startTest("test_2500_SPJC_01");
		gotocate(2);
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "筛选视频车辆操作");
		clickElementByName("筛选");
		checkTitlebar1("高级筛选");//跳转筛选页成功
		wait(1);
		clickElementByName("优信服务");
		wait(1);
		clickElementByName("视频检测");
		sleep(500);
		clickElementById("ll_search");
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "点击第一辆视频车辆");
//		driver.findElement(MobileBy.xpath("//android.support.v7.widget.RecyclerView[@resource-id='com.uxin.usedcar:id/ptrListViewMarket']"+
//			"/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]"+
//			"/android.widget.ImageView[2]")).click();//点击第一辆视频车辆
		toDetail(1);
		checkTitlebar_Detail();//详情页检测
		String carname = findElementById("tvVehicleDetailsCarName").getText();
		if(carname.substring(0, 4).equals("今日特惠")) {
			carname = carname.substring(5, carname.length());
		}
		String carprice = findElementById("tvVehicleDetailsPrice").getText();
		System.out.println("carprice="+carprice);
		System.out.println("carname="+carname);
		if (CheckViewVisibilty(By.id("imVideoIconVehicleDetail"))) {
			reports_BuyCarTest.log(LogStatus.INFO, "视频图标存在,点击播放视频");
			clickElementById("imVideoIconVehicleDetail");
			wait(2);
			String viedoName = findElementById("tv_realpicture_title").getText();
			String viedoCarName = viedoName.substring(0, viedoName.lastIndexOf(" "));
			String viedoPrice = viedoName.substring(viedoName.lastIndexOf(" ")+1, viedoName.length());
//			String xiangcarname = findElementById("tv_CarName").getText();
//			String xiangprice = findElementById("tv_CarPrice").getText();
			System.out.println("viedoCarName= "+viedoCarName+"|| viedoPrice= "+viedoPrice);
			if (carname.equals(viedoCarName)&&viedoPrice.equals(carprice)) {
				reports_BuyCarTest.log(LogStatus.INFO, "详情页车辆名称和视频播放头图页车辆名称一致且车价也一致");
				System.out.println("详情页车辆名称和视频播放头图页车辆名称一致且车价也一致");
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "详情页车辆名称和视频播放头图页车辆名称或者车价存在不一致，请检查");
				failAndMessage("详情页的车辆名称和车价为："+carname +"," + carprice + "||头图播放页面的车辆名称和车价为：" + viedoCarName + "," + viedoPrice);
			}
		}else {
			reports_BuyCarTest.log(LogStatus.INFO, "高级筛选筛选视频检测类车辆，没有视频图标，请检查原因");
			GetScreenshot("test_2500_SPJC_01");
			failAndMessage("高级筛选筛选视频检测类车辆，没有视频图标，请检查原因");
		}	 
	}
	
	/**
	 * @Name 2500_SPJCBG_01
	 * @Category 买车-详情页-视频检测车辆-查看检测报告
	 * @Grade 高级
	 * @FunctionPoint 买车-详情页-视频播放头图页面UI检测, 整车视频、分段视频以及节点视频和检测报告的UI
	 */
	@Test
	public void test_2500_SPJCBG_01(){
		reports_BuyCarTest.startTest("test_2500_SPJCBG_01");
		gotocate(2);
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "筛选视频车辆操作");
		clickElementByName("筛选");
		checkTitlebar1("高级筛选");//跳转筛选页成功
		wait(1);
		clickElementByName("优信服务");
		wait(1);
		clickElementByName("视频检测");
		sleep(500);
		clickElementById("ll_search");
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "点击第一辆视频车辆");
//		driver.findElement(MobileBy.xpath("//android.support.v7.widget.RecyclerView[@resource-id='com.uxin.usedcar:id/ptrListViewMarket']"+
//			"/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]"+
//			"/android.widget.ImageView[2]")).click();//点击第一辆视频车辆
		toDetail(1);
		checkTitlebar_Detail();//详情页检测
		String[] strVideo = {"车头", "发动机舱", "右前车身", "右前内饰", "右后车身", "右后内饰", 
				"车尾", "后备箱内部", "左后车身", "左后内饰", "左前车身", "左前内饰"};
		if (CheckViewVisibilty(By.id("imVideoIconVehicleDetail"))) {
			clickElementById("imVideoIconVehicleDetail");
			reports_BuyCarTest.log(LogStatus.INFO, "视频图标存在,点击播放视频");
			wait(2);
			if (findElementById("tvAllCar").getText().equals("整车视频")) {
				reports_BuyCarTest.log(LogStatus.INFO, "整车视频按钮显示正常");
				System.out.println("整车视频按钮显示正常");
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "整车视频按钮显示异常，请检查");
				GetScreenshot("test_2500_SPJCBG_01_1");
				failAndMessage("整车视频按钮显示异常，请检查");
			}
			for(int i=0; i<strVideo.length; i++) {
				clickElementByName(strVideo[i]);
				wait(1);
				reports_BuyCarTest.log(LogStatus.INFO, "点击分段视频："+ strVideo[i]);
				System.out.println("点击分段视频："+ strVideo[i]);
			}
			if (CheckViewVisibilty(By.id("check_report"))) {  //检测报告
				reports_BuyCarTest.log(LogStatus.INFO, "点击视频检测报告");
				wait(1);
				clickElementById("check_report");
				wait(1);
				String[] str = {"事故排查","轻微碰撞","易损耗部件","常用功能","启动检测","外观内饰骨架", "漆面检测","瑕疵及修复"};
				for (int i = 0; i < str.length; i++) {
					findElementByName(str[i]).click();
					wait(1);
				}
			}	
		}else {
			reports_BuyCarTest.log(LogStatus.INFO, "高级筛选筛选视频检测类车辆，没有视频图标，请检查原因");
			GetScreenshot("test_2500_SPJCBG_01_2");
			failAndMessage("高级筛选筛选视频检测类车辆，没有视频图标，请检查原因");
		}	 
	}
	
	/**
	 * @Name 2500_XQ_SP_Little_to_Big
	 * @Catalogue 车市页-详情页-视频
	 * @Grade 中级
	 * @FunctionPoint 车市页-详情页-视频,点击缩略图到大屏
	 */
	@Test
	public void test_2500_XQ_SP_Little_to_Big() {
		reports_BuyCarTest.startTest("test_2500_XQ_SP_Little_to_Big");
		gotocate(2);
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "筛选视频车辆操作");
		clickElementByName("筛选");
		checkTitlebar1("高级筛选");//跳转筛选页成功
		wait(1);
		clickElementByName("优信服务");
		wait(1);
		clickElementByName("视频检测");
		sleep(500);
		clickElementById("ll_search");
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "点击第一辆视频车辆");
		toDetail(1);
		sliding("up");
		reports_BuyCarTest.log(LogStatus.INFO, "点击导航栏的检测tab");
		clickElementByName("检测");
		if(CheckViewVisibilty(By.id("img_center_start"))) {
			clickElementById("img_center_start");
			reports_BuyCarTest.log(LogStatus.INFO, "点击常规屏的播放视频按钮");
			System.out.println("点击常规屏的播放视频按钮");			
			wait(2);
			sliding("up");
			wait(1);
			sliding("up");
			wait(1);
			if(CheckViewVisibilty(By.id("flCheckVideoSmall"))) {
				clickElementById("flCheckVideoSmall");
				reports_BuyCarTest.log(LogStatus.INFO, "点击小屏");
				System.out.println("点击小屏");
				wait(1);
				driver.pressKeyCode(AndroidKeyCode.BACK);
				wait(1);
				if(CheckViewVisibilty(By.id("flCheckVideoSmall"))) {  //因全屏的控件元素获取不到，所以用返回到小屏的方式检查
					reports_BuyCarTest.log(LogStatus.INFO, "点击小屏进入全屏，功能正常。");
					System.out.println("点击小屏进入全屏，功能正常。");
				}else {
					reports_BuyCarTest.log(LogStatus.ERROR, "点击小屏没有进入全屏，请检查");
					failAndMessage("点击小屏没有进入全屏，请检查");
				}
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "详情页点击常规屏视频播放之后，滑动出常规屏之后没有出现小屏，请检查。");
				failAndMessage("详情页点击常规屏视频播放之后，滑动出常规屏之后没有出现小屏，请检查。");
			}
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "高级筛选的视频车辆，检测视频处没有视频播放按钮。");
			failAndMessage("高级筛选的视频车辆，检测视频处没有视频播放按钮。");
		}
	}
	
	/**
	 * @Name 2704_search
	 * @Catalogue 车市页-搜索
	 * @Grade 中级
	 * @FunctionPoint 车市页-搜索功能-列表页带筛选条件搜索
	 */
	@Test
	public void test_2704_search() {
		reports_BuyCarTest.startTest("test_2704_search");
		gotocate(2);
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "进入付一半列表");
		clickFQG();
		wait(2);
		reports_BuyCarTest.log(LogStatus.INFO, "点击搜索框");
		clickElementById("tv_search");
		inputById("etSearchText", "奥迪");
		reports_BuyCarTest.log(LogStatus.INFO, "搜索奥迪");
		driver.pressKeyCode(AndroidKeyCode.ENTER);
		wait(2);
		if (findElementById("tv_pop_content").getText().equals("奥迪")) {
			
		}else {
			GetScreenshot("test_2704_search");
			failAndMessage("在付一半列表中搜索奥迪失败");
			reports_BuyCarTest.log(LogStatus.ERROR,"在付一半列表中搜索奥迪失败");
		}
	}
	
	/**
	 * @Name 2500_NoReason
	 * @Catalogue 车市页-高级筛选-三天无理由
	 * @Grade 中级
	 * @FunctionPoint 车市页-高级筛选-三天无理由
	 */
	@Test
	public void test_2500_NoReason() {
		reports_BuyCarTest.startTest("test_2500_NoReason");
		gotocate(2);
		wait(2);
		clickElementByName("筛选");
		wait(1);
		clickElementByName("优信服务");
		wait(1);
		clickElementByName("三天无理由");
		clickElementById("ll_search");
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "点击高级筛选，筛选三天无理由");
		toDetail(1);
		reports_BuyCarTest.log(LogStatus.INFO, "进入筛选列表的第一辆车");
		if(CheckViewVisibilty(By.id("tvWithdraw"))) {
			if(findElementById("tvWithdraw").getText().equals("三天无理由退车")) {
				reports_BuyCarTest.log(LogStatus.INFO, "高级筛选筛选三天无理由的车显示正常。");
				System.out.println("高级筛选筛选三天无理由的车显示正常。");
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "高级筛选三天无理由的车辆不是三天无理由的车，请检查");
				GetScreenshot("test_2500_NoReason_1");
				failAndMessage("高级筛选三天无理由的车辆不是三天无理由的车，请检查");
			}
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "高级筛选三天无理由的车辆不是三天无理由的车，请检查");
			GetScreenshot("test_2500_NoReason_2");
			failAndMessage("高级筛选三天无理由的车辆不是三天无理由的车，请检查");
		}
	}
	
	/**
	 * @Name 2500_NoReason
	 * @Catalogue 车市页-搜该款车的人还搜过
	 * @Grade 中级
	 * @FunctionPoint 车市页-搜该款车的人还搜过
	 */
	@Test
	public void test_2500_search_list() {
		reports_BuyCarTest.startTest("test_2500_search_list");
		gotocate(2);
		wait(1);
		clickElementByName("品牌");
		wait(1);
		clickElementByName("大众");
		wait(1);
		clickElementByName("帕萨特");
		wait(3);
		reports_BuyCarTest.log(LogStatus.INFO, "筛选品牌车系");
//		sliding("up");
//		wait(1);
//		clickElementByName("3年以内");
//		wait(3);
//		reports_BuyCarTest.log(LogStatus.INFO, "选择3年以内");
		swipeUntilElementAppear(By.id("recyclerview_similar_series"), "up", 11);   //搜该款车的人还搜过
		String carName1 = findElementById("tv_car_series").getText();
		String carNameStr1 = carName1.split(" ")[0];
		String carNameStr2 = carName1.split(" ")[1];
		System.out.println(carNameStr1+";"+carNameStr2);
		clickElementById("tv_car_series");
		wait(2);
//		if(findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[2]/android.view.ViewGroup[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]/android.widget.HorizontalScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText().equals(carNameStr1)&&
//				findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[2]/android.view.ViewGroup[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]/android.widget.HorizontalScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.TextView[1]").getText().equals(carNameStr2)) {
		if(findElementById("tv_pop_content").getText().equals(carNameStr1)) {
			reports_BuyCarTest.log(LogStatus.INFO, "点击卡片将品牌车系代入到了搜索框气泡");
			System.out.println("点击卡片将品牌车系代入到了搜索框气泡");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "点击卡片没有将品牌车系代入到了搜索框气泡，请检查");
			failAndMessage("点击卡片没有将品牌车系代入到了搜索框气泡，请检查");
		}
	}
	
	/**
	 * @Name 2500_NoReason
	 * @Catalogue 车市页-找相似
	 * @Grade 中级
	 * @FunctionPoint 车市页-找相似
	 */
	@Test
	public void test_2500_ZXS() {
		reports_BuyCarTest.startTest("test_2500_ZXS");
		gotocate(2);
		wait(2);
		if(CheckViewVisibilty(By.id("lookupsimilarcar"))) {
			clickElementById("lookupsimilarcar");
			wait(1);
			checkTitlebar1("找相似");
			List<WebElement> carname = driver.findElements(By.id("tvCarWholeName"));
			String firstCarBrand = carname.get(0).getAttribute("text").split(" ")[0];
			String firstCarSerie = carname.get(0).getAttribute("text").split(" ")[1];
			String secondCarBrand = carname.get(1).getAttribute("text").split(" ")[0];
			String secondCarSerie = carname.get(1).getAttribute("text").split(" ")[1];
			if(CheckViewVisibilty(By.id("tv_similar_car_count"))) {
				if (firstCarBrand.equals(secondCarBrand)&&firstCarSerie.equals(secondCarSerie)) {
					System.out.println("第一辆车："+firstCarBrand+">>>"+firstCarSerie);
					System.out.println("第二辆车："+secondCarBrand+">>>"+secondCarSerie);
					reports_BuyCarTest.log(LogStatus.INFO, "找到相似的车辆");
					System.out.println("找到相似的车辆");
					swipeUntilElementAppear(By.id("tv_similar_car_bottom_notice"), "up", 3);
					if(findElementById("tv_similar_car_bottom_notice").getText().equals("我也是有底线的哦")) {
						reports_BuyCarTest.log(LogStatus.INFO, "找相似页面滑到底部，文案显示正确");
						System.out.println("找相似页面滑到底部，文案显示正确");
					}else {
						reports_BuyCarTest.log(LogStatus.ERROR, "未找到找相似页面底部文案");
						failAndMessage("未找到找相似页面底部文案");
					}
				}else {
					GetScreenshot("test_2500_ZXS");
					reports_BuyCarTest.log(LogStatus.ERROR, "相似的第一辆车的品牌和车系与本车的不同，请人工核查");
					failAndMessage("相似的第一辆车的品牌和车系与本车的不同，请人工核查");
				}
				
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "找相似页面没有<发现N个相似车源>的提示文案");
				failAndMessage("找相似页面没有<发现N个相似车源>的提示文案");
			}
		}else {
			reports_BuyCarTest.log(LogStatus.INFO, "本方案的买车页没有找相似");
			System.out.println("本方案的买车页没有找相似");
		}
	}

	/**
	 * @Name 2500_BWZC_img
	 * @Catalogue 车市页-帮我找车
	 * @Grade 中级
	 * @FunctionPoint 车市页-帮我找车 屏幕滑动6屏左右 默认车市页 点击帮我找车图片跳转是否正确。
	 */
	@Test
	public void test_2500_BWZC_img(){
		reports_BuyCarTest.startTest("test_2500_BWZC_img");
		gotocate(2);
		wait(2);
		swipeUntilElementAppear(By.id("rlWishCarIMContainer"), "up", 6);
		wait(1);
		clickElementById("rlWishCarIMContainer");
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "点击在线咨询帮我找车IM图片");
		checkTitlebar1("优信二手车");
		if(CheckViewVisibilty(By.id("iv_userhead"))&&
				CheckViewVisibilty(By.id("tv_chatcontent"))) {
			reports_BuyCarTest.log(LogStatus.INFO, "点击列表页IM帮我找车，跳转成功。");
			System.out.println("点击列表页IM帮我找车，跳转成功。");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "点击列表页IM帮我找车，跳转失败，请检查。");
			failAndMessage("点击列表页IM帮我找车，跳转失败，请检查。");
		}
	}

	
	/**
	 * @Name 2501_GJSX_Engine
	 * @Catalogue 车市页-高级筛选-发动机
	 * @Grade 中级
	 * @FunctionPoint 车市页-高级筛选-发动机，遍历发动机筛选
	 */
	@Test
	public void test_2501_GJSX_Engine() {
		reports_BuyCarTest.startTest("test_2501_GJSX_Engine");
		gotocate(2);
		wait(1);
		String[] strings = {"自然吸气", "涡轮增压", "机械增压", "电动机", "混合动力"};
		for(int i = 0; i< strings.length; i++) {
			clickElementByName("筛选");
			reports_BuyCarTest.log(LogStatus.INFO, "点击高级筛选");
			wait(2);
			if(CheckViewVisibilty(By.id("resetinfo_lly"))) {
				clickElementById("resetinfo_lly");
				sleep(500);
				reports_BuyCarTest.log(LogStatus.INFO, "存在重置按钮，先点击重置");
			}
			clickElementByName("发动机");
			wait(1);
//			if(CheckViewVisibilty(By.id("resetinfo_lly"))) {
//				clickElementById("resetinfo_lly");
//				System.out.println("点击重置");
//			}
			clickElementByName(strings[i]);
			sleep(500);
			System.out.println("点击发动机：" + strings[i]);
			reports_BuyCarTest.log(LogStatus.INFO, "点击发动机：" + strings[i]);
			if(findElementById("advanced_search_counttv").getText().equals("暂无符合车辆，看看推荐")) {
				backBTN();
			}else {
				clickElementById("advanced_search_counttv");
				reports_BuyCarTest.log(LogStatus.INFO, "点击找到XXX辆车");
				wait(2);
				toDetail(1);
				int width = driver.manage().window().getSize().width;
				int height = driver.manage().window().getSize().height;
				driver.swipe(width/2, height*3/4, width/2, height/4, 1000);
				wait(1);
				if(CheckViewVisibilty(By.id("engine_type"))){
					if(findElementById("engine_type").getText().equals(strings[i])||
							findElementById("engine_type").getText().contains(strings[i])) {
						reports_BuyCarTest.log(LogStatus.INFO, "高筛发动机:"+strings[i]+",功能正常");
						System.out.println("高筛发动机:"+strings[i]+",功能正常");
					}else {
						reports_BuyCarTest.log(LogStatus.ERROR, "高筛发动机:"+strings[i]+",功能异常，请检查");
						failAndMessage("高筛发动机:"+strings[i]+",功能异常，请检查,详情页为："+findElementById("engine_type").getText());
					}
				}else {
					reports_BuyCarTest.log(LogStatus.ERROR, "详情页车辆档案处没有发动机信息，请检查");
					GetScreenshot("test_2501_GJSX_Engine");
					failAndMessage("详情页车辆档案处没有发动机信息，请检查");
				}
				backBTN();
			}
		}
	}
	
	/**
	 * @Name 2501_GJSX_price
	 * @Catalogue 车市页-高级筛选-车价
	 * @Grade 中级
	 * @FunctionPoint 车市页-高级筛选-车价，遍历车价筛选
	 */
	@Test
	public void test_2501_GJSX_price() {
		reports_BuyCarTest.startTest("test_2501_GJSX_price");
		gotocate(2);
		wait(1);
		String[] str = {"5万以下", "5-10万", "10-15万", "15-20万", "20-30万", "30-50万", "50万以上"};
		String[] str_pop = {"5万以下", "5-10万元", "10-15万元", "15-20万元", "20-30万元", "30-50万元", "50万以上"};
		for(int i = 0; i< str.length; i++) {
			clickElementByName("筛选");
			reports_BuyCarTest.log(LogStatus.INFO, "点击高级筛选");
			wait(1);
			clickElementByName("车价");
			wait(1);
			if(CheckViewVisibilty(By.id("resetinfo"))) {
				clickElementById("resetinfo");
				reports_BuyCarTest.log(LogStatus.INFO, "存在重置按钮，先点击重置");
			}
			clickElementByName(str[i]);
			reports_BuyCarTest.log(LogStatus.INFO, "点击车价：" + str[i]);
			wait(1);
			clickElementById("advanced_search_counttv");
			reports_BuyCarTest.log(LogStatus.INFO, "点击找到XXX辆车");
			wait(2);
			if(findElementById("tv_pop_content").getText().equals(str_pop[i])) {
				reports_BuyCarTest.log(LogStatus.INFO, "点击高级筛选里面的对应的车价，列表页气泡显示正确");
				System.out.println("点击高级筛选里面的对应的车价，列表页气泡显示正确");
//				clickElementByName("车价");
//				reports_BuyCarTest.log(LogStatus.INFO, "点击列表页的车价按钮");
//				if(CheckViewVisibilty(By.id("tvPriceSelected"))) {
//					if(findElementById("tvPriceSelected").getText().equals(str[i])) {
//						reports_BuyCarTest.log(LogStatus.INFO, "高级筛选的车价同步到了车价筛选中，显示正常");
//						System.out.println("高级筛选的车价同步到了车价筛选中，显示正常");
//					}else {
//						reports_BuyCarTest.log(LogStatus.ERROR, "高级筛选的车价没有同步到车价筛选中，显示异常，请查看");
//						failAndMessage("高级筛选的车价没有同步到车价筛选中，显示异常，请查看");
//					}
//				}else {
//					reports_BuyCarTest.log(LogStatus.ERROR, "高级筛选的车价没有同步到车价筛选中，显示异常，请查看");
//					failAndMessage("高级筛选的车价没有同步到车价筛选中，显示异常，请查看");
//				}
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "点击高级筛选的车价["+str[i]+"]，搜索框气泡显示["+findElementById("tv_pop_content").getText()+"],与之前不一样，请确认需求");
				failAndMessage("点击高级筛选的车价["+str[i]+"]，搜索框气泡显示["+findElementById("tv_pop_content").getText()+"],与之前不一样，请确认需求");
			}
			backBTN();
			wait(1);
			clickElementById("iv_pop_delete");
			wait(2);
			reports_BuyCarTest.log(LogStatus.INFO, "点击气泡，去掉车价筛选");
		}
	}
	
	/**
	 * @Name 2501_GJSX_GPBZ
	 * @Catalogue 车市页-高级筛选-国排标准
	 * @Grade 中级
	 * @FunctionPoint 车市页-高级筛选-国排标准，遍历国排标准筛选
	 */
	@Test
	public void test_2501_GJSX_GPBZ() {
		reports_BuyCarTest.startTest("test_2501_GJSX_GPBZ");
		gotocate(2);
		wait(1);
		String[] strings = {"国三及以上", "国四及以上", "国五"};
		for(int i = 0; i< strings.length; i++) {
			clickElementByName("筛选");
			reports_BuyCarTest.log(LogStatus.INFO, "点击高级筛选");
			wait(1);
			if(CheckViewVisibilty(By.id("resetinfo"))) {
				clickElementById("resetinfo");
				reports_BuyCarTest.log(LogStatus.INFO, "存在重置按钮，先点击重置");
			}
			clickElementByName("排放标准");
			clickElementByName(strings[i]);
			reports_BuyCarTest.log(LogStatus.INFO, "点击排放标准：" + strings[i]);
			if(findElementById("advanced_search_counttv").getText().equals("暂无符合车辆，看看推荐")) {
				backBTN();
			}else {
				clickElementById("advanced_search_counttv");
				reports_BuyCarTest.log(LogStatus.INFO, "点击找到XXX辆车");
				wait(2);
				toDetail(1);
				int width = driver.manage().window().getSize().width;
				int height = driver.manage().window().getSize().height;
				driver.swipe(width/2, height*3/4, width/2, height/4, 1000);
				wait(1);
				if(CheckViewVisibilty(By.id("tvEmissionStandard"))) {
					String string = findElementById("tvEmissionStandard").getText().substring(1, 2);
					if((i==0 && Integer.parseInt(string)>=3)
							||(i==1 && Integer.parseInt(string) >= 4)
							||(i==2 && Integer.parseInt(string)>=5)) {
						reports_BuyCarTest.log(LogStatus.INFO, "高筛排放标准:"+strings[i]+",功能正常");
						System.out.println("高筛排放标准:"+strings[i]+",功能正常");
					}else {
						reports_BuyCarTest.log(LogStatus.ERROR, "高筛排放标准:"+strings[i]+",功能异常，请检查");
						failAndMessage("高筛排放标准:"+strings[i]+",功能异常，请检查");
					}
				}else {
					reports_BuyCarTest.log(LogStatus.ERROR, "详情页车辆档案处没有排放标准信息，请检查");
					GetScreenshot("test_2501_GJSX_GPBZ");
					failAndMessage("详情页车辆档案处没有排放标准信息，请检查");
				}
				backBTN();
			}
		}
	}
	
	/**
	 * @Name 2501_GJSX_color
	 * @Catalogue 车市页-高级筛选-颜色
	 * @Grade 中级
	 * @FunctionPoint 车市页-高级筛选-颜色，遍历颜色筛选
	 */
	@Test
	public void test_2501_GJSX_color() {
		reports_BuyCarTest.startTest("test_2501_GJSX_color");
		gotocate(2);
		wait(1);
		String[] strings = {"黑色", "深灰色", "银灰色", "白色", "香槟色",
				"黄色", "橙色", "红色", "蓝色", "粉红色", "紫色", "咖啡色",
				"绿色", "多彩色", "其它"};
		for(int i = 0; i< strings.length; i++) {
			clickElementByName("筛选");
			reports_BuyCarTest.log(LogStatus.INFO, "点击高级筛选");
			wait(2);
			if(CheckViewVisibilty(By.id("resetinfo_lly"))) {
				clickElementById("resetinfo_lly");
				wait(1);
				reports_BuyCarTest.log(LogStatus.INFO, "存在重置按钮，先点击重置");
			}
			int width = driver.manage().window().getSize().width;
			int height = driver.manage().window().getSize().height;
			driver.swipe(width/9, height*3/5, width/9, height*2/5, 1000);
			wait(1);
			clickElementByName("颜色");
			wait(1);
			clickElementByName(strings[i]);
			sleep(500);
			reports_BuyCarTest.log(LogStatus.INFO, "点击颜色：" + strings[i]);
			if(findElementById("advanced_search_counttv").getText().equals("暂无符合车辆，看看推荐")) {
				backBTN();
			}else {
				clickElementById("advanced_search_counttv");
				reports_BuyCarTest.log(LogStatus.INFO, "点击找到XXX辆车");
				wait(2);
				toDetail(1);
				driver.swipe(width/2, height*3/4, width/2, height/4, 1000);
				wait(1);
				if(CheckViewVisibilty(By.id("tv_car_color"))) {
					if(findElementById("tv_car_color").getText().equals(strings[i])) {
						reports_BuyCarTest.log(LogStatus.INFO, "高筛颜色:"+strings[i]+",功能正常");
						System.out.println("高筛颜色:"+strings[i]+",功能正常");
					}else {
						reports_BuyCarTest.log(LogStatus.ERROR, "高筛颜色:"+strings[i]+",功能异常，请检查");
						failAndMessage("高筛颜色:"+strings[i]+",当前车辆颜色为"+findElementById("tv_car_color").getText()+",功能异常，请检查");
					}
				}else {
					reports_BuyCarTest.log(LogStatus.ERROR, "详情页车辆档案处没有颜色信息，请检查");
					GetScreenshot("test_2501_GJSX_color");
					failAndMessage("详情页车辆档案处没有颜色信息，请检查");
				}
				backBTN();
			}
		}
	}

	/**
	 * @Name 2501_XQ_CJCZ
	 * @Catalogue 详情页-车价超值
	 * @Grade 中级
	 * @FunctionPoint 详情页-车价超值，点击车价超值进入车价分析报告
	 */
	@Test
	public void test_2501_XQ_CJCZ() {
		reports_BuyCarTest.startTest("test_2501_XQ_CJCZ");
		gotocate(2);
		wait(2);
		clickElementByName("筛选");
		reports_BuyCarTest.log(LogStatus.INFO, "点击高级筛选");
		wait(1);
		clickElementByName("优信服务");
		clickElementByName("超值");
		clickElementById("advanced_search_counttv");
		reports_BuyCarTest.log(LogStatus.INFO, "点击找到XXX辆车");
		wait(2);
		toDetail(1);
		if(!CheckViewVisibilty(By.id("carpriceanalyse"))) {
			reports_BuyCarTest.log(LogStatus.INFO, "列表页第一辆超值车没有车价超值按钮，尝试查看第二辆车是否有车价超值按钮");
			System.out.println("列表页第一辆超值车没有车价超值按钮，尝试查看第二辆车是否有车价超值按钮");
			backBTN();
			toDetail(2);
		}
		String carName = findElementById("tvVehicleDetailsCarName").getText();
		if(carName.substring(0, 4).equals("今日特惠")) {
			carName = carName.substring(5, carName.length());
		}
		System.out.println(carName);
		if(CheckViewVisibilty(By.id("carpriceanalyse"))) {
			clickElementById("carpriceanalyse");
			wait(4);
			checkTitlebar_Webview("车价分析报告");
			if(CheckViewVisibilty(By.id("tvPhoneConsult"))&&
					CheckViewVisibilty(By.id("tvAskPrice"))) {
				reports_BuyCarTest.log(LogStatus.INFO, "车价分析报告页面底部按钮显示正常");
				System.out.println("车价分析报告页面底部按钮显示正常");
			}
//			try {
//				switchToWebView();
//			} catch (Exception e) {
//				System.out.println("第一次切换失败尝试再次切换");
//				driver.pressKeyCode(AndroidKeyCode.BACK);
//				wait(1);
//				clickElementById("carpriceanalyse");
//				wait(3);
//				switchToWebView();
//			}
//			String carName1 = findElementByXpath("/html/body/div[1]/span[1]").getText();
//			System.out.println(carName1);
//			if(carName.trim().replace(" ", "").equals(carName1.trim().replace(" ", ""))) {
//				reports_BuyCarTest.log(LogStatus.INFO, "车价分析报告页面车辆标题显示正常");
//				System.out.println("车价分析报告页面车辆标题显示正常");
//			}else {
//				reports_BuyCarTest.log(LogStatus.ERROR, "车价分析报告页面车辆标题显示不正确，请检查");
//				failAndMessage("车价分析报告页面车辆标题显示不正确，请检查");
//			}
//			if(CheckViewVisibilty(By.xpath("/html/body/dl"))) {
//				reports_BuyCarTest.log(LogStatus.INFO, "车价分析报告页面横向车价对比UI显示正常");
//				System.out.println("车价分析报告页面横向车价对比UI显示正常");
//			}else {
//				reports_BuyCarTest.log(LogStatus.ERROR, "车价分析报告页面没有横向车价对比UI，请检查");
//				GetScreenshot("test_2501_XQ_CJCZ_2");
//				failAndMessage("车价分析报告页面没有横向车价对比UI，请检查");
//			}
//			clickElementByXpath("/html/body/div[2]/div[2]/dl/dt");
//			reports_BuyCarTest.log(LogStatus.INFO, "点击车价分析排序的更多按钮");
//			System.out.println("点击车价分析排序的更多按钮");
//			if(findElementByXpath("/html/body/div[2]/div[2]/dl/dd[1]").getText().equals("性价比最高")&&
//					findElementByXpath("/html/body/div[2]/div[2]/dl/dd[2]").getText().equals("车况最优")&&
//					findElementByXpath("/html/body/div[2]/div[2]/dl/dd[3]").getText().equals("车价最低")&&
//					findElementByXpath("/html/body/div[2]/div[2]/dl/dd[4]").getText().equals("车龄最短")&&
//					findElementByXpath("/html/body/div[2]/div[2]/dl/dd[5]").getText().equals("里程最少")) {
//				reports_BuyCarTest.log(LogStatus.INFO, "车价分析报告页面排序UI显示正常");
//				System.out.println("车价分析报告页面排序UI显示正常");
//			}else {
//				reports_BuyCarTest.log(LogStatus.ERROR, "车价分析报告页面排序UI显示异常，请检查");
//				GetScreenshot("test_2501_XQ_CJCZ_1");
//				failAndMessage("车价分析报告页面排序UI显示异常，请检查");
//			}
		}else {
			reports_BuyCarTest.log(LogStatus.INFO, "列表页第二辆超值车依然没有车价超值按钮");
			System.out.println("列表页第二辆超值车依然车没有价超值按钮");
		}
	}
	
	/**
	 * @Name 2501_list_top
	 * @Catalogue 列表页-返回顶部按钮
	 * @Grade 中级
	 * @FunctionPoint 列表页-返回顶部按钮
	 */
	@Test
	public void test_2501_list_top() {
		reports_BuyCarTest.startTest("test_2501_list_top");
		gotocate(2);
		wait(2);
		for (int i = 0; i < 5; i++) {
			sliding("up");
		}
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "列表页下滑3屏并且上滑一点");
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*3/6, width/2, height*5/6, 1000);
		wait(1);
		if(CheckViewVisibilty(By.id("iv_go_top"))) {
			clickElementById("iv_go_top");
			reports_BuyCarTest.log(LogStatus.INFO, "点击列表页返回顶部按钮");
			wait(1);
			if(!CheckViewVisibilty(By.id("iv_go_top"))&&
					CheckViewVisibiltyByName("足迹")) {
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
	 * @Name 2501_XQ_top
	 * @Catalogue 详情页-返回顶部按钮
	 * @Grade 中级
	 * @FunctionPoint 详情页-返回顶部按钮
	 */
	@Test
	public void test_2501_XQ_top() {
		reports_BuyCarTest.startTest("test_2501_XQ_top");
		gotocate(2);
		wait(2);
		toDetail(1);
		sliding("up");
		wait(1);
		sliding("up");
		wait(1);
		if(CheckViewVisibilty(By.id("btTop"))) {
			clickElementById("btTop");
			sleep(500);
			if(CheckViewVisibilty(By.id("rlVehicleDetailsTop"))) {
				reports_BuyCarTest.log(LogStatus.INFO, "详情页点击返回顶部按钮，返回到了顶部，功能正常！");
				System.out.println("详情页点击返回顶部按钮，返回到了顶部，功能正常！");
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "详情页返回顶部按钮功能异常，请检查！");
				failAndMessage("详情页返回顶部按钮功能异常，请检查！");
			}
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "详情页没有返回顶部按钮，请检查！");
			failAndMessage("详情页没有返回顶部按钮，请检查！");
		}
	}
//	/**
//	 * @Name 2500_SPJCBG_02
//	 * @Category 买车-详情页-视频检测车辆-查看检测报告
//	 * @Grade 高级
//	 * @FunctionPoint 买车-详情页-详情页车辆检测报告的文案
//	 */
//	@Test
//	public void test_2500_SPJCBG_02(){
//		reports_BuyCarTest.startTest("test_2500_SPJCBG_02");
//		gotocate(2);
//		wait(1);
//		reports_BuyCarTest.log(LogStatus.INFO, "筛选视频车辆操作");
//		clickElementByName("筛选");
//		checkTitlebar1("高级筛选");//跳转筛选页成功
//		wait(2);
//		clickElementByName("优信服务");
//		wait(1);
//		clickElementByName("视频检测");
//		sleep(500);
//		clickElementById("ll_search");
//		wait(2);
//		reports_BuyCarTest.log(LogStatus.INFO, "点击第一辆视频车辆");
//		toDetail(1);
//		checkTitlebar_Detail();//详情页检测
//		if (CheckViewVisibilty(By.id("imVideoIconVehicleDetail"))) {
//			reports_BuyCarTest.log(LogStatus.INFO, "视频图标存在,点击播放视频");
//			clickElementById("imVideoIconVehicleDetail");
//			wait(2);
//			if (findElementById("tvAllCar").getText().equals("整车视频")) {
//				reports_BuyCarTest.log(LogStatus.INFO, "整车视频按钮显示正常");
//				System.out.println("整车视频按钮显示正常");
//			}else {
//				reports_BuyCarTest.log(LogStatus.ERROR, "整车视频按钮显示异常，请检查");
//				GetScreenshot("test_2500_SPJCBG_01_1");
//				failAndMessage("整车视频按钮显示异常，请检查");
//			}
//			if (CheckViewVisibilty(By.id("check_report"))) {  //检测报告
//				reports_BuyCarTest.log(LogStatus.INFO, "点击视频检测报告");
//				wait(1);
//				clickElementById("check_report");
//				wait(1);
//				String[] str = {"启动检测","外观内饰"};
//				for (int i = 0; i < str.length; i++) {
//					Point  location= findElementById("horizonscrollview").getLocation();
//					int startX = location.getX();
//					int startY = location.getY();
//					System.out.println("开始位置x="+startX+"||"+"开始位置y="+startY);
//					Dimension  q = findElementById("horizonscrollview").getSize();
//					int x =q.getWidth();
//					int y =q.getHeight();
//					System.out.println("控件x长="+x+"||"+"控件y宽="+y);
//					// 计算出控件结束坐标
//					int endX = startX+x;
//					int endY = startY+y;
//					System.out.println("控件结束坐标X："+endX+"控件结束坐标Y："+endY);
//					driver.swipe(endX*9/10, endY-30, endX*1/10, endY-30, 1000);
//					wait(2);
//					String carname = getListByLocator(MobileBy.id("com.uxin.usedcar:id/tv_detail_tab_item"), i+2).getAttribute("name");
//					System.out.println("carname ="+carname);
//					System.out.println("str ="+str[i]);
//					if (str[i].equals(carname)) {
//						System.out.println("检测报告文案检查正确>>>"+carname);
//					}else {
//						System.out.println("检测报告文案检测失败>>>"+carname);
//						failAndMessage("检测报告文案检测失败,请检查原因");
//					}
//				}
//			}
//		}	
//	}
	
	/**
	 * @Name 2500_kefu
	 * @Category 买车-详情页-在线咨询
	 * @Grade 高级
	 * @FunctionPoint 买车-详情页-在线咨询，点击客服头像
	 */
	@Test
	public void test_2500_kefu() {
		reports_BuyCarTest.startTest("test_2500_kefu");
		gotocate(2);
		wait(1);
		toDetail(1);
		reports_BuyCarTest.log(LogStatus.INFO, "进入第一辆车详情页");
		checkTitlebar_Detail();
		clickElementByName("在线咨询");
		reports_BuyCarTest.log(LogStatus.INFO, "点击在线咨询");
		wait(2);
		checkTitlebar1("优信二手车");
		clickElementById("iv_userhead");
		reports_BuyCarTest.log(LogStatus.INFO, "点击客服头像");
		wait(1);
		checkTitlebar1("客服主页");
		if(CheckViewVisibilty(By.id("customer_service_name_tv"))&&
				CheckViewVisibilty(By.id("customer_service_num_tv"))&&
				CheckViewVisibilty(By.id("umcsdk_customer_bg"))&&
				CheckViewVisibilty(By.id("customer_service_car_tv"))&&
				CheckViewVisibilty(By.id("tvCarWholeName"))) {
			reports_BuyCarTest.log(LogStatus.INFO, "客服主页显示正常");
			System.out.println("客服主页显示正常");
			clickElementById("tvCarWholeName");
			reports_BuyCarTest.log(LogStatus.INFO, "点击第一辆推荐的车进入详情页");
			if(CheckViewVisibilty(By.id("tvVehicleDetailsCarName"))) {
				reports_BuyCarTest.log(LogStatus.INFO, "点击客服主页的推荐好车的第一辆车进入详情页");
				System.out.println("点击客服主页的推荐好车的第一辆车进入详情页");
			}else {
				reports_BuyCarTest.log(LogStatus.INFO, "点击客服主页的推荐好车的第一辆车没有进入详情页");
				failAndMessage("点击客服主页的推荐好车的第一辆车没有进入详情页");
			}
		}else {
			reports_BuyCarTest.log(LogStatus.INFO, "客服主页显示异常，请查看");
			GetScreenshot("test_2500_kefu");
			failAndMessage("客服主页显示异常，请查看");
		}
	}
	
	/**
	 * @Name 2600_GJSXCJ
	 * @Category 买车-筛选-高级筛选
	 * @Grade 高级
	 * @FunctionPoint 买车-筛选-高级筛选，左侧所有文案检测
	 */
	@Test
	public void test_2600_GJSXCJ() {
		reports_BuyCarTest.startTest("test_2600_GJSXCJ");
		gotocate(2);
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "点击高级筛选");
		clickElementByName("筛选");
		checkTitlebar1("高级筛选");
		reports_BuyCarTest.log(LogStatus.INFO, "文案检查");
		wait(1);
//		String[] str = {"优信服务","品牌","车价","车型","变速箱","车龄","里程","排量","发动机","排放标准","颜色","燃料类型","厂商类型","座位数","国别","驱动","配置亮点"};
//		clickElementByXpath("//android.widget.TextView[@text='优信服务']");
//		String[] yxfw = {"优信服务","（多选）","一成购","分期购","视频检测","超值","三天无理由","优信金牌认证","今日新上"};
//		for (int i = 0; i < yxfw.length; i++) {
//			sleep(500);
//			findElementByName(yxfw[i]);
//			System.out.println("str>>>"+yxfw[i]);
//		}
		clickElementByXpath("//android.widget.TextView[@text='品牌']");
		String[] pp = {"品牌","不限"};
		for (int i = 0; i < pp.length; i++) {
			sleep(500);
			findElementByName(pp[i]);
			System.out.println("str>>>"+pp[i]);
		}
		clickElementByXpath("//android.widget.TextView[@text='车价']");
		String[] jg = {"5万以下","5-10万","10-15万","15-20万","20-30万","30-50万","50万以上","自定义车价","（万元）","车型"};
		for (int i = 0; i < jg.length; i++) {
			sleep(500);
			findElementByName(jg[i]);
			System.out.println("str>>>"+jg[i]);
		}
		clickElementByXpath("//android.widget.TextView[@text='车型']");
		String[] cx = {"三厢轿车","SUV","两厢轿车","MPV","跑车","面包车","皮卡","旅行车"};
		for (int i = 0; i < cx.length; i++) {
			sleep(500);
			findElementByName(cx[i]);
			System.out.println("str>>>"+cx[i]);
		}
		clickElementByXpath("//android.widget.TextView[@text='变速箱']");
		
		String[] bsx = {"变速箱","手动档","自动档","车龄","（年）","里程","（万公里）","排量","（L）","发动机","自然吸气","涡轮增压","机械增压"};
		for (int i = 0; i < bsx.length; i++) {
			sleep(500);
			findElementByName(bsx[i]);
			System.out.println("str>>>"+bsx[i]);
		}
		clickElementByXpath("//android.widget.TextView[@text='车龄']");
		clickElementByXpath("//android.widget.TextView[@text='里程']");
//		clickElementByXpath("//android.widget.ScrollView[@resource-id='com.uxin.usedcar:id/scroll_title']/android.widget.RelativeLayout[5]/android.widget.TextView[1]");//排量
		clickElementByName("排量");
		clickElementByName("发动机");
		String[] fdj = {"自然吸气","涡轮增压","机械增压","电动机","混合动力"};
		for (int i = 0; i < fdj.length; i++) {
			sleep(500);
			findElementByName(fdj[i]);
			System.out.println("str>>>"+fdj[i]);
		}
		clickElementByName("排放标准");
		String[] pfbz = {"排放标准","国三及以上","国四及以上","国五"};
		for (int i = 0; i < pfbz.length; i++) {
			sleep(500);
			findElementByName(pfbz[i]);
			System.out.println("str>>>"+pfbz[i]);
		}
		clickElementByName("颜色");
		String[] ys = {"黑色","深灰色","银灰色","白色","香槟色","黄色","橙色","红色","蓝色","粉红色","紫色","咖啡色","绿色","多彩色","其它"};
		for (int i = 0; i < ys.length; i++) {
			sleep(500);
			findElementByName(ys[i]);
			System.out.println("str>>>"+ys[i]);
		}
		clickElementByName("燃料类型");
		String[] rllx = {"燃料类型","汽油","柴油","电动","油电混合"};
		for (int i = 0; i < rllx.length; i++) {
			sleep(500);
			findElementByName(rllx[i]);
			System.out.println("str>>>"+rllx[i]);
		}
		clickElementByName("厂商类型");
		String[] cslx = {"厂商类型","合资","进口","国产"};
		for (int i = 0; i < cslx.length; i++) {
			sleep(500);
			findElementByName(cslx[i]);
			System.out.println("str>>>"+cslx[i]);
		}
		clickElementByName("座位数");
		String[] zws = {"座位数","2座","4座","5座","6座","7座及以上"};
		for (int i = 0; i < zws.length; i++) {
			sleep(500);
			findElementByName(zws[i]);
			System.out.println("str>>>"+zws[i]);
		}
		clickElementByName("国别");
		String[] gb = {"国别","德国","日本","韩国","美国","法国","中国"};
		for (int i = 0; i < gb.length; i++) {
			sleep(500);
			findElementByName(gb[i]);
			System.out.println("str>>>"+gb[i]);
		}
		clickElementByName("驱动");
		String[] qd = {"驱动","前驱","后驱","四驱"};
		for (int i = 0; i < qd.length; i++) {
			sleep(500);
			findElementByName(qd[i]);
			System.out.println("str>>>"+qd[i]);
		}
		clickElementByName("配置亮点");
		String[] pzld = {"配置亮点","（多选）","座椅按摩","记忆后视镜","座椅通风","全景影像","无钥匙进入","自动泊车","并线辅助","电动后备箱","全景天窗","后独立空调",
				"LED大灯","自动驻车","座椅记忆","感应雨刷","运动座椅","电动方向盘","方向盘换挡","座椅加热","定速巡航","电动折耳"};
		for (int i = 0; i < pzld.length; i++) {
			sleep(500);
			findElementByName(pzld[i]);
			System.out.println("str>>>"+pzld[i]);
		}
		sliding("up");
		String[] pzld1 = {"真皮座椅","胎压监测","无钥匙启动","电动座椅","倒车影像","中控屏","驻车雷达","自动空调","多功能方向盘"};
		for (int i = 0; i < pzld1.length; i++) {
			sleep(500);
			findElementByName(pzld1[i]);
			System.out.println("str>>>"+pzld1[i]);
		}

	}
	/**
	 * @Name 2601_CLXQDA
	 * @Category 买车-车辆详情页-车辆档案，查看详细参数配置，排放标准，颜色 文案检索
	 * @Grade 高级
	 * @FunctionPoint 买车-车辆详情页-车辆档案，查看详细参数配置，排放标准，颜色 文案检索
	 */
	@Test
	public void test_2601_CLXQDA() {
		reports_BuyCarTest.startTest("test_2601_CLXQDA");
		gotocate(2);
		reports_BuyCarTest.log(LogStatus.INFO, "文案检查");
		toDetail(1);
		wait(1);
		findElementByName("车辆档案");
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*4/5, width/2, height*2/5, 1000);
		wait(1);
		findElementByName("查看详细参数配置");
		findElementByName("排放标准");
		findElementByName("颜色");
	}
	/**
	 * @Name 2602_YXRZ
	 * @Category 买车-车辆详情页-优信金牌认证、服务咨询
	 * @Grade 高级
	 * @FunctionPoint 买车-车辆详情页-优信金牌认证4个tab文案检测、服务咨询文案检测
	 */
	@Test
	public void test_2602_YXRZ() {
		reports_BuyCarTest.startTest("test_2602_YXRZ");
		gotocate(2);
		clickElementById("tv_filter");
		wait(1);
		clickElementByName("视频检测");
		clickElementById("ll_search");
		wait(3);
		toDetail(1);
//		reports_BuyCarTest.log(LogStatus.INFO, "滑动到优信金牌认证卡片");
//		swipeUntilElement(By.xpath("//android.widget.TextView[@text='认证服务']"), "up", 2);  //优信金牌认证
//		wait(2);
//		int width2 = driver.manage().window().getSize().width;
//		int height2 = driver.manage().window().getSize().height;
//		driver.swipe(width2/2, height2*4/10, width2/2, height2*2/10, 1000);
//		String[] str = {"优信金牌认证","服务咨询","30天包退","一年保修","315项排查","全国联保"};
//		for (int i = 0; i < str.length; i++) {
//			findElementByName(str[i]);
//			System.out.println("str>>>"+str[i]);
//		}
//		wait(2);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*3/5, width/2, height*2/5, 1000);
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "滑动到出现导航栏");
		if(CheckViewVisibiltyByName("历史")) {
			clickElementByName("历史");
			wait(1);
			reports_BuyCarTest.log(LogStatus.INFO, "点击历史导航栏按钮");
			findElementByName("车辆历史");
			int width3 = driver.manage().window().getSize().width;
			int height3 = driver.manage().window().getSize().height;
			driver.swipe(width3/2, height3*4/10, width3/2, height3*2/10, 1000);
			findElementByName("免费查看详细维保记录");
			wait(1);
		}else {
			reports_BuyCarTest.log(LogStatus.INFO, "当前车辆没有历史模块。");
			System.out.println("当前车辆没有历史模块。");
		}
//		int width1 = driver.manage().window().getSize().width;
//		int height1 = driver.manage().window().getSize().height;
//		driver.swipe(width1/2, height1*7/10, width1/2, height1*1/10, 1000);  //车辆实拍
		clickElementByName("车图");
		wait(1);
		findElementByName("车辆实拍");
//		swipeUntilElement(By.id("tv_warrant_title"), "up", 3);  //相似推荐
		clickElementByName("推荐");
		wait(2);
		findElementByName("相似推荐");
	}
	
	/**
	 * @Name 2603_7XJC
	 * @Category 买车-车辆详情页-车辆检测
	 * @Grade 高级
	 * @FunctionPoint 买车-车辆详情页-车辆检测7大项文案检测
	 */
	@Test
	public void test_2603_7XJC() {
		reports_BuyCarTest.startTest("test_2603_7XJC");
		gotocate(2);
		clickElementById("tv_filter");
		wait(1);
		clickElementByName("视频检测");
		clickElementById("ll_search");
		wait(3);
		toDetail(1);
		int width1 = driver.manage().window().getSize().width;
		int height1 = driver.manage().window().getSize().height;
		driver.swipe(width1/2, height1*3/5, width1/2, height1*2/5, 1000);
		wait(1);
		clickElementByName("检测");
		wait(1);
		driver.swipe(width1/2, height1*8/10, width1/2, height1*2/10, 1000);
		wait(1);
		String[] str = {"事故排查","轻微碰撞","易损耗部件","常用功能","启动检测","漆面检测"};
		for (int i = 0; i < str.length; i++) {
			findElementByName(str[i]);
			System.out.println("str>>>"+str[i]);
		}
		if(CheckViewVisibiltyByName("外观内饰")||CheckViewVisibiltyByName("外观内饰骨架")) {
			System.out.println("当前车辆详情页的车辆检测显示无问题");
			reports_BuyCarTest.log(LogStatus.INFO, "当前车辆详情页的车辆检测显示无问题");
		}else {
			failAndMessage("当前车辆详情页的车辆检测显示异常，请检查");
		}
	}
	
	/**
	 * @Name 2604_XCXF
	 * @Category 买车-车辆详情页-检测瑕疵和修复数量
	 * @Grade 高级
	 * @FunctionPoint 买车-车辆详情页-检测瑕疵处数量、修复数量和报告结果页一致
	 */
	@Test
	public void test_2604_XCXF() {
		reports_BuyCarTest.startTest("test_2604_XCXF");
		gotocate(2);
		wait(2);
		clickElementById("tv_filter");
		wait(1);
		clickElementByName("视频检测");
		clickElementById("ll_search");
		wait(3);
		toDetail(1);
		reports_BuyCarTest.log(LogStatus.INFO, "滑动检测项异常");
		swipeUntilElement(By.id("tv_warrant_title"), "up", 4); 
		wait(2);
		String str =findElementById("tv_err_count").getText();
		System.out.println("str>>>"+str);
		clickElementById("tv_maintenance_title");
		wait(2);
		slidingInElement(findElementById("daohan"), "left");
		wait(1);
		clickElementByName("瑕疵及修复");
		sliding("up");
//		swipeUntilElement(By.id("tv_warrant_title"), "up", 25); 
		wait(1);
		String str2 = findElementById("tv_err_count").getText();
		if (str.equals(str2)) {
			System.out.println("详情页瑕疵图和报告页瑕疵图修复数一致>>>"+str);
		}else {
			failAndMessage("详情页瑕疵图修复数和报告页不一致str>>>"+str+"   str2>>>"+str2);
		}
	}
	/**
	 * @Name 2605_VRXQ
	 * @Category 买车-VR详情页-筛选VR车源后，进入VR详情页检查头图部分显示
	 * @Grade 高级
	 * @FunctionPoint 买车-筛选VR车源后，进入VR详情页检查头图部分显示
	 */
	@Test
	public void test_2605_VRXQ() {
		reports_BuyCarTest.startTest("test_2605_VRXQ");
		gotocate(2);
		clickElementById("tv_filter");
		wait(1);
		clickElementByName("VR车源");
		clickElementById("ll_search");
		wait(4);
		if (CheckViewVisibilty(By.id("ivvideoTag"))) {
			clickElementById("ivvideoTag");
			wait(5);
			reports_BuyCarTest.log(LogStatus.INFO, "进入VR详情页");
			String carid = findElementById("tvCarid").getAttribute("text").split("：")[1];
			if (CheckViewVisibilty(By.id("vr_capsule_exterior"))&&CheckViewVisibilty(By.id("vr_capsule_interior"))
					&&CheckViewVisibilty(By.id("tvShot"))) {
				clickElementByName("内饰");
				String carname = findElementById("tvVehicleDetailsCarName").getAttribute("text");
				System.out.println("carname:"+carname);
				sleep(500);
				clickElementById("mIViews");
				wait(10);
				reports_BuyCarTest.log(LogStatus.INFO, "点击VR头图进入VR详情页");
				String carnameInVR = findElementByXpath("//android.view.View[contains(@text,'款')]").getAttribute("text");
				System.out.println("carnameInVR:"+carnameInVR);
				if (carname.equals(carnameInVR)) {
					System.out.println("name校验通过");
					waitForVisible(By.xpath("//android.widget.Image[contains(@text,'kefu')]"), 3);
					waitForVisible(By.xpath("//android.view.View[@text='关门外观']"), 3);
					waitForVisible(By.xpath("//android.view.View[@text='开门外观']"), 3);
					waitForVisible(By.xpath("//android.view.View[@text='内饰']"), 3);
					clickElementByXpath("//android.widget.Image[contains(@text,'kefu')]");
					if (CheckViewVisibilty(By.name("优信二手车"))) {
						
					}else {
						failAndMessage("VR播放页面点击客服按钮没有跳转到IM界面");
					}
				}else {
					failAndMessage("VR播放页面的车辆名称与详情页名称不一致，请人工核查，carid为："+carid);
				}
			}else {
				GetScreenshot("test_2605_VRXQ");
				failAndMessage("VR详情页头图部分显示不正常，请人工检查，不正常的carid为"+carid);
			}
		}
	}
	
	/**
	 * @Name 2606_VRXQ
	 * @Category 买车-VR详情页-筛选VR车源后，进入VR详情页检查头图部分显示
	 * @Grade 高级
	 * @FunctionPoint 买车-筛选VR车源后，进入VR详情页检查头图-视频和实拍按钮
	 */
	@Test
	public void test_2606_VRXQ() {
		reports_BuyCarTest.startTest("test_2605_VRXQ");
		gotocate(2);
		clickElementById("tv_filter");
		wait(1);
		clickElementByName("VR车源");
		clickElementById("ll_search");
		wait(4);
		if (CheckViewVisibilty(By.id("ivvideoTag"))) {
			clickElementById("ivvideoTag");
			wait(5);
			reports_BuyCarTest.log(LogStatus.INFO, "进入VR详情页");
			String carid = findElementById("tvCarid").getAttribute("text").split("：")[1];
			if (CheckViewVisibilty(By.id("tvVideo"))) {
				clickElementById("tvVideo");
				reports_BuyCarTest.log(LogStatus.INFO, "点击视频按钮");
				wait(3);
				List<WebElement> titleList = driver.findElements(By.id("title"));
				System.out.println(titleList.get(0).getText());
				System.out.println(titleList.get(0).getAttribute("selected"));
				if(titleList.get(0).getText().equals("视频")&&
						titleList.get(0).getAttribute("selected").equals("true")) {
					reports_BuyCarTest.log(LogStatus.INFO, "点击视频按钮，进入视频详情页");
					clickElementByName("整车视频");
					wait(1);
				}else {
					reports_BuyCarTest.log(LogStatus.ERROR, "点击视频按钮，没有进入视频详情页");
					failAndMessage("点击视频按钮，没有进入视频详情页");
				}
				backBTN();
			}else {
				reports_BuyCarTest.log(LogStatus.INFO, "当前VR车辆没有视频按钮");
				System.out.println("当前VR车辆没有视频按钮");
			}
			if (CheckViewVisibilty(By.id("tvShot"))) {
				clickElementById("tvShot");
				reports_BuyCarTest.log(LogStatus.INFO, "点击实拍按钮");
				wait(3);
				List<WebElement> titleList = driver.findElements(By.id("title"));
				if(titleList.get(1).getText().equals("外观")&&
						titleList.get(1).getAttribute("selected").equals("true")) {
					reports_BuyCarTest.log(LogStatus.INFO, "点击实拍按钮，进入图片详情页");
					clickElementById("ivItemPic");
					wait(1);
					reports_BuyCarTest.log(LogStatus.INFO, "点击第一个图片");
					backBTN();
				}else {
					reports_BuyCarTest.log(LogStatus.ERROR, "点击实拍按钮，没有进入图片详情页");
					failAndMessage("点击实拍按钮，没有进入图片详情页");
				}
			}else {
				reports_BuyCarTest.log(LogStatus.INFO, "当前VR车辆没有实拍按钮");
				System.out.println("当前VR车辆没有实拍按钮");
			}
		}
	}
	
	/**
	 * @Name 2606_storeRoom
	 * @Category 买车-车源仓
	 * @Grade 高级
	 * @FunctionPoint 买车-车源仓，列表页与详情页的车源仓进行对比，检测是否一致
	 */
	@Test
	public void test_2606_storeRoom() {
		reports_BuyCarTest.startTest("test_2606_storeRoom");
		gotocate(2);
		wait(2);
		if(CheckViewVisibilty(By.id("tvStoreRoom"))) {
			String storeNameList = findElementById("tvStoreRoom").getText().substring(3);
			System.out.println(storeNameList);
			clickElementById("tvStoreRoom");
			wait(2);
			reports_BuyCarTest.log(LogStatus.INFO, "进去详情页");
			String storeNameDetail = findElementById("tvWarehouse").getText().substring(3, 6);
			System.out.println(storeNameDetail);
			if(storeNameList.equals(storeNameDetail)) {
				reports_BuyCarTest.log(LogStatus.INFO, "列表页和详情页的车源仓保持一致");
				System.out.println("列表页和详情页的车源仓保持一致");
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "列表页和详情页的车源仓不一致，分别为"+storeNameList+","+storeNameDetail);
				failAndMessage("列表页和详情页的车源仓不一致，分别为"+storeNameList+","+storeNameDetail);
			}
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "当前列表没有车源仓展示");
			System.out.println("当前列表没有车源仓展示");
		}
	}
	
	/**
	 * @Name 2700_brand_IM
	 * @Category 买车-品牌-帮我找车
	 * @Grade 高级
	 * @FunctionPoint 买车-品牌-帮我找车，点击帮我找车进入客服聊天页面
	 */
	@Test
	public void test_2700_brand_IM() {
		reports_BuyCarTest.startTest("test_2700_brand_IM");
		gotocate(2);
		wait(2);
		clickElementByName("品牌");
		wait(1);
		reports_BuyCarTest.log(LogStatus.INFO, "点击品牌");
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
			}
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "品牌导航栏页面没有帮我找车按钮。");
			GetScreenshot("test_2700_brand_IM");
			failAndMessage("品牌导航栏页面没有帮我找车按钮。");
		}
	}
	
	/**
	 * @Name 2400_monthlyPriceCard
	 * @Category 买车-月供推荐卡片
	 * @Grade 高级
	 * @FunctionPoint 买车-月供推荐卡片，点击月供2-5千，检查月供显示范围
	 */
	@Test
	public void test_2400_monthlyPriceCard() {
		reports_BuyCarTest.startTest("test_2400_monthlyPriceCard");
		gotocate(2);
		wait(2);
		clickElementByName("月供2-5千");
		reports_BuyCarTest.log(LogStatus.INFO, "点击月供2-5千");
		wait(2);
		List<WebElement> monthlyPriceList = driver.findElementsById("tvmonthlyprice");
		String mp1=monthlyPriceList.get(0).getText().substring(2, 6);
		String mp2=monthlyPriceList.get(1).getText().substring(2, 6);
		System.out.println(mp1+"---------"+mp2);
		Double mp_1 = Double.parseDouble(mp1);
		Double mp_2 = Double.parseDouble(mp2);
		System.out.println(mp_1+"**"+mp_2);
		if (mp_1 > 2000 &&
				mp_1 < 5000 &&
				mp_2 > 2000 &&
				mp_2 < 5000) {
			reports_BuyCarTest.log(LogStatus.INFO, "点击月供卡片：月供2-5千，列表页车辆月供值范围显示正确。");
			if(mp_1 <= mp_2 ) {
				reports_BuyCarTest.log(LogStatus.INFO, "点击月供卡片：月供2-5千，列表页月供值范围按照月供值由低到高排序，逻辑正确。");
				toDetail(1);
//				checkTitlebar_Detail();
			}else {
				reports_BuyCarTest.log(LogStatus.ERROR, "点击月供卡片：月供2-5千，列表页月供值范围不是按照月供值由低到高排序的，逻辑不正确，请检查");
				GetScreenshot("test_2400_monthlyPriceCard_1");
				failAndMessage("点击月供卡片：月供2-5千，列表页月供值范围不是按照月供值由低到高排序的，逻辑不正确，请检查");
			}
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "点击月供卡片：月供2-5千，列表页车辆月供值范围显示不正确，请人工检查");
			GetScreenshot("test_2400_monthlyPriceCard_2");
			failAndMessage("点击月供卡片：月供2-5千，列表页车辆月供值范围显示不正确，请人工检查");
		}
	}
	
	/**
	 * @Name 2400_listFenQi
	 * @Category 买车-分期
	 * @Grade 高级
	 * @FunctionPoint 买车-分期，选择月供2-5千，首付1-3万，检查月供、首付显示范围
	 */
	@Test
	public void test_2400_listFenQi() {
		reports_BuyCarTest.startTest("test_2400_listFenQi");
		gotocate(2);
		wait(2);
		clickElementByName("分期");
		reports_BuyCarTest.log(LogStatus.INFO, "点击分期按钮");
		sleep(500);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		TouchAction action = new TouchAction(driver);
		action.tap(width*5/6, height*5/12).perform();
		reports_BuyCarTest.log(LogStatus.INFO, "点击月供2-5千");
		sleep(500);
		action.tap(width*5/6, height*15/24).perform();
		reports_BuyCarTest.log(LogStatus.INFO, "点击首付1-3万");
		sleep(500);
		action.tap(width*5/6, height*3/4).perform();
		reports_BuyCarTest.log(LogStatus.INFO, "点击确定");
		wait(2);
		List<WebElement> halfPriceList = driver.findElementsById("tvHalfPrice");
		String hp1=halfPriceList.get(0).getText().substring(2, 6);
		String hp2=halfPriceList.get(1).getText().substring(2, 6);
		System.out.println(hp1+"---------"+hp2);
		Double hp_1 = Double.parseDouble(hp1);
		Double hp_2 = Double.parseDouble(hp2);
		System.out.println(hp_1+"**"+hp_2);
		if (hp_1 > 1 &&
				hp_1 < 3 &&
				hp_2 > 1 &&
				hp_2 < 3) {
			reports_BuyCarTest.log(LogStatus.INFO, "分期选择月供2-5千，首付1-3万，列表页车辆首付值范围显示正确。");
			System.out.println("分期选择月供2-5千，首付1-3万，列表页车辆首付值范围显示正确。");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "分期选择月供2-5千，首付1-3万，列表页车辆首付值范围显示不正确，请人工检查");
			GetScreenshot("test_2400_listFenQi_1");
			failAndMessage("分期选择月供2-5千，首付1-3万，列表页车辆首付值范围显示不正确，请人工检查");
		}
		
		List<WebElement> monthlyPriceList = driver.findElementsById("tvmonthlyprice");
		String mp1=monthlyPriceList.get(0).getText().substring(2, 6);
		String mp2=monthlyPriceList.get(1).getText().substring(2, 6);
		System.out.println(mp1+"---------"+mp2);
		Double mp_1 = Double.parseDouble(mp1);
		Double mp_2 = Double.parseDouble(mp2);
		System.out.println(mp_1+"**"+mp_2);
		if (mp_1 > 2000 &&
				mp_1 < 5000 &&
				mp_2 > 2000 &&
				mp_2 < 5000) {
			reports_BuyCarTest.log(LogStatus.INFO, "分期选择月供2-5千，首付1-3万，列表页车辆月供值范围显示正确。");
			System.out.println("分期选择月供2-5千，首付1-3万，列表页车辆月供值范围显示正确。");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "分期选择月供2-5千，首付1-3万，列表页车辆月供值范围显示不正确，请人工检查");
			GetScreenshot("test_2400_listFenQi_2");
			failAndMessage("分期选择月供2-5千，首付1-3万，列表页车辆月供值范围显示不正确，请人工检查");
		}
	}
	
	/**
	 * @Name 2400_GaoShaiFenQi
	 * @Category 买车-高筛-分期
	 * @Grade 高级
	 * @FunctionPoint 买车-高筛-分期，选择车价15-20万，月供2-5千，首付1-3万，检查月供、首付显示范围
	 */
	@Test
	public void test_2400_GaoShaiFenQi() {
		reports_BuyCarTest.startTest("test_2400_GaoShaiFenQi");
		gotocate(2);
		wait(2);
		clickElementById("tv_filter");
		reports_BuyCarTest.log(LogStatus.INFO, "点击高级筛选");
		wait(1);
		clickElementByName("车价");
		reports_BuyCarTest.log(LogStatus.INFO, "点击车价");
		clickElementByName("15-20万");
		reports_BuyCarTest.log(LogStatus.INFO, "点击15-20万");
		clickElementByName("首付");
		reports_BuyCarTest.log(LogStatus.INFO, "点击首付");
		clickElementByName("1-3万");
		reports_BuyCarTest.log(LogStatus.INFO, "点击首付1-3万");
		clickElementByName("月供");
		reports_BuyCarTest.log(LogStatus.INFO, "点击月供");
		clickElementByName("2-5千");
		reports_BuyCarTest.log(LogStatus.INFO, "点击月供2-5千");
		sleep(500);
		clickElementById("ll_search");
		wait(4);
		List<WebElement> halfPriceList = driver.findElementsById("tvHalfPrice");
		String hp1_str=halfPriceList.get(0).getText();
		String hp1 = hp1_str.substring(2, hp1_str.length()-1);
		String hp2_str=halfPriceList.get(1).getText();
		String hp2 = hp2_str.substring(2, hp2_str.length()-1);
		System.out.println(hp1+"---------"+hp2);
		Double hp_1 = Double.parseDouble(hp1);
		Double hp_2 = Double.parseDouble(hp2);
		System.out.println(hp_1+"**"+hp_2);
		if (hp_1 > 1 &&
				hp_1 < 3 &&
				hp_2 > 1 &&
				hp_2 < 3) {
			reports_BuyCarTest.log(LogStatus.INFO, "高筛选择车价15-20万，月供2-5千，首付1-3万，列表页车辆首付值范围显示正确。");
			System.out.println("高筛选择车价15-20万，月供2-5千，首付1-3万，列表页车辆首付值范围显示正确。");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "高筛选择车价15-20万，月供2-5千，首付1-3万，列表页车辆首付值范围显示不正确，请人工检查");
			GetScreenshot("test_2400_GaoShaiFenQi_1");
			failAndMessage("高筛选择车价15-20万，月供2-5千，首付1-3万，列表页车辆首付值范围显示不正确，请人工检查");
		}
		
		List<WebElement> monthlyPriceList = driver.findElementsById("tvmonthlyprice");
		String mp1_str=monthlyPriceList.get(0).getText();
		String mp1 = mp1_str.substring(2, mp1_str.length()-1);
		String mp2_str=monthlyPriceList.get(1).getText();
		String mp2 = mp2_str.substring(2, mp2_str.length()-1);
		System.out.println(mp1+"---------"+mp2);
		Double mp_1 = Double.parseDouble(mp1);
		Double mp_2 = Double.parseDouble(mp2);
		System.out.println(mp_1+"**"+mp_2);
		if (mp_1 > 2000 &&
				mp_1 < 5000 &&
				mp_2 > 2000 &&
				mp_2 < 5000) {
			reports_BuyCarTest.log(LogStatus.INFO, "高筛选择车价15-20万，月供2-5千，首付1-3万，列表页车辆月供值范围显示正确。");
			System.out.println("高筛选择车价15-20万，月供2-5千，首付1-3万，列表页车辆月供值范围显示正确。");
		}else {
			reports_BuyCarTest.log(LogStatus.ERROR, "高筛选择车价15-20万，月供2-5千，首付1-3万，列表页车辆月供值范围显示不正确，请人工检查");
			GetScreenshot("test_2400_GaoShaiFenQi_2");
			failAndMessage("高筛选择车价15-20万，月供2-5千，首付1-3万，列表页车辆月供值范围显示不正确，请人工检查");
		}
	}
}
