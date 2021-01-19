package backups;

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
		double[] price = { 6002, 6008 }; // ����ί�м۸�
		double[] size = { 1, 1 }; // ����ί������
		double[] leverage = { 10, 10 }; // �ܸ�

		// ִ����
		frozenDeposit(initialAssets, price, size, leverage);
	}

	public static void frozenDeposit(double initialAssets, double[] price, double[] size, double[] leverage) {
		StringBuffer testDetail = new StringBuffer();

		double buyPrice = -5500; // ��һ�۸�
		double sellPrice = 10000000; // ��һ�۸�
		double frozenDepositSell = 0; // �յ����ᱣ֤��
		double frozenDepositBuy = 0; // �൥���ᱣ֤��
		double balance = 0; // ���

		// ��ʽ��
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.########");

		for (int i = 0; i < price.length; i++) {

			testDetail.append("������" + (i + 1) + "��ί�е�������������").append("\r\n");
			testDetail.append("��ʼ�ʽ�:" + initialAssets).append("\r\n");
			// �൥���ᱣ֤��
			frozenDepositBuy = (Math.min(price[i], sellPrice) / leverage[i] + 0.0006 * Math.min(price[i], sellPrice)
					+ 0.0006 * Math.min(price[i], sellPrice)) * Math.abs(size[i]) * 0.002;
			// �յ����ᱣ֤��
			frozenDepositSell = (Math.max(price[i], buyPrice) / leverage[i] + 0.0006 * Math.max(price[i], buyPrice)
					+ 0.0006 * Math.max(price[i], buyPrice)) * Math.abs(size[i]) * 0.002;

			if (size[i] < 0) {
				balance = initialAssets - frozenDepositSell;
				initialAssets = balance;
			} else {
				balance = initialAssets - frozenDepositBuy;
				initialAssets = balance;
			}

			testDetail.append("���ඳ�ᱣ֤��:" + df.format(frozenDepositBuy)).append("\r\n");
			testDetail.append("���ն��ᱣ֤��:" + df.format(frozenDepositSell)).append("\r\n");
			testDetail.append("���:" + df.format(balance)).append("\r\n");
			testDetail.append("======================").append("\r\n");
		}
		System.out.println(testDetail);
	}
}
