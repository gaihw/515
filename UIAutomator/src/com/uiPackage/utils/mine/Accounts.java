package com.uiPackage.utils.mine;

import com.uiPackage.config.Config;
import com.uiPackage.utils.BaseUtils;
import com.uiPackage.utils.MysqlDataBase;
import com.uiPackage.utils.ProLoading;

public class Accounts {
	public static String database = Config.DATABASE;;
	public static String user = Config.USER;;
	public static String password = Config.PASSWORD;;
	public static String sql ;
	public static String totalAssetsViewBTC ;
	/**
	 * 往账号中充钱
	 * @param phone
	 * @param money
	 */
	public static void investAccouts(String phone,String money) {
		//已登录状态
		//获取总资产的值,单位BTC
		totalAssetsViewBTC = BaseUtils.id(ProLoading.totalAssetsBTC).getText();
		if(totalAssetsViewBTC.contains("***")) {
			BaseUtils.id(ProLoading.hide).click();
			BaseUtils.sleep(10);
			totalAssetsViewBTC = BaseUtils.id(ProLoading.totalAssetsBTC).getText();
		}
		//对获取的文本，以空格分割，获取前面的数值
		String[] temp = totalAssetsViewBTC.split(" ");
		String[] temp1 = temp[0].split(",");
		String temp2 = "";
		for (int i = 0; i < temp1.length; i++) {
			temp2 += temp1[i];
		}
		double assetsBTC = Double.parseDouble(temp2);
		if(assetsBTC==0) {
			//查询该用户的user_id
			sql = "select id from umc.user_base_info where mobile = "+phone;
			Integer userId = MysqlDataBase.sql(database, user, password, sql);
			//查询该用户在资产表中是否存在
			sql = "select count(*) from 58account.tb_account_info where user_id="+userId;
			int flage = MysqlDataBase.sql(database, user, password, sql);
			if(flage==0) {		
				//如果在资产表中不存在，则插入一条数据，充钱
				sql = "INSERT INTO 58account.tb_account_info ( `user_id`, `currency_id`, `available`, `hold`, `status`, `created_date`, `modify_date`)  VALUES ( "+userId+", 8, "+money+", 0,0, now(), now())";
				MysqlDataBase.sql(database, user, password, sql);
			}else {
				//如果在资产表中存在，则更新
				sql = "update 58account.tb_account_info set `available` = "+money+" where user_id ="+userId;
				MysqlDataBase.sql(database, user, password, sql);
			}
			System.out.println("账号"+phone+"中成功充值了"+money+"BTC");
		}else {
			System.out.println("账号"+phone+"中的资产为"+assetsBTC+"BTC");
		}	
	}
	/**
	 * 
	 * @param xxxAccounts USDT账户  交割账户 币币账户
	 * @param currency
	 */
	public static void transferAccounts(String xxxAccounts,String currency) {
		//已登录页面
		//我的页面，点击转账
		BaseUtils.id(ProLoading.transfer).click();
//		BaseUtils.id(ProLoading.edtSearch).clear();
//		BaseUtils.id(ProLoading.edtSearch).sendKeys("USDT");
		BaseUtils.xpath(ProLoading.currencyName).click();
		BaseUtils.sleep(5);
		BaseUtils.id(ProLoading.tv_transfer_from).click();
		BaseUtils.xpath(ProLoading.rechargeWithdrawal).click();
		BaseUtils.id(ProLoading.tv_transfer_to).click();
		if(xxxAccounts.equals("USDT账户")) {			
			BaseUtils.xpath(ProLoading.USDTAccount).click();
		}else if(xxxAccounts.equals("交割账户")) {
			BaseUtils.xpath(ProLoading.RBAccount).click();			
		}else if(xxxAccounts.equals("币币账户")) {
			BaseUtils.xpath(ProLoading.CurrencyAccount).click();
		}
		BaseUtils.sleep(5);
		BaseUtils.id(ProLoading.et_amount).clear();
		BaseUtils.id(ProLoading.et_amount).sendKeys(currency);
		BaseUtils.sleep(5);
		BaseUtils.id(ProLoading.btn_transfer).click();
		System.out.println("选择的币种是"+BaseUtils.xpath(ProLoading.currencyName).getText()+";转出的账户为"+BaseUtils.xpath(ProLoading.rechargeWithdrawal).getText()+
				";转入的账户为"+xxxAccounts+";转移了"+currency+"USDT");
		//点击转账页面的返回按钮
		BaseUtils.sleep(20);
//		BaseUtils.xpath(ProLoading.ImageButton).click();
		//点击系统的返回键
		BaseUtils.goBack();
	}
}