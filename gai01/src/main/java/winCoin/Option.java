package winCoin;

import java.math.BigDecimal;

/**
 * 期权，计算预计盈亏
 *         看涨期权
 *         买入：
 *         行权曲线=（max（0，指数坐标价格-行权价格）-输入委托价格）*输入订单数量
 *         资产曲线=（BS计算价格-输入委托价格）*输入订单数量
 *         卖出：
 *         行权曲线=（输入委托价格-max（0，指数坐标价格-行权价格））*输入订单数量
 *         资产曲线=（输入委托价格-BS计算价格）*输入订单数量
 *
 *         看跌期权
 *         买入：
 *         行权曲线=（max（0，行权价格-指数坐标价格）-输入委托价格）*输入订单数量
 *         资产曲线= （BS计算价格-输入委托价格）*输入订单数量
 *         卖出：
 *         行权曲线=（输入委托价格-max（0，行权价格-指数坐标价格））*输入订单数量
 *         资产曲线=（输入委托价格-BS计算价格）*输入订单数量
 *
 *         1、标记价格计算：
 *         看张期权价格 = S х N(d1) - X х e-r х T х N(d2)
 *         看跌期权价格= X х e-r х T х N(-d2) - S х N(-d1)
 *         其中:
 *         d1 = (ln(S/X) + (r + sigma2/2) х T)/(sigma х T 0.5)
 *         d2 = (ln(S/X) + (r - sigma2/2) х T)/(sigma х T 0.5)
 *         S - 标的资产指数价格
 *         X - 行权价格
 *         r - 无风险利率
 *         T - 到期时间，折算到年
 *         N(x) - 累积正态分布函数，标准正态分布中离差小于x的概率；
 *         sigma - 隐含波动率 即：标记价格iv
 *
 * @author gaihw
 * @date 2023/5/30 14:05
 */
public class Option {
    public static void main(String[] args) {
        // 指数价格
        BigDecimal indexPriceMin = BigDecimal.valueOf(17600);
        BigDecimal indexPriceMax = BigDecimal.valueOf(38200);




    }
}
