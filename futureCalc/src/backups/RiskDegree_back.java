package backups;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import config.ConfigCenter;
import utils.GetInput;
import utils.ResultLog;

/**
 * 
 * @author Muguozheng
 * @date 2018��10��24��
 * @version 2.0
 * @description ���λ�йص�ָ�����㣬֧�ֶ����˫�򿪲�
 */
public class RiskDegree_back {

	static StringBuffer testDetail = new StringBuffer();
	static StringBuffer testReport = new StringBuffer();
	static StringBuffer testInput = new StringBuffer();

	public static void main(String[] args) throws FileNotFoundException, IOException {
		long start = System.currentTimeMillis();
		// ��������
		// { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 }
		Properties prop = ConfigCenter.dataParser();
		// �����ֵı�Ǽ۸�,�����Ǽ۸��б仯,�輰ʱ����,"-1"ռλ��
		double[] indexPrices = { -1, 6444, 5.035, 500, 200, 100, 0.4, 9.8 };
		double taker = 0.0006; // �����ɽ�������
		double maker = 0.0003; // �����ɽ�������
		String[] tableNames = { "ERROR", "BTC", "EOS", "BCH", "ETH", "LTC", "XRP", "ETC" };

		// ��ֵ��
		double initialAssets = 350; // ��ʼ�ʽ�
		int[] contractIds = { 4 }; // 1-7�ֱ�ΪBTC��EOS��BCH��ETH��LTC��XRP��ETC
		double[][] prices = { { 210, 205, 220, 190 } }; // ���ּ�
		double[][] sizes = { { 60, 80, -100, 120 } }; // ����������Ϊ��
		int[][] leverages = { { 100, 100, 100, 100 } }; // �ܸ�
		double[][] feeTypes = { { taker, taker, taker, taker } }; // taker:�����ɽ�,maker:�����ɽ�������

		// ִ����
		StringBuffer testResult = RiskDegree_back.getPosition(tableNames, initialAssets, contractIds, indexPrices,
				prices, sizes, leverages, feeTypes);

		// ��¼��
		String resultFile = prop.getProperty("resultFile");
		String allTestResult = prop.getProperty("allTestResult");

		ResultLog.writeResult(testResult, resultFile, "reWrite");
		ResultLog.writeResult(testResult, allTestResult, "Write");
		long end = System.currentTimeMillis();
		System.out.println("���Ժ�ʱ:" + (end - start) + "ms");
	}

	public static StringBuffer getPosition(String[] tableNames, double initialAssets, int[] contractIds,
			double[] indexPrices, double[][] prices, double[][] sizes, int[][] leverages, double[][] feeTypes)
					throws FileNotFoundException, IOException {
		// ��¼��ʼʱ��
		long startTime = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ

		// ��������������
		// �������ֺ�Լ���� ����Ϊ:BTC��EOS��BCH��ETH��LTC��XRP��ETC
		// { -1, 0.002, 2, 0.02, 0.05, 0.2, 40, 1 };
		double[] ratios = ConfigCenter.getRatios();
		double MM = 0.005; // ά�ֱ�֤����

		// ������
		double holdAll = 0; // ��ȫδ�ɽ�ί�е����ᱣ֤��֮��
		double buyPrice = 4.5; // ��һ��
		double sellPrice = 4.8; // ��һ��

		double currentAssets = 0; // ��ǰ�ʲ�
		double balance = 0;
		double available = 0; // ����

		double positionSizeBuy = 0; // ��ֲ�λ����
		double positionSizeSell = 0; // �ղֲ�λ����

		double positionValueBuy = 0; // ��ֲ�λ�ܼ�ֵ
		double positionValueSell = 0; // ��ֲ�λ�ܼ�ֵ
		double netPosition = 0; // ��λ��ֵ
		double netPositionAll = 0; // ���б��ֵĲ�λ��ֵ
		double realPosition = 0; // ��ղֽϴ�ֵ
		double realPositionAll = 0; // ���б��ֶ�ղֽϴ�ֵ֮��

		double averagePriceBuy = 0; // ��ֲֳ־���
		double averagePriceSell = 0; // �ղֲֳ־���

		double actualLeverageBuy = 0; // ���ʵ�ʸܸ�
		double actualLeverageSell = 0; // �ղ�ʵ�ʸܸ�

		double occupancyBondAll = 0; // ռ�ñ�֤���ܺ�
		double occupancyBondBuy = 0; // �ղ�ռ�ñ�֤��
		double occupancyBondSell = 0; // �ղ�ռ�ñ�֤��
		double occupancyBondNet = 0; // ����λ��֤��,ȡ����յĽϴ�ֵ
		double occupancyBondNetAll = 0; // ���б��ֵľ���λ��֤��

		double unrealizedBuy = 0; // ���δʵ��ӯ��
		double unrealizedSell = 0; // �ղ�δʵ��ӯ��
		double unrealizedBuyRate = 0; // ���δʵ��ӯ����
		double unrealizedSellRate = 0; // �ղ�δʵ��ӯ����
		double unrealized = 0; // ������ȫ����λδʵ��ӯ��
		double unrealizedAll = 0; // ȫ�����ֵ�δʵ��ӯ��

		double openFeeBuy = 0; // ��ֿ���������
		double openFeeSell = 0; // �ղֿ���������

		double closeFeeBuy = 0; // ���ƽ��������
		double closeFeeSell = 0; // �ղ�ƽ��������
		double feeAll = 0; // �ܵ�������
		double riskDegree = 0; // ���ն�

		// ��ʽ��
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.######");
		// ���д���
		int testNum = Integer.valueOf((String) ConfigCenter.executeParser().get("testNum"));

		for (int i = 0; i < contractIds.length; i++) {
			// ���㿪������
			double temp1 = 0;
			double temp2 = 0;
			int buyNum = 0; // �������
			int sellNum = 0; // �ղ�����
			for (int j = 0; j < prices[i].length; j++) {
				if (sizes[i][j] > 0) { // �����Ϣ
					// �����Ϸ���У�
					if (leverages[i][j] <= 0 || contractIds[i] <= 0 || contractIds[i] > 7 || ratios[contractIds[i]] < 0
							|| indexPrices[contractIds[i]] < 0) {
						System.out.println("�۸��������ܸˡ�contractId���������Ϲ���,�����");
						return null;
					}
					// ���ֵ���price��������δ�ɽ���ί�е�
					if (prices[i][j] > 0) {
						// ��λ����
						positionSizeBuy = positionSizeBuy + sizes[i][j] * ratios[contractIds[i]];
						// ��λ�ܼ�ֵ
						positionValueBuy = positionValueBuy + prices[i][j] * sizes[i][j] * ratios[contractIds[i]];
						// ռ�ñ�֤��
						occupancyBondBuy = occupancyBondBuy
								+ (prices[i][j] / leverages[i][j]) * sizes[i][j] * ratios[contractIds[i]];
						// ʵ�ʳֲָܸ�
						temp1 = temp1 + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] / leverages[i][j];
						// ���������
						openFeeBuy = openFeeBuy + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] * feeTypes[i][j];
						// ƽ�������� --����,�̶�ȡtaker
						closeFeeBuy = closeFeeBuy + prices[i][j] * sizes[i][j] * ratios[contractIds[i]] * 0.0006;
						// ���δʵ��ӯ��
						unrealizedBuy = unrealizedBuy + (indexPrices[contractIds[i]] - prices[i][j]) * sizes[i][j] * 1
								* ratios[contractIds[i]];
						buyNum++;
					} else {
						// ��ȫδ�ɽ�ί�е����ᱣ֤��
						holdAll = holdAll + (Math.max(buyPrice, sellPrice) / leverages[i][j]
								+ 0.0006 * Math.max(buyPrice, sellPrice) + 0.0006 * Math.max(buyPrice, sellPrice))
								* Math.abs(Math.abs(sizes[i][j])) * ratios[contractIds[i]];
					}
				} else if (sizes[i][j] < 0) { // �ղ���Ϣ
					// �����Ϸ���У�
					if (leverages[i][j] <= 0 || contractIds[i] <= 0 || contractIds[i] > 7 || ratios[contractIds[i]] < 0
							|| indexPrices[contractIds[i]] < 0) {
						System.out.println("openPrice��size��leverage��ratios��contractId�Ȳ��������Ϲ���,�����");
						return null;
					}
					if (prices[i][j] > 0) {
						// ��λ����
						positionSizeSell = positionSizeSell + sizes[i][j] * (-1) * ratios[contractIds[i]];
						// ��λ�ܼ�ֵ
						positionValueSell = positionValueSell
								+ prices[i][j] * Math.abs(sizes[i][j]) * ratios[contractIds[i]];
						// ռ�ñ�֤��
						occupancyBondSell = occupancyBondSell
								+ (prices[i][j] / leverages[i][j]) * sizes[i][j] * (-1) * ratios[contractIds[i]];
						// ʵ�ʳֲָܸ�
						temp2 = temp2 + prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] / leverages[i][j];
						// �ղ�������
						openFeeSell = openFeeSell
								+ prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] * feeTypes[i][j];
						// ƽ�������� --����,�̶�ȡtaker
						closeFeeSell = closeFeeSell
								+ prices[i][j] * sizes[i][j] * (-1) * ratios[contractIds[i]] * 0.0006;
						// �ղ�δʵ��ӯ��
						unrealizedSell = unrealizedSell
								+ (indexPrices[contractIds[i]] - prices[i][j]) * sizes[i][j] * ratios[contractIds[i]];
						sellNum++;
					} else {
						// ��ȫδ�ɽ�ί�е����ᱣ֤��
						holdAll = holdAll + (Math.max(sellPrice, buyPrice) / leverages[i][j]
								+ 0.0006 * Math.max(sellPrice, buyPrice) + 0.0006 * Math.max(sellPrice, buyPrice))
								* Math.abs(Math.abs(sizes[i][j])) * ratios[contractIds[i]];
						System.out.println("holdAll----:" + holdAll);
					}
				} else {
					System.out.println("�µ�������д����,����д1���൥���򣩻���-1���յ�����");
					return null;
				}
				// ������δʵ��ӯ��
				unrealized = unrealizedBuy + unrealizedSell;
			}

			// �ܵĲ�λ��֤��
			occupancyBondAll = occupancyBondAll + occupancyBondBuy + occupancyBondSell;
			// ��λ��ֵ
			netPosition = Math.abs(positionValueBuy - positionValueSell);
			// �����ֶ�ղֽϴ�ֵ
			realPosition = getMax(positionValueBuy, positionValueSell);
			// �����ܵ�������
			feeAll = feeAll + openFeeBuy + closeFeeBuy + openFeeSell + closeFeeSell;
			// ���㵥���ֵ�ʵ�ʸܸ�
			actualLeverageBuy = positionValueBuy / temp1;
			actualLeverageSell = positionValueSell / temp2;
			// ���㵥���ֵĶ�ֲֳ־���
			averagePriceBuy = positionValueBuy / positionSizeBuy;
			// ���㵥���ֵĿղֲֳ־���
			averagePriceSell = positionValueSell / positionSizeSell;
			// �����ֵ�δʵ��ӯ����
			unrealizedBuyRate = (indexPrices[contractIds[i]] - averagePriceBuy) / averagePriceBuy * actualLeverageBuy;
			// �����ֵ�δʵ��ӯ����
			unrealizedSellRate = (averagePriceSell - indexPrices[contractIds[i]]) / averagePriceSell
					* actualLeverageSell;
			// �������б��ֵĲ�λ��ֵ
			netPositionAll = netPositionAll + Math.abs(netPosition);
			// �������б��ֵĶ�ղ�λ�ϴ�ֵ
			realPositionAll = realPositionAll + realPosition;
			// �������б��ֵ�δʵ��ӯ��
			unrealizedAll = unrealizedAll + unrealized;
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
				testDetail.append("��ֲ�λ��ֵ:" + df.format(positionValueBuy) + "\r\n");
				testDetail.append("���ʵ�ʸܸ�:" + df.format(actualLeverageBuy) + "\r\n");
				testDetail.append("��ֿ���������:" + df.format(openFeeBuy) + "\r\n");
				testDetail.append("���ƽ��������:" + df.format(closeFeeBuy) + "\r\n");
				testDetail.append("���ռ�ñ�֤��:" + df.format(occupancyBondBuy) + "\r\n");
				testDetail.append("���δʵ��ӯ��:" + df.format(unrealizedBuy) + "\r\n");
				testDetail.append("���δʵ��ӯ����:" + df.format(unrealizedBuyRate * 100) + "%\r\n");
			}

			if (sellNum > 0) {
				testDetail.append("-------�ղֲ�λ��Ϣ-------\r\n");
				testDetail.append("�ղֲֳ־���:" + df.format(averagePriceSell) + "\r\n");
				testDetail.append("�ղֿ�������:" + df.format(positionSizeSell) + "\r\n");
				testDetail.append("�ղֲ�λ��ֵ:" + df.format(positionValueSell) + "\r\n");
				testDetail.append("�ղ�ʵ�ʸܸ�:" + df.format(actualLeverageSell) + "\r\n");
				testDetail.append("�ղֿ���������:" + df.format(openFeeSell) + "\r\n");
				testDetail.append("�ղ�ƽ��������:" + df.format(closeFeeSell) + "\r\n");
				testDetail.append("�ղ�ռ�ñ�֤��:" + df.format(occupancyBondSell) + "\r\n");
				testDetail.append("�ղ�δʵ��ӯ��:" + df.format(unrealizedSell) + "\r\n");
				testDetail.append("�ղ�δʵ��ӯ����:" + df.format(unrealizedSellRate * 100) + "%\r\n");
			}

			testDetail.append("======================\r\n\r\n");

			// �����ݹ���
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
			unrealized = 0;
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
		currentAssets = initialAssets + unrealizedAll - feeAll;
		// Ǯ�����
		balance = currentAssets - unrealizedAll - occupancyBondNetAll;
		// ����
		available = currentAssets - occupancyBondNetAll - holdAll;
		// ������ն�
		riskDegree = realPositionAll * MM / (currentAssets - holdAll);

		testReport.append("����������˻���Ϣ���������\r\n");
		testReport.append("��ʼ�ʽ�:" + df.format(initialAssets) + "\r\n");
		testReport.append("��������:" + df.format(feeAll) + "\r\n");
		testReport.append("��ǰ�ʲ�:" + df.format(currentAssets) + "\r\n");
		testReport.append("balance:" + df.format(balance) + "\r\n");
		testReport.append("�����ʲ�:" + df.format(available) + "\r\n");
		testReport.append("���ᱣ֤��:" + df.format(holdAll) + "\r\n");
		testReport.append("�˻���λ��¶ͷ��:" + df.format(netPositionAll) + "\r\n");
		testReport.append("�˻���սϴ��λֵ:" + df.format(realPositionAll) + "\r\n");
		testReport.append("�˻�ռ�ñ�֤��:" + df.format(occupancyBondNetAll) + "\r\n");
		testReport.append("ӯ���ܼ�:" + df.format(unrealizedAll) + "\r\n");
		testReport.append("���ն�:" + df.format(riskDegree * 100) + "%\r\n");
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
