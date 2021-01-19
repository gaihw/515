package backups;

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
		double[] price = { 6002, 6008 }; // 单笔委托价格
		double[] size = { 1, 1 }; // 单笔委托数量
		double[] leverage = { 10, 10 }; // 杠杆

		// 执行区
		frozenDeposit(initialAssets, price, size, leverage);
	}

	public static void frozenDeposit(double initialAssets, double[] price, double[] size, double[] leverage) {
		StringBuffer testDetail = new StringBuffer();

		double buyPrice = -5500; // 买一价格
		double sellPrice = 10000000; // 卖一价格
		double frozenDepositSell = 0; // 空单冻结保证金
		double frozenDepositBuy = 0; // 多单冻结保证金
		double balance = 0; // 余额

		// 格式化
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.########");

		for (int i = 0; i < price.length; i++) {

			testDetail.append("★★★★第" + (i + 1) + "次委托单冻结情况★★★★").append("\r\n");
			testDetail.append("初始资金:" + initialAssets).append("\r\n");
			// 多单冻结保证金
			frozenDepositBuy = (Math.min(price[i], sellPrice) / leverage[i] + 0.0006 * Math.min(price[i], sellPrice)
					+ 0.0006 * Math.min(price[i], sellPrice)) * Math.abs(size[i]) * 0.002;
			// 空单冻结保证金
			frozenDepositSell = (Math.max(price[i], buyPrice) / leverage[i] + 0.0006 * Math.max(price[i], buyPrice)
					+ 0.0006 * Math.max(price[i], buyPrice)) * Math.abs(size[i]) * 0.002;

			if (size[i] < 0) {
				balance = initialAssets - frozenDepositSell;
				initialAssets = balance;
			} else {
				balance = initialAssets - frozenDepositBuy;
				initialAssets = balance;
			}

			testDetail.append("开多冻结保证金:" + df.format(frozenDepositBuy)).append("\r\n");
			testDetail.append("开空冻结保证金:" + df.format(frozenDepositSell)).append("\r\n");
			testDetail.append("余额:" + df.format(balance)).append("\r\n");
			testDetail.append("======================").append("\r\n");
		}
		System.out.println(testDetail);
	}
}
