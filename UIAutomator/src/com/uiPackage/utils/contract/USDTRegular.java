package com.uiPackage.utils.contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.uiPackage.utils.BaseUtils;
import com.uiPackage.utils.ProLoading;

public class USDTRegular {
	/**
	 * 交易模块，下单
	 * @param price 开仓价格
	 * @param size 手数
	 * @param buyOrSell  0-开仓 1-平仓
	 * @param moreOrLess  0-开多/平多  1-开空/平空
	 */
	public static void placeOrder(String price,String size,int buyOrSell,int moreOrLess) {
		HashMap<Integer, String> bs = new HashMap<Integer, String>();
		bs.put(0, "开仓");
		bs.put(1, "平仓");
		HashMap<Integer, String> ml1 = new HashMap<Integer, String>();
		ml1.put(0, "开多");
		ml1.put(1, "开空");
		HashMap<Integer, String> ml2 = new HashMap<Integer, String>();
		ml2.put(0, "平多");
		ml2.put(1, "平空");
		String moreOrLess_temp;
		if(buyOrSell==0) {
			moreOrLess_temp = ml1.get(moreOrLess);
		}else {
			moreOrLess_temp = ml2.get(moreOrLess);			
		}
		BaseUtils.id(ProLoading.contract).click();
		SwitchContract.switchContract();
		BaseUtils.id(ProLoading.usdtRegular).click();
		BaseUtils.xpath(ProLoading.change).click();
		//如果buyOrSell=0，为开仓
		if(buyOrSell==0) {			
			BaseUtils.id(ProLoading.buy).click();
			BaseUtils.id(ProLoading.price).clear();
			BaseUtils.id(ProLoading.price).sendKeys(price);
			BaseUtils.id(ProLoading.size).clear();
			BaseUtils.id(ProLoading.size).sendKeys(size);
			//如果moreOrLess=0，为开多
			if(moreOrLess==0) {				
				BaseUtils.xpath(ProLoading.buyMore).click();
			}else {
				BaseUtils.xpath(ProLoading.buyLess).click();				
			}
		}else {
			BaseUtils.id(ProLoading.sell).click();
			BaseUtils.id(ProLoading.price).clear();
			BaseUtils.id(ProLoading.price).sendKeys(price);
			BaseUtils.id(ProLoading.size).clear();
			BaseUtils.id(ProLoading.size).sendKeys(size);
			//如果moreOrLess=0，为开多
			if(moreOrLess==0) {				
				BaseUtils.xpath(ProLoading.sellMore).click();
			}else {
				BaseUtils.xpath(ProLoading.sellLess).click();				
			}
		}
		System.out.println("**********下单->"+bs.get(buyOrSell)+",价格"+price+",手数"+size+",默认杠杆100X"+","+moreOrLess_temp+"**********");
		BaseUtils.id(ProLoading.confirm).click();
	}
	/**
	 * 交易模块，下单
	 * @param price 开仓价格
	 * @param size 手数
	 * @param lever 杠杆
	 * @param buyOrSell  0-开仓 1-平仓
	 * @param moreOrLess  0-开多/平多  1-开空/平空
	 */
	public static void placeOrder(String price,String size,String lever,int buyOrSell,int moreOrLess) {
		HashMap<Integer, String> bs = new HashMap<Integer, String>();
		bs.put(0, "开仓");
		bs.put(1, "平仓");
		HashMap<Integer, String> ml1 = new HashMap<Integer, String>();
		ml1.put(0, "开多");
		ml1.put(1, "开空");
		HashMap<Integer, String> ml2 = new HashMap<Integer, String>();
		ml2.put(0, "平多");
		ml2.put(1, "平空");
		String moreOrLess_temp;
		if(buyOrSell==0) {
			moreOrLess_temp = ml1.get(moreOrLess);
		}else {
			moreOrLess_temp = ml2.get(moreOrLess);			
		}
		BaseUtils.id(ProLoading.contract).click();
		SwitchContract.switchContract();
		BaseUtils.id(ProLoading.usdtRegular).click();
		BaseUtils.xpath(ProLoading.change).click();
		if(lever.equals("2X")||lever.equals("3X")||lever.equals("5X")||lever.equals("10X")
					||lever.equals("20X")||lever.equals("33X")||lever.equals("50X")||lever.equals("100X")) {			
			if(buyOrSell==0) {			
				BaseUtils.id(ProLoading.buy).click();		
				//获取杠杆页面的杠杆倍数，并获取索引值
				BaseUtils.xpath(ProLoading.lever).click();
				//滑动杠杆参数,定位到与输入的杠杆一致
				BaseUtils.getLeverX(lever);
				BaseUtils.id(ProLoading.leverConfirm).click();		
				BaseUtils.id(ProLoading.price).clear();
				BaseUtils.id(ProLoading.price).sendKeys(price);
				BaseUtils.id(ProLoading.size).clear();
				BaseUtils.id(ProLoading.size).sendKeys(size);
				if(moreOrLess==0) {				
					BaseUtils.xpath(ProLoading.buyMore).click();
				}else {
					BaseUtils.xpath(ProLoading.buyLess).click();				
				}
			}else {
				BaseUtils.id(ProLoading.sell).click();			
				BaseUtils.id(ProLoading.price).clear();
				BaseUtils.id(ProLoading.price).sendKeys(price);
				BaseUtils.id(ProLoading.size).clear();
				BaseUtils.id(ProLoading.size).sendKeys(size);
				if(moreOrLess==0) {				
					BaseUtils.xpath(ProLoading.sellMore).click();
				}else {
					BaseUtils.xpath(ProLoading.sellLess).click();				
				}
			}
			BaseUtils.id(ProLoading.confirm).click();
			if(buyOrSell==0) {
				System.out.println("**********下单->"+bs.get(buyOrSell)+",价格"+price+",手数"+size+",杠杆"+lever+","+moreOrLess_temp+"**********");				
			}else {
				System.out.println("**********下单->"+bs.get(buyOrSell)+",价格"+price+",手数"+size+","+moreOrLess_temp+"**********");				
			}
		}else {
			System.out.println("输入的杠杆有误...");
		}
	}
	/**
	 * 持仓，指在当前页面操作
	 * @param xxxUSDT 选择哪个币种
	 * @param moreOrLess 选择该币种下的多仓或者空仓 0-多仓 1-空仓
	 * @param stopLossClose 止盈/止损/平仓 1-止盈 2-止损 3-平仓
	 * @param triggerPrice 触发价格
	 * @param limiteOrmarket 0-限价 1-市价
	 * @param executePrice 执行价格
	 * @param parity 可平的手数
	 * 
	 */
	public static void position(String xxxUSDT,int moreOrLess,int stopLossClose,String triggerPrice,int limiteOrmarket,String executePrice,int parity) {
		BaseUtils.id(ProLoading.contract).click();
		SwitchContract.switchContract();
		BaseUtils.id(ProLoading.usdtRegular).click();
		BaseUtils.xpath(ProLoading.position).click();
		HashMap<Integer, String> hashmap = new HashMap<Integer, String>();
		hashmap.put(0, "多");
		hashmap.put(1, "空");
		String moreOrLess_temp = hashmap.get(moreOrLess);
		//获取各个币种的所有仓位
		int count = howManyPositions();
		for (int i = 1; i <= count; i++) {
			if(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")) {
				if(BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText().equals(xxxUSDT)
						&&BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText().equals(moreOrLess_temp)){
					//获取总共/可用的手数
					String parity_temp = BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText();
					//对总共/可用的手数进行正则分离
					String[] parity_accountAndCand = parity_temp.split("/");
					//总共多少手数
					int parity_account = Integer.valueOf(parity_accountAndCand[0]);
					//可用多少手数
					int parity_cand = Integer.valueOf(parity_accountAndCand[1]);
					BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[2]/android.widget.TextView["+stopLossClose+"]").click();
					//止盈
					if(stopLossClose==1) {
						BaseUtils.id(ProLoading.triggerPrice).clear();
						BaseUtils.id(ProLoading.triggerPrice).sendKeys(triggerPrice);
						//限价
						if(limiteOrmarket==0) {
							BaseUtils.xpath(ProLoading.limitedPrice).click();
							BaseUtils.xpath(ProLoading.executePrice).clear();
							BaseUtils.xpath(ProLoading.executePrice).sendKeys(executePrice);
						}else {
							BaseUtils.xpath(ProLoading.marketPrice).click();
						}
						if(parity_cand >= parity) {
							BaseUtils.xpath(ProLoading.parity).clear();
							BaseUtils.xpath(ProLoading.parity).sendKeys(""+parity);
							//点击确定
							BaseUtils.clickPress();
						}else {
							System.out.println("该仓位可用的手数有"+parity_cand+"--->要平的手数为"+parity+"    ps:要平的手数以超过可用的手数，请重新调整要平的手数！");
						}
						//止损
					}else if(stopLossClose==2) {
						BaseUtils.id(ProLoading.triggerPrice).clear();
						BaseUtils.id(ProLoading.triggerPrice).sendKeys(triggerPrice);
						//限价
						if(limiteOrmarket==0) {
							BaseUtils.xpath(ProLoading.limitedPrice).click();
							BaseUtils.xpath(ProLoading.executePrice).clear();
							BaseUtils.xpath(ProLoading.executePrice).sendKeys(executePrice);
						}else {
							BaseUtils.xpath(ProLoading.marketPrice).click();
						}
						if(parity_cand >= parity) {
							BaseUtils.xpath(ProLoading.parity).clear();
							BaseUtils.xpath(ProLoading.parity).sendKeys(""+parity);
							//点击确定
							BaseUtils.clickPress();
						}else {
							System.out.println("该仓位可用的手数有"+parity_cand+"--->要平的手数为"+parity+"    ps:要平的手数以超过可用的手数，请重新调整要平的手数！");
						}
						//平仓
					}else {
						//限价
						if(limiteOrmarket==0) {
							BaseUtils.xpath(ProLoading.limitedPrice).click();
							BaseUtils.xpath(ProLoading.positionSellPrice).clear();
							BaseUtils.xpath(ProLoading.positionSellPrice).sendKeys(executePrice);
						}else {
							BaseUtils.xpath(ProLoading.marketPrice).click();
						}
						if(parity_cand >= parity) {
							BaseUtils.xpath(ProLoading.positionSellAmount).clear();
							BaseUtils.xpath(ProLoading.positionSellAmount).sendKeys(""+parity);
							//如果多仓点击的平仓，则点击平多
							if(moreOrLess_temp.equals("多")) {
								BaseUtils.xpath(ProLoading.sellMore).click();
							}else {
								BaseUtils.xpath(ProLoading.sellLess).click();
							}
							//点击弹窗平仓确认按钮
							BaseUtils.xpath(ProLoading.warehouseClosingConfirmatio).click();
						}else {
							System.out.println("该仓位可用的手数有"+parity_cand+"--->要平的手数为"+parity+"    ps:要平的手数以超过可用的手数，请重新调整要平的手数！");
						}
					}
				}
			}
		}
	}
	/**
	 * 持仓
	 * @param index 选择第几个仓位
	 * @param stopLossClose 止盈/止损/平仓 1-止盈 2-止损 3-平仓
	 * @param triggerPrice 触发价格
	 * @param limiteOrmarket 0-限价 1-市价
	 * @param executePrice 执行价格
	 * @param parity 可平的手数
	 * 
	 */
	public static void position(int index,int stopLossClose,String triggerPrice,int limiteOrmarket,String executePrice,int parity) {
		HashMap<Integer, String>  slc= new HashMap<Integer, String>();
		slc.put(1, "止盈");
		slc.put(2, "止损");
		slc.put(3, "平仓");
		String stopLossClose_temp = slc.get(stopLossClose);
		HashMap<Integer, String> lm = new HashMap<Integer, String>();
		lm.put(0, "限价");
		lm.put(1, "市价");
		String limiteOrmarket_temp = lm.get(limiteOrmarket);
		BaseUtils.id(ProLoading.contract).click();
		SwitchContract.switchContract();
		BaseUtils.id(ProLoading.usdtRegular).click();
		BaseUtils.xpath(ProLoading.position).click();
		List<String> howManyPosition = howManyPosition();
		HashMap<Integer, String> hashmap = new HashMap<Integer, String>();
		int temp = 1;
		for (int i = 0; i < howManyPosition.size(); i++) {
			hashmap.put(temp++, howManyPosition.get(i));
		}
		String howManyPosition_temp = hashmap.get(index);
		//获取总共/可用的手数
		String parity_temp ;
		//对总共/可用的手数进行正则分离
		String[] parity_accountAndCand ;
		//总共多少手数
		int parity_account ;
		//可用多少手数
		int parity_cand = 0;
		int i = 1;
		boolean flag = false ;
		//这个while用来使页面回到顶部
		while((BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[2]"))
				||(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
						&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[2]"))){
			//如果顶部的文案和第一个相同，则此时页面回到顶部
			if((BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
					+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText()).equals(howManyPosition.get(0))) {				
				//判断第一个和第二个是否为真
				while((BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
						&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]"))
						||(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]"))) {
					//第一个为真
					if(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")	
							&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
							&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[2]/android.widget.TextView["+stopLossClose+"]")) {
						//第一个的文案和取消值是否相同
						if((BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()	
								+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText()).equals(howManyPosition_temp)){
							parity_temp = BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText();
							parity_accountAndCand = parity_temp.split("/");
							parity_account = Integer.valueOf(parity_accountAndCand[0]);
							parity_cand = Integer.valueOf(parity_accountAndCand[1]);
							//获取的是多仓还是空仓
							String text = BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText();
							BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[2]/android.widget.TextView["+stopLossClose+"]").click();				
							//止盈
							if(stopLossClose==1) {
								BaseUtils.id(ProLoading.triggerPrice).clear();
								BaseUtils.id(ProLoading.triggerPrice).sendKeys(triggerPrice);
								//限价
								if(limiteOrmarket==0) {
									BaseUtils.sleep(3);
									BaseUtils.xpath(ProLoading.limitedPrice).click();
									BaseUtils.xpath(ProLoading.executePrice).clear();
									BaseUtils.xpath(ProLoading.executePrice).sendKeys(executePrice);
								}else {
									BaseUtils.sleep(3);
									BaseUtils.xpath(ProLoading.marketPrice).click();
								}
								//如果可用的仓位大于等于输入的手数
								if(parity_cand >= parity) {
									BaseUtils.xpath(ProLoading.parity).clear();
									BaseUtils.xpath(ProLoading.parity).sendKeys(""+parity);
									//点击确定
									BaseUtils.clickPress();
									flag = true;
									break;
								}else {
									BaseUtils.xpath(ProLoading.parity).clear();
									BaseUtils.xpath(ProLoading.parity).sendKeys(""+parity_cand);
									//点击确定
									BaseUtils.clickPress();
									System.out.println("该仓位可用的手数有"+parity_cand+"--->要平的手数为"+parity+"    ps:要平的手数以超过可用的手数，已自动调整手数！");
									flag = true;
									break;
								}
								//止损
							}else if(stopLossClose==2) {
								BaseUtils.id(ProLoading.triggerPrice).clear();
								BaseUtils.id(ProLoading.triggerPrice).sendKeys(triggerPrice);
								//限价
								if(limiteOrmarket==0) {
									BaseUtils.sleep(3);
									BaseUtils.xpath(ProLoading.limitedPrice).click();
									BaseUtils.xpath(ProLoading.executePrice).clear();
									BaseUtils.xpath(ProLoading.executePrice).sendKeys(executePrice);
								}else {
									BaseUtils.sleep(3);
									BaseUtils.xpath(ProLoading.marketPrice).click();
								}
								if(parity_cand >= parity) {
									BaseUtils.xpath(ProLoading.parity).clear();
									BaseUtils.xpath(ProLoading.parity).sendKeys(""+parity);
									//点击确定
									BaseUtils.clickPress();
									flag = true;
									break;
								}else {
									BaseUtils.xpath(ProLoading.parity).clear();
									BaseUtils.xpath(ProLoading.parity).sendKeys(""+parity_cand);
									//点击确定
									BaseUtils.clickPress();
									System.out.println("该仓位可用的手数有"+parity_cand+"--->要平的手数为"+parity+"    ps:要平的手数以超过可用的手数，已自动调整手数！");
									flag = true;
									break;
								}
								//平仓
							}else {
								//限价
								if(limiteOrmarket==0) {
									BaseUtils.sleep(3);
									BaseUtils.xpath(ProLoading.limitedPrice).click();
									BaseUtils.xpath(ProLoading.positionSellPrice).clear();
									BaseUtils.xpath(ProLoading.positionSellPrice).sendKeys(executePrice);
								}else {
									BaseUtils.sleep(3);
									BaseUtils.xpath(ProLoading.marketPrice).click();
								}
								if(parity_cand >= parity) {
									BaseUtils.xpath(ProLoading.positionSellAmount).clear();
									BaseUtils.xpath(ProLoading.positionSellAmount).sendKeys(""+parity);
									//如果多仓点击的平仓，则点击平多
									if(text.equals("多")) {
										BaseUtils.xpath(ProLoading.sellMore).click();
									}else {
										BaseUtils.xpath(ProLoading.sellLess).click();
									}
									//点击弹窗平仓确认按钮
									BaseUtils.xpath(ProLoading.warehouseClosingConfirmatio).click();
									flag = true;
									break;
								}else {
									BaseUtils.xpath(ProLoading.positionSellAmount).clear();
									BaseUtils.xpath(ProLoading.positionSellAmount).sendKeys(""+parity_cand);
									//如果多仓点击的平仓，则点击平多
									if(text.equals("多")) {
										BaseUtils.xpath(ProLoading.sellMore).click();
									}else {
										BaseUtils.xpath(ProLoading.sellLess).click();
									}
									//点击弹窗平仓确认按钮
									BaseUtils.xpath(ProLoading.warehouseClosingConfirmatio).click();
									System.out.println("该仓位可用的手数有"+parity_cand+"--->要平的手数为"+parity+"    ps:要平的手数以超过可用的手数，已自动调整手数！");
									flag = true;
									break;
								}
							}
						}

					}
					if(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")	
							&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
							&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[2]/android.widget.TextView["+stopLossClose+"]")) {
						
						if((BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()	
								+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText()).equals(howManyPosition_temp)){
							parity_temp = BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText();
							parity_accountAndCand = parity_temp.split("/");
							parity_account = Integer.valueOf(parity_accountAndCand[0]);
							parity_cand = Integer.valueOf(parity_accountAndCand[1]);
							//获取的是多仓还是空仓
							String text = BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText();
							BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[2]/android.widget.TextView["+stopLossClose+"]").click();				
							//止盈
							if(stopLossClose==1) {
								BaseUtils.id(ProLoading.triggerPrice).clear();
								BaseUtils.id(ProLoading.triggerPrice).sendKeys(triggerPrice);
								//限价
								if(limiteOrmarket==0) {
									BaseUtils.sleep(3);
									BaseUtils.xpath(ProLoading.limitedPrice).click();
									BaseUtils.xpath(ProLoading.executePrice).clear();
									BaseUtils.xpath(ProLoading.executePrice).sendKeys(executePrice);
								}else {
									BaseUtils.sleep(3);
									BaseUtils.xpath(ProLoading.marketPrice).click();
								}
								//如果可用的仓位大于等于输入的手数
								if(parity_cand >= parity) {
									BaseUtils.xpath(ProLoading.parity).clear();
									BaseUtils.xpath(ProLoading.parity).sendKeys(""+parity);
									//点击确定
									BaseUtils.clickPress();
									flag = true;
									break;
								}else {
									BaseUtils.xpath(ProLoading.parity).clear();
									BaseUtils.xpath(ProLoading.parity).sendKeys(""+parity_cand);
									//点击确定
									BaseUtils.clickPress();
									System.out.println("该仓位可用的手数有"+parity_cand+"--->要平的手数为"+parity+"    ps:要平的手数以超过可用的手数，已自动调整手数！");
									flag = true;
									break;
								}
								//止损
							}else if(stopLossClose==2) {
								BaseUtils.id(ProLoading.triggerPrice).clear();
								BaseUtils.id(ProLoading.triggerPrice).sendKeys(triggerPrice);
								//限价
								if(limiteOrmarket==0) {
									BaseUtils.sleep(3);
									BaseUtils.xpath(ProLoading.limitedPrice).click();
									BaseUtils.xpath(ProLoading.executePrice).clear();
									BaseUtils.xpath(ProLoading.executePrice).sendKeys(executePrice);
								}else {
									BaseUtils.sleep(3);
									BaseUtils.xpath(ProLoading.marketPrice).click();
								}
								if(parity_cand >= parity) {
									BaseUtils.xpath(ProLoading.parity).clear();
									BaseUtils.xpath(ProLoading.parity).sendKeys(""+parity);
									//点击确定
									BaseUtils.clickPress();
									flag = true;
									break;
								}else {
									BaseUtils.xpath(ProLoading.parity).clear();
									BaseUtils.xpath(ProLoading.parity).sendKeys(""+parity_cand);
									//点击确定
									BaseUtils.clickPress();
									System.out.println("该仓位可用的手数有"+parity_cand+"--->要平的手数为"+parity+"    ps:要平的手数以超过可用的手数，已自动调整手数！");
									flag = true;
									break;
								}
								//平仓
							}else {
								//限价
								if(limiteOrmarket==0) {
									BaseUtils.sleep(3);
									BaseUtils.xpath(ProLoading.limitedPrice).click();
									BaseUtils.xpath(ProLoading.positionSellPrice).clear();
									BaseUtils.xpath(ProLoading.positionSellPrice).sendKeys(executePrice);
								}else {
									BaseUtils.sleep(3);
									BaseUtils.xpath(ProLoading.marketPrice).click();
								}
								if(parity_cand >= parity) {
									BaseUtils.xpath(ProLoading.positionSellAmount).clear();
									BaseUtils.xpath(ProLoading.positionSellAmount).sendKeys(""+parity);
									//如果多仓点击的平仓，则点击平多
									if(text.equals("多")) {
										BaseUtils.xpath(ProLoading.sellMore).click();
									}else {
										BaseUtils.xpath(ProLoading.sellLess).click();
									}
									//点击弹窗平仓确认按钮
									BaseUtils.xpath(ProLoading.warehouseClosingConfirmatio).click();
									flag = true;
									break;
								}else {
									BaseUtils.xpath(ProLoading.positionSellAmount).clear();
									BaseUtils.xpath(ProLoading.positionSellAmount).sendKeys(""+parity_cand);
									//如果多仓点击的平仓，则点击平多
									if(text.equals("多")) {
										BaseUtils.xpath(ProLoading.sellMore).click();
									}else {
										BaseUtils.xpath(ProLoading.sellLess).click();
									}
									//点击弹窗平仓确认按钮
									BaseUtils.xpath(ProLoading.warehouseClosingConfirmatio).click();
									System.out.println("该仓位可用的手数有"+parity_cand+"--->要平的手数为"+parity+"    ps:要平的手数以超过可用的手数，已自动调整手数！");
									flag = true;
									break;
								}
							}
						}
					}
					//如果当前页没有匹配的值，则向上滑动页面
					if(i % 3 == 0) {
						BaseUtils.slideUpAWholeScreen();
						i = 1;
					}else {				
						i++;
					}
				}
			}else {
				BaseUtils.slideDownAWholeScreen();
			}
			if(flag == true) {
				break;
			}
		}
		
		if(stopLossClose==3) {
			if(parity_cand >= parity) {				
				System.out.println("第"+index+"仓位,"+stopLossClose_temp+","+limiteOrmarket_temp+",手数"+parity);
			}else {
				System.out.println("第"+index+"仓位,"+stopLossClose_temp+","+limiteOrmarket_temp+",手数"+parity_cand);				
			}
		}else {
			if(limiteOrmarket==1) {	
				if(parity_cand >= parity) {				
					System.out.println("第"+index+"仓位,"+stopLossClose_temp+",触发价格"+triggerPrice+","+limiteOrmarket_temp+",手数"+parity);
				}else {
					System.out.println("第"+index+"仓位,"+stopLossClose_temp+",触发价格"+triggerPrice+","+limiteOrmarket_temp+",手数"+parity_cand);				
				}
			}else {
				if(parity_cand >= parity) {	
					System.out.println("第"+index+"仓位,"+stopLossClose_temp+",触发价格"+triggerPrice+","+limiteOrmarket_temp+","+executePrice+",手数"+parity);			
				}else {
					System.out.println("第"+index+"仓位,"+stopLossClose_temp+",触发价格"+triggerPrice+","+limiteOrmarket_temp+","+executePrice+",手数"+parity_cand);			
				}
			}
		}
			
	}
	/**
	 * 委托，针对当前页面操作
	 * @param xxxUSDT 选择何种币种进行撤销
	 * @param moreOrLess 0-多开 1-空开
	 * @param cancel 0-全部撤销 1-针对仓位撤销
	 * @param index 假如空仓仓位有多个，撤销哪个空仓
	 */
	public static void comission(String xxxUSDT,int moreOrLess,int cancel,int index) {
		BaseUtils.id(ProLoading.contract).click();
		SwitchContract.switchContract();
		BaseUtils.id(ProLoading.usdtRegular).click();
		HashMap<Integer, String> hashmap = new HashMap<Integer, String>();
		hashmap.put(0, "多开");
		hashmap.put(1, "空开");
		String moreOrLess_temp = hashmap.get(moreOrLess);
		BaseUtils.xpath(ProLoading.entrust).click();
		//获取各个币种的所有仓位
		int count = howManyPositions();
//		System.out.println("count==="+count);
		//全部平仓
		if(cancel==0) {
			BaseUtils.xpath(ProLoading.allCancel).click();
			//点击确定
			BaseUtils.xpath(ProLoading.cancelAccept).click();
		}else {
			//count=3,定位时索引从2开始，到3结束
			int temp = 1;//如果撤销空仓，记录现在定位到第几个空仓
			for (int i = 2; i <= count; i++) {
				if(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
						&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")) {
					if(BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText().equals(xxxUSDT)
							&&BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText().equals(moreOrLess_temp)) {
						if(temp == index) {							
							BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[4]").click();
							BaseUtils.xpath(ProLoading.cancelAccept).click();
						}else {
							temp += 1;
						}
					}
				}
			}
			BaseUtils.slideUpAWholeScreen();
		}
	}
	/**
	 * 委托，针对滑屏操作
	 * @param index 撤销第几个仓位
	 * @param cancel 0-全部撤销 1-针对仓位撤销
	 */
	public static void comission(int index,int cancel) {
		BaseUtils.id(ProLoading.contract).click();
		SwitchContract.switchContract();
		BaseUtils.id(ProLoading.usdtRegular).click();
		BaseUtils.xpath(ProLoading.entrust).click();
		List<String> howManyComission = howManyComission();
		HashMap<Integer, String> hashmap = new HashMap<Integer, String>();
		int temp = 1;
		for (int i = 0; i < howManyComission.size(); i++) {
//			System.out.println(howManyComission.get(i));
			hashmap.put(temp ++, howManyComission.get(i));
		}
		String moreOrLess_temp = hashmap.get(index);
		boolean flag = false;
//		System.out.println("count==="+count);
		while(BaseUtils.xpathBoolean(ProLoading.allCancel)
				||(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
						&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
						&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[3]"))) {
			if(BaseUtils.xpathBoolean(ProLoading.allCancel)) {				
				if(!BaseUtils.xpath(ProLoading.allCancel).getText().equals("全部撤销")) {
					BaseUtils.slideDownAWholeScreen();
				}else {
					//全部平仓
					if(cancel==0) {
						BaseUtils.xpath(ProLoading.allCancel).click();
						//点击确定
						BaseUtils.xpath(ProLoading.cancelAccept).click();
					}else {
						int i = 2;
						while((BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
									&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
									&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]"))
								||(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
										&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
										&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]"))){
							if(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
									&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
									&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]")
									&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[4]"))	{
								if((BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
										+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText()
										+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]").getText()).equals(moreOrLess_temp)) {							
										BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[4]").click();
										BaseUtils.sleep(5);
										BaseUtils.xpath(ProLoading.cancelAccept).click();
										flag = true;
										break;
								}
							}
							if(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
									&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
									&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]")
									&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[4]")) {
								if((BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
										+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText()
										+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]").getText()).equals(moreOrLess_temp)) {							
										BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[4]").click();
										BaseUtils.sleep(5);
										BaseUtils.xpath(ProLoading.cancelAccept).click();
										flag = true;
										break;
								}
							}
							
							if((i-1) % 3 == 0) {
								BaseUtils.slideUpAWholeScreen();
								i = 1;
							}else {
								i ++;
							}
						}
					}
				}
			}else {
				BaseUtils.slideDownAWholeScreen();
				}
			if(flag = true) {
				break;
			}
		}		
		System.out.println("撤销的是第"+index+"仓位");
	}
	//已成交
	public static void dealConcluded() {
		BaseUtils.xpath(ProLoading.dealConcluded).click();
		
	}
	//已撤销
	public static void revoked() {
		BaseUtils.xpath(ProLoading.revoked).click();
		
	}
	public static void setting() {
		
	}
	/**
	 * 止盈止损，取消当前页面
	 * @param profitOrLoss 0-止盈 1-止损
	 * @param xxxUSDT 选择哪个币种
	 * @param moreOrLess 0-多平 1-空平
	 * @param index 假如止盈止损仓位有多个，取消哪个仓位
	 */
	public static void stoploss(int profitOrLoss,String xxxUSDT,int moreOrLess,int index) {
		BaseUtils.id(ProLoading.contract).click();
		SwitchContract.switchContract();
		BaseUtils.id(ProLoading.usdtRegular).click();
		HashMap<Integer, String> hashmap = new HashMap<Integer, String>();
		hashmap.put(0, "多平");
		hashmap.put(1, "空平");
		HashMap<Integer, String> hashmap1 = new HashMap<Integer, String>();
		hashmap1.put(0, "止盈");
		hashmap1.put(1, "止损");
		String moreOrLess_temp = hashmap.get(moreOrLess);
		String profitOrLoss_temp = hashmap1.get(profitOrLoss);
		BaseUtils.xpath(ProLoading.stoploss).click();
		int count = howManyPositions();
		int temp = 1;//记录取消的仓位
		for (int i = 1; i <= count; i++) {
			if(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]")) {
				if(BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText().equals(profitOrLoss_temp)
						&&BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText().equals(xxxUSDT)
						&&BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]").getText().equals(moreOrLess_temp)) {
					if(temp == index) {		
						System.out.println("index==="+index);
						BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[4]").click();
					}else {
						temp += 1;
					}
				}
			}
		}
	}
	/**
	 * 止盈止损，可以取消第二页的仓位
	 * @param index 假如止盈止损仓位有多个，取消哪个仓位
	 */
	public static void stoploss(int index) {
		//点击合约
		BaseUtils.id(ProLoading.contract).click();
		SwitchContract.switchContract();
		//点击usdt合约
		BaseUtils.id(ProLoading.usdtRegular).click();
		//点击止盈止损模块
		BaseUtils.xpath(ProLoading.stoploss).click();
		//获取当前模块下，所有的仓位，用list集合存储，索引为0的，排在第一位
		List<String> howManyStoploss = howManyStoploss();
		HashMap<Integer, String> hashmap = new HashMap<Integer, String>();
		int temp = 1;
		for (int i = 0; i < howManyStoploss.size(); i++) {
			System.out.println(howManyStoploss.get(i));
			//把list集合中的元素存放到map集合中，此步骤可以省略，直接取list中的元素
			hashmap.put(temp++,howManyStoploss.get(i));
		}
		boolean flag = false;
		//由于获取了当前模块共有多少仓位，此时页面会在最底部，需要滑倒页面顶部
		//取当前页面的第一个LinearLayout和第二个LinearLayout
		while((BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[3]"))
				||(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
						&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
						&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[3]"))) {
			//如果第一个LinearLayout为真
			if(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[3]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[2]/android.widget.TextView[1]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[3]/android.widget.TextView[1]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[2]/android.widget.TextView[1]")) {				
				//判断页面第一个LinearLayout是不是最顶部的模块
				if(!(BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[3]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[2]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[3]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[2]/android.widget.TextView[1]").getText())
						.equals(howManyStoploss.get(0))) {
					//如果与页面最顶部的元素不匹配，则页面向下滑动
					BaseUtils.slideDownAWholeScreen();
				}else {//页面已在最顶部
					System.out.println("页面已在最顶部...");
					//index为要取消的第几个仓位，所以获取对应位置的仓位信息
					String moreOrLess_temp = hashmap.get(index);
					//LinearLayout class索引是从1开始的，所以i=1
					int i = 1;
					//取当前页的LinearLayout为i和i+1的元素
					while((BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]"))
								||(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
										&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
										&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]"))) {
						//获取LinearLayout为i的页面元素，与对应的仓位信息做比较，如果为真，说明当前i为要取消的仓位
						if(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[2]/android.widget.TextView[1]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[3]/android.widget.TextView[1]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[2]/android.widget.TextView[1]")) {							
							if((BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
									+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText()
									+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]").getText()
									+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
									+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[2]/android.widget.TextView[1]").getText()
									+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[3]/android.widget.TextView[1]").getText()
									+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
									+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[2]/android.widget.TextView[1]").getText()
									).equals(moreOrLess_temp)){				
								//点击取消
								BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[4]").click();
								flag = true;
								break;
							}
						}
						//获取LinearLayout为i+1的页面元素，与对应的仓位信息做比较，如果为真，说明当前i+1为要取消的仓位
						if(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[2]/android.widget.TextView[1]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[3]/android.widget.TextView[1]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
								&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[2]/android.widget.TextView[1]")) {							
							if((BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
									+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText()
									+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]").getText()
									+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
									+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[2]/android.widget.TextView[1]").getText()
									+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[3]/android.widget.TextView[1]").getText()
									+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
									+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[2]/android.widget.TextView[1]").getText()
									).equals(moreOrLess_temp)) {
								//点击取消
								BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[4]").click();
								flag = true;
								break;
							}
						}	
						//如果当前页的仓位信息，与要取消的仓位信息不匹配，则滑动页面
						//由于止盈止损一个页面有4个仓位，所以当i=4时，即为最后当仓位，需要滑动页面
						if(i % 4 == 0) {
							BaseUtils.slideUpAWholeScreen();
							i = 1;
						}else {
							i ++;
						}
						//如果定位到暂无更多数据和i=4，证明已到页面的最底部，则结束循环
						if(BaseUtils.xpathBoolean(ProLoading.noMoreData)&&i==1) {
							break;
						}
					}
				}
			}else {
				BaseUtils.slideDownAWholeScreen();
			}	
			if(flag == true) {
				break;
			}
		}
		System.out.println("取消的是第"+index+"仓位");
	}
	/**
	 * 1.获取当前各个币种一共有多少仓位
	 * 2.获取委托中有多少委托的仓位
	 * @return
	 */
	public static int howManyPositions() {
		int i = 1;
		while(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]")) {
			i += 1;
		}
		return i-1;
	}
	/**
	 * 返回各个仓位的顺序，针对的是持仓模块
	 * @return
	 */
	public static List<String> howManyPosition() {
		List<String> list = new ArrayList<String>();
		int i = 1;
		while(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")) {
			String key = BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
					+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText();
			if(!list.contains(key)) {
				list.add(key);
			}
			if(i % 3 == 0) {
				BaseUtils.slideUpAWholeScreen();
				i = 1 ;
			}else {
				i += 1;				
			}
			if(BaseUtils.idBoolean(ProLoading.pnl_label)&&i==1) {
				break;
			}
		}
		return list;
	}
	/**
	 * 针对委托模块
	 * @return
	 */
	public static List<String> howManyComission() {
		List<String> list = new ArrayList<String>();
		int i = 2;
		String key = null;
		//while循环，用来判断第一个或者第二个LinearLayout是否存在
		while((BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]"))
				||(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
						&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
						&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]"))) {
			//如果第一个LinearLayout存在
			if(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]")) {				
				key = BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]").getText()
						;
			}
			//如果第一个不存在，第二个LinearLayout存在
			if(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]")
					&&!BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
					&&!BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
					&&!BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]")) {
				key = BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]").getText()
						;
			}
			if(!list.contains(key)) {
				list.add(key);
			}
			if((i-1) % 3 == 0) {
				BaseUtils.slideUpAWholeScreen();
				i = 1 ;
			}else {
				i += 1;				
			}
			if(BaseUtils.xpathBoolean(ProLoading.noMoreData)&&i==1) {
				break;
			}
		}
		return list;
	}
	/**
	 * 针对止盈止损
	 * @return
	 */
	public static List<String> howManyStoploss() {
		List<String> list = new ArrayList<String>();
		int i = 1;
		String key = null;
		//while循环，用来判断第一个或者第二个LinearLayout是否存在
		while((BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
				&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]"))
				||(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
						&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
						&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]"))) {
			//如果第一个LinearLayout存在，获取对应元素位置的文本，拼接后存入list中，用来做唯一标示
			if(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[2]/android.widget.TextView[1]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[3]/android.widget.TextView[1]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[2]/android.widget.TextView[1]")) {				
				key = BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[2]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[3]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[2]/android.widget.TextView[1]").getText()
						;
			}
			//如果第一个不存在，第二个LinearLayout存在
			if(BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[2]/android.widget.TextView[1]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[3]/android.widget.TextView[1]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
					&&BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[2]/android.widget.TextView[1]")
					&&!BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]")
					&&!BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]")
					&&!BaseUtils.xpathBoolean("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]")) {
				key = BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[2]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.LinearLayout[1]/android.widget.TextView[3]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[2]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[3]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText()
						+BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+(i+1)+"]/android.widget.RelativeLayout[2]/android.widget.LinearLayout[2]/android.widget.TextView[1]").getText()
						;
			}
			//如果list中没有该元素，则存入
			if(!list.contains(key)) {
				list.add(key);
			}
			if(i % 4 == 0) {
				BaseUtils.slideUpAWholeScreen();
				i = 1 ;
			}else {
				i += 1;				
			}
			//如果定位到暂无更多数据和i=4，证明已到页面的最底部，则结束循环
			if(BaseUtils.xpathBoolean(ProLoading.noMoreData)&&i==1) {
				break;
			}
		}
		return list;
	}
	/**
	 * 统计相同类型的仓位有多少个
	 * @return
	 */
	public static HashMap<String,Integer > howManySamePositions() {
		HashMap<String,Integer > hashmap = new HashMap<String,Integer >();
		int num1 = 0;
		int num2 = 0;
		int count = howManyPositions();
		for (int i = 2; i < count; i++) {
			if(BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText().equals("多开")) {
				num1 += 1;
			}
			if(BaseUtils.xpath("//android.support.v7.widget.RecyclerView/android.widget.LinearLayout["+i+"]/android.widget.LinearLayout[1]/android.widget.TextView[1]").getText().equals("空开")){
				num2 += 1;
			}			
		}
		hashmap.put("多开",num1);
		hashmap.put("空开",num2);
		return hashmap;
	}
	
	public static void test() {
		BaseUtils.id(ProLoading.contract).click();
		BaseUtils.id(ProLoading.usdtRegular).click();
		BaseUtils.xpath(ProLoading.entrust).click();
		System.out.println(howManyPositions());
	}
}
