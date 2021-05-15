package com.zmj.dao.orther.regular;

import com.zmj.domain.orther.CoinUser;
import com.zmj.domain.orther.MailMessage;
import com.zmj.domain.orther.ShortMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FexDao {
    @Select("SELECT mobile,send_time sendTime,keywords FROM fex_umc.sms_record_${year} WHERE keywords!='' ORDER BY send_time DESC LIMIT #{pageNum},#{pageSize}")
    List<ShortMessage> getFexSmsList(@Param("year") int year, @Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    @Select("SELECT mobile,send_time sendTime,keywords FROM fex_umc.sms_record_${year} WHERE keywords!='' AND mobile=#{mobile} ORDER BY send_time DESC")
    List<ShortMessage> searchFexSmsList(@Param("year") int year, @Param("mobile") String mobile);

    /**
     * 根据用户id区间批量充值
     */
    @Insert("INSERT INTO fex_account.tb_account_deposit_coin(`user_id`,`currency_id`,`address`,`amount`,`height`,`txid`,`status`,`chain_source_type`) VALUES ${value}")
    void rechargeByUserId(@Param("value") String value);

    /**
     * 获取所有的币种名称
     *
     * @return 币种名称集合
     */
    @Select("SELECT name FROM fex_dict.tb_dict_currency")
    List<String> getCurrencyNames();

    /**
     * 查询站点名称
     *
     * @return
     */
    @Select("SELECT name FROM fex_dict.tb_dict_site")
    List<String> getSites();

    /**
     * 根据站点名称查询站点id
     *
     * @param name 站点名称，如币币账户
     * @return
     */
    @Select("SELECT id FROM fex_dict.tb_dict_site WHERE name=#{name}")
    int getSiteIdByName(@Param("name") String name);

    /**
     * 根据币种名称精确查询币种ID
     */
    @Select("SELECT id FROM fex_dict.tb_dict_currency WHERE name=#{name}")
    String getCurrencyId(@Param("name") String name);

    /**
     * 根据手机号查询用户
     */
    @Select("SELECT mobile,id,uid FROM fex_umc.user_base_info WHERE mobile=#{mobile}")
    CoinUser getUserInfoByMobile(@Param("mobile") String mobile);

    /**
     * FEX邮箱验证码
     *
     * @return 验证码列表
     */
    @Select("SELECT user_id userId,email,keywords,code,send_time sendTime FROM fex_umc.mail_record WHERE code!='' ORDER BY send_time DESC LIMIT #{pageNum},#{pageSize}")
    List<MailMessage> getFexMailList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
}
