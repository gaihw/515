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
 * @date 2018��10��21��
 * @version
 * @description ���嶨ʱ��������
 */
public class PlanService {

	// ���嶨ʱ����ľ�������
	public void taskExecute() throws IOException, SQLException {
		// ���������ļ�������
		Properties dbProperties = ConfigCenter.dataParser();
		String driver = (String) dbProperties.get("driver");

		// ��ȡswaps������Ϣ
		String urlFuture = (String) dbProperties.get("urlFuture");
		String userNameFuture = (String) dbProperties.get("userFuture");
		String passWordFuture = (String) dbProperties.get("passwordFuture");

		// �������ݿ�
		MysqlConnector.dbConnect(driver, urlFuture, userNameFuture, passWordFuture); // �������ݿ�
		String testReport = DbCheckFuture.checkOrderSize().toString(); // ִ�м�鷽��
		// DbCheckService.CheckSystemWallet(); // ϵͳǮ�����
		// DbCheckService.CheckTransfer(); // ת�˼��
		// DbCheckService.CheckSwapWallet(); // �˻����
		// DbCheckService.CheckPosition(); // ��λ���
		MysqlConnector.dbClose(); // �ر����ݿ�

		// �����ʼ�����
		String subject = "USDT��Ŀ������鱨��";
		// �ʼ�����
		StringBuffer content = new StringBuffer();
		content.append("<h2>Dear all,</h2><br/>");
		content.append("����ִ�����,�ſ�����,�����������: <br/>");
		content.append("<p style=\"background-color:rgba(255,0,0,0.75)\">������Ϣ����Ҫ,���Ը���ɫ����</p>");
		content.append(testReport);
		content.append("<h3>Muguozheng,</h3>").append("<h3>�з���������</h3>");
		EmailSend.emailSend(subject, content.toString());
	}
}
