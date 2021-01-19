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
 * @date 2018��10��26��
 * @version
 * @description ִ��ȯ�̼���
 */
public class TestExecuteFuture {
	static double taker = 0.0006; // �����ɽ�������
	static double maker = 0.0003; // �����ɽ�������
	// ��Լ�б�,"ERROR"ռλ��
	static String[] tableNames = { "ERROR", "BTC", "EOS", "BCH", "ETH", "LTC", "XRP", "ETC" };

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// openTest(); // �ɿ�����
		// orderTest(); // ί�е�����
		// positionTest(); // ��λ����
		// getBankruptPrice(); //���ּۼ���
		getExchange(); // ���������ʼ���
	}

	// ������ն�
	public static void positionTest() throws FileNotFoundException, IOException {
		long start = System.currentTimeMillis();
		// ��������
		Properties prop = ConfigCenter.dataParser();
		// �����ֵı�Ǽ۸�,�����Ǽ۸��б仯,�輰ʱ����,"-1"ռλ��
		double[] indexPrices = { -1, 6400, 5, 400, 185.5, 100, 0.4, 9.8 };

		// ��ֵ��
		double initialAssets = 1000; // ��ʼ�ʽ�
		int[] contractIds = { 4 }; // 1-7�ֱ�ΪBTC��EOS��BCH��ETH��LTC��XRP��ETC
		double[][] openPrices = { { 222 }, { 400, 398 }, { 201 } }; // ���ּ�
		double[][] sizes = { { 500 }, { 500, -200 }, { 1000 } }; // ����������Ϊ��
		int[][] leverages = { { 100 }, { 100, 100 }, { 100 } }; // �ܸ�
		double[][] feeTypes = { { taker }, { taker, taker }, { taker } }; // taker:�����ɽ�,maker:�����ɽ�
		double[] sellPrices = { 0 };

		// ִ����
		StringBuffer testResult = RiskDegree.getPosition(tableNames, initialAssets, contractIds, indexPrices,
				sellPrices, openPrices, sizes, leverages, feeTypes);

		// ��¼��
		String resultFile = prop.getProperty("resultFile");
		String allTestResult = prop.getProperty("allTestResult");

		ResultLog.writeResult(testResult, resultFile, "reWrite");
		ResultLog.writeResult(testResult, allTestResult, "Write");
		long end = System.currentTimeMillis();
		System.out.println("���Ժ�ʱ:" + (end - start) + "ms");
	}

	// ���㴩�ּ�
	public static void getBankruptPrice() {
		// ��ֵ��
		double initialAssets = 2000; // �ֲ�״̬�µĳ�ʼ�ʽ�
		int[] contractId = { 1, 2 }; // �ֲֺ�ԼID
		double[][] price = { { 6400, 6300 }, { 5, 6 } }; // �ֲ־���
		double[][] size = { { 10000, -20000 }, { 10000, -5000 } }; // �ֲ���,�ղ�Ϊ����
		double[] indexPriceFront = { -1, 6400, 6 }; // ����ǰ��Ǽ۸�
		double[] indexPriceBack = { -1, 6462, 4 }; // ����ʱ��Ǽ۸�
		//
		BankruptPrice.getBankruptPrice(initialAssets, contractId, price, size, indexPriceFront, indexPriceBack);
	}

	// ���㻥��������
	public static void getExchange() throws FileNotFoundException, IOException {
		// ��ֵ��
		int contractId = 1001;
		double indexPrice = 500;

		// ���������ļ�������--���ݿ���������
		Properties dbProperties = ConfigCenter.dataParser();
		String driver = (String) dbProperties.get("driver");
		// ��ȡswaps������Ϣ
		String urlSwaps = (String) dbProperties.get("urlSwaps");
		String userNameSwaps = (String) dbProperties.get("userSwaps");
		String passWordSwaps = (String) dbProperties.get("passwordSwaps");

		// �������ݿ�
		try {
			MysqlConnector.dbConnect(driver, urlSwaps, userNameSwaps, passWordSwaps);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("���ݿ������쳣");
		} // �������ݿ�

		try {
			Exchange.getExchange(contractId, indexPrice);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// �ر����ݿ�
		try {
			MysqlConnector.dbClose();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("���ݿ�ر��쳣");
		}
	}
}
