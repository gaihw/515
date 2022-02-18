package calc;

import calc.utils.SqlUtil;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FullPosition {
    //合约面值
    public static BigDecimal btc_unit = new BigDecimal(0.001);
    public static BigDecimal shib_unit = new BigDecimal(1000);
    public static BigDecimal fil_unit = new BigDecimal(0.1);
    public static BigDecimal unit = btc_unit;
    public static BigDecimal taker = new BigDecimal(0.0004);
    public static String user_id = "51914709";
    //`bib_cfd`.`user_position_log` 表的订单id
    public static String id = " 6,7 ";
    public static void main(String[] args) throws SQLException {
        //------------------------下单设置项------------------------

        //下单张数
        BigDecimal open_size = new BigDecimal(20);

        //下单杠杆
        BigDecimal open_x = new BigDecimal(50);

        //开仓价｜｜持仓均价
        BigDecimal open_price = null;

        //标记价
        BigDecimal index_price = new BigDecimal(46657.4);

        //开仓方向
        BigDecimal direction = new BigDecimal(1);

        //------------------------现有持仓信息------------------------

        //账号可用余额
        BigDecimal available_balance = null;
        String balance_sql = "SELECT * FROM `bib_cfd`.`user_balance` WHERE `user_id` = '"+user_id+"' LIMIT 0,1000";
        ResultSet balance_rs = SqlUtil.select(balance_sql);
        while (balance_rs.next()){
            available_balance = balance_rs.getBigDecimal("balance");
        }

        //------------------------计算结果------------------------
        //开仓均价 = （合约面值 * 张数1 * 开仓价1 + 合约面值 * 张数2 * 开仓价2 + 。。。+合约面值 * 张数n * 开仓价n）/ （sum（张数） * 合约面值）
        BigDecimal avg_price = null;
        String avg_price_sql = "select sum(current_piece*open_price)/sum(current_piece) as avg_price from `bib_cfd`.`user_position_log` where id in ("+id+")";
        ResultSet avg_price_rs = SqlUtil.select(avg_price_sql);
        while (avg_price_rs.next()){
            avg_price = avg_price_rs.getBigDecimal("avg_price").setScale(8,BigDecimal.ROUND_UP);
        }

        open_price = avg_price;

        //币数量= 张数 * 合约面值
        BigDecimal amount = open_size.multiply(unit).setScale(8,BigDecimal.ROUND_UP);

        //保证金= 张数 * 合约面值 * 开仓价 / 杠杆
        BigDecimal margin = open_size.multiply(unit).multiply(open_price).divide(open_x,8,BigDecimal.ROUND_UP);

        //过夜费=保证金*0.0002
        BigDecimal 	overnight_charge = margin.multiply(BigDecimal.valueOf(0.0002));

        //手续费=张数*开仓价*合约面值*手续费率
        BigDecimal fee = open_size.multiply(open_price).multiply(unit).multiply(taker).setScale(8,BigDecimal.ROUND_UP);

        //浮盈亏 = （标记价 - 开仓均价）* 币数量 * 合约方向
        BigDecimal unrealized_profit_loss = (index_price.subtract(open_price)).multiply(amount).multiply(direction).setScale(8,BigDecimal.ROUND_UP);

        //净值=账户余额+(浮动盈亏-手续费-过夜费)
        BigDecimal net_value = available_balance.add(unrealized_profit_loss).subtract(fee).subtract(overnight_charge);

        //风险率=净值/已用保证金*100=(账户余额available+ SUM(真实的浮动盈亏)-总的手续费totalFee-总的过夜费totalFundingFee)/总的已用保证金totalMargin
        BigDecimal risk_rate = net_value.divide(margin,8,BigDecimal.ROUND_UP).multiply(BigDecimal.valueOf(100));


        //适用于其它情况：
        //单向持仓，预估强平价 = 标记价 - 合约方向 * （账户可用余额 + sum（所有仓位浮动盈亏）- sum（手续费）- sum（保证金）* 风险率）/ （张数 * 合约面值）
        BigDecimal single_estimated_strong_price = index_price.subtract(
                direction.multiply(
                        available_balance.add(unrealized_profit_loss).subtract(fee).subtract(margin.multiply(BigDecimal.valueOf(0.1)))
                ).divide(open_size.multiply(unit),8,BigDecimal.ROUND_UP)
        ).setScale(8,BigDecimal.ROUND_UP);

        //双向持仓，方向以大仓位为准，算的是净头寸，预估强平价 = 标记价 - 合约方向 * （账户可用余额 + sum（所有仓位浮动盈亏）- sum（手续费）- sum（保证金）* 风险率）/ （abs（张数之差） * 合约面值））
//        BigDecimal two_estimated_strong_price = (index_price.subtract(direction.multiply(available_balance.add(unrealized_profit_loss))).subtract(fee.multiply(risk_rate))).divide(hold_size.add(open_size),8,BigDecimal.ROUND_UP);



        System.out.println("币数量："+amount);
        System.out.println("保证金："+margin);
        System.out.println("风险率："+risk_rate);
        System.out.println("浮盈亏："+unrealized_profit_loss);
        System.out.println("账户余额："+available_balance);
        System.out.println("手续费："+fee);
        System.out.println("净值："+net_value);
        System.out.println("持仓均价："+avg_price);
        System.out.println("预估强平价："+single_estimated_strong_price);

    }
}
