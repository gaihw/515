package com.zmj.dao.orther.base;

import com.zmj.domain.orther.AddressMessage;
import com.zmj.domain.orther.CoinUser;
import com.zmj.domain.orther.MailMessage;
import com.zmj.domain.orther.ShortMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MainDao {
    @Select("SELECT mobile,send_time sendTime,keywords FROM umc.sms_record_${year} WHERE keywords!='' ORDER BY send_time DESC LIMIT #{pageNum},#{pageSize}")
    List<ShortMessage> getMainSmsList(@Param("year") int year, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    @Select("SELECT mobile,send_time sendTime,keywords FROM umc.sms_record_${year} WHERE keywords!='' AND mobile=#{mobile} ORDER BY send_time DESC")
    List<ShortMessage> searchMainSmsList(@Param("year") int year, @Param("mobile") String mobile);

    @Select("SELECT user_id userId,email,keywords,code,send_time sendTime FROM umc.mail_record WHERE code!='' ORDER BY send_time DESC LIMIT #{pageNum},#{pageSize}")
    List<MailMessage> getMainMailList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    @Select("SELECT user_id userId,email,keywords,code,send_time sendTime FROM umc.mail_record WHERE code!='' AND user_id = #{userId} ORDER BY send_time DESC")
    List<MailMessage> searchMainMailList(@Param("userId") Long userId);

//    @Select("SELECT id,uid,mobile,google_key FROM umc.user_base_info WHERE mobile=#{id}")
//    List<CoinUser> userinfoByMobile(@Param("id") String id);
//
//    @Select("SELECT id,uid,mobile,google_key FROM umc.user_base_info WHERE id=#{id}")
//    List<CoinUser> userinfoById(@Param("id") String id);
//
//    @Select("SELECT id,uid,mobile,google_key FROM umc.user_base_info WHERE uid=#{id}")
//    List<CoinUser> userinfoByUid(@Param("id") String id);

    @Select("SELECT id,user_id userId,currency_id currencyId,inner_id innerId,address,payment_id paymentId,tag,trust,created_date createdDate,modify_date modifyDate FROM 58account.tb_account_withdraw_address ORDER BY modify_date DESC LIMIT #{pageNum},#{pageSize}")
    List<AddressMessage> getMainAddressList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    @Select("SELECT id,user_id userId,currency_id currencyId,inner_id innerId,address,payment_id paymentId,tag,trust,created_date createdDate,modify_date modifyDate FROM 58account.tb_account_withdraw_address WHERE user_id = #{userId} ORDER BY modify_date DESC LIMIT #{pageNum},#{pageSize}")
    List<AddressMessage> searchMainAddressList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize,@Param("userId") Long userId);

    @Select("SELECT count(*) FROM 58account.tb_account_withdraw_address WHERE user_id = #{userId}")
    int MainAddressCount(@Param("userId") Long userId);

//    @Update("UPDATE `58account`.`tb_account_withdraw_address` SET  `trust` = 1 WHERE `id` in #{id}")
//    int updateMainAddressTrust(@Param("id") List id);

    @Update("<script>" +
            "UPDATE `58account`.`tb_account_withdraw_address` SET  `trust` = 1 WHERE `id` in("
            +"<foreach collection='id' separator=',' item='id'>"
            + "#{id} "
            + "</foreach> "
            +")</script>")
    int updateMainAddressTrust(@Param("id")List<Integer> id);


    //直接传入sql语句
    @Select("${sqlStr}")
    List<CoinUser> userinfo(@Param(value = "sqlStr") String sqlStr);

    /**
     * 根据币种名称精确查询币种ID
     */
    @Select("SELECT id FROM 58dict.tb_dict_currency WHERE name=#{name}")
    String getCurrencyId(@Param("name") String name);

    /**
     * 根据手机号查询用户
     */
    @Select("SELECT mobile,id,uid FROM umc.user_base_info WHERE mobile=#{mobile}")
    CoinUser getUserInfoByMobile(@Param("mobile") String mobile);

    /**
     * 根据用户id区间批量充值
     */
    @Insert("INSERT INTO 58account.tb_account_deposit_coin(`user_id`,`currency_id`,`address`,`amount`,`height`,`txid`,`status`,`chain_source_type`) VALUES ${value}")
    void rechargeByUserId(@Param("value") String value);

    /**
     * 获取所有的币种名称
     *
     * @return 币种名称集合
     */
    @Select("SELECT name FROM 58dict.tb_dict_currency")
    List<String> getCurrencyNames();

    /**
     * 查询站点名称
     *
     * @return
     */
    @Select("SELECT name FROM 58dict.tb_dict_site")
    List<String> getSites();

    /**
     * 根据站点名称查询站点id
     *
     * @param name 站点名称，如币币账户
     * @return
     */
    @Select("SELECT id FROM 58dict.tb_dict_site WHERE name=#{name}")
    int getSiteIdByName(@Param("name") String name);

}
