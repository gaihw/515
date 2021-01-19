package com.uiPackage.test;

import org.testng.annotations.Test;

import com.uiPackage.utils.BaseUtils;
import com.uiPackage.utils.currency.Currency;
import com.uiPackage.utils.mine.Login;

public class CurrencyTestCase extends BaseUtils{
	//@Test
	public void test_unfinishedEntrustment() {
		Currency.unfinishedEntrustment("btc");
	}
	//@Test
	public void test_kline() {
		Currency.currencyKline();
	}
	@Test
	public void test_trade()  {
		Login.login("13020071928", "12345678");
		Currency.trade("1302007928", "123456", "btc", 1, 0, "20.908", "3");
	}
}

