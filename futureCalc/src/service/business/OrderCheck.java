package service.business;

import config.ConfigCenter;

/**
 * 
 * @author Muguozheng
 * @date 2018��11��8��
 * @version 1.0
 * @description �����ּ��,���µ�ǰ����У��,��ֹ�����µ��󼴱��ֵ����
 */
public class OrderCheck {
	// ��ʽ��
	static java.text.DecimalFormat df = new java.text.DecimalFormat("#.######");

	public static void main(String[] args) {
		orderCheck();
	}

	// ί�е������ּ��
	public static void orderCheck() {
		// ��ֵ��
		// �����ֵı�Ǽ۸�,�����Ǽ۸��б仯,�輰ʱ����,"-1"ռλ��
		double[] indexPrices = { -1, 4038, 4, 125, 35, 100, 0.4, 9.8 };
		int contractId = 1; // ��Լid
		double assetsFront = 350; // ��ǰȨ��-ǰ
		double riskDegree = 0; // ��ǰ���ն�
		double type = 1; // type:�µ�����,1 �޼۵� 2 �м۵�
		double price = 4000; // ί�м۸�
		double size = -100; // ί������
		double averagePriceBuy = 0; // �µ����ֶ�ֲֳ־���
		double positionSizeBuy = 0; // �µ����ֶ������

		double averagePriceSell = 0; // �µ����ֿղֲֳ־���
		double positionSizeSell = 0; // �µ����ֿղ�����
		double buyFirst = 3.5; // ��һ�۸�
		double sellFirst = 4.5; // ��һ�۸�

		// ϵͳ����,һ�㲻��
		String[] tableNames = { "ERROR", "BTC", "EOS", "ETH", "LTC", "XRP", "ETC" };
		// �������ֺ�Լ���� ����Ϊ:BTC��EOS��ETH��LTC��XRP��ETC
		// { -1, 0.002, 2, 0.05, 0.2, 40, 1 };
		double[] ratios = ConfigCenter.getRatios();
		double takerFee = 0.0006; // taker��������
		double MM = 0.005; // ά�ֱ�֤����

		// ��ǰ�˻�ά�ֱ�֤��
		double occupancyBondFront = assetsFront * riskDegree;
		// ��ǰ��λ��ͷ��
		double positionValueNet = (averagePriceBuy * positionSizeBuy - averagePriceSell * positionSizeSell)
				* ratios[contractId];
		// �µ����ֶ��ά�ֱ�֤��
		double occupancyBondBuy = averagePriceBuy * positionSizeBuy * ratios[contractId] * MM;
		// �µ����ֿղ�ά�ֱ�֤��
		double occupancyBondSell = averagePriceSell * positionSizeSell * ratios[contractId] * MM;

		double assetsBack = 0; // �µ�ǰ�ʲ�
		double occupancyBondBack = 0; // ��ά�ֱ�֤��
		double sizeMax = 0; // ����ֵ
		double orderValue = 0; // ί�м�ֵ
		double orderPrice = 0; // ί�м۸�
		StringBuffer testDetail = new StringBuffer();

		// ����У��
		if (price <= 0 || size == 0 || type != 1 && type != 2 || assetsFront <= 0) {
			System.out.println("������������,���飡");
			return;
		}

		System.out.println("======" + tableNames[contractId] + "�����ּ���" + "======");

		// �޼۵�
		if (type == 1) {
			// ί�м�ֵ��ֵ
			orderValue = price * size * ratios[contractId];
			orderPrice = price; // �޼۵�ί�м۸�����۸�

			// ί�е�Ԥ��ӯ��,���Խ��о���У��
			// �ʲ��仯=Ԥ��ӯ��-(���������Ѻ�ƽ��������)
			double assetsAdd = (indexPrices[contractId] - price) * size * ratios[contractId]
					- 2 * price * size * ratios[contractId] * takerFee;
			// ռ�ñ�֤��仯
			double occupancyBondAdd = Math.abs(orderValue) * MM;

			if (assetsAdd >= occupancyBondAdd) {
				System.out.println("��ί�е�Ԥ��ӯ��,(ӯ��-������)>��ί������ռ�ñ�֤��,����У��");
				return; // �������У��,������ֹ����
			}

			// ����У��δ������,�������У��
			// �޼���
			if (size > 0) {
				// ��ͷ�緽��͸�ί�е�������ͬ
				if (size * positionValueNet >= 0) {
					// �����¿���Ȩ�� �¿���Ȩ��=����Ȩ��-2*ί�м�*ί����*����*taker+��index-ί�мۣ�*ί����*����
					assetsBack = assetsFront - 2 * price * size * ratios[contractId] * takerFee
							+ (indexPrices[contractId] - price) * size * ratios[contractId];
					// ������ά�ֱ�֤�� ��ά�ֱ�֤�� = ά�ֱ�֤��+ί�м�*ί����*����*MM
					occupancyBondBack = occupancyBondFront + orderValue * MM;
					// �Ա��ж��ܷ��µ�
					check(assetsBack, occupancyBondBack);
					// 1-ί����=������Ȩ��-ά�ֱ�֤��/((ί�м�*MM+2*ί�м�*taker-(index-ί�м�))*����)
					sizeMax = (assetsFront - occupancyBondFront)
							/ ((price * MM + 2 * price * takerFee - (indexPrices[contractId] - price))
									* ratios[contractId]);
					testDetail.append("1-�޼���-ͬ��\r\n���ɿ�����:" + df.format(sizeMax));
				} else {// ��ͷ�緽��͸�ί�е������෴
					if (Math.abs(positionValueNet) > Math.abs(orderValue)) { // ��ͷ�������ڵ��ڸ�ί�е���ֵ
						// �¿���Ȩ��=����Ȩ��-2*ί�м�*ί����*����*taker+��index-ί�мۣ�*ί����*�� ��
						// ��ά�ֱ�֤�� = ά�ֱ�֤��
						assetsBack = assetsFront - 2 * price * size * ratios[contractId] * takerFee
								+ (indexPrices[contractId] - price) * size * ratios[contractId];
						occupancyBondBack = occupancyBondFront;
						// �Ա��ж��ܷ��µ�
						check(assetsBack, occupancyBondBack);
						// 2-ί����=(����Ȩ��-ά�ֱ�֤��)/((2*ί�м�*taker-(index-ί�м�))*����)
						sizeMax = (assetsFront - occupancyBondFront)
								/ ((2 * price * takerFee - (indexPrices[contractId] - price)) * ratios[contractId]);
						testDetail.append("2-�޼���-����-��λ>ί����\r\n���ɿ�����:" + df.format(sizeMax));
					} else { // ��ͷ����С�ڸ�ί�е���
						// �¿���Ȩ��=����Ȩ��-2*ί�м�*ί����*����*taker+��index-ί�мۣ�*ί����*�� ��
						// ��ά�ֱ�֤��=ά�ֱ�֤��(�˻�)-�ñ��ֿղ�ά�ֱ�֤��+�ñ��ֶ��ά�ֱ�֤��+ί�м�*ί����*����*MM
						assetsBack = assetsFront - 2 * price * size * ratios[contractId] * takerFee
								+ (indexPrices[contractId] - price) * size * ratios[contractId];
						occupancyBondBack = occupancyBondFront - occupancyBondSell + occupancyBondBuy
								+ Math.abs(orderValue) * MM;
						// �Ա��ж��ܷ��µ�
						check(assetsBack, occupancyBondBack);
						// 3-ί����=(����Ȩ��-ά�ֱ�֤��(�˻�)+�ñ��ֿղ�ά�ֱ�֤��-�ñ��ֶ��ά�ֱ�֤��)/((2*ί�м�*taker-��index-ί�м�)+ί�м�*MM)*����)
						sizeMax = (assetsFront - occupancyBondFront + occupancyBondSell - occupancyBondBuy)
								/ ((2 * price * takerFee - (indexPrices[contractId] - price) + price * MM)
										* ratios[contractId]);
						testDetail.append("3-�޼���-����-��λ<ί����\r\n���ɿ�����:" + df.format(sizeMax));
					}
				}
			} else { // �޼�����
				// ��ͷ�緽��͸�ί�е�������ͬ
				if (size * positionValueNet >= 0) {
					assetsBack = assetsFront - (2 * price * takerFee - (price - indexPrices[contractId]))
							* Math.abs(size) * ratios[contractId];
					occupancyBondBack = occupancyBondFront + Math.abs(orderValue) * MM;
					// �Ա��ж��ܷ��µ�
					check(assetsBack, occupancyBondBack);
					// 4-ί����=(����Ȩ��-ά�ֱ�֤��)/((ί�м�*MM+2*ί�м�*taker-��ί�м�-index��)*����)
					sizeMax = (assetsFront - occupancyBondFront)
							/ ((price * MM + 2 * price * takerFee - (price - indexPrices[contractId]))
									* ratios[contractId]);
					testDetail.append("4-�޼���-ͬ��\r\n���ɿ�����:" + df.format(sizeMax));
				} else { // ��ͷ�緽��͸�ί�е������෴
					// ��ͷ�������ڵ��ڸ�ί�е���
					if (Math.abs(positionValueNet) >= Math.abs(orderValue)) {
						// �¿���Ȩ��=����Ȩ��-2*ί�м�*ί����*����*taker+��ί�м�-index��*ί����*�� ��
						// ��ά�ֱ�֤�� = ά�ֱ�֤��
						assetsBack = assetsFront - (2 * price * takerFee - (price - indexPrices[contractId]))
								* Math.abs(size) * ratios[contractId];
						occupancyBondBack = occupancyBondFront;
						// �Ա��ж��ܷ��µ�
						check(assetsBack, occupancyBondBack);
						// 5-ί����=(����Ȩ��-ά�ֱ�֤��)/((2*ί�м�*taker-��ί�м�-index��)*����)
						sizeMax = (assetsFront - occupancyBondFront)
								/ ((2 * price * takerFee - (price - indexPrices[contractId])) * ratios[contractId]);
						testDetail.append("5-�޼���-����-��λ>ί����\r\n���ɿ�����:" + df.format(sizeMax));
					} else { // ��ͷ����С�ڸ�ί�е���
						// �¿���Ȩ��=����Ȩ��-2*ί�м�*ί����*����*taker+��ί�м�-index��*ί����*�� ��
						// ��ά�ֱ�֤��=ά�ֱ�֤��(�˻�)-�ñ��ֶ��ά�ֱ�֤��+�ñ��ֿղ�ά�ֱ�֤��+ί�м�*ί����*����*MM
						assetsBack = assetsFront - (2 * price * takerFee - (price - indexPrices[contractId]))
								* Math.abs(size) * ratios[contractId];
						occupancyBondBack = occupancyBondFront - occupancyBondBuy + occupancyBondSell
								+ Math.abs(orderValue) * MM;
						// �Ա��ж��ܷ��µ�
						check(assetsBack, occupancyBondBack);

						// 6-ί����=(����Ȩ��-ά�ֱ�֤��(�˻�)+�ñ��ֶ��ά�ֱ�֤��-�ñ��ֿղ�ά�ֱ�֤��)/((2*ί�м�*taker-��ί�м�-index��+ί�м�*MM)*����)
						sizeMax = (assetsFront - occupancyBondFront + occupancyBondBuy - occupancyBondSell)
								/ ((2 * price * takerFee - (price - indexPrices[contractId]) + price * MM)
										* ratios[contractId]);
						testDetail.append("6-�޼���-����-��λ<ί����\r\n���ɿ�����:" + df.format(sizeMax));
					}
				}
			}

			// �м۵�
		} else {
			// �м���
			if (size > 0) {
				// ί�м�ֵ��ֵ
				orderValue = sellFirst * size * ratios[contractId];
				orderPrice = sellFirst; // �м���,ί�м۸���һ��
				// ��ͷ�緽��͸�ί�е�������ͬ
				if (size * positionValueNet >= 0) {
					// �¿���Ȩ��=����Ȩ��-2*ί�м�*ί����*����*taker+��index-��1�ۣ�*ί����*����
					// ��ά�ֱ�֤�� = ά�ֱ�֤��+ί�м�*ί����*����*MM
					assetsBack = assetsFront - (2 * sellFirst * takerFee - (indexPrices[contractId] - sellFirst)) * size
							* ratios[contractId];
					occupancyBondBack = occupancyBondFront + orderValue * MM;
					// �Ա��ж��ܷ��µ�
					check(assetsBack, occupancyBondBack);
					// 7-ί����=(����Ȩ��-ά�ֱ�֤��)/((ί�м�*MM+2*��һ��*taker-��index-��1�ۣ�)*����)
					sizeMax = (assetsFront - occupancyBondFront)
							/ ((sellFirst * MM + 2 * sellFirst * takerFee - (indexPrices[contractId] - sellFirst))
									* ratios[contractId]);
					testDetail.append("7-�м���-ͬ��\r\n���ɿ�����:" + df.format(sizeMax));
				} else { // ��ͷ�緽��͸�ί�е������෴
					// ��ͷ�������ڵ��ڸ�ί�е���
					if (Math.abs(positionValueNet) >= Math.abs(orderValue)) {
						// �¿���Ȩ��=����Ȩ��-2*ί�м�*ί����*����*taker+��index-��1�ۣ�*ί����*�� ��
						// ��ά�ֱ�֤�� = ά�ֱ�֤��
						assetsBack = assetsFront - (2 * sellFirst * takerFee - (indexPrices[contractId] - sellFirst))
								* size * ratios[contractId];
						occupancyBondBack = occupancyBondFront;
						// �Ա��ж��ܷ��µ�
						check(assetsBack, occupancyBondBack);

						// 8-ί����=(����Ȩ��-ά�ֱ�֤��)/((2*��һ��*taker-��index-��1�ۣ�)*����)
						sizeMax = (assetsFront - occupancyBondFront)
								/ ((2 * sellFirst * takerFee - (indexPrices[contractId] - sellFirst))
										* ratios[contractId]);
						testDetail.append("8-�м���-��λ����-��λ>ί����\r\n���ɿ�����:" + df.format(sizeMax));
					} else { // ��ͷ����С�ڸ�ί�е���
						// �¿���Ȩ��=����Ȩ��-2*ί�м�*ί����*����*taker+��index-��1�ۣ�*ί����*����
						// ��ά�ֱ�֤��=ά�ֱ�֤��(�˻�)-�ñ��ֿղ�ά�ֱ�֤��+�ñ��ֶ��ά�ֱ�֤��+��һ��*ί����*����*MM
						assetsBack = assetsFront - (2 * sellFirst * takerFee - (indexPrices[contractId] - sellFirst))
								* size * ratios[contractId];
						occupancyBondBack = occupancyBondFront - occupancyBondSell + occupancyBondBuy
								+ Math.abs(orderValue) * MM;
						// �Ա��ж��ܷ��µ�
						check(assetsBack, occupancyBondBack);
						// 9-ί����=(����Ȩ��-ά�ֱ�֤��(�˻�)+�ñ��ֿղ�ά�ֱ�֤��-�ñ��ֶ��ά�ֱ�֤��)/((2*��1��*taker-(index-��1��)+��һ��*MM)*������
						sizeMax = (assetsFront - occupancyBondFront + occupancyBondSell - occupancyBondBuy)
								/ ((2 * sellFirst * takerFee - (indexPrices[contractId] - sellFirst) + sellFirst * MM)
										* ratios[contractId]);
						testDetail.append("9-�м���-����-��λ<ί����\r\n���ɿ�����:" + df.format(sizeMax));
					}
				}

			} else { // �м����� size<0
				// ί�м�ֵ��ֵ
				orderValue = buyFirst * size * ratios[contractId];
				orderPrice = buyFirst; // �м�����,ί�м۸���һ��
				// ��ͷ�緽��͸�ί�е�������ͬ
				if (size * positionValueNet >= 0) {
					// �¿���Ȩ��=����Ȩ��-2*ί�м�*ί����*����*taker+����1��-index��*ί����*����
					// ��ά�ֱ�֤�� = ά�ֱ�֤��+ί�м�*ί����*����*MM
					assetsBack = assetsFront - (2 * buyFirst * takerFee - (buyFirst - indexPrices[contractId]))
							* Math.abs(size) * ratios[contractId];
					occupancyBondBack = occupancyBondFront + Math.abs(orderValue) * MM;
					// �Ա��ж��ܷ��µ�
					check(assetsBack, occupancyBondBack);
					// 10-ί����=(����Ȩ��-ά�ֱ�֤��)/((2*��һ��*taker-����1��-index��+��һ��*MM)*����)
					sizeMax = (assetsFront - occupancyBondFront)
							/ ((2 * buyFirst * takerFee - (buyFirst - indexPrices[contractId]) + buyFirst * MM)
									* ratios[contractId]);
					testDetail.append("10-�м���-ͬ��\r\n���ɿ�����:" + df.format(sizeMax));
				} else { // ��ͷ�緽��͸�ί�е������෴
					// ��ͷ�������ڵ��ڸ�ί�е���
					if (Math.abs(positionValueNet) >= Math.abs(orderValue)) {
						// �¿���Ȩ��=����Ȩ��-2*ί�м�*ί����*����*taker+����1��-index��*ί����*����
						// ��ά�ֱ�֤�� = ά�ֱ�֤��
						assetsBack = assetsFront - (2 * buyFirst * takerFee - (buyFirst - indexPrices[contractId]))
								* Math.abs(size) * ratios[contractId];
						occupancyBondBack = occupancyBondFront;
						// �Ա��ж��ܷ��µ�
						check(assetsBack, occupancyBondBack);
						// 11-ί����=(����Ȩ��-ά�ֱ�֤��)/((2*��һ��*taker-����1��-index��)*����)
						sizeMax = (assetsFront - occupancyBondFront)
								/ ((2 * buyFirst * takerFee - (buyFirst - indexPrices[contractId]))
										* ratios[contractId]);
						testDetail.append("11-�м���-����-��λ>ί����\r\n���ɿ�����:" + df.format(sizeMax));
					} else { // ��ͷ����С�ڸ�ί�е���
						// �¿���Ȩ��=����Ȩ��-2*ί�м�*ί����*����*taker+����1��-index��*ί����*�� ��
						// ��ά�ֱ�֤��=ά�ֱ�֤��(�˻�)-�ñ��ֶ��ά�ֱ�֤��+�ñ��ֿղ�ά�ֱ�֤��+��һ��*ί����*����*MM
						assetsBack = assetsFront - (2 * buyFirst * takerFee - (buyFirst - indexPrices[contractId]))
								* Math.abs(size) * ratios[contractId];
						occupancyBondBack = occupancyBondFront - occupancyBondBuy + occupancyBondSell
								+ Math.abs(orderValue) * MM;
						// �Ա��ж��ܷ��µ�
						check(assetsBack, occupancyBondBack);
						// 12-ί����=(����Ȩ��-ά�ֱ�֤��(�˻�)+�ñ��ֶ��ά�ֱ�֤��-�ñ��ֿղ�ά�ֱ�֤��)/((2*��һ��*taker-����1��-index��+��һ��*MM)*����)s
						sizeMax = (assetsFront - occupancyBondFront + occupancyBondBuy - occupancyBondSell)
								/ ((2 * buyFirst * takerFee - (buyFirst - indexPrices[contractId]) + buyFirst * MM)
										* ratios[contractId]);
						testDetail.append("12-�м���-����-��λ<ί����\r\n���ɿ�����:" + df.format(sizeMax));
					}
				}
			}
		}

		System.out.println("-------���޿�����-------");
		System.out.println(testDetail + "\r\n");

		System.out.println("-------������Ϣ-------");
		System.out.println("�˻���ǰά�ֱ�֤��:" + df.format(occupancyBondFront));
		System.out.println("�˻���ǰ���ն�:" + riskDegree);
		System.out.println("�˻�����Ȩ��:" + assetsFront + "\r\n");
		System.out.println("��Ǽ۸�:" + indexPrices[contractId]);
		System.out.println("ί�м۸�:" + orderPrice);
		System.out.println("ί������:" + size);
		System.out.println("ί�м�ֵ:" + df.format(orderValue));
		System.out.println("��λ��ֵ:" + positionValueNet);
		System.out.println("��ֲ�λ��ֵ:" + (averagePriceBuy * positionSizeBuy * ratios[contractId]));
		System.out.println("�ղֲ�λ��ֵ:" + (averagePriceSell * positionSizeSell * ratios[contractId]));
	}

	public static void check(double assetsBack, double occupancyBondBack) {
		// �Ա��ж��ܷ��µ�
		if (assetsBack <= occupancyBondBack) {
			System.out.println("����Ȩ�治��,�����µ�!!!\r\n");
		} else {
			System.out.println("����Ȩ�����,�����µ�\r\n");
		}
		System.out.println("�¿���Ȩ��:" + df.format(assetsBack) + "\r\n" + "�²�λ��֤��:" + df.format(occupancyBondBack));
		System.out.println("���ն�:" + df.format((occupancyBondBack / assetsBack * 100)) + "100%");
	}
}
