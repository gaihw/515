package com.uxin.usedcar.test.libs;


import io.appium.java_client.android.AndroidDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;

@SuppressWarnings("unused")
public class StringUtils {

	/**
	 * 去除字符串中的所有空格换行符
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	public static void Screenshot(AndroidDriver driver,String ScreenName) {
		
		  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		  String  dateString = formatter.format(new Date());
		  String dir_name=System.getProperty("user.dir")+"/异常图片";
		  System.out.println("异常图片目录"+dir_name);
		  if (!(new File(dir_name).isDirectory()))
	        { 
	        new File(dir_name).mkdir();
	        }
		  try {
			  File screen = driver.getScreenshotAs(OutputType.FILE);
			  //图片最后存放的路径由  目录 ：dir_name +时间戳 +测试套件+测试用例+测试步骤组合生成
			  System.out.println("异常图片名称"+dir_name+"/"+dateString+ScreenName+".jpg");
			  CaseConfig.sTestScreenshotName=dir_name+"/"+dateString+ScreenName+".jpg";
			  FileUtils.copyFile(screen,new File(CaseConfig.sTestScreenshotName));
		} catch (WebDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  
		  
	}
//	public static void splitObject(IOSDriver driver,String bytype,String object, String data) throws InterruptedException {
//		//分解Object
//		String tem[]=object.split(",");
//		for (int i=0;i<tem.length;i++) {
//			 String Object=tem[i];
////			 Thread.sleep(2000);
//			 //执行重载
//			 click(driver,bytype,Object,data);
//		}
//        
//		
//	}
	/**
	 * //UIAApplication[1]/UIAWindow[1]/UIATableView[1]/UIATableCell[1]/UIAButton[5]
	 * @param str
	 * @return
	 */
	private  String xpathToIosLocatorJs(String str){
	    String[] array=str.trim().replace("//","/").split("/");
	    for (int i=1;i<array.length;i++){
	        String tag=array[i];
	        String[] tagArray=tag.split("\\[");
	        String tagName=tagArray[0];
	        if (tagArray.length==2){
	            if (!tagName.contains("UIAApplication")&&!tagName.contains("UIAWindow")){
	                String indexStr=tagArray[1].replace("]","");
	                if (String.valueOf(indexStr) != null){
	                    int  index=Integer.valueOf(indexStr)-1;
	                    indexStr=String.valueOf(index);
	                    tag=tag.replace("["+tagArray[1],"["+indexStr+"]");
	                }else {
	                    indexStr="\""+indexStr.split("=")[1].replace("\"","").replace("'","")+"\"";
	                    tag=tag.replace("["+tagArray[1],"["+indexStr+"]");
	                }
	            }
	        }
	        switch (tagName){
	            case "UIAApplication":
	                tagName="frontMostApp";
	                break;
	            case "UIAWindow":
	                tagName="mainWindow";
	                break;
	            case "UIATableCell":
	                tagName="cells";
	                break;
	            case "*":
	                tagName="frontMostApp().mainWindow().elements";
	                break;
	            default:
	                tagName=tagName.replace("UIA","")+"s";
	                break;
	        }
	        tagName=tagName+"()";
	        StringBuffer sb=new StringBuffer();
	        if (!tagName.equals("")){
	            sb.append(tagName.charAt(0));
	            tagName=tagName.replace(tagName.charAt(0),sb.toString().toLowerCase().charAt(0));
	        }
	        tag=tag.replace(tagArray[0],tagName);
	        if (tagName.contains("frontMostApp")||tagName.contains("mainWindow")){
	            tag=tag.replace("["+tagArray[1],"");
	        }
	        str=str.replace(array[i],tag);
	    }
	    str=" var webElement="+str.replace("//","UIATarget.localTarget().").replace("/",".")+";";
	    return str;

	}

	/**
	 * 获得src中的字符串find的个数
	 */
	public static int getOccur(String src, String find) {
		int o = 0;
		int index = -1;
		while ((index = src.indexOf(find, index)) > -1) {
			++index;
			++o;
		}
		return o;
	}

	/**
	 * 将字符串通过分隔符find转换成二维字符串
	 */
	public static ArrayList<String> getStrsByMarker(String src, String find) {
		if (src.equals("")) {
			return null;
		}
		src = src + find;
		ArrayList<String> strs = new ArrayList<String>();
		while (!src.equals("")) {
			strs.add(src.substring(0, src.indexOf(find)));
			src = src.substring(src.indexOf(find) + 1);
		}
		return strs;
	}

	/**
	 * 以行为单位读取文件
	 */
	public static ArrayList<String> readFileByLines(String fileName) {
		ArrayList<String> readStrings = new ArrayList<String>();
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				if (!tempString.equals("")) {
					readStrings.add(tempString);
				}
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return readStrings;
	}
}
