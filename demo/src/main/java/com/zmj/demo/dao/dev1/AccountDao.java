package com.zmj.demo.dao.dev1;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.domain.dev1.*;
import com.zmj.demo.domain.dev1.UserDistributorChain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
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
    List<UserBalanceChain> getUserBalanceByUser(@Param("userId") String userId);

    /**
     * 查询user_balance表,部分用户批量查询
     * @return
     */
    @Select("SELECT user_id userId,currency_id currencyId,balance,hold " +
            "FROM `bib_cfd`.`user_balance` " )//+
//            "LIMIT 0,10")
    List<UserBalanceChain> getUserBalance();

    /**
     * 获取user_balance表所有账户的总金额
     * @return
     */
    @Select("SELECT sum(balance) balance,sum(hold) hold " +
            "FROM `bib_cfd`.`user_balance`")
    UserBalanceChain getAllUserBalanceTotal();

    /**
     * 获取user_partner_balance表所有账户的总金额
     * @return
     */
    @Select("SELECT sum(balance) balance,sum(hold) hold " +
            "FROM `bib_cfd`.`user_partner_balance`")
    UserBalanceChain getAllUserPartnerBalanceTotal();


    /**
     * 查询user_partner_balance表
     * @param userId
     * @return
     */
    @Select("SELECT user_id userId,currency_id currencyId,balance,hold " +
            "FROM `bib_cfd`.`user_partner_balance` " +
            "WHERE `user_id` = '${userId}'  " +
            "LIMIT 0,1")
    List<UserBalanceChain> getUserPartnerBalanceByUser(@Param("userId") String userId);


    /**
     * user_bill表，查询单个用户的该段时间内的钱数变化,去掉该用户type=16,27,38,59的类型，这个是用户返佣的钱，应该加到user_partner_balance表中
     * @param time
     * @param userId
     * @return
     * @throws SQLException
     */
    @Select("SELECT user_id userId,sum(size) total " +
            "from `bib_cfd`.`user_bill` " +
            "where created_date>'${time}' and user_id = #{userId} and type not in (16,27,38,59)")
    UserBillChain getUserBillTotalByUser(@Param("userId") String userId,@Param("time") String time);

    /**
     * 查询user_bill表，获取type=16,27,38,59的类型，统计user_partner_balance表中增加的金额
     * @param time
     * @param userId
     * @return
     */
    @Select({"<script>"+
            "SELECT user_id userId,sum(size) total " +
            "from `bib_cfd`.`user_bill` " +
            "where user_id = #{userId} and type in (16,27,38,59) " +
            "<if test=\"time!=null and time!=''\">"+
            " AND `created_date` > '${time}' " +
            "</if>"+
            "</script>"})
    UserBillChain getUserBillTotalToPartnerBalanceByUser( @Param("userId") String userId,@Param("time") String time);

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
     * @param sourceId
     * @param type
     * @return
     * @throws SQLException
     */
    @Select({"<script>"+
            "select user_id userId,type,size,pre_balance preBalance,post_balance postBalance,pre_profit preProfit,post_profit postProfit,pre_margin preMargin,post_margin postMargin,partner_id partnerId,parent_id parentId,source_id sourceId,note,created_date createdDate,from_user_id fromUserId " +
            "from `bib_cfd`.`user_bill` " +
            "where user_id=#{userId}  and source_id='${sourceId}' and type=#{type} " +
            " limit 0,1"+
            "</script>"})
    UserBillChain getUserBill(@Param("userId") String userId,@Param("sourceId") String sourceId,@Param("type") int type);

    /**
     * 获取user_bill表,所有账户type=16,27,38,59的类型变化金额总和，即获取了user_partner_balance表的变化总和
     * @return
     */
    @Select("SELECT SUM(size) size " +
            "FROM `bib_cfd`.`user_bill` " +
            "where type in (16,27,38,59) ")
    BigDecimal getAllUserBillToPartnerBalance();

    /**
     * 获取user_bill表,所有账户type不等于16,27,38,59的类型变化金额总和，即获取了user_balance表的变化总和
     * @return
     */
    @Select("SELECT SUM(size) size " +
            "FROM `bib_cfd`.`user_bill` " +
            "where type not in (16,27,38,59) ")
    BigDecimal getAllUserBillTotal();

    /**
     * 查询type类型为16,27,38,50,54,55,56,58,59一共有多少条，进行流水对账
     * @return
     */
    @Select("SELECT count(*) c " +
            "FROM `bib_cfd`.`user_bill` " +
            "WHERE `type` IN (3,16,27,35,38,50,54,55,56,58,59)")
    int getUserBillByTypeCount();

    /**
     * 查询type类型为16,27,38,50,54,55,56,58,59的流水明细，进行对账
     * @param limit
     * @return
     */
    @Select("select user_id userId,type,size,pre_balance preBalance,post_balance postBalance,source_id sourceId,note,created_date createdDate " +
            "from `bib_cfd`.`user_bill` " +
            " WHERE `type` IN (3,16,27,35,38,50,54,55,56,58,59) " +
            " order by id asc " +
            " limit ${limit},1000")
    List<UserBillChain> getUserBillByType(@Param("limit") int limit);


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
    @Select("SELECT id sourceId,margin_type marginType" +
            " FROM `bib_cfd`.`swap_order` " +
            "WHERE `id` = '${id}' ")
    SwapOrderChain getMarginType(@Param("id") String id);

    /**
     * 查询clearing_transfer中的盈亏
     * @param userId
     * @param orderId
     * @param marginType
     * @return
     */
    @Select("SELECT  amount,profit,fee " +
            "FROM `bib_cfd`.`clearing_transfer` " +
            "WHERE `user_id` = '${userId}' AND `source_type` LIKE 'cfd.bill.type.code.4' AND `order_id` = '${orderId}' AND `margin_type` = '${marginType}'")
    ClearingTransferChain getClearlingTransfer(@Param("userId") String userId,@Param("orderId") String orderId,@Param("marginType") String marginType);


    /**
     * 查询position_finish表
     * @param userId
     * @return
     */
    @Select("SELECT id,position_id positionId,order_id orderId,user_id userId,symbol,leverage,margin_type marginType,direction,side,quantity,closed_quantity closedQuantity,margin, open_price openPrice,close_price closePrice,avg_cost avgCost,fee,partner_id partnerId,parent_id parentId,liquidate_by liquidateBy " +
            "FROM `bib_cfd`.`position_finish` " +
            "WHERE `user_id` = '${userId}' "+//AND `liquidate_by` LIKE '%coerceClose%' " +
            "LIMIT 0,1000")
    List<PositionFinishChain> positionFinish(@Param("userId") String userId);

    /**
     * 查询position_action表
     * @param userId
     * @return
     */
    @Select({"<script>"+
            "SELECT id,position_id positionId,order_id orderId,user_id userId,symbol,leverage,margin_type marginType,direction,side,quantity,closed_quantity closedQuantity,margin, open_price openPrice,close_price closePrice,avg_cost avgCost,liquidate_by liquidateBy " +
            "FROM `bib_cfd`.`position_action` " +
            "WHERE `user_id` = '${userId}' " +
            "<if test=\"liquidateBy!=null and liquidateBy!=''\">"+
            " AND `liquidate_by` LIKE '%${liquidateBy}%' " +
            "</if>"+
            "<if test=\"orderId!=null and orderId!=''\">"+
            " AND `order_id` = '${orderId}' " +
            "</if>"+
            " LIMIT 0,1000"+
            "</script>"})
    List<PositionActionChain> positionAction(@Param("userId") String userId,@Param("orderId") String orderId,@Param("liquidateBy") String liquidateBy);

    /**
     * 通过币种，获取面值
     * @param symbol
     * @return
     */
    @Select("SELECT one_lot_size oneLotSize FROM `bib_cfd`.`instruments` WHERE `symbol` LIKE '%${symbol}%' ")
    BigDecimal instruments(@Param("symbol") String symbol);
}
