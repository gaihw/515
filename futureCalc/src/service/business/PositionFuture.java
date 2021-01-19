package service.business;

import config.ConfigCenter;

/**
 * 
 * @author Muguozheng
 * @date 2018年10月24日
 * @version
 * @description 单币种爆仓价、穿仓价计算
 */
public class PositionFuture {
	// 各个币种合约乘数 依次为:BTC、EOS、BCH、ETH、LTC、XRP、ETC
	// { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 };
	static double[] ratios = ConfigCenter.getRatios();
	static double MM = 0.005; // 维持保证金率
	static double taker = 0.0006; // 主动成交手续费
	static double maker = 0.0003; // 被动成交手续费

	public static void main(String[] args) {
		double initialAssets = 100; // 初始资金
		double currentAssets = 0; // 当前资产
		int contractId = 2; // 合约id
		double indexPrice = 3.3; // 标记价格
		double[] openPrices = { 3.8 }; // 开仓价格
		double[] sizes = { 30 }; // 开仓数量：手
		int[] leverages = { 33 }; // 杠杆
		double[] sides = { 1 }; // 开仓方向,1为多，-1为空
		double[] feeType = { taker }; // taker:主动成交,maker:被动成交

		double positionSize = 0; // 仓位总量
		double positionValue = 0; // 仓位总价值
		double netPositionValue = 0; // 仓位净值
		double averagePrice = 0; // 持仓均价
		double actualLeverage = 0; // 实际杠杆
		double occupancyBond = 0; // 占用保证金
		double unrealized = 0; // 未实现盈亏
		double openFee = 0; // 开仓手续费
		double closeFee = 0; // 平仓手续费
		double riskDegree = 0; // 风险度
		double explosion = 0; // 爆仓价格
		double bankruptPrice = 0; // 破产价格

		// 格式化
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.######");

		double temp = 0;
		for (int i = 0; i < openPrices.length; i++) {
			// 计算开仓总量
			positionSize = positionSize + sizes[i] * ratios[contractId];
			// 计算仓位价值
			positionValue = positionValue + openPrices[i] * sizes[i] * ratios[contractId];
			// 计算仓位净值
			netPositionValue = netPositionValue + (openPrices[i] * sizes[i] * ratios[contractId]) * sides[i];
			temp = temp + openPrices[i] * sizes[i] * ratios[contractId] / leverages[i];
			// 占用保证金
			occupancyBond = occupancyBond
					+ (openPrices[i] / leverages[i] + openPrices[i] * feeType[i]) * sizes[i] * ratios[contractId];
			// 未实现盈亏
			unrealized = unrealized + (indexPrice - openPrices[i]) * sizes[i] * sides[i] * ratios[contractId];
			// 计算开仓手续费
			openFee = openFee + openPrices[i] * sizes[i] * ratios[contractId] * feeType[i];
			// 计算平仓手续费
			closeFee = closeFee + openPrices[i] * sizes[i] * ratios[contractId] * 0.0006;

		}
		// 实际杠杆
		actualLeverage = positionValue / temp;
		// 持仓均价
		averagePrice = positionValue / positionSize;
		// 计算当前资产
		currentAssets = initialAssets + unrealized - openFee - closeFee;
		// 计算风险度
		riskDegree = positionValue * MM / currentAssets;
		// 计算爆仓价格
		explosion = averagePrice + (positionValue * MM - initialAssets + openFee + closeFee) / positionSize;
		// 计算破产价格
		bankruptPrice = averagePrice - (initialAssets - openFee - closeFee) / positionSize;

		System.out.println("持仓均价:" + df.format(averagePrice));
		System.out.println("开仓总量:" + df.format(positionSize));
		System.out.println("仓位价值:" + df.format(positionValue));
		System.out.println("仓位净值:" + df.format(netPositionValue));
		System.out.println("实际杠杆:" + df.format(actualLeverage));
		System.out.println("占用保证金:" + df.format(occupancyBond));
		System.out.println("未实现盈亏:" + df.format(unrealized));
		System.out.println("开仓手续费:" + df.format(openFee));
		System.out.println("平仓手续费:" + df.format(closeFee));
		System.out.println("当前资产:" + df.format(currentAssets));
		System.out.println("风险度:" + df.format(riskDegree * 100) + "%");
		System.out.println("标记价格:" + df.format(indexPrice));
		System.out.println("爆仓价格:" + df.format(explosion));
		System.out.println("穿仓价格:" + df.format(bankruptPrice));
	}
}
