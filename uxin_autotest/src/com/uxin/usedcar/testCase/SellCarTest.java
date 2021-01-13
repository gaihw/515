package com.uxin.usedcar.testCase;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.ios.IOSElement;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By.ById;
import org.seleniumhq.jetty9.server.Authentication.Failed;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;
import com.uxin.usedcar.test.libs.BaseTest;
import com.uxin.usedcar.test.libs.CaseConfig;

@SuppressWarnings("unused")
public class SellCarTest extends BaseTest {
	 @BeforeClass
	  public static void first() throws Exception {
		 reports_SellCarTest.init("./report/SellCar/reportSellCar.html",true);
	  }

	@AfterClass 
	public static void last() throws Exception {
		reports_SellCarTest.endTest();
		System.out.println("tearDown");
	}
	
	/**
	 * @Name 3000_MC
	 * @Catalogue 卖车-我要卖车
	 * @Grade 高级
	 * @FunctionPoint 卖车tab-我要卖车-卖车界面UI-服务流程 预约卖车文案、价格高里边的 车辆图片和车辆名称元素、底部预约卖车按钮元素
	 **/
	@Test
	public void test_3000_MC() {
		reports_SellCarTest.startTest("test_3000_MC");
		gotocate(3);
		wait(2);
		reports_SellCarTest.log(LogStatus.INFO, "进入卖车页，检查界面UI");
		if(CheckViewVisibilty(By.id("tv_order_sellcar"))&&    //预约卖车
				CheckViewVisibilty(By.id("tv_free_consultation"))&&//免费咨询
				CheckViewVisibilty(By.id("tv_evaluation"))) {   //先估个价
			reports_SellCarTest.log(LogStatus.INFO, "卖车页第一屏界面UI显示正常。");
			System.out.println("卖车页第一屏界面UI显示正常。");
		}
		else {
			reports_SellCarTest.log(LogStatus.ERROR, "卖车页第一屏界面UI显示异常。");
			GetScreenshot("test_3000_MC_1");
			failAndMessage("卖车页第一屏界面UI显示异常。");
		}
//		sliding("", "", "", "up", "");
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*3/4, width/2, height/4, 1000);
		wait(2);
		if(CheckViewVisibilty(By.id("ll_service_flow_item"))) {
			reports_SellCarTest.log(LogStatus.INFO, "卖车页服务流程界面UI显示正常。");
			System.out.println("卖车页服务流程界面UI显示正常。");
		}
		else {
			reports_SellCarTest.log(LogStatus.ERROR, "卖车页服务流程界面UI显示异常。");
			GetScreenshot("test_3000_MC_2");
			failAndMessage("卖车页服务流程界面UI显示异常。");
		}
		driver.swipe(width/2, height*3/4, width/2, height/4, 1000);
		wait(2);
		if(CheckViewVisibilty(By.id("tv_deal_time"))&&//检查近期成交文案
			CheckViewVisibilty(By.id("tv_car_name"))&&//检查车辆名称元素是否存在
			CheckViewVisibilty(By.id("iv_car_icon"))) {//检查车辆图片元素是否存在
			reports_SellCarTest.log(LogStatus.INFO, "卖车页价格相关界面UI显示正常。");
			System.out.println("卖车页价格相关界面UI显示正常。");
		}
		sliding("", "", "", "up", "");
		wait(2);
		sliding("", "", "", "up", "");
		wait(2);
		sliding("", "", "", "up", "");
		wait(2);
		if(CheckViewVisibilty(By.id("tv_order_sellcar_bottom"))) {
			reports_SellCarTest.log(LogStatus.INFO, "卖车页底部界面UI显示正常。");
			System.out.println("卖车页底部界面UI显示正常。");
		}else {
			reports_SellCarTest.log(LogStatus.ERROR, "卖车页第底部界面UI显示异常。");
			GetScreenshot("test_3000_MC_5");
			failAndMessage("卖车页底部界面UI显示异常。");
		}
	}
	
	/**
	 * @Name 3000_MC_MCJL
	 * @Catalogue 卖车-卖车记录
	 * @Grade 高级
	 * @FunctionPoint 卖车tab-卖车记录-检查预约卖车title，检查卖车记录为空的页面元素
	 **/
	@Test
	public void test_3000_MC_MCJL() {
		reports_SellCarTest.startTest("test_3000_MC_MCJL");
		gotocate(3);
		wait(2);
		checkTitlebar_Webview("我要卖车");
		clickElementByName("卖车记录");
		if(CheckViewVisibilty(By.id("tvEmptyMsgTextTop"))&&
				CheckViewVisibilty(By.id("ivEmptyMsgIcon"))) {
			reports_SellCarTest.log(LogStatus.INFO, "您还没有发布的车辆");
			System.out.println("您还没有发布的车辆");
			clickElementById("imgBtBack");
			wait(1);
			checkTitlebar_Webview("我要卖车");
			if(CheckViewVisibilty(By.id("tv_order_sellcar"))) {   //预约卖车
				clickElementById("tv_order_sellcar");
				reports_SellCarTest.log(LogStatus.INFO, "点击预约卖车，进入预约卖车界面。");
				wait(1);
				checkTitlebar1("车辆信息");
				System.out.println("检查预约卖车title成功");
			}
		}else {
			System.out.println("预约卖车有记录数据，请手工查看");
		}
	}
	
	/**
	 * @Name 3000_MC_YYMC
	 * @Catalogue 卖车-预约卖车界面
	 * @Grade 高级
	 * @FunctionPoint 卖车tab-预约卖车界面UI检测-检测提交按钮是否变为可用
	 **/
	@Test
	public void test_3000_MC_YYMC() {
		reports_SellCarTest.startTest("test_3000_MC_YYMC");
		gotocate(3);
		wait(2);
		clickElementByName("预约卖车");
		checkTitlebar1("车辆信息");
		if(CheckViewVisibilty(By.id("ll_sellcity"))&&
				CheckViewVisibilty(By.id("tv_car_license_plate"))&&
				CheckViewVisibilty(By.id("tv_car_license_plate_short"))&&
				CheckViewVisibilty(By.id("rl_car_self_evaluation"))&&
				CheckViewVisibilty(By.id("rl_select_brand"))&&
				CheckViewVisibilty(By.id("ll_vehicle_condition"))&&
				CheckViewVisibilty(By.id("ll_plateTime"))) {
			reports_SellCarTest.log(LogStatus.INFO, "预约卖车页面显示正常。");
			System.out.println("预约卖车页面显示正常。");		
			reports_SellCarTest.log(LogStatus.INFO, "预约卖车页面-选择要卖的车。");
			clickElementById("car_name");
			wait(2);
			clickElementByName("奥迪");
			wait(2);
			clickElementByName("A4L");
			clickElementByName("2019款 1.4T 自动 35TFSI进取型 国V");
			reports_SellCarTest.log(LogStatus.INFO, "预约卖车页面-输入里程。");
			findElementById("et_vehicle_condition").sendKeys("25");
			reports_SellCarTest.log(LogStatus.INFO, "预约卖车页面-选择上牌时间。");
			clickElementById("tv_plateTime");
			wait(1);
			int width = driver.manage().window().getSize().width;
			int height = driver.manage().window().getSize().height;
//			driver.swipe(width/3-20, height*3/8, width/3-20, height*3/8+150, 1000);
			driver.swipe(width*5/17, height*3/8, width*5/17, height*3/8+150, 1000);
			wait(1);
			driver.swipe(width*2/3, height*3/8, width*2/3, height*3/8+100, 1000);
			wait(1);
			clickElementByName("完成");
			clickElementById("tv_car_self_evaluation");
			findElementByXpath("//android.widget.TextView[@text='车况自评']");
			checkTitlebar3("碰撞描述");
			clickElementByName("从未有碰撞");
			sleep(500);
			clickElementByName("从未有损伤");
			sleep(500);
			clickElementByName("有过水泡");
			sleep(1000);
			clickElementById("tv_sure");
			wait(1);
			reports_SellCarTest.log(LogStatus.INFO, "预约卖车页面-检查提交资料按钮是否变为可用。");
			if(findElementById("btn_post").getAttribute("enabled").equals("true")) {
				reports_SellCarTest.log(LogStatus.INFO, "预约卖车页面-输入所有信息之后，提交资料按钮变为可用。");
				System.out.println("预约卖车页面-输入所有信息之后，提交资料按钮变为可用。");
			}
			else {
				reports_SellCarTest.log(LogStatus.INFO, "预约卖车页面-输入所有信息之后，提交资料按钮仍然不可用，请检查。");
				GetScreenshot("test_3000_MC_YYMC_button");
				System.out.println("预约卖车页面-输入所有信息之后，提交资料按钮仍然不可用，请检查。");
			}
		}else {
			reports_SellCarTest.log(LogStatus.INFO, "预约卖车页面显示异常。");
			GetScreenshot("test_3000_MC_YYMC");
			failAndMessage("预约卖车页面显示异常。");
		}
	}
	
	/**
	 * @Name 3000_MC_CLGJ
	 * @Catalogue 卖车-车辆估计
	 * @Grade 高级
	 * @FunctionPoint 卖车tab-车辆估计-界面UI元素检测 检查提交按钮是否变为可用
	 **/
	@Test
	public void test_3000_MC_CLGJ() {
		reports_SellCarTest.startTest("test_3000_MC_CLGJ");
		gotocate(3);
		wait(2);
		clickElementByName("先估个价");
		sleep(500);
		checkTitlebar1("车辆估价");
		if(CheckViewVisibilty(By.id("ll_sellcity"))&&
				CheckViewVisibilty(By.id("tv_car_license_plate"))&&
				CheckViewVisibilty(By.id("tv_car_license_plate_short"))&&
				CheckViewVisibilty(By.id("rl_car_self_evaluation"))&&
				CheckViewVisibilty(By.id("tv_select_brand"))&&
				CheckViewVisibilty(By.id("ll_vehicle_condition"))&&
				CheckViewVisibilty(By.id("ll_plateTime"))) {
			reports_SellCarTest.log(LogStatus.INFO, "车辆估价页面显示正常。");
			System.out.println("车辆估价页面显示正常。");		
			reports_SellCarTest.log(LogStatus.INFO, "车辆估价页面-选择要卖的车。");
			clickElementById("car_name");
			wait(2);
			clickElementByName("奥迪");
			wait(2);
			clickElementByName("A4L");
			clickElementByName("2019款 1.4T 自动 35TFSI进取型 国V");
			reports_SellCarTest.log(LogStatus.INFO, "车辆估价页面-输入里程。");
			findElementById("et_vehicle_condition").sendKeys("25");
			reports_SellCarTest.log(LogStatus.INFO, "车辆估价页面-点击车况自评。");
			clickElementById("tv_car_self_evaluation");
			findElementByXpath("//android.widget.TextView[@text='车况自评']");
			checkTitlebar3("碰撞描述");
			clickElementByName("从未有碰撞");
			sleep(500);
			clickElementByName("从未有损伤");
			sleep(500);
			clickElementByName("有过水泡");
			sleep(1000);
			clickElementById("tv_sure");
			wait(1);
			reports_SellCarTest.log(LogStatus.INFO, "车辆估价页面-检查开始估价按钮是否变为可用。");
			if(findElementById("btn_commit").getAttribute("enabled").equals("true")) {
				reports_SellCarTest.log(LogStatus.INFO, "车辆估价页面-输入所有信息之后，开始估价按钮变为可用。");
				System.out.println("车辆估价页面-输入所有信息之后，开始估价按钮变为可用。");
			}
			else {
				reports_SellCarTest.log(LogStatus.INFO, "车辆估价页面-输入所有信息之后，开始估价按钮仍然不可用，请检查。");
				GetScreenshot("test_3000_MC_YYMC_button");
				System.out.println("车辆估价页面-输入所有信息之后，开始估价按钮仍然不可用，请检查。");
			}
		}else {
			reports_SellCarTest.log(LogStatus.INFO, "车辆估价页面显示异常。");
			GetScreenshot("test_3000_MC_CLGJ");
			failAndMessage("车辆估价页面显示异常。");	
		}
	}
	
	/**
	 * @Name 3000_MC_JQCJ
	 * @Catalogue 卖车-近期成交记录
	 * @Grade 高级
	 * @FunctionPoint 卖车tab-近期成交记录列表页 UI检测
	 **/
	@Test
	public void test_3000_MC_JQCJ() {
		reports_SellCarTest.startTest("test_3000_MC_JQCJ");
		gotocate(3);
		wait(1);
		reports_SellCarTest.log(LogStatus.INFO, "进入我要卖车页，下滑，找到查看更多成交记录按钮。");
//		sliding("", "", "", "up", "");
		int width = driver.manage().window().getSize().width;
		int height =driver.manage().window().getSize().height;
		driver.swipe(width/2, height*9/10, width/2, height*1/10, 1000);
		wait(1);
		if(CheckViewVisibilty(By.id("fm_see_more_business_records"))) {
			clickElementByName("查看更多成交记录");
			wait(3);
			checkTitlebar_Webview("近期成交");
//			try {
//				switchToWebView();
//			} catch (Exception e) {
//				System.out.println("再一次切换webview");
//				driver.pressKeyCode(AndroidKeyCode.BACK);
//				clickElementByName("查看更多成交记录");
//				wait(3);
//				switchToWebView();
//			}
//			wait(2);
//			if(findElementByXpath("/html/body/ul/li[1]/img").getAttribute("class").equals("carImg")&&
//					findElementByXpath("/html/body/ul/li[1]/span[1]").getAttribute("class").equals("title")&&
//					findElementByXpath("/html/body/ul/li[1]/h1").getAttribute("class").equals("head")&&
//					findElementByXpath("/html/body/ul/li[1]/span[2]").getAttribute("class").equals("times")&&
//					findElementByXpath("/html/body/ul/li[1]").getAttribute("class").equals("carLi")&&
//					findElementByXpath("/html/body/ul").getAttribute("class").equals("carList")) {
//				reports_SellCarTest.log(LogStatus.INFO, "近期成交记录车辆页面显示正常。");
//				System.out.println("近期成交记录车辆页面显示正常。");
//			}else {
//				reports_SellCarTest.log(LogStatus.INFO, "近期成交记录车辆页面显示异常。");
//				GetScreenshot("test_3000_MC_JQCJ");
//				failAndMessage("近期成交记录车辆页面显示异常。");
//			}		
		}
		else {
			reports_SellCarTest.log(LogStatus.INFO, "没有找到查看更多成交记录按钮。");
			failAndMessage("没有找到查看更多成交记录按钮,请手动查看");
			System.out.println("没有找到查看更多成交记录按钮。");
		}
	}
	
	
	/**
	 * @Name 3000_MC_CJWT
	 * @Catalogue 卖车-常见问题
	 * @Grade 高级
	 * @FunctionPoint 卖车tab-常见问题文案检查
	 **/
	@Test
	public void test_3000_MC_CJWT() {
		reports_SellCarTest.startTest("test_3000_MC_CJWT");
		gotocate(3);
		wait(2);
		int i = 0;
		while(i < 5) {
			sliding("", "", "", "up", "");
			i = i + 1;
		}
		if(CheckViewVisibilty(By.xpath("//android.widget.TextView[@text='卖车问答']"))) {
			reports_SellCarTest.log(LogStatus.INFO, "卖车页面-卖车问答标题文案显示正常。");
			System.out.println("卖车页面-卖车问答标题文案显示正常。");
			if(CheckViewVisibilty(By.xpath("//android.widget.TextView[@text='优信卖车模式是什么？']"))) {
				clickElementByName("优信卖车模式是什么？");
				if(CheckViewVisibilty(By.id("tv_qa_answer"))) {
					if (findElementById("tv_qa_answer").getText().equals("借助优信全国的网络为车主找寻意向最强的买家，上门验车，即时报价，立刻打款")
					) {
						System.out.println("点击小文案显示正常");
					}else {
						failAndMessage("点击小文案显示异常 请手工查看");
					}
					reports_SellCarTest.log(LogStatus.INFO, "卖车页面-卖车问答-优信的卖车模式是什么？-相关文案显示正常！");
					System.out.println("卖车页面-常见问题-优信的卖车模式是什么？-相关文案显示正常！");
				}else {
					reports_SellCarTest.log(LogStatus.ERROR, "卖车页面-卖车问答-优信的卖车模式是什么？-相关文案显示异常！");
					failAndMessage("卖车页面-常见问题-优信的卖车模式是什么？-相关文案显示异常！");
				}
			}else {
				failAndMessage("优信卖车模式是什么？相关文案显示异常！");
			}
			clickElementByName("优信卖车模式是什么？");
			if(CheckViewVisibilty(By.xpath("//android.widget.TextView[@text='为什么选择在优信卖车？']"))) {
				clickElementByName("为什么选择在优信卖车？");
				if(CheckViewVisibilty(By.id("tv_qa_answer"))) {
					reports_SellCarTest.log(LogStatus.INFO, "卖车页面-卖车问答-为什么选择在优信卖车？相关文案显示正常！");
					System.out.println("卖车页面-卖车问答-为什么选择在优信卖车？相关文案显示正常！");
					System.out.println("yx="+findElementById("tv_qa_answer").getText());
					if (findElementById("tv_qa_answer").getText().equals("1. 价格高：优信全国超过270个城市的买家网络，最高帮您多卖25%"
							+"\n2. 卖的快：最快当天预约，当天成交，当天付款"
							+"\n3. 透明交易：买卖交易面对面，中间环节无干扰")) {
						System.out.println("点击小文案显示正常");
						}else {
							failAndMessage("点击小文案显示异常 请手工查看");
						}
				}else {
					reports_SellCarTest.log(LogStatus.ERROR, "卖车页面-卖车问答-为什么选择在优信卖车？相关文案显示正常！-相关文案显示异常！");
					failAndMessage("卖车页面-卖车问答-为什么选择在优信卖车？相关文案显示正常！-相关文案显示异常！");
				}
			}
			clickElementByName("为什么选择在优信卖车？");
			if(CheckViewVisibilty(By.xpath("//android.widget.TextView[@text='在优信卖车个人信息是否安全？']"))) {
				clickElementByName("在优信卖车个人信息是否安全？");
				if(CheckViewVisibilty(By.id("tv_qa_answer"))) {
					reports_SellCarTest.log(LogStatus.INFO, "卖车页面-卖车问答-在优信卖车个人信息是否安全？-相关文案显示正常！");
					System.out.println("卖车页面-卖车问答-在优信卖车个人信息是否安全？-相关文案显示正常！");
					if (findElementById("tv_qa_answer").getText().equals("优信将对您的个人信息进行全程保密，确保安全信息不泄露")) {
							System.out.println("点击小文案显示正常");
							}else {
								failAndMessage("点击小文案显示异常 请手工查看");
							}
				}else {
					reports_SellCarTest.log(LogStatus.ERROR, "卖车页面-卖车问答-在优信卖车个人信息是否安全？-相关文案显示异常！");
					failAndMessage("卖车页面-卖车问答-在优信卖车个人信息是否安全？-相关文案显示异常！");
				}
			}
			clickElementByName("在优信卖车个人信息是否安全？");
			if(CheckViewVisibilty(By.xpath("//android.widget.TextView[@text='通过哪里可以找到优信卖车？']"))) {
				clickElementByName("通过哪里可以找到优信卖车？");
				if(CheckViewVisibilty(By.id("tv_qa_answer"))) {
					reports_SellCarTest.log(LogStatus.INFO, "卖车页面-卖车问答-通过哪里可以找到优信卖车？-相关文案显示正常！");
					System.out.println("卖车页面-卖车问答-通过哪里可以找到优信卖车？-相关文案显示正常！");
					if (findElementById("tv_qa_answer").getText().equals("登录优信二手车APP或官网www.xin.com预约卖车（支持自主或电话预约服务）")) {
						System.out.println("点击小文案显示正常");
						}else {
							failAndMessage("点击小文案显示异常 请手工查看");
						}
				}else {
					reports_SellCarTest.log(LogStatus.ERROR, "卖车页面-卖车问答-通过哪里可以找到优信卖车？-相关文案显示异常！");
					failAndMessage("卖车页面-卖车问答-通过哪里可以找到优信卖车？-相关文案显示异常！");
				}
			}
		}else {
			failAndMessage("没有找到常见问题文案，请手工查看");
		}
	}
	
	/**
	 * @Name 3000_MC_YYMC_DB
	 * @Catalogue 卖车-底部预约卖车按钮
	 * @Grade 高级
	 * @FunctionPoint 卖车tab-底部预约卖车按钮
	 **/
	@Test
	public void test_3000_MC_YYMC_DB() {
		reports_SellCarTest.startTest("test_3000_MC_YYMC_DB");
		gotocate(3);
		wait(2);
		int i = 0;
		while(i < 5) {
			sliding("", "", "", "up", "");
			i = i + 1;
		}
		clickElementByName("预约卖车");
		wait(1);
		checkTitlebar1("车辆信息");
		if(CheckViewVisibilty(By.id("ll_sellcity"))&&
				CheckViewVisibilty(By.id("tv_car_license_plate"))&&
				CheckViewVisibilty(By.id("tv_car_license_plate_short"))&&
				CheckViewVisibilty(By.id("rl_car_self_evaluation"))&&
				CheckViewVisibilty(By.id("rl_select_brand"))&&
				CheckViewVisibilty(By.id("ll_vehicle_condition"))&&
				CheckViewVisibilty(By.id("ll_plateTime"))) {
			reports_SellCarTest.log(LogStatus.INFO, "车辆信息页面显示正常。");
			System.out.println("车辆信息页面显示正常。");		
			reports_SellCarTest.log(LogStatus.INFO, "车辆信息页面-选择要卖的车。");
			clickElementById("car_name");
			wait(2);
			clickElementByName("奥迪");
			wait(2);
			clickElementByName("A4L");
			clickElementByName("2019款 1.4T 自动 35TFSI进取型 国V");
			reports_SellCarTest.log(LogStatus.INFO, "预约卖车页面-输入里程。");
			findElementById("et_vehicle_condition").sendKeys("25");
			reports_SellCarTest.log(LogStatus.INFO, "预约卖车页面-选择上牌时间。");
			clickElementById("tv_plateTime");
			wait(1);
			int width = driver.manage().window().getSize().width;
			int height = driver.manage().window().getSize().height;
			driver.swipe(width*5/17, height*3/8, width*5/17, height*3/8+150, 1000);
			wait(1);
			driver.swipe(width*2/3, height*3/8, width*2/3, height*3/8+100, 1000);
			wait(1);
			clickElementByName("完成");
			clickElementById("tv_car_self_evaluation");
			findElementByXpath("//android.widget.TextView[@text='车况自评']");
			reports_SellCarTest.log(LogStatus.INFO, "预约卖车页面显示正常。");
			System.out.println("预约卖车页面显示正常。");		
		}else {
			reports_SellCarTest.log(LogStatus.INFO, "预约卖车页面显示异常。");
			GetScreenshot("test_3000_MC_YYMC_DB");
			failAndMessage("预约卖车页面显示异常。");
		}
	}
	/**
	 * @Name 3000_MC_CKZP
	 * @Catalogue 卖车-预约卖车-车况自评
	 * @Grade 高级
	 * @FunctionPoint 卖车-预约卖车-车况自评文案检测
	 **/
	@Test
	public void test_3000_MC_CKZP(){
		reports_SellCarTest.startTest("test_3000_MC_YYMC_DB");
		gotocate(3);
		wait(2);
		clickElementByName("预约卖车");
		checkTitlebar1("车辆信息");
		clickElementById("tv_car_self_evaluation");
		wait(1);
		String[] str1 = {"从未有碰撞","轻微碰撞","碰撞伤及骨架"};
		for(int i=0; i<str1.length;i++){
			sleep(500);
			List<WebElement> list = driver.findElements(By.xpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]"
					+ "/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]"
					+ "/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.HorizontalScrollView[1]"
					+ "/android.widget.LinearLayout[1]/android.widget.LinearLayout"));
			System.out.println("索引返回节点个数--->>  "+list.size());
			System.out.println("索引返回节文案--->>  "+list.get(i).findElement(By.xpath("//android.widget.TextView[1]")).getAttribute("text"));
			String pzms = list.get(i).findElement(By.xpath("//android.widget.TextView[1]")).getAttribute("text");
			if (pzms.equals(str1[i])) {
				System.out.println("碰撞描述下边三个文案匹配成功");
			}else {
				failAndMessage(str1[i]+"没有正常显示");
				reports_BuyCarTest.log(LogStatus.ERROR, pzms+"没有正常显示");
				break;
			}
		}
		String[] str2 = {"从未有损伤","剐蹭/划伤可见","剐蹭/划伤已修复","其他描述","有过水泡","有过火烧"};
		for(int i=6; i<12;i++){
			sleep(500);
			List<WebElement> list = driver.findElements(By.className("android.widget.TextView"));
			System.out.println("索引返回节点个数--->>  "+list.size());
			System.out.println("索引返回节文案--->>  "+list.get(i).getAttribute("text"));
			String pzms1 = list.get(i).getAttribute("text");
			System.out.println(str2[i-6]);
			if (pzms1.equals(str2[i-6])) {
				System.out.println("外观描述下边三个文案匹配成功");
			}else {
				failAndMessage(str2[i-6]+"没有正常显示");
				reports_BuyCarTest.log(LogStatus.ERROR, pzms1+"没有正常显示");
				break;
			}
		}
	}



//	
//	/**
//	 * @Name 3000_MC_TEL
//	 * @Catalogue 卖车-我要卖车-验证是否代入默认手机号（由于需要切换webview，所以要用debug包）
//	 * @Grade 高级
//	 * @FunctionPoint 卖车tab-我要卖车-验证是否代入默认手机号
//	 **/
//	@Test
//	public void test_3000_MC_TEL() {
//		reports_SellCarTest.startTest("test_3000_MC_TEL");
//		String tel_num = CaseConfig.USERNAME_PUBLIC;
//		gotocate(3);
//		wait(5);
//		reports_SellCarTest.log(LogStatus.INFO, "进入卖车页，先检查当前页面是不是我要卖车的页面，排除估计结果页面和预约卖车页面。");
//		if(findElementById("tvTitle").getText().equals("估价结果")) {
//			reports_SellCarTest.log(LogStatus.ERROR, "---当前卖车页面是估价结果页面，请联系后台战败此估价信息！---");
//			failAndMessage("---当前卖车页面是估价结果页面，请联系后台战败此估价信息！---");
//		}else if(findElementById("tvTitle").getText().equals("预约卖车")) {
//			reports_SellCarTest.log(LogStatus.ERROR, "---当前账号的卖车页面是预约卖车页面，请联系后台战败此估价信息！---");
//			failAndMessage("---当前账号的卖车页面是预约卖车页面，请联系后台战败此估价信息！---");
//		}else {
//			checkTitlebar_Webview("我要卖车");
//			gotoCateSet(4);
//			wait(1);
//			reports_SellCarTest.log(LogStatus.INFO, "获取当前登录账号");
//			if(findElementById("tvTips").getText().equals("147****6911")) {
//				tel_num = CaseConfig.USERNAME_BEIYONG1;
//			}else if(findElementById("tvTips").getText().equals("147****6912")) {
//				tel_num = CaseConfig.USERNAME_BEIYONG2;
//			}else if(findElementById("tvTips").getText().equals("147****6914")) {
//				tel_num = CaseConfig.USERNAME_PUBLIC;
//			}
//			reports_SellCarTest.log(LogStatus.INFO, "当前登录账号为："+tel_num);
//			System.out.println("当前登录账号为："+tel_num);
//			gotoCateSet(2);
//			wait(5);
//			try {
//				switchToWebView();
//			} catch (Exception e) {
//				System.out.println("第一次切换失败尝试再次切换");
//				clickElementByName("买车");
//				wait(5);
//				clickElementByName("卖车");
//				wait(3);
//				switchToWebView();	
//			}
//			String number = findElementByXpath("//*[@id=\"buyCars\"]/input").getAttribute("value");
//			reports_SellCarTest.log(LogStatus.INFO, "当前预约卖车区域显示为："+number);
//			System.out.println("当前预约卖车区域显示为："+number);
//			if(number.equals(tel_num)) {
//				reports_SellCarTest.log(LogStatus.INFO, "卖车-预约卖车手机号填写的是默认的手机号！");
//				System.out.println("卖车-预约卖车手机号填写的是默认的手机号！");
//			}else if(number.equals("请输入您的手机号码")) {
//				reports_SellCarTest.log(LogStatus.ERROR, "卖车-预约卖车手机号没有填写手机号！");
//				failAndMessage("卖车-预约卖车手机号没有填写手机号！");
//			}else {
//				reports_SellCarTest.log(LogStatus.ERROR, "卖车-预约卖车手机号填写的不是默认的手机号！");
//				failAndMessage("卖车-预约卖车手机号填写的不是默认的手机号！");
//			}		
//		}
//	}
//
	/**
	 * @Name 3000_MC_CSQH
	 * @Catalogue 卖车-首页城市切换点击卖车按钮 
	 * @Grade 高级
	 * @FunctionPoint 卖车-首页城市切换点击卖车按钮 城市选择白山 卖车预约按钮变成 当前城市暂未开通卖车服务
	 **/
	@Test
	public void test_3000_MC_CSQH() {
		reports_SellCarTest.startTest("test_3000_MC_CSQH");
		gotocate(1);
		reports_SellCarTest.log(LogStatus.INFO, "点击城市切换选择白山");
		try {
			clickElementByName("北京");
			wait(2);
			clickElementByName("白山");
			gotocate(3);
			if (findElementById("tv_sellcar_homepage_city").getText().equals("白山")) {
				System.out.println("城市第一次切换白山成功");
				if (findElementById("tv_order_sellcar").getText().equals("当前城市暂未开通卖车服务")) {
					System.out.println("文案检索成功>>>"+findElementById("tv_order_sellcar").getText());
				}else {
					failAndMessage("当前城市文案检索失败>>>"+findElementById("tv_order_sellcar").getText());
				}
			}else {
				failAndMessage("城市第一次切换失败，手动查找原因");
			}
		} finally {
//			gotocate(1);
//			clickElementById("btChooseCity");
//			clickElementByName("北京");
		}
		
	}
	
	/**
	 * @Name 3000_MC_ZMLC
	 * @Catalogue 卖车-直卖流程
	 * @Grade 高级
	 * @FunctionPoint 卖车-直卖流程-直卖流程文案检测、点击合同范本
	 **/
	@Test
	public void test_3000_MC_ZMLC() {
		reports_SellCarTest.startTest("test_3000_MC_ZMLC");
		gotocate(3);
		wait(2);
		findElementByName("直卖流程");
		wait(1);
		int width = driver.manage().window().getSize().width;
		int height = driver.manage().window().getSize().height;
		driver.swipe(width/2, height*4/5, width/2, height*3/5, 1000);
		wait(1);
		clickElementByName("签约付款");
		Point location=findElementById("tv_service_des").getLocation();
		//获取控件开始位置
		int startX = location.getX();
		int startY = location.getY();
		System.out.println("控件开始X："+startX+"控件开始Y："+startY);
		//获取坐标轴差
		Dimension q = findElementById("tv_service_des").getSize();
		int x = q.getWidth();
		int y = q.getHeight();
		System.out.println("坐标差值X："+x+"坐标差值Y："+y);
		 // 计算出控件开始结束坐标
	     int endX = x + startX;
	     int endY = y + startY;
	     System.out.println("控件结束坐标X："+endX+"控件结束坐标Y："+endY);
	     driver.swipe(endX*1/10, endY-30, endX*9/10, endY-30, 1000);
	     checkTitlebar_Webview("电子合同范本");
	}
}

