package service.business;

import config.ConfigCenter;

/**
 * 
 * @author Muguozheng
 * @date 2018��10��24��
 * @version
 * @description �����ֱ��ּۡ����ּۼ���
 */
public class PositionFuture {
	// �������ֺ�Լ���� ����Ϊ:BTC��EOS��BCH��ETH��LTC��XRP��ETC
	// { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 };
	static double[] ratios = ConfigCenter.getRatios();
	static double MM = 0.005; // ά�ֱ�֤����
	static double taker = 0.0006; // �����ɽ�������
	static double maker = 0.0003; // �����ɽ�������

	public static void main(String[] args) {
		double initialAssets = 100; // ��ʼ�ʽ�
		double currentAssets = 0; // ��ǰ�ʲ�
		int contractId = 2; // ��Լid
		double indexPrice = 3.3; // ��Ǽ۸�
		double[] openPrices = { 3.8 }; // ���ּ۸�
		double[] sizes = { 30 }; // ������������
		int[] leverages = { 33 }; // �ܸ�
		double[] sides = { 1 }; // ���ַ���,1Ϊ�࣬-1Ϊ��
		double[] feeType = { taker }; // taker:�����ɽ�,maker:�����ɽ�

		double positionSize = 0; // ��λ����
		double positionValue = 0; // ��λ�ܼ�ֵ
		double netPositionValue = 0; // ��λ��ֵ
		double averagePrice = 0; // �ֲ־���
		double actualLeverage = 0; // ʵ�ʸܸ�
		double occupancyBond = 0; // ռ�ñ�֤��
		double unrealized = 0; // δʵ��ӯ��
		double openFee = 0; // ����������
		double closeFee = 0; // ƽ��������
		double riskDegree = 0; // ���ն�
		double explosion = 0; // ���ּ۸�
		double bankruptPrice = 0; // �Ʋ��۸�

		// ��ʽ��
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.######");

		double temp = 0;
		for (int i = 0; i < openPrices.length; i++) {
			// ���㿪������
			positionSize = positionSize + sizes[i] * ratios[contractId];
			// �����λ��ֵ
			positionValue = positionValue + openPrices[i] * sizes[i] * ratios[contractId];
			// �����λ��ֵ
			netPositionValue = netPositionValue + (openPrices[i] * sizes[i] * ratios[contractId]) * sides[i];
			temp = temp + openPrices[i] * sizes[i] * ratios[contractId] / leverages[i];
			// ռ�ñ�֤��
			occupancyBond = occupancyBond
					+ (openPrices[i] / leverages[i] + openPrices[i] * feeType[i]) * sizes[i] * ratios[contractId];
			// δʵ��ӯ��
			unrealized = unrealized + (indexPrice - openPrices[i]) * sizes[i] * sides[i] * ratios[contractId];
			// ���㿪��������
			openFee = openFee + openPrices[i] * sizes[i] * ratios[contractId] * feeType[i];
			// ����ƽ��������
			closeFee = closeFee + openPrices[i] * sizes[i] * ratios[contractId] * 0.0006;

		}
		// ʵ�ʸܸ�
		actualLeverage = positionValue / temp;
		// �ֲ־���
		averagePrice = positionValue / positionSize;
		// ���㵱ǰ�ʲ�
		currentAssets = initialAssets + unrealized - openFee - closeFee;
		// ������ն�
		riskDegree = positionValue * MM / currentAssets;
		// ���㱬�ּ۸�
		explosion = averagePrice + (positionValue * MM - initialAssets + openFee + closeFee) / positionSize;
		// �����Ʋ��۸�
		bankruptPrice = averagePrice - (initialAssets - openFee - closeFee) / positionSize;

		System.out.println("�ֲ־���:" + df.format(averagePrice));
		System.out.println("��������:" + df.format(positionSize));
		System.out.println("��λ��ֵ:" + df.format(positionValue));
		System.out.println("��λ��ֵ:" + df.format(netPositionValue));
		System.out.println("ʵ�ʸܸ�:" + df.format(actualLeverage));
		System.out.println("ռ�ñ�֤��:" + df.format(occupancyBond));
		System.out.println("δʵ��ӯ��:" + df.format(unrealized));
		System.out.println("����������:" + df.format(openFee));
		System.out.println("ƽ��������:" + df.format(closeFee));
		System.out.println("��ǰ�ʲ�:" + df.format(currentAssets));
		System.out.println("���ն�:" + df.format(riskDegree * 100) + "%");
		System.out.println("��Ǽ۸�:" + df.format(indexPrice));
		System.out.println("���ּ۸�:" + df.format(explosion));
		System.out.println("���ּ۸�:" + df.format(bankruptPrice));
	}
}
