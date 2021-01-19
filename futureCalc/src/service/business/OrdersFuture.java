package service.business;

import config.ConfigCenter;

/**
 * 
 * @author Muguozheng
 * @date 2018��10��25��
 * @version
 * @description ί�е����ᱣ֤�����
 */
public class OrdersFuture {

	public static void main(String[] args) {
		// ��ֵ��
		double initialAssets = 1000; // ��ǰ�ʽ�
		int contractId = 3; // ��ԼID
		double sellPrice = 500; // ��һ�۸�
		double[] price = { 500, 495 }; // ����ί�м۸�
		double[] size = { 100, 200 }; // ����ί������
		double[] leverage = { 100, 100 }; // �ܸ�

		// ִ����
		frozenDeposit(initialAssets, contractId, sellPrice, price, size, leverage);
	}

	public static void frozenDeposit(double initialAssets, int contractId, double sellPrice, double[] price,
			double[] size, double[] leverage) {
		StringBuffer testDetail = new StringBuffer();
		// �������ֺ�Լ���� ����Ϊ:BTC��EOS��BCH��ETH��LTC��XRP��ETC
		// { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 };
		double[] ratios = ConfigCenter.getRatios();
		double frozenDeposit = 0; // �൥���ᱣ֤��
		double balance = 0; // ���

		// ��Լ����
		String[] tableName = { "ERROR", "BTC", "EOS", "BCH", "ETH", "LTC", "XRP", "ETC" };

		// ��ʽ��
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.########");

		for (int i = 0; i < price.length; i++) {

			testDetail.append("�����" + (i + 1) + "��ί�е��������\r\n");
			testDetail.append("��ǰ�ʲ�:" + initialAssets + "\r\n");
			// �൥���ᱣ֤��
			frozenDeposit = (sellPrice / leverage[i] + 0.0006 * sellPrice + 0.0006 * sellPrice) * Math.abs(size[i])
					* ratios[contractId];

			balance = initialAssets - frozenDeposit;
			initialAssets = balance;

			testDetail.append("�����ʲ�:" + df.format(balance) + "\r\n");
			testDetail.append("���ᱣ֤��:" + df.format(frozenDeposit) + "\r\n");

			testDetail.append("----ί�е�����----\r\n");
			testDetail.append("��Լ����:" + tableName[contractId] + "\r\n");
			testDetail.append("��һ�۸�:" + sellPrice + "\r\n");
			testDetail.append("ί�м۸�:" + price[i] + "\r\n");
			testDetail.append("ί������:" + size[i] + "\r\n");
			testDetail.append("�ܸ�:" + leverage[i] + "\r\n");
			testDetail.append("================\r\n");
		}
		System.out.println(testDetail);
	}
}
