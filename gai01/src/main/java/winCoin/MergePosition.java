package winCoin;

import old.Calc.fuZi.B;

import java.math.BigDecimal;

public class MergePosition {
    //合约面值
    public static BigDecimal btc_unit = new BigDecimal(0.001);
    public static void main(String[] args) {
        //------------------------下单设置项------------------------

        //下单张数
        BigDecimal open_size = new BigDecimal(1);

        //下单杠杆
        BigDecimal open_x = new BigDecimal(5);

        //开仓价
        BigDecimal open_price = new BigDecimal(49876.23);

        //标记价
        BigDecimal index_price = new BigDecimal(49930.22);

        //开仓方向
        BigDecimal direction = new BigDecimal(1);

        //------------------------现有持仓信息------------------------
        //持仓张数
        BigDecimal hold_size = new BigDecimal(23);

        //持仓价格
        BigDecimal hold_price = new BigDecimal(49583.39);

        //账号可用余额
        BigDecimal available_balance = new BigDecimal(3029.3);

        //手续费
        BigDecimal fee = new BigDecimal(2.30);

        //风险率
        BigDecimal risk_rate = new BigDecimal(0.002);

        //------------------------计算结果------------------------

        //币数量= 张数 * 合约面值
        BigDecimal amount = open_size.multiply(btc_unit).setScale(8,BigDecimal.ROUND_UP);

        //保证金= 张数 * 合约面值 * 开仓价 / 杠杆
        BigDecimal margin = open_size.multiply(btc_unit).multiply(open_price).divide(open_x).setScale(8,BigDecimal.ROUND_UP);

        //浮盈亏 = （标记价 - 开仓均价）* 币数量 * 合约方向
        BigDecimal unrealized_profit_loss = (index_price.subtract(open_price)).multiply(amount).multiply(direction).setScale(8,BigDecimal.ROUND_UP);

        //开仓均价 = （合约面值 * 张数1 * 开仓价1 + 合约面值 * 张数2 * 开仓价2 + 。。。+合约面值 * 张数n * 开仓价n）/ （sum（张数） * 合约面值）
        BigDecimal avg_price = (open_size.multiply(open_price).add(hold_size.multiply(hold_price))).divide(open_size.add(hold_size),8,BigDecimal.ROUND_UP);

        //适用于其它情况：
        //单项持仓，预估强平价 = 标记价 - 合约方向 * （账户可用余额 + sum（所有仓位浮动盈亏）- sum（手续费）- sum（保证金）* 风险率）/ （张数 * 合约面值）
        BigDecimal single_estimated_strong_price = (index_price.subtract(direction.multiply(available_balance.add(unrealized_profit_loss))).subtract(fee.multiply(risk_rate))).divide(hold_size.add(open_size),8,BigDecimal.ROUND_UP);

        //双向持仓，方向以大仓位为准，算的是净头寸，预估强平价 = 标记价 - 合约方向 * （账户可用余额 + sum（所有仓位浮动盈亏）- sum（手续费）- sum（保证金）* 风险率）/ （abs（张数之差） * 合约面值））
        BigDecimal two_estimated_strong_price = (index_price.subtract(direction.multiply(available_balance.add(unrealized_profit_loss))).subtract(fee.multiply(risk_rate))).divide(hold_size.add(open_size),8,BigDecimal.ROUND_UP);

        System.out.println("币数量："+amount);
        System.out.println("保证金："+margin);
        System.out.println("浮盈亏："+unrealized_profit_loss);
        System.out.println("开仓均价："+avg_price);
        System.out.println("预估强平价："+single_estimated_strong_price);

    }
}
