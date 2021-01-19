package service.business;

import config.ConfigCenter;

/**
 * 
 * @author Muguozheng
 * @date 2018年10月25日
 * @version
 * @description 委托单冻结保证金计算
 */
public class OrdersFuture {

	public static void main(String[] args) {
		// 赋值区
		double initialAssets = 1000; // 当前资金
		int contractId = 3; // 合约ID
		double sellPrice = 500; // 卖一价格
		double[] price = { 500, 495 }; // 单笔委托价格
		double[] size = { 100, 200 }; // 单笔委托数量
		double[] leverage = { 100, 100 }; // 杠杆

		// 执行区
		frozenDeposit(initialAssets, contractId, sellPrice, price, size, leverage);
	}

	public static void frozenDeposit(double initialAssets, int contractId, double sellPrice, double[] price,
			double[] size, double[] leverage) {
		StringBuffer testDetail = new StringBuffer();
		// 各个币种合约乘数 依次为:BTC、EOS、BCH、ETH、LTC、XRP、ETC
		// { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 };
		double[] ratios = ConfigCenter.getRatios();
		double frozenDeposit = 0; // 多单冻结保证金
		double balance = 0; // 余额

		// 合约名称
		String[] tableName = { "ERROR", "BTC", "EOS", "BCH", "ETH", "LTC", "XRP", "ETC" };

		// 格式化
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.########");

		for (int i = 0; i < price.length; i++) {

			testDetail.append("★★★第" + (i + 1) + "次委托单冻结★★★\r\n");
			testDetail.append("当前资产:" + initialAssets + "\r\n");
			// 多单冻结保证金
			frozenDeposit = (sellPrice / leverage[i] + 0.0006 * sellPrice + 0.0006 * sellPrice) * Math.abs(size[i])
					* ratios[contractId];

			balance = initialAssets - frozenDeposit;
			initialAssets = balance;

			testDetail.append("可用资产:" + df.format(balance) + "\r\n");
			testDetail.append("冻结保证金:" + df.format(frozenDeposit) + "\r\n");

			testDetail.append("----委托单条件----\r\n");
			testDetail.append("合约名称:" + tableName[contractId] + "\r\n");
			testDetail.append("卖一价格:" + sellPrice + "\r\n");
			testDetail.append("委托价格:" + price[i] + "\r\n");
			testDetail.append("委托数量:" + size[i] + "\r\n");
			testDetail.append("杠杆:" + leverage[i] + "\r\n");
			testDetail.append("================\r\n");
		}
		System.out.println(testDetail);
	}
}
