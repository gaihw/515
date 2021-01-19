package backups;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import config.ConfigCenter;
import utils.GetInput;
import utils.ResultLog;

/**
 * 
 * @author Muguozheng
 * @date 2018年10月24日
 * @version 2.0
 * @description 与仓位有关的指数计算，支持多币种双向开仓
 */
public class RiskDegree_back {

	static StringBuffer testDetail = new StringBuffer();
	static StringBuffer testReport = new StringBuffer();
	static StringBuffer testInput = new StringBuffer();

	public static void main(String[] args) throws FileNotFoundException, IOException {
		long start = System.currentTimeMillis();
		// 加载配置
		// { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 }
		Properties prop = ConfigCenter.dataParser();
		// 各币种的标记价格,如果标记价格有变化,需及时更新,"-1"占位用
		double[] indexPrices = { -1, 6444, 5.035, 500, 200, 100, 0.4, 9.8 };
		double taker = 0.0006; // 主动成交手续费
		double maker = 0.0003; // 被动成交手续费
		String[] tableNames = { "ERROR", "BTC", "EOS", "BCH", "ETH", "LTC", "XRP", "ETC" };

		// 赋值区
		double initialAssets = 350; // 初始资金
		int[] contractIds = { 4 }; // 1-7分别为BTC、EOS、BCH、ETH、LTC、XRP、ETC
		double[][] prices = { { 210, 205, 220, 190 } }; // 开仓价
		double[][] sizes = { { 60, 80, -100, 120 } }; // 手数，负数为空
		int[][] leverages = { { 100, 100, 100, 100 } }; // 杠杆
		double[][] feeTypes = { { taker, taker, taker, taker } }; // taker:主动成交,maker:被动成交手续费

		// 执行区
		StringBuffer testResult = RiskDegree_back.getPosition(tableNames, initialAssets, contractIds, indexPrices,
				prices, sizes, leverages, feeTypes);

		// 记录区
		String resultFile = prop.getProperty("resultFile");
		String allTestResult = prop.getProperty("allTestResult");

		ResultLog.writeResult(testResult, resultFile, "reWrite");
		ResultLog.writeResult(testResult, allTestResult, "Write");
		long end = System.currentTimeMillis();
		System.out.println("测试耗时:" + (end - start) + "ms");
	}

	public static StringBuffer getPosition(String[] tableNames, double initialAssets, int[] contractIds,
			double[] indexPrices, double[][] prices, double[][] sizes, int[][] leverages, double[][] feeTypes)
					throws FileNotFoundException, IOException {
		// 记录开始时间
		long startTime = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 可以方便地修改日期格式

		// 基本参数配置区
		// 各个币种合约乘数 依次为:BTC、EOS、BCH、ETH、LTC、XRP、ETC
		// { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 };
		double[] ratios = ConfigCenter.getRatios();
		double MM = 0.005; // 维持保证金率

		// 计算区
		double holdAll = 0; // 完全未成交委托单冻结保证金之和
		double buyPrice = 4.5; // 买一价
		double sellPrice = 4.8; // 卖一价

		double currentAssets = 0; // 当前资产
		double balance = 0;
		double available = 0; // 可用

		double positionSizeBuy = 0; // 多仓仓位总量
		double positionSizeSell = 0; // 空仓仓位总量

		double positionValueBuy = 0; // 多仓仓位总价值
		double positionValueSell = 0; // 多仓仓位总价值
		double netPosition = 0; // 仓位净值
		double netPositionAll = 0; // 所有币种的仓位净值
		double realPosition = 0; // 多空仓较大值
		double realPositionAll = 0; // 所有币种多空仓较大值之和

		double averagePriceBuy = 0; // 多仓持仓均价
		double averagePriceSell = 0; // 空仓持仓均价

		double actualLeverageBuy = 0; // 多仓实际杠杆
		double actualLeverageSell = 0; // 空仓实际杠杆

		double occupancyBondAll = 0; // 占用保证金总合
		double occupancyBondBuy = 0; // 空仓占用保证金
		double occupancyBondSell = 0; // 空仓占用保证金
		double occupancyBondNet = 0; // 净仓位保证金,取多与空的较大值
		double occupancyBondNetAll = 0; // 所有币种的净仓位保证金

		double unrealizedBuy = 0; // 多仓未实现盈亏
		double unrealizedSell = 0; // 空仓未实现盈亏
		double unrealizedBuyRate = 0; // 多仓未实现盈亏率
		double unrealizedSellRate = 0; // 空仓未实现盈亏率
		double unrealized = 0; // 单币种全部仓位未实现盈亏
		double unrealizedAll = 0; // 全部币种的未实现盈亏

		double openFeeBuy = 0; // 多仓开仓手续费
		double openFeeSell = 0; // 空仓开仓手续费

		double closeFeeBuy = 0; // 多仓平仓手续费
		double closeFeeSell = 0; // 空仓平仓手续费
		double feeAll = 0; // 总的手续费
		double riskDegree = 0; // 风险度

		// 格式化
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.######");
		// 运行次数
		int testNum = Integer.valueOf((String) ConfigCenter.executeParser().get("testNum"));

		for (int i = 0; i < contractIds.length; i++) {
			// 计算开仓总量
			double temp1 = 0;
			double temp2 = 0;
			int buyNum = 0; // 多仓数量
			int sellNum = 0; // 空仓数量
			for (int j = 0; j < prices[i].length; j++) {
				if (sizes[i][j] > 0) { // 多仓信息
					// 参数合法性校驗
					if (leverages[i][j] <= 0 || contractIds[i] <= 0 || contractIds[i] > 7 || ratios[contractIds[i]] < 0
							|| indexPrices[contractIds[i]] < 0) {
						System.out.println("价格、手数、杠杆、contractId参数不符合规则,请检验");
						return null;
					}
					// 开仓单，price负数代表未成交的委托单
					if (prices[i][j] > 0) {
						// 仓位总量
						positionSizeBuy = positionSizeBuy + sizes[i][j] * ratios[contractIds[i]];
						// 仓位总价值
						positionValueBuy = positionValueBuy + prices[i][j] * sizes[i][j] * ratios[contractIds[i]];
						// 占用保证金
						occupancyBondBuy = occupancyBondBuy
								+ (prices[i][j] / leverages[i][j]) * sizes[i][j] * ratios[contractIds[i]];
						// 实际持仓杠杆
						temp1 = temp1 + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] / leverages[i][j];
						// 多仓手续费
						openFeeBuy = openFeeBuy + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] * feeTypes[i][j];
						// 平仓手续费 --冻结,固定取taker
						closeFeeBuy = closeFeeBuy + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] * 0.0006;
						// 多仓未实现盈亏
						unrealizedBuy = unrealizedBuy + (indexPrices[contractIds[i]] - prices[i][j]) * sizes[i][j] * 1
								* ratios[contractIds[i]];
						buyNum++;
					} else {
						// 完全未成交委托单冻结保证金
						holdAll = holdAll + (Math.max(buyPrice, sellPrice) / leverages[i][j]
								+ 0.0006 * Math.max(buyPrice, sellPrice) + 0.0006 * Math.max(buyPrice, sellPrice))
								* Math.abs(Math.abs(sizes[i][j])) * ratios[contractIds[i]];
					}
				} else if (sizes[i][j] < 0) { // 空仓信息
					// 参数合法性校驗
					if (leverages[i][j] <= 0 || contractIds[i] <= 0 || contractIds[i] > 7 || ratios[contractIds[i]] < 0
							|| indexPrices[contractIds[i]] < 0) {
						System.out.println("openPrice、size、leverage、ratios、contractId等参数不符合规则,请检验");
						return null;
					}
					if (prices[i][j] > 0) {
						// 仓位总量
						positionSizeSell = positionSizeSell + sizes[i][j] * (-1) * ratios[contractIds[i]];
						// 仓位总价值
						positionValueSell = positionValueSell
								+ prices[i][j] * Math.abs(sizes[i][j]) * ratios[contractIds[i]];
						// 占用保证金
						occupancyBondSell = occupancyBondSell
								+ (prices[i][j] / leverages[i][j]) * sizes[i][j] * (-1) * ratios[contractIds[i]];
						// 实际持仓杠杆
						temp2 = temp2 + prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] / leverages[i][j];
						// 空仓手续费
						openFeeSell = openFeeSell
								+ prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] * feeTypes[i][j];
						// 平仓手续费 --冻结,固定取taker
						closeFeeSell = closeFeeSell
								+ prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] * 0.0006;
						// 空仓未实现盈亏
						unrealizedSell = unrealizedSell
								+ (indexPrices[contractIds[i]] - prices[i][j]) * sizes[i][j] * ratios[contractIds[i]];
						sellNum++;
					} else {
						// 完全未成交委托单冻结保证金
						holdAll = holdAll + (Math.max(sellPrice, buyPrice) / leverages[i][j]
								+ 0.0006 * Math.max(sellPrice, buyPrice) + 0.0006 * Math.max(sellPrice, buyPrice))
								* Math.abs(Math.abs(sizes[i][j])) * ratios[contractIds[i]];
						System.out.println("holdAll----:" + holdAll);
					}
				} else {
					System.out.println("下单方向填写有误,请填写1（多单方向）或者-1（空单方向）");
					return null;
				}
				// 单币种未实现盈亏
				unrealized = unrealizedBuy + unrealizedSell;
			}

			// 总的仓位保证金
			occupancyBondAll = occupancyBondAll + occupancyBondBuy + occupancyBondSell;
			// 仓位净值
			netPosition = Math.abs(positionValueBuy - positionValueSell);
			// 单币种多空仓较大值
			realPosition = getMax(positionValueBuy, positionValueSell);
			// 计算总的手续费
			feeAll = feeAll + openFeeBuy + closeFeeBuy + openFeeSell + closeFeeSell;
			// 计算单币种的实际杠杆
			actualLeverageBuy = positionValueBuy / temp1;
			actualLeverageSell = positionValueSell / temp2;
			// 计算单币种的多仓持仓均价
			averagePriceBuy = positionValueBuy / positionSizeBuy;
			// 计算单币种的空仓持仓均价
			averagePriceSell = positionValueSell / positionSizeSell;
			// 计算多仓的未实现盈亏率
			unrealizedBuyRate = (indexPrices[contractIds[i]] - averagePriceBuy) / averagePriceBuy * actualLeverageBuy;
			// 计算多仓的未实现盈亏率
			unrealizedSellRate = (averagePriceSell - indexPrices[contractIds[i]]) / averagePriceSell
					* actualLeverageSell;
			// 计算所有币种的仓位净值
			netPositionAll = netPositionAll + Math.abs(netPosition);
			// 计算所有币种的多空仓位较大值
			realPositionAll = realPositionAll + realPosition;
			// 计算所有币种的未实现盈亏
			unrealizedAll = unrealizedAll + unrealized;
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
				testDetail.append("多仓仓位价值:" + df.format(positionValueBuy) + "\r\n");
				testDetail.append("多仓实际杠杆:" + df.format(actualLeverageBuy) + "\r\n");
				testDetail.append("多仓开仓手续费:" + df.format(openFeeBuy) + "\r\n");
				testDetail.append("多仓平仓手续费:" + df.format(closeFeeBuy) + "\r\n");
				testDetail.append("多仓占用保证金:" + df.format(occupancyBondBuy) + "\r\n");
				testDetail.append("多仓未实现盈亏:" + df.format(unrealizedBuy) + "\r\n");
				testDetail.append("多仓未实现盈亏率:" + df.format(unrealizedBuyRate * 100) + "%\r\n");
			}

			if (sellNum > 0) {
				testDetail.append("-------空仓仓位信息-------\r\n");
				testDetail.append("空仓持仓均价:" + df.format(averagePriceSell) + "\r\n");
				testDetail.append("空仓开仓总量:" + df.format(positionSizeSell) + "\r\n");
				testDetail.append("空仓仓位价值:" + df.format(positionValueSell) + "\r\n");
				testDetail.append("空仓实际杠杆:" + df.format(actualLeverageSell) + "\r\n");
				testDetail.append("空仓开仓手续费:" + df.format(openFeeSell) + "\r\n");
				testDetail.append("空仓平仓手续费:" + df.format(closeFeeSell) + "\r\n");
				testDetail.append("空仓占用保证金:" + df.format(occupancyBondSell) + "\r\n");
				testDetail.append("空仓未实现盈亏:" + df.format(unrealizedSell) + "\r\n");
				testDetail.append("空仓未实现盈亏率:" + df.format(unrealizedSellRate * 100) + "%\r\n");
			}

			testDetail.append("======================\r\n\r\n");

			// 将数据归零
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
			unrealized = 0;
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

		testReport.append("★★★★★★第" + testNum + "次测试报告★★★★★★\r\n");
		testReport.append("测试开始时间:" + dateFormat.format(startTime) + "\r\n");
		testReport.append("测试执行人:" + ConfigCenter.executeProperties.get("testExecutor") + "\r\n\r\n");

		// 计算当前资产
		currentAssets = initialAssets + unrealizedAll - feeAll;
		// 钱包余额
		balance = currentAssets - unrealizedAll - occupancyBondNetAll;
		// 可用
		available = currentAssets - occupancyBondNetAll - holdAll;
		// 计算风险度
		riskDegree = realPositionAll * MM / (currentAssets - holdAll);

		testReport.append("☆☆☆☆☆☆☆☆账户信息☆☆☆☆☆☆☆☆\r\n");
		testReport.append("初始资金:" + df.format(initialAssets) + "\r\n");
		testReport.append("总手续费:" + df.format(feeAll) + "\r\n");
		testReport.append("当前资产:" + df.format(currentAssets) + "\r\n");
		testReport.append("balance:" + df.format(balance) + "\r\n");
		testReport.append("可用资产:" + df.format(available) + "\r\n");
		testReport.append("冻结保证金:" + df.format(holdAll) + "\r\n");
		testReport.append("账户仓位裸露头寸:" + df.format(netPositionAll) + "\r\n");
		testReport.append("账户多空较大仓位值:" + df.format(realPositionAll) + "\r\n");
		testReport.append("账户占用保证金:" + df.format(occupancyBondNetAll) + "\r\n");
		testReport.append("盈亏总计:" + df.format(unrealizedAll) + "\r\n");
		testReport.append("风险度:" + df.format(riskDegree * 100) + "%\r\n");
		testReport.append("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆\r\n\r\n");

		// 修改运行次数
		testNum++;
		ResultLog.modifyFile(ConfigCenter.executeProFile, "testNum", String.valueOf(testNum));

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
