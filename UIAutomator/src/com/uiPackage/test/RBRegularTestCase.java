package com.uiPackage.test;

import org.databene.benerator.anno.Source;
import org.testng.annotations.Test;

import com.uiPackage.utils.BaseUtils;
import com.uiPackage.utils.contract.RBRegular;
import com.uiPackage.utils.mine.Accounts;
import com.uiPackage.utils.mine.Login;
import com.uiPackage.utils.welcomePage.WelcomePage;

import io.appium.java_client.android.AndroidDriver;

public class RBRegularTestCase extends BaseUtils{
	public static AndroidDriver<?> driver ;
	@Test(dataProvider = "feeder")
	@Source("source/contract/rb/loginpAccountsCurrencyPlaceOrderComissionPosition.csv")
	public void test_exchangeCurrency(String num,String phone,String password,String money,String xxxAccounts, String xxxCurrency,String trans_money,
			String price,String size,String lever,String buyOrSell,String moreOrLess,
			String index,String cancle,
			String p_index,String stopLossClose,String triggerPrice,String limiteOrmarket,String executePrice,String parity
			) {
		long start = BaseUtils.time();
		System.out.println("*************start***********登录->充钱->选择币种->分配资金->下单->止盈->委托取消************start************");
		Login.login(phone,password);
//		Accounts.transferAccounts(xxxAccounts, money);
//		RBRegular.exchangeCurrency(xxxCurrency);
//		RBRegular.fundAllocations(trans_money);
		RBRegular.placeOrder(price, size, lever, Integer.valueOf(buyOrSell), Integer.valueOf(moreOrLess));
		RBRegular.comission(Integer.valueOf(index), Integer.valueOf(cancle));
		RBRegular.position(Integer.valueOf(p_index), Integer.valueOf(stopLossClose), triggerPrice, Integer.valueOf(limiteOrmarket), executePrice, Integer.valueOf(parity));
		long end = BaseUtils.time();
		System.out.println("*************end***********登录->充钱->选择币种->分配资金->下单->止盈->委托取消************end************");
		System.out.println("************************耗时"+(end-start)+"s************************");
	}
}
