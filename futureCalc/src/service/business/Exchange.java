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
 * @date 2018��11��3��
 * @version
 * @description ���������ʼ���
 */
public class Exchange {
	public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
		// ��ֵ��
		int contractId = 1002;
		double indexPrice = 5.035;

		long startTime = System.currentTimeMillis();
		// ���������ļ�������
		Properties dbProperties = ConfigCenter.dataParser();
		String driver = (String) dbProperties.get("driver");
		// ��ȡswaps������Ϣ
		String urlSwaps = (String) dbProperties.get("urlFuture");
		String userNameSwaps = (String) dbProperties.get("userFuture");
		String passWordSwaps = (String) dbProperties.get("passwordFuture");

		// �������ݿ�
		MysqlConnector.dbConnect(driver, urlSwaps, userNameSwaps, passWordSwaps); // �������ݿ�

		// ִ����
		getExchange(contractId, indexPrice);

		// �ر����ݿ�
		MysqlConnector.dbClose();
		long endTime = System.currentTimeMillis();
		System.out.println("����ʱ��:" + (endTime - startTime) + "ms");
	}

	// ����������
	public static void getExchange(int contractId, double indexPrice) throws SQLException {
		final String dataBaseName = "58future"; // ���ݿ�����
		double averagePriceBuy = 0; // ��ȼ�Ȩ���
		double averagePriceSell = 0; // ��ȼ�Ȩ����
		double averagePrice = 0; // ��ȼ�Ȩ�м��
		double interestRate = 0.0003; // ����
		double justBasis = 0; // �������
		double HSAHP = 0; // ���ָ��
		double fundingRate = 0; // �ʽ����
		double fundingRateReal = 0; // ��ʵ�ʽ����
		// ��ʽ��
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.######");

		StringBuffer testDetail = new StringBuffer();

		// ��ȼ�Ȩ���sql���
		String averagePriceBuySql = "SELECT SUM(price*(size-executed_size))/SUM(size-executed_size) FROM "
				+ dataBaseName + ".tb_future_order WHERE contract_id=" + contractId + " AND side=1;";
		// ��ȼ�Ȩ����sql���
		String averagePriceSellSql = "SELECT SUM(price*(size-executed_size))/SUM(size-executed_size) FROM "
				+ dataBaseName + ".tb_future_order WHERE contract_id=" + contractId + " AND side=2;";

		// ��ȼ�Ȩ���������
		averagePriceBuy = MysqlConnector.sqlRsToDouble(averagePriceBuySql, 1);
		averagePriceSell = MysqlConnector.sqlRsToDouble(averagePriceSellSql, 1);

		// ��ȼ�Ȩ��
		averagePrice = (averagePriceBuy + averagePriceSell) / 2;
		// �������
		justBasis = indexPrice * (averagePrice / indexPrice - 1);
		// ���ָ��=��ȼ�Ȩ�м��/ָ���۸� - 1
		HSAHP = averagePrice / indexPrice - 1;
		// �ʽ���ʣ����������ʣ�
		fundingRate = HSAHP + clamp(interestRate - HSAHP, -0.00375, 0.00375);
		fundingRateReal = HSAHP + (interestRate - HSAHP);

		testDetail.append("����������:" + df.format(fundingRate * 100) + "%\r\n");
		testDetail.append("��ʵ����������:" + df.format(fundingRateReal) + "\r\n");
		testDetail.append("��ȼ�Ȩ���:" + df.format(averagePriceBuy) + "\r\n");
		testDetail.append("��ȼ�Ȩ����:" + df.format(averagePriceSell) + "\r\n");
		testDetail.append("��ȼ�Ȩ�м��:" + df.format(averagePrice) + "\r\n");
		testDetail.append("�������:" + df.format(justBasis) + "\r\n");
		testDetail.append("���ָ��:" + df.format(HSAHP) + "\r\n");
		testDetail.append("clamp:" + clamp(interestRate - HSAHP, -0.00375, 0.00375) + "\r\n");
		System.out.println(testDetail);
	}

	// clamp����
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
