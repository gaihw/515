package com.uiPackage.utils.homePage;

import java.util.HashMap;

import com.uiPackage.utils.BaseUtils;
import com.uiPackage.utils.ObtainSms;
import com.uiPackage.utils.ProLoading;

public class HomePage {
	/**
	 * 涨幅榜
	 */
	public static void rankOfIncrease() {
		BaseUtils.id(ProLoading.homePage).click();
		BaseUtils.slideUpAWholeScreen();
		BaseUtils.id(ProLoading.rankOfIncrease).click();
	}
	/**
	 * 
	 * @param buyOrSell 0-买入 1-卖出
	 * @param limitOrMarget 0-限价 1-市价
	 * @param price 价格
	 * @param amount 数量
	 */
	public static void currencyMarket(String phone ,int buyOrSell ,int limitOrMarget,String price ,String amount) {
		BaseUtils.id(ProLoading.homePage).click();
		BaseUtils.xpath(ProLoading.currencyMarket).click();
		BaseUtils.slideUpAWholeScreen();
		BaseUtils.slideDown();
		BaseUtils.sleep(3);
		BaseUtils.xpath(ProLoading.currencyMarket_BTC).click();	
		HashMap<Integer, String> map1 = new HashMap<Integer, String>();
		map1.putIfAbsent(0, "买入");
		map1.putIfAbsent(1, "卖出");
		HashMap<Integer, String> map2 = new HashMap<Integer, String>();
		map2.putIfAbsent(0, "限价");
		map2.putIfAbsent(1, "市价");
		if(buyOrSell==0) {			
			BaseUtils.xpath(ProLoading.currencyMarket_Buy).click();
		}else {
			BaseUtils.xpath(ProLoading.currencyMarket_Sell).click();			
		}
		BaseUtils.id(ProLoading.currencyMarket_BuySell_select).click();
		//限价
		if(limitOrMarget==0) {
			BaseUtils.xpath(ProLoading.currencyMarket_BuySell_limitprice).click();
			BaseUtils.id(ProLoading.currencyMarket_BuySell_price).clear();
			BaseUtils.id(ProLoading.currencyMarket_BuySell_price).sendKeys(price);
		}else {
			BaseUtils.xpath(ProLoading.currencyMarket_BuySell_marketprice).click();
		}
		BaseUtils.id(ProLoading.currencyMarket_BuySell_amount).clear();
		BaseUtils.id(ProLoading.currencyMarket_BuySell_amount).sendKeys(amount);
		//点击确定按钮
		BaseUtils.id(ProLoading.currencyMarket_BuySell_btnOrder).click();
		if(BaseUtils.xpathBoolean(ProLoading.currencyMarket_BuySell_setting)) {			
			//点击设置
			BaseUtils.xpath(ProLoading.currencyMarket_BuySell_setting).click();
			BaseUtils.id(ProLoading.currencyMarket_BuySell_password).clear();
			BaseUtils.id(ProLoading.currencyMarket_BuySell_password).sendKeys("123456");
			BaseUtils.id(ProLoading.currencyMarket_BuySell_PasswordConfirm).clear();
			BaseUtils.id(ProLoading.currencyMarket_BuySell_PasswordConfirm).sendKeys("123456");
			BaseUtils.id(ProLoading.currencyMarket_BuySell_confirm).click();
			BaseUtils.id(ProLoading.currencyMarket_BuySell_sms).click();
			BaseUtils.id(ProLoading.currencyMarket_BuySell_phoneverify).clear();
			String sms = ObtainSms.obtainSms(phone, 0);
			BaseUtils.id(ProLoading.currencyMarket_BuySell_phoneverify).sendKeys(sms);
			BaseUtils.clickPress();
			System.out.println("短信验证码："+sms);
		}
		if(limitOrMarget==0) {
			System.out.println("太子-ETH币种，"+map1.get(buyOrSell)+",价格"+price+",手数"+amount);
		}else {
			System.out.println("太子-ETH币种，"+map1.get(buyOrSell)+",手数"+amount);			
		}
	}
	/**
	 * 
	 * @param xxxUSDT BTCUSDT/EOSUSDT/ETHUSDT/LTCUSDT/XRPUSDT/ETCUSDT/DASHUSDT/BCHUSDT/BSVUSDT
	 */
	public static void swapContract(String xxxUSDT) {
		BaseUtils.id(ProLoading.homePage).click();
		String xxxUSDT_temp = null;
		switch(xxxUSDT) {
			case "BTCUSDT" :
				xxxUSDT_temp = ProLoading.homePage_swap_BTCUSDT;
				break;
			case "EOSUSDT" :
				xxxUSDT_temp = ProLoading.homePage_swap_EOSUSDT;
				break;
			case "ETHUSDT" :
				xxxUSDT_temp = ProLoading.homePage_swap_ETHUSDT;
				break;
			case "LTCUSDT" :
				xxxUSDT_temp = ProLoading.homePage_swap_LTCUSDT;
				break;
			case "XRPUSDT" :
				xxxUSDT_temp = ProLoading.homePage_swap_XRPUSDT;
				break;
			case "ETCUSDT" :
				xxxUSDT_temp = ProLoading.homePage_swap_ETCUSDT;
				break;
			case "DASHUSDT" :
				xxxUSDT_temp = ProLoading.homePage_swap_DASHUSDT;
				break;
			case "BCHUSDT" :
				xxxUSDT_temp = ProLoading.homePage_swap_BCHUSDT;
				break;
			default :
				xxxUSDT_temp = ProLoading.homePage_swap_BSVUSDT;
				break;
		}
		//判断当前模块，是否包含所要点击的币种
		while(!BaseUtils.xpathBoolean(xxxUSDT_temp)) {			
			BaseUtils.id(ProLoading.homePage_swap).click();
		}
		BaseUtils.xpath(xxxUSDT_temp).click();
		System.out.println("选择"+xxxUSDT+"币种");
	}
}
