package service.business;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import config.ConfigCenter;
import utils.MysqlConnector;

/**
 * 
 * @author Muguozheng
 * @date 2018年11月3日
 * @version
 * @description 互换补偿率计算
 */
public class Exchange {
	public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
		// 赋值区
		int contractId = 1002;
		double indexPrice = 5.035;

		long startTime = System.currentTimeMillis();
		// 在配置中心加载配置
		Properties dbProperties = ConfigCenter.dataParser();
		String driver = (String) dbProperties.get("driver");
		// 读取swaps配置信息
		String urlSwaps = (String) dbProperties.get("urlFuture");
		String userNameSwaps = (String) dbProperties.get("userFuture");
		String passWordSwaps = (String) dbProperties.get("passwordFuture");

		// 连接数据库
		MysqlConnector.dbConnect(driver, urlSwaps, userNameSwaps, passWordSwaps); // 连接数据库

		// 执行区
		getExchange(contractId, indexPrice);

		// 关闭数据库
		MysqlConnector.dbClose();
		long endTime = System.currentTimeMillis();
		System.out.println("运行时间:" + (endTime - startTime) + "ms");
	}

	// 互换补偿率
	public static void getExchange(int contractId, double indexPrice) throws SQLException {
		final String dataBaseName = "58future"; // 数据库名称
		double averagePriceBuy = 0; // 深度加权买价
		double averagePriceSell = 0; // 深度加权卖价
		double averagePrice = 0; // 深度加权中间价
		double interestRate = 0.0003; // 利率
		double justBasis = 0; // 合理基差
		double HSAHP = 0; // 溢价指数
		double fundingRate = 0; // 资金费率
		double fundingRateReal = 0; // 真实资金费率
		// 格式化
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.######");

		StringBuffer testDetail = new StringBuffer();

		// 深度加权买价sql语句
		String averagePriceBuySql = "SELECT SUM(price*(size-executed_size))/SUM(size-executed_size) FROM "
				+ dataBaseName + ".tb_future_order WHERE contract_id=" + contractId + " AND side=1;";
		// 深度加权卖价sql语句
		String averagePriceSellSql = "SELECT SUM(price*(size-executed_size))/SUM(size-executed_size) FROM "
				+ dataBaseName + ".tb_future_order WHERE contract_id=" + contractId + " AND side=2;";

		// 深度加权买价与卖价
		averagePriceBuy = MysqlConnector.sqlRsToDouble(averagePriceBuySql, 1);
		averagePriceSell = MysqlConnector.sqlRsToDouble(averagePriceSellSql, 1);

		// 深度加权价
		averagePrice = (averagePriceBuy + averagePriceSell) / 2;
		// 合理基差
		justBasis = indexPrice * (averagePrice / indexPrice - 1);
		// 溢价指数=深度加权中间价/指数价格 - 1
		HSAHP = averagePrice / indexPrice - 1;
		// 资金费率（互换补偿率）
		fundingRate = HSAHP + clamp(interestRate - HSAHP, -0.00375, 0.00375);
		fundingRateReal = HSAHP + (interestRate - HSAHP);

		testDetail.append("互换补偿率:" + df.format(fundingRate * 100) + "%\r\n");
		testDetail.append("真实互换补偿率:" + df.format(fundingRateReal) + "\r\n");
		testDetail.append("深度加权买价:" + df.format(averagePriceBuy) + "\r\n");
		testDetail.append("深度加权卖价:" + df.format(averagePriceSell) + "\r\n");
		testDetail.append("深度加权中间价:" + df.format(averagePrice) + "\r\n");
		testDetail.append("合理基差:" + df.format(justBasis) + "\r\n");
		testDetail.append("溢价指数:" + df.format(HSAHP) + "\r\n");
		testDetail.append("clamp:" + clamp(interestRate - HSAHP, -0.00375, 0.00375) + "\r\n");
		System.out.println(testDetail);
	}

	// clamp方法
	public static double clamp(double x, double y, double z) {
		if (x < y) {
			return y;
		} else if (x > z) {
			return z;
		} else {
			return x;
		}
	}
}
