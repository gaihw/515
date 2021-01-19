package service.business;

import config.ConfigCenter;

/**
 * 
 * @author Muguozheng
 * @date 2018年11月8日
 * @version 1.0
 * @description 防爆仓检查,即下单前进行校验,防止出现下单后即爆仓的情况
 */
public class OrderCheck {
	// 格式化
	static java.text.DecimalFormat df = new java.text.DecimalFormat("#.######");

	public static void main(String[] args) {
		orderCheck();
	}

	// 委托单防爆仓检查
	public static void orderCheck() {
		// 赋值区
		// 各币种的标记价格,如果标记价格有变化,需及时更新,"-1"占位用
		double[] indexPrices = { -1, 4038, 4, 125, 35, 100, 0.4, 9.8 };
		int contractId = 1; // 合约id
		double assetsFront = 350; // 当前权益-前
		double riskDegree = 0; // 当前风险度
		double type = 1; // type:下单类型,1 限价单 2 市价单
		double price = 4000; // 委托价格
		double size = -100; // 委托数量
		double averagePriceBuy = 0; // 下单币种多仓持仓均价
		double positionSizeBuy = 0; // 下单币种多仓数量

		double averagePriceSell = 0; // 下单币种空仓持仓均价
		double positionSizeSell = 0; // 下单币种空仓数量
		double buyFirst = 3.5; // 买一价格
		double sellFirst = 4.5; // 卖一价格

		// 系统参数,一般不变
		String[] tableNames = { "ERROR", "BTC", "EOS", "ETH", "LTC", "XRP", "ETC" };
		// 各个币种合约乘数 依次为:BTC、EOS、ETH、LTC、XRP、ETC
		// { -1, 0.002, 2, 0.05, 0.2, 40, 1 };
		double[] ratios = ConfigCenter.getRatios();
		double takerFee = 0.0006; // taker手续费率
		double MM = 0.005; // 维持保证金率

		// 当前账户维持保证金
		double occupancyBondFront = assetsFront * riskDegree;
		// 当前仓位净头寸
		double positionValueNet = (averagePriceBuy * positionSizeBuy - averagePriceSell * positionSizeSell)
				* ratios[contractId];
		// 下单币种多仓维持保证金
		double occupancyBondBuy = averagePriceBuy * positionSizeBuy * ratios[contractId] * MM;
		// 下单币种空仓维持保证金
		double occupancyBondSell = averagePriceSell * positionSizeSell * ratios[contractId] * MM;

		double assetsBack = 0; // 新当前资产
		double occupancyBondBack = 0; // 新维持保证金
		double sizeMax = 0; // 极限值
		double orderValue = 0; // 委托价值
		double orderPrice = 0; // 委托价格
		StringBuffer testDetail = new StringBuffer();

		// 参数校验
		if (price <= 0 || size == 0 || type != 1 && type != 2 || assetsFront <= 0) {
			System.out.println("参数输入有误,请检查！");
			return;
		}

		System.out.println("======" + tableNames[contractId] + "防爆仓计算" + "======");

		// 限价单
		if (type == 1) {
			// 委托价值赋值
			orderValue = price * size * ratios[contractId];
			orderPrice = price; // 限价单委托价格即输入价格

			// 委托单预期盈利,可以进行精简校验
			// 资产变化=预期盈利-(开仓手续费和平仓手续费)
			double assetsAdd = (indexPrices[contractId] - price) * size * ratios[contractId]
					- 2 * price * size * ratios[contractId] * takerFee;
			// 占用保证金变化
			double occupancyBondAdd = Math.abs(orderValue) * MM;

			if (assetsAdd >= occupancyBondAdd) {
				System.out.println("该委托单预期盈利,(盈利-手续费)>该委托新增占用保证金,无需校验");
				return; // 无需后续校验,所以终止方法
			}

			// 精简校验未能拦截,进入后续校验
			// 限价买单
			if (size > 0) {
				// 净头寸方向和该委托单方向相同
				if (size * positionValueNet >= 0) {
					// 计算新可用权益 新可用权益=可用权益-2*委托价*委托量*乘数*taker+（index-委托价）*委托量*乘数
					assetsBack = assetsFront - 2 * price * size * ratios[contractId] * takerFee
							+ (indexPrices[contractId] - price) * size * ratios[contractId];
					// 计算新维持保证金 新维持保证金 = 维持保证金+委托价*委托量*乘数*MM
					occupancyBondBack = occupancyBondFront + orderValue * MM;
					// 对比判断能否下单
					check(assetsBack, occupancyBondBack);
					// 1-委托量=（可用权益-维持保证金）/((委托价*MM+2*委托价*taker-(index-委托价))*乘数)
					sizeMax = (assetsFront - occupancyBondFront)
							/ ((price * MM + 2 * price * takerFee - (indexPrices[contractId] - price))
									* ratios[contractId]);
					testDetail.append("1-限价买-同向\r\n最大可开手数:" + df.format(sizeMax));
				} else {// 净头寸方向和该委托单方向相反
					if (Math.abs(positionValueNet) > Math.abs(orderValue)) { // 净头寸量大于等于该委托单价值
						// 新可用权益=可用权益-2*委托价*委托量*乘数*taker+（index-委托价）*委托量*乘 数
						// 新维持保证金 = 维持保证金
						assetsBack = assetsFront - 2 * price * size * ratios[contractId] * takerFee
								+ (indexPrices[contractId] - price) * size * ratios[contractId];
						occupancyBondBack = occupancyBondFront;
						// 对比判断能否下单
						check(assetsBack, occupancyBondBack);
						// 2-委托量=(可用权益-维持保证金)/((2*委托价*taker-(index-委托价))*乘数)
						sizeMax = (assetsFront - occupancyBondFront)
								/ ((2 * price * takerFee - (indexPrices[contractId] - price)) * ratios[contractId]);
						testDetail.append("2-限价买-反向-仓位>委托量\r\n最大可开手数:" + df.format(sizeMax));
					} else { // 净头寸量小于该委托单量
						// 新可用权益=可用权益-2*委托价*委托量*乘数*taker+（index-委托价）*委托量*乘 数
						// 新维持保证金=维持保证金(账户)-该币种空仓维持保证金+该币种多仓维持保证金+委托价*委托量*乘数*MM
						assetsBack = assetsFront - 2 * price * size * ratios[contractId] * takerFee
								+ (indexPrices[contractId] - price) * size * ratios[contractId];
						occupancyBondBack = occupancyBondFront - occupancyBondSell + occupancyBondBuy
								+ Math.abs(orderValue) * MM;
						// 对比判断能否下单
						check(assetsBack, occupancyBondBack);
						// 3-委托量=(可用权益-维持保证金(账户)+该币种空仓维持保证金-该币种多仓维持保证金)/((2*委托价*taker-（index-委托价)+委托价*MM)*乘数)
						sizeMax = (assetsFront - occupancyBondFront + occupancyBondSell - occupancyBondBuy)
								/ ((2 * price * takerFee - (indexPrices[contractId] - price) + price * MM)
										* ratios[contractId]);
						testDetail.append("3-限价买-反向-仓位<委托量\r\n最大可开手数:" + df.format(sizeMax));
					}
				}
			} else { // 限价卖单
				// 净头寸方向和该委托单方向相同
				if (size * positionValueNet >= 0) {
					assetsBack = assetsFront - (2 * price * takerFee - (price - indexPrices[contractId]))
							* Math.abs(size) * ratios[contractId];
					occupancyBondBack = occupancyBondFront + Math.abs(orderValue) * MM;
					// 对比判断能否下单
					check(assetsBack, occupancyBondBack);
					// 4-委托量=(可用权益-维持保证金)/((委托价*MM+2*委托价*taker-（委托价-index）)*乘数)
					sizeMax = (assetsFront - occupancyBondFront)
							/ ((price * MM + 2 * price * takerFee - (price - indexPrices[contractId]))
									* ratios[contractId]);
					testDetail.append("4-限价卖-同向\r\n最大可开手数:" + df.format(sizeMax));
				} else { // 净头寸方向和该委托单方向相反
					// 净头寸量大于等于该委托单量
					if (Math.abs(positionValueNet) >= Math.abs(orderValue)) {
						// 新可用权益=可用权益-2*委托价*委托量*乘数*taker+（委托价-index）*委托量*乘 数
						// 新维持保证金 = 维持保证金
						assetsBack = assetsFront - (2 * price * takerFee - (price - indexPrices[contractId]))
								* Math.abs(size) * ratios[contractId];
						occupancyBondBack = occupancyBondFront;
						// 对比判断能否下单
						check(assetsBack, occupancyBondBack);
						// 5-委托量=(可用权益-维持保证金)/((2*委托价*taker-（委托价-index）)*乘数)
						sizeMax = (assetsFront - occupancyBondFront)
								/ ((2 * price * takerFee - (price - indexPrices[contractId])) * ratios[contractId]);
						testDetail.append("5-限价卖-反向-仓位>委托量\r\n最大可开手数:" + df.format(sizeMax));
					} else { // 净头寸量小于该委托单量
						// 新可用权益=可用权益-2*委托价*委托量*乘数*taker+（委托价-index）*委托量*乘 数
						// 新维持保证金=维持保证金(账户)-该币种多仓维持保证金+该币种空仓维持保证金+委托价*委托量*乘数*MM
						assetsBack = assetsFront - (2 * price * takerFee - (price - indexPrices[contractId]))
								* Math.abs(size) * ratios[contractId];
						occupancyBondBack = occupancyBondFront - occupancyBondBuy + occupancyBondSell
								+ Math.abs(orderValue) * MM;
						// 对比判断能否下单
						check(assetsBack, occupancyBondBack);

						// 6-委托量=(可用权益-维持保证金(账户)+该币种多仓维持保证金-该币种空仓维持保证金)/((2*委托价*taker-（委托价-index）+委托价*MM)*乘数)
						sizeMax = (assetsFront - occupancyBondFront + occupancyBondBuy - occupancyBondSell)
								/ ((2 * price * takerFee - (price - indexPrices[contractId]) + price * MM)
										* ratios[contractId]);
						testDetail.append("6-限价卖-反向-仓位<委托量\r\n最大可开手数:" + df.format(sizeMax));
					}
				}
			}

			// 市价单
		} else {
			// 市价买单
			if (size > 0) {
				// 委托价值赋值
				orderValue = sellFirst * size * ratios[contractId];
				orderPrice = sellFirst; // 市价买单,委托价格即卖一价
				// 净头寸方向和该委托单方向相同
				if (size * positionValueNet >= 0) {
					// 新可用权益=可用权益-2*委托价*委托量*乘数*taker+（index-卖1价）*委托量*乘数
					// 新维持保证金 = 维持保证金+委托价*委托量*乘数*MM
					assetsBack = assetsFront - (2 * sellFirst * takerFee - (indexPrices[contractId] - sellFirst)) * size
							* ratios[contractId];
					occupancyBondBack = occupancyBondFront + orderValue * MM;
					// 对比判断能否下单
					check(assetsBack, occupancyBondBack);
					// 7-委托量=(可用权益-维持保证金)/((委托价*MM+2*卖一价*taker-（index-卖1价）)*乘数)
					sizeMax = (assetsFront - occupancyBondFront)
							/ ((sellFirst * MM + 2 * sellFirst * takerFee - (indexPrices[contractId] - sellFirst))
									* ratios[contractId]);
					testDetail.append("7-市价买-同向\r\n最大可开手数:" + df.format(sizeMax));
				} else { // 净头寸方向和该委托单方向相反
					// 净头寸量大于等于该委托单量
					if (Math.abs(positionValueNet) >= Math.abs(orderValue)) {
						// 新可用权益=可用权益-2*委托价*委托量*乘数*taker+（index-卖1价）*委托量*乘 数
						// 新维持保证金 = 维持保证金
						assetsBack = assetsFront - (2 * sellFirst * takerFee - (indexPrices[contractId] - sellFirst))
								* size * ratios[contractId];
						occupancyBondBack = occupancyBondFront;
						// 对比判断能否下单
						check(assetsBack, occupancyBondBack);

						// 8-委托量=(可用权益-维持保证金)/((2*卖一价*taker-（index-卖1价）)*乘数)
						sizeMax = (assetsFront - occupancyBondFront)
								/ ((2 * sellFirst * takerFee - (indexPrices[contractId] - sellFirst))
										* ratios[contractId]);
						testDetail.append("8-市价买-仓位反向-仓位>委托量\r\n最大可开手数:" + df.format(sizeMax));
					} else { // 净头寸量小于该委托单量
						// 新可用权益=可用权益-2*委托价*委托量*乘数*taker+（index-卖1价）*委托量*乘数
						// 新维持保证金=维持保证金(账户)-该币种空仓维持保证金+该币种多仓维持保证金+卖一价*委托量*乘数*MM
						assetsBack = assetsFront - (2 * sellFirst * takerFee - (indexPrices[contractId] - sellFirst))
								* size * ratios[contractId];
						occupancyBondBack = occupancyBondFront - occupancyBondSell + occupancyBondBuy
								+ Math.abs(orderValue) * MM;
						// 对比判断能否下单
						check(assetsBack, occupancyBondBack);
						// 9-委托量=(可用权益-维持保证金(账户)+该币种空仓维持保证金-该币种多仓维持保证金)/((2*卖1价*taker-(index-卖1价)+卖一价*MM)*乘数）
						sizeMax = (assetsFront - occupancyBondFront + occupancyBondSell - occupancyBondBuy)
								/ ((2 * sellFirst * takerFee - (indexPrices[contractId] - sellFirst) + sellFirst * MM)
										* ratios[contractId]);
						testDetail.append("9-市价买-反向-仓位<委托量\r\n最大可开手数:" + df.format(sizeMax));
					}
				}

			} else { // 市价卖单 size<0
				// 委托价值赋值
				orderValue = buyFirst * size * ratios[contractId];
				orderPrice = buyFirst; // 市价卖单,委托价格即买一价
				// 净头寸方向和该委托单方向相同
				if (size * positionValueNet >= 0) {
					// 新可用权益=可用权益-2*委托价*委托量*乘数*taker+（买1价-index）*委托量*乘数
					// 新维持保证金 = 维持保证金+委托价*委托量*乘数*MM
					assetsBack = assetsFront - (2 * buyFirst * takerFee - (buyFirst - indexPrices[contractId]))
							* Math.abs(size) * ratios[contractId];
					occupancyBondBack = occupancyBondFront + Math.abs(orderValue) * MM;
					// 对比判断能否下单
					check(assetsBack, occupancyBondBack);
					// 10-委托量=(可用权益-维持保证金)/((2*买一价*taker-（买1价-index）+买一价*MM)*乘数)
					sizeMax = (assetsFront - occupancyBondFront)
							/ ((2 * buyFirst * takerFee - (buyFirst - indexPrices[contractId]) + buyFirst * MM)
									* ratios[contractId]);
					testDetail.append("10-市价卖-同向\r\n最大可开手数:" + df.format(sizeMax));
				} else { // 净头寸方向和该委托单方向相反
					// 净头寸量大于等于该委托单量
					if (Math.abs(positionValueNet) >= Math.abs(orderValue)) {
						// 新可用权益=可用权益-2*委托价*委托量*乘数*taker+（买1价-index）*委托量*乘数
						// 新维持保证金 = 维持保证金
						assetsBack = assetsFront - (2 * buyFirst * takerFee - (buyFirst - indexPrices[contractId]))
								* Math.abs(size) * ratios[contractId];
						occupancyBondBack = occupancyBondFront;
						// 对比判断能否下单
						check(assetsBack, occupancyBondBack);
						// 11-委托量=(可用权益-维持保证金)/((2*买一价*taker-（买1价-index）)*乘数)
						sizeMax = (assetsFront - occupancyBondFront)
								/ ((2 * buyFirst * takerFee - (buyFirst - indexPrices[contractId]))
										* ratios[contractId]);
						testDetail.append("11-市价卖-反向-仓位>委托量\r\n最大可开手数:" + df.format(sizeMax));
					} else { // 净头寸量小于该委托单量
						// 新可用权益=可用权益-2*委托价*委托量*乘数*taker+（买1价-index）*委托量*乘 数
						// 新维持保证金=维持保证金(账户)-该币种多仓维持保证金+该币种空仓维持保证金+买一价*委托量*乘数*MM
						assetsBack = assetsFront - (2 * buyFirst * takerFee - (buyFirst - indexPrices[contractId]))
								* Math.abs(size) * ratios[contractId];
						occupancyBondBack = occupancyBondFront - occupancyBondBuy + occupancyBondSell
								+ Math.abs(orderValue) * MM;
						// 对比判断能否下单
						check(assetsBack, occupancyBondBack);
						// 12-委托量=(可用权益-维持保证金(账户)+该币种多仓维持保证金-该币种空仓维持保证金)/((2*买一价*taker-（买1价-index）+买一价*MM)*乘数)s
						sizeMax = (assetsFront - occupancyBondFront + occupancyBondBuy - occupancyBondSell)
								/ ((2 * buyFirst * takerFee - (buyFirst - indexPrices[contractId]) + buyFirst * MM)
										* ratios[contractId]);
						testDetail.append("12-市价卖-反向-仓位<委托量\r\n最大可开手数:" + df.format(sizeMax));
					}
				}
			}
		}

		System.out.println("-------极限开仓量-------");
		System.out.println(testDetail + "\r\n");

		System.out.println("-------辅助信息-------");
		System.out.println("账户当前维持保证金:" + df.format(occupancyBondFront));
		System.out.println("账户当前风险度:" + riskDegree);
		System.out.println("账户可用权益:" + assetsFront + "\r\n");
		System.out.println("标记价格:" + indexPrices[contractId]);
		System.out.println("委托价格:" + orderPrice);
		System.out.println("委托手数:" + size);
		System.out.println("委托价值:" + df.format(orderValue));
		System.out.println("仓位净值:" + positionValueNet);
		System.out.println("多仓仓位价值:" + (averagePriceBuy * positionSizeBuy * ratios[contractId]));
		System.out.println("空仓仓位价值:" + (averagePriceSell * positionSizeSell * ratios[contractId]));
	}

	public static void check(double assetsBack, double occupancyBondBack) {
		// 对比判断能否下单
		if (assetsBack <= occupancyBondBack) {
			System.out.println("可用权益不足,限制下单!!!\r\n");
		} else {
			System.out.println("可用权益充足,可以下单\r\n");
		}
		System.out.println("新可用权益:" + df.format(assetsBack) + "\r\n" + "新仓位保证金:" + df.format(occupancyBondBack));
		System.out.println("风险度:" + df.format((occupancyBondBack / assetsBack * 100)) + "100%");
	}
}
