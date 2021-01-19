package service.business;

import config.ConfigCenter;

/**
 * 
 * @author Muguozheng
 * @date 2018年11月3日
 * @version
 * @description 可开手数和保证金计算
 */
public class Open {
	// 各币种的标记价格,如果标记价格有变化,需及时更新,"-1"占位用
	static double[] indexPrices = { -1, 6400, 2.3584, 100, 200, 100, 0.4, 9.8 };

	public static void main(String[] args) {
		// 赋值区
		double available = 20000; // 当前可用资产
		int contractId = 3; // 合约ID,1-7分别为BTC、EOS、BCH、ETH、LTC、XRP、ETC
		double price = 90; // 委托价格
		double size = 1000; // 委托数量
		int leverage = 100; // 杠杆
		// 执行区
		getOpen(available, contractId, price, size, leverage);
	}

	public static void getOpen(double available, int contractId, double price, double size, int leverage) {
		// 合约名称
		String[] tableName = { "ERROR", "BTC", "EOS", "ETH", "LTC", "XRP", "ETC" };
		// 各个币种合约乘数 依次为:BTC、EOS、BCH、ETH、LTC、XRP、ETC
		// { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 };
		double[] ratios = ConfigCenter.getRatios();
		// double[] ratios = { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 };

		// 可开手数= （可用资产*资金比例）/【（委托价格/杠杆）+（委托价格*2taker）】
		double openSize = available
				/ (price / leverage * ratios[contractId] + price * 2 * 0.00015 * ratios[contractId]);
		double openSizeMarket = available / (indexPrices[contractId] * (1 + 0.03) / leverage * ratios[contractId]
				+ indexPrices[contractId] * (1 + 0.03) * 2 * 0.00015 * ratios[contractId]);

		// 前端展示的占用保证金,不包括手续费
		double occupancyBond = price / leverage * size * ratios[contractId];
		double occupancyBondMarket = indexPrices[contractId] * (1 + 0.03) / leverage * size * ratios[contractId];

		System.out.println(tableName[contractId] + "限价可开手数:" + openSize);
		System.out.println(tableName[contractId] + "市价可开手数:" + openSizeMarket);
		System.out.println(tableName[contractId] + "限价占用保证金:" + occupancyBond);
		System.out.println(tableName[contractId] + "市价占用保证金:" + occupancyBondMarket);
	}
}
