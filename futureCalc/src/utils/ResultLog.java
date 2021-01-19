package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ResultLog {
	// �����н�����뱾���ļ�
	public static void writeResult(StringBuffer result, String resultFile, String isReWrite) throws IOException {
		// ����ļ��Ƿ���ڣ��������򴴽�
		File file = new File(resultFile);
		if (!file.exists()) {
			file.createNewFile();
			// ��ʼ�������ļ��Ĳ��Դ���Ϊ1
			// modifyFile("executeProperty.txt", "testNum", "2"); // ��ʱͣ��
		}
		if (isReWrite.equals("reWrite")) { // ��д
			BufferedWriter bw = new BufferedWriter(new FileWriter(resultFile));
			try {
				bw.write(result.toString());
			} catch (Exception e) {
				System.out.println("���Բ������󣬲�������ʧ�ܣ������¼���");
			}
			bw.flush();
			bw.close();

		} else { // ĩβ׷��д
			BufferedWriter bw = new BufferedWriter(new FileWriter(resultFile, true));
			try {
				bw.write(result.toString());
			} catch (Exception e) {
				System.out.println("���Բ������󣬲�������ʧ�ܣ������¼���");
			}
			bw.flush();
			bw.close();
		}
	}

	// ��д�����ļ��ķ���
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
