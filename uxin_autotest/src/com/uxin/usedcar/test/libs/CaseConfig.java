package com.uxin.usedcar.test.libs;

import java.io.File;
import com.google.common.base.FinalizablePhantomReference;

@SuppressWarnings("unused")
public class CaseConfig {
	
	//账号信息
		public static final String USERNAME_PUBLIC1 = "14725836911";
		public static final String USERNAME_PUBLIC2 = "14725836912";
		public static final String USERNAME_PUBLIC3 = "14725836913";
		public static final String USERNAME_PUBLIC4 = "14725836914";
		public static final String USERNAME_PUBLIC5 = "14725836915";
		public static final String USERNAME_PUBLIC6 = "14725836916";
		//密码
		public static final String PASSWORD = "666666";
		//liyifeng server
		public static final String SERVICEURL_LYF = "http://127.0.0.1:4723/wd/hub";
		//liyifeng device
		public static final String UDID_LYF = "7319a7c7914d9b73e0cc4cc7173bd0f3b592cbec";
		public static final String UDID_LYW = "1851e96c01d50f6fbc2032a31a4e903181cdc159";
		//liyifeng deviceIosVersion
		public static final String IOS_VERSION="10.3.1";
		//liyiwan device
		public static final String serviceURL_LYW = "http://127.0.0.1:4723/wd/hub";
		public static String sTestScreenshotName="";
		public static final String xcodeConfigFile = "CODE_SIGN_IDENTITY = iPhone Developer DEVELOPMENT_TEAM = 9H4SUA4596";
		/** 跳过欢迎页状态 */
		public static final String SKIPHELLOAPGESTATE = "欢迎页状态";
		/** 跳过欢迎页状态 - 已跳过欢迎页面 */
		public static final String SKIPHELLOPAGESTATE_SKIPED = "已经跳过欢迎页";
		/**登录状态*/
		public static final String LOGIN = "登录状态";
		/** 登陆状态 - 已登陆 */
		public static final String LOGINSTATE = "已登录账号";
		/** 登陆状态 - 未登陆 */
		public static final String LOGINSTATE_UNLOGIN = "未登录";
		/** 启动状态- 首次启动 */
		public static final String START_STATE = "启动状态";
		/** 启动状态- 非首次启动 */
		public static final String START_UNFIRST = "非首次启动";
		
		//步骤执行成功
		public static final String STEPRESULTPASS="PASS";
		public static final String STEPRESULTFAIL="FAIL";
		//每步执行成功与否标志
		public static String STEPRESULT="";
		//执行步骤名称
		public static String STEPNAME="";
		
		public static String ReportPath=System.getProperty("user.dir")+"/report/HomePage/reportHomePage.html";
//		public static String ReportPath=System.getProperty("user.dir")+"/report/HomePage"+reportType+"*.html";
	
	  public final static String CATCH_PICTURE = "./ExceptionPic";
	  public static String sResult="";
	  public static String sTestCase_Data="";
	  public static String sTestCase_CheckData="";
	  public static String TestStepResult="";
	  public static int TestSuitePass=0;
	  public static int TestSuiteFail=0;
	  public static int TestCasePass=0;
	  public static int TestCaseFail=0;		
      public static int TestStepPass=0;
      public static int TestStepFail=0;
	  public static  String SuiteName= "";     //测试套件的名称
	  public static  String Suite_DataIndex= "";  //测试数据索引
	  public static  String Suite_module= "";  //测试模块名称
	  public static  String Suite_CaseName= ""; //测试用例名称
	  public static  String Test_FileName=""; //测试用例文件名称
	  public static String Suite_Loop=""; //循环次数
	  public static  String Plan_Suite= ""; 
	  public static  String Plan_RunModel= ""; 
	  public static int    iTestCase_Step=0;
	  public static int    iTestCase_Type=4;
	  public static int    iTestCase_ObjectName=3;
	  public static int    iTestCase_Object=5;
	  public static int    iTestCase_Operation=6;
	  public static int    iTestCase_Data=7;
	  public static int    iTestCase_CheckData=8;
	  public static int    iTestCase_DriverType=9;
	  public static String    sTestCase_Step="";
	  public static String    sTestCase_Type="";
	  public static String    sTestCase_ObjectName="";
	  public static String    sTestCase_Object="";
	  public static String   sTestCase_Operation="";
	  public static String    sTestCase_DriverType="";
	  public static String  TestResultDesc="";
	  public static String  TestACTIONVALUE="";
	  public static String OldTime;
	  public static String FirstTime="";
	  public static String secondTime="";
	  public static String FirstTime_Str="";
	  public static String secondTime_Str="";
	  
	  
	  public static String Num1;
	  
	  public static String Num2;
	  
	  
	  public static String Num3;
	  
	  public static String Num4;
	  
	  
	  
}
