package service.business;

import config.ConfigCenter;

/**
 * 
 * @author Muguozheng
 * @date 2018��11��3��
 * @version
 * @description �ɿ������ͱ�֤�����
 */
public class Open {
	// �����ֵı�Ǽ۸�,�����Ǽ۸��б仯,�輰ʱ����,"-1"ռλ��
	static double[] indexPrices = { -1, 6400, 2.3584, 100, 200, 100, 0.4, 9.8 };

	public static void main(String[] args) {
		// ��ֵ��
		double available = 20000; // ��ǰ�����ʲ�
		int contractId = 3; // ��ԼID,1-7�ֱ�ΪBTC��EOS��BCH��ETH��LTC��XRP��ETC
		double price = 90; // ί�м۸�
		double size = 1000; // ί������
		int leverage = 100; // �ܸ�
		// ִ����
		getOpen(available, contractId, price, size, leverage);
	}

	public static void getOpen(double available, int contractId, double price, double size, int leverage) {
		// ��Լ����
		String[] tableName = { "ERROR", "BTC", "EOS", "ETH", "LTC", "XRP", "ETC" };
		// �������ֺ�Լ���� ����Ϊ:BTC��EOS��BCH��ETH��LTC��XRP��ETC
		// { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 };
		double[] ratios = ConfigCenter.getRatios();
		// double[] ratios = { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 };

		// �ɿ�����= �������ʲ�*�ʽ������/����ί�м۸�/�ܸˣ�+��ί�м۸�*2taker����
		double openSize = available
				/ (price / leverage * ratios[contractId] + price * 2 * 0.00015 * ratios[contractId]);
		double openSizeMarket = available / (indexPrices[contractId] * (1 + 0.03) / leverage * ratios[contractId]
				+ indexPrices[contractId] * (1 + 0.03) * 2 * 0.00015 * ratios[contractId]);

		// ǰ��չʾ��ռ�ñ�֤��,������������
		double occupancyBond = price / leverage * size * ratios[contractId];
		double occupancyBondMarket = indexPrices[contractId] * (1 + 0.03) / leverage * size * ratios[contractId];

		System.out.println(tableName[contractId] + "�޼ۿɿ�����:" + openSize);
		System.out.println(tableName[contractId] + "�мۿɿ�����:" + openSizeMarket);
		System.out.println(tableName[contractId] + "�޼�ռ�ñ�֤��:" + occupancyBond);
		System.out.println(tableName[contractId] + "�м�ռ�ñ�֤��:" + occupancyBondMarket);
	}
}
