package calc;

import calc.utils.HttpUtil;
import calc.utils.SqlUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WarehousePosition {
    static {
        System.setProperty("fileName", "position/info.log");
    }
    public static Logger log = LoggerFactory.getLogger(WarehousePosition.class);
    //合约面值
    public static BigDecimal btc_unit = new BigDecimal(0.001);
    public static BigDecimal shib_unit = new BigDecimal(1000);
    public static BigDecimal fil_unit = new BigDecimal(0.1);
    public static BigDecimal unit = btc_unit;
    public static BigDecimal taker = new BigDecimal(0.0004);
    public static void main(String[] args) throws SQLException {
        String user_id = "51905033";
        //账户余额
        BigDecimal available = BigDecimal.valueOf(0);
        //保证金
        BigDecimal totalMargin = BigDecimal.valueOf(0);
        String available_hold_sql = "SELECT balance,hold FROM `bib_cfd`.`user_balance` WHERE `user_id` = '"+user_id+"' ";
        ResultSet available_hold_rs = SqlUtil.select(available_hold_sql);
        while (available_hold_rs.next()){
            available = available_hold_rs.getBigDecimal("balance").setScale(8,BigDecimal.ROUND_UP);
        }
        String accountsCountInfo = HttpUtil.getByForm("https://dev1.tfbeee.com/v1/cfd/app/accountsCountInfo/2?positionModel=0");
        //总的手续费
        BigDecimal totalFee = BigDecimal.valueOf(0);
        totalFee = JSONObject.parseObject(accountsCountInfo).getJSONObject("data").getBigDecimal("totalFee").setScale(8,BigDecimal.ROUND_UP);
        totalMargin = JSONObject.parseObject(accountsCountInfo).getJSONObject("data").getBigDecimal("totalMargin").setScale(8,BigDecimal.ROUND_UP);

        //浮动盈亏
        BigDecimal unprofitloss = BigDecimal.valueOf(-0.06);
        //风险率或保证金率= (账户余额available+ SUM(真实的浮动盈亏)-总的手续费totalFee-总的过夜费totalFundingFee)/总的已用保证金totalMargin
        BigDecimal rate = (available.add(unprofitloss).subtract(totalFee)).divide(totalMargin.multiply(BigDecimal.valueOf(100)),8,BigDecimal.ROUND_UP);

        //预估强平 = （sum（保证金）- 风险率 * 开仓占用保证金 - 过夜费）/ （张数 * 合约单位）+ 开仓价
        BigDecimal estimated_strong_price = (totalMargin.subtract(rate.multiply(totalMargin))).divide(BigDecimal.valueOf(2).multiply(unit),8,BigDecimal.ROUND_UP).subtract(BigDecimal.valueOf(43553.5));

        System.out.println("账户余额："+available);
        System.out.println("保证金："+totalMargin);
        System.out.println("手续费："+totalFee);
        System.out.println("风险率："+rate);
        System.out.println("预估强平价："+estimated_strong_price);
    }
}
