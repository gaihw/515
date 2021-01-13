package com.uxin.usedcar.testCase;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.ios.IOSElement;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By.ById;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;
import com.uxin.usedcar.test.libs.BaseTest;
import com.uxin.usedcar.test.libs.StringUtils;



@SuppressWarnings("unused")
public class ValuableBookTest extends BaseTest {
	 @BeforeClass
	  public static void first() throws Exception {
		 reports_ValuableBookTest.init("./report/ValuableBook/reportValuableBook.html",true);
		
		 
	  }

	@AfterClass 
	public static void last() throws Exception {
		reports_ValuableBookTest.endTest();
		System.out.println("tearDown");
	}
	
	/**
	 * @Name 4200_BDSY
	 * @Catalogue 宝典-一区UI显示（顶部到百科）
	 * @Grade 高级
	 * @FunctionPoint 宝典tab首页-一区UI显示（顶部到百科）跳转是否正常，检查首页加载完整
	 **/
	@Test
	public void test_4200_BDSY() {
		reports_ValuableBookTest.startTest("test_4200_BDSY");
		gotocate(4);
		wait(2);
		if(!CheckViewVisibilty(By.id("vgSearch"))) {
			reports_ValuableBookTest.log(LogStatus.ERROR, "宝典首页-顶部没有搜索框！");
			failAndMessage("宝典首页-顶部没有搜索框，请人工检查！");
		}
		if(!CheckViewVisibilty(By.id("ad_carousel"))) {
			reports_ValuableBookTest.log(LogStatus.ERROR, "宝典首页-顶部没有banner！");
			System.err.println("宝典首页-顶部没有banner，请人工检查！");
		}		
		reports_ValuableBookTest.log(LogStatus.INFO, "点击问答，进入问答列表。");
		clickElementByName("问答");//问答
		wait(1);
		checkTitlebar1("问答");
		driver.pressKeyCode(AndroidKeyCode.BACK); 
		reports_ValuableBookTest.log(LogStatus.INFO, "点击资讯，进入资讯列表。");
		clickElementByName("资讯");//资讯
		wait(1);
		checkTitlebar1("资讯");
		driver.pressKeyCode(AndroidKeyCode.BACK); 
		reports_ValuableBookTest.log(LogStatus.INFO, "点击百科，进入百科列表。");
		clickElementByName("百科");//百科
		wait(1);
		checkTitlebar_Webview("百科");
		driver.pressKeyCode(AndroidKeyCode.BACK); 
		reports_ValuableBookTest.log(LogStatus.INFO, "点击精选百科，进入百科列表。");
		CheckViewVisibilty(By.id("tvConfigShine"));//精选百科
		clickElementByName("更多百科");
		wait(1);
		checkTitlebar_Webview("百科");
		driver.pressKeyCode(AndroidKeyCode.BACK); 
		if(CheckViewVisibilty(By.id("ivBibleBaikeItem"))&&
				CheckViewVisibilty(By.id("tvBibleBaikeItem"))&&
				CheckViewVisibilty(By.id("tvBibleBaikeItemTag"))) {
			reports_ValuableBookTest.log(LogStatus.INFO, "宝典首页-精选百科显示正常！");
			System.out.println("宝典首页-精选百科显示正常！");
		}else {
			failAndMessage("宝典首页-精选百科显示异常,请手动查看");
		}	
	}
	
	/**
	 * @Name 4202_BDSY
	 * @Catalogue 宝典-二区UI显示（百科底部到问答列表）
	 * @Grade 高级
	 * @FunctionPoint 宝典tab首页-二区UI显示（百科底部到问答列表）检查UI显示
	 **/
	@Test
	public void test_4201_BDSY() {
		reports_ValuableBookTest.startTest("test_4201_BDSY");
		gotocate(4);
		wait(2);	
//		int width = driver.manage().window().getSize().width;    //测试版本没有banner，不用滑动
//		int height = driver.manage().window().getSize().height;
//		driver.swipe(width/2, height*3/4, width/2, height/4, 1000);
//		wait(1);
		reports_ValuableBookTest.log(LogStatus.INFO, "点击更多资讯，进入资讯列表。");
		CheckViewVisibilty(By.id("rlBaikeInfoTitle"));//热门资讯
		clickElementByName("更多资讯");
		wait(1);
		checkTitlebar1("资讯");
		driver.pressKeyCode(AndroidKeyCode.BACK); 
		reports_ValuableBookTest.log(LogStatus.INFO, "检查宝典首页底部UI文案。");
		sliding("", "", "", "up", "");
//		CheckViewVisibilty(By.name("车辆问答"));//车辆问答
//		clickElementByName("更多问答");
//		wait(1);
//		checkTitlebar1("问答");
//		driver.pressKeyCode(AndroidKeyCode.BACK); 
		if(CheckViewVisibilty(By.id("tvBibleQuestion"))&&
				CheckViewVisibilty(By.id("tvBibleAnswerName"))&&
				CheckViewVisibilty(By.id("tvBibleAnswer"))&
				CheckViewVisibilty(By.id("tvAnswerNum"))&&
				CheckViewVisibilty(By.id("rlQuestionAnswerCommonBottom"))) {
			reports_ValuableBookTest.log(LogStatus.INFO, "宝典首页-问答显示正常！");
			System.out.println("宝典首页-问答显示正常！");
		}else {
			failAndMessage("宝典问答UI元素检测有问题，请手动查看");
		}
		reports_ValuableBookTest.log(LogStatus.INFO, "点击宝典首页底的提问按钮。");
		clickElementById("llAskQuestion");//提问按钮
		sleep(500);
		checkTitlebar1("我要提问");
		if(CheckViewVisibilty(By.id("ask_tagtv"))&&
		CheckViewVisibilty(By.id("ask_submittv"))) {
			reports_ValuableBookTest.log(LogStatus.INFO, "宝典-提问页面UI显示正常！");
			System.out.println("宝典-提问页面UI显示正常！");
		}else {
			failAndMessage("宝典-提问页面UI显示有问题，请手动查看");
		}
		driver.pressKeyCode(AndroidKeyCode.BACK); 
		reports_ValuableBookTest.log(LogStatus.INFO, "宝典首页-百科底部到问答列表UI显示正常！");
		System.out.println("宝典首页-百科底部到问答列表UI显示正常！");
	}
	/**
	 * @Name test_4201_WDBQ
	 * @Catalogue 宝典-二区UI显示-车辆问答-标签检测
	 * @Grade 高级
	 * @FunctionPoint 宝典tab首页-宝典-二区UI显示-车辆问答第一个问题 如果有3个标签 检测3个标签元素是否存在
	 */
	@Test
	public void test_4201_WDBQ(){
		reports_ValuableBookTest.startTest("test_4201_WDBQ");
		gotocate(4);
		wait(2);
		sliding("", "", "", "up", "");
		List<WebElement>taglist = driver.findElements(By.xpath("//android.widget.FrameLayout[1]"
				+ "/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
				+ "/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]"
				+ "/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[2]"
				+ "/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]"
				+ "/android.widget.ListView[1]/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]"
				+ "/android.widget.RelativeLayout[1]/android.widget.TextView"));
		System.out.println("taglist.size()="+taglist.size());
		if (taglist.size() == 3) {
			System.out.println("问答第一个问题 有3个元素控件可以进行检测");
			if (CheckViewVisibilty(By.id("tvAnswerNum"))&&
					CheckViewVisibilty(By.id("tvQuestionTag1"))&&
					CheckViewVisibilty(By.id("tvQuestionTag2"))) {
				System.out.println("问答三个标签标签元素检查正确");
			}else {
				failAndMessage("问答三个标签元素检测失败，请手动查看");
			}

		}
	}
	
	/**
	 * @Name 4110_search
	 * @Catalogue 宝典-搜索-suggest词
	 * @Grade 高级
	 * @FunctionPoint 宝典tab首页-文字搜索"二手车"，显示出搜索联想后点击第一个联想词,zhen+联想词不超过6个
	 **/
	@Test
	public void test_4110_search() {
		reports_ValuableBookTest.startTest("test_4110_search");
		gotocate(4);
		wait(2);
		reports_ValuableBookTest.log(LogStatus.INFO, "点击搜索框搜索“二手”；");
		clickElementById("vgSearch");
		wait(1);
		findElementById("etSearchText").sendKeys("二手");
		wait(1);
		WebElement parent = findElementById("lvTips");
		List<WebElement> list = parent.findElements(By.id("tvText"));
		System.out.println(list.size());
		if(list.size() > 6) {
			reports_ValuableBookTest.log(LogStatus.ERROR, "宝典首页-搜索框搜索“二手”，联想词大于6！");
			failAndMessage("宝典首页-搜索框搜索“二手”，联想词大于6！");
		}else if(list.size() == 0) {
			reports_ValuableBookTest.log(LogStatus.INFO, "宝典首页-搜索框搜索“二手”，没有联想词！");
			System.out.println("宝典首页-搜索框搜索“二手”，没有联想词！");
		}else {
			clickElementById("tvText");
			wait(5);
			checkTitlebar_Webview("车辆问答");
			reports_ValuableBookTest.log(LogStatus.INFO, "宝典首页-搜索框搜索“二手”，点击第一条联想词，进入问答详情页！");
			System.out.println("宝典首页-搜索框搜索“二手”，点击第一条联想词，进入问答详情页！");
		}
	}
	
	/**
	 * @Name 4120_search
	 * @Catalogue 宝典-搜索
	 * @Grade 高级
	 * @FunctionPoint 宝典tab首页-文字搜索“奥迪”，并点击键盘搜索按钮，检查是否跳转到搜索结果页
	 **/
	@Test
	public void test_4120_search() {
		reports_ValuableBookTest.startTest("test_4120_search");
		gotocate(4);
		wait(2);
		reports_ValuableBookTest.log(LogStatus.INFO, "点击搜索框搜索“奥迪”，回车搜索。");
		clickElementById("vgSearch");
		wait(1);
		findElementById("etSearchText").sendKeys("奥迪");
		wait(2);
		driver.pressKeyCode(AndroidKeyCode.ENTER);
		wait(2);
		checkTitlebar1("搜索结果");
		if(CheckViewVisibilty(By.xpath(".//*[contains(@text," + "奥迪" + ")]"))) {
			clickElementById("tvBibleQuestion");
//			WebElement parent = findElementById("list");
//			List<WebElement> list = parent.findElements(By.id("tvBibleQuestion"));
//			list.get(0).click();
			wait(5);
			checkTitlebar_Webview("车辆问答");
			reports_ValuableBookTest.log(LogStatus.INFO, "宝典首页-搜索框搜索“奥迪”，点击回车后的第一条搜索结果，进入问答详情页！");
			System.out.println("宝典首页-搜索框搜索“奥迪”，点击回车后的第一条搜索结果，进入问答详情页！");
		}else {
			reports_ValuableBookTest.log(LogStatus.ERROR, "宝典首页-搜索框搜索“奥迪”，结果列表中没有“奥迪”关键字！");
			failAndMessage("宝典首页-搜索框搜索“奥迪”，结果列表中没有“奥迪”关键字！");
		}
	}
	
	/**
	 * @Name 4130_search
	 * @Catalogue 宝典-搜索
	 * @Grade 高级
	 * @FunctionPoint 宝典tab首页-点击热门搜索第一位，进入问题详情页校验详情页展示
	 **/
	@Test
	public void test_4130_search() {
		reports_ValuableBookTest.startTest("test_4130_search");
		gotocate(4);
		wait(2);
		reports_ValuableBookTest.log(LogStatus.INFO, "点击热门搜索第一位；");
		clickElementById("vgSearch");
		wait(1);
		String str = findElementById("tvSearchHistory").getText();
		WebElement parent = findElementById("flowLayoutHotTip");
		List<WebElement> list = parent.findElements(By.id("tvSearchHistory"));
		list.get(0).click();
		wait(3);
//		if(CheckViewVisibilty(By.xpath(".//*[contains(@text," + str + ")]"))) {
			checkTitlebar1("搜索结果");
			clickElementById("tvBibleQuestion");
			wait(5);
			checkTitlebar_Webview("车辆问答");
			reports_ValuableBookTest.log(LogStatus.INFO, "宝典首页-点击热门搜索第一位，点击第一条搜索结果，进入问答详情页！");
			System.out.println("宝典首页-点击热门搜索第一位，点击第一条搜索结果，进入问答详情页！");
//		}else {
//			reports_ValuableBookTest.log(LogStatus.ERROR, "宝典首页-点击热门搜索第一位，跳转的搜索结果页中不包含"+str);
//			failAndMessage("宝典首页-点击热门搜索第一位，跳转的搜索结果页中不包含"+str);
//		}
	}
	
	/**
	 * @Name 4140_search
	 * @Catalogue 宝典-搜索
	 * @Grade 高级
	 * @FunctionPoint 宝典tab首页-文字搜索“&&：wap。”，并点击键盘搜索按钮，检查是否跳转到无数据界面
	 **/
	@Test
	public void test_4140_search() {
		reports_ValuableBookTest.startTest("test_4140_search");
		gotocate(4);
		wait(2);
		reports_ValuableBookTest.log(LogStatus.INFO, "点击搜索框，输入“&&：wap。”，回车搜索。");
		clickElementById("vgSearch");
		wait(1);
		findElementById("etSearchText").sendKeys("&&：wap。");
		wait(2);
		driver.pressKeyCode(AndroidKeyCode.ENTER);
		GetScreenshot("baodian1234");
		wait(4);
		checkTitlebar1("搜索结果");
		if(CheckViewVisibilty(By.id("tvDirectStatusNull"))) {
			reports_ValuableBookTest.log(LogStatus.INFO, "宝典首页-搜索框搜索“&&：wap。”，跳转到无数据界面!");
			System.out.println("宝典首页-搜索框搜索“&&：wap。”，跳转到无数据界面!");
		}
		else {
			reports_ValuableBookTest.log(LogStatus.ERROR, "宝典首页-搜索框搜索“&&：wap。”，结果列表有数据!");
			failAndMessage("宝典首页-搜索框搜索“&&：wap。”，结果列表有数据!");
		}
	}
	
	/**
	 * @Name 4200_QAlist
	 * @Catalogue 宝典-问答列表
	 * @Grade 高级
	 * @FunctionPoint 宝典-问答列表，检查问答列表界面显示
	 **/
	@Test
	public void test_4200_QAlist() {
		reports_ValuableBookTest.startTest("test_4200_QAlist");
		gotocate(4);
		wait(2);
		reports_ValuableBookTest.log(LogStatus.INFO, "点击问答进入问答列表。");
		clickElementByName("问答");//问答
		wait(3);
		checkTitlebar1("问答");
		if(CheckViewVisibilty(By.id("tvIndicator"))) {
			findElementByName("热门").click();
			if(CheckViewVisibilty(By.id("tvBibleQuestion"))&&
					CheckViewVisibilty(By.id("tvBibleAnswerName"))&&
					CheckViewVisibilty(By.id("tvBibleAnswer"))&&
					CheckViewVisibilty(By.id("tvAnswerNum"))&&
					CheckViewVisibilty(By.id("rlQuestionAnswerCommonBottom"))) {
				reports_ValuableBookTest.log(LogStatus.INFO, "宝典-问答列表-热门问答UI显示正常！");
				System.out.println("宝典-问答列表-热门问答UI显示正常！");
			}
			findElementByName("最新").click();
			if(CheckViewVisibilty(By.id("tvBibleQuestion"))&&
					CheckViewVisibilty(By.id("tvBibleAnswerName"))&&
					CheckViewVisibilty(By.id("tvBibleAnswer"))&&
					CheckViewVisibilty(By.id("tvAnswerNum"))&&
					CheckViewVisibilty(By.id("rlQuestionAnswerCommonBottom"))) {
				reports_ValuableBookTest.log(LogStatus.INFO, "宝典-问答列表-最新问答UI显示正常！");
				System.out.println("宝典-问答列表-最新问答UI显示正常！");
			}
		}
		reports_ValuableBookTest.log(LogStatus.INFO, "点击问答列表的提问按钮。");
		clickElementById("llAskQuestion");//提问按钮
		wait(1);
		checkTitlebar1("我要提问");
		if(CheckViewVisibilty(By.id("ask_tagtv"))&&
		CheckViewVisibilty(By.id("ask_submittv"))) {
			reports_ValuableBookTest.log(LogStatus.INFO, "宝典-问答列表-点击提问按钮，页面UI显示正常！");
			System.out.println("宝典-问答列表-点击提问按钮，页面UI显示正常！");
		}
		driver.pressKeyCode(AndroidKeyCode.BACK);
		wait(1);
		reports_ValuableBookTest.log(LogStatus.INFO, "点击问答列表右上角的搜索按钮，搜索“奥迪”，回车搜索。");
		clickElementById("ivSearch");
		wait(1);
		findElementById("etSearchText").sendKeys("奥迪");
		wait(2);
		driver.pressKeyCode(AndroidKeyCode.ENTER);
		wait(2);
		checkTitlebar1("搜索结果");
		reports_ValuableBookTest.log(LogStatus.INFO, "宝典-问答列表-搜索按钮功能正常！");
		System.out.println("宝典-问答列表-点击提问按钮，搜索按钮功能正常！");
	}
	
	/**
	 * @Name 4200_QA_XQ
	 * @Catalogue 宝典-问答列表
	 * @Grade 高级
	 * @FunctionPoint 宝典-问答列表，检查问答详情页界面显示
	 **/
	@Test
	public void test_4200_QA_XQ() {
		reports_ValuableBookTest.startTest("test_4200_QA_XQ");
		gotocate(4);
		wait(2);
		reports_ValuableBookTest.log(LogStatus.INFO, "点击问答进入问答列表，进入问答详情页。");
		clickElementByName("问答");//问答
		wait(3);
		checkTitlebar1("问答");
		clickElementById("tvBibleQuestion");
		wait(1);
		reports_ValuableBookTest.log(LogStatus.INFO, "检测问答详情页的标题、关注按钮和转发按钮，以及追问输入框。");
		checkTitlebar_Webview("车辆问答");
		clickElementById("iv_foucs");   //点击收藏按钮
		toastCheck("关注成功");
		wait(3);
		clickElementById("iv_foucs");   //再次点击取消按钮
		toastCheck("取消关注");
		wait(3);
		clickElementById("iv_share");   //点击转发按钮
		if(CheckViewVisibilty(By.id("wechat"))&&
				CheckViewVisibilty(By.id("wechat_circle"))&&
				CheckViewVisibilty(By.id("qq"))&&
				CheckViewVisibilty(By.id("link"))) {
			reports_ValuableBookTest.log(LogStatus.INFO, "宝典-问答详情页的转发按钮功能正常！");
			System.out.println("问答详情页的转发按钮功能正常!");
		}
		clickElementByName("取消");
		clickElementById("bt_pop");//我要追问
		if(CheckViewVisibilty(By.id("bt_ask"))) {
			reports_ValuableBookTest.log(LogStatus.INFO, "宝典-问答详情页的追问功能正常！");
			System.out.println("问答详情页的追问功能正常！");
			driver.pressKeyCode(AndroidKeyCode.BACK);
		}
	}
	
	/**
	 * @Name 4700_ZXlist
	 * @Catalogue 宝典-资讯列表
	 * @Grade 高级
	 * @FunctionPoint 宝典-资讯列表，检查资讯列表界面显示
	 **/
	@Test
	public void test_4700_ZXlist() {
		reports_ValuableBookTest.startTest("test_4700_ZXlist");
		gotocate(4);
		wait(2);
		reports_ValuableBookTest.log(LogStatus.INFO, "点击资讯进入资讯列表");
		clickElementByName("资讯");//资讯
		wait(1);
		checkTitlebar1("资讯");
		if(CheckViewVisibilty(By.id("ivBibleInfoImg"))&&
				CheckViewVisibilty(By.id("tvBibleInfoTitle"))&&
				CheckViewVisibilty(By.id("tvBibleInfoTime"))&&
				CheckViewVisibilty(By.id("tvBibleInfoReadNum"))) {
			reports_ValuableBookTest.log(LogStatus.INFO, "宝典-资讯列表UI显示正常！");
			System.out.println("宝典-资讯列表UI显示正常！");
		}
	}	
		
	/**
	 * @Name 4700_ZXlist
	 * @Catalogue 宝典-资讯列表
	 * @Grade 高级
	 * @FunctionPoint 宝典-资讯列表，检查资讯详情页界面显示
	 **/
	@Test
	public void test_4700_ZX_XQ() {
		reports_ValuableBookTest.startTest("test_4700_ZX_XQ");
		gotocate(4);
		wait(2);
		reports_ValuableBookTest.log(LogStatus.INFO, "点击资讯，进入资讯列表；");
		clickElementByName("资讯");//问答
		wait(3);
		reports_ValuableBookTest.log(LogStatus.INFO, "检查资讯详情页的标题、收藏和转发按钮。");
		checkTitlebar1("资讯");
		clickElementById("ivBibleInfoImg");
		wait(1);
		clickElementById("iv_foucs");   //点击收藏按钮
		toastCheck("收藏成功");
		wait(3);
		clickElementById("iv_foucs");   //再次点击取消按钮
		toastCheck("取消收藏");
		wait(3);
		clickElementById("iv_share");   //点击转发按钮
		if(CheckViewVisibilty(By.id("wechat"))&&
				CheckViewVisibilty(By.id("wechat_circle"))&&
				CheckViewVisibilty(By.id("qq"))&&
				CheckViewVisibilty(By.id("link"))) {
			reports_ValuableBookTest.log(LogStatus.INFO, "宝典-资讯详情页的转发按钮功能正常！");
			System.out.println("资讯详情页的转发按钮功能正常!");
			clickElementByName("取消");
		}
	}
		
	/**
	 * @Name 4800_BKlist
	 * @Catalogue 宝典-百科列表
	 * @Grade 高级
	 * @FunctionPoint 宝典-百科列表，检查百科列表界面显示
	 **/
	@Test
	public void test_4800_BKlist() {
		reports_ValuableBookTest.startTest("test_4800_BKlist");
		gotocate(4);
		wait(2);
		reports_ValuableBookTest.log(LogStatus.INFO, "进入百科列表页。");
		clickElementByName("百科");//百科
		wait(1);
		checkTitlebar_Webview("百科");
		wait(1);
//		try {
//			switchToWebView();
//		} catch (Exception e) {
//			System.out.println("第一次切换失败尝试再次切换");
//			driver.pressKeyCode(AndroidKeyCode.BACK);
//			wait(1);
//			clickElementByName("百科");
//			wait(1);
//			switchToWebView();	
//		}
//		if(findElementByXpath("/html/body/div/div[1]/a[1]").getText().equals("买车流程")&&
//				CheckViewVisibilty(By.xpath("/html/body/div/div[2]/div[2]"))) {
//			reports_ValuableBookTest.log(LogStatus.INFO, "宝典-百科列表显示正常！");
//			System.out.println("宝典-百科列表显示正常!");
//		}		
	}
	
	/**
	 * @Name 4201_BQMC
	 * @Catalogue 宝典-标签-买车
	 * @Grade 高级
	 * @FunctionPoint 宝典-标签-买车，检查功能及跳转后的UI展示
	 **/
	@Test
	public void test_4201_BQMC() {
		reports_ValuableBookTest.startTest("test_4201_BQMC");
		gotocate(4);
		wait(2);
		reports_ValuableBookTest.log(LogStatus.INFO, "进入问答列表，点击买车标签，检查买车列表。");
		clickElementByName("问答");
		wait(2);
		if(CheckViewVisibilty(By.id("tvQuestionTag1"))) {
			clickElementByName("买车");
			wait(1);
			checkTitlebar1("买车");
			if(CheckViewVisibilty(By.id("tvBibleQuestion"))&&
				CheckViewVisibilty(By.id("tvBibleAnswerName"))&&
				CheckViewVisibilty(By.id("tvBibleAnswer"))&&
				CheckViewVisibilty(By.id("tvAnswerNum"))&&
				CheckViewVisibilty(By.id("rlQuestionAnswerCommonBottom"))) {
				reports_ValuableBookTest.log(LogStatus.INFO, "宝典-问答-买车标签列表UI显示正常！");
				System.out.println("宝典-问答-买车标签列表UI显示正常！");
			}		
		}else {
			reports_ValuableBookTest.log(LogStatus.ERROR, "宝典-问答列表，没找到买车标签！");
			System.err.println("宝典-问答列表，没找到买车标签!");
		}
	}
	
	/**
	 * @Name 4200_TW
	 * @Catalogue 宝典-提问
	 * @Grade 高级
	 * @FunctionPoint 宝典-提问，检查我要提问的页面
	 **/
	@Test
	public void test_4200_TW() {
		reports_ValuableBookTest.startTest("test_4200_TW");
		gotocate(4);
		wait(2);
		reports_ValuableBookTest.log(LogStatus.INFO, "点击宝典底部的提问按钮；");
		clickElementById("llAskQuestion");//提问按钮
		sleep(500);
		checkTitlebar1("我要提问");
		reports_ValuableBookTest.log(LogStatus.INFO, "输入“a”1个字符");
		inputById("etMyQuestion", "a");   //小于6个字
		clickElementByName("提交");
		reports_ValuableBookTest.log(LogStatus.INFO, "只输入“a”1个字符，点击提交按钮，检查是否有toast提示。");
		toastCheck("请输入6-240个字");
		if(CheckViewVisibilty(By.id("look_questDetail"))){
			reports_ValuableBookTest.log(LogStatus.ERROR, "只输入1个字符，点击提交按钮，提示提交成功。");
			failAndMessage("只输入1个字符，点击提交按钮，提示提交成功。");
		}
		wait(2);
//		reports_ValuableBookTest.log(LogStatus.INFO, "输入241个字符，检查toast提示。");
//		String str = org.apache.commons.lang3.StringUtils.repeat("a", 241);
//		System.out.println(str);		
//		inputById("etMyQuestion", str);   //大于240个字
//		toastCheck("最多支持240个字");
//		wait(5);
		reports_ValuableBookTest.log(LogStatus.INFO, "点击“车价评估”");
		clickElementByName("车价评估");
		wait(1);
		if(CheckViewVisibilty(By.id("question_tag_tv"))){  //快速了解车辆价格，点此自助估价
			reports_ValuableBookTest.log(LogStatus.INFO, "点击“车价评估”标签弹出的小气泡。");
			clickElementById("question_tag_tv");
			wait(1);
			checkTitlebar1("车辆估价");
		}else {
			reports_ValuableBookTest.log(LogStatus.INFO, "点击“车价评估”标签没有弹出小气泡。");
			failAndMessage("点击“车价评估”标签没有弹出小气泡。");
		}
	}
}
