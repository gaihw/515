package service.business;

import config.ConfigCenter;

/**
 * 
 * @author Muguozheng
 * @date 2018年11月6日
 * @version
 * @description USDT合约计算
 */
public class Calculator {
	// 合约币种
	static final String[] tableNames = { "ERROR", "BTC", "EOS", "BCH", "ETH", "LTC", "XRP", "ETC" };
	// 各个币种合约乘数 依次为:BTC、EOS、BCH、ETH、LTC、XRP、ETC
	// { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 };
	static double[] ratios = ConfigCenter.getRatios();
	// 格式化
	static java.text.DecimalFormat df = new java.text.DecimalFormat("#.####");

	public static void main(String[] args) {
		// 赋值区
		int contractId = 1; // 合约ID,1-7分别为BTC、EOS、BCH、ETH、LTC、XRP、ETC
		int leverage = 100; // 杠杆
		double openPrice = 6000; // 开仓价格
		double closePrice = 9000; // 平仓价格
		double size = 100; // 开仓数量
		// 收益计算
		earnings(contractId, leverage, openPrice, closePrice, size);

		// 赋值区
		int contactId2 = 1; // 合约ID
		double openPrice2 = 6000; // 开仓价格
		double size2 = 100; // 开仓数量
		int leverage2 = 100; // 杠杆
		double targetProfit = 100; // 目标收益
		double targetRate = 47.17; // 目标收益率,单位为%
		// 目标价计算
		priceTarget(contactId2, openPrice2, size2, leverage2, targetProfit, targetRate);
	}

	// 收益计算
	public static void earnings(int contractId, int leverage, double openPrice, double closePrice, double size) {
		StringBuffer testDetail = new StringBuffer();

		double taker = 0.0006; // taker费率
		String type = null; // 仓位类型
		// 参数校验
		if (openPrice <= 0 || closePrice <= 0) {
			System.out.println("请输入合理的开仓价格和平仓价格！");
			return;
		} else if (leverage <= 0 || leverage > 100) {
			System.out.println("杠杆请输入2、3、5、10、20、33、50、100！");
			return;
		}
		// 计算区
		double occupancyBond = openPrice / leverage * Math.abs(size) * ratios[contractId]
				+ openPrice * Math.abs(size) * ratios[contractId] * taker;
		// （平仓价格-开仓价格）*开仓数量
		double earnings = (closePrice - openPrice) * Math.abs(size) * ratios[contractId];
		// 收益率:收益/保证金/杠杆*100%
		double earningRate = earnings / occupancyBond / leverage;
		if (size > 0) {
			testDetail.append("★★★★★★" + tableNames[contractId] + "多仓收益计算★★★★★★★★\r\n");
			type = "多";
		} else if (size < 0) {
			testDetail.append("★★★★★★" + tableNames[contractId] + "空仓收益计算★★★★★★\r\n");
			type = "空";
		} else {
			System.out.println("开仓数量不能为零");
		}

		testDetail.append("====计算结果====\r\n");
		testDetail.append("保证金:" + occupancyBond + "\r\n");
		testDetail.append("收益:" + earnings + "\r\n");
		testDetail.append("收益率:" + df.format(earningRate * 100) + "%\r\n");

		testDetail.append("====计算条件====").append("\r\n");
		testDetail.append("合约:" + tableNames[contractId] + "USD\r\n");
		testDetail.append("开仓类型:" + type + "\r\n");
		testDetail.append("杠杆:" + leverage + "\r\n");
		testDetail.append("开仓价格:" + openPrice + "\r\n");
		testDetail.append("平仓价格:" + closePrice + "\r\n");
		testDetail.append("开仓数量:" + Math.abs(size) + "\r\n");
		testDetail.append("======================\r\n");
		System.out.println(testDetail);
	}

	// 目标价计算
	public static void priceTarget(int contactId, double openPrice, double size, int leverage, double targetProfit,
			double targetRate) {
		StringBuffer testDetail = new StringBuffer();

		// 计算区
		double closePrice = 0; // 预期平仓价
		String type = null;
		double taker = 0.0006; // taker费率
		double occupancyBond = openPrice / leverage * Math.abs(size) * ratios[contactId]
				+ openPrice * Math.abs(size) * ratios[contactId] * taker;
		System.out.println(occupancyBond);

		testDetail.append("★★★★★★★★目标价计算★★★★★★★\r\n");

		// earningRate*occupyBond/(size*ratios)+open=close
		if (size > 0) {
			closePrice = targetRate / 100 * leverage * occupancyBond / (size * ratios[contactId]) + openPrice;
			type = "多";
		} else if (size < 0) {
			closePrice = openPrice - targetRate / 100 * occupancyBond * leverage / (size * ratios[contactId]);
			type = "空";
		} else {
			System.out.println("请输入合理的开仓数量");
		}
		testDetail.append("====计算结果====\r\n");
		testDetail.append("平仓价格(目标收益):" + (targetProfit / (size * ratios[contactId]) + openPrice) + "\r\n");
		testDetail.append("平仓价格(目标收益率):" + closePrice + "\r\n\r\n");

		testDetail.append("====计算条件====\r\n");
		testDetail.append("合约:" + tableNames[contactId] + "USD\r\n");
		testDetail.append("开仓类型:" + type + "\r\n");
		testDetail.append("开仓价格:" + openPrice + "\r\n");
		testDetail.append("开仓数量:" + Math.abs(size) + "\r\n");
		testDetail.append("目标收益:" + targetProfit + "\r\n");
		testDetail.append("目标收益率:" + targetRate + "%" + "\r\n");
		testDetail.append("======================\r\n");
		System.out.println(testDetail);
	}
}
