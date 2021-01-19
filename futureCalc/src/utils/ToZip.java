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
 * @date 2018年10月19日
 * @version
 * @description 定义了将源文件打成压缩包的工具类
 */
public class ToZip {

	/**
	 * 将源文件目录下的所有文件打包成zip文件
	 * 
	 * @param sourceFilePath
	 *            e.g. xml
	 * @param zipFilePath
	 *            e.g. zip
	 * @param fileName
	 *            e.g. person
	 * @return 返回生成的zip文件目录 e.g. zip/person.zip
	 */
	public static String tozip(String sourceFilePath, String zipFilePath, String fileName) {
		// 数据源文件 --等待压缩处理
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;

		// 压缩包路径+文件名
		String createZipPath = zipFilePath + "/" + fileName + ".zip";

		if (!sourceFile.exists()) {
			System.out.println("待压缩的文件目录:" + sourceFilePath + "不存在");
		} else {
			try {
				final File zipFile = new File(createZipPath);
				final File[] sourceFiles = sourceFile.listFiles();
				if (null == sourceFiles || sourceFiles.length < 1) {
					System.out.println("待压缩的文件目录:" + sourceFilePath + " 里面不存在文件,无需压缩");
				} else {
					fos = new FileOutputStream(zipFile);
					zos = new ZipOutputStream(new BufferedOutputStream(fos));
					final byte[] bufs = new byte[1024 * 10];
					for (int i = 0; i < sourceFiles.length; i++) {
						// 创建ZIP实体,并添加进压缩包
						final ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
						zos.putNextEntry(zipEntry);
						// 读取待压缩的文件并写进压缩包里
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

	// 测试方法
	public static void tozip2() {
		Properties prop = new Properties();
		try {
			// 加载配置文件
			prop.load(new FileInputStream("resource/configuration/sendMail"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 发送数据源文件
		final File sourceFile = new File(prop.getProperty("sourceFilePath"));
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		// 压缩包路径
		String zipFilePath = prop.getProperty("zipFilePath");
		// 压缩包文件名
		String fileName = prop.getProperty("fileName");

		final String createZipPath = zipFilePath + "/" + fileName + ".zip";

		if (!sourceFile.exists()) {
			System.out.println("待压缩的文件目录：" + prop.getProperty("sourceFilePath") + "不存在");
		} else {
			try {
				final File zipFile = new File(createZipPath);
				final File[] sourceFiles = sourceFile.listFiles();
				if (null == sourceFiles || sourceFiles.length < 1) {
					System.out.println("待压缩的文件目录：" + prop.getProperty("sourceFilePath") + " 里面不存在文件,无需压缩");
				} else {
					fos = new FileOutputStream(zipFile);
					zos = new ZipOutputStream(new BufferedOutputStream(fos));
					final byte[] bufs = new byte[1024 * 10];
					for (int i = 0; i < sourceFiles.length; i++) {
						// 创建ZIP实体,并添加进压缩包
						final ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
						zos.putNextEntry(zipEntry);
						// 读取待压缩的文件并写进压缩包里
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
