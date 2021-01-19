package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import service.business.BankruptPrice;
import service.business.Exchange;
import service.business.Open;
import config.ConfigCenter;
import service.business.RiskDegree;
import utils.MysqlConnector;
import utils.ResultLog;

/**
 * 
 * @author Muguozheng
 * @date 2018年10月26日
 * @version
 * @description 执行券商计算
 */
public class TestExecuteFuture {
	static double taker = 0.0006; // 主动成交手续费
	static double maker = 0.0003; // 被动成交手续费
	// 合约列表,"ERROR"占位用
	static String[] tableNames = { "ERROR", "BTC", "EOS", "BCH", "ETH", "LTC", "XRP", "ETC" };

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// openTest(); // 可开计算
		// orderTest(); // 委托单计算
		// positionTest(); // 仓位计算
		// getBankruptPrice(); //穿仓价计算
		getExchange(); // 互换补偿率计算
	}

	// 计算风险度
	public static void positionTest() throws FileNotFoundException, IOException {
		long start = System.currentTimeMillis();
		// 加载配置
		Properties prop = ConfigCenter.dataParser();
		// 各币种的标记价格,如果标记价格有变化,需及时更新,"-1"占位用
		double[] indexPrices = { -1, 6400, 5, 400, 185.5, 100, 0.4, 9.8 };

		// 赋值区
		double initialAssets = 1000; // 初始资金
		int[] contractIds = { 4 }; // 1-7分别为BTC、EOS、BCH、ETH、LTC、XRP、ETC
		double[][] openPrices = { { 222 }, { 400, 398 }, { 201 } }; // 开仓价
		double[][] sizes = { { 500 }, { 500, -200 }, { 1000 } }; // 手数，负数为空
		int[][] leverages = { { 100 }, { 100, 100 }, { 100 } }; // 杠杆
		double[][] feeTypes = { { taker }, { taker, taker }, { taker } }; // taker:主动成交,maker:被动成交
		double[] sellPrices = { 0 };

		// 执行区
		StringBuffer testResult = RiskDegree.getPosition(tableNames, initialAssets, contractIds, indexPrices,
				sellPrices, openPrices, sizes, leverages, feeTypes);

		// 记录区
		String resultFile = prop.getProperty("resultFile");
		String allTestResult = prop.getProperty("allTestResult");

		ResultLog.writeResult(testResult, resultFile, "reWrite");
		ResultLog.writeResult(testResult, allTestResult, "Write");
		long end = System.currentTimeMillis();
		System.out.println("测试耗时:" + (end - start) + "ms");
	}

	// 计算穿仓价
	public static void getBankruptPrice() {
		// 赋值区
		double initialAssets = 2000; // 持仓状态下的初始资金
		int[] contractId = { 1, 2 }; // 持仓合约ID
		double[][] price = { { 6400, 6300 }, { 5, 6 } }; // 持仓均价
		double[][] size = { { 10000, -20000 }, { 10000, -5000 } }; // 持仓量,空仓为负数
		double[] indexPriceFront = { -1, 6400, 6 }; // 爆仓前标记价格
		double[] indexPriceBack = { -1, 6462, 4 }; // 爆仓时标记价格
		//
		BankruptPrice.getBankruptPrice(initialAssets, contractId, price, size, indexPriceFront, indexPriceBack);
	}

	// 计算互换补偿率
	public static void getExchange() throws FileNotFoundException, IOException {
		// 赋值区
		int contractId = 1001;
		double indexPrice = 500;

		// 在配置中心加载配置--数据库连接配置
		Properties dbProperties = ConfigCenter.dataParser();
		String driver = (String) dbProperties.get("driver");
		// 读取swaps配置信息
		String urlSwaps = (String) dbProperties.get("urlSwaps");
		String userNameSwaps = (String) dbProperties.get("userSwaps");
		String passWordSwaps = (String) dbProperties.get("passwordSwaps");

		// 连接数据库
		try {
			MysqlConnector.dbConnect(driver, urlSwaps, userNameSwaps, passWordSwaps);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("数据库连接异常");
		} // 连接数据库

		try {
			Exchange.getExchange(contractId, indexPrice);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// 关闭数据库
		try {
			MysqlConnector.dbClose();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("数据库关闭异常");
		}
	}
}
