package backups;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;

public class JsonTest {
	public static void main(String[] args) {
		String test = "{\"indexPrices\" : [3000, 2, 100, 12, 0.4, 4, 12],\"sellPrices\" : [3100, 2.1, 105, 13, 0.44, 4.3, 13],\"initialAssets\" : 100,\"orders\" : [{\"contractId\" : 1,\"prices\" : [3000, 3200],\"sizes\" : [30, 40],\"leverages\" : [100, 200],\"feeRates\" : [0.0003, 0.0003]}, {\"contractId\" : 2,\"prices\" : [3000, 3200],\"sizes\" : [30, 40],\"leverages\" : [100, 200],\"feeRates\" : [0.0003, 0.0003]}]}";
		
		JSONObject parse = JSON.parseObject(test);
		System.out.println(getPosition(parse));
	}

	public static String getPosition(JSONObject object) {
		StringBuffer testDetail = new StringBuffer();
		StringBuffer testReport = new StringBuffer();
		StringBuffer testInput = new StringBuffer();

		JSONObject o = JSON.parseObject(object.toString());
		JSONArray indexPrices = o.getJSONArray("indexPrices");
		JSONArray sellPrices = o.getJSONArray("sellPrices");
		double initialAssets = o.getDouble("initialAssets");
		JSONArray orders = o.getJSONArray("orders");

		System.out.println(orders);

		String[] ratios = { "-1", "0.002", "2" };
		String[] tableNames = { "ERROR", "BTC", "EOS" };
		// 记录开始时间
		long startTime = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 可以方便地修改日期格式

		// 基本参数配置区
		// 各个币种合约乘数 依次为:BTC、EOS、ETH、LTC、XRP、ETC
		// { -1, 0.002, 2, 0.05, 0.2, 40, 1 };

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

		for (int i = 0; i < orders.size(); i++) {
			int contractId = orders.getJSONObject(i).getInteger("contractId");
			JSONArray prices = orders.getJSONObject(i).getJSONArray("prices");
			JSONArray sizes = orders.getJSONObject(i).getJSONArray("sizes");
			JSONArray leverages = orders.getJSONObject(i).getJSONArray("leverages");
			JSONArray feeRates = orders.getJSONObject(i).getJSONArray("feeRates");
			
			System.out.println(prices);
			System.out.println(sizes);
			System.out.println(leverages);
			System.out.println(feeRates);

			// 计算开仓总量
			double temp1 = 0;
			double temp2 = 0;
			int buyNum = 0; // 多仓数量
			int sellNum = 0; // 空仓数量
			for (int j = 0; j < prices.size(); j++) {
				double price = prices.getDouble(j);
				double size = sizes.getDouble(j);
				double leverage = leverages.getDouble(j);
				double feeRate = feeRates.getDouble(j);
				

				System.out.println(price);
				System.out.println(size);
				System.out.println(leverage);
				System.out.println(feeRate);
				
				// // 开仓单,price正数代表开仓
				if (price > 0) {
					if (leverage > 0) {
						// 多仓开仓
						if (size > 0) {
							// 参数合法性校驗
							if (leverage <= 0 || contractId <= 0 || contractId > 7
									|| Double.valueOf(ratios[contractId]) < 0
									|| indexPrices.getDouble(contractId) < 0) {
								System.out.println("价格、手数、杠杆、contractId参数不符合规则,请检验");
								return null;
							}

							// 多仓总仓
							sizeBuy = sizeBuy + size;
							// 仓位总量
							positionSizeBuy = positionSizeBuy + size * Double.valueOf(ratios[contractId]);
							// 仓位总价值
							positionValueBuy = positionValueBuy + price * size * Double.valueOf(ratios[contractId]);
							// 占用保证金
							occupancyBondBuy = occupancyBondBuy
									+ (price / leverage) * size * Double.valueOf(ratios[contractId]);
							// 实际持仓杠杆
							temp1 = temp1 + price * size * Double.valueOf(ratios[contractId]) / leverage;
							// 最新持仓杠杆
							leverageBuy = positionValueBuy / temp1;
							// 最新持仓均价
							averagePriceBuy = positionValueBuy / positionSizeBuy;
							// 多仓手续费
							openFeeBuy = openFeeBuy + price * size * Double.valueOf(ratios[contractId]) * feeRate;
							// 平仓手续费 --冻结,固定取taker
							closeFeeBuy = closeFeeBuy + price * size * Double.valueOf(ratios[contractId]) * 0.0006;
							// 当日手续费
							feeCurrent = feeCurrent + price * size * Double.valueOf(ratios[contractId]) * feeRate;
							// 多仓未实现盈亏
							System.out.println(indexPrices.getDouble(contractId) + "--" + averagePriceBuy + "--"
									+ sizeBuy + "--" + ratios[contractId]);
							unrealizedBuy = (indexPrices.getDouble(contractId) - averagePriceBuy) * sizeBuy
									* Double.valueOf(ratios[contractId]);
							buyNum++;

						} else if (size < 0) {
							// 参数合法性校驗
							if (contractId <= 0 || contractId > 8) {
								System.out.println("openPrice、size、leverage、ratios、contractId等参数不符合规则,请检验");
								return null;
							}
							// 空仓总仓
							sizeSell = sizeSell - size;

							// 仓位总量
							positionSizeSell = positionSizeSell + size * (-1) * Double.valueOf(ratios[contractId]);
							// 仓位总价值
							positionValueSell = positionValueSell
									+ price * Math.abs(size) * Double.valueOf(ratios[contractId]);
							// 占用保证金
							occupancyBondSell = occupancyBondSell
									+ (price / leverage) * size * (-1) * Double.valueOf(ratios[contractId]);
							// 实际持仓杠杆
							temp2 = temp2 + price * Math.abs(size) * Double.valueOf(ratios[contractId]) / leverage;
							// 最新持仓杠杆
							leverageSell = positionValueSell / temp2;
							// 最新持仓均价
							averagePriceSell = positionValueSell / positionSizeSell;
							// 空仓手续费
							openFeeSell = openFeeSell
									+ price * size * (-1) * Double.valueOf(ratios[contractId]) * feeRate;
							// 平仓手续费 --冻结,固定取taker
							closeFeeSell = closeFeeSell
									+ price * size * (-1) * Double.valueOf(ratios[contractId]) * 0.0006;
							// 当日手续费
							feeCurrent = feeCurrent
									+ price * size * (-1) * Double.valueOf(ratios[contractId]) * feeRate;
							// 空仓未实现盈亏
							unrealizedSell = (indexPrices.getDouble(contractId) - averagePriceSell) * sizeSell * (-1)
									* Double.valueOf(ratios[contractId]);
							sellNum++;
						}
					} else {
						// 完全未成交委托单冻结保证金
						hold = hold + (Math.max(price, sellPrices.getDouble(i)) / leverage * (-1)
								+ 0.0006 * Math.max(price, sellPrices.getDouble(i)) * 2) * Math.abs(size)
								* Double.valueOf(ratios[contractId]);
					}
					// price<0代表平仓
				} else if (price < 0) {
					if (size > 0) { // 买入平空
						// 参数校验
						if (leverageSell == 0 || size * Double.valueOf(ratios[contractId]) > positionSizeSell) {
							System.out.println("超过空仓可平数量");
							return null;
						}
						// 空仓未实现盈亏 --返还
						unrealizedSell = unrealizedSell - (averagePriceSell - indexPrices.getDouble(contractId)) * size
								* 1 * Double.valueOf(ratios[contractId]);
						// 空仓已实现盈亏 --追加
						realizedSell = realizedSell
								+ (averagePriceSell - Math.abs(price)) * size * 1 * Double.valueOf(ratios[contractId]);
						// 平仓手续费 --平仓时先返还冻结的平仓佣金,再按实际成交扣除平仓手续费
						closeFeeSell = closeFeeSell - (averagePriceSell * 0.0006 + price * feeRate) * size
								* Double.valueOf(ratios[contractId]);
						// 当日手续费
						feeCurrent = feeCurrent + price * size * (-1) * Double.valueOf(ratios[contractId]) * feeRate;
						// 仓位总仓 减少
						sizeSell = sizeSell - size;
						// 仓位总量 新仓位数量 = 原仓位数量 - 平仓部分数量
						positionSizeSell = positionSizeSell - size * Double.valueOf(ratios[contractId]);
						// 仓位总价值 新仓位总价值 = 持仓均价*仓位数量
						positionValueSell = averagePriceSell * positionSizeSell;
						// 占用保证金 新占用保证金 = 原占用保证金 - 平仓部分占用保证金
						occupancyBondSell = (averagePriceSell / leverageSell) * positionSizeSell;

					} else if (size < 0) { // 卖出平多
						// 参数校验
						if (leverageBuy == 0
								|| Math.abs(size) * Double.valueOf(ratios[contractId]) > Math.abs(positionSizeBuy)) {
							System.out.println("超过多仓可平数量");
							return null;
						}
						// 平仓手续费 --平仓时先返还冻结的平仓佣金,再按实际成交扣除平仓手续费
						closeFeeBuy = closeFeeBuy - (averagePriceBuy * 0.0006 + price * feeRate) * (-1) * size
								* Double.valueOf(ratios[contractId]);
						// 当日手续费
						feeCurrent = feeCurrent + price * size * Double.valueOf(ratios[contractId]) * feeRate;
						// 空仓未实现盈亏 --返还
						unrealizedBuy = unrealizedBuy - (averagePriceBuy - indexPrices.getDouble(contractId)) * size
								* Double.valueOf(ratios[contractId]);
						// 空仓已实现盈亏 --追加
						realizedBuy = realizedBuy + (Math.abs(price) - averagePriceBuy) * Math.abs(size)
								* Double.valueOf(ratios[contractId]);
						// 仓位总量 新仓位数量 = 原仓位数量 - 平仓部分数量
						positionSizeBuy = positionSizeBuy + size * Double.valueOf(ratios[contractId]);
						// 仓位总仓
						sizeBuy = sizeBuy + size;
						// 仓位总价值 新仓位总价值 = 持仓均价*仓位数量
						positionValueBuy = averagePriceBuy * positionSizeBuy;
						// 占用保证金 新占用保证金 = 原占用保证金 - 平仓部分占用保证金
						occupancyBondBuy = (averagePriceBuy / leverageBuy) * positionSizeBuy;
						// 实际持仓杠杆
						temp1 = temp1 + price * size * Double.valueOf(ratios[contractId]) / leverage;
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
			unrealizedBuyRate = (indexPrices.getDouble(contractId) - averagePriceBuy) / averagePriceBuy;
			// 计算多仓的未实现盈亏率
			unrealizedSellRate = (averagePriceSell - indexPrices.getDouble(contractId)) / averagePriceSell;
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

			testDetail.append("=======" + tableNames[contractId] + "仓位信息=======\r\n");
			testDetail.append(tableNames[contractId] + "标记价格:" + indexPrices.getDouble(contractId) + "\r\n");
			testDetail.append("------" + tableNames[contractId] + "总仓位信息" + "------\r\n");
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
		// testInput.append("输入参数如下:\r\n");
		// testInput.append("indexPrices:" + input.getInput(indexPrices) +
		// "\r\n");
		// testInput.append("initialAssets:" + initialAssets + "\r\n");
		// testInput.append("contractIds:" + input.getInput(contractIds) +
		// "\r\n");
		// testInput.append("openPrices:" + input.getInput(prices).toString() +
		// "\r\n");
		// testInput.append("sizes:" + input.getInput(sizes) + "\r\n");
		// testInput.append("leverages:" + input.getInput(leverages) + "\r\n");
		// testInput.append("feeTypes:" + input.getInput(feeTypes) +
		// "\r\n\r\n");

		testReport.append("测试开始时间:" + dateFormat.format(startTime) + "\r\n");

		// 计算当前资产
		currentAssets = initialAssets + unrealizedAll + realizedAll - feeAll;
		// 钱包余额
		balance = currentAssets - unrealizedAll - occupancyBondNetAll;
		// 可用
		available = currentAssets - occupancyBondNetAll - hold;
		// 计算风险度
		riskDegree = realPositionAll * MM / (currentAssets - hold);

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
		return testResult.toString();
	}

	// 计算最大值的方法
	public static double getMax(double a, double b) {
		return a > b ? a : b;
	}
}
