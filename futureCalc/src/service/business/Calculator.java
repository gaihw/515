package service.business;

import config.ConfigCenter;

/**
 * 
 * @author Muguozheng
 * @date 2018��11��6��
 * @version
 * @description USDT��Լ����
 */
public class Calculator {
	// ��Լ����
	static final String[] tableNames = { "ERROR", "BTC", "EOS", "BCH", "ETH", "LTC", "XRP", "ETC" };
	// �������ֺ�Լ���� ����Ϊ:BTC��EOS��BCH��ETH��LTC��XRP��ETC
	// { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 };
	static double[] ratios = ConfigCenter.getRatios();
	// ��ʽ��
	static java.text.DecimalFormat df = new java.text.DecimalFormat("#.####");

	public static void main(String[] args) {
		// ��ֵ��
		int contractId = 1; // ��ԼID,1-7�ֱ�ΪBTC��EOS��BCH��ETH��LTC��XRP��ETC
		int leverage = 100; // �ܸ�
		double openPrice = 6000; // ���ּ۸�
		double closePrice = 9000; // ƽ�ּ۸�
		double size = 100; // ��������
		// �������
		earnings(contractId, leverage, openPrice, closePrice, size);

		// ��ֵ��
		int contactId2 = 1; // ��ԼID
		double openPrice2 = 6000; // ���ּ۸�
		double size2 = 100; // ��������
		int leverage2 = 100; // �ܸ�
		double targetProfit = 100; // Ŀ������
		double targetRate = 47.17; // Ŀ��������,��λΪ%
		// Ŀ��ۼ���
		priceTarget(contactId2, openPrice2, size2, leverage2, targetProfit, targetRate);
	}

	// �������
	public static void earnings(int contractId, int leverage, double openPrice, double closePrice, double size) {
		StringBuffer testDetail = new StringBuffer();

		double taker = 0.0006; // taker����
		String type = null; // ��λ����
		// ����У��
		if (openPrice <= 0 || closePrice <= 0) {
			System.out.println("���������Ŀ��ּ۸��ƽ�ּ۸�");
			return;
		} else if (leverage <= 0 || leverage > 100) {
			System.out.println("�ܸ�������2��3��5��10��20��33��50��100��");
			return;
		}
		// ������
		double occupancyBond = openPrice / leverage * Math.abs(size) * ratios[contractId]
				+ openPrice * Math.abs(size) * ratios[contractId] * taker;
		// ��ƽ�ּ۸�-���ּ۸�*��������
		double earnings = (closePrice - openPrice) * Math.abs(size) * ratios[contractId];
		// ������:����/��֤��/�ܸ�*100%
		double earningRate = earnings / occupancyBond / leverage;
		if (size > 0) {
			testDetail.append("�������" + tableNames[contractId] + "������������������\r\n");
			type = "��";
		} else if (size < 0) {
			testDetail.append("�������" + tableNames[contractId] + "�ղ��������������\r\n");
			type = "��";
		} else {
			System.out.println("������������Ϊ��");
		}

		testDetail.append("====������====\r\n");
		testDetail.append("��֤��:" + occupancyBond + "\r\n");
		testDetail.append("����:" + earnings + "\r\n");
		testDetail.append("������:" + df.format(earningRate * 100) + "%\r\n");

		testDetail.append("====��������====").append("\r\n");
		testDetail.append("��Լ:" + tableNames[contractId] + "USD\r\n");
		testDetail.append("��������:" + type + "\r\n");
		testDetail.append("�ܸ�:" + leverage + "\r\n");
		testDetail.append("���ּ۸�:" + openPrice + "\r\n");
		testDetail.append("ƽ�ּ۸�:" + closePrice + "\r\n");
		testDetail.append("��������:" + Math.abs(size) + "\r\n");
		testDetail.append("======================\r\n");
		System.out.println(testDetail);
	}

	// Ŀ��ۼ���
	public static void priceTarget(int contactId, double openPrice, double size, int leverage, double targetProfit,
			double targetRate) {
		StringBuffer testDetail = new StringBuffer();

		// ������
		double closePrice = 0; // Ԥ��ƽ�ּ�
		String type = null;
		double taker = 0.0006; // taker����
		double occupancyBond = openPrice / leverage * Math.abs(size) * ratios[contactId]
				+ openPrice * Math.abs(size) * ratios[contactId] * taker;
		System.out.println(occupancyBond);

		testDetail.append("���������Ŀ��ۼ����������\r\n");

		// earningRate*occupyBond/(size*ratios)+open=close
		if (size > 0) {
			closePrice = targetRate / 100 * leverage * occupancyBond / (size * ratios[contactId]) + openPrice;
			type = "��";
		} else if (size < 0) {
			closePrice = openPrice - targetRate / 100 * occupancyBond * leverage / (size * ratios[contactId]);
			type = "��";
		} else {
			System.out.println("���������Ŀ�������");
		}
		testDetail.append("====������====\r\n");
		testDetail.append("ƽ�ּ۸�(Ŀ������):" + (targetProfit / (size * ratios[contactId]) + openPrice) + "\r\n");
		testDetail.append("ƽ�ּ۸�(Ŀ��������):" + closePrice + "\r\n\r\n");

		testDetail.append("====��������====\r\n");
		testDetail.append("��Լ:" + tableNames[contactId] + "USD\r\n");
		testDetail.append("��������:" + type + "\r\n");
		testDetail.append("���ּ۸�:" + openPrice + "\r\n");
		testDetail.append("��������:" + Math.abs(size) + "\r\n");
		testDetail.append("Ŀ������:" + targetProfit + "\r\n");
		testDetail.append("Ŀ��������:" + targetRate + "%" + "\r\n");
		testDetail.append("======================\r\n");
		System.out.println(testDetail);
	}
}
