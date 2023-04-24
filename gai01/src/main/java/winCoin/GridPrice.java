package winCoin;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 *  网格价格计算
 *
 * @author gaihw
 * @date 2023/2/7 12:42
 */
public class GridPrice {
    public static void main(String[] args) {
        BigDecimal highestPrice = BigDecimal.valueOf(25.27811);
        BigDecimal lowestPrice = BigDecimal.valueOf(19.84645);
        int gridNum = 40;

        List<BigDecimal> priceList = new ArrayList<BigDecimal>();

        // 等比
        // 公比q=(an/a1)^(1/(n-1)))
        BigDecimal q = bigRoot(highestPrice.divide(lowestPrice,8,BigDecimal.ROUND_DOWN),gridNum,16,5);
        System.out.println("公比="+q);

        for (int i = 0; i <= gridNum; i++) {
            if ( i == gridNum)
                priceList.add(highestPrice);
            else
                priceList.add(lowestPrice.multiply(q.pow(i)).setScale(16,BigDecimal.ROUND_DOWN));
        }
        System.out.println(priceList);
        // 投入最低保证金
//        中性网格=（面值*最小下单张数*求和（挂单价）／(杠杆*调整系数)）+面值*最小下单张数*求和（挂单价）*maker费率
//        做多/空网格=（面值*最小下单张数*求和（挂单价）／(杠杆*调整系数)）+面值*最小下单张数*求和（挂单价）*taker费率
//        调整系数为多租户后台配置的值，最小下单张数由后台配置，默认为1
//        保留两位小数，进位
//        求和（挂单价），取n+1个价格，n=网格数量，即：网格最低价格、最高价格、内部价格
        // 面值
        BigDecimal oneLotSize = BigDecimal.valueOf(0.01);
        // 最小下单张数
        BigDecimal minQuantity = BigDecimal.valueOf(1);
        // 杠杆
        BigDecimal leverage = BigDecimal.valueOf(50);
        // 调整系数
        BigDecimal ratio = BigDecimal.valueOf(0.9);

        // 初始保证金
        BigDecimal initMargin = BigDecimal.valueOf(1000);

        BigDecimal sumPrice = BigDecimal.ZERO;
        for (BigDecimal b:priceList
             ) {
            sumPrice = sumPrice.add(b);
        }
//        sumPrice = BigDecimal.valueOf(99.54*11);
        // 投入最低保证金
        BigDecimal minDeposit = oneLotSize.multiply(minQuantity).multiply(sumPrice).multiply(BigDecimal.ONE.divide(leverage.multiply(ratio),16,BigDecimal.ROUND_DOWN).add(BigDecimal.valueOf(0.0004)));
        System.out.println("投入最低保证金="+minDeposit);

        // 单笔委托数量=调整系数* 初始保证金* 杠杆倍数/ 求和(挂单价)/合约面值/（1+2费率），舍尾取整。其中，求和（挂单价）计算n+1个值，n表示网格数量；即包含网格最高价格、最低价格、网格内部区间的挂单价格
        BigDecimal singalQuantity = ratio.multiply(initMargin).multiply(leverage).divide(sumPrice.multiply(oneLotSize).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(2*0.0004))),16,BigDecimal.ROUND_DOWN);
        System.out.println("单笔委托数量="+singalQuantity);
        priceList.clear();
        System.out.println("=============");

        // 等差
        // 公差d=(an-a1)/(n-1)
        BigDecimal d = (highestPrice.subtract(lowestPrice)).divide(BigDecimal.valueOf(gridNum),16,BigDecimal.ROUND_DOWN);
        System.out.println("公差="+d);
        for (int i = 0; i <= gridNum ; i++) {
            if ( i == gridNum)
                priceList.add(highestPrice);
            else
                priceList.add(lowestPrice.add(d.multiply(BigDecimal.valueOf(i))));
        }
        System.out.println(priceList);
        sumPrice = BigDecimal.ZERO;
        for (BigDecimal b:priceList
        ) {
            sumPrice = sumPrice.add(b);
        }
//        sumPrice = BigDecimal.valueOf(99.54*11);
        // 投入最低保证金
        minDeposit = oneLotSize.multiply(minQuantity).multiply(sumPrice).multiply(BigDecimal.ONE.divide(leverage.multiply(ratio),16,BigDecimal.ROUND_DOWN).add(BigDecimal.valueOf(0.0004)));
        System.out.println("投入最低保证金="+minDeposit);
        // 单笔委托数量=调整系数* 初始保证金* 杠杆倍数/ 求和(挂单价)/合约面值，舍尾取整。其中，求和（挂单价）计算n+1个值，n表示网格数量；即包含网格最高价格、最低价格、网格内部区间的挂单价格
        singalQuantity = ratio.multiply(initMargin).multiply(leverage).divide(sumPrice.multiply(oneLotSize).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(2*0.0004))),16,BigDecimal.ROUND_DOWN);
        System.out.println("单笔委托数量="+singalQuantity);

        System.out.println(bigRoot(BigDecimal.valueOf(1000),10,8,BigDecimal.ROUND_DOWN));

    }

    /**
     * bigdicemal 开N次方根
     */
    public static BigDecimal bigRoot(BigDecimal number, int n, int scale, int roundingMode) {
        boolean negate = false;
        if (n < 0)
            throw new ArithmeticException();
        if (number.compareTo(BigDecimal.ZERO) < 0) {
            if (n % 2 == 0)
                throw new ArithmeticException();
            else {
                number = number.negate();
                negate = true;
            }
        }

        BigDecimal root;

        if (n == 0)
            root = BigDecimal.ONE;
        else if (n == 1)
            root = number;
        else {
            final BigInteger N = BigInteger.valueOf(n);
            final BigInteger N2 = BigInteger.TEN.pow(n);
            final BigInteger N3 = BigInteger.TEN.pow(n - 1);
            final BigInteger NINE = BigInteger.valueOf(9);

            BigInteger[] C = new BigInteger[n + 1];
            for (int i = 0; i <= n; i++) {
                C[i] = combination(n, i);
            }

            BigInteger integer = number.toBigInteger();
            String strInt = integer.toString();
            int lenInt = strInt.length();
            for (int i = lenInt % n; i < n && i > 0; i++)
                strInt = "0" + strInt;
            lenInt = (lenInt + n - 1) / n * n;
            BigDecimal fraction = number.subtract(number.setScale(0, BigDecimal.ROUND_DOWN));
            int lenFrac = (fraction.scale() + n - 1) / n * n;
            fraction = fraction.movePointRight(lenFrac);
            String strFrac = fraction.toPlainString();
            for (int i = strFrac.length(); i < lenFrac; i++)
                strFrac = "0" + strFrac;

            BigInteger res = BigInteger.ZERO;
            BigInteger rem = BigInteger.ZERO;
            for (int i = 0; i < lenInt / n; i++) {
                rem = rem.multiply(N2);

                BigInteger temp = new BigInteger(strInt.substring(i * n, i * n + n));
                rem = rem.add(temp);

                BigInteger j;
                if (res.compareTo(BigInteger.ZERO) != 0)
                    j = rem.divide(res.pow(n - 1).multiply(N).multiply(N3));
                else
                    j = NINE;
                BigInteger test = BigInteger.ZERO;
                temp = res.multiply(BigInteger.TEN);
                while (j.compareTo(BigInteger.ZERO) >= 0) {
                    //test = res.multiply(BigInteger.TEN);
                    //test = ((test.add(j)).pow(n)).subtract(test.pow(n));
                    test = BigInteger.ZERO;
                    if (j.compareTo(BigInteger.ZERO) > 0)
                        for (int k = 1; k <= n; k++)
                            test = test.add(j.pow(k).multiply(C[k]).multiply(temp.pow(n - k)));
                    if (test.compareTo(rem) <= 0)
                        break;
                    j = j.subtract(BigInteger.ONE);
                }

                rem = rem.subtract(test);
                res = res.multiply(BigInteger.TEN);
                res = res.add(j);
            }
            for (int i = 0; i <= scale; i++) {
                rem = rem.multiply(N2);

                if (i < lenFrac / n) {
                    BigInteger temp = new BigInteger(strFrac.substring(i * n, i * n    + n));
                    rem = rem.add(temp);
                }

                BigInteger j;
                if (res.compareTo(BigInteger.ZERO) != 0) {
                    j = rem.divide(res.pow(n - 1).multiply(N).multiply(N3));
                }
                else
                    j = NINE;
                BigInteger test = BigInteger.ZERO;
                BigInteger temp = res.multiply(BigInteger.TEN);
                while (j.compareTo(BigInteger.ZERO) >= 0) {
                    //test = res.multiply(BigInteger.TEN);
                    //test = ((test.add(j)).pow(n)).subtract(test.pow(n));
                    test = BigInteger.ZERO;
                    if (j.compareTo(BigInteger.ZERO) > 0)
                        for (int k = 1; k <= n; k++)
                            test = test.add(j.pow(k).multiply(C[k]).multiply(temp.pow(n - k)));
                    if (test.compareTo(rem) <= 0)
                        break;
                    j = j.subtract(BigInteger.ONE);
                }

                rem = rem.subtract(test);
                res = res.multiply(BigInteger.TEN);
                res = res.add(j);
            }
            root = new BigDecimal(res).movePointLeft(scale + 1);
            if (negate)
                root = root.negate();
        }
        return root.setScale(scale, BigDecimal.ROUND_DOWN);
    }

    public static BigInteger combination(int n, int k) {
        if (k > n || n < 0 || k < 0)
            return BigInteger.ZERO;
        if (k > n / 2)
            return combination(n, n - k);
        BigInteger N1 = BigInteger.ONE;
        BigInteger N2 = BigInteger.ONE;
        BigInteger N = BigInteger.valueOf(n);
        BigInteger K = BigInteger.valueOf(k);
        for (int i = 0; i < k; i++) {
            N1 = N1.multiply(N);
            N2 = N2.multiply(K);
            N = N.subtract(BigInteger.ONE);
            K = K.subtract(BigInteger.ONE);
        }
        return N1.divide(N2);
    }
}
