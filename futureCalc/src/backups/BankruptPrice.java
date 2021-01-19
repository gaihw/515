package backups;

import java.util.ArrayList;

/**
 * 
 * @author Muguozheng
 * @date 2018年11月2日
 * @version
 * @description 穿仓价计算
 */
public class BankruptPrice {
	public static void main(String[] args) {
		getBankruptPrice();
	}

	public static void getBankruptPrice() {
		StringBuffer testDetail = new StringBuffer();
		testDetail.append("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★").append("\r\n");
		// 合约币种
		String[] tableNames = { "ERROR", "BTC", "EOS", "BCH", "ETH", "LTC", "XRP", "ETC" };
		// 各个币种合约乘数 依次为:BTC、EOS、BCH、ETH、LTC、XRP、ETC
		double[] ratios = { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 };
		double MM = 0.005; // 维持保证金率

		double initialAssets = 2000; // 持仓状态下的初始资金
		int[] contractId = { 1, 2 }; // 持仓合约ID
		double[][] price = { { 6400, 6300 }, { 5, 6 } }; // 持仓均价
		double[][] size = { { 10000, -20000 }, { 10000, -5000 } }; // 持仓量,空仓为负数
		double[] indexPriceFront = { -1, 6400, 6 }; // 爆仓前标记价格
		double[] indexPriceBack = { -1, 6462, 4 }; // 爆仓时标记价格

		double surplus = 0; // 剩余额度
		double brunkuptPrice = 0; // 单个仓位穿仓价
		ArrayList<Double> weights = new ArrayList<Double>(); // 各个仓位的权重
		ArrayList<Double> positionQtys = new ArrayList<Double>(); // 各个币种的仓位总量
		ArrayList<Double> realizeds = new ArrayList<Double>(); // 各个仓位的盈亏

		double positionQty = 0; // 所有仓位数量之和
		double weight = 0; // 权重
		double realized = 0; // 仓位盈亏
		double realizedAll = 0; // 所有仓位盈亏之和
		double positionValueAll = 0; // 所有仓位价值之和
		double positionValueBuy = 0; // 多仓仓位价值
		double positionValueSell = 0; // 空仓仓位价值
		double positionValueNet = 0; // 计算风险度的仓位价值
		double riskDegree = 0; // 风险度

		// 格式化
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.######");

		for (int i = 0; i < contractId.length; i++) {
			// 内循环为单币种仓位循环
			for (int j = 0; j < size[i].length; j++) {
				// 所有仓位的数量之和
				positionQty = positionQty + Math.abs(size[i][j]);
				// 每个仓位的盈亏
				realized = (indexPriceBack[contractId[i]] - indexPriceFront[contractId[i]]) * size[i][j]
						* ratios[contractId[i]];
				realizeds.add(realized);
				// 总的盈亏
				realizedAll = realizedAll + realized;
				// 仓位价值
				if (size[i][j] < 0) {
					positionValueSell = price[i][j] * Math.abs(size[i][j]) * ratios[contractId[i]];
				} else {
					positionValueBuy = price[i][j] * Math.abs(size[i][j]) * ratios[contractId[i]];
				}
			}
			positionValueNet = positionValueNet + Math.max(positionValueBuy, positionValueSell);
			// positionValue为所有币种的仓位价值之和（多空相加）
			positionValueAll = positionValueAll + positionQty * indexPriceBack[contractId[i]] * ratios[contractId[i]];
			positionQtys.add(positionQty);

			positionQty = 0;
			positionValueSell = 0;
		}

		// 计算最终剩余额度
		surplus = initialAssets + realizedAll;
		// 根据最终剩余额度计算风险度
		riskDegree = positionValueNet * MM / surplus;

		// 计算穿仓价
		for (int i = 0; i < contractId.length; i++) {
			for (int j = 0; j < size[i].length; j++) {
				// 计算权重
				weight = Math.abs(size[i][j]) * indexPriceBack[contractId[i]] * ratios[contractId[i]]
						/ positionValueAll;
				weights.add(weight);
				// 破产价格
				brunkuptPrice = indexPriceBack[contractId[i]] - surplus * weight / size[i][j] * ratios[contractId[i]];
				if (size[i][j] > 0) {
					testDetail.append(tableNames[contractId[i]] + "穿仓价(多):" + brunkuptPrice).append("\r\n");
				} else {
					testDetail.append(tableNames[contractId[i]] + "穿仓价(空):" + brunkuptPrice).append("\r\n");
				}
			}
		}
		testDetail.append("================================").append("\r\n");
		// 记录相关结果
		testDetail.append("仓位价值之和:" + positionValueAll).append("\r\n");
		testDetail.append("全部币种仓位数量:" + positionQtys).append("\r\n");
		testDetail.append("全部仓位盈亏:" + realizeds).append("\r\n");
		testDetail.append("全部仓位权重:" + weights).append("\r\n");
		testDetail.append("最终剩余额度:" + surplus).append("\r\n");
		testDetail.append("维持保证金:" + positionValueNet * MM).append("\r\n");
		testDetail.append("全部仓位净值:" + positionValueNet).append("\r\n");
		testDetail.append("风险度:" + df.format(riskDegree * 100) + "%").append("\r\n");

		System.out.println(testDetail);
	}
}
