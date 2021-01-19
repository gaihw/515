package service.business;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import config.ConfigCenter;
import utils.GetInput;

/**
 * 
 * @author Muguozheng
 * @date 2018年11月26日
 * @version 1.0
 * @description 防爆仓检查,能够支持检索当前委托单和平仓逻辑
 */
public class OrderCheck2 {

	static StringBuffer testDetail = new StringBuffer();
	static StringBuffer testReport = new StringBuffer();
	static StringBuffer testInput = new StringBuffer();

	public static void main(String[] args) throws FileNotFoundException, IOException {
		long start = System.currentTimeMillis();

		// 各币种的标记价格,如果标记价格有变化,需及时更新,"-1"占位用
		double[] indexPrices = { -1, 4456, 4, 125, 35, 0.4, 9.8 };
		double taker = 0.0006; // 主动成交手续费
		double maker = 0.0003; // 被动成交手续费
		String[] tableNames = { "ERROR", "BTC", "EOS", "ETH", "LTC", "XRP", "ETC", "DASH" };

		// 赋值区
		double initialAssets = 100; // 初始资金
		int[] contractIds = { 2 }; // 1-7分别为BTC、EOS、ETH、LTC、XRP、ETC、DASH
		double[][] prices = { { 3.5, 3.5 } }; // 开仓价,负数代表平仓
		double[][] sizes = { { -50, -45 } }; // 手数,负数为空
		int[][] leverages = { { 100, 100 } }; // 杠杆,负数代表未成交
		double[][] feeTypes = { { taker, taker } }; // taker:主动成交,maker:被动成交手续费
		double[] sellPrices = { 4.4 }; // 卖一价格,如果不计算冻结保证金,此项可忽略

		// 执行区
		OrderCheck2.getPosition(tableNames, initialAssets, contractIds, indexPrices, sellPrices, prices, sizes,
				leverages, feeTypes);

		long end = System.currentTimeMillis();
		System.out.println("测试用时:" + (end - start) + "ms");
	}

	public static StringBuffer getPosition(String[] tableNames, double initialAssets, int[] contractIds,
			double[] indexPrices, double[] sellPrices, double[][] prices, double[][] sizes, int[][] leverages,
			double[][] feeTypes) throws FileNotFoundException, IOException {
		// 记录开始时间
		long startTime = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 可以方便地修改日期格式

		// 基本参数配置区
		// 各个币种合约乘数 依次为:BTC、EOS、ETH、LTC、XRP、ETC
		// { -1, 0.002, 2, 0.05, 0.2, 40, 1 };
		double[] ratios = ConfigCenter.getRatios();
		double MM = 0.005; // 维持保证金率

		// 计算区
		double currentAssets = 0; // 当前资产
		double balance = 0; // 余额,数据库记录字段
		double available = 0; // 可用

		double sizeBuy = 0; // 多仓总仓
		double sizeSell = 0; // 空仓总仓
		double positionSizeBuy = 0; // 多仓仓位总量
		double positionSizeSell = 0; // 空仓仓位总量

		double positionValueBuy = 0; // 多仓仓位总价值
		double positionValueSell = 0; // 多仓仓位总价值
		double netPosition = 0; // 仓位净值
		double netPositionAll = 0; // 所有币种的仓位净值
		double realPosition = 0; // 多空仓较大值
		double realPositionAll = 0; // 所有币种多空仓较大值之和

		double averagePriceBuy = 0; // 当前多仓持仓均价
		double averagePriceSell = 0; // 当前空仓持仓均价
		double leverageBuy = 0; // 多仓最新实际杠杆
		double leverageSell = 0; // 空仓最新实际杠杆

		double hold = 0; // 冻结保证金
		double occupancyBondAll = 0; // 占用保证金总合
		double occupancyBondBuy = 0; // 空仓占用保证金
		double occupancyBondSell = 0; // 空仓占用保证金
		double occupancyBondNet = 0; // 净仓位保证金,取多与空的较大值
		double occupancyBondNetAll = 0; // 所有币种的净仓位保证金

		double realizedBuy = 0; // 多仓已实现盈亏
		double realizedSell = 0; // 空仓已实现盈亏
		double realized = 0; // 单币种的已实现盈亏
		double realizedAll = 0; // 全部币种的已实现盈亏
		double unrealizedBuy = 0; // 多仓未实现盈亏
		double unrealizedSell = 0; // 空仓未实现盈亏
		double unrealized = 0; // 单币种全部仓位未实现盈亏
		double unrealizedAll = 0; // 全部币种的未实现盈亏
		double unrealizedBuyRate = 0; // 多仓未实现盈亏率
		double unrealizedSellRate = 0; // 空仓未实现盈亏率

		double openFeeBuy = 0; // 多仓开仓手续费
		double openFeeSell = 0; // 空仓开仓手续费
		double closeFeeBuy = 0; // 多仓平仓手续费
		double closeFeeSell = 0; // 空仓平仓手续费
		double feeAll = 0; // 总的手续费
		double feeCurrent = 0; // 当日手续费,实扣的,不含冻结
		double riskDegree = 0; // 风险度

		// 格式化
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.######");

		for (int i = 0; i < contractIds.length; i++) {
			// 计算开仓总量
			double temp1 = 0;
			double temp2 = 0;
			int buyNum = 0; // 多仓数量
			int sellNum = 0; // 空仓数量
			for (int j = 0; j < prices[i].length; j++) {
				// // 开仓单,price正数代表开仓
				if (prices[i][j] > 0) {
					// 多仓开仓
					if (sizes[i][j] > 0) {
						// 参数合法性校驗
						if (leverages[i][j] <= 0 || contractIds[i] <= 0 || contractIds[i] > 7
								|| ratios[contractIds[i]] < 0 || indexPrices[contractIds[i]] < 0) {
							System.out.println("价格、手数、杠杆、contractId参数不符合规则,请检验");
							return null;
						}

						// 多仓总仓
						sizeBuy = sizeBuy + sizes[i][j];
						// 仓位总量
						positionSizeBuy = positionSizeBuy + sizes[i][j] * ratios[contractIds[i]];
						// 仓位总价值
						positionValueBuy = positionValueBuy + prices[i][j] * sizes[i][j] * ratios[contractIds[i]];
						// 占用保证金
						occupancyBondBuy = occupancyBondBuy
								+ (prices[i][j] / leverages[i][j]) * sizes[i][j] * ratios[contractIds[i]];
						// 实际持仓杠杆
						temp1 = temp1 + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] / leverages[i][j];
						// 最新持仓杠杆
						leverageBuy = positionValueBuy / temp1;
						// 最新持仓均价
						averagePriceBuy = positionValueBuy / positionSizeBuy;
						// 多仓手续费
						openFeeBuy = openFeeBuy + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] * feeTypes[i][j];
						// 平仓手续费 --冻结,固定取taker
						closeFeeBuy = closeFeeBuy + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] * 0.0006;
						// 当日手续费
						feeCurrent = feeCurrent + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] * feeTypes[i][j];
						// 多仓未实现盈亏
						unrealizedBuy = (indexPrices[contractIds[i]] - averagePriceBuy) * sizeBuy
								* ratios[contractIds[i]];
						buyNum++;

					} else if (sizes[i][j] < 0) {
						// 参数合法性校驗
						if (leverages[i][j] <= 0 || contractIds[i] <= 0 || contractIds[i] > 7
								|| ratios[contractIds[i]] < 0 || indexPrices[contractIds[i]] < 0) {
							System.out.println("openPrice、size、leverage、ratios、contractId等参数不符合规则,请检验");
							return null;
						}
						// 空仓总仓
						sizeSell = sizeSell - sizes[i][j];

						// 仓位总量
						positionSizeSell = positionSizeSell + sizes[i][j] * (-1) * ratios[contractIds[i]];
						// 仓位总价值
						positionValueSell = positionValueSell
								+ prices[i][j] * Math.abs(sizes[i][j]) * ratios[contractIds[i]];
						// 占用保证金
						occupancyBondSell = occupancyBondSell
								+ (prices[i][j] / leverages[i][j]) * sizes[i][j] * (-1) * ratios[contractIds[i]];
						// 实际持仓杠杆
						temp2 = temp2 + prices[i][j] * Math.abs(sizes[i][j]) * ratios[contractIds[i]] / leverages[i][j];
						// 最新持仓杠杆
						leverageSell = positionValueSell / temp2;
						// 最新持仓均价
						averagePriceSell = positionValueSell / positionSizeSell;
						// 空仓手续费
						openFeeSell = openFeeSell
								+ prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] * feeTypes[i][j];
						// 平仓手续费 --冻结,固定取taker
						closeFeeSell = closeFeeSell
								+ prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] * 0.0006;
						// 当日手续费
						feeCurrent = feeCurrent
								+ prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] * feeTypes[i][j];
						// 空仓未实现盈亏
						unrealizedSell = (indexPrices[contractIds[i]] - averagePriceSell) * sizeSell * (-1)
								* ratios[contractIds[i]];
						sellNum++;
					}
					// price<0代表平仓
				} else if (prices[i][j] < 0) {
					if (sizes[i][j] > 0) { // 买入平空
						// 参数校验
						if (leverageSell == 0 || sizes[i][j] * ratios[contractIds[i]] > positionSizeSell) {
							System.out.println("超过空仓可平数量");
							return null;
						}
						// 空仓未实现盈亏 --返还
						unrealizedSell = unrealizedSell - (averagePriceSell - indexPrices[contractIds[i]]) * sizes[i][j]
								* 1 * ratios[contractIds[i]];
						// 空仓已实现盈亏 --追加
						realizedSell = realizedSell + (averagePriceSell - Math.abs(prices[i][j])) * sizes[i][j] * 1
								* ratios[contractIds[i]];
						// 平仓手续费 --平仓时先返还冻结的平仓佣金,再按实际成交扣除平仓手续费
						closeFeeSell = closeFeeSell - (averagePriceSell * 0.0006 + prices[i][j] * feeTypes[i][j])
								* sizes[i][j] * ratios[contractIds[i]];
						// 当日手续费
						feeCurrent = feeCurrent
								+ prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] * feeTypes[i][j];
						// 仓位总仓 减少
						sizeSell = sizeSell - sizes[i][j];
						// 仓位总量 新仓位数量 = 原仓位数量 - 平仓部分数量
						positionSizeSell = positionSizeSell - sizes[i][j] * ratios[contractIds[i]];
						// 仓位总价值 新仓位总价值 = 持仓均价*仓位数量
						positionValueSell = averagePriceSell * positionSizeSell;
						// 占用保证金 新占用保证金 = 原占用保证金 - 平仓部分占用保证金
						occupancyBondSell = (averagePriceSell / leverageSell) * positionSizeSell;

					} else if (sizes[i][j] < 0) { // 卖出平多
						// 参数校验
						if (leverageBuy == 0
								|| Math.abs(sizes[i][j]) * ratios[contractIds[i]] > Math.abs(positionSizeBuy)) {
							System.out.println("超过多仓可平数量");
							return null;
						}
						// 平仓手续费 --平仓时先返还冻结的平仓佣金,再按实际成交扣除平仓手续费
						closeFeeBuy = closeFeeBuy - (averagePriceBuy * 0.0006 + prices[i][j] * feeTypes[i][j]) * (-1)
								* sizes[i][j] * ratios[contractIds[i]];
						// 当日手续费
						feeCurrent = feeCurrent + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] * feeTypes[i][j];
						// 空仓未实现盈亏 --返还
						unrealizedBuy = unrealizedBuy - (averagePriceBuy - indexPrices[contractIds[i]]) * sizes[i][j]
								* ratios[contractIds[i]];
						// 空仓已实现盈亏 --追加
						realizedBuy = realizedBuy + (Math.abs(prices[i][j]) - averagePriceBuy) * Math.abs(sizes[i][j])
								* ratios[contractIds[i]];
						// 仓位总量 新仓位数量 = 原仓位数量 - 平仓部分数量
						positionSizeBuy = positionSizeBuy + sizes[i][j] * ratios[contractIds[i]];
						// 仓位总仓
						sizeBuy = sizeBuy + sizes[i][j];
						// 仓位总价值 新仓位总价值 = 持仓均价*仓位数量
						positionValueBuy = averagePriceBuy * positionSizeBuy;
						// 占用保证金 新占用保证金 = 原占用保证金 - 平仓部分占用保证金
						occupancyBondBuy = (averagePriceBuy / leverageBuy) * positionSizeBuy;
						// 实际持仓杠杆
						temp1 = temp1 + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] / leverages[i][j];
					}
				}

				// 单币种未实现盈亏
				unrealized = unrealizedBuy + unrealizedSell;
				// 单币种已实现盈亏
				realized = realizedBuy + realizedSell;
			}
			// 总的仓位保证金
			occupancyBondAll = occupancyBondAll + occupancyBondBuy + occupancyBondSell;
			// 仓位净值
			netPosition = Math.abs(positionValueBuy - positionValueSell);
			// 单币种多空仓较大值
			realPosition = getMax(positionValueBuy, positionValueSell);
			// 计算总的手续费
			feeAll = feeAll + openFeeBuy + closeFeeBuy + openFeeSell + closeFeeSell;
			// 计算多仓的未实现盈亏率
			unrealizedBuyRate = (indexPrices[contractIds[i]] - averagePriceBuy) / averagePriceBuy;
			// 计算多仓的未实现盈亏率
			unrealizedSellRate = (averagePriceSell - indexPrices[contractIds[i]]) / averagePriceSell;
			// 计算所有币种的仓位净值
			netPositionAll = netPositionAll + Math.abs(netPosition);
			// 计算所有币种的多空仓位较大值
			realPositionAll = realPositionAll + realPosition;
			// 计算所有币种的未实现盈亏
			unrealizedAll = unrealizedAll + unrealized;
			// 计算所有币种的实现盈亏
			realizedAll = realizedAll + realized;
			// 获取净仓位保证金
			occupancyBondNet = getMax(occupancyBondBuy, occupancyBondSell);
			// 所有币种的净仓位保证金
			occupancyBondNetAll = occupancyBondNetAll + occupancyBondNet;

			testDetail.append("=======" + tableNames[contractIds[i]] + "仓位信息=======\r\n");
			testDetail.append(tableNames[contractIds[i]] + "标记价格:" + indexPrices[contractIds[i]] + "\r\n");

			testDetail.append("------" + tableNames[contractIds[i]] + "总仓位信息" + "------\r\n");
			testDetail.append("币种未实现盈亏:" + df.format(unrealized) + "\r\n");
			testDetail.append("币种仓位裸露头寸:" + df.format(netPosition) + "\r\n");

			testDetail.append("币种占用保证金:" + df.format(occupancyBondNet) + "\r\n");

			if (buyNum > 0) {
				testDetail.append("-------多仓仓位信息-------\r\n");
				testDetail.append("多仓持仓均价:" + df.format(averagePriceBuy) + "\r\n");
				testDetail.append("多仓开仓总量:" + df.format(positionSizeBuy) + "\r\n");
				testDetail.append("多仓总仓:" + sizeBuy + "\r\n");
				testDetail.append("多仓仓位价值:" + df.format(positionValueBuy) + "\r\n");
				testDetail.append("多仓实际杠杆:" + df.format(leverageBuy) + "\r\n");
				testDetail.append("多仓开仓手续费:" + df.format(openFeeBuy) + "\r\n");
				testDetail.append("多仓平仓手续费:" + df.format(closeFeeBuy) + "\r\n");
				testDetail.append("多仓占用保证金:" + df.format(occupancyBondBuy) + "\r\n");
				testDetail.append("多仓未实现盈亏:" + df.format(unrealizedBuy) + "\r\n");
				testDetail.append("多仓已实现盈亏:" + df.format(realizedBuy) + "\r\n");
				testDetail.append("多仓未实现盈亏率:" + df.format(unrealizedBuyRate * 100) + "%\r\n");
			}

			if (sellNum > 0) {
				testDetail.append("-------空仓仓位信息-------\r\n");
				testDetail.append("空仓持仓均价:" + df.format(averagePriceSell) + "\r\n");
				testDetail.append("空仓总仓:" + sizeSell + "\r\n");
				testDetail.append("空仓开仓总量:" + df.format(positionSizeSell) + "\r\n");
				testDetail.append("空仓仓位价值:" + df.format(positionValueSell) + "\r\n");
				testDetail.append("空仓实际杠杆:" + df.format(leverageSell) + "\r\n");
				testDetail.append("空仓开仓手续费:" + df.format(openFeeSell) + "\r\n");
				testDetail.append("空仓平仓手续费:" + df.format(closeFeeSell) + "\r\n");
				testDetail.append("空仓占用保证金:" + df.format(occupancyBondSell) + "\r\n");
				testDetail.append("空仓未实现盈亏:" + df.format(unrealizedSell) + "\r\n");
				testDetail.append("空仓已实现盈亏:" + df.format(realizedSell) + "\r\n");
				testDetail.append("空仓未实现盈亏率:" + df.format(unrealizedSellRate * 100) + "%\r\n");
			}

			testDetail.append("======================\r\n\r\n");

			// 将数据归零
			sizeBuy = 0;
			sizeSell = 0;
			positionSizeBuy = 0;
			positionValueBuy = 0;
			occupancyBondBuy = 0;
			openFeeBuy = 0;
			closeFeeBuy = 0;
			openFeeSell = 0;
			closeFeeSell = 0;
			positionSizeSell = 0;
			positionValueSell = 0;
			occupancyBondSell = 0;
			netPosition = 0;
			realPosition = 0;
			unrealizedBuy = 0;
			unrealizedSell = 0;
			realizedBuy = 0;
			realizedSell = 0;
			unrealized = 0;
			realized = 0;
		}

		// 操作日志记录
		testInput.append("输入参数如下:\r\n");
		testInput.append("indexPrices:" + GetInput.getInput(indexPrices) + "\r\n");
		testInput.append("initialAssets:" + initialAssets + "\r\n");
		testInput.append("contractIds:" + GetInput.getInput(contractIds) + "\r\n");
		testInput.append("openPrices:" + GetInput.getInput(prices).toString() + "\r\n");
		testInput.append("sizes:" + GetInput.getInput(sizes) + "\r\n");
		testInput.append("leverages:" + GetInput.getInput(leverages) + "\r\n");
		testInput.append("feeTypes:" + GetInput.getInput(feeTypes) + "\r\n\r\n");

		testReport.append("★★★★★★★防爆仓测试报告★★★★★★★\r\n");
		testReport.append("测试开始时间:" + dateFormat.format(startTime) + "\r\n");

		// 计算当前资产
		currentAssets = initialAssets + unrealizedAll + realizedAll - feeAll;
		// 钱包余额
		balance = currentAssets - unrealizedAll - occupancyBondNetAll;
		// 可用
		available = currentAssets - occupancyBondNetAll - hold;
		// 计算风险度
		riskDegree = realPositionAll * MM / (currentAssets);

		testReport.append("☆☆☆☆☆☆☆☆账户信息☆☆☆☆☆☆☆☆\r\n");
		testReport.append("风险度:" + df.format(riskDegree * 100) + "%\r\n");
		testReport.append("------我的合约资产------\r\n");
		testReport.append("当前资产:" + df.format(currentAssets) + "\r\n");
		testReport.append("可用资产:" + df.format(available) + "\r\n");
		testReport.append("占用保证金:" + df.format(occupancyBondNetAll) + "\r\n");
		testReport.append("冻结保证金:" + df.format(hold) + "\r\n");
		testReport.append("未实现盈亏:" + df.format(unrealizedAll) + "\r\n");
		testReport.append("当日已实现:" + df.format(realizedAll) + "\r\n");
		testReport.append("当日手续费:" + df.format(feeCurrent) + "\r\n");
		testReport.append("--------------------\r\n");
		testReport.append("初始资金:" + df.format(initialAssets) + "\r\n");
		testReport.append("balance:" + df.format(balance) + "\r\n");
		testReport.append("账户仓位裸露头寸:" + df.format(netPositionAll) + "\r\n");
		testReport.append("账户多空较大仓位值:" + df.format(realPositionAll) + "\r\n");
		testReport.append("维持保证金:" + df.format(realPositionAll * MM) + "\r\n");
		testReport.append("总手续费(含冻结):" + df.format(feeAll) + "\r\n");

		testReport.append("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆\r\n\r\n");

		System.out.print(testReport);
		System.out.print(testDetail);
		StringBuffer testResult = testReport.append(testDetail).append(testInput);
		return testResult;
	}

	// 计算最大值的方法
	public static double getMax(double a, double b) {
		return a > b ? a : b;
	}
}
