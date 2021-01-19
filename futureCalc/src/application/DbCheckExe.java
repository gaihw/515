package application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import config.ConfigCenter;
import service.business.DbCheckFuture;
import utils.MysqlConnector;

public class DbCheckExe {
	public static void main(String[] args) throws IOException, SQLException {
		dbCheckFuture();
	}

	public static void dbCheckFuture() throws IOException, SQLException {

		// 在配置中心加载配置
		Properties dbProperties = ConfigCenter.dataParser();
		String driver = (String) dbProperties.get("driver");

		// 读取swaps配置信息
		String urlFuture = (String) dbProperties.get("urlFuture");
		String userNameFuture = (String) dbProperties.get("userFuture");
		String passWordFuture = (String) dbProperties.get("passwordFuture");

		// // 读取本地库配置信息
		// String urlLocal = (String) dbProperties.get("urlLocal");
		// String userNameLocal = (String) dbProperties.get("userLocal");
		// String passWordLocal = (String) dbProperties.get("passwordLocal");

		// 连接数据库
		MysqlConnector.dbConnect(driver, urlFuture, userNameFuture, passWordFuture); // 连接数据库
		String testReport = DbCheckFuture.checkOrderSize().toString(); // 执行检查方法
		// DbCheckService.CheckSystemWallet(); // 系统钱包检查
		// DbCheckService.CheckTransfer(); // 转账检查
		// DbCheckService.CheckSwapWallet(); // 账户检查
		// DbCheckService.CheckPosition(); // 仓位检查

		System.out.println(testReport);
		MysqlConnector.dbClose(); // 关闭数据库

		// // 发邮件
		// // 设置邮件标题
		// String subject = "USDT项目订单检查报告";
		// // 邮件内容
		// StringBuffer content = new StringBuffer();
		// content.append("<h2>Dear all,</h2><br/>");
		// content.append("测试执行完毕,概况如下,详情请见附件: <br/>");
		// content.append("<p
		// style=\"background-color:rgba(255,0,0,0.75)\">这条信息很重要,所以给红色背景</p>");
		// content.append(testReport);
		// content.append("<h3>Muguozheng,</h3>").append("<h3>研发部测试组</h3>");
		// EmailSend.emailSend(subject, content.toString());
	}
}
