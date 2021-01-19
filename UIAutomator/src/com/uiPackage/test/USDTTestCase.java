package com.uiPackage.test;

import org.databene.benerator.anno.Source;
import org.testng.annotations.Test;

import com.uiPackage.utils.BaseUtils;
import com.uiPackage.utils.contract.USDTRegular;
import com.uiPackage.utils.mine.Accounts;
import com.uiPackage.utils.mine.Login;
import com.uiPackage.utils.mine.LoginOut;

import io.appium.java_client.android.AndroidDriver;


public class USDTTestCase extends BaseUtils{
	public static AndroidDriver<?> driver ;
	
//	@Test(dataProvider = "feeder")
//	@Source("source/contract/usdt/placeOrderPositionComission.csv")
	public void test_placeOrderPositionComission(String num,String phone,String password,
			String price,String size,String lever,String buyOrSell,String moreOrLess,
			String p_index,String stopLossClose,String triggerPrice,String limiteOrmarket,String executePrice,String parity,
			String index,String cancel) {
		long start = BaseUtils.time();
		System.out.println("*************start***********登录->下单->止损->委托取消->退出登录************start************");
		Login.login(phone,password);
		USDTRegular.placeOrder(price, size, lever,Integer.valueOf(buyOrSell),Integer.valueOf(moreOrLess));
		USDTRegular.position(Integer.valueOf(p_index),Integer.valueOf(stopLossClose),triggerPrice,Integer.valueOf(limiteOrmarket),executePrice,Integer.valueOf(parity));
		USDTRegular.comission(Integer.valueOf(index),Integer.valueOf(cancel));
		LoginOut.logout();
		long end = BaseUtils.time();
		System.out.println("*************end***********登录->下单->止损->委托取消->退出登录************end************");
		System.out.println("************************耗时"+(end-start)+"s************************");
	}
	@Test(dataProvider = "feeder")
	@Source("source/contract/usdt/loginpPlaceOrderPositionStoploss.csv")
	public void test_loginpPlaceOrderPositionStoploss(String num,String phone,String password,String money,String xxxAccounts, String trans_money,
			String price,String size,String lever,String buyOrSell,String moreOrLess,
			String p_index,String stopLossClose,String triggerPrice,String limiteOrmarket,String executePrice,String parity
			) {
		long start = BaseUtils.time();
		System.out.println("*************start***********登录->充钱->转账->下单->平仓->退出登录************start************");
		Login.login(phone,password);
		Accounts.investAccouts(phone, money);
		Accounts.transferAccounts(xxxAccounts,trans_money);
		USDTRegular.placeOrder(price, size, lever,Integer.valueOf(buyOrSell),Integer.valueOf(moreOrLess));
		USDTRegular.position(Integer.valueOf(p_index),Integer.valueOf(stopLossClose),triggerPrice,Integer.valueOf(limiteOrmarket),executePrice,Integer.valueOf(parity));
		LoginOut.logout();
		long end = BaseUtils.time();
		System.out.println("*************end***********登录->充钱->转账->下单->平仓->退出登录************end************");
		System.out.println("************************耗时"+(end-start)+"s************************");
	} 
}
