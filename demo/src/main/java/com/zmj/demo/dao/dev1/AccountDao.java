package com.zmj.demo.dao.dev1;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.domain.dev1.*;
import com.zmj.demo.domain.dev1.UserDistributorChain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface AccountDao {

    /**
     * 查询user_balance表
     * 通过userId获取用户的balance和hold
     * @param userId
     * @return
     * @throws SQLException
     */
    @Select("SELECT user_id userId,currency_id currencyId,balance,hold " +
            "FROM `bib_cfd`.`user_balance` " +
            "WHERE `user_id` = '${userId}'  " +
            "LIMIT 0,1")
    List<UserBalanceChain> getUserBalanceHold(@Param("userId") String userId);


    /**
     * user_bill表，查询单个用户的该段时间内的钱数变化,去掉该用户type=16的类型，这个是用户返佣的钱，应该加到user_partner_balance表中
     * @param time
     * @param userId
     * @return
     * @throws SQLException
     */
    @Select("SELECT user_id userId,sum(size) total " +
            "from `bib_cfd`.`user_bill` " +
            "where created_date>'${time}' and user_id = #{userId} and type!=16")
    UserBillChain getUserBillTotal(@Param("time") String time, @Param("userId") String userId);

    /**
     * 查询user_bill表
     * 通过userID，查询user_bill表中的数据
     * @param userId
     * @return
     * @throws SQLException
     */
    @Select("SELECT user_id userId,type,size,pre_balance preBalance,post_balance postBalance,pre_profit preProfit,post_profit postProfit,pre_margin preMargin,post_margin postMargin,partner_id partnerId,parent_id parentId,source_id sourceId,note,created_date createdDate,from_user_id fromUserId " +
            "FROM `bib_cfd`.`user_bill` " +
            "WHERE `user_id` = '${userId}' and created_date>='${time}'")
    List<UserBillChain> getUserBillByUser(@Param("userId") String userId, @Param("time") String time);

    /**
     * 查询user_bill表，根据用户开平仓，获取合伙人开平仓手续费、盈亏
     * @param userId
     * @param partnerId
     * @param sourceId
     * @param type
     * @return
     * @throws SQLException
     */
    @Select({"<script>"+
            "select user_id userId,type,size,pre_balance preBalance,post_balance postBalance,pre_profit preProfit,post_profit postProfit,pre_margin preMargin,post_margin postMargin,partner_id partnerId,parent_id parentId,source_id sourceId,note,created_date createdDate,from_user_id fromUserId " +
            "from `bib_cfd`.`user_bill` " +
            "where user_id=#{partnerId}  and source_id='${sourceId}' and type=#{type} " +
            " limit 0,1"+
            "</script>"})
    UserBillChain getUserBill(@Param("partnerId") String partnerId,@Param("sourceId") String sourceId,@Param("type") int type);

    /**
     * 查询给用户返的手续费
     * @param userId
     * @param sourceId
     * @param type
     * @return
     */
    @Select("SELECT user_id userId,type,size,pre_balance preBalance,post_balance postBalance,pre_profit preProfit,post_profit postProfit,pre_margin preMargin,post_margin postMargin,partner_id partnerId,parent_id parentId,source_id sourceId,note,created_date createdDate,from_user_id fromUserId  " +
            "FROM `bib_cfd`.`user_bill` " +
            "WHERE `type` = '${type}' AND `user_id` = '${userId}' AND `source_id` = '${sourceId}'")
    UserBillChain getFeeToUser(@Param("userId") String userId,@Param("sourceId") String sourceId,@Param("type") int type);

    /**
     * 查询user_distributor表
     * 根据用户，查询组织配置数据
     * @param userId
     * @return
     * @throws SQLException
     */
    @Select("SELECT user_id userId,parent_id parentId,gparent_id gparentId,partner_id partnerId,dist_id distId,level,type,defaulted,config,open_bet openBet,is_profession isProfession " +
            "FROM `bib_cfd`.`user_distributor` " +
            "WHERE `user_id` = '${userId}' LIMIT 0,1")
    UserDistributorChain getUserDistributor(@Param("userId") String userId);

    /**
     * 通过订单，查询该笔订单类型
     * @param id
     * @return
     */
    @Select("SELECT id sourceId,margin_type marginType FROM `bib_cfd`.`swap_order` WHERE `id` = '${id}' ")
    SwapOrderChain getMarginType(@Param("id") String id);

    /**
     * 查询clearing_transfer中的盈亏
     * @param userId
     * @param orderId
     * @param marginType
     * @return
     */
    @Select("SELECT  amount,profit,fee FROM `bib_cfd`.`clearing_transfer` WHERE `user_id` = '${userId}' AND `source_type` LIKE '%cfd.bill.type.code.4%' AND `order_id` = '${orderId}' AND `margin_type` = '${marginType}'")
    ClearingTransferChain getClearlingTransfer(@Param("userId") String userId,@Param("orderId") String orderId,@Param("marginType") String marginType);


}
