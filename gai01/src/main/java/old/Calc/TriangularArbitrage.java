package old.Calc;

import java.math.BigDecimal;

public class TriangularArbitrage {
    public static void main(String[] args) {
//
//        {EOS_BTC=ProductTransActionInfo(productName=EOS_BTC, buyPrice=3.46E-4, sellPrice=0.222589, lastPrice=3.47E-4),
//                EOS_USDT=ProductTransActionInfo(productName=EOS_USDT, buyPrice=2.5562, sellPrice=2.5576, lastPrice=2.5563),
//                BTC_USDT=ProductTransActionInfo(productName=BTC_USDT, buyPrice=9477.51, sellPrice=9502.76, lastPrice=9477.5101)}

        BigDecimal eos_usdt_buyPrice = BigDecimal.valueOf(2.5562);
        BigDecimal eos_usdt_sellPrice = BigDecimal.valueOf(2.5576);
        BigDecimal eos_usdt_lastPrice = BigDecimal.valueOf(2.5563);

        BigDecimal eth_usdt_buyPrice = BigDecimal.valueOf(9477.51);
        BigDecimal eth_usdt_sellPrice = BigDecimal.valueOf(9502.76);
        BigDecimal eth_usdt_lastPrice = BigDecimal.valueOf(9477.5101);

        BigDecimal eos_eth_buyPrice = BigDecimal.valueOf(0.000346);
        BigDecimal eos_eth_sellPrice = BigDecimal.valueOf(0.222589);
        BigDecimal eos_eth_lastPrice = BigDecimal.valueOf(0.000347);



        BigDecimal spread = eos_usdt_sellPrice.subtract(eos_usdt_buyPrice)
                .add((eth_usdt_sellPrice.subtract(eth_usdt_buyPrice)).multiply(eos_eth_lastPrice))
                .add((eos_eth_sellPrice.subtract(eos_eth_buyPrice)).multiply(eth_usdt_lastPrice));

        System.out.println((eos_eth_sellPrice.subtract(eos_eth_buyPrice)).multiply(eth_usdt_lastPrice));

        System.out.println("spread=="+spread);

        BigDecimal spread1 = BigDecimal.valueOf(2106.3108613942);
        BigDecimal spread2 = BigDecimal.valueOf(2106.3204389043);

        BigDecimal mean = (spread1.add(spread2)).divide(BigDecimal.valueOf(2));
        double std = Math.sqrt(((spread1.subtract(mean)).pow(2).add((spread2.subtract(mean)).pow(2))).divide(BigDecimal.valueOf(2)).doubleValue());
        System.out.println("mean=="+mean);
        System.out.println("std=="+std);
        System.out.println("范围=="+mean.add(BigDecimal.valueOf(std).multiply(BigDecimal.valueOf(2))));

        System.out.println(eos_usdt_lastPrice.subtract(eth_usdt_lastPrice.multiply(eos_eth_lastPrice)));



    }
}
