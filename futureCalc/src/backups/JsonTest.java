package backups;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;

public class JsonTest {
	public static void main(String[] args) {
		String test = "{\"indexPrices\" : [3000, 2, 100, 12, 0.4, 4, 12],\"sellPrices\" : [3100, 2.1, 105, 13, 0.44, 4.3, 13],\"initialAssets\" : 100,\"orders\" : [{\"contractId\" : 1,\"prices\" : [3000, 3200],\"sizes\" : [30, 40],\"leverages\" : [100, 200],\"feeRates\" : [0.0003, 0.0003]}, {\"contractId\" : 2,\"prices\" : [3000, 3200],\"sizes\" : [30, 40],\"leverages\" : [100, 200],\"feeRates\" : [0.0003, 0.0003]}]}";
		
		JSONObject parse = JSON.parseObject(test);
		System.out.println(getPosition(parse));
	}

	public static String getPosition(JSONObject object) {
		StringBuffer testDetail = new StringBuffer();
		StringBuffer testReport = new StringBuffer();
		StringBuffer testInput = new StringBuffer();

		JSONObject o = JSON.parseObject(object.toString());
		JSONArray indexPrices = o.getJSONArray("indexPrices");
		JSONArray sellPrices = o.getJSONArray("sellPrices");
		double initialAssets = o.getDouble("initialAssets");
		JSONArray orders = o.getJSONArray("orders");

		System.out.println(orders);

		String[] ratios = { "-1", "0.002", "2" };
		String[] tableNames = { "ERROR", "BTC", "EOS" };
		// ��¼��ʼʱ��
		long startTime = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// ���Է�����޸����ڸ�ʽ

		// ��������������
		// �������ֺ�Լ���� ����Ϊ:BTC��EOS��ETH��LTC��XRP��ETC
		// { -1, 0.002, 2, 0.05, 0.2, 40, 1 };

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

		for (int i = 0; i < orders.size(); i++) {
			int contractId = orders.getJSONObject(i).getInteger("contractId");
			JSONArray prices = orders.getJSONObject(i).getJSONArray("prices");
			JSONArray sizes = orders.getJSONObject(i).getJSONArray("sizes");
			JSONArray leverages = orders.getJSONObject(i).getJSONArray("leverages");
			JSONArray feeRates = orders.getJSONObject(i).getJSONArray("feeRates");
			
			System.out.println(prices);
			System.out.println(sizes);
			System.out.println(leverages);
			System.out.println(feeRates);

			// ���㿪������
			double temp1 = 0;
			double temp2 = 0;
			int buyNum = 0; // �������
			int sellNum = 0; // �ղ�����
			for (int j = 0; j < prices.size(); j++) {
				double price = prices.getDouble(j);
				double size = sizes.getDouble(j);
				double leverage = leverages.getDouble(j);
				double feeRate = feeRates.getDouble(j);
				

				System.out.println(price);
				System.out.println(size);
				System.out.println(leverage);
				System.out.println(feeRate);
				
				// // ���ֵ�,price����������
				if (price > 0) {
					if (leverage > 0) {
						// ��ֿ���
						if (size > 0) {
							// �����Ϸ���У�
							if (leverage <= 0 || contractId <= 0 || contractId > 7
									|| Double.valueOf(ratios[contractId]) < 0
									|| indexPrices.getDouble(contractId) < 0) {
								System.out.println("�۸��������ܸˡ�contractId���������Ϲ���,�����");
								return null;
							}

							// ����ܲ�
							sizeBuy = sizeBuy + size;
							// ��λ����
							positionSizeBuy = positionSizeBuy + size * Double.valueOf(ratios[contractId]);
							// ��λ�ܼ�ֵ
							positionValueBuy = positionValueBuy + price * size * Double.valueOf(ratios[contractId]);
							// ռ�ñ�֤��
							occupancyBondBuy = occupancyBondBuy
									+ (price / leverage) * size * Double.valueOf(ratios[contractId]);
							// ʵ�ʳֲָܸ�
							temp1 = temp1 + price * size * Double.valueOf(ratios[contractId]) / leverage;
							// ���³ֲָܸ�
							leverageBuy = positionValueBuy / temp1;
							// ���³ֲ־���
							averagePriceBuy = positionValueBuy / positionSizeBuy;
							// ���������
							openFeeBuy = openFeeBuy + price * size * Double.valueOf(ratios[contractId]) * feeRate;
							// ƽ�������� --����,�̶�ȡtaker
							closeFeeBuy = closeFeeBuy + price * size * Double.valueOf(ratios[contractId]) * 0.0006;
							// ����������
							feeCurrent = feeCurrent + price * size * Double.valueOf(ratios[contractId]) * feeRate;
							// ���δʵ��ӯ��
							System.out.println(indexPrices.getDouble(contractId) + "--" + averagePriceBuy + "--"
									+ sizeBuy + "--" + ratios[contractId]);
							unrealizedBuy = (indexPrices.getDouble(contractId) - averagePriceBuy) * sizeBuy
									* Double.valueOf(ratios[contractId]);
							buyNum++;

						} else if (size < 0) {
							// �����Ϸ���У�
							if (contractId <= 0 || contractId > 8) {
								System.out.println("openPrice��size��leverage��ratios��contractId�Ȳ��������Ϲ���,�����");
								return null;
							}
							// �ղ��ܲ�
							sizeSell = sizeSell - size;

							// ��λ����
							positionSizeSell = positionSizeSell + size * (-1) * Double.valueOf(ratios[contractId]);
							// ��λ�ܼ�ֵ
							positionValueSell = positionValueSell
									+ price * Math.abs(size) * Double.valueOf(ratios[contractId]);
							// ռ�ñ�֤��
							occupancyBondSell = occupancyBondSell
									+ (price / leverage) * size * (-1) * Double.valueOf(ratios[contractId]);
							// ʵ�ʳֲָܸ�
							temp2 = temp2 + price * Math.abs(size) * Double.valueOf(ratios[contractId]) / leverage;
							// ���³ֲָܸ�
							leverageSell = positionValueSell / temp2;
							// ���³ֲ־���
							averagePriceSell = positionValueSell / positionSizeSell;
							// �ղ�������
							openFeeSell = openFeeSell
									+ price * size * (-1) * Double.valueOf(ratios[contractId]) * feeRate;
							// ƽ�������� --����,�̶�ȡtaker
							closeFeeSell = closeFeeSell
									+ price * size * (-1) * Double.valueOf(ratios[contractId]) * 0.0006;
							// ����������
							feeCurrent = feeCurrent
									+ price * size * (-1) * Double.valueOf(ratios[contractId]) * feeRate;
							// �ղ�δʵ��ӯ��
							unrealizedSell = (indexPrices.getDouble(contractId) - averagePriceSell) * sizeSell * (-1)
									* Double.valueOf(ratios[contractId]);
							sellNum++;
						}
					} else {
						// ��ȫδ�ɽ�ί�е����ᱣ֤��
						hold = hold + (Math.max(price, sellPrices.getDouble(i)) / leverage * (-1)
								+ 0.0006 * Math.max(price, sellPrices.getDouble(i)) * 2) * Math.abs(size)
								* Double.valueOf(ratios[contractId]);
					}
					// price<0����ƽ��
				} else if (price < 0) {
					if (size > 0) { // ����ƽ��
						// ����У��
						if (leverageSell == 0 || size * Double.valueOf(ratios[contractId]) > positionSizeSell) {
							System.out.println("�����ղֿ�ƽ����");
							return null;
						}
						// �ղ�δʵ��ӯ�� --����
						unrealizedSell = unrealizedSell - (averagePriceSell - indexPrices.getDouble(contractId)) * size
								* 1 * Double.valueOf(ratios[contractId]);
						// �ղ���ʵ��ӯ�� --׷��
						realizedSell = realizedSell
								+ (averagePriceSell - Math.abs(price)) * size * 1 * Double.valueOf(ratios[contractId]);
						// ƽ�������� --ƽ��ʱ�ȷ��������ƽ��Ӷ��,�ٰ�ʵ�ʳɽ��۳�ƽ��������
						closeFeeSell = closeFeeSell - (averagePriceSell * 0.0006 + price * feeRate) * size
								* Double.valueOf(ratios[contractId]);
						// ����������
						feeCurrent = feeCurrent + price * size * (-1) * Double.valueOf(ratios[contractId]) * feeRate;
						// ��λ�ܲ� ����
						sizeSell = sizeSell - size;
						// ��λ���� �²�λ���� = ԭ��λ���� - ƽ�ֲ�������
						positionSizeSell = positionSizeSell - size * Double.valueOf(ratios[contractId]);
						// ��λ�ܼ�ֵ �²�λ�ܼ�ֵ = �ֲ־���*��λ����
						positionValueSell = averagePriceSell * positionSizeSell;
						// ռ�ñ�֤�� ��ռ�ñ�֤�� = ԭռ�ñ�֤�� - ƽ�ֲ���ռ�ñ�֤��
						occupancyBondSell = (averagePriceSell / leverageSell) * positionSizeSell;

					} else if (size < 0) { // ����ƽ��
						// ����У��
						if (leverageBuy == 0
								|| Math.abs(size) * Double.valueOf(ratios[contractId]) > Math.abs(positionSizeBuy)) {
							System.out.println("������ֿ�ƽ����");
							return null;
						}
						// ƽ�������� --ƽ��ʱ�ȷ��������ƽ��Ӷ��,�ٰ�ʵ�ʳɽ��۳�ƽ��������
						closeFeeBuy = closeFeeBuy - (averagePriceBuy * 0.0006 + price * feeRate) * (-1) * size
								* Double.valueOf(ratios[contractId]);
						// ����������
						feeCurrent = feeCurrent + price * size * Double.valueOf(ratios[contractId]) * feeRate;
						// �ղ�δʵ��ӯ�� --����
						unrealizedBuy = unrealizedBuy - (averagePriceBuy - indexPrices.getDouble(contractId)) * size
								* Double.valueOf(ratios[contractId]);
						// �ղ���ʵ��ӯ�� --׷��
						realizedBuy = realizedBuy + (Math.abs(price) - averagePriceBuy) * Math.abs(size)
								* Double.valueOf(ratios[contractId]);
						// ��λ���� �²�λ���� = ԭ��λ���� - ƽ�ֲ�������
						positionSizeBuy = positionSizeBuy + size * Double.valueOf(ratios[contractId]);
						// ��λ�ܲ�
						sizeBuy = sizeBuy + size;
						// ��λ�ܼ�ֵ �²�λ�ܼ�ֵ = �ֲ־���*��λ����
						positionValueBuy = averagePriceBuy * positionSizeBuy;
						// ռ�ñ�֤�� ��ռ�ñ�֤�� = ԭռ�ñ�֤�� - ƽ�ֲ���ռ�ñ�֤��
						occupancyBondBuy = (averagePriceBuy / leverageBuy) * positionSizeBuy;
						// ʵ�ʳֲָܸ�
						temp1 = temp1 + price * size * Double.valueOf(ratios[contractId]) / leverage;
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
			unrealizedBuyRate = (indexPrices.getDouble(contractId) - averagePriceBuy) / averagePriceBuy;
			// �����ֵ�δʵ��ӯ����
			unrealizedSellRate = (averagePriceSell - indexPrices.getDouble(contractId)) / averagePriceSell;
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

			testDetail.append("=======" + tableNames[contractId] + "��λ��Ϣ=======\r\n");
			testDetail.append(tableNames[contractId] + "��Ǽ۸�:" + indexPrices.getDouble(contractId) + "\r\n");
			testDetail.append("------" + tableNames[contractId] + "�ܲ�λ��Ϣ" + "------\r\n");
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
		// testInput.append("�����������:\r\n");
		// testInput.append("indexPrices:" + input.getInput(indexPrices) +
		// "\r\n");
		// testInput.append("initialAssets:" + initialAssets + "\r\n");
		// testInput.append("contractIds:" + input.getInput(contractIds) +
		// "\r\n");
		// testInput.append("openPrices:" + input.getInput(prices).toString() +
		// "\r\n");
		// testInput.append("sizes:" + input.getInput(sizes) + "\r\n");
		// testInput.append("leverages:" + input.getInput(leverages) + "\r\n");
		// testInput.append("feeTypes:" + input.getInput(feeTypes) +
		// "\r\n\r\n");

		testReport.append("���Կ�ʼʱ��:" + dateFormat.format(startTime) + "\r\n");

		// ���㵱ǰ�ʲ�
		currentAssets = initialAssets + unrealizedAll + realizedAll - feeAll;
		// Ǯ�����
		balance = currentAssets - unrealizedAll - occupancyBondNetAll;
		// ����
		available = currentAssets - occupancyBondNetAll - hold;
		// ������ն�
		riskDegree = realPositionAll * MM / (currentAssets - hold);

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
		return testResult.toString();
	}

	// �������ֵ�ķ���
	public static double getMax(double a, double b) {
		return a > b ? a : b;
	}
}
