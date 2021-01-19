package com.uiPackage.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static boolean isDate(String value,String format){  
        
        SimpleDateFormat sdf = null;  
        ParsePosition pos = new ParsePosition(0);//指定从所传字符串的首位开始解析  
          
        if(value == null || isEmpty(format)){  
            return false;  
        }  
        try {  
            sdf = new SimpleDateFormat(format);  
            sdf.setLenient(false);  
            Date date = sdf.parse(value,pos);  
            if(date == null){  
                return false;  
            }else{  
//                System.out.println("-------->pos : " + pos.getIndex());  
//                System.out.println("-------->date : " + sdf.format(date));  
                //更为严谨的日期,如2011-03-024认为是不合法的  
                if(pos.getIndex() > sdf.format(date).length()){  
                    return false;  
                }  
                return true;  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        } 
	}
	public static boolean isEmpty(String value){  
        if(value == null || "".equals(value)){  
            return true;  
        }  
        return false;  
    } 
}	
