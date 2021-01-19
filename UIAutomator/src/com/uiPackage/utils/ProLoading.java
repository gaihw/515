package com.uiPackage.utils;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class ProLoading {
	//合约模块
	//交割
	public static String contractFilePath = "properties/Contract.properties";
	public static String contract;
	public static String rbRegular;
	public static String change;
	public static String buy;
	public static String buyMore;
	public static String buyLess;
	public static String price;//交易模块平仓的价格
	public static String size;//交易模块平仓的手数
	public static String confirm;//下订单确认按钮
	public static String leverConfirm;//杠杆的确定按钮
	public static String lever;
	public static String twoX;
	public static String threeX;
	public static String fiveX;
	public static String tenX;
	public static String twentyX;
	public static String thirty_threeX;
	public static String fiftyX;
	public static String one_hundredX;
	public static String sell;
	public static String sellMore;
	public static String sellLess;
	public static String position;//持仓
	public static String entrust;//委托
	public static String dealConcluded;//已成交
	public static String revoked;//已撤销
	public static String stoploss;//止盈止损
	public static String l_hamburger;//左上角的抽屉
	public static String r_hamburger;//右上角的抽屉
	public static String BTC;//左上角的抽屉币种
	public static String EOS;//左上角的抽屉币种
	public static String ETH;//左上角的抽屉币种
	public static String LTC;//左上角的抽屉币种
	public static String XRP;//左上角的抽屉币种
	public static String ETC;//左上角的抽屉币种
	public static String BNB;//左上角的抽屉币种
	public static String HT1;//左上角的抽屉币种
	public static String fundAllocations;//右上角的资金分配
	public static String fundAllocations_count;//资金分配-转账金额
	public static String fundAllocations_allCount;//资金分配的金额  全部转入
	public static String transferOffunds;//右上角的资金转入
	//usdt
	public static String usdtRegular;
	public static String triggerPrice;//触发价格
	public static String limitedPrice;//限价
	public static String marketPrice;//市价
	public static String executePrice;//执行价格
	public static String parity;//可平手数
	public static String positionSellPrice;//持仓模块平仓价格
	public static String positionSellAmount;//持仓模块平仓数量
	public static String warehouseClosingConfirmatio;//持仓模块平仓确认
	public static String allCancel;//全部撤销
	public static String cancelAccept;//撤销确定
	public static String volume;//右上角的成交量按钮
	public static String topSetting;//右上角的设置
	public static String pullDown;//右上角的下拉按钮
	public static String noMoreData;//暂无更多数据
	public static String pnl_label;//持仓位的盈亏总计
	
	//币币模块
	public static String currencyFilePath = "properties/Currency.properties";
	public static String currency;
	public static String unfinishedEntrustment;//未成交委托
	public static String historicalEntrustment;//历史委托
	public static String currency_kline;//币币交易的k线，卖出右侧的三个竖杠
	public static String currency_BTC;//币币页面左上角的币种
	public static String currency_ETH;//币币页面左上角的币种
	public static String currency_LTC;//币币页面左上角的币种
	public static String currency_XRP;//币币页面左上角的币种
	public static String currency_ETC;//币币页面左上角的币种
	public static String currency_BCH;//币币页面左上角的币种
	public static String currency_OMG;//币币页面左上角的币种
	public static String currency_58B;//币币页面左上角的币种
	public static String currency_leftTop_OPTIONAL;//币币页面左上角的自选
	public static String currency_leftTop_USDT;//币币页面左上角的USDT
	public static String currency_leftTop_BTC;//币币页面左上角的BTC
	public static String currency_leftTop_ETH;//币币页面左上角的ETH
	public static String currency_unfinish_cancel;//未成交委托的仓位撤单
	public static String currency_unfinish_cancel_accept;//未成交委托的仓位撤单的确认
	public static String currency_collect;//币币的收藏
	public static String k_screenBig;//k线页面的放大
	public static String k_setting;//k线页面的设置
	public static String k_more;//k线页面的更多
	
	
	//首页模块
	public static String homePageFilePath = "properties/HomePage.properties";
	public static String homePage;
	public static String homePage_USDTContract;
	public static String homePage_RBContract;
	public static String homePage_digitContract;
	public static String bitArithmetic;//比特算力
	public static String mortgageLoan;//抵押贷款
	public static String currencyMarket;//币币行情
	public static String rankOfIncrease;//涨幅榜
	public static String homePage_USDT;
	public static String homePage_BTC;
	public static String homePage_ETH;
	public static String currencyMarket_BTC;
	public static String currencyMarket_ETH;
	public static String currencyMarket_Buy;//币币行情页的买入按钮
	public static String currencyMarket_Sell;
	public static String currencyMarket_BuySell_select;//币币行情-买入卖出-限价-下拉框
	public static String currencyMarket_BuySell_limitprice;//币币行情-买入卖出-限价
	public static String currencyMarket_BuySell_marketprice;
	public static String currencyMarket_BuySell_price;//币币行情-买入卖出-价格
	public static String currencyMarket_BuySell_amount;
	public static String currencyMarket_BuySell_btnOrder;
	public static String currencyMarket_BuySell_buy;//币币行情-买入卖出-买入
	public static String currencyMarket_BuySell_sell;
	public static String currencyMarket_BuySell_setting;//币币行情-买入卖出-设置
	public static String currencyMarket_BuySell_password;//币币行情-买入卖出-请输入资金密码
	public static String currencyMarket_BuySell_PasswordConfirm;//币币行情-买入卖出-确认资金密码
	public static String currencyMarket_BuySell_confirm;//币币行情-买入卖出-确认
	public static String currencyMarket_BuySell_sms;//币币行情-买入卖出-获取验证码
	public static String currencyMarket_BuySell_phoneverify;//币币行情-买入卖出-请输入短信验证码
	public static String homePage_swap;
	public static String homePage_swap_BTCUSDT;
	public static String homePage_swap_EOSUSDT;
	public static String homePage_swap_ETHUSDT;
	public static String homePage_swap_LTCUSDT;
	public static String homePage_swap_XRPUSDT;
	public static String homePage_swap_ETCUSDT;
	public static String homePage_swap_DASHUSDT;
	public static String homePage_swap_BCHUSDT;
	public static String homePage_swap_BSVUSDT;
	
	//otc模块
	public static String otcFilePath = "properties/OTC.properties";
	public static String otc;
	
	//我的模块
	public static String mineFilePath = "properties/Mine.properties";
	public static String mine;
	public static String pleseaLogin;
	public static String writePhone;
	public static String writePassword;
	public static String loginButton;
	public static String setting;
	public static String quit;
	public static String accept;
	public static String totalAssetsBTC;
	public static String totalAssetsCnyCNY;
	public static String transfer;
	public static String withdraw;
	public static String deposit;
	public static String edtSearch;
	public static String currencyName;
	public static String tv_transfer_from;
	public static String tv_transfer_to;
	public static String et_amount;
	public static String btn_transfer;
	public static String rechargeWithdrawal;//充值提现
	public static String USDTAccount;//USDT账户
	public static String RBAccount;//交割账户
	public static String CurrencyAccount;//币币账户
	public static String ImageButton;//转账页面的返回按钮
	public static String hide;//隐藏的眼睛

	
	static {
		//合约模块
		contract = getValueByKey(contractFilePath,"合约");
		//交割合约
		rbRegular = getValueByKey(contractFilePath,"交割");
		change = getValueByKey(contractFilePath,"交易");
		buy = getValueByKey(contractFilePath,"开仓");
		price = getValueByKey(contractFilePath,"价格");//
		size = getValueByKey(contractFilePath,"手数");
		buyMore = getValueByKey(contractFilePath,"开多");
		buyLess = getValueByKey(contractFilePath,"开空");
		confirm = getValueByKey(contractFilePath,"确认");
		leverConfirm = getValueByKey(contractFilePath,"杠杆确认");
		lever = getValueByKey(contractFilePath,"杠杆");
		twoX = getValueByKey(contractFilePath,"2X");
		threeX = getValueByKey(contractFilePath,"3X");
		fiveX = getValueByKey(contractFilePath,"5X");
		tenX = getValueByKey(contractFilePath,"10X");
		twentyX = getValueByKey(contractFilePath,"20X");
		thirty_threeX = getValueByKey(contractFilePath,"33X");
		fiftyX = getValueByKey(contractFilePath,"50X");
		one_hundredX = getValueByKey(contractFilePath,"100X");
		sell = getValueByKey(contractFilePath,"平仓");
		sellMore = getValueByKey(contractFilePath,"平多");
		sellLess = getValueByKey(contractFilePath,"平空");
		position = getValueByKey(contractFilePath,"持仓");
		entrust = getValueByKey(contractFilePath,"委托");
		dealConcluded = getValueByKey(contractFilePath,"已成交");
		revoked = getValueByKey(contractFilePath,"已撤销");
		stoploss = getValueByKey(contractFilePath,"止盈止损");
		l_hamburger = getValueByKey(contractFilePath,"左上角的抽屉");
		r_hamburger = getValueByKey(contractFilePath,"右上角的抽屉");
		BTC = getValueByKey(contractFilePath,"BTC");
		EOS = getValueByKey(contractFilePath,"EOS");
		ETH = getValueByKey(contractFilePath,"ETH");
		LTC = getValueByKey(contractFilePath,"LTC");
		XRP = getValueByKey(contractFilePath,"XRP");
		ETC = getValueByKey(contractFilePath,"ETC");
		BNB = getValueByKey(contractFilePath,"BNB");
		HT1 = getValueByKey(contractFilePath,"HT1");
		fundAllocations = getValueByKey(contractFilePath,"资金分配");
		fundAllocations_count = getValueByKey(contractFilePath,"资金分配-转账金额");
		fundAllocations_allCount = getValueByKey(contractFilePath,"资金分配-全部转入");
		transferOffunds = getValueByKey(contractFilePath,"资金转入");
		//usdt
		usdtRegular = getValueByKey(contractFilePath,"usdt");
		triggerPrice = getValueByKey(contractFilePath,"触发价格");
		limitedPrice = getValueByKey(contractFilePath,"限价");
		marketPrice = getValueByKey(contractFilePath,"市价");
		executePrice = getValueByKey(contractFilePath,"执行价格");
		parity = getValueByKey(contractFilePath,"可平手数");
		positionSellPrice = getValueByKey(contractFilePath,"持仓平仓-价格");
		positionSellAmount = getValueByKey(contractFilePath,"持仓平仓-数量");
		warehouseClosingConfirmatio = getValueByKey(contractFilePath,"平仓确认");
		allCancel = getValueByKey(contractFilePath,"全部撤销");
		cancelAccept = getValueByKey(contractFilePath,"撤销确定");
		noMoreData = getValueByKey(contractFilePath,"暂无更多数据");
		pnl_label = getValueByKey(contractFilePath,"盈亏总计");
		
		//顶部按钮
		topSetting = getValueByKey(contractFilePath,"设置");
		volume = getValueByKey(contractFilePath,"成交量");
		pullDown = getValueByKey(contractFilePath,"下拉按钮");
		
		//币币模块
		currency = getValueByKey(currencyFilePath,"币币");
		unfinishedEntrustment = getValueByKey(currencyFilePath,"未成交委托");
		historicalEntrustment = getValueByKey(currencyFilePath,"历史委托");
		currency_kline = getValueByKey(currencyFilePath,"币币-k线");
		currency_BTC = getValueByKey(currencyFilePath,"币币-BTC");
		currency_ETH = getValueByKey(currencyFilePath,"币币-ETH");
		currency_LTC = getValueByKey(currencyFilePath,"币币-LTC");
		currency_XRP = getValueByKey(currencyFilePath,"币币-XRP");
		currency_ETC = getValueByKey(currencyFilePath,"币币-ETC");
		currency_BCH = getValueByKey(currencyFilePath,"币币-BCH");
		currency_OMG = getValueByKey(currencyFilePath,"币币-OMG");
		currency_58B = getValueByKey(currencyFilePath,"币币-58B");
		currency_leftTop_OPTIONAL = getValueByKey(currencyFilePath,"币币-左上角-自选");
		currency_leftTop_USDT = getValueByKey(currencyFilePath,"币币-左上角-USDT");
		currency_leftTop_BTC = getValueByKey(currencyFilePath,"币币-左上角-BTC");
		currency_leftTop_ETH = getValueByKey(currencyFilePath,"币币-左上角-ETH");
		currency_unfinish_cancel = getValueByKey(currencyFilePath,"币币-未成交-撤单");
		currency_unfinish_cancel_accept = getValueByKey(currencyFilePath,"币币-未成交-撤单-确认");
		currency_collect = getValueByKey(currencyFilePath,"币币-收藏");
		k_screenBig = getValueByKey(currencyFilePath,"k线页面-放大");
		k_setting = getValueByKey(currencyFilePath,"k线页面-设置");
		k_more = getValueByKey(currencyFilePath,"k线页面-更多");//k线页面的更多
		
		//首页模块
		homePage = getValueByKey(homePageFilePath,"首页");
		homePage_USDTContract = getValueByKey(homePageFilePath,"USDT合约");
		homePage_RBContract = getValueByKey(homePageFilePath,"交割合约");
		homePage_digitContract = getValueByKey(homePageFilePath,"币本位合约");
		bitArithmetic = getValueByKey(homePageFilePath,"比特算力");
		mortgageLoan = getValueByKey(homePageFilePath,"抵押贷款");
		currencyMarket = getValueByKey(homePageFilePath,"币币行情");
		rankOfIncrease = getValueByKey(homePageFilePath,"涨幅榜");
		homePage_USDT = getValueByKey(homePageFilePath,"USDT");
		homePage_BTC = getValueByKey(homePageFilePath,"BTC");
		homePage_ETH = getValueByKey(homePageFilePath,"ETH");
		currencyMarket_BTC = getValueByKey(homePageFilePath,"比特币-BTC");
		currencyMarket_ETH = getValueByKey(homePageFilePath,"太子-ETH");
		currencyMarket_Buy = getValueByKey(homePageFilePath,"币币行情-买入");
		currencyMarket_Sell = getValueByKey(homePageFilePath,"币币行情-卖出");
		currencyMarket_BuySell_select = getValueByKey(homePageFilePath,"币币行情-买入卖出-限价-下拉框");
		currencyMarket_BuySell_limitprice = getValueByKey(homePageFilePath,"币币行情-买入卖出-限价");
		currencyMarket_BuySell_marketprice = getValueByKey(homePageFilePath,"币币行情-买入卖出-市价");
		currencyMarket_BuySell_price = getValueByKey(homePageFilePath,"币币行情-买入卖出-价格");
		currencyMarket_BuySell_amount = getValueByKey(homePageFilePath,"币币行情-买入卖出-数量");
		currencyMarket_BuySell_btnOrder = getValueByKey(homePageFilePath,"币币行情-买入/卖出确定按钮");
		currencyMarket_BuySell_buy = getValueByKey(homePageFilePath,"币币行情-买入卖出-买入");
		currencyMarket_BuySell_sell = getValueByKey(homePageFilePath,"币币行情-买入卖出-卖出");
		currencyMarket_BuySell_setting = getValueByKey(homePageFilePath,"币币行情-买入卖出-设置");
		currencyMarket_BuySell_password = getValueByKey(homePageFilePath,"币币行情-买入卖出-请输入资金密码");
		currencyMarket_BuySell_PasswordConfirm = getValueByKey(homePageFilePath,"币币行情-买入卖出-确认资金密码");
		currencyMarket_BuySell_confirm = getValueByKey(homePageFilePath,"币币行情-买入卖出-确认");
		currencyMarket_BuySell_sms = getValueByKey(homePageFilePath,"币币行情-买入卖出-获取验证码");
		currencyMarket_BuySell_phoneverify = getValueByKey(homePageFilePath,"币币行情-买入卖出-请输入短信验证码");
		homePage_swap = getValueByKey(homePageFilePath,"首页-切换右箭头");
		homePage_swap_BTCUSDT = getValueByKey(homePageFilePath,"切换-BTCUSDT");
		homePage_swap_EOSUSDT = getValueByKey(homePageFilePath,"切换-EOSUSDT");
		homePage_swap_ETHUSDT = getValueByKey(homePageFilePath,"切换-ETHUSDT");
		homePage_swap_LTCUSDT = getValueByKey(homePageFilePath,"切换-LTCUSDT");
		homePage_swap_XRPUSDT = getValueByKey(homePageFilePath,"切换-XRPUSDT");
		homePage_swap_ETCUSDT = getValueByKey(homePageFilePath,"切换-ETCUSDT");
		homePage_swap_DASHUSDT = getValueByKey(homePageFilePath,"切换-DASHUSDT");
		homePage_swap_BCHUSDT = getValueByKey(homePageFilePath,"切换-BCHUSDT");
		homePage_swap_BSVUSDT = getValueByKey(homePageFilePath,"切换-BSVUSDT");
		
		//OTC模块
		otc = getValueByKey(otcFilePath,"OTC");
		
		//我的模块
		mine = getValueByKey(mineFilePath,"我的");
		pleseaLogin = getValueByKey(mineFilePath,"请登录");
		writePhone = getValueByKey(mineFilePath,"手机号");
		writePassword = getValueByKey(mineFilePath,"密码");
		loginButton = getValueByKey(mineFilePath,"登录");
		setting = getValueByKey(mineFilePath,"设置");
		quit = getValueByKey(mineFilePath,"退出");
		accept = getValueByKey(mineFilePath,"确定");
		totalAssetsBTC = getValueByKey(mineFilePath,"总资产BTC");
		totalAssetsCnyCNY = getValueByKey(mineFilePath,"总资产CNY");
		transfer = getValueByKey(mineFilePath,"我的页面转账");
		withdraw = getValueByKey(mineFilePath,"提现");
		deposit = getValueByKey(mineFilePath,"充值");
		edtSearch = getValueByKey(mineFilePath,"搜索币种");
		currencyName = getValueByKey(mineFilePath,"当前币种");
		tv_transfer_from = getValueByKey(mineFilePath,"转出账户");
		tv_transfer_to = getValueByKey(mineFilePath,"转入账户");
		et_amount = getValueByKey(mineFilePath,"转账数量");
		btn_transfer = getValueByKey(mineFilePath,"转账");
		rechargeWithdrawal = getValueByKey(mineFilePath,"充值提现账户");
		USDTAccount = getValueByKey(mineFilePath,"USDT合约账户");
		RBAccount = getValueByKey(mineFilePath,"交割合约账户");
		CurrencyAccount = getValueByKey(mineFilePath,"币币账户");
		ImageButton = getValueByKey(mineFilePath,"转账返回按钮");
		hide = getValueByKey(mineFilePath,"隐藏的眼睛");
	}
	public static String getValueByKey(String filePath, String key)  {
		Reader fileReader;
		try {
			fileReader = new FileReader(filePath);
			Properties properties = new Properties();
			properties.load(fileReader);
			fileReader.close();
			return properties.getProperty(key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}