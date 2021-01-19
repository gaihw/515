package service.business;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import config.ConfigCenter;
import utils.GetInput;

/**
 * 
 * @author Muguozheng
 * @date 2018��11��26��
 * @version 1.0
 * @description �����ּ��,�ܹ�֧�ּ�����ǰί�е���ƽ���߼�
 */
public class OrderCheck2 {

	static StringBuffer testDetail = new StringBuffer();
	static StringBuffer testReport = new StringBuffer();
	static StringBuffer testInput = new StringBuffer();

	public static void main(String[] args) throws FileNotFoundException, IOException {
		long start = System.currentTimeMillis();

		// �����ֵı�Ǽ۸�,�����Ǽ۸��б仯,�輰ʱ����,"-1"ռλ��
		double[] indexPrices = { -1, 4456, 4, 125, 35, 0.4, 9.8 };
		double taker = 0.0006; // �����ɽ�������
		double maker = 0.0003; // �����ɽ�������
		String[] tableNames = { "ERROR", "BTC", "EOS", "ETH", "LTC", "XRP", "ETC", "DASH" };

		// ��ֵ��
		double initialAssets = 100; // ��ʼ�ʽ�
		int[] contractIds = { 2 }; // 1-7�ֱ�ΪBTC��EOS��ETH��LTC��XRP��ETC��DASH
		double[][] prices = { { 3.5, 3.5 } }; // ���ּ�,��������ƽ��
		double[][] sizes = { { -50, -45 } }; // ����,����Ϊ��
		int[][] leverages = { { 100, 100 } }; // �ܸ�,��������δ�ɽ�
		double[][] feeTypes = { { taker, taker } }; // taker:�����ɽ�,maker:�����ɽ�������
		double[] sellPrices = { 4.4 }; // ��һ�۸�,��������㶳�ᱣ֤��,����ɺ���

		// ִ����
		OrderCheck2.getPosition(tableNames, initialAssets, contractIds, indexPrices, sellPrices, prices, sizes,
				leverages, feeTypes);

		long end = System.currentTimeMillis();
		System.out.println("������ʱ:" + (end - start) + "ms");
	}

	public static StringBuffer getPosition(String[] tableNames, double initialAssets, int[] contractIds,
			double[] indexPrices, double[] sellPrices, double[][] prices, double[][] sizes, int[][] leverages,
			double[][] feeTypes) throws FileNotFoundException, IOException {
		// ��¼��ʼʱ��
		long startTime = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ

		// ��������������
		// �������ֺ�Լ���� ����Ϊ:BTC��EOS��ETH��LTC��XRP��ETC
		// { -1, 0.002, 2, 0.05, 0.2, 40, 1 };
		double[] ratios = ConfigCenter.getRatios();
		double MM = 0.005; // ά�ֱ�֤����

		// ������
		double currentAssets = 0; // ��ǰ�ʲ�
		double balance = 0; // ���,���ݿ��¼�ֶ�
		double available = 0; // ����

		double sizeBuy = 0; // ����ܲ�
		double sizeSell = 0; // �ղ��ܲ�
		double positionSizeBuy = 0; // ��ֲ�λ����
		double positionSizeSell = 0; // �ղֲ�λ����

		double positionValueBuy = 0; // ��ֲ�λ�ܼ�ֵ
		double positionValueSell = 0; // ��ֲ�λ�ܼ�ֵ
		double netPosition = 0; // ��λ��ֵ
		double netPositionAll = 0; // ���б��ֵĲ�λ��ֵ
		double realPosition = 0; // ��ղֽϴ�ֵ
		double realPositionAll = 0; // ���б��ֶ�ղֽϴ�ֵ֮��

		double averagePriceBuy = 0; // ��ǰ��ֲֳ־���
		double averagePriceSell = 0; // ��ǰ�ղֲֳ־���
		double leverageBuy = 0; // �������ʵ�ʸܸ�
		double leverageSell = 0; // �ղ�����ʵ�ʸܸ�

		double hold = 0; // ���ᱣ֤��
		double occupancyBondAll = 0; // ռ�ñ�֤���ܺ�
		double occupancyBondBuy = 0; // �ղ�ռ�ñ�֤��
		double occupancyBondSell = 0; // �ղ�ռ�ñ�֤��
		double occupancyBondNet = 0; // ����λ��֤��,ȡ����յĽϴ�ֵ
		double occupancyBondNetAll = 0; // ���б��ֵľ���λ��֤��

		double realizedBuy = 0; // �����ʵ��ӯ��
		double realizedSell = 0; // �ղ���ʵ��ӯ��
		double realized = 0; // �����ֵ���ʵ��ӯ��
		double realizedAll = 0; // ȫ�����ֵ���ʵ��ӯ��
		double unrealizedBuy = 0; // ���δʵ��ӯ��
		double unrealizedSell = 0; // �ղ�δʵ��ӯ��
		double unrealized = 0; // ������ȫ����λδʵ��ӯ��
		double unrealizedAll = 0; // ȫ�����ֵ�δʵ��ӯ��
		double unrealizedBuyRate = 0; // ���δʵ��ӯ����
		double unrealizedSellRate = 0; // �ղ�δʵ��ӯ����

		double openFeeBuy = 0; // ��ֿ���������
		double openFeeSell = 0; // �ղֿ���������
		double closeFeeBuy = 0; // ���ƽ��������
		double closeFeeSell = 0; // �ղ�ƽ��������
		double feeAll = 0; // �ܵ�������
		double feeCurrent = 0; // ����������,ʵ�۵�,��������
		double riskDegree = 0; // ���ն�

		// ��ʽ��
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.######");

		for (int i = 0; i < contractIds.length; i++) {
			// ���㿪������
			double temp1 = 0;
			double temp2 = 0;
			int buyNum = 0; // �������
			int sellNum = 0; // �ղ�����
			for (int j = 0; j < prices[i].length; j++) {
				// // ���ֵ�,price����������
				if (prices[i][j] > 0) {
					// ��ֿ���
					if (sizes[i][j] > 0) {
						// �����Ϸ���У�
						if (leverages[i][j] <= 0 || contractIds[i] <= 0 || contractIds[i] > 7
								|| ratios[contractIds[i]] < 0 || indexPrices[contractIds[i]] < 0) {
							System.out.println("�۸��������ܸˡ�contractId���������Ϲ���,�����");
							return null;
						}

						// ����ܲ�
						sizeBuy = sizeBuy + sizes[i][j];
						// ��λ����
						positionSizeBuy = positionSizeBuy + sizes[i][j] * ratios[contractIds[i]];
						// ��λ�ܼ�ֵ
						positionValueBuy = positionValueBuy + prices[i][j] * sizes[i][j] * ratios[contractIds[i]];
						// ռ�ñ�֤��
						occupancyBondBuy = occupancyBondBuy
								+ (prices[i][j] / leverages[i][j]) * sizes[i][j] * ratios[contractIds[i]];
						// ʵ�ʳֲָܸ�
						temp1 = temp1 + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] / leverages[i][j];
						// ���³ֲָܸ�
						leverageBuy = positionValueBuy / temp1;
						// ���³ֲ־���
						averagePriceBuy = positionValueBuy / positionSizeBuy;
						// ���������
						openFeeBuy = openFeeBuy + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] * feeTypes[i][j];
						// ƽ�������� --����,�̶�ȡtaker
						closeFeeBuy = closeFeeBuy + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] * 0.0006;
						// ����������
						feeCurrent = feeCurrent + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] * feeTypes[i][j];
						// ���δʵ��ӯ��
						unrealizedBuy = (indexPrices[contractIds[i]] - averagePriceBuy) * sizeBuy
								* ratios[contractIds[i]];
						buyNum++;

					} else if (sizes[i][j] < 0) {
						// �����Ϸ���У�
						if (leverages[i][j] <= 0 || contractIds[i] <= 0 || contractIds[i] > 7
								|| ratios[contractIds[i]] < 0 || indexPrices[contractIds[i]] < 0) {
							System.out.println("openPrice��size��leverage��ratios��contractId�Ȳ��������Ϲ���,�����");
							return null;
						}
						// �ղ��ܲ�
						sizeSell = sizeSell - sizes[i][j];

						// ��λ����
						positionSizeSell = positionSizeSell + sizes[i][j] * (-1) * ratios[contractIds[i]];
						// ��λ�ܼ�ֵ
						positionValueSell = positionValueSell
								+ prices[i][j] * Math.abs(sizes[i][j]) * ratios[contractIds[i]];
						// ռ�ñ�֤��
						occupancyBondSell = occupancyBondSell
								+ (prices[i][j] / leverages[i][j]) * sizes[i][j] * (-1) * ratios[contractIds[i]];
						// ʵ�ʳֲָܸ�
						temp2 = temp2 + prices[i][j] * Math.abs(sizes[i][j]) * ratios[contractIds[i]] / leverages[i][j];
						// ���³ֲָܸ�
						leverageSell = positionValueSell / temp2;
						// ���³ֲ־���
						averagePriceSell = positionValueSell / positionSizeSell;
						// �ղ�������
						openFeeSell = openFeeSell
								+ prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] * feeTypes[i][j];
						// ƽ�������� --����,�̶�ȡtaker
						closeFeeSell = closeFeeSell
								+ prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] * 0.0006;
						// ����������
						feeCurrent = feeCurrent
								+ prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] * feeTypes[i][j];
						// �ղ�δʵ��ӯ��
						unrealizedSell = (indexPrices[contractIds[i]] - averagePriceSell) * sizeSell * (-1)
								* ratios[contractIds[i]];
						sellNum++;
					}
					// price<0����ƽ��
				} else if (prices[i][j] < 0) {
					if (sizes[i][j] > 0) { // ����ƽ��
						// ����У��
						if (leverageSell == 0 || sizes[i][j] * ratios[contractIds[i]] > positionSizeSell) {
							System.out.println("�����ղֿ�ƽ����");
							return null;
						}
						// �ղ�δʵ��ӯ�� --����
						unrealizedSell = unrealizedSell - (averagePriceSell - indexPrices[contractIds[i]]) * sizes[i][j]
								* 1 * ratios[contractIds[i]];
						// �ղ���ʵ��ӯ�� --׷��
						realizedSell = realizedSell + (averagePriceSell - Math.abs(prices[i][j])) * sizes[i][j] * 1
								* ratios[contractIds[i]];
						// ƽ�������� --ƽ��ʱ�ȷ��������ƽ��Ӷ��,�ٰ�ʵ�ʳɽ��۳�ƽ��������
						closeFeeSell = closeFeeSell - (averagePriceSell * 0.0006 + prices[i][j] * feeTypes[i][j])
								* sizes[i][j] * ratios[contractIds[i]];
						// ����������
						feeCurrent = feeCurrent
								+ prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] * feeTypes[i][j];
						// ��λ�ܲ� ����
						sizeSell = sizeSell - sizes[i][j];
						// ��λ���� �²�λ���� = ԭ��λ���� - ƽ�ֲ�������
						positionSizeSell = positionSizeSell - sizes[i][j] * ratios[contractIds[i]];
						// ��λ�ܼ�ֵ �²�λ�ܼ�ֵ = �ֲ־���*��λ����
						positionValueSell = averagePriceSell * positionSizeSell;
						// ռ�ñ�֤�� ��ռ�ñ�֤�� = ԭռ�ñ�֤�� - ƽ�ֲ���ռ�ñ�֤��
						occupancyBondSell = (averagePriceSell / leverageSell) * positionSizeSell;

					} else if (sizes[i][j] < 0) { // ����ƽ��
						// ����У��
						if (leverageBuy == 0
								|| Math.abs(sizes[i][j]) * ratios[contractIds[i]] > Math.abs(positionSizeBuy)) {
							System.out.println("������ֿ�ƽ����");
							return null;
						}
						// ƽ�������� --ƽ��ʱ�ȷ��������ƽ��Ӷ��,�ٰ�ʵ�ʳɽ��۳�ƽ��������
						closeFeeBuy = closeFeeBuy - (averagePriceBuy * 0.0006 + prices[i][j] * feeTypes[i][j]) * (-1)
								* sizes[i][j] * ratios[contractIds[i]];
						// ����������
						feeCurrent = feeCurrent + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] * feeTypes[i][j];
						// �ղ�δʵ��ӯ�� --����
						unrealizedBuy = unrealizedBuy - (averagePriceBuy - indexPrices[contractIds[i]]) * sizes[i][j]
								* ratios[contractIds[i]];
						// �ղ���ʵ��ӯ�� --׷��
						realizedBuy = realizedBuy + (Math.abs(prices[i][j]) - averagePriceBuy) * Math.abs(sizes[i][j])
								* ratios[contractIds[i]];
						// ��λ���� �²�λ���� = ԭ��λ���� - ƽ�ֲ�������
						positionSizeBuy = positionSizeBuy + sizes[i][j] * ratios[contractIds[i]];
						// ��λ�ܲ�
						sizeBuy = sizeBuy + sizes[i][j];
						// ��λ�ܼ�ֵ �²�λ�ܼ�ֵ = �ֲ־���*��λ����
						positionValueBuy = averagePriceBuy * positionSizeBuy;
						// ռ�ñ�֤�� ��ռ�ñ�֤�� = ԭռ�ñ�֤�� - ƽ�ֲ���ռ�ñ�֤��
						occupancyBondBuy = (averagePriceBuy / leverageBuy) * positionSizeBuy;
						// ʵ�ʳֲָܸ�
						temp1 = temp1 + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] / leverages[i][j];
					}
				}

				// ������δʵ��ӯ��
				unrealized = unrealizedBuy + unrealizedSell;
				// ��������ʵ��ӯ��
				realized = realizedBuy + realizedSell;
			}
			// �ܵĲ�λ��֤��
			occupancyBondAll = occupancyBondAll + occupancyBondBuy + occupancyBondSell;
			// ��λ��ֵ
			netPosition = Math.abs(positionValueBuy - positionValueSell);
			// �����ֶ�ղֽϴ�ֵ
			realPosition = getMax(positionValueBuy, positionValueSell);
			// �����ܵ�������
			feeAll = feeAll + openFeeBuy + closeFeeBuy + openFeeSell + closeFeeSell;
			// �����ֵ�δʵ��ӯ����
			unrealizedBuyRate = (indexPrices[contractIds[i]] - averagePriceBuy) / averagePriceBuy;
			// �����ֵ�δʵ��ӯ����
			unrealizedSellRate = (averagePriceSell - indexPrices[contractIds[i]]) / averagePriceSell;
			// �������б��ֵĲ�λ��ֵ
			netPositionAll = netPositionAll + Math.abs(netPosition);
			// �������б��ֵĶ�ղ�λ�ϴ�ֵ
			realPositionAll = realPositionAll + realPosition;
			// �������б��ֵ�δʵ��ӯ��
			unrealizedAll = unrealizedAll + unrealized;
			// �������б��ֵ�ʵ��ӯ��
			realizedAll = realizedAll + realized;
			// ��ȡ����λ��֤��
			occupancyBondNet = getMax(occupancyBondBuy, occupancyBondSell);
			// ���б��ֵľ���λ��֤��
			occupancyBondNetAll = occupancyBondNetAll + occupancyBondNet;

			testDetail.append("=======" + tableNames[contractIds[i]] + "��λ��Ϣ=======\r\n");
			testDetail.append(tableNames[contractIds[i]] + "��Ǽ۸�:" + indexPrices[contractIds[i]] + "\r\n");

			testDetail.append("------" + tableNames[contractIds[i]] + "�ܲ�λ��Ϣ" + "------\r\n");
			testDetail.append("����δʵ��ӯ��:" + df.format(unrealized) + "\r\n");
			testDetail.append("���ֲ�λ��¶ͷ��:" + df.format(netPosition) + "\r\n");

			testDetail.append("����ռ�ñ�֤��:" + df.format(occupancyBondNet) + "\r\n");

			if (buyNum > 0) {
				testDetail.append("-------��ֲ�λ��Ϣ-------\r\n");
				testDetail.append("��ֲֳ־���:" + df.format(averagePriceBuy) + "\r\n");
				testDetail.append("��ֿ�������:" + df.format(positionSizeBuy) + "\r\n");
				testDetail.append("����ܲ�:" + sizeBuy + "\r\n");
				testDetail.append("��ֲ�λ��ֵ:" + df.format(positionValueBuy) + "\r\n");
				testDetail.append("���ʵ�ʸܸ�:" + df.format(leverageBuy) + "\r\n");
				testDetail.append("��ֿ���������:" + df.format(openFeeBuy) + "\r\n");
				testDetail.append("���ƽ��������:" + df.format(closeFeeBuy) + "\r\n");
				testDetail.append("���ռ�ñ�֤��:" + df.format(occupancyBondBuy) + "\r\n");
				testDetail.append("���δʵ��ӯ��:" + df.format(unrealizedBuy) + "\r\n");
				testDetail.append("�����ʵ��ӯ��:" + df.format(realizedBuy) + "\r\n");
				testDetail.append("���δʵ��ӯ����:" + df.format(unrealizedBuyRate * 100) + "%\r\n");
			}

			if (sellNum > 0) {
				testDetail.append("-------�ղֲ�λ��Ϣ-------\r\n");
				testDetail.append("�ղֲֳ־���:" + df.format(averagePriceSell) + "\r\n");
				testDetail.append("�ղ��ܲ�:" + sizeSell + "\r\n");
				testDetail.append("�ղֿ�������:" + df.format(positionSizeSell) + "\r\n");
				testDetail.append("�ղֲ�λ��ֵ:" + df.format(positionValueSell) + "\r\n");
				testDetail.append("�ղ�ʵ�ʸܸ�:" + df.format(leverageSell) + "\r\n");
				testDetail.append("�ղֿ���������:" + df.format(openFeeSell) + "\r\n");
				testDetail.append("�ղ�ƽ��������:" + df.format(closeFeeSell) + "\r\n");
				testDetail.append("�ղ�ռ�ñ�֤��:" + df.format(occupancyBondSell) + "\r\n");
				testDetail.append("�ղ�δʵ��ӯ��:" + df.format(unrealizedSell) + "\r\n");
				testDetail.append("�ղ���ʵ��ӯ��:" + df.format(realizedSell) + "\r\n");
				testDetail.append("�ղ�δʵ��ӯ����:" + df.format(unrealizedSellRate * 100) + "%\r\n");
			}

			testDetail.append("======================\r\n\r\n");

			// �����ݹ���
			sizeBuy = 0;
			sizeSell = 0;
			positionSizeBuy = 0;
			positionValueBuy = 0;
			occupancyBondBuy = 0;
			openFeeBuy = 0;
			closeFeeBuy = 0;
			openFeeSell = 0;
			closeFeeSell = 0;
			positionSizeSell = 0;
			positionValueSell = 0;
			occupancyBondSell = 0;
			netPosition = 0;
			realPosition = 0;
			unrealizedBuy = 0;
			unrealizedSell = 0;
			realizedBuy = 0;
			realizedSell = 0;
			unrealized = 0;
			realized = 0;
		}

		// ������־��¼
		testInput.append("�����������:\r\n");
		testInput.append("indexPrices:" + GetInput.getInput(indexPrices) + "\r\n");
		testInput.append("initialAssets:" + initialAssets + "\r\n");
		testInput.append("contractIds:" + GetInput.getInput(contractIds) + "\r\n");
		testInput.append("openPrices:" + GetInput.getInput(prices).toString() + "\r\n");
		testInput.append("sizes:" + GetInput.getInput(sizes) + "\r\n");
		testInput.append("leverages:" + GetInput.getInput(leverages) + "\r\n");
		testInput.append("feeTypes:" + GetInput.getInput(feeTypes) + "\r\n\r\n");

		testReport.append("������������ֲ��Ա����������\r\n");
		testReport.append("���Կ�ʼʱ��:" + dateFormat.format(startTime) + "\r\n");

		// ���㵱ǰ�ʲ�
		currentAssets = initialAssets + unrealizedAll + realizedAll - feeAll;
		// Ǯ�����
		balance = currentAssets - unrealizedAll - occupancyBondNetAll;
		// ����
		available = currentAssets - occupancyBondNetAll - hold;
		// ������ն�
		riskDegree = realPositionAll * MM / (currentAssets);

		testReport.append("����������˻���Ϣ���������\r\n");
		testReport.append("���ն�:" + df.format(riskDegree * 100) + "%\r\n");
		testReport.append("------�ҵĺ�Լ�ʲ�------\r\n");
		testReport.append("��ǰ�ʲ�:" + df.format(currentAssets) + "\r\n");
		testReport.append("�����ʲ�:" + df.format(available) + "\r\n");
		testReport.append("ռ�ñ�֤��:" + df.format(occupancyBondNetAll) + "\r\n");
		testReport.append("���ᱣ֤��:" + df.format(hold) + "\r\n");
		testReport.append("δʵ��ӯ��:" + df.format(unrealizedAll) + "\r\n");
		testReport.append("������ʵ��:" + df.format(realizedAll) + "\r\n");
		testReport.append("����������:" + df.format(feeCurrent) + "\r\n");
		testReport.append("--------------------\r\n");
		testReport.append("��ʼ�ʽ�:" + df.format(initialAssets) + "\r\n");
		testReport.append("balance:" + df.format(balance) + "\r\n");
		testReport.append("�˻���λ��¶ͷ��:" + df.format(netPositionAll) + "\r\n");
		testReport.append("�˻���սϴ��λֵ:" + df.format(realPositionAll) + "\r\n");
		testReport.append("ά�ֱ�֤��:" + df.format(realPositionAll * MM) + "\r\n");
		testReport.append("��������(������):" + df.format(feeAll) + "\r\n");

		testReport.append("����������������������\r\n\r\n");

		System.out.print(testReport);
		System.out.print(testDetail);
		StringBuffer testResult = testReport.append(testDetail).append(testInput);
		return testResult;
	}

	// �������ֵ�ķ���
	public static double getMax(double a, double b) {
		return a > b ? a : b;
	}
}
