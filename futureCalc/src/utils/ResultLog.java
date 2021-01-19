package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ResultLog {
	// 将运行结果记入本地文件
	public static void writeResult(StringBuffer result, String resultFile, String isReWrite) throws IOException {
		// 检查文件是否存在，不存在则创建
		File file = new File(resultFile);
		if (!file.exists()) {
			file.createNewFile();
			// 初始化配置文件的测试次数为1
			// modifyFile("executeProperty.txt", "testNum", "2"); // 暂时停用
		}
		if (isReWrite.equals("reWrite")) { // 重写
			BufferedWriter bw = new BufferedWriter(new FileWriter(resultFile));
			try {
				bw.write(result.toString());
			} catch (Exception e) {
				System.out.println("测试参数有误，测试运行失败，无需记录结果");
			}
			bw.flush();
			bw.close();

		} else { // 末尾追加写
			BufferedWriter bw = new BufferedWriter(new FileWriter(resultFile, true));
			try {
				bw.write(result.toString());
			} catch (Exception e) {
				System.out.println("测试参数有误，测试运行失败，无需记录结果");
			}
			bw.flush();
			bw.close();
		}
	}

	// 改写配置文件的方法
	public static void modifyFile(String file, String key, String value) throws IOException {
		Properties pro = new Properties();
		FileInputStream fis = new FileInputStream(file);
		pro.load(fis);
		fis.close();

		pro.setProperty(key, value);
		FileOutputStream fos = new FileOutputStream(file);
		pro.store(fos, "write by Muguozheng");
		fos.close();
	}
}
