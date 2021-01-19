package service.business;

import java.util.ArrayList;

import config.ConfigCenter;
import utils.GetInput;

/**
 * 
 * @author Muguozheng
 * @date 2018��11��2��
 * @version
 * @description ���ּۼ���
 */
public class BankruptPrice {
	public static void main(String[] args) {
		// ��ֵ��
		double initialAssets = 500; // �ֲ�״̬�µĳ�ʼ�ʽ�
//		int[] contractId = { 1, 2 }; // �ֲֺ�ԼID
		int[] contractId = { 8}; // �ֲֺ�ԼID
//		double[][] price = { { 9000, 9000 }, { 7.1, 7.1 } }; // �ֲ־���
		double[][] price = { { 368.7335} }; // �ֲ־���
//		double[][] size = { { 500, -1100 }, { 3000, -6200 } }; // �ֲ���,�ղ�Ϊ����
		double[][] size = { { -65}}; // �ֲ���,�ղ�Ϊ����

		// ����ǰ��Ǽ۸�
		double[] indexPriceFront = { -1, 10531.000, 27.1025, 0.05,2204.9857, 32, 0.4, 9.8 ,368.7335};
		// ����ʱ��Ǽ۸�
		double[] indexPriceBack = { -1, 10065.45, 22.2461, 0.04568,2075.7994, 32, 0.4, 9.8 ,381.8319};
		// ִ����
		getBankruptPrice(initialAssets, contractId, price, size, indexPriceFront, indexPriceBack);
	}

	public static void getBankruptPrice(double initialAssets, int[] contractId, double[][] price, double[][] size,
			double[] indexPriceFront, double[] indexPriceBack) {
		long start = System.currentTimeMillis();
		StringBuffer testDetail = new StringBuffer();
		testDetail.append("==========������Ϣ==========").append("\r\n");
		// ��Լ����
		String[] tableNames = { "ERROR", "BTC", "EOS", "TRX","ETH", "LTC", "XRP", "ETC" ,"BNB"};
		// �������ֺ�Լ���� ����Ϊ:BTC��EOS��BCH��ETH��LTC��XRP��ETC
		// { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 };
		double[] ratios = ConfigCenter.getRatios();
		double MM = 0.005; // ά�ֱ�֤����

		double surplus = 0; // ʣ����
		double brunkuptPrice = 0; // ������λ���ּ�
		ArrayList<Double> weights = new ArrayList<Double>(); // ������λ��Ȩ��
		ArrayList<Double> positionQtys = new ArrayList<Double>(); // �������ֵĲ�λ����
		ArrayList<Double> realizeds = new ArrayList<Double>(); // ������λ��ӯ��
		ArrayList<Double> positionValues = new ArrayList<Double>(); // ������λ�Ĳ�λ��ֵ
		ArrayList<Double> positionValueNets = new ArrayList<Double>(); // �������ֵĲ�λ��ֵ(���ȡ�ϴ���)

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
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.#######");

		// ��ѭ��������ѭ��
		for (int i = 0; i < contractId.length; i++) {
			// ��ѭ��Ϊ�����ֲ�λѭ��
			for (int j = 0; j < size[i].length; j++) {
				// ���в�λ������֮��
				positionQty = positionQty + Math.abs(size[i][j]);
				// ÿ����λ��ӯ��,������Ǽ۸���
				realized = (indexPriceBack[contractId[i]] - indexPriceFront[contractId[i]]) * size[i][j]
						* ratios[contractId[i]];
				realizeds.add(Double.valueOf(df.format(realized)));
				// �ܵ�ӯ��
				realizedAll = realizedAll + realized;
				// ��λ��ֵ
				if (size[i][j] < 0) {
					positionValueSell = price[i][j] * Math.abs(size[i][j]) * ratios[contractId[i]];
					positionValues.add(positionValueSell);
				} else if (size[i][j] > 0) {
					positionValueBuy = price[i][j] * Math.abs(size[i][j]) * ratios[contractId[i]];
					positionValues.add(positionValueBuy);
				} else {
					System.out.println("��λ��������Ϊ0��");
				}
				positionValueAll = positionValueAll + Math.abs(size[i][j]) * price[i][j] * ratios[contractId[i]];
			}
			positionValueNets.add(Double.valueOf(df.format(Math.max(positionValueBuy, positionValueSell))));
			// ��λ��ֵ
			positionValueNet = positionValueNet + Math.max(positionValueBuy, positionValueSell);
			// ���㣬��ֹ���α���δ��ĳ�������λ��������д��ֵ�����´����ϴεĲ�λֵ
			positionValueBuy = 0;
			positionValueSell = 0;
			positionQtys.add(Double.valueOf(df.format(positionQty)));
			positionQty = 0;
		}

		// ��������ʣ����
		surplus = initialAssets + realizedAll;
		// ��������ʣ���ȼ�����ն�
		riskDegree = positionValueNet * MM / surplus;

		// ���㴩�ּ�
		for (int i = 0; i < contractId.length; i++) {
			for (int j = 0; j < size[i].length; j++) {
				// ����Ȩ��
				weight = Math.abs(size[i][j]) * price[i][j] * ratios[contractId[i]] / positionValueAll;
				weights.add(Double.valueOf(df.format(weight)));
				// �Ʋ��۸�
				brunkuptPrice = indexPriceBack[contractId[i]] - surplus * weight / (size[i][j] * ratios[contractId[i]]);
				if (size[i][j] > 0) {
					testDetail.append(tableNames[contractId[i]] + "���ּ�(��):" + df.format(brunkuptPrice)).append("\r\n");
				} else {
					testDetail.append(tableNames[contractId[i]] + "���ּ�(��):" + df.format(brunkuptPrice)).append("\r\n");
				}
			}
		}

		testDetail.append("==========�˻���Ϣ==========").append("\r\n");
		testDetail.append("����ʣ����:" + df.format(surplus)).append("\r\n");
		testDetail.append("ά�ֱ�֤��:" + df.format(positionValueNet * MM)).append("\r\n");
		testDetail.append("ȫ����λ��ֵ:" + df.format(positionValueNet)).append("\r\n");
		testDetail.append("���ն�:" + df.format(riskDegree * 100) + "%").append("\r\n");
		testDetail.append("==========������Ϣ==========").append("\r\n");
		// ��¼��ؽ��
		testDetail.append("ȫ����λӯ���仯:" + df.format(realizedAll)).append("\r\n");
		testDetail.append("��λ��ֵ֮��:" + df.format(positionValueAll)).append("\r\n");
		testDetail.append("�����ֲ�λ����:" + positionQtys).append("\r\n");
		testDetail.append("�����ֲ�λ��ֵ:" + positionValueNets).append("\r\n");
		testDetail.append("����λ��λֵ:" + positionValues).append("\r\n");
		testDetail.append("����λӯ���仯:" + realizeds).append("\r\n");
		testDetail.append("����λȨ��:" + weights).append("\r\n");
		testDetail.append("����λ���ּ�:" + GetInput.getInput(indexPriceBack)).append("\r\n");

		System.out.println(testDetail);
		long end = System.currentTimeMillis();
		System.out.println("������ʱ:" + (end - start) + "ms");
	}
}
