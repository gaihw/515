package com.zmj.demo.dao.test;

import com.zmj.demo.domain.tool.SmsEmailcodeDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SmsEmailCodeDao {


    @Select("SELECT id,created_date as createdDate,mobile,email,template_params as templateParams,status " +
            "FROM `bib_cfd`.`intgr_message` " +
//            "WHERE `template_code` IN (SELECT DISTINCT template_code FROM `bib_cfd`.`intgr_message` WHERE `template_params` LIKE '%code%' ) " +
            "WHERE `template_code` IN ('MAIL_ASSET_WITHDRAW_CODE','MAIL_USERS_ACTIVE_CODE','MAIL_USERS_FIAT_ALIPAY_SETTING_CODE','MAIL_USERS_FIAT_BANK_SETTING_CODE','MAIL_USERS_FIAT_WEPAY_SETTING_CODE','MAIL_USERS_GOOGLE_BIND_CODE','MAIL_USERS_LOGIN_PWD_RESET_CODE','MAIL_USERS_LOGIN_STEP2AUTH_CODE','MAIL_USERS_LOGIN_STEP2AUTH_SUCCESS','MAIL_USERS_MOBILE_BIND_SUCCESS','MAIL_USERS_OTC_SELL_CODE','MAIL_USERS_PAY_PWD_SET_CODE','MAIL_USERS_REGISTER_CODE','SMS_ASSET_WITHDRAW_CODE','SMS_USERS_FIAT_ALIPAY_SETTING_CODE','SMS_USERS_FIAT_BANK_SETTING_CODE','SMS_USERS_FIAT_WEPAY_SETTING_CODE','SMS_USERS_GOOGLE_BIND_CODE','SMS_USERS_LOGIN_PWD_RESET_CODE','SMS_USERS_LOGIN_STEP2AUTH_CODE','SMS_USERS_MOBILE_BIND_CODE','SMS_USERS_MOBILE_BIND_SUCCESS','SMS_USERS_MOBILE_REGISTER_CODE','SMS_USERS_OTC_SELL_CODE','SMS_USERS_PAY_PWD_SET_CODE')" +
            "ORDER BY `id` DESC " +
            "LIMIT 0,10")
    List<SmsEmailcodeDomain> getSmsList();

}
