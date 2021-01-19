package service.business;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import config.ConfigCenter;
import utils.MysqlConnector;
import utils.ResultLog;

public class DbCheckFuture {

	public static void main(String[] args) throws IOException, SQLException {
		// 在配置中心加载配置
		Properties dbProperties = ConfigCenter.dataParser();
		String driver = (String) dbProperties.get("driver");

		// 读取swaps配置信息
		String urlFuture = (String) dbProperties.get("urlFuture");
		String userNameFuture = (String) dbProperties.get("userFuture");
		String passWordFuture = (String) dbProperties.get("passwordFuture");

		// checkOrderSize(); // 订单检查
		// CheckSystemWallet(); // 系统钱包检查
		// CheckSwapWallet(); // 用户钱包检查
		// CheckPosition(); // 仓位检查
		// MysqlConnector.dbClose(); // 关闭数据库
	}

	// 订单检查
	public static  StringBuffer checkOrderSize() throws SQLException, IOException {
		// 在配置中心加载配置
		Properties dataProperties = ConfigCenter.dataParser();
		Properties exeProperties = ConfigCenter.executeParser();

		// 测试报告
		StringBuffer testReport = new StringBuffer();
		int testNum = Integer.valueOf(exeProperties.getProperty("dbFutureTestNum")); // 读取历史测试次数

		testReport.append("★★★★★★★★★★★★★★★★第" + testNum + "次测试报告★★★★★★★★★★★★★★★★").append("\r\n");
		// 测试详情
		StringBuffer testDetail = new StringBuffer();
		testDetail.append("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆测试详情☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆").append("\r\n");
		// 订单检查开始时间
		long start = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 可以方便地修改日期格式
		//
		ArrayList<String> contractPass = new ArrayList<String>();
		ArrayList<String> contractFailed = new ArrayList<String>();
		// 获取所有products
		String sqlTableName = "SELECT id,table_name FROM tb_future_contract";
		ArrayList<String> contracIds = MysqlConnector.sqlRsToArr(sqlTableName, 1);
		ArrayList<String> tableNames = MysqlConnector.sqlRsToArr(sqlTableName, 2);

		for (int i = 0; i < tableNames.size(); i++) {
			StringBuffer contractDetail = new StringBuffer();

			long startSingle = 0;
			long endSingle = 0;
			long timeSingle = 0;
			contractDetail.append("开始" + tableNames.get(i) + "订单检查").append("\r\n");
			startSingle = System.currentTimeMillis();
			// 记录订单检查结果
			contractDetail.append(assertOrderSize(contracIds.get(i), tableNames.get(i))).append("\r\n");

			endSingle = System.currentTimeMillis();
			timeSingle = endSingle - startSingle;
			contractDetail.append(tableNames.get(i) + "订单检查结束,用时:" + timeSingle + "ms").append("\r\n");
			contractDetail.append("=========================================").append("\r\n");

			// 如果含有ERROR,该币种被判定检查未通过,加入检查失败的集合中
			if (contractDetail.toString().contains("ERROR")) {
				contractFailed.add(tableNames.get(i));
			} else {
				contractPass.add(tableNames.get(i));
			}

			// 单个币种的订单检查结果写入测试详情
			testDetail.append(contractDetail);
		}
		// 修改运行次数
		testNum++;
		ResultLog.modifyFile(ConfigCenter.executeProFile, "dbFutureTestNum", String.valueOf(testNum));

		long end = System.currentTimeMillis();
		long orderSizeTime = end - start;
		String testExecutor = exeProperties.getProperty("testExecutor");

		testReport.append("开始时间:" + dateFormat.format(start)).append("\t");
		testReport.append("订单检查总用时:" + orderSizeTime * 0.001 + "s").append("\t");
		testReport.append("订单检查执行人:" + testExecutor).append("\r\n");
		testReport.append("检查币种数:" + tableNames.size()).append("\t");
		testReport.append("检查通过币种数:" + contractPass.size()).append("\t");
		testReport.append("检查失败币种数:" + contractFailed.size()).append("\t");
		testReport.append("通过率:" + ((float) contractPass.size() / (float) tableNames.size()) * 100 + "%")
				.append("\r\n");
		testReport.append("订单检查通过币种:" + contractPass).append("\r\n");
		testReport.append("订单检查失败币种:" + contractFailed).append("\r\n");

		// 测试结果写入本地文件
		String resultFile = dataProperties.getProperty("dbFutureResult");
		String allTestResult = dataProperties.getProperty("dbFutureResultAll");

		// reWrite表示覆盖原有内容，write表示追加写入，不覆盖原有内容
		ResultLog.writeResult(testReport, resultFile, "reWrite"); // 写入测试报告
		ResultLog.writeResult(testDetail, resultFile, "write"); // 写入测试详情

		ResultLog.writeResult(testReport, allTestResult, "write"); // 写入测试报告
		ResultLog.writeResult(testDetail, allTestResult, "write"); // 写入测试详情
		System.out.println("数据已存入结果文件:" + resultFile);
		System.out.println(testReport);
		System.out.println(testDetail);
		return testReport;
	}

	// 订单准确性判断（先总体判断，不通过启动逐条判断）
	public static StringBuffer assertOrderSize(String contractId, String tableName) throws SQLException {
		// 测试详情
		StringBuffer testDetail = new StringBuffer();

		String sqlOrderIdsBuy = "SELECT DISTINCT order_id FROM tb_future_order_fill WHERE side=1 AND contract_id= "
				+ contractId;
		String sqlOrderFillNum = "SELECT COUNT(DISTINCT(order_id)) FROM tb_future_order_fill WHERE contract_id= "
				+ contractId;
		// 获取订单数量
		double orderFillNum = MysqlConnector.sqlRsToDouble(sqlOrderFillNum, 1);
		testDetail.append(tableName + "订单数量为：" + orderFillNum).append("\r\n");
		// System.out.println(tableName + "订单数量为：" + orderFillNum);
		ArrayList<String> fillOrderIdsBuy = MysqlConnector.sqlRsToArr(sqlOrderIdsBuy, 1);

		String sqlOrderIdsSell = "SELECT DISTINCT order_id FROM tb_future_order_fill"
				+ " WHERE side=2 AND contract_id= " + contractId;
		ArrayList<String> fillOrderIdsSell = MysqlConnector.sqlRsToArr(sqlOrderIdsSell, 1);

		// 订单表买单已成交数量
		String sqlOrderBuySum = "SELECT SUM(executed_size) FROM tb_future_order WHERE side=1 AND executed_size>0 AND contract_id= "
				+ contractId;
		// 订单完成表买单已成交数量
		String sqlOrderFinBuySum = "SELECT SUM(executed_size) FROM tb_future_order_finish WHERE side=1 AND executed_size>0 AND contract_id= "
				+ contractId;
		// order_fill表买单数量
		String sqlOrderFillBuySum = "SELECT SUM(size) FROM tb_future_order_fill WHERE side=1 AND contract_id= "
				+ contractId;

		// order表卖单已成交数量
		String sqlOrderSellSum = "SELECT SUM(executed_size) FROM tb_future_order WHERE side=2 AND contract_id= "
				+ contractId;
		// order_finish表卖单1️已成交数量
		String sqlOrderFinSellSum = "SELECT SUM(executed_size) FROM tb_future_order_finish WHERE side=2 AND contract_id= "
				+ contractId;
		// order_fill表卖单数量
		String sqlOrderFillSellSum = "SELECT SUM(size) FROM tb_future_order_fill WHERE side=2 AND contract_id= "
				+ contractId;

		double OrderBuySum = MysqlConnector.sqlRsToDouble(sqlOrderBuySum, 1);
		double OrderFinBuySum = MysqlConnector.sqlRsToDouble(sqlOrderFinBuySum, 1);
		double OrderFillBuySum = MysqlConnector.sqlRsToDouble(sqlOrderFillBuySum, 1);

		double OrderSellSum = MysqlConnector.sqlRsToDouble(sqlOrderSellSum, 1);
		double OrderFinSellSum = MysqlConnector.sqlRsToDouble(sqlOrderFinSellSum, 1);

		double OrderFillSellSum = MysqlConnector.sqlRsToDouble(sqlOrderFillSellSum, 1);

		// 买单&卖单:order+order_finish+order_finish_history=order_fill+order_fill_history
		double resultBuy = Math.abs(OrderBuySum) + Math.abs(OrderFinBuySum) - OrderFillBuySum;
		double resultSell = Math.abs(OrderSellSum) + Math.abs(OrderFinSellSum) - OrderFillSellSum;

		// 多空相等逻辑
		double fillResult = OrderFillBuySum - OrderFillSellSum;
		double finishResult = OrderBuySum + OrderFinBuySum - OrderSellSum - OrderFinSellSum;

		if (resultBuy == 0 && resultSell == 0 && fillResult == 0 && finishResult == 0) {
			testDetail.append(tableName + "订单汇总检查通过，无需遍历检查\r\n");
			// System.out.println(tableName + "订单汇总检查通过，无需遍历检查");
		} else {
			if (resultBuy != 0) {
				testDetail.append("ERROR:" + tableName + "买单汇总检查未通过，启动遍历检查\r\n");
				// System.out.println(tableName + "买单汇总检查未通过，启动遍历检查");
				// 输出信息:
				testDetail.append("OrderBuySum:" + OrderBuySum + "\r\n");
				testDetail.append("OrderFinBuySum:" + OrderFinBuySum + "\r\n");
				testDetail.append("OrderFillBuySum:" + OrderFillBuySum + "\r\n");

				testDetail.append("resultBuy:" + resultBuy + "---resultSell:" + resultSell + "---fillResult:"
						+ fillResult + "--finishResult:" + finishResult + "\r\n");
				// 记录订单检查结果
				testDetail.append(ergodicAssertOrder(contractId, tableName, fillOrderIdsBuy) + "\r\n");
				testDetail.append(tableName + "买单遍历检查结束\r\n");
				// System.out.println(tableName + "买单遍历检查结束");
			}
			if (resultSell != 0) {
				testDetail.append("ERROR:" + tableName + "卖单汇总检查未通过，启动遍历检查\r\n");
				// 输出详细信息
				testDetail.append("OrderSellSum:" + OrderSellSum + "\r\n");
				testDetail.append("OrderFinSellSum:" + OrderFinSellSum + "\r\n");
				testDetail.append("OrderFillSellSum:" + OrderFillSellSum + "\r\n");

				testDetail.append("resultBuy:" + resultBuy + "---resultSell:" + resultSell + "---fillResult:"
						+ fillResult + "--finishResult:" + finishResult + "\r\n");
				testDetail.append(ergodicAssertOrder(contractId, tableName, fillOrderIdsSell) + "\r\n");

				testDetail.append(tableName + "卖单遍历检查结束\r\n");
				// System.out.println(tableName + "卖单遍历检查结束");
			}
			if (resultBuy == 0 && resultSell == 0) { // 适用于两边多或者少同样的数值
				if (resultSell != 0 || fillResult != 0 || finishResult != 0) {
					testDetail.append("ERROR:买单与卖单数量不一致，请排查\r\n");
					testDetail.append(
							"resultBuy:" + resultBuy + "--fillResult:" + fillResult + "--finishResult:" + finishResult)
							.append("\r\n");
					testDetail.append(
							"OrderFillBuySum:" + OrderFillBuySum + "---" + "OrderFillSellSum:" + OrderFillSellSum)
							.append("\r\n");
				}
			}
		}
		return testDetail;
	}

	// 逐条记录判断
	public static StringBuffer ergodicAssertOrder(String contractId, String tableName, ArrayList<String> orderIds)
			throws SQLException {
		// 逐条记录详情
		StringBuffer ergodicAssert = new StringBuffer();
		String sqlOrderSize = null;
		String sqlFinishSize = null;
		String sqlFinHisSize = null;
		String sqlOrderFillSize = null;
		String sqlOrderFillHisSize = null;

		double orderSize = 0;
		double finishSize = 0;
		double finHisSize = 0;
		double orderFillSize = 0;
		double orderFillHisSize = 0;

		double OrderCheckResult = 0;

		int count = 0; // 运行次数
		int errorCount = 0;

		for (int i = 0; i < orderIds.size(); i++) {
			// 获取order_size
			sqlOrderSize = "SELECT executed_size FROM tb_future_order WHERE id_sole = " + orderIds.get(i)
					+ " AND contract_id= " + contractId;

			orderSize = MysqlConnector.sqlRsToDouble(sqlOrderSize, 1);

			// 获取finish_size
			sqlFinishSize = "SELECT executed_size FROM tb_future_order_finish WHERE id_sole = " + orderIds.get(i)
					+ " AND contract_id= " + contractId;
			finishSize = MysqlConnector.sqlRsToDouble(sqlFinishSize, 1);

			// 获取finish_history_size
			sqlFinHisSize = "SELECT executed_size FROM tb_future_order_history WHERE id_sole = " + orderIds.get(i)
					+ " AND contract_id= " + contractId;
			finHisSize = MysqlConnector.sqlRsToDouble(sqlFinHisSize, 1);

			// 获取order_fill_size
			sqlOrderFillSize = "SELECT SUM(size) FROM tb_future_order_fill WHERE order_id = " + orderIds.get(i)
					+ " AND contract_id= " + contractId;
			orderFillSize = MysqlConnector.sqlRsToDouble(sqlOrderFillSize, 1);

			// 获取order_fill_history_size
			sqlOrderFillHisSize = "SELECT SUM(size) FROM tb_future_order_fill_history WHERE order_id = "
					+ orderIds.get(i) + " AND contract_id= " + contractId;
			orderFillHisSize = MysqlConnector.sqlRsToDouble(sqlOrderFillHisSize, 1);

			// 计算
			OrderCheckResult = Math.abs(orderSize) + Math.abs(finishSize) + Math.abs(finHisSize)
					- Math.abs(orderFillSize) - Math.abs(orderFillHisSize);

			if (OrderCheckResult != 0) {
				ergodicAssert.append(tableName + "订单检查不通过," + "orderId:" + orderIds.get(i) + "!---OrderCheckResult:"
						+ OrderCheckResult + "--orderSize:" + orderSize + "--finishSize:" + finishSize + "--finHisSize:"
						+ finHisSize + "--orderFillSize:" + orderFillSize + "--orderFillHisSize:" + orderFillHisSize)
						.append("\r\n");
				errorCount++;
			}
			count++;
			try {
				// 进度展示
				if (count % (orderIds.size() / 10) == 0) {
					System.out.println(tableName + "已检索" + count + "条,共" + orderIds.size() + "条,当前进度:"
							+ (float) (count * 100) / (float) orderIds.size() + "%");
				}
			} catch (Exception e) {
				System.out.println("除數是零");
			}

		}
		ergodicAssert.append("------------------------------------------\r\n");
		ergodicAssert.append(tableName + "订单遍历检查结束,运行" + count + "次,检索到" + errorCount + "条错误\r\n");
		return ergodicAssert;
	}

	// 系统钱包检查
	/**
	 * 
	 * @modify:future相对于swap项目，取消了保险基金，增加了隔夜费
	 */
	public static void checkSystemWallet() throws SQLException {
		System.out.println("=============系统钱包检查=============");
		String sqlSystemWallet = "SELECT SUM(user_balance),SUM(fee),SUM(balance) FROM tb_system_wallet";
		// 手续费特殊存放
		String sqlFee = "SELECT SUM(balance) FROM tb_future_wallet WHERE user_id=1";
		// 手续费特殊存放
		String sqlOvernightFee = "SELECT SUM(balance) FROM tb_future_wallet WHERE user_id=2";

		// 用户账户总余额
		double userBalance = MysqlConnector.sqlRsToDouble(sqlSystemWallet, 1);
		// 总手续费余额
		double fee = MysqlConnector.sqlRsToDouble(sqlFee, 1);
		// 总的过夜费
		double overnightFee = MysqlConnector.sqlRsToDouble(sqlOvernightFee, 1);
		// 公司账户余额
		double balance = MysqlConnector.sqlRsToDouble(sqlSystemWallet, 3);

		double result = Math.abs(userBalance + fee + overnightFee - balance);
		if (result < 0.000001) {
			System.out.println("系统钱包检查通过");
		} else {
			System.out.println("系统钱包检查不通过");
		}
		System.out.println("--------参数信息--------");
		System.out.println("逻辑:result = userBalance + fee + voernightFee - balance = 0");
		System.out.println("result:" + result);
		System.out.println("userBalance:" + userBalance);
		System.out.println("fee:" + fee);
		System.out.println("overnightFee:" + overnightFee);
		System.out.println("balance:" + balance);
	}

	// 转账检查
	public static void checkTransfer() throws SQLException {
		// 获取转入金额
		String sqlAmountIn = "SELECT SUM(amount) amount_in FROM tb_future_transfer WHERE action=1 and status=1";
		String sqlAmountOut = "SELECT SUM(amount) amount_in FROM tb_future_transfer WHERE action=2 and status=1";
		String sqlBanlance = "SELECT SUM(balance) balance FROM tb_system_wallet";
		double amountIn = MysqlConnector.sqlRsToDouble(sqlAmountIn, 1);
		double amountOut = MysqlConnector.sqlRsToDouble(sqlAmountOut, 1);
		double balance = MysqlConnector.sqlRsToDouble(sqlBanlance, 1);

		double result = Math.abs(amountIn - amountOut - balance);
		if (result < 0.000001) {
			System.out.println("转账检查通过");
		} else {
			System.out.println("转账检查不通过,result:" + result + "--amount_in:" + amountIn + "--amount_out:" + amountOut
					+ "--balance：" + balance);
		}
		System.out.println(
				"result:" + result + "--amount_in:" + amountIn + "--amount_out:" + amountOut + "--balance:" + balance);
	}

	// 账户检查--待校对
	public static void checkSwapWallet() throws SQLException {
		long start = System.currentTimeMillis();
		System.out.println("===========账户检查===========");
		String sqlContractIds = "SELECT id,table_name FROM tb_future_contract";

		ArrayList<String> contracIds = MysqlConnector.sqlRsToArr(sqlContractIds, 1);

		// 仓位保证金 --取每个用户每个币种
		double positionMargin = 0;
		double positionMarginAll = 0;

		ArrayList<Double> positionMargins = new ArrayList<Double>();
		for (int i = 0; i < contracIds.size(); i++) {
			String sqlPosMargin = "SELECT MAX(position_margin) FROM tb_future_contract_position WHERE contract_id="
					+ contracIds.get(i) + " GROUP BY user_id";
			// 结果存入集合
			positionMargins = MysqlConnector.sqlRsToDoubleArr(sqlPosMargin, 1);
			// 对集合求和
			for (int j = 0; j < positionMargins.size(); j++) {
				positionMargin = positionMargin + positionMargins.get(j);
			}
			positionMarginAll = positionMarginAll + positionMargin;
			System.out.println("contractId:" + contracIds.get(i) + "--positionMargin:" + positionMargin);
			positionMargin = 0;
		}
		System.out.println();

		// 仓位保证金、平仓佣金--所有币种之和
		String sqlPosMargin = "SELECT SUM(close_commission) FROM tb_future_contract_position";
		// 平仓佣金
		double closeCommission = MysqlConnector.sqlRsToDouble(sqlPosMargin, 1);
		// 用户余额和已实现--所有币种之和
		String sqlSysWallet = "SELECT user_balance,realised FROM tb_system_wallet WHERE currency_id=8";
		double userBalance = MysqlConnector.sqlRsToDouble(sqlSysWallet, 1);
		double realised = MysqlConnector.sqlRsToDouble(sqlSysWallet, 2);

		// 单个用户余额总合、冻结--所有币种之和
		String sqlSwapWallet = "SELECT SUM(balance),SUM(hold) FROM tb_future_wallet WHERE currency_id=8 AND user_id!=1 AND user_id!=2";
		double balance = MysqlConnector.sqlRsToDouble(sqlSwapWallet, 1);
		double hold = MysqlConnector.sqlRsToDouble(sqlSwapWallet, 2);

		// 数据校验
		double result = Math.abs(balance + hold + positionMarginAll + closeCommission + realised - userBalance);
		if (result < 0.000001) {
			System.out.println("账户钱包检查通过");
		} else {
			System.out.println("账户钱包检查不通过!!!");
		}
		System.out.println("---------------------------");
		System.out
				.println("逻辑:result = balance + hold + positionMargin + closeCommission + realised - userBalance = 0");
		System.out.println("result:" + result);
		System.out.println("balance:" + balance);
		System.out.println("hold:" + hold);
		System.out.println("positionMargin:" + positionMarginAll);
		System.out.println("closeCommission:" + closeCommission);
		System.out.println("realised:" + realised);
		System.out.println("userBalance:" + userBalance);
		long end = System.currentTimeMillis();
		System.out.println("测试用时:" + (end - start) + "ms");
	}

	// 仓位检查
	public static void checkPosition() throws SQLException {
		String sqlTableName = "SELECT id,table_name FROM tb_future_contract";
		ArrayList<String> contractIds = MysqlConnector.sqlRsToArr(sqlTableName, 1);
		ArrayList<String> tableNames = MysqlConnector.sqlRsToArr(sqlTableName, 2);

		for (int i = 0; i < tableNames.size(); i++) {
			System.out.println("-----" + tableNames.get(i) + "仓位检查-----");
			// 多仓总量
			String sqlPositionBuy = "SELECT SUM(position_qty) FROM tb_future_contract_position WHERE contract_id="
					+ contractIds.get(i) + " AND side=1";
			// 空仓总量
			String sqlPositionSell = "SELECT SUM(position_qty) FROM tb_future_contract_position WHERE contract_id="
					+ contractIds.get(i) + " AND side=2";
			double positionSell = MysqlConnector.sqlRsToDouble(sqlPositionBuy, 1);
			double positionBuy = MysqlConnector.sqlRsToDouble(sqlPositionSell, 1);

			double result = positionSell - positionBuy;
			if (result == 0) {
				System.out.println(tableNames.get(i) + "仓位检查通过");
			} else {
				System.out.println(tableNames.get(i) + "仓位检查不通过");
				System.out.println("result:" + result);
			}

			System.out.println("positionBuy:" + positionBuy);
			System.out.println("positionSell:" + positionSell);
			System.out.println("------------------");
		}
	}
}
