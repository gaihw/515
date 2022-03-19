package calc.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommonUtil {
    static {
        System.setProperty("fileName", "info.log");
    }
    public static Logger log = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 查询user_balance表
     * 通过userId获取用户的balance和hold
     * @param userId
     * @return
     * @throws SQLException
     */
    public static JSONObject getUserBalanceHold(String userId) throws SQLException {
        String user_balance_sql = "SELECT balance,hold FROM `bib_cfd`.`user_balance` WHERE `user_id` = '"+userId+"' LIMIT 0,1";
        ResultSet user_balance_rs = SqlUtil.select(user_balance_sql);
        JSONObject balance_hold_jb = new JSONObject();
        while (user_balance_rs.next()){
            balance_hold_jb.put("balance",user_balance_rs.getBigDecimal("balance"));
            balance_hold_jb.put("hold",user_balance_rs.getBigDecimal("hold"));
        }
        log.info("获取用户:{}，余额:{}，hold:{}",userId,balance_hold_jb.getBigDecimal("balance"),balance_hold_jb.getBigDecimal("hold"));
        return balance_hold_jb;
    }

    /**
     * 查position_action表
     * orderId，查position_action表中的数据
     * @param orderId
     * @return
     * @throws SQLException
     */
    public static JSONObject getPositionAction(String orderId) throws SQLException {
        String position_action_sql = "SELECT * FROM `bib_cfd`.`position_action` WHERE `order_id` = '"+orderId+"' LIMIT 0,1";
        ResultSet position_action_rs = SqlUtil.select(position_action_sql);
        JSONObject position_action_jb = new JSONObject();
        while (position_action_rs.next()){
            position_action_jb.put("quantity",position_action_rs.getBigDecimal("quantity"));
            position_action_jb.put("margin",position_action_rs.getBigDecimal("margin"));
            position_action_jb.put("open_price",position_action_rs.getBigDecimal("open_price"));
            position_action_jb.put("leverage",position_action_rs.getBigDecimal("leverage"));
            position_action_jb.put("order_id",position_action_rs.getBigDecimal("order_id"));
        }
        log.info("position_action表仓位:{},信息:{}",orderId,position_action_jb);
        return position_action_jb;
    }

    /**
     * 查询clearing_transfer表
     * 通过userID和orderID，查询clearing_transfer表中的数据
     * @param userId
     * @param orderId
     * @return
     * @throws SQLException
     */
    public static ResultSet getClearingTransfer(String userId,String orderId) throws SQLException {
        String clearing_transfer_sql = "SELECT * FROM `bib_cfd`.`clearing_transfer` WHERE `user_id` = '"+userId+"' AND `order_id` = '"+orderId+"' ORDER BY `id` DESC LIMIT 0,1000";
        ResultSet clearing_transfer_rs = SqlUtil.select(clearing_transfer_sql);
        return clearing_transfer_rs;
    }

    /**
     * 查询asset_bill表
     * 通过userID，查询asset_bill表中的数据
     * @param userId
     * @return
     * @throws SQLException
     */
    public static ResultSet getAssetBill(String userId) throws SQLException {
        String asset_bill_sql = "SELECT * FROM `bib_cfd`.`asset_bill` WHERE `user_id` = '"+userId+"' ORDER BY `id` DESC LIMIT 0,1000";
        ResultSet asset_bill_rs = SqlUtil.select(asset_bill_sql);
        return asset_bill_rs;
    }


    /**
     * 查询instruments表
     * 获取合约面值
     * @param currencyName
     * @return
     * @throws SQLException
     */
    public static BigDecimal getOneLotSize(String currencyName) throws SQLException {
        String instruments_sql = "SELECT * FROM `bib_cfd`.`instruments` WHERE `base` LIKE '%"+currencyName+"%' LIMIT 0,1";
        BigDecimal oneLotSize = BigDecimal.ZERO;
        ResultSet instruments_rs = SqlUtil.select(instruments_sql);
        while (instruments_rs.next()){
            oneLotSize = instruments_rs.getBigDecimal("one_lot_size");
        }
        log.info("币种名称:{},合约面值:{}",currencyName,oneLotSize);
        return oneLotSize;
    }


    /**
     * 查询position表
     * @param userId
     * @return
     */
    public static ResultSet getPosition(String userId){
        String position_sql = "SELECT * FROM `bib_cfd`.`position` WHERE `user_id` = '"+userId+"' ORDER BY `id` DESC LIMIT 0,1000";
        ResultSet position_rs = SqlUtil.select(position_sql);
        return position_rs;
    }

    /**
     * 全仓下单时，获取仓位列表是否有同币种、同方向、同杠杆的持仓，如果有，就返回
     * @param userId
     * @param currencyName
     * @param direction
     * @param leverage
     * @return
     * @throws SQLException
     */
    public static JSONObject samePositionData(String userId,String currencyName,String direction,BigDecimal leverage) throws SQLException {
        JSONObject priceQualityJB = new JSONObject();
        priceQualityJB.put("openPrice",BigDecimal.ZERO);
        priceQualityJB.put("quality", BigDecimal.ZERO);
        priceQualityJB.put("margin", BigDecimal.ZERO);
        ResultSet position_res = getPosition(userId);
        while (position_res.next()){
            if (position_res.getString("symbol").contains(currencyName)&&
                    position_res.getString("direction").equalsIgnoreCase(direction)&&
                    position_res.getBigDecimal("leverage").compareTo(leverage)==0){
                priceQualityJB.put("openPrice",position_res.getBigDecimal("open_price"));
                priceQualityJB.put("quality", position_res.getBigDecimal("quantity"));
                priceQualityJB.put("margin", position_res.getBigDecimal("margin"));
            }
        }
        return priceQualityJB;
    }

    /**
     * 查询swap_order表数据
     * @param orderId
     * @return
     * @throws SQLException
     */
    public static JSONObject getSwapOder(String orderId) throws SQLException {
        String swap_order_sql = "SELECT * FROM `bib_cfd`.`swap_order` WHERE `id` = '"+orderId+"' ORDER BY `id` DESC LIMIT 0,1";
        ResultSet swap_order_rs = SqlUtil.select(swap_order_sql);
        JSONObject swap_order_jb = new JSONObject();
        while (swap_order_rs.next()){
            swap_order_jb.put("quantity",swap_order_rs.getBigDecimal("quantity"));
            swap_order_jb.put("margin",swap_order_rs.getBigDecimal("margin"));
            swap_order_jb.put("estimated_price",swap_order_rs.getBigDecimal("estimated_price"));
        }
        log.info("swap_order订单:{},信息:{}",orderId,swap_order_jb);
        return swap_order_jb;
    }

    /**
     * 开仓
     * @param currencyName 币种名称 btc、eth、ltc
     * @param positionType 0-市价;1-限价;2-计划单
     * @param quantityUnit 0-金额;1-张数
     * @param direction long-多仓;short-空仓
     * @param leverage 杠杆
     * @param quantity 数量
     * @param right 0-立即成交;1-挂单
     * @param difference 限价挂单，价差
     * @param triggerType 计划委托 0-限价;1-市价
     * @param triggerPrice 计划委托触发价格
     * @param triggerOpenPrice 计划委托触发后，限价挂单价格
     *
     * @return
     */
    public static String open(String currencyName,int positionType,int quantityUnit,String direction,BigDecimal leverage,int quantity,int right,BigDecimal difference,int triggerType,BigDecimal triggerPrice,BigDecimal triggerOpenPrice){

        //根据币种名称，从redis获取标记价格
        BigDecimal indexPrice = BigDecimal.valueOf(Double.valueOf(RedisUtil.jedis(0).get(currencyName)));

        String params = "";
        //市价下单
        if (positionType == 0){
            //按照金额下单
            if(quantityUnit == 0){
                params = "{\"positionType\":\"execute\",\"quantity\":\""+quantity+"\",\"direction\":\""+direction+"\",\"leverage\":"+leverage+",\"quantityUnit\":0,\"positionModel\":1,\"contractType\":1}";
            }else {
                params = "{\"positionType\":\"execute\",\"quantity\":\""+quantity+"\",\"direction\":\""+direction+"\",\"leverage\":"+leverage+",\"quantityUnit\":1,\"positionModel\":1,\"contractType\":1}";
            }
            //限价下单
        }else if (positionType == 1){
            if (direction.equalsIgnoreCase("long")){
                if (right == 0){
                    //买 标记价加价
                    indexPrice = indexPrice.add(difference);
                }else {
                    //买 标记价减价
                    indexPrice = indexPrice.subtract(difference);
                }
            }else {
                if (right == 0 ){
                    //卖 标记价减价
                    indexPrice = indexPrice.subtract(difference);
                }else {
                    //卖 标记价加减
                    indexPrice = indexPrice.add(difference);
                }
            }
            //按照金额下单
            if(quantityUnit == 0){
                params = "{\"positionType\":\"plan\",\"quantity\":\""+quantity+"\",\"direction\":\""+direction+"\",\"leverage\":"+leverage+",\"quantityUnit\":0,\"openPrice\": \""+indexPrice+"\",\"positionModel\":1,\"contractType\":1}";
            }else {
                params = "{\"positionType\":\"plan\",\"quantity\":\""+quantity+"\",\"direction\":\""+direction+"\",\"leverage\":"+leverage+",\"quantityUnit\":1,\"openPrice\": \""+indexPrice+"\",\"positionModel\":1,\"contractType\":1}";
            }
        }else {
            if (direction.equalsIgnoreCase("long")){
                    //买 标记价减价
                    indexPrice = indexPrice.subtract(triggerPrice);
                    triggerOpenPrice = indexPrice.subtract(triggerOpenPrice);
            }else {
                    //卖 标记价加价
                    indexPrice = indexPrice.add(triggerPrice);
                    triggerOpenPrice = triggerOpenPrice.add(indexPrice);
            }

            //市价成交
            if (triggerType == 1){
                params = "{\"positionType\":\"planTrigger\",\"quantity\":\""+quantity+"\",\"direction\":\""+direction+"\",\"triggerType\":"+triggerType+",\"contractType\":1,\"leverage\":"+leverage+",\"quantityUnit\":1,\"positionModel\":0,\"triggerPrice\":\""+triggerPrice+"\"}";
            }else {
                params = "{\"positionType\":\"planTrigger\",\"quantity\":\""+quantity+"\",\"direction\":\""+direction+"\",\"triggerType\":"+triggerType+",\"openPrice\":\""+triggerOpenPrice+"\",\"contractType\":1,\"leverage\":"+leverage+",\"quantityUnit\":1,\"positionModel\":0,\"triggerPrice\":\""+triggerPrice+"\"}";
            }
        }
        log.info("下单参数:::{}",params);
        String res = HttpUtil.postByJson(Config.openUrl,params,Config.token);
        log.info("下单返回结果:{}",res);
        return res;
    }

}
