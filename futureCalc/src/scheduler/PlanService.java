package scheduler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import config.ConfigCenter;
import service.business.DbCheckFuture;
import utils.EmailSend;
import utils.MysqlConnector;

/**
 * 
 * @author Muguozheng
 * @date 2018年10月21日
 * @version
 * @description 定义定时任务详情
 */
public class PlanService {

	// 定义定时任务的具体内容
	public void taskExecute() throws IOException, SQLException {
		// 在配置中心加载配置
		Properties dbProperties = ConfigCenter.dataParser();
		String driver = (String) dbProperties.get("driver");

		// 读取swaps配置信息
		String urlFuture = (String) dbProperties.get("urlFuture");
		String userNameFuture = (String) dbProperties.get("userFuture");
		String passWordFuture = (String) dbProperties.get("passwordFuture");

		// 连接数据库
		MysqlConnector.dbConnect(driver, urlFuture, userNameFuture, passWordFuture); // 连接数据库
		String testReport = DbCheckFuture.checkOrderSize().toString(); // 执行检查方法
		// DbCheckService.CheckSystemWallet(); // 系统钱包检查
		// DbCheckService.CheckTransfer(); // 转账检查
		// DbCheckService.CheckSwapWallet(); // 账户检查
		// DbCheckService.CheckPosition(); // 仓位检查
		MysqlConnector.dbClose(); // 关闭数据库

		// 设置邮件标题
		String subject = "USDT项目订单检查报告";
		// 邮件内容
		StringBuffer content = new StringBuffer();
		content.append("<h2>Dear all,</h2><br/>");
		content.append("测试执行完毕,概况如下,详情请见附件: <br/>");
		content.append("<p style=\"background-color:rgba(255,0,0,0.75)\">这条信息很重要,所以给红色背景</p>");
		content.append(testReport);
		content.append("<h3>Muguozheng,</h3>").append("<h3>研发部测试组</h3>");
		EmailSend.emailSend(subject, content.toString());
	}
}
