package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

public class UserData {
	public static void main(String[] args) throws IOException {
		File file = new File("D:/Software_test/apache-jmeter-3.3/bin/data/58future/online/id&mobile_5000.txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
		BigInteger mobile = new BigInteger("18800015000");
		BigInteger add = new BigInteger("1");
		int id = 15000;

		for (int i = 0; i < 5000; i++) {
			id = id + 1;
			mobile = mobile.add(add);
			bw.write(String.valueOf(id));
			bw.write(",");
			bw.write(String.valueOf(mobile));
			bw.write("\r\n");
		}
		bw.flush();
		bw.close();
	}
}
