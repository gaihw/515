package com.uiPackage.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExcuteCommand {
	static Process process;
	public static void delScreenshot(){
		File file = new File("screenshot/");
		deleteAllFilesOfDir(file);
//		excuteCmd("mkdir screenshot");	
		if(!file.exists()) {
			file.mkdir();
		}
	}
	public static void  excuteCmd(String cmd){
        StringBuilder stringBuilder = new StringBuilder();         
        try {
            process = Runtime.getRuntime().exec(cmd); 
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream())); 
//            String line = "";
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line + "\r\n" );
//            }
//            bufferedReader.close();
            process.destroy();
        } catch (IOException e) {
            // TODO: handle exception           
            System.err.println(e);
        }
//        System.out.println(stringBuilder);
    }
	/* 删除文件夹（强制删除）
	 * 
	 * @param path
	 */
	public static void deleteAllFilesOfDir(File path) {
		if (null != path) {
			if (!path.exists())
				return;
			if (path.isFile()) {
				boolean result = path.delete();
				int tryCount = 0;
				while (!result && tryCount++ < 10) {
					System.gc(); // 回收资源
					result = path.delete();
				}
			}
			File[] files = path.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					deleteAllFilesOfDir(files[i]);
				}
			}
			path.delete();
		}
	}
}
