package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author Muguozheng
 * @date 2018��11��19��
 * @version
 * @description ����ƽ�����
 */
public class AveragePriceTest {
	public static void main(String[] args) throws NumberFormatException, IOException {
		File matchTimeFile = new File("E:/sell.txt");
		BufferedReader br = new BufferedReader(new FileReader(matchTimeFile));

		ArrayList<Double> prices = new ArrayList<Double>();
		ArrayList<Double> sizes = new ArrayList<Double>();
		ArrayList<Double> executedSizes = new ArrayList<Double>();

		String str = null;
		while ((str = br.readLine()) != null) {// ʹ��readLine������һ�ζ�һ��
			String[] sourceStrArray = str.split(" \\| ", 3); // \\��ʾת��
			prices.add(Double.valueOf(sourceStrArray[0]));
			sizes.add(Double.valueOf(sourceStrArray[1]));
			executedSizes.add(Double.valueOf(sourceStrArray[2]));
		}
		br.close();

		// ����
		double averagePriceBuy = 0;
		double averagePriceSell = 0;

		double temp1 = 0;
		double temp2 = 0;

		for (int i = 0; i < prices.size(); i++) {
			temp1 = temp1 + prices.get(i) * (sizes.get(i) - executedSizes.get(i));
			temp2 = temp2 + (sizes.get(i) - executedSizes.get(i));
		}
		averagePriceBuy = temp1 / temp2;
		System.out.println(averagePriceBuy);
	}
}
