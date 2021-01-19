package com.uiPackage.utils;

import com.uiPackage.config.Config;
import com.uiPackage.utils.BaseUtils;
import com.uiPackage.utils.MysqlDataBase;
import com.uiPackage.utils.ProLoading;

public class ObtainSms {
	public static String database = Config.DATABASE;;
	public static String user = Config.USER;;
	public static String password = Config.PASSWORD;;
	public static String sql ;
	/**
	 * 往账号中充钱
	 * @param phone
	 * @param sms_type 0-验证码 1-非验证码
	 */
	public static String obtainSms(String phone,int sms_type) {
			sql = "select keywords from umc.sms_record_2019 where mobile = "+phone+" and sms_type="+sms_type+" order by send_time desc limit 0,1";
			return String.valueOf(MysqlDataBase.sql(database, user, password, sql));
	}
//	public static void main(String[] args) {
//		System.out.println(obtainSms("13020071928",0));
//	}
}