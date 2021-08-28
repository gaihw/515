package service.business;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import config.ConfigCenter;
import utils.GetInput;
import utils.ResultLog;

/**
 * 
 * @author Muguozheng
 * @date 2018��10��24��
 * @version 3.0
 * @description ���λ�йص�ָ�����㣬֧�ֶ����˫�򿪲�
 * @modify 1��2018/11/20:����ƽ���߼�;2��2018/12/17:����ǿƽ�ۼ����߼�
 */

public class RiskDegree {

	static StringBuffer testDetail = new StringBuffer();
	static StringBuffer testReport = new StringBuffer();
	static StringBuffer testInput = new StringBuffer();

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		// ��������
		Properties prop = ConfigCenter.dataParser();
		// �����ֵı�Ǽ۸�,�����Ǽ۸��б仯,�輰ʱ����,"-1"ռλ��
		double[] indexPrices = { -1, 4000, 2.6, 150, 31, 0.35, 5.08, 79.9, 150, 86 };
		double taker = 0.00015; // �����ɽ�������
		double maker = 0.00015; // �����ɽ�������
		String[] tableNames = { "ERROR", "BTC", "EOS", "ETH", "LTC", "XRP", "ETC", "DASH", "BCH", "BSV" };
		// ��ֵ��
//		double initialAssets = 100; // ��ʼ�ʽ�
//		int[] contractIds = { 1}; // 1-7�ֱ�ΪBTC��EOS��ETH��LTC��XRP��ETC��DASH
//		double[][] prices = { { 3950.4 }}; // ���ּ�,��������ƽ��
//		double[][] sizes = { { 10 }}; // ����,����Ϊ��
//		int[][] leverages = { { 100 } }; // �ܸ�,��������δ�ɽ�
//		double[][] feeTypes = { { taker }}; // taker:�����ɽ�,maker:�����ɽ�������
//		double[] sellPrices = { 3.1 }; // ��һ�۸�,��������㶳�ᱣ֤��,����ɺ���
//		// ��ֵ��
		double initialAssets = 100; // ��ʼ�ʽ�
		int[] contractIds = { 1, 8, 9 }; // 1-7�ֱ�ΪBTC��EOS��ETH��LTC��XRP��ETC��DASH
		double[][] prices = { { 3950.4 }, { 160, 130, -130 }, { 103 } }; // ���ּ�,��������ƽ��
		double[][] sizes = { { 10 }, { 10, -10, -10 }, { 10 } }; // ����,����Ϊ��
		int[][] leverages = { { 100 }, { 100, 100, 100 }, { 100 } }; // �ܸ�,��������δ�ɽ�
		double[][] feeTypes = { { taker }, { taker, taker, taker }, { taker } }; // taker:�����ɽ�,maker:�����ɽ�������
		double[] sellPrices = { 3.1 }; // ��һ�۸�,��������㶳�ᱣ֤��,����ɺ���

		// ִ����
		StringBuffer testResult = RiskDegree.getPosition(tableNames, initialAssets, contractIds, indexPrices,
				sellPrices, prices, sizes, leverages, feeTypes);

		// ��¼��
		String resultFile = prop.getProperty("resultFile");
		String allTestResult = prop.getProperty("allTestResult");

		ResultLog.writeResult(testResult, resultFile, "reWrite");
		ResultLog.writeResult(testResult, allTestResult, "Write");
		long end = System.currentTimeMillis();
		System.out.println("���Ժ�ʱ:" + (end - start) + "ms");

	}

	public static StringBuffer getPosition(String[] tableNames, double initialAssets, int[] contractIds,
			double[] indexPrices, double[] sellPrices, double[][] prices, double[][] sizes, int[][] leverages,
			double[][] feeTypes) throws IOException {
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
		double positionValueSell = 0; // �ղֲ�λ�ܼ�ֵ
		double positionValueNet = 0; // ��λ��ֵ
		double positionValueNetAll = 0; // ���б��ֵĲ�λ��ֵ
		double positionValueReal = 0; // ��ղֽϴ�ֵ
		double positionValueRealAll = 0; // ���б��ֶ�ղֽϴ�ֵ֮��
		double positionValueAll = 0; // ���б��ֶ�ղּ�ֵ֮��

		double averagePriceBuy = 0; // ��ǰ��ֲֳ־���
		double averagePriceSell = 0; // ��ǰ�ղֲֳ־���
		double leverageBuy = 0; // �������ʵ�ʸܸ�
		double leverageSell = 0; // �ղ�����ʵ�ʸܸ�

		double hold = 0; // ���ᱣ֤��
		double occupancyMarginAll = 0; // ռ�ñ�֤���ܺ�
		double occupancyMarginBuy = 0; // ���ռ�ñ�֤��
		double occupancyMarginSell = 0; // �ղ�ռ�ñ�֤��
		double occupancyMarginNet = 0; // ����λ��֤��,ȡ����յĽϴ�ֵ
		double occupancyMarginNetAll = 0; // ���б��ֵľ���λ��֤��

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
		double liquidationPrice = 0; // ǿƽ�۸�
		// ��¼��ղ�λ�ϴ�ֵ,��������ǿƽ��
		HashMap<Integer, ArrayList<Double>> positions = new HashMap<Integer, ArrayList<Double>>();

		// ��ʽ��
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.########");
		// ���д���
		int testNum = Integer.valueOf((String) ConfigCenter.executeParser().get("testNum"));

		for (int i = 0; i < contractIds.length; i++) {
			// ���㿪������
			double temp1 = 0;
			double temp2 = 0;
			int buyNum = 0; // �������
			int sellNum = 0; // �ղ�����
			for (int j = 0; j < prices[i].length; j++) {
				// // ���ֵ�,price������������
				if (prices[i][j] > 0) {
					if (leverages[i][j] > 0) {
						// ��ֿ���
						if (sizes[i][j] > 0) {
							// �����Ϸ���У�
							if (leverages[i][j] <= 0 || contractIds[i] <= 0 || contractIds[i] > 9
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
							occupancyMarginBuy = occupancyMarginBuy
									+ (prices[i][j] / leverages[i][j]) * sizes[i][j] * ratios[contractIds[i]];
							// ʵ�ʳֲָܸ�
							temp1 = temp1 + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] / leverages[i][j];
							// ���³ֲָܸ�
							leverageBuy = positionValueBuy / temp1;
							// ���³ֲ־���
							averagePriceBuy = positionValueBuy / positionSizeBuy;
							// ���������
							openFeeBuy = openFeeBuy
									+ prices[i][j] * sizes[i][j] * ratios[contractIds[i]] * feeTypes[i][j];
							// ƽ�������� --����,�̶�ȡtaker
							closeFeeBuy = closeFeeBuy + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] * 0.00015;
							// ����������
							feeCurrent = feeCurrent
									+ prices[i][j] * sizes[i][j] * ratios[contractIds[i]] * feeTypes[i][j];
							// ���δʵ��ӯ��
							unrealizedBuy = (indexPrices[contractIds[i]] - averagePriceBuy) * sizeBuy
									* ratios[contractIds[i]];
							buyNum++;

						} else if (sizes[i][j] < 0) {
							// �����Ϸ���У�
							if (contractIds[i] <= 0 || contractIds[i] > 8) {
								System.out.println("openPrice��size��leverage��ratios��contractId�Ȳ��������Ϲ���,�����");
								return null;
							}
							// �ղ��ܲ�
							sizeSell = sizeSell - sizes[i][j];

							// ��λ����
							positionSizeSell = positionSizeSell + sizes[i][j] * (-1) * ratios[contractIds[i]];
							// ��λ�ܼ�ֵ
							// positionValueSell = positionValueSell
							// + prices[i][j] * Math.abs(sizes[i][j]) *
							// ratios[contractIds[i]];
							positionValueSell = positionValueSell + prices[i][j] * sizes[i][j] * ratios[contractIds[i]];
							// ռ�ñ�֤��
							occupancyMarginSell = occupancyMarginSell
									+ (prices[i][j] / leverages[i][j]) * sizes[i][j] * (-1) * ratios[contractIds[i]];
							// ʵ�ʳֲָܸ�
							temp2 = temp2
									+ prices[i][j] * Math.abs(sizes[i][j]) * ratios[contractIds[i]] / leverages[i][j];
							// ���³ֲָܸ�
							leverageSell = Math.abs(positionValueSell) / temp2;
							// ���³ֲ־���
							averagePriceSell = Math.abs(positionValueSell) / positionSizeSell;
							// �ղ�������
							openFeeSell = openFeeSell
									+ prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] * feeTypes[i][j];
							// ƽ�������� --����,�̶�ȡtaker
							closeFeeSell = closeFeeSell
									+ prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] * 0.00015;
							// ����������
							feeCurrent = feeCurrent
									+ prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] * feeTypes[i][j];
							// �ղ�δʵ��ӯ��
							unrealizedSell = (averagePriceSell - indexPrices[contractIds[i]]) * sizeSell
									* ratios[contractIds[i]];
							sellNum++;
						}
					} else {
						// ��ȫδ�ɽ�ί�е����ᱣ֤��
						hold = hold + (Math.max(prices[i][j], sellPrices[i]) / leverages[i][j] * (-1)
								+ 0.00015 * Math.max(prices[i][j], sellPrices[i]) * 2) * Math.abs(sizes[i][j])
								* ratios[contractIds[i]];
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
						closeFeeSell = closeFeeSell - (averagePriceSell * 0.00015 + prices[i][j] * feeTypes[i][j])
								* sizes[i][j] * ratios[contractIds[i]];
						// ����������
						feeCurrent = feeCurrent
								+ prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] * feeTypes[i][j];
						// ��λ�ܲ� ����
						sizeSell = sizeSell - sizes[i][j];
						// ��λ���� �²�λ���� = ԭ��λ���� - ƽ�ֲ�������
						// positionSizeSell = positionSizeSell + sizes[i][j] *
						// ratios[contractIds[i]];
						positionSizeSell = positionSizeSell - sizes[i][j] * ratios[contractIds[i]];
						// ��λ�ܼ�ֵ �²�λ�ܼ�ֵ = �ֲ־���*��λ����
						positionValueSell = averagePriceSell * positionSizeSell;
						// ռ�ñ�֤�� ��ռ�ñ�֤�� = ԭռ�ñ�֤�� - ƽ�ֲ���ռ�ñ�֤��
						occupancyMarginSell = (averagePriceSell / leverageSell) * positionSizeSell;

					} else if (sizes[i][j] < 0) { // ����ƽ��
						// ����У��
						if (leverageBuy == 0
								|| Math.abs(sizes[i][j]) * ratios[contractIds[i]] > Math.abs(positionSizeBuy)) {
							System.out.println("������ֿ�ƽ����");
							return null;
						}
						// ƽ�������� --ƽ��ʱ�ȷ��������ƽ��Ӷ��,�ٰ�ʵ�ʳɽ��۳�ƽ��������
						closeFeeBuy = closeFeeBuy - (averagePriceBuy * 0.00015 + prices[i][j] * feeTypes[i][j]) * (-1)
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
						// positionSizeBuy = positionSizeBuy + sizes[i][j] *
						// ratios[contractIds[i]];
						positionSizeBuy = positionSizeBuy + sizes[i][j] * ratios[contractIds[i]];
						// ��λ�ܲ�
						sizeBuy = sizeBuy + sizes[i][j];
						// ��λ�ܼ�ֵ �²�λ�ܼ�ֵ = �ֲ־���*��λ����
						positionValueBuy = averagePriceBuy * positionSizeBuy;
						// ռ�ñ�֤�� ��ռ�ñ�֤�� = ԭռ�ñ�֤�� - ƽ�ֲ���ռ�ñ�֤��
						occupancyMarginBuy = (averagePriceBuy / leverageBuy) * positionSizeBuy;
						// ʵ�ʳֲָܸ�
						temp1 = temp1 + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] / leverages[i][j];
					}
				}

				// ������δʵ��ӯ��
				unrealized = unrealizedBuy + unrealizedSell;
				// ��������ʵ��ӯ��
				realized = realizedBuy + realizedSell;
			}
			// �ܵĲ�λռ�ñ�֤��
			occupancyMarginAll = occupancyMarginAll + occupancyMarginBuy + occupancyMarginSell;
			// ��λ��ֵ
			positionValueNet = Math.abs(positionValueBuy + positionValueSell);
			// �����ֶ�ղֽϴ�ֵ
			positionValueReal = getMax(positionValueBuy, Math.abs(positionValueSell));
			// �����ֶ��֮��
			positionValueAll = positionValueAll + positionValueBuy + Math.abs(positionValueSell);
			// ��¼�����ֶ�սϴ�ֵ
			// positionReals�����վ�ͷ���������ϴ�ֵ���Ͷ�վ���λֵ���ϴ�ֵ��
			ArrayList<Double> positionReals = new ArrayList<Double>();
			// ��ͷռ����
			if (positionValueBuy + positionValueSell >= 0) {
				positionReals.add(sizeBuy - sizeSell);
				positionReals.add(positionValueBuy);
			} else { // ��ͷռ����
				positionReals.add(sizeBuy - sizeSell);
				positionReals.add(positionValueSell);
			}
			positions.put(contractIds[i], positionReals);

			// �����ܵ�������
			feeAll = feeAll + openFeeBuy + closeFeeBuy + openFeeSell + closeFeeSell;
			// �����ֵ�δʵ��ӯ����
			unrealizedBuyRate = (indexPrices[contractIds[i]] - averagePriceBuy) / averagePriceBuy;
			// ����ղֵ�δʵ��ӯ����
			unrealizedSellRate = (averagePriceSell - indexPrices[contractIds[i]]) / averagePriceSell;
			// �������б��ֵĲ�λ��ֵ
			positionValueNetAll = positionValueNetAll + Math.abs(positionValueNet);
			// �������б��ֵĶ�ղ�λ�ϴ�ֵ
			positionValueRealAll = positionValueRealAll + positionValueReal;
			// �������б��ֵ�δʵ��ӯ��
			unrealizedAll = unrealizedAll + unrealized;
			// �������б��ֵ�ʵ��ӯ��
			realizedAll = realizedAll + realized;
			// ��ȡ�����־���λ��֤��
			occupancyMarginNet = getMax(occupancyMarginBuy, occupancyMarginSell);
			// ���б��ֵľ���λ��֤��
			occupancyMarginNetAll = occupancyMarginNetAll + occupancyMarginNet;

			testDetail.append("=======" + tableNames[contractIds[i]] + "��λ��Ϣ=======\r\n");
			testDetail.append(tableNames[contractIds[i]] + "��Ǽ۸�:" + indexPrices[contractIds[i]] + "\r\n");
			testDetail.append("------" + tableNames[contractIds[i]] + "�ܲ�λ��Ϣ" + "------\r\n");
			testDetail.append("����δʵ��ӯ��:" + df.format(unrealized) + "\r\n");
			testDetail.append("���ֲ�λ��ͷ��(��):" + df.format(positionValueNet) + "\r\n");
			testDetail.append("���ֲ�λ��ͷ��(Max):" + df.format(positionValueReal) + "\r\n");
			testDetail.append("����ռ�ñ�֤��:" + df.format(occupancyMarginNet) + "\r\n");

			if (buyNum > 0) {
				testDetail.append("-------��ֲ�λ��Ϣ-------\r\n");
				testDetail.append("��ֲֳ־���:" + df.format(averagePriceBuy) + "\r\n");
				testDetail.append("��ֿ�������:" + df.format(positionSizeBuy) + "\r\n");
				testDetail.append("����ܲ�:" + sizeBuy + "\r\n");
				testDetail.append("��ֲ�λ��ֵ:" + df.format(positionValueBuy) + "\r\n");
				testDetail.append("���ʵ�ʸܸ�:" + df.format(leverageBuy) + "\r\n");
				testDetail.append("��ֿ���������:" + df.format(openFeeBuy) + "\r\n");
				testDetail.append("���ƽ��������:" + df.format(closeFeeBuy) + "\r\n");
				testDetail.append("���ռ�ñ�֤��:" + df.format(occupancyMarginBuy) + "\r\n");
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
				testDetail.append("�ղ�ռ�ñ�֤��:" + df.format(occupancyMarginSell) + "\r\n");
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
			occupancyMarginBuy = 0;
			openFeeBuy = 0;
			closeFeeBuy = 0;
			openFeeSell = 0;
			closeFeeSell = 0;
			positionSizeSell = 0;
			positionValueSell = 0;
			occupancyMarginSell = 0;
			positionValueNet = 0;
			positionValueReal = 0;
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

		testReport.append("��������" + testNum + "�β��Ա���������\r\n");
		testReport.append("���Կ�ʼʱ��:" + dateFormat.format(startTime) + "\r\n");
		testReport.append("����ִ����:" + ConfigCenter.executeProperties.get("testExecutor") + "\r\n\r\n");

		// ���㵱ǰ�ʲ�
		currentAssets = initialAssets + unrealizedAll + realizedAll - feeAll;
		// Ǯ�����
		balance = currentAssets - unrealizedAll - occupancyMarginNetAll;
		// ����
		available = currentAssets - occupancyMarginNetAll - hold;
		// ������ն�
		riskDegree = positionValueRealAll * MM / (currentAssets - hold);

		testReport.append("����������˻���Ϣ���������\r\n");
		testReport.append("���ն�:" + df.format(riskDegree * 100) + "%\r\n");

		System.out.println(positions);
		// ���������ǿƽ�۸�
		for (Map.Entry<Integer, ArrayList<Double>> entry : positions.entrySet()) {
			// �ʽ����Ȩ��
			double weight = Math.abs(entry.getValue().get(1) / positionValueRealAll);
			// Ԥ��ǿƽ�۸����
			liquidationPrice = indexPrices[entry.getKey()] - (currentAssets - positionValueRealAll * MM) * weight
					/ (entry.getValue().get(0) * ratios[entry.getKey()]);
			// System.out.println(indexPrices[entry.getKey()] + "-(" +
			// currentAssets + "-" + positionValueRealAll + "*"
			// + MM + ")*" + weight + "/(" + entry.getValue().get(0) + "*" +
			// ratios[entry.getKey()] + ")");
			testReport.append(tableNames[entry.getKey()] + "Ԥ��ǿƽ��:" + df.format(liquidationPrice) + "\r\n");
		}

		testReport.append("------�ҵĺ�Լ�ʲ�------\r\n");
		testReport.append("��ǰ�ʲ�:" + df.format(currentAssets) + "\r\n");
		testReport.append("�����ʲ�:" + df.format(available) + "\r\n");
		testReport.append("ռ�ñ�֤��:" + df.format(occupancyMarginNetAll) + "\r\n");
		testReport.append("���ᱣ֤��:" + df.format(hold) + "\r\n");
		testReport.append("δʵ��ӯ��:" + df.format(unrealizedAll) + "\r\n");
		testReport.append("������ʵ��:" + df.format(realizedAll) + "\r\n");
		testReport.append("����������:" + df.format(feeCurrent) + "\r\n");
		testReport.append("--------------------\r\n");
		testReport.append("��ʼ�ʽ�:" + df.format(initialAssets) + "\r\n");
		testReport.append("balance:" + df.format(balance) + "\r\n");
		testReport.append("�˻���λ��¶ͷ��:" + df.format(positionValueNetAll) + "\r\n");
		testReport.append("�˻���սϴ��λֵ:" + df.format(positionValueRealAll) + "\r\n");
		testReport.append("ά�ֱ�֤��:" + df.format(positionValueRealAll * MM) + "\r\n");
		testReport.append("��������(������):" + df.format(feeAll) + "\r\n");

		testReport.append("����������������������\r\n\r\n");

		// �޸����д���
		testNum++;
		ResultLog.modifyFile(ConfigCenter.executeProFile, "testNum", String.valueOf(testNum));

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