package service.business;

import java.util.ArrayList;

import config.ConfigCenter;
import utils.GetInput;

/**
 * 
 * @author Muguozheng
 * @date 2018年11月2日
 * @version
 * @description 穿仓价计算
 */
public class BankruptPrice {
	public static void main(String[] args) {
		// 赋值区
		double initialAssets = 500; // 持仓状态下的初始资金
//		int[] contractId = { 1, 2 }; // 持仓合约ID
		int[] contractId = { 8}; // 持仓合约ID
//		double[][] price = { { 9000, 9000 }, { 7.1, 7.1 } }; // 持仓均价
		double[][] price = { { 368.7335} }; // 持仓均价
//		double[][] size = { { 500, -1100 }, { 3000, -6200 } }; // 持仓量,空仓为负数
		double[][] size = { { -65}}; // 持仓量,空仓为负数

		// 爆仓前标记价格
		double[] indexPriceFront = { -1, 10531.000, 27.1025, 0.05,2204.9857, 32, 0.4, 9.8 ,368.7335};
		// 爆仓时标记价格
		double[] indexPriceBack = { -1, 10065.45, 22.2461, 0.04568,2075.7994, 32, 0.4, 9.8 ,381.8319};
		// 执行区
		getBankruptPrice(initialAssets, contractId, price, size, indexPriceFront, indexPriceBack);
	}

	public static void getBankruptPrice(double initialAssets, int[] contractId, double[][] price, double[][] size,
			double[] indexPriceFront, double[] indexPriceBack) {
		long start = System.currentTimeMillis();
		StringBuffer testDetail = new StringBuffer();
		testDetail.append("==========穿仓信息==========").append("\r\n");
		// 合约币种
		String[] tableNames = { "ERROR", "BTC", "EOS", "TRX","ETH", "LTC", "XRP", "ETC" ,"BNB"};
		// 各个币种合约乘数 依次为:BTC、EOS、BCH、ETH、LTC、XRP、ETC
		// { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 };
		double[] ratios = ConfigCenter.getRatios();
		double MM = 0.005; // 维持保证金率

		double surplus = 0; // 剩余额度
		double brunkuptPrice = 0; // 单个仓位穿仓价
		ArrayList<Double> weights = new ArrayList<Double>(); // 各个仓位的权重
		ArrayList<Double> positionQtys = new ArrayList<Double>(); // 各个币种的仓位总量
		ArrayList<Double> realizeds = new ArrayList<Double>(); // 各个仓位的盈亏
		ArrayList<Double> positionValues = new ArrayList<Double>(); // 各个仓位的仓位价值
		ArrayList<Double> positionValueNets = new ArrayList<Double>(); // 各个币种的仓位净值(多空取较大者)

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
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.#######");

		// 外循环按币种循环
		for (int i = 0; i < contractId.length; i++) {
			// 内循环为单币种仓位循环
			for (int j = 0; j < size[i].length; j++) {
				// 所有仓位的数量之和
				positionQty = positionQty + Math.abs(size[i][j]);
				// 每个仓位的盈亏,调整标记价格导致
				realized = (indexPriceBack[contractId[i]] - indexPriceFront[contractId[i]]) * size[i][j]
						* ratios[contractId[i]];
				realizeds.add(Double.valueOf(df.format(realized)));
				// 总的盈亏
				realizedAll = realizedAll + realized;
				// 仓位价值
				if (size[i][j] < 0) {
					positionValueSell = price[i][j] * Math.abs(size[i][j]) * ratios[contractId[i]];
					positionValues.add(positionValueSell);
				} else if (size[i][j] > 0) {
					positionValueBuy = price[i][j] * Math.abs(size[i][j]) * ratios[contractId[i]];
					positionValues.add(positionValueBuy);
				} else {
					System.out.println("仓位数量不能为0！");
				}
				positionValueAll = positionValueAll + Math.abs(size[i][j]) * price[i][j] * ratios[contractId[i]];
			}
			positionValueNets.add(Double.valueOf(df.format(Math.max(positionValueBuy, positionValueSell))));
			// 仓位净值
			positionValueNet = positionValueNet + Math.max(positionValueBuy, positionValueSell);
			// 归零，防止本次币种未开某个方向仓位，不能重写该值，导致带入上次的仓位值
			positionValueBuy = 0;
			positionValueSell = 0;
			positionQtys.add(Double.valueOf(df.format(positionQty)));
			positionQty = 0;
		}

		// 计算最终剩余额度
		surplus = initialAssets + realizedAll;
		// 根据最终剩余额度计算风险度
		riskDegree = positionValueNet * MM / surplus;

		// 计算穿仓价
		for (int i = 0; i < contractId.length; i++) {
			for (int j = 0; j < size[i].length; j++) {
				// 计算权重
				weight = Math.abs(size[i][j]) * price[i][j] * ratios[contractId[i]] / positionValueAll;
				weights.add(Double.valueOf(df.format(weight)));
				// 破产价格
				brunkuptPrice = indexPriceBack[contractId[i]] - surplus * weight / (size[i][j] * ratios[contractId[i]]);
				if (size[i][j] > 0) {
					testDetail.append(tableNames[contractId[i]] + "穿仓价(多):" + df.format(brunkuptPrice)).append("\r\n");
				} else {
					testDetail.append(tableNames[contractId[i]] + "穿仓价(空):" + df.format(brunkuptPrice)).append("\r\n");
				}
			}
		}

		testDetail.append("==========账户信息==========").append("\r\n");
		testDetail.append("最终剩余额度:" + df.format(surplus)).append("\r\n");
		testDetail.append("维持保证金:" + df.format(positionValueNet * MM)).append("\r\n");
		testDetail.append("全部仓位净值:" + df.format(positionValueNet)).append("\r\n");
		testDetail.append("风险度:" + df.format(riskDegree * 100) + "%").append("\r\n");
		testDetail.append("==========辅助信息==========").append("\r\n");
		// 记录相关结果
		testDetail.append("全部仓位盈亏变化:" + df.format(realizedAll)).append("\r\n");
		testDetail.append("仓位价值之和:" + df.format(positionValueAll)).append("\r\n");
		testDetail.append("各币种仓位数量:" + positionQtys).append("\r\n");
		testDetail.append("各币种仓位净值:" + positionValueNets).append("\r\n");
		testDetail.append("各仓位仓位值:" + positionValues).append("\r\n");
		testDetail.append("各仓位盈亏变化:" + realizeds).append("\r\n");
		testDetail.append("各仓位权重:" + weights).append("\r\n");
		testDetail.append("各仓位爆仓价:" + GetInput.getInput(indexPriceBack)).append("\r\n");

		System.out.println(testDetail);
		long end = System.currentTimeMillis();
		System.out.println("测试用时:" + (end - start) + "ms");
	}
}
