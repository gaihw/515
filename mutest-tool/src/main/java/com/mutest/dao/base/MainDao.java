package com.mutest.dao.base;

import com.mutest.model.CoinUser;
import com.mutest.model.MailMessage;
import com.mutest.model.ShortMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author muguozheng
 * @date 2020/6/22 21:15
 * @Description: 主站短信验证码
 * @modify
 */
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
