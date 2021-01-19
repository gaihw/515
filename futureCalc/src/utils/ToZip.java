package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 
 * @author Muguozheng
 * @date 2018��10��19��
 * @version
 * @description �����˽�Դ�ļ����ѹ�����Ĺ�����
 */
public class ToZip {

	/**
	 * ��Դ�ļ�Ŀ¼�µ������ļ������zip�ļ�
	 * 
	 * @param sourceFilePath
	 *            e.g. xml
	 * @param zipFilePath
	 *            e.g. zip
	 * @param fileName
	 *            e.g. person
	 * @return �������ɵ�zip�ļ�Ŀ¼ e.g. zip/person.zip
	 */
	public static String tozip(String sourceFilePath, String zipFilePath, String fileName) {
		// ����Դ�ļ� --�ȴ�ѹ������
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		// ѹ����·��+�ļ���
		String createZipPath = zipFilePath + "/" + fileName + ".zip";

		if (!sourceFile.exists()) {
			System.out.println("��ѹ�����ļ�Ŀ¼:" + sourceFilePath + "������");
		} else {
			try {
				final File zipFile = new File(createZipPath);
				final File[] sourceFiles = sourceFile.listFiles();
				if (null == sourceFiles || sourceFiles.length < 1) {
					System.out.println("��ѹ�����ļ�Ŀ¼:" + sourceFilePath + " ���治�����ļ�,����ѹ��");
				} else {
					fos = new FileOutputStream(zipFile);
					zos = new ZipOutputStream(new BufferedOutputStream(fos));
					final byte[] bufs = new byte[1024 * 10];
					for (int i = 0; i < sourceFiles.length; i++) {
						// ����ZIPʵ��,����ӽ�ѹ����
						final ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
						zos.putNextEntry(zipEntry);
						// ��ȡ��ѹ�����ļ���д��ѹ������
						fis = new FileInputStream(sourceFiles[i]);
						bis = new BufferedInputStream(fis, 1024 * 10);
						int read = 0;
						while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
							zos.write(bufs, 0, read);
						}
					}
				}
			} catch (final FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (final IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				try {
					if (null != bis) {
						bis.close();
					}
					if (null != zos) {
						zos.close();
					}
				} catch (final IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}

		return createZipPath;
	}

	// ���Է���
	public static void tozip2() {
		Properties prop = new Properties();
		try {
			// ���������ļ�
			prop.load(new FileInputStream("resource/configuration/sendMail"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// ��������Դ�ļ�
		final File sourceFile = new File(prop.getProperty("sourceFilePath"));
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		// ѹ����·��
		String zipFilePath = prop.getProperty("zipFilePath");
		// ѹ�����ļ���
		String fileName = prop.getProperty("fileName");

		final String createZipPath = zipFilePath + "/" + fileName + ".zip";

		if (!sourceFile.exists()) {
			System.out.println("��ѹ�����ļ�Ŀ¼��" + prop.getProperty("sourceFilePath") + "������");
		} else {
			try {
				final File zipFile = new File(createZipPath);
				final File[] sourceFiles = sourceFile.listFiles();
				if (null == sourceFiles || sourceFiles.length < 1) {
					System.out.println("��ѹ�����ļ�Ŀ¼��" + prop.getProperty("sourceFilePath") + " ���治�����ļ�,����ѹ��");
				} else {
					fos = new FileOutputStream(zipFile);
					zos = new ZipOutputStream(new BufferedOutputStream(fos));
					final byte[] bufs = new byte[1024 * 10];
					for (int i = 0; i < sourceFiles.length; i++) {
						// ����ZIPʵ��,����ӽ�ѹ����
						final ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
						zos.putNextEntry(zipEntry);
						// ��ȡ��ѹ�����ļ���д��ѹ������
						fis = new FileInputStream(sourceFiles[i]);
						bis = new BufferedInputStream(fis, 1024 * 10);
						int read = 0;
						while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
							zos.write(bufs, 0, read);
						}
					}
				}

			} catch (final FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (final IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				try {
					if (null != bis) {
						bis.close();
					}
					if (null != zos) {
						zos.close();
					}
				} catch (final IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		System.out.println("prop:" + prop);
		// return createZipPath;
	}
}
