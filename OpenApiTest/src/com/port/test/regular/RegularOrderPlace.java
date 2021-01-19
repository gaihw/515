package com.port.test.regular;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.testng.annotations.Test;

import com.port.util.AccessToken;
import com.port.util.BaseUtil;
import com.port.util.Config;

import net.sf.json.JSONObject;

public class RegularOrderPlace extends  BaseUtil{
	public static FileWriter file ;
//	int side = 1;//	1:多仓 2:空仓
//	int close = 0;//0:开仓 1:平仓
//	String size = "30";//下单手数
//	int mode 1:止盈单 2:止损单
	
	//@Test//场景1
	public void test_egularOrderPlace_1() throws IOException {
		file = new FileWriter(new File("./source/result.txt"));
		
		int duoying = 0;
		int duosun = 0;
		int kongying = 0;
		int kongsun = 0;
		
		Object [][] obj =  {{"14899990002",1,0,30},{"14899990002",2,duoying,10,1},//开多30，多仓止盈10手
				{"14899990002",2,0,50},{"14899990002",1,0,50},//开空50 开多50
				{"14888880007",1,0,10},{"14899990002",2,duosun,30,2},//对手接盘,多仓止损30手
				{"14899990002",2,0,10},{"14899990002",1,kongying,30,1},//开空10 ，空仓止盈30手
				{"14888880007",1,0,30},{"14888880007",2,0,30},//对手接盘
				{"14899990002",1,0,10},{"14899990002",2,1,30},//开多10 平多30
				{"14899990002",2,0,50},{"14899990002",1,1,10},//开空50 平空60
				{"14899990002",2,0,10},{"14899990002",1,2,30},//开空10 开多30
				{"14899990002",2,1,40},{"14899990002",2,1,10},//平多40 平多10
				{"14899990002",1,kongsun,20,2},{"14888880007",2,0,20}//空仓止损20手，对手接盘
				};
		for (int i = 0; i < obj.length; i++) {
			if(i==1||i==5||i==7||i==18) {
				try {
					Thread.sleep(1000);
					file.write(i+"==="+regularUsdtOrderPlanClose(obj[i][0],obj[i][1],obj[i][2],obj[i][3],obj[i][4])+System.getProperty("line.separator"));
					file.flush();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {try {
				Thread.sleep(1000);
				file.write(i+"==="+regularOrderPlace(obj[i][0],obj[i][1],obj[i][2],obj[i][3])+System.getProperty("line.separator"));
				file.flush();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
		}
		file.close();
	}
	
	//@Test//场景2
	public void test_egularOrderPlace_3() throws IOException {
		file = new FileWriter(new File("./source/result.txt"));
		Object [][] obj =  {{"14899990004",1,0,100},{"14899990004",2,1,10},
				{"14899990004",2,1,10},{"14899990004",2,1,10},
				{"14899990004",2,1,10},{"14899990004",2,1,10},
				{"14899990004",2,1,10},{"14899990004",2,1,10},
				{"14899990004",2,1,10},{"14899990004",2,1,10},
				{"14899990004",2,1,10}
				};
		for (int i = 0; i < obj.length; i++) {
			try {
				Thread.sleep(1000);
				file.write(i+"==="+regularOrderPlace(obj[i][0],obj[i][1],obj[i][2],obj[i][3])+System.getProperty("line.separator"));
				file.flush();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	file.close();
	}
	//@Test//场景3
	public void test_egularOrderPlace_2() throws IOException {
		file = new FileWriter(new File("./source/result.txt"));
		int duoying = 0;
		int kongsun = 0;
		Object [][] obj =  {{"14899990003",1,0,30},{"14899990003",1,0,10},//开多30，开多10
				{"14899990003",2,1,20},{"14899990003",2,1,10},//平多20，平多10
				{"14899990003",2,0,20},{"14899990003",1,1,10},//开空20，平空10
				{"14899990003",1,0,40},{"14899990003",2,duoying,10,1},//开多40，10多仓止盈
				{"14899990003",2,0,20},{"14899990003",2,0,80},//开空20，开空80
				{"14899990003",1,kongsun,60,2},//空仓60止损，
				{"14888880003",1,0,10},{"14888880003",2,0,60}//对手接盘
				};
		for (int i = 0; i < obj.length; i++) {
			if(i==7||i==10) {
				try {
					Thread.sleep(1000);
					file.write(i+"==="+regularUsdtOrderPlanClose(obj[i][0],obj[i][1],obj[i][2],obj[i][3],obj[i][4])+System.getProperty("line.separator"));
					file.flush();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {try {
				Thread.sleep(1000);
				file.write(i+"==="+regularOrderPlace(obj[i][0],obj[i][1],obj[i][2],obj[i][3])+System.getProperty("line.separator"));
				file.flush();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
		}
		file.close();
	}
	//@Test//场景4
	public void test_egularOrderPlace_4() throws IOException {
		file = new FileWriter(new File("./source/result.txt"));
		Object [][] obj =  {{"14899990005",1,0,30},{"14899990005",1,0,30},//开多30，开多30
				{"14899990005",2,1,40},{"14899990005",2,0,30},//平多40，开空30
				{"14899990005",1,1,10},{"14899990005",1,0,40},//平空10，40开多
				{"14899990005",1,1,10},{"14899990005",2,0,80},//平空10，开空80
				{"14899990005",2,1,50},{"14899990005",1,1,40},//开空80，平空40
				{"14899990005",1,11896,20,2},{"14888880007",2,0,20},//20空仓止损，对手接盘
				{"14899990005",1,0,30},//开多30
				};
		for (int i = 0; i < obj.length; i++) {
			if(i==10) {
				try {
					Thread.sleep(1000);
					file.write(i+"==="+regularUsdtOrderPlanClose(obj[i][0],obj[i][1],obj[i][2],obj[i][3],obj[i][4])+System.getProperty("line.separator"));
					file.flush();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {try {
				Thread.sleep(1000);
				file.write(i+"==="+regularOrderPlace(obj[i][0],obj[i][1],obj[i][2],obj[i][3])+System.getProperty("line.separator"));
				file.flush();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	file.close();
	}
	//@Test//场景5
	public void test_egularOrderPlace_5() throws IOException {
		file = new FileWriter(new File("./source/result.txt"));
		Object [][] obj =  {{"14899990000",1,0,30},{"14899990000",1,0,10},//开多30 开多10
				{"14899990000",1,0,20},{"14899990000",1,0,50},//开多20 开多50
				{"14899990000",1,0,5},{"14899990000",1,0,30},//开多5 开多30
				{"14899990000",2,1,20},{"14899990000",2,1,20},//平多20 平多20
				{"14899990000",2,1,15},{"14899990000",2,1,40},//平多15 平多40
				{"14899990000",2,1,15},{"14899990000",2,1,35},//平多15 平多35
				{"14899990000",2,0,50},{"14899990000",2,0,60},//开空50 开空60
				{"14899990000",2,0,10},{"14899990000",1,1,10},//开空10 平空10
				{"14899990000",1,1,10},{"14899990000",1,1,10},//平空10 平空10
				{"14899990000",1,1,20},{"14899990000",1,1,30},//平空20 平空30
				{"14899990000",1,1,40}//平空40
				};
		for (int i = 0; i < obj.length; i++) {
			try {
				Thread.sleep(1000);
				file.write(i+"==="+regularOrderPlace(obj[i][0],obj[i][1],obj[i][2],obj[i][3])+System.getProperty("line.separator"));
				file.flush();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
			}
		}
	file.close();
	}
	//@Test//场景6
	public void test_egularOrderPlace_0() throws IOException {
		file = new FileWriter(new File("./source/result.txt"));
		Object [][] obj =  {{"14899990001",1,0,20},{"14899990001",1,0,40},//开多20，开多40
				{"14899990001",2,1,20},{"14899990001",1,0,10},//平多20，开多10
				{"14899990001",2,0,10},{"14899990001",1,1,5},//开空10，平空5
				{"14899990001",2,1,30},{"14899990001",2,0,20},//平多30，开空20
				{"14899990001",2,0,30},{"14899990001",1,0,10},//开空30，开多10
				{"14899990001",2,1,20},{"14899990001",1,1,40},//平多20，平空40
				{"14899990001",2,0,20},{"14899990001",1,1,10},//开空20，平空10
				{"14899990001",1,1,25}//平空25
				};
		for (int i = 0; i < obj.length; i++) {
			try {
				Thread.sleep(1000);
				file.write(i+"==="+regularOrderPlace(obj[i][0],obj[i][1],obj[i][2],obj[i][3])+System.getProperty("line.separator"));
				file.flush();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		file.close();
	}
	//@Test//场景7
	public void test_egularOrderPlace_6() throws IOException {
		file = new FileWriter(new File("./source/result.txt"));
		Object [][] obj =  {{"14899990006",1,0,30},{"14899990006",1,0,30},//开多30 开多30
				{"14899990006",2,1,40},{"14899990006",2,0,10},//平多40 开空30
				{"14899990006",1,1,10},{"14899990006",1,11755,10,2},{"14888880009",1,0,10},//平空10，10多仓止损，对手接盘
				{"14899990006",1,0,40},{"14899990006",1,1,10},//开多40 平空10
				{"14899990006",2,11755,10,2},{"14888880009",2,0,10},//空仓10手止损，对手接盘
				{"14899990006",1,0,40},{"14899990006",1,1,10},//开多40 平空10
				{"14899990006",2,11755,10,2},{"14888880009",2,0,10},//空仓10手止损，对手接盘
				{"14899990006",2,1,20},{"14899990006",1,11758,10,1},{"14888880009",1,0,10},//平多20，10多仓止盈，对手接盘
				{"14899990006",2,0,30},{"14899990006",1,1,10},//开空30 平多10
				{"14899990006",2,11755,20,2},{"14888880009",2,0,20},//空仓20手止损，对手接盘
				};
		for (int i = 0; i < obj.length; i++) {
			if(i==5||i==9||i==13||i==16||i==20) {
				try {
					Thread.sleep(1000);
					file.write(i+"==="+regularUsdtOrderPlanClose(obj[i][0],obj[i][1],obj[i][2],obj[i][3],obj[i][4])+System.getProperty("line.separator"));
					file.flush();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {try {
				Thread.sleep(1000);
				file.write(i+"==="+regularOrderPlace(obj[i][0],obj[i][1],obj[i][2],obj[i][3])+System.getProperty("line.separator"));
				file.flush();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			
		}
	file.close();
	}
	//@Test//场景8
	public void test_egularOrderPlace_7() throws IOException {
		file = new FileWriter(new File("./source/result.txt"));
		Object [][] obj =  {{"14899990008",1,0,100},{"14899990008",2,0,10},
				{"14899990008",2,0,10},{"14899990008",2,0,10},
				{"14899990008",2,0,10},{"14899990008",2,0,10},
				{"14899990008",2,0,10},{"14899990008",2,0,10},
				{"14899990008",2,0,10},{"14899990008",2,0,10},
				{"14899990008",2,0,10}
				};
		for (int i = 0; i < obj.length; i++) {
			if(i==5||i==8||i==11) {
				try {
					Thread.sleep(1000);
					file.write(i+"==="+regularUsdtOrderPlanClose(obj[i][0],obj[i][1],obj[i][2],obj[i][3],obj[i][4])+System.getProperty("line.separator"));
					file.flush();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				try {
					Thread.sleep(1000);
					if(obj[i][2].equals("1")) {				
						Thread.sleep(700000);
						file.write(i+"==="+regularOrderPlace(obj[i][0],obj[i][1],obj[i][2],obj[i][3])+System.getProperty("line.separator"));
						file.flush();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	file.close();
	}
	//@Test//场景9
	public void test_egularOrderPlace_8() throws IOException {
		file = new FileWriter(new File("./source/result.txt"));
		int duoying = 0;
		int kongsun = 0;
		int kongying = 0;
		Object [][] obj =  {{"14899990007",1,0,30},{"14899990007",1,0,50},//开多30 开多30
				{"14899990007",2,0,20},{"14899990007",1,0,50},//开空20 开多50
				{"14899990007",1,1,20},{"14899990007",1,duoying,5,1},{"14888880009",1,0,5},//平空10，5多仓止损，对手接盘
				{"14899990007",2,1,30},{"14899990007",1,kongsun,5,2},{"14888880009",1,0,5},//平多30，多仓止损，对手接盘
				{"14899990007",1,1,20},{"14899990007",1,kongying,10,1},{"14888880009",2,0,10},//平空20，空仓10手止盈，对手接盘
				{"14899990007",2,1,50},{"14899990007",2,0,30},//平多50 开空30
				{"14899990007",1,1,20},{"14888880007",1,1,10},//平空20 平空20
				};
		for (int i = 0; i < obj.length; i++) {
			if(i==5||i==8||i==11) {
				try {
					Thread.sleep(1000);
					file.write(i+"==="+regularUsdtOrderPlanClose(obj[i][0],obj[i][1],obj[i][2],obj[i][3],obj[i][4])+System.getProperty("line.separator"));
					file.flush();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				try {
					Thread.sleep(1000);
					if(obj[i][2].equals("1")) {				
						Thread.sleep(700000);
						file.write(i+"==="+regularOrderPlace(obj[i][0],obj[i][1],obj[i][2],obj[i][3])+System.getProperty("line.separator"));
						file.flush();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	file.close();
	}
	//止盈止损
	public static String regularUsdtOrderPlanClose(Object user_name,Object side,Object triggerPrice,Object size,Object mode) {
		String access_token_url = Config.ACCESS_TOKEN_TEST_URL;
		String url = Config.REST_CESHI_API+"/usdt/order/plan/close";
		JSONObject access_token_params = new JSONObject();
		access_token_params.put("googleCode", "");
		access_token_params.put("user_name", user_name);
		access_token_params.put("password", "12345678");
		access_token_params.put("ticket", "");
		access_token_params.put("device", "web");
		String access_token = AccessToken.getAccessToken(client,access_token_url, access_token_params);
		JSONObject params = new JSONObject();
		String price = "";
		params.put("contractId", "1001");
		params.put("type", "2");
		params.put("side", side);
		params.put("size", size);
		params.put("price", price);
		params.put("triggerPrice",triggerPrice);
		params.put("mode", mode);
		System.out.println(getPostResponseBodyAndAccess_Tooken(client, url, params, access_token));
		return getPostResponseBodyAndAccess_Tooken(client, url, params, access_token);
	}
	//下单
	public static String regularOrderPlace(Object user_name,Object side,Object close,Object size) {
		String access_token_url = Config.ACCESS_TOKEN_TEST_URL;
		String url = Config.REST_CESHI_API+"/usdt/order/place";
		JSONObject access_token_params = new JSONObject();
		access_token_params.put("googleCode", "");
		access_token_params.put("user_name", user_name);
		access_token_params.put("password", "12345678");
		access_token_params.put("ticket", "");
		access_token_params.put("device", "web");
		String access_token = AccessToken.getAccessToken(client,access_token_url, access_token_params);
		JSONObject params = new JSONObject();
		String price = "";
		params.put("contractId", "1001");
		params.put("type", "2");
		params.put("side", side);
		params.put("leverage", "100");
		params.put("size", size);
		params.put("price", price);
		params.put("close", close);
		params.put("orderFrom", "0");
		params.put("clientOid", "1");
		System.out.println(getPostResponseBodyAndAccess_Tooken(client, url, params, access_token));
		return getPostResponseBodyAndAccess_Tooken(client, url, params, access_token);
	}
}