package com.uxin.usedcar.testCase;

import java.awt.image.RescaleOp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidKeyCode;

import org.apache.http.client.ClientProtocolException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.handler.FindElement;
import org.openqa.selenium.remote.server.handler.interactions.touch.LongPressOnElement;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.relevantcodes.extentreports.LogLevel;
import com.relevantcodes.extentreports.LogStatus;
import com.uxin.usedcar.test.libs.BaseTest;




@SuppressWarnings("unused")
public class MyTest extends BaseTest {
	 @BeforeClass
	  public static void first() throws Exception {
		 reports_MyTest.init("./report/MyTest/reportMyTest.html",true);
		 System.out.println("------setUp-------");
	  }

	@AfterClass 
	public static void last(){
		reports_MyTest.endTest();
//		driver.quit();
		System.out.println("------tearDown-------");
	}
	/**
	 * String数字类型转换int类型
	 * @author yanxin
	 */
	public static int StringSwitchInt(String num){
		if (num != null) {
			try {
				int number = Integer.parseInt(num);
				System.out.println("number="+number);
				return number;
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("转换int型异常");
				Assert.assertTrue(false, "转换int型异常");
			}
		}
		return 0 ;
	}

	/**
	 * @Name 5200_WDXX
	 * @Catalogue 我的-未读消息 
	 * @Grade 高级
	 * @FunctionPoint 我的-未读消息，消息未读时有红点，进去之后检查有未读的消息
	 */
	@Test
	public void test_5200_WDXX() {
		reports_MyTest.startTest("test_5200_WDXX");
		gotocate(5);
		reports_MyTest.log(LogStatus.INFO, "检查是否有未读消息");
		if(CheckViewVisibilty(By.id("tvMessageCount"))) {//信封上的红点
			reports_MyTest.log(LogStatus.INFO, "有未读消息。点击进去查找是否有红点。");
			clickElementById("rlMessage");//点击消息通知（即信封）按钮
			wait(1);
			checkTitlebar1("消息中心");
			if(CheckViewVisibilty(By.id("redDotInteraction")) ||
					CheckViewVisibilty(By.id("redDotRecommend")) ||
					CheckViewVisibilty(By.id("tv_reddot"))) {//消息通知里的红点
				reports_MyTest.log(LogStatus.INFO, "消息通知里确实有未读消息，红点显示正确！");
				System.out.println("消息通知里确实有未读消息，红点显示正确！");
			}
			else {
				reports_MyTest.log(LogStatus.ERROR, "消息通知里没有未读消息，红点显示异常！");
				failAndMessage("消息通知里没有未读消息，红点显示异常！");
			}
		}else {
			reports_MyTest.log(LogStatus.INFO, "暂时没有未读消息。");
			System.out.println("暂时没有未读消息。");
		}
	}
	
	/**
	 * @Name 5200_LT
	 * @Catalogue 我的-在线聊天 （只能在正式包上执行此case，测试包的在线聊天点击不动）
	 * @Grade 高级
	 * @FunctionPoint 我的-聊天，检测有无聊天记录，如果没有聊天记录，则创建一条记录并检查发送文字、表情功能；并且检查长按删除聊天会话
	 */
	@Test
	public void test_5200_LT() {
		reports_MyTest.startTest("test_5200_LT");
		gotocate(5);
		wait(1);
		reports_MyTest.log(LogStatus.INFO, "点击消息通知（即信封）按钮-在线聊天");
		clickElementById("rlMessage");//点击消息通知（即信封）按钮
		wait(1);
		checkTitlebar1("消息中心");
		WebElement ListView = findElementById("msg_container");
		List<WebElement> items = ListView.findElements(By.id("tv_name"));
		int n = items.size();
		for(int i=0; i<n; i++) {
			System.out.println(items.get(i).getText());
			if(items.get(i).getText().equals("优信二手车")) {
				clickElementByName("优信二手车");
				wait(1);
				checkTitlebar1("优信二手车");
//				if(CheckViewVisibilty(By.id("empty_message"))) {
//					reports_MyTest.log(LogStatus.INFO, "在线聊天为空时，去详情页点击在线聊天，发送消息。");
//					driver.pressKeyCode(AndroidKeyCode.BACK);    //安卓的返回键
//					wait(1);
//					driver.pressKeyCode(AndroidKeyCode.BACK); 
//					gotocate(2);
//					wait(3);
//					toDetail(1);
//					wait(1);
//					clickElementByName("在线客服");
//					findElementById("et_sendmessage").sendKeys("您好");
//					clickElementById("btn_send");  //发送
//					reports_MyTest.log(LogStatus.INFO, "详情页-在线客服为空时发送“您好”。");
//					wait(1);
//					if(CheckViewVisibilty(By.id("msg_status"))) {
//						reports_MyTest.log(LogStatus.ERROR, "详情页-在线客服发送文字消息失败！");
//						failAndMessage("详情页-在线客服发送文字消息失败！");
//					}
//					clickElementById("iv_face_normal");//表情
//					wait(3);
//					WebElement parent = findElementById("gridview");
//					List<WebElement> list = parent.findElements(By.id("iv_expression"));
//					list.get(0).click();
//					clickElementById("btn_send");  //发送
//					reports_MyTest.log(LogStatus.INFO, "详情页-在线客服-发送表情。");
//					wait(1);
//					if(CheckViewVisibilty(By.id("msg_status"))) {
//						reports_MyTest.log(LogStatus.ERROR, "详情页-在线客服发送表情消息失败！");
//						failAndMessage("详情页-在线客服发送表情消息失败！");
//				    }
//					clickElementById("btn_more");
//					wait(1);
//					CheckViewVisibilty(By.xpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
//							+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
//							+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
//							+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
//							+ "/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]"
//							+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[2]"
//							+ "/android.widget.GridView[1]/android.widget.LinearLayout[1]"
//							+ "/android.widget.LinearLayout[1]"));    //图片
//					CheckViewVisibilty(By.xpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]"
//							+ "/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
//							+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]"
//							+ "/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[2]"
//							+ "/android.widget.GridView[1]/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]"));   //拍照
//					driver.pressKeyCode(AndroidKeyCode.BACK);    //安卓的返回键
//					wait(1);
//					driver.pressKeyCode(AndroidKeyCode.BACK); 
//					wait(1);
//					driver.pressKeyCode(AndroidKeyCode.BACK); 
//					wait(1);
//					gotocate(5);
//					wait(1);
//					clickElementById("rlMessage");//点击消息通知（即信封）按钮
//					wait(1);
//					clickElementByName("优信二手车");
//					wait(1);
//				}
//				clickElementById("avatar");
				if(CheckViewVisibilty(By.id("tv_chatcontent"))) {
					reports_MyTest.log(LogStatus.INFO, "在线聊天消息展示正常！");
					System.out.println("在线聊天消息展示正常！");
				}
//				driver.pressKeyCode(AndroidKeyCode.BACK); 
//				wait(1);
//				reports_MyTest.log(LogStatus.INFO, "在线聊天-长按删除！");
//				TouchAction a = new TouchAction(driver);
////				a.press(findElementById("list_itease_layout")).waitAction(5000);
//				a.longPress(findElementById("list_itease_layout")).perform();     //长按，进行删除会话
//				wait(1);
//				if(CheckViewVisibilty(By.id("tvDialogMessage"))) {   //删除会话？
//					clickElementByName("删除");
//					reports_MyTest.log(LogStatus.INFO, "在线聊天消息-长按删除成功！");
//					System.out.println("在线聊天消息-长按删除成功！");
//				}
//				else {
//					reports_MyTest.log(LogStatus.ERROR, "在线聊天消息-长按删除失败！");
//					GetScreenshot("test_5200_LT_在线聊天消息-长按删除失败！");
//					failAndMessage("在线聊天消息-长按删除失败！");
//				}
				break;
			}else if (i == n-1) {
				reports_MyTest.log(LogStatus.INFO, "消息中心暂时没有客服聊天消息。");
				System.out.println("消息中心暂时没有客服聊天消息。");
			}
		}
	}
	
	/**
	 * @Name 5100_HDXX
	 * @Catalogue 我的-互动消息
	 * @Grade 高级
	 * @FunctionPoint 我的-互动消息，检测互动消息页面显示是否正常，以及互动消息左滑删除。
	 */
	@Test
	public void test_5100_HDXX() {
		reports_MyTest.startTest("test_5100_HDXX");
		int i = 0;
		gotocate(5);
		wait(2);
		reports_MyTest.log(LogStatus.INFO, "点击消息通知（即信封）按钮");
		clickElementById("rlMessage");//点击消息通知（即信封）按钮
		wait(1);
		WebElement ListView = findElementById("msg_container");
		List<WebElement> items = ListView.findElements(By.id("tv_name"));
		int n = items.size();
		for(int j=0; j<n; j++) {
			System.out.println(items.get(j).getText());
			if(items.get(j).getText().equals("互动消息")) {
				reports_MyTest.log(LogStatus.INFO, "进入互动消息");
				clickElementByName("互动消息");
				wait(5);
				checkTitlebar1("互动消息");
	//			System.out.println(findElementById("tvEmptyMsgTextTop").getText());
				if(CheckViewVisibilty(By.id("tvEmptyMsgTextTop"))) {
					reports_MyTest.log(LogStatus.INFO, "暂时没有收到互动相关消息。");
					System.out.println("暂时没有收到互动相关消息。");
				}else if (CheckViewVisibilty(By.id("ivMessageIcon"))&&
						CheckViewVisibilty(By.id("tvTitle"))&&
						CheckViewVisibilty(By.id("tvMsgContent"))&&
						CheckViewVisibilty(By.id("tvTime"))) {	
					reports_MyTest.log(LogStatus.INFO, "有互动消息，点击消息；");
					clickElementById("tvMsgContent");
					wait(3);
					checkTitlebar_Webview("车辆问答");
					driver.pressKeyCode(AndroidKeyCode.BACK);
					wait(1);
					reports_MyTest.log(LogStatus.INFO, "互动消息页面UI显示正常，点击消息进入车辆问答。");
					System.out.println("互动消息页面UI显示正常，点击消息进入车辆问答。");
					WebElement parent = findElementById("android:id/list");
					List<WebElement> list = parent.findElements(By.id("tvMsgContent"));
	//				List<WebElement> list = driver.findElements(By.id("tvMsgContent"));
					int old_num = list.size();//获取当前页面互动消息显示个数
					System.out.println(old_num);
					if(old_num == 1) {      //当前页面互动消息个数为1的时候，不进行删除操作
						reports_MyTest.log(LogStatus.INFO, "当前只有一条互动消息。");
						System.out.println("当前只有一条互动消息。");
					}else{
						while(old_num > 3 && i < 30) {//当前页面互动消息个数大于3的时候，进行删除操作，直到删除到3个消息为止
							slidingInElement(findElementById("tvMsgContent"), "left");
							old_num = parent.findElements(By.id("tvMsgContent")).size();
							i++;
						}		
						if(i == 30) {
							reports_MyTest.log(LogStatus.ERROR, "互动消息左滑删除失败，请检查。");
							failAndMessage("互动消息左滑删除失败，请检查。");
						}
						reports_MyTest.log(LogStatus.INFO, "左滑删除互动消息。");
						slidingInElement(findElementById("tvMsgContent"), "left");
						int new_num = parent.findElements(By.id("tvMsgContent")).size();
						if((old_num-1) == new_num) {
							reports_MyTest.log(LogStatus.INFO, "互动消息左滑删除成功。");
							System.out.println("互动消息左滑删除成功。");
						}else {
							reports_MyTest.log(LogStatus.ERROR, "互动消息左滑删除失败。");
							GetScreenshot("test_5100_HDXX_左滑删除互动消息失败。");
							failAndMessage("互动消息左滑删除失败，请检查。");
						}
					}		
				}
				break;
			}else if (j == n-1) {
				reports_MyTest.log(LogStatus.INFO, "消息中心暂时没有互动消息。");
				System.out.println("消息中心暂时没有互动消息。");
			}
		}
	}
	
	/**
	 * @Name 5200_JYXX
	 * @Catalogue 我的-交易消息
	 * @Grade 高级
	 * @FunctionPoint 我的-交易消息-检测交易消息页面显示是否正常。
	 */
	@Test
	public void test_5200_JYXX() {
		reports_MyTest.startTest("test_5200_JYXX");
		gotocate(5);
		wait(2);
		reports_MyTest.log(LogStatus.INFO, "点击消息通知（即信封）按钮");
		clickElementById("rlMessage");//点击消息通知（即信封）按钮
		wait(1);
		WebElement ListView = findElementById("msg_container");
		List<WebElement> items = ListView.findElements(By.id("tv_name"));
		int n = items.size();
		for(int i=0; i<n; i++) {
			System.out.println(items.get(i).getText());
			if(items.get(i).getText().equals("交易消息")) {
				reports_MyTest.log(LogStatus.INFO, "进入交易消息。");
				clickElementByName("交易消息");
				checkTitlebar1("交易消息");
				if(CheckViewVisibilty(By.id("tvEmptyMsgTextTop"))&&
						CheckViewVisibilty(By.id("btEmptyMsgButtonBuy"))&&    //去买车
						CheckViewVisibilty(By.id("btEmptyMsgButtonSell"))) { //去卖车
					reports_MyTest.log(LogStatus.INFO, "暂时没有交易消息！");
					System.out.println("暂时没有交易消息！");
				}
				break;
			}else if (i == n-1) {
				reports_MyTest.log(LogStatus.INFO, "消息中心暂时没有交易消息");
				System.out.println("消息中心暂时没有交易消息");
			}
		}
	}
	
	/**
	 * @Name 5200_SXTZ
	 * @Catalogue 我的-上新通知
	 * @Grade 高级
	 * @FunctionPoint 我的-上新通知-检测上新通知页面显示是否正常。
	 */
	@Test
	public void test_5200_SXTZ() {
		reports_MyTest.startTest("test_5200_SXTZ");
		gotocate(5);
		wait(2);
		reports_MyTest.log(LogStatus.INFO, "点击消息通知（即信封）按钮");
		clickElementByName("消息");//点击消息通知（即信封）按钮
		wait(1);
		WebElement ListView = findElementById("msg_container");
		List<WebElement> items = ListView.findElements(By.id("tv_name"));
		int n = items.size();
		for(int i=0; i<n; i++) {
			System.out.println(items.get(i).getText());
			if(items.get(i).getText().equals("上新通知")) {
				clickElementByName("上新通知");
				wait(1);
				checkTitlebar1("上新通知");
				if(CheckViewVisibilty(By.id("tv_desc"))&&
						CheckViewVisibilty(By.id("tv_date"))&&
						CheckViewVisibilty(By.id("jiangjia_name"))&&
						CheckViewVisibilty(By.id("jiangjia_price"))&&
//						CheckViewVisibilty(By.id("tv_bottom_tips"))&&
						CheckViewVisibilty(By.id("btn_msg_more"))) {
					reports_MyTest.log(LogStatus.INFO, "上新通知列表页面显示正常。");
					System.out.println("上新通知列表页面显示正常。");
					reports_MyTest.log(LogStatus.INFO, "点击更多上新车源。");
					clickElementById("btn_msg_more");
					wait(2);
					if(findElementByName("买车").getAttribute("checked").equals("true")) {
						reports_MyTest.log(LogStatus.INFO, "点击更多上新车源，进入买车列表页。");
						System.out.println("点击更多上新车源，进入买车列表页。");
					}else {
						reports_MyTest.log(LogStatus.ERROR, "点击更多上新车源，没有进入买车列表页，请检查。");
						GetScreenshot("test_5200_SXTZ_1");
						failAndMessage("点击更多上新车源，没有进入买车列表页，请检查。");
					}
				}else {
					reports_MyTest.log(LogStatus.ERROR, "上新通知列表页面显示异常，请检查。");
					GetScreenshot("test_5200_SXTZ_2");
					failAndMessage("上新通知列表页面显示异常，请检查。");
				}
				break;
			}else if (i == n-1) {
				reports_MyTest.log(LogStatus.INFO, "消息列表没有上新通知。");
				System.out.println("消息列表没有上新通知。");
			}
		}
	}
	
	/**
	 * @Name 5800_SZ_PUSH
	 * @Catalogue 我的-设置-推送通知
	 * @Grade 高级
	 * @FunctionPoint 我的-设置-推送通知，检查推送通知开关的关闭和开启。
	 */
	@Test
	public void test_5800_SZ_PUSH() {
		reports_MyTest.startTest("test_5800_SZ_PUSH");
		gotocate(5);
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*2/3, width/2, height/3, 1000);
		wait(1);
		reports_MyTest.log(LogStatus.INFO, "进入设置页面");
		clickElementById("vgSetting");
		wait(2);
		checkTitlebar1("设置");
		reports_MyTest.log(LogStatus.INFO, "检查推送通知页面");
		clickElementById("vgNoticeManagerid");//推送通知
		wait(2);
		checkTitlebar1("推送通知");
		clickElementById("ivSwitch");
		wait(1);
		clickElementById("btnCancel");   //关闭推送通知开关
		wait(1);
		if(!CheckViewVisibilty(By.id("vgSubNews"))) {    //订阅车源通知
			reports_MyTest.log(LogStatus.INFO, "关闭推送通知开关成功。");
			System.out.println("关闭推送通知开关成功。");
		}else {
			reports_MyTest.log(LogStatus.INFO, "关闭推送通知开关失败。");
			failAndMessage("关闭推送通知开关失败。");
		}		
		clickElementById("ivSwitch");
		wait(1);
		clickElementById("btnConfirm"); //开启推送通知开关
		wait(2);
		if(CheckViewVisibilty(By.id("vgSubNews"))) {    //订阅车源通知
			reports_MyTest.log(LogStatus.INFO, "开启推送通知开关成功。");
			System.out.println("开启推送通知开关成功。");
		}else {
			reports_MyTest.log(LogStatus.INFO, "开启推送通知开关失败。");
			failAndMessage("开启推送通知开关失败。");
		}	
		clickElementById("imgBtBack");
	}
	
	/**
	 * @Name 5800_SZ_QCHC
	 * @Catalogue 我的-设置-清除缓存
	 * @Grade 高级
	 * @FunctionPoint 我的-设置-清除缓存，检查清除缓存的功能。
	 */
	@Test
	public void test_5800_SZ_QCHC() {
		reports_MyTest.startTest("test_5800_SZ_QCHC");
		gotocate(5);
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*2/3, width/2, height/3, 1000);
		wait(1);
		reports_MyTest.log(LogStatus.INFO, "进入设置页面");
		clickElementById("vgSetting");
		wait(2);
		checkTitlebar1("设置");
		reports_MyTest.log(LogStatus.INFO, "检查是否有缓存，有的话清除缓存");
		if(CheckViewVisibilty(By.id("tvCache"))) {
			clickElementById("tvCache");
			CheckViewVisibilty(By.id("tvDialogMessage"));
			wait(1);
			clickElementById("btnConfirm");
			if(!CheckViewVisibilty(By.id("tvCache"))) {
				reports_MyTest.log(LogStatus.INFO, "清除缓存成功。");
				System.out.println("清除缓存成功。");
			}else {
				reports_MyTest.log(LogStatus.INFO, "清除缓存失败。");
				failAndMessage("清除缓存失败。");
			}
		}else {
			reports_MyTest.log(LogStatus.INFO, "暂无缓存。");
			System.out.println("暂无缓存。");		
		}
	}
	
	
	/**
	 * @Name 5800_SZ_CJWT
	 * @Catalogue 我的-设置-常见问题
	 * @Grade 高级
	 * @FunctionPoint 我的-设置-常见问题，检查常见问题文案显示。
	 */
	@Test
	public void test_5800_SZ_CJWT() {
		reports_MyTest.startTest("test_5800_SZ_CJWT");
		gotocate(5);
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*2/3, width/2, height/3, 1000);
		wait(1);
		reports_MyTest.log(LogStatus.INFO, "进入设置页面");
		clickElementById("vgSetting");
		wait(2);
		checkTitlebar1("设置");
		reports_MyTest.log(LogStatus.INFO, "进入常见问题页面");
		clickElementByName("常见问题");
		wait(3);
		checkTitlebar_Webview("常见问题");
//		try {
//			switchToWebView();
//		} catch (Exception e) {
//			System.out.println("再次切换到webview");
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
//			clickElementByName("常见问题");
//			wait(1);
//			switchToWebView();
//		}
//		if(findElementByXpath("/html/body/div/div/div/ul/li[1]/p[1]/em").getText().equals("我如何购买优信二手车？")&&
//				findElementByXpath("/html/body/div/div/div/ul/li[2]/p[1]/em").getText().equals("优信二手车服务收费么？")&&
//				findElementByXpath("/html/body/div/div/div/ul/li[3]/p[1]/em").getText().equals("优信二手车出售的车辆有保障么？")&&
//				findElementByXpath("/html/body/div/div/div/ul/li[4]/p[1]/em").getText().equals("什么是优信认证的车辆？")&&
//				findElementByXpath("/html/body/div/div/div/ul/li[5]/p[1]/em").getText().equals("什么是4S店原厂质保车辆？")) {
//			reports_MyTest.log(LogStatus.INFO, "常见问题页面显示正常。");
//			System.out.println("常见问题页面显示正常。");
//		}else {
//			reports_MyTest.log(LogStatus.INFO, "常见问题页面显示异常，请查看。");
//			failAndMessage("常见问题页面显示异常，请查看。");
//		}			
	}
	
	/**
	 * @Name 5800_SZ_YJFK
	 * @Catalogue 我的-意见反馈
	 * @Grade 高级
	 * @FunctionPoint 我的-意见反馈，检查意见反馈的文案显示和输入框的个数限制。
	 */
	@Test
	public void test_5800_SZ_YJFK() {
		reports_MyTest.startTest("test_5800_SZ_YJFK");
		gotocate(5);
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*3/4, width/2, height*2/4, 1000);
		wait(1);
		reports_MyTest.log(LogStatus.INFO, "进入意见反馈页面");
		clickElementByName("意见反馈");//意见反馈
		wait(2);
		checkTitlebar1("意见反馈");
		String text = findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
				+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]"
				+ "/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]"
				+ "/android.widget.TextView[1]").getText();
		if (text.equals("如果您在使用优信二手车的过程中遇到任何问题，请留下您的宝贵建议和联系方式，我们会及时与您取得联系：")) {
			reports_MyTest.log(LogStatus.INFO, "意见反馈页面正常");
			System.out.println("意见反馈页面正常");
		}else {
			reports_MyTest.log(LogStatus.ERROR, "意见反馈页面异常");
			GetScreenshot("test_5800_SZ_我的设置-意见反馈");
			failAndMessage("意见反馈页面异常");
		}  
		findElementById("etSubmitContent").sendKeys("自动化测试意见反馈");//9个意见反馈字
		findElementById("etPhone").sendKeys("136136136");//9位手机号码
		if(findElementByName("确认提交").getAttribute("enabled").equals("false")) {
			reports_MyTest.log(LogStatus.INFO, "意见反馈页面，输入少于11个字的意见反馈和少于11位的号码，确认按钮不可用。");
			System.out.println("意见反馈页面，输入少于11个字的意见反馈和少于11位的号码，确认按钮不可用。");
		}else {
			reports_MyTest.log(LogStatus.INFO, "意见反馈页面，输入少于11个字的意见反馈和少于11位的号码，确认按钮可点击，请检查。");
			failAndMessage("意见反馈页面，输入少于11个字的意见反馈和少于11位的号码，确认按钮可点击，请检查。");
		}
		findElementById("etSubmitContent").sendKeys("自动化测试意见反馈测试测试");//13个意见反馈字
		findElementById("etPhone").sendKeys("136136136");//9位手机号码
		if(findElementByName("确认提交").getAttribute("enabled").equals("false")) {
			reports_MyTest.log(LogStatus.INFO, "意见反馈页面，输入13个字的意见反馈和少于11位的号码，确认按钮不可用。");
			System.out.println("意见反馈页面，输入13个字的意见反馈和少于11位的号码，确认按钮不可用。");
		}else {
			reports_MyTest.log(LogStatus.INFO, "意见反馈页面，输入13个字的意见反馈和少于11位的号码，确认按钮可点击，请检查。");
			failAndMessage("意见反馈页面，输入13个字的意见反馈和少于11位的号码，确认按钮可点击，请检查。");
		}
		findElementById("etSubmitContent").sendKeys("自动化测试意见反馈");//9个意见反馈字
		
		findElementById("etPhone").sendKeys("13613613612");//11位手机号码
		if(findElementByName("确认提交").getAttribute("enabled").equals("false")) {
			reports_MyTest.log(LogStatus.INFO, "意见反馈页面，输入少于11个字的意见反馈和11位的号码，确认按钮不可用。");
			System.out.println("意见反馈页面，输入少于11个字的意见反馈和11位的号码，确认按钮不可用。");
		}else {
			reports_MyTest.log(LogStatus.INFO, "意见反馈页面，输入少于11个字的意见反馈和11位的号码，确认按钮可点击，请检查。");
			failAndMessage("意见反馈页面，输入少于11个字的意见反馈和11位的号码，确认按钮可点击，请检查。");
		}
		findElementById("etSubmitContent").sendKeys("自动化测试意见反馈测试");//11个意见反馈字
		findElementById("etPhone").sendKeys("13613613612");//11位手机号码
		if(findElementByName("确认提交").getAttribute("enabled").equals("true")) {
			reports_MyTest.log(LogStatus.INFO, "意见反馈页面，输入11个字的意见反馈和11位的号码，确认按钮可用。");
			System.out.println("意见反馈页面，输入11个字的意见反馈和11位的号码，确认按钮可用。");
		}else {
			reports_MyTest.log(LogStatus.INFO, "意见反馈页面，输入11个字的意见反馈和11位的号码，确认按钮不可点击，请检查。");
			failAndMessage("意见反馈页面，输入11个字的意见反馈和11位的号码，确认按钮不可点击，请检查。");
		}
	}
	
	/**
	 * @Name 5800_SZ_UXIN
	 * @Catalogue 我的-设置-关于优信二手车
	 * @Grade 高级
	 * @FunctionPoint 我的-设置-关于优信二手车，检查关于优信二手车的文案显示。
	 */
	@Test
	public void test_5800_SZ_UXIN(){
		reports_MyTest.startTest("test_5800_SZ_UXIN");
		gotocate(5);
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*2/3, width/2, height/3, 1000);
		wait(1);
		reports_MyTest.log(LogStatus.INFO, "进入设置页面");
		clickElementById("vgSetting");
		wait(2);
		checkTitlebar1("设置");
		reports_MyTest.log(LogStatus.INFO, "进入关于优信二手车页面");
		clickElementByName("关于优信二手车");
		wait(2);
		checkTitlebar1("优信二手车");
		if (CheckViewVisibilty(By.id("tvAboutVersion"))) {
			reports_MyTest.log(LogStatus.INFO, "关于优信二手车页面正常");
			System.out.println("关于优信二手车页面正常");
		}else {
			reports_MyTest.log(LogStatus.ERROR, "关于优信二手车页面异常");
			failAndMessage("关于优信二手车页面异常");
		}	
	}
	
//	/**
//	 * @Name 5700_WDSC
//	 * @Catalogue 我的-我的收藏
//	 * @Grade 高级
//	 * @FunctionPoint 我的-我的收藏  检查收藏的车辆（图片、标题、地址、车龄UI元素是否存在），以及删除功能是否验证正确。
//	 */
//	@Test
//	public void test_5700_WDSC(){
//		reports_MyTest.startTest("test_5700_WDSC");
//		gotocate(5);
//		wait(1);
//		reports_MyTest.log(LogStatus.INFO, "获取我的收藏的个数。");
//		String str1 = findElementById("tvFavorateCarNum").getText();//先获取我的-收藏个数，与删除之后的数量做比较
//		if (str1.equals("0")) {//没有收藏的时候
//			gotocate(2);
//			wait(5);
//			reports_MyTest.log(LogStatus.INFO, "没有收藏的时候，进入车辆详情页，进行收藏。");
//			toDetail(1);
////			clickElementById("ivShouCang");//收藏
//			if(CheckViewVisibilty(By.id("rlReserve"))) {   //新详情页
//				clickElementById("rlCollect");
//			}else {
//				clickElementById("rlShouCang");//9.1老详情页的收藏控件id更改
//			}
//			wait(3);
//			clickElementById("imgBtBack");
//			wait(3);
//			gotocate(5);
//			str1 = findElementById("tvFavorateCarNum").getText();
//		}
//		clickElementByName("我的收藏");
//		wait(1);
//		checkTitlebar1("我的收藏");
//		if(CheckViewVisibilty(By.id("tvIndicator")) &&
//				CheckViewVisibilty(By.id("rootLine"))&&//整体
//				CheckViewVisibilty(By.id("tvCarWholeName"))&&//标题
//				CheckViewVisibilty(By.id("tvCityName"))&&//城市
//				CheckViewVisibilty(By.id("tvAge"))&&//车龄
//				CheckViewVisibilty(By.id("tvMileage"))&&//里程
//				CheckViewVisibilty(By.id("tvPrice"))&&//价格
//				CheckViewVisibilty(By.id("ivItemPic"))) {//图片
//			reports_MyTest.log(LogStatus.INFO, "收藏页面的车辆UI显示正常！");
//			System.out.println("收藏页面的车辆UI显示正常！");
//		}else {
//			reports_MyTest.log(LogStatus.ERROR, "收藏页面的车辆UI显示异常！");
//			GetScreenshot("test_5700_WDSC_收藏车辆");
//			failAndMessage("收藏页面的车辆UI显示异常！");
//		}
//		slidingInElement(findElementById("rootLine"), "left");//左滑删除
//		clickElementById("imgBtBack");
//		if(str1.equals(findElementById("tvFavorateCarNum").getText())) {
//			reports_MyTest.log(LogStatus.ERROR, "我收藏的车，左滑删除失败！");
//			failAndMessage("我收藏的车，左滑删除失败！");
//		}else {
//			reports_MyTest.log(LogStatus.INFO, "我收藏的车，左滑删除成功！");
//			System.out.println("我收藏的车，左滑删除成功！");
//		}
//	}
	
//	/**
//	 * @Name 5700_DP
//	 * @Catalogue 我的-我的收藏-收藏的店铺
//	 * @Grade 高级
//	 * @FunctionPoint 我的-我的收藏-收藏的店铺  检查收藏的店铺页面UI显示，以及删除功能是否验证正确。
//	 */
//	@Test
//	public void test_5700_DP() {
//		reports_MyTest.startTest("test_5700_WDSC");
//		int i = 0;
//		gotocate(5);
//		wait(1);
//		reports_MyTest.log(LogStatus.INFO, "进入我的收藏");
//		clickElementById("tvFavorateCarNum");//点击我的收藏
//		wait(1);
//		clickElementByName("收藏的店铺");
//		if(CheckViewVisibilty(By.id("tvEmptyMsgTextTop"))&&
//				CheckViewVisibilty(By.id("btEmptyMsgButton"))) {
//			reports_MyTest.log(LogStatus.INFO, "暂时没有关注的店铺！");
//			System.out.println("暂时没有关注的店铺！");
//		}else if(CheckViewVisibilty(By.id("tvShopName"))&&
//				CheckViewVisibilty(By.id("tvShopAdress"))) {
//			reports_MyTest.log(LogStatus.INFO, "我关注的店铺页面UI显示正常！");
//			System.out.println("我关注的店铺页面UI显示正常！");
//			
//			WebElement parent = findElementById("android:id/list");
//			List<WebElement> list = parent.findElements(By.id("tvShopName"));
//			int old_sum = list.size();
////			System.out.println(old_sum);
//			if(old_sum == 1) {      //当前页面收藏店铺个数为1的时候，不进行删除操作
//				reports_MyTest.log(LogStatus.INFO, "当前收藏店铺个数为1。");
//				System.out.println("当前收藏店铺个数为1。");
//			}else{
//				while(old_sum > 6 && i < 30) {//当前页面收藏店铺个数大于6的时候，进行删除操作，直到删除到6个消息为止
//					list = parent.findElements(By.className("android.widget.LinearLayout"));
//					slidingInElement(list.get(1), "left");
////					slidingInElement(driver.findElement(By.className("android.widget.LinearLayout")), "left");
//					old_sum = parent.findElements(By.id("tvShopName")).size();
//					i++;
//				}		
//				if(i == 30) {
//					reports_MyTest.log(LogStatus.ERROR, "收藏店铺左滑删除失败，请检查。");
//					failAndMessage("收藏店铺左滑删除失败，请检查。");
//				}
//				list = parent.findElements(By.className("android.widget.LinearLayout"));
//				slidingInElement(list.get(1), "left");
////				slidingInElement(driver.findElement(By.className("android.widget.LinearLayout")), "left");
//				int new_num = parent.findElements(By.id("tvShopName")).size();
//				if(new_num == old_sum-1) {
//					reports_MyTest.log(LogStatus.INFO, "我收藏的店铺，左滑删除成功！");
//					System.out.println("我收藏的店铺，左滑删除成功！");
//				}else {
//					reports_MyTest.log(LogStatus.ERROR, "我收藏的店铺，左滑删除失败！");
//					failAndMessage("我收藏的店铺，左滑删除失败！");
//				}	
//			}	
//		}
//	}
	
	/**
	 * @Name 5700_GUANZHU
	 * @Catalogue 我的-我的关注
	 * @Grade 高级
	 * @FunctionPoint 我的-我的关注，检查关注清单为空时
	 */
	@Test
	public void test_5700_GUANZHU() {
		reports_MyTest.startTest("test_5700_GUANZHU");
		gotocate(5);
		wait(2);
		reports_MyTest.log(LogStatus.INFO, "获取我的关注的个数");
		String str1 = findElementById("tvFavorateCarNum").getText();	
		clickElementById("tvMyCollect");
		wait(4);
		reports_MyTest.log(LogStatus.INFO, "点击我的关注");
		if(str1.equals("0")) {   //我的关注为空	
			System.out.println("我的关注为空");
		}else {
			reports_MyTest.log(LogStatus.INFO, "我的关注不为空时，全选-删除");
			clickElementById("iv_edit");//点击修改
			clickElementById("ll_bottom_button_edit");//点击全选
			clickElementById("tv_commit");//点击删除
			sleep(500);
			if(CheckViewVisibilty(By.id("tvDialogMessage"))) {
				clickElementById("bt_confirm_ok");
			}
		}
		wait(1);
		reports_MyTest.log(LogStatus.INFO, "检查我的关注默认图。");
		if(CheckViewVisibilty(By.id("tv_see_car_empty"))){
			reports_MyTest.log(LogStatus.INFO, "我的关注，默认图显示正常。");
			System.out.println("我的关注，默认图显示正常。");
		}else {
			reports_MyTest.log(LogStatus.ERROR, "我的关注，默认图显示异常，或者全选删除失败，请查看。");
			GetScreenshot("test_5700_GUANZHU");
			failAndMessage("我的关注，默认图显示异常，或者全选删除失败，请查看。");
		}
	}
	
	/**
	 * @Name 5700_GUANZHU_LIST
	 * @Catalogue 我的-我的关注
	 * @Grade 高级
	 * @FunctionPoint 我的-我的关注，检查关注清单中的车辆显示
	 */
	@Test
	public void test_5700_GUANZHU_LIST() {
		reports_MyTest.startTest("test_5700_GUANZHU_LIST");
		gotocate(5);
		wait(2);
		reports_MyTest.log(LogStatus.INFO, "获取我的关注的个数");
		String str1 = findElementById("tvFavorateCarNum").getText();	 	
		if(str1.equals("0")) {   //我的关注为空	
			System.out.println("我的关注为空,获取关注数为0");
			reports_MyTest.log(LogStatus.INFO, "我的关注为空时，去车市页的详情页点击关注，加入关注清单。");
			gotoCateSet(1);
			wait(3);
			toDetail(1);
			wait(1);
			clickElementById("rlFocus");
			driver.pressKeyCode(AndroidKeyCode.BACK);
			gotoCateSet(4);			
		}
		reports_MyTest.log(LogStatus.INFO, "我的关注已有车辆时，点击我的关注，检查页面显示。");
		clickElementById("tvMyCollect");
		wait(4);
		if(//CheckViewVisibilty(By.id("com.uxin.usedcar:id/tv_see_car_list"))&&
				CheckViewVisibilty(By.id("tvCarWholeName"))&&
				CheckViewVisibilty(By.id("tvPrice"))&&
				CheckViewVisibilty(By.id("ivItemPic"))&&
				CheckViewVisibilty(By.id("tvMileage"))&&
				CheckViewVisibilty(By.id("tvAge"))) {
			reports_MyTest.log(LogStatus.INFO, "我的关注有车辆时，页面显示正常。");
			System.out.println("我的关注有车辆时，页面显示正常。");
		}else {
			reports_MyTest.log(LogStatus.ERROR, "我的关注有车辆时，页面显示异常，请查看。");
			GetScreenshot("test_5700_GUANZHU_LIST");
			failAndMessage("我的关注有车辆时，页面显示异常，请查看。");
		}
	}
		
	/**
	 * @Name 5900_LLLS_2
	 * @Catalogue 我的-浏览历史
	 * @Grade 高级
	 * @FunctionPoint 我的-浏览历史 检查浏览历史的车辆（图片、标题、地址、车龄UI元素是否存在），以及删除功能是否验证正确。
	 */
	@Test		
	 public void test_5900_LLLS_2(){
		reports_MyTest.startTest("test_5900_LLLS_2");
		 gotocate(5);
		 wait(1);
		 reports_MyTest.log(LogStatus.INFO, "获取我的浏览历史个数");
		 String str1 = findElementById("tvHistoryCarNum").getText();	
		 if(str1.equals("100")) {//浏览历史个数大于等于100
			 reports_MyTest.log(LogStatus.INFO, "浏览历史大于等于100个，请手工删除后再执行。");
			 System.out.println("浏览历史大于等于100个，请手工删除后再执行。");
		 }else {
			 if (str1.equals("0")) {//没有浏览历史
				 gotocate(2);
				 wait(1);
				 toDetail(1);
				 wait(1);
				 clickElementById("imgBtBack");
				 wait(1);
				 gotocate(5);
				 wait(1);
				 str1 = findElementById("tvHistoryCarNum").getText();
			 }
			 reports_MyTest.log(LogStatus.INFO, "进入浏览历史页面");
			 clickElementByName("浏览历史");
			 wait(1);
			 checkTitlebar1("浏览历史"); 
			 //检查浏览历史列表页
			 if(CheckViewVisibilty(By.id("rootLine"))&&         //整体
//					 CheckViewVisibilty(By.id("tvCityName"))&&   //车辆名称
					 CheckViewVisibilty(By.id("tvAge"))&&        //车龄
					 CheckViewVisibilty(By.id("tvMileage"))&&   //公里
					 CheckViewVisibilty(By.id("tvPrice"))&&     //价格
					 CheckViewVisibilty(By.id("ivItemPic"))) {  //图片
				 reports_MyTest.log(LogStatus.INFO, "浏览历史页面显示正常！");
				 System.out.println("浏览历史页面显示正常！");
			 }else {
				 reports_MyTest.log(LogStatus.ERROR, "浏览历史页面显示异常！");
				 GetScreenshot("test_5900_LLLS_2_浏览历史页面");
				 failAndMessage("浏览历史页面显示异常！");
			 }
			 slidingInElement(findElementById("rootLine"), "left");
			 sleep(200);
			 clickElementById("imgBtBack");
			 sleep(500);
			 if(str1.equals(findElementById("tvHistoryCarNum").getText())) {
				 reports_MyTest.log(LogStatus.ERROR, "浏览历史页面的车辆左滑删除失败！");
				 failAndMessage("浏览历史页面的车辆左滑删除失败！");
			 }else {
				 reports_MyTest.log(LogStatus.INFO, "浏览历史页面的车辆左滑删除成功！");
				 System.out.println("浏览历史页面的车辆左滑删除成功！");
			 }
		} 
	 }

	/**
	 * @Name 5900_YYJL_3
	 * @Catalogue 我的-预约记录/看车日程
	 * @Grade 高级
	 * @FunctionPoint 我的-如果是预约记录 检查没有预约记录的页面UI检测和有预约记录的车辆（图片、标题、地址、车龄UI元素是否存在），以及删除功能是否验证正确。
	 *   如果是看车日程，检查看车日程页面
	 */
	@Test
	 public void test_5900_YYJL_3(){
		 reports_MyTest.startTest("test_5900_YYJL_3");
		 gotocate(5);
		 wait(1);
		 if(CheckViewVisibilty(By.id("rlWatchCarSchedule"))) {//看车日程
			 reports_MyTest.log(LogStatus.INFO, "点击看车日程");
			 clickElementById("rlWatchCarSchedule");
			 wait(1);
			 if(CheckViewVisibilty(By.id("btEmptyMsgButton"))) {
				 clickElementById("btEmptyMsgButton");
				 wait(1);
				 if(findElementByName("买车").getAttribute("checked").equals("true")){
					 reports_MyTest.log(LogStatus.INFO, "看车日程，点击立即预约看车，进入买车tab页。");
			        System.out.println("看车日程，点击立即预约看车，进入买车tab页。");
			     }else {
			        GetScreenshot("test_1100_MC_maiche");
			        reports_MyTest.log(LogStatus.ERROR, "看车日程，点击立即预约看车，没有进入买车tab页。");
			       	failAndMessage("看车日程，点击立即预约看车，没有进入买车tab页。");
				}
			 }
		 }else {//预约记录
			 reports_MyTest.log(LogStatus.INFO, "获取我的预约记录个数");
			 String str1 = findElementById("tvOrderNum").getText();
			 if (str1.equals("0")) {//没有预约记录
				 reports_MyTest.log(LogStatus.INFO, "没有预约记录的时候去详情页预约个车。");
				 clickElementByName("预约记录");
				 wait(1);
				 clickElementByName("去看看车");
				 wait(1);
				 toDetail(1);
				 clickElementById("reserve_btTop");
				 wait(1);
				 clickElementById("enable_time_tv");
				 clickElementById("reserveOkBtn");
				 wait(5);
				 clickElementById("imgBtBack");
				 gotocate(5);
				 wait(1);
				 str1 = findElementById("tvOrderNum").getText();
			 }	
			 reports_MyTest.log(LogStatus.INFO, "点击进入预约记录页面");
			 clickElementByName("预约记录");
			 wait(1);
			 checkTitlebar1("预约记录");
			 wait(1);
			 if(CheckViewVisibilty(By.id("rootLine"))&&  //整体
					 CheckViewVisibilty(By.id("tvCityName"))&&
					 CheckViewVisibilty(By.id("tvAge"))&&
					 CheckViewVisibilty(By.id("tvMileage"))&&
					 CheckViewVisibilty(By.id("tvPrice"))&&
					 CheckViewVisibilty(By.id("ivItemPic"))) {
				 reports_MyTest.log(LogStatus.INFO, "预约记录车辆UI显示正常！");
				 System.out.println("预约记录车辆UI显示正常！");
			 }else {
				 reports_MyTest.log(LogStatus.ERROR, "预约记录车辆UI显示异常！");
				 GetScreenshot("test_5900_YYJL_3_预约记录");
				 failAndMessage("预约记录车辆UI显示异常！");
			 }
			 
			 reports_MyTest.log(LogStatus.INFO, "预约记录左滑删除车辆");
			 slidingInElement(findElementById("rootLine"), "left");//左滑控件删除
			 clickElementById("imgBtBack");
			 if(str1.equals(findElementById("tvOrderNum").getText())) {
				 reports_MyTest.log(LogStatus.ERROR, "预约记录车辆左滑删除失败！");
				 failAndMessage("预约记录车辆左滑删除失败！");
			 }else {
				 reports_MyTest.log(LogStatus.INFO, "预约记录车辆左滑删除成功！");
				 System.out.println("预约记录车辆左滑删除成功！");
			}	 
		 } 
	 }
	
	/**
	 * @Name 5900_WDDY_4
	 * @Catalogue 我的-我的订阅
	 * @Grade 高级
	 * @FunctionPoint 我的-我的订阅  订阅无记录页面UI检测，订阅有记录页面UI检测 以及删除订阅操作。
	 */
	@Test
	 public void test_5900_WDDY_4(){
		reports_MyTest.startTest("test_5900_WDDY_4");
		 gotocate(5);
		 wait(2);
		 reports_MyTest.log(LogStatus.INFO, "点击进入我的订阅页面");
		 clickElementByName("我的订阅");
		 wait(2);
		 checkTitlebar1("我的订阅");
		 if(CheckViewVisibilty(By.id("tvEmptyMsgTextTop"))) {   //您还没有订阅车源
			 if(CheckViewVisibilty(By.id("btEmptyMsgButton"))) {   //添加订阅
				 reports_MyTest.log(LogStatus.INFO, "我的订阅页面，无订阅记录时页面UI显示正常！");
				 System.out.println("我的订阅页面，无订阅记录时页面UI显示正常！");
				 
			 }else {
				 reports_MyTest.log(LogStatus.ERROR, "我的订阅页面，无订阅记录时页面UI显示异常！");
				 GetScreenshot("test_5900_WDDY_4_订阅页面_无记录");
				 failAndMessage("我的订阅页面，无订阅记录时页面UI显示异常！");
			}
			 clickElementById("btEmptyMsgButton");//添加订阅
			 wait(2);
			 checkTitlebar1("订阅车源");
			 clickElementByName("面包车");
			 clickElementById("add_subscription_filte_lly");    //添加订阅按钮
		 }
		 wait(3);
		 if(CheckViewVisibilty(By.id("mysub_content_headid"))&&  //订阅条件
				 CheckViewVisibilty(By.id("carUpdateTip"))&&     //更新按钮
				 CheckViewVisibilty(By.id("rootLine"))) {       //车辆信息
			int width = driver.manage().window().getSize().width;
			int height = driver.manage().window().getSize().height;
			driver.swipe(width/2, height*3/4, width/2, height*2/4, 1000);
			if(CheckViewVisibilty(By.id("sub_editTv"))&&        //编辑按钮   
				 CheckViewVisibilty(By.id("sub_deleteTv"))){  //删除按钮
				 reports_MyTest.log(LogStatus.INFO, "我的订阅页面，订阅有记录时页面UI显示正常！");
				 System.out.println("我的订阅页面，订阅有记录时页面UI显示正常！");
				 clickElementById("sub_editTv");//编辑按钮
				 wait(1);
				 checkTitlebar1("订阅车源");
				 if(CheckViewVisibilty(By.id("add_subscription_filte_lly"))) {  //添加订阅
					 reports_MyTest.log(LogStatus.INFO, "我的订阅页面，订阅有记录时编辑按钮操作正常！");
					 System.out.println("我的订阅页面，订阅有记录时编辑按钮操作正常！");
				 }else {
					 reports_MyTest.log(LogStatus.ERROR, "我的订阅页面，订阅有记录时编辑按钮操作异常！");
					 System.err.println("我的订阅页面，订阅有记录时编辑按钮操作异常！");
				 }
			}
			clickElementById("imgBtBack");
//			clickElementById("sub_deleteTv");//删除按钮
//			if(CheckViewVisibilty(By.id("alertdialog_body_content"))) {  //确认删除订阅吗？
//				clickElementByName("确定");
//				reports_MyTest.log(LogStatus.INFO, "我的订阅页面，订阅有记录时删除按钮操作正常！");
//				System.out.println("我的订阅页面，订阅有记录时删除按钮操作正常！");
//				
//			}else {
//				reports_MyTest.log(LogStatus.ERROR, "我的订阅页面，订阅有记录时删除按钮操作异常！");
//				failAndMessage("我的订阅页面，订阅有记录时删除按钮操作异常！");
//			}
		 }else {
			 reports_MyTest.log(LogStatus.ERROR, "我的订阅页面，订阅有记录时页面UI显示异常！");
			 GetScreenshot("test_5900_WDDY_4_订阅页面_有记录_异常");
			 failAndMessage("我的订阅页面，订阅有记录时页面UI显示异常！");
		}
	 }
			
	/**
	 * @Name 5400_WDC
	 * @Catalogue 我的-我买的车-我买的车介绍页
	 * @Grade 高级
	 * @FunctionPoint 我的-我买的车-我买的车介绍页，检测我的车介绍页页面UI检索
	 */
	@Test
	public void test_5400_WDC(){
		reports_MyTest.startTest("test_5400_WDC");
		gotocate(5);
		wait(1);
		reports_MyTest.log(LogStatus.INFO, "点击进入我买的车");
		clickElementById("vgUserMyBuyCarDefault");
		wait(1);
//		checkTitlebar1("我买的车");
		if(CheckViewVisibilty(By.id("tvEmptyMsgTextTop"))) {  //您最近还没有通过优信购买车辆
			reports_MyTest.log(LogStatus.INFO, "我买的车页面显示正常！");
			System.out.println("我买的车页面显示正常！");
		}
	}
	
	/**
	 * @Name 5900_WMDC_1
	 * @Catalogue 我的-我卖的车
	 * @Grade 高级
	 * @FunctionPoint 我的-我卖的车 检查没有发布车辆的页面UI检测和已发布的车辆（图片、标题、地址、车龄UI元素是否存在），以及删除功能是否验证正确。
	 */
	@Test
	 public void test_5900_WMDC_1(){
		 reports_MyTest.startTest("test_5900_WMDC_1");
		 gotocate(5);
		 wait(2);
		 reports_MyTest.log(LogStatus.INFO, "点击我卖的车");
		 clickElementByName("我卖的车");
		 wait(2);
		 checkTitlebar1("我卖的车");
		 wait(2);
		 if(CheckViewVisibilty(By.id("tvEmptyMsgTextTop"))) {   //您还没有发布的车辆
			 reports_MyTest.log(LogStatus.INFO, "没有发布的车辆");
			 System.out.println("没有发布的车辆");
//			 clickElementByName("立即发布车辆");
//			 checkTitlebar_Webview("我要卖车");
		 }
		 else if (CheckViewVisibilty(By.id("tvCarNameC2B"))&&    //标题
				 CheckViewVisibilty(By.id("tvC2bButton"))&&      //删除按钮
				 CheckViewVisibilty(By.id("tvPublishCityC2B"))&&  //  城市
				 CheckViewVisibilty(By.id("tvPublishTimeC2B"))&&   //   时间
				 CheckViewVisibilty(By.id("tvStatusC2B"))) {       //   交易状态 
			 reports_MyTest.log(LogStatus.INFO, "C2B车辆信息显示正常。");
			 System.out.println("C2B车辆信息显示正常。");
		 }		 
	 }
	
	/**
	 * @Name 5900_WDFQ
	 * @Catalogue 我的-我的分期
	 * @Grade 高级
	 * @FunctionPoint 我的-我的分期
	 */
	@Test
	 public void test_5900_WDFQ(){
		 reports_MyTest.startTest("test_5900_WDFQ");
		 gotocate(5);
		 wait(2);
		 reports_MyTest.log(LogStatus.INFO, "点击我的分期");
		 clickElementByName("我的分期");
		 wait(2);
		 checkTitlebar1("我的分期");
		 wait(2);
		 if(CheckViewVisibilty(By.id("tvEmptyMsgTextTop"))) {   //您还没有相关记录
			 reports_MyTest.log(LogStatus.INFO, "还没有相关记录");
			 System.out.println("还没有相关记录");
		 }
		 else {    
//			 reports_MyTest.log(LogStatus.INFO, "我的分期信息显示正常。");
//			 System.out.println("我的分期信息显示正常。");
		 }		 
	 }
	
	/**
	 * @Name 5900_WDHT
	 * @Catalogue 我的-我的合同
	 * @Grade 高级
	 * @FunctionPoint 我的-我的合同
	 */
	@Test
	 public void test_5900_WDHT(){
		 reports_MyTest.startTest("test_5900_WDHT");
		 gotocate(5);
		 wait(2);
		 reports_MyTest.log(LogStatus.INFO, "点击我的合同");
		 clickElementByName("我的合同");
		 wait(2);
		 checkTitlebar1("合同列表");
		 wait(2);
		 if(CheckViewVisibilty(By.id("tvEmptyMsgTextTop"))) {   //您还没有相关记录
			 reports_MyTest.log(LogStatus.INFO, "还没有相关记录");
			 System.out.println("还没有相关记录");
		 }
		 else {    
//			 reports_MyTest.log(LogStatus.INFO, "我的合同信息显示正常。");
//			 System.out.println("我的合同信息显示正常。");
		 }		 
	 }
	
	
	/**
	 * @Name 5900_WDWD_4
	 * @Catalogue 我的-我的宝典-我的提问
	 * @Grade 高级
	 * @FunctionPoint 我的-我的宝典-我的提问 问题解决状态 问题发布时间 元素UI层检测
	 */
	@Test
	 public void test_5900_WDDA_4(){
		 reports_MyTest.startTest("test_5900_WDDA_4");
		 gotocate(5);
		 wait(2);
		 if(findElementById("rbFaXian").getText().equals("关注")) {
			 reports_MyTest.log(LogStatus.INFO, "关注方案，无我的宝典");
		 }else {
			 reports_MyTest.log(LogStatus.INFO, "进入我的宝典页面");
			 clickElementByName("我的宝典");
			 wait(1);
			 checkTitlebar1("我的宝典");
			 if(CheckViewVisibilty(By.id("myQuestion"))&&              //问题
					 CheckViewVisibilty(By.id("myQuestionStatus"))&&   //问题解决状态
					 CheckViewVisibilty(By.id("myQuestionTime"))) {    //问题发布时间
				 reports_MyTest.log(LogStatus.INFO, "我的宝典页面，问题、问题解决状态和问题发布时间UI显示正常！");
				 System.out.println("我的宝典页面，问题、问题解决状态和问题发布时间UI显示正常！");
			 }else {
				 reports_MyTest.log(LogStatus.INFO, "该用户未提交过问答!");
				 System.out.println("该用户未提交过问答!");
			 }
		 }
	 }
		
	/**
	 * @Name 5901_WDWD_4
	 * @Catalogue 我的-我的宝典
	 * @Grade 高级
	 * @FunctionPoint 我的-我的宝典-我的提问 ，问答内容的标签点击
	 */
	@Test
	 public void test_5901_WDWD_tagclick(){
		reports_MyTest.startTest("test_5901_WDWD_tagclick");
		 gotocate(5);
		 wait(2);
		 if(findElementById("rbFaXian").getText().equals("关注")) {
			 reports_MyTest.log(LogStatus.INFO, "关注方案，无我的宝典");
		 }else {
			 reports_MyTest.log(LogStatus.INFO, "进入我的宝典页面");
			 clickElementByName("我的宝典");
			 wait(2);
			 checkTitlebar1("我的宝典");
			 if(CheckViewVisibilty(By.id("myQuestion"))) {
				 clickElementById("myQuestion");
				 wait(2);
				 checkTitlebar_Webview("车辆问答");
				 reports_MyTest.log(LogStatus.INFO, "我的宝典，点击问答进入问答详情页!");
				 System.out.println("我的宝典，点击问答进入问答详情页!");
			 }else {
				 reports_MyTest.log(LogStatus.INFO, "该用户未提交过问答!");
				 System.out.println("该用户未提交过问答!");
			 }	 
		 }
	 }
		
	/**
	 * @Name 5902_WDDA_status_1
	 * @Catalogue 我的-我的宝典-我的提问
	 * @Grade 高级
	 * @FunctionPoint 我的-宝典-我的提问， 问题待解决状态及详情页的界面检查
	 */
	@Test
	 public void test_5902_WDDA_status_1(){
		reports_MyTest.startTest("test_5902_WDDA_status_1");
		 gotocate(5);
		 wait(2);
		 if(findElementById("rbFaXian").getText().equals("关注")) {
			 reports_MyTest.log(LogStatus.INFO, "关注方案，无我的宝典");
		 }else {
			 reports_MyTest.log(LogStatus.INFO, "进入我的宝典");
			 clickElementByName("我的宝典");
			 wait(2);
			 checkTitlebar1("我的宝典");
			 if(CheckViewVisibilty(By.id("myQuestion"))) {
				 reports_MyTest.log(LogStatus.INFO, "我的宝典，点击问答进入问答详情页!");
				 System.out.println("我的宝典，点击问答进入问答详情页!");
				 int i = 0;
				 WebElement parent = findElementById("android:id/list");
				 List<WebElement> list = parent.findElements(By.id("myQuestionStatus"));
				 int count = list.size();
				 if(count == 0) {
					 reports_MyTest.log(LogStatus.INFO, "该用户未提交过问答!");
					 System.out.println("该用户未提交过问答!");
				 }else {
					 for(i=1; i< count+1; i++) {	 
						 String status = findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
						 		+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]"
						 		+ "/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.support.v4.view.ViewPager[1]"
						 		+ "/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
						 		+ "/android.widget.ListView[1]/android.widget.LinearLayout["+ i +"]/android.widget.RelativeLayout[1]"
						 		+ "/android.widget.TextView[2]").getText();
						 if(status.equals("待解决")) {
							 clickElementByName("待解决");
//							 try {
//								 switchToWebView();
//							 } catch (Exception e) {
//								 System.out.println("第一次切换失败尝试再次切换");
//								 driver.pressKeyCode(AndroidKeyCode.BACK);
//								 wait(1);
//								 clickElementByName("待解决");
//								 switchToWebView();
//							 }
//							 if(CheckViewVisibilty(By.xpath("/html/body/div[1]/div/a/span[2]"))) {//待解决
//								 if(findElementByXpath("/html/body/div[1]/div/a/span[2]").getText().equals("待解决")) {
//									 reports_MyTest.log(LogStatus.INFO, "待解决问答详情页显示正常");
//									 System.out.println("待解决问答详情页显示正常");
//								 }
//							 }else if(CheckViewVisibilty(By.xpath("//*[@id=\"ques_main\"]/div"))){
//								 if(findElementByXpath("//*[@id=\"ques_main\"]/div").getText().equals("相关问题已被删除")) {
//									 reports_MyTest.log(LogStatus.INFO, "相关问题已被删除");
//									 System.out.println("相关问题已被删除");
//								 }
//							 }else {
//								 reports_MyTest.log(LogStatus.ERROR, "待解决问答详情页显示异常");
//								 GetScreenshot("test_5902_WDDA_status_1");
//								 failAndMessage("待解决问答详情页显示异常");
//							}
							 break;
						 }else if(i == count+1) {
							 reports_MyTest.log(LogStatus.ERROR, "该用户没有待解决的问答。");
							 System.out.println("该用户没有待解决的问答。");
						 }
					 }
				 }
			 }
		 }
	 }
	
	/**
	 * @Name 5902_WDDA_status_2
	 * @Catalogue 我的-我的宝典-我的提问
	 * @Grade 高级
	 * @FunctionPoint 我的-宝典-我的提问， 问题1个回答状态及详情页对应的界面
	 */
	@Test
	public void test_5902_WDDA_status_2(){
		reports_MyTest.startTest("test_5902_WDDA_status_2");
		gotocate(5);
		wait(2);
		if(findElementById("rbFaXian").getText().equals("关注")) {
			reports_MyTest.log(LogStatus.INFO, "关注方案，无我的宝典");
		}else {
			reports_MyTest.log(LogStatus.INFO, "进入我的宝典");
			clickElementByName("我的宝典");
			wait(2);
			checkTitlebar1("我的宝典");
			if(CheckViewVisibilty(By.id("myQuestion"))) {
				reports_MyTest.log(LogStatus.INFO, "我的宝典，点击问答进入问答详情页!");
				System.out.println("我的宝典，点击问答进入问答详情页!");
				int i = 0;
				WebElement parent = findElementById("android:id/list");
				List<WebElement> list = parent.findElements(By.id("myQuestionStatus"));
				int count = list.size();
				if(count == 0) {
					reports_MyTest.log(LogStatus.INFO, "该用户未提交过问答!");
					System.out.println("该用户未提交过问答!");
				}else {
					for(i=1; i< count+1; i++) {	
						String status = findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
						 		+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]"
						 		+ "/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.support.v4.view.ViewPager[1]"
						 		+ "/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
						 		+ "/android.widget.ListView[1]/android.widget.LinearLayout["+ i +"]/android.widget.RelativeLayout[1]"
						 		+ "/android.widget.TextView[2]").getText();
						 if(status.equals("1个回答")) {
							 clickElementByName("1个回答");
//							  try {
//									 switchToWebView();
//							   } catch (Exception e) {
//									 System.out.println("第一次切换失败尝试再次切换");
//									 driver.pressKeyCode(AndroidKeyCode.BACK);
//									 wait(1);
//									 clickElementByName("1个回答");
//									 switchToWebView();
//							   }
//							   if(CheckViewVisibilty(By.xpath("//*[@id=\"ques_main\"]/div[2]"))) {
//							  		reports_MyTest.log(LogStatus.INFO, "问答详情页显示正常");
//							  		System.out.println("问答详情页显示正常");
//							   }else {
//									 reports_MyTest.log(LogStatus.ERROR, "1个回答的问答详情页显示异常");
//									 GetScreenshot("test_5902_WDDA_status_2");
//									 failAndMessage("1个回答的问答详情页显示异常");
//								}
							  break;
						 }else if(i == count+1) {
							 reports_MyTest.log(LogStatus.ERROR, "该用户没有1个回答的问答。");
							 System.out.println("该用户没有1个回答的问答。");
						 }
					}
				}
			}
		}
	}
	
	/**
	 * @Name 5902_WDGZ
	 * @Catalogue 我的-我的宝典-我的关注
	 * @Grade 高级
	 * @FunctionPoint 我的-宝典-我的关注， 关注界面UI检查。
	 */
	@Test
	public void test_5902_WDGZ() {
		reports_MyTest.startTest("test_5902_WDGZ");
		gotocate(5);
		wait(2);
		if(findElementById("rbFaXian").getText().equals("关注")) {
			reports_MyTest.log(LogStatus.INFO, "关注方案，无我的宝典");
		}else {
			reports_MyTest.log(LogStatus.INFO, "进入我的宝典-我的关注");
			clickElementByName("我的宝典");
			wait(1);
			clickElementByName("我的关注");
			wait(1);
			if(CheckViewVisibilty(By.id("myQuestion"))&&              //问题
					 CheckViewVisibilty(By.id("myQuestionStatus"))&&   //问题解决状态
					 CheckViewVisibilty(By.id("myQuestionTime"))) {    //问题发布时间
				 reports_MyTest.log(LogStatus.INFO, "我的宝典-我的关注页面，问题、问题解决状态和问题发布时间UI显示正常！");
				 System.out.println("我的宝典-我的关注页面，问题、问题解决状态和问题发布时间UI显示正常！");
			 }else {
				 reports_MyTest.log(LogStatus.INFO, "暂无关注的问答!");
				 System.out.println("暂无关注的问答!");
			 }
		}
	}
	
	/**
	 * @Name 5900_WDZXSC
	 * @Catalogue 我的-我的宝典-资讯收藏
	 * @Grade 高级
	 * @FunctionPoint 我的-宝典-资讯收藏， 关注界面UI检查。
	 */
	@Test
	public void test_5900_WDZXSC() {
		reports_MyTest.startTest("test_5900_WDZXSC");
		gotocate(5);
		wait(2);
		if(findElementById("rbFaXian").getText().equals("关注")) {
			reports_MyTest.log(LogStatus.INFO, "关注方案，无我的宝典");
		}else {
			reports_MyTest.log(LogStatus.INFO, "进入我的宝典-资讯收藏");
			clickElementByName("我的宝典");
			wait(1);
			clickElementByName("资讯收藏");
			wait(1);
			if(CheckViewVisibilty(By.id("myQuestion"))&&              //资讯名字
					 CheckViewVisibilty(By.id("myQuestionTime"))) {    //资讯发布时间
				 reports_MyTest.log(LogStatus.INFO, "我的宝典-咨询收藏页面，资讯和资讯发布时间UI显示正常！");
				 System.out.println("我的宝典-咨询收藏页面，资讯和资讯发布时间UI显示正常！");
			 }else {
				 reports_MyTest.log(LogStatus.INFO, "暂无咨询收藏!");
				 System.out.println("暂无咨询收藏!");
			 }
		}
	}
	
	/**
	 * @Name 5900_WDBD
	 * @Catalogue 我的-我的宝典
	 * @Grade 高级
	 * @FunctionPoint 我的-宝典，提问、关注和资讯收藏三个tab切换检查
	 */
	@Test
	public void test_5900_WDBD() {
		reports_MyTest.startTest("test_5900_WDBD");
		gotocate(5);
		wait(2);
		if(findElementById("rbFaXian").getText().equals("关注")) {
			reports_MyTest.log(LogStatus.INFO, "关注方案，无我的宝典");
		}else {
			reports_MyTest.log(LogStatus.INFO, "进入我的宝典");
			clickElementByName("我的宝典");
			wait(1);
			slidingInElement(findElementById("android:id/list"), "left");
//			System.out.println(findElementByName("我的关注").getAttribute("checked"));
			if(findElementByName("我的关注").getAttribute("checked").equals("true")) {
				reports_MyTest.log(LogStatus.INFO, "我的宝典-从我的提问左滑成功切换到我的关注！");
				System.out.println("我的宝典-从我的提问左滑成功切换到我的关注！");
			}else {
				reports_MyTest.log(LogStatus.ERROR, "我的宝典-从我的提问左滑切换到我的关注失败！");
				failAndMessage("我的宝典-从我的提问左滑切换到我的关注失败！");
			}
			wait(1);
			slidingInElement(findElementById("android:id/list"), "left");
			if(findElementByName("资讯收藏").getAttribute("checked").equals("true")) {
				reports_MyTest.log(LogStatus.INFO, "我的宝典-从我的关注左滑成功切换到资讯收藏！");
				System.out.println("我的宝典-从我的关注左滑成功切换到资讯收藏！");
			}else {
				reports_MyTest.log(LogStatus.ERROR, "我的宝典-从我的关注左滑切换到资讯收藏失败！");
				failAndMessage("我的宝典-从我的关注左滑切换到资讯收藏失败！");
			}
			wait(1);
			slidingInElement(findElementById("android:id/list"), "right");
			if(findElementByName("我的关注").getAttribute("checked").equals("true")) {
				reports_MyTest.log(LogStatus.INFO, "我的宝典-从资讯收藏右滑成功切换到我的关注！");
				System.out.println("我的宝典-从资讯收藏右滑成功切换到我的关注！");
			}else {
				reports_MyTest.log(LogStatus.ERROR, "我的宝典-从资讯收藏右滑切换到我的关注失败！");
				failAndMessage("我的宝典-从资讯收藏右滑切换到我的关注失败！");
			}
			wait(1);
			slidingInElement(findElementById("android:id/list"), "right");
			if(findElementByName("我的提问").getAttribute("checked").equals("true")) {
				reports_MyTest.log(LogStatus.INFO, "我的宝典-从我的关注右滑成功切换到我的提问！");
				System.out.println("我的宝典-从我的关注右滑成功切换到我的提问！");
			}else {
				reports_MyTest.log(LogStatus.ERROR, "我的宝典-从我的关注右滑切换到我的提问失败！");
				failAndMessage("我的宝典-从我的关注右滑切换到我的提问失败！");
			}
		}
	}
	
	/**
	 * @Name 5600_XQBZ_1
	 * @Catalogue 我的-常用工具-限迁标准(h5页面需要用debug包执行)
	 * @Grade 高级
	 * @FunctionPoint 我的-常用工具-限迁标准-按城市查排放标准UI检索(由于切换webview问题暂时先不检查h5的二级页面)
	 */
	@Test
	public void test_5600_XQBZ_1(){
		reports_MyTest.startTest("test_5600_XQBZ_1");
		gotocate(5);
		wait(2);
		reports_MyTest.log(LogStatus.INFO, "点击我的-常用工具");
		clickElementByName("常用工具");
		reports_MyTest.log(LogStatus.INFO, "点击限迁标准");
		clickElementByName("限迁标准");
		wait(3);
		checkTitlebar_Webview("限迁标准");
		wait(1);
//		try {
//			switchToWebView();
//		} catch (Exception e) {
//			System.out.println("第一次切换到webView失败尝试再次切换");
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
//			sliding("", "", "", "up", "");
//			clickElementByName("限迁标准");
//			switchToWebView();
//		}
//		System.out.println(findElementByXpath("/html/body/div/a[1]").getText());	
//		if (findElementByXpath("/html/body/div/a[1]").getText().equals("按城市查排放标准")) {
//			reports_MyTest.log(LogStatus.INFO, "限迁标准-按城市查排放标准显示正常!");
//			System.out.println("限迁标准-按城市查排放标准显示正常!");
//		}
//		clickElementByXpath("/html/body/div/a[1]");  //按城市查排放标准
//		wait(1);
		//需要再次切换到webview
//		System.out.println(findElementByXpath("//*[@id=\"wrapper1\"]/ul/a[2]").getText());
//		clickElementByXpath("//*[@id=\"wrapper1\"]/ul/a[2]");//河北
//		wait(1);
//		System.out.println(findElementByXpath("//*[@id=\"wrapper2\"]/aside/a[6]").getText());
//		clickElementByXpath("//*[@id=\"wrapper2\"]/aside/a[6]");//石家庄
//		wait(1);
//		checkTitlebar_Webview("限迁标准");
//		if (findElementByXpath("/html/body/div/header/p/i").getText().equals("5")) {
//			reports_MyTest.log(LogStatus.INFO, "限迁标准显示正常!");
//			System.out.println("限迁标准显示正常!");
//		}			
	}
	
	/**
	 * @Name 5600_XQBZ_2
	 * @Catalogue 我的-我的工具-限迁标准(h5页面需要用debug包执行)
	 * @Grade 高级
	 * @FunctionPoint 我的-我的工具-限迁标准-按排放标准查限迁城市UI检索(由于切换webview问题暂时先不检查h5的二级页面)
	 */
	@Test
	public void test_5600_XQBZ_2(){
		reports_MyTest.startTest("test_5600_XQBZ_2");
		gotocate(5);
		wait(2);
		reports_MyTest.log(LogStatus.INFO, "点击我的-常用工具");
		clickElementByName("常用工具");
		reports_MyTest.log(LogStatus.INFO, "点击限迁标准");
		clickElementByName("限迁标准");
		wait(3);
		checkTitlebar_Webview("限迁标准");
		wait(1);
//		try {
//			switchToWebView();
//		} catch (Exception e) {
//			System.out.println("第一次切换到webView失败尝试再次切换");
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
//			sliding("", "", "", "up", "");
//			clickElementByName("限迁标准");
//			switchToWebView();
//		}
//		if (findElementByXpath("/html/body/div/a[2]").getText().equals("按排放标准查限迁城市")) { 
//			reports_MyTest.log(LogStatus.INFO, "限迁标准-按排放标准查限迁城市显示正常!");
//			System.out.println("限迁标准-按排放标准查限迁城市显示正常!");
//		}
//		clickElementByXpath("/html/body/div/a[2]");//按排放标准查限迁城市
//		wait(2);
//		clickElementByXpath("/html/body/div/ul/li[2]");//国4
//		wait(3);
//		if (CheckViewVisibilty(By.xpath("/html/body/div/div[2]/article[1]/span"))) {   //福建
//			reports_MyTest.log(LogStatus.INFO, "按排放标准查限迁城市显示正常！");
//			System.out.println("按排放标准查限迁城市显示正常！");
//		}
	}
	
	/**
	 * @Name 5600_WXBYJL_1
	 * @Catalogue 我的-我的工具-维修保养记录
	 * @Grade 高级
	 * @FunctionPoint 我的-我的工具-维修保养记录-查询记录，检测查询记录页面显示是否正常
	 */
//	@Test
//	public void test_5600_WXBYJL_1(){
//		reports_MyTest.startTest("test_5600_WXBYJL_1");
//		gotocate(5);
//		wait(2);
//		sliding("","","","up","");
//		reports_MyTest.log(LogStatus.INFO, "点击我的-维修保养记录");
//		clickElementByName("维修保养记录");
//		wait(2);
//		checkTitlebar1("查询记录");
//		if (CheckViewVisibilty(By.name("您暂时还没有查询记录，"))) {
//			reports_MyTest.log(LogStatus.INFO, "暂未查询记录！");
//			System.out.println("暂未查询记录！");
//		}else {
//			reports_MyTest.log(LogStatus.INFO, "有查询记录！");
//			System.out.println("检索有记录!");
//		}		
//	}
	
	/**
	 * @Name 5600_WXBYJL_2
	 * @Catalogue 我的-我的工具-维修保养记录
	 * @Grade 高级
	 * @FunctionPoint 我的-我的工具-维修保养记录UI检索-以及检索一条正确数据查询成功。
	 */
//	@Test
//	public void test_5600_WXBYJL_2(){
//		reports_MyTest.startTest("test_5600_WXBYJL_2");
//		gotocate(5);
//		wait(3);
//		sliding("","","","up","");
//		reports_MyTest.log(LogStatus.INFO, "点击我的-维修保养记录");
//		clickElementByName("维修保养记录");
//		wait(2);
//		checkTitlebar1("查询记录");
//		if (CheckViewVisibilty(By.name("您暂时还没有查询记录，"))) {
//			reports_MyTest.log(LogStatus.INFO, "暂未查询记录！");
//			System.out.println("暂未查询记录！");
//		}else {
//			wait(2);
//			if(getTextById("tv_query_status").equals("查询失败")) {
//				clickElementById("tvCarName");
//				wait(1);
//				if(CheckViewVisibilty(By.name("该车4S店系统正在维护中，无法查询"))) {
//					reports_MyTest.log(LogStatus.INFO, "查维保-查询失败的页面显示正常！");
//					System.out.println("查维保-查询失败的页面显示正常！");
//				}
//			}else if(getTextById("tv_query_status").equals("查询成功")){
//				clickElementById("tvCarName");
//				wait(3);
//				reports_MyTest.log(LogStatus.INFO, "查维保-查询成功的页面可以正常进入！");
//				checkTitlebar_Webview("车辆历史报告");
//			}
//		}			
//	}
	
	/**
	 * @Name 5600_WXBYJL_3
	 * @Catalogue 我的-我的工具-维修保养记录(有h5页面的检查，需要在debug包中执行)
	 * @Grade 高级
	 * @FunctionPoint 我的-我的工具-维修保养记录UI检索-检查车辆检测报告页面是否显示正常
	 */
//	@Test
//	public void test_5600_WXBYJL_3(){
//		reports_MyTest.startTest("test_5600_WXBYJL_3");
//		gotocate(5);
//		wait(1);
//		sliding("","","","up","");
//		reports_MyTest.log(LogStatus.INFO, "点击我的-维修保养记录");
//		clickElementByName("维修保养记录");
//		wait(2);
//		checkTitlebar1("查询记录");
//		if (CheckViewVisibilty(By.name("您暂时还没有查询记录，"))) {
//			reports_MyTest.log(LogStatus.INFO, "暂未查询记录！");
//			System.out.println("暂未查询记录！");
//		}
//		wait(2);
//		if(getTextById("tv_query_status").equals("查询成功")){
//			clickElementById("tvCarName");
//			wait(5);
//			checkTitlebar_Webview("车辆历史报告");
//			wait(2);
//			try {
//				switchToWebView();
//			} catch (Exception e) {
//				System.out.println("第一次切换失败，尝试第二次切换");
//				driver.pressKeyCode(AndroidKeyCode.BACK);
//				wait(1);
//				clickElementById("tvCarName");
//				switchToWebView();
//			}
//			String text1 = findElementByXpath("/html/body/div/div/div/dl/dd/div/span").getText();//重点检测项
//			String text2 = findElementByXpath("/html/body/div/div/div/dl/dt/span").getText();//本报告基于4S店维保记录
//			System.out.println(text1);
//			System.out.println(text2);		
//			if ( text1.equals("重点检测项") && text2.equals("本报告基于4S店维保记录")){
//				reports_MyTest.log(LogStatus.INFO, "车辆检测报告页面显示正常！");
//				System.out.println("车辆检测报告页面显示正常！");
//			}else {
//				reports_MyTest.log(LogStatus.ERROR, "车辆检测报告显示异常！");
//				GetScreenshot("test_5600_WXBYJL_3_车辆检测报告");
//				failAndMessage("车辆检测报告显示异常！");
//			}
//		}
//	}	
		
//	/**
//	 * @Name 5600_WSGCN
//	 * @Catalogue 我的-常用工具-优信认证（h5页面需要用debug包执行）
//	 * @Grade 高级
//	 * @Channel 我的-常用工具-优信认证
//	 * @FunctionPoint 我的-常用工具-优信认证-优信认证页面UI(由于切换webview问题暂时先不检查h5的二级页面)
//	 */
//	@Test
//	public void test_5600_WSGCN(){
//		reports_MyTest.startTest("test_5600_WSGCN");
//		int i = 0;
//		gotocate(5);
//		wait(2);
//		reports_MyTest.log(LogStatus.INFO, "点击我的-常用工具");
//		clickElementByName("常用工具");
//		reports_MyTest.log(LogStatus.INFO, "点击优信认证");
//		clickElementByName("优信认证");
//		wait(5);
//		checkTitlebar_Webview("优信认证");
//		i = 0;
//		while(i < 4) {               // 循环滑动到屏幕底部
//			sliding("", "", "", "up", "");
//			i = i + 1;
//		}
		
//		try {
//			switchToWebView();
//		} catch (Exception e) {
//			System.out.println("第一次切换失败，尝试第二次切换");
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
//			sliding("", "", "", "up", "");
//			clickElementByName("优信认证");
//			i = 0;
//			while(i < 4) {               // 循环滑动到屏幕底部
//				sliding("", "", "", "up", "");
//				i = i + 1;
//			}
//			switchToWebView();
//		}	
////		System.out.println(findElementByXpath("/html/body/div[8]/a[1]").getText());
//		if(findElementByXpath("/html/body/div[8]/a[1]").getText().equals("质保范围") &&
//				findElementByXpath("/html/body/div[8]/a[2]").getText().equals("售后保障")) {
//			reports_MyTest.log(LogStatus.INFO, "优信认证页面显示正常！");
//			System.out.println("优信认证页面显示正常！");
//		}
//		clickElementByXpath("/html/body/div[8]/a[1]");//质保范围
//		wait(3);
//		switchToWebView();
//		System.out.println(findElementByXpath("/html/body/div[2]/h1").getText());
//		CheckViewVisibilty(By.xpath("/html/body/div[2]/h1"));//30天包退
//		clickElementById("tvBack");
//		clickElementByXpath("/html/body/div[8]/a[2]");//售后保障
//		wait(3);
//		checkTitlebar_Webview("优信认证");
//		CheckViewVisibilty(By.xpath("/html/body/div[1]/h1"));//30天包退，如何退车？

//	}
	
	/**
	 * @Name 5600_WZCX
	 * @Catalogue 我的-常用工具-违章查询
	 * @Grade 高级
	 * @FunctionPoint 我的-常用工具-违章查询UI检测和查询一条错误数据检查点。
	 */
	@Test
	public void test_5600_WZCX(){
		reports_MyTest.startTest("test_5600_WZCX");
		gotocate(5);
		wait(2);
		reports_MyTest.log(LogStatus.INFO, "点击我的-常用工具");
		clickElementByName("常用工具");
		reports_MyTest.log(LogStatus.INFO, "点击违章查询");
		clickElementByName("违章查询");
		wait(1);
		checkTitlebar1("违章查询");
		if(CheckViewVisibilty(By.id("tvCity"))&&
				CheckViewVisibilty(By.id("etCarNumber"))&&
				CheckViewVisibilty(By.id("etCarEngine"))&&
				CheckViewVisibilty(By.id("etCarBody"))) {
			clickElementByName("请选择查询城市");//查询城市
			wait(2);
			checkTitlebar1("选择城市");
			wait(1);
//			clickElementByName("北京");
			clickElementById("tvCity");
			wait(1);
			clickElementById("tvProvince");//省会简称
			clickElementByName("京");
			findElementByName("请输入车牌号").sendKeys("12345");//车牌号
			wait(1);
//			clickElementById("tvQuestionEngine");//点击发动机后面的问号
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
			findElementByName("请输入全部发动机号").sendKeys("88888888");//发动机号
			wait(1);
//			clickElementById("tvQuestionBody");//点击车架号后面的问号
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
			findElementByName("您不需要输入车架号").sendKeys("55555555");//车架号
			wait(1);
			clickElementByName("立即查询");
			wait(1);
			//检查toast的方法
			toastCheck("查询失败，您输入的车牌号有误，请核对后重新查询");
		}else {
			reports_MyTest.log(LogStatus.INFO, "违章查询页面显示异常。");
			failAndMessage("违章查询页面显示异常。");
		}
	}
	
	/**
	 * @Name 5600_WZCX_LS
	 * @Catalogue 我的-我的工具-违章查询
	 * @Grade 高级
	 * @FunctionPoint 我的-我的工具-违章查询-违章查询历史检查。
	 */
	@Test
	public void test_5600_WZCX_LS() {
		reports_MyTest.startTest("test_5600_WZCX_LS");
		gotocate(5);
		wait(2);
		reports_MyTest.log(LogStatus.INFO, "点击我的-常用工具");
		clickElementByName("常用工具");
		reports_MyTest.log(LogStatus.INFO, "点击违章查询");
		clickElementByName("违章查询");
		wait(1);
		checkTitlebar1("违章查询");
		if(findElementById("tvHistory").getAttribute("enabled").equals("true")) {
			reports_MyTest.log(LogStatus.INFO, "有违章查询历史。");
			System.out.println("有违章查询历史。");
			clickElementById("tvHistory");
			wait(1);
			checkTitlebar1("违章查询历史");
			String titleText = findElementByXpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
					+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]"
					+ "/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]"
					+ "/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText();
			if(titleText.equals("以下结果仅供参考，详询当地交管局")) {
				if(CheckViewVisibilty(By.id("tvCarNum"))&&         //车牌号
						CheckViewVisibilty(By.id("tvCity"))&&      //查询城市
						CheckViewVisibilty(By.id("tvEngineNo"))&&   //发动机号
						CheckViewVisibilty(By.id("tvChejiaNo"))) {  //车架号
					reports_MyTest.log(LogStatus.INFO, "违章查询历史显示正常。");
					System.out.println("违章查询历史显示正常。");
					reports_MyTest.log(LogStatus.INFO, "清空违章查询历史。");
					clickElementByName("清空");
					if(CheckViewVisibilty(By.id("alertdialog_body_content"))) {   //确定要清空所有的查违章记录吗？
						clickElementById("alertdialog_confirm");       //确定
						sleep(500);
						driver.pressKeyCode(AndroidKeyCode.BACK);
						wait(1);
						if(findElementById("tvHistory").getAttribute("enabled").equals("false")) {
							reports_MyTest.log(LogStatus.INFO, "清空违章查询历史成功。");
							System.out.println("清空违章查询历史成功。");
						}else {
							reports_MyTest.log(LogStatus.ERROR, "清空违章查询历史失败。");
							System.out.println("清空违章查询历史失败。");
						}
					}
				}
			}
		}else {
			reports_MyTest.log(LogStatus.INFO, "没有违章查询历史。");
			System.out.println("没有违章查询历史。");
		}
	}
	
	
	/**
	 * @Name 5600_SJSQ
	 * @Catalogue 我的-我的工具-商家申请(h5页面，需要在debug包中执行)
	 * @Grade 高级
	 * @FunctionPoint 我的-我的工具-商家申请UI检索 以及马上申请页面UI检测和下载商家APP页面UI检测
	 */
//	@Test
//	public void test_5600_SJSQ(){
//		reports_MyTest.startTest("test_5600_SJSQ");
//		gotocate(5);
//		wait(3);
//		sliding("", "", "", "up", "");
//		reports_MyTest.log(LogStatus.INFO, "点击商家申请！");
//		clickElementByName("商家申请");
//		wait(5);
//		checkTitlebar_Webview("商家申请");
//		try {
//			switchToWebView();
//		} catch (Exception e) {
//			System.out.println("第一次切换失败，尝试第二次切换");
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
//			sliding("", "", "", "up", "");
//			clickElementByName("商家申请");
//			switchToWebView();
//		}	
//		if (findElementByXpath("/html/body/div/div/div/a[1]").getText().equals("马上申请")) {//马上申请
//			reports_MyTest.log(LogStatus.INFO, "商家申请-马上申请UI显示正常！");
//			System.out.println("马上申请UI正常！");
//		}
//		if (CheckViewVisibilty(By.xpath("/html/body/div/div/div/a[2]"))){//下载商家app
//			reports_MyTest.log(LogStatus.INFO, "商家申请-下载商家app显示正常！");
//			System.out.println("下载商家appUI正常！");
//		}
//	}
	
	/**
	 * @Name 5600_CLGJ
	 * @Catalogue 我的-常用工具-车辆估价（线上不能有提交数据，所以不提交，只检查UI。）
	 * @Grade 高级
	 * @FunctionPoint 我的-常用工具-车辆估价UI检索。
	 */
	@Test
	public void test_5600_CLGJ(){
		reports_MyTest.startTest("test_5600_CLGJ");
		gotocate(5);
		wait(3);
		reports_MyTest.log(LogStatus.INFO, "点击我的-常用工具");
		clickElementByName("常用工具");
		reports_MyTest.log(LogStatus.INFO, "点击车辆估价");
		clickElementByName("车辆估价");
		wait(2);
		checkTitlebar1("车辆估价");
//		reports_MyTest.log(LogStatus.INFO, "检查估价历史页面");
//		clickElementByName("估价历史");//检测估价历史页面UI
//		wait(2);
//		checkTitlebar1("估价历史");
//		if (!CheckViewVisibilty(By.id("tvEmptyMsgTextTop"))) {  //您还没有估价历史
//			clickElementByName("清空");
//			if(CheckViewVisibilty(By.id("tvDialogMessage"))) {  //确定要清空所有的估价记录吗？
//				clickElementById("alertdialog_confirm");//点击确定
//			}
//		}
//		clickElementById("imgBtBack");
//		wait(2);
		reports_MyTest.log(LogStatus.INFO, "检查车辆估价页面的UI");
		clickElementByName("开始估价");
		toastCheck("请选择品牌车系");
		wait(2);
		clickElementById("car_name");//请选择品牌车系
		wait(2);
		clickElementByName("奥迪");
		wait(1);
		clickElementByName("A4L");
		wait(1);
		clickElementByName("2019款 1.4T 自动 35TFSI进取型 国V");
		wait(1);
//		clickElementByName("开始估价");
//		toastCheck("请输入行驶里程");
		findElementByName("请输入行驶里程").sendKeys("25");//输入行驶里程
//		wait(1);
//		clickElementByName("开始估价");
//		toastCheck("请选择出售城市");
//		clickElementById("tv_sell_city");//输入出售城市
//		wait(3);
//		clickElementByName("北京");
//		wait(1);
//		clickElementByName("开始估价");
//		toastCheck("请选择上牌城市");
//		clickElementById("tv_plate_city");//输入上牌城市
//		wait(3);
//		clickElementByName("北京");
//		wait(1);
//		clickElementById("tv_plateTime");//请选择上牌时间
//		wait(1);
//		clickElementByName("完成");
//		wait(1);
		if(CheckViewVisibilty(By.id("ll_sellcity"))&&
				CheckViewVisibilty(By.id("tv_car_license_plate"))&&
				CheckViewVisibilty(By.id("tv_car_license_plate_short"))&&
				CheckViewVisibilty(By.id("rl_car_self_evaluation"))&&
				CheckViewVisibilty(By.id("tv_select_brand"))&&
				CheckViewVisibilty(By.id("ll_vehicle_condition"))&&
				CheckViewVisibilty(By.id("ll_plateTime"))) {    //上牌时间
			reports_MyTest.log(LogStatus.INFO, "车辆估价UI显示正常。");
			System.out.println("车辆估价UI显示正常。");
		}else {
			reports_MyTest.log(LogStatus.INFO, "我的-车辆估价UI显示异常，请检查。");
			GetScreenshot("test_5600_CLGJ");
			failAndMessage("我的-车辆估价UI显示异常，请检查。");
		}
	}
	
	/**
	 * @Name 5600_CNXH
	 * @Catalogue 我的-猜你喜欢
	 * @Grade 高级
	 * @FunctionPoint 我的-猜你喜欢-UI和进入详情页
	 */
	@Test
	public void test_5600_CNXH() {
		reports_MyTest.startTest("test_5600_CNXH");
		gotocate(5);
		wait(3);
		reports_MyTest.log(LogStatus.INFO, "滑动到猜你喜欢");
		sliding("up");
		wait(1);
		if(CheckViewVisibilty(By.id("rootLine"))&&         //整体
				 CheckViewVisibilty(By.id("tvCarWholeName"))&&   //车辆名称
				 CheckViewVisibilty(By.id("tvAge"))&&        //车龄
				 CheckViewVisibilty(By.id("tvMileage"))&&   //公里
				 CheckViewVisibilty(By.id("tvPrice"))) {
			reports_HomePageTest.log(LogStatus.INFO, "猜你喜欢车辆信息显示正常！");
			System.out.println("猜你喜欢车辆信息显示正常！");
		}else {
			reports_HomePageTest.log(LogStatus.ERROR, "猜你喜欢车辆信息显示异常！");
			failAndMessage("猜你喜欢车辆信息显示异常！");
		}
		if(CheckViewVisibilty(By.id("tvCarWholeName"))){
			String carName = findElementById("tvCarWholeName").getText();
			System.out.println(carName);
			reports_HomePageTest.log(LogStatus.INFO, "点击猜你喜欢的某个车辆。");
			clickElementById("rootLine");
			wait(2);
			String carDetailName = findElementById("tvVehicleDetailsCarName").getText();
			System.out.println(carDetailName);
			if(carDetailName.trim().equals(carName.trim())) {  //.trim()是为了去掉字符串首尾的空格
				reports_HomePageTest.log(LogStatus.INFO, "点击猜你喜欢的车辆进入车辆详情页。");
				System.out.println("点击猜你喜欢的车辆进入车辆详情页。");
			}else {
				reports_HomePageTest.log(LogStatus.ERROR, "点击猜你喜欢的车辆，进入车辆详情页异常。");
				failAndMessage("点击猜你喜欢的车辆，进入车辆详情页异常。");
			}
		}	
	}
	
	/**
	 * @Name 5601_CLGJ_result0
	 * @Catalogue 我的-我的工具-车辆估价（线上不能有提交数据，所以将此case注释。）
	 * @Grade 高级
	 * @FunctionPoint 我的-我的工具-车辆估价有结果无在售车辆的页面UI
	 */
//	@Test
//	 public void test_5601_CLGJ_result0(){
//		reports_MyTest.startTest("test_5601_CLGJ_result0");
//		gotocate(5);
//		wait(3);
//		sliding("", "", "", "up", "");
//		reports_MyTest.log(LogStatus.INFO, "车辆估价");
//		clickElementByName("车辆估价");
//		wait(2);
//		checkTitlebar1("车辆估价");
//		clickElementByName("估价历史");//检测估价历史页面UI
//		wait(2);
//		checkTitlebar1("估价历史");
//		if (!CheckViewVisibilty(By.name("您还没有估价历史"))) {
//			clickElementByName("清空");
//			if(CheckViewVisibilty(By.name("确定要清空所有的估价记录吗？"))) {
//				clickElementById("alertdialog_confirm");//点击确定
//			}
//		}
//		reports_MyTest.log(LogStatus.INFO, "现在估价");
//		clickElementById("imgBtBack");//现在估价
//		wait(2);
////		clickElementById("tv_plateTime");//请选择上牌时间
////		wait(1);
////		clickElementByName("完成");
////		wait(1);
//		clickElementById("tv_car_name");//请选择品牌车系
//		wait(2);
//		clickElementByName("大众");
//		wait(1);
//		clickElementByName("Tiguan");
//		wait(1);
//		clickElementByName("2017款 1.4T 自动 280TSI精英型前驱");
//		wait(1);
//		findElementByName("请输入").sendKeys("3");//输入行驶里程   //公里数越大 查询结果就查询不到
//		wait(1);
//		clickElementById("tv_sell_city");//输入出售城市
//		wait(3);
//		clickElementByName("北京");
//		wait(1);
//		clickElementById("tv_plate_city");//输入上牌城市
//		wait(3);
//		clickElementByName("北京");
//		wait(1);
//		clickElementByName("车况等级");
//		wait(1);
//		clickElementByName("A 优秀");
//		wait(1);
//		clickElementByName("开始估价");
//		wait(2);
//		if (CheckViewVisibilty(By.name("主人,您的车真是高大上,我们暂时还没有准备好给您看的信息。"))) {
//			reports_MyTest.log(LogStatus.ERROR, "估价失败！");
//			System.err.println("估价失败！");
//			clickElementByName("知道了");
//		}else {
//			checkTitlebar1("估价结果");
//			if (CheckViewVisibilty(By.name("市场参考估价  "))) {
//				reports_MyTest.log(LogStatus.INFO, "估价成功！");
//				System.out.println("估价成功！");
//			}
//			CheckViewVisibilty(By.id("cgvGraph"));   //价格走势
//			wait(1);
//			sliding("", "", "", "up", "");
//			CheckViewVisibilty(By.id("cgvShortageGraph"));  //紧缺度	
//			String samecars = findElementById("tvSameSelling").getText();
//			System.out.println(samecars);
//			if(samecars.equals("当前有0辆同款在售")) {
//				reports_MyTest.log(LogStatus.INFO, "车辆估价有结果无同款在售车辆的页面UI显示正常！");
//				System.out.println("车辆估价有结果无同款在售车辆的页面UI显示正常！");
//			}else {
//				reports_MyTest.log(LogStatus.INFO, "车辆估价有结果并且有同款在售车辆！");
//				System.out.println("车辆估价有结果有同款在售车辆！");
//			}
//		}					
////			String regEx = "[^0-9]";
////			Pattern p = Pattern.compile(regEx);//提取字符串中的数字
////			Matcher m = p.matcher(samecars);
////			String sellNum = m.replaceAll("").trim();
////			System.out.println(sellNum);
////			int samenumb = Integer.parseInt(sellNum);//将数字字符串格式转换为整型；
////			System.out.println(samenumb);			
//	 }
	 
	/**
	 * @Name 5601_CLGJ_result
	 * @Catalogue 我的-我的工具-车辆估价（线上不能有提交数据，所以将此case注释。）
	 * @Grade 高级
	 * @FunctionPoint 我的-我的工具-车辆估价有结果有在售车辆的页面UI
	 */
//	@Test
//	 public void test_5602_CLGJ_result2(){
//		reports_MyTest.startTest("test_5602_CLGJ_result2");
//		gotocate(5);
//		wait(3);
//		sliding("", "", "", "up", "");
//		reports_MyTest.log(LogStatus.INFO, "车辆估价");
//		clickElementByName("车辆估价");
//		wait(2);
//		checkTitlebar1("车辆估价");
//		clickElementByName("估价历史");//检测估价历史页面UI
//		wait(2);
//		checkTitlebar1("估价历史");
//		if (!CheckViewVisibilty(By.name("您还没有估价历史"))) {
//			clickElementByName("清空");
//			if(CheckViewVisibilty(By.name("确定要清空所有的估价记录吗？"))) {
//				clickElementById("alertdialog_confirm");//点击确定
//			}
//		}
//		reports_MyTest.log(LogStatus.INFO, "现在估价");
//		clickElementById("imgBtBack");//现在估价
//		wait(2);
////		clickElementById("tv_plateTime");//请选择上牌时间
////		wait(1);
////		clickElementByName("完成");
////		wait(1);
//		clickElementById("tv_car_name");//请选择品牌车系
//		wait(2);
//		clickElementByName("奥迪");
//		wait(1);
//		clickElementByName("A6");
//		wait(1);
//		clickElementByName("2006款 1.8 手动");
//		wait(1);
//		findElementByName("请输入").sendKeys("3");//输入行驶里程   //公里数越大 查询结果就查询不到
//		wait(1);
//		clickElementById("tv_sell_city");//输入出售城市
//		wait(3);
//		clickElementByName("北京");
//		wait(1);
//		clickElementById("tv_plate_city");//输入上牌城市
//		wait(3);
//		clickElementByName("北京");
//		wait(1);
//		clickElementByName("车况等级");
//		wait(1);
//		clickElementByName("A 优秀");
//		wait(1);
//		clickElementByName("开始估价");
//		wait(2);
//		if (CheckViewVisibilty(By.name("主人,您的车真是高大上,我们暂时还没有准备好给您看的信息。"))) {
//			reports_MyTest.log(LogStatus.ERROR, "估价失败！");
//			System.err.println("估价失败！");
//			clickElementByName("知道了");
//		}else {
//			checkTitlebar1("估价结果");
//			if (CheckViewVisibilty(By.name("市场参考估价  "))) {
//				reports_MyTest.log(LogStatus.INFO, "估价成功！");
//				System.out.println("估价成功！");
//			}
//			CheckViewVisibilty(By.id("cgvGraph"));   //价格走势
//			wait(1);
//			sliding("", "", "", "up", "");
//			CheckViewVisibilty(By.id("cgvShortageGraph"));  //紧缺度	
//			String samecars = findElementById("tvSameSelling").getText();
//			System.out.println(samecars);
//			if(samecars.equals("当前有0辆同款在售")) {
//				CheckViewVisibilty(By.name("现在就去卖车>>"));
//				reports_MyTest.log(LogStatus.INFO, "车辆估价有结果无同款在售车辆的页面UI显示正常！");
//				System.out.println("车辆估价有结果无同款在售车辆的页面UI显示正常！");
//			}else if (samecars.equals("当前有1辆同款在售") || samecars.equals("当前有2辆同款在售")) {
//				CheckViewVisibilty(By.name("现在就去卖车>>"));
//				reports_MyTest.log(LogStatus.INFO, "车辆估价有结果并且有1或者2辆同款在售车辆的页面UI显示正常！");
//				System.out.println("车辆估价有结果并且有有1或者2辆同款在售车辆的页面UI显示正常！");
//			}else {//大于2辆在售车辆时
//				CheckViewVisibilty(By.id("tvMore"));//1或者2辆车时没有查看更多按钮
//				CheckViewVisibilty(By.name("现在就去卖车>>"));
//				reports_MyTest.log(LogStatus.INFO, "车辆估价有结果并且有2辆以上同款在售车辆的页面UI显示正常！");
//				System.out.println("车辆估价有结果并且有2辆以上同款在售车辆的页面UI显示正常！");
//			}
//		}	
//	 }
	/**
	 * @Name 5600_WYHK_1
	 * @Catalogue 我的-常用工具-我要还款(h5页面需要用debug包执行)
	 * @Grade 高级
	 * @FunctionPoint 我的-常用工具-我要还款-检查title 检查页面元素 姓名是否存在(由于切换webview问题暂时先不检查h5的二级页面)
	 * @author yanxin
	 */
	@Test
	public void test_5600_WYHK_1(){
		reports_MyTest.startTest("test_5600_WYHK_1");
		gotocate(5);
		wait(2);
		reports_MyTest.log(LogStatus.INFO, "点击我的-常用工具");
		clickElementByName("常用工具");
		checkTitlebar1("常用工具");//确定跳转常用工具页面
		reports_MyTest.log(LogStatus.INFO, "点击我要还款");
		clickElementByName("我要还款");
		wait(3);
		checkTitlebar_Webview("我要还款");
		wait(1);
//		try {
//			switchToWebView();
//		} catch (Exception e) {
//			System.out.println("第一次切换到webView失败尝试再次切换");
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
//			clickElementByName("我要还款");
//			switchToWebView();
//		}
//		wait(2);
//		System.out.println(findElementByXpath("/html/body/div[2]/ul/li[1]/label/span").getText());	
//		if (findElementByXpath("/html/body/div[2]/ul/li[1]/label/span").getText().equals("姓名")) {
//			reports_MyTest.log(LogStatus.INFO, "我要还款-姓名显示正常!");
//			System.out.println("我要还款-姓名显示正常!");
//		}else {
//			reports_MyTest.log(LogStatus.INFO, "我要还款-姓名显示不正常!");
//			System.out.println("我要还款页面-姓名显示不正常!请查看原因");
//		}
	}
	/**
	 * @Name 5600_WDYK_1
	 * @Catalogue 我的-常用工具-我的油卡(h5页面需要用debug包执行)
	 * @Grade 高级
	 * @FunctionPoint 我的-常用工具-我的油卡-检查title 文字检查：请输入您领取油卡的手机号 点击查询按钮 提示检查(由于切换webview问题暂时先不检查h5的二级页面)
	 * @author yanxin
	 */
	@Test
	public void test_5600_WDYK_1(){
		reports_MyTest.startTest("test_5600_WDYK_1");
		gotocate(5);
		wait(2);
		reports_MyTest.log(LogStatus.INFO, "点击我的-常用工具");
		clickElementByName("常用工具");
		checkTitlebar1("常用工具");//确定跳转常用工具页面
		reports_MyTest.log(LogStatus.INFO, "点击我的油卡");
		clickElementByName("我的油卡");
		wait(3);
		checkTitlebar_Webview("我的油卡");
		wait(1);
//		try {
//			switchToWebView();
//		} catch (Exception e) {
//			System.out.println("第一次切换到webView失败尝试再次切换");
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
//			clickElementByName("我的油卡");
//			switchToWebView();
//		}
//		wait(2);
//		System.out.println(findElementByXpath("/html/body/div[1]/div/p[1]").getText());	
//		if (findElementByXpath("/html/body/div[1]/div/p[1]").getText().equals("请输入您领取油卡的手机号")) {
//			reports_MyTest.log(LogStatus.INFO, "我的油卡-请输入您领取油卡的手机号显示正常!");
//			System.out.println("我的油卡-请输入您领取油卡的手机号显示正常!");
//		}else {
//			reports_MyTest.log(LogStatus.INFO, "我的油卡-请输入您领取油卡的手机号显示不正常!");
//			System.out.println("我的油卡-请输入您领取油卡的手机号显示不正常!");
//		}
//		if (findElementByXpath("/html/body/div[1]/div/a").getText().equals("确定")) {
//			reports_MyTest.log(LogStatus.INFO, "我的油卡-确定按钮显示正常!");
//			System.out.println("我的油卡-确定按钮显示正常!");
//			findElementByXpath("/html/body/div[1]/div/a").click();
//			wait(1);
//		}else {
//			reports_MyTest.log(LogStatus.INFO, "我的油卡-确定按钮显示不正常!");
//			System.out.println("我的油卡-确定按钮显示不正常!");
//		}
	}
	
//	/**
//	 * @Name 5901_YXYC_1
//	 * @Catalogue 我的-优信养车-优信养车二级页面(h5页面需要用debug包执行)
//	 * @Grade 高级
//	 * @FunctionPoint 我的-优信养车-优信养车二级页面-检查title 文字检查 查询按钮 提示检查(由于切换webview问题暂时先不检查h5的二级页面)
//	 * @author yanxin
//	 */
//	@Test
//	public void test_5901_YXYC_1(){
//		reports_MyTest.startTest("test_5901_YXYC_1");
//		gotocate(5);
//		wait(2);
//		reports_MyTest.log(LogStatus.INFO, "点击我的-优信养车");
//		clickElementByName("优信养车");
//		wait(1);
//		checkTitlebar_Webview("爱车保养");//确定跳转常用工具页面	
//		wait(1);
//		try {
//			switchToWebView();
//		} catch (Exception e) {
//			System.out.println("第一次切换到webView失败尝试再次切换");
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
//			clickElementByName("优信养车");
//			switchToWebView();
//		}
//		wait(2);
//		System.out.println(findElementByXpath("/html/body/div[1]/div[5]/div/a").getText());	
//		if (findElementByXpath("/html/body/div[1]/div[5]/div/a").getText().equals("现在购买")) {
//			reports_MyTest.log(LogStatus.INFO, "我的-优信养车H5页面-现在购买按钮显示正常!");
//			System.out.println("我的-优信养车H5页面-现在购买显示正常!");
//		}else {
//			reports_MyTest.log(LogStatus.INFO, "我的-优信养车H5页面-现在购买显示按钮不正常!");
//			System.out.println("我的-优信养车H5页面-现在购买显示不正常!");
//		}
//		if (findElementByXpath("/html/body/div[1]/div[1]/div[1]").getText().equals("我的爱车")) {
//			reports_MyTest.log(LogStatus.INFO, "我的-优信养车H5页面 文案：我的爱车 显示正常!");
//			System.out.println("我的-优信养车H5页面 文案：我的爱车 显示正常!");
//			wait(1);
//		}else {
//			reports_MyTest.log(LogStatus.INFO, "我的-优信养车H5页面 文案：我的爱车 显示不正常!");
//			System.out.println("我的-优信养车H5页面 文案：我的爱车 显示不正常!");
//		}
//	}
	
	/**
	 * @Name 5902_DLJC
	 * @Catalogue 我的-用户登录
	 * @Grade 高级
	 * @FunctionPoint 我的-用户登录页面 UI检测 密码登录跳转、用户注册协议文案检测
	 * @author yanxin
	 */
	@Test
	public void test_5902_DLJC(){
		reports_MyTest.startTest("test_5902_DLJC");
		gotocate(5);
		reports_MyTest.log(LogStatus.INFO, "登录判断，已登录退出登录");
		loginOut();
		wait(1);
		clickElementById("imgTouXiang");
		CheckViewVisibiltyByName("登录优信二手车");
		clickElementByName("密码登录");
		wait(1);
		CheckViewVisibiltyByName("短信验证码登录");
		CheckViewVisibiltyByName("忘记密码");
		clickElementByName("短信验证码登录");
		CheckViewVisibiltyByName("获取验证码");
		wait(2);
		if (findElementById("smslogin_useragreement").getText().equals("登录即视为已同意《用户注册协议》")) {
				clickElementById("smslogin_useragreement");
				wait(2);
				CheckViewVisibiltyByName("协议内容");
				System.out.println("检测成功");
			}else {
				failAndMessage("用户注册协议文案错误，请手动查看");
			}
	}
	/**
	 * @Name 5903_WJMM
	 * @Catalogue 我的-用户登录-忘记密码
	 * @Grade 高级
	 * @FunctionPoint 我的-用户登录-忘记密码-ui检测
	 * @author yanxin
	 */
	@Test
	public void test_5903_WJMM(){
		reports_MyTest.startTest("test5903_WJMM");
		gotocate(5);
		reports_MyTest.log(LogStatus.INFO, "登录判断，已登录退出登录");
		loginOut();
		clickElementById("imgTouXiang");
		CheckViewVisibiltyByName("登录优信二手车");
		clickElementByName("密码登录");
		wait(1);
		CheckViewVisibiltyByName("短信验证码登录");
		clickElementByName("忘记密码");
		CheckViewVisibiltyByName("设置密码");
		findElementById("setpwd_phone").sendKeys("14725846915");
		clickElementByName("获取验证码");
  	  	wait(1);
  	  	clickElementById("setpwd_getverify");
  	  	wait(1);
  		findElementById("setpwd_verifycode").sendKeys("666666");
  		clickElementById("setpwd_newpwd");
	  	for(int i = 0; i<6; i++){
       	  	driver.pressKeyCode(AndroidKeyCode.KEYCODE_6);
       	  	System.out.println("input 6");
       	  	}
  		reports_MyTest.log(LogStatus.INFO, "点击确定按钮");
  	  	clickElementById("setpwd_ok");
	}
	/**
	 * @Name 5904_SZMM
	 * @Catalogue 我的-用户登录-设置密码
	 * @Grade 高级
	 * @FunctionPoint 我的-用户登录-设置密码-ui检测
	 * @author yanxin
	 */
	@Test
	public void test_5904_SZMM(){
		reports_MyTest.startTest("test_5904_SZMM");
		gotocate(5);
		reports_MyTest.log(LogStatus.INFO, "登录判断，未登录账号登录");
		login();
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*4/5, width/2, height*3/5, 1000);
		wait(2);
        clickElementById("vgSetting");		    
		sleep(2000);
		checkTitlebar1("设置");
		clickElementByName("设置密码");
		sleep(500);
		CheckViewVisibiltyByName("设置密码");
		clickElementByName("获取验证码");
		findElementById("setpwd_verifycode").sendKeys("666666");
		sleep(500);
		clickElementById("setpwd_newpwd");
	  	for(int i = 0; i<6; i++){
       	  	driver.pressKeyCode(AndroidKeyCode.KEYCODE_6);
       	  	System.out.println("input 6");
       	  	}
  		reports_MyTest.log(LogStatus.INFO, "点击确定按钮");
  	  	clickElementById("setpwd_ok");
	}
	/**
	 * @Name 5700_GUANZHU_LDJC
	 * @Catalogue 我的-我的关注
	 * @Grade 高级
	 * @FunctionPoint 我的-我的关注，右侧雷达监测和在线关注检测，以及咨询模块检测
	 */
	@Test
	public void test_5700_GUANZHU_LDJC() {
		reports_MyTest.startTest("test_5700_GUANZHU_LDJC");
		gotocate(5);
		login();
		wait(2);
		reports_MyTest.log(LogStatus.INFO, "获取我的关注的个数");
		String str1 = findElementById("tvFavorateCarNum").getText();	 	
		if(str1.equals("0")) {
			System.out.println("我的关注为空,获取关注数为0");
			reports_MyTest.log(LogStatus.INFO, "我的关注为空时，去车市页的详情页点击关注，加入关注清单。");
			gotoCateSet(1);
			wait(3);
			toDetail(1);
			wait(1);
			clickElementById("rlFocus");
			driver.pressKeyCode(AndroidKeyCode.BACK);
			gotoCateSet(4);			
		}
		reports_MyTest.log(LogStatus.INFO, "我的关注已有车辆时，点击我的关注，检查页面显示。");
		clickElementById("tvMyCollect");
		wait(4);
		if(	CheckViewVisibilty(By.id("radar_view"))&&//雷达图片
			CheckViewVisibilty(By.id("tv_consultation"))) {//在线咨询
			reports_MyTest.log(LogStatus.INFO, "我的关注右侧雷达图片和在线咨询，页面显示正常。");
			System.out.println("我的关注有车辆时，页面显示正常。");
			reports_MyTest.log(LogStatus.INFO, "点击在线咨询");
			clickElementById("tv_consultation");
			CheckViewVisibiltyByName("优信二手车");
			backBTN();
			clickElementByName("咨询");
			CheckViewVisibilty(By.id("tvCarWholeName"));
			CheckViewVisibilty(By.id("radar_view"));
			CheckViewVisibilty(By.id("tv_consultation"));
			System.out.println("检测咨询页面UI元素通过");
			clickElementByName("全部");
			CheckViewVisibilty(By.id("tvCarWholeName"));
			CheckViewVisibilty(By.id("radar_view"));
			CheckViewVisibilty(By.id("tv_consultation"));
			System.out.println("检测全部页面UI元素通过");
		}else {
			reports_MyTest.log(LogStatus.ERROR, "我的关注右侧雷达图片和在线咨询，页面显示异常，请查看。");
			GetScreenshot("test_5700_GUANZHU_LDJC");
			failAndMessage("我的关注有车辆时，我的关注右侧雷达图片和在线咨询 页面显示异常，请查看。");
		}
	}
	/**
	 * @Name 5500_WDYHQ_1
	 * @Catalogue 我的-我的优惠券
	 * @Grade 高级
	 * @FunctionPoint 我的-我的优惠券文案检查 以及跳转h5页面 title检查
	 */
	@Test
	 public void test_5500_WDYHQ_1(){
		 reports_MyTest.startTest("test_5500_WDYHQ_1");
		 gotocate(5);
		 login();
		 reports_MyTest.log(LogStatus.INFO, "点击我的优惠券");
		 clickElementByName("我的优惠券");
		 wait(2);
		 checkTitlebar1("我的优惠券");
		 wait(2);
	}
	/**
	 * @Name 5501_HYTH_1
	 * @Catalogue 我的-会员特惠
	 * @Grade 高级
	 * @FunctionPoint 我的-会员特惠文案检查 以及跳转h5页面 title检查
	 */
	@Test
	 public void test_5501_HYTH_1(){
		 reports_MyTest.startTest("test_5501_HYTH_1");
		 gotocate(5);
		 login();
		 reports_MyTest.log(LogStatus.INFO, "点击会员特惠");
		 clickElementByName("会员特惠");
		 wait(2);
		 checkTitlebar1("会员特惠");
		 wait(2);
	}
	
	
}
