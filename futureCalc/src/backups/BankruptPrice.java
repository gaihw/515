package backups;

import java.util.ArrayList;

/**
 * 
 * @author Muguozheng
 * @date 2018��11��2��
 * @version
 * @description ���ּۼ���
 */
public class BankruptPrice {
	public static void main(String[] args) {
		getBankruptPrice();
	}

	public static void getBankruptPrice() {
		StringBuffer testDetail = new StringBuffer();
		testDetail.append("������������������������������").append("\r\n");
		// ��Լ����
		String[] tableNames = { "ERROR", "BTC", "EOS", "BCH", "ETH", "LTC", "XRP", "ETC" };
		// �������ֺ�Լ���� ����Ϊ:BTC��EOS��BCH��ETH��LTC��XRP��ETC
		double[] ratios = { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 };
		double MM = 0.005; // ά�ֱ�֤����

		double initialAssets = 2000; // �ֲ�״̬�µĳ�ʼ�ʽ�
		int[] contractId = { 1, 2 }; // �ֲֺ�ԼID
		double[][] price = { { 6400, 6300 }, { 5, 6 } }; // �ֲ־���
		double[][] size = { { 10000, -20000 }, { 10000, -5000 } }; // �ֲ���,�ղ�Ϊ����
		double[] indexPriceFront = { -1, 6400, 6 }; // ����ǰ��Ǽ۸�
		double[] indexPriceBack = { -1, 6462, 4 }; // ����ʱ��Ǽ۸�

		double surplus = 0; // ʣ����
		double brunkuptPrice = 0; // ������λ���ּ�
		ArrayList<Double> weights = new ArrayList<Double>(); // ������λ��Ȩ��
		ArrayList<Double> positionQtys = new ArrayList<Double>(); // �������ֵĲ�λ����
		ArrayList<Double> realizeds = new ArrayList<Double>(); // ������λ��ӯ��

		double positionQty = 0; // ���в�λ����֮��
		double weight = 0; // Ȩ��
		double realized = 0; // ��λӯ��
		double realizedAll = 0; // ���в�λӯ��֮��
		double positionValueAll = 0; // ���в�λ��ֵ֮��
		double positionValueBuy = 0; // ��ֲ�λ��ֵ
		double positionValueSell = 0; // �ղֲ�λ��ֵ
		double positionValueNet = 0; // ������նȵĲ�λ��ֵ
		double riskDegree = 0; // ���ն�

		// ��ʽ��
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.######");

		for (int i = 0; i < contractId.length; i++) {
			// ��ѭ��Ϊ�����ֲ�λѭ��
			for (int j = 0; j < size[i].length; j++) {
				// ���в�λ������֮��
				positionQty = positionQty + Math.abs(size[i][j]);
				// ÿ����λ��ӯ��
				realized = (indexPriceBack[contractId[i]] - indexPriceFront[contractId[i]]) * size[i][j]
						* ratios[contractId[i]];
				realizeds.add(realized);
				// �ܵ�ӯ��
				realizedAll = realizedAll + realized;
				// ��λ��ֵ
				if (size[i][j] < 0) {
					positionValueSell = price[i][j] * Math.abs(size[i][j]) * ratios[contractId[i]];
				} else {
					positionValueBuy = price[i][j] * Math.abs(size[i][j]) * ratios[contractId[i]];
				}
			}
			positionValueNet = positionValueNet + Math.max(positionValueBuy, positionValueSell);
			// positionValueΪ���б��ֵĲ�λ��ֵ֮�ͣ������ӣ�
			positionValueAll = positionValueAll + positionQty * indexPriceBack[contractId[i]] * ratios[contractId[i]];
			positionQtys.add(positionQty);

			positionQty = 0;
			positionValueSell = 0;
		}

		// ��������ʣ����
		surplus = initialAssets + realizedAll;
		// ��������ʣ���ȼ�����ն�
		riskDegree = positionValueNet * MM / surplus;

		// ���㴩�ּ�
		for (int i = 0; i < contractId.length; i++) {
			for (int j = 0; j < size[i].length; j++) {
				// ����Ȩ��
				weight = Math.abs(size[i][j]) * indexPriceBack[contractId[i]] * ratios[contractId[i]]
						/ positionValueAll;
				weights.add(weight);
				// �Ʋ��۸�
				brunkuptPrice = indexPriceBack[contractId[i]] - surplus * weight / size[i][j] * ratios[contractId[i]];
				if (size[i][j] > 0) {
					testDetail.append(tableNames[contractId[i]] + "���ּ�(��):" + brunkuptPrice).append("\r\n");
				} else {
					testDetail.append(tableNames[contractId[i]] + "���ּ�(��):" + brunkuptPrice).append("\r\n");
				}
			}
		}
		testDetail.append("================================").append("\r\n");
		// ��¼��ؽ��
		testDetail.append("��λ��ֵ֮��:" + positionValueAll).append("\r\n");
		testDetail.append("ȫ�����ֲ�λ����:" + positionQtys).append("\r\n");
		testDetail.append("ȫ����λӯ��:" + realizeds).append("\r\n");
		testDetail.append("ȫ����λȨ��:" + weights).append("\r\n");
		testDetail.append("����ʣ����:" + surplus).append("\r\n");
		testDetail.append("ά�ֱ�֤��:" + positionValueNet * MM).append("\r\n");
		testDetail.append("ȫ����λ��ֵ:" + positionValueNet).append("\r\n");
		testDetail.append("���ն�:" + df.format(riskDegree * 100) + "%").append("\r\n");

		System.out.println(testDetail);
	}
}
