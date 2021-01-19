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

		// ���������ļ�������
		Properties dbProperties = ConfigCenter.dataParser();
		String driver = (String) dbProperties.get("driver");

		// ��ȡswaps������Ϣ
		String urlFuture = (String) dbProperties.get("urlFuture");
		String userNameFuture = (String) dbProperties.get("userFuture");
		String passWordFuture = (String) dbProperties.get("passwordFuture");

		// // ��ȡ���ؿ�������Ϣ
		// String urlLocal = (String) dbProperties.get("urlLocal");
		// String userNameLocal = (String) dbProperties.get("userLocal");
		// String passWordLocal = (String) dbProperties.get("passwordLocal");

		// �������ݿ�
		MysqlConnector.dbConnect(driver, urlFuture, userNameFuture, passWordFuture); // �������ݿ�
		String testReport = DbCheckFuture.checkOrderSize().toString(); // ִ�м�鷽��
		// DbCheckService.CheckSystemWallet(); // ϵͳǮ�����
		// DbCheckService.CheckTransfer(); // ת�˼��
		// DbCheckService.CheckSwapWallet(); // �˻����
		// DbCheckService.CheckPosition(); // ��λ���

		System.out.println(testReport);
		MysqlConnector.dbClose(); // �ر����ݿ�

		// // ���ʼ�
		// // �����ʼ�����
		// String subject = "USDT��Ŀ������鱨��";
		// // �ʼ�����
		// StringBuffer content = new StringBuffer();
		// content.append("<h2>Dear all,</h2><br/>");
		// content.append("����ִ�����,�ſ�����,�����������: <br/>");
		// content.append("<p
		// style=\"background-color:rgba(255,0,0,0.75)\">������Ϣ����Ҫ,���Ը���ɫ����</p>");
		// content.append(testReport);
		// content.append("<h3>Muguozheng,</h3>").append("<h3>�з���������</h3>");
		// EmailSend.emailSend(subject, content.toString());
	}
}
