package calc;

import calc.utils.CommonUtil;
import calc.utils.Config;
import calc.utils.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FullExecuteAndLimit {
    static {
        System.setProperty("fileName", "info.log");
    }
    public static Logger log = LoggerFactory.getLogger(FullExecuteAndLimit.class);

    public static String userId = Config.userId;

    public static void main(String[] args) throws SQLException {
        log.info("------FullExecuteAndLimit start------");
        //------------------------下单前，记录user_balance表中balance和hold值,同时查看同币种同方向同杠杆的仓位------------------------
        JSONObject balance_hold_jb_pre = CommonUtil.getUserBalanceHold(userId);
        BigDecimal balance_pre = balance_hold_jb_pre.getBigDecimal("balance");
        BigDecimal hold_pre = balance_hold_jb_pre.getBigDecimal("hold");
        log.info("下单前，balance:{},hold:{}",balance_pre,hold_pre);
        String currencyName = "btc";
        String direction = "short";
        BigDecimal leverage = BigDecimal.valueOf(31);
        JSONObject prePriceAndQuality = CommonUtil.samePositionData(userId,currencyName,direction,leverage);
        //下单前当前仓位的开仓价格，用于计算合仓的开仓平均价
        BigDecimal openPricePre = prePriceAndQuality.getBigDecimal("openPrice");
        //下单前当前仓位的持仓数量，用于计算合仓的总持仓数量
        BigDecimal qualityPre = prePriceAndQuality.getBigDecimal("quality");

        //------------------------下单>>>查看position_action表中的开仓价格和持仓数量------------------------

        String orderId = "73";
        JSONObject positionActionRes = new JSONObject();
        //开仓张数
        BigDecimal quantity = BigDecimal.ZERO;
        //开仓价格
        BigDecimal openPrice = BigDecimal.ZERO;
        //仓位ID
        String positionID = null;
        //是否下单
        boolean openFlag = true;
        //下单响应
        String open_res = null;
        try{
            if (openFlag){
                open_res = CommonUtil.open(currencyName,0,1,direction,leverage,1,0,BigDecimal.valueOf(5),0,BigDecimal.ZERO,BigDecimal.ZERO);
                orderId = JSONObject.parseObject(open_res).getString("data");
                positionActionRes= CommonUtil.getPositionAction(orderId);
                quantity = positionActionRes.getBigDecimal("quantity");
                openPrice = positionActionRes.getBigDecimal("open_price");
                positionID = positionActionRes.getString("position_id");
            }else {
                positionActionRes= CommonUtil.getPositionAction(orderId);
                openPrice = positionActionRes.getBigDecimal("open_price");
                quantity = positionActionRes.getBigDecimal("quantity");
            }

            //合约面值
            BigDecimal unit = CommonUtil.getOneLotSize(currencyName);

            //------------------------校验开仓手续费的流水账单------------------------
            //手续费=张数*开仓价*合约面值*手续费率
            BigDecimal openFee = quantity.multiply(openPrice).multiply(unit).multiply(Config.taker).setScale(7,BigDecimal.ROUND_UP);
            System.out.println("开仓手续费:"+openFee);
            //获取clearing_transfer表中的流水账单
            ResultSet clearingTransferRes = CommonUtil.getClearingTransfer(userId,orderId);
            checkClearFee(openFee,clearingTransferRes);
            //获取user_bill表中的流水账单
            ResultSet userBillRes = CommonUtil.getAssetBill(userId);
            checkBillFee(openFee,userBillRes);

            //------------------------计算平均开仓价格和持仓数量,并校验------------------------
            //下单后，查询position表该仓位的合仓数据
            JSONObject postPositionData = CommonUtil.samePositionData(userId,currencyName,direction,leverage);
            //计算持仓数量
            BigDecimal total = BigDecimal.ZERO;
            //平均开仓价格=（合约面值 * 张数1 * 开仓价1 + 合约面值 * 张数2 * 开仓价2 + 。。。+合约面值 * 张数n * 开仓价n）/ （sum（张数） * 合约面值）
            BigDecimal avgOpenPrice = BigDecimal.ZERO;
            if (openFlag){
                //计算持仓数量
                total = qualityPre.add(quantity);
                //平均开仓价格=（合约面值 * 张数1 * 开仓价1 + 合约面值 * 张数2 * 开仓价2 + 。。。+合约面值 * 张数n * 开仓价n）/ （sum（张数） * 合约面值）
                avgOpenPrice = (openPricePre.multiply(qualityPre).add(openPrice.multiply(quantity))).divide(total,8,BigDecimal.ROUND_UP);
                checkPriceAndQuality(avgOpenPrice,total,postPositionData);
            }else {
                total = qualityPre;
                avgOpenPrice = openPricePre;
            }

            //------------------------计算浮盈亏、浮盈亏率------------------------
            //根据币种名称，从redis获取标记价格
            BigDecimal indexPrice = BigDecimal.valueOf(Double.valueOf(RedisUtil.jedis(0).get(currencyName)));
            //浮盈亏 = （标记价 - 开仓均价）* 币数量 * 合约方向
            //浮动盈亏率 = （标记价 - 开仓均价）/开仓均价*杠杆*100
            if (direction.equalsIgnoreCase("long")){
                System.out.println("浮动盈亏："+(indexPrice.subtract(avgOpenPrice)).multiply(total).multiply(unit).setScale(4,BigDecimal.ROUND_UP));
                System.out.println("浮动盈亏率："+(indexPrice.subtract(avgOpenPrice)).divide(avgOpenPrice,8,BigDecimal.ROUND_UP).multiply(leverage).multiply(BigDecimal.valueOf(100)));
            }else {
                System.out.println("浮动盈亏："+(avgOpenPrice.subtract(indexPrice)).multiply(total).multiply(unit).setScale(4,BigDecimal.ROUND_UP));
                System.out.println("浮动盈亏率："+(avgOpenPrice.subtract(indexPrice)).divide(avgOpenPrice,8,BigDecimal.ROUND_UP).multiply(leverage).multiply(BigDecimal.valueOf(100)));
            }

            //------------------------校验保证金------------------------
//        总保证金(position表)= 张数 * 合约面值 * 开仓价 / 杠杆
            BigDecimal margin = total.multiply(unit).multiply(avgOpenPrice).divide(leverage,8,BigDecimal.ROUND_UP);
            checkMargin(margin,postPositionData);

            //------------------------校验user_balance表中balance和hold值扣减------------------------
            JSONObject balance_hold_jb_post = CommonUtil.getUserBalanceHold(userId);
            BigDecimal balance_post = balance_hold_jb_post.getBigDecimal("balance");
            BigDecimal hold_post = balance_hold_jb_post.getBigDecimal("hold");
            log.info("下单后，balance:{},hold:{}",balance_post,hold_post);
            checkBalance(balance_hold_jb_pre,balance_hold_jb_post,openFee);
        }catch (Exception e){
            System.out.println("下单失败，信息:"+JSONObject.parseObject(open_res).getString("msg"));
        }

        log.info("------FullExecuteAndLimit end------");
    }

    /**
     * 校验持仓保证金
     * @param margin
     * @param postPositionData
     */
    private static void checkMargin(BigDecimal margin, JSONObject postPositionData) {
        System.out.println("---保证金校验---margin计算得:"+margin+";数据库得:"+postPositionData.getBigDecimal("margin")+";差值:"+margin.subtract(postPositionData.getBigDecimal("margin")));
    }

    /**
     * 校验余额的前后变化
     * @param balance_hold_jb_pre
     * @param balance_hold_jb_post
     * @param openFee
     */
    private static void checkBalance(JSONObject balance_hold_jb_pre, JSONObject balance_hold_jb_post, BigDecimal openFee) {
        System.out.println("---余额校验---balance下单前:"+balance_hold_jb_pre.getBigDecimal("balance")+";下单后:"+balance_hold_jb_post.getBigDecimal("balance")+";差值:"+balance_hold_jb_pre.getBigDecimal("balance").subtract(openFee).subtract(balance_hold_jb_post.getBigDecimal("balance")));
        System.out.println("---hold校验---下单前:"+balance_hold_jb_pre.getBigDecimal("hold")+";下单后:"+balance_hold_jb_post.getBigDecimal("hold")+";差值:"+balance_hold_jb_pre.getBigDecimal("hold").subtract(balance_hold_jb_post.getBigDecimal("hold")));
    }

    /**
     * 合仓时，校验平均开仓价格和持仓数量
     * @param avgOpenPrice
     * @param total
     * @throws SQLException
     */
    private static void checkPriceAndQuality(BigDecimal avgOpenPrice, BigDecimal total, JSONObject postPriceAndQuality) throws SQLException {
        System.out.println("---平均开仓价格校验---计算得:"+avgOpenPrice+";数据库得:"+postPriceAndQuality.getBigDecimal("openPrice")+";差值:"+avgOpenPrice.subtract(postPriceAndQuality.getBigDecimal("openPrice")));
        System.out.println("---持仓数量校验---计算得:"+total+";数据库得:"+postPriceAndQuality.getBigDecimal("quality")+";差值得:"+total.subtract(postPriceAndQuality.getBigDecimal("quality")));
    }

    /**
     * user_bill表流水，校验手续费 51-手续费
     * @param openFee
     * @param userBillRes
     * @throws SQLException
     */
    private static void checkBillFee(BigDecimal openFee, ResultSet userBillRes) throws SQLException {
        while (userBillRes.next()){
            if(userBillRes.getInt("type") == 51){
                System.out.println("---userbill手续费校验---计算得:"+openFee+";数据库得:"+userBillRes.getBigDecimal("size").abs()+";差值得:"+openFee.subtract(userBillRes.getBigDecimal("size").abs()));
            }else {
                continue;
            }
        }
    }

    /**
     * clear表流水
     * 校验手续费 51-手续费
     * @param openFee
     * @param clearingTransfer
     */
    private static void checkClearFee(BigDecimal openFee,ResultSet clearingTransfer) throws SQLException {
        while (clearingTransfer.next()){
            if(Integer.valueOf(clearingTransfer.getString("source_type").split("\\.")[4]) == 51){
                System.out.println("---clearingTransfer手续费校验---计算得:"+openFee+";数据库得:"+clearingTransfer.getBigDecimal("amount").abs()+";计算得:"+openFee.subtract(clearingTransfer.getBigDecimal("amount").abs()));
            }else {
                continue;
            }
        }
    }
}
