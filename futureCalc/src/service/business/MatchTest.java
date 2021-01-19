package service.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class MatchTest {
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		
		File matchTimeFile = new File("data/matchTime.txt");
		BufferedReader br = new BufferedReader(new FileReader(matchTimeFile));

		TreeMap<Long, Long> map = new TreeMap<Long, Long>();
		ArrayList<Long> list = new ArrayList<Long>();
		int num = 0;
		String str = null;
		while ((str = br.readLine()) != null) {// 使用readLine方法，一次读一行
			String[] sourceStrArray = str.split(" \\| ", 2); // \\表示转译
			map.put(Long.valueOf(sourceStrArray[0]), Long.valueOf(sourceStrArray[1]));
		}
		br.close();

		for (long requestId : map.keySet()) {
			list.add(map.get(requestId));
		}

		for (int i = 0; i < list.size(); i++) {
			try {
				if (list.get(i) > list.get(i + 1)) {
					System.out.println("异常:" + (i + 1) + "--" + list.get(i) + "--" + list.get(i + 1));
					num++;
				}
			} catch (Exception e) {
				System.out.println("已是最后一行数据");
			}
		}
		System.out.println("错误数据:" + num + "条");
		long end = System.currentTimeMillis();
		System.out.println("测试耗时:" + (end - start) + "ms");
	}
}