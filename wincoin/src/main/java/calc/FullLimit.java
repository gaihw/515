package calc;

import calc.utils.CommonUtil;
import calc.utils.Config;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;

public class FullLimit {
    static {
        System.setProperty("fileName", "info.log");
    }
    public static Logger log = LoggerFactory.getLogger(FullLimit.class);
    public static String userId = Config.userId;

    public static void main(String[] args) throws SQLException {
        log.info("------FullLimit start------");

        //------------------------下单前，记录user_balance表中balance和hold值------------------------
        JSONObject balance_hold_jb_pre = CommonUtil.getUserBalanceHold(userId);
        BigDecimal balance_pre = balance_hold_jb_pre.getBigDecimal("balance");
        BigDecimal hold_pre = balance_hold_jb_pre.getBigDecimal("hold");
        log.info("下单前，balance:{},hold:{}",balance_pre,hold_pre);
        String currencyName = "btc";
        String direction = "short";
        BigDecimal leverage = BigDecimal.valueOf(2);
        //合约面值
        BigDecimal unit = CommonUtil.getOneLotSize(currencyName);

        //------------------------下单>>>查看swap_order表中的开仓价格和持仓数量------------------------
        String orderId = JSONObject.parseObject(CommonUtil.open(currencyName,1,1,direction,leverage,1,1,BigDecimal.valueOf(200),0,BigDecimal.ZERO,BigDecimal.ZERO)).getString("data");
        //下单后，查询订单表，对应的订单信息
        JSONObject postSwapOderData= CommonUtil.getSwapOder(orderId);
        //开仓张数
        BigDecimal quantity = postSwapOderData.getBigDecimal("quantity");
        //开仓价格
        BigDecimal estimatedPrice = postSwapOderData.getBigDecimal("estimated_price");

        //------------------------校验保证金------------------------
//        总保证金(position表)= 张数 * 合约面值 * 开仓价 / 杠杆
        BigDecimal margin = quantity.multiply(unit).multiply(estimatedPrice).divide(leverage,8,BigDecimal.ROUND_UP);
        checkMargin(margin,postSwapOderData);

        //------------------------校验user_balance表中balance和hold值扣减------------------------
        JSONObject balance_hold_jb_post = CommonUtil.getUserBalanceHold(userId);
        BigDecimal balance_post = balance_hold_jb_post.getBigDecimal("balance");
        BigDecimal hold_post = balance_hold_jb_post.getBigDecimal("hold");
        log.info("下单后，balance:{},hold:{}",balance_post,hold_post);
        checkBalance(balance_hold_jb_pre,balance_hold_jb_post);

        log.info("------FullLimit end------");
    }

    /**
     * 校验余额的前后变化
     * @param balance_hold_jb_pre
     * @param balance_hold_jb_post
     */
    private static void checkBalance(JSONObject balance_hold_jb_pre, JSONObject balance_hold_jb_post) {
        System.out.println("---余额校验---balance下单前:"+balance_hold_jb_pre.getBigDecimal("balance")+";下单后:"+balance_hold_jb_post.getBigDecimal("balance")+";差值:"+balance_hold_jb_pre.getBigDecimal("balance").subtract(balance_hold_jb_post.getBigDecimal("balance")));
        System.out.println("---hold校验---下单前:"+balance_hold_jb_pre.getBigDecimal("hold")+";下单后:"+balance_hold_jb_post.getBigDecimal("hold")+";差值:"+balance_hold_jb_pre.getBigDecimal("hold").subtract(balance_hold_jb_post.getBigDecimal("hold")));
    }
    /**
     * 校验持仓保证金
     * @param margin
     * @param postSwapOderData
     */
    private static void checkMargin(BigDecimal margin, JSONObject postSwapOderData) {
        System.out.println("---保证金校验---margin计算得:"+margin+";数据库得:"+postSwapOderData.getBigDecimal("margin")+";差值:"+margin.subtract(postSwapOderData.getBigDecimal("margin")));
    }
}
