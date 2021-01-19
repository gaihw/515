package com.uiPackage.utils.currency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.uiPackage.utils.BaseUtils;
import com.uiPackage.utils.DateUtils;
import com.uiPackage.utils.ObtainSms;
import com.uiPackage.utils.ProLoading;
import com.uiPackage.utils.contract.SwitchContract;

public class Currency {
	/**
	 * 未成交委托
	 * @param xxxCurrency btc eth ltc xrp etc bch omg 58b
	 */
	public static void unfinishedEntrustment(String xxxCurrency) {
		BaseUtils.id(ProLoading.currency).click();
		BaseUtils.id(ProLoading.l_hamburger).click();
		BaseUtils.xpath(ProLoading.currency_leftTop_USDT).click();
		String xxxCurrency_temp = swapCurrency(xxxCurrency);
		System.out.println("当前选择的币种是"+xxxCurrency);
		BaseUtils.xpath(xxxCurrency_temp).click();
		BaseUtils.xpath(ProLoading.unfinishedEntrustment).click();
		System.out.println("点击未成交委托...");
		BaseUtils.slideUpAWholeScreen();
//		BaseUtils.slideDown();
		List<String> list = accountPosition();
		for (String string : list) {
			System.out.println(string);
		}
//		if(BaseUtils.xpathBoolean(ProLoading.currency_unfinish_cancel)) {			
//			BaseUtils.xpath(ProLoading.currency_unfinish_cancel).click();
//			BaseUtils.xpath(ProLoading.currency_unfinish_cancel_accept).click();
//		}
	}
	/**
	 * 历史委托
	 * @param xxxUSDT BTC/USDT  
	 */
	public static void historicalEntrustment(String xxxUSDT) {
		BaseUtils.id(ProLoading.currency).click();
		BaseUtils.xpath(ProLoading.historicalEntrustment).click();
		System.out.println("点击历史委托...");
		BaseUtils.slideUpAWholeScreen();
	}
	/**
	 * k线页面-右上角的收藏
	 */
	public static void currencyKline() {
		BaseUtils.id(ProLoading.currency).click();
		BaseUtils.id(ProLoading.currency_kline).click();
		System.out.println("点击币币交易的k线...");
		BaseUtils.id(ProLoading.currency_collect).click();
	}
	
	/**
	 * @param phone 手机号
	 * @param password 密码
	 * @param xxxCurrency 选择币种 btc eth ltc xrp etc bch omg 58b
	 * @param buyOrSell 0-买入 1-卖出
	 * @param limitOrMarget 0-限价 1-市价
	 * @param price 价格
	 * @param amount 数量
	 */
	public static void trade(String phone ,String password,String xxxCurrency,int buyOrSell ,int limitOrMarget,String price ,String amount) {
		BaseUtils.id(ProLoading.currency).click();
		BaseUtils.id(ProLoading.l_hamburger).click();
		BaseUtils.xpath(ProLoading.currency_leftTop_USDT).click();
		String xxxCurrency_temp = swapCurrency(xxxCurrency);
		System.out.println("当前选择的币种是"+xxxCurrency);
		BaseUtils.xpath(xxxCurrency_temp).click();
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
			BaseUtils.id(ProLoading.currencyMarket_BuySell_password).sendKeys(password);
			BaseUtils.id(ProLoading.currencyMarket_BuySell_PasswordConfirm).clear();
			BaseUtils.id(ProLoading.currencyMarket_BuySell_PasswordConfirm).sendKeys(password);
			BaseUtils.id(ProLoading.currencyMarket_BuySell_confirm).click();
			BaseUtils.id(ProLoading.currencyMarket_BuySell_sms).click();
			BaseUtils.id(ProLoading.currencyMarket_BuySell_phoneverify).clear();
			String sms = ObtainSms.obtainSms(phone, 0);
			BaseUtils.id(ProLoading.currencyMarket_BuySell_phoneverify).sendKeys(sms);
			BaseUtils.clickPress();
			System.out.println("短信验证码："+sms);
		}
		if(limitOrMarget==0) {
			System.out.println(xxxCurrency+"币种，"+map1.get(buyOrSell)+",价格"+price+",手数"+amount);
		}else {
			System.out.println(xxxCurrency+"币种，"+map1.get(buyOrSell)+",手数"+amount);			
		}
	}
	public static List<String> accountPosition(){
		List<String> position = new ArrayList<String>();
		int i = 1;
		while((BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout["+i+"]/android.widget.TextView[1]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout["+i+"]/android.widget.TextView[2]"))
				||(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout["+(i+1)+"]/android.widget.TextView[1]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout["+(i+1)+"]/android.widget.TextView[2]"))
				) {
			if(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout["+i+"]/android.widget.TextView[1]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout["+i+"]/android.widget.TextView[2]")) {				
				String buyOrSell_temp = BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout["+i+"]/android.widget.TextView[1]").getText();
				String date_temp = BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout["+i+"]/android.widget.TextView[2]").getText();
//			2019-08-29 20:37:26
				if(!DateUtils.isDate(date_temp, "yyyy-MM-dd HH:mm:ss")) {
					BaseUtils.slideDown();
				}else {				
					String temp = buyOrSell_temp+date_temp;
					if(!position.contains(temp)) {				
						position.add(temp);
					}
					i ++;
				}
			}
			if(!BaseUtils.xpathBoolean(ProLoading.noMoreData)&&i==4) {
				BaseUtils.slideUp();
			}
		}
		return position;
	}
	public static String swapCurrency(String xxxCurrency) {
		switch(xxxCurrency) {
			case "btc":
				return ProLoading.currency_BTC;
			case "eth":
				return ProLoading.currency_ETH;
			case "ltc":
				return ProLoading.currency_LTC;
			case "xrp":
				return ProLoading.currency_XRP;
			case "etc":
				return ProLoading.currency_ETC;
			case "bch":
				return ProLoading.currency_BCH;
			case "omg":
				return ProLoading.currency_OMG;
			case "58b":
				return ProLoading.currency_58B;
			default:				
				return null;
		}
	}
}
