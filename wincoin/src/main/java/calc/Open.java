package calc;

import calc.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Open {
    static {
        System.setProperty("fileName", "open/info.log");
    }
    public static Logger log = LoggerFactory.getLogger(Open.class);
    public static void main(String[] args) throws InterruptedException {

        //连接本地的 Redis 服务
//        Jedis jedis = new Jedis("127.0.0.1",6379);
//        jedis.auth("123456");
//        jedis.select(0);
//        BigDecimal new_price = BigDecimal.valueOf(Double.valueOf(jedis.get("btc"))).add(BigDecimal.valueOf(5));
//        BigDecimal new_price = BigDecimal.valueOf(36585);
//        System.out.println("最新成交价："+new_price);
        int leverage = 66;

        String open_url = "https://app.3ol-dss9-sif9.cc/v1/cfd/trade/btc/open";
        //市价参数
//        BigDecimal stopLossPrice1 = new_price.add(BigDecimal.valueOf(5));
//        BigDecimal stopProfitPrice1 = new_price.subtract(BigDecimal.valueOf(100));
//        System.out.println("止损价格："+stopLossPrice1);
//        System.out.println("止盈价格："+stopProfitPrice1);



        for (int i = 1; i <= 3; i++) {
            //限价
            String params = "{\"positionType\":\"plan\"," +
                    "\"quantity\":\"1\"," +
                    "\"direction\":\"long\"," +
                    "\"contractType\":1," +
                    "\"stopProfitRate\":0.6"+i*5+"," +
                    "\"stopLossPrice\":\"4300"+i*3+"\"," +
                    "\"leverage\":87," +
                    "\"quantityUnit\":1," +
                    "\"positionModel\":1," +
                    "\"openPrice\":\"4361"+i*3+"\"}";
            System.out.println("\"stopProfitRate\":0.6"+i*5+"---"+"\"stopLossPrice\":\"4400"+i*3+"\"---" +"\"openPrice\":\"4419"+i*3+"---"+System.currentTimeMillis());
            System.out.println(HttpUtil.postByJson(open_url,params));
            Thread.sleep(10000);
        }

//        System.out.println(HttpUtils.postByJson(open_url,params2));
    }
}
